package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.IncidenteDistribuicao;
import br.gov.stf.estf.entidade.processostf.TipoConfidencialidade;
import br.gov.stf.estf.processostf.model.dataaccess.IncidenteDistribuicaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class IncidenteDistribuicaoDaoHibernate extends GenericHibernateDao<IncidenteDistribuicao, Long>
		implements IncidenteDistribuicaoDao {
	
	private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));

	public IncidenteDistribuicaoDaoHibernate() {
		super(IncidenteDistribuicao.class);
	}

	private static final long serialVersionUID = -6996835210272663629L;

	@SuppressWarnings("unchecked")
	public List<IncidenteDistribuicao> pesquisar(Integer numAta, Date dataAta) throws DaoException {
		
		List<IncidenteDistribuicao> resp = null;

		try {

			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT dis FROM IncidenteDistribuicao dis, Processo p ");
			hql.append(" WHERE dis.numeroAta = ? ");
			hql.append(" AND to_char(dis.dataAta, 'dd/mm/yyyy')  = ? ");
			hql.append(" AND dis.objetoIncidente.principal = p.id ");
			hql.append(" AND ( p.tipoConfidencialidade <> ? OR p.tipoConfidencialidade <> ? OR p.tipoConfidencialidade IS NULL ) ");
			
			
			
			Query q = session.createQuery( hql.toString() );
			q.setInteger(0, numAta);
			q.setString(1, format.format(dataAta));
			q.setString(2, TipoConfidencialidade.OCULTO.getCodigo());
			q.setString(3, TipoConfidencialidade.SIGILOSO.getCodigo());
			
			resp = q.list();			
			
		} catch ( Exception e ) {
			throw new DaoException(e);
		}

		return resp;
	}

	

}

