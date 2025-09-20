package br.gov.stf.estf.publicacao.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.publicacao.FaseTextoProcesso;
import br.gov.stf.estf.publicacao.model.dataaccess.FaseTextoProcessoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * Implementação de <code>FaseTextoProcessoDao</code>
 * 
 * @author Rodrigo.Barreiros
 * @since 25.05.2009
 */
@Repository
public class FaseTextoProcessoDaoHibernate extends GenericHibernateDao<FaseTextoProcesso, Long> implements FaseTextoProcessoDao {

	private static final long serialVersionUID = 4932886597847044205L;

	public FaseTextoProcessoDaoHibernate() {
		super(FaseTextoProcesso.class);
	}

	/**
	 * @see br.gov.stf.estf.publicacao.model.service.FaseTextoProcessoService#recuperarUltimaFaseDoTexto(br.gov.stf.estf.entidade.documento.Texto)
	 */
	@SuppressWarnings("unchecked")
	public FaseTextoProcesso recuperarUltimaFaseDoTexto(Texto texto) throws DaoException {
		try {
			Criteria criteria = retrieveSession().createCriteria(getPersistentClass(), "ftp");
			criteria = criteria.add(Restrictions.eq("ftp.texto.id", texto.getId()));
			criteria = criteria.addOrder(Order.desc("ftp.dataTransicao"));
			criteria.setMaxResults(1);
			return (FaseTextoProcesso) criteria.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	public List<FaseTextoProcesso> pesquisarFasesDoTexto(Texto texto) throws DaoException {
		Criteria criteria = retrieveSession().createCriteria(FaseTextoProcesso.class, "ftp");
		criteria.add(Restrictions.eq("ftp.texto.id", texto.getId()));
		return criteria.list();
	}

}
