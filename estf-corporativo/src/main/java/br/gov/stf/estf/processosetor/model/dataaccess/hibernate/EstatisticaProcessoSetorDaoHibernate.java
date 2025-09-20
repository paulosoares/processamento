package br.gov.stf.estf.processosetor.model.dataaccess.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.Secao;
import br.gov.stf.estf.entidade.localizacao.SecaoSetor;
import br.gov.stf.estf.entidade.localizacao.TipoFaseSetor;
import br.gov.stf.estf.entidade.localizacao.TipoStatusSetor;
import br.gov.stf.estf.entidade.processosetor.EstatisticaProcessoSetorSecao;
import br.gov.stf.estf.processosetor.model.dataaccess.EstatisticaProcessoSetorDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.HibernateDao;

@Repository
public class EstatisticaProcessoSetorDaoHibernate extends HibernateDao
implements EstatisticaProcessoSetorDao {
	
	
	private Boolean status = Boolean.FALSE;
	
	public EstatisticaProcessoSetorDaoHibernate (SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	public List<EstatisticaProcessoSetorSecao> gerarEstatisticaProcessoSetorSecao(
			Long idSetor) throws DaoException {
		
		List<EstatisticaProcessoSetorSecao> result = new LinkedList<EstatisticaProcessoSetorSecao>();
		
		Session session = retrieveSession();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			StringBuffer sql = new StringBuffer(
			" SELECT s.dsc_secao, COUNT(*) AS qtd"+
			" FROM EGAB.processo_setor ps   "+
			" LEFT JOIN EGAB.historico_deslocamento dps "+
			" ON dps.seq_processo_setor = ps.seq_processo_setor "+
			" LEFT JOIN EGAB.secao_setor ss "+
			" ON (ss.seq_secao = dps.seq_secao_destino "+
			" AND ss.cod_setor = dps.cod_setor) "+
			" LEFT JOIN EGAB.secao s "+
			" ON s.seq_secao = ss.seq_secao "+
			" WHERE ps.cod_setor = ? "+
			" AND ps.dat_saida_setor IS NULL "+
			" GROUP BY s.dsc_secao "+
			" ORDER BY s.dsc_secao");

			pstmt = session.connection().prepareStatement(sql.toString());
			pstmt.setLong(1, idSetor);
			rs = pstmt.executeQuery();
			
			while( rs.next() ) {
				
				result.add( montarListaEstatisticaProcessoSetorSecao(rs) );
			}
			
			//SQLQuery sqlQuery = session.createSQLQuery(sql.toString());
			
		}
        catch(HibernateException e) {
            throw new DaoException("HibernateException",
                    SessionFactoryUtils.convertHibernateAccessException(e));
        }
        catch( SQLException e ) {
        	throw new DaoException("SQLException", e);
        }
        catch( RuntimeException e ) {
            throw new DaoException("RuntimeException", e);
        }finally {
			try{
				if( pstmt != null )
					pstmt.close();
				if( rs != null )
					rs.close();
			}catch(SQLException e){
				throw new DaoException("SQLException Finally", e);
			}
		}		
		
        return result;
	}
	public List<EstatisticaProcessoSetorSecao> gerarEstatisticaProcessoFaseSetor(
			Long idSetor , Boolean status ) throws DaoException{
		
		this.status = status;
		List<EstatisticaProcessoSetorSecao> result = new LinkedList<EstatisticaProcessoSetorSecao>();
		Session session = retrieveSession();
		StringBuffer sql = new StringBuffer();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			
			if ( status.booleanValue() ){
				
				sql.append(
						" SELECT tf.dsc_tipo_fase_setor, ts.dsc_tipo_status_setor, count(*) AS qtd "+
						" FROM EGAB.processo_setor ps"+
						" LEFT JOIN EGAB.historico_fase hf"+
						" ON hf.seq_processo_setor = ps.seq_processo_setor"+
						" LEFT JOIN EGAB.tipo_fase_setor tf"+
						" ON tf.seq_tipo_fase_setor = hf.seq_tipo_fase_setor"+
						" LEFT JOIN EGAB.tipo_status_setor ts"+
						" ON ts.seq_tipo_status_setor = hf.seq_tipo_status_setor"+
						" WHERE ps.cod_setor = ? "+
						" AND ps.dat_saida_Setor IS NULL"+
						" GROUP BY tf.dsc_tipo_fase_setor, ts.dsc_tipo_status_setor");
					
			}
			else{		
			sql.append(
					" SELECT tf.dsc_tipo_fase_setor, COUNT(*) AS qtd" +
					" FROM EGAB.processo_setor ps" +
					" LEFT JOIN EGAB.historico_fase hf" +
					" ON hf.seq_processo_setor = ps.seq_processo_setor" +
					" LEFT JOIN EGAB.tipo_fase_setor tf" +
					" ON tf.seq_tipo_fase_setor = hf.seq_tipo_fase_setor" +
					" WHERE ps.cod_setor = ? " +
					" AND ps.dat_saida_setor IS NULL" +
					" GROUP BY tf.dsc_tipo_fase_setor" );
			
			}
			
			pstmt = session.connection().prepareStatement(sql.toString());
			pstmt.setLong(1, idSetor);
			rs = pstmt.executeQuery();
			
			while (rs.next()){
				
				result.add( montarListaProcessoFaseSetorEStatusSetor(rs) );
			}
					
		}catch(HibernateException e) {
		    throw new DaoException("HibernateException",
		     SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( SQLException e ) {
		 	throw new DaoException("SQLException", e);
		}
		catch( RuntimeException e ) {
		    throw new DaoException("RuntimeException", e);
		}finally {
			try{
				if( pstmt != null )
					pstmt.close();
				if( rs != null )
					rs.close();
			}catch(SQLException e){
				throw new DaoException("SQLException Finally", e);
			}
		}				
		
		return result;
	}
	/**
	 * 
	 * Metodo responsável por pegar um ResultSet e construir um objeto do tipo
	 * EstatisticaProcessoSetorSecao
	 * @param rs
	 * @return
	 * @throws DaoException
	 */
	private EstatisticaProcessoSetorSecao montarListaEstatisticaProcessoSetorSecao( ResultSet rs ) throws DaoException{
		
		try{
			EstatisticaProcessoSetorSecao estatistica = new EstatisticaProcessoSetorSecao();
			SecaoSetor secaoSetor = new SecaoSetor();
			Secao secao = new Secao();
			secaoSetor.setSecao(secao);		
			secao.setDescricao(rs.getString("dsc_secao"));		
			estatistica.setSecao( secaoSetor );
			estatistica.setQuantidade(rs.getLong("qtd"));
			
			return estatistica;
		}
		catch(SQLException ex ){
			throw new DaoException(ex);
		}
			
		
	}
	private EstatisticaProcessoSetorSecao montarListaProcessoFaseSetorEStatusSetor(ResultSet rs) throws DaoException {
					
		try{
			EstatisticaProcessoSetorSecao estatistica = new EstatisticaProcessoSetorSecao();
			
			TipoFaseSetor fase = new TipoFaseSetor();
			fase.setDescricao( rs.getString( "dsc_tipo_fase_setor" ) );
			estatistica.setFaseSetor( fase );
			
			if( status.booleanValue() ){
				TipoStatusSetor status = new TipoStatusSetor();
				status.setDescricao( rs.getString("dsc_tipo_status_setor") );
				estatistica.setStatusSetor( status );
			}
						
			estatistica.setQuantidade(rs.getLong("qtd"));
			return estatistica;
		}
		catch(SQLException ex ){
			throw new DaoException(ex);
		}
		
	}
	
}
