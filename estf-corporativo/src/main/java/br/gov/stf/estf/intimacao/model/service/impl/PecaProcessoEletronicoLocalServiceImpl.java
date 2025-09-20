package br.gov.stf.estf.intimacao.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.TextoAndamentoProcesso;
import br.gov.stf.estf.intimacao.model.dataaccess.PecaProcessoEletronicoLocalDao;
import br.gov.stf.estf.intimacao.model.service.PecaProcessoEletronicoLocalService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("pecaProcessoEletronicoLocalService")
public class PecaProcessoEletronicoLocalServiceImpl extends
					GenericServiceImpl<PecaProcessoEletronico, Long, PecaProcessoEletronicoLocalDao> 
						implements PecaProcessoEletronicoLocalService {	
	
	@Autowired
	public PecaProcessoEletronicoLocalServiceImpl(PecaProcessoEletronicoLocalDao dao) {
		super(dao);
	}

	@Override
	public List<PecaProcessoEletronico> pesquisarPecasPorProcessoIncidente(Long idProcessoIncidente,
			Boolean incluirCancelados) throws ServiceException {
		try {
			return dao.pesquisarPecasPorProcessoIncidente(idProcessoIncidente, incluirCancelados);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<PecaProcessoEletronico> pesquisarPecasAndamento8507(Long idProcessoIncidente) throws ServiceException {
		try {
			return dao.pesquisarPecasAndamento8507(idProcessoIncidente);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<TextoAndamentoProcesso> pesquisarPecasUtilizadasEmVista(Long idProcessoIncidente) throws ServiceException {
		try{
			return dao.pesquisarPecasUtilizadasEmVista(idProcessoIncidente);
		}catch (Exception e){
			throw new ServiceException(e);
		}
	}
	
	
	
}
