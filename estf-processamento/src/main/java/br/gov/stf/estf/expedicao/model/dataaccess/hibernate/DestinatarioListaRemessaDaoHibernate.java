package br.gov.stf.estf.expedicao.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.corp.entidade.Municipio;
import br.gov.stf.estf.expedicao.entidade.DestinatarioListaRemessa;
import br.gov.stf.estf.expedicao.model.dataaccess.DestinatarioListaRemessaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class DestinatarioListaRemessaDaoHibernate extends GenericHibernateDao<DestinatarioListaRemessa, Long> implements DestinatarioListaRemessaDao {

    public static final long serialVersionUID = 1L;

    public static final String NOME_PARAMETRO_CONSULTA_VARIOS_CAMPOS = "nomeParametroVariosCampos";
    public static final String NOME_SIGLA_UF = Util.getNomeColunaMapeadaEntidade(Municipio.class, "siglaUf");
    public static final String NOME_COLUNA_DETALHE_PRE = Util.getNomeColunaMapeadaEntidade(DestinatarioListaRemessa.class, "descricaoAnterior");
    public static final String NOME_COLUNA_DETALHE_PRIN = Util.getNomeColunaMapeadaEntidade(DestinatarioListaRemessa.class, "descricaoPrincipal");
    public static final String NOME_COLUNA_DETALHE_POS = Util.getNomeColunaMapeadaEntidade(DestinatarioListaRemessa.class, "descricaoPosterior");
    public static final String NOME_COLUNA_LOGRADOURO = Util.getNomeColunaMapeadaEntidade(DestinatarioListaRemessa.class, "logradouro");
    public static final String NOME_COLUNA_NUMERO = Util.getNomeColunaMapeadaEntidade(DestinatarioListaRemessa.class, "numero");
    public static final String NOME_COLUNA_COMPLEMENTO = Util.getNomeColunaMapeadaEntidade(DestinatarioListaRemessa.class, "complemento");
    public static final String NOME_COLUNA_BAIRRO = Util.getNomeColunaMapeadaEntidade(DestinatarioListaRemessa.class, "bairro");
    public static final String NOME_COLUNA_MUNICIPIO = Util.getNomeColunaMapeadaEntidade(DestinatarioListaRemessa.class, "municipio");
    public static final String NOME_COLUNA_CEP = Util.getNomeColunaMapeadaEntidade(DestinatarioListaRemessa.class, "cep");
    public static final String NOME_COLUNA_NOME_CONTATO = Util.getNomeColunaMapeadaEntidade(DestinatarioListaRemessa.class, "nomeContato");
    public static final String NOME_COLUNA_EMAIL = Util.getNomeColunaMapeadaEntidade(DestinatarioListaRemessa.class, "email");
    public static final String NOME_COLUNA_CODIGO_AREA_TELEFONE = Util.getNomeColunaMapeadaEntidade(DestinatarioListaRemessa.class, "codigoAreaTelefone");
    public static final String NOME_COLUNA_NUMERO_TELEFONE = Util.getNomeColunaMapeadaEntidade(DestinatarioListaRemessa.class, "numeroTelefone");
    public static final String NOME_COLUNA_CODIGO_AREA_FAX = Util.getNomeColunaMapeadaEntidade(DestinatarioListaRemessa.class, "codigoAreaFax");
    public static final String NOME_COLUNA_NUMERO_FAX = Util.getNomeColunaMapeadaEntidade(DestinatarioListaRemessa.class, "numeroFax");
    public static final String NOME_COLUNA_DETALHE_AGRUP = Util.getNomeColunaMapeadaEntidade(DestinatarioListaRemessa.class, "agrupador");
    public static final String NOME_COLUNA_DETALHE_COD_OR = Util.getNomeColunaMapeadaEntidade(DestinatarioListaRemessa.class, "codigoOrigem");
    public static final String NOME_COLUNA_USU_INCLUSAO = Util.getNomeColunaMapeadaEntidade(DestinatarioListaRemessa.class, "usuarioInclusao");
    public static final String NOME_COLUNA_USU_ALTERACAO = Util.getNomeColunaMapeadaEntidade(DestinatarioListaRemessa.class, "usuarioAlteracao");
    public static final String NOME_COLUNA_DAT_INCLUSAO = Util.getNomeColunaMapeadaEntidade(DestinatarioListaRemessa.class, "dataInclusao");
    public static final String NOME_COLUNA_DAT_ALTERACAO = Util.getNomeColunaMapeadaEntidade(DestinatarioListaRemessa.class, "dataAlteracao");

    public DestinatarioListaRemessaDaoHibernate() {
        super(DestinatarioListaRemessa.class);
    }

    public final String aliasGenerico = Util.gerarAliasGenerico(getPersistentClass());
    public final String selectGenerico = Util.gerarSelectHqlGenerico(getPersistentClass(), aliasGenerico);
    public static final String aliasMunicipio = "m";

    @Override
    public List<DestinatarioListaRemessa> pesquisarVariosCampos(String texto) throws DaoException {
        Session session = retrieveSession();
        String sql = "SELECT * FROM EXPEDICAO.DESTINATARIO";
        String clausulasWhereVariosCampos = gerarClausulasSqlWhereVariosCampos();

        // WHERE
        if (!texto.trim().isEmpty()) {
            sql += Util.WHERE + clausulasWhereVariosCampos;
        }

        SQLQuery sqlQuery = session.createSQLQuery(sql).addEntity(getPersistentClass());

        if (!texto.trim().isEmpty()) {
            adicionarParametrosClausulasSqlWhereVariosCampos(sqlQuery, texto);
        }

        return sqlQuery.list();
    }

    @Override
    public List<DestinatarioListaRemessa> pesquisar(DestinatarioListaRemessa destinatario, String siglaUf) throws DaoException {
        Session session = retrieveSession();
        String siglaUfAux = null;
        if (siglaUf != null) {
        	siglaUfAux = siglaUf.trim();
        }
        String sql = "SELECT " + aliasGenerico + ".* FROM EXPEDICAO.DESTINATARIO " + aliasGenerico + " INNER JOIN CORP.MUNICIPIO " + aliasMunicipio
        		+ " ON (" + aliasGenerico + "." + NOME_COLUNA_MUNICIPIO + " = " + aliasMunicipio + "." + NOME_COLUNA_MUNICIPIO + ")";

        // WHERE
        String clausulasWhere = "";
        if (siglaUfAux != null && !siglaUfAux.isEmpty()) {
        	clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasMunicipio, NOME_SIGLA_UF), siglaUfAux);
        }
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_COLUNA_DETALHE_PRE), destinatario.getDescricaoAnterior());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_COLUNA_DETALHE_PRIN), destinatario.getDescricaoPrincipal());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_COLUNA_DETALHE_POS), destinatario.getDescricaoPosterior());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_COLUNA_LOGRADOURO), destinatario.getLogradouro());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_COLUNA_NUMERO), destinatario.getNumero());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_COLUNA_COMPLEMENTO), destinatario.getComplemento());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_COLUNA_BAIRRO), destinatario.getBairro());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereEqualHqlComParametro(aliasGenerico, NOME_COLUNA_MUNICIPIO), destinatario.getMunicipio());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_COLUNA_CEP), destinatario.getCep());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_COLUNA_NOME_CONTATO), destinatario.getNomeContato());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_COLUNA_EMAIL), destinatario.getEmail());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_COLUNA_CODIGO_AREA_TELEFONE), destinatario.getCodigoAreaTelefone());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_COLUNA_NUMERO_TELEFONE), destinatario.getNumeroTelefone());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_COLUNA_CODIGO_AREA_FAX), destinatario.getCodigoAreaFax());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_COLUNA_NUMERO_FAX), destinatario.getNumeroFax());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_COLUNA_DETALHE_AGRUP), destinatario.getAgrupador());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereEqualHqlComParametro(aliasGenerico, NOME_COLUNA_DETALHE_COD_OR), destinatario.getCodigoOrigem());       

        if (!clausulasWhere.isEmpty()) {
        	sql += Util.WHERE + clausulasWhere;
        }

        SQLQuery sqlQuery = session.createSQLQuery(sql).addEntity(getPersistentClass());

        if (siglaUfAux != null && !siglaUfAux.isEmpty()) {
        	Util.setParametroStringQuery(sqlQuery, NOME_SIGLA_UF, siglaUfAux);
        }
        Util.setParametroStringQuery(sqlQuery, NOME_COLUNA_DETALHE_PRE, destinatario.getDescricaoAnterior());
        Util.setParametroStringQuery(sqlQuery, NOME_COLUNA_DETALHE_PRIN, destinatario.getDescricaoPrincipal());
        Util.setParametroStringQuery(sqlQuery, NOME_COLUNA_DETALHE_POS, destinatario.getDescricaoPosterior());
        Util.setParametroStringQuery(sqlQuery, NOME_COLUNA_LOGRADOURO, destinatario.getLogradouro());
        Util.setParametroStringQuery(sqlQuery, NOME_COLUNA_NUMERO, destinatario.getNumero());
        Util.setParametroStringQuery(sqlQuery, NOME_COLUNA_COMPLEMENTO, destinatario.getComplemento());
        Util.setParametroStringQuery(sqlQuery, NOME_COLUNA_BAIRRO, destinatario.getBairro());
        Util.setParametroStringQueryLike(sqlQuery, NOME_COLUNA_MUNICIPIO, (destinatario.getMunicipio() != null ? destinatario.getMunicipio().getId() : null), false, false);
        Util.setParametroStringQuery(sqlQuery, NOME_COLUNA_CEP, destinatario.getCep());
        Util.setParametroStringQuery(sqlQuery, NOME_COLUNA_NOME_CONTATO, destinatario.getNomeContato());
        Util.setParametroStringQuery(sqlQuery, NOME_COLUNA_EMAIL, destinatario.getEmail());
        Util.setParametroStringQuery(sqlQuery, NOME_COLUNA_CODIGO_AREA_TELEFONE, destinatario.getCodigoAreaTelefone());
        Util.setParametroStringQuery(sqlQuery, NOME_COLUNA_NUMERO_TELEFONE, destinatario.getNumeroTelefone());
        Util.setParametroStringQuery(sqlQuery, NOME_COLUNA_CODIGO_AREA_FAX, destinatario.getCodigoAreaFax());
        Util.setParametroStringQuery(sqlQuery, NOME_COLUNA_NUMERO_FAX, destinatario.getNumeroFax());
        Util.setParametroStringQuery(sqlQuery, NOME_COLUNA_DETALHE_AGRUP, destinatario.getAgrupador());
                
        if (destinatario.getCodigoOrigem() != null) {
            sqlQuery.setShort(NOME_COLUNA_DETALHE_COD_OR, destinatario.getCodigoOrigem());
        }

        return (List<DestinatarioListaRemessa>) sqlQuery.list();
    }

    /**
     * Gera uma query, utilizando sql nativo, para uma consulta envolvendo 5 campos os seguintes campos:
     * Descri��o Anterior
     * Descri��o Principal
     * Descri��o Posterior
     * Agrupador
     * C�digo Origem
     *
     * @return 
     */
    public static String gerarClausulasSqlWhereVariosCampos() {
        return DestinatarioListaRemessaDaoHibernate.gerarClausulasSqlWhereVariosCampos(NOME_PARAMETRO_CONSULTA_VARIOS_CAMPOS, "");
    }

    /**
     * Gera uma query, utilizando sql nativo, para uma consulta envolvendo 5 campos os seguintes campos:
     * Descri��o Anterior
     * Descri��o Principal
     * Descri��o Posterior
     * Agrupador
     * C�digo Origem
     *
     * @param alias
     * @return 
     */
    public static String gerarClausulasSqlWhereVariosCampos(String alias) {
        return DestinatarioListaRemessaDaoHibernate.gerarClausulasSqlWhereVariosCampos(NOME_PARAMETRO_CONSULTA_VARIOS_CAMPOS, alias);
    }

    /**
     * Gera uma query, utilizando sql nativo, para uma consulta envolvendo 5 campos os seguintes campos:
     * Descri��o Anterior
     * Descri��o Principal
     * Descri��o Posterior
     * Agrupador
     * C�digo Origem
     *
     * @param nomeParametroVariosCampos
     * @param alias
     * @return 
     */
    public static String gerarClausulasSqlWhereVariosCampos(String nomeParametroVariosCampos, String alias) {
        String aliasComPonto = alias;
        if (!aliasComPonto.isEmpty() && !aliasComPonto.endsWith(Util.PONTO)) {
            aliasComPonto += Util.PONTO;
        }
        String clausulasWhereVariosCampos = " CONTAINS(" + aliasComPonto + "FLG_ATUALIZADO, :" + nomeParametroVariosCampos + ", 1) > 0" +
                " OR TO_CHAR(" + aliasComPonto + "COD_ORIGEM) LIKE :" + nomeParametroVariosCampos + " ";
        return clausulasWhereVariosCampos;
    }

    /**
     * Adiciona o valor informado ao par�metro com o nome padr�o (NOME_PARAMETRO_CONSULTA_VARIOS_CAMPOS), com tipo texto, na query.
     *
     * @param sqlQuery
     * @param texto 
     */
    public static void adicionarParametrosClausulasSqlWhereVariosCampos(SQLQuery sqlQuery, String texto) {
        DestinatarioListaRemessaDaoHibernate.adicionarParametrosClausulasSqlWhereVariosCampos(NOME_PARAMETRO_CONSULTA_VARIOS_CAMPOS, sqlQuery, texto);
    }

    /**
     * Adiciona o valor informado ao par�metro com o nome informado, com tipo texto, na query.
     *
     * @param nomeParametroVariosCampos
     * @param sqlQuery
     * @param texto 
     */
    public static void adicionarParametrosClausulasSqlWhereVariosCampos(String nomeParametroVariosCampos, SQLQuery sqlQuery, String texto) {
    	sqlQuery.setString(nomeParametroVariosCampos, texto);
    }
}