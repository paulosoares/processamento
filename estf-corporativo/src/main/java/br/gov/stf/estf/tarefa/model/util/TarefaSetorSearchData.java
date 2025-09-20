package br.gov.stf.estf.tarefa.model.util;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.tarefa.CampoTarefaValor;
import br.gov.stf.estf.entidade.tarefa.RelatorioAnaliticoTarefaSetor;
import br.gov.stf.estf.entidade.tarefa.RelatorioAnaliticoTarefaSetor.ValorCampoTipoTarefaRelatorio;
import br.gov.stf.estf.model.util.ESTFSearchData;

/**
 * Objeto que encapsula os diversos parâmetros que podem ser utilizados na consulta de tarefas.
 * 
 * @author thiago.miranda
 */
public class TarefaSetorSearchData extends ESTFSearchData {

	private static final long serialVersionUID = 7087309797625475115L;

	/*
	 * Construtor utilizado apenas para facilitar a transição dos métodos que listam todos os parâmetros para listarem apenas um objeto TarefaSetorSearchData.
	 * Deverá ser descartado no futuro.
	 */
	public TarefaSetorSearchData(Long codigo, String descricao, String classeProcessual, Long numeroProcesso, Long idSetorOrigem, Long idSetorDestino, Long idTipoTarefa,
			Long prioridade, Long idTipoSituacaoTarefa, Date dataCriacaoInicial, Date dataCriacaoFinal, Date dataInicioPrevistoInicial, Date dataInicioPrevistoFinal,
			Date dataFimPrevistoInicial, Date dataFimPrevistoFinal, Date dataInicioInicial, Date dataInicioFim, Date dataFimInicial, Date dataFimFinal,
			Date dataTipoCampoTarefaInicial, Date dataTipoCampoTarefaFinal, Long idTipoCampoTarefaPeriodo, String sigUsuario, Boolean urgente, Boolean sigiloso, Boolean iniciado,
			Boolean finalizado, Boolean semCampoTarefa, Boolean limitarPesquisa, List<CampoTarefaValor> listaCampoTarefa,
			List<ValorCampoTipoTarefaRelatorio> listaValorCampoTipoTarefa, Boolean semTarefaSetor, Boolean readOnlyQuery) {
		this.codigo = codigo;
		this.descricao = descricao;
		this.classeProcessual = classeProcessual;
		this.numeroProcesso = numeroProcesso;
		this.idSetorOrigem = idSetorOrigem;
		this.idSetorDestino = idSetorDestino;
		this.idTipoTarefa = idTipoTarefa;
		this.prioridade = prioridade;
		this.idTipoSituacaoTarefa = idTipoSituacaoTarefa;
		this.dataCriacaoInicial = dataCriacaoInicial;
		this.dataCriacaoFinal = dataCriacaoFinal;
		this.dataInicioPrevistoInicial = dataInicioPrevistoInicial;
		this.dataInicioPrevistoFinal = dataInicioPrevistoFinal;
		this.dataFimPrevistoInicial = dataFimPrevistoInicial;
		this.dataFimPrevistoFinal = dataFimPrevistoFinal;
		this.dataInicioInicial = dataInicioInicial;
		this.dataInicioFim = dataInicioFim;
		this.dataFimInicial = dataFimInicial;
		this.dataFimFinal = dataFimFinal;
		this.dataTipoCampoTarefaInicial = dataTipoCampoTarefaInicial;
		this.dataTipoCampoTarefaFinal = dataTipoCampoTarefaFinal;
		this.idTipoCampoTarefaPeriodo = idTipoCampoTarefaPeriodo;
		this.sigUsuario = sigUsuario;
		this.urgente = urgente;
		this.sigiloso = sigiloso;
		this.iniciado = iniciado;
		this.finalizado = finalizado;
		this.semCampoTarefa = semCampoTarefa;
		this.limitarPesquisa = limitarPesquisa;
		this.listaCampoTarefa = listaCampoTarefa;
		this.listaValorCampoTipoTarefa = listaValorCampoTipoTarefa;
		this.semTarefaSetor = semTarefaSetor;
		this.readOnlyQuery = readOnlyQuery;
	}

	public TarefaSetorSearchData() {
		super();
	}

	public Long codigo;
	public String descricao;
	public String classeProcessual;
	public Long numeroProcesso;
	public Long idSetorOrigem;
	public Long idSetorDestino;
	public Long idTipoTarefa;
	public Long prioridade;
	public Long idTipoSituacaoTarefa;
	public Date dataCriacaoInicial;
	public Date dataCriacaoFinal;
	public Date dataInicioPrevistoInicial;
	public Date dataInicioPrevistoFinal;
	public Date dataFimPrevistoInicial;
	public Date dataFimPrevistoFinal;
	public Date dataInicioInicial;
	public Date dataInicioFim;
	public Date dataFimInicial;
	public Date dataFimFinal;
	public Date dataTipoCampoTarefaInicial;
	public Date dataTipoCampoTarefaFinal;
	public Long idTipoCampoTarefaPeriodo;
	public String sigUsuario;
	public Boolean urgente;
	public Boolean sigiloso;
	public Boolean iniciado;
	public Boolean finalizado;
	public Boolean semCampoTarefa;
	public List<CampoTarefaValor> listaCampoTarefa;
	public List<RelatorioAnaliticoTarefaSetor.ValorCampoTipoTarefaRelatorio> listaValorCampoTipoTarefa;
	public Boolean semTarefaSetor;
}
