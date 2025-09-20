package br.gov.stf.estf.intimacao.model.dataaccess.hibernate;

import br.gov.stf.estf.entidade.documento.TipoComunicacao;
import br.gov.stf.estf.intimacao.model.dataaccess.TipoComunicacaoLocalDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
public class TipoComunicacaoLocalDaoHibernate extends GenericHibernateDao<TipoComunicacao, Long> implements TipoComunicacaoLocalDao {

    public static final long serialVersionUID = 1L;

    private static final Log LOG = LogFactory.getLog(TipoComunicacaoLocalDaoHibernate.class);

    public TipoComunicacaoLocalDaoHibernate() {
        super(TipoComunicacao.class);
    }

    @Override
    public Long gerarProximoNumeroComunicacao(Long idTipoComunicacao) throws DaoException {

        final String nomeFuncaoGeraNumeroLista = "{? = call JUDICIARIO.FNC_GERA_NUMERO_COMUNICACAO(?)}";
        Session session = retrieveSession();
        Connection connection = session.connection();
        CallableStatement statement = null;
        try {
            statement = connection.prepareCall(nomeFuncaoGeraNumeroLista);
            statement.registerOutParameter(1, java.sql.Types.DOUBLE);
            statement.setLong(2, idTipoComunicacao);

            statement.executeUpdate();
            long numeroComunicacao = statement.getLong(1);
            return numeroComunicacao;
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    LOG.error("Erro ao fechar objeto statement.", ex);
                }
            }
        }
    }
}
