package br.gov.stf.estf.expedicao.model.dataaccess.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.expedicao.entidade.Remetente;
import br.gov.stf.estf.expedicao.model.dataaccess.RemetenteDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class RemetenteDaoHibernate extends GenericHibernateDao<Remetente, Long> implements RemetenteDao {

    public static final long serialVersionUID = 1L;

    public final String aliasGenerico = Util.gerarAliasGenerico(getPersistentClass());
    public final String selectGenerico = Util.gerarSelectHqlGenerico(getPersistentClass(), aliasGenerico);

    public RemetenteDaoHibernate() {
    	super(Remetente.class);
    }

	@Override
	public List<Remetente> listarTodos() throws DaoException {
		return pesquisar();
	}
}