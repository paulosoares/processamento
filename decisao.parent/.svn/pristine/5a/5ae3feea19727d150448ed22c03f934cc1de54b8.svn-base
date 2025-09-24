package br.jus.stf.estf.decisao.texto.support;

import java.io.Serializable;
import java.text.MessageFormat;

import org.springframework.context.ApplicationContext;

import br.gov.stf.estf.entidade.documento.FlagTipoAssinatura;
import br.gov.stf.estf.entidade.documento.TipoDocumentoTexto;
import br.jus.stf.estf.decisao.documento.support.DocumentoWrapper;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;

public class TextoWrapper implements Serializable, DocumentoWrapper {

	private static final long serialVersionUID = -3810094284195509120L;
	private ApplicationContext applicationContext;
	private TipoDocumentoTexto tipoDocumentoTexto;
	private TextoDto texto;
	private Boolean inserirTimbre = false;
	private Long sequencialDocumentoEletronico;
	private String hashValidacao;
	private String idUsuario;
	private String observacao;
	private FlagTipoAssinatura tipoAssinatura = FlagTipoAssinatura.PADRAO;

	public TextoWrapper(ApplicationContext applicationContext, TipoDocumentoTexto tipoDocumentoTexto, TextoDto texto,
			Boolean inserirTimbre, Long sequencialDocumentoEletronico, String hashValidacao, String idUsuario, String observacao, FlagTipoAssinatura tipoAssinatura) {
		this.applicationContext = applicationContext;
		this.tipoDocumentoTexto = tipoDocumentoTexto;
		this.texto = texto;
		this.inserirTimbre = inserirTimbre;
		this.sequencialDocumentoEletronico = sequencialDocumentoEletronico;
		this.hashValidacao = hashValidacao;
		this.idUsuario = idUsuario;
		this.observacao = observacao;
		this.tipoAssinatura = tipoAssinatura;
	}
	
	public TextoWrapper(ApplicationContext applicationContext, TipoDocumentoTexto tipoDocumentoTexto, TextoDto texto,
			Boolean inserirTimbre, Long sequencialDocumentoEletronico, String hashValidacao, String idUsuario, String observacao) {
		this(applicationContext, tipoDocumentoTexto, texto, inserirTimbre, sequencialDocumentoEletronico, hashValidacao, idUsuario, observacao, FlagTipoAssinatura.PADRAO);
	}
	
	public String getNome() {
		return MessageFormat.format("{0}({1})", texto.toString(), texto.getId().toString());
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public TipoDocumentoTexto getTipoDocumentoTexto() {
		return tipoDocumentoTexto;
	}

	public void setTipoDocumentoTexto(TipoDocumentoTexto tipoDocumentoTexto) {
		this.tipoDocumentoTexto = tipoDocumentoTexto;
	}

	public TextoDto getTexto() {
		return texto;
	}

	public void setTexto(TextoDto texto) {
		this.texto = texto;
	}

	public Boolean getInserirTimbre() {
		return inserirTimbre;
	}

	public void setInserirTimbre(Boolean inserirTimbre) {
		this.inserirTimbre = inserirTimbre;
	}

	public Long getSequencialDocumentoEletronico() {
		return sequencialDocumentoEletronico;
	}

	public void setSequencialDocumentoEletronico(Long sequencialDocumentoEletronico) {
		this.sequencialDocumentoEletronico = sequencialDocumentoEletronico;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public FlagTipoAssinatura getTipoAssinatura() {
		return tipoAssinatura;
	}

	public void setTipoAssinatura(FlagTipoAssinatura tipoAssinatura) {
		this.tipoAssinatura = tipoAssinatura;
	}

	public String getHashValidacao() {
		return hashValidacao;
	}

	public void setHashValidacao(String hashValidacao) {
		this.hashValidacao = hashValidacao;
	}
	
}
