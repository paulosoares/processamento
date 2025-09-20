package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.ComunicacaoIncidenteDao;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente.FlagProcessoLote;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ComunicacaoIncidenteDaoHibernate extends GenericHibernateDao<ComunicacaoIncidente, Long> implements ComunicacaoIncidenteDao {

	private static final long serialVersionUID = -1349257265783428307L;

	public ComunicacaoIncidenteDaoHibernate() {
		super(ComunicacaoIncidente.class);
	}

	public ObjetoIncidente<?> selecionaObjetoIncidente(Long idDocumento) throws DaoException {
		Session session = retrieveSession();

		try {
			StringBuilder hql = new StringBuilder("SELECT ci.objetoIncidente FROM ComunicacaoIncidente ci");
			hql.append(" WHERE (1=1) ");

			if (idDocumento != null && idDocumento != 0L) {
				hql.append(" AND ci.comunicacao.id = " + idDocumento);
			}
			hql.append(" AND ci.tipoVinculo = 'P' ");

			Query q = session.createQuery(hql.toString());
			return (ObjetoIncidente<?>) q.uniqueResult();

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	public List<ComunicacaoIncidente> verificaSeExisteProcessosVinculados(Comunicacao comunicacao) throws DaoException {
		List<ComunicacaoIncidente> listaProcessosVinculados = Collections.emptyList();

		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ComunicacaoIncidente.class);

			c.add(Restrictions.eq("comunicacao.id", comunicacao.getId()));
			c.add(Restrictions.eq("tipoVinculo", FlagProcessoLote.V));

			listaProcessosVinculados = c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}

		return listaProcessosVinculados;
	}
	
	
	@Override
	public ComunicacaoIncidente recuperarPorAndamento(Long idAndamento)  throws DaoException {
		
		ComunicacaoIncidente comunicacaoIncidente = null;
		
		try {
			Session session = retrieveSession();
			
			Criteria criteria = session.createCriteria(ComunicacaoIncidente.class);
			
			criteria.add(Restrictions.eq("andamentoProcesso.id", idAndamento));
			
			comunicacaoIncidente = (ComunicacaoIncidente) criteria.uniqueResult();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch(RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
			
		return comunicacaoIncidente;
		
	}
	
}
