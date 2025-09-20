package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.ProtocoloPublicado;
import br.gov.stf.estf.entidade.processostf.TextoAssociadoProtocolo;
import br.gov.stf.estf.entidade.processostf.TextoAssociadoProtocolo.TextoAssociadoProtocoloId;
import br.gov.stf.estf.entidade.processostf.TextoAssociadoProtocolo.TipoAssociacao;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.processostf.model.dataaccess.TextoAssociadoProtocoloDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TextoAssociadoProtocoloDaoHibernate extends GenericHibernateDao<TextoAssociadoProtocolo, TextoAssociadoProtocoloId> 
	implements TextoAssociadoProtocoloDao {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3805086568044107730L;

	public TextoAssociadoProtocoloDaoHibernate () {
		super(TextoAssociadoProtocolo.class);
	}
	

	public List<TextoAssociadoProtocolo> pesquisar(
			ConteudoPublicacao conteudoPublicacao,
			TipoAssociacao... tipoAssociacao) throws DaoException {

		List<TextoAssociadoProtocolo> textos = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(TextoAssociadoProtocolo.class);
			c.add( Restrictions.eq("protocoloPublicado.conteudoPublicacao.id", conteudoPublicacao.getId()) );
			
			if ( tipoAssociacao!=null && tipoAssociacao.length>0 ) {
				String[] siglas = new String[tipoAssociacao.length];
				for ( int i=0 ; i<siglas.length ; i++ ) {
					siglas[i] = tipoAssociacao[i].getSigla();
				}
				c.add( Restrictions.in("id.tipoAssociacao", siglas) );
			}
			
			textos = c.list();
			
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		
		return textos;
	}

	public List<TextoAssociadoProtocolo> pesquisar(
			ProtocoloPublicado protocoloPublicado) throws DaoException {
		List<TextoAssociadoProtocolo> textos = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(TextoAssociadoProtocolo.class);
			c.add( Restrictions.eq("protocoloPublicado.id", protocoloPublicado.getId()) );
			
			textos = c.list();
			
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		
		return textos;
	}

	public TextoAssociadoProtocolo recuperar(
			ProtocoloPublicado protocoloPublicado, TipoAssociacao tipoAssociacao)
			throws DaoException {
		TextoAssociadoProtocolo texto = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(TextoAssociadoProtocolo.class);
			c.add( Restrictions.eq("protocoloPublicado.id", protocoloPublicado.getId()) );
			c.add( Restrictions.eq("id.tipoAssociacao", tipoAssociacao.getSigla()) );
			texto = (TextoAssociadoProtocolo) c.uniqueResult();
			
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		
		return texto;
	}

}
