package br.gov.stf.estf.entidade.documento;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@org.hibernate.annotations.Entity(persister = "br.gov.stf.estf.entidade.documento.ArquivoEletronicoPersister")
@Table(name = "ARQUIVO_TMP", schema = "DOC")
public class ArquivoEletronico extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 8485065882330457221L;

	public static final String DESCRICAO_FORMATO_RTF = "RTF";
	public static final String ALGORITMO_PADRAO = "SHA1";

	private byte[] conteudo;
	private String formato;
	private Long tamanhoArquivo;
	private String usuarioBloqueio;
	private Date dataBloqueio;

	void setTamanhoArquivo(Long tamanhoArquivo) {
		this.tamanhoArquivo = tamanhoArquivo;
	}

	@Formula(value = "length(txt_conteudo)")
	public Long getTamanhoArquivo() {
		return tamanhoArquivo;
	}

	@Id
	@Column(name = "SEQ_ARQUIVO_ELETRONICO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "DOC.SEQ_ARQUIVO_ELETRONICO", allocationSize = 1)
	public Long getId() {
		return id;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "TXT_CONTEUDO")
	public byte[] getConteudo() {
		return this.conteudo;
	}

	public void setConteudo(byte[] txtConteudo) {
		this.conteudo = txtConteudo;
	}

	@Column(name = "DSC_FORMATO", length = 20)
	public String getFormato() {
		return this.formato;
	}

	public void setFormato(String dscFormato) {
		this.formato = dscFormato;
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
	
	public byte[] hash() {
		try {
			MessageDigest md = MessageDigest.getInstance(ALGORITMO_PADRAO);
			return md.digest(conteudo);		
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

}
