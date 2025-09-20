package br.gov.stf.estf.entidade.documento;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.cache.CacheConcurrencyStrategy;
import org.hibernate.engine.Mapping;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.persister.entity.SingleTableEntityPersister;

public class AssinaturaDigitalPersister extends SingleTableEntityPersister {
		
		private static final String CODIGO_INSERT_UPDATE_STORED_PROCEDURE_CALL = "{call DOC.PKG_DOCUMENTO.PRC_ASSINA_DOCUMENTO (?)}";
		
		/*public AssinaturaDigitalPersister(PersistentClass arg0, CacheConcurrencyStrategy arg1, 
				SessionFactoryImplementor arg2, Mapping arg3) throws HibernateException {
			super(arg0, arg1, arg2, arg3);
		}*/
		

		public AssinaturaDigitalPersister(PersistentClass persistentClass,
				CacheConcurrencyStrategy cacheAccessStrategy,
				SessionFactoryImplementor factory, Mapping mapping)
				throws HibernateException {
			super(persistentClass, cacheAccessStrategy, factory, mapping);			
		}


		public Serializable insert(Object[] fields, Object object, SessionImplementor session)throws HibernateException {
			
			//Chamada do método de insert padrão do Hibernate
			Serializable returnedId = super.insert(fields, object, session);
			
			//Inserção no Batcher da Stored Procedure de INSERT/UPDATE
			prepararBatcherProcedureCallableStatementAssinaturaDigital( CODIGO_INSERT_UPDATE_STORED_PROCEDURE_CALL, (Long) returnedId, session);
			
			return returnedId;
		}

		public void insert(Serializable id, Object[] fields, Object object, SessionImplementor session) throws HibernateException {			
			
			//Chamada do método de insert padrão do Hibernate
			super.insert(id, fields, object, session);
			
			//Inserção no Batcher da Stored Procedure de INSERT/UPDATE
			prepararBatcherProcedureCallableStatementAssinaturaDigital( CODIGO_INSERT_UPDATE_STORED_PROCEDURE_CALL, (Long) id, session);
		}
		
		private void prepararBatcherProcedureCallableStatementAssinaturaDigital(String procedureCall, Long seqAssinaturaDigital, SessionImplementor session)
				throws HibernateException {
			try {
				CallableStatement storedProcedureStmt =  session.getBatcher().prepareCallableStatement( procedureCall);
				
				storedProcedureStmt.setLong(1, seqAssinaturaDigital );
				
				storedProcedureStmt.execute();
				
				session.getBatcher().closeStatement(storedProcedureStmt);
			} catch(SQLException e) {
				throw new HibernateException(e);
			}
		}
	}	
