package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.TipoDocumentoTextoDao;
import br.gov.stf.estf.entidade.documento.TipoDocumentoTexto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoDocumentoTextoDaoHibernate extends GenericHibernateDao<TipoDocumentoTexto, Long> 
	implements TipoDocumentoTextoDao {

	public TipoDocumentoTextoDaoHibernate() {
		super(TipoDocumentoTexto.class);
	}

	private static final long serialVersionUID = 8649523466346122224L;

	@SuppressWarnings("unchecked")
	public List<TipoDocumentoTexto> pesquisarTiposDocumentoTextoPorSetor(Long codSetor) 
		throws DaoException {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(TipoDocumentoTexto.class);
			criteria = criteria.add(Restrictions.or(
		        Restrictions.eq("setor.id", codSetor),
		        Restrictions.isNull("setor")
		    ));
			criteria = criteria.add(Restrictions.eq("codigoEspacoClassificacaoDocumento", 2L));
			
			return getHibernateTemplate().findByCriteria(criteria);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	public TipoDocumentoTexto recuperar(Long codigoTipoTexto)
		throws DaoException {
		TipoDocumentoTexto tipoDocumentoTexto = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(TipoDocumentoTexto.class);
			c.add( Restrictions.eq("id", codigoTipoTexto) );
			tipoDocumentoTexto = (TipoDocumentoTexto) c.uniqueResult();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return tipoDocumentoTexto;
	}
	
}
