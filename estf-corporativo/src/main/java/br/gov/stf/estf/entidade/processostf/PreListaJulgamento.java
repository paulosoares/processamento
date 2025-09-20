package br.gov.stf.estf.entidade.processostf;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = "JUDICIARIO", name = "LISTA") /* Na issue BD-633 foi pedida a alteração do nome da tabela para ficar condizente com o nome da entidade, porém, o pedido não foi aceito. */
@PrimaryKeyJoinColumn(name = "SEQ_LISTA")
@SequenceGenerator(name = "SEQUENCE", sequenceName = "JUDICIARIO.SEQ_LISTA", allocationSize = 1)
public class PreListaJulgamento extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nome;
	private Setor setor;
	private List<Agrupador> categorias;
	private List<PreListaJulgamentoObjetoIncidente> objetosIncidentes;
	private String cabecalho;
	
	@Id
	@Column(name = "SEQ_LISTA")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_LISTA", allocationSize = 1)	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NOM_LISTA")
	public String getNome() {
		return nome;
	}

	public void setNome(String ementa) {
		this.nome = ementa;
	}

	@OneToMany(fetch=FetchType.LAZY)
	@JoinTable(schema = "JUDICIARIO", name = "LISTA_AGRUPADOR", joinColumns = @JoinColumn(name = "SEQ_LISTA"), inverseJoinColumns = @JoinColumn(name = "SEQ_AGRUPADOR"))
	public List<Agrupador> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Agrupador> categorias) {
		this.categorias = categorias;
	}
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "SEQ_LISTA")
	public List<PreListaJulgamentoObjetoIncidente> getObjetosIncidentes() {
		return getObjetosIncidentes(null);
	}

	public List<PreListaJulgamentoObjetoIncidente> getObjetosIncidentes(Boolean ordenacaoNumerica) {
		
		if (objetosIncidentes != null && ordenacaoNumerica != null)
			if (ordenacaoNumerica)
				Collections.sort(objetosIncidentes, ordenacaoNumericaComparator());
			else
				Collections.sort(objetosIncidentes, ordenacaoAlfaNumericaComparator());
		
		return objetosIncidentes;
	}
	
	public void setObjetosIncidentes(List<PreListaJulgamentoObjetoIncidente> preListaJulgamentoObjetoIncidente) {
		this.objetosIncidentes = preListaJulgamentoObjetoIncidente;
	}

	@ManyToOne(cascade={}, fetch=FetchType.EAGER)
    @JoinColumn(name="COD_SETOR", unique=false, nullable=false, insertable=true, updatable=true)
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	@Column(name = "DOC_CABECALHO")
	public String getCabecalho() {
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}
	
	private Comparator<PreListaJulgamentoObjetoIncidente> ordenacaoNumericaComparator() {	
		return new Comparator<PreListaJulgamentoObjetoIncidente>() {
			@Override
			public int compare(PreListaJulgamentoObjetoIncidente o1, PreListaJulgamentoObjetoIncidente o2) {
				Long desc1 = Long.parseLong(o1.getObjetoIncidente().getIdentificacao().replaceAll("[^0-9]", ""));
				Long desc2 = Long.parseLong(o2.getObjetoIncidente().getIdentificacao().replaceAll("[^0-9]", ""));
				
				int comparacao = desc1.compareTo(desc2);
				
				if (comparacao != 0) 
					return comparacao;
				else
					return o1.getObjetoIncidente().getIdentificacao().compareTo(o2.getObjetoIncidente().getIdentificacao());
			}
		};
	}

	private Comparator<PreListaJulgamentoObjetoIncidente> ordenacaoAlfaNumericaComparator() {
		return new Comparator<PreListaJulgamentoObjetoIncidente>() {
			@Override
			public int compare(PreListaJulgamentoObjetoIncidente o1, PreListaJulgamentoObjetoIncidente o2) {
				return o1.getObjetoIncidente().getIdentificacao().compareTo(o2.getObjetoIncidente().getIdentificacao());
			}
		};
	}
}