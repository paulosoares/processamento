package br.gov.stf.estf.alerta.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.alerta.model.dataaccess.AlertaDao;
import br.gov.stf.estf.alerta.model.service.AlertaService;
import br.gov.stf.estf.alerta.model.util.AlertaSearchData;
import br.gov.stf.estf.entidade.alerta.Alerta;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.model.util.TipoOrdem;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("alertaService")
public class AlertaServiceImpl extends GenericServiceImpl<Alerta, Long, AlertaDao> implements AlertaService {

	protected AlertaServiceImpl(AlertaDao dao) {
		super(dao);
	}

	@Deprecated
	@Override
	public List<Alerta> pesquisarAlerta(Andamento andamento, Usuario usuario, Date dataNotificado, Date dataAndamento, Short anoProtocolo, Long numeroProtocolo,
			String siglaClasseProcessual, Long numeroProcessual, Long idSetor, Boolean limitarPesquisa, Boolean oderByProcesso, Boolean oderByProtocolo,
			Boolean oderByDataAndamento, TipoOrdem tipoOrdem) throws ServiceException {

		List<Alerta> listaAlerta = null;

		try {
			listaAlerta = dao.pesquisarAlerta(andamento, usuario, dataNotificado, dataAndamento, anoProtocolo, numeroProtocolo, siglaClasseProcessual, numeroProcessual, idSetor,
					limitarPesquisa, oderByProcesso, oderByProtocolo, oderByDataAndamento, tipoOrdem);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return listaAlerta;
	}

	@Override
	public List<Alerta> pesquisarAlerta(AlertaSearchData searchData) throws ServiceException {
		List<Alerta> listaAlerta = null;

		try {
			listaAlerta = dao.pesquisarAlerta(searchData);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return listaAlerta;
	}

	@Deprecated
	@Override
	public Integer recuperarQuantidadeAlerta(Andamento andamento, Usuario usuario, Date dataNotificado, Date dataAndamento, Short anoProtocolo, Long numeroProtocolo,
			String siglaClasseProcessual, Long numeroProcessual, Long idSetor) throws ServiceException {

		Integer qtd = null;

		try {
			qtd = dao.recuperarQuantidadeAlerta(andamento, usuario, dataNotificado, dataAndamento, anoProtocolo, numeroProtocolo, siglaClasseProcessual, numeroProcessual, idSetor);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return qtd;
	}

	@Override
	public Integer recuperarQuantidadeAlerta(AlertaSearchData searchData) throws ServiceException {
		Integer qtd = null;

		try {
			qtd = dao.recuperarQuantidadeAlerta(searchData);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return qtd;
	}

	@Override
	public Boolean gravaAlertaJDBC(List<Alerta> listaAlerta) throws ServiceException {

		Boolean alterado = null;
		try {
			for (Alerta alerta : listaAlerta) {
				alerta.setDataNotificado(new Date());
				alterado = dao.gravaAlertaJDBC(alerta);
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return alterado;
	}
	
	// Novo método criado por muller.mendes
	
	public Boolean gravaTodosAlertaJDBC(Usuario usuario) throws ServiceException {

		Boolean alterado = null;
		try {
				//alerta.setDataNotificado(new Date());
				alterado = dao.gravaTodosAlertaJDBC(usuario);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return alterado;
	}

    public Boolean persistirAlerta(List<Alerta> listaAlerta) throws ServiceException{
    	
    	Boolean alterado = null;
    	try {
    		for(Alerta alerta: listaAlerta){
    			alerta.setDataNotificado(new Date());
    			alterado = dao.persistirAlerta(alerta);
    		}
		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
		return alterado;
    }
}