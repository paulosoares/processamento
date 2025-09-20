package br.gov.stf.estf.localizacao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.ConfiguracaoSetor;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface SetorDao extends GenericDao<Setor, Long> {
	public List<Setor> pesquisar(Integer[] codigoCapitulo) throws DaoException;

	public List<Setor> pesquisar(Long[] listaCodigoSetor) throws DaoException;

	public List<Setor> recuperarSetorMinistrosAtivos() throws DaoException;

	public Boolean alterarGuiaSetor(Setor setor) throws DaoException;

	public Setor recuperarSetorPorId(Long id) throws DaoException;

	public List<Setor> pesquisarSetoresGabinete() throws DaoException;

	void incrementarGuiaDoSetor(Setor setor) throws DaoException;

	public Setor recuperarSetor(Long id, String sigla) throws DaoException;

	public Boolean alterarSetor(Setor localizacao) throws DaoException;

	public List<Setor> pesquisarSetores(Boolean ativo) throws DaoException;

	public List<Setor> pesquisarSetores(String sigla, String nome, Boolean ativo, Boolean somenteGabinetesPresidencia)
			throws DaoException;

	public List<Setor> pesquisarSetores(Long id, String Sigla, String siglaTipoConfiguracaoSetor, Boolean localizacaoAtivo,
			Boolean configuracaoAtiva) throws DaoException;

	public List<Setor> pesquisarSetoresEGab() throws DaoException;

	public Boolean incluirConfiguracaoSetor(ConfiguracaoSetor configuracaoSetor) throws DaoException;

	public Boolean excluirConfiguracaoSetor(ConfiguracaoSetor configuracaoSetor) throws DaoException;

	List<Setor> pesquisarGabinetesComPresidenciaEVice() throws DaoException;

	public List<Setor> recuperarSetorPorIdOuDescricao(String id) throws DaoException;
	
	public List<Setor> recuperarSetorPorIdOuDescricao(String id, boolean deslocaProcesso) throws DaoException;
	
	public List<Setor> recuperarSetorPorIdOuDescricaoAtivoInativo(String id) throws DaoException;
	
	public List<Setor>  pesquisarSetoresDeslocaComunicacao(boolean deslocaComunicaco) throws DaoException;
	
	public List<Setor> pesquisarSetoresPorDescricao(String id,Boolean ativo,Boolean deslocaProcesso) throws DaoException;
	
	public List<Setor> pesquisarSetoresPorID(Long id,Boolean ativo,Boolean deslocaProcesso) throws DaoException;

	public List<Setor> pesquisarSetoresAtivosDeslocaComunicacao(boolean deslocaComunicaco) throws DaoException;
	
	
}
