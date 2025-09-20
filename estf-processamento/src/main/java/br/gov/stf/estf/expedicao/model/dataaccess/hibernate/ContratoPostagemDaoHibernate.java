package br.gov.stf.estf.expedicao.model.dataaccess.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.expedicao.entidade.ContratoPostagem;
import br.gov.stf.estf.expedicao.model.dataaccess.ContratoPostagemDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ContratoPostagemDaoHibernate extends GenericHibernateDao<ContratoPostagem, Long> implements ContratoPostagemDao {

    public static final long serialVersionUID = 1L;

    public static final String NOME_ATRIBUTO_DATA_VIGENCIA_INICIAL = "dataVigenciaInicial";
    public static final String NOME_ATRIBUTO_DATA_VIGENCIA_FINAL = "dataVigenciaFinal";

    public final String aliasGenerico = Util.gerarAliasGenerico(getPersistentClass());
    public final String selectGenerico = Util.gerarSelectHqlGenerico(getPersistentClass(), aliasGenerico);

    public ContratoPostagemDaoHibernate() {
    	super(ContratoPostagem.class);
    }

	@Override
	public List<ContratoPostagem> listarContratosSemDataVigencia() throws DaoException {
        Session session = retrieveSession();
		String hql = selectGenerico;

	    // WHERE
	    String clausulasWhere = Util.criarWhereNull(aliasGenerico, NOME_ATRIBUTO_DATA_VIGENCIA_FINAL);

    	hql += Util.WHERE + clausulasWhere;

    	Query query = session.createQuery(hql);

    	return (List<ContratoPostagem>) query.list();
	}

	@Override
	public List<ContratoPostagem> listarContratosEncerrados() throws DaoException {
        Session session = retrieveSession();
		String hql = selectGenerico;

	    // WHERE
	    String clausulasWhere = Util.criarWhereNotNull(aliasGenerico, NOME_ATRIBUTO_DATA_VIGENCIA_FINAL);

    	hql += Util.WHERE + clausulasWhere;

    	Query query = session.createQuery(hql);

		return (List<ContratoPostagem>) query.list();
	}

	@Override
	public List<ContratoPostagem> listar() throws DaoException {
		return super.pesquisar();
	}

	@Override
	public List<ContratoPostagem> listarIniciadosApos(Date data) throws DaoException {
        Session session = retrieveSession();
		String hql = selectGenerico;

	    // WHERE
	    String clausulasWhere = Util.criarWhereMaiorQueHqlComParametro(true, aliasGenerico, NOME_ATRIBUTO_DATA_VIGENCIA_INICIAL, data);

    	hql += Util.WHERE + clausulasWhere;

    	Query query = session.createQuery(hql);

    	Util.setParametroTimestampQuery(query, NOME_ATRIBUTO_DATA_VIGENCIA_INICIAL, data, AjusteDataEnum.PRIMEIRA_HORA_DIA);

    	List<ContratoPostagem> lista = (List<ContratoPostagem>) query.list();
    	return lista;
	}

	public ContratoPostagem buscarContratoVigenteEm(Date data) throws DaoException {
        Session session = retrieveSession();
		String hql = selectGenerico;

	    // WHERE
	    String clausulasDataInicio = Util.criarWhereMenorQueHqlComParametro(true, aliasGenerico, NOME_ATRIBUTO_DATA_VIGENCIA_INICIAL, data);
	    String clausulasDataFim1 = Util.criarWhereNull(aliasGenerico, NOME_ATRIBUTO_DATA_VIGENCIA_FINAL);
	    String clausulasDataFim2 = Util.criarWhereMaiorQueHqlComParametro(true, aliasGenerico, NOME_ATRIBUTO_DATA_VIGENCIA_FINAL, data);
	    String clausulasWhere = clausulasDataInicio + " AND (" + clausulasDataFim1 + " OR " + clausulasDataFim2 + ")";

    	hql += Util.WHERE + clausulasWhere;

    	Query query = session.createQuery(hql);

    	Util.setParametroTimestampQuery(query, NOME_ATRIBUTO_DATA_VIGENCIA_INICIAL, data, AjusteDataEnum.ULTIMA_HORA_DIA);
    	Util.setParametroTimestampQuery(query, NOME_ATRIBUTO_DATA_VIGENCIA_FINAL, data, AjusteDataEnum.PRIMEIRA_HORA_DIA);

    	List<ContratoPostagem> lista = (List<ContratoPostagem>) query.list();
    	ContratoPostagem contratoPostagem = null;
    	if (!lista.isEmpty()) {
    		if (lista.size() > 1) {
    			throw new DaoException("Mais de um contrato vigente encontrado na data informada.");
    		}
    		contratoPostagem = lista.get(0);
    	}
		return contratoPostagem;
	}
}