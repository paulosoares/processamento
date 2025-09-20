package br.gov.stf.estf.localizacao.model.dataaccess.hibernate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.processostf.Orgao;
import br.gov.stf.estf.entidade.processostf.Procedencia;
import br.gov.stf.estf.localizacao.model.dataaccess.OrigemDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class OrigemDaoHibernate extends GenericHibernateDao<Origem, Long> implements OrigemDao {
	public OrigemDaoHibernate() {
		super(Origem.class);
	}

	private static final long serialVersionUID = 1L;

	public List<Origem> recuperarOrigemPorIdOuDescricao(String id) throws DaoException {

		List<Origem> origens = null;

		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append("SELECT o FROM Origem o ");
			hql.append("WHERE o.id LIKE :id OR o.descricao LIKE :idUpperCase ");
			
			Query q = session.createQuery(hql.toString());
			q.setParameter("id", "%" + id + "%");
			q.setParameter("idUpperCase", "%" + id.toUpperCase() + "%");
			
			origens = q.list();
			
			/*			
						Criteria criteria = session.createCriteria(Origem.class);
						criteria.add(Restrictions.like("descricao", id, MatchMode.ANYWHERE).ignoreCase());
						criteria.add(Restrictions.eq("ativo", true));

						origens = criteria.list();
			*/
//			SQLQuery query = session.createSQLQuery("SELECT * " + "  FROM judiciario.origem " +
//			// " WHERE CONTAINS (dsc_origem,'" + id.toString() + "') > 0 AND ROWNUM <= 100").addEntity(Origem.class);
//					"WHERE dsc_origem LIKE '%" + id.toString().toUpperCase() + "%' AND ROWNUM <= 100").addEntity(Origem.class);
//			origens = (List<Origem>) query.list();

		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}

		return origens;
	}

	public List<Origem> recuperarApenasPgr() throws DaoException {
		List<Origem> origens = Collections.emptyList();

		try {
			Session session = retrieveSession();
			SQLQuery query = session.createSQLQuery("SELECT * FROM judiciario.origem WHERE COD_ORIGEM = 23").addEntity(Origem.class);
			origens = (List<Origem>) query.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return origens;
	}
	
	public List<Origem> recuperarApenasAgu() throws DaoException {
		List<Origem> origens = Collections.emptyList();

		try {
			Session session = retrieveSession();
			SQLQuery query = session.createSQLQuery("SELECT * FROM judiciario.origem WHERE COD_ORIGEM = 26").addEntity(Origem.class);
			origens = (List<Origem>) query.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return origens;
	}

	public List<Origem> recuperarApenasDpf() throws DaoException {
		List<Origem> origens = Collections.emptyList();

		try {
			Session session = retrieveSession();
			SQLQuery query = session.createSQLQuery("SELECT * FROM judiciario.origem WHERE COD_ORIGEM = 273").addEntity(Origem.class);
			origens = (List<Origem>) query.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return origens;
	}
	
	@Override
	public List<Origem> pesquisarOrigensAtivas(Orgao orgao, Procedencia procedencia) throws DaoException {

		Session session = retrieveSession();
		String hql = "select distinct or from Orgao o, RegiaoOrgao ro, RegiaoProcedencia rp,  Origem or, Procedencia p where " +
					 "o.id = :idOrgao and p.id = :idProcedencia and o.id = ro.orgao and ro.id = rp.regiaoOrgao and ro.id = or.regiaoOrgao " + 
					 " and or.ativo = 'S' and rp.procedencia = p.id order by or.descricao";
		
		org.hibernate.Query query = session.createQuery(hql);
		query.setLong("idOrgao", orgao.getId());
		query.setLong("idProcedencia", procedencia.getId());
		
		List<Origem> origens = query.list();  
		
		return origens;
	}

	@Override
	public Procedencia pesquisarProcedenciaPadrao(Origem origem) throws DaoException {

		List<Procedencia> procedencias = Collections.emptyList();

		try {
			if(origem != null) {
				Session session = retrieveSession();
				SQLQuery query = session.createSQLQuery("SELECT pc.* FROM judiciario.origem_procedencia op, judiciario.procedencia pc  WHERE op.cod_procedencia = pc.cod_procedencia and op.COD_ORIGEM = " + origem.getId() +" order by op.flg_procedencia_padrao desc ").addEntity(Procedencia.class);
				procedencias = (List<Procedencia>) query.list();
			}
		return procedencias.get(0);
		}catch (Exception e) {
			throw new DaoException(e);
		}
		}
	
	@Override
	public Boolean isOrigemIntegrada(Origem origem) throws DaoException {

		Session session = retrieveSession();
		String sql = new String();
	
		sql = "select seq_pessoa from judiciario.origem  where cod_origem = :idOrigem";

		Query q = session.createSQLQuery(sql);
		q.setLong("idOrigem", origem.getId());

		List usuarioExternoOrigem = q.list();
		
		if (usuarioExternoOrigem == null  || (usuarioExternoOrigem.get(0) == null)){
			return false;
		} else {
			return ((BigDecimal)usuarioExternoOrigem.get(0)).longValue() > 0;
		}
			
	}

	@Override
	public List recuperaUsuarioExternoESTF(Long origem) throws DaoException {
		try{
			Session session = retrieveSession();
			StringBuffer sql = new StringBuffer("SELECT ueo.SEQ_USUARIO_EXTERNO FROM ESTF.USUARIO_EXTERNO_ORIGEM ueo WHERE ueo.COD_ORIGEM = :codOrigem ");
			
			Query q = session.createSQLQuery(sql.toString());
			
			q.setLong("codOrigem", origem);
			
			return q.list();
		}catch (Exception e) {
			throw new DaoException(e);
		}
		
	}

	@Override
	public Long recuperaUsuarioExternoOrigemESTF(Long seqUsuarioExterno) throws DaoException {
		try{
			Session session = retrieveSession();
			StringBuffer sql = new StringBuffer("SELECT ueo.SEQ_USUARIO_EXTERNO FROM ESTF.USUARIO_EXTERNO_ORIGEM ueo WHERE ueo.COD_ORIGEM = :seqUsuarioExterno ");
			
			Query q = session.createSQLQuery(sql.toString());
			
			q.setLong("seqUsuarioExterno", seqUsuarioExterno);
			
			return (Long) q.uniqueResult();
		}catch (Exception e) {
			throw new DaoException(e);
		}
		
	}
	
	@Override
	public List<Origem> recuperarOrigemPorId(Long id,Boolean ativo) throws DaoException {
		List<Origem> origens = null;

		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT o FROM Origem o ");
			hql.append(" WHERE (1=1) "); 
			if (id != null){
				hql.append("AND ( o.id LIKE ('%" + id + "') OR o.id LIKE ('" + id  + "%') )");
			}
			
			if(ativo != null){
				if(ativo){
					hql.append("AND o.ativo = 'S' ");
				}else{
					hql.append("AND o.ativo = 'N' ");
				}
			}
			hql.append(" ORDER BY o.id ASC ");
			
			Query q = session.createQuery(hql.toString());
			
			origens = q.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}

		return origens;
	}

	@Override
	public List<Origem> recuperarOrigemPorDescricao(String id,Boolean ativo)
			throws DaoException {
		List<Origem> origens = null;

		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append("SELECT o FROM Origem o ");
			hql.append("WHERE upper(o.descricao) LIKE upper('%"+ id +"%') ");
			
			if(ativo != null){
				if(ativo){
					hql.append("AND o.ativo = 'S' ");
				}else{
					hql.append("AND o.ativo = 'N' ");
				}
			}
			
			hql.append(" ORDER BY o.descricao ASC");
			
			Query q = session.createQuery(hql.toString());
			
			origens = q.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}

		return origens;
	}
	
	@Override
	public List<Origem> recuperarTodasOrigens(Boolean ativo) throws DaoException {
		List<Origem> origens = null;

		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT o FROM Origem o ");
			hql.append(" WHERE (1=1) "); 
			
			if(ativo != null){
				if(ativo){
					hql.append("AND o.ativo = 'S' ");
				}else{
					hql.append("AND o.ativo = 'N' ");
				}
			}
			hql.append(" ORDER BY o.ativo DESC, o.descricao ASC ");
			
			Query q = session.createQuery(hql.toString());
			
			origens = q.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}

		return origens;
	}
	
	@Override
	public Boolean isOrigemAptaParaParaNotificacao(Long id) throws DaoException{
		Origem origem = null;
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT o "+
					   " FROM Origem o "+
					" WHERE     o.ativo = 'S' "+
					" AND o.descricaoUrlIntegracao IS NOT NULL "+
					" AND o.senha IS NOT NULL "+
					" AND o.usuario IS NOT NULL "+
					" AND o.baixaMni = 'S' "+
					" AND o.id = '"+id+"'");
			
			Query q = session.createQuery(hql.toString());
			
			origem = (Origem) q.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
		return origem != null;
	}
	
}
