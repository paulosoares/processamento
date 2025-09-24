/**
 * 
 */
package br.jus.stf.estf.decisao.comunicacao.support;

import java.io.Serializable;

import org.springframework.context.ApplicationContext;

import br.gov.stf.estf.entidade.documento.FlagTipoAssinatura;
import br.jus.stf.estf.decisao.documento.support.DocumentoWrapper;
import br.jus.stf.estf.decisao.pesquisa.domain.ComunicacaoDto;

/**
 * @author Paulo.Estevao
 * @since 13.05.2011
 */
public class ComunicacaoWrapper implements Serializable, DocumentoWrapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7995652512231262597L;

	private ApplicationContext applicationContext;
	private ComunicacaoDto comunicacao;
	private String idUsuario;
	private FlagTipoAssinatura tipoAssinatura = FlagTipoAssinatura.PADRAO;
	private String hashValidacao;
	
	public ComunicacaoWrapper(ApplicationContext applicationContext,
			ComunicacaoDto comunicacao, String idUsuario, FlagTipoAssinatura tipoAssinatura, String hashValidacao) {
		this.applicationContext = applicationContext;
		this.comunicacao = comunicacao;
		this.idUsuario = idUsuario;
		this.tipoAssinatura = tipoAssinatura;
		this.hashValidacao = hashValidacao;
	}
	
	public ComunicacaoWrapper(ApplicationContext applicationContext,
			ComunicacaoDto comunicacao, String idUsuario, String hashValidacao) {
		this(applicationContext, comunicacao, idUsuario, FlagTipoAssinatura.PADRAO, hashValidacao);
	}
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	public ComunicacaoDto getComunicacao() {
		return comunicacao;
	}
	
	public void setComunicacao(ComunicacaoDto comunicacao) {
		this.comunicacao = comunicacao;
	}
	
	public String getIdUsuario() {
		return idUsuario;
	}
	
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public String getNome() {
		return getComunicacao().getId().toString();
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
