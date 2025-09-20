package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.FaseComunicacaoDao;
import br.gov.stf.estf.entidade.documento.FaseComunicacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class FaseComunicacaoDaoHibernate extends GenericHibernateDao<FaseComunicacao, Long> 
implements FaseComunicacaoDao{
	
	private static final long serialVersionUID = -1177829070152206842L;

	public FaseComunicacaoDaoHibernate() {
		super(FaseComunicacao.class);
	}
			
	public FaseComunicacao pesquisarFaseAtual(Long idComunicacao) throws DaoException {
		try {
			Session session = retrieveSession();

			StringBuilder hql = new StringBuilder("select fc from FaseComunicacao fc");
			hql.append(" WHERE fc.flagFaseAtual='S'");

			if (idComunicacao != null || idComunicacao != 0L){
				hql.append(" AND fc.comunicacao.id = " + idComunicacao);	
			}

			Query q = session.createQuery(hql.toString());
			return (FaseComunicacao) q.uniqueResult();

		} catch (Exception e) {
			throw new DaoException (e);
		}
	}

	public void incluirFase(FaseComunicacao faseComunicacao) throws DaoException{
		
		Session session = retrieveSession();
		try {
					
			atualizarHistorico(faseComunicacao.getComunicacao().getId());
			session.save(faseComunicacao);
			session.flush();

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	
		
	}

	private void atualizarHistorico (Long idComunicacao) throws DaoException {
		
		Session session = retrieveSession();
		try {
		
		StringBuffer sql = new StringBuffer("update judiciario.fase_comunicacao set flg_fase_atual='N' where flg_fase_atual='S' and seq_comunicacao ="+idComunicacao);
	
		PreparedStatement psUp = session.connection().prepareStatement(sql.toString());
		psUp.executeUpdate(sql.toString());
		psUp.close();

		}catch(SQLException e){
			throw new DaoException("SQLException",e);
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}
		  
	}
}
