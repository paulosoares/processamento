package br.gov.stf.estf.publicacao.model.dataaccess.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.publicacao.Feriado;
import br.gov.stf.estf.publicacao.model.dataaccess.FeriadoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@SuppressWarnings("unchecked")
@Repository
public class FeriadoDaoHibernate extends GenericHibernateDao<Feriado, String> implements FeriadoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FeriadoDaoHibernate() {
		super(Feriado.class);
	}

	@Override
	public List<Feriado> recuperar(String mesAno) throws DaoException {
		List<Object[]> feriadosObject  = null;
		List<Feriado> feriados = null;
		try {
			Session session = retrieveSession();
			
			/**
			 * Alterado pelo DECISAO-2295
			 *
			Criteria c = session.createCriteria(Feriado.class);
			if ( mesAno != null ) {
				c.add( Restrictions.eq("id", mesAno) );
			}
			feriados = c.list();
			*/
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT vwf.MES_ANO        ");
			sql.append("      ,vwf.DESCRICAO      ");
			sql.append("      ,vwf.DIA            ");
			sql.append("      ,vwf.HORARIO_INICIO ");
			sql.append("      ,vwf.HORARIO_FIM    ");
			sql.append("FROM                      ");
			sql.append("    SRH2.VW_FERIADOS vwf  ");
			sql.append("WHERE 1=1");
			if ( mesAno != null ) {
				sql.append(" AND vwf.HORARIO_INICIO='0000' AND vwf.HORARIO_FIM = '2359'");
				sql.append(" AND vwf.MES_ANO='"+mesAno+"'");
			}
			Query q = session.createSQLQuery(sql.toString());
			feriadosObject = q.list();
			feriados = this.Objetc2Feriado(feriadosObject);			
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return feriados;
	}
	
	private List<Feriado> Objetc2Feriado(List<Object[]> feriadosObject) {
		List<Feriado> feriados = new ArrayList<Feriado>();
		if(feriadosObject != null){
			for (Object[] feriadoObject : feriadosObject) {
				Feriado feriado = new Feriado();
				feriado.setId           ((String) feriadoObject[0]);
				feriado.setDescricao    ((String) feriadoObject[1]);
				feriado.setDia          ((String) feriadoObject[2]);
				feriado.setHorarioInicio((String) feriadoObject[3]);
				feriado.setHorarioFim   ((String) feriadoObject[4]);
				feriados.add(feriado);
			}
		}
		return feriados;
	}
	
	public List<Feriado> recuperar(String mesAno, String dia) throws DaoException {
		List<Feriado> feriados = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Feriado.class);
			if ( mesAno != null ) {
				c.add( Restrictions.eq("id", mesAno) );
			}
			if ( dia != null ) {
				c.add( Restrictions.eq("dia", dia) );
			}
			feriados = c.list();
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return feriados;
	}

}