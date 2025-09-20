package br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.andamento;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.faces.event.ActionEvent;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.ProcessoParser;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanRegistrarAndamentoT extends AssinadorBaseBean{
	
	private static final Object OBJETOINCIDENTE = new Object();
	private static final Object LISTA_ANDAMENTO_PROCESSO= new Object();
	private static final Object PROCESSO = new Object();
	private static final Object OBSERVACAO = new Object();
	private static final Object ANDAMENTO_PROCESSO = new Object();
	
	private String siglaNumeroProcesso;
	private Processo processo;
	private AndamentoProcesso andamentoProcessoSelecionado;
	private Long objetoIncidente;
	private String obsAndamento;
	private List<CheckableDataTableRowWrapper> listaAndamentoProcesso;
	private List<AndamentoProcesso> andamentosProcessoTotal;
	
	private static List<String> classes;
	private org.richfaces.component.html.HtmlDataTable tabelaProcessoAndamento;
	
	
	
	private void restaurarSessao() {
		
		if (getAtributo(OBJETOINCIDENTE) == null) {
			setAtributo(OBJETOINCIDENTE, objetoIncidente);
		}
		setObjetoIncidente((Long) getAtributo(OBJETOINCIDENTE));
		
		if (getAtributo(ANDAMENTO_PROCESSO) == null) {
			setAtributo(ANDAMENTO_PROCESSO, new AndamentoProcesso());
		}
		setAndamentoProcessoSelecionado((AndamentoProcesso) getAtributo(ANDAMENTO_PROCESSO));
		
		if (getAtributo(OBSERVACAO) == null) {
			setAtributo(OBSERVACAO, "");
		}
		setObsAndamento((String) getAtributo(OBSERVACAO));
		
		if (getAtributo(LISTA_ANDAMENTO_PROCESSO) == null) {
			setAtributo(LISTA_ANDAMENTO_PROCESSO, new ArrayList<CheckableDataTableRowWrapper>());
		} else {
			setListaAndamentoProcesso((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_ANDAMENTO_PROCESSO));
		}
		
		if (getAtributo(PROCESSO) == null) {
			setAtributo(PROCESSO, processo);
		}
		setProcesso((Processo) getAtributo(PROCESSO));
	}
	
	
	public BeanRegistrarAndamentoT(){
		restaurarSessao();
	}
	
	
	public void atualizaSessaoAction(ActionEvent evt){
		atualizaSessao();
	}
	
	
	public void limparTelaAction(ActionEvent evt){
		limparTela();
		atualizaSessao();
	}
	
	
	public void atualizaSessao() {
		setAtributo(LISTA_ANDAMENTO_PROCESSO, listaAndamentoProcesso);
		setAtributo(PROCESSO, processo);
		setAtributo(OBSERVACAO, obsAndamento);
		setAtributo(ANDAMENTO_PROCESSO, andamentoProcessoSelecionado);
	}
	
	public void limparTela(){
		setProcesso(null);
		setListaAndamentoProcesso(null);
		setObsAndamento("");
		setAndamentoProcessoSelecionado(null);
		setSiglaNumeroProcesso("");
		atualizaSessao();
	}
	
	
	public void procuraAndamentoProcessoPeloID(){
		try {
			getAndamentoProcessoService().limparSessao();
			andamentosProcessoTotal = getAndamentoProcessoService().pesquisarAndamentoProcesso(processo.getSiglaClasseProcessual(), processo.getNumeroProcessual());
			listaAndamentoProcesso = new ArrayList<CheckableDataTableRowWrapper>();
		} catch (Exception e) {
			e.printStackTrace();
			reportarErro("erro ao pesquisa os andamentos");
		}
		preencherResumoAndamentos();
	}
	
	
	public void recuperaObservacaoSelecionada(ActionEvent evt){
		andamentoProcessoSelecionado = (AndamentoProcesso) ((CheckableDataTableRowWrapper) tabelaProcessoAndamento.getRowData()).getWrappedObject();
		setObsAndamento(andamentoProcessoSelecionado.getDescricaoObservacaoInterna());
		atualizaSessao();
		
	}
	
	
	public void salvarObservacaoAction(ActionEvent evt){
		try {
			
			andamentoProcessoSelecionado.setDescricaoObservacaoInterna(obsAndamento);
			//getAndamentoProcessoService().limparSessao();
			getAndamentoProcessoService().alterar(andamentoProcessoSelecionado);
			reportarAviso("Andamento do processo atualizado com sucesso.");

			procuraAndamentoProcessoPeloID();
		} catch (Exception e) {
			reportarErro("Erro ao atualizar um andamento. ", e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void preencherResumoAndamentos() {

		int count = 1;
		List<AndamentoProcesso> listaT = new LinkedList<AndamentoProcesso>();
		for (int i = andamentosProcessoTotal.size() - 1; i >= 0 && count <= 100; i--) {
			listaT.add(andamentosProcessoTotal.get(i));
			count++;
		}
		setListaAndamentoProcesso(getCheckableDataTableRowWrapperList(listaT));
		atualizaSessao();
	}
	
	
	@SuppressWarnings({ "rawtypes"})
	public List pesquisarIncidentesPrincipal(Object value) {

		String siglaNumero = null;
		List<ObjetoIncidente<?>> incidentes = null;
		if (value != null)
			siglaNumero = value.toString();

		if (StringUtils.isNotVazia(siglaNumero)) {
			try {
				String sigla = ProcessoParser.getSigla(siglaNumero);
				Long lNumero = ProcessoParser.getNumero(siglaNumero);

				if (StringUtils.isNotVazia(sigla) && lNumero != null) {
					sigla = converterClasse(sigla, classes);

					if (sigla == null) {
						reportarAviso("Classe processual não encontrada: " + sigla);
						return null;
					}

					processo = getProcessoService().recuperarProcesso(sigla, lNumero);

					if (processo != null) {
						incidentes = recuperarIncidentes(processo.getId());
					}
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				reportarErro("Número de processo inválido: " + siglaNumero);
			} catch (ServiceException e) {
				e.printStackTrace();
				reportarErro("Erro ao pesquisar os incidentes do processo: " + siglaNumero);
			}
		}
		atualizaSessao();
		return incidentes;
	}
	
	
	


	public String getSiglaNumeroProcesso() {
		return siglaNumeroProcesso;
	}


	public void setSiglaNumeroProcesso(String siglaNumeroProcesso) {
		this.siglaNumeroProcesso = siglaNumeroProcesso;
	}


	public Long getObjetoIncidente() {
		return objetoIncidente;
	}


	public void setObjetoIncidente(Long objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}


	public List<CheckableDataTableRowWrapper> getListaAndamentoProcesso() {
		return listaAndamentoProcesso;
	}


	public void setListaAndamentoProcesso(
			List<CheckableDataTableRowWrapper> listaAndamentoProcesso) {
		this.listaAndamentoProcesso = listaAndamentoProcesso;
	}


	public org.richfaces.component.html.HtmlDataTable getTabelaProcessoAndamento() {
		return tabelaProcessoAndamento;
	}


	public void setTabelaProcessoAndamento(
			org.richfaces.component.html.HtmlDataTable tabelaProcessoAndamento) {
		this.tabelaProcessoAndamento = tabelaProcessoAndamento;
	}


	public Processo getProcesso() {
		return processo;
	}


	public void setProcesso(Processo processo) {
		this.processo = processo;
	}


	public List<AndamentoProcesso> getAndamentosProcessoTotal() {
		return andamentosProcessoTotal;
	}


	public void setAndamentosProcessoTotal(
			List<AndamentoProcesso> andamentosProcessoTotal) {
		this.andamentosProcessoTotal = andamentosProcessoTotal;
	}


	public String getObsAndamento() {
		return obsAndamento;
	}


	public void setObsAndamento(String obsAndamento) {
		this.obsAndamento = obsAndamento;
	}


	public AndamentoProcesso getAndamentoProcessoSelecionado() {
		return andamentoProcessoSelecionado;
	}


	public void setAndamentoProcessoSelecionado(
			AndamentoProcesso andamentoProcessoSelecionado) {
		this.andamentoProcessoSelecionado = andamentoProcessoSelecionado;
	}

}
