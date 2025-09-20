package br.gov.stf.estf.processostf.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.ministro.Ocorrencia;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.SituacaoMinistroProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.SituacaoMinistroProcessoDao;
import br.gov.stf.estf.processostf.model.service.SituacaoMinistroProcessoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("situacaoMinistroProcessoService")
public class SituacaoMinistroProcessoServiceImpl extends GenericServiceImpl<SituacaoMinistroProcesso, Long, SituacaoMinistroProcessoDao> implements
		SituacaoMinistroProcessoService {
	public SituacaoMinistroProcessoServiceImpl(SituacaoMinistroProcessoDao dao) {
		super(dao);
	}

	public List<SituacaoMinistroProcesso> pesquisar(String[] codOcorrencia, String siglaProcesso, Long numProcesso, Long codRecurso, String tipoJulgamento,
			Boolean orderByCodOcorrenciaDesc) throws ServiceException {
		List<SituacaoMinistroProcesso> lista = null;

		try {
			lista = dao.pesquisar(codOcorrencia, siglaProcesso, numProcesso, codRecurso, tipoJulgamento, orderByCodOcorrenciaDesc);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return lista;
	}

	public Ministro recuperarMinistroRelatorAtual(String siglaClasse, Long numeroProcesso) throws ServiceException {
		try {
			return dao.recuperarMinistroRelatorAtual(siglaClasse, numeroProcesso);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	public Ministro recuperarMinistroRelatorAtual(Processo processo) throws ServiceException {
		try {
			return dao.recuperarMinistroRelatorAtual(processo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public Ministro recuperarMinistroRelatorAtual(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		try {
			return dao.recuperarMinistroRelatorAtual(objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<SituacaoMinistroProcesso> pesquisar(ObjetoIncidente<?> objetoIncidente, Ocorrencia ocorrencia) throws ServiceException {
		try {
			return dao.pesquisar(objetoIncidente, ocorrencia);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void remover(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		try {
			dao.remover(objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public void designarNovoRelatorAcordao(ObjetoIncidente<?> objetoIncidente, Ministro ministro, Ocorrencia ocorrencia) throws ServiceException{
		try {
			SituacaoMinistroProcesso novoRelatorAcordao = new SituacaoMinistroProcesso();
			novoRelatorAcordao.setObjetoIncidente(objetoIncidente);
			novoRelatorAcordao.setMinistroRelator(ministro);
			novoRelatorAcordao.setOcorrencia(ocorrencia);
			novoRelatorAcordao.setDataOcorrencia(new Date());
			novoRelatorAcordao.setRelatorAtual(Boolean.FALSE);
			novoRelatorAcordao.setRelatorIncidenteAtual(Boolean.FALSE);

			dao.incluir(novoRelatorAcordao);
		} catch (DaoException e) {
			throw new ServiceException("Erro nao foi possivel ajustar relator para o acordao.", e);
		}
	}
	
	
}
