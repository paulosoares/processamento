<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<script type="text/javascript">
	function validarCampos() {
		return true;
	}
</script>

<f:subview id="viewMessages1">
	<a4j:outputPanel id="outputPanelMessages1"
						ajaxRendered="true"
						keepTransient="false">
		<t:panelGrid id="pnlMessages1"
						forceId="true"
						rendered="#{not empty facesContext.maximumSeverity}"
						cellpadding="0"
						cellspacing="0"
						columns="1"
						style="width: 100%; text-align: center;">
			<t:messages errorClass="ErrorMessage"
						style="text-align: left"
						infoClass="InfoMessage"
						warnClass="WarningMessage"
						showSummary="true"
						showDetail="true"
						layout="table" />
		</t:panelGrid>
	</a4j:outputPanel>
</f:subview>

<h:panelGrid columns="1" id="painelCadastroContratoDestinatario">
	<rich:panel bodyClass="inpanelBody">
		<f:facet name="header">
			<b>Novo Contrato:</b>
		</f:facet>
	        <div>
	            <table style="width: 500px;">
	                <tr>
	                    <td><h:outputText value="Número do Contrato *"/></td>
	                    <td>
	                        <t:inputText id="idNumeroContrato"
	                                     size="40"
	                                     value="#{beanContratoPostagem.contratoPostagemNovo.numero}" />
	                    </td>
	                </tr>
	                <tr>
	                    <td><h:outputText value="Número do Cartão *" /></td>
	                    <td>
	                        <t:inputText id="idNumeroCartao"
	                                     size="40"
	                                     value="#{beanContratoPostagem.contratoPostagemNovo.cartao}" />
	                    </td>
	                </tr>
	                <tr>
	                    <td><h:outputText value="Código Administrativo *" /></td>
	                    <td>
	                        <t:inputText id="idCodigoAdministrativo"
	                                     size="40"
	                                     value="#{beanContratoPostagem.contratoPostagemNovo.codigoAdministrativo}" />
	                    </td>
	                </tr>
	                <tr>
	                    <td><h:outputText value="Número Diretoria Regional *" /></td>
	                    <td>
	                        <t:inputText id="idNumeroDiretoriaRegional"
	                                     size="40"
	                                     value="#{beanContratoPostagem.contratoPostagemNovo.numeroDiretoriaRegional}" />
	                    </td>
	                </tr>
	                <tr>
	                    <td><h:outputText value="Data de Início *" /></td>
	                    <td>
	                    	<rich:calendar id="idDataInicio"
	                    					value="#{beanContratoPostagem.contratoPostagemNovo.dataVigenciaInicial}"
	                    					datePattern="dd/MM/yyyy"
	                    					locale="pt_Br" />
	                    </td>
	                </tr>
	                <tr>
	                    <td><h:outputText value="Login Web Service *" /></td>
	                    <td>
	                        <t:inputText id="idLoginWebService"
	                                     size="40"
	                                     value="#{beanContratoPostagem.contratoPostagemNovo.usuarioAutenticacaoWS}" />
	                    </td>
	                </tr>
	                <tr>
	                    <td><h:outputText value="Senha Web Service *" /></td>
	                    <td>
	                        <t:inputText id="idSenhaWebService"
	                                     size="40"
	                                     value="#{beanContratoPostagem.contratoPostagemNovo.senhaAutenticacaoWS}" />
	                    </td>
	                </tr>
	            </table>
	        </div>
	</rich:panel>
	<rich:panel bodyClass="inpanelBody">
		<table>
			<tr>
				<td>
					<a4j:commandButton id="btnSalvar"
										styleClass="BotaoPadrao"
										value="Salvar"
										oncomplete="#{beanContratoPostagem.oncompleteModalCadastro}"
										onclick="if(!validarCampos()){return;}else{Richfaces.showModalPanel('idPnlContratoPostagemCadastro');}"
										actionListener="#{beanContratoPostagem.criarNovoContratoVigente}"
										reRender="formPesquisaContratoPostagem" />
				<td>
					<a4j:commandButton styleClass="BotaoPadrao"
										value="Fechar"
										oncomplete="Richfaces.hideModalPanel('idPnlContratoPostagemCadastro')"
										reRender="principalPesquisa"
										immediate="true" />
				</td>
			</tr>
		</table>
	</rich:panel>
</h:panelGrid>