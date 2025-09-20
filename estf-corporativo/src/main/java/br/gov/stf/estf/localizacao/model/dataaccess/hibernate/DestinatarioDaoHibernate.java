package br.gov.stf.estf.localizacao.model.dataaccess.hibernate;

import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.Destinatario;
import br.gov.stf.estf.localizacao.model.dataaccess.DestinatarioDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class DestinatarioDaoHibernate extends GenericHibernateDao<Destinatario, Long> implements DestinatarioDao { 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DestinatarioDaoHibernate() {
		super(Destinatario.class);
	}

	@Override
	// TODO: implementar o codigo para o método que tem a finalidade recuperar todos os destinatários de uma origem
	public List<Destinatario> recuperarDestinatarioDaOrigem(Long codOrigem, String id) throws DaoException {
		List<Destinatario> destinatarios = Collections.emptyList();

		try {
			Session session = retrieveSession();
			
			StringBuffer hql = new StringBuffer();
			hql.append("SELECT d FROM Destinatario d ");
			hql.append("WHERE (1=1)  ");
			if(codOrigem != null){
				hql.append(" and (d.origem.id = :codOrigem)  ");				
			}
			if (id != null){
				hql.append(" and (d.id like '%" + id + "%' or d.nomDestinatario like '%" + id.toUpperCase() + "%')");
			} 

			Query q = session.createQuery(hql.toString());
			if(codOrigem != null){
			q.setParameter("codOrigem", codOrigem);
			}
			
			destinatarios = q.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
		return destinatarios;
	}

	@Override
	public List<Destinatario> pesquisarDestinatarioDescricao(String descricao)
			throws DaoException {
		Session session = retrieveSession();

		Criteria criteria = session.createCriteria(Destinatario.class);
		criteria.add(Restrictions.eq("ativo", true));
		criteria.add(Restrictions.ilike("nomDestinatario", "%" + descricao + "%"));
		criteria.addOrder(Order.asc("nomDestinatario"));

		return criteria.list();
	}

	@Override
	public List pesquisarDestinatario(Long codOrigem, Long codDestinatario) throws DaoException {

		List listaDestinatarios = null;
		StringBuffer sql = new StringBuffer();
		Session session = retrieveSession();

		try {
			
			sql.append(" SELECT DISTINCT d.DSC_DESTINATARIO AS NOME_DESTINATARIO, d.SEQ_DESTINATARIO_ORIGEM, ");
			sql.append(" (ed.DSC_LOGRADOURO || ' ' || ed.DSC_COMPLEMENTO || ' ' || ed.NUM_LOCALIZACAO || ' - ' || ed.NOM_BAIRRO ");
			sql.append(" || ' - ' || ed.nom_municipio || ' - ' || ed.SIG_UF) AS ENDERECO ");
			sql.append(" , '' AS NOME_ORGAO, '' AS NOME_PROCEDENCIA, oe.DSC_ORIGEM, ed.FLG_ATIVO ");
			sql.append(" FROM JUDICIARIO.DESTINATARIO_ORIGEM d, JUDICIARIO.ENDERECO_DESTINATARIO ed ");
			sql.append(" , JUDICIARIO.ORIGEM oe ");
			sql.append(" WHERE (1=1) ");
			sql.append(" AND d.SEQ_DESTINATARIO_ORIGEM = ed.SEQ_DESTINATARIO_ORIGEM (+) ");
			sql.append(" AND d.COD_ORIGEM = oe.COD_ORIGEM ");
			
			if (codDestinatario != null){
				sql.append(" AND d.SEQ_DESTINATARIO_ORIGEM = :codDestinatario ");
			}
			
			if (codOrigem != null){
				sql.append(" AND d.COD_ORIGEM = :codOrigem ");
			}
			
			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());

			if (codDestinatario != null){
				sqlQuery.setLong("codDestinatario", codDestinatario);
			}
			
			if (codOrigem != null){
				sqlQuery.setLong("codOrigem", codOrigem);
			}

			listaDestinatarios = sqlQuery.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return listaDestinatarios;
	}

}
