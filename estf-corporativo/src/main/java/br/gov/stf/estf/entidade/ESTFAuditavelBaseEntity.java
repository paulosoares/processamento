package br.gov.stf.estf.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import br.gov.stf.estf.entidade.usuario.Usuario;


@MappedSuperclass
public abstract class ESTFAuditavelBaseEntity<ID extends Serializable>
	extends ESTFBaseEntity<ID> {
	
	private static final long serialVersionUID = 1L;
	
	private Date dataInclusao;
	private Usuario usuarioInclusao;
	private Date dataAlteracao;
	private Usuario usuarioAlteracao;
	
	@Column(name = "DAT_INCLUSAO", length = 7, insertable = false, updatable = false)
	public Date getDataInclusao() {
		return this.dataInclusao;
	}

	public void setDataInclusao(Date datInclusao) {
		this.dataInclusao = datInclusao;
	}

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USU_INCLUSAO", insertable = false, updatable = false)
	public Usuario getUsuarioInclusao() {
		return this.usuarioInclusao;
	}

	public void setUsuarioInclusao(Usuario usuInclusao) {
		this.usuarioInclusao = usuInclusao;
	}

	@Column(name = "DAT_ALTERACAO", length = 7, insertable = false, updatable = false)
	public Date getDataAlteracao() {
		return this.dataAlteracao;
	}

	public void setDataAlteracao(Date datAlteracao) {
		this.dataAlteracao = datAlteracao;
	}

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USU_ALTERACAO", insertable = false, updatable = false)
	public Usuario getUsuarioAlteracao() {
		return this.usuarioAlteracao;
	}

	public void setUsuarioAlteracao(Usuario usuAlteracao) {
		this.usuarioAlteracao = usuAlteracao;
	}
}
