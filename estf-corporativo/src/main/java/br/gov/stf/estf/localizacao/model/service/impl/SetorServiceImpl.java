package br.gov.stf.estf.localizacao.model.service.impl;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.ConfiguracaoSetor;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.localizacao.TipoConfiguracaoSetor;
import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.localizacao.model.dataaccess.ConfiguracaoSetorDao;
import br.gov.stf.estf.localizacao.model.dataaccess.SetorDao;
import br.gov.stf.estf.localizacao.model.service.SetorService;
import br.gov.stf.estf.localizacao.model.service.exception.NaoExisteSetorParaDeslocamentoException;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;
import br.gov.stf.framework.model.util.TipoOperacaoServico;

@Service("setorService")
public class SetorServiceImpl extends GenericServiceImpl<Setor, Long, SetorDao> implements SetorService {

	private final ConfiguracaoSetorDao configuracaoSetorDao;

	public SetorServiceImpl(SetorDao dao, ConfiguracaoSetorDao configuracaoSetorDao) {
		super(dao);
		this.configuracaoSetorDao = configuracaoSetorDao;
	}

	public List<Setor> pesquisar(Integer[] codigoCapitulo) throws ServiceException {
		List<Setor> lista = Collections.emptyList();
		try {
			lista = dao.pesquisar(codigoCapitulo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return lista;
	}

	public List<Setor> pesquisar(Long[] listaCodigoSetor) throws ServiceException {

		List<Setor> listaSetor = Collections.emptyList();

		try {
			listaSetor = dao.pesquisar(listaCodigoSetor);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return listaSetor;
	}

	public List<Setor> recuperarSetoresMinistrosAtivos() throws ServiceException {
		try {
			return dao.recuperarSetorMinistrosAtivos();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public Boolean alterarGuiaSetor(Setor setor) throws ServiceException {
		try {
			return dao.alterarGuiaSetor(setor);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public void incrementarGuiaDoSetor(Setor setor) throws ServiceException {
		try {
			dao.incrementarGuiaDoSetor(setor);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public List<Setor> pesquisarSetoresGabinete() throws ServiceException {
		try {
			List<Setor> setores = dao.pesquisarSetoresGabinete();
			return setores;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public boolean isSetorGabinete(Setor setor) throws ServiceException {
		List<Setor> setores = pesquisarSetoresGabinete();
		return setores.contains(setor);
	}

	public Setor recuperarSetorPorId(Long id) throws ServiceException {
		try {
			return dao.recuperarSetorPorId(id);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Quem utiliza esse método deve alterar para o método existente na classe
	 * MapeamentoClasseSetorService
	 */
	@Deprecated
	public Setor recuperarSetorParaDeslocamento(String classeDoProcesso) throws ServiceException,
			NaoExisteSetorParaDeslocamentoException {
		if (classeDoProcesso.equals(Classe.SIGLA_RECURSO_EXTRAORDINARIO)) {
			return recuperarPorId(Setor.CODIGO_SETOR_RECURSO_EXTRAORDINARIO);
		}
		if (classeDoProcesso.equals(Classe.SIGLA_AGRAVO_DE_INSTRUMENTO)) {
			return recuperarPorId(Setor.CODIGO_SETOR_AGRAVO_DE_INSTRUMENTO);
		}
		throw new NaoExisteSetorParaDeslocamentoException();
	}

	public Setor recuperarSetor(Long id, String sigla) throws ServiceException {

		if (id == null && (sigla == null || sigla.trim().equals("")))
			throw new ServiceException("Nenhum parâmetro válido foi informado.");

		Setor localizacao = null;

		try {

			localizacao = dao.recuperarSetor(id, sigla);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return localizacao;
	}

	public Boolean alterarSetor(Setor localizacao) throws ServiceException {

		try {
			validarSetor(localizacao, TipoOperacaoServico.ALTERAR);

			return dao.alterarSetor(localizacao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	private void validarSetor(Setor localizacao, TipoOperacaoServico tipoOperacao) throws ServiceException {

		if (localizacao == null)
			throw new NullPointerException("Setor nulo");

		if (localizacao.getId() == null && tipoOperacao.equals(TipoOperacaoServico.ALTERAR))
			throw new NullPointerException("Identificação do localizacao nula");

		if (localizacao.getSigla() == null && tipoOperacao.equals(TipoOperacaoServico.ALTERAR))
			throw new NullPointerException("Sigla do localizacao nula");

		if (localizacao.getNome() == null && tipoOperacao.equals(TipoOperacaoServico.ALTERAR))
			throw new NullPointerException("Nome do localizacao nulo");

		if ((localizacao.getTiposConfiguracao() == null || localizacao.getTiposConfiguracao().size() <= 0)
				&& tipoOperacao.equals(TipoOperacaoServico.ALTERAR))
			throw new ServiceException("Setor não possui nenhum tipo de configuração");
		else {
			boolean possuiConfiguracaoEGab = false;
			for (TipoConfiguracaoSetor tipoConf : localizacao.getTiposConfiguracao()) {
				if (tipoConf.getSigla().equals(TipoConfiguracaoSetor.SIGLA_TIPO_CONFIGURACAO_EGAB))
					possuiConfiguracaoEGab = true;
			}

			if (!possuiConfiguracaoEGab)
				throw new ServiceException("Setor não possui configuração de utilização do EGAB");
		}
	}

	public List<Setor> pesquisarSetores(Boolean ativo) throws ServiceException {

		List<Setor> listaSetores = Collections.emptyList();
		try {

			listaSetores = dao.pesquisarSetores(ativo);

		} catch (DaoException ex) {

			throw new ServiceException("Erro ao pesquisar os localizações");
		}
		return listaSetores;
	}

	public List<Setor> pesquisarSetores(Long id, String Sigla, String siglaTipoConfiguracaoSetor, Boolean localizacaoAtivo,
			Boolean configuracaoAtiva) throws ServiceException {
		try {
			return dao.pesquisarSetores(id, Sigla, siglaTipoConfiguracaoSetor, localizacaoAtivo, configuracaoAtiva);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar os localizacaoes");
		}
	}

	public List<Setor> pesquisarSetores(String sigla, String nome, Boolean ativo, Boolean somenteGabinetesPresidencia)
			throws ServiceException {
		List<Setor> listaSetores = Collections.emptyList();
		try {

			listaSetores = dao.pesquisarSetores(sigla, nome, ativo, somenteGabinetesPresidencia);

		} catch (DaoException ex) {

			throw new ServiceException("Erro ao pesquisar os localizações");
		}
		return listaSetores;
	}

	public List<Setor> pesquisarGabinetesComPresidenciaEVice() throws ServiceException {
		List<Setor> listaSetores = Collections.emptyList();
		try {
			listaSetores = dao.pesquisarGabinetesComPresidenciaEVice();
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar os localizações");
		}
		return listaSetores;
	}

	public List<Setor> recuperarSetorPorIdOuDescricao(String id) throws ServiceException {

		List<Setor> listaSetores = Collections.emptyList();
		try {
			listaSetores = dao.recuperarSetorPorIdOuDescricao(id);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar os localizações");
		}
		return listaSetores;
	}

	public List<Setor> recuperarSetorPorIdOuDescricao(String id, boolean deslocaProcesso) throws ServiceException {

		List<Setor> listaSetores = Collections.emptyList();
		try {
			listaSetores = dao.recuperarSetorPorIdOuDescricao(id, deslocaProcesso);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar os localizações");
		}
		return listaSetores;
	}

	public List<Setor> recuperarSetorPorIdOuDescricaoAtivoInativo(String id) throws ServiceException {

		List<Setor> listaSetores = Collections.emptyList();
		try {
			listaSetores = dao.recuperarSetorPorIdOuDescricaoAtivoInativo(id);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar os localizações");
		}
		return listaSetores;
	}
	
	
	public List<Setor> pesquisarSetoresEGab() throws ServiceException {
		List<Setor> listaSetores = Collections.emptyList();

		try {
			//listaSetores = dao.pesquisar(new AscendantOrder("nome"));
			listaSetores = dao.pesquisarSetoresEGab();
		} catch (DaoException ex) {

			throw new ServiceException("Erro ao pesquisar as localizações");
		}

		return listaSetores;
	}

	/*
	 * public Boolean incluirTiposConfiguracaoSetor(Setor localizacao,
	 * List<TipoConfiguracaoSetor> tiposConfiguracaoIncluir) throws
	 * ServiceException { Boolean incluidos = Boolean.FALSE;
	 * 
	 * validarSetor(localizacao, TipoOperacaoServico.ALTERAR);
	 * 
	 * if(tiposConfiguracaoIncluir == null || tiposConfiguracaoIncluir.size() <=
	 * 0) throw new
	 * InvalidParameterException("A lista de tipos de configuração não " +
	 * "possui elementos.");
	 * 
	 * if(localizacao.getTiposConfiguracao().addAll(tiposConfiguracaoIncluir))
	 * incluidos = alterarSetor(localizacao);
	 * 
	 * return incluidos; }
	 */

	public Boolean incluirTiposConfiguracaoSetor(Setor localizacao, List<TipoConfiguracaoSetor> tiposConfiguracaoIncluir)
			throws ServiceException {
		Boolean incluidos = Boolean.FALSE;

		validarSetor(localizacao, TipoOperacaoServico.ALTERAR);

		if (tiposConfiguracaoIncluir == null || tiposConfiguracaoIncluir.size() <= 0)
			throw new InvalidParameterException("A lista de tipos de configuração não " + "possui elementos.");

		for (TipoConfiguracaoSetor tipoConfSetor : tiposConfiguracaoIncluir) {
			ConfiguracaoSetor configuracao = new ConfiguracaoSetor();
			configuracao.setSetor(localizacao);
			configuracao.setTipoConfiguracaoSetor(tipoConfSetor);
			try {
				incluidos = dao.incluirConfiguracaoSetor(configuracao);
			} catch (DaoException e) {
				throw new ServiceException(e.getMessage());
			}

		}

		/*
		 * configuracao.getTipoConfiguracoes().addAll(tiposConfiguracaoIncluir);
		 * 
		 * 
		 * 
		 * if(localizacao.getTiposConfiguracao().addAll(tiposConfiguracaoIncluir)
		 * ) incluidos = alterarSetor(localizacao);
		 */

		return incluidos;
	}

	public Boolean excluirTiposConfiguracaoSetor(Setor localizacao, List<TipoConfiguracaoSetor> tiposConfiguracaoExcluir)
			throws ServiceException {
		Boolean excluidos = Boolean.FALSE;
		ConfiguracaoSetor confSetor = null;

		validarSetor(localizacao, TipoOperacaoServico.ALTERAR);

		if (tiposConfiguracaoExcluir == null || tiposConfiguracaoExcluir.size() <= 0)
			throw new InvalidParameterException("A lista de tipos de configuração não " + "possui elementos.");

		for (TipoConfiguracaoSetor tipoConf : tiposConfiguracaoExcluir) {
			if (tipoConf.getSigla().equals(TipoConfiguracaoSetor.SIGLA_TIPO_CONFIGURACAO_EGAB))
				throw new ServiceException("Não é permitida a exclusão da configuração de " + "utilização do EGAB");

			try {

				confSetor = configuracaoSetorDao.pesquisarConfiguracaoSetor(localizacao.getId(), tipoConf.getId());
				excluidos = dao.excluirConfiguracaoSetor(confSetor);

			} catch (DaoException e) {
				throw new ServiceException(e.getMessage());
			}
		}

		/*
		 * if(localizacao.getTiposConfiguracao().removeAll(tiposConfiguracaoExcluir
		 * )) excluidos = alterarSetor(localizacao);
		 */

		return excluidos;
	}
	
	@Override
	public List<Setor>  pesquisarSetoresDeslocaComunicacao(boolean deslocaComunicaco) throws ServiceException{
		List<Setor> listaSetores = Collections.emptyList();
		try{
			listaSetores = dao.pesquisarSetoresDeslocaComunicacao(deslocaComunicaco);
		}catch (DaoException e) {
			throw new ServiceException("Erro ao pesquisar os setores que podem deslocar domunicações.");
		}
		
		return listaSetores;
	}
	
	@Override
	public List<Setor>  pesquisarSetoresAtivosDeslocaComunicacao(boolean deslocaComunicaco) throws ServiceException{
		List<Setor> listaSetores = Collections.emptyList();
		try{
			listaSetores = dao.pesquisarSetoresAtivosDeslocaComunicacao(deslocaComunicaco);
		}catch (DaoException e) {
			throw new ServiceException("Erro ao pesquisar os setores que podem deslocar domunicações.");
		}
		
		return listaSetores;
	}

	@Override
	public List<Setor> pesquisarSetoresPorDescricao(String id, Boolean ativo,Boolean deslocaProcesso)
			throws ServiceException {
		List<Setor> listaSetores = Collections.emptyList();
		if(id == null || id.isEmpty()){
			return null;
		}
		try{
			listaSetores = dao.pesquisarSetoresPorDescricao(id, ativo,deslocaProcesso);
		}catch (DaoException e) {
			throw new ServiceException("Erro ao pesquisar setores.");
		}
		
		return listaSetores;
	}

	@Override
	public List<Setor> pesquisarSetoresPorId(Long id, Boolean ativo,Boolean deslocaProcesso)
			throws ServiceException {
		List<Setor> listaSetores = Collections.emptyList();
		if(id == null || id.equals(0L)){
			return null;
		}
		try{
			listaSetores = dao.pesquisarSetoresPorID(id, ativo,deslocaProcesso);
		}catch (DaoException e) {
			throw new ServiceException("Erro ao pesquisar setores.");
		}
		
		return listaSetores;
	}
	
	/*
	 * public Boolean excluirTiposConfiguracaoSetor(Setor localizacao,
	 * List<TipoConfiguracaoSetor> tiposConfiguracaoExcluir) throws
	 * ServiceException { Boolean excluidos = Boolean.FALSE;
	 * 
	 * validarSetor(localizacao, TipoOperacaoServico.ALTERAR);
	 * 
	 * if(tiposConfiguracaoExcluir == null || tiposConfiguracaoExcluir.size() <=
	 * 0) throw new
	 * InvalidParameterException("A lista de tipos de configuração não " +
	 * "possui elementos.");
	 * 
	 * for(TipoConfiguracaoSetor tipoConf : tiposConfiguracaoExcluir) {
	 * if(tipoConf
	 * .getSigla().equals(TipoConfiguracaoSetor.SIGLA_TIPO_CONFIGURACAO_EGAB))
	 * throw new
	 * ServiceException("Não é permitida a exclusão da configuração de " +
	 * "utilização do EGAB"); }
	 * 
	 * 
	 * 
	 * if(localizacao.getTiposConfiguracao().removeAll(tiposConfiguracaoExcluir))
	 * excluidos = alterarSetor(localizacao);
	 * 
	 * return excluidos; }
	 */
}
