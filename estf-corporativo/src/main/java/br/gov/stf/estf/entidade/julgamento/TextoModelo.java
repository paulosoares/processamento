package br.gov.stf.estf.entidade.julgamento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 * Entidade responsavel pela guarda de modelos de textos para a criacao automatica
 * de decisoes no julgamento virtual por listas
 * @author edvaldoo
 *
 */
@Entity
@Table(name = "TEXTO_MODELO", schema = "JULGAMENTO")
public class TextoModelo extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = -1464477652963258648L;

	private String textoModelo;
	private String descricaoModelo;
	private Integer codigoAndamento;
	private String descricaoAndamentoResumido;
	private Boolean ativo;

	public TextoModelo() {
		super();
	}

	@Id
	@Column(name = "SEQ_TEXTO_MODELO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JULGAMENTO.SEQ_TEXTO_MODELO", allocationSize = 1)
	public Long getId() {
		return id;
	}

	@Column(name = "TXT_MODELO", unique = true, nullable = false, insertable = true, updatable = true)
	public String getTextoModelo() {
		return textoModelo;
	}

	public void setTextoModelo(String textoModelo) {
		this.textoModelo = textoModelo;
	}

	@Column(name = "DSC_MODELO", unique = true, nullable = false, insertable = true, updatable = true)
	public String getDescricaoModelo() {
		return descricaoModelo;
	}

	public void setDescricaoModelo(String descricaoModelo) {
		this.descricaoModelo = descricaoModelo;
	}

	@Column(name = "COD_ANDAMENTO", unique = true, nullable = false, insertable = true, updatable = true)
	public Integer getCodigoAndamento() {
		return codigoAndamento;
	}

	public void setCodigoAndamento(Integer codigoAndamento) {
		this.codigoAndamento = codigoAndamento;
	}

	@Column(name = "DSC_ANDAMENTO_RESUMIDO", unique = true, nullable = false, insertable = true, updatable = true)
	public String getDescricaoAndamentoResumido() {
		return descricaoAndamentoResumido;
	}

	public void setDescricaoAndamentoResumido(String descricaoAndamentoResumido) {
		this.descricaoAndamentoResumido = descricaoAndamentoResumido;
	}

	@Column(name = "FLG_ATIVO", nullable = true, insertable = true, updatable = true, unique = false)
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((ativo == null) ? 0 : ativo.hashCode());
		result = prime * result + codigoAndamento;
		result = prime * result + ((descricaoAndamentoResumido == null) ? 0 : descricaoAndamentoResumido.hashCode());
		result = prime * result + ((descricaoModelo == null) ? 0 : descricaoModelo.hashCode());
		result = prime * result + ((textoModelo == null) ? 0 : textoModelo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TextoModelo other = (TextoModelo) obj;
		if (ativo == null) {
			if (other.ativo != null)
				return false;
		} else if (!ativo.equals(other.ativo))
			return false;
		if (codigoAndamento != other.codigoAndamento)
			return false;
		if (descricaoAndamentoResumido == null) {
			if (other.descricaoAndamentoResumido != null)
				return false;
		} else if (!descricaoAndamentoResumido.equals(other.descricaoAndamentoResumido))
			return false;
		if (descricaoModelo == null) {
			if (other.descricaoModelo != null)
				return false;
		} else if (!descricaoModelo.equals(other.descricaoModelo))
			return false;
		if (textoModelo == null) {
			if (other.textoModelo != null)
				return false;
		} else if (!textoModelo.equals(other.textoModelo))
			return false;
		return true;
	}

}
