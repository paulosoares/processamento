package br.gov.stf.estf.assinatura.visao.jsf.beans.integracao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.component.html.HtmlDataTable;

import br.gov.stf.estf.assinatura.visao.util.commons.NumberUtils;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanAndamentoProcesso extends AbstractBeanIntegracao {

	private static final long serialVersionUID = 937039474033005784L;
	
	private static final Long ANDAMENTOS[] = {7002L, 7101L, 7104L, 7108L};

	private static final Object LISTA_PROCESSO_ANDAMENTO = new Object();

	private static final Object LISTA_ANDAMENTOS = new Object();
	private static final Object LISTA_PROCESSOS_ORIGINARIO = new Object();

	private static final Object KEY_PROCESSO_ORIGINARIO = new Object();
	private static final Object KEY_ANDAMENTO_EXPEDITO = new Object();
	private static final Object KEY_OBSERVACAO = new Object();
	private static final Object KEY_ANDAMENTO = new Object();
	private static final Object ANDAMENTO_PROCESSO = new Object();

	private List<CheckableDataTableRowWrapper> listaProcessoAndamento;
	private HtmlDataTable tabelaProcessoAndamento;

	private List<SelectItem> listaAndamentos;
	private List<SelectItem> processosOriginarios;

	private CheckableDataTableRowWrapper andamentoProcesso;
	private Boolean processoOriginario;
	private Boolean andamentoExpedito;
	private Long andamento;
	private String observacao;

	public BeanAndamentoProcesso() {
		restauraSessao();
	}

	public void restauraSessao() {
		if (getAtributo(LISTA_ANDAMENTOS) == null) {
			setAtributo(LISTA_ANDAMENTOS, carregarAndamentos());
		}
		setListaAndamentos((List<SelectItem>) getAtributo(LISTA_ANDAMENTOS));

		if (getAtributo(LISTA_PROCESSOS_ORIGINARIO) == null) {
			setAtributo(LISTA_PROCESSOS_ORIGINARIO,
					carregaProcessosOriginarios());
		}
		setProcessosOriginarios((List<SelectItem>) getAtributo(LISTA_PROCESSOS_ORIGINARIO));

		if (getAtributo(LISTA_PROCESSO_ANDAMENTO) == null) {
			setAtributo(LISTA_PROCESSO_ANDAMENTO, listaProcessoAndamento);
		}

		setListaProcessoAndamento((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_PROCESSO_ANDAMENTO));

		if (getAtributo(KEY_PROCESSO_ORIGINARIO) == null) {
			setAtributo(KEY_PROCESSO_ORIGINARIO, processoOriginario);
		}
		setProcessoOriginario((Boolean) getAtributo(KEY_PROCESSO_ORIGINARIO));

		if (getAtributo(KEY_ANDAMENTO_EXPEDITO) == null) {
			setAtributo(KEY_ANDAMENTO_EXPEDITO, andamentoExpedito);
		}
		setAndamentoExpedito((Boolean) getAtributo(KEY_ANDAMENTO_EXPEDITO));

		if (getAtributo(KEY_ANDAMENTO) == null) {
			setAtributo(KEY_ANDAMENTO, andamento);
		}
		setAndamento((Long) getAtributo(KEY_ANDAMENTO));

		if (getAtributo(ANDAMENTO_PROCESSO) == null) {
			setAtributo(ANDAMENTO_PROCESSO, andamentoProcesso);
		}
		setAndamentoProcesso((CheckableDataTableRowWrapper) getAtributo(ANDAMENTO_PROCESSO));

		if (getAtributo(KEY_OBSERVACAO) == null) {
			setAtributo(KEY_OBSERVACAO, observacao);
		}
		setObservacao((String) getAtributo(KEY_OBSERVACAO));

		super.restaurarSessao();
	}

	@Override
	public void atualizarSessao() {
		setAtributo(LISTA_ANDAMENTOS, listaAndamentos);
		setAtributo(LISTA_PROCESSOS_ORIGINARIO, processosOriginarios);
		setAtributo(KEY_PROCESSO_ORIGINARIO, processoOriginario);
		setAtributo(KEY_ANDAMENTO, andamento);
		setAtributo(LISTA_PROCESSO_ANDAMENTO, listaProcessoAndamento);
		setAtributo(KEY_OBSERVACAO, observacao);
		setAtributo(ANDAMENTO_PROCESSO, andamentoProcesso);
		setAtributo(KEY_ANDAMENTO_EXPEDITO, andamentoExpedito);

		super.atualizarSessao();
	}

	@Override
	public void pesquisarAction() {
		pesquisar();
		if (listaProcessoAndamento == null || listaProcessoAndamento.size() == 0) {
			reportarAviso("Não foi encontrado nenhum registro.");
		} else { 
			reportarInformacao(listaProcessoAndamento.size() + " avisos encontrados.");
		}
		atualizarSessao();
	}

	public void limparSessaoModalAction() {
		limparSessaoModalAndamento();
		atualizarSessao();
	}

	public void marcarProcessoAction() {
		atualizarSessao();
	}

	public void inserirOrigemAction() {
		Processo processo = (Processo) getAndamentoProcessoWrapper().getObjetoIncidente().getPrincipal();
		inserirOrigem(processo);
		atualizarSessao();
	}

	@Override
	public void voltarAction() {
		super.voltar(getAndamentoProcessoWrapper().getObjetoIncidente());
		
		atualizarSessao();
	}
	
	@Override
	public void deslocaProcessoAction() {
		if (deslocaProcesso(getAndamentoProcessoWrapper())) {
			reportarInformacao("Deslocamento alterado com sucesso ! ");
			chamaProcInterop(getAndamentoProcessoWrapper().getObjetoIncidente());
			listaProcessoAndamento.remove(andamentoProcesso);
		}
		limparSessaoModalAndamento();
		
		atualizarSessao();
	}

	@Override
	public void abrirModal() throws ServiceException {
		this.andamentoProcesso = (CheckableDataTableRowWrapper) tabelaProcessoAndamento.getRowData();
		
		AndamentoProcesso andamentoProcessoEntity = getAndamentoProcessoService().recuperarPorId(getAndamentoProcessoWrapper().getId());

		listaDeslocaProcessos = carregarListaDeslocaProcesso(andamentoProcessoEntity.getObjetoIncidente());

		listaOrigens = carregarOrigensCadastradas(andamentoProcessoEntity.getObjetoIncidente());

		atualizarSessao();
	}

	// Metodos privados//

	private void limparSessaoModalAndamento(){
		super.limparSessaoModal();
		
		andamentoProcesso = null;
		setAtributo(KEY_ANDAMENTO_EXPEDITO, andamentoProcesso);
	}
	
	private List<SelectItem> carregarAndamentos() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		try {
			lista.add(new SelectItem(null, null));
			for (Long id : ANDAMENTOS) {
				Andamento andamento = getAndamentoService().recuperarPorId(id);
				if (andamento != null) {
					lista.add(new SelectItem(andamento.getId(), andamento.getIdentificacao()));
				}
			}

		} catch (ServiceException e) {
			reportarErro("Erro ao recuperar os andamentos.",
					e.getLocalizedMessage());
		}
		return lista;
	}

	private List<SelectItem> carregaProcessosOriginarios() {
		List<SelectItem> lista = new ArrayList<SelectItem>();

		lista.add(new SelectItem(null, null));
		lista.add(new SelectItem(true, "Sim"));
		lista.add(new SelectItem(false, "Não"));

		return lista;
	}

	private AndamentoProcesso getAndamentoProcessoWrapper() {
		return (AndamentoProcesso) this.andamentoProcesso.getWrappedObject();
	}

	private void pesquisar() {
		List<AndamentoProcesso> listaAndamento = null;
		String observacao = "";
		Long numProcesso = new Long(-1);
		if (StringUtils.isNotVazia(this.observacao)) {
			observacao = this.observacao.toUpperCase();
		}

		if ((andamentoExpedito != null && andamentoExpedito)
				&& (andamento != null)) {
			reportarAviso("Não é possível selecionar um anamento e selecionar, \"Pesquisar por processos Expeditos\".");
			return;
		}
		if (dataInicial != null || dataFinal != null) {
			if (dataInicial == null) {
				reportarAviso("Se informar uma data final é nescessário informar a data inicial. ");
				return ;
			} else {

				if (dataFinal != null && dataFinal.before(dataInicial)) {
					reportarAviso("A data final não pode ser maior que a data inicial. ");
					return;
				}

				if (dataFinal == null) {
					dataFinal = new Date();
				}
			}
		}
		
		if (processoOriginario != null) {
			if (StringUtils.isNotVazia(siglaProcesso)
					|| !NumberUtils.isNullOuMenorIgualZero(numProcesso)) {
				reportarAviso("Não é possivel filtrar por processos recursais e inserir uma sigla e número de processo ao mesmo tempo.");
				return;
			}
		}
		if (this.numProcesso != null) {
			numProcesso = this.numProcesso.longValue();
		}
		try {
			listaAndamento = getAndamentoProcessoService()
					.pesquisarAvisosNaoCriados(andamento, observacao,
							processoOriginario, dataInicial, dataFinal,
							andamentoExpedito, siglaProcesso, numProcesso);
		} catch (ServiceException e) {
			reportarErro("Erro ao realizara a pesquisa.");
			e.printStackTrace();
		}
		setListaProcessoAndamento(getCheckableDataTableRowWrapperList(listaAndamento));
	}
	// Getters and Setters //

	public Boolean getProcessoOriginario() {
		return processoOriginario;
	}

	public void setProcessoOriginario(Boolean processoOriginario) {
		this.processoOriginario = processoOriginario;
	}

	public Long getAndamento() {
		return andamento;
	}

	public void setAndamento(Long andamento) {
		this.andamento = andamento;
	}

	public List<SelectItem> getListaAndamentos() {
		return listaAndamentos;
	}

	public void setListaAndamentos(List<SelectItem> listaAndamentos) {
		this.listaAndamentos = listaAndamentos;
	}

	public List<SelectItem> getProcessosOriginarios() {
		return processosOriginarios;
	}

	public void setProcessosOriginarios(List<SelectItem> processosOriginarios) {
		this.processosOriginarios = processosOriginarios;
	}

	public List<CheckableDataTableRowWrapper> getListaProcessoAndamento() {
		return listaProcessoAndamento;
	}

	public void setListaProcessoAndamento(
			List<CheckableDataTableRowWrapper> listaProcessoAndamento) {
		this.listaProcessoAndamento = listaProcessoAndamento;
	}

	public HtmlDataTable getTabelaProcessoAndamento() {
		return tabelaProcessoAndamento;
	}

	public void setTabelaProcessoAndamento(HtmlDataTable tabelaProcessoAndamento) {
		this.tabelaProcessoAndamento = tabelaProcessoAndamento;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public CheckableDataTableRowWrapper getAndamentoProcesso() {
		return andamentoProcesso;
	}

	public void setAndamentoProcesso(
			CheckableDataTableRowWrapper andamentoProcesso) {
		this.andamentoProcesso = andamentoProcesso;
	}

	public Boolean getAndamentoExpedito() {
		return andamentoExpedito;
	}

	public void setAndamentoExpedito(Boolean andamentoExpedito) {
		this.andamentoExpedito = andamentoExpedito;
	}

}
