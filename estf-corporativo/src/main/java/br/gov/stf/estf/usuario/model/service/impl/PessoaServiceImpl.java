package br.gov.stf.estf.usuario.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.entidade.usuario.Pessoa;
import br.gov.stf.estf.usuario.model.dataaccess.PessoaDao;
import br.gov.stf.estf.usuario.model.service.PessoaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("PessoaService")
public class PessoaServiceImpl extends GenericServiceImpl<Pessoa, Long, PessoaDao> implements PessoaService {
	
	public PessoaServiceImpl(PessoaDao dao) {
		super(dao);
	}
	
	@Override
	public Pessoa recuperarPorId(Long codigoPessoaDestinatario) throws ServiceException{
		Pessoa pessoa;
		
		try {
			pessoa = dao.recuperarPorId(codigoPessoaDestinatario);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return pessoa;

		
	}
	
	@Override
	public List<Pessoa> recuperarApenasPartesIntegradas(List<Parte> partes) throws ServiceException {
		try {
			return dao.recuperarApenasPartesIntegradas(partes);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}
