package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 * Representa as Unidades Federativas e alguns pa�ses de onde podem vir processos para o STF.
 * 
 * @author Rodrigo Barreiros
 * @author Dem�trius Jub�
 * 
 * @since 15.07.2009
 */
@Entity
@Table(name = "PROCEDENCIA", schema = "JUDICIARIO")
public class Procedencia extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 5059345029190722908L;

	private String descricao;

	private Boolean ativo;
	
	private String siglaProcedencia;

	/**
	 * Identifica a proced�ncia.
	 */
	@Id @Column(name = "COD_PROCEDENCIA")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Descri��o da proced�ncia. Pode receber o Nome do Pa�s ou da UF.
	 */
	@Column(name = "DSC_PROCEDENCIA")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "SIG_PROCEDENCIA")
	public String getSiglaProcedencia() {
		return siglaProcedencia;
	}

	public void setSiglaProcedencia(String siglaProcedencia) {
		this.siglaProcedencia = siglaProcedencia;
	}

	/**
	 * Indica se o registro est� ativo ou n�o.
	 */
	@Column(name = "FLG_ATIVO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

}
