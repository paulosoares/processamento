package br.gov.stf.estf.intimacao.model.dataaccess.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.TextoAndamentoProcesso;
import br.gov.stf.estf.intimacao.model.dataaccess.PecaProcessoEletronicoLocalDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class PecaProcessoEletronicoLocalDaoHibernate extends
				GenericHibernateDao<PecaProcessoEletronico, Long>  implements PecaProcessoEletronicoLocalDao{

	public static final long serialVersionUID = 1L;
	
	public PecaProcessoEletronicoLocalDaoHibernate() {
		super(PecaProcessoEletronico.class);
	}

	@SuppressWarnings("unchecked")
	public List<PecaProcessoEletronico> pesquisarPecasPorProcessoIncidente(Long idProcessoIncidente,
			Boolean incluirCancelados) throws DaoException {
		
		List<PecaProcessoEletronico> pecaProcessoEletronicos = new ArrayList<PecaProcessoEletronico>();
		Session session = retrieveSession();

		try {
			StringBuffer hql = new StringBuffer();	

            hql.append("SELECT ppe ");
            hql.append("FROM ObjetoIncidente oi, ");
            hql.append("PecaProcessoEletronico ppe ");
            hql.append("WHERE oi.id = :processoId ");

            hql.append("AND ( ");
            hql.append("(oi.principal.id = oi.id AND ppe.objetoIncidenteProcesso.id = oi.id) ");
            hql.append("OR ( ");
            hql.append("(oi.principal.id <> oi.id AND ppe.objetoIncidente.id = oi.id) ");
            hql.append(") ");
            hql.append(") ");
			
			if(incluirCancelados != null && !incluirCancelados){
				hql.append(" AND ppe.tipoSituacaoPeca != 1 ");
			}
			
			hql.append( "ORDER BY ppe.numeroOrdemPeca ");
			
			Query query = session.createQuery(hql.toString());

			query.setLong("processoId", idProcessoIncidente);

			pecaProcessoEletronicos = query.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return pecaProcessoEletronicos;
	}

	@Override
	public List<PecaProcessoEletronico> pesquisarPecasAndamento8507(Long idProcessoIncidente) throws DaoException {
		List<PecaProcessoEletronico> pecaProcessoEletronicos = new ArrayList<PecaProcessoEletronico>();
		Session session = retrieveSession();

		try {
			StringBuffer hql = new StringBuffer();	

            hql.append("SELECT ppe 												");
            hql.append("FROM PecaProcessoEletronico ppe, ObjetoIncidente oi  	");
            hql.append("WHERE ppe.objetoIncidenteProcesso.id = oi.id 			");
            //hql.append(" AND oi.principal.id = oi.id							");
//            hql.append("WHERE ( 											 	");
//            hql.append("(oi.principal.id = oi.id AND ppe.objetoIncidenteProcesso.id = oi.id) 	");
//            hql.append("OR																		");
            //hql.append("(oi.principal.id <> oi.id AND ppe.objetoIncidente.id = oi.id) 			");
//            hql.append(") ");
            hql.append(" AND oi.id = :processoId ");
			hql.append(" AND ppe.tipoSituacaoPeca != 1 ");
			hql.append(" AND ppe.tipoPecaProcesso.id in (1060,1065,1221,1326,1510) ");
/*
            hql.append(" UNION ");
			
            hql.append("SELECT ppe 												");
            hql.append("FROM PecaProcessoEletronico ppe, ObjetoIncidente oi  	");
            hql.append("WHERE ppe.objetoIncidente.id = oi.id				 	");
            hql.append(" AND oi.principal.id <> oi.id 							");
            hql.append(" AND oi.id = :processoId2 ");
			hql.append(" AND ppe.tipoSituacaoPeca != 1 ");
			hql.append(" AND ppe.tipoPecaProcesso.id in (1060,1065,1221,1326,1510) ");
*/			
			hql.append( "ORDER BY ppe.numeroOrdemPeca ");
			
			Query query = session.createQuery(hql.toString());

			query.setLong("processoId", idProcessoIncidente);
//			query.setLong("processoId2", idProcessoIncidente);

			pecaProcessoEletronicos = query.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return pecaProcessoEletronicos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TextoAndamentoProcesso> pesquisarPecasUtilizadasEmVista(Long idProcessoIncidente) throws DaoException {
		
		List<TextoAndamentoProcesso> textoAndamentoProcesso = new ArrayList<TextoAndamentoProcesso>();
		Session session = retrieveSession();
		try{
			StringBuffer hql = new StringBuffer();	
			
			 hql.append("SELECT tap                                             ");
			 hql.append("FROM TextoAndamentoProcesso tap , AndamentoProcesso ap ");
			 hql.append("WHERE tap.andamentoProcesso.id = ap.id 				");
			 hql.append(" AND   ap.tipoAndamento.id = 8507           ");
			 hql.append(" AND   ap.objetoIncidente.id = :processoId ");
			
			 Query query = session.createQuery(hql.toString());
			 query.setLong("processoId", idProcessoIncidente);
			 textoAndamentoProcesso = query.list();
			
		} catch(Exception e){
			throw new DaoException(e);
		}
		
		return textoAndamentoProcesso;
	}
	
}
