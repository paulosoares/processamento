package br.gov.stf.estf.entidade.jurisprudencia;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(schema = "SJUR", name = "LOG_SJUR")
public class Auditoria extends  ESTFBaseEntity<Long>{

	private static final long serialVersionUID = -2404469216280417792L;

	private Long id;
	private String usuario;
	private String tipoOperacao;
	private Date dataOperacao;
	private String referenciaProcesso;
	private String tipoDocumento;
	private String textoRegistro;

	@Override
	@Id
	@Column(name = "SEQ_LOG_SJUR")
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DOC_REGISTRO")
	public String getTextoRegistro() {
		return textoRegistro;
	}

	public void setTextoRegistro(String textoRegistro) {
		this.textoRegistro = textoRegistro;
	}

	@Column(name = "SIG_USUARIO")
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	@Column(name = "TIP_OPERACAO")
	public String getTipoOperacao() {
		return tipoOperacao;
	}

	public void setTipoOperacao(String tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

	@Column(name = "DAT_OPERACAO")
	@Temporal( TemporalType.TIMESTAMP )
	public Date getDataOperacao() {
		return dataOperacao;
	}

	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}

	@Column(name = "DSC_REGISTRO")
	public String getReferenciaProcesso() {
		return referenciaProcesso;
	}

	public void setReferenciaProcesso(String referenciaProcesso) {
		this.referenciaProcesso = referenciaProcesso;
	}

	@Column(name = "TIP_DOCUMENTO")
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

}
