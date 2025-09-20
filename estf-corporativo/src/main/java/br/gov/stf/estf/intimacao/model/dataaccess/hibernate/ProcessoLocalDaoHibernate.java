package br.gov.stf.estf.intimacao.model.dataaccess.hibernate;

import java.math.BigDecimal;
import java.util.List;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.TipoMeioProcesso;
import br.gov.stf.estf.intimacao.model.dataaccess.ProcessoLocalDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;


@Repository
public class ProcessoLocalDaoHibernate extends GenericHibernateDao<Processo, Long> implements ProcessoLocalDao {

    public ProcessoLocalDaoHibernate() {
        super(Processo.class);
    }

    public static final long serialVersionUID = 1L;

    @Override
    public Processo recuperarProcessoEletronico(String classe, Long numero, Long idSetor) throws DaoException {
        Processo resp = null;

        try {
            Session session = retrieveSession();
            Criteria c = session.createCriteria(Processo.class);

            if (classe != null && classe.trim().length() > 0) {
                c.add(Restrictions.eq("classeProcessual.id", classe).ignoreCase());
            }

            if (numero != null) {
                c.add(Restrictions.eq("numeroProcessual", numero));
            }

            c.add(Restrictions.eq("tipoMeioProcesso", TipoMeioProcesso.ELETRONICO));

            resp = (Processo) c.uniqueResult();
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return resp;
    }
    
    public List<ObjetoIncidente<?>> recuperarIncidentesDoProcessoEletronico(Long idObjetoIncidentePrincipal) throws DaoException {
    	Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();

		try {			
			
			hql.append(" SELECT oi FROM ObjetoIncidente oi ");
			hql.append(" WHERE oi.principal.id = '" + idObjetoIncidentePrincipal + "' ");

			Query q = session.createQuery(hql.toString());		

			return q.list();

        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

	@Override
	public Processo recuperarProcessoEletronicoPorIdProcessoPrincipal(Long idObjetoIncidentePrincipal) throws DaoException {
		Processo processo = new Processo();
        StringBuffer sql = new StringBuffer();
        try {
            sql.append(" SELECT p.SIG_CLASSE_PROCES, p.NUM_PROCESSO FROM JUDICIARIO.OBJETO_INCIDENTE oi ");
            sql.append(" INNER JOIN JUDICIARIO.VW_PROCESSO_RELATOR p on ( oi.SEQ_OBJETO_INCIDENTE_PRINCIPAL = p.SEQ_OBJETO_INCIDENTE) ");
            sql.append(" WHERE oi.SEQ_OBJETO_INCIDENTE = :idObjetoIncidentePrincipal");            

            SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());
            
            sqlQuery.setLong("idObjetoIncidentePrincipal", idObjetoIncidentePrincipal);
           
            Object[] obj = (Object[]) sqlQuery.uniqueResult();
            String siglaClasseProcessual = (String) obj[0];
            BigDecimal numeroProcesso = (BigDecimal) obj[1];
            
            processo.setSiglaClasseProcessual(siglaClasseProcessual);
            processo.setNumeroProcessual(numeroProcesso.longValue());
        } catch (Exception e) {
            throw new DaoException(e);
        }
       	return processo;
	}   
}