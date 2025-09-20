package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.OrigemAndamentoDecisao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface AndamentoProcessoDao extends
		GenericDao<AndamentoProcesso, Long> {
	public Long recuperarUltimoNumeroSequencia(ObjetoIncidente processo)
			throws DaoException;

	public AndamentoProcesso recuperarAndamentoProcesso(String sigla,
			Long numero, Long codigoTipoAndamento) throws DaoException;

	public List pesquisarAndamentoProcesso(String sigla, Long numero,
			Long codigoTipoAndamento) throws DaoException;

	public Long persistirAndamentoProcesso(AndamentoProcesso andamentoProcesso)
			throws DaoException;

	public Long recuperarUltimaSequenciaAndamento(String sigla, Long numero)
			throws DaoException;

	public AndamentoProcesso recuperarUltimoAndamentoProcesso(
			String siglaClasse, Long numero) throws DaoException;

	public OrigemAndamentoDecisao recuperarOrigemAndamentoDecisao(Long id,
			String descricao, Long codigoSetor, Long codigoMinistro,
			Boolean ativo) throws DaoException;

	public List<AndamentoProcesso> pesquisarAndamentoProcessoSetor(
			String sigla, Long numero, Long setor) throws DaoException;

	public Boolean verificaAndamentoProcesso(String siglaProcessual,
			Long numeroProcessual, Long codigoAndamento) throws DaoException;

	public List<AndamentoProcesso> pesquisarAndamentoProcesso(
			Long codigoAndamento, Date dataInicial, Date dataFinal)
			throws DaoException;

	public AndamentoProcesso recuperarAndamentoProcesso(
			Long seqAndamentoProcesso) throws DaoException;

	public void atualizarAndamentoProcesso(AndamentoProcesso andamentoProcesso)
			throws DaoException;

	public AndamentoProcesso recuperarUltimoAndamento(Processo processo)
			throws DaoException;

	public Boolean verificarAndamentoProcessoNaoIndevido(
			String siglaProcessual, Long numeroProcessual, Long codigoAndamento)
			throws DaoException;
	
	public Long recuperarQuantidadeAndamentoProcesso(String siglaProcessual, Long numeroProcessual,
			Long codigoAndamento, Boolean incluirIndevidos) throws DaoException;
		

	public List<AndamentoProcesso> pesquisarAndamentoProcesso(String sigla,
			Long numero) throws DaoException;

	public boolean possuiAndamentoJulgamentoOuMerito(Processo processo)
			throws DaoException;

	public boolean isLancadoPorDispositivo(AndamentoProcesso andamentoProcesso)
			throws DaoException;

	public List<AndamentoProcesso> pesquisarAvisosNaoCriados(Long andamento,
			String observacao, Boolean processoOriginario, Date dataInicial,
			Date dataFinal, Boolean andamentoExpedito, String siglaProcesso,
			Long numProcesso) throws DaoException;
	
	public AndamentoProcesso recuperarUltimoAndamentoSelecionado(Long numeroProcesso, 
			String classeProcesso, Long codigoAndamento) throws DaoException;
	
	public void alterarAndamento(AndamentoProcesso ap) throws DaoException;

	public String recuperarObsInterna(Long idAndamentoProcesso) throws DaoException;

	public Long recuperarCodAndamentoPorNumeroSequencia(Processo processo, Long numeroSequenciaErrado) throws DaoException;

	public AndamentoProcesso recuperarUltimoAndamentoSelecionadoData(Long idProcesso, Long codigoAndamento,Date dataInicial, Date dataFinal)
			throws DaoException;

	public List<AndamentoProcesso> recuperarTodosAndamentos(ObjetoIncidente<?> referendo) throws DaoException;

	public void alterarObsAndamento(Long seqAndamento, String obs) throws DaoException;
}
