package br.gov.stf.estf.expedicao.entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 *
 * @author Roberio.Fernandes
 */
@Entity
@Table(schema = "EXPEDICAO", name = "TIPO_COMUNICACAO")
public class TipoComunicacaoExpedicao extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = -5871645711681029675L;

	private String descricao;
	private Boolean exigeNumeracao;

	@Id
	@Column(name = "SEQ_TIPO_COMUNICACAO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "EXPEDICAO.SEQ_TIPO_COMUNICACAO",  allocationSize = 0)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "DSC_TIPO_COMUNICACAO", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "FLG_EXIGE_NUMERACAO", unique = false, nullable = false, insertable = true, updatable = true, length = 1)
    @Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean isExigeNumeracao() {
		return exigeNumeracao;
	}

	public void setExigeNumeracao(Boolean exigeNumeracao) {
		this.exigeNumeracao = exigeNumeracao;
	}
}