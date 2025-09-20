/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

import java.util.Date;

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

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.framework.util.DateTimeHelper;

/**
 * @author Paulo.Estevao
 * @since 17.10.2012
 */
@Entity
@Table(schema = "JURISPRUDENCIA", name = "DECISAO_CITADA")
public class DecisaoCitada extends ESTFAuditavelBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7325556484917503249L;
	
	private Long id;
	private AssuntoCitacao assuntoCitacao;
	private Texto texto;
	private Date dataPublicacao;
	private String decisaoNaoEstruturada;
	
	@Override
	@Id
	@Column(name = "SEQ_DECISAO_CITADA")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JURISPRUDENCIA.SEQ_DECISAO_CITADA", allocationSize = 1)
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
	@JoinColumn(name = "SEQ_TEXTO", referencedColumnName = "SEQ_TEXTOS")
	public Texto getTexto() {
		return texto;
	}
	
	public void setTexto(Texto texto) {
		this.texto = texto;
	}
	
	@Formula(value = "(SELECT MAX(DP.DAT_PUBLICACAO_DJ) " 
		+ "FROM STF.DATA_PUBLICACOES DP, STF.MATERIAS M, STF.PROCESSO_PUBLICADOS PP, STF.TEXTOS T "
		+ "WHERE DP.SEQ_DATA_PUBLICACOES = M.SEQ_DATA_PUBLICACOES "
		+ "AND M.COD_CAPITULO = PP.COD_CAPITULO "
		+ "AND M.COD_MATERIA = PP.COD_MATERIA "
		+ "AND M.NUM_MATERIA = PP.NUM_MATERIA "
		+ "AND M.ANO_MATERIA = PP.ANO_MATERIA "
		+ "AND M.COD_CAPITULO = 6 "
		+ "AND M.COD_CONTEUDO = 50 "
		+ "AND PP.SEQ_OBJETO_INCIDENTE = T.SEQ_OBJETO_INCIDENTE "
		+ "AND T.SEQ_TEXTOS = SEQ_TEXTO "
		+ "AND T.SEQ_ARQUIVO_ELETRONICO = PP.SEQ_ARQUIVO_ELETRONICO_TEXTO)")
	public Date getDataPublicacao() {
		return dataPublicacao;
	}
	
	public void setDataPublicacao(Date dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}
	
	@Transient
	public String getDataPublicacaoFormatada() {
		return DateTimeHelper.getDataString(dataPublicacao);
	}

	@Column(name = "TXT_DECISAO_CITADA")
	public String getDecisaoNaoEstruturada() {
		return decisaoNaoEstruturada;
	}

	public void setDecisaoNaoEstruturada(String decisaoNaoEstruturada) {
		this.decisaoNaoEstruturada = decisaoNaoEstruturada;
	}
}
