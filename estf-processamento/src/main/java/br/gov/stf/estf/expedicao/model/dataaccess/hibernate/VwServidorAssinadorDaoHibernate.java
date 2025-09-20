package br.gov.stf.estf.expedicao.model.dataaccess.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.expedicao.entidade.VwServidorAssinador;
import br.gov.stf.estf.expedicao.model.dataaccess.VwServidorAssinadorDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class VwServidorAssinadorDaoHibernate extends GenericHibernateDao<VwServidorAssinador, String> implements VwServidorAssinadorDao {

    public static final long serialVersionUID = 1L;

    public VwServidorAssinadorDaoHibernate() {
    	super(VwServidorAssinador.class);
    }

    @Override
    public List<VwServidorAssinador> listar() throws DaoException {
        return super.pesquisar();
    }
}