package br.gov.stf.estf.localizacao.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.Advogado;
import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.localizacao.model.dataaccess.AdvogadoDao;
import br.gov.stf.estf.localizacao.model.service.AdvogadoService;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("advogadoService")
public class AdvogadoServiceImpl extends GenericServiceImpl<Advogado, Long, AdvogadoDao> 
	implements AdvogadoService {
    public AdvogadoServiceImpl(AdvogadoDao dao) { super(dao); }
	

	public Advogado recuperar(Parte parte) throws ServiceException {
		Advogado advogado = null;
		try {
			advogado = dao.recuperar(parte);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return advogado;
	}
	
	/**
	 * @see br.gov.stf.eprocesso.processostf.servico.IServicoAdvogado#persistirAdvogado(br.gov.stf.eprocesso.processostf.modelo.Advogado)
	 */
	public void incluirAdvogado(Advogado advogado) throws ServiceException {
		try {
			dao.incluirAdvogado(advogado);
		} catch (Throwable e) {
			throw new ServiceException("Ocorreu um erro ao persistir o advogado.", e);
		}
	}


	/**
	 * @see br.gov.stf.eprocesso.processostf.servico.IServicoAdvogado#pesquisarAdvogado(java.lang.String)
	 */
	public List<Advogado> pesquisarAdvogado(Advogado advogado) throws ServiceException {
		try {
			return dao.pesquisarAdvogado(advogado);
		} catch (Throwable e) {
			throw new ServiceException("Ocorreu um erro ao pesquisar o advogado.", e);
		}
	}


	/**
	 * @throws RegraDeNegocioException 
	 * @see br.gov.stf.eprocesso.processostf.servico.IServicoAdvogado#validarNumeroOab(java.lang.String)
	 */
	public String validarNumeroOab(String numeroOab) throws ServiceException {
		// Verifica se o número de OAB é nulo.
		if (numeroOab == null || numeroOab.length() < 1) {
			throw new ServiceException("Ocorreu um erro ao validar o número da OAB: OAB nulo.");
		}
		numeroOab = numeroOab.replace(" ", "");
		if (numeroOab.length() > 8) {
			throw new ServiceException("Ocorreu um erro ao validar o número da OAB: OAB com mais de 8 caracteres.");
		}
		
		try {
			// Retira todos os espaços em branco.
			numeroOab = numeroOab.replace(" ", "");
			return dao.validarNumeroOab(numeroOab);
		} catch (Throwable e) {
			throw new ServiceException("Ocorreu um erro ao validar o número da OAB: " + numeroOab, e);
		}
	}	

	public List<Advogado> recuperarAdvogadoPorIdOuDescricao(String id) throws ServiceException {

		List<Advogado> listaAdvogados = null;
		try {
			listaAdvogados = dao.recuperarAdvogadoPorIdOuDescricao(id);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar advogados");
		}
		return listaAdvogados;
	}
	
	public List<Advogado> recuperarAdvogadoPorDescricao(String id) throws ServiceException {
		List<Advogado> listaAdvogados = null;
		try {
			listaAdvogados = dao.recuperarAdvogadoPorDescricao(id);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar advogados");
		}
		return listaAdvogados;
	
	}


	@Override
	public List<Advogado> recuperarAdvogadoPorId(Long id)
			throws ServiceException {
		List<Advogado> listaAdvogados = null;
		try {
			listaAdvogados = dao.recuperarAdvogadoPorId(id);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar advogados");
		}
		return listaAdvogados;
	}
}
