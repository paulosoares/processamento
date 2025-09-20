package br.gov.stf.estf.expedicao.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.expedicao.entidade.UnidadePostagem;
import br.gov.stf.estf.expedicao.entidade.ValorFlagSimNaoEnum;
import br.gov.stf.estf.expedicao.model.dataaccess.UnidadePostagemDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class UnidadePostagemDaoHibernate extends GenericHibernateDao<UnidadePostagem, Long> implements UnidadePostagemDao {

    public static final long serialVersionUID = 1L;

    public final String aliasGenerico = Util.gerarAliasGenerico(getPersistentClass());
    public final String selectGenerico = Util.gerarSelectHqlGenerico(getPersistentClass(), aliasGenerico);

    public UnidadePostagemDaoHibernate() {
        super(UnidadePostagem.class);
    }

    @Override
    public List<UnidadePostagem> listarAtivos() throws DaoException {
        Session session = retrieveSession();
        String hql = selectGenerico;

        // WHERE
        String nomeAtributoId = "id";
        String nomeAtributoAtivo = "ativo";

        String clausulas = Util.WHERE + Util.criarWhereEqualHqlComParametro(aliasGenerico, nomeAtributoAtivo, true, false);
        hql += clausulas;

        hql += " ORDER BY " + aliasGenerico + Util.PONTO + nomeAtributoAtivo + ", " + aliasGenerico + Util.PONTO + nomeAtributoId;

        Query query = session.createQuery(hql);

        query.setString(nomeAtributoAtivo, ValorFlagSimNaoEnum.buscar(true).getValorTexto());

        return (List<UnidadePostagem>) query.list();
    }
}