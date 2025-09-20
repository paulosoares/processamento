<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
           prefix="security"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />


<script type="text/javascript">
    function aguarde(mostrar, div) {
        if (mostrar == true) {
            document.getElementById(div).innerHTML = '<img src="/processamento/images/loading.gif" /><font class="Padrao">&nbsp;&nbsp;Aguarde...</font>';
        }
    }

    function pesquisar() {
        document.getElementById('botaoPesquisar').click();
    }

    function pesquisarHandler(valor) {
        document.getElementById(valor).click();
    }

    function caixaAlta(campo) {
        campo.value = campo.value.toUpperCase();
    }

    function funcaoVazia() {
    }

    function verifyLength(field, maxlength){
		if(field.value.length > maxlength)
			field.value = field.value.substring(0,maxlength);
	}
</script>

<f:view>
    <a4j:page pageTitle="::.. Principal ..::">

        <h:form prependId="false">
            <jsp:include page="/pages/template/logoMenu.jsp" flush="true">
                <jsp:param name="nomePagina" value="Assinar Documentos" />
            </jsp:include>
        </h:form>

        <a4j:form id="form" prependId="false">

            <h:panelGrid styleClass="MolduraExterna" cellpadding="0"
                         cellspacing="0" id="pnlCentral">
                <h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0">
                    <a4j:outputPanel id="pnlPrincipalPesquisa" ajaxRendered="true">
                        <div class="PainelTituloCriaTexto">
                            <span> Pesquisa: </span>
                        </div>

                        <div style="padding-top: 10px;">
                            <span class="Padrao">
                            	<h:outputText styleClass="Padrao" value='Situação:' /> 
                            	<h:selectOneMenu value="#{beanAssinarDocumentos.codigoFaseDocumento}"
                                          		 style="margin-left:5px;">
                                    <f:selectItems value="#{beanAssinarDocumentos.itensFaseDocumento}" />
                                </h:selectOneMenu> 
                            	<h:outputText styleClass="Padrao" value='Data de geração:' style="margin-left:5px;"/> 
                            	<rich:calendar id="idDataGeracao"
											   value="#{beanAssinarDocumentos.dataCriacaoDocumento}"
											   datePattern="dd/MM/yyyy" locale="pt_Br" />
											   
                        		<rich:spacer height="10px"/>								
								<h:selectBooleanCheckbox value="#{beanAssinarDocumentos.buscarApenasSigilosos}" />
								<h:outputText value="Exibir apenas processos sigilosos"/>
								
								
                                <a4j:commandButton styleClass="BotaoPesquisar"
                                                   style="margin-left:15px;" value="Pesquisar"
                                                   id="btnPesquisarProcesso"
                                                   actionListener="#{beanAssinarDocumentos.pesquisarDocumentosAction}"
                                                   ignoreDupResponses="true" reRender="pnlResultadoPesquisa"
                                                   onclick="exibirMsgProcessando(true)"
                                                   oncomplete="exibirMsgProcessando(false);" />
                             </span>
                        </div>
                    </a4j:outputPanel>

                    <a4j:outputPanel ajaxRendered="true" keepTransient="false"
                                     id="pnlResultadoPesquisa" styleClass="MolduraInterna">
                        <c:if test="${not empty beanAssinarDocumentos.listaDocumentos}">
                            <hr color="red" align="left" size="1px" width="90%" />
                            <h:outputText value="Total de registros: #{beanAssinarDocumentos.totalRegistros}"/>                            	
                            <rich:dataTable headerClass="DataTableDefaultHeader"
                                            styleClass="DataTableDefault"
                                            footerClass="DataTableDefaultFooter"
                                            rowClasses="DataTableRow, DataTableRow2"
                                            columnClasses="tres, dezCenter, dezCenter, quinzeCenter, vinteCenter, vinteCenter, cincoCenter, tres, quinzeCenter, cinco, cinco,cinco,cinco"
                                            value="#{beanAssinarDocumentos.listaDocumentos}"
                                            var="wrappedDocumento"
                                            rows="#{beanAssinarDocumentos.rows}"                                            
                                            id="tbDocumentos"
                                            binding="#{beanAssinarDocumentos.tabelaDocumentos}">
                                <rich:column  style="width:1%">
                                    <f:facet name="header">
                                        <a4j:commandButton image="../../images/setabaixo.gif"
                                                           onclick="exibirMsgProcessando(true)"
                                                           oncomplete="exibirMsgProcessando(false);"
                                                           actionListener="#{beanAssinarDocumentos.marcarTodosTextos}" />
                                    </f:facet>
                                    <h:selectBooleanCheckbox
                                        onclick="document.getElementById('BotaoAtualizarMarcacao').click();"
                                        value="#{wrappedDocumento.checked}" />                                    
                                </rich:column>

                                <rich:column style="width:5%"
                                    sortBy="#{wrappedDocumento.wrappedObject.comunicacao.dataEntradaDeslocamentoAtual}">
                                    <f:facet name="header">
                                        <h:outputText value="Data de Entrada" />
                                    </f:facet>
                                    <h:outputText styleClass="Padrao" style="text-align:center;"
                                                  value="#{wrappedDocumento.wrappedObject.comunicacao.dataEntradaDeslocamentoAtual}">
                                        <f:convertDateTime pattern="dd/MM/yyyy HH:mm"/>  
                                    </h:outputText>
                                </rich:column>

                                <rich:column  style="width:2%" sortBy="#{wrappedDocumento.wrappedObject.comunicacao.concatenaAnoNumeracaoUnica}" >
                                    <f:facet name="header">
                                        <h:outputText value="Nº Documento"
                                                      styleClass="DataTableDocumentoTexto" style="width: 101px;"/>
                                    </f:facet>
                                    <h:outputText styleClass="Padrao" style="text-align:left;"
                                                  value="#{wrappedDocumento.wrappedObject.comunicacao.concatenaAnoNumeracaoUnica}" />
                                </rich:column>

                                <rich:column  style="width:5%"
                                    comparator="#{beanAssinarDocumentos.idProcessualComunicacaoComparator}"
                                    sortBy="#{wrappedDocumento.wrappedObject.comunicacao.identificacaoProcessual}">
                                    <f:facet name="header">
                                        <h:outputText value="Processo"
                                                      styleClass="DataTableDocumentoTexto" />
                                    </f:facet>
                                    <h:commandLink styleClass="PadraoLink"
                                                   style="text-align:center; width: 88px;"  target="_blank"
                                                   value="#{wrappedDocumento.wrappedObject.comunicacao.identificacaoProcessual}"
                                                   action="#{beanAssinarDocumentos.consultarProcessoDigital}">
                                        <f:setPropertyActionListener
                                            target="#{beanAssinarDocumentos.seqObjetoIncidente}"
                                            value="#{wrappedDocumento.wrappedObject.comunicacao.objetoIncidenteUnico.principal.id}" />
                                    </h:commandLink>
                                </rich:column>

                                <rich:column  style="width:10%" sortBy="#{wrappedDocumento.wrappedObject.comunicacao.nomeMinistroRelatorAtual}">
                                    <f:facet name="header">
                                        <h:outputText value="Relator"
                                                      styleClass="DataTableDocumentoTexto" />
                                    </f:facet>
                                    <h:outputText styleClass="Padrao" style="text-align:center;"
                                                  value="#{wrappedDocumento.wrappedObject.comunicacao.nomeMinistroRelatorAtual}" />
                                </rich:column>

                                <rich:column  style="width:3%" sortBy="#{wrappedDocumento.wrappedObject.comunicacao.comunicacaoIncidente[0].objetoIncidente.nivelSigilo}">
                                    <f:facet name="header">
                                        <h:outputText value="Confidencialidade"
                                                      styleClass="DataTableDocumentoTexto" />
                                    </f:facet>
                                  <!-- 
                                    <h:outputText styleClass="Padrao" style="text-align:center;"
                                                  value="#{(wrappedDocumento.wrappedObject.comunicacao.confidencialidade eq '') ? 'Público' : wrappedDocumento.wrappedObject.comunicacao.confidencialidade}" />
                                        -->           
                                                    <h:outputText styleClass="Padrao" style="text-align:center;"
                                                  value="#{wrappedDocumento.wrappedObject.comunicacao.comunicacaoIncidente[0].objetoIncidente.nivelSigilo}" />
                                                  
                                                  
                                </rich:column>
                                

                                <rich:column  style="width:15%" sortBy="#{wrappedDocumento.wrappedObject.comunicacao.dscNomeDocumento}">
                                    <f:facet name="header">
                                        <h:outputText value="Nome Documento"
                                                      styleClass="DataTableDocumentoTexto" />
                                    </f:facet>
                                    <h:outputText styleClass="Padrao" style="text-align:center;"
                                                  value="#{wrappedDocumento.wrappedObject.comunicacao.dscNomeDocumento}" />
                                </rich:column>

                                <rich:column  style="width:5%" sortBy="#{wrappedDocumento.wrappedObject.comunicacao.modeloComunicacao.tipoComunicacao.descricao}">
                                    <f:facet name="header">
                                        <h:outputText value="Tipo Modelo" style="width: 88px;"
                                                      styleClass="DataTableDocumentoTexto" />							
                                    </f:facet>
                                    <h:outputText styleClass="Padrao" style="text-align:center;"
                                                  value="#{wrappedDocumento.wrappedObject.comunicacao.modeloComunicacao.tipoComunicacao.descricao}" />
                                </rich:column>

                                <rich:column  style="width:1%">
                                    <f:facet name="header">
                                        <h:outputText value="PDF" />
                                    </f:facet>
                                    <h:commandLink
                                        rendered="#{wrappedDocumento.wrappedObject.possuiPDF}"
                                        actionListener="#{beanAssinarDocumentos.abrirPdf}">
                                        <h:graphicImage value="/images/pdf.png"></h:graphicImage>
                                    </h:commandLink>
                                    <rich:toolTip  followMouse="true" direction="top-left" style="width:800px"	
										rendered="#{wrappedDocumento.wrappedObject.possuiPDF}"												                                            
	                                    hideDelay="20"
	                                    showDelay="250">    		                                             
	                             		<h:outputText value="#{beanAssinarDocumentos.recuperarTextoDocumento}"/>
		                            </rich:toolTip>				
                                </rich:column>

                                <rich:column  rendered="#{! beanAssinarDocumentos.salaOficiais}" style="width:10%" sortBy="#{wrappedDocumento.wrappedObject.comunicacao.nomeUsuarioCriacao}" sortOrder="ASCENDING">
                                    <f:facet name="header">
                                        <h:outputText value="Criado por "  />
                                    </f:facet>
                                    <h:outputText styleClass="Padrao" style="text-align:center;"
                                                  value="#{wrappedDocumento.wrappedObject.comunicacao.nomeUsuarioCriacao}" />
                                </rich:column>

							 <rich:column  style="width:10%" sortBy="#{wrappedDocumento.wrappedObject.comunicacao.nomeUsuarioResponsavel}" sortOrder="ASCENDING" rendered="#{beanAssinarDocumentos.salaOficiais}"> 
                                    <f:facet name="header">
                                        <h:outputText value="Atribuído para " />
                                    </f:facet>
                                    <h:outputText styleClass="Padrao" style="text-align:center;"
                                                  value="#{wrappedDocumento.wrappedObject.comunicacao.nomeUsuarioResponsavel}" />
                                </rich:column>

							 <rich:column  style="width:5%" sortBy="#{wrappedDocumento.wrappedObject.comunicacao.dataAtribuicao}" sortOrder="ASCENDING" rendered="#{beanAssinarDocumentos.salaOficiais}"> 
                                    <f:facet name="header">
                                        <h:outputText value="Atribuído em " />
                                    </f:facet>
                                        <h:outputText styleClass="Padrao" style="text-align:center;"
                                                  value="#{wrappedDocumento.wrappedObject.comunicacao.dataAtribuicao}">
                                        <f:convertDateTime pattern="dd/MM/yyyy HH:mm"/>  
                                    </h:outputText>
                                </rich:column>


                                <rich:column  style="width:3%" sortBy="#{wrappedDocumento.wrappedObject.comunicacao.descricaoFaseAtual}">
                                    <f:facet name="header">
                                        <h:outputText value="Situação" />
                                    </f:facet>
                                    <h:outputText styleClass="Padrao"
                                                  value="#{wrappedDocumento.wrappedObject.comunicacao.descricaoFaseAtual}" />
                                </rich:column>
                                
                                <rich:column sortBy="#{wrappedDocumento.wrappedObject.comunicacao.observacaoFaseAtual}">
									<f:facet name="header">
										<h:outputText value="Motivo" />
									</f:facet>
									<h:outputText styleClass="Padrao"
 										value="#{wrappedDocumento.wrappedObject.comunicacao.observacaoFaseAtual}" />
								</rich:column>

                                <rich:column  style="width:1%">
                                    <f:facet name="header">
                                        <h:outputText value="Editar" />
                                    </f:facet>
                                    <a4j:commandLink id="btnEditarCancelaDocumento" 
                                                     rendered="#{wrappedDocumento.wrappedObject.possuiPDF}"
                                                     reRender="frmEditarDocumento" process="btnEditarCancelaDocumento"
                                                     actionListener="#{beanAssinarDocumentos.recuperaLinhaParaModalPanel}" 
                                                     oncomplete="Richfaces.showModalPanel('modalPanelEditarDocumento')">
                                        <h:graphicImage url="../../images/btabrir.gif"
                                                        title="Editar Documento" />
                                        <a4j:status>
								            <f:facet name="start">
								                <h:graphicImage value="/images/ai.gif" alt="ai" />
								            </f:facet>
								        </a4j:status>
                                    </a4j:commandLink>
                                </rich:column>

                                <rich:column  style="width:1%">
                                    <f:facet name="header">
                                        <h:outputText value="Cancelar" />
                                    </f:facet>
                                    <a4j:commandLink id="btnCancelarAssinaturaDocumento"
                                                     rendered="#{wrappedDocumento.wrappedObject.comunicacao.assinado 
                                                                 || wrappedDocumento.wrappedObject.comunicacao.aguardandoEncaminhamentoEstfDecisao}"
                                                     reRender="frmCancelarAssinaturaDocumento"
                                                     actionListener="#{beanAssinarDocumentos.recuperaLinhaParaModalPanel}">
                                        <rich:componentControl
                                            for="modalPanelCancelarAssinaturaDocumento"
                                            attachTo="btnCancelarAssinaturaDocumento" operation="show"
                                            event="onclick" />
                                        <h:graphicImage url="../../images/close.gif"
                                                        title="Cancelar Assinatura" />
                                    </a4j:commandLink>
                                </rich:column>

                                <rich:column  style="width:1%" sortBy="#{wrappedDocumento.wrappedObject.documentoComunicacaoResult.listaPecasProcessoEletronicoComunicacao}">
                                    <f:facet name="header">
                                        <h:outputText value="Peças" />
                                    </f:facet>
                                    <h:commandLink value="peças "
                                                   rendered="#{not empty wrappedDocumento.wrappedObject.documentoComunicacaoResult.listaPecasProcessoEletronicoComunicacao}" />
                                    <rich:toolTip followMouse="false" direction="top-right"
                                                  horizontalOffset="-5" verticalOffset="-5"
                                                  styleClass="tooltipPecasEletronicas" hideDelay="20"
                                                  showDelay="250"
                                                  rendered="#{not empty wrappedDocumento.wrappedObject.documentoComunicacaoResult.listaPecasProcessoEletronicoComunicacao}">
                                        <h:dataTable var="valor" id="tblValor"
                                                     value="#{wrappedDocumento.wrappedObject.documentoComunicacaoResult.listaPecasProcessoEletronicoComunicacao}"
                                                     headerClass="CabecalhoTabelaDados"
                                                     rowClasses="LinhaParTabelaDados"
                                                     columnClasses="ColunaTabelaDados"
                                                     styleClass="TabelaDadosPreview">
                                            <h:column>
                                                <f:facet name="header">
                                                    <h:outputText value="Processo" />
                                                </f:facet>
                                                <h:panelGrid style="text-align: left;">
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
                                                <h:commandLink action="#{assinadorBaseBean.report}">
                                                    <f:setPropertyActionListener value="#{valor}"
                                                                                 target="#{assinadorBaseBean.valor}" />
                                                    <h:graphicImage value="/images/pdf.png"></h:graphicImage>
                                                </h:commandLink>
                                            </h:column>
                                        </h:dataTable>
                                    </rich:toolTip>
                                </rich:column>

                                <rich:column  style="width:1%">
                                    <f:facet name="header">
                                        <h:outputText value="Lote" />
                                    </f:facet>
                                    <h:commandLink value="vinculados "
                                                   rendered="#{not empty wrappedDocumento.wrappedObject.documentoComunicacaoResult.listaProcessoLoteVinculados}" />
                                    <rich:toolTip followMouse="false" direction="top-right"
                                                  horizontalOffset="-5" verticalOffset="-5"
                                                  styleClass="tooltipPrecedentes" hideDelay="20" showDelay="250"
                                                  rendered="#{not empty wrappedDocumento.wrappedObject.documentoComunicacaoResult.listaProcessoLoteVinculados}">

                                        <h:dataTable var="valorL" id="tblValorL"
                                                     value="#{wrappedDocumento.wrappedObject.documentoComunicacaoResult.listaProcessoLoteVinculados}"
                                                     headerClass="CabecalhoTabelaDados"
                                                     rowClasses="LinhaParTabelaDados"
                                                     columnClasses="ColunaTabelaDados"
                                                     styleClass="TabelaDadosPreview">
                                            <h:column>
                                                <f:facet name="header">
                                                    <h:outputText value="Processo" />
                                                </f:facet>
                                                <h:panelGrid style="text-align: center;" width="60%">
                                                    <h:outputText value="#{valorL}" styleClass="Padrao" />
                                                </h:panelGrid>
                                            </h:column>
                                        </h:dataTable>
                                    </rich:toolTip>
                                </rich:column>

                                <rich:column  style="width:1%" sortBy="#{wrappedDocumento.wrappedObject.comunicacao.obsComunicacao}">
                                    <f:facet name="header">
                                        <h:outputText value="Obs" />
                                    </f:facet>
                                    <a4j:commandLink
                                        rendered="#{not empty wrappedDocumento.wrappedObject.comunicacao.obsComunicacao}">
                                        <h:graphicImage value="/images/icobs.png"></h:graphicImage>
                                    </a4j:commandLink>
                                    <rich:toolTip followMouse="false" direction="top-right"
                                                  horizontalOffset="-5" verticalOffset="-5"
                                                  styleClass="tooltipPrecedentes" hideDelay="20"
                                                  showDelay="250"
                                                  rendered="#{not empty wrappedDocumento.wrappedObject.comunicacao.obsComunicacao}">
                                        <h:outputText value="#{wrappedDocumento.wrappedObject.comunicacao.obsComunicacao}"/>
                                    </rich:toolTip>	
                                </rich:column>
                            </rich:dataTable>
                                                        
                            <h:panelGroup>
                            	<rich:spacer height="15"/>
                            	<h:outputText styleClass="Padrao" value='Quantidade de registros por página:'/>
                            	<h:inputText value="#{beanAssinarDocumentos.rows}" 
                            				 style="margin-left:10px; width: 30px !important;"
                            				 immediate="true">
                            		<a4j:support event="onblur" reRender="pnlResultadoPesquisa"/>
                            	</h:inputText>
                            </h:panelGroup>                                                   
							<rich:datascroller for="tbDocumentos"
							                   maxPages="#{beanAssinarDocumentos.maxPages}"  
							                   page="1"							                   
							                   id="scroll" align="left" />
							
                            <security:authorize ifAnyGranted="RS_ASSINATURA_TEXTOS,RS_MASTER_PROCESSAMENTO">
                                <a4j:push interval="2000"
                                          actionListener="#{beanAssinarDocumentos.pesquisarDocumentosAction}"
                                          ignoreDupResponses="true" reRender="pnlResultadoPesquisa" 
                                          onbeforedomupdate="exibirMsgProcessando(true)"
                                          oncomplete="exibirMsgProcessando(false);"
                                          eventProducer="#{beanAssinarDocumentos.refreshController.adicionarListenerRefresh}"									
                                          eventsQueue="ajaxQueue"
                                          id="pushPesquisaTextoProcesso" />

							<security:authorize ifAnyGranted="RS_FINALIZAR_TEXTOS,RS_MASTER_PROCESSAMENTO">
                               
                                                    <a4j:commandButton id="botaoAtribuir" value="Atribuir" rendered="#{beanAssinarDocumentos.salaOficiais}"
                                                   actionListener="#{beanAssinarDocumentos.procurarSetorRelatorAction}"
                                                   styleClass="BotaoPadrao" immediate="true"
                                                   onclick="exibirMsgProcessando(true);"
                                                   oncomplete="exibirMsgProcessando(false); Richfaces.showModalPanel('modalPanelAtribuir');"
                                                   reRender="modalPanelAtribuir" />
                                                   
                                                   
                                                   
                            </security:authorize>

                                <h:commandButton styleClass="BotaoPadrao"
                                                 action="#{beanAssinarDocumentos.assinarDocumentosSelecionados}"
                                                 value="Assinar" />

                                <a4j:commandButton id="botaoEncaminhar" value="Encaminhar"
                                                   actionListener="#{beanAssinarDocumentos.procurarSetorRelatorAction}"
                                                   styleClass="BotaoPadrao" immediate="true"
                                                   onclick="exibirMsgProcessando(true);"
                                                   oncomplete="exibirMsgProcessando(false); Richfaces.showModalPanel('modalPanelEncaminharDocumento');"
                                                   reRender="modalPanelEncaminharDocumento" />

                                <a4j:commandButton id="botaoEncaminharDJe" value="Encaminhar para o DJe"
                                                   actionListener="#{beanAssinarDocumentos.encaminharParaDJeAction}"
                                                   styleClass="BotaoPadraoEstendido" onclick="exibirMsgProcessando(true);"
                                                   oncomplete="exibirMsgProcessando(false);" reRender="pnlResultadoPesquisa" />
                            </security:authorize>
                            
                             <h:commandButton id="botaoImprimir" value="Imprimir"
                                                   actionListener="#{beanAssinarDocumentos.imprimirDocumentosAction}"
                                                   styleClass="BotaoPadrao" />

                            <security:authorize ifAnyGranted="RS_FINALIZAR_TEXTOS,RS_MASTER_PROCESSAMENTO">
                                <a4j:commandButton id="botaoFinalizar" value="Finalizar"
                                                   actionListener="#{beanAssinarDocumentos.finalizarDocumentosAction}"
                                                   styleClass="BotaoPadrao" onclick="exibirMsgProcessando(true);"
                                                   oncomplete="exibirMsgProcessando(false);" reRender="pnlResultadoPesquisa" />
                            </security:authorize>

                            <a4j:commandButton styleClass="BotaoOculto"
                                               id="BotaoAtualizarMarcacao"
                                               actionListener="#{beanAssinarDocumentos.atualizarMarcacao}" />

                        </c:if>
                    </a4j:outputPanel>

                    <a4j:commandButton styleClass="BotaoOculto" id="btnAtualizaSessao"
                                       actionListener="#{beanAssinarDocumentos.atualizaSessaoAction}" />
                    <a4j:commandButton id="botaoPesquisar" value="Pesquisar"
                                       styleClass="BotaoOculto"
                                       actionListener="#{beanAssinarDocumentos.pesquisarDocumentosAction}"
                                       reRender="pnlResultadoPesquisa"
                                       onclick="exibirMsgProcessando(true)"
                                       oncomplete="exibirMsgProcessando(false);" />
                    <a4j:commandButton id="idBotaoPesquisar" value="Pesquisar"
                                       styleClass="BotaoOculto"
                                       actionListener="#{beanAssinarDocumentos.pesquisarAguardandoAssinaturaAction}"
                                       reRender="pnlResultadoPesquisa"
                                       onclick="exibirMsgProcessando(true)"
                                       oncomplete="exibirMsgProcessando(false);" />

                </h:panelGrid>
            </h:panelGrid>
        </a4j:form>

        <rich:modalPanel id="modalPanelEncaminharDocumento" width="630"
                         height="100" keepVisualState="true">
            <f:facet name="header">
                <h:outputText value="Favor selecionar o setor de destino" />
            </f:facet>
            <a4j:form prependId="false" rendered="#{beanAssinarDocumentos.selecionadosTela}">

                <div>
                    <span class="Padrao">
                        <h:outputText styleClass="Padrao"
                                      value="Setor de Destino:" />
                        <h:selectOneMenu
                            style="margin-left:10px;"
                            value="#{beanAssinarDocumentos.idSetorDestino}"
                            styleClass="Input">
							<f:selectItems
								value="#{beanAssinarDocumentos.itensSetoresDestino}" />
						</h:selectOneMenu>
                    </span>
                </div>

                <div style="padding-top: 20px;">
                    <span>
                        <a4j:commandButton styleClass="BotaoPadrao"
                                           actionListener="#{beanAssinarDocumentos.encaminharDocumentosAction}"
                                           onclick="Richfaces.hideModalPanel('modalPanelEncaminharDocumento');"
                                           value="OK" />
                    </span>
                    <span>
                        <h:commandButton
                            onclick="Richfaces.hideModalPanel('modalPanelEncaminharDocumento');"
                            styleClass="BotaoPadrao" value="Fechar" />
                    </span>
                </div>

            </a4j:form>
            <a4j:form prependId="false" rendered="#{!beanAssinarDocumentos.selecionadosTela}" >

				<div>
					<span class="Padrao"> <h:outputText styleClass="Padrao"
							value="Favor selecionar ao menos um documento para proceder com a operação." /> 
					</span>
				</div>

				<div style="padding-top: 20px;">
					 <span> <h:commandButton
							onclick="Richfaces.hideModalPanel('modalPanelEncaminharDocumento');"
							styleClass="BotaoPadrao" value="Fechar" />
					</span>
				</div>

			</a4j:form>
        </rich:modalPanel>


		<rich:modalPanel id="modalPanelAtribuir" width="630" height="100"
			keepVisualState="true">
					
			<f:facet name="header">
				<h:outputText value="Atribuir Documento" />

			</f:facet>
			<a4j:form prependId="false" rendered="#{beanAssinarDocumentos.selecionadosTela}" >

				<div>
					<span class="Padrao"> <h:outputText styleClass="Padrao"
							value="Responsável:" /> <h:selectOneMenu
							style="margin-left:10px;"
							value="#{beanAssinarDocumentos.idUsuarioAtribuicao}"
							styleClass="Input">
							<f:selectItems
								value="#{beanAssinarDocumentos.itensUsuariosSetor}" />
						</h:selectOneMenu>
					</span>
				</div>

				<div style="padding-top: 20px;">
					<span> <a4j:commandButton styleClass="BotaoPadrao"
							actionListener="#{beanAssinarDocumentos.atribuirDocumentosAction}"
							onclick="exibirMsgProcessando(true); Richfaces.hideModalPanel('modalPanelAtribuir');"
							value="OK" rendered="#{beanAssinarDocumentos.selecionadosTela}"
							 oncomplete="exibirMsgProcessando(false); Richfaces.hideModalPanel('modalPanelAtribuir');setTimeout('pesquisar()', 500);"/>
					</span> <span> <h:commandButton
							onclick="Richfaces.hideModalPanel('modalPanelAtribuir');"
							styleClass="BotaoPadrao" value="Fechar" />
					</span>
				</div>

			</a4j:form>
			
			<a4j:form prependId="false" rendered="#{!beanAssinarDocumentos.selecionadosTela}" >

				<div>
					<span class="Padrao"> <h:outputText styleClass="Padrao"
							value="Favor selecionar ao menos um documento para proceder com a operação." /> 
					</span>
				</div>

				<div style="padding-top: 20px;">
					 <span> <h:commandButton
							onclick="Richfaces.hideModalPanel('modalPanelAtribuir');"
							styleClass="BotaoPadrao" value="Fechar" />
					</span>
				</div>

			</a4j:form>
			
		</rich:modalPanel>



		<rich:modalPanel id="modalPanelEditarDocumento" width="630"
                         height="160">
            <f:facet name="header">
                <h:outputText value="Favor inserir o motivo do cancelamento" />
            </f:facet>
            <h:form id="frmEditarDocumento">
                <div>
                    <span class="Padrao"> <h:outputText styleClass="Padrao"
                                  value="Motivo:" /> </span>
                </div>
                <div style="padding-top: 3px;">
                    <span> <h:inputTextarea cols="80" rows="5" onkeyup="verifyLength(this,1000)"
                                     value="#{beanAssinarDocumentos.anotacaoCancelamento}" /> </span>
                </div>
                <div style="padding-top: 15px;">
                    <span> <h:commandButton
                            actionListener="#{beanAssinarDocumentos.devolverDocumentosAction}"
                            onclick="Richfaces.hideModalPanel('modalPanelEditarDocumento');"
                            value="Encaminhar para Correção" /> <h:commandButton
                            onclick="Richfaces.hideModalPanel('modalPanelEditarDocumento');"
                            value="Fechar" /> <h:commandButton
                            action="#{beanAssinarDocumentos.editarDocumentos}"
                            onclick="Richfaces.hideModalPanel('modalPanelEditarDocumento');"
                            value="Editar Documento" /> </span>
                </div>
            </h:form>
        </rich:modalPanel>

        <rich:modalPanel id="modalPanelCancelarAssinaturaDocumento"
                         width="630" height="160">
            <f:facet name="header">
                <h:outputText value="Favor inserir o motivo do cancelamento" />
            </f:facet>
            <h:form id="frmCancelarAssinaturaDocumento">
                <div>
                    <span class="Padrao"> <h:outputText styleClass="Padrao"
                                  value="Motivo:" /> </span>
                </div>
                <div style="padding-top: 3px;">
                    <span> <h:inputTextarea cols="80" rows="5"  onkeyup="verifyLength(this,1000)"
                                     value="#{beanAssinarDocumentos.anotacaoCancelamento}" /> </span>
                </div>
                <div style="padding-top: 15px;">
                    <span>
                        <a4j:commandButton
                            actionListener="#{beanAssinarDocumentos.cancelarAssinaturaDocumentoAction}"
                            onclick="Richfaces.hideModalPanel('modalPanelCancelarAssinaturaDocumento');"
                            value="Encaminhar para Correção"
                            oncomplete="setTimeout('pesquisar()', 1000);" />
                        <h:commandButton
                            onclick="Richfaces.hideModalPanel('modalPanelCancelarAssinaturaDocumento');"
                            value="Fechar" />
                    </span>
                </div>
            </h:form>
        </rich:modalPanel>

        <jsp:include page="/pages/template/footer.jsp" flush="true" />

    </a4j:page>
</f:view>