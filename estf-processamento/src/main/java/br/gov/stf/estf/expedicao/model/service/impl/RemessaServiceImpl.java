package br.gov.stf.estf.expedicao.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.expedicao.entidade.Remessa;
import br.gov.stf.estf.expedicao.model.dataaccess.RemessaDao;
import br.gov.stf.estf.expedicao.model.service.RemessaService;
import br.gov.stf.estf.expedicao.model.util.PesquisaListaRemessaDto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("remessaService")
public class RemessaServiceImpl extends GenericServiceImpl<Remessa, Long, RemessaDao> implements RemessaService {

    public static final long serialVersionUID = 1L;

    public RemessaServiceImpl(RemessaDao dao) {
        super(dao);
    }

    @Override
    public List<Remessa> pesquisar(PesquisaListaRemessaDto pesquisaListaRemessaDto) throws ServiceException {
        try {
            return dao.pesquisar(pesquisaListaRemessaDto);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

	@Override
	public List<Remessa> pesquisarEnviadas(PesquisaListaRemessaDto pesquisaListaRemessaDto) throws ServiceException {
        try {
            return dao.pesquisarEnviadas(pesquisaListaRemessaDto);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
	}
}