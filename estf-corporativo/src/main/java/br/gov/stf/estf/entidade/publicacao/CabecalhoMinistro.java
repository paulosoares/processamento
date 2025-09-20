package br.gov.stf.estf.entidade.publicacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.ministro.Ocorrencia;

/**
 * Em um cabeçalho podem aparecer diversos ministros (relator, revisor e etc). Ao mesmo tempo,
 * um mesmo ministro pode aparecer em diversos cabeçalhos (diversos processos).
 * 
 * <p>Essa classe representa a associação entre cabeçalho e ministro, necessária
 * para mapear o relacionamento NxN entre essa entidades.
 * 
 * @author Rodrigo Barreiros
 * @since 26.05.2009
 */
@Entity
@Table(name="CABECALHO_MINISTRO", schema="DOC")
public class CabecalhoMinistro extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = -1470686664523285570L;
	
	private String nomeMinistro;
	private Integer ordem;
	private Ministro ministro;
	private Ocorrencia ocorrencia;
	private CabecalhoTexto cabecalhoTexto;

	@Id
    @Column(name="SEQ_CABECALHO_MINISTRO", insertable = false, updatable = false) 
    @GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "DOC.SEQ_CABECALHO_MINISTRO", allocationSize=1)
	@Override
	public Long getId() {
		return id;
	}

	@Column(name="NOM_MINISTRO")
	public String getNomeMinistro() {
		return nomeMinistro;
	}

	public void setNomeMinistro(String nomeMinistro) {
		this.nomeMinistro = nomeMinistro;
	}

	@Column(name="NUM_ORDEM")
	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_MINISTRO")
	public Ministro getMinistro() {
		return ministro;
	}

	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}


	@Column(name = "COD_OCORRENCIA", insertable = false, updatable = false)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.ministro.Ocorrencia"),
			@Parameter(name = "idClass", value = "java.lang.String") })
	public Ocorrencia getOcorrencia() {
		return ocorrencia;
	}

	public void setOcorrencia(Ocorrencia ocorrencia) {
		this.ocorrencia = ocorrencia;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_CABECALHO_TEXTO")
	public CabecalhoTexto getCabecalhoTexto() {
		return cabecalhoTexto;
	}

	public void setCabecalhoTexto(CabecalhoTexto cabecalhoTexto) {
		this.cabecalhoTexto = cabecalhoTexto;
	}

}
