package br.gov.stf.estf.intimacao.model.dataaccess.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.processostf.ModeloComunicacaoEnum;
import br.gov.stf.estf.intimacao.model.dataaccess.ModeloComunicacaoLocalDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ModeloComunicacaoLocalDaoHibernate extends GenericHibernateDao<ModeloComunicacao, Long> implements ModeloComunicacaoLocalDao {

    public ModeloComunicacaoLocalDaoHibernate() {
        super(ModeloComunicacao.class);
    }

    public static final long serialVersionUID = 1L;

    @Override
    public ModeloComunicacao buscar(String descricaoTipoComunicacao, String descricaoModelo) throws DaoException {
        ModeloComunicacao modeloDocumento;
        Session session = retrieveSession();
        try {
            String hql = "SELECT mc FROM ModeloComunicacao mc "
                    + "INNER JOIN mc.tipoComunicacao tc "
                    + "WHERE mc.dscModelo = :descricaoModelo "
                    + "AND tc.descricao = :descricaoTipoComunicacao";
            Query query = session.createQuery(hql);
            query.setString("descricaoModelo", descricaoModelo);
            query.setString("descricaoTipoComunicacao", descricaoTipoComunicacao);

            modeloDocumento = (ModeloComunicacao) query.uniqueResult();
        } catch (HibernateException e) {
            throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
        } catch (RuntimeException e) {
            throw new DaoException("RuntimeException", e);
        }
        return modeloDocumento;
    }

    @Override
    public ModeloComunicacao buscar(ModeloComunicacaoEnum modeloComunicacaoEnum) throws DaoException {
        return buscar(modeloComunicacaoEnum.getDescricaoTipoComunicacao(), modeloComunicacaoEnum.getDescricaoModelo());
    }
}
