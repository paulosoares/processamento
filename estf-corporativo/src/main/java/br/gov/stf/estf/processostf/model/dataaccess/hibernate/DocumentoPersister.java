package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

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
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.springframework.stereotype.Repository;

@Repository
public class DocumentoPersister extends SingleTableEntityPersister {
	
	private static final String CODIGO_LOAD_STORED_PROCEDURE_CALL = 
			"{call DOC.PKG_DOCUMENTO.PRC_RECUPERA_DOCUMENTO(?)}";
	private static final String CODIGO_INSERT_UPDATE_STORED_PROCEDURE_CALL = 
			"{call DOC.PKG_DOCUMENTO.PRC_GRAVA_DOCUMENTO(?)}";
	private static final String CODIGO_DELETE_STORED_PROCEDURE_CALL = 
			"{call DOC.PKG_DOCUMENTO.PRC_CANCELA_DOCUMENTO(?,'')}";
	
	public DocumentoPersister(PersistentClass arg0, CacheConcurrencyStrategy arg1, 
			SessionFactoryImplementor arg2, Mapping arg3) throws HibernateException {
		super(arg0, arg1, arg2, arg3);
	}

	public void delete(Serializable id, Object version, Object object, SessionImplementor session) 
	throws HibernateException {
						
		StopWatch stopWatch = new Log4JStopWatch();
		
		//Inserção no Batcher da Stored Procedure de DELETE		
		prepararBatcherProcedureCallableStatementArquivoComputador(CODIGO_DELETE_STORED_PROCEDURE_CALL, 
				(Long) id, session);
		
		stopWatch.stop("CancelaDocumento");
	}

	public Serializable insert(Object[] fields, Object object, SessionImplementor session) 
	throws HibernateException {
		
		StopWatch stopWatch = new Log4JStopWatch();
		
		//Chamada do método de insert padrão do Hibernate
		Serializable returnedId = super.insert(fields, object, session);
		
		//Inserção no Batcher da Stored Procedure de INSERT/UPDATE
		prepararBatcherProcedureCallableStatementArquivoComputador(
				CODIGO_INSERT_UPDATE_STORED_PROCEDURE_CALL, (Long) returnedId, session);
		
		stopWatch.stop("GravaDocumento");
		
		return returnedId;
	}

	public void insert(Serializable id, Object[] fields, Object object, SessionImplementor session) 
	throws HibernateException {
		
		StopWatch stopWatch = new Log4JStopWatch();
		
		//Chamada do método de insert padrão do Hibernate
		super.insert(id, fields, object, session);
		
		//Inserção no Batcher da Stored Procedure de INSERT/UPDATE
		prepararBatcherProcedureCallableStatementArquivoComputador(
				CODIGO_INSERT_UPDATE_STORED_PROCEDURE_CALL, (Long) id, session);
		
		stopWatch.stop("GravaDocumento");
	}

	public Object load(Serializable id, Object optionalObject, LockMode lockMode, SessionImplementor session) 
	throws HibernateException {		
		
		StopWatch stopWatch = new Log4JStopWatch();
		
		//Inserção no Batcher da Stored Procedure de LOAD
		prepararBatcherProcedureCallableStatementArquivoComputador(CODIGO_LOAD_STORED_PROCEDURE_CALL, 
				(Long) id, session);

		stopWatch.stop("RecuperaDocumento");
		
		//Chamada do método de load padrão do Hibernate
		return super.load(id, optionalObject, lockMode, session);
	}
	
	public void update(Serializable id, Object[] fields, int[] dirtyFields, boolean hasDirtyCollection, 
			Object[] oldFields, Object oldVersion, Object object, Object rowId, SessionImplementor session) 
	throws HibernateException {
		
		StopWatch stopWatch = new Log4JStopWatch();
		
		//Inserção no Batcher da Stored Procedure de LOAD
		prepararBatcherProcedureCallableStatementArquivoComputador(CODIGO_LOAD_STORED_PROCEDURE_CALL, 
				(Long) id, session);

		stopWatch.lap("RecuperaDocumento");
		
		//Chamada do método de update padrão do Hibernate
		super.update(id, fields, dirtyFields, hasDirtyCollection, oldFields, oldVersion, object, 
				rowId, session);
		
		//Inserção no Batcher da Stored Procedure de INSERT/UPDATE
		prepararBatcherProcedureCallableStatementArquivoComputador(
				CODIGO_INSERT_UPDATE_STORED_PROCEDURE_CALL, (Long) id, session);

		stopWatch.stop("GravaDocumento");
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
