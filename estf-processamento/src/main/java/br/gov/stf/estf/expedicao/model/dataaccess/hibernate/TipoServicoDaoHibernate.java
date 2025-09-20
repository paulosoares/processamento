package br.gov.stf.estf.expedicao.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.expedicao.entidade.TipoServico;
import br.gov.stf.estf.expedicao.entidade.ValorFlagSimNaoEnum;
import br.gov.stf.estf.expedicao.model.dataaccess.TipoServicoDao;
import br.gov.stf.estf.expedicao.model.util.TipoServicoEnum;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoServicoDaoHibernate extends GenericHibernateDao<TipoServico, Long> implements TipoServicoDao {

    public static final long serialVersionUID = 1L;

    public final String aliasGenerico = Util.gerarAliasGenerico(getPersistentClass());
    public final String selectGenerico = Util.gerarSelectHqlGenerico(getPersistentClass(), aliasGenerico);

    public TipoServicoDaoHibernate() {
    	super(TipoServico.class);
    }

    @Override
    public List<TipoServico> listarTiposServicoPorTipoEntrega(boolean isEntregaCorreios, TipoServicoEnum tipoServico) throws DaoException {
        Session session = retrieveSession();
        String hql = selectGenerico;

        // WHERE
        String nomeAtributoAtivo = "ativo";
        String nomeAtributoServicoCorreios = "servicoCorreios";
        String nomeAtributoTipoServicoCorreios = "tipoServicoCorreios";

        String clausulas = Util.WHERE + Util.criarWhereEqualHqlComParametro(aliasGenerico, nomeAtributoAtivo, true, false);
        clausulas = Util.inserirClausulaWhereAnd(clausulas, Util.criarWhereEqualHqlComParametro(aliasGenerico, nomeAtributoServicoCorreios, true, false));
        if (tipoServico != null) {
            clausulas = Util.inserirClausulaWhereAnd(clausulas, Util.criarWhereEqualHqlComParametro(aliasGenerico, nomeAtributoTipoServicoCorreios, true, false));
        }
        hql += clausulas;

        Query query = session.createQuery(hql);

        query.setString(nomeAtributoAtivo, ValorFlagSimNaoEnum.buscar(true).getValorTexto());
        query.setString(nomeAtributoServicoCorreios, ValorFlagSimNaoEnum.buscar(isEntregaCorreios).getValorTexto());
        if (tipoServico != null) {
            query.setString(nomeAtributoTipoServicoCorreios, tipoServico.getCodigo());
        }

        return (List<TipoServico>) query.list();
    }
}