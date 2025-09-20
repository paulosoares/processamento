<%@ page contentType="text/html;charset=windows-1252"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<script type="text/javascript">
	function validacaoCampo(campo) {
		for (var i = 0; i < campo.value.length; i++) {
				if (i > 499) {
					campo.value = campo.value.substring(0, 500);
				}
		}
	}
</script>

<f:view>
	<h:form id="uploadForm" enctype="multipart/form-data">
		<a4j:page pageTitle="::.. Principal ..::">
		
		    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaRemessa.js"></script>
			
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Finalizar Lista de Remessa" />
			</jsp:include>

			<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
				cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0">
					<a4j:outputPanel id="pnlPrincipalPesquisa" ajaxRendered="true">
						<h:panelGrid id="pnlFiltro" styleClass="PadraoTituloPanel"
							cellpadding="0" cellspacing="0">
							<h:outputText value="Finalizar Lista de Remessa" />
						</h:panelGrid>
					</a4j:outputPanel>
				</h:panelGrid>
			</h:panelGrid>

			<a4j:outputPanel id="pnlFinalizar" ajaxRendered="true">
				<h:panelGrid columns="1" id="painelFinalizarListaRemessa">
					<rich:panel bodyClass="inpanelBody">
						<div>
							<table>
								<tr>
									<th align="left" colspan="2">
										<h:outputText value="Lista de Remessa - Lista nº #{beanListaRemessa.listaRemessas.numeroListaRemessa}" />
										<h:outputText value="  (lista reaberta)" rendered="#{beanListaRemessa.listaReaberta}" escape="true" style="color: red;"/>
										<br>
										<h:outputText value="Criada em #{beanListaRemessa.dataCriacao}" /><br>
										<h:outputText value="Data de remessa: " /> <rich:calendar
											id="idDataFinalizacao"
											value="#{beanListaRemessa.dataFinalizacao}"
											datePattern="dd/MM/yyyy" locale="pt_Br" /><br>
										<h:outputText value="Andamento: " />
										<h:selectOneMenu value="#{beanListaRemessa.idAndamentoSelecionado}">
											<f:selectItems value="#{beanListaRemessa.listaAndamentos}"/>
											<a4j:support ajaxSingle="true" event="onchange" action="#{beanListaRemessa.validarFinalizarLista}"/>
										</h:selectOneMenu>
									</th>
								</tr>
								<tr>
									<td colspan="2">Lista - Arquivo PDF <t:inputFileUpload
											id="idArquivo" value="#{beanListaRemessa.uploadedFile}"
											storage="default" accept="pdf" /> <br> Tamanho máximo: 10mb
									</td>
								</tr>
								<tr>
									<td colspan="2">Observações:</td>
								</tr>
								<tr>
									<td colspan="2"><h:inputTextarea id="idObservacao" 
											value="#{beanListaRemessa.listaRemessas.observacao}"
											style="width: 500px; height: 100px;"
											onkeyup="javascript:validacaoCampo(this);"/></td>
								</tr>
								
								<tr>
									<td><h:commandButton id="btnFinalizarLista"
											styleClass="BotaoPadraoEstendido" value="Finalizar Lista"
											action="#{beanListaRemessa.finalizarListaRemessa}"
											onclick="if (!confirmaFinalizacaoLista()) {return false;}" />
									</td>
									<td><h:commandButton id="btnVoltarVisualizacao"
											styleClass="BotaoPadraoEstendido" style="margin-left:15px;"
											value="Voltar"
											action="#{beanListaRemessa.voltarRemessaFinalizacaoPesquisa}" />
									</td>
									

									
									<h:inputHidden id="idMensagemValidacao" value="#{beanListaRemessa.mensagemValidacaoFinalizacaoListaRemessa }"/>								
								</tr>
							</table>
						</div>
					</rich:panel>
				</h:panelGrid>
			</a4j:outputPanel>
		</a4j:page>
	</h:form>
</f:view>