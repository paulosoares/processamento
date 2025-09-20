package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.processostf.model.dataaccess.PeticaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class PeticaoDaoHibernate extends GenericHibernateDao<Peticao, Long> implements PeticaoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4017676997804821982L;

	public PeticaoDaoHibernate() {
		super(Peticao.class);
	}

	public Peticao recuperarPeticao(Long numero, Short ano) throws DaoException {

		Session session = retrieveSession();

		Peticao peticao = null;

		try {
			Criteria criteria = session.createCriteria(Peticao.class);
			criteria.add(Restrictions.eq("numeroPeticao", numero));
			criteria.add(Restrictions.eq("anoPeticao", ano));
//			peticao = (Peticao) criteria.uniqueResult();
			if (criteria.list().size() == 0) {
				peticao = null;
			} else {
				peticao = (Peticao) criteria.list().get(0);
			}

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return peticao;
	}

	public Long persistirPeticao(Peticao peticao) throws DaoException {

		Session session = retrieveSession();
		Long numero = recuperarNumeroPeticao();
		peticao.setNumeroPeticao(numero);

		try {
			// System.out.println("Gravando Peticao.....");
			session.save(peticao);
			session.flush();
			// System.out.println("Flush, Gravado.");
			return numero;
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

	}

	private Long recuperarNumeroPeticao() {

		String tipo = "PA";
		try {
			Session session = retrieveSession();
			Long id = null;

			Connection connection = session.connection();

			CallableStatement stmt = connection.prepareCall("{?=call STF.FNC_RETORNA_ANO_NUM_PETICAO (?,?)}");

			java.sql.Date data = null;
			data = new java.sql.Date(new java.util.Date().getTime());
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setDate(2, data);
			stmt.setString(3, tipo);

			stmt.execute();

			id = stmt.getLong(1);
			stmt.close();

			String numero = id.toString();

			id = id.parseLong(numero.substring(4, numero.length()));

			return id;

		} catch (Exception e) {
			e.printStackTrace();

		}

		return null;
	}

	/*
	 * public Long persistirPeticao(String descricao)throws DaoException{
	 * 
	 * Session session = retrieveSession();
	 * 
	 * Peticao peticao = new Peticao(); Long numero = recuperarNumeroPeticao();
	 * peticao.setNumero(numero);
	 * 
	 * SimpleDateFormat fmt = new SimpleDateFormat("yyyy"); short ano =
	 * Short.parseShort((fmt.format(new Date())));
	 * 
	 * System.out.println("ano: "+ano); System.out.println("numero: "+numero);
	 * System.out.println("descrição: "+descricao);
	 * 
	 * peticao.setAno(ano); peticao.setDescricao(descricao);
	 * peticao.setDataPeticao(new Date());
	 * 
	 * 
	 * 
	 * 
	 * 
	 * try { System.out.println("Gravando Peticao....."); session.save(peticao);
	 * session.flush(); System.out.println("Flush, Gravado.");
	 * 
	 * } catch(HibernateException e) { throw new
	 * DaoException("HibernateException",
	 * SessionFactoryUtils.convertHibernateAccessException(e)); } catch(
	 * RuntimeException e ) { throw new DaoException("RuntimeException", e); }
	 * 
	 * return numero;
	 * 
	 * 
	 * }
	 */

    
	public Peticao recuperarPeticaoProcesso( Long numeroPeticao, 
													 Short anoPeticao, 
													 String siglaProcessual, 
													 Long numeroProcessual, 
													 Short codRecurso,
													 Boolean flgJuntado
	) throws DaoException {			
		
		Peticao peticaoProcessoRecuperada = null;
		
		try {
			Session session = retrieveSession();
			
			Criteria criteria = session.createCriteria( Peticao.class );
			
			if( numeroPeticao != null )
				criteria.add(Restrictions.eq("peticao.numeroPeticao", numeroPeticao));
			
			if( anoPeticao != null )
				criteria.add(Restrictions.eq("peticao.anoPeticao", anoPeticao));
			
			if( siglaProcessual != null && siglaProcessual.trim().length() > 0 )
				criteria.add(Restrictions.eq("processo.siglaClasseProcessual", siglaProcessual));
			
			if( numeroProcessual != null )
				criteria.add(Restrictions.eq("processo.numeroProcessual", numeroProcessual));
			
			if( codRecurso != null )
				criteria.add(Restrictions.eq("codigoRecurso", codRecurso));
			
			
			if( flgJuntado != null ){
				if( flgJuntado )
					criteria.add(Restrictions.eq("juntadaProcesso", 'S'));
				else 
					criteria.add(Restrictions.eq("juntadaProcesso", 'N'));
			}
			
			peticaoProcessoRecuperada = (Peticao) criteria.uniqueResult();
			
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}
		
		return peticaoProcessoRecuperada;		
	}

	
	
	public List<ObjetoIncidente<?>> recuperarListaObjetoPeloObjetoIncidentePrincipal(
			Long idObjetoIncidente) throws DaoException {
		
		List<ObjetoIncidente<?>> listaObjetoIncidentes = new ArrayList<ObjetoIncidente<?>>();
		
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT pet.objetoIncidente FROM Peticao pet ");
				hql.append(" WHERE pet.objetoIncidenteVinculado.id = :idObjetoIncidente");

			Query q = session.createQuery(hql.toString());
			
			q.setLong("idObjetoIncidente", idObjetoIncidente);

			listaObjetoIncidentes = q.list();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		
		return listaObjetoIncidentes;
	}

	@Override
	public Peticao recuperarPeticao(Long idObjetoIndicente) throws DaoException {

		Session session = retrieveSession();

		try {
			Criteria criteria = session.createCriteria(Peticao.class);
			criteria.add(Restrictions.eq("idObjetoIncidente", idObjetoIndicente));
			return (Peticao) criteria.uniqueResult();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		}
	}

	@Override
	public List<Peticao> recuperarPeticoes(Long numero, Short ano) throws DaoException {
		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(Peticao.class).addOrder(Order.desc("anoPeticao"));
		
		try {
			if(numero != null){
				criteria.add(Restrictions.eq("numeroPeticao", numero));
			}
			
			if(ano != null){
				criteria.add(Restrictions.eq("anoPeticao", ano));
			}
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return criteria.list();
	}
	@Override
	public Boolean isPendenteDigitalizacao(Peticao peticao) throws DaoException {
		try {
			SQLQuery sqlQuery = retrieveSession().createSQLQuery("SELECT p.flg_pendente_digitalizacao" +
					                                             " FROM judiciario.peticao p WHERE " +
					                                             " p.num_peticao = " + peticao.getNumeroPeticao() +
					                                             " AND p.ano_peticao = " + peticao.getAnoPeticao());
			String simNao = (String) sqlQuery.uniqueResult();
			if (simNao == null) {
				return false;
			}
			return simNao.equals("S");
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	@Override
	public Boolean isRemessaIndevida(Peticao peticao) throws DaoException {
		try {
			SQLQuery sqlQuery = retrieveSession().createSQLQuery("SELECT p.flg_remessa_indevida" +
					                                             " FROM judiciario.peticao p WHERE " +
					                                             " p.num_peticao = " + peticao.getNumeroPeticao() +
					                                             " AND p.ano_peticao = " + peticao.getAnoPeticao());
			String simNao = (String) sqlQuery.uniqueResult();
			if (simNao == null) {
				return false;
			}
			return simNao.equals("S");
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}