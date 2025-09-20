package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.TextoAndamentoProcessoDao;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TextoAndamentoProcesso;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TextoAndamentoProcessoDaoHibernate extends GenericHibernateDao<TextoAndamentoProcesso, Long> implements TextoAndamentoProcessoDao { 
    public TextoAndamentoProcessoDaoHibernate() {
		super(TextoAndamentoProcesso.class);
	}

	private static final long serialVersionUID = 1L;
	
    public List<TextoAndamentoProcesso> recuperarTextoAndamentoProcesso(Long codigoAndamentoProcesso, Long codigoDocumento) throws DaoException {
	       Session session = retrieveSession();
	        
	        List<TextoAndamentoProcesso> listaTextoAndamentoProcesso = null;

	        try {
	        	
	        	Criteria criteria = session.createCriteria(TextoAndamentoProcesso.class);
	        	
	        	if (codigoAndamentoProcesso!=null){
					criteria.add(Restrictions.eq("andamentoProcesso.id", codigoAndamentoProcesso));
				}
	        	if (codigoDocumento!=null){
					criteria.add(Restrictions.eq("seqDocumento", codigoDocumento));
				}
	        	
	        	listaTextoAndamentoProcesso = criteria.list();
	        }
	        catch(HibernateException e) {
	            throw new DaoException("HibernateException",
	                    SessionFactoryUtils.convertHibernateAccessException(e));
	        }
	        catch( RuntimeException e ) {
	            throw new DaoException("RuntimeException", e);
	        }	       
	        return listaTextoAndamentoProcesso;		
	}
	
 
	public TextoAndamentoProcesso recuperarTextoAndamentoProcesso(Long numero) throws DaoException {
	       Session session = retrieveSession();
	        
	        TextoAndamentoProcesso textoAndamentoProcesso = null;

	        try {
	        	
	        	Criteria criteria = session.createCriteria(TextoAndamentoProcesso.class);
	        	criteria.add(Restrictions.idEq(numero));
	        	textoAndamentoProcesso = (TextoAndamentoProcesso) criteria.uniqueResult();
	        }
	        catch(HibernateException e) {
	            throw new DaoException("HibernateException",
	                    SessionFactoryUtils.convertHibernateAccessException(e));
	        }
	        catch( RuntimeException e ) {
	            throw new DaoException("RuntimeException", e);
	        }	       
	        return textoAndamentoProcesso;		
	}
	
	public Texto recuperarTexto(Long codigoTexto) throws DaoException {
	        Session session = retrieveSession();	        
	        Texto texto = null;
	        try {
	        	
	        	Criteria criteria = session.createCriteria(Texto.class);
	        	criteria.add(Restrictions.idEq(codigoTexto));
	        	texto = (Texto) criteria.uniqueResult();
	        }
	        catch(HibernateException e) {
	            throw new DaoException("HibernateException",
	                    SessionFactoryUtils.convertHibernateAccessException(e));
	        }
	        catch( RuntimeException e ) {
	            throw new DaoException("RuntimeException", e);
	        }	       
	        return texto;		
	}	
	
 public void persistirTextoAndamentoProcesso(TextoAndamentoProcesso textoAndamentoProcesso) throws DaoException {
     Session session = retrieveSession();            
     try {                    
         session.persist(textoAndamentoProcesso);          
         session.flush();
     }
     catch(HibernateException e) {
         throw new DaoException("HibernateException",
                 SessionFactoryUtils.convertHibernateAccessException(e));
     }
     catch( RuntimeException e ) {
         throw new DaoException("RuntimeException", e);
     }            
 }
 
 public void persistirTexto(Texto texto) throws DaoException {
     Session session = retrieveSession();
             
     try {
     	String sqlBrs = "select ARQUIVOSIAJ.SEQ_ARQUIVO_COMPUTADOR.nextval id from dual";
     	
     	Long codigoBrs = (Long) session.createSQLQuery(sqlBrs)
			.addScalar("id", Hibernate.INTEGER).uniqueResult();
     	
     	//FIXME Isto é um POG(Arrumar).        	
     	String sql = "select STF.SEQ_TEXTOS.nextval id from dual";
			Long codigo = (Long) session.createSQLQuery(sql)
			.addScalar("id", Hibernate.LONG).uniqueResult();
			texto.setId(codigo+1);
			texto.getArquivoEletronico().setId(codigoBrs); // TODO Ver com Leo
         session.persist(texto);
         session.flush();                      
     }
     catch(HibernateException e) {
         throw new DaoException("HibernateException",
                 SessionFactoryUtils.convertHibernateAccessException(e));
     }
     catch( RuntimeException e ) {
         throw new DaoException("RuntimeException", e);
     }            
 }    
	
 public void persistirDocumentoTexto(DocumentoTexto documentoTexto) throws DaoException {
     Session session = retrieveSession();            
     try {                    
         session.persist(documentoTexto);
         session.flush();
     }
     catch(HibernateException e) {
         throw new DaoException("HibernateException",
                 SessionFactoryUtils.convertHibernateAccessException(e));
     }
     catch( RuntimeException e ) {
         throw new DaoException("RuntimeException", e);
     }            
 }   
}
