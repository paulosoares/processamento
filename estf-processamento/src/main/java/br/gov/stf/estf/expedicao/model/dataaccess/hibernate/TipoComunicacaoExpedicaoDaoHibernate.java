package br.gov.stf.estf.expedicao.model.dataaccess.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.expedicao.entidade.TipoComunicacaoExpedicao;
import br.gov.stf.estf.expedicao.model.dataaccess.TipoComunicacaoExpedicaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoComunicacaoExpedicaoDaoHibernate extends GenericHibernateDao<TipoComunicacaoExpedicao, Long> implements TipoComunicacaoExpedicaoDao {

    public static final long serialVersionUID = 1L;

    public final String aliasGenerico = Util.gerarAliasGenerico(getPersistentClass());
    public final String selectGenerico = Util.gerarSelectHqlGenerico(getPersistentClass(), aliasGenerico);

    public TipoComunicacaoExpedicaoDaoHibernate() {
    	super(TipoComunicacaoExpedicao.class);
    }

	@Override
	public List<TipoComunicacaoExpedicao> listarTiposComunicacao() throws DaoException {
		return pesquisar();
	}
}