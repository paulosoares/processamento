package br.gov.stf.estf.usuario.model.dataaccess.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.usuario.NotificacaoLogon;
import br.gov.stf.estf.usuario.model.dataaccess.NotificacaoLogonDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.DateTimeHelper;

@Repository
public class NotificacaoLogonDaoHibernate extends GenericHibernateDao<NotificacaoLogon, Long> implements NotificacaoLogonDao {

	public NotificacaoLogonDaoHibernate() {
		super(NotificacaoLogon.class);
	}

	private static final long serialVersionUID = 1L;

	public List<NotificacaoLogon> pesquisaNotificacao(String grupoTopico,
			Date dataInicio, Date dataFim, Date dataNotificacaoFim, String usuarioInlusao, Integer prioridade) throws DaoException {
		
		List<NotificacaoLogon> listaNotificacao = null;

		Session sessao = retrieveSession();
		try{
			StringBuffer hql = new StringBuffer();

			hql.append(" SELECT nl FROM NotificacaoLogon nl WHERE (1=1)");
			
			
			if(dataInicio != null){ 
				hql.append(" AND nl.dataInicioNotificacao >= to_date(:dataInicialInicio, 'DD/MM/YYYY HH24:MI:SS') ");        
				hql.append(" AND nl.dataInicioNotificacao <= to_date(:dataFinalInicio, 'DD/MM/YYYY HH24:MI:SS') ");
			}	
			
			if(dataFim != null){ 
				hql.append(" AND nl.dataFimNotificacao >= to_date(:dataInicioFim, 'DD/MM/YYYY HH24:MI:SS') ");        
				hql.append(" AND nl.dataFimNotificacao <= to_date(:dataFinalFim, 'DD/MM/YYYY HH24:MI:SS') ");
			}	
			
			if( dataNotificacaoFim != null) 
				hql.append(" AND nl.dataFimNotificacao >= to_date(:dataNotificacaoFim, 'DD/MM/YYYY HH24:MI:SS') ");        

			if (grupoTopico != null && grupoTopico.trim().length() > 0)
				hql.append(" AND nl.grupoTopico = :grupoTopico "); 
			
			if( usuarioInlusao != null && usuarioInlusao.trim().length() > 0)
				hql.append(" AND nl.usuarioInclusao = :usuarioInlusao");

			if( prioridade != null ) 
				hql.append(" AND nl.prioridade = :prioridade");
			
			
			hql.append(" ORDER BY nl.grupoTopico, nl.dataFimNotificacao DESC ");

			Query query = sessao.createQuery( hql.toString() );

			if(dataInicio != null){ 
				query.setString("dataInicialInicio", DateTimeHelper.getDataString(dataInicio)+ "00:00:00");
				query.setString("dataFinalInicio", DateTimeHelper.getDataString(dataInicio)+ "23:59:59");
			}	
			
			if(dataFim != null){ 
				query.setString("dataInicioFim", DateTimeHelper.getDataString(dataFim)+ "00:00:00");
				query.setString("dataFinalFim", DateTimeHelper.getDataString(dataFim)+ "23:59:59");
			}
			
			if( dataNotificacaoFim != null)
				query.setString("dataNotificacaoFim", DateTimeHelper.getDataString(dataNotificacaoFim)+ "00:00:00");

			if (grupoTopico != null && grupoTopico.trim().length() > 0)
				query.setString("grupoTopico", grupoTopico.toUpperCase()); 
			
			if( usuarioInlusao != null && usuarioInlusao.trim().length() > 0)
				query.setString("usuarioInlusao", usuarioInlusao.toUpperCase());

			if( prioridade != null )
				query.setInteger("prioridade", prioridade);

			listaNotificacao = query.list();         
		}
		catch( HibernateException ex ){
			throw new DaoException( "HibernateException" , ex );
		}
		catch(RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}       

		return listaNotificacao;        
	}	

}
