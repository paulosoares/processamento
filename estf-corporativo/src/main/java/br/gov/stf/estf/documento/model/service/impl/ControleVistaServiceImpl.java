package br.gov.stf.estf.documento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.stf.estf.documento.model.dataaccess.ControleVistaDao;
import br.gov.stf.estf.documento.model.service.ControleVistaService;
import br.gov.stf.estf.entidade.documento.ControleVista;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("controleVistaService")
public class ControleVistaServiceImpl extends GenericServiceImpl<ControleVista, Long, ControleVistaDao> implements ControleVistaService {
	
	public ControleVistaServiceImpl(ControleVistaDao dao) {
		super(dao);
	}

	/*
	 * Recupera os pedidos de vista, considerando o código do Ministro.
	 * */	
	@Override
	public List<ControleVista> recuperar(String siglaClasseProcessual, Long numeroProcesso, Long codigoMinistro) throws ServiceException {
		try{
			return dao.recuperar(siglaClasseProcessual, numeroProcesso, codigoMinistro);		
		}catch (DaoException se){
			throw new ServiceException(se);
		}
	}
	
	/*
	 * Recupera os pedidos de vista, sem levar em conta o código do Ministro.
	 * */
	@Override
	public List<ControleVista> recuperar(String siglaClasseProcessual, Long numeroProcesso) throws ServiceException {
		try{
			return dao.recuperar(siglaClasseProcessual, numeroProcesso);		
		}catch (DaoException se){
			throw new ServiceException(se);
		}
	}
	
	/*
	 * Recupera os pedidos de vista, através do objeto incidente.
	 * */
	@Override
	public List<ControleVista> recuperar(Long objetoIncidente) throws ServiceException {
		try{
			return dao.recuperar(objetoIncidente);		
		}catch (DaoException se){
			throw new ServiceException(se);
		}
	}

	/*
	 * Verifica se há Pedido de vista, levando em consideração
	 * a sigla processual, o número do processo e o código do Ministro.
	 * */
	@Override
	public Boolean validarPedidoVista(String siglaClasseProcessual, Long numeroProcesso, Long codigoMinistro) throws ServiceException {
		try{
			List<ControleVista> pedidoVista = dao.recuperar(siglaClasseProcessual, numeroProcesso, codigoMinistro);
			if (pedidoVista == null || (pedidoVista.size() <= 0)) {
				return false;
			}
		}catch (DaoException se){
			throw new ServiceException(se);
		}
		return true;
	}
	
	@Override
	@Transactional
	public List<ControleVista> listarVistasVencidas() throws ServiceException {
		try {
			return dao.listarVistasVencidas();
		} catch (DaoException se) {
			throw new ServiceException(se);
		}
	}

}
