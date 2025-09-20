package br.gov.stf.estf.corp.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.corp.entidade.UnidadeFederacao;
import br.gov.stf.estf.corp.model.dataaccess.UnidadeFederacaoDao;
import br.gov.stf.estf.expedicao.entidade.ValorFlagSimNaoEnum;
import br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class UnidadeFederacaoDaoHibernate extends GenericHibernateDao<UnidadeFederacao, Long> implements UnidadeFederacaoDao {

    public static final long serialVersionUID = 1L;

    public final String aliasGenerico = Util.gerarAliasGenerico(getPersistentClass());
    public final String selectGenerico = Util.gerarSelectHqlGenerico(getPersistentClass(), aliasGenerico);

    public UnidadeFederacaoDaoHibernate() {
        super(UnidadeFederacao.class);
    }

    @Override
    public List<UnidadeFederacao> listarAtivos() throws DaoException {
        Session session = retrieveSession();
        String hql = selectGenerico;

        // WHERE
        String nomeAtributoAtivo = "ativo";
        String nomeAtributoSigla = "sigla";

        String clausulas = Util.WHERE + Util.criarWhereEqualHqlComParametro(aliasGenerico, nomeAtributoAtivo, true, false);
        hql += clausulas;

        hql += " ORDER BY " + aliasGenerico + Util.PONTO + nomeAtributoSigla;

        Query query = session.createQuery(hql);

        query.setString(nomeAtributoAtivo, ValorFlagSimNaoEnum.buscar(true).getValorTexto());

        return (List<UnidadeFederacao>) query.list();
    }
}