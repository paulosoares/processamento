package br.gov.stf.estf.expedicao.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.expedicao.entidade.ListaRemessa;
import br.gov.stf.estf.expedicao.entidade.Remessa;
import br.gov.stf.estf.expedicao.entidade.RemessaVolume;
import br.gov.stf.estf.expedicao.model.dataaccess.RemessaDao;
import br.gov.stf.estf.expedicao.model.util.PesquisaListaRemessaDto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class RemessaDaoHibernate extends GenericHibernateDao<Remessa, Long> implements RemessaDao {

    public static final long serialVersionUID = 1L;

    public static final String NOME_COLUNA_NUMERO_LISTA_REMESSA = Util.getNomeColunaMapeadaEntidade(ListaRemessa.class, "numeroListaRemessa");
    public static final String NOME_COLUNA_ANO_LISTA_REMESSA = Util.getNomeColunaMapeadaEntidade(ListaRemessa.class, "anoListaRemessa");
    public static final String NOME_COLUNA_DOCUMENTO = Util.getNomeColunaMapeadaEntidade(Remessa.class, "numeroComunicacao");
    public static final String NOME_COLUNA_VINCULO = Util.getNomeColunaMapeadaEntidade(Remessa.class, "vinculo");
    public static final String NOME_COLUNA_GUIA_DESLOCAMENTO = Util.getNomeColunaMapeadaEntidade(Remessa.class, "guiaDeslocamento");
    public static final String NOME_COLUNA_OBSERVACAO = Util.getNomeColunaMapeadaEntidade(Remessa.class, "observacao");
    public static final String NOME_COLUNA_DATA_CRIACAO = Util.getNomeColunaMapeadaEntidade(ListaRemessa.class, "dataCriacao");
    public static final String NOME_PARAMETRO_DATA_CRIACAO_INICIO = "dataCriacaoInicio";
    public static final String NOME_PARAMETRO_DATA_CRIACAO_FIM = "dataCriacaoFim";
    public static final String NOME_COLUNA_DATA_ENVIO = Util.getNomeColunaMapeadaEntidade(ListaRemessa.class, "dataEnvio");
    public static final String NOME_PARAMETRO_DATA_ENVIO_INICIO = "dataEnvioInicio";
    public static final String NOME_PARAMETRO_DATA_ENVIO_FIM = "dataEnvioFim";
    public static final String NOME_COLUNA_MALOTE = Util.getNomeColunaMapeadaEntidade(Remessa.class, "malote");
    public static final String NOME_COLUNA_LACRE = Util.getNomeColunaMapeadaEntidade(Remessa.class, "lacre");
    public static final String NOME_COLUNA_NUMERO_ETIQUETA_CORREIOS = Util.getNomeColunaMapeadaEntidade(RemessaVolume.class, "numeroEtiquetaCorreios");

    public RemessaDaoHibernate() {
        super(Remessa.class);
    }

    public final String aliasGenerico = Util.gerarAliasGenerico(getPersistentClass());
    public final String selectGenerico = Util.gerarSelectHqlGenerico(getPersistentClass(), aliasGenerico);
    public final String aliasGenericoListaRemessa = Util.gerarAliasGenerico(ListaRemessa.class);
    public final String aliasGenericoListaRemessa2 = Util.gerarAliasGenerico(ListaRemessa.class) + 2;
    public final String aliasGenericoRemessaVolume = Util.gerarAliasGenerico(RemessaVolume.class) + "v";
    public final String aliasGenericoRemessaListaRemessa = "rlr";

    @Override
    public List<Remessa> pesquisar(PesquisaListaRemessaDto pesquisaListaRemessaDto) throws DaoException {
    	return pesquisar(pesquisaListaRemessaDto, false);
    }

	@Override
	public List<Remessa> pesquisarEnviadas(PesquisaListaRemessaDto pesquisaListaRemessaDto) throws DaoException {
    	return pesquisar(pesquisaListaRemessaDto, true);
	}
	
    private List<Remessa> pesquisar(PesquisaListaRemessaDto pesquisaListaRemessaDto, boolean apenasEnviadas) throws DaoException {
        Session session = retrieveSession();        
        String sql = " SELECT " + aliasGenerico + ".* FROM EXPEDICAO.REMESSA " + aliasGenerico;
        String clausulasWhere = "";

        String textoFiltroDestinatario = checkedTrim(pesquisaListaRemessaDto.getDestinatario());
        if (apenasEnviadas
    		|| (!textoFiltroDestinatario.isEmpty())
    		|| pesquisaListaRemessaDto.getNumeroListaRemessa() != null
    		|| pesquisaListaRemessaDto.getAnoListaRemessa() != null
            || pesquisaListaRemessaDto.getDataCriacaoInicio() != null
            || pesquisaListaRemessaDto.getDataCriacaoFim() != null
            || pesquisaListaRemessaDto.getDataEnvioInicio() != null
            || pesquisaListaRemessaDto.getDataEnvioFim() != null) {
            sql += " INNER JOIN EXPEDICAO.LISTA_REMESSA " + aliasGenericoListaRemessa;
            sql += " ON " + aliasGenerico + ".SEQ_LISTA_REMESSA = " + aliasGenericoListaRemessa + ".SEQ_LISTA_REMESSA ";
        }
        
        if(pesquisaListaRemessaDto.getRemessasListaRemessa() != null && !pesquisaListaRemessaDto.getRemessasListaRemessa().trim().isEmpty()){
            sql += " INNER JOIN EXPEDICAO.REMESSA_LISTA_REMESSA " + aliasGenericoRemessaListaRemessa;
            sql += " ON " + aliasGenerico + ".SEQ_REMESSA = " + aliasGenericoRemessaListaRemessa + ".SEQ_REMESSA ";
            sql += " INNER JOIN EXPEDICAO.LISTA_REMESSA " + aliasGenericoListaRemessa2;
            sql += " ON " + aliasGenericoListaRemessa2 + ".SEQ_LISTA_REMESSA = " + aliasGenericoRemessaListaRemessa + ".SEQ_LISTA_REMESSA ";            
        }        

        sql = avaliaCodigoRastreio(sql, pesquisaListaRemessaDto);

        if (!textoFiltroDestinatario.isEmpty()) {
        	clausulasWhere += "(" + DestinatarioListaRemessaDaoHibernate.gerarClausulasSqlWhereVariosCampos(aliasGenerico) + ")";
        }

        clausulasWhere = avaliaApenasEnviadas(apenasEnviadas, clausulasWhere);

        // WHERE
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenericoListaRemessa, NOME_COLUNA_NUMERO_LISTA_REMESSA), pesquisaListaRemessaDto.getNumeroListaRemessa());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenericoListaRemessa, NOME_COLUNA_ANO_LISTA_REMESSA), pesquisaListaRemessaDto.getAnoListaRemessa());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_COLUNA_DOCUMENTO), pesquisaListaRemessaDto.getDocumento());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_COLUNA_VINCULO), pesquisaListaRemessaDto.getVinculo());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_COLUNA_GUIA_DESLOCAMENTO), pesquisaListaRemessaDto.getGuiaDeslocamento());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_COLUNA_OBSERVACAO), pesquisaListaRemessaDto.getObservacao());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereBetweenHqlComParametro(aliasGenericoListaRemessa,
        		NOME_COLUNA_DATA_CRIACAO,
                NOME_PARAMETRO_DATA_CRIACAO_INICIO,
                NOME_PARAMETRO_DATA_CRIACAO_FIM,
                pesquisaListaRemessaDto.getDataCriacaoInicio(),
                pesquisaListaRemessaDto.getDataCriacaoFim()));
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereBetweenHqlComParametro(aliasGenericoListaRemessa,
        		NOME_COLUNA_DATA_ENVIO,
                NOME_PARAMETRO_DATA_ENVIO_INICIO,
                NOME_PARAMETRO_DATA_ENVIO_FIM,
                pesquisaListaRemessaDto.getDataEnvioInicio(),
                pesquisaListaRemessaDto.getDataEnvioFim()));
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_COLUNA_MALOTE), pesquisaListaRemessaDto.getMalote());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_COLUNA_LACRE), pesquisaListaRemessaDto.getLacre());
        clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereLikeHqlComParametro(aliasGenericoRemessaVolume, NOME_COLUNA_NUMERO_ETIQUETA_CORREIOS), pesquisaListaRemessaDto.getCodigoRastreio());
        clausulasWhere = inserirClausulaRemessaListaRemessa(pesquisaListaRemessaDto, clausulasWhere);
        
        if (!clausulasWhere.isEmpty()) {
        	sql += Util.WHERE + clausulasWhere;
        }

        SQLQuery sqlQuery = session.createSQLQuery(sql).addEntity(getPersistentClass());

        if (!textoFiltroDestinatario.isEmpty()) {
            DestinatarioListaRemessaDaoHibernate.adicionarParametrosClausulasSqlWhereVariosCampos(sqlQuery, Util.PERCENT + textoFiltroDestinatario + Util.PERCENT);
        }
        Util.setParametroLongQuery(sqlQuery, NOME_COLUNA_NUMERO_LISTA_REMESSA, pesquisaListaRemessaDto.getNumeroListaRemessa());
        Util.setParametroLongQuery(sqlQuery, NOME_COLUNA_ANO_LISTA_REMESSA, pesquisaListaRemessaDto.getAnoListaRemessa());
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

	private String inserirClausulaRemessaListaRemessa(PesquisaListaRemessaDto pesquisaListaRemessaDto, String clausulasWhere) {
		StringBuffer sb = new StringBuffer();
        if(pesquisaListaRemessaDto.getRemessasListaRemessa() != null && !pesquisaListaRemessaDto.getRemessasListaRemessa().trim().isEmpty()){
	        if(!clausulasWhere.isEmpty()){
	        	sb.append(" AND ");
	        }
	        sb.append(" (").append(aliasGenericoListaRemessa2).append(".").append(NOME_COLUNA_NUMERO_LISTA_REMESSA).append(",").append(aliasGenericoListaRemessa2).append(".").append(NOME_COLUNA_ANO_LISTA_REMESSA).append(") IN (");
	        String[] listas = pesquisaListaRemessaDto.getRemessasListaRemessa().trim().split(";");
	        int totalLista = listas.length;
	        int indiceLista = 0;
	        for(String lista : listas){
	        	indiceLista++;
	        	String[] parteLista = lista.trim().split("/");
	        	sb.append("(").append(parteLista[0]).append(",").append(parteLista[1]).append(")");
	        	if(indiceLista < totalLista){
	        		sb.append(",");
	        	}
	        }
	        sb.append(") ");
        }
        
        return clausulasWhere.concat(sb.toString());
	}
    
    private String checkedTrim(String string){
    	return string == null ? "" : string.trim();
    }
    
    private String avaliaCodigoRastreio(String sql, PesquisaListaRemessaDto pesquisaListaRemessaDto) {
		
		if (pesquisaListaRemessaDto.getCodigoRastreio() != null && !pesquisaListaRemessaDto.getCodigoRastreio().isEmpty()) {
	    	sql += " INNER JOIN EXPEDICAO.REMESSA_VOLUME " + aliasGenericoRemessaVolume;
	        sql += " ON (" + aliasGenerico + ".SEQ_REMESSA = " + aliasGenericoRemessaVolume + ".SEQ_REMESSA) ";
	    }
	
		return sql;
	}
    
    private String avaliaApenasEnviadas(boolean apenasEnviadas, String clausulasWhere) {
    	
    	if (apenasEnviadas) {
        	clausulasWhere = Util.inserirClausulaWhereAnd(clausulasWhere, Util.criarWhereNotNull(aliasGenericoListaRemessa, NOME_COLUNA_DATA_ENVIO));
        }
    	
    	return clausulasWhere;
    }
}