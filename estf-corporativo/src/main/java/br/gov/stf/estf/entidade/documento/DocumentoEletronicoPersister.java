/**
 * 
 */
package br.gov.stf.estf.entidade.documento;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.cache.CacheConcurrencyStrategy;
import org.hibernate.engine.Mapping;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.persister.entity.SingleTableEntityPersister;

public class DocumentoEletronicoPersister extends SingleTableEntityPersister {
	
	private static final String CODIGO_LOAD_STORED_PROCEDURE_CALL = 
			"{call DOC.PKG_DOCUMENTO.PRC_RECUPERA_DOCUMENTO(?)}";
	private static final String CODIGO_INSERT_UPDATE_STORED_PROCEDURE_CALL = 
			"{call DOC.PKG_DOCUMENTO.PRC_GRAVA_DOCUMENTO (?)}";
	private static final String CODIGO_DELETE_STORED_PROCEDURE_CALL = 
			"{call DOC.PRC_CANCELA_DOCUMENTO(?,?)}";
	
	/*public DocumentoEletronicoPersister(PersistentClass arg0, CacheConcurrencyStrategy arg1, 
			SessionFactoryImplementor arg2, Mapping arg3) throws HibernateException {
		super(arg0, arg1, arg2, arg3);
	}*/
	
	

	public DocumentoEletronicoPersister(PersistentClass persistentClass,
			CacheConcurrencyStrategy cacheAccessStrategy,
			SessionFactoryImplementor factory, Mapping mapping)
			throws HibernateException {
		super(persistentClass, cacheAccessStrategy, factory, mapping); 
	}



	public void delete(Serializable id, Object version, Object object, SessionImplementor session) 
	throws HibernateException {
		
		//Inserção no Batcher da Stored Procedure de DELETE
		prepararBatcherProcedureCallableStatementArquivoComputador(CODIGO_DELETE_STORED_PROCEDURE_CALL, 
				(Long) id, session);
	}

	public Serializable insert(Object[] fields, Object object, SessionImplementor session) 
	throws HibernateException {
		
		//Chamada do método de insert padrão do Hibernate
		Serializable returnedId = super.insert(fields, object, session);
		
		//Inserção no Batcher da Stored Procedure de INSERT/UPDATE
		prepararBatcherProcedureCallableStatementArquivoComputador(
				CODIGO_INSERT_UPDATE_STORED_PROCEDURE_CALL, (Long) returnedId, session);
		
		return returnedId;
	}

	public void insert(Serializable id, Object[] fields, Object object, SessionImplementor session) 
	throws HibernateException {			
		
		//Chamada do método de insert padrão do Hibernate
		super.insert(id, fields, object, session);
		
		//Inserção no Batcher da Stored Procedure de INSERT/UPDATE
		prepararBatcherProcedureCallableStatementArquivoComputador(
				CODIGO_INSERT_UPDATE_STORED_PROCEDURE_CALL, (Long) id, session);
	}

	public Object load(Serializable id, Object optionalObject, LockMode lockMode, SessionImplementor session) 
	throws HibernateException {	
		
		//Inserção no Batcher da Stored Procedure de LOAD
		prepararBatcherProcedureCallableStatementArquivoComputador(CODIGO_LOAD_STORED_PROCEDURE_CALL, 
				(Long) id, session);
		
		//Chamada do método de load padrão do Hibernate
		return super.load(id, optionalObject, lockMode, session);
	}
	
	public void update(Serializable id, Object[] fields, int[] dirtyFields, boolean hasDirtyCollection, 
			Object[] oldFields, Object oldVersion, Object object, Object rowId, SessionImplementor session) 
	throws HibernateException {		
		
		//Inserção no Batcher da Stored Procedure de LOAD
		prepararBatcherProcedureCallableStatementArquivoComputador(CODIGO_LOAD_STORED_PROCEDURE_CALL, 
				(Long) id, session);
		
		//Chamada do método de update padrão do Hibernate
		super.update(id, fields, dirtyFields, hasDirtyCollection, oldFields, oldVersion, object, 
				rowId, session);
		
		//Inserção no Batcher da Stored Procedure de INSERT/UPDATE
		prepararBatcherProcedureCallableStatementArquivoComputador(
				CODIGO_INSERT_UPDATE_STORED_PROCEDURE_CALL, (Long) id, session);
		
	}
	
	private void prepararBatcherProcedureCallableStatementArquivoComputador(String procedureCall, 
			Long codigoArquivoComputador, SessionImplementor session)
	throws HibernateException
	{
		try
		{
			CallableStatement storedProcedureStmt = 
					session.getBatcher().prepareCallableStatement(
					procedureCall);
			
			storedProcedureStmt.setLong(1, codigoArquivoComputador.longValue());
			
			storedProcedureStmt.execute();
			
			session.getBatcher().closeStatement(storedProcedureStmt);
		}
		catch(SQLException e) {
			throw new HibernateException(e);
		}
	}
}
