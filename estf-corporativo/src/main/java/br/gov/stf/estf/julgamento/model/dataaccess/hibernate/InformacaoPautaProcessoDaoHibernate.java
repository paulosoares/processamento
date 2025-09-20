/**
 * 
 */
package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.SessionImplementor;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.InformacaoPautaProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.SubtemaPauta;
import br.gov.stf.estf.julgamento.model.dataaccess.InformacaoPautaProcessoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 29.06.2011
 */
@Repository
public class InformacaoPautaProcessoDaoHibernate extends
		GenericHibernateDao<InformacaoPautaProcesso, Long> implements InformacaoPautaProcessoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7316838690339766942L;

	public InformacaoPautaProcessoDaoHibernate() {
		super(InformacaoPautaProcesso.class);
	}

	@Override
	public InformacaoPautaProcesso recuperar(ObjetoIncidente<?> objetoIncidente)
			throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(InformacaoPautaProcesso.class);
			c.setFetchMode("objetoIncidente", FetchMode.JOIN);
			c.add(Restrictions.eq("objetoIncidente.id", objetoIncidente.getId()));
			return (InformacaoPautaProcesso) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<ObjetoIncidente<?>> recuperarProcessosJulgamentoConjunto(
			Long seqListaJulgamentoConjunto) throws DaoException {
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer(" SELECT oi FROM ObjetoIncidente oi, InformacaoPautaProcesso ipp ");
			hql.append(" WHERE ipp.objetoIncidente.id = oi.id ");
			hql.append(" AND ipp.seqListaJulgamentoConjunto = :listaJulgamentoConjunto ");
			Query q = session.createQuery(hql.toString());
			if (seqListaJulgamentoConjunto != null && seqListaJulgamentoConjunto.intValue() > 0) {
				q.setLong("listaJulgamentoConjunto", seqListaJulgamentoConjunto);
				return q.list();
			}
			return new ArrayList<ObjetoIncidente<?>>();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	@Override
	public Long recuperarQtdProcessosSubTema(SubtemaPauta subTemaPauta)
			throws DaoException {
		try {
			SessionImplementor session = (SessionImplementor) retrieveSession();
			StringBuffer sql = new StringBuffer(" SELECT COUNT(*) AS QTD FROM JULGAMENTO.INFORMACAO_PAUTA_PROCESSO WHERE SEQ_SUBTEMA_PAUTA = " + subTemaPauta.getId());
			Statement stm = session.connection().createStatement();
			ResultSet rs = stm.executeQuery(sql.toString());
			Long nextVal = 0L;
			if (rs.next()) {
				nextVal = rs.getLong("QTD");
			}
			stm.close();
			rs.close();
			return nextVal;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	@Override
	public Long recuperarProximaSequenceListaJulgamentoConjunto()
			throws DaoException {
		try {
			SessionImplementor session = (SessionImplementor) retrieveSession();
			StringBuffer sql = new StringBuffer(" SELECT JULGAMENTO.SEQ_LISTA_JULGAMENTO_CONJUNTO.NEXTVAL FROM DUAL ");
			Statement stm = session.connection().createStatement();
			ResultSet rs = stm.executeQuery(sql.toString());
			Long nextVal = 0L;
			if (rs.next()) {
				nextVal = rs.getLong("NEXTVAL");
			}
			stm.close();
			rs.close();
			return nextVal;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	@Override
	public void refresh(InformacaoPautaProcesso informacaoPautaProcesso) throws DaoException {
		retrieveSession().refresh( informacaoPautaProcesso );
	}
}
