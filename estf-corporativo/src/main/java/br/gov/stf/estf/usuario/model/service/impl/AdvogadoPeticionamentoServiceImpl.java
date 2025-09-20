package br.gov.stf.estf.usuario.model.service.impl;

import java.util.List;

import br.gov.stf.estf.entidade.usuario.AdvogadoPeticionamento;
import br.gov.stf.estf.usuario.model.dataaccess.AdvogadoPeticionamentoDao;
import br.gov.stf.estf.usuario.model.service.AdvogadoPeticionamentoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

public class AdvogadoPeticionamentoServiceImpl extends GenericServiceImpl<AdvogadoPeticionamento, Long, AdvogadoPeticionamentoDao>  implements AdvogadoPeticionamentoService {
	private AdvogadoPeticionamentoDao	daoAdvogadoPeticionamento	= null;


	public AdvogadoPeticionamentoServiceImpl(AdvogadoPeticionamentoDao dao) {
		super(dao);
	}


	public void persistirAdvogado(AdvogadoPeticionamento advogado) throws ServiceException {
		try{
			daoAdvogadoPeticionamento.persistirAdvogado(advogado);
		}
		catch(DaoException e) {
			throw new ServiceException(e);
		}
	}


	public List<AdvogadoPeticionamento> pesquisarAdvogado(Long cpf) throws ServiceException {
		List<AdvogadoPeticionamento> listaAdvogado = null;
		try {
			listaAdvogado = daoAdvogadoPeticionamento.pesquisarAdvogado(cpf);
			return listaAdvogado;
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}


	public AdvogadoPeticionamentoDao getDaoAdvogadoPeticionamento() {
		return daoAdvogadoPeticionamento;
	}


	public void setDaoAdvogadoPeticionamento(
			AdvogadoPeticionamentoDao daoAdvogadoPeticionamento) {
		this.daoAdvogadoPeticionamento = daoAdvogadoPeticionamento;
	}




}
