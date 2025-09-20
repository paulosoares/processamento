package br.gov.stf.estf.expedicao.model.dataaccess.hibernate;

import static br.gov.stf.estf.expedicao.model.dataaccess.hibernate.RemessaDaoHibernate.NOME_COLUNA_DATA_CRIACAO;
import static br.gov.stf.estf.expedicao.model.dataaccess.hibernate.RemessaDaoHibernate.NOME_COLUNA_DATA_ENVIO;
import static br.gov.stf.estf.expedicao.model.dataaccess.hibernate.RemessaDaoHibernate.NOME_COLUNA_DOCUMENTO;
import static br.gov.stf.estf.expedicao.model.dataaccess.hibernate.RemessaDaoHibernate.NOME_COLUNA_GUIA_DESLOCAMENTO;
import static br.gov.stf.estf.expedicao.model.dataaccess.hibernate.RemessaDaoHibernate.NOME_COLUNA_LACRE;
import static br.gov.stf.estf.expedicao.model.dataaccess.hibernate.RemessaDaoHibernate.NOME_COLUNA_MALOTE;
import static br.gov.stf.estf.expedicao.model.dataaccess.hibernate.RemessaDaoHibernate.NOME_COLUNA_NUMERO_ETIQUETA_CORREIOS;
import static br.gov.stf.estf.expedicao.model.dataaccess.hibernate.RemessaDaoHibernate.NOME_COLUNA_NUMERO_LISTA_REMESSA;
import static br.gov.stf.estf.expedicao.model.dataaccess.hibernate.RemessaDaoHibernate.NOME_COLUNA_OBSERVACAO;
import static br.gov.stf.estf.expedicao.model.dataaccess.hibernate.RemessaDaoHibernate.NOME_COLUNA_VINCULO;
import static br.gov.stf.estf.expedicao.model.dataaccess.hibernate.RemessaDaoHibernate.NOME_PARAMETRO_DATA_CRIACAO_FIM;
import static br.gov.stf.estf.expedicao.model.dataaccess.hibernate.RemessaDaoHibernate.NOME_PARAMETRO_DATA_CRIACAO_INICIO;
import static br.gov.stf.estf.expedicao.model.dataaccess.hibernate.RemessaDaoHibernate.NOME_PARAMETRO_DATA_ENVIO_FIM;
import static br.gov.stf.estf.expedicao.model.dataaccess.hibernate.RemessaDaoHibernate.NOME_PARAMETRO_DATA_ENVIO_INICIO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.expedicao.entidade.ListaRemessa;
import br.gov.stf.estf.expedicao.entidade.Remessa;
import br.gov.stf.estf.expedicao.entidade.RemessaVolume;
import br.gov.stf.estf.expedicao.model.dataaccess.ListaRemessaDao;
import br.gov.stf.estf.expedicao.model.util.PesquisaListaRemessaDto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ListaRemessaDaoHibernate extends GenericHibernateDao<ListaRemessa, Long> implements ListaRemessaDao {

    public static final long serialVersionUID = 1L;

    public ListaRemessaDaoHibernate() {
        super(ListaRemessa.class);
    }

    enum Situacao_Lista {
    	TODAS,
    	ENVIADAS,
    	NAO_ENVIADAS;
    }

    public final String aliasGenerico = Util.gerarAliasGenerico(getPersistentClass());
    public final String selectGenerico = Util.gerarSelectHqlGenerico(getPersistentClass(), aliasGenerico);
    public final String aliasGenericoRemessa = Util.gerarAliasGenerico(Remessa.class);
    public final String aliasGenericoRemessaVolume = Util.gerarAliasGenerico(RemessaVolume.class) + "v";

    @Override
    public List<ListaRemessa> pesquisar(PesquisaListaRemessaDto pesquisaListaRemessaDto) throws DaoException {
    	return pesquisar(pesquisaListaRemessaDto, false);
    }

    @Override
    public List<ListaRemessa> pesquisar(PesquisaListaRemessaDto pesquisaListaRemessaDto, Boolean enviadas) throws DaoException {
    	Situacao_Lista situacaoLista;
        if (enviadas == null) {
    		situacaoLista = Situacao_Lista.TODAS;
    	} else {
    		if (enviadas) {
    			situacaoLista = Situacao_Lista.ENVIADAS;
    		} else {
    			situacaoLista = Situacao_Lista.NAO_ENVIADAS;
    		}
    	}
    	return pesquisar(pesquisaListaRemessaDto, situacaoLista);
    }

    private List<ListaRemessa> pesquisar(PesquisaListaRemessaDto pesquisaListaRemessaDto, Situacao_Lista situacaoLista) throws DaoException {
        Session session = retrieveSession();
        String sql = "SELECT " + aliasGenerico + ".* FROM EXPEDICAO.LISTA_REMESSA " + aliasGenerico;
        String clausulasWhere = "";
        String textoFiltroDestinatario = (pesquisaListaRemessaDto.getDestinatario() == null ? "" : pesquisaListaRemessaDto.getDestinatario().trim());
        if (!textoFiltroDestinatario.isEmpty()) {
            sql += " INNER JOIN EXPEDICAO.REMESSA " + aliasGenericoRemessa;
            sql += " ON (" + aliasGenerico + ".SEQ_LISTA_REMESSA = " + aliasGenericoRemessa + ".SEQ_LISTA_REMESSA) ";
            clausulasWhere = "(" + DestinatarioListaRemessaDaoHibernate.gerarClausulasSqlWhereVariosCampos(aliasGenericoRemessa) + ")";
        }
        if (pesquisaListaRemessaDto.getCodigoRastreio() != null && !pesquisaListaRemessaDto.getCodigoRastreio().isEmpty()) {
        	sql += " INNER JOIN EXPEDICAO.REMESSA_VOLUME " + aliasGenericoRemessaVolume;
            sql += " ON (" + aliasGenericoRemessa + ".SEQ_REMESSA = " + aliasGenericoRemessaVolume + ".SEQ_REMESSA) ";
        }

        clausulasWhere = clausulaWhereSituacaoLista(clausulasWhere, situacaoLista);

        // WHERE
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereEqualHqlComParametro(aliasGenerico, NOME_COLUNA_NUMERO_LISTA_REMESSA, true, false), pesquisaListaRemessaDto.getNumeroListaRemessa());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenericoRemessa, NOME_COLUNA_DOCUMENTO), pesquisaListaRemessaDto.getDocumento());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenericoRemessa, NOME_COLUNA_VINCULO), pesquisaListaRemessaDto.getVinculo());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenericoRemessa, NOME_COLUNA_GUIA_DESLOCAMENTO), pesquisaListaRemessaDto.getGuiaDeslocamento());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenericoRemessa, NOME_COLUNA_OBSERVACAO), pesquisaListaRemessaDto.getObservacao());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereBetweenHqlComParametro(aliasGenerico,
        		NOME_COLUNA_DATA_CRIACAO,
                NOME_PARAMETRO_DATA_CRIACAO_INICIO,
                NOME_PARAMETRO_DATA_CRIACAO_FIM,
                pesquisaListaRemessaDto.getDataCriacaoInicio(),
                pesquisaListaRemessaDto.getDataCriacaoFim()));
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereBetweenHqlComParametro(aliasGenerico,
        		NOME_COLUNA_DATA_ENVIO,
                NOME_PARAMETRO_DATA_ENVIO_INICIO,
                NOME_PARAMETRO_DATA_ENVIO_FIM,
                pesquisaListaRemessaDto.getDataEnvioInicio(),
                pesquisaListaRemessaDto.getDataEnvioFim()));
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenericoRemessa, NOME_COLUNA_MALOTE), pesquisaListaRemessaDto.getMalote());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenericoRemessa, NOME_COLUNA_LACRE), pesquisaListaRemessaDto.getLacre());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenericoRemessaVolume, NOME_COLUNA_NUMERO_ETIQUETA_CORREIOS), pesquisaListaRemessaDto.getCodigoRastreio());

        if (!clausulasWhere.isEmpty()) {
        	sql += Util.WHERE + clausulasWhere;
        }

        SQLQuery sqlQuery = session.createSQLQuery(sql).addEntity(getPersistentClass());

        if (!textoFiltroDestinatario.isEmpty()) {
            DestinatarioListaRemessaDaoHibernate.adicionarParametrosClausulasSqlWhereVariosCampos(sqlQuery, textoFiltroDestinatario);
        }
        Util.setParametroLongQuery(sqlQuery, NOME_COLUNA_NUMERO_LISTA_REMESSA, pesquisaListaRemessaDto.getNumeroListaRemessa());
        Util.setParametroStringQuery(sqlQuery, NOME_COLUNA_DOCUMENTO, pesquisaListaRemessaDto.getDocumento());
        Util.setParametroStringQuery(sqlQuery, NOME_COLUNA_VINCULO, pesquisaListaRemessaDto.getVinculo());
        Util.setParametroStringQuery(sqlQuery, NOME_COLUNA_GUIA_DESLOCAMENTO, pesquisaListaRemessaDto.getGuiaDeslocamento());
        Util.setParametroStringQuery(sqlQuery, NOME_COLUNA_OBSERVACAO, pesquisaListaRemessaDto.getObservacao());
        Util.setParametroTimestampQuery(sqlQuery, NOME_PARAMETRO_DATA_CRIACAO_INICIO, pesquisaListaRemessaDto.getDataCriacaoInicio(), AjusteDataEnum.PRIMEIRA_HORA_DIA);
        Util.setParametroTimestampQuery(sqlQuery, NOME_PARAMETRO_DATA_CRIACAO_FIM, pesquisaListaRemessaDto.getDataCriacaoFim(), AjusteDataEnum.ULTIMA_HORA_DIA);
        Util.setParametroTimestampQuery(sqlQuery, NOME_PARAMETRO_DATA_ENVIO_INICIO, pesquisaListaRemessaDto.getDataEnvioInicio(), AjusteDataEnum.PRIMEIRA_HORA_DIA);
        Util.setParametroTimestampQuery(sqlQuery, NOME_PARAMETRO_DATA_ENVIO_FIM, pesquisaListaRemessaDto.getDataEnvioFim(), AjusteDataEnum.ULTIMA_HORA_DIA);
        Util.setParametroStringQuery(sqlQuery, NOME_COLUNA_MALOTE, pesquisaListaRemessaDto.getMalote());
        Util.setParametroStringQuery(sqlQuery, NOME_COLUNA_LACRE, pesquisaListaRemessaDto.getLacre());
        Util.setParametroStringQuery(sqlQuery, NOME_COLUNA_NUMERO_ETIQUETA_CORREIOS, pesquisaListaRemessaDto.getCodigoRastreio());

        return sqlQuery.list();
    }
    
    private String clausulaWhereSituacaoLista(String clausulasWhere, Situacao_Lista situacaoLista){
    	if (situacaoLista != null && !situacaoLista.equals(Situacao_Lista.TODAS)) {
        	switch (situacaoLista) {
			case ENVIADAS:
				clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereNotNull(aliasGenerico, NOME_COLUNA_DATA_ENVIO));
				break;
			case NAO_ENVIADAS:
				clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereNull(aliasGenerico, NOME_COLUNA_DATA_ENVIO));
				break;
			default:
				break;
			}
        }
    	return clausulasWhere;
    }

	@Override
    public ListaRemessa pesquisar(long numeroListaRemessa, int ano) throws DaoException {
        Date dataInicio = Util.primeiroDiaAno(ano);
        Date dataFim = Util.ultimoDiaAno(ano);

        Session session = retrieveSession();
        String sql = "SELECT " + aliasGenerico + ".* FROM EXPEDICAO.LISTA_REMESSA " + aliasGenerico;

        // WHERE
        String clausulasWhere = Util.inserirClausulaWhereAnd("", Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_COLUNA_NUMERO_LISTA_REMESSA), numeroListaRemessa);
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereBetweenHqlComParametro(aliasGenerico,
        		NOME_COLUNA_DATA_CRIACAO,
                NOME_PARAMETRO_DATA_CRIACAO_INICIO,
                NOME_PARAMETRO_DATA_CRIACAO_FIM,
                dataInicio,
                dataFim));

        if (!clausulasWhere.isEmpty()) {
        	sql += Util.WHERE + clausulasWhere;
        }

        SQLQuery sqlQuery = session.createSQLQuery(sql).addEntity(getPersistentClass());

        Util.setParametroLongQuery(sqlQuery, NOME_COLUNA_NUMERO_LISTA_REMESSA, numeroListaRemessa);
        Util.setParametroTimestampQuery(sqlQuery, NOME_PARAMETRO_DATA_CRIACAO_INICIO, dataInicio, AjusteDataEnum.PRIMEIRA_HORA_DIA);
        Util.setParametroTimestampQuery(sqlQuery, NOME_PARAMETRO_DATA_CRIACAO_FIM, dataFim, AjusteDataEnum.ULTIMA_HORA_DIA);

        return (ListaRemessa) sqlQuery.uniqueResult();
	}

	@Override
	public long gerarNumeroListaRemessa(int ano) throws DaoException {
		final String nomeFuncaoGeraNumeroLista = "{? = call EXPEDICAO.FNC_GERA_NUMERO_LISTA_REMESSA}";
        Session session = retrieveSession();
		Connection connection = session.connection();
		CallableStatement statement;
		try {
			statement = connection.prepareCall(nomeFuncaoGeraNumeroLista);
			statement.registerOutParameter(1, java.sql.Types.DOUBLE);
			statement.executeUpdate();
			long numeroLista = statement.getLong(1);
			return numeroLista;
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}
}