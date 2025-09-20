package br.gov.stf.estf.expedicao.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.expedicao.entidade.ValorFlagSimNaoEnum;
import br.gov.stf.estf.expedicao.entidade.VwEndereco;
import br.gov.stf.estf.expedicao.model.dataaccess.VwEnderecoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class VwEnderecoDaoHibernate extends GenericHibernateDao<VwEndereco, Long> implements VwEnderecoDao {

    public static final long serialVersionUID = 1L;

    public static final String NOME_LOGRADOURO = Util.getNomeColunaMapeadaEntidade(VwEndereco.class, "logradouro");
    public static final String NOME_CEP = Util.getNomeColunaMapeadaEntidade(VwEndereco.class, "cep");
    public static final String NOME_BAIRRO = Util.getNomeColunaMapeadaEntidade(VwEndereco.class, "bairro");
    public static final String NOME_MUNICIPIO = Util.getNomeColunaMapeadaEntidade(VwEndereco.class, "municipio");
    public static final String NOME_UF = Util.getNomeColunaMapeadaEntidade(VwEndereco.class, "uf");
    public static final String NOME_CLIENTE = Util.getNomeColunaMapeadaEntidade(VwEndereco.class, "cliente");

    public final String aliasGenerico = Util.gerarAliasGenerico(getPersistentClass());
    public final String selectGenerico = Util.gerarSelectHqlGenerico(getPersistentClass(), aliasGenerico);

    public VwEnderecoDaoHibernate() {
    	super(VwEndereco.class);
    }

    @Override
    public List<VwEndereco> pesquisar(String cep) throws DaoException {
        String numeroCep = cep + "";
        numeroCep = numeroCep.trim();

        Session session = retrieveSession();
        String hql = selectGenerico;

        // WHERE
        String nomeAtributoCep = "cep";
        String nomeAtributoNome = "logradouro";

        String clausulas = Util.inserirClausulaWhereAnd("", Util.criarWhereLikeHqlComParametro(aliasGenerico, nomeAtributoCep, true, false), numeroCep);
        if (!numeroCep.isEmpty()) {
        	clausulas = Util.WHERE + clausulas;
        }
        hql += clausulas;

        hql += " ORDER BY " + aliasGenerico + Util.PONTO + nomeAtributoNome;

        Query query = session.createQuery(hql);

        query.setString(nomeAtributoCep, ValorFlagSimNaoEnum.buscar(true).getValorTexto());
        if (!numeroCep.isEmpty()) {
            query.setString(nomeAtributoCep, numeroCep);
        }

        return (List<VwEndereco>) query.list();
    }

    @Override
    public List<VwEndereco> pesquisar(VwEndereco vwEndereco) throws DaoException {
        Session session = retrieveSession();
        String sql = "SELECT " + aliasGenerico + ".* FROM EXPEDICAO.VW_ENDERECO " + aliasGenerico;

        // WHERE
        String where = "";
        if (vwEndereco != null) {
		where = Util.inserirClausulaWhereOr(where, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_LOGRADOURO, false, true), vwEndereco.getLogradouro());
		where = Util.inserirClausulaWhereOr(where, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_CEP, false, true), vwEndereco.getCep());
		where = Util.inserirClausulaWhereOr(where, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_BAIRRO, false, true), vwEndereco.getBairro());
		where = Util.inserirClausulaWhereOr(where, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_MUNICIPIO, false, true), vwEndereco.getMunicipio());
		where = Util.inserirClausulaWhereOr(where, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_UF, false, true), vwEndereco.getUf());
		where = Util.inserirClausulaWhereOr(where, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_CLIENTE, false, true), vwEndereco.getCliente());
        }
        if (!where.isEmpty()) {
		where = Util.WHERE + where;
        }
        sql += where + " ORDER BY " + aliasGenerico + Util.PONTO + NOME_LOGRADOURO;

        SQLQuery sqlQuery = session.createSQLQuery(sql).addEntity(getPersistentClass());

        if (vwEndereco != null) {
                Util.setParametroStringQueryLike(sqlQuery, NOME_LOGRADOURO, vwEndereco.getLogradouro(), true, true);
                Util.setParametroStringQueryLike(sqlQuery, NOME_CEP, vwEndereco.getCep(), true, true);
                Util.setParametroStringQueryLike(sqlQuery, NOME_BAIRRO, vwEndereco.getBairro(), true, true);
                Util.setParametroStringQueryLike(sqlQuery, NOME_MUNICIPIO, vwEndereco.getMunicipio(), true, true);
                Util.setParametroStringQueryLike(sqlQuery, NOME_UF, vwEndereco.getUf(), true, true);
                Util.setParametroStringQueryLike(sqlQuery, NOME_CLIENTE, vwEndereco.getCliente(), true, true);
        }

        return (List<VwEndereco>) sqlQuery.list();
    }
}