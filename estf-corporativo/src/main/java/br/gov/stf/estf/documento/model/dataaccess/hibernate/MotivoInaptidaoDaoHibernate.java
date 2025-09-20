package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.MotivoInaptidaoDao;
import br.gov.stf.estf.entidade.documento.MotivoInaptidao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class MotivoInaptidaoDaoHibernate extends GenericHibernateDao<MotivoInaptidao, Long> implements MotivoInaptidaoDao { 
    public MotivoInaptidaoDaoHibernate() {
		super(MotivoInaptidao.class);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public List<MotivoInaptidao> pesquisarTodos() throws DaoException {
		List<MotivoInaptidao> lista = new ArrayList<MotivoInaptidao>();
		try {
			Session session = retrieveSession();

			StringBuilder hql = new StringBuilder();
			hql.append(" FROM MotivoInaptidao m ");
			hql.append(" ORDER BY m.descricao ");

			Query q = session.createQuery(hql.toString());

			lista = q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return lista;
	}
}
