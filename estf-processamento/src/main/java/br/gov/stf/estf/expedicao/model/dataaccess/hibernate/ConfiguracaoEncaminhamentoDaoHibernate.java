package br.gov.stf.estf.expedicao.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.expedicao.entidade.ConfiguracaoEncaminhamento;
import br.gov.stf.estf.expedicao.model.dataaccess.ConfiguracaoEncaminhamentoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ConfiguracaoEncaminhamentoDaoHibernate extends GenericHibernateDao<ConfiguracaoEncaminhamento, Long> implements ConfiguracaoEncaminhamentoDao {

    public static final long serialVersionUID = 1L;

    public static final String NOME_CODIGO_ANDAMENTO = "codigoAndamento";

    public final String aliasGenerico = Util.gerarAliasGenerico(getPersistentClass());
    public final String selectGenerico = Util.gerarSelectHqlGenerico(getPersistentClass(), aliasGenerico);

    public ConfiguracaoEncaminhamentoDaoHibernate() {
    	super(ConfiguracaoEncaminhamento.class);
    }

    @Override
    public List<ConfiguracaoEncaminhamento> listar() throws DaoException {
    	return super.pesquisar();
    }

    @Override
    public ConfiguracaoEncaminhamento buscar(Long codigo) throws DaoException {
        Session session = retrieveSession();
        String hql = selectGenerico;

        // WHERE
        String clausulas = Util.inserirClausulaWhereAnd("", Util.criarWhereLikeHqlComParametro(aliasGenerico, NOME_CODIGO_ANDAMENTO, true, false), codigo);
        if (!clausulas.isEmpty()) {
        	clausulas = Util.WHERE + clausulas;
        }
        hql += clausulas;

        hql += " ORDER BY " + aliasGenerico + Util.PONTO + NOME_CODIGO_ANDAMENTO;

        Query query = session.createQuery(hql);

        query.setLong(NOME_CODIGO_ANDAMENTO, codigo);

        List<ConfiguracaoEncaminhamento> lista = (List<ConfiguracaoEncaminhamento>) query.list();
        ConfiguracaoEncaminhamento resultado = null;
        if (!lista.isEmpty()) {
        	resultado = lista.get(0);
        }

        return resultado;
    }
}