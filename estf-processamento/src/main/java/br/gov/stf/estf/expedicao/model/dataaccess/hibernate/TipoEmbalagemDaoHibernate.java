package br.gov.stf.estf.expedicao.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.expedicao.entidade.TipoEmbalagem;
import br.gov.stf.estf.expedicao.model.dataaccess.TipoEmbalagemDao;
import br.gov.stf.estf.expedicao.model.util.TipoEmbalagemEnum;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoEmbalagemDaoHibernate extends GenericHibernateDao<TipoEmbalagem, Long> implements TipoEmbalagemDao {

    public static final long serialVersionUID = 1L;

    public final String aliasGenerico = Util.gerarAliasGenerico(getPersistentClass());
    public final String selectGenerico = Util.gerarSelectHqlGenerico(getPersistentClass(), aliasGenerico);

    public TipoEmbalagemDaoHibernate() {
    	super(TipoEmbalagem.class);
    }

    @Override
    public List<TipoEmbalagem> listarTiposServicoPorTipoEntrega(TipoEmbalagemEnum tipoEmbalagem) throws DaoException {
        Session session = retrieveSession();
        String hql = selectGenerico;

        // WHERE
        String nomeAtributoServicoCorreios = "tipo";

        if (tipoEmbalagem != null) {
        	String clausulas = Util.WHERE + Util.criarWhereEqualHqlComParametro(aliasGenerico, nomeAtributoServicoCorreios, true, false);
        	hql += clausulas;
        }

        Query query = session.createQuery(hql);

        if (tipoEmbalagem != null) {
        	query.setString(nomeAtributoServicoCorreios, tipoEmbalagem.getCodigo());
        }

        return (List<TipoEmbalagem>) query.list();
    }
}