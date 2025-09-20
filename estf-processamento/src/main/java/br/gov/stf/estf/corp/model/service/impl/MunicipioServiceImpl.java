package br.gov.stf.estf.corp.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.corp.entidade.Municipio;
import br.gov.stf.estf.corp.model.dataaccess.MunicipioDao;
import br.gov.stf.estf.corp.model.dataaccess.hibernate.MunicipioDaoHibernate;
import br.gov.stf.estf.corp.model.service.MunicipioService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

@Service("municipioService")
public class MunicipioServiceImpl implements MunicipioService {

    public static final long serialVersionUID = 1L;

    protected MunicipioDao dao;

    public MunicipioServiceImpl(MunicipioDao dao) {
        this.dao = dao;
    }

	@Override
	public Municipio recuperarPorId(String idMunicipio) throws ServiceException {
        try {
        	return dao.recuperarPorId(idMunicipio);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
	}

    @Override
    public List<Municipio> listarAtivos() throws ServiceException {
        try {
            return dao.listarAtivos();
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    @Override
    public List<Municipio> listarAtivos(String siglaUf) throws ServiceException {
        try {
            return dao.listarAtivos(siglaUf);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    @Override
    public List<Municipio> listarAtivosTipoMunicipio(String siglaUf) throws ServiceException {
        try {
            return dao.listarAtivosTipoMunicipio(siglaUf);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

	@Override
	public Municipio listarAtivo(String siglaUf, String nome) throws ServiceException {
        try {
    		return dao.listarAtivo(siglaUf, nome);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
	}

	@Override
	public Municipio listarAtivoTipoMunicipio(String siglaUf, String nome) throws ServiceException {
        try {
    		return dao.listarAtivoTipoMunicipio(siglaUf, nome);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
	}

	@Override
	public Municipio buscarMunicipioCorrespondente(String id) throws ServiceException {
        try {
        	Municipio municipio = dao.recuperarPorId(id);
            if (!municipio.getTipo().equals(MunicipioDaoHibernate.TIPO_MUNICIPIO)) {
                municipio = buscarMunicipioCorrespondente(municipio.getIdMunicipioSubordinacao());
            }
            return municipio;
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
	}
}