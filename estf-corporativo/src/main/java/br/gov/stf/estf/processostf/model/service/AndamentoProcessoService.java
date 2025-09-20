package br.gov.stf.estf.processostf.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.OrigemAndamentoDecisao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.processostf.model.dataaccess.AndamentoProcessoDao;
import br.gov.stf.estf.processostf.model.service.exception.AndamentoNaoAutorizadoException;
import br.gov.stf.estf.processostf.model.service.exception.LancamentoIndevidoException;
import br.gov.stf.estf.processostf.model.service.exception.MinistroRelatorAposentadoException;
import br.gov.stf.estf.processostf.model.service.exception.ProcessoOutraRelatoriaException;
import br.gov.stf.estf.processostf.model.util.AndamentoProcessoInfo;
import br.gov.stf.estf.processostf.model.util.ContainerGuiaProcessos;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface AndamentoProcessoService extends
		GenericService<AndamentoProcesso, Long, AndamentoProcessoDao> {
	@SuppressWarnings("rawtypes")
	public Long recuperarUltimoNumeroSequencia(ObjetoIncidente processo) throws ServiceException;
	
	@SuppressWarnings("rawtypes")
	public Long recuperarProximoNumeroSequencia(ObjetoIncidente objetoIncidente) throws ServiceException;

	public AndamentoProcesso recuperarAndamentoProcesso(String sigla,
			Long numero, Long codigoTipoAndamento, Long numeroPeticao,
			Short anoPeticao) throws ServiceException;

	public Long persistirAndamentoProcesso(AndamentoProcesso andamentoProcesso)
			throws ServiceException;

	public OrigemAndamentoDecisao recuperarOrigemAndamentoDecisao(Long id,
			String descricao, Long codigoSetor, Long codigoMinistro,
			Boolean ativo) throws ServiceException;

	public AndamentoProcesso recuperarUltimoAndamentoProcesso(
			String siglaClasse, Long numero) throws ServiceException;

	public List<AndamentoProcesso> recuperarAndamentoProcessoSetor(
			String sigla, Long numero, Long setor) throws ServiceException;

	public Boolean verificaAndamentoProcesso(String siglaProcessual,
			Long numeroProcessual, Long codigoAndamento)
			throws ServiceException;

	public List<AndamentoProcesso> recuperarAndamentoProcesso(
			Long codigoAndamento, Date dataInicial, Date dataFinal)
			throws ServiceException;

	public AndamentoProcesso recuperarAndamentoProcesso(
			Long seqAndamentoProcesso) throws ServiceException;

	public void atualizarAndamentoProcesso(AndamentoProcesso andamentoProcesso)
			throws ServiceException;

	public AndamentoProcesso recuperarUltimoAndamento(Processo processo)
			throws ServiceException;

	public Boolean verificarAndamentoProcessoNaoIndevido(
			String siglaProcessual, Long numeroProcessual, Long codigoAndamento)
			throws ServiceException;
	
	public Long recuperarQuantidadeAndamentoProcessoNaoIndevido(
			String siglaProcessual, Long numeroProcessual, Long codigoAndamento)
			throws ServiceException;

	/**
	 * Este método carrega o AndamentoProcesso e inicializa a listaTextoAndamentoProcessos.
	 * 
	 * @param sigla do processo
	 * @param numero do processo
	 * @return Lista de AndamentoProcesso na qual cada objeto possui o atributo listaTextoAndamentoProcessos inicializado.
	 * @throws ServiceException
	 */
	public List<AndamentoProcesso> pesquisarAndamentoProcesso(String sigla,
			Long numero) throws ServiceException;

	public String getSituacaoAndamento(AndamentoProcesso andamentoProcesso)
			throws ServiceException;

	public void verificarSetorUsuario(Processo processo, Setor setor,
			VerificadorPerfilService verificadorPerfilService)
			throws AndamentoNaoAutorizadoException,
			ProcessoOutraRelatoriaException, ServiceException;

	public void verificarSituacaoProcesso(Processo processo)
			throws ServiceException;

	public void verificarMinistroRelatorAposentado(Processo processo)
			throws MinistroRelatorAposentadoException;

	public void verificarPossibilidadeApensamento(Processo processoAndamento,
			Processo processoPrincipal) throws ServiceException;

	public AndamentoProcesso salvarAndamentoIndevido(
			Processo processoAndamento, AndamentoProcesso andamentoProcesso,
			Setor setor, String codigoUsuario,
			AndamentoProcesso ultimoAndamento, String observacao,
			String observacaoInterna, ObjetoIncidente<?> objetoIncidente)
			throws ServiceException, LancamentoIndevidoException;

	public void cancelarLancamentoAndamentoIndevido(
			AndamentoProcesso andamentoProcessoIndevido,
			List<AndamentoProcesso> andamentosProcesso)
			throws ServiceException, LancamentoIndevidoException;

	public boolean precisaProcessoPrincipal(Andamento andamento);

	public boolean precisaProcessosPrincipais(Andamento andamento);

	public boolean precisaPeticao(Andamento andamento);

	public boolean precisaOrigemDecisao(Andamento andamento, Setor setor)
			throws ServiceException;

	public boolean precisaPresidenteInterino(Long idOrigemDecisao, Setor setor,
			Andamento andamento) throws ServiceException;

	public boolean podeEditarObservacao(AndamentoProcesso andamentoProcesso, String userName);

	public boolean precisaVerificarCodigoOrigem(Andamento andamento)
			throws ServiceException;

	public List<OrigemAndamentoDecisao> getOrigensDecisao(Andamento andamento)
			throws ServiceException;

	public boolean precisaTipoDevolucao(Andamento andamento);

	public boolean precisaConfirmacaoLancarAndamento(Andamento andamento,
			Processo processoAndamento) throws ServiceException;

	public String getMensagemConfirmacao(Andamento andamento);

	void salvarAndamentoParaVariosProcessos(
			AndamentoProcessoInfo andamentoProcessoInfo,
			List<Processo> processosSelecionados) throws ServiceException;

	public abstract AndamentoProcesso salvarAndamento(
			AndamentoProcessoInfo andamentoProcessoInfo,
			Processo processoAndamento, ObjetoIncidente<?> objetoIncidente)
			throws ServiceException;

	public boolean isLancadoPorDispositivo(AndamentoProcesso andamentoProcesso)
			throws ServiceException;

	public void verificarLancamentoIndevido(AndamentoProcesso andamentoProcesso)
			throws ServiceException;

	public List<AndamentoProcesso> pesquisarAvisosNaoCriados(Long andamento,
			String observacao, Boolean processoOriginario, Date dataInicial,
			Date dataFinal, Boolean andamentoExpedito, String siglaProcesso,
			Long numProcesso) throws ServiceException;
	
	public AndamentoProcesso recuperarUltimoAndamentoSelecionado(Long numeroProcesso, 
			String classeProcesso, Long codigoAndamento) throws ServiceException;
	
	public AndamentoProcesso recuperarUltimoAndamentoSelecionadoData(Long idProcesso, Long codigoAndamento, Date dataInicio, Date dataFinal) throws ServiceException;
	
	public void alterarAndamento(AndamentoProcesso ap)  throws ServiceException;

	public String recuperarObsInterna(Long idAndamentoProcesso) throws ServiceException;

	public Long recuperarCodAndamentoPorNumeroSequencia(Processo processo, Long numeroSequenciaErrado) throws ServiceException;

	String salvarAndamentoBaixa(List<ContainerGuiaProcessos> containerDeGuias) throws ServiceException;

	AndamentoProcesso gerarAndamentoBasico(Long codigoAndamento, ObjetoIncidente<?> oi, Usuario usuario, String descricaoObs,
			String descricaoObsInterna) throws ServiceException;

	public void excluirTodosOsAndamentos(ObjetoIncidente<?> referendo) throws ServiceException;

	public List<AndamentoProcesso> recuperarTodosAndamentos(ObjetoIncidente<?> referendo) throws ServiceException;

	public void alterarObsAndamento(Long seqAndamento, String obs) throws ServiceException;

}
