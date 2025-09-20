package br.gov.stf.estf.assinatura.visao.jsf.beans.integracao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.component.html.HtmlDataTable;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.OrigemDestino;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.HistoricoProcessoOrigem;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Orgao;
import br.gov.stf.estf.entidade.processostf.Procedencia;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.TipoHistorico;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public abstract class AbstractBeanIntegracao extends AssinadorBaseBean {

	private static final long serialVersionUID = 8142135945208518236L;

	protected static final String ENVIADO_POR_MIDIA = "Enviado por mídia.";
	protected static final String ENVIADO_POR_MIDIA_ORGAO_NAO_INTEGRADO =  "Enviado por mídia, órgão não integrado.";
	protected static final Integer[] TIPOS_COMUNICACAO = { 101, 102, 103 };

	private static Object KEY_DATA_INICIAL = new Object();
	private static Object KEY_DATA_FINAL = new Object();
	private static Object KEY_NUM_PROCESSO = new Object();
	private static Object KEY_SIGLA_PROCESSO = new Object();

	protected Date dataInicial;
	protected Date dataFinal;
	protected Integer numProcesso;
	protected String siglaProcesso;

	// Modal//
	private static Object LISTA_MODAL_DESLOCA_PROCESSOS = new Object();
	private static Object LISTA_MODAL_ORGAOS = new Object();
	private static Object LISTA_MODAL_PROCEDENCIA = new Object();
	private static Object LISTA_MODAL_ORIGEM = new Object();

	private static Object LISTA_ORIGEM = new Object();

	private static Object KEY_MODAL_ORGAO = new Object();
	private static Object KEY_MODAL_PROCEDENCIA = new Object();
	private static Object KEY_MODAL_ORIGEM = new Object();

	private static Object KEY_MODAL_SIGLA_ORIGEM = new Object();
	private static Object KEY_MODAL_NUMERO_ORIGEM = new Object();
	private static Object KEY_MODAL_NUMERO_UNICO = new Object();
	private static Object KEY_MODAL_ORIGEM_CHECK = new Object();
	private static Object KEY_MODAL_DESLOCA_PROCESSO_CHECK = new Object();
	private static Object KEY_DESLOCA_PROCESSO = new Object();
	private static Object KEY_EDITANDO = new Object();

	private List<SelectItem> listaModalOrgao;
	private List<SelectItem> listaModalProcedencia;
	private List<SelectItem> listaModalOrigem;

	protected List<HistoricoProcessoOrigem> listaOrigens;

	protected List<DeslocaProcesso> listaDeslocaProcessos;

	protected HtmlDataTable tabelaOrigens;
	protected HtmlDataTable tabelaDeslocaProcessos;

	protected Long modalOrgao;
	protected Long modalProcedencia;
	protected Long modalOrigem;

	protected String modalSiglaOrigem;
	protected String modalNumeroOrigem;
	protected String modalNumeroUnico;
	protected Integer modalOrigemCheck;
	protected Integer modalDeslocaProcessoCheck;

	protected DeslocaProcesso deslocaProcesso;

	protected Boolean editando;

	public void restaurarSessao() {
		if (getAtributo(KEY_DATA_INICIAL) == null) {
			setAtributo(KEY_DATA_INICIAL, dataInicial);
		}
		setDataInicial((Date) getAtributo(KEY_DATA_INICIAL));

		if (getAtributo(KEY_DATA_FINAL) == null) {
			setAtributo(KEY_DATA_FINAL, dataFinal);
		}
		setDataFinal((Date) getAtributo(KEY_DATA_FINAL));

		if (getAtributo(KEY_SIGLA_PROCESSO) == null) {
			setAtributo(KEY_SIGLA_PROCESSO, siglaProcesso);
		}
		setSiglaProcesso((String) getAtributo(KEY_SIGLA_PROCESSO));

		if (getAtributo(KEY_NUM_PROCESSO) == null) {
			setAtributo(KEY_NUM_PROCESSO, numProcesso);
		}
		setNumProcesso((Integer) getAtributo(KEY_NUM_PROCESSO));

		if (getAtributo(LISTA_MODAL_ORGAOS) == null) {
			setAtributo(LISTA_MODAL_ORGAOS, carregarOrgaos());
		}

		setListaModalOrgao((List<SelectItem>) getAtributo(LISTA_MODAL_ORGAOS));

		if (getAtributo(LISTA_MODAL_PROCEDENCIA) == null) {
			setAtributo(LISTA_MODAL_PROCEDENCIA, carregarProcedencias());
		}

		setListaModalProcedencia((List<SelectItem>) getAtributo(LISTA_MODAL_PROCEDENCIA));

		if (getAtributo(LISTA_MODAL_ORIGEM) == null) {
			setAtributo(LISTA_MODAL_ORIGEM, carregarOrigensDevolucao());
		}

		setListaModalOrigem((List<SelectItem>) getAtributo(LISTA_MODAL_ORIGEM));

		if (getAtributo(KEY_MODAL_ORGAO) == null) {
			setAtributo(KEY_MODAL_ORGAO, modalOrigem);
		}

		setModalOrgao((Long) getAtributo(KEY_MODAL_ORGAO));

		if (getAtributo(KEY_MODAL_PROCEDENCIA) == null) {
			setAtributo(KEY_MODAL_PROCEDENCIA, modalProcedencia);
		}

		setModalProcedencia((Long) getAtributo(KEY_MODAL_PROCEDENCIA));

		if (getAtributo(KEY_MODAL_ORIGEM) == null) {
			setAtributo(KEY_MODAL_ORIGEM, modalOrigem);
		}
		setModalOrigem((Long) getAtributo(KEY_MODAL_ORIGEM));

		if (getAtributo(KEY_MODAL_NUMERO_ORIGEM) == null) {
			setAtributo(KEY_MODAL_NUMERO_ORIGEM, modalNumeroOrigem);
		}
		setModalNumeroOrigem((String) getAtributo(KEY_MODAL_NUMERO_ORIGEM));

		if (getAtributo(KEY_MODAL_NUMERO_UNICO) == null) {
			setAtributo(KEY_MODAL_NUMERO_UNICO, modalNumeroUnico);
		}
		setModalNumeroUnico((String) getAtributo(KEY_MODAL_NUMERO_UNICO));

		if (getAtributo(KEY_MODAL_SIGLA_ORIGEM) == null) {
			setAtributo(KEY_MODAL_SIGLA_ORIGEM, modalSiglaOrigem);
		}
		setModalSiglaOrigem((String) getAtributo(KEY_MODAL_SIGLA_ORIGEM));

		if (getAtributo(KEY_MODAL_ORIGEM_CHECK) == null) {
			setAtributo(KEY_MODAL_ORIGEM_CHECK, modalOrigemCheck);
		}
		setModalOrigemCheck((Integer) getAtributo(KEY_MODAL_ORIGEM_CHECK));

		if (getAtributo(LISTA_ORIGEM) == null) {
			setAtributo(LISTA_ORIGEM, listaOrigens);
		}
		setListaOrigens((List<HistoricoProcessoOrigem>) getAtributo(LISTA_ORIGEM));

		if (getAtributo(LISTA_MODAL_DESLOCA_PROCESSOS) == null) {
			setAtributo(LISTA_MODAL_DESLOCA_PROCESSOS, listaDeslocaProcessos);
		}
		setListaDeslocaProcessos((List<DeslocaProcesso>) getAtributo(LISTA_MODAL_DESLOCA_PROCESSOS));

		if (getAtributo(KEY_MODAL_DESLOCA_PROCESSO_CHECK) == null) {
			setAtributo(KEY_MODAL_DESLOCA_PROCESSO_CHECK,
					modalDeslocaProcessoCheck);
		}
		setModalDeslocaProcessoCheck((Integer) getAtributo(KEY_MODAL_DESLOCA_PROCESSO_CHECK));

		if (getAtributo(KEY_DESLOCA_PROCESSO) == null) {
			setAtributo(KEY_DESLOCA_PROCESSO, deslocaProcesso);
		}
		setDeslocaProcesso((DeslocaProcesso) getAtributo(KEY_DESLOCA_PROCESSO));

		if (getAtributo(KEY_EDITANDO) == null) {
			setAtributo(KEY_EDITANDO, editando);
		}
		setEditando((Boolean) getAtributo(KEY_EDITANDO));

	}

	public void atualizarSessao() {
		setAtributo(KEY_DATA_INICIAL, dataInicial);
		setAtributo(KEY_DATA_FINAL, dataFinal);
		setAtributo(KEY_SIGLA_PROCESSO, siglaProcesso);
		setAtributo(KEY_NUM_PROCESSO, numProcesso);

		// Modal//

		setAtributo(LISTA_MODAL_ORGAOS, listaModalOrgao);
		setAtributo(LISTA_MODAL_PROCEDENCIA, listaModalProcedencia);
		setAtributo(LISTA_MODAL_ORIGEM, listaModalOrigem);
		if (listaOrigens != null) {
			setAtributo(LISTA_ORIGEM, listaOrigens);
		}

		setAtributo(KEY_MODAL_ORGAO, modalOrgao);
		if (modalProcedencia != null) {
			setAtributo(KEY_MODAL_PROCEDENCIA, modalProcedencia);
		}
		setAtributo(KEY_MODAL_ORIGEM, modalOrigem);

		setAtributo(KEY_MODAL_ORIGEM_CHECK, modalOrigemCheck);
		setAtributo(KEY_MODAL_NUMERO_ORIGEM, modalNumeroOrigem);
		setAtributo(KEY_MODAL_SIGLA_ORIGEM, modalSiglaOrigem);
		setAtributo(KEY_MODAL_NUMERO_UNICO, modalNumeroUnico);

		if (listaDeslocaProcessos != null) {
			setAtributo(LISTA_MODAL_DESLOCA_PROCESSOS, listaDeslocaProcessos);
		}

		setModalDeslocaProcessoCheck((Integer) getAtributo(KEY_MODAL_DESLOCA_PROCESSO_CHECK));
		setAtributo(KEY_DESLOCA_PROCESSO, deslocaProcesso);
		if (editando != null) {
			setAtributo(KEY_EDITANDO, editando);
		}

	}

	public void excluirOrigemAction() {
		excluirOrigem();
		atualizarSessao();
	}

	public void alterarDeslocaProcessoAction() {
		alterarDeslocaProcesso();
		atualizarSessao();
	}

	public abstract void voltarAction();

	public abstract void pesquisarAction();

	// Metodos da tela modal//
	public boolean getIsOrigemIntegrada() {
		HistoricoProcessoOrigem historicoOrigem = (HistoricoProcessoOrigem) tabelaOrigens
				.getRowData();

		try {
			Boolean isIntegrada = getOrigemService().isOrigemIntegrada(
					historicoOrigem.getOrigem());
			return isIntegrada.booleanValue();

		} catch (ServiceException e) {
			reportarErro("Erro ao verificar se a origem está integrada: "
					+ e.getMessage());
			return false;
		}
	}

	public boolean getOrigemEstaIntegrada() {
		if (modalOrigemCheck != null) {
			try {
				Origem origem = listaOrigens.get(modalOrigemCheck).getOrigem();
				Boolean isIntegrada = getOrigemService().isOrigemIntegrada(
						origem);

				return isIntegrada.booleanValue();
			} catch (ServiceException e) {
				reportarErro("Erro ao verificar se a origem está integrada: "
						+ e.getMessage());
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean getVerificaNumUnico() {
		if (modalOrigemCheck != null) {
			HistoricoProcessoOrigem historicoProcessoOrigem = listaOrigens
					.get(modalOrigemCheck);

			return !StringUtils.isVazia(historicoProcessoOrigem
					.getNumeroUnicoProcesso());
		}
		return false;

	}

	public boolean getVerificaNumOrigem() {
		if (modalOrigemCheck != null) {
			HistoricoProcessoOrigem historicoProcessoOrigem = listaOrigens
					.get(modalOrigemCheck);
			return !StringUtils.isVazia(historicoProcessoOrigem
					.getNumeroProcessoOrigem());
		}
		return false;
	}

	public List<SelectItem> getListaOrigensSelectItens() {

		ArrayList<SelectItem> origensCadastradasSelectItems = new ArrayList<SelectItem>();
		if (listaOrigens != null) {
			for (int i = 0; i < listaOrigens.size(); i++) {
				origensCadastradasSelectItems.add(new SelectItem(i, ""));
			}
		}

		return origensCadastradasSelectItems;
	}

	public int getIndexOrigemCadastrada() {
		return tabelaOrigens.getRowIndex();
	}

	public boolean getTemOrigemSelecionada() {
		return modalOrigemCheck != null;
	}

	public void escolherOrigemPrincipal() {
		try {
			HistoricoProcessoOrigem historicoOrigemSelecionado = (HistoricoProcessoOrigem) tabelaOrigens
					.getRowData();
			historicoOrigemSelecionado.setPrincipal(true);
			getHistoricoProcessoOrigemService().alterar(
					historicoOrigemSelecionado);

			// Setar as outras origens como não principal.
			for (HistoricoProcessoOrigem origem : listaOrigens) {
				if (!origem.getId().equals(historicoOrigemSelecionado.getId())
						&& origem.getPrincipal()) {
					origem.setPrincipal(false);
					getHistoricoProcessoOrigemService().alterar(origem);
				}
			}

		} catch (ServiceException e1) {

			reportarErro("Erro ao selecionar a origem como principal.");
		}
	}

	public abstract void deslocaProcessoAction();

	public abstract void abrirModal() throws ServiceException;

	public List<SelectItem> getListaDeslocaProcessoSelectItem() {
		ArrayList<SelectItem> deslocaProcessosItem = new ArrayList<SelectItem>();

		if (listaDeslocaProcessos != null) {
			for (int i = 0; i < listaDeslocaProcessos.size(); i++) {
				deslocaProcessosItem.add(new SelectItem(i, ""));
			}
		}

		return deslocaProcessosItem;
	}

	public String getDescricaoOrigem() {

		OrigemDestino origemDestino = new OrigemDestino();
		try {
			DeslocaProcesso deslocaProcesso = (DeslocaProcesso) tabelaDeslocaProcessos
					.getRowData();
			origemDestino = getOrigemDestinoService().recuperarPorId(
					deslocaProcesso.getId().getCodigoOrgaoOrigem());
		} catch (ServiceException e) {
			reportarErro("Erro ao realizar recuperar a origem.",
					e.getLocalizedMessage());
		}
		return origemDestino.getDescricao();
	}

	public String getDescricaoDestino() {
		DeslocaProcesso deslocaProcesso = (DeslocaProcesso) tabelaDeslocaProcessos
				.getRowData();
		Origem origem = new Origem();
		try {
			origem = getOrigemService().recuperarPorId(
					deslocaProcesso.getCodigoOrgaoDestino());
		} catch (ServiceException e) {
			reportarErro("Erro ao realizar recuperar o destino.",
					e.getLocalizedMessage());
		}
		return origem.getDescricao();
	}

	public int getIndexDeslocaProcesso() {
		return tabelaDeslocaProcessos.getRowIndex();
	}

	public boolean getConfirmarBaixa() {
		if (modalDeslocaProcessoCheck != null) {
			DeslocaProcesso deslocaProcesso = listaDeslocaProcessos
					.get(modalDeslocaProcessoCheck);
			Boolean isIntegrada = new Boolean(false);
			Boolean numeroCadastrado = new Boolean(false);
			try {
				Origem origem = getOrigemService().recuperarPorId(
						deslocaProcesso.getCodigoOrgaoDestino());
				isIntegrada = getOrigemService().isOrigemIntegrada(origem);
				for (HistoricoProcessoOrigem historicoProcessoOrigem : listaOrigens) {
					if (historicoProcessoOrigem.getOrigem().getId()
							.equals(deslocaProcesso.getCodigoOrgaoDestino())) {
						numeroCadastrado = true;
					}
				}
			} catch (ServiceException e) {
				reportarErro("Erro ao descobrir se a origem está integrada.");
			}
			if (isIntegrada && numeroCadastrado) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean getTemDeslocaProcessoSelecionado() {
		return modalDeslocaProcessoCheck != null;
	}

	public boolean getNumeroDestinoEstaIntegrada() {
		DeslocaProcesso deslocaProcesso = (DeslocaProcesso) tabelaDeslocaProcessos
				.getRowData();
		for (HistoricoProcessoOrigem historicoProcessoOrigem : listaOrigens) {
			if (historicoProcessoOrigem.getOrigem().getId()
					.equals(deslocaProcesso.getCodigoOrgaoDestino())) {
				return true;
			}
		}
		return false;
	}

	public boolean getDestinoEstaIntegrada() {
		DeslocaProcesso deslocaProcesso = (DeslocaProcesso) tabelaDeslocaProcessos
				.getRowData();
		try {
			Origem origem = getOrigemService().recuperarPorId(
					deslocaProcesso.getCodigoOrgaoDestino());
			Boolean isIntegrada = getOrigemService().isOrigemIntegrada(origem);

			return isIntegrada.booleanValue();
		} catch (ServiceException e) {
			reportarErro("Erro ao verificar se o destino está integrada: "
					+ e.getMessage());
			return false;
		}
	}

	public void abrirBotaoEditar() {
		deslocaProcesso = (DeslocaProcesso) tabelaDeslocaProcessos.getRowData();
		modalDeslocaProcessoCheck = tabelaDeslocaProcessos.getRowIndex();
		editando = true;
		atualizarSessao();
	}

	// Metodos protegidos//

	protected void voltar(ObjetoIncidente<?> objetoIncidente) {
		if (!listaDeslocaProcessos.isEmpty()) {
			listaDeslocaProcessos.clear();
		}
		carregarListaDeslocaProcesso(objetoIncidente);

		deslocaProcesso = null;
		setAtributo(KEY_DESLOCA_PROCESSO, deslocaProcesso);

		modalDeslocaProcessoCheck = null;
		setAtributo(KEY_MODAL_DESLOCA_PROCESSO_CHECK, modalDeslocaProcessoCheck);
		editando = false;
	}

	protected boolean deslocaProcesso(AndamentoProcesso andamentoProcesso) {

		try {
			Origem origem = getOrigemService().recuperarPorId(deslocaProcesso.getCodigoOrgaoDestino());
			getDeslocaProcessoService().alterarDeslocaProcessosPorAndamento(deslocaProcesso, andamentoProcesso,getSetorUsuarioAutenticado(), origem);
		} catch (ServiceException e) {
			reportarAviso("Erro ao alterar o deslocamento.",e.getLocalizedMessage());
			return false;
		}

		return true;
	}

	private void alterarDeslocaProcesso() {
		if (modalOrigemCheck != null) {
			
			DeslocaProcesso deslocaProcesso = (DeslocaProcesso) tabelaDeslocaProcessos.getRowData();
			HistoricoProcessoOrigem historicoProcessoOrigem = listaOrigens.get(modalOrigemCheck);
			deslocaProcesso.setCodigoOrgaoDestino(historicoProcessoOrigem.getOrigem().getId());

			if (!listaDeslocaProcessos.isEmpty()) {
				listaDeslocaProcessos.clear();
			}
			listaDeslocaProcessos.add(deslocaProcesso);

			// Após limpar a lista ele seleciona o unico desloca processo
			// existente.
			modalDeslocaProcessoCheck = 0;
			setAtributo(KEY_MODAL_DESLOCA_PROCESSO_CHECK,modalDeslocaProcessoCheck);
			editando = false;
		}
	}

	private void initModalVerificarOrigem() {
		modalOrgao = null;
		modalProcedencia = null;
		modalOrigem = null;
		listaModalProcedencia.clear();
		listaModalOrigem.clear();
		modalNumeroOrigem = "";
		modalSiglaOrigem = "";
		modalNumeroUnico = "";
		atualizarSessao();
	}

	private List<SelectItem> carregarOrgaos() {
		List<SelectItem> orgaos = new ArrayList<SelectItem>();
		orgaos.add(new SelectItem(null, ""));

		try {
			for (Orgao orgao : getOrgaoService().pesquisarOrgaosAtivos()) {
				orgaos.add(new SelectItem(orgao.getId(), orgao.getDescricao()));
			}
		} catch (ServiceException e) {
			reportarErro("Erro ao pesquisar órgãos.", e.getMessage());
		}

		setAtributo(LISTA_MODAL_ORGAOS, orgaos);
		return orgaos;
	}

	private List<SelectItem> carregarProcedencias() {
		List<SelectItem> procedencias = new ArrayList<SelectItem>();

		procedencias.add(new SelectItem(null, ""));

		if (modalOrgao != null) {
			try {
				Orgao orgao = getOrgaoService().recuperarPorId(this.modalOrgao);

				for (Procedencia procedencia : getProcedenciaService()
						.pesquisarProcedenciasComOrigemAtiva(orgao)) {
					procedencias.add(new SelectItem(procedencia.getId(),
							procedencia.getSiglaProcedencia()));
				}
			} catch (ServiceException e) {
				reportarErro("Erro ao pesquisar procedências.", e.getMessage());
			}
		}

		setListaModalProcedencia(procedencias);
		return procedencias;
	}

	private List<SelectItem> carregarOrigensDevolucao() {
		List<SelectItem> origensDevolucao = new ArrayList<SelectItem>();
		origensDevolucao.add(new SelectItem(null, ""));

		if (modalOrgao != null && modalProcedencia != null) {
			try {
				Orgao orgao = getOrgaoService().recuperarPorId(modalOrgao);
				Procedencia procedencia = getProcedenciaService()
						.recuperarPorId(modalProcedencia);

				for (Origem origem : getOrigemService().pesquisarOrigensAtivas(
						orgao, procedencia)) {
					if (getOrigemService().isOrigemIntegrada(origem)) {
						origensDevolucao.add(new SelectItem(origem.getId(),
								origem.getDescricao()));
					}
				}
			} catch (ServiceException e) {
				reportarErro("Erro ao pesquisar origens.", e.getMessage());
			}
		}

		setListaModalOrigem(origensDevolucao);

		return origensDevolucao;
	}

	protected List<DeslocaProcesso> carregarListaDeslocaProcesso(
			ObjetoIncidente<?> objetoIncidente) {
		listaDeslocaProcessos = new ArrayList<DeslocaProcesso>();

		try {
			objetoIncidente = getObjetoIncidenteService().recuperarPorId(
					objetoIncidente.getId());
			Processo processo = (Processo) objetoIncidente.getPrincipal();
			listaDeslocaProcessos = getDeslocaProcessoService()
					.recuperaPorProcessoOrigemExterna(processo);
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		setAtributo(LISTA_MODAL_DESLOCA_PROCESSOS, listaDeslocaProcessos);
		return listaDeslocaProcessos;
	}

	protected void inserirOrigem(ObjetoIncidente<?> objetoIncidente) {

		HistoricoProcessoOrigem hpo = new HistoricoProcessoOrigem();

		try {

			Procedencia procedencia = getProcedenciaService().recuperarPorId(
					modalProcedencia);
			Origem origem = getOrigemService().recuperarPorId(modalOrigem);

			hpo.setProcedencia(procedencia);
			hpo.setOrigem(origem);
			hpo.setOrigemEletronica(false);
			hpo.setProcessoInicial(false);
			hpo.setPrincipal(false);
			hpo.setTipoHistorico(TipoHistorico.ORIGEM);
			hpo.setObjetoIncidente(objetoIncidente.getId());
			hpo.setNumeroProcessoOrigem(modalNumeroOrigem);
			hpo.setSiglaClasseOrigem(modalSiglaOrigem);
			hpo.setNumeroUnicoProcesso(modalNumeroUnico);

			getHistoricoProcessoOrigemService().incluir(hpo);

			listaOrigens.add(hpo);

			initModalVerificarOrigem();

		} catch (ServiceException ex) {
			reportarErro("Erro ao adicionar origem. " + ex.getMessage());
		}
	}

	protected void excluirOrigem() {
		HistoricoProcessoOrigem historicoOrigem = (HistoricoProcessoOrigem) tabelaOrigens
				.getRowData();

		try {
			getHistoricoProcessoOrigemService().excluir(historicoOrigem);
			listaOrigens.remove(historicoOrigem);
		} catch (ServiceException ex) {
			reportarErro("Erro ao excluir origem: " + ex.getMessage());
		}
	}

	protected void limparSessaoModal() {
		modalNumeroOrigem = null;
		modalNumeroUnico = null;
		modalOrgao = null;
		modalOrigem = null;
		modalOrigemCheck = null;
		modalProcedencia = null;

		modalSiglaOrigem = null;

		listaOrigens = null;
		setAtributo(LISTA_ORIGEM, listaOrigens);

		listaModalOrigem.clear();
		listaModalProcedencia.clear();

		listaDeslocaProcessos = null;
		setAtributo(LISTA_MODAL_DESLOCA_PROCESSOS, listaDeslocaProcessos);

		modalDeslocaProcessoCheck = null;
		setAtributo(KEY_MODAL_DESLOCA_PROCESSO_CHECK, modalDeslocaProcessoCheck);

		deslocaProcesso = null;
		setAtributo(KEY_DESLOCA_PROCESSO, deslocaProcesso);
	}

	protected List<HistoricoProcessoOrigem> carregarOrigensCadastradas(
			ObjetoIncidente<?> objetoIncidente) {
		listaOrigens = new ArrayList<HistoricoProcessoOrigem>();
		if (objetoIncidente != null) {
			Processo processo = (Processo) objetoIncidente.getPrincipal();
			try {
				listaOrigens = getHistoricoProcessoOrigemService()
						.recuperarPorObjetoIncidente(processo.getId());
			} catch (ServiceException e) {
				reportarErro("Erro ao pesquisar as origens cadastradas.",
						e.getMessage());
			}
		}
		setAtributo(LISTA_ORIGEM, listaOrigens);
		return listaOrigens;
	}

	protected boolean chamaProcInterop(ObjetoIncidente<?> objetoIncidente) {

		try {
			getProcessoIntegracaoService().chamaPrcPreparaInterop(
					objetoIncidente);
			return true;
		} catch (ServiceException e) {
			reportarErro("Erro ao chama a procedure.");
			return false;
		}
	}

	// Getters and Setters//
	public Boolean getEditando() {
		if (editando == null) {
			return false;
		}
		return editando.booleanValue();
	}

	public void setEditando(Boolean editando) {
		this.editando = editando;
	}

	public Integer getModalDeslocaProcessoCheck() {
		return modalDeslocaProcessoCheck;
	}

	public void setModalDeslocaProcessoCheck(Integer modalDeslocaProcessoCheck) {
		this.modalDeslocaProcessoCheck = modalDeslocaProcessoCheck;
		if (modalDeslocaProcessoCheck != null) {
			setAtributo(KEY_MODAL_DESLOCA_PROCESSO_CHECK,
					modalDeslocaProcessoCheck);
			if (listaDeslocaProcessos != null
					&& !listaDeslocaProcessos.isEmpty()) {
				setDeslocaProcesso(listaDeslocaProcessos
						.get(modalDeslocaProcessoCheck));
				setAtributo(KEY_DESLOCA_PROCESSO, deslocaProcesso);
			}
		}
	}

   public void marcarOuDesmarcarTodasRich(List<CheckableDataTableRowWrapper> listaCheck, HtmlDataTable tabela)
    {
      if ((listaCheck != null) && (listaCheck.size() > 0))
        if (tabela != null) {
          int tamanho = tabela.getRows() + tabela.getFirst();
          for (int i = 0; i < tamanho; ++i)
            if (i >= tabela.getFirst()) {
              if (i >= listaCheck.size()) break;
              CheckableDataTableRowWrapper check = (CheckableDataTableRowWrapper)listaCheck.get(i);
                if (check.getChecked())
                  check.setChecked(false);
                else
                 check.setChecked(true);
              }
       }
       else
       {
         marcarOuDesmarcarTodas(listaCheck);
       }
    }
   
	public void setModalOrgao(Long modalOrgao) {
		this.modalOrgao = modalOrgao;

		if (this.modalOrgao != null) {
			carregarProcedencias();
			modalProcedencia = null;
			modalOrigem = null;
			atualizarSessao();
		} else {
			initModalVerificarOrigem();
		}
	}

	public Long getModalProcedencia() {
		return modalProcedencia;
	}

	public void setModalProcedencia(Long modalProcedencia) {
		this.modalProcedencia = modalProcedencia;

		if (this.modalProcedencia != null) {
			carregarOrigensDevolucao();
			modalOrigem = null;
			atualizarSessao();
		}
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Integer getNumProcesso() {
		return numProcesso;
	}

	public void setNumProcesso(Integer numProcesso) {
		this.numProcesso = numProcesso;
	}

	public String getSiglaProcesso() {
		return siglaProcesso;
	}

	public void setSiglaProcesso(String siglaProcesso) {
		this.siglaProcesso = siglaProcesso;
	}

	public List<SelectItem> getListaModalOrgao() {
		return listaModalOrgao;
	}

	public void setListaModalOrgao(List<SelectItem> listaModalOrgao) {
		this.listaModalOrgao = listaModalOrgao;
	}

	public List<SelectItem> getListaModalProcedencia() {
		return listaModalProcedencia;
	}

	public void setListaModalProcedencia(List<SelectItem> listaModalProcedencia) {
		this.listaModalProcedencia = listaModalProcedencia;
	}

	public List<SelectItem> getListaModalOrigem() {
		return listaModalOrigem;
	}

	public void setListaModalOrigem(List<SelectItem> listaModalOrigem) {
		this.listaModalOrigem = listaModalOrigem;
	}

	public List<HistoricoProcessoOrigem> getListaOrigens() {
		return listaOrigens;
	}

	public void setListaOrigens(List<HistoricoProcessoOrigem> listaOrigens) {
		this.listaOrigens = listaOrigens;
	}

	public HtmlDataTable getTabelaOrigens() {
		return tabelaOrigens;
	}

	public void setTabelaOrigens(HtmlDataTable tabelaOrigens) {
		this.tabelaOrigens = tabelaOrigens;
	}

	public Long getModalOrigem() {
		return modalOrigem;
	}

	public void setModalOrigem(Long modalOrigem) {
		this.modalOrigem = modalOrigem;
	}

	public String getModalSiglaOrigem() {
		return modalSiglaOrigem;
	}

	public void setModalSiglaOrigem(String modalSiglaOrigem) {
		this.modalSiglaOrigem = modalSiglaOrigem;
	}

	public String getModalNumeroOrigem() {
		return modalNumeroOrigem;
	}

	public void setModalNumeroOrigem(String modalNumeroOrigem) {
		this.modalNumeroOrigem = modalNumeroOrigem;
	}

	public String getModalNumeroUnico() {
		return modalNumeroUnico;
	}

	public void setModalNumeroUnico(String modalNumeroUnico) {
		this.modalNumeroUnico = modalNumeroUnico;
	}

	public Integer getModalOrigemCheck() {
		return modalOrigemCheck;
	}

	public void setModalOrigemCheck(Integer modalOrigemCheck) {
		this.modalOrigemCheck = modalOrigemCheck;
	}

	public Long getModalOrgao() {
		return modalOrgao;
	}

	public List<DeslocaProcesso> getListaDeslocaProcessos() {
		return listaDeslocaProcessos;
	}

	public void setListaDeslocaProcessos(
			List<DeslocaProcesso> listaDeslocaProcessos) {
		this.listaDeslocaProcessos = listaDeslocaProcessos;
	}

	public HtmlDataTable getTabelaDeslocaProcessos() {
		return tabelaDeslocaProcessos;
	}

	public void setTabelaDeslocaProcessos(HtmlDataTable tabelaDeslocaProcessos) {
		this.tabelaDeslocaProcessos = tabelaDeslocaProcessos;
	}

	public DeslocaProcesso getDeslocaProcesso() {
		return deslocaProcesso;
	}

	public void setDeslocaProcesso(DeslocaProcesso deslocaProcesso) {
		this.deslocaProcesso = deslocaProcesso;
	}

}
