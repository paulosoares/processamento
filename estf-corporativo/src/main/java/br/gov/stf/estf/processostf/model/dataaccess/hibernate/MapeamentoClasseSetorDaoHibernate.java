package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.MapeamentoClasseSetor;
import br.gov.stf.estf.processostf.model.dataaccess.MapeamentoClasseSetorDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.DateTimeHelper;

@Repository
public class MapeamentoClasseSetorDaoHibernate extends GenericHibernateDao<MapeamentoClasseSetor, Long> implements
		MapeamentoClasseSetorDao {

	public MapeamentoClasseSetorDaoHibernate() {
		super(MapeamentoClasseSetor.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @seebr.gov.stf.estf.processosetor.model.dataaccess.hibernate.
	 * MapeamentoClasseSetorDao#recuperarMapeamentosDaClasse(java.lang.String)
	 */
	public List<MapeamentoClasseSetor> recuperarMapeamentosDaClasse(String classe) throws DaoException {
		String alias = "mcs";
		Criteria criteria = retrieveSession().createCriteria(getPersistentClass(), alias);
		criteria.add(Restrictions.eq(alias + ".classe.id", classe));
		criteria.add(Restrictions.or(Restrictions.isNull(alias + ".dataFinal"), Restrictions.ge(alias + ".dataFinal",
				DateTimeHelper.getDataAtual())));
		criteria.add(Restrictions.eq(alias + ".ativo", true));
		criteria.addOrder(Order.desc(alias + ".tipoPreferencia"));
		return criteria.list();
	}
	
	public List<String> buscaClasseDoSetor(Setor setorDoUsuario) throws DaoException{
		
		List<String> listaClasseSetor = null;
		
		StringBuffer hql = new StringBuffer();
		Session session = retrieveSession();
		
		hql.append(" SELECT mcs.classe.id FROM MapeamentoClasseSetor mcs ");
		hql.append(" WHERE mcs.setor.id = :idSetor ");
		Query q = session.createQuery(hql.toString());

		if (setorDoUsuario != null) {
			q.setLong("idSetor", setorDoUsuario.getId());
		}
		
		listaClasseSetor = q.list();
		return listaClasseSetor;
		
	}

}
