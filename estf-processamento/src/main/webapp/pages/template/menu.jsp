<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>


<rich:modalPanel id="modalPanelConfirmarSaida" rendered="#{beanMenu.renderizarConfirmacaoSair}"
   showWhenRendered="true" autosized="false" height="150" width="600">
	<h:panelGrid>
		<h:outputText styleClass="PadraoMaior" escape="false" value="Você será desconectado de todas as 
			aplicações em que estiver logado.<br/>Deseja realmente sair?<br/>(Para fechar somente esta tela, feche a aba do navegador.)<br/><br/>"/>
 		<t:div>
 			<h:commandLink styleClass="BotaoPadrao" style="float:left;width:50%;font-size:12px" value="Confirmar" id="btnConfirmarSaida" action="#{beanMenu.sair}"/>
 			<h:commandLink value="Cancelar" style="float:left;width:50%;font-size:12px" actionListener="#{beanMenu.cancelarSaida}" disabled="false"/>
 		</t:div>
	</h:panelGrid>
	
</rich:modalPanel>
<t:jscookMenu id="jsCookMenuId" layout="hbr" theme="ThemeOffice"
	styleLocation="/styles">
	<t:navigationMenuItem id="imPrincipal" itemLabel="Principal"
		action="#{beanMenu.principal}" />

	<t:navigationMenuItem id="imAssinatura" itemLabel="Assinatura">
		<security:authorize ifAnyGranted="RS_MASTER_PROCESSAMENTO">
			<t:navigationMenuItem itemLabel="Gabinete"
				action="#{beanMenu.assinarDocumentosGabinete}"
				icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize
			ifAnyGranted="RS_PECA_ASSINADOR,RS_MASTER_PROCESSAMENTO">
			<t:navigationMenuItem itemLabel="Peças processuais"
				action="#{beanMenu.assinarPeca}" icon="/images/blank.gif" />
		</security:authorize>
	</t:navigationMenuItem>

	<t:navigationMenuItem id="imAndamento" itemLabel="Andamento">
		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_REGISTRAR_ANDAMENTO">
			<t:navigationMenuItem itemLabel="Registrar Andamento"
				action="#{beanMenu.registrarAndamento}" icon="/images/blank.gif" />
		</security:authorize>
		

		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_REGISTRAR_ANDAMENTO_EM_LOTE">
			<t:navigationMenuItem
				itemLabel="Registrar Andamento para vários processos"
				action="#{beanMenu.registrarAndamentoParaVariosProcessos}"
				icon="/images/blank.gif" />
		</security:authorize>
	</t:navigationMenuItem>


	<t:navigationMenuItem id="imDeslocamento" itemLabel="Deslocamento">
		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_REGISTRAR_DESLOCAMENTO">
			<t:navigationMenuItem itemLabel="Remeter Documento"
				action="#{beanMenu.remeterDocumento}" icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_PESQUISAR_AUTOS_EMPRESTADOS">
			<t:navigationMenuItem itemLabel="Receber Documento"
				action="#{beanMenu.receberExterno}" icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_PESQUISAR_DESLOCAMENTO">
			<t:navigationMenuItem itemLabel="Pesquisar Guia de Deslocamento"
				action="#{beanMenu.pesquisarGuia}" icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize
			ifAnyGranted="RS_AUTORIZAR_BAIXA">
			<t:navigationMenuItem itemLabel="Autorização para Baixa"
				action="#{beanMenu.autorizarBaixaProcesso}"
				icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_PESQUISAR_DESLOCAMENTO">
			<t:navigationMenuItem itemLabel="Consultar deslocamento de acórdãos"
				action="#{beanMenu.processosPublicadosNaoDeslocados}"
				icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize ifAnyGranted="RS_CARGA_AUTOS">
			<t:navigationMenuItem itemLabel="Carga" icon="/images/blank.gif">
				<t:navigationMenuItem itemLabel="Cadastrar Pessoa"
					action="#{beanMenu.manterPessoaCarga}"
					icon="/images/blank.gif" />
				<t:navigationMenuItem itemLabel="Autorização e Carga de Processos"
					action="#{beanMenu.autorizarCargaAutos}"
					icon="/images/blank.gif" />
				<t:navigationMenuItem itemLabel="Relatório de Carga dos Autos"
					action="#{beanMenu.gerirAutosEmprestados}"
					icon="/images/blank.gif" />
			</t:navigationMenuItem>	
		</security:authorize>		
	</t:navigationMenuItem>

	<t:navigationMenuItem id="imJudiciaria" itemLabel="Expediente">
		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_ELABORACAO_TEXTOS,RS_GESTAO_TEXTOS">
			<t:navigationMenuItem itemLabel="Elaborar Documentos"
				action="#{beanMenu.manterDocumentos}" icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_GESTAO_TEXTOS,RS_REVISAO_TEXTOS,RS_ASSINATURA_TEXTOS">
			<t:navigationMenuItem itemLabel="Enviar para Assinar/Revisar"
				action="#{beanMenu.aguardandoAssinatura}" icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_GESTAO_TEXTOS,RS_REVISAO_TEXTOS">
			<t:navigationMenuItem itemLabel="Revisar Documentos"
				action="#{beanMenu.aguardandoRevisao}" icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_ASSINATURA_TEXTOS">
			<t:navigationMenuItem itemLabel="Assinar Documentos"
				action="#{beanMenu.assinarDocumentos}" icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_ACOMPANHAMENTO_TEXTOS, RS_EXPEDICAO_TEXTOS">
			<t:navigationMenuItem itemLabel="Expedir Documentos"
				action="#{beanMenu.expedirDocumentos}" icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_EDICAO_MODELOS">
			<t:navigationMenuItem itemLabel="Elaborar Modelos"
				action="#{beanMenu.administrarModelos}" icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_EDICAO_MODELOS">
			<t:navigationMenuItem itemLabel="Criar Tipo Modelos"
				action="#{beanMenu.administrarTiposModelos}"
				icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_EDICAO_MODELOS">
			<t:navigationMenuItem itemLabel="Gerar Intimações"
				action="#{beanMenu.gerarIntimacaoFisica}"	
				actionListener="#{beanPartesGerarIntimacao.inicialisarTelaGerarIntimacaoFisica}"			
				icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_EDICAO_MODELOS">
			<t:navigationMenuItem itemLabel="Pesquisar Intimação Eletrônica"
				action="#{beanMenu.intimacaoPesquisa}"	
				actionListener="#{beanComunicacaoExterna.inicialisarTelaPesquisaECadastro}"				
				icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_EDICAO_MODELOS">
			<t:navigationMenuItem itemLabel="Gerar Intimação Eletrônica"
				action="#{beanMenu.gerarIntimacaoEletronica}"
				actionListener="#{beanComunicacaoExterna.inicialisarTelaPesquisaECadastro}"				
				icon="/images/blank.gif" />
		</security:authorize>
		
		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_EDICAO_MODELOS">
			<t:navigationMenuItem itemLabel="Finalizar Restritos"
				action="#{beanMenu.finalizarRestritos}"
				icon="/images/blank.gif" />
		</security:authorize>
		
	</t:navigationMenuItem>

	<t:navigationMenuItem id="imConsulta" itemLabel="Consulta">
		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_CONSULTA_TEXTOS,RS_ELABORACAO_TEXTOS,RS_GESTAO_TEXTOS,RS_ASSINATURA_TEXTOS,RS_ACOMPANHAMENTO_TEXTOS,RS_EXPEDICAO_TEXTOS">
			<t:navigationMenuItem itemLabel="Expedientes Elaborados"
				action="#{beanMenu.consultarDocumentosElaborados}"
				icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_GESTAO_TEXTOS">
			<t:navigationMenuItem itemLabel="Expedientes Sigilosos"
				action="#{beanMenu.consultarDocumentosSigilosos}"
				icon="/images/blank.gif" />
		</security:authorize>
		
		<security:authorize
			ifAnyGranted="RS_CONSULTA_TEXTOS_ASSINADOS">
			<t:navigationMenuItem itemLabel="Expedientes Assinados"
				action="#{beanMenu.consultarDocumentosAssinados}"
				icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_GESTAO_TEXTOS">
			<t:navigationMenuItem itemLabel="Elaboração por Unidade"
				action="#{beanMenu.consultarDocumentosUnidade}"
				icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO">
			<t:navigationMenuItem itemLabel="Visibilidade de Peça"
				action="#{beanMenu.visibilidadePeca}" icon="/images/blank.gif" />
		</security:authorize>
		
		<t:navigationMenuItem itemLabel="Prescrição"
			action="#{beanMenu.consultarPrescricao}" icon="/images/blank.gif" />
		<t:navigationMenuItem itemLabel="Processo de Interesse"
			action="#{beanMenu.processoInteresse}" icon="/images/blank.gif" />
	</t:navigationMenuItem>
	<security:authorize ifAnyGranted="RS_INTEGRACAO">
		<t:navigationMenuItem id="imIntegracao" itemLabel="Integração">
			<security:authorize ifAnyGranted="RS_INTEGRACAO">
				<t:navigationMenuItem itemLabel="Avisos de Comunicação"
					action="#{beanMenu.processoIntegracao}" icon="/images/blank.gif" />
			</security:authorize>
			<security:authorize ifAnyGranted="RS_INTEGRACAO">
				<t:navigationMenuItem itemLabel="Avisos Não Criados"
					action="#{beanMenu.andamentoProcesso}" icon="/images/blank.gif" />
			</security:authorize>
		</t:navigationMenuItem>
	</security:authorize>
	<t:navigationMenuItem id="imAdministracao" itemLabel="Administração">
		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_EDICAO_MODELOS">
			<t:navigationMenuItem itemLabel="Tags Livres"
				action="#{beanMenu.administrarTagsLivres}" icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize ifAnyGranted="RS_MASTER_PROCESSAMENTO">
			<t:navigationMenuItem itemLabel="Tipos Tags Livres"
				action="#{beanMenu.administrarTipoTagsLivres}"
				icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize
			ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_EDICAO_MODELOS">
			<t:navigationMenuItem itemLabel="Autoridades"
				action="#{beanMenu.administrarAutoridades}" icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize ifAnyGranted="RS_MASTER_PROCESSAMENTO">
			<t:navigationMenuItem itemLabel="Permissões de Modelos"
				action="#{beanMenu.administrarTiposPermissoesModelos}"
				icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize ifAnyGranted="RS_MASTER_PROCESSAMENTO">
			<t:navigationMenuItem itemLabel="Grupo de Distribuição"
				action="#{beanMenu.manterTipoGrupo}" icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_MANTER_GRUPO_USUARIOS">	
			<t:navigationMenuItem itemLabel="Grupo de Usuários para Distribuição"
				action="#{beanMenu.manterTipoGrupoUsuario}" icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_REGISTRAR_DESLOCAMENTO, RS_PESQUISAR_DESLOCAMENTO">	
			<t:navigationMenuItem itemLabel="Destinatários para Baixa de Processos e Petições Físicos"
				action="#{beanMenu.manterDestinatariosBaixaExpedicao}" icon="/images/blank.gif" />
		</security:authorize>	
		
		<security:authorize ifAnyGranted="RS_MANTER_PERMISSAO_DESLOCAMENTO">	
			<t:navigationMenuItem itemLabel="Manter permissões de deslocamento"
				action="#{beanMenu.manterPermissaoDeslocamento}" icon="/images/blank.gif" />
		</security:authorize>		
			
		<security:authorize ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_COPIAR_PECAS">
			<t:navigationMenuItem itemLabel="Copiar peças entre Processo"
				action="#{beanMenu.copiarPecas}" icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize ifAnyGranted="RS_PERIODO_EXCLUSAO_MINISTRO">
			<t:navigationMenuItem itemLabel="Exclusão de Ministro da Distribuição"
				action="#{beanMenu.exclusaoMinistroDaDistribuicao}" icon="/images/blank.gif" />
		</security:authorize>
	</t:navigationMenuItem>
	<t:navigationMenuItem id="imExpedicao" itemLabel="Expedição">		
		<security:authorize ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_EXPEDICAO_REMESSA,RS_EXPEDICAO_ADM">
			<t:navigationMenuItem itemLabel="Cadastrar / Pesquisar Destinatário"
				action="#{beanDestinatario.abrirTelaPesquisa}" icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_EXPEDICAO_ADM,RS_EXPEDICAO_REMESSA">
			<t:navigationMenuItem
				itemLabel="Cadastrar / Pesquisar Lista de Remessa"
				action="#{beanListaRemessa.remessaPesquisa}" icon="/images/blank.gif">
				</t:navigationMenuItem>
		</security:authorize>
		<security:authorize ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_EXPEDICAO_ADM,RS_EXPEDICAO_REMESSA">
			<t:navigationMenuItem itemLabel="Finalizar Lista de Remessa"
				action="#{beanListaRemessa.remessaFinalizacaoPesquisa}"
				icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_EXPEDICAO_ADM">
			<t:navigationMenuItem itemLabel="Visualizar / Cadastrar Contrato de Postagem"
				action="#{beanMenu.contratoPostagemVisualizacao}" icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_EXPEDICAO_ADM,RS_EXPEDICAO_REMESSA,RS_EXPEDICAO_CONSULTA">
			<t:navigationMenuItem itemLabel="Pesquisar Lista de Remessa"
				action="#{beanListaRemessa.remessaPesquisaExterna}" icon="/images/blank.gif" />
		</security:authorize>
		<security:authorize ifAnyGranted="RS_MASTER_PROCESSAMENTO,RS_EXPEDICAO_ADM,RS_EXPEDICAO_REMESSA">
			<t:navigationMenuItem itemLabel="Gerar Etiquetas do Processo"
				action="#{beanGeracaoEtiquetaProcesso.gerarEtiquetaProcesso}" icon="/images/blank.gif">
			</t:navigationMenuItem>
		</security:authorize>
	</t:navigationMenuItem>

	<t:navigationMenuItem id="imSair" itemLabel="Sair"
		action="#{beanMenu.verificarSaida}" />
</t:jscookMenu>