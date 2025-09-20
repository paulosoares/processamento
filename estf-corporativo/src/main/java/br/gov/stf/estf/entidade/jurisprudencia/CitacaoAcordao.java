/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;

/**
 * @author Paulo.Estevao
 * @since 17.10.2012
 */
@Entity
@Table(schema = "JURISPRUDENCIA", name = "CITACAO_ACORDAO")
public class CitacaoAcordao extends ESTFAuditavelBaseEntity<Long> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4282560560447439947L;
	
	private Long id;
	private AssuntoCitacao assuntoCitacao;
	private ObjetoIncidente<?> objetoIncidente;
	private TribunalJurisprudenciaConstante tribunal;
	private String acordaosNaoEstruturados;
	private Boolean casoLider;
	private String codigoColegiado;
	private ProcessoRtj rtjCitado;
	
	@Override
	@Id
	@Column(name = "SEQ_CITACAO_ACORDAO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JURISPRUDENCIA.SEQ_CITACAO_ACORDAO", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_ASSUNTO_CITACAO")
	public AssuntoCitacao getAssuntoCitacao() {
		return assuntoCitacao;
	}
	
	public void setAssuntoCitacao(AssuntoCitacao assuntoCitacao) {
		this.assuntoCitacao = assuntoCitacao;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}
	
	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
	
	@Column(name = "SEQ_ORGAO")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.jurisprudencia.TribunalJurisprudenciaConstante"),
			@Parameter(name = "idClass", value = "java.lang.Long"),
			@Parameter(name = "nullValue", value = "OUT")})
	public TribunalJurisprudenciaConstante getTribunal() {
		return tribunal;
	}
	
	public void setTribunal(TribunalJurisprudenciaConstante tribunal) {
		this.tribunal = tribunal;
	}
	
	@Column(name = "TXT_ACORDAO_CITADO")
	public String getAcordaosNaoEstruturados() {
		return acordaosNaoEstruturados;
	}
	
	public void setAcordaosNaoEstruturados(String acordaosNaoEstruturados) {
		this.acordaosNaoEstruturados = acordaosNaoEstruturados;
	}
	
	@Column(name = "FLG_CASO_LIDER")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getCasoLider() {
		return casoLider;
	}
	
	public void setCasoLider(Boolean casoLider) {
		this.casoLider = casoLider;
	}
	
	@Formula(value = "(SELECT DISTINCT CASE "
		+ "WHEN PP.COD_MATERIA = 1 THEN 'TP' "
		+ "WHEN PP.COD_MATERIA = 2 THEN '1T' "
		+ "WHEN PP.COD_MATERIA = 3 THEN '2T' "
		+ "WHEN PP.COD_MATERIA = 4 THEN '3T' "
		+ "WHEN PP.COD_MATERIA = 15 THEN 'TP' "
		+ "WHEN PP.COD_MATERIA = 16 THEN '1T' "
		+ "WHEN PP.COD_MATERIA = 17 THEN '2T' "
		+ "WHEN PP.COD_MATERIA = 18 THEN '3T' "
		+ "ELSE NULL "
		+ "END "
		+ "FROM STF.PROCESSO_PUBLICADOS PP, STF.MATERIAS M, STF.DATA_PUBLICACOES DP "
		+ "WHERE PP.SEQ_OBJETO_INCIDENTE = SEQ_OBJETO_INCIDENTE "
		+ "AND PP.COD_CAPITULO = M.COD_CAPITULO "
		+ "AND PP.COD_MATERIA = M.COD_MATERIA "
		+ "AND PP.NUM_MATERIA = M.NUM_MATERIA "
		+ "AND PP.ANO_MATERIA = M.ANO_MATERIA "
		+ "AND M.SEQ_DATA_PUBLICACOES = DP.SEQ_DATA_PUBLICACOES "
		+ "AND DP.DAT_PUBLICACAO_DJ IS NOT NULL "
		+ "AND M.COD_CONTEUDO = 50 "
		+ "AND M.COD_CAPITULO = 5 "
		+ "AND ROWNUM = 1 )")
	public String getCodigoColegiado() {
		return codigoColegiado;
	}
	
	public void setCodigoColegiado(String codigoColegiado) {
		this.codigoColegiado = codigoColegiado;
	}
	
	@Transient
	public TipoColegiadoConstante getColegiado() {
		return TipoColegiadoConstante.valueOfSigla(codigoColegiado);
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_PROCESSO_RTJ_UNIF")
	public ProcessoRtj getRtjCitado() {
		return rtjCitado;
	}

	public void setRtjCitado(ProcessoRtj rtjCitado) {
		this.rtjCitado = rtjCitado;
	}

}
