/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess.hibernate;

import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Types;

import org.apache.commons.io.IOUtils;
import org.hibernate.engine.SessionImplementor;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisprudencia.LegislacaoIncidenteAnalise;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.LegislacaoIncidenteAnaliseDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 03.08.2012
 */
@Repository
public class LegislacaoIncidenteAnaliseDaoHibernate extends GenericHibernateDao<LegislacaoIncidenteAnalise, Long> implements
		LegislacaoIncidenteAnaliseDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7456344095482121434L;

	public LegislacaoIncidenteAnaliseDaoHibernate() {
		super(LegislacaoIncidenteAnalise.class);
	}

	@Override
	public byte[] recuperarVisualizacaoLegislacaoSucessivo(LegislacaoIncidenteAnalise legislacaoIncidenteAnalise)
			throws DaoException {
		try {
			SessionImplementor session = (SessionImplementor) retrieveSession();
			
			CallableStatement stmt = session.getBatcher().prepareCallableStatement(
					"{? = call JURISPRUDENCIA.PKG_CARREGA_BRS_SJUR.FNC_VISUALIZAR_LEG_SUCESSIVO ( ? )}");
			
			stmt.registerOutParameter(1, Types.CLOB);
			stmt.setLong(2, legislacaoIncidenteAnalise.getId());
			stmt.execute();
			
			Clob clob = stmt.getClob(1);			
			byte[] retorno = IOUtils.toByteArray( new InputStreamReader( clob.getAsciiStream() ), "UTF-8");

			session.getBatcher().closeStatement(stmt);			
			return retorno;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
