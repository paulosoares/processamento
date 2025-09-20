<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />


<script type="text/javascript">
	function aguarde(mostrar, div) {
		if (mostrar == true) {
			document.getElementById(div).innerHTML = '_$tag_________________________________________<font class="Padrao">&nbsp;&nbsp;Aguarde..._$tag__';
		}
	}
	function downloadPecas() {
		 document.getElementById('botaoDonwloadMid').click();
	}
	function pesquisarHandler(valor) {
		document.getElementById(valor).click();
	}

	function caixaAlta(campo) {
		campo.value = campo.value.toUpperCase();
	}

	function pesquisar() {
        document.getElementById('btnPesquisarProcesso').click();
    }

</script>

<f:view>
	<a4j:page pageTitle="::.. Principal ..::">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Expedir Documentos" />
			</jsp:include>
		</h:form>

		<a4j:form id="form" prependId="false">

			<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
				cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0">
				
                    <a4j:outputPanel id="pnlPrincipalPesquisa" ajaxRendered="true">
                        <div class="PainelTituloCriaTexto">
                            <span> Pesquisa: </span>
                        </div>

                        <div style="padding-top: 10px;">
                            <span class="Padrao">
                            	<h:outputText styleClass="Padrao" value='Data da assinatura:' style="margin-left:5px;"/> 
                            	<rich:calendar id="idDataGeracao"
											   value="#{beanExpedirDocumentos.dataAssinaturaDocumento}"
											   datePattern="dd/MM/yyyy" locale="pt_Br" />
								
								<rich:spacer height="10px"/>
								
								<h:selectBooleanCheckbox value="#{beanExpedirDocumentos.buscarApenasSigilosos}"  />
								<h:outputText value="Exibir apenas processos sigilosos"/>			   
											                               
                                <a4j:commandButton styleClass="BotaoPesquisar"
                                                   style="margin-left:15px;" value="Pesquisar"
                                                   id="btnPesquisarProcesso"
                                                   actionListener="#{beanExpedirDocumentos.pesquisarAssinadosAction}"
                                                   ignoreDupResponses="true" reRender="pnlResultadoPesquisa"
                                                   onclick="exibirMsgProcessando(true)"
                                                   oncomplete="exibirMsgProcessando(false);" />
                             </span>
                        </div>
                    </a4j:outputPanel>				

					<a4j:outputPanel ajaxRendered="true" keepTransient="false"
						id="pnlResultadoPesquisa" styleClass="MolduraInterna">
						<c:if test="${not empty beanExpedirDocumentos.listaDocumentos}">
							<hr color="red" align="left" size="1px" width="90%" />
							<rich:dataTable headerClass="DataTableDefaultHeader"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								columnClasses="tres, dezCenter, quinzeCenter, dezCenter, vinteCenter, vinteCenter, tres, sete, cinco, sete, cinco,cinco"
								sortMode="single"
								rows="#{beanExpedirDocumentos.rows}"  
								id="tbDocumentos"
								value="#{beanExpedirDocumentos.listaDocumentos}"
								var="wrappedDocumento"
								binding="#{beanExpedirDocumentos.tabelaDocumentos}">
								<rich:column>
									<f:facet name="header">
										<a4j:commandButton image="../../images/setabaixo.gif"
											onclick="exibirMsgProcessando(true)"
											oncomplete="exibirMsgProcessando(false);"
											actionListener="#{beanExpedirDocumentos.marcarTodosTextos}" />
									</f:facet>
									<h:selectBooleanCheckbox
										onclick="document.getElementById('BotaoAtualizarMarcacao').click();"
										value="#{wrappedDocumento.checked}" />
								</rich:column>


<rich:column
									sortBy="#{wrappedDocumento.wrappedObject.comunicacao.dataEntradaDeslocamentoAtual}">
									<f:facet name="header">
										<h:outputText value="Data de Entrada" />
									</f:facet>
									<h:outputText styleClass="Padrao" style="text-align:center;"
									 	value="#{wrappedDocumento.wrappedObject.comunicacao.dataEntradaDeslocamentoAtual}">
										<f:convertDateTime pattern="dd/MM/yyyy HH:mm"/> 
									</h:outputText>	
								</rich:column>
								
								
								<rich:column
									sortBy="#{wrappedDocumento.wrappedObject.comunicacao.dataAssinaturaExpedirDocumentos}">
									<f:facet name="header">
										<h:outputText value="Data da Assinatura" />
									</f:facet>
									<h:outputText styleClass="Padrao" style="text-align:center;"
										value="#{wrappedDocumento.wrappedObject.comunicacao.dataAssinaturaExpedirDocumentos}">
										<f:convertDateTime pattern="dd/MM/yyyy HH:mm"/> 
									</h:outputText>	
								</rich:column>
								
								<rich:column  sortBy="#{wrappedDocumento.wrappedObject.comunicacao.concatenaAnoNumeracaoUnica}">
									<f:facet name="header">
										<h:outputText value="Nº Documento"
											styleClass="DataTableDocumentoTexto" style="text-align:center"/>
									</f:facet>
									<h:outputText styleClass="Padrao" style="text-align:center;"
										value="#{wrappedDocumento.wrappedObject.comunicacao.concatenaAnoNumeracaoUnica}" />
								</rich:column>
								
								<rich:column
									comparator="#{beanExpedirDocumentos.idProcessualComunicacaoComparator}"
									sortBy="#{wrappedDocumento.wrappedObject.comunicacao.identificacaoProcessual}">
									<f:facet name="header">
										<h:outputText value="Processo"
											styleClass="DataTableDocumentoTexto" />
									</f:facet>
									<h:commandLink styleClass="PadraoLink"
										style="text-align:center;" target="_blank"
										value="#{wrappedDocumento.wrappedObject.comunicacao.identificacaoProcessual}"
										action="#{beanExpedirDocumentos.consultarProcessoDigital}">
										<f:setPropertyActionListener
											target="#{beanExpedirDocumentos.seqObjetoIncidente}"
											value="#{wrappedDocumento.wrappedObject.comunicacao.objetoIncidenteUnico.principal.id}" />
									</h:commandLink>
								</rich:column>

								<rich:column sortBy="#{wrappedDocumento.wrappedObject.comunicacao.nomeMinistroRelatorAtual}">
									<f:facet name="header">
										<h:outputText value="Relator"
											styleClass="DataTableDocumentoTexto" />
									</f:facet>
									<h:outputText styleClass="Padrao" style="text-align:center;"
										value="#{wrappedDocumento.wrappedObject.comunicacao.nomeMinistroRelatorAtual}" />
								</rich:column>

                                <rich:column sortBy="#{wrappedDocumento.wrappedObject.comunicacao.confidencialidade}">
                                    <f:facet name="header">
                                        <h:outputText value="Confidencialidade"
                                                      styleClass="DataTableDocumentoTexto" />
                                    </f:facet>
                                            <h:outputText styleClass="Padrao" style="text-align:center;"
                                                  value="#{wrappedDocumento.wrappedObject.comunicacao.comunicacaoIncidente[0].objetoIncidente.nivelSigilo}" />
                                </rich:column>

								<rich:column sortBy="#{wrappedDocumento.wrappedObject.comunicacao.dscNomeDocumento}">
									<f:facet name="header">
										<h:outputText value="Nome Documento"
											styleClass="DataTableDocumentoTexto" />
									</f:facet>
									<h:outputText styleClass="Padrao" style="text-align:center;"
										value="#{wrappedDocumento.wrappedObject.comunicacao.dscNomeDocumento}" />
								</rich:column>

								<rich:column sortBy="#{wrappedDocumento.wrappedObject.comunicacao.modeloComunicacao.tipoComunicacao.descricao}">
									<f:facet name="header">
										<h:outputText value="Tipo Modelo" />
									</f:facet>
									<h:outputText styleClass="Padrao" style="text-align:center;"
										value="#{wrappedDocumento.wrappedObject.comunicacao.modeloComunicacao.tipoComunicacao.descricao}" />
								</rich:column>

								<rich:column style="text-align: left">
									<f:facet name="header">
										<h:outputText value="PDF" />
									</f:facet>
									<h:outputLink 
										rendered="#{wrappedDocumento.wrappedObject.possuiPDF}"
										value="#{wrappedDocumento.wrappedObject.linkPDF}">
										<h:graphicImage style="text-align: right" value="/images/pdf.png">											
										</h:graphicImage>
									</h:outputLink>
									<rich:toolTip  followMouse="true" direction="top-left" style="width:800px"	
										rendered="#{wrappedDocumento.wrappedObject.possuiPDF}"												                                            
	                                    hideDelay="20"
	                                    showDelay="250">    		                                             
	                             		<h:outputText value="#{beanExpedirDocumentos.recuperarTextoDocumento}"/>
		                            </rich:toolTip>		
		                            <h:outputText value="  " />	
									<h:outputLink
										rendered="#{not empty wrappedDocumento.wrappedObject.comunicacao.pecasProcessoEletronico}"
										value="#{wrappedDocumento.wrappedObject.linkPDFUnico}" title="Impressão do expediente e das peças vinculadas">
										<h:graphicImage value="/images/printer1.png"></h:graphicImage>
									</h:outputLink>
									
									<!-- 
									<h:outputLink 
										rendered="#{not empty wrappedDocumento.wrappedObject.comunicacao.pecasProcessoEletronico}"
										value=""
										onclick="downloadPecas()" title="Audios">
										
										<h:graphicImage value="/images/video.png" width="16" height="18" ></h:graphicImage>
									</h:outputLink>
									 -->
									
								<h:outputText value="  " />

                                                    <a4j:commandLink  id="botaoDonwloadMid1" value="" rendered="#{wrappedDocumento.wrappedObject.linkPecaUnico ne '#' && wrappedDocumento.wrappedObject.documentoComunicacaoResult.possuiArquivoMidia}"
                                                   styleClass="link" immediate="true"
                                                   title="Download da Comunicação e das peças vinculadas à comunicação"
                                                   onclick="exibirMsgProcessando(true);location.href='#{wrappedDocumento.wrappedObject.linkPecaUnico}'"
                                                   oncomplete="exibirMsgProcessando(false);">
                                                    <h:graphicImage url="/images/zip_2.png" width="16" height="16"/></a4j:commandLink>
                                                   
                                                   
                                                   <a4j:commandLink  id="botaoDonwloadMid2" value="" rendered="#{wrappedDocumento.wrappedObject.linkPecaUnico eq '#' && wrappedDocumento.wrappedObject.documentoComunicacaoResult.possuiArquivoMidia}"
                                                   styleClass="link" immediate="true"
                                                   title="Arquivo excedeu o limite para download. Favor utilizar o STFDigital"
                                                   onclick="exibirMsgProcessando(true);location.href='#"
                                                   oncomplete="exibirMsgProcessando(false);">
                                                    <h:graphicImage url="/images/op-nao-permitida.png" width="16" height="16"/></a4j:commandLink>
                                                   
                                                   
                                                   	<h:outputText value="  " />
                                                <!-- 
                                                   <a4j:commandLink id="botaoDonwloadMid3" value="" rendered="#{wrappedDocumento.wrappedObject.documentoComunicacaoResult.possuiArquivoMidia}"
                                                   styleClass="link" immediate="true"
                                                   title="Download da comunicação e de Todas as peças do processo"
                                                   onclick="exibirMsgProcessando(true);location.href='#{wrappedDocumento.wrappedObject.linkPecaTodas}'"
                                                   oncomplete="exibirMsgProcessando(false);">
													 <h:graphicImage url="/images/zip_3.png" width="16" height="16"/></a4j:commandLink>
                                                    -->
                                                   
                                                   
                                                   <a4j:commandLink id="botaoDonwloadMid4" value="" rendered="#{wrappedDocumento.wrappedObject.linkPecaTodas ne '#' && wrappedDocumento.wrappedObject.documentoComunicacaoResult.possuiArquivoMidia}"
                                                   styleClass="link" immediate="true"
                                                   disabled="#{wrappedDocumento.wrappedObject.linkPecaTodas eq '#'}"
                                                   title="Download da comunicação e de Todas as peças do processo"
                                                   onclick="exibirMsgProcessando(true);location.href='#{wrappedDocumento.wrappedObject.linkPecaTodas}'"
                                                   oncomplete="exibirMsgProcessando(false);">
													 <h:graphicImage url="/images/zip_3.png" width="16" height="16"/></a4j:commandLink>
													 
													 
													<a4j:commandLink id="botaoDonwloadMid5" value="" rendered="#{wrappedDocumento.wrappedObject.linkPecaTodas eq '#' && wrappedDocumento.wrappedObject.documentoComunicacaoResult.possuiArquivoMidia}"
                                                   styleClass="link" immediate="true"
                                                   disabled="#{wrappedDocumento.wrappedObject.linkPecaTodas eq '#'}"
                                                   title="Arquivo excedeu o limite para download. Favor utilizar o STFDigital"
                                                   onclick="exibirMsgProcessando(true);location.href='#'"
                                                   oncomplete="exibirMsgProcessando(false);">
													 <h:graphicImage url="/images/op-nao-permitida.png" width="16" height="16"/></a4j:commandLink>
													 
													 
													 
                                                   
								</rich:column>
	
								<rich:column sortBy="#{wrappedDocumento.wrappedObject.comunicacao.faseAtual}">
									<f:facet name="header">
										<h:outputText value="Situação" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{wrappedDocumento.wrappedObject.comunicacao.faseAtual}" />
								</rich:column>

                                <rich:column>
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
                                                    <h:outputText value="Arquivo" />
                                                </f:facet>
                                                
                                                
                                                
                                                
                                                <h:commandLink action="#{assinadorBaseBean.report}" rendered="#{valor.pecaProcessoEletronico.descricaoTipoArquivo=='IND'}">
                                                    <f:setPropertyActionListener value="#{valor}"
                                                                                 target="#{assinadorBaseBean.valor}" />
                                                    <h:graphicImage value="/images/pdf.png"></h:graphicImage>
                                                </h:commandLink>
                                                
                                             
                                                  <h:commandLink action="#{assinadorBaseBean.report}" rendered="#{valor.pecaProcessoEletronico.descricaoTipoArquivo=='DOC'}">
                                                    <f:setPropertyActionListener value="#{valor}"
                                                                                 target="#{assinadorBaseBean.valor}" />
                                                    <h:graphicImage value="/images/pdf.png"></h:graphicImage>
                                                </h:commandLink>
                                              
                                                     <h:commandLink action="#{assinadorBaseBean.report}" rendered="#{valor.pecaProcessoEletronico.descricaoTipoArquivo == 'VID'}">
                                                    <f:setPropertyActionListener value="#{valor}"
                                                                                 target="#{assinadorBaseBean.valor}" />
                                                    <h:graphicImage value="/images/video_1.png" width="16" height="18"></h:graphicImage>
                                                </h:commandLink>
                                                 <h:commandLink action="#{assinadorBaseBean.report}" rendered="#{valor.pecaProcessoEletronico.descricaoTipoArquivo == 'AUD'}">
                                                    <f:setPropertyActionListener value="#{valor}"
                                                                                 target="#{assinadorBaseBean.valor}" />
                                                    <h:graphicImage value="/images/audio_1.png" width="16" height="18" ></h:graphicImage>
                                                </h:commandLink>
            
                                            </h:column>
                                        </h:dataTable>                                                
                                    </rich:toolTip>
                                </rich:column>
                                
              

                                <rich:column sortBy="#{wrappedDocumento.wrappedObject.comunicacao.nomeUsuarioCriacao}" rendered="#{! beanExpedirDocumentos.salaOficiais}">
                                    <f:facet name="header">
                                        <h:outputText value="Usuário" />
                                    </f:facet>
                                    <h:outputText styleClass="Padrao" style="text-align:center;"
                                                  value="#{wrappedDocumento.wrappedObject.comunicacao.nomeUsuarioCriacao}" />
                                </rich:column>
									<rich:column  style="width:10%" sortBy="#{wrappedDocumento.wrappedObject.comunicacao.nomeUsuarioResponsavel}" sortOrder="ASCENDING" rendered="#{beanExpedirDocumentos.salaOficiais}"> 
                                    <f:facet name="header">
                                        <h:outputText value="Atribuído para " />
                                    </f:facet>
                                    <h:outputText styleClass="Padrao" style="text-align:center;"
                                                  value="#{wrappedDocumento.wrappedObject.comunicacao.nomeUsuarioResponsavel}" />
                                </rich:column>

							 <rich:column  style="width:5%" sortBy="#{wrappedDocumento.wrappedObject.comunicacao.dataAtribuicao}" sortOrder="ASCENDING" rendered="#{beanExpedirDocumentos.salaOficiais}"> 
                                    <f:facet name="header">
                                        <h:outputText value="Atribuído em " />
                                    </f:facet>
                                        <h:outputText styleClass="Padrao" style="text-align:center;"
                                                  value="#{wrappedDocumento.wrappedObject.comunicacao.dataAtribuicao}">
                                        <f:convertDateTime pattern="dd/MM/yyyy HH:mm"/>  
                                    </h:outputText>
                                </rich:column>


                                <rich:column>
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
								
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Observacao" />
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
                            	<h:inputText value="#{beanExpedirDocumentos.rows}" 
                            				 style="margin-left:10px; width: 30px !important;"
                            				 immediate="true">
                            		<a4j:support event="onblur" reRender="pnlResultadoPesquisa"/>
                            	</h:inputText>
                            </h:panelGroup>   
							<rich:datascroller for="tbDocumentos"
							                   maxPages="#{beanExpedirDocumentos.maxPages}"  
							                   page="1"
							                   id="scroll" align="left" />							

							<security:authorize ifAnyGranted="RS_FINALIZAR_TEXTOS,RS_MASTER_PROCESSAMENTO">
                               
                                                    <a4j:commandButton id="botaoAtribuir" value="Atribuir" rendered="#{beanAssinarDocumentos.salaOficiais}"
                                                
                                                   styleClass="BotaoPadrao" immediate="true" disabled="#{not beanExpedirDocumentos.selecionadosTela}" title="#{(beanExpedirDocumentos.selecionadosTela) ? 'Atribui os documentos a um responsável' : 'Selecione um ou mais documentos para prosseguir'}"
                                                   onclick="exibirMsgProcessando(true);"
                                                   oncomplete="exibirMsgProcessando(false); Richfaces.showModalPanel('modalConfirmaAtribuir');"
                                                   reRender="modalConfirmaAtribuir" />
                                                   
                                                   
                                                   
                            </security:authorize>
                            



							<security:authorize
								ifAnyGranted="RS_EXPEDICAO_TEXTOS,RS_MASTER_PROCESSAMENTO">
								<a4j:commandButton styleClass="BotaoPadrao" rendered="false"
									actionListener="#{beanExpedirDocumentos.expedirDocumentosAction}"
									onclick="exibirMsgProcessando(true)" 
									oncomplete="exibirMsgProcessando(false);"
									reRender="pnlResultadoPesquisa1" value="Expedir" />
							</security:authorize>


							<security:authorize
								ifAnyGranted="RS_EXPEDICAO_TEXTOS,RS_MASTER_PROCESSAMENTO">
								<a4j:commandButton styleClass="BotaoPadrao" value="Expedir"  disabled="#{not beanExpedirDocumentos.selecionadosTela}" title="#{(beanExpedirDocumentos.selecionadosTela) ? 'Expede os documentos selecionados' : 'Selecione um ou mais documentos para prosseguir'}"
									onclick="exibirMsgProcessando(true);"
									reRender="pnlResultadoPesquisa,modalConfirmaExpedicao" 
									oncomplete="exibirMsgProcessando(false); Richfaces.showModalPanel('modalConfirmaExpedicao');"
									 />
							</security:authorize>



							<a4j:commandButton id="botaoEncaminharNovo" value="Encaminhar"
								actionListener="#{beanExpedirDocumentos.procurarSetorRelatorAction}"
								styleClass="BotaoPadrao" immediate="true"
								onclick="exibirMsgProcessando(true);"  disabled="#{not beanExpedirDocumentos.selecionadosTela}" title="#{(beanExpedirDocumentos.selecionadosTela) ? 'Encaminha os documentos selecionados' : 'Selecione um ou mais documentos para prosseguir'}"
								oncomplete="exibirMsgProcessando(false); Richfaces.showModalPanel('modalConfirmaEncaminhar');"
								reRender="modalConfirmaEncaminhar" />


							<a4j:commandButton id="botaoEncaminhar" value="Encaminhar"  rendered="false"
								actionListener="#{beanExpedirDocumentos.procurarSetorRelatorAction}"
								styleClass="BotaoPadrao" immediate="true"
								onclick="exibirMsgProcessando(true);"
								oncomplete="exibirMsgProcessando(false); Richfaces.showModalPanel('modalPanelEncaminharDocumento');"
								reRender="modalPanelEncaminharDocumento" />
								
							<h:commandButton id="botaoImprimir" value="Imprimir"  disabled="#{not beanExpedirDocumentos.selecionadosTela}" title="#{(beanExpedirDocumentos.selecionadosTela) ? 'Imprime os documentos selecionados' : 'Selecione um ou mais documentos para prosseguir'}"
                                                   actionListener="#{beanExpedirDocumentos.imprimirDocumentosAction}"
                                                   styleClass="BotaoPadrao" />
								
							<security:authorize ifAnyGranted="RS_FINALIZAR_TEXTOS,RS_MASTER_PROCESSAMENTO">
								<a4j:commandButton id="botaoFinalizar" value="Finalizar" rendered="false"
									actionListener="#{beanExpedirDocumentos.finalizarDocumentosAction}"
									styleClass="BotaoPadrao" onclick="exibirMsgProcessando(true);"
									oncomplete="exibirMsgProcessando(false);" reRender="pnlResultadoPesquisa" />
							</security:authorize>

							<security:authorize
								ifAnyGranted="RS_EXPEDICAO_TEXTOS,RS_MASTER_PROCESSAMENTO">
								<a4j:commandButton styleClass="BotaoPadrao" value="Finalizar"
									onclick="exibirMsgProcessando(true);"  disabled="#{not beanExpedirDocumentos.selecionadosTela}" title="#{(beanExpedirDocumentos.selecionadosTela) ? 'Finaliza os documentos selecionados' : 'Selecione um ou mais documentos para prosseguir'}"
									reRender="pnlResultadoPesquisa,modalConfirmaFinalizar"
									oncomplete="exibirMsgProcessando(false); Richfaces.showModalPanel('modalConfirmaFinalizar');"
									 />
							</security:authorize>


							<a4j:commandButton styleClass="BotaoOculto"
								id="BotaoAtualizarMarcacao"
								actionListener="#{beanExpedirDocumentos.atualizarMarcacao}" />

						</c:if>
					</a4j:outputPanel>

					<a4j:commandButton styleClass="BotaoOculto" id="btnAtualizaSessao"
						actionListener="#{beanExpedirDocumentos.atualizaSessaoAction}" />
					<a4j:commandButton id="idBotaoPesquisar" value="Pesquisar"
						styleClass="BotaoOculto"
						actionListener="#{beanExpedirDocumentos.pesquisarAssinadosAction}"
						reRender="pnlResultadoPesquisa"
						onclick="exibirMsgProcessando(true)"
						oncomplete="exibirMsgProcessando(false);" />
						
					<a4j:commandButton styleClass="BotaoOculto"
								id="exibePDFsJuntadosId"
								actionListener="#{beanExpedirDocumentos.exibePDFsJuntadosAction}"/>

				</h:panelGrid>
			</h:panelGrid>
		</a4j:form>



		<rich:modalPanel id="modalPanelAtribuir" width="630" height="100"
			keepVisualState="true">
			<f:facet name="header">
				<h:outputText value="Atribuir Documento" />
			</f:facet>
			<a4j:form prependId="false" rendered="#{beanExpedirDocumentos.selecionadosTela}">

				<div>
					<span class="Padrao"> <h:outputText styleClass="Padrao"
							value="Responsável:" /> <h:selectOneMenu
							style="margin-left:10px;"
							value="#{beanExpedirDocumentos.idUsuarioAtribuicao}"
							styleClass="Input">
							<f:selectItems
								value="#{beanExpedirDocumentos.itensUsuariosSetor}" />
						</h:selectOneMenu>
					</span>
				</div>

				<div style="padding-top: 20px;">
					<span> <a4j:commandButton styleClass="BotaoPadrao"
							actionListener="#{beanExpedirDocumentos.atribuirDocumentosAction}"
							onclick="exibirMsgProcessando(true); Richfaces.hideModalPanel('modalPanelAtribuir');"
							value="OK"
							 oncomplete="exibirMsgProcessando(false); Richfaces.hideModalPanel('modalPanelAtribuir');setTimeout('pesquisar()', 500);"/>
					</span> <span> <h:commandButton
							onclick="Richfaces.hideModalPanel('modalPanelAtribuir');"
							styleClass="BotaoPadrao" value="Fechar" />
					</span>
				</div>

			</a4j:form>
			
			<a4j:form prependId="false" rendered="#{!beanExpedirDocumentos.selecionadosTela}" >

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







		<rich:modalPanel id="modalConfirmaExpedicao" width="900" height="400"
			keepVisualState="true">
			<f:facet name="header">
				<h:outputText value="Confirmação de Documentos para Expedição" />
			</f:facet>

			<rich:scrollableDataTable var="docSelecionadosExpedicao"
				id="tblValor23" styleClass="divPad"
				rendered="#{beanExpedirDocumentos.selecionadosTela}"
				style="scrollable" width="100%" height="75%"
				value="#{beanExpedirDocumentos.listaSelecionados}"
				headerClass="DataTableDefaultHeader"
				footerClass="DataTableDefaultFooter"
				rowClasses="DataTableRow, DataTableRow2"
				columnClasses="tres, dezCenter, quinzeCenter, dezCenter, vinteCenter, vinteCenter, tres, sete, cinco, sete, cinco,cinco">

				<div class="divPad">
					<rich:column width="150">
						<f:facet name="header">
							<h:outputText value="Assinatura" />
						</f:facet>

						<h:outputText styleClass="Padrao" style="text-align:center;"
							value="#{docSelecionadosExpedicao.comunicacao.dataAssinaturaExpedirDocumentos}">
							<f:convertDateTime pattern="dd/MM/yyyy HH:mm" />
						</h:outputText>

					</rich:column>
					
					<rich:column width="70">
						<f:facet name="header">
							<h:outputText value="Nº Documento" />
						</f:facet>

						<h:outputText
							value="#{docSelecionadosExpedicao.documentoComunicacao.comunicacao.concatenaAnoNumeracaoUnica}"
							styleClass="Padrao" />
					</rich:column>

					<rich:column width="100">
						<f:facet name="header">
							<h:outputText value="Processo " />
						</f:facet>
						<h:outputText
							value="#{docSelecionadosExpedicao.documentoComunicacao.comunicacao.identificacaoProcessual}"
							styleClass="Padrao" />

					</rich:column>
					
					<rich:column width="300">
						<f:facet name="header">
							<h:outputText value="Nome do Documento" />
						</f:facet>
						<h:outputText
							value="#{docSelecionadosExpedicao.documentoComunicacao.comunicacao.dscNomeDocumento}"
							styleClass="Padrao" />
					</rich:column>

					<rich:column width="150">
						<f:facet name="header">
							<h:outputText value="Relator" />
						</f:facet>

						<h:outputText
							value="#{docSelecionadosExpedicao.documentoComunicacao.comunicacao.nomeMinistroRelatorAtual}"
							styleClass="Padrao" />
					</rich:column>

					<rich:column width="100">
						<f:facet name="header">
							<h:outputText value="Tipo Modelo" />
						</f:facet>
						<h:outputText
							value="#{docSelecionadosExpedicao.documentoComunicacao.comunicacao.modeloComunicacao.tipoComunicacao.descricao}"
							styleClass="Padrao" />
					</rich:column>

					<rich:column width="100">
						<f:facet name="header">
							<h:outputText value="Confidencialidade" />
						</f:facet>
						<h:outputText
							value="#{(docSelecionadosExpedicao.documentoComunicacao.comunicacao.confidencialidade eq '') ? 'Público' : docSelecionadosExpedicao.documentoComunicacao.comunicacao.confidencialidade}"
							styleClass="Padrao" />
					</rich:column>

				</div>

			</rich:scrollableDataTable>
<div style="padding-top: 10px;padding-bottom: 10px;">
				 <h:outputText style="color:red;font-size:10pt"   styleClass="Padrao" value="ATENÇÃO: Ao confirmar, será(ão) expedido(s) #{fn:length(beanExpedirDocumentos.listaSelecionados)} documento(s). " /> 
			</div>
			<a4j:form prependId="false"
				rendered="#{beanExpedirDocumentos.selecionadosTela}">
				<div style="padding-top: 20px;">
					<span> <a4j:commandButton styleClass="BotaoPadrao"
							actionListener="#{beanExpedirDocumentos.expedirDocumentosAction}"
							onclick="exibirMsgProcessando(true); Richfaces.hideModalPanel('modalConfirmaExpedicao');"
							value="Confirmar"
							oncomplete="exibirMsgProcessando(false); Richfaces.hideModalPanel('modalConfirmaExpedicao');setTimeout('pesquisar()', 500);" />
					</span> <span> <h:commandButton
							onclick="Richfaces.hideModalPanel('modalConfirmaExpedicao');"
							styleClass="BotaoPadrao" value="Cancelar" />
					</span>
				</div>

			</a4j:form>

			<a4j:form prependId="false"
				rendered="#{!beanExpedirDocumentos.selecionadosTela}">

				<div>
					<span class="Padrao"> <h:outputText styleClass="Padrao"
							value="Favor selecionar ao menos um documento para proceder com a operação." />
					</span>
				</div>

				<div style="padding-top: 20px;">
					<span> <h:commandButton
							onclick="Richfaces.hideModalPanel('modalConfirmaExpedicao');"
							styleClass="BotaoPadrao" value="Fechar" />
					</span>
				</div>

			</a4j:form>

		</rich:modalPanel>



		<rich:modalPanel id="modalConfirmaFinalizar" width="900" height="400"
			keepVisualState="true">
			<f:facet name="header">
				<h:outputText value="Confirmação de Documentos para Finalizar" />
			</f:facet>

			<rich:scrollableDataTable var="docSelecionadosFinalizar"
				id="tblValor24" styleClass="divPad"
				rendered="#{beanExpedirDocumentos.selecionadosTela}"
				style="scrollable" width="100%" height="75%"
				value="#{beanExpedirDocumentos.listaSelecionados}"
				headerClass="DataTableDefaultHeader"
				footerClass="DataTableDefaultFooter"
				rowClasses="DataTableRow, DataTableRow2"
				columnClasses="tres, dezCenter, quinzeCenter, dezCenter, vinteCenter, vinteCenter, tres, sete, cinco, sete, cinco,cinco">

				<div class="divPad">
					<rich:column width="150">
						<f:facet name="header">
							<h:outputText value="Assinatura" />
						</f:facet>

						<h:outputText styleClass="Padrao" style="text-align:center;"
							value="#{docSelecionadosFinalizar.comunicacao.dataAssinaturaExpedirDocumentos}">
							<f:convertDateTime pattern="dd/MM/yyyy HH:mm" />
						</h:outputText>

					</rich:column>
					
					<rich:column width="70">
						<f:facet name="header">
							<h:outputText value="Nº Documento" />
						</f:facet>

						<h:outputText
							value="#{docSelecionadosFinalizar.documentoComunicacao.comunicacao.concatenaAnoNumeracaoUnica}"
							styleClass="Padrao" />
					</rich:column>

					<rich:column width="100">
						<f:facet name="header">
							<h:outputText value="Processo " />
						</f:facet>
						<h:outputText
							value="#{docSelecionadosFinalizar.documentoComunicacao.comunicacao.identificacaoProcessual}"
							styleClass="Padrao" />

					</rich:column>
					
					<rich:column width="300">
						<f:facet name="header">
							<h:outputText value="Nome do Documento" />
						</f:facet>
						<h:outputText
							value="#{docSelecionadosFinalizar.documentoComunicacao.comunicacao.dscNomeDocumento}"
							styleClass="Padrao" />
					</rich:column>

					<rich:column width="150">
						<f:facet name="header">
							<h:outputText value="Relator" />
						</f:facet>

						<h:outputText
							value="#{docSelecionadosFinalizar.documentoComunicacao.comunicacao.nomeMinistroRelatorAtual}"
							styleClass="Padrao" />
					</rich:column>

					<rich:column width="100">
						<f:facet name="header">
							<h:outputText value="Tipo Modelo" />
						</f:facet>
						<h:outputText
							value="#{docSelecionadosFinalizar.documentoComunicacao.comunicacao.modeloComunicacao.tipoComunicacao.descricao}"
							styleClass="Padrao" />
					</rich:column>

					<rich:column width="100">
						<f:facet name="header">
							<h:outputText value="Confidencialidade" />
						</f:facet>
						<h:outputText
							value="#{(docSelecionadosFinalizar.documentoComunicacao.comunicacao.confidencialidade eq '') ? 'Público' : docSelecionadosFinalizar.documentoComunicacao.comunicacao.confidencialidade}"
							styleClass="Padrao" />
					</rich:column>

				</div>

			</rich:scrollableDataTable>
<div style="padding-top: 10px;padding-bottom: 10px;">
				 <h:outputText style="color:red;font-size:10pt"  styleClass="Padrao" value="ATENÇÃO: Ao confirmar, será(ão) finalizado(s) #{fn:length(beanExpedirDocumentos.listaSelecionados)} documento(s). " /> 
			</div>
			<a4j:form prependId="false"
				rendered="#{beanExpedirDocumentos.selecionadosTela}">
				<div style="padding-top: 20px;">
					<span> <a4j:commandButton styleClass="BotaoPadrao"
							actionListener="#{beanExpedirDocumentos.finalizarDocumentosAction}"
							onclick="exibirMsgProcessando(true); Richfaces.hideModalPanel('modalConfirmaFinalizar');"
							value="Confirmar"
							oncomplete="exibirMsgProcessando(false); Richfaces.hideModalPanel('modalConfirmaFinalizar');setTimeout('pesquisar()', 500);" />
					</span> <span> <h:commandButton
							onclick="Richfaces.hideModalPanel('modalConfirmaFinalizar');"
							styleClass="BotaoPadrao" value="Cancelar" />
					</span>
				</div>

			</a4j:form>

			<a4j:form prependId="false"
				rendered="#{!beanExpedirDocumentos.selecionadosTela}">

				<div>
					<span class="Padrao"> <h:outputText styleClass="Padrao"
							value="Favor selecionar ao menos um documento para proceder com a operação." />
					</span>
				</div>

				<div style="padding-top: 20px;">
					<span> <h:commandButton
							onclick="Richfaces.hideModalPanel('modalConfirmaFinalizar');"
							styleClass="BotaoPadrao" value="Fechar" />
					</span>
				</div>

			</a4j:form>

		</rich:modalPanel>



		<rich:modalPanel id="modalConfirmaEncaminhar" width="900" height="400"
			keepVisualState="true">
			<f:facet name="header">
				<h:outputText value="Confirmação de Documentos para Encaminhar" />
			</f:facet>

			<rich:scrollableDataTable var="docSelecionadosEncaminhar"
				id="tblValor26" styleClass="divPad"
				rendered="#{beanExpedirDocumentos.selecionadosTela}"
				style="scrollable" width="100%" height="75%"
				value="#{beanExpedirDocumentos.listaSelecionados}"
				headerClass="DataTableDefaultHeader"
				footerClass="DataTableDefaultFooter"
				rowClasses="DataTableRow, DataTableRow2"
				columnClasses="tres, dezCenter, quinzeCenter, dezCenter, vinteCenter, vinteCenter, tres, sete, cinco, sete, cinco,cinco">

				<div class="divPad">
					<rich:column width="150">
						<f:facet name="header">
							<h:outputText value="Assinatura" />
						</f:facet>

						<h:outputText styleClass="Padrao" style="text-align:center;"
							value="#{docSelecionadosEncaminhar.comunicacao.dataAssinaturaExpedirDocumentos}">
							<f:convertDateTime pattern="dd/MM/yyyy HH:mm" />
						</h:outputText>

					</rich:column>
					
					<rich:column width="70">
						<f:facet name="header">
							<h:outputText value="Nº Documento" />
						</f:facet>

						<h:outputText
							value="#{docSelecionadosEncaminhar.documentoComunicacao.comunicacao.concatenaAnoNumeracaoUnica}"
							styleClass="Padrao" />
					</rich:column>

					<rich:column width="100">
						<f:facet name="header">
							<h:outputText value="Processo " />
						</f:facet>
						<h:outputText
							value="#{docSelecionadosEncaminhar.documentoComunicacao.comunicacao.identificacaoProcessual}"
							styleClass="Padrao" />

					</rich:column>
					
					<rich:column width="300">
						<f:facet name="header">
							<h:outputText value="Nome do Documento" />
						</f:facet>
						<h:outputText
							value="#{docSelecionadosEncaminhar.documentoComunicacao.comunicacao.dscNomeDocumento}"
							styleClass="Padrao" />
					</rich:column>

					<rich:column width="150">
						<f:facet name="header">
							<h:outputText value="Relator" />
						</f:facet>

						<h:outputText
							value="#{docSelecionadosEncaminhar.documentoComunicacao.comunicacao.nomeMinistroRelatorAtual}"
							styleClass="Padrao" />
					</rich:column>

					<rich:column width="100">
						<f:facet name="header">
							<h:outputText value="Tipo Modelo" />
						</f:facet>
						<h:outputText
							value="#{docSelecionadosEncaminhar.documentoComunicacao.comunicacao.modeloComunicacao.tipoComunicacao.descricao}"
							styleClass="Padrao" />
					</rich:column>

					<rich:column width="100">
						<f:facet name="header">
							<h:outputText value="Confidencialidade" />
						</f:facet>
						<h:outputText
							value="#{(docSelecionadosEncaminhar.documentoComunicacao.comunicacao.confidencialidade eq '') ? 'Público' : docSelecionadosEncaminhar.documentoComunicacao.comunicacao.confidencialidade}"
							styleClass="Padrao" />
					</rich:column>

				</div>

			</rich:scrollableDataTable>
			
			<div style="padding-top: 10px;padding-bottom: 10px;">
				 <h:outputText style="color:red;font-size:10pt"   styleClass="Padrao" value="ATENÇÃO: Ao confirmar, será(ão) encaminhado(s) #{fn:length(beanExpedirDocumentos.listaSelecionados)} documento(s) para o setor selecionado. " /> 
			</div>
			
			<a4j:form prependId="false"  rendered="#{beanExpedirDocumentos.selecionadosTela}">

			
				<div>
					<span class="Padrao"> <h:outputText styleClass="Padrao"
							value="Setor de Destino:" /> <h:selectOneMenu
							style="margin-left:10px;"
							value="#{beanExpedirDocumentos.idSetorDestino}"
							styleClass="Input">
							<f:selectItems
								value="#{beanExpedirDocumentos.itensSetoresDestino}" />
						</h:selectOneMenu> </span>
				</div>

				<div style="padding-top: 10px;">
					<span> <a4j:commandButton styleClass="BotaoPadrao"
							actionListener="#{beanExpedirDocumentos.encaminharDocumentosAction}"
							onclick="exibirMsgProcessando(true);Richfaces.hideModalPanel('modalConfirmaEncaminhar');"
							value="Confirmar" oncomplete="exibirMsgProcessando(false);" /> 
							
							</span> <span> <h:commandButton
							onclick="Richfaces.hideModalPanel('modalConfirmaEncaminhar');"
							styleClass="BotaoPadrao" value="Cancelar" /> </span>
				</div>
			</a4j:form>
			
			<a4j:form prependId="false" rendered="#{!beanExpedirDocumentos.selecionadosTela}" >

				<div>
					<span class="Padrao"> <h:outputText styleClass="Padrao"
							value="Favor selecionar ao menos um documento para proceder com a operação." /> 
					</span>
				</div>

				<div style="padding-top: 20px;">
					 <span> <h:commandButton
							onclick="Richfaces.hideModalPanel('modalConfirmaEncaminhar');"
							styleClass="BotaoPadrao" value="Fechar" />
					</span>
				</div>

			</a4j:form>
			

		</rich:modalPanel>

		<rich:modalPanel id="modalConfirmaAtribuir" width="900" height="400"
			keepVisualState="true">
			<f:facet name="header">
				<h:outputText value="Confirmação de Documentos para Abrituição" />
			</f:facet>

			<rich:scrollableDataTable var="docSelecionadosAtribuir"
				id="tblValor28" styleClass="divPad"
				rendered="#{beanExpedirDocumentos.selecionadosTela}"
				style="scrollable" width="100%" height="70%"
				value="#{beanExpedirDocumentos.listaSelecionados}"
				headerClass="DataTableDefaultHeader"
				footerClass="DataTableDefaultFooter"
				rowClasses="DataTableRow, DataTableRow2"
				columnClasses="tres, dezCenter, quinzeCenter, dezCenter, vinteCenter, vinteCenter, tres, sete, cinco, sete, cinco,cinco">

				<div class="divPad">
					<rich:column width="150">
						<f:facet name="header">
							<h:outputText value="Assinatura" />
						</f:facet>

						<h:outputText styleClass="Padrao" style="text-align:center;"
							value="#{docSelecionadosAtribuir.comunicacao.dataAssinaturaExpedirDocumentos}">
							<f:convertDateTime pattern="dd/MM/yyyy HH:mm" />
						</h:outputText>

					</rich:column>
					
					<rich:column width="70">
						<f:facet name="header">
							<h:outputText value="Nº Documento" />
						</f:facet>

						<h:outputText
							value="#{docSelecionadosAtribuir.documentoComunicacao.comunicacao.concatenaAnoNumeracaoUnica}"
							styleClass="Padrao" />
					</rich:column>

					<rich:column width="100">
						<f:facet name="header">
							<h:outputText value="Processo " />
						</f:facet>
						<h:outputText
							value="#{docSelecionadosAtribuir.documentoComunicacao.comunicacao.identificacaoProcessual}"
							styleClass="Padrao" />

					</rich:column>
					
					<rich:column width="300">
						<f:facet name="header">
							<h:outputText value="Nome do Documento" />
						</f:facet>
						<h:outputText
							value="#{docSelecionadosAtribuir.documentoComunicacao.comunicacao.dscNomeDocumento}"
							styleClass="Padrao" />
					</rich:column>

					<rich:column width="150">
						<f:facet name="header">
							<h:outputText value="Relator" />
						</f:facet>

						<h:outputText
							value="#{docSelecionadosAtribuir.documentoComunicacao.comunicacao.nomeMinistroRelatorAtual}"
							styleClass="Padrao" />
					</rich:column>

					<rich:column width="100">
						<f:facet name="header">
							<h:outputText value="Tipo Modelo" />
						</f:facet>
						<h:outputText
							value="#{docSelecionadosAtribuir.documentoComunicacao.comunicacao.modeloComunicacao.tipoComunicacao.descricao}"
							styleClass="Padrao" />
					</rich:column>

					<rich:column width="100">
						<f:facet name="header">
							<h:outputText value="Confidencialidade" />
						</f:facet>
						<h:outputText
							value="#{(docSelecionadosAtribuir.documentoComunicacao.comunicacao.confidencialidade eq '') ? 'Público' : docSelecionadosAtribuir.documentoComunicacao.comunicacao.confidencialidade}"
							styleClass="Padrao" />
					</rich:column>

				</div>

			</rich:scrollableDataTable>
						<div style="padding-top: 10px;padding-bottom: 10px;">
				 <h:outputText style="color:red;font-size:10pt"   styleClass="Padrao" value="ATENÇÃO: Ao confirmar, será(ão) atribuído(s) #{fn:length(beanExpedirDocumentos.listaSelecionados)} documento(s) para o usuário selecionado. " /> 
			</div>	
						<a4j:form prependId="false" rendered="#{beanExpedirDocumentos.selecionadosTela}">

				<div style="padding-top: 20px;">
					<span class="Padrao"> <h:outputText styleClass="Padrao"
							value="Responsável:" /> <h:selectOneMenu
							style="margin-left:10px;"
							value="#{beanExpedirDocumentos.idUsuarioAtribuicao}"
							styleClass="Input">
							<f:selectItems
								value="#{beanExpedirDocumentos.itensUsuariosSetor}" />
						</h:selectOneMenu>
					</span>
				</div>

				<div style="padding-top: 20px;">
					<span> <a4j:commandButton styleClass="BotaoPadrao"
							actionListener="#{beanExpedirDocumentos.atribuirDocumentosAction}"
							onclick="exibirMsgProcessando(true); Richfaces.hideModalPanel('modalConfirmaAtribuir');"
							value="OK"
							 oncomplete="exibirMsgProcessando(false); Richfaces.hideModalPanel('modalConfirmaAtribuir');setTimeout('pesquisar()', 500);"/>
					</span> <span> <h:commandButton
							onclick="Richfaces.hideModalPanel('modalConfirmaAtribuir');"
							styleClass="BotaoPadrao" value="Cancelar" />
					</span>
				</div>

			</a4j:form>
			
			<a4j:form prependId="false" rendered="#{!beanExpedirDocumentos.selecionadosTela}" >

				<div>
					<span class="Padrao"> <h:outputText styleClass="Padrao"
							value="Favor selecionar ao menos um documento para proceder com a operação." /> 
					</span>
				</div>

				<div style="padding-top: 20px;">
					 <span> <h:commandButton
							onclick="Richfaces.hideModalPanel('modalConfirmaAtribuir');"
							styleClass="BotaoPadrao" value="Fechar" />
					</span>
				</div>

			</a4j:form>
			

		</rich:modalPanel>
		

		<rich:modalPanel id="modalPanelEncaminharDocumento" width="630"
			height="100" keepVisualState="true">
			<f:facet name="header">
				<h:outputText value="Favor selecionar o setor de destino" />
			</f:facet>
			<a4j:form prependId="false"  rendered="#{beanExpedirDocumentos.selecionadosTela}">

				<div>
					<span class="Padrao"> <h:outputText styleClass="Padrao"
							value="Setor de Destino:" /> <h:selectOneMenu
							style="margin-left:10px;"
							value="#{beanExpedirDocumentos.idSetorDestino}"
							styleClass="Input">
							<f:selectItems
								value="#{beanExpedirDocumentos.itensSetoresDestino}" />
						</h:selectOneMenu> </span>
				</div>

				<div style="padding-top: 20px;">
					<span> <a4j:commandButton styleClass="BotaoPadrao"
							actionListener="#{beanExpedirDocumentos.encaminharDocumentosAction}"
							onclick="Richfaces.hideModalPanel('modalPanelEncaminharDocumento');"
							value="OK" /> </span> <span> <h:commandButton
							onclick="Richfaces.hideModalPanel('modalPanelEncaminharDocumento');"
							styleClass="BotaoPadrao" value="Fechar" /> </span>
				</div>
			</a4j:form>
			<a4j:form prependId="false" rendered="#{!beanExpedirDocumentos.selecionadosTela}" >

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

		<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>