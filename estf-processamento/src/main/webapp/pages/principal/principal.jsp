<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<script type="text/javascript">

	function pesquisarHandler(valor) {
		document.getElementById(valor).click();
	}
	
</script>
<f:view>
	<a4j:page pageTitle="::.. Principal ..::">
		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Principal" />
			</jsp:include>
		</h:form>		
		<a4j:form id="form" prependId="false">
			<h:panelGrid styleClass="MolduraExterna" cellpadding="0" cellspacing="0" id="pnlCentral" rendered="#{beanUsuario.carregaDados}">
				<a4j:outputPanel id="pnlPrincipalPesquisa" ajaxRendered="true">
					<h:panelGrid columns="2">
						<h:selectOneMenu value="#{beanUsuario.setorAtual}" id="seletorSetores" immediate="true">
							<rich:toolTip style="background-color:white; width:500px" followMouse="false" direction="top-right" horizontalOffset="20" verticalOffset="20"
							id="toolTiptexto" showDelay="1000" >
								<h:outputText style="color:red" value="A lista de setores apresentada neste item é composta dos setores que o usuário está vinculado no sistema eSTF-Gabinetes.
								Para incluir ou excluir um setor da lista é necessário entrar em contato com o administrador do sistema no setor."/><br>
							</rich:toolTip>
							<f:selectItems value="#{beanUsuario.listaSetoresGabinetes}" />
						</h:selectOneMenu>
						<a4j:commandButton id="altSetor" value="Alterar Setor" action="#{beanUsuario.alterarSetor}" reRender="divMolduraCentral,idPanelTituloPagina" onclick="exibirMsgProcessando(true)" oncomplete="exibirMsgProcessando(false);" />
						<a4j:commandButton id="mostrarPainel" value="Exibir Painel de Controle" action="#{beanUsuario.renderizarPainelDeControle}" reRender="divMolduraCentral" rendered="#{!beanUsuario.exibePainelDeControles}" onclick="exibirMsgProcessando(true)" oncomplete="exibirMsgProcessando(false);" />
						<a4j:commandButton id="atualizarPainel" value="Atualizar Painel de Controle" action="#{beanUsuario.pesquisaControlesExistentes}" reRender="divMolduraCentral" rendered="#{beanUsuario.exibePainelDeControles}" onclick="exibirMsgProcessando(true)" oncomplete="exibirMsgProcessando(false);" />
					</h:panelGrid>
				</a4j:outputPanel>

				<a4j:outputPanel  id="divMolduraCentral">
					<a4j:outputPanel rendered="#{beanUsuario.exibePainelDeControles}" id="panelControle" style="width:1273px;">
						<div id="divControle">
			        		<div style="float: left; width: 25%; padding: 0px 0px 0px 5px;">
			        			<span>
						            <rich:panel>
						                <f:facet name="header">
						                	<h:outputLabel value="Controle" style="height:15; text-align:center;"/>
						                </f:facet>
										<rich:tree style="width:240px;" value="#{beanUsuario.arvoreControle}" var="item" 
											rendered="#{beanUsuario.renderedArvoreControle}">
							               		<rich:treeNode> 
													<h:commandLink value="#{item.descricao}" action="#{beanUsuario.realizaAcaoControle}">
														<f:setPropertyActionListener value="#{item.chave}" target="#{beanUsuario.chaveControle}"/>
													</h:commandLink>
							               		</rich:treeNode>
										</rich:tree>
										<rich:tree style="width:240px;" value="#{beanUsuario.arvoreExpedienteExpedido}" var="itemExExp" 
											adviseNodeOpened="#{beanUsuario.expandeNoArvore}">
						               		<rich:treeNode> 
												<h:commandLink value="#{itemExExp.descricao}" action="#{beanUsuario.realizaAcaoControle}">
													<f:setPropertyActionListener value="#{itemExExp.chave}" target="#{beanUsuario.chaveControle}"/>
												</h:commandLink>
						               		</rich:treeNode>
										</rich:tree>
						               	<c:if test="${beanUsuario.renderedArvoreUsuarios}">
						               		<hr color="red" />
						               		<rich:tree style="width:240px" value="#{beanUsuario.arvoreItensUsuario}" var="usu"
						               			adviseNodeOpened="#{beanUsuario.expandeNoArvore}">
							               		<rich:treeNode>
													<h:commandLink value="#{usu.descricao}" action="#{beanUsuario.carregaItensUsuario}">
														<f:setPropertyActionListener value="#{usu.chave}" target="#{beanUsuario.chaveControleUsuario}"/>
													</h:commandLink>
							               		</rich:treeNode>
						               		</rich:tree>
						               	</c:if>
						            </rich:panel>
					            </span>
			        		</div>
				        	<div style="float: left; width: 1px; padding: 0px 0px 0px 5px;"></div>
				        	<div style="float: left; width: 74%; padding: 0px 0px 0px 0px;">
				        		<span>
						            <rich:panel id="painelItemControles" rendered="#{beanUsuario.renderedArvoreControle}">
						                <f:facet name="header">
						                	<h:commandLink action="#{beanUsuario.chamaPackageItemControle}"
						                		 value="Item Controle #{beanUsuario.nomeControleTabela}"  style="height:10; color:black;
						                		 	font-family : Arial; font-size: 11px;">
												<h:graphicImage url="../../images/recur.png" style="margin-left=30px;"
													title="Atualizar" />
											</h:commandLink>
						                </f:facet>
						                <f:facet name="header">
						                </f:facet>
						                <a4j:outputPanel ajaxRendered="true"
											id="pnlResultadoPesquisa" styleClass="MolduraInterna"
											rendered="#{beanUsuario.renderedItensControle}">
											<rich:dataTable var="itens" headerClass="DataTableDefaultHeaderControle"
												rowClasses="DataTableRow, DataTableRow3" id="tabelaItensControle"
												columnClasses="tres, tres, tres, dezCenter, tres, seteCenter, dezCenter, dezCenter, dezCenter, dezCenter, dezCenter"
												value="#{beanUsuario.listaItensControle}" rows="45">
												<rich:column>
													<f:facet name="header">
														<a4j:commandButton image="../../images/setabaixo.gif"
															onclick="exibirMsgProcessando(true)"
															oncomplete="exibirMsgProcessando(false);"
															actionListener="#{beanUsuario.marcarTodosItensControles}" />
													</f:facet>
													<h:selectBooleanCheckbox
														onclick="document.getElementById('BotaoAtualizarMarcacao').click();"
														value="#{itens.checked}" />
												</rich:column>
												
												<rich:column>
													<f:facet name="header">
														<h:outputText value="Pref." />
													</f:facet>
													<h:commandLink value="#{itens.wrappedObject.itemControleResult.itemControle.siglaTipoPreferencia}" 
														onblur="Richfaces.showModalPanel('modalPartesPrescricao');" action="#{beanUsuario.carregaInformacoesPrescricao}"/>
													<rich:toolTip followMouse="false" direction="top-right" id="toolTipPreferencia"
														horizontalOffset="-5" verticalOffset="-5"
														styleClass="tooltipPrecedentes" hideDelay="20" showDelay="250" 
														rendered="#{not empty itens.wrappedObject.itemControleResult.itemControle.siglaTipoPreferencia}">
														<h:dataTable var="valorL" id="tblValor"
															value="#{itens.wrappedObject.itemControleResult.itemControle.processo.incidentePreferencia}"
															headerClass="CabecalhoTabelaDados"
															rowClasses="LinhaParTabelaDados"
															columnClasses="ColunaTabelaDados"
															styleClass="TabelaDadosPreview">
															<h:column>
																<f:facet name="header">
																	<h:outputText value="Descrição" />
																</f:facet>
																<h:panelGrid style="text-align: center;" width="60%">
																	<h:outputText value="#{valorL.tipoPreferencia.descricao}" styleClass="Padrao" />
																</h:panelGrid>
															</h:column>
														</h:dataTable>
													</rich:toolTip>
												</rich:column>
								
								<rich:column sortBy="#{itens.wrappedObject.itemControleResult.itemControle.isEletronico}">
									<f:facet name="header">
										<h:outputText value="Tipo" />
									</f:facet>	
									<h:outputText styleClass="Padrao" style="text-align:center;" 
												rendered="#{itens.wrappedObject.itemControleResult.itemControle.isEletronico}"
												value="E">
									</h:outputText>
									<h:outputText styleClass="Padrao" style="text-align:center;" 
												rendered="#{!itens.wrappedObject.itemControleResult.itemControle.isEletronico}"
												value="F">
									</h:outputText>		
								</rich:column>
								
								<rich:column sortBy="#{itens.wrappedObject.itemControleResult.itemControle.objetoIncidente.identificacao}">
									<f:facet name="header">
										<h:outputText value="Processo" />
									</f:facet>
									<t:popup
										rendered="#{itens.wrappedObject.itemControleResult.itemControle.objetoIncidente.identificacao}"
										displayAtDistanceX="2">
										<t:panelGroup>
											<t:outputText
												rendered="#{itens.wrappedObject.itemControleResult.itemControle.isEletronico}"
												styleClass="PadraoEletronico" value="e"  />
											<t:outputText styleClass="PadraoLink" 
												rendered="#{itens.wrappedObject.itemControleResult.itemControle.objetoIncidente.identificacao}"
												value="#{itens.wrappedObject.itemControleResult.itemControle.objetoIncidente.identificacao}" />
										</t:panelGroup>
			
									<f:facet name="popup">
										<t:panelGrid cellpadding="0" cellspacing="0" columns="1"
											rowClasses="LinhaParTabelaDadosPreview,LinhaImparTabelaDadosPreview"
											columnClasses="ColunaTabelaDadosPreview">
												<h:commandLink  styleClass="PadraoLink"
													style="text-align:center;" target="_blank" 
													value="Consultar Internet"
													action="#{beanConsultarDocumentoExterno.consultarProcessoInternet}">
													<f:setPropertyActionListener
														target="#{beanConsultarDocumentoExterno.seqObjetoIncidente}"
														value="#{itens.wrappedObject.itemControleResult.itemControle.objetoIncidente.principal.id}" />
												</h:commandLink>
																						
												<h:commandLink styleClass="PadraoLink"
													style="text-align:center;" target="_blank"
													value="Consultar Peças"
													action="#{beanConsultarPecas.consultarPecas}">
													<f:setPropertyActionListener
														target="#{beanConsultarPecas.seqObjetoIncidente}"
														value="#{itens.wrappedObject.itemControleResult.itemControle.objetoIncidente.principal.id}" />
												</h:commandLink>																																																																	
										</t:panelGrid>
									</f:facet>
								</t:popup>
							</rich:column>
				
							<rich:column>
								<f:facet name="header">
									<h:outputText value="PDF" />
								</f:facet>
								<h:outputLink
									rendered="#{itens.wrappedObject.possuiPDF}"
									value="#{itens.wrappedObject.linkPDF}">
									<h:graphicImage value="/images/pdf.png"></h:graphicImage>
								</h:outputLink>
							</rich:column>											
												
							<rich:column>
								<f:facet name="header">
									<h:outputText value="Ações"
										styleClass="DataTableDocumentoTexto" />
								</f:facet>
								<h:commandLink action="#{beanUsuario.assumirControle}">
									<h:graphicImage url="../../images/ok.png"
										title="Assumir controle" />
								</h:commandLink>
								<a4j:commandLink action="#{beanUsuario.fecharControle}" style="margin-left:3px;"
									reRender="painelItemControles">
									<h:graphicImage url="../../images/deletecell.png"
										title="Fechar controle" />
								</a4j:commandLink>
								<a4j:commandLink action="#{beanUsuario.gerarExpediente}" style="margin-left:3px;">
									<h:graphicImage url="../../images/edit.png"
										title="Gerar Expediente" />
								</a4j:commandLink>		
								<a4j:commandLink action="#{beanUsuario.gerarAndamento}" style="margin-left:3px;">
									<h:graphicImage url="../../images/list.png"
										title="Registrar Andamento" />
								</a4j:commandLink>		
								<h:commandLink action="#{beanUsuario.gerarDeslocamento}" style="margin-left:1px;">
									<h:graphicImage url="../../images/report.png"
										title="Remeter Processo/Petição" />
								</h:commandLink>								
							</rich:column>	
												
							<rich:column sortBy="#{itens.wrappedObject.itemControleResult.itemControle.nomeMinistroRelator}">
								<f:facet name="header">
									<h:outputText value="Relator"
										styleClass="DataTableDocumentoTexto"/>
								</f:facet>
								<h:outputText styleClass="Padrao" style="text-align:center;"
									value="#{itens.wrappedObject.itemControleResult.itemControle.nomeMinistroRelator}"/>
							</rich:column>	
																						
							<rich:column sortBy="#{itens.wrappedObject.itemControleResult.itemControle.tipoSituacaoControle.descricao}">
								<f:facet name="header">
									<h:outputText value="Situacao" styleClass="DataTableDocumentoTexto" />
								</f:facet>
								<h:outputText styleClass="Padrao" style="text-align:center; #{itens.wrappedObject.itemControleResult.itemControle.tipoSituacaoControle.id == 1 ? 'color:green' : 'color:blue'};"
									value="#{itens.wrappedObject.itemControleResult.itemControle.tipoSituacaoControle.descricao}" />
							</rich:column>
							
							<rich:column sortBy="#{itens.wrappedObject.itemControleResult.itemControle.dataInclusao}">
								<f:facet name="header">
									<h:outputText value="Data" styleClass="DataTableDocumentoTexto" />
								</f:facet>
								<h:outputText styleClass="Padrao" style="text-align:center;"
									value="#{itens.wrappedObject.itemControleResult.itemControle.dataInclusao}">
									 <f:convertDateTime pattern="dd/MM/yyyy HH:mm"/> 
								</h:outputText>
							</rich:column>
							
							<rich:column sortBy="#{itens.wrappedObject.itemControleResult.itemControle.usuario.id}">
								<f:facet name="header">
									<h:outputText value="Usuário"  styleClass="DataTableDocumentoTexto" />
								</f:facet>
								<h:outputText styleClass="Padrao" style="text-align:center;"
									value="#{itens.wrappedObject.itemControleResult.itemControle.usuario.id}"/>
							</rich:column>
							
							<rich:column sortBy="#{itens.wrappedObject.itemControleResult.nomeSetorDestino}">
								<f:facet name="header">
									<h:outputText value="Localização Atual" styleClass="DataTableDocumentoTexto" />
								</f:facet>
								<h:outputText styleClass="Padrao" style="text-align:center;"
									value="#{itens.wrappedObject.itemControleResult.nomeSetorDestino}"/>
							</rich:column>
						
						</rich:dataTable>
						<rich:datascroller id="scroll" for="tabelaItensControle" maxPages="20" 
							align="left" />
						<security:authorize ifAnyGranted="RS_MANTER_GRUPO_USUARIOS">	
							<a4j:commandButton value="Atribuição/Reatribuição" style="left:27px; margin-top:10px;"
								styleClass="BotaoPadraoEstendido" oncomplete="Richfaces.showModalPanel('modalPanelReatribuicaoControle');"
								actionListener="#{beanUsuario.listarComboUsuarios}" reRender="modalPanelReatribuicaoControle"/>
						</security:authorize>
						<div style="margin-top: 15px;"></div>
					</a4j:outputPanel>
	    		</rich:panel>
    		</span>
     	</div>
    	</div>
    	</a4j:outputPanel>
       	</a4j:outputPanel>
		<a4j:commandButton styleClass="BotaoOculto"
			id="BotaoAtualizarMarcacao"
			actionListener="#{beanUsuario.atualizarMarcacao}"
			reRender="pnlCentral" />
					
			</h:panelGrid>
		</a4j:form>
		
		<rich:modalPanel id="modalPanelReatribuicaoControle" width="630"
			height="100" keepVisualState="true">
			<f:facet name="header">
				<h:outputText value="Selecione o usuário para atribuição:" />
			</f:facet>
			<a4j:form prependId="false">
				<div>
					<span class="Padrao"> 
						<h:outputText styleClass="Padrao"
								value="Usuário:" />
						 <h:selectOneMenu
							style="margin-left:10px;"
							value="#{beanUsuario.idUsuarioContr}" styleClass="Input">
							<f:selectItems
								value="#{beanUsuario.listaUsuariosControles}" />
						</h:selectOneMenu> 
					</span>
				</div>
				<div style="padding-top: 20px;">
					<span> 
						<a4j:commandButton styleClass="BotaoPadrao"
							actionListener="#{beanUsuario.atribuirControleAoUsuario}"
							onclick="Richfaces.hideModalPanel('modalPanelReatribuicaoControle');"
							value="Atribuir" /> 
					</span> 
					<span> 
						<h:commandButton
							onclick="Richfaces.hideModalPanel('modalPanelReatribuicaoControle');"
							styleClass="BotaoPadrao" value="Fechar" /> 
					</span>
				</div>
			</a4j:form>
		</rich:modalPanel>
		
		<rich:modalPanel id="modalPartesPrescricao" width="630"
			height="100" keepVisualState="true">
			<f:facet name="header">
				<h:outputText value="Informações de prescrição:" />
			</f:facet>
			<a4j:form prependId="false">
				<div>
					<rich:dataTable 
						id="tbPrescricao" var="_prescricao" value="#{beanUsuario.prescricaoReu}"
						rowClasses="DataTableRow, DataTableRow3"
						styleClass="data-table" >
						<rich:column styleClass="dr-table-firstcol" style="width: 70%;">
							<f:facet name="header">
								<h:outputText value="Parte" />
							</f:facet>
							<h:outputText value="#{_prescricao.jurisdicionado.nome}" />
						</rich:column>
						<rich:column styleClass="dr-table-col-center">
							<f:facet name="header">
								<h:outputText value="Data de Prescrição"/>
							</f:facet>
							<a4j:outputPanel rendered="#{not empty _prescricao.dataPrescricao}">
								<h:outputText value="#{_prescricao.dataPrescricao}"/>
							</a4j:outputPanel>
							<a4j:outputPanel rendered="#{empty _prescricao.dataPrescricao}">
								<h:outputText value="#{_prescricao.dataPrescricaoPenaMinima}" />
								<span> | </span>
								<h:outputText value="#{_prescricao.dataPrescricaoPenaMaxima}" />
							</a4j:outputPanel>
						</rich:column>
					</rich:dataTable>
				</div>
			</a4j:form>
		</rich:modalPanel>
		<jsp:include page="/pages/template/footer.jsp" flush="true" />
	</a4j:page>
</f:view>