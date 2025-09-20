package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.PermissaoDeslocamentoDao;
import br.gov.stf.estf.entidade.documento.PermissaoDeslocamento;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class PermissaoDeslocamentoDaoHibernate extends GenericHibernateDao<PermissaoDeslocamento, Long> implements PermissaoDeslocamentoDao {

	private static final long serialVersionUID = -4958664420772533712L;
	Log log = LogFactory.getLog(PermissaoDeslocamentoDaoHibernate.class);

	public PermissaoDeslocamentoDaoHibernate() {
		super(PermissaoDeslocamento.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PermissaoDeslocamento> pesquisar(Long setorOrigem, Long setorDestino, Boolean permissao) throws DaoException {
		List<PermissaoDeslocamento> textos = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(PermissaoDeslocamento.class);

			if(setorOrigem != null){
				c.add(Restrictions.eq("setorOrigem.id", setorOrigem));
			}
			if(setorDestino != null){
				c.add(Restrictions.eq("setorDestino.id", setorDestino));
			}
			if(permissao != null){
				c.add(Restrictions.eq("permissao", permissao));
			}
			c.addOrder(Order.asc("setorOrigem"));
			textos = c.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return textos;
	}

}
