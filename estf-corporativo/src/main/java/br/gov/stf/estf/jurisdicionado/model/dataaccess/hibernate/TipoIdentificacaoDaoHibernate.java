package br.gov.stf.estf.jurisdicionado.model.dataaccess.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisdicionado.TipoIdentificacao;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.TipoIdentificacaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoIdentificacaoDaoHibernate extends GenericHibernateDao<TipoIdentificacao, Long>
	implements TipoIdentificacaoDao {
	
	private static final long serialVersionUID = 5134499306418006125L;

	public TipoIdentificacaoDaoHibernate() {
		super(TipoIdentificacao.class);
	}

	@Override
	public TipoIdentificacao pesquisaPelaSigla(String sigla) throws DaoException {
		TipoIdentificacao identificacao = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(TipoIdentificacao.class);

			c.add(Restrictions
					.eq("siglaTipoIdentificacao", sigla));

			identificacao = (TipoIdentificacao) c.uniqueResult();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return identificacao;
	}

}
