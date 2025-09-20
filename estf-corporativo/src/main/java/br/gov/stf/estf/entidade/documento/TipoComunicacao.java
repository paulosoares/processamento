package br.gov.stf.estf.entidade.documento;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name = "TIPO_COMUNICACAO", schema = "JUDICIARIO")
public class TipoComunicacao extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = -5871645711681029675L;

	public static final TipoComunicacao NOTIFICACAO = new TipoComunicacao(123L, "Notificação");

	private Long id;
	private String descricao;
	private Long numeroComunicacaoAnterior;
	private Date dataAlteracao;
	private Boolean protegido;

	public TipoComunicacao() {
		super();
	}
	
	public TipoComunicacao(Long id, String descricao) {
		super();
		this.id = id;
		this.descricao = descricao;
	}

	@Id
	@Column(name = "SEQ_TIPO_COMUNICACAO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_TIPO_COMUNICACAO",  allocationSize=1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "DSC_TIPO_COMUNICACAO", unique = false, nullable = false, insertable = true, updatable = true, length = 1000)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "NUM_COMUNICACAO_ANTERIOR", unique = false, nullable = false, insertable = true, updatable = true, length = 10)
	public Long getNumeroComunicacaoAnterior() {
		return numeroComunicacaoAnterior;
	}

	public void setNumeroComunicacaoAnterior(Long numeroComunicacaoAnterior) {
		this.numeroComunicacaoAnterior = numeroComunicacaoAnterior;
	}

	@Temporal( TemporalType.TIMESTAMP )
	@Column( name="DAT_ALTERACAO", insertable=true, updatable=true, unique=false, nullable=true, length=7 )		
	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}


	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	@Column( name="FLG_PROTEGIDO")
	public Boolean getProtegido() {
		return protegido;
	}

	public void setProtegido(Boolean protegido) {
		this.protegido = protegido;
	}


}
