package br.gov.stf.estf.corp.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.corp.entidade.Logradouro;
import br.gov.stf.estf.corp.model.dataaccess.LogradouroDao;
import br.gov.stf.estf.expedicao.entidade.ValorFlagSimNaoEnum;
import br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class LogradouroDaoHibernate extends GenericHibernateDao<Logradouro, Long> implements LogradouroDao {

    public static final long serialVersionUID = 1L;

    public final String aliasGenerico = Util.gerarAliasGenerico(getPersistentClass());
    public final String selectGenerico = Util.gerarSelectHqlGenerico(getPersistentClass(), aliasGenerico);

    public LogradouroDaoHibernate() {
    	super(Logradouro.class);
    }

    @Override
    public List<Logradouro> pesquisarPeloCep(String cep) throws DaoException {
        String numeroCep = cep + "";

        Session session = retrieveSession();
        String hql = selectGenerico;

        // WHERE
        String nomeAtributoCep = "cep";
        String nomeAtributoNome = "nome";

        String clausulas = "";
        if (!numeroCep.isEmpty()) {
        	clausulas = Util.WHERE + Util.inserirClausulaWhereAnd(clausulas, Util.criarWhereLikeHqlComParametro(aliasGenerico, nomeAtributoCep, true, false), numeroCep);
        }
        hql += clausulas;

        hql += " ORDER BY " + aliasGenerico + Util.PONTO + nomeAtributoNome;

        Query query = session.createQuery(hql);

        query.setString(nomeAtributoCep, ValorFlagSimNaoEnum.buscar(true).getValorTexto());
        if (!numeroCep.isEmpty()) {
            query.setString(nomeAtributoCep, numeroCep);
        }

        return (List<Logradouro>) query.list();
    }
}