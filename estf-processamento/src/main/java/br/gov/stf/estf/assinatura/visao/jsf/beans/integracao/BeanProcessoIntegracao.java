package br.gov.stf.estf.assinatura.visao.jsf.beans.integracao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.richfaces.component.html.HtmlDataTable;

import br.gov.stf.estf.assinatura.visao.util.commons.CollectionUtils;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.HistoricoProcessoOrigem;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ProcessoIntegracao;
import br.gov.stf.estf.entidade.usuario.UsuarioExterno;
import br.gov.stf.estf.processostf.model.util.ProcessoIntegracaoEnum;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanProcessoIntegracao extends AbstractBeanIntegracao {

	private static final long serialVersionUID = 1L;

	private static final Object LISTA_USUARIOS_EXTERNOS = new Object();
	private static final Object LISTA_PROCESSO_TIPO_PROCESSO_INTEGRACAO = new Object();
	private static final Object LISTA_TIPO_COMUNICACAO = new Object();

	private static final Object LISTA_PROCESSO = new Object();

	private static final Object KEY_USUARIO_EXTERNO = new Object();
	private static final Object KEY_TIPO_PROCESSO_INTEGRACAO = new Object();

	private static final Object KEY_TIPO_COMUNICACAO = new Object();
	private static final Object KEY_PROCESSO_INTEGRACAO = new Object();
	
	private static final Integer REMESSA_INDEVIDA = new Integer(106);
    private static final Integer DEVOLUCAOPROCESSO = new Integer(101);
    private static final Integer DEVOLUCAOPROCESSOORIGEM = new Integer(102);
    private static final Integer BAIXAPROCESSOAORIGEM = new Integer(103);
    private static final Integer PROCESSOPROTOCOLADO = new Integer(107);
    private static final Integer PROCESSOREAUTUDAO = new Integer(109);
    private static final Integer PROCESSOAUTUADO = new Integer(108);
    private static final long SEQ_USUARIO_EXTERNO_PARA_ORGAO_NAO_INTEGRADO = 92000L;

	
	private List<SelectItem> listaUsuariosExternos;
	private List<SelectItem> listaTipoProcessoIntegracao;
	private List<SelectItem> listaTipoComunicacao;

	private Integer usuarioExterno;
	private String tipoProcessoIntegracao;
	private Integer tipoComunicacao;
	private CheckableDataTableRowWrapper processoIntegracao;

	private List<CheckableDataTableRowWrapper> listaProcessos;
	private HtmlDataTable tabelaProcessos;

	public BeanProcessoIntegracao() {
		restauraSessao();
		super.restaurarSessao();
	}

	private void restauraSessao() {
		if (getAtributo(LISTA_USUARIOS_EXTERNOS) == null) {
			setAtributo(LISTA_USUARIOS_EXTERNOS, carregaUsuariosExternos());
		}
		setListaUsuariosExternos((List<SelectItem>) getAtributo(LISTA_USUARIOS_EXTERNOS));

		if (getAtributo(LISTA_PROCESSO_TIPO_PROCESSO_INTEGRACAO) == null) {
			setAtributo(LISTA_PROCESSO_TIPO_PROCESSO_INTEGRACAO,
					carregarTipoProcessoIntegracao());
		}
		setListaTipoProcessoIntegracao((List<SelectItem>) getAtributo(LISTA_PROCESSO_TIPO_PROCESSO_INTEGRACAO));

		if (getAtributo(LISTA_TIPO_COMUNICACAO) == null) {
			setAtributo(LISTA_TIPO_COMUNICACAO, carregarTipoComunicacao());
		}

		setListaTipoComunicacao((List<SelectItem>) getAtributo(LISTA_TIPO_COMUNICACAO));

		if (getAtributo(LISTA_PROCESSO) == null) {
			setAtributo(LISTA_PROCESSO, listaProcessos);
		}

		setListaProcessos((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_PROCESSO));

		if (getAtributo(KEY_USUARIO_EXTERNO) == null) {
			setAtributo(KEY_USUARIO_EXTERNO, usuarioExterno);
		}

		setUsuarioExterno((Integer) getAtributo(KEY_USUARIO_EXTERNO));

		if (getAtributo(KEY_TIPO_PROCESSO_INTEGRACAO) == null) {
			setAtributo(KEY_TIPO_PROCESSO_INTEGRACAO, tipoProcessoIntegracao);
		}
		setTipoProcessoIntegracao((String) getAtributo(KEY_TIPO_PROCESSO_INTEGRACAO));

		if (getAtributo(KEY_TIPO_COMUNICACAO) == null) {
			setAtributo(KEY_TIPO_COMUNICACAO, tipoComunicacao);
		}

		setTipoComunicacao((Integer) getAtributo(KEY_TIPO_COMUNICACAO));

		if (getAtributo(KEY_PROCESSO_INTEGRACAO) == null) {
			setAtributo(KEY_PROCESSO_INTEGRACAO, processoIntegracao);
		}
		
		setProcessoIntegracao((CheckableDataTableRowWrapper) getAtributo(KEY_PROCESSO_INTEGRACAO));

	}
	public void limparSessaoAction(){
		limparSessao();
		atualizarSessao();
	}
	
	public void limparSessao(){
		setUsuarioExterno(null);
		setTipoProcessoIntegracao(null);
		setTipoComunicacao(null);
		setDataInicial(null);
		setDataFinal(null);
		setSiglaProcesso(null);
		setNumProcesso(null);
		setListaProcessos(null);
	}

	public void atualizarSessao() {
		setAtributo(LISTA_USUARIOS_EXTERNOS, listaUsuariosExternos);
		setAtributo(LISTA_PROCESSO_TIPO_PROCESSO_INTEGRACAO,listaTipoProcessoIntegracao);
		setAtributo(LISTA_TIPO_COMUNICACAO, listaTipoComunicacao);
		setAtributo(LISTA_PROCESSO, listaProcessos);

		setAtributo(KEY_USUARIO_EXTERNO, usuarioExterno);
		setAtributo(KEY_TIPO_PROCESSO_INTEGRACAO, tipoProcessoIntegracao);
		setAtributo(KEY_TIPO_COMUNICACAO, tipoComunicacao);

		if (processoIntegracao != null) {
			setAtributo(KEY_PROCESSO_INTEGRACAO, processoIntegracao);
		}

		super.atualizarSessao();
		
		keepStateInHttpSession();
	}
	
	public void marcarTodosProcessosIntegracao(ActionEvent evt) {
		marcarOuDesmarcarTodasRich(listaProcessos, tabelaProcessos);
		setListaProcessos(listaProcessos);
		setAtributo(LISTA_PROCESSO, listaProcessos); 
	}

	public void atualizarMarcacao(ActionEvent evt) {
		 setListaProcessos(listaProcessos);
		 setAtributo(LISTA_PROCESSO, listaProcessos);
	}

	public void marcarComoLidos(){
		List<ProcessoIntegracao> listaProcessoIntegracaoSelecionados = retornarItensSelecionados(listaProcessos);
		
		if (CollectionUtils.isVazia(listaProcessoIntegracaoSelecionados) || listaProcessoIntegracaoSelecionados == null) {
			reportarInformacao("Selecione pelo menos 1 documento para marcar como lido");
			return;
		}

		if (verificarProcessoJaEnviadoMarcado(listaProcessoIntegracaoSelecionados)) {
			reportarAviso("Somente processo ainda não lido(enviado) pode ser selecionado!");
			return;
		}
		try {
			for (ProcessoIntegracao pi : listaProcessoIntegracaoSelecionados) {
				List<Long> codigosUsuariosExternos = getOrigemService().recuperaUsuarioExternoESTF(pi.getOrigem().getId()); 
				
				if(codigosUsuariosExternos.isEmpty()){
					getProcessoIntegracaoService().inserirEncaminhadoPorMidia(pi, SEQ_USUARIO_EXTERNO_PARA_ORGAO_NAO_INTEGRADO , ENVIADO_POR_MIDIA_ORGAO_NAO_INTEGRADO);
				}

				for(Long usuarioExternoESTF : codigosUsuariosExternos){
					getProcessoIntegracaoService().inserirEncaminhadoPorMidia(pi, usuarioExternoESTF, ENVIADO_POR_MIDIA);
				}
				listaProcessos.remove(wrappedObjectInCheckableDataTableRowWrapper(pi));
			}
			
			limparSessaoAction();
			reportarInformacao("Processo(s) marcado(s) como lido(s) com sucesso!");
			
 		} catch (Exception e) {
			e.printStackTrace();
			reportarErro("Erro ao marcar o aviso enviado por midia.");
		}
	}
	
	private boolean verificarProcessoJaEnviadoMarcado(List<ProcessoIntegracao> listaProcessoIntegracaoSelecionados) {
		for (ProcessoIntegracao pi : listaProcessoIntegracaoSelecionados) {
			if (pi.isEnviado()) {
				return true;
			}
		}
		return false;
	}

	/** Actions **/
	@Override
	public void deslocaProcessoAction() {
		if(deslocaProcesso(getProcessoIntegracaoWrapped().getAndamentoProcesso())){
			reportarInformacao("Reenviado com sucesso.");
			excluirAviso();
//			chamaProcInterop(getProcessoIntegracaoWrapped().getProcesso());
			listaProcessos.remove(processoIntegracao);
		}
		limparSessaoModal();
		processoIntegracao = null;
		setAtributo(KEY_PROCESSO_INTEGRACAO, processoIntegracao);
		atualizarSessao();
	}
	
	@Override
	public void voltarAction() {
		super.voltar(getProcessoIntegracaoWrapped().getProcesso());
		atualizarSessao();
	}
	
	public void abrirModal() throws ServiceException {
		this.processoIntegracao = (CheckableDataTableRowWrapper) tabelaProcessos.getRowData();

		ObjetoIncidente<?> processo = getObjetoIncidenteService().recuperarPorId(getProcessoIntegracaoWrapped().getProcesso().getId());
		
		listaOrigens = carregarOrigensCadastradas(processo);
		
		listaDeslocaProcessos = carregarListaDeslocaProcesso(processo);
		
		atualizarSessao();
	}
	
	public void pesquisarAction() {
		listaProcessos = null;
		tabelaProcessos = null;
		if(pesquisar()){
			if (listaProcessos == null || listaProcessos.size() == 0) {
				reportarAviso("Não foi encontrado nenhum registro.");
			} else {
				reportarInformacao("A consulta retornou " + listaProcessos.size() + " avisos.(Limite : 1000 avisos por consulta)");
			}
			if(usuarioExterno != null && usuarioExterno > 0){
				reportarInformacao(recuperarQtdeAvisosPendentes(usuarioExterno));
				reportarInformacao(recuperarUltimaManifestacao(Long.valueOf(usuarioExterno)) + recuperarUltimaConsultaDocumento(Long.valueOf(usuarioExterno)));
			}	
		}	
		atualizarSessao();
	}


	public void inserirOrigemAction() {
		inserirOrigem(getProcessoIntegracaoWrapped().getProcesso());
		atualizarSessao();
	}

	public void limparSessaoModalAction() {
		processoIntegracao = null;
		setAtributo(KEY_PROCESSO_INTEGRACAO, processoIntegracao);
		limparSessaoModal();
		atualizarSessao();
	}
	
	public void marcarEnviadoPorMidiaAction(){
		marcarEnviadoPorMidia();
		limparSessaoModal();
		listaProcessos.remove(processoIntegracao);
		processoIntegracao = null;
		setAtributo(KEY_PROCESSO_INTEGRACAO, processoIntegracao);
		atualizarSessao();
	}

	
	public String recuperarQtdeAvisosPendentes(Integer seqUsuarioExterno){
		String mensagemAvisosPendentes = new String();
		try {
			 Long QtdeAvisosPendentes = getProcessoIntegracaoService().pesquisarQtdAvisosLidos(seqUsuarioExterno);
			 if(QtdeAvisosPendentes > 0){
				 mensagemAvisosPendentes = "Existem " + QtdeAvisosPendentes + " avisos de baixa/devolução pendentes.";	 
			 }
			 else
			 {
				 mensagemAvisosPendentes = "Não existem avisos pendentes.";
			 }
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return mensagemAvisosPendentes; 
	}
	
	
	public boolean getConfirmarMarcadoPorMidia(){
		if (modalDeslocaProcessoCheck != null) {
			DeslocaProcesso deslocaProcesso = listaDeslocaProcessos	.get(modalDeslocaProcessoCheck);
			if(!getProcessoIntegracaoWrapped().getOrigem().getId().equals(deslocaProcesso.getCodigoOrgaoDestino())){
				return false;
			}
			Boolean isIntegrada = new Boolean(false);
			Boolean numeroCadastrado = new Boolean(false);
			try {
				Origem origem = getOrigemService().recuperarPorId(	deslocaProcesso.getCodigoOrgaoDestino());
				isIntegrada = getOrigemService().isOrigemIntegrada(origem);
				
				for(HistoricoProcessoOrigem historicoProcessoOrigem : listaOrigens){
					if(historicoProcessoOrigem.getOrigem().getId().equals(deslocaProcesso.getCodigoOrgaoDestino())){
						numeroCadastrado = true;
					}
				}
			} catch (ServiceException e) {
				reportarErro("Erro ao descobrir se a origem está integrada.");
				e.printStackTrace();
			}
			if(isIntegrada && numeroCadastrado){
				return true;
			}else{
				return false;
			}
		}else{
			return false;	
		}
	}
	
	// Métodos publicos usados na tela
	private void marcarEnviadoPorMidia(){
		try{
			DeslocaProcesso deslocaProcesso = listaDeslocaProcessos.get(modalDeslocaProcessoCheck);
			if(!getProcessoIntegracaoWrapped().getOrigem().getId().equals(deslocaProcesso.getCodigoOrgaoDestino())){
				reportarAviso("É necessário que a origem selecionada seja igual ao do aviso.");
				return; 
			}
			List<Long> usuarios = getOrigemService().recuperaUsuarioExternoESTF(deslocaProcesso.getCodigoOrgaoDestino()); 
			
			for(Long usuarioExternoESTF : usuarios){
				getProcessoIntegracaoService().inserirEncaminhadoPorMidia(getProcessoIntegracaoWrapped(), usuarioExternoESTF, ENVIADO_POR_MIDIA);
			}
		}catch(Exception e){
			e.printStackTrace();
			reportarErro("Erro ao marcar o aviso enviado por midia.");
		}
	}	
	
	private void excluirAviso(){
		try {
			getProcessoIntegracaoService().excluir(getProcessoIntegracaoWrapped());
		} catch (ServiceException e) {
			e.printStackTrace();
			reportarErro("Não foi possível excluir o aviso.");
		}
	}

	private boolean pesquisar() {
		List<ProcessoIntegracao> listaProcesso = null;
		if (verificaDados()) {
			return false;
		}
		try {
			//Caso onde a situação é Devolução/Baixa.
			if(tipoComunicacao == 1){
				listaProcesso = getProcessoIntegracaoService().pesquisar(
						usuarioExterno, tipoProcessoIntegracao, dataInicial,
						dataFinal, numProcesso, siglaProcesso, REMESSA_INDEVIDA, DEVOLUCAOPROCESSO,
						DEVOLUCAOPROCESSOORIGEM, BAIXAPROCESSOAORIGEM);
			}
			//Caso onde a situação é Notificação.
			if(tipoComunicacao == 2){
				listaProcesso = getProcessoIntegracaoService().pesquisar(
						usuarioExterno, tipoProcessoIntegracao, dataInicial,
						dataFinal, numProcesso, siglaProcesso, PROCESSOPROTOCOLADO, PROCESSOREAUTUDAO,
						PROCESSOAUTUADO);
			}
			//Caso onde a situação é Todos.
			if(tipoComunicacao == 3){
				listaProcesso = getProcessoIntegracaoService().pesquisar(
						usuarioExterno, tipoProcessoIntegracao, dataInicial,
						dataFinal, numProcesso, siglaProcesso, REMESSA_INDEVIDA, PROCESSOPROTOCOLADO, DEVOLUCAOPROCESSO,
						DEVOLUCAOPROCESSOORIGEM, BAIXAPROCESSOAORIGEM, PROCESSOREAUTUDAO, PROCESSOAUTUADO);
			}
		
		} catch (ServiceException e) {
			e.printStackTrace();
			reportarErro("Erro ao realizar a pesquisa.",
					e.getLocalizedMessage());
		}
		setListaProcessos(getCheckableDataTableRowWrapperList(listaProcesso));
		return true;
	}

	private String recuperarUltimaManifestacao(Long seqUsuarioExterno){
		String dataUltimaManifestacaoFormatada = " Última manifestação não determinada";
		try {
			 Date dataUltimaManifestacao = getLogControleProcessService().findMaxDataByUsuarioExterno(seqUsuarioExterno);
			 if(dataUltimaManifestacao != null){
				 dataUltimaManifestacaoFormatada = new SimpleDateFormat("dd/MM/yyyy").format(dataUltimaManifestacao);	 
				 dataUltimaManifestacaoFormatada =  " Última manifestação em: " +  dataUltimaManifestacaoFormatada;
			 }
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return dataUltimaManifestacaoFormatada; 
	}
	
	private String recuperarUltimaConsultaDocumento(Long seqUsuarioExterno){
		String dataUltimaConsultaDocumentoFormatada = ". Última consulta não determinada";
		try {
			 Date dataUltimaConsultaDocumento = getIntegracaoDocumentoService().findMaxDataById(seqUsuarioExterno);
			 if(dataUltimaConsultaDocumento != null){
				 dataUltimaConsultaDocumentoFormatada = new SimpleDateFormat("dd/MM/yyyy").format(dataUltimaConsultaDocumento);	 
				 dataUltimaConsultaDocumentoFormatada =" | Última consulta em: " + dataUltimaConsultaDocumentoFormatada;
			 }
		} catch (ServiceException e) {
			e.printStackTrace();
		} 
		return dataUltimaConsultaDocumentoFormatada; 
	}

	private ProcessoIntegracao getProcessoIntegracaoWrapped() {
		return (ProcessoIntegracao) this.processoIntegracao.getWrappedObject();
	}

	private boolean verificaDados() {
		boolean verificaDados = false;
		boolean usuarioExterno = false;
		boolean processo = false;
		boolean tipoComunicacao = false;
		boolean datas = false;
		if (this.usuarioExterno == null) {
			usuarioExterno = true;
		}

		if (this.tipoComunicacao == null) {
			tipoComunicacao = true;
		}

		if (dataInicial != null || dataFinal != null) {
			if (dataInicial == null) {
				reportarAviso("Se informar uma data final é nescessário informar a data inicial. ");
				datas = true;
			} else {

				if (dataFinal != null && dataFinal.before(dataInicial)) {
					reportarAviso("A data final não pode ser maior que a data inicial. ");
					datas = true;
				}

				if (dataFinal == null) {
					dataFinal = new Date();
				}
			}
		}else{
			datas = true;
		}

		if (StringUtils.isVazia(siglaProcesso)) {
			if (numProcesso == null) {
				processo = true;
			}
			else {
				reportarAviso("Ao pesquisar pelo processo é necessário inserir a Classe e o Número do Processo.");
				verificaDados = true;
			}
		} else {
			if (numProcesso == null) {
				reportarAviso("Ao pesquisar pelo processo é necessário inserir a Classe e o Número do Processo.");
				verificaDados = true;
			} else {
				siglaProcesso = siglaProcesso.trim();

			}
		}
		if (usuarioExterno && datas && processo && tipoComunicacao) {
			reportarAviso("Para continuar, é necessário também selecionar um Usuário Externo ou Período. ");
			verificaDados = true;
		}
		if (usuarioExterno && datas && processo && !tipoComunicacao) {
			reportarAviso("Para continuar, é necessário também selecionar um Usuário Externo ou Período. ");
			verificaDados = true;
		}


		if ((usuarioExterno && tipoComunicacao && datas && processo)
				|| verificaDados) {
			verificaDados = true;

			this.tipoProcessoIntegracao = null;
		}
		return verificaDados;
	}

	private List<SelectItem> carregarTipoProcessoIntegracao() {
		List<SelectItem> listaCombo = new ArrayList<SelectItem>();

		for (ProcessoIntegracaoEnum integracaoEnum : ProcessoIntegracaoEnum
				.values()) {
			listaCombo.add(new SelectItem(integracaoEnum.getCodigo(),
					integracaoEnum.getDescricao()));
		}

		return listaCombo;
	}

	private List<SelectItem> carregaUsuariosExternos() {
		List<SelectItem> listaCombo = new ArrayList<SelectItem>();
		listaCombo.add(new SelectItem(null, null));

		try {
			for (UsuarioExterno usuarioExterno : getUsuarioExternoService()
					.pesquisarOrgaosIntegracao()) {
				listaCombo.add(new SelectItem(usuarioExterno.getId(),
						usuarioExterno.getNome() + " - ("
								+ usuarioExterno.getUsrSistemas() + ")"));
			}
		} catch (ServiceException e) {
			reportarErro("Erro ao popular a lista.", e.getLocalizedMessage());
		}
		listaCombo.add(new SelectItem(null, null));

		return listaCombo;
	}

	private List<SelectItem> carregarTipoComunicacao() {
		List<SelectItem> listaCombo = new ArrayList<SelectItem>();
		listaCombo.add(new SelectItem("1", "Devolução/Baixa"));		
		listaCombo.add(new SelectItem("3", "Todos"));
		listaCombo.add(new SelectItem("2", "Notificação"));
		return listaCombo;
	}

	/** Getter´s e Setter´s **/

	public HtmlDataTable getTabelaProcessos() {
		return tabelaProcessos;
	}

	public void setTabelaProcessos(HtmlDataTable tabelaProcessos) {
		this.tabelaProcessos = tabelaProcessos;
	}

	public List<CheckableDataTableRowWrapper> getListaProcessos() {
		return listaProcessos;
	}

	public void setListaProcessos(
			List<CheckableDataTableRowWrapper> listaProcessos) {
		this.listaProcessos = listaProcessos;
	}

	public Integer getUsuarioExterno() {
		return usuarioExterno;
	}

	public List<SelectItem> getListaTipoProcessoIntegracao() {
		return listaTipoProcessoIntegracao;
	}

	public void setListaTipoProcessoIntegracao(
			List<SelectItem> listaTipoProcessoIntegracao) {
		this.listaTipoProcessoIntegracao = listaTipoProcessoIntegracao;
	}

	public String getTipoProcessoIntegracao() {
		return tipoProcessoIntegracao;
	}

	public void setTipoProcessoIntegracao(String tipoProcessoIntegracao) {
		this.tipoProcessoIntegracao = tipoProcessoIntegracao;
	}

	public void setUsuarioExterno(Integer usuarioExterno) {
		this.usuarioExterno = usuarioExterno;
	}

	public List<SelectItem> getListaUsuariosExternos() {
		return listaUsuariosExternos;
	}

	public void setListaUsuariosExternos(List<SelectItem> listaUsuariosExternos) {
		this.listaUsuariosExternos = listaUsuariosExternos;
	}

	public List<SelectItem> getListaTipoComunicacao() {
		return listaTipoComunicacao;
	}

	public void setListaTipoComunicacao(List<SelectItem> listaTipoComunicacao) {
		this.listaTipoComunicacao = listaTipoComunicacao;
	}

	public Integer getTipoComunicacao() {
		return tipoComunicacao;
	}

	public void setTipoComunicacao(Integer tipoComunicacao) {
		this.tipoComunicacao = tipoComunicacao;
	}

	public CheckableDataTableRowWrapper getProcessoIntegracao() {
		return processoIntegracao;
	}

	public void setProcessoIntegracao(
			CheckableDataTableRowWrapper processoIntegracao) {
		this.processoIntegracao = processoIntegracao;
	}

	

}
