package br.gov.stf.estf.processostf.model.service;

import java.util.ArrayList;
import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso.DeslocaProcessoId;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processostf.model.dataaccess.DeslocaProcessoDao;
import br.gov.stf.estf.processostf.model.service.exception.NaoExistemDeslocamentosException;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface DeslocaProcessoService extends GenericService<DeslocaProcesso, DeslocaProcessoId, DeslocaProcessoDao> {

	/**
	 * O método que deve ser utilizado é recuperarUltimoDeslocamentoProcesso(String siglaClasse, Long numeroProcesso)
	 * 
	 * @param siglaClasse
	 * @param numeroProcesso
	 * @return
	 * @throws ServiceException
	 * @throws NaoExistemDeslocamentosException
	 */
	@Deprecated
	DeslocaProcesso consultaUltimoDeslocamentoDoProcesso(String classeDoProcesso, Long numeroDoProcesso) throws ServiceException,
			NaoExistemDeslocamentosException;

	public DeslocaProcesso recuperarUltimoDeslocamentoProcesso(String siglaClasse, Long numeroProcesso) throws ServiceException;

	public void persistirDeslocamentoProcesso(DeslocaProcesso deslocamentoProcesso) throws ServiceException;

	/**
	 * Recupera o ultimo deslocamento de um objeto incidente. Como o Deslocamento tem como base o Processo, o dado utilizado para filtrar é o getPrincipal() do
	 * ObjetoIncidente.
	 * 
	 * @param objetoIncidente
	 * @return
	 * @throws ServiceException
	 */
	DeslocaProcesso recuperarUltimoDeslocamentoProcesso(ObjetoIncidente<?> objetoIncidente) throws ServiceException;

	public void receberProcesso(DeslocaProcesso deslocamentoProcesso) throws ServiceException;

	// realiza o deslocamento para processo ou petição: alimenta a tabela judiciario.controlar_desloca_incidente e chama a pkg_desloca_incidente.
	// public String inserirDeslocamento(Guia guia, ArrayList listaObjetoIncidentes) throws ServiceException;
	public DeslocaProcesso recuperarDeslocamentoProcesso(Guia guia) throws ServiceException;

	public List<DeslocaProcesso> recuperarDeslocamentoProcessos(Guia guia) throws ServiceException;

	/**
	 * Recupera o código do setor do último deslocamento do processo.
	 */
	public Long pesquisarSetorUltimoDeslocamento(Processo processo) throws ServiceException;

	public void removerProcesso(DeslocaProcesso processo) throws ServiceException;

	public List<DeslocaProcesso> pesquisarDataRecebimentoGuiaProcesso(Guia guia) throws ServiceException;

	List<DeslocaProcesso> recuperarProcessosPeloSetor(Long codigoSetor) throws ServiceException;

	public DeslocaProcesso recuperarUltimoDeslocamentoProcesso(Processo processo) throws ServiceException;

	public void deslocaProcessoSetor(Long codigoOrgaoOrigem, Long codigoOrgaoDestino, String tipoOrgaoOrigem, String tipoOrgaoDestino, Long seqObjetoIncidente)
			throws ServiceException;
	
	void deslocarProcesso(Processo processoDoTexto, Long codigoOrgaoDestino,
			Long id, Integer tipoOrgaoDestino, Integer tipoOrgaoInterno) throws ServiceException;

	void insereProcesso(String chkTipoOrgao, String tipoGuia, Long codigoLotacao, Processo processoSelecionado) throws ServiceException,
			RegraDeNegocioException;

	List<DeslocaProcesso> recuperarDeslocamentoProcessosRecebimentoExterno(Guia guia) throws ServiceException;

	DeslocaProcesso recuperaDeslocamentoProcessoRecebimento(DeslocaProcesso deslocaProcesso, Long setorUsuario) throws ServiceException;

	public void inserirBaixa(Processo processo, Origem origem, Setor setor, AndamentoProcesso andamentoProcesso) throws ServiceException;
	
	public void deslocarProcessoParaSetorUsuario(Processo processo, Setor setor) throws ServiceException ;

	public void darBaixaProcesso(Processo processo, Origem origem, Setor setor, AndamentoProcesso andamentoProcesso) throws ServiceException ;

	public void darBaixaProcesso(Processo processo, Origem origem, Long codigoOrigem, Integer tipoOrgaoOrigem, AndamentoProcesso andamentoProcesso) throws ServiceException;
	
	public void devolverDeslocamento(Processo processo) throws ServiceException;
	
	public List<DeslocaProcesso> recuperaPorProcessoOrigemExterna(Processo processo) throws ServiceException;

	public boolean isBaixadoParaOrigem(Processo processo, Origem origem, Andamento andamento) throws ServiceException;
	
	/**
	 * 
	 * Esse método associa um andamento processo ao um deslocamento processo.
	 */
	
	public void alterarDeslocaProcessosPorAndamento(DeslocaProcesso deslocaProcesso, AndamentoProcesso andamentoProcesso, Setor setor, Origem origem) throws ServiceException ;

	public Integer recuperarUltimaSequencia(Guia guia) throws ServiceException;

	public DeslocaProcesso recuperarUltimaRemessaProcesso(String siglaProcesso, Long numeroProcesso) throws ServiceException;

	Setor pesquisarSetorUltimoDeslocamento(Long seqObjetoIncidente) throws ServiceException;

	String darBaixaProcesso(Guia guia, ArrayList<Long> processos)
			throws ServiceException;
	
	public void atualizaAndamento(DeslocaProcesso deslocaProcesso, Long idAndamentoProcesso)  throws ServiceException;
	
}
