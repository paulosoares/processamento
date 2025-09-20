package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.SessionImplementor;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.processostf.model.dataaccess.IncidenteJulgamentoDao;
import br.gov.stf.estf.processostf.model.service.exception.DuplicacaoChaveAntigaException;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.SearchData;

@Repository
public class IncidenteJulgamentoDaoHibernate extends GenericHibernateDao<IncidenteJulgamento, Long>
		implements IncidenteJulgamentoDao {

	public IncidenteJulgamentoDaoHibernate() {
		super(IncidenteJulgamento.class);
	}

	private static final long serialVersionUID = -6996835210272663629L;

	@SuppressWarnings("unchecked")
	public List<IncidenteJulgamento> pesquisar(Long idObjetoIncidentePrincipal,
			String siglaTipoRecurso) throws DaoException {
		List<IncidenteJulgamento> resp = null;

		try {

			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append("SELECT i FROM IncidenteJulgamento i");
			hql.append(" WHERE (1=1)");
			
			if( idObjetoIncidentePrincipal != null ){
				hql.append(" AND i.principal.id = :idObjetoIncidentePrincipal");
			}
			
			if( SearchData.stringNotEmpty(siglaTipoRecurso) ){
				hql.append(" AND i.tipoJulgamento.sigla = :siglaTipoRecurso");
			}
			
			Query q = session.createQuery( hql.toString() );
			
			if( idObjetoIncidentePrincipal != null ){
				q.setLong("idObjetoIncidentePrincipal", idObjetoIncidentePrincipal);
			}
			
			if( SearchData.stringNotEmpty(siglaTipoRecurso) ){
				q.setString("siglaTipoRecurso", siglaTipoRecurso);
			}
			
			resp = q.list();			
			
		} catch ( Exception e ) {
			throw new DaoException(e);
		}

		return resp;
	}
	
	public IncidenteJulgamento recuperarIncidenteJulgamento(String siglaClasse,
			Long numeroProcesso, Long tipoRecurso, Long tipoJulgamento)
			throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	public IncidenteJulgamento inserirIncidenteJulgamento(
			Long idObjetoIncidentePai, Long idTipoRecurso,
			Integer sequenciaCadeia) throws DaoException {
		CallableStatement stmt = null;
		try {

			Session session = retrieveSession(); 
			
			Connection connection = session.connection();
			stmt = connection.prepareCall("{call JUDICIARIO.PRC_INSERE_INCIDENTE(?,?,?,?)}"); 

			stmt.setLong(1, idObjetoIncidentePai);
			stmt.setLong(2, idTipoRecurso);
			stmt.setLong(3, sequenciaCadeia);
			stmt.registerOutParameter(4, Types.LONGVARCHAR);
			stmt.execute();
			Long idObjetoIncidente = stmt.getLong(4);
			if( idObjetoIncidente != null ){
				Criteria criteria = session.createCriteria(IncidenteJulgamento.class);
				criteria.add(Restrictions.eq("id", idObjetoIncidente));
				return (IncidenteJulgamento) criteria.uniqueResult();
			}

		}catch( Exception e ) {
			throw new DaoException(e);
		}finally{
			if( stmt != null )
				try {
					stmt.close();
				} catch (SQLException e) {
					throw new DaoException(e);
				}
		}
		return null;
	}
	
	@Override
	public IncidenteJulgamento inserirIncidenteJulgamentoESTFDecisao(
			Long idObjetoIncidentePai, Long idTipoRecurso,
			Integer sequenciaCadeia) throws DaoException, DuplicacaoChaveAntigaException {
		CallableStatement stmt = null;
		try {

			Session session = retrieveSession(); 
			
			Connection connection = session.connection();
			stmt = connection.prepareCall("{call JUDICIARIO.PRC_INSERE_INCIDENTE(?,?,?,?)}"); 

			stmt.setLong(1, idObjetoIncidentePai);
			stmt.setLong(2, idTipoRecurso);
			stmt.setLong(3, sequenciaCadeia);
			stmt.registerOutParameter(4, Types.LONGVARCHAR);
			stmt.execute();
			Long idObjetoIncidente = stmt.getLong(4);
			if( idObjetoIncidente != null ){
				Criteria criteria = session.createCriteria(IncidenteJulgamento.class);
				criteria.add(Restrictions.eq("id", idObjetoIncidente));
				return (IncidenteJulgamento) criteria.uniqueResult();
			}

		}catch( Exception e ) {
			if (e.getMessage() != null && e.getMessage().contains("ORA-20001")) {
				throw new DuplicacaoChaveAntigaException(e);
			} else {
				throw new DaoException(e);
			}
		}finally{
			if( stmt != null )
				try {
					stmt.close();
				} catch (SQLException e) {
					throw new DaoException(e);
				}
		}
		return null;
	}

	public Integer proximaSequenciaCadeia(Long idObjetoIncidentePai, Long idTipoRecurso)
			throws DaoException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {

			Session session = retrieveSession(); 
			
			Connection connection = session.connection();
			stmt = connection.prepareStatement(
			 " select case when(max(num_sequencia_cadeia) > 0 )" +
			 " then max(num_sequencia_cadeia) + 1 " +
			 " else 1 end as PROX_SEQUENCIA " +
			 " from judiciario.objeto_incidente oi" +
			 " join judiciario.incidente_julgamento ij on ij.seq_objeto_incidente = oi.seq_objeto_incidente" +
			 " where oi.seq_objeto_incidente_pai = ?" +
			 "   and ij.seq_tipo_recurso = ?");
			
			stmt.setLong(1, idObjetoIncidentePai);
			stmt.setLong(2, idTipoRecurso);
			rs = stmt.executeQuery();
			if( rs != null && rs.next() ){
				return rs.getInt("PROX_SEQUENCIA");
			}

		}catch( Exception e ) {
			throw new DaoException(e);
		}finally{
			try {
				if( stmt != null )
					stmt.close();
				if( rs != null )
					rs.close();
			} catch (SQLException e) {
				throw new DaoException(e);
			}
		}
		return null;
	}

	public Boolean existeSequenciaCadeia(Long idObjetoIncidentePai,Long idTipoRecurso,Integer numeroSequenciaCadeia)
			throws DaoException {

		try {

			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append("SELECT count(i.id) FROM IncidenteJulgamento i");
			hql.append(" WHERE (1=1)");
			
			if( idObjetoIncidentePai != null )
				hql.append(" AND i.pai.id = :seqObjetoIncidentePai");
			
			if( idTipoRecurso != null )
				hql.append(" AND i.tipoJulgamento.id = :idTipoRecurso");
			
			hql.append(" AND i.numeroSequenciaCadeia = :numeroSequenciaCadeia");
			
			Query q = session.createQuery( hql.toString() );
			
			if( idObjetoIncidentePai != null )
				q.setLong("seqObjetoIncidentePai", idObjetoIncidentePai);
			
			if( idTipoRecurso != null )
				q.setLong("idTipoRecurso", idTipoRecurso);

			q.setInteger("numeroSequenciaCadeia", numeroSequenciaCadeia);
			
			Long qtd = (Long) q.uniqueResult();
			
			return qtd != null && qtd > 0L;
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
	}
	
	public List<IncidenteJulgamento> recuperarIdObjetoIncidente(String siglaProcesso, Long numeroProcesso) 
	throws DaoException {
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();	
		
		try {

			
			hql.append(" SELECT ij FROM ObjetoIncidente oi, Processo p, IncidenteJulgamento ij " );
			hql.append(" WHERE oi.principal.id = p.id ");
			hql.append(" AND oi.id = ij.id ");
			
			if (siglaProcesso != null && siglaProcesso.length() > 0){
				hql.append(" AND p.siglaClasseProcessual = :siglaProcesso ");
			}
			
			if (numeroProcesso != null && numeroProcesso > 0){
				hql.append(" AND p.numeroProcessual = :numeroProcesso ");
			}
			
			Query q = session.createQuery(hql.toString());
			
			if (siglaProcesso != null){
				q.setString("siglaProcesso", siglaProcesso);
			}
			
			if (numeroProcesso != null){
				q.setLong("numeroProcesso", numeroProcesso);
			}
			
			return q.list();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	public String excluirIncidenteJulgamento(Long ij) throws DaoException {
		CallableStatement stmt = null;
		String msg="";
		try {
			Session session = retrieveSession(); 
			Connection connection = session.connection();
			stmt = connection.prepareCall("{call JUDICIARIO.PKG_ELIMINA_RECURSO.PRC_ELIMINA_INCIDENTE(?)}"); 
			stmt.setLong(1, ij);
			stmt.execute();
			connection.commit();
			
		}catch( Exception e ) {
			msg = e.toString();
			throw new DaoException(e);
		}finally{
			if( stmt != null )
				try {
					stmt.close();
				} catch (SQLException e) {
					throw new DaoException(e);
				}
		}
		return msg;
	}

	@Override
	public void pautarRJ(ObjetoIncidente<?> objetoIncidente) throws DaoException {
		try{
			Session session = retrieveSession();
			SessionImplementor sessao = (SessionImplementor) session;
			
			CallableStatement stmt = sessao.getBatcher().prepareCallableStatement("{call STF.PRC_DJ_PAUTAR_RJ( ? )}");
			
			stmt.setLong(1, objetoIncidente.getId());			
			
			stmt.execute();
			
			sessao.getBatcher().closeStatement(stmt);
			
		}catch (Exception e) {
			throw new DaoException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public Ministro recuperarRedatorIncidente(ObjetoIncidente<?> objetoIncidente) throws DaoException {
			String sql = "SELECT m.* FROM STF.MINISTROS m INNER JOIN JUDICIARIO.VW_INCIDENTE_REDATOR v ON v.cod_ministro_redator = m.cod_ministro WHERE v.SEQ_OBJETO_INCIDENTE = :oi";
			
			return (Ministro) retrieveSession().createSQLQuery(sql)
					.addEntity(Ministro.class)
					.setParameter("oi", objetoIncidente.getId())
					.uniqueResult();
	}
}

