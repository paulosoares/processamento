package br.gov.stf.estf.assinatura.stfoffice.editor;

import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.stfoffice.editor.web.requisicao.jnlp.RequisicaoJnlpDocumento;
import br.jus.stf.estf.montadortexto.SpecDadosDocumentoSecretaria;

public class RequisicaoAbrirDocumento extends RequisicaoJnlpDocumento {


	private static final long serialVersionUID = 8823510967544150523L;

	private ArquivoEletronico arquivoEletronico;
	private Comunicacao comunicacao;
	private SpecDadosDocumentoSecretaria dados;
	private String user;
	
	public ArquivoEletronico getArquivoEletronico() {
		return arquivoEletronico;
	}
	
	public void setArquivoEletronico(ArquivoEletronico arquivoEletronico) {
		this.arquivoEletronico = arquivoEletronico;
	}
	
	public Comunicacao getComunicacao() {
		return comunicacao;
	}
	
	public void setComunicacao(Comunicacao comunicacao) {
		this.comunicacao = comunicacao;
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public SpecDadosDocumentoSecretaria getDados() {
		return dados;
	}

	public void setDados(SpecDadosDocumentoSecretaria dados) {
		this.dados = dados;
	}

	public String toString() {
		return this.getClass().getName()+" -id: " + arquivoEletronico.toString();
	}

}