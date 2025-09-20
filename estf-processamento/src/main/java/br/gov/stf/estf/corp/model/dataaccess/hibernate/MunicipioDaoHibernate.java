package br.gov.stf.estf.corp.model.dataaccess.hibernate;

import java.util.List;

import javax.persistence.NonUniqueResultException;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.corp.entidade.Municipio;
import br.gov.stf.estf.corp.model.dataaccess.MunicipioDao;
import br.gov.stf.estf.expedicao.entidade.ValorFlagSimNaoEnum;
import br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class MunicipioDaoHibernate extends GenericHibernateDao<Municipio, String> implements MunicipioDao {

    public static final long serialVersionUID = 1L;

    public static final String TIPO_MUNICIPIO = "M";
    public final String aliasGenerico = Util.gerarAliasGenerico(getPersistentClass());
    public final String selectGenerico = Util.gerarSelectHqlGenerico(getPersistentClass(), aliasGenerico);

    public MunicipioDaoHibernate() {
    	super(Municipio.class);
    }

    @Override
    public List<Municipio> listarAtivos() throws DaoException {
        return listarAtivos("");
    }

    @Override
    public List<Municipio> listarAtivos(String siglaUf) throws DaoException {
    	return listarAtivos(siglaUf, null, null);
    }

    @Override
    public List<Municipio> listarAtivosTipoMunicipio(String siglaUf) throws DaoException {
    	return listarAtivos(siglaUf, null, TIPO_MUNICIPIO);
    }

	@Override
	public Municipio listarAtivo(String siglaUf, String nome) throws DaoException {
		List<Municipio> municipios = listarAtivos(siglaUf, nome, TIPO_MUNICIPIO);
		if (municipios.size() != 1) {
			throw new NonUniqueResultException();
		}
        return municipios.get(0);
	}

	@Override
	public Municipio listarAtivoTipoMunicipio(String siglaUf, String nome) throws DaoException {
		List<Municipio> municipios = listarAtivos(siglaUf, nome, TIPO_MUNICIPIO);
		if (municipios.size() != 1) {
			throw new NonUniqueResultException();
		}
        return municipios.get(0);
	}

	private List<Municipio> listarAtivos(String siglaUf, String nome, String tipo) throws DaoException {
        Session session = retrieveSession();
        String hql = selectGenerico;

        // WHERE
        String nomeAtributoAtivo = "ativo";
        String nomeAtributoSiglaUf = "siglaUf";
        String nomeAtributoNome = "nome";
        String nomeAtributoTipo = "tipo";

        String clausulas = Util.WHERE + Util.criarWhereEqualHqlComParametro(aliasGenerico, nomeAtributoAtivo, true, false);
        if (siglaUf != null) {
        	clausulas = Util.inserirClausulaWhereAnd(clausulas, Util.criarWhereLikeHqlComParametro(aliasGenerico, nomeAtributoSiglaUf, true, false), siglaUf);
        }
        if (nome != null) {
        	clausulas = Util.inserirClausulaWhereAnd(clausulas, Util.criarWhereLikeHqlComParametro(aliasGenerico, nomeAtributoNome, true, false), nome);
        }
        if (tipo != null) {
        	clausulas = Util.inserirClausulaWhereAnd(clausulas, Util.criarWhereLikeHqlComParametro(aliasGenerico, nomeAtributoTipo, true, false), tipo);
        }
        hql += clausulas;

        hql += " ORDER BY " + aliasGenerico + Util.PONTO + nomeAtributoNome;

        Query query = session.createQuery(hql);

        query.setString(nomeAtributoAtivo, ValorFlagSimNaoEnum.buscar(true).getValorTexto());
        if (siglaUf != null) {
	        query.setString(nomeAtributoSiglaUf, siglaUf.trim());
        }
        if (nome != null) {
	        query.setString(nomeAtributoNome, nome.trim());
        }
        if (tipo != null) {
	        query.setString(nomeAtributoTipo, tipo.trim());
        }

        return (List<Municipio>) query.list();
	}
}