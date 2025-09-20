package br.gov.stf.estf.usuario.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.usuario.TipoCustomizacao;
import br.gov.stf.estf.usuario.model.dataaccess.TipoCustomizacaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoCustomizacaoDaoHibernate  extends GenericHibernateDao<TipoCustomizacao, Long> implements TipoCustomizacaoDao{
	

	private static final long serialVersionUID = -576864687364257672L;

	public TipoCustomizacaoDaoHibernate() {
		super(TipoCustomizacao.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public TipoCustomizacao buscaPorDscParametro(String dscParametro) throws DaoException {
		TipoCustomizacao tipoCustomizacao = null;
	    
	    try {
			List<TipoCustomizacao> listaTipoCustomizacao = null;
	    	
	    	Session session = retrieveSession();
	    	
	    	StringBuilder hql = new StringBuilder(" SELECT tc FROM TipoCustomizacao tc ");
	    	hql.append(" WHERE (1=1) ");

	    	if (dscParametro != null && dscParametro.length() > 0){
	    		hql.append(" AND upper(tc.parametro) = '" + dscParametro.toUpperCase() + "'");
	    	}
	    	
			Query q = session.createQuery(hql.toString());
			listaTipoCustomizacao = (List<TipoCustomizacao>)q.list();
			if (listaTipoCustomizacao.size() > 0)
				tipoCustomizacao =  listaTipoCustomizacao.get(0);
	    	
		} catch (Exception e) {
			throw new DaoException(e);
		}
	    
		return tipoCustomizacao;
	}


}
