package br.gov.stf.estf.usuario.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.usuario.Transacao;
import br.gov.stf.estf.usuario.model.dataaccess.TransacaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TransacaoDaoHibernate extends GenericHibernateDao<Transacao, Long> implements TransacaoDao {

	public TransacaoDaoHibernate() {
		super(Transacao.class);
	}

	private static final long serialVersionUID = 1L;

	public Transacao recuperarPorSistemaOrdem(String sistema, int numOrdem) throws DaoException {
		Session session = retrieveSession();
		Criteria c = session.createCriteria(Transacao.class);
		c.add(Restrictions.eq("siglaSistema", sistema));
		c.add(Restrictions.eq("numOrdem", numOrdem));
		c.setFetchMode("perfis", FetchMode.JOIN);
		return (Transacao) c.uniqueResult();
	}
	
	public boolean usuarioPossuiTransacao(String sigUsuario, String sistema, String transacao) throws DaoException {
		
		List<String> listaUsuario = null;
	    StringBuffer sql = new StringBuffer();
		
	    try {
	    	
	    	sql.append(" SELECT P.SEQ_PERFIL, P.DSC_PERFIL								"
	    			 + "	FROM GLOBAL.PERFIL_USUARIO U								"
	    			 + "   	   , GLOBAL.PERFIL P										"
	    			 + "   	   , GLOBAL.TRANSACAO_PERFIL TP								"
	    			 + "   	   , GLOBAL.TRANSACAO T										"
	    			 + " WHERE P.SEQ_PERFIL = U.SEQ_PERFIL								"
	    			 + "  	AND P.SEQ_PERFIL = TP.SEQ_PERFIL							"
	    			 + "   	AND TP.SEQ_TRANSACAO = T.SEQ_TRANSACAO						"
	    			 + "   	AND SIG_USUARIO = :SIG_USUARIO								"
	    			 + "   	AND P.SIG_SISTEMA = :SIG_SISTEMA							"
	    			 + "   	AND T.DSC_TRANSACAO = :DSC_TRANSACAO						");
	    	
	    	
	    	SQLQuery sqlQuery = retrieveSession().createSQLQuery( sql.toString() );
	    	
    		sqlQuery.setString("SIG_USUARIO", sigUsuario);
    		sqlQuery.setString("SIG_SISTEMA", sistema);
    		sqlQuery.setString("DSC_TRANSACAO", transacao);
	    	
			listaUsuario = sqlQuery.list();
			
			return (listaUsuario != null && ! listaUsuario.isEmpty()); 
		} catch (Exception e) {
			throw new DaoException(e);
		}

	}	


}
