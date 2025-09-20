package br.gov.stf.estf.usuario.model.dataaccess.hibernate;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.usuario.IntegracaoDocumento;
import br.gov.stf.estf.usuario.model.dataaccess.IntegracaoDocumentoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class IntegracaoDocumentoDaoHibernate extends GenericHibernateDao<IntegracaoDocumento, Long> implements IntegracaoDocumentoDao  {

	private static final long serialVersionUID = -3173926043520257734L;

	public IntegracaoDocumentoDaoHibernate(){
		super(IntegracaoDocumento.class);
	}

    @Override
	public Date findMaxDatInclusao(Long seqUsuarioExterno) throws DaoException {
		
		try {
				Session session = retrieveSession();
				
				StringBuffer hql = new StringBuffer();
				hql.append(" select MAX (oEntidade.dataInclusao) from IntegracaoDocumento oEntidade ");
				hql.append(" where oEntidade.chave.seqUsuario = :id");
				
				Query q = session.createQuery(hql.toString());
				q.setLong("id", seqUsuarioExterno);
				
				return (Date) q.uniqueResult();
				
			}catch(HibernateException e) {
				throw new DaoException("HibernateException",
						SessionFactoryUtils.convertHibernateAccessException(e));
			}
			catch(RuntimeException e) {
				throw new DaoException("RuntimeException", e);
			}
	}

}
