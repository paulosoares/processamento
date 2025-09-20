package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoInteresse;
import br.gov.stf.estf.processostf.model.dataaccess.ProcessoInteresseDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ProcessoInteresseDaoHibernate extends GenericHibernateDao<ProcessoInteresse, Long> implements ProcessoInteresseDao {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProcessoInteresseDaoHibernate(){
		super(ProcessoInteresse.class);
	}
	
	@Override
	public List<ProcessoInteresse> recuperarProcessosInteresse(Jurisdicionado advogado) throws DaoException {
		try {
			
			StringBuilder select = new StringBuilder();
			select.append("SELECT SEQ_PROCESSO_INTERESSE, SEQ_JURISDICIONADO, SEQ_OBJETO_INCIDENTE FROM JUDICIARIO.PROCESSO_INTERESSE ");
			select.append(" WHERE SEQ_JURISDICIONADO = " + advogado.getId());
			SQLQuery q = retrieveSession().createSQLQuery(select.toString());
			q.addEntity(ProcessoInteresse.class);
			return q.list();

/*			Session session = retrieveSession();

			Criteria c = session.createCriteria(ProcessoInteresse.class)
		    	.add( Restrictions.eq("advogado.id", advogado.getId()))
		    	.add( Restrictions.eq("advogado.id", advogado.getId()));
			
			List<ProcessoInteresse> processosInteresse = c.list();
			return processosInteresse;
*/
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}
	
	@Override
	public ProcessoInteresse recuperarProcessosInteresse(Jurisdicionado advogado, Processo processo) throws DaoException {
		try {
			
			StringBuilder select = new StringBuilder();
			select.append("SELECT SEQ_PROCESSO_INTERESSE, SEQ_JURISDICIONADO, SEQ_OBJETO_INCIDENTE FROM JUDICIARIO.PROCESSO_INTERESSE ");
			select.append(" WHERE SEQ_JURISDICIONADO = " + advogado.getId());
			select.append(" AND SEQ_OBJETO_INCIDENTE = " + processo.getId());
			SQLQuery q = retrieveSession().createSQLQuery(select.toString());
			q.addEntity(ProcessoInteresse.class);
			return (ProcessoInteresse) q.uniqueResult();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}
	
	@Override
	public Boolean existeMovimentada(Jurisdicionado advogado) throws DaoException {
		try {
			
			StringBuilder select = new StringBuilder();
			
			select.append("			SELECT count(*)");
			select.append("			FROM STF.ANDAMENTO_PROCESSOS ap,");
			select.append("			     STF.ANDAMENTOS a,");
			select.append("			     JUDICIARIO.PROCESSO_INTERESSE pi,");
			select.append("			     JUDICIARIO.PROCESSO p,");
			select.append("			     JUDICIARIO.JURISDICIONADO j,");
			select.append("			     JUDICIARIO.IDENTIFICACAO_PESSOA i");
			select.append("			WHERE ap.COD_ANDAMENTO = A.COD_ANDAMENTO");
		    select.append("			  AND AP.SEQ_OBJETO_INCIDENTE = PI.SEQ_OBJETO_INCIDENTE");
			select.append("			  AND PI.SEQ_OBJETO_INCIDENTE = P.SEQ_OBJETO_INCIDENTE");
			select.append("			  AND PI.SEQ_JURISDICIONADO = " + advogado.getId());
			select.append("			  AND PI.SEQ_JURISDICIONADO = J.SEQ_JURISDICIONADO");
			select.append("			  AND I.SEQ_JURISDICIONADO = J.SEQ_JURISDICIONADO (+)");
			select.append("			  AND I.SEQ_TIPO_IDENTIFICACAO (+) = 2");
			select.append("			  AND ap.DAT_ANDAMENTO BETWEEN (sysdate - 7) AND sysdate");

			SQLQuery q = retrieveSession().createSQLQuery(select.toString());
			if (q == null) {
				return false;
			}
			Long total = NumberUtils.createLong(q.uniqueResult().toString());
			if (total == null) {
				return false;
			}
			if (total.equals(new Long(0))) {
				return false;
			}
			if (total > 0) {
				return true;
			}

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return false;
		
	}

}
