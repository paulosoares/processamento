package br.gov.stf.estf.expedicao.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.expedicao.entidade.TipoEmbalagem;
import br.gov.stf.estf.expedicao.model.dataaccess.TipoEmbalagemDao;
import br.gov.stf.estf.expedicao.model.service.TipoEmbalagemService;
import br.gov.stf.estf.expedicao.model.util.TipoEmbalagemEnum;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

@Service("tipoEmbalagemService")
public class TipoEmbalagemServiceImpl implements TipoEmbalagemService {

    public static final long serialVersionUID = 1L;

    protected TipoEmbalagemDao dao;

    public TipoEmbalagemServiceImpl(TipoEmbalagemDao dao) {
        this.dao = dao;
    }

    @Override
    public List<TipoEmbalagem> listarTiposServicoPorTipoEntrega(TipoEmbalagemEnum tipoEmbalagem) throws ServiceException {
    	try {
    		return dao.listarTiposServicoPorTipoEntrega(tipoEmbalagem);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }
}