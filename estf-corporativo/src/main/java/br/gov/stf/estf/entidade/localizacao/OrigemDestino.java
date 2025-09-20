package br.gov.stf.estf.entidade.localizacao;




import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 * Mantém informações sobre advogados, setores e origens.
 * 
 * @author Ricardo Leão
 * 
 * @since 28.11.2012
 */
@Entity
//@Immutable
@Table(name = "VW_ORIGEM_DESTINO", schema = "STF")
public class OrigemDestino extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String descricao;
	private Short tipoOrigemDestino;
	private Boolean ativo;
	private Boolean deslocaProcesso;
	private String descricaoTipoOrigemDestino;
	
	/**
	 * Identifica a origem/destino do deslocamento.
	 */
	@Id
	@Column(name = "COD_ORIGEM_DESTINO", updatable = false, insertable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Descrição da origem/destino do deslocamento: nome de advogado, de setor ou de órgão externo.
	 */
	@Column(name = "DSC_ORIGEM_DESTINO", updatable = false, insertable = false)
	public String getDescricao() {
		if (!getTipoOrigemDestino().equals((short)1)) {
			return descricao + " (" + getDescricaoTipoOrigemDestino() + ")";
		} else {
			return descricao;
		}
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	/**
	 * Tipo da origem/destino do deslocamento: 1- Advogado, 2- Interno, 3- Externo.
	 */
	@Column(name = "TIP_ORIGEM_DESTINO", updatable = false, insertable = false)
	public Short getTipoOrigemDestino() {
		return tipoOrigemDestino;
	}

	public void setTipoOrigemDestino(Short tipoOrigemDestino) {
		this.tipoOrigemDestino = tipoOrigemDestino;
	}
	
	/**
	 * Indica se o registro está ativo ou não.
	 */
	@Column(name = "FLG_ATIVO", updatable = false, insertable = false)
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}

	// está com escopo privado para evitar problema na tentativa do hibernante persistir a view
	private void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	/**
	 * Indica se o registro está apto para o deslocamento ou não.
	 */
	@Column(name = "FLG_DESLOC_PROC", updatable = false, insertable = false)
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getDeslocaProcesso() {
		return deslocaProcesso;
	}

	// está com escopo privado para evitar problema na tentativa do hibernante persistir a view
	private void setDeslocaProcesso(Boolean deslocaProcesso) {
		this.deslocaProcesso = deslocaProcesso;
	}
	
	@Transient
	public String getDescricaoTipoOrigemDestino(){
		if (getTipoOrigemDestino() == 1) {
			this.descricaoTipoOrigemDestino = "";
		} else if (getTipoOrigemDestino() == 2) {
			this.descricaoTipoOrigemDestino = "Setor STF";
		} else if (getTipoOrigemDestino() == 3) {
			this.descricaoTipoOrigemDestino = "Órgão externo";
		}
		return this.descricaoTipoOrigemDestino;
	}
}
