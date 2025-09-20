package br.gov.stf.estf.entidade.documento;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFoundAction;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.usuario.Usuario;

@Entity
@Table(name = "VW_ARQUIVO_ELETRONICO", schema = "DOC")
public class ArquivoEletronicoView extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String formato;
	private String usuarioBloqueio;
	private Date dataBloqueio;
	private Usuario usuarioInclusao;
	private Date dataInclusao;
	private Usuario usuarioAlteracaoArquivo;
	private Date dataAlteracaoArquivo;

	@Id
	@Column(name = "SEQ_ARQUIVO_ELETRONICO")
	public Long getId() {
		return id;
	}

	@Column(name = "DAT_BLOQUEIO", insertable = false, updatable = false)
	public Date getDataBloqueio() {
		return dataBloqueio;
	}

	public void setDataBloqueio(Date dataBloqueio) {
		this.dataBloqueio = dataBloqueio;
	}

	@Column(name = "SIG_USUARIO_BLOQUEIO", insertable = false, updatable = false)
	public String getUsuarioBloqueio() {
		return usuarioBloqueio;
	}

	public void setUsuarioBloqueio(String usuarioBloqueio) {
		this.usuarioBloqueio = usuarioBloqueio;
	}

	@Column(name = "DSC_FORMATO", length = 20)
	public String getFormato() {
		return this.formato;
	}

	public void setFormato(String dscFormato) {
		this.formato = dscFormato;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USU_INCLUSAO", updatable = false)
	@org.hibernate.annotations.NotFound(action = NotFoundAction.IGNORE)
	public Usuario getUsuarioInclusao() {
		return usuarioInclusao;
	}

	public void setUsuarioInclusao(Usuario usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_INCLUSAO", updatable = false)
	public Date getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SIG_USUARIO_ALTERA_DOC", updatable = false)
	@org.hibernate.annotations.NotFound(action = NotFoundAction.IGNORE)
	public Usuario getUsuarioAlteracaoArquivo() {
		return usuarioAlteracaoArquivo;
	}

	public void setUsuarioAlteracaoArquivo(Usuario usuarioAlteracaoArquivo) {
		this.usuarioAlteracaoArquivo = usuarioAlteracaoArquivo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_ALTERACAO_DOC", updatable = false)
	public Date getDataAlteracaoArquivo() {
		return dataAlteracaoArquivo;
	}

	public void setDataAlteracaoArquivo(Date dataAlteracaoArquivo) {
		this.dataAlteracaoArquivo = dataAlteracaoArquivo;
	}

}
