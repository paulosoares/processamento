/**
 * 
 */
package br.gov.stf.estf.entidade.julgamento;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoMotivoAlteracao;
import br.gov.stf.estf.entidade.usuario.Usuario;

@Entity
@Table(name="PROCESSO_LISTA_JULG", schema="JULGAMENTO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProcessoListaJulgamento extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 335852959288120329L;
	
	private Long id;
	private ListaJulgamento listaJulgamento;
	private ObjetoIncidente<?> objetoIncidente;
	private Andamento andamento;
	private PreListaJulgamentoMotivoAlteracao motivo;
	private Usuario usuarioRevisor;
	private Date dataRevisao;
	private String observacao;
	private Integer ordemNaLista;
	private JulgamentoProcesso julgamentoProcesso;
	private Texto texto;
	private Set<ManifestacaoRepresentante> manifestacoes;
	
	@Override
	@Id
	@Column(name="SEQ_PROCESSO_LISTA_JULG")
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JULGAMENTO.SEQ_PROCESSO_LISTA_JULG", allocationSize=1 )	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_LISTA_JULGAMENTO")
	public ListaJulgamento getListaJulgamento() {
		return listaJulgamento;
	}

	public void setListaJulgamento(ListaJulgamento listaJulgamento) {
		this.listaJulgamento = listaJulgamento;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_OBJETO_INCIDENTE_CANDIDATO")
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_ANDAMENTO")
	public Andamento getAndamento() {
		return andamento;
	}

	public void setAndamento(Andamento andamento) {
		this.andamento = andamento;
	}
	
	@Column(name = "COD_MOTIVO_ALTERACAO_LISTA", updatable = false)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.PreListaJulgamentoMotivoAlteracao"),
			@Parameter(name = "idClass", value = "java.lang.Long") })
	public PreListaJulgamentoMotivoAlteracao getMotivo() {
		return motivo;
	}

	public void setMotivo(PreListaJulgamentoMotivoAlteracao motivo) {
		this.motivo = motivo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SIG_USUARIO_REVISAO")
	public Usuario getUsuarioRevisor() {
		return usuarioRevisor;
	}

	public void setUsuarioRevisor(Usuario usuarioRevisor) {
		this.usuarioRevisor = usuarioRevisor;
	}

	@Column(name="DAT_REVISAO")
	public Date getDataRevisao() {
		return dataRevisao;
	}

	public void setDataRevisao(Date dataRevisao) {
		this.dataRevisao = dataRevisao;
	}
	
	@Column(name = "TXT_OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Column(name = "NUM_ITEM_LISTA")
	@OrderBy("ordemNaLista ASC")
	public Integer getOrdemNaLista() {
		return ordemNaLista;
	}

	public void setOrdemNaLista(Integer ordemNaLista) {
		this.ordemNaLista = ordemNaLista;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_JULGAMENTO_PROCESSO")
	public JulgamentoProcesso getJulgamentoProcesso() {
		return julgamentoProcesso;
	}

	
	public void setJulgamentoProcesso(JulgamentoProcesso julgamentoProcesso) {
		this.julgamentoProcesso = julgamentoProcesso;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TEXTOS")
	public Texto getTexto() {
		return texto;
	}

	public void setTexto(Texto texto) {
		this.texto = texto;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="SEQ_OBJETO_INCIDENTE", referencedColumnName = "SEQ_OBJETO_INCIDENTE_CANDIDATO", insertable=false, updatable=false),
		@JoinColumn(name="SEQ_LISTA_JULGAMENTO", referencedColumnName = "SEQ_LISTA_JULGAMENTO", insertable=false, updatable=false)
	})
	public Set<ManifestacaoRepresentante> getManifestacoes() {
		return manifestacoes;
	}
	
	public void setManifestacoes(Set<ManifestacaoRepresentante> manifestacoes) {
		this.manifestacoes = manifestacoes;
	}
	
	@Transient
	public List<ManifestacaoRepresentante> getSustentacoesOrais() {
		List<ManifestacaoRepresentante> sustentacoes = new ArrayList<ManifestacaoRepresentante>();
		
		if (manifestacoes != null && !manifestacoes.isEmpty())
			for(ManifestacaoRepresentante m : manifestacoes)
				if (TipoManifestacao.SUSTENTACAO_ORAL.equals(m.getTipoManifestacao()))
					sustentacoes.add(m);
		
		return sustentacoes;
	}
	
	@Transient
	public List<ManifestacaoRepresentante> getQuestoesFato() {
		List<ManifestacaoRepresentante> questoesFato = new ArrayList<ManifestacaoRepresentante>();
		
		if (manifestacoes != null && !manifestacoes.isEmpty())
			for(ManifestacaoRepresentante m : manifestacoes)
				if (TipoManifestacao.QUESTAO_FATO.equals(m.getTipoManifestacao()))
					questoesFato.add(m);
		
		return questoesFato;
	}
	
	@Transient
	public List<ManifestacaoRepresentante> getSustentacoesOraisEQuestoesFato() {
		List<ManifestacaoRepresentante> sustentacoes = new ArrayList<ManifestacaoRepresentante>();
		
		if (manifestacoes != null && !manifestacoes.isEmpty())
			for(ManifestacaoRepresentante m : manifestacoes)
				if (Arrays.asList(TipoManifestacao.SUSTENTACAO_ORAL, TipoManifestacao.QUESTAO_FATO).contains(m.getTipoManifestacao()))
					sustentacoes.add(m);
		
		return sustentacoes;
	}

	@Transient
	public boolean isSustentacoesOraisLidasPeloMinistro(Ministro ministro) {
		for (ManifestacaoRepresentante so : getSustentacoesOrais())
			if (!so.isSustentacaoOralLidaPeloMinistro(ministro))
				return false;
		
		return true;
	}
}