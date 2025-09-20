package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.TipoSituacaoControle;
import br.gov.stf.estf.processostf.model.dataaccess.TipoSituacaoControleDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoSituacaoControleDaoHibernate extends GenericHibernateDao<TipoSituacaoControle, Long> implements
		TipoSituacaoControleDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7967526565517299634L;

	public TipoSituacaoControleDaoHibernate() {
		super(TipoSituacaoControle.class);
	}
	
	public TipoSituacaoControle pesquisarSituacaoControle(String descricao) throws DaoException {
		TipoSituacaoControle tipo = null;
		
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(TipoSituacaoControle.class);

			c.add(Restrictions.eq("descricao", descricao));

			tipo = (TipoSituacaoControle) c.uniqueResult();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return tipo;
	}

	@Override
	public TipoSituacaoControle recuperarPorCodigo(String codigoTipoSituacaoControle) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(TipoSituacaoControle.class);
			c.add(Restrictions.eq("codigo", codigoTipoSituacaoControle));
			
			return (TipoSituacaoControle) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
