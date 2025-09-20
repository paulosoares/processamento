package br.gov.stf.estf.ministro.model.service.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.ministro.MinistroPresidente;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.ministro.model.dataaccess.MinistroDao;
import br.gov.stf.estf.ministro.model.service.MinistroPresidenteService;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.SituacaoMinistroProcessoService;
import br.gov.stf.estf.usuario.model.util.TipoTurma;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("ministroService")
public class MinistroServiceImpl extends GenericServiceImpl<Ministro, Long, MinistroDao> implements MinistroService {

	private final MinistroPresidenteService ministroPresidenteService;
	private final SituacaoMinistroProcessoService situacaoMinistroProcessoService;
	private final ObjetoIncidenteService objetoIncidenteService;

	private MinistroPresidente ministroPresidente;

	public MinistroServiceImpl(MinistroPresidenteService ministroPresidenteService, MinistroDao dao,
			SituacaoMinistroProcessoService situacaoMinistroProcessoService, ObjetoIncidenteService objetoIncidenteService) {
		super(dao);
		this.ministroPresidenteService = ministroPresidenteService;
		this.situacaoMinistroProcessoService = situacaoMinistroProcessoService;
		this.objetoIncidenteService = objetoIncidenteService;
	}

	private MinistroPresidente getMinistroPresidente() throws ServiceException {
		if (ministroPresidente == null) {
			ministroPresidente = ministroPresidenteService.recuperarMinistroPresidenteAtual();
		}
		return ministroPresidente;
	}

	public Ministro recuperarMinistro(Setor setor) throws ServiceException {
		Ministro resultado = null;

		try {
			resultado = dao.recuperarMinistro(setor);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return resultado;
	}

	public List<Ministro> pesquisarMinistros(Boolean ativo, Boolean incluirGabinetePresidencia, Boolean primeiraTurma, Boolean segundaTurma,
			Boolean sessaoPlenaria) throws ServiceException {
		List<Ministro> resultado = null;
		MinistroPresidente ministroPresidente = null;

		try {
			// recupera o ministro presidente para eliminá-lo da turma devido
			// problemas de inconsistência de dados
			ministroPresidente = ministroPresidenteService.recuperarMinistroPresidenteAtual();
			resultado = dao.pesquisarMinistros(ativo, incluirGabinetePresidencia, ministroPresidente, primeiraTurma, segundaTurma, sessaoPlenaria);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return resultado;
	}

	public Ministro recuperarMinistroRelator(String siglaClasse, Long numeroProcessual, Long tipoRecurso, Long tipoJulgamento) throws ServiceException {

		Processo processo = (Processo) objetoIncidenteService.recuperar(siglaClasse, numeroProcessual, null, null);

		return recuperarMinistroRelator(processo);
	}

	public Ministro recuperarMinistroRelator(Processo processo) throws ServiceException {
		Ministro ministro = situacaoMinistroProcessoService.recuperarMinistroRelatorAtual(processo);

		if (ministro == null) {
			throw new ServiceException("Ministro relator não encontrado: objetoIncidente=" + processo);
		}
		return ministro;
	}

	public Ministro recuperarMinistroRelator(ObjetoIncidente objetoIncidente) throws ServiceException {
		Ministro relator = null;
		try {
			relator = dao.recuperarMinistroRelator(objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return relator;
	}

	public Ministro recuperarRelatorAcordao(ObjetoIncidente objetoIncidente) throws ServiceException {
		Ministro relator = null;
		try {
			relator = dao.recuperarRelatorAcordao(objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return relator;
	}

	@Override
	public Ministro recuperarRedatorAcordao(ObjetoIncidente objetoIncidente) throws ServiceException {
		Ministro redator = null;
		try {
			redator = dao.recuperarRedatorAcordao(objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return redator;
	}
	
	public Ministro recuperarMinistro(String nome, Long id) throws ServiceException {

		try {
			return dao.recuperarMinistro(nome, id);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	public Ministro recuperarPresidente(Boolean incluirGabinetePresidencia, Boolean primeiraTurma, Boolean segundaTurma, Boolean sessaoPlenaria)
			throws ServiceException {
		try {
			return dao.recuperarPresidente(incluirGabinetePresidencia, primeiraTurma, segundaTurma, sessaoPlenaria);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public List<Ministro> pesquisarMinistrosAtivos() throws ServiceException {
		List<Ministro> ministros = null;
		try {
			ministros = dao.pesquisarMinistrosAtivos();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return ministros;
	}
	
	// ---------------------
	public List<Ministro> pesquisarMinistro(Long codigoMinistro, String sigla, String nome, Long codigoSetor, TipoTurma tipoTurma, Boolean ativo,
			Boolean semMinistroPresidente) throws ServiceException {
		List<Ministro> usuarios = null;

		try {

			usuarios = dao.pesquisarMinistro(codigoMinistro, sigla, nome, codigoSetor, tipoTurma, ativo, semMinistroPresidente);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return usuarios;

	}

	// ---------------------

	public boolean isMinistroRelatorDoProcesso(Ministro ministroDoGabinete, Processo processo) throws ServiceException {
		if (processo.getMinistroRelatorAtual() != null) {
			Hibernate.initialize(processo.getMinistroRelatorAtual());
			return processo.getMinistroRelatorAtual().equals(ministroDoGabinete);
		}
		return false;
	}

	public boolean isMinistroTemRelatoriaDaPresidencia(Ministro ministro, Processo processo) throws ServiceException {
		return ((isSetorDoPresidente(ministro)) && (isMinistroRelatorDoProcesso(getMinistroPresidente().getId().getMinistro(), processo) || (isProcessoDoMinistroPresidentePadrao(processo))));

	}

	private boolean isSetorDoPresidente(Ministro ministroDoGabinete) throws ServiceException {
		return (ministroDoGabinete.getId().equals(getMinistroPresidente().getId()) || (ministroDoGabinete.getSetor().getId()
				.equals(Setor.CODIGO_SETOR_PRESIDENCIA)));
	}

	private boolean isProcessoDoMinistroPresidentePadrao(Processo processo) throws ServiceException {
		Ministro ministroRelator = situacaoMinistroProcessoService.recuperarMinistroRelatorAtual(processo);
		if (ministroRelator != null)
			return ministroRelator.getId().equals(Ministro.COD_MINISTRO_PRESIDENTE);
		else
			return false;
	}

	public List<Ministro> pesquisarMinistros(String nomeMinistro, Boolean ativo) throws ServiceException {
		try {
			return dao.pesquisarMinistros(nomeMinistro, ativo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public Ministro retornaMinistroPresidenteAtual() throws ServiceException {
		MinistroPresidente ministroPresidente = getMinistroPresidente();
		return ministroPresidente.getId().getMinistro();
	}

	@Override
	public Ministro recuperarMinistroRelatorIncidente(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		try {
			return dao.recuperarMinistroRelatorIncidente(objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Ministro recuperarMinistroRelatorIncidenteDataJulgamento(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		try {
			Ministro ministro = dao.recuperarMinistroRelatorIncidenteDataJulgamento(objetoIncidente);
			if (ministro == null)
				ministro = dao.recuperarMinistroRelatorIncidente(objetoIncidente);
			return ministro;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Ministro> pesquisarMinistrosPresidenteInterino() throws ServiceException {

		try {
			MinistroPresidente ministroPresidente = getMinistroPresidente();
			List<Ministro> ministros = dao.pesquisarMinistrosAtivos();
			ministros.remove(ministroPresidente.getId().getMinistro());
			return ministros;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Ministro recuperarMinistroRevisorIncidente(Long idObjetoIncidente) throws ServiceException {
		try {
			return dao.recuperarMinistroRevisorIncidente(idObjetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}	
	}

	@Override
	public Ministro recuperarMinistroPresidente(Date data) throws ServiceException {
		try {
			return dao.recuperarMinistroPresidente(data);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}
