package br.gov.stf.estf.assinatura.visao.jsf.beans.menu;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.framework.util.Constants;

public class BeanMenu extends AssinadorBaseBean {

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("sistemas-externos");

	private static final long serialVersionUID = 8642327771515587530L;

	private static final Log LOG = LogFactory.getLog(BeanMenu.class);
	
	private static final String RENDERIZAR_CONFIRMACAO_SAIR = "CONFIRMAR_SAIDA";

	private String urlRelatorioTestes;

	public String sair() {
		try {
			limparAtributos();
			setSessionValue(Constants.SECURITY_ENABLED_KEY, null);
			setSessionValue(Constants.USER_KEY, null);

			HttpServletRequest httpRequest = (HttpServletRequest) getFacesContext().getExternalContext().getRequest();
			StringBuffer requestURLStringBuffer = httpRequest.getRequestURL();
			int index = requestURLStringBuffer.indexOf("/", 8);
			String serverURL = requestURLStringBuffer.substring(0, index);
			String contextURL = serverURL + httpRequest.getContextPath();

			getFacesContext().getExternalContext().redirect(contextURL + "/j_spring_security_logout");
		} catch (IOException e) {
			reportarErro("Erro ao realizar logout.", e, LOG);
		}

		return null;
	}

	public String assinarDocumentosGabinete() {
		limparAtributos();
		return "assinarDocumentosGabinete";
	}

	public String principal() {
		limparAtributos();
		return "principal";
	}

	public String visibilidadePeca() {
		limparAtributos();
		return "visibilidadePeca";
	}

	public String administrarTiposPermissoesModelos() {
		limparAtributos();
		return "administrarTiposPermissoesModelos";
	}

	public String manterTipoGrupo() {
		limparAtributos();
		return "manterTipoGrupo";
	}

	public String manterTipoGrupoUsuario() {
		limparAtributos();
		return "manterTipoGrupoUsuario";
	}

	public String registrarAndamento() {
		limparAtributos();
		return "registrarAndamento";
	}
	
	public String registrarAndamentoT() {
		limparAtributos();
		return "registrarAndamentoT";
	}

	public String registrarAndamentoParaVariosProcessos() {
		limparAtributos();
		return "registrarAndamentoParaVariosProcessos";
	}
	
	public String pesquisarGuia() {
		limparAtributos();
		return "pesquisarGuia";
	}

	public String remeterDocumento() {
		limparAtributos();
		return "remeterDocumento";
	}

	public String gerirAutosEmprestados() {
		return "gerirAutosEmprestados";
	}
	
	public String autorizarCargaAutos() {
		return "autorizarCargaAutos";
	}
	
	public String autorizarBaixaProcesso() {
		return "autorizarBaixa";
	}

	public String assinarPeca() {
		limparAtributos();
		return "assinarPeca";
	}

	public String manterDocumentos() {
		limparAtributos();
		return "manterDocumentos";
	}

	public String assinarDocumentos() {
		limparAtributos();
		return "assinarDocumentos";
	}

	public String expedirDocumentos() {
		limparAtributos();
		return "expedirDocumentos";
	}

	public String administrarModelos() {
		limparAtributos();
		return "administrarModelos";
	}

	public String administrarTiposModelos() {
		limparAtributos();
		return "administrarTiposModelos";
	}

	public String consultarDocumentosElaborados() {
		limparAtributos();
		return "consultarDocumentosElaborados";
	}
	
	
	public String consultarDocumentosSigilosos() {
		limparAtributos();
		return "consultarDocumentosSigilosos";
	}
	
	public String consultarDocumentosAssinados() {
		limparAtributos();
		return "consultarDocumentosAssinados";
	}

	public String consultarDocumentosUnidade() {
		limparAtributos();
		return "consultarDocumentosUnidade";
	}

	public String administrarTagsLivres() {
		limparAtributos();
		return "administrarTagsLivres";
	}

	public String administrarTipoTagsLivres() {
		limparAtributos();
		return "administrarTipoTagsLivres";
	}

	public String administrarAutoridades() {
		limparAtributos();
		return "administrarAutoridades";
	}

	public String aguardandoAssinatura() {
		limparAtributos();
		return "aguardandoAssinatura";
	}

	public String aguardandoRevisao() {
		limparAtributos();
		return "aguardandoRevisao";
	}
	
	public String consultarPrescricao() {
		limparAtributos();
		return "consultarPrescricao";
	}
	
	public String receberExterno(){
		limparAtributos();
		return "receberExterno";
	}
	
	public String processoIntegracao(){
		limparAtributos();
		return "processoIntegracao";
	}
	
	public String andamentoProcesso(){
		limparAtributos();
		return "andamentoProcesso";
	}
	
	public String manterDestinatariosBaixaExpedicao(){
		limparAtributos();
		return "manterDestinatariosBaixaExpedicao";
	}	
	
	public String manterPermissaoDeslocamento(){
		limparAtributos();
		return "manterPermissaoDeslocamento";
	}
	
	public String copiarPecas(){
		limparAtributos();
		return "copiarPecas";
	}
	
	public String exclusaoMinistroDaDistribuicao(){
		limparAtributos();
		return "exclusaoMinistroDaDistribuicao";
	}
	
	public String processosPublicadosNaoDeslocados(){
		limparAtributos();
		return "processoPublicadoNaoDeslocado";
	}
	
	public String manterPessoaCarga(){
		limparAtributos();
		return "manterPessoaCarga";
	}
	
	public String processoInteresse(){
		limparAtributos();
		return "processoInteresse";
	}

	public String contratoPostagemVisualizacao() {
		return "contratoPostagemVisualizacao";
	}

	public String destinatarioPesquisa() {
		limparAtributos();
		return "destinatarioPesquisa";
	}

	public String destinatarioPesquisaAvancada() {
		limparAtributos();
		return "destinatarioPesquisaAvancada";
	}

	public String destinatarioCadastro() {
		limparAtributos();
		return "destinatarioCadastro";
	}

	public String remessaPesquisa() {
		limparAtributos();
		return "remessaPesquisa";
	}

	public String remessaCadastro() {
		limparAtributos();
		return "remessaCadastro";
	}

	public String remessaFinalizacaoPesquisa() {
		limparAtributos();
		return "remessaFinalizacaoPesquisa";
	}

	public String remessaFinalizacao() {
		limparAtributos();
		return "remessaFinalizacao";
	}
	
    public String gerarIntimacoes() {
        limparAtributos();
        return "gerarIntimacoes";
    }

	public String gerarIntimacaoFisica() {
		limparAtributos();
		return "gerarIntimacaoFisica";
	}
	
    public String intimacaoPesquisa() {
        limparAtributos();
        return "comunicacaoExterna";
    }

    public String gerarIntimacaoEletronica() {
        limparAtributos();
        return "gerarIntimacaoEletronica";
    }
    
    public String finalizarRestritos() {
        limparAtributos();
        return "finalizarRestritos";
    }
    
	public Boolean getRenderizarConfirmacaoSair() {
		Boolean resultado = (Boolean)getAtributo(RENDERIZAR_CONFIRMACAO_SAIR);
		if (resultado == null){
			return false;
		}
		return resultado;
	}

	public void setRenderizarConfirmacaoSair(Boolean renderizarConfirmacaoSair) {
		setAtributo(RENDERIZAR_CONFIRMACAO_SAIR,renderizarConfirmacaoSair);
	}
	
	public void cancelarSaida(ActionEvent ev){
		setRenderizarConfirmacaoSair(false);
		
	}
	
	public String verificarSaida(){
		setRenderizarConfirmacaoSair(true);
		return null;
	}
	
	public static ResourceBundle getResourceBundle() {
		return RESOURCE_BUNDLE;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Log getLog() {
		return LOG;
	}

	public String getUrlRelatorioTestes(){
		return RESOURCE_BUNDLE.getString("query.string.relatorioTestes").toString() + getAppVersion();
	}


	public void setUrlRelatorioTestes(String urlRelatorioTestes) {
		this.urlRelatorioTestes = urlRelatorioTestes;
	}
	
	
	
}
