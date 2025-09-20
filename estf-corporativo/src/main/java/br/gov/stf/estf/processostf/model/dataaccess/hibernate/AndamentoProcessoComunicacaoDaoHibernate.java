package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.AndamentoProcessoComunicacao;
import br.gov.stf.estf.processostf.model.dataaccess.AndamentoProcessoComunicacaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class AndamentoProcessoComunicacaoDaoHibernate extends
		GenericHibernateDao<AndamentoProcessoComunicacao, Long> implements AndamentoProcessoComunicacaoDao {

	private static final long serialVersionUID = 4619936846720570518L;

	public AndamentoProcessoComunicacaoDaoHibernate() {
		super(AndamentoProcessoComunicacao.class);
	}


	@Override
	public AndamentoProcessoComunicacao recuperarPorAndamento(Long idAndamento)  throws DaoException {
		
		AndamentoProcessoComunicacao andamento = null;
		
		try {
			Session session = retrieveSession();
			
			Criteria criteria = session.createCriteria(AndamentoProcessoComunicacao.class);
			
			criteria.add(Restrictions.eq("andamentoProcesso.id", idAndamento));
			
			andamento = (AndamentoProcessoComunicacao) criteria.uniqueResult();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch(RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
			
		return andamento;
		
	}
}
