package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.DocumentoTextoPeticaoDao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTextoPeticao;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class DocumentoTextoPeticaoDaoHibernate extends GenericHibernateDao<DocumentoTextoPeticao, Long>
	implements DocumentoTextoPeticaoDao {

	private static final long serialVersionUID = -6767145954311715518L;
	
	public DocumentoTextoPeticaoDaoHibernate() {
		super(DocumentoTextoPeticao.class);
	}

	@SuppressWarnings("unchecked")
	public List<DocumentoTextoPeticao> pesquisarDocumentosSetor(Setor setor, TipoSituacaoDocumento tipoSituacaoDocumento, Date dataInicio, Date dataFim, Short anoProtocolo, Long numeroProtocolo) 
			throws DaoException {
	
		try {
			if(dataInicio !=null){			
					dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
						.parse(new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " 00:00:00");
			}
			if(dataFim !=null){			
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
					.parse(new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59");
			}		
		} catch (ParseException e) {
			throw new DaoException(e);
		}
		
		if ( anoProtocolo!=null && anoProtocolo.intValue()==0 ) {
			anoProtocolo = null;
		}
		if ( numeroProtocolo!=null && numeroProtocolo.intValue()==0 ) {
			numeroProtocolo = null;
		}
		
		List<DocumentoTextoPeticao> listaDocumentoTextoPeticao = null;
		
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT dt FROM DocumentoTextoPeticao dt, Protocolo p left join fetch dt.assinaturaDigitalView join fetch dt.documentoEletronicoView join fetch dt.textoPeticao join fetch dt.textoPeticao.objetoIncidente ");
		hql.append(" WHERE dt.tipoSituacaoDocumento = :tipoSituacaoDocumento ");
		hql.append(" AND dt.dataRevisao IS NOT NULL ");
		hql.append(" AND dt.textoPeticao.objetoIncidente = p ");
		if ( anoProtocolo!=null ) {
			hql.append(" AND p.anoProtocolo = :anoProtocolo ");
		}
		if ( numeroProtocolo!=null ) {
			hql.append(" AND p.numeroProtocolo = :numeroProtocolo ");
		}
		
		boolean maxResult = false;
		if( tipoSituacaoDocumento==TipoSituacaoDocumento.REVISADO ){				
			if( dataInicio != null && dataFim != null ) {
				hql.append(" AND dt.dataRevisao BETWEEN :dataInicio AND :dataFim ");
			}
		}else if(tipoSituacaoDocumento.equals(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE)){
			if(dataInicio != null && dataFim != null){
				hql.append(" AND dt.assinaturaDigitalView.dataCarimboTempo BETWEEN :dataInicio AND :dataFim ");
			}else{
				maxResult = true;
			}
		}	
		
		hql.append(" ORDER BY dt.textoPeticao.arquivoEletronico.id ASC, p.numeroProtocolo ASC ");
		
		Session session = retrieveSession();
//		Criteria criteria  = session.createCriteria(DocumentoTextoPeticao.class);
//			criteria.add(Restrictions.eq("tipoSituacaoDocumento", tipoSituacaoDocumento));
//		Criteria criteriaTextoPeticao = criteria.createCriteria("textoPeticao");
//			if( anoProtocolo != null || numeroProtocolo != null ){			
//				criteriaTextoPeticao.setFetchMode("protocolo", FetchMode.JOIN);
//				Criteria criteriaProtocolo = criteriaTextoPeticao.createCriteria("protocolo");
//				if( anoProtocolo != null ) {
//					criteriaProtocolo.add(Restrictions.eq("id.ano", anoProtocolo));
//				}
//				if( numeroProtocolo != null ) {
//					criteriaProtocolo.add(Restrictions.eq("id.numero", numeroProtocolo));
//				}
//			}			
//
//			// Ordenação dos documentos
//			criteriaTextoPeticao.addOrder(Order.asc("arquivoEletronico"));
//			criteriaTextoPeticao.addOrder(Order.asc("protocolo"));
//			criteria.add(Restrictions.isNotNull("dataRevisao"));
//		if(tipoSituacaoDocumento.equals(TipoSituacaoDocumento.REVISADO)){				
//			if(dataInicio != null && dataFim != null){				
//				criteria.add(Restrictions.between("dataRevisao", dataInicio, dataFim));				
//			}
//		}else if(tipoSituacaoDocumento.equals(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE)){			
//			Criteria criteriaAssinaturaDigital = criteria.createCriteria("assinaturaDigitalView");
//			criteria.setFetchMode("assinaturaDigitalView", FetchMode.JOIN);
//			if(dataInicio != null && dataFim != null){
//				criteriaAssinaturaDigital.add(Restrictions.between("dataCarimboTempo", dataInicio, dataFim));							
//			}else{
//				// Limita em 300 documentos assinados digitalmente se não for informado o período
//				criteria.setMaxResults(300);
//			}
//		}		
//			
//		listaDocumentoTextoPeticao = criteria.list();	
		
		Query q = session.createQuery( hql.toString() );
		if ( maxResult ) {
			q.setMaxResults(300);
		}
		
		q.setParameter("tipoSituacaoDocumento", tipoSituacaoDocumento.getCodigo());
		
		if ( dataInicio!=null ) {
			q.setDate("dataInicio", dataInicio);
		}
		if ( dataFim!=null ) {
			q.setDate("dataFim", dataFim);
		}
		if ( anoProtocolo!=null ) {
			q.setShort("anoProtocolo", anoProtocolo);
		}
		if ( numeroProtocolo!=null ) {
			q.setLong("numeroProtocolo", numeroProtocolo);
		}
		
		listaDocumentoTextoPeticao = q.list();
		
		return listaDocumentoTextoPeticao;
	}
	
	public DocumentoTextoPeticao recuperar(DocumentoEletronico documentoEletronico)
			throws DaoException {
		DocumentoTextoPeticao documentoTextoPeticao = null;

		Session session = retrieveSession();		
		Criteria criteria = session.createCriteria(DocumentoTextoPeticao.class, "dtp");
		criteria = criteria.createAlias(
				"dtp.documentoEletronico", "de", CriteriaSpecification.INNER_JOIN).
				setFetchMode("de", FetchMode.JOIN);
		criteria.add(Restrictions.eq("de.id", documentoEletronico.getId()));
	
		return documentoTextoPeticao = (DocumentoTextoPeticao) criteria.uniqueResult();	
	}	

}
