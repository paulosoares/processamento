package br.gov.stf.estf.processosetor.model.dataaccess.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.Secao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processosetor.HistoricoDeslocamento;
import br.gov.stf.estf.entidade.processosetor.HistoricoDeslocamentoPeticao;
import br.gov.stf.estf.entidade.processosetor.PeticaoSetor;
import br.gov.stf.estf.entidade.processosetor.ProcessoSetor;
import br.gov.stf.estf.processosetor.model.dataaccess.DeslocamentoProcessoSetorDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.DateTimeHelper;

@Repository
public class DeslocamentoProcessoSetorDaoHibernate
extends GenericHibernateDao<HistoricoDeslocamento, Long>
implements DeslocamentoProcessoSetorDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3744816067546306023L;

	public DeslocamentoProcessoSetorDaoHibernate() {
		super(HistoricoDeslocamento.class);
	} 

	public HistoricoDeslocamento recuperarDeslocamento(Long id) throws DaoException {
		
		HistoricoDeslocamento deslocamento = null;
		
		Session session = retrieveSession();
		
		try {
			Criteria criteria = session.createCriteria(HistoricoDeslocamento.class);
			
			criteria.add(Restrictions.idEq(id));
			
			deslocamento = (HistoricoDeslocamento) criteria.uniqueResult();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}
		
		return deslocamento;
	}

	public List<HistoricoDeslocamento> pesquisarDeslocamentos(Long id, Setor setor, ProcessoSetor processoSetor, 
			PeticaoSetor peticaoSetor, Date dataRemessa, String numeroSala, String numeroEstante, 
			String numeroPrateleira, String numeroArmario, String numeroColuna) throws  DaoException {
		Session session = retrieveSession();

		List<HistoricoDeslocamento> deslocamentos = null;

		try {

			Criteria criteria = session.createCriteria(HistoricoDeslocamento.class);

			if(id != null) {
				criteria.add(Restrictions.eq("id", id));
			}
			
			if(setor != null && setor.getId() != null) {
				criteria.add(Restrictions.eq("setor.id", setor.getId()));
			}
			
			if(processoSetor != null && processoSetor.getId() != null) {
				criteria.add(Restrictions.eq("processoSetor.id", processoSetor.getId()));
			}
			
			if(peticaoSetor != null && peticaoSetor.getId() != null) {
				criteria.add(Restrictions.eq("peticaoSetor.id", peticaoSetor.getId()));
			}
			
			if(dataRemessa != null) {
				criteria.add(Restrictions.eq("dataRemessa", dataRemessa));
			}
			
			if(numeroSala != null && !numeroSala.equals("")) {
				criteria.add(Restrictions.eq("numeroSala", numeroSala));
			}
			
			if(numeroEstante != null && !numeroEstante.equals("")) {
				criteria.add(Restrictions.eq("numeroEstante", numeroEstante));
			}
			if(numeroPrateleira != null && !numeroPrateleira.equals("")) {
				criteria.add(Restrictions.eq("numeroPrateleira", numeroPrateleira));
			}
			
			if(numeroArmario != null && !numeroArmario.equals("")) {
				criteria.add(Restrictions.eq("numeroArmario", numeroArmario));
			}
			
			if(numeroColuna != null && !numeroArmario.equals("")) {
				criteria.add(Restrictions.eq("numeroColuna", numeroColuna));
			}
			
			criteria.addOrder(Order.desc("id"));
			deslocamentos = criteria.list();

			//Query query = session.createQuery();
			//List result = query.list();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return deslocamentos;    	
	}
	
	public List<HistoricoDeslocamento> pesquisarDeslocamentos(Long idSetor, 
            Secao secaoOrigem, Secao secaoDestino) 
    throws DaoException {

		Session session = retrieveSession();

		try {
			StringBuffer hql = new StringBuffer(" FROM HistoricoDeslocamento d " + " WHERE 1=1 ");

			if(idSetor != null ){
				hql.append(" AND d.setor.id = " + idSetor);
			}
			if(secaoOrigem != null && secaoOrigem.getId() != null && secaoDestino == null){
				hql.append(" AND d.secaoOrigem.id= " + secaoOrigem.getId());
			}
			if(secaoDestino != null && secaoDestino.getId() != null && secaoOrigem == null){
				hql.append(" AND d.secaoDestino.id= " + secaoDestino.getId());
			}
			if(secaoOrigem != null && secaoOrigem.getId() != null && secaoDestino != null && secaoDestino.getId() != null){
				hql.append(" AND (d.secaoOrigem.id= " + secaoOrigem.getId() + " OR d.secaoDestino.id= " + secaoDestino.getId() + ") ");
			}

			Query query = session.createQuery(hql.toString());

			List<HistoricoDeslocamento> result = query.list();

			return result;
		} catch (HibernateException e) {
			throw new DaoException(SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException(e);
		}
	}	
	
	public List<HistoricoDeslocamento> pesquisarDeslocamentos(Long id, Setor setor, ProcessoSetor processoSetor, 
            Date dataRecebimento, Secao secaoOrigem, Date dataRemessa, Secao secaoDestino, 
            String numeroSala, String numeroEstante, String numeroPrateleira, String numeroArmario, String numeroColuna, Boolean localizadoSetor) 
    throws DaoException {

		Session session = retrieveSession();

		try {
			StringBuffer hql = new StringBuffer(" FROM HistoricoDeslocamento d " + " WHERE 1=1 ");

			if(id!=null){
				hql.append(" and d.id = " + id);
			}
			if(setor!=null&&setor.getId()!=null){
				hql.append(" AND d.setor.id = " + setor.getId());
			}
			if(processoSetor!=null&&processoSetor.getId()!=null){
				hql.append(" AND d.processoSetor.id= " + processoSetor.getId());
			}
			if(dataRecebimento!=null){
				hql.append(" AND d.dataRecebimento = to_date('" + DateTimeHelper.getDataString(dataRecebimento) + "','dd/mm/yyyy')");
			}
			if(secaoOrigem!=null&&secaoOrigem.getId()!=null){
				hql.append(" AND d.secaoOrigem.id= " + secaoOrigem.getId());
			}
			if(dataRemessa!=null){
				hql.append(" AND d.dataRemessa = to_date('" + DateTimeHelper.getDataString(dataRemessa) + "','dd/mm/yyyy')");
			}
			if(secaoDestino!=null&&secaoDestino.getId()!=null){
				hql.append(" AND d.secaoDestino.id= " + secaoDestino.getId());
			}
			if(numeroSala!=null&&!numeroSala.equals("")){
				hql.append(" AND d.numeroSala = '" + numeroSala+"'");
			}
			if(numeroEstante!=null&&!numeroEstante.equals("")){
				hql.append(" AND d.numeroEstante = '" + numeroEstante+"'");
			}
			if(numeroPrateleira!=null&&!numeroPrateleira.equals("")){
				hql.append(" AND d.numeroPrateleira = '" + numeroPrateleira+"'");
			}
			if(numeroArmario!=null&&!numeroArmario.equals("")){
				hql.append(" AND d.numeroArmario = '" + numeroArmario+"'");
			}
			if(numeroColuna!=null&&!numeroArmario.equals("")){
				hql.append(" AND d.numeroColuna = '" + numeroColuna+"'");
			}
			hql.append(" AND d.dataRemessa = (SELECT MAX(dg.dataRemessa) FROM HistoricoDeslocamento dg WHERE 1=1 "+
					" AND dg.processoSetor.id = d.processoSetor.id" +
			" AND d.setor.id = dg.setor.id)");
			
			if( localizadoSetor != null && localizadoSetor )
				hql.append(" AND d.processoSetor.dataSaida IS NULL ");
			
			hql.append(" ORDER BY d.id DESC ");


			Query query = session.createQuery(hql.toString());

			List<HistoricoDeslocamento> result = query.list();

			return result;
		} catch (HibernateException e) {
			throw new DaoException(SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException(e);
		}
	}

	public List<HistoricoDeslocamento> pesquisarDeslocamentos(Long id, Setor setor, String siglaClasseProcessual, Date dataRecebimento, Secao secaoOrigem, Date dataRemessa, Secao secaoDestino, String numeroSala, String numeroEstante, String numeroPrateleira, String numeroArmario, String numeroColuna, Boolean semLocalizacao, Date dataInicio, Date dataFim , Boolean emTramite) throws DaoException {
		Session session = retrieveSession();

		try {
			StringBuffer hql = new StringBuffer(" FROM HistoricoDeslocamento d " + " WHERE 1=1 ");

			if(id!=null){
				hql.append(" AND d.id = " + id);
			}
			if(setor!=null&&setor.getId()!=null){
				hql.append(" AND d.setor.id = " + setor.getId());
			}
			if(siglaClasseProcessual!=null&&!siglaClasseProcessual.equals("")){
				hql.append(" AND d.processoSetor.siglaClasseProcessual= '" +siglaClasseProcessual.toUpperCase() +"'");
			}
			if(dataRecebimento!=null){
				hql.append(" AND d.dataRecebimento = to_date('" + DateTimeHelper.getDataString(dataRecebimento) + "','dd/mm/yyyy')");
			}
			if(secaoOrigem!=null&&secaoOrigem.getId()!=null){
				hql.append(" AND d.secaoOrigem.id= " + secaoOrigem.getId());
			}
			if(dataRemessa!=null){
				hql.append(" AND d.dataRemessa = to_date('" + DateTimeHelper.getDataString(dataRemessa) + "','dd/mm/yyyy')");
			}
			if(secaoDestino!=null&&secaoDestino.getId()!=null){
				hql.append(" AND d.secaoDestino.id= " + secaoDestino.getId());
			}
			if(semLocalizacao==null||!semLocalizacao.booleanValue()){
				if(numeroSala!=null&&!numeroSala.equals("")){
					hql.append(" AND d.numeroSala like '" + numeroSala+"%'");
				}
				if(numeroEstante!=null&&!numeroEstante.equals("")){
					hql.append(" AND d.numeroEstante like '" + numeroEstante+"%'");
				}
				if(numeroPrateleira!=null&&!numeroPrateleira.equals("")){
					hql.append(" AND d.numeroPrateleira like '" + numeroPrateleira+"%'");
				}
				if(numeroArmario!=null&&!numeroArmario.equals("")){
					hql.append(" AND d.numeroArmario like '" + numeroArmario+"%'");
				}
				if(numeroColuna!=null&&!numeroColuna.equals("")){
					hql.append(" AND d.numeroColuna like '" + numeroColuna+"'%");
				}
			}else{
				
				hql.append(" AND d.numeroSala  is null");
				hql.append(" AND d.numeroEstante  is null");
				hql.append(" AND d.numeroPrateleira  is null");
				hql.append(" AND d.numeroArmario  is null");
				hql.append(" AND d.numeroColuna  is null");
			}

						
			if(dataInicio != null){

				hql.append(" AND d.dataRemessa >= to_date('" + DateTimeHelper.getDataString(dataInicio) + " 00:00:00','dd/mm/yyyy hh24:mi:ss ')");

			}

			if(dataFim != null ){

				hql.append(" AND d.dataRemessa <= to_date('" + DateTimeHelper.getDataString(dataFim) + " 23:59:59','dd/mm/yyyy hh24:mi:ss')");
				
			}

			if(emTramite!=null){
				if(emTramite.booleanValue()){
					hql.append(" AND d.processoSetor.dataSaida IS NULL ");
				}else{
					hql.append(" AND d.processoSetor.dataSaida IS NOT NULL ");
				}
			}
			
			
			if(dataRemessa==null){
				hql.append(" AND d.dataRemessa = (SELECT MAX(dg.dataRemessa) FROM HistoricoDeslocamento dg WHERE 1=1 "+
						" AND dg.processoSetor.id = d.processoSetor.id" +
				" AND d.setor.id = dg.setor.id)");

				hql.append(" ORDER BY  d.id DESC , d.dataRemessa ");
				
			}
			
			
			Query query = session.createQuery(hql.toString());

			List result = query.list();

			return result;
		} catch (HibernateException e) {
			throw new DaoException(SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException(e);
		}
	}
	/*private ResultSet pesquisarQuantidadeProcessoSecao( Long id ) throws DaoException {

		Session sessao = retrieveSession();
		String sql = 
		" SELECT s.dsc_secao, count(*) FROM EGAB.processo_setor ps" +
		" LEFT JOIN EGAB.historico_deslocamento dps" +
		" ON dps.seq_processo_setor = ps.seq_processo_setor" +
		" LEFT JOIN EGAB.secao_setor ss" +
		" ON (ss.seq_secao = dps.seq_secao_destino" +
		" AND ss.cod_setor = dps.cod_setor)" +
		" LEFT JOIN EGAB.secao s" +
		" ON s.seq_secao = ss.seq_secao" +
		" WHERE ps.cod_setor = ? " +
		" AND ps.dat_saida_Setor IS NULL " +
		" GROUP BY s.dsc_secao ";

		try{
			ResultSet rs;
			PreparedStatement stmt = sessao.connection().prepareStatement(sql);
			stmt.clearParameters();
			stmt.setLong( 0 , id );
			rs = stmt.executeQuery();

			return rs;

		}catch(HibernateException ex){
			throw new DaoException(SessionFactoryUtils.convertHibernateAccessException(ex));

		}catch(RuntimeException ex){
			throw new DaoException(ex);

		}catch(SQLException ex){
			throw new DaoException(ex);
		}
	}*/
	
	public Boolean persistirHistoricoDeslocamento(HistoricoDeslocamento historicoDeslocamento) 
	throws DaoException {

		Boolean alterado = Boolean.FALSE;

		Session session = retrieveSession();

		try {

			session.persist(historicoDeslocamento);
			session.flush();

			alterado = Boolean.TRUE;

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return alterado;
	}
	
	public Boolean persistirHistoricoDeslocamentoPeticao(HistoricoDeslocamentoPeticao historicoDeslocamento) 
	throws DaoException {

		Boolean alterado = Boolean.FALSE;

		Session session = retrieveSession();

		try {

			session.persist(historicoDeslocamento);
			session.flush();

			alterado = Boolean.TRUE;

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return alterado;
	}	
	
	public Boolean excluirHistoricoDeslocamento(HistoricoDeslocamento deslocamento) 
	throws DaoException{
		Session session = retrieveSession();
		Boolean alterado = Boolean.FALSE;
		
		try{

			session.delete(deslocamento);
			session.flush();

			alterado = Boolean.TRUE;

		}catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	

		return alterado;
	}
	
}