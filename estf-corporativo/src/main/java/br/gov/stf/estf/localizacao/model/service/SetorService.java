package br.gov.stf.estf.localizacao.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.localizacao.TipoConfiguracaoSetor;
import br.gov.stf.estf.localizacao.model.dataaccess.SetorDao;
import br.gov.stf.estf.localizacao.model.service.exception.NaoExisteSetorParaDeslocamentoException;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface SetorService extends GenericService<Setor, Long, SetorDao> {
	public List<Setor> pesquisar(Integer[] codigoCapitulo) throws ServiceException;

	/**
	 * Recupera os setores através de uma lista de códigos de setor
	 * 
	 * @param listaCodigoSetor
	 * @return Setores
	 * @throws ServiceException
	 * @author Rodrigo.Lisboa
	 */
	public List<Setor> pesquisar(Long[] listaCodigoSetor) throws ServiceException;

	public List<Setor> recuperarSetoresMinistrosAtivos() throws ServiceException;

	public Boolean alterarGuiaSetor(Setor setor) throws ServiceException;

	public List<Setor> pesquisarSetoresGabinete() throws ServiceException;

	public Setor recuperarSetorPorId(Long id) throws ServiceException;

	/**
	 * Quem utiliza esse método deve alterar para o método existente na classe
	 * MapeamentoClasseSetorService
	 */
	@Deprecated
	public Setor recuperarSetorParaDeslocamento(String classeDoProcesso) throws ServiceException,
			NaoExisteSetorParaDeslocamentoException;

	void incrementarGuiaDoSetor(Setor setor) throws ServiceException;

	boolean isSetorGabinete(Setor setor) throws ServiceException;

	public Setor recuperarSetor(Long id, String sigla) throws ServiceException;

	public Boolean alterarSetor(Setor localizacao) throws ServiceException;

	public List<Setor> pesquisarSetoresEGab() throws ServiceException;

	// TODO pode tentar unificar os métodos abaixo
	public List<Setor> pesquisarSetores(Boolean ativo) throws ServiceException;

	public List<Setor> pesquisarSetores(Long id, String Sigla, String siglaTipoConfiguracaoSetor, Boolean localizacaoAtivo,
			Boolean configuracaoAtiva) throws ServiceException;

	/**
	 * metodo resposnsavel por pesquisar os localizacao
	 * 
	 * @param sigla
	 *            sig do localizacao
	 * @param nome
	 *            nome do localizacao
	 * @param ativo
	 * @param somenteGabinetesPresidencia
	 *            pesquisar somente os localizacaoes q sejam gabinete ou
	 *            presidencia
	 * @return list<Setor>
	 * @throws ServiceException
	 */
	public List<Setor> pesquisarSetores(String sigla, String nome, Boolean ativo, Boolean somenteGabinetesPresidencia)
			throws ServiceException;

	public Boolean incluirTiposConfiguracaoSetor(Setor localizacao, List<TipoConfiguracaoSetor> tiposConfiguracaoIncluir)
			throws ServiceException;

	public Boolean excluirTiposConfiguracaoSetor(Setor localizacao, List<TipoConfiguracaoSetor> tiposConfiguracaoExcluir)
			throws ServiceException;

	List<Setor> pesquisarGabinetesComPresidenciaEVice() throws ServiceException;

	public List<Setor> recuperarSetorPorIdOuDescricao(String id) throws ServiceException;
	public List<Setor> recuperarSetorPorIdOuDescricao(String id, boolean deslocaProcesso) throws ServiceException;
	public List<Setor> recuperarSetorPorIdOuDescricaoAtivoInativo(String id) throws ServiceException;
	
	public List<Setor> pesquisarSetoresDeslocaComunicacao(boolean deslocaComunicaco) throws ServiceException;
	
	public List<Setor> pesquisarSetoresAtivosDeslocaComunicacao(boolean deslocaComunicaco) throws ServiceException;
	
	public List<Setor> pesquisarSetoresPorDescricao(String id,Boolean ativo,Boolean deslocaProcesso) throws ServiceException;
	
	public List<Setor> pesquisarSetoresPorId(Long id,Boolean ativo,Boolean deslocaProcesso) throws ServiceException;
	
}
