package br.gov.stf.estf.publicacao.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.publicacao.EstruturaPublicacao;
import br.gov.stf.estf.entidade.publicacao.EstruturaPublicacao.EstruturaPublicacaoId;
import br.gov.stf.estf.publicacao.model.dataaccess.EstruturaPublicacaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.model.entity.Flag;

@Repository
public class EstruturaPublicacaoDaoHibernate
	extends GenericHibernateDao<EstruturaPublicacao,EstruturaPublicacaoId> 
	implements EstruturaPublicacaoDao {

		private static final long serialVersionUID = 9103400958532997314L;
		
		

		public EstruturaPublicacaoDaoHibernate() {
			super(EstruturaPublicacao.class);
		}

		public String recuperarDescricao(Integer codigoCapitulo, Integer codigoMateria)
				throws DaoException {
			String resp = null;
			
			if( codigoCapitulo == null  ) {
				throw new IllegalArgumentException();
			}
			
			if ( codigoMateria==null ) {
				codigoMateria = 0;
			}
			
			try {
				Session session = retrieveSession();

				Criteria criteria = session.createCriteria(EstruturaPublicacao.class);
				
				criteria.setProjection( Projections.property("descricao") );
				criteria.add( Restrictions.eq("id.codigoCapitulo", codigoCapitulo) );				
				criteria.add( Restrictions.eq("id.codigoMateria", codigoMateria));
				criteria.add( Restrictions.eq("id.codigoConteudo", 0));
				
				resp = (String) criteria.uniqueResult();				
				
			} catch ( Exception e ) {
				throw new DaoException(e);
			}
			
			return resp;
		}

		//TODO: verificar regra para pesquisas de estrutura ( verificado, esta de acordo com a implementacao antiga)
		//TODO: alterar o nome deste metodo para pesquisarCapitulosEstruturas
		@SuppressWarnings("unchecked")
		public List<EstruturaPublicacao> pesquisarEstruturasPorCodigoCapitulo(byte codigoCapitulo)
				throws DaoException {
			List<EstruturaPublicacao> estruturasPublicacao = null;
			
			try {
				Session session = retrieveSession();

				Criteria criteria = session.createCriteria(EstruturaPublicacao.class);
				criteria.add(Restrictions.eq("ativo", true));
				criteria.addOrder(Order.asc("ordemImpressao"));
				
				estruturasPublicacao = criteria.list();
				
			} catch ( Exception e ) {
				throw new DaoException(e);
			}
			
			return estruturasPublicacao;
		}
		
		@SuppressWarnings("unchecked")
		public List<EstruturaPublicacao> recuperarTodos() throws DaoException{
			
			String hql = new String("SELECT estrutura FROM EstruturaPublicacao estrutura WHERE estrutura.ativo = ?  ORDER BY estrutura.ordemImpressao ");
			 List<EstruturaPublicacao> lista = null;
			
			try {
				Session session = retrieveSession();
				Query q = session.createQuery(hql);
				q.setString(0, Flag.SIM);
				lista =  q.list();
			} catch ( Exception e ) {
				throw new DaoException(e);
			}
			return lista;
			
		}

		public EstruturaPublicacao recuperar(Integer codCapitulo,
				Integer codMateria, Integer codConteudo) throws DaoException {
			EstruturaPublicacao estruturaPublicacao = null;
			try {
				Session session = retrieveSession();
				Criteria c = session.createCriteria(EstruturaPublicacao.class);
				c.add( Restrictions.eq("id.codigoCapitulo", codCapitulo) );
				c.add( Restrictions.eq("id.codigoMateria", codMateria) );
				c.add( Restrictions.eq("id.codigoConteudo", codConteudo) );
				estruturaPublicacao = (EstruturaPublicacao) c.uniqueResult();
			} catch ( Exception e ) {
				throw new DaoException(e);
			}
			return estruturaPublicacao;
		}

		public List<EstruturaPublicacao> pesquisar(Integer codigoCapitulo,
				Integer codigoMateria, Integer codigoConteudo)
				throws DaoException {
			List<EstruturaPublicacao> capitulos = null;
			try {
				Session session = retrieveSession();
				Criteria c = session.createCriteria(EstruturaPublicacao.class);
				if ( codigoCapitulo!=null ) {
					c.add( Restrictions.eq("id.codigoCapitulo", codigoCapitulo) );
				}
				if ( codigoMateria!=null ) {
					c.add( Restrictions.eq("id.codigoMateria", codigoMateria) );
				}
				if ( codigoConteudo!=null ) {
					c.add( Restrictions.eq("id.codigoConteudo", codigoConteudo) );
				}
				capitulos = c.list();
			} catch ( Exception e ) {
				throw new DaoException(e);
			}
			return capitulos;
		}

}
