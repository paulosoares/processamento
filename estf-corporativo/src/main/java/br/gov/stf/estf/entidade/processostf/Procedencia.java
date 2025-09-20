package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 * Representa as Unidades Federativas e alguns países de onde podem vir processos para o STF.
 * 
 * @author Rodrigo Barreiros
 * @author Demétrius Jubé
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
	 * Identifica a procedência.
	 */
	@Id @Column(name = "COD_PROCEDENCIA")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Descrição da procedência. Pode receber o Nome do País ou da UF.
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
	 * Indica se o registro está ativo ou não.
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
