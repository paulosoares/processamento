package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.DeslocaPeticao;
import br.gov.stf.estf.entidade.processostf.DeslocaPeticao.DeslocaPeticaoId;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processostf.model.dataaccess.DeslocamentoPeticaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class DeslocamentoPeticaoDaoHibernate extends GenericHibernateDao<DeslocaPeticao, DeslocaPeticaoId> implements DeslocamentoPeticaoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2014629311436378003L;

	public DeslocamentoPeticaoDaoHibernate() {
		super(DeslocaPeticao.class);
	}

	/*
	 * public DeslocaProcesso recuperarUltimoDeslocaProcesso(String siglaClasse,
	 * Long numeroProcesso) throws DaoException { Session session =
	 * retrieveSession(); DeslocaProcesso DeslocaProcesso = new
	 * DeslocaProcesso();
	 * 
	 * 
	 * StringBuffer hql = new StringBuffer(" FROM DeslocaProcesso "+ " where
	 * sig_classe_proces = '"+ siglaClasse + "' and NUM_processo = " +
	 * numeroProcesso + " order by dat_recebimento desc ");
	 * 
	 * 
	 * Query query = session.createQuery(hql.toString()); List list =
	 * query.list();
	 * 
	 * Iterator iterator = list.iterator();
	 * 
	 * 
	 * DeslocaProcesso = (DeslocaProcesso) iterator.next();
	 * 
	 * 
	 * 
	 * return DeslocaProcesso; }
	 */

	public void persistirDeslocamentoPeticao(DeslocaPeticao deslocamentoPeticao) throws DaoException {

		Session session = retrieveSession();

		try {
			// System.out.println("Gravando.Deslocamento Petição.....");
			session.save(deslocamentoPeticao);
			session.flush();
			// System.out.println("Flush, Gravado.");

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

	}

	// ------------------------------

	public Long getNumeroGuia(long cod_orgao_origem, long cod_orgao_destino, short ano_guia) throws DaoException {

		Session session = retrieveSession();
		DeslocaProcesso DeslocaProcesso = new DeslocaProcesso();

		StringBuffer hql = new StringBuffer(" FROM DeslocaProcesso " + " where cod_orgao_origem = '" + cod_orgao_origem + "' and cod_orgao_destino = '" + cod_orgao_destino
				+ "' and ano_guia = '" + ano_guia + "' order by num_guia desc ");

		Query query = session.createQuery(hql.toString());

		Iterator iterator = query.iterate();
		long novoNumGuia = 0;
		if (iterator.hasNext()) {

			DeslocaProcesso = (DeslocaProcesso) iterator.next();

			novoNumGuia = DeslocaProcesso.getId().getNumeroGuia();
			return novoNumGuia++;
		}

		return novoNumGuia;
	}

	// recupera o(s) deslocamento(s) a partir de uma guia
	public List<DeslocaPeticao> recuperarDeslocamentoPeticaos(Guia guia) throws DaoException {
		Session session = retrieveSession();

		try {

			Criteria c = session.createCriteria(DeslocaPeticao.class);
			c.add(Restrictions.eq("id.anoGuia", guia.getId().getAnoGuia()));
			c.add(Restrictions.eq("id.numeroGuia", guia.getId().getNumeroGuia()));
			c.add(Restrictions.eq("id.codigoOrgaoOrigem", guia.getId().getCodigoOrgaoOrigem()));

			List<DeslocaPeticao> deslocaPeticaos = c.list();

			return deslocaPeticaos;
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}
	
	@Override
	public List<DeslocaPeticao> recuperarDeslocamentoPeticaoRecebimentoExterno(Guia guia) throws DaoException {
		Session session = retrieveSession();

		try {

			Criteria c = session.createCriteria(DeslocaPeticao.class);
			c.add(Restrictions.eq("id.anoGuia", guia.getId().getAnoGuia()));
			c.add(Restrictions.eq("id.numeroGuia", guia.getId().getNumeroGuia()));
			c.add(Restrictions.eq("id.codigoOrgaoOrigem", guia.getId().getCodigoOrgaoOrigem()));
			c.add(Restrictions.isNull("dataRecebimento"));

			List<DeslocaPeticao> deslocaPeticaos = c.list();

			return deslocaPeticaos;
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}
	
	@Override
	public List<DeslocaPeticao> recuperarDeslocamentoPeticaoRecebimentoExterno(Peticao peticao) throws DaoException {
		Session session = retrieveSession();

		try {

			Criteria c = session.createCriteria(DeslocaPeticao.class);
			c.add(Restrictions.eq("numeroPeticao", peticao.getNumeroPeticao()));
			c.add(Restrictions.eq("anoPeticao", peticao.getAnoPeticao().longValue()));
			c.add(Restrictions.eq("ultimoDeslocamento", true));

			List<DeslocaPeticao> deslocaPeticaos = c.list();

			return deslocaPeticaos;
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}
	

	@Override
	public void removerPeticao(DeslocaPeticao deslocaPeticao) throws DaoException {
		Session session = retrieveSession();

		try {
			session.delete(deslocaPeticao);
			session.flush();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		}
	}

	@Override
	public List<DeslocaPeticao> pesquisarDataRecebimentoGuiaPeticao(Guia guia) throws DaoException {
		Session session = retrieveSession();
		try {

			Criteria criteria = session.createCriteria(DeslocaPeticao.class);
			criteria.add(Restrictions.eq("id.anoGuia", guia.getId().getAnoGuia()));
			criteria.add(Restrictions.eq("id.numeroGuia", guia.getId().getNumeroGuia()));
			criteria.add(Restrictions.eq("id.codigoOrgaoOrigem", guia.getId().getCodigoOrgaoOrigem()));
			criteria.add(Restrictions.isNotNull("dataRecebimento"));
			return criteria.list();
		} catch (Exception e) {
			throw new DaoException("HibernateException", e);
		}
	}
	
	@Override
	public Long pesquisarSetorUltimoDeslocamento(Long seqObjetoIncidente) throws DaoException {

		Session session = retrieveSession();

		String sql = "select COD_ORGAO_DESTINO from STF.DESLOCA_PETICAOS " + 
					 "where SEQ_OBJETO_INCIDENTE = :seq_objeto_incidente and FLG_ULTIMO_DESLOCAMENTO='S'";

		SQLQuery query = session.createSQLQuery(sql);

		query.setParameter("seq_objeto_incidente", seqObjetoIncidente);

		Number setor = (Number)query.uniqueResult();
		
		return setor == null ? null : setor.longValue();
	}
	
	@Override
	public DeslocaPeticao recuperarUltimoDeslocamentoPeticao(Peticao peticao) throws DaoException {

		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(DeslocaPeticao.class);
		criteria.add(Restrictions.eq("numeroPeticao", peticao.getNumeroPeticao()));
		criteria.add(Restrictions.eq("anoPeticao", peticao.getAnoPeticao().longValue()));
		criteria.addOrder(Order.desc("dataRecebimento"));
		if (criteria.list().size() == 0) {
			return null;
		} else {
			return (DeslocaPeticao) criteria.list().get(0);
		}
	}

	public Boolean deslocarProcesso(Processo processo) throws DaoException {
		return null;
	}
	
	@Override
	public Integer recuperarUltimaSequencia(Guia guia) throws DaoException {
		try {
			String sql = "SELECT MAX(D.NUM_SEQUENCIA) ULTIMA_SEQUENCIA FROM STF.DESLOCA_PETICAOS D" +
					     " WHERE D.NUM_GUIA = " + guia.getId().getNumeroGuia() +
					     " AND D.ANO_GUIA = " + guia.getId().getAnoGuia() +
					     " AND D.COD_ORGAO_ORIGEM = " + guia.getId().getCodigoOrgaoOrigem();

			SQLQuery q = retrieveSession().createSQLQuery(sql.toString());
			Integer sequencia = NumberUtils.createInteger( q.uniqueResult().toString() );
			return sequencia;
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}



}
