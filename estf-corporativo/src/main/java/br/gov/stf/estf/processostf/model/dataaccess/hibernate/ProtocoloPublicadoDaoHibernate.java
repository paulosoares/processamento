package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.ProtocoloPublicado;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.processostf.model.dataaccess.ProtocoloPublicadoDao;
import br.gov.stf.estf.processostf.model.util.ObjetoIncidenteComConfidencialidadeQuery;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ProtocoloPublicadoDaoHibernate extends GenericHibernateDao<ProtocoloPublicado, Long>
	implements ProtocoloPublicadoDao {
	
	public ProtocoloPublicadoDaoHibernate () {
		super(ProtocoloPublicado.class);
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7250053082003057177L;

	public List<ProtocoloPublicado> pesquisar(
			ConteudoPublicacao conteudoPublicacao,
			Boolean recuperarOcultos) throws DaoException {

		List<ProtocoloPublicado> protocolos = null;
		try {
			Session session = retrieveSession();
			/*Criteria c = session.createCriteria(ProtocoloPublicado.class);
			c.add( Restrictions.eq("conteudoPublicacao", conteudoPublicacao) );
			protocolos = c.list();*/
			
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT pp FROM ProtocoloPublicado pp ");
			hql.append(" JOIN pp.protocolo pt ");
			hql.append(" WHERE pp.conteudoPublicacao.id = ? ");
			
			// Restrição para não recuperar processos ocultos
			hql.append(" AND " + ObjetoIncidenteComConfidencialidadeQuery.restringirProcessoOculto_HQL_JPQL("pt", recuperarOcultos));
			
			Query q = session.createQuery(hql.toString());
			q.setLong(0, conteudoPublicacao.getId());
			
			protocolos = q.list();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return protocolos;
	}

}
