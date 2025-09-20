package br.gov.stf.estf.processosetor.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processosetor.GrupoProcessoSetor;
import br.gov.stf.estf.processosetor.model.dataaccess.GrupoProcessoSetorDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.model.entity.Flag;

@Repository
public class GrupoProcessoSetorDaoHibernate extends GenericHibernateDao<GrupoProcessoSetor, Long> 
implements GrupoProcessoSetorDao {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1968868389028886296L;

	public GrupoProcessoSetorDaoHibernate () {
		super(GrupoProcessoSetor.class);
	}

	public Boolean verificarUnicidadeGrupoProcessoSetor(Long id, Long setorId, String nomeGrupo)
	throws DaoException
	{
		Boolean unico = Boolean.TRUE;

		Session sessao = retrieveSession();

		List<GrupoProcessoSetor> gruposProcessoSetor = null;

		try{
			Criteria criteria = sessao.createCriteria(GrupoProcessoSetor.class);

			if(id != null)
				criteria.add(Restrictions.ne("id", id));

			criteria.add(Restrictions.eq("setor.id", setorId));            
			criteria.add(Restrictions.eq("nomeGrupo", nomeGrupo));

			gruposProcessoSetor = criteria.list();
		}
		catch(HibernateException e)
		{
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch(RuntimeException e)
		{
			throw new DaoException("RuntimeException", e);
		}

		if(gruposProcessoSetor != null && gruposProcessoSetor.size() > 0)
			unico = Boolean.FALSE;

		return unico;
	}

	public GrupoProcessoSetor recuperarGrupoProcessoSetor(Long id)
	throws DaoException
	{
		Session sessao = retrieveSession();

		GrupoProcessoSetor grupoProcessoSetor = null;

		try{

			Criteria criteria = sessao.createCriteria(GrupoProcessoSetor.class);

			criteria.add(Restrictions.eq("id", id));

			grupoProcessoSetor = (GrupoProcessoSetor) criteria.uniqueResult();
		}
		catch(HibernateException e)
		{
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch(RuntimeException e)
		{
			throw new DaoException("RuntimeException", e);
		}

		return grupoProcessoSetor;
	}

	public List<GrupoProcessoSetor> pesquisarGrupoProcessoSetor(String nomeGrupo, Boolean ativo, 
			Long idSetor, Long idGrupo, String siglaClasseProcessual, Long numeroProcessual)
			throws DaoException
			{
		Session sessao = retrieveSession();

		List<GrupoProcessoSetor> gruposProcessoSetor = null;

		try{

			/*StringBuffer hql = new StringBuffer("SELECT DISTINCT g FROM GrupoProcessoSetor g, ProcessosSetor ps  ");

			if( (siglaClasseProcessual != null && siglaClasseProcessual.trim().length() > 0) || 
					(numeroProcessual != null && numeroProcessual > 0) ){
				hql.append(" ,Processo p ");
			}*/
			
			StringBuffer hql = new StringBuffer("SELECT DISTINCT g FROM GrupoProcessoSetor g ");

			if( (siglaClasseProcessual != null && siglaClasseProcessual.trim().length() > 0) || 
					(numeroProcessual != null && numeroProcessual > 0) ){
				hql.append(" JOIN g.processosSetor ps ");
				hql.append(" ,Processo p ");
			}

			hql.append(" WHERE 1=1");
			
//			hql.append(" ps.objetoIncidente.id = g.objetoIncidente.id ");
			
			if( (siglaClasseProcessual != null && siglaClasseProcessual.trim().length() > 0) || 
					(numeroProcessual != null && numeroProcessual > 0) ){
				hql.append(" AND ps.objetoIncidente.id = p.id ");
			}

			if( idGrupo != null && idGrupo > 0 ) {
				hql.append(" AND g.id = " + idGrupo);
			}
			
			if(nomeGrupo != null && !nomeGrupo.trim().equals(""))
				hql.append(" AND g.nomeGrupo LIKE '%"+nomeGrupo.toUpperCase()+"%'");

			if(ativo != null)
				if(ativo)
					hql.append(" AND g.ativo = '" + Flag.FLAG_SIM.getValor()+"'");
				else
					hql.append(" AND g.ativo = '" + Flag.FLAG_NAO.getValor()+"'");

			if(idSetor != null)
				hql.append(" AND g.setor.id = " + idSetor);

			if( siglaClasseProcessual != null && siglaClasseProcessual.trim().length() > 0 ){
				hql.append(" AND p.siglaClasseProcessual = '"+siglaClasseProcessual+"'");
			}

			if( numeroProcessual != null && numeroProcessual > 0 ){
				hql.append(" AND p.numeroProcessual = "+numeroProcessual);
			}

			hql.append(" ORDER BY g.nomeGrupo ASC");

			Query q = sessao.createQuery(hql.toString());
			gruposProcessoSetor = q.list();
		}
		catch(HibernateException e)
		{
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch(RuntimeException e)
		{
			throw new DaoException("RuntimeException", e);
		}

		return gruposProcessoSetor;
			}

	public Boolean persistirGrupoProcessoSetor(GrupoProcessoSetor grupoProcessoSetor)
	throws DaoException
	{
		Boolean alterado = Boolean.FALSE;

		Session sessao = retrieveSession();

		try
		{
			sessao.persist(grupoProcessoSetor);
			sessao.flush();

			alterado = Boolean.TRUE;
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch(RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return alterado;
	}

	public Boolean excluirGrupoProcessoSetor(GrupoProcessoSetor grupoProcessoSetor)
	throws DaoException
	{
		Boolean excluido = Boolean.FALSE;

		Session sessao = retrieveSession();

		try
		{
			sessao.delete(grupoProcessoSetor);
			sessao.flush();

			excluido = Boolean.TRUE;
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch(RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return excluido;
	}
}
