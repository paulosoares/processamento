<%@ page contentType="text/html;charset=windows-1252"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<script type="text/javascript">
    function pesquisarRecursos() {
        document.getElementById('botaoCarregarComboRecursos').click();
    }
    function aguarde(mostrar, div) {
        if (mostrar == true) {
            document.getElementById(div).innerHTML = '<img src="/processamento/images/loading.gif" /><font class="Padrao">&nbsp;&nbsp;Aguarde...</font>';
        }
    }

    function pesquisar() {
        document.getElementById('idBotaoPesquisar').click();
    }

    function pesquisarHandler(valor) {
        document.getElementById(valor).click();
    }

    function verifyLength(field, maxlength){
		if(field.value.length > maxlength)
			field.value = field.value.substring(0,maxlength);
	}
</script>

<f:view>
    <a4j:page pageTitle="::.. Principal ..::"
              onload="pesquisarHandler('idBotaoPesquisar');">

        <h:form prependId="false">
            <jsp:include page="/pages/template/logoMenu.jsp" flush="true">
                <jsp:param name="nomePagina" value="Enviar para Assinar" />
            </jsp:include>
        </h:form>

        <a4j:form id="form" prependId="false">
            <h:panelGrid styleClass="MolduraExterna" cellpadding="0"
                         cellspacing="0">
                <h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0"
                             id="pnlCentral">
                    <a4j:outputPanel ajaxRendered="true" keepTransient="false"
                                     id="pnlResultadoPesquisa" styleClass="MolduraInterna">
                        <c:if
                            test="${not empty beanAguardandoAssinatura.listaAguadAssinatura}">
                            <div class="PainelTituloCriaTexto">
                                <span> Pesquisa: </span>
                            </div>

                            <rich:dataTable id="tableDocumentos" headerClass="DataTableDefaultHeader"
                                            styleClass="DataTableDefault"
                                            footerClass="DataTableDefaultFooter"
                                            rowClasses="DataTableRow, DataTableRow2"
                                            columnClasses="tres, dezCenter, dezCenter, vinteLeft, vinteLeft, vinteLeft, dezCenter, tres, tres, tres, cinco, sete" 
                                            rows="30"
                                            value="#{beanAguardandoAssinatura.listaAguadAssinatura}"
                                            var="wrappedAguard"
                                            binding="#{beanAguardandoAssinatura.tabelaAguardAssinatura}">

                                <rich:column>
                                    <f:facet name="header">
                                        <a4j:commandButton image="../../images/setabaixo.gif"
                                                           onclick="exibirMsgProcessando(true)"
                                                           oncomplete="exibirMsgProcessando(false);"
                                                           actionListener="#{beanAguardandoAssinatura.marcarTodosTextos}" />
                                    </f:facet>
                                    <h:selectBooleanCheckbox
                                        onclick="document.getElementById('BotaoAtualizarMarcacao').click();"
                                        value="#{wrappedAguard.checked}" />
                                </rich:column>

                                <rich:column sortBy="#{wrappedAguard.wrappedObject.comunicacao.identificacaoProcessual}" sortOrder="ASCENDING">
                                    <f:facet name="header">
                                        <h:outputText value="Processo" 
                                                      styleClass="DataTableDocumentoTexto" style="width: 88px;" />
                                    </f:facet>

                                    <h:commandLink styleClass="PadraoLink"
                                                   style="text-align:center;" target="_blank"
                                                   value="#{wrappedAguard.wrappedObject.comunicacao.identificacaoProcessual}"
                                                   action="#{beanAguardandoAssinatura.consultarProcessoDigital}">
                                        <f:setPropertyActionListener
                                            target="#{beanAguardandoAssinatura.seqObjetoIncidente}"
                                            value="#{wrappedAguard.wrappedObject.comunicacao.objetoIncidenteUnico.principal.id}" />
                                    </h:commandLink>

                                </rich:column>

                                <rich:column sortBy="#{wrappedAguard.wrappedObject.comunicacao.concatenaAnoNumeracaoUnica}" >
                                    <f:facet name="header">
                                        <h:outputText value="Nº Documento"
                                                      styleClass="DataTableDocumentoTexto" style="width: 101px;"/>
                                    </f:facet>
                                    <h:outputText styleClass="Padrao" style="text-align:left;"
                                                  value="#{wrappedAguard.wrappedObject.comunicacao.concatenaAnoNumeracaoUnica}" />
                                </rich:column>

                                <rich:column sortBy="#{wrappedAguard.wrappedObject.comunicacao.nomeMinistroRelatorAtual}">
                                    <f:facet name="header">
                                        <h:outputText value="Relator"
                                                      styleClass="DataTableDocumentoTexto" />
                                    </f:facet>
                                    <h:outputText styleClass="Padrao" style="text-align:center;"
                                                  value="#{wrappedAguard.wrappedObject.comunicacao.nomeMinistroRelatorAtual}" />
                                </rich:column>

                                <rich:column  style="width:3%" sortBy="#{wrappedAguard.wrappedObject.comunicacao.comunicacaoIncidente[0].objetoIncidente.nivelSigilo}">
                                    <f:facet name="header">
                                        <h:outputText value="Confidencialidade"
                                                      styleClass="DataTableDocumentoTexto" />
                                    </f:facet>
                                  <!-- 
                                    <h:outputText styleClass="Padrao" style="text-align:center;"
                                                  value="#{(wrappedDocumento.wrappedObject.comunicacao.confidencialidade eq '') ? 'Público' : wrappedDocumento.wrappedObject.comunicacao.confidencialidade}" />
                                        -->           
                                                    <h:outputText styleClass="Padrao" style="text-align:center;"
                                                  value="#{wrappedAguard.wrappedObject.comunicacao.comunicacaoIncidente[0].objetoIncidente.nivelSigilo}" />
                                                  
                                                  
                                </rich:column>

                                <rich:column sortBy="#{wrappedAguard.wrappedObject.comunicacao.dscNomeDocumento}">
                                    <f:facet name="header">
                                        <h:outputText value="Nome Documento"
                                                      styleClass="DataTableDocumentoTexto" />
                                    </f:facet>
                                    <h:outputText styleClass="Padrao" style="text-align:center;"
                                                  value="#{wrappedAguard.wrappedObject.comunicacao.dscNomeDocumento}" />
                                </rich:column>

                                <rich:column sortBy="#{wrappedAguard.wrappedObject.comunicacao.modeloComunicacao.tipoComunicacao.descricao}">
                                    <f:facet name="header">
                                        <h:outputText value="Tipo Documento" />
                                    </f:facet>
                                    <h:outputText styleClass="Padrao" style="text-align:center;"
                                                  value="#{wrappedAguard.wrappedObject.comunicacao.modeloComunicacao.tipoComunicacao.descricao}" />
                                </rich:column>

                                <rich:column sortBy="#{wrappedAguard.wrappedObject.comunicacao.descricaoFaseAtual}">
                                    <f:facet name="header">
                                        <h:outputText value="Situação" />
                                    </f:facet>
                                    <h:outputText styleClass="Padrao"
                                                  value="#{wrappedAguard.wrappedObject.comunicacao.descricaoFaseAtual}" />
                                </rich:column>
                                
                                <rich:column sortBy="#{wrappedAguard.wrappedObject.comunicacao.observacaoFaseAtual}">
                                    <f:facet name="header">
                                        <h:outputText value="Motivo" />
                                    </f:facet>
                                    <h:outputText styleClass="Padrao"
                                                  value="#{wrappedAguard.wrappedObject.comunicacao.observacaoFaseAtual}" />
                                </rich:column>

                                <rich:column sortBy="#{wrappedAguard.wrappedObject.comunicacao.nomeUsuarioCriacao}">
                                    <f:facet name="header">
                                        <h:outputText value="Usuário" />
                                    </f:facet>
                                    <h:outputText styleClass="Padrao"
                                                  value="#{wrappedAguard.wrappedObject.comunicacao.nomeUsuarioCriacao}" />
                                </rich:column>

                                <rich:column sortBy="#{wrappedAguard.wrappedObject.possuiPDF}">
                                    <f:facet name="header">
                                        <h:outputText value="PDF" />
                                    </f:facet>
                                    <h:outputLink
                                        rendered="#{wrappedAguard.wrappedObject.possuiPDF}"
                                        value="#{wrappedAguard.wrappedObject.linkPDF}">
                                        <h:graphicImage value="/images/pdf.png"></h:graphicImage>
                                    </h:outputLink>
                                    <rich:toolTip  followMouse="true" direction="top-left" style="width:800px"	
										rendered="#{wrappedAguard.wrappedObject.possuiPDF}"												                                            
	                                    hideDelay="20"
	                                    showDelay="250">    		                                             
	                             		<h:outputText value="#{beanAguardandoAssinatura.recuperarTextoDocumento}"/>
		                            </rich:toolTip>				
                                </rich:column>

                                <rich:column >
                                    <f:facet name="header">
                                        <h:outputText value="Cancelar" />
                                    </f:facet>
                                    <a4j:commandLink
                                        oncomplete="Richfaces.showModalPanel('modalPanelMotivoCancelaDocumento');"
                                        rendered="#{wrappedAguard.wrappedObject.possuiPDF}"
                                        actionListener="#{beanAguardandoAssinatura.recuperaLinhaParaModalPanel}">
                                        <h:graphicImage url="../../images/close.gif" title="Cancelar" />
                                    </a4j:commandLink>
                                </rich:column>

                                <rich:column  sortBy="#{wrappedAguard.wrappedObject.documentoComunicacaoResult.listaPecasProcessoEletronicoComunicacao}">
                                    <f:facet name="header">
                                        <h:outputText value="Peças" />
                                    </f:facet>
                                    <h:commandLink value="peças "
                                                   rendered="#{not empty wrappedAguard.wrappedObject.documentoComunicacaoResult.listaPecasProcessoEletronicoComunicacao}" />
                                    <rich:toolTip followMouse="false" direction="top-right"
                                                  horizontalOffset="-5" verticalOffset="-5"
                                                  styleClass="tooltipPecasEletronicas" hideDelay="20"
                                                  showDelay="250"
                                                  rendered="#{not empty wrappedAguard.wrappedObject.documentoComunicacaoResult.listaPecasProcessoEletronicoComunicacao}">
                                        <h:dataTable var="valor" id="tblValor"
                                                     value="#{wrappedAguard.wrappedObject.documentoComunicacaoResult.listaPecasProcessoEletronicoComunicacao}"
                                                     headerClass="CabecalhoTabelaDados"
                                                     rowClasses="LinhaParTabelaDados"
                                                     columnClasses="ColunaTabelaDados"
                                                     styleClass="TabelaDadosPreview">
                                            <h:column>
                                                <f:facet name="header">
                                                    <h:outputText value="Processo" />
                                                </f:facet>
                                                <h:panelGrid style="text-align: left;" width="60%">
                                                    <h:outputText value="#{valor.pecaProcessoEletronico.objetoIncidente.identificacao}"
                                                                  styleClass="Padrao" />
                                                </h:panelGrid>
                                            </h:column>
                                            <h:column>
                                                <f:facet name="header">
                                                    <h:outputText value="Tipo Peça" />
                                                </f:facet>
                                                <h:panelGrid style="text-align: left;" width="60%">
                                                    <h:outputText value="#{valor.pecaProcessoEletronico.tipoPecaProcesso.descricao}"
                                                                  styleClass="Padrao" />
                                                </h:panelGrid>
                                            </h:column>
                                            <h:column>
                                                <f:facet name="header">
                                                    <h:outputText value="PDF" />
                                                </f:facet>
                                                <h:commandLink action="#{beanAguardandoAssinatura.report}">
                                                    <f:setPropertyActionListener value="#{valor}"
                                                                                 target="#{beanAguardandoAssinatura.valor}" />
                                                    <h:graphicImage value="/images/pdf.png"></h:graphicImage>
                                                </h:commandLink>
                                            </h:column>
                                        </h:dataTable>
                                    </rich:toolTip>
                                </rich:column>

                                <rich:column sortBy="#{wrappedAguard.wrappedObject.documentoComunicacaoResult.listaProcessoLoteVinculados}">
                                    <f:facet name="header">
                                        <h:outputText value="Lote" />
                                    </f:facet>
                                    <h:commandLink value="vinculados "
                                                   rendered="#{not empty wrappedAguard.wrappedObject.documentoComunicacaoResult.listaProcessoLoteVinculados}" />
                                    <rich:toolTip followMouse="false" direction="top-right"
                                                  horizontalOffset="-5" verticalOffset="-5"
                                                  styleClass="tooltipPrecedentes" hideDelay="20"
                                                  showDelay="250"
                                                  rendered="#{not empty wrappedAguard.wrappedObject.documentoComunicacaoResult.listaProcessoLoteVinculados}">

                                        <h:dataTable var="valorL" id="tblValorL"
                                                     value="#{wrappedAguard.wrappedObject.documentoComunicacaoResult.listaProcessoLoteVinculados}"
                                                     headerClass="CabecalhoTabelaDados"
                                                     rowClasses="LinhaParTabelaDados"
                                                     columnClasses="ColunaTabelaDados"
                                                     styleClass="TabelaDadosPreview">
                                            <h:column>
                                                <f:facet name="header">
                                                    <h:outputText value="Processo" />
                                                </f:facet>
                                                <h:panelGrid style="text-align: center;" width="60%">
                                                    <h:outputText
                                                        value="#{valorL}"
                                                        styleClass="Padrao" />
                                                </h:panelGrid>
                                            </h:column>
                                        </h:dataTable>
                                    </rich:toolTip>
                                </rich:column>

                                <rich:column sortBy="#{wrappedAguard.wrappedObject.comunicacao.obsComunicacao}">
                                    <f:facet name="header">
                                        <h:outputText value="Observacao" />
                                    </f:facet>
                                    <a4j:commandLink
                                        rendered="#{not empty wrappedAguard.wrappedObject.comunicacao.obsComunicacao}">
                                        <h:graphicImage value="/images/icobs.png"></h:graphicImage>
                                    </a4j:commandLink>
                                    <rich:toolTip followMouse="false" direction="top-right"
                                                  horizontalOffset="-5" verticalOffset="-5"
                                                  styleClass="tooltipPrecedentes" hideDelay="20"
                                                  showDelay="250"
                                                  rendered="#{not empty wrappedAguard.wrappedObject.comunicacao.obsComunicacao}">
                                        <h:outputText value="#{wrappedAguard.wrappedObject.comunicacao.obsComunicacao}"/>
                                    </rich:toolTip>	
                                </rich:column>									
                            </rich:dataTable>

                            <rich:datascroller id="dataScrollerDocumentos" for="tableDocumentos"
                                               fastControls="hide" maxPages="10" pageIndexVar="paginaAtual"
                                               pagesVar="paginas" eventsQueue="ajaxQueue"
                                               ignoreDupResponses="true" reRender="pnlCentral">
                                <f:facet name="first">
                                    <h:outputText value="Primeira" />
                                </f:facet>
                                <f:facet name="first_disabled">
                                    <h:outputText value="Primeira" />
                                </f:facet>
                                <f:facet name="previous">
                                    <h:outputText value="Anterior" />
                                </f:facet>
                                <f:facet name="previous_disabled">
                                    <h:outputText value="Anterior" />
                                </f:facet>
                                <f:facet name="last">
                                    <h:outputText value="Última" />
                                </f:facet>
                                <f:facet name="last_disabled">
                                    <h:outputText value="Última" />
                                </f:facet>
                                <f:facet name="next">
                                    <h:outputText value="Próxima" />
                                </f:facet>
                                <f:facet name="next_disabled">
                                    <h:outputText value="Próxima" />
                                </f:facet>
                            </rich:datascroller>

                            <a4j:push interval="2000"
                                      actionListener="#{beanAguardandoAssinatura.pesquisaDocumentosGeradosAction}"
                                      reRender="pnlCentral"
                                      ignoreDupResponses="true"  
                                      onbeforedomupdate="exibirMsgProcessando(true)"
                                      oncomplete="exibirMsgProcessando(false);"
                                      eventProducer="#{beanAguardandoAssinatura.refreshController.adicionarListenerRefresh}"									
                                      eventsQueue="ajaxQueue"
                                      id="pushPesquisaAguardandoAssinatura" />

                            <a4j:commandButton id="botaoEncaminhar" value="Encaminhar para Assinatura"
                                               actionListener="#{beanAguardandoAssinatura.procurarSetorRelatorAction}"
                                               styleClass="BotaoEstendido" immediate="true"
                                               disabled="#{beanAguardandoAssinatura.verificarPerfilEncaminharAssinatura}"
                                               title="#{beanAguardandoAssinatura.hintBotaoEncaminharAssinatura}"
                                               oncomplete="if(#{beanAguardandoAssinatura.checkBoxSelecionado})Richfaces.showModalPanel('modalPanelEncaminharDocumento');"
                                               reRender="modalPanelEncaminharDocumento,pnlCentral" />

                            <a4j:commandButton id="botaoRevisar" value="Encaminhar para Revisão"
                                               actionListener="#{beanAguardandoAssinatura.procurarSetorRelatorAction}"
                                               styleClass="BotaoEstendido" immediate="true"
                                               disabled="#{beanAguardandoAssinatura.verificarPerfilEncaminharRevisao}"
                                               title="#{beanAguardandoAssinatura.hintBotaoEncaminharRevisao}"
                                               oncomplete="if(#{beanAguardandoAssinatura.checkBoxSelecionado})Richfaces.showModalPanel('modalPanelRevisarDocumento');"
                                               reRender="modalPanelEncaminharDocumento,pnlCentral" />
                        </c:if>
                    </a4j:outputPanel>

                    <a4j:commandButton styleClass="BotaoOculto"
                                       id="BotaoAtualizarMarcacao"
                                       actionListener="#{beanAguardandoAssinatura.atualizarMarcacao}"
                                       reRender="pnlResultadoPesquisa" />

                    <a4j:commandButton styleClass="BotaoOculto" id="idBotaoPesquisar"
                                       actionListener="#{beanAguardandoAssinatura.pesquisaDocumentosGeradosAction}"
                                       reRender="pnlCentral" onclick="exibirMsgProcessando(true)"
                                       oncomplete="exibirMsgProcessando(false);" />

                    <a4j:commandButton styleClass="BotaoOculto" id="btnAtualizaSessao"
                                       actionListener="#{beanAguardandoAssinatura.atualizaSessaoAction}" />
                </h:panelGrid>
            </h:panelGrid>

        </a4j:form>

        <rich:modalPanel id="modalPanelMotivoCancelaDocumento" width="630"
                         height="160" keepVisualState="true">
            <f:facet name="header">
                <h:outputText value="Favor inserir o motivo do cancelamento" />
            </f:facet>
            <a4j:form prependId="false">
                <div>
                    <span class="Padrao"> <h:outputText styleClass="Padrao"
                                  value="Motivo:" /> </span>
                </div>
                <div style="padding-top: 3px;">
                    <span> <h:inputTextarea cols="80" rows="5" onkeyup="verifyLength(this,1000)"
                                     value="#{beanAguardandoAssinatura.anotacaoCancelamento}" /> </span>
                </div>
                <div style="padding-top: 15px;">
                    <span class="Padrao">
                        <a4j:commandButton
                            oncomplete="aguarde(false, 'dvAguardePanelAtualizarControleVotos');
                            Richfaces.hideModalPanel('modalPanelMotivoCancelaDocumento');"
                            value="Salvar"
                            reRender="modalPanelMotivoCancelaDocumento"			
                            actionListener="#{beanAguardandoAssinatura.cancelaPDFDocumentoAction}"
                            onclick="aguarde(true, 'dvAguardePanelCancelaPDFDocumento');" />
                        <a4j:commandButton 
                            type="reset"
                            onclick="Richfaces.hideModalPanel('modalPanelMotivoCancelaDocumento');"
                            value="Fechar" />
                    </span>
                </div>
                <div id="dvAguardePanelCancelaPDFDocumento"></div>
            </a4j:form>
        </rich:modalPanel>

        <rich:modalPanel id="modalPanelEncaminharDocumento" width="600"
                         height="140" keepVisualState="true">
            <f:facet name="header">
                <h:outputText value="Favor selecionar o setor de destino para assinatura" />
            </f:facet>
            <a4j:form id="modalPanelEncaminharDocumentoForm" prependId="false">

                <div>
                    <span>
                        <h:outputText styleClass="Padrao" style="font-size:12px;"
                                      value="Aviso: Um ou mais documento(s) 
                                      selecionado(s) serão encaminhados para relator diverso do processo a 
                                      ele(s) vinculado." id="otAlertaEncaminhamento"
                                      rendered="#{beanAguardandoAssinatura.isEncaminhandoRelatorDiversoProcesso}" />
                    </span>
                </div>

                <div style="padding-top: 10px;">
                    <span class="Padrao">
                        <h:outputText styleClass="Padrao"
                                      value="Setor de Destino:" />
                        <h:selectOneMenu styleClass="Input"	style="margin-left:10px;"
                                         value="#{beanAguardandoAssinatura.idSetorDestino}">
                            <f:selectItems
                                value="#{beanAguardandoAssinatura.itensSetoresDestino}" />
                            <a4j:support 
                                actionListener="#{beanAguardandoAssinatura.alterarSetorDestino}"
                                event="onchange" reRender="modalPanelEncaminharDocumentoForm" />
                        </h:selectOneMenu>
                    </span>
                </div>

                <div style="padding-top: 20px; text-align: center;">
                    <span>
                        <a4j:commandButton styleClass="BotaoPadrao"
                                           actionListener="#{beanAguardandoAssinatura.encaminharDocumentosAction}"
                                           onclick="Richfaces.hideModalPanel('modalPanelEncaminharDocumento');"
                                           value="OK">
                        </a4j:commandButton>
                    </span>
                    <span>
                        <a4j:commandButton
                            onclick="Richfaces.hideModalPanel('modalPanelEncaminharDocumento');"
                            styleClass="BotaoPadrao" value="Fechar" />
                    </span>
                </div>
            </a4j:form>
        </rich:modalPanel>

        <rich:modalPanel id="modalPanelRevisarDocumento" width="600"
                         height="115" keepVisualState="true">
            <f:facet name="header">
                <h:outputText value="Favor selecionar o setor de destino para revisão" />
            </f:facet>

            <a4j:form id="modalPanelRevisarDocumentoForm" prependId="false">

                <div style="padding-top: 10px; text-align: center;">
                    <span class="Padrao">
                        <h:outputText styleClass="Padrao" value="Setor de Destino:" />

                        <h:selectOneMenu styleClass="Input"	style="margin-left:10px;" value="#{beanAguardandoAssinatura.idSetorDestino}">
                            <f:selectItems value="#{beanAguardandoAssinatura.itensSetoresDestino}" />
                            <a4j:support 
                                actionListener="#{beanAguardandoAssinatura.alterarSetorDestino}"
                                event="onchange" reRender="modalPanelRevisarDocumentoForm" />
                        </h:selectOneMenu>
                    </span>
                </div>

                <div style="padding-top: 20px; text-align: center;">
                    <a4j:commandButton styleClass="BotaoPadrao"
                                       actionListener="#{beanAguardandoAssinatura.encaminharDocumentosParaRevisaoAction}"
                                       onclick="if (confirm('Confirma o encaminhamento dos documentos selecionados para a revisão?')) {Richfaces.hideModalPanel('modalPanelRevisarDocumento');} else {return;}"
                                       value="OK">
                    </a4j:commandButton>

                    <a4j:commandButton
                        onclick="Richfaces.hideModalPanel('modalPanelRevisarDocumento');"
                        styleClass="BotaoPadrao" value="Fechar" />
                </div>

            </a4j:form>

        </rich:modalPanel>

        <jsp:include page="/pages/template/footer.jsp" flush="true" />

    </a4j:page>
</f:view>