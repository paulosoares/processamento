package br.gov.stf.estf.expedicao.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.expedicao.entidade.TipoComunicacaoExpedicao;
import br.gov.stf.estf.expedicao.model.dataaccess.TipoComunicacaoExpedicaoDao;
import br.gov.stf.estf.expedicao.model.service.TipoComunicacaoExpedicaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

@Service("tipoComunicacaoExpedicaoService")
public class TipoComunicacaoExpedicaoServiceImpl implements TipoComunicacaoExpedicaoService {

    public static final long serialVersionUID = 1L;

    protected TipoComunicacaoExpedicaoDao dao;

    public TipoComunicacaoExpedicaoServiceImpl(TipoComunicacaoExpedicaoDao dao) {
        this.dao = dao;
    }

	@Override
	public List<TipoComunicacaoExpedicao> listarTiposComunicacao() throws ServiceException {
		try {
            return dao.listarTiposComunicacao();
	    } catch (DaoException ex) {
	        throw new ServiceException(ex);
	    }
	}
}