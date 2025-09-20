package br.gov.stf.estf.localizacao.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.OrigemDestino;
import br.gov.stf.estf.localizacao.model.dataaccess.OrigemDestinoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class OrigemDestinoDaoHibernate extends GenericHibernateDao<OrigemDestino, Long> implements OrigemDestinoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OrigemDestinoDaoHibernate() {
		super(OrigemDestino.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<OrigemDestino> recuperarPorIdOuDescricao(String value, Boolean ativo, Boolean deslocaProcesso) throws DaoException {

		List<OrigemDestino> origensDestinos = null;

		try {
			Session session = retrieveSession();
						
			Criteria criteria = session.createCriteria(OrigemDestino.class);
			Criterion restrDescricao = Restrictions.like("descricao", value, MatchMode.ANYWHERE).ignoreCase();
			Criterion restrCodigoInicioFim = Restrictions.sqlRestriction("UPPER({alias}.COD_ORIGEM_DESTINO) LIKE '" + value.toUpperCase() + "%'" + 
					" OR UPPER({alias}.COD_ORIGEM_DESTINO) LIKE '%" + value.toUpperCase() + "'");
			
			criteria.add(Restrictions.eq("ativo", ativo));
			criteria.add(Restrictions.eq("deslocaProcesso", deslocaProcesso));
			criteria.add(Restrictions.or(restrDescricao, restrCodigoInicioFim));
			criteria.setMaxResults(50);

			origensDestinos = (List<OrigemDestino>) criteria.list();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}

		return origensDestinos;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<OrigemDestino> recuperarPorIdOuDescricao(String value) throws DaoException {

		List<OrigemDestino> origensDestinos = null;

		try {
			Session session = retrieveSession();
			Criteria criteria = session.createCriteria(OrigemDestino.class);
			if (soNumeros(value)) {
				criteria.add(Restrictions.eq("id", new Long(value)));
			} else {
		        criteria.add(Restrictions.like("descricao", value, MatchMode.ANYWHERE).ignoreCase());
			}
			
			criteria.setMaxResults(50);

			origensDestinos = (List<OrigemDestino>) criteria.list();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}

		return origensDestinos;
	}
	
	/**
	 * Traz a apenas setores e órgãos externos da view.
	 * @param value
	 * @param ativo
	 * @param deslocaProcesso
	 * @return
	 * @throws DaoException
	 */
	@Override
	public List<OrigemDestino> recuperarPorIdOuDescricaoSetoresEOrgaosExternos(String value) throws DaoException {

		List<OrigemDestino> origensDestinos = null;

		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			
			hql.append("SELECT od FROM OrigemDestino od WHERE 1=1 ");
			
			// não recuperar advogados - somente tipo 2 e 3 (respectivamente setores e órgãos externos)
			hql.append(" AND od.tipoOrigemDestino <> 1");
			hql.append(" AND od.ativo = 'S' ");
			hql.append(" AND od.deslocaProcesso = 'S' ");

			if(value != null && !value.isEmpty()){
				if (soNumeros(value)) {
					hql.append(" AND od.id = " + value );
				} else {
					hql.append(" AND upper(od.descricao) LIKE upper('%" + value + "%') ");
				}
			}
			
			hql.append(" AND ROWNUM < 51 ORDER BY od.id ASC ");
			
			Query query = session.createQuery(hql.toString());
			
			origensDestinos = query.list();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}

		return origensDestinos;
	}
	
	/**
	 * Traz a apenas setores do STF da view.
	 * @param value
	 * @param ativo
	 * @param deslocaProcesso
	 * @return
	 * @throws DaoException
	 */
	@Override
	public List<OrigemDestino> recuperarPorIdOuDescricao(String value, Integer tipoOrigemDestino) throws DaoException {

		List<OrigemDestino> origensDestinos = null;

		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			
			hql.append("SELECT od FROM OrigemDestino od WHERE 1=1 ");
			
			hql.append(" AND od.tipoOrigemDestino = " + tipoOrigemDestino);
			hql.append(" AND od.ativo = 'S' ");
			hql.append(" AND od.deslocaProcesso = 'S' ");

			if(value != null && !value.isEmpty()){
				if (soNumeros(value)) {
					hql.append(" AND od.id = " + value );
				} else {
					hql.append(" AND upper(od.descricao) LIKE upper('%" + value + "%') ");
				}
			}
			
			hql.append(" AND ROWNUM < 51 ORDER BY od.id ASC ");
			
			Query query = session.createQuery(hql.toString());
			
			origensDestinos = query.list();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}

		return origensDestinos;
	}

	@Override
	public List<OrigemDestino> recuperarPorId(String value, Boolean ativo,
			Boolean deslocaProcesso) throws DaoException {
		List<OrigemDestino> origensDestinos = null;

		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			
			hql.append("SELECT od FROM OrigemDestino od WHERE 1=1 ");
			
			if(ativo != null && ativo){
				hql.append(" AND od.ativo = 'S' ");
			}
			
			if(deslocaProcesso != null && deslocaProcesso){
				hql.append(" AND od.deslocaProcesso = 'S' ");
			}
			
			if(value != null && !value.isEmpty()){
				hql.append(" AND upper(od.id) LIKE upper('"+ value+ "%') AND upper(od.id) LIKE upper('%" + value + "') ");
			}
			
			hql.append(" AND ROWNUM < 51 ORDER BY od.id ASC ");
			
			Query query = session.createQuery(hql.toString());
			
			origensDestinos = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}	
		return origensDestinos;
	}

	@Override
	public List<OrigemDestino> recuperarPorDescricao(String value, Boolean ativo,
			Boolean deslocaProcesso) throws DaoException {
		List<OrigemDestino> origensDestinos = null;

		try {
			Session session = retrieveSession();
			
			StringBuffer hql = new StringBuffer();
			
			hql.append("SELECT od FROM OrigemDestino od WHERE 1=1");
			
			if(ativo != null && ativo){
				hql.append("AND od.ativo = 'S' ");
			}
			
			if(deslocaProcesso != null && deslocaProcesso){
				hql.append("AND od.deslocaProcesso = 'S' ");
			}
			
			if(value != null && !value.isEmpty()){
				hql.append("AND (upper(od.descricao) LIKE upper('%"+ value + "%')) ");
			}
			
			hql.append("AND ROWNUM < 51 ORDER BY od.id ASC");
			
			
			Query query = session.createQuery(hql.toString());
			
			
			origensDestinos = query.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
		return origensDestinos;
	}
	
	/**
	 * Recupera um registro da view OrigemDestino, onde:
	 * id = o código da origem/destino
	 * tipoOrigemDestion = 1 - Advogado; 2 - Setor STF; 3 - Órgãos Externos
	 */
	@Override
	public OrigemDestino recuperarPorId(Long id, int tipoOrigemDestino) throws DaoException {
		OrigemDestino origenDestino = null;

		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			
			hql.append("SELECT od FROM OrigemDestino od WHERE 1=1 ");
			
			hql.append(" AND od.tipoOrigemDestino = " + tipoOrigemDestino );
			hql.append(" AND od.id = " + id );
			
			Query query = session.createQuery(hql.toString());
			
			origenDestino = (OrigemDestino) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}	
		return origenDestino;
	}
	
	/**
	 * Verifica se uma String na sua totalidade tem somente números.<br />
	 */
	
	private boolean soNumeros(String valor) {
		
		if(valor == null || valor.length() == 0 ){
			return false;
		}
		
		for(int i = 0 ; i < valor.length() ; i++){
			if(!Character.isDigit(valor.charAt(i))){
				return false;
			}
		}
		
		return true;
	}

	

}
