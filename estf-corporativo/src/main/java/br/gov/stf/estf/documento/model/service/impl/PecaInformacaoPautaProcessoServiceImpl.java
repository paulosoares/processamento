package br.gov.stf.estf.documento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.julgamento.InformacaoPautaProcesso;
import br.gov.stf.estf.entidade.julgamento.PecaInformacaoPautaProcesso;
import br.gov.stf.estf.julgamento.model.dataaccess.PecaInformacaoPautaProcessoDao;
import br.gov.stf.estf.julgamento.model.service.PecaInformacaoPautaProcessoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("pecaInformacaoPautaProcessoService")
public class PecaInformacaoPautaProcessoServiceImpl extends GenericServiceImpl<PecaInformacaoPautaProcesso, Long, PecaInformacaoPautaProcessoDao> implements PecaInformacaoPautaProcessoService {
	
	protected PecaInformacaoPautaProcessoServiceImpl(PecaInformacaoPautaProcessoDao dao) {
		super(dao);
	}

	@Override
	public List<PecaInformacaoPautaProcesso> recuperar(InformacaoPautaProcesso informacaoPautaProcesso, boolean somenteDocumentoExterno) throws ServiceException {
		try {
			return dao.recuperar( informacaoPautaProcesso, somenteDocumentoExterno );
		} catch(DaoException e) {
			throw new ServiceException(e);
		}	
	}

	@Override
	public List<PecaInformacaoPautaProcesso> recuperar(InformacaoPautaProcesso informacaoPautaProcesso, PecaProcessoEletronico pecaProcessoEletronico) throws ServiceException {
		try {
			return dao.recuperar( informacaoPautaProcesso, pecaProcessoEletronico );
		} catch(DaoException e) {
			throw new ServiceException(e);
		}	
	}
	
}