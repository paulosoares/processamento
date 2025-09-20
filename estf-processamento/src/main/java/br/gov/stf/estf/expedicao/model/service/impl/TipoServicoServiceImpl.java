package br.gov.stf.estf.expedicao.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.expedicao.entidade.TipoServico;
import br.gov.stf.estf.expedicao.model.dataaccess.TipoServicoDao;
import br.gov.stf.estf.expedicao.model.service.TipoServicoService;
import br.gov.stf.estf.expedicao.model.util.TipoEntregaEnum;
import br.gov.stf.estf.expedicao.model.util.TipoServicoEnum;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

@Service("tipoServicoService")
public class TipoServicoServiceImpl implements TipoServicoService {

    public static final long serialVersionUID = 1L;

    protected TipoServicoDao dao;

    public TipoServicoServiceImpl(TipoServicoDao dao) {
        this.dao = dao;
    }

    @Override
    public List<TipoServico> listarTiposServicoPorTipoEntrega(TipoEntregaEnum tipoEntregaEnum) throws ServiceException {
        List<TipoServico> tiposServico = null;
        try {
            switch (tipoEntregaEnum) {
                case CORREIOS: {
                    tiposServico = dao.listarTiposServicoPorTipoEntrega(true, TipoServicoEnum.POSTAGEM);
                    break;
                }
                case ENTREGA_PORTARIA: {
                    tiposServico = dao.listarTiposServicoPorTipoEntrega(false, null);
                    break;
                }
                case MALOTE: {
                    tiposServico = dao.listarTiposServicoPorTipoEntrega(true, TipoServicoEnum.MALOTE);
                    break;
                }
            }
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
        return tiposServico;
    }
}