/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisprudencia.JurisprudenciaRelevante;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.JurisprudenciaRelevanteDao;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 20.08.2012
 */
@Repository
public class JurisprudenciaRelevanteDaoHibernate extends GenericHibernateDao<JurisprudenciaRelevante, Long> implements
	JurisprudenciaRelevanteDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6814493405850566110L;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public JurisprudenciaRelevanteDaoHibernate() {
		super(JurisprudenciaRelevante.class);
	}

	
}
