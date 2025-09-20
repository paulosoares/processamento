package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.PrescricaoReu;
import br.gov.stf.estf.processostf.model.dataaccess.PrescricaoReuDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.DateTimeHelper;

@Repository
public class PrescricaoReuDaoHibernate extends GenericHibernateDao<PrescricaoReu, Long> implements PrescricaoReuDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4500258460084749041L;

	public PrescricaoReuDaoHibernate() {
		super(PrescricaoReu.class);
	}	

	@Override
	public List<PrescricaoReu> pesquisarProcessosPrescricao( Long idObjetoIncidente, Date dataInicialPena, Date dataFinalPena, 
			Long idMinistro, String codigoPena, Boolean filtroEmTramitacao) throws DaoException {
		
		List<PrescricaoReu> listaPrescricaoReu = null;
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();

		try {
			hql.append(" SELECT  peu FROM Processo po, PrescricaoReu peu, ");
			hql.append(" ReferenciaPrescricao rp, IncidentePreferencia ip ");
			hql.append(" WHERE po.id = rp.objetoIncidente.id ");
			hql.append(" AND ip.objetoIncidente.id = po.id ");
			hql.append(" AND rp.id = peu.referenciaPrescricao.id ");
			//tipo = 2 busca somente os processos criminais
			hql.append(" AND ip.tipoPreferencia.id = 2 ");
				
			if (idObjetoIncidente != null && idObjetoIncidente > 0L) {
				hql.append("   AND po.id = :idObjetoIncidente ");									
			}
			
			if (idMinistro != null && idMinistro > 0L) {
				hql.append("   AND po.ministroRelatorAtual.id = :idMinistro ");									

			}
			
			if(filtroEmTramitacao == true){
				hql.append("  AND po.filtroEmTramitacao = 'S' ");
			}
			
			if (dataInicialPena != null && dataFinalPena != null && codigoPena == null){
				hql.append(" AND ((peu.dataPrescricao between to_date(:dataInicialPena, 'DD/MM/YYYY HH24:MI:SS') " +
						" and to_date(:dataFinalPena , 'DD/MM/YYYY HH24:MI:SS')) ");
				hql.append(" OR (peu.dataPrescricaoPenaMinima between to_date(:dataInicialPena, 'DD/MM/YYYY HH24:MI:SS') " +
				"		and to_date(:dataFinalPena , 'DD/MM/YYYY HH24:MI:SS')) ");
				hql.append(" OR (peu.dataPrescricaoPenaMaxima between to_date(:dataInicialPena, 'DD/MM/YYYY HH24:MI:SS') " +
				"		and to_date(:dataFinalPena , 'DD/MM/YYYY HH24:MI:SS'))) ");
			}else if (dataInicialPena != null && dataFinalPena != null && codigoPena != null){
				if (codigoPena.equals("A")){
					hql.append(" AND ((peu.dataPrescricaoPenaMinima between to_date(:dataInicialPena, 'DD/MM/YYYY HH24:MI:SS') " +
					"		and to_date(:dataFinalPena , 'DD/MM/YYYY HH24:MI:SS')) ");
					hql.append(" OR (peu.dataPrescricaoPenaMaxima between to_date(:dataInicialPena, 'DD/MM/YYYY HH24:MI:SS') " +
					"		and to_date(:dataFinalPena , 'DD/MM/YYYY HH24:MI:SS'))) ");
				}else{
					hql.append(" AND peu.dataPrescricao between to_date(:dataInicialPena, 'DD/MM/YYYY HH24:MI:SS') " +
					" and to_date(:dataFinalPena , 'DD/MM/YYYY HH24:MI:SS') ");
				}
			}
			
			if (codigoPena != null && codigoPena.equals("A")){
				hql.append(" AND peu.dataPrescricaoPenaMinima  is not null" +
				" AND peu.dataPrescricaoPenaMaxima is not null  ");
			}
			
			if (codigoPena != null && codigoPena.equals("C")){
				hql.append(" AND peu.dataPrescricao is not null" );
			}
			
//			hql.append(" AND ROWNUM < 5 ");
			
			Query query = session.createQuery(hql.toString());
			
			if (idObjetoIncidente != null && idObjetoIncidente > 0L) {
				query.setLong("idObjetoIncidente", idObjetoIncidente);
			}

			if (idMinistro != null && idMinistro > 0L) {
				query.setLong("idMinistro", idMinistro);
			}

			if (dataInicialPena != null && dataFinalPena != null && codigoPena == null){
				query.setString("dataInicialPena", DateTimeHelper.getDataString(dataInicialPena) + " 00:00:00");
				query.setString("dataFinalPena", DateTimeHelper.getDataString(dataFinalPena) + " 23:59:59");
			}else if (dataInicialPena != null && dataFinalPena != null && codigoPena != null){
				if (codigoPena.equals("A")){
					query.setString("dataInicialPena", DateTimeHelper.getDataString(dataInicialPena) + " 00:00:00");
					query.setString("dataFinalPena", DateTimeHelper.getDataString(dataFinalPena) + " 23:59:59");
				}else{
					query.setString("dataInicialPena", DateTimeHelper.getDataString(dataInicialPena) + " 00:00:00");
					query.setString("dataFinalPena", DateTimeHelper.getDataString(dataFinalPena) + " 23:59:59");
				}
			}
				
			
			listaPrescricaoReu = query.list();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return listaPrescricaoReu;
		
	}

	

}
