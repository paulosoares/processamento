package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.HistoricoPublicacaoDocumentoDao;
import br.gov.stf.estf.entidade.documento.HistoricoPublicacaoDocumento;
import br.gov.stf.estf.entidade.publicacao.Publicacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@SuppressWarnings("unchecked")
@Repository
public class HistoricoPublicacaoDocumentoDaoHibernate extends GenericHibernateDao<HistoricoPublicacaoDocumento, Long> 
	implements HistoricoPublicacaoDocumentoDao {
	
	public HistoricoPublicacaoDocumentoDaoHibernate () {
		super(HistoricoPublicacaoDocumento.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1518181686417996481L;

	
	public List<HistoricoPublicacaoDocumento> pesquisar(Publicacao publicacao)
			throws DaoException {
		List<HistoricoPublicacaoDocumento> historicos = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(HistoricoPublicacaoDocumento.class);
			c.add( Restrictions.eq("publicacao", publicacao) );
			c.addOrder( Order.desc("dataInclusao") );
			historicos = c.list();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return historicos;
	}

}
