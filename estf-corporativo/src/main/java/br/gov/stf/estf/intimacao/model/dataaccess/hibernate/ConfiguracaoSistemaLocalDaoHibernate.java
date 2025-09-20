package br.gov.stf.estf.intimacao.model.dataaccess.hibernate;

import br.gov.stf.estf.configuracao.model.dataaccess.ConfiguracaoSistemaDao;
import br.gov.stf.estf.entidade.configuracao.ConfiguracaoSistema;
import br.gov.stf.estf.intimacao.model.dataaccess.ConfiguracaoSistemaLocalDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ConfiguracaoSistemaLocalDaoHibernate extends GenericHibernateDao<ConfiguracaoSistema, Long> implements ConfiguracaoSistemaLocalDao {

    public ConfiguracaoSistemaLocalDaoHibernate() {
        super(ConfiguracaoSistema.class);
    }

    public static final long serialVersionUID = 1L;

    @Autowired
    private ConfiguracaoSistemaDao configuracaoSistemaDao;

    @Override
    public ConfiguracaoSistema salvarValor(String siglaSistema, String chave, String valor) throws DaoException {
        Session session = retrieveSession();
        session.flush();
        ConfiguracaoSistema configuracaoSistema = configuracaoSistemaDao.recuperarValor(siglaSistema, chave);
        if (configuracaoSistema == null) {
            configuracaoSistema = new ConfiguracaoSistema();
            Long id = getMaxSeqConfiguracaoSistema();
            id++;
            configuracaoSistema.setId(id);
            configuracaoSistema.setSiglaSistema(siglaSistema);
            configuracaoSistema.setChave(chave);
        }
        configuracaoSistema.setValor(valor);

        try {
            session.saveOrUpdate(configuracaoSistema);
        } catch (Exception e) {
            throw new DaoException(e.getMessage(), e);
        }

        return configuracaoSistema;
    }

    private Long getMaxSeqConfiguracaoSistema() throws DaoException {
        Session session = retrieveSession();
        try {
            String hql = "SELECT MAX(cs.id) FROM ConfiguracaoSistema cs";
            Query query = session.createQuery(hql);
            return (Long) query.uniqueResult();
        } catch (HibernateException e) {
            throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
        } catch (RuntimeException e) {
            throw new DaoException("RuntimeException", e);
        }
    }
}
