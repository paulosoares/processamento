package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.HistoricoProcessoOrigem;
import br.gov.stf.estf.entidade.processostf.Procedencia;
import br.gov.stf.estf.entidade.processostf.TipoHistorico;
import br.gov.stf.estf.processostf.model.dataaccess.HistoricoProcessoOrigemDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class HistoricoProcessoOrigemDaoHibernate extends
		GenericHibernateDao<HistoricoProcessoOrigem, Long> implements HistoricoProcessoOrigemDao {

	public HistoricoProcessoOrigemDaoHibernate() {
		super(HistoricoProcessoOrigem.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5828281070407186206L;

    public List<HistoricoProcessoOrigem> recuperarPorObjetoIncidente(Long objetoIncidente) throws DaoException{

    	Session session = retrieveSession();
    	List<HistoricoProcessoOrigem> listaHistorico = null;
	    try {
	    	Criteria criteria = session.createCriteria(HistoricoProcessoOrigem.class);

	    	if(objetoIncidente != null && objetoIncidente > 0){
                criteria.add(Restrictions.eq("objetoIncidente", objetoIncidente));
                criteria.add(Restrictions.eq("tipoHistorico", TipoHistorico.ORIGEM));
            }
            criteria.addOrder(Order.asc("id"));
	    	listaHistorico = criteria.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	    
	    return listaHistorico;    	
    }
    
    public List<HistoricoProcessoOrigem> recuperarTodosPorObjetoIncidente(Long objetoIncidente) throws DaoException{

    	Session session = retrieveSession();
    	List<HistoricoProcessoOrigem> listaHistorico = null;
	    try {
	    	Criteria criteria = session.createCriteria(HistoricoProcessoOrigem.class);

	    	if(objetoIncidente != null && objetoIncidente > 0){
                criteria.add(Restrictions.eq("objetoIncidente", objetoIncidente));
            }
            criteria.addOrder(Order.asc("id"));
	    	listaHistorico = criteria.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	    
	    return listaHistorico;    	
    }

    
    /**
     * Recupera a origem (iniciada eletronicamente pelo STJ)
     * @param historicoProcessoOrigem
     * @return uma instancia do objeto historicoProcessoOrigem populado com a origem inicial do STJ - NULL quando não existir.
     * @throws DaoException
     */
    public HistoricoProcessoOrigem recuperarOrigemInicialSTJ(Long idObjetoIncidente) throws DaoException{
	    try {
	    	StringBuffer sql = new StringBuffer();
	    	sql.append("      SELECT HPO.SIG_CLASSE_ORIGEM, HPO.NUM_PROCESSO_ORIGEM, P.SIG_PROCEDENCIA ");
	    	sql.append("	  	FROM ESTF.PROCESSO_ORIGEM PO,");
	    	sql.append("	   	     ESTF.HISTORICO_PROCESSO_ORIGEM HPO,");
	    	sql.append("	   	     JUDICIARIO.procedencia P");
	    	sql.append("	   	WHERE PO.SEQ_PROCESSO_ORIGEM = HPO.SEQ_PROCESSO_ORIGEM");
	    	sql.append("	   	  AND p.cod_procedencia = hpo.cod_procedencia");
	    	sql.append("	   	  AND po.seq_objeto_incidente = " + idObjetoIncidente);
	    	sql.append("	   	  AND HPO.COD_ORIGEM IN (7, 5186)");
	    	sql.append("	   	  AND HPO.FLG_PRINCIPAL = 'S'");
	    	
			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());
//			sqlQuery.addEntity(HistoricoProcessoOrigem.class);
//			List<HistoricoProcessoOrigem> historicos = sqlQuery.list();
			List<Object[]> listaResult = sqlQuery.list();
			if (listaResult == null) {
				return null;
			}
			HistoricoProcessoOrigem historicoResult = new HistoricoProcessoOrigem();
			for (Object[] registro : listaResult) {
				if (registro[0] == null) {
					historicoResult.setSiglaClasseOrigem( "" );
				} else {
					historicoResult.setSiglaClasseOrigem( registro[0].toString() );
				}
				if (registro[1] == null) {
					historicoResult.setNumeroProcessoOrigem( "" );
				} else {
					historicoResult.setNumeroProcessoOrigem( registro[1].toString() );
				}
				Procedencia procedencia = new Procedencia();
				procedencia.setSiglaProcedencia( registro[2].toString() );
				historicoResult.setProcedencia(procedencia);
				break;
			}
			
			return historicoResult;
	    	
		} catch (Exception e) {
			throw new DaoException(e);
		}
    	
    
    }

}
