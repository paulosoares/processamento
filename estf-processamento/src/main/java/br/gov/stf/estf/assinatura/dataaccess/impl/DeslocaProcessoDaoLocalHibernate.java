package br.gov.stf.estf.assinatura.dataaccess.impl;

import br.gov.stf.estf.assinatura.dataaccess.DeslocaProcessoDaoLocal;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.HibernateDao;
import java.sql.CallableStatement;
import java.sql.SQLException;
import org.hibernate.engine.SessionImplementor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author roberio.fernandes
 */
@Repository
public class DeslocaProcessoDaoLocalHibernate extends HibernateDao implements DeslocaProcessoDaoLocal {

    private static final String CODIGO_DESLOCA_INCIDENTE = "{call JUDICIARIO.PRC_DESLOCA_INCIDENTE (?, ?, ?, ?, ?, ?, ?)}";

    private static final String CODIGO_DESLOCA_INCIDENTE_APENSOS = "{call JUDICIARIO.PRC_DESLOCA_INCIDENTE_APENSOS (?, ?, ?, ?, ?, ?, ?)}";

    @Override
    public void deslocarProcesso(Long idProcesso,
            Long codigoOrgaoSetorOrigem,
            Long codigoOrgaoSetorDestino,
            Integer tipoOrgaoSetorOrigem,
            Integer tipoOrgaoSetorDestino,
            boolean isDeslocamentoAutomatico,
            Long codigoAndamento) throws DaoException {
        try {
            CallableStatement stmt = montaChamadaDaProcedure(CODIGO_DESLOCA_INCIDENTE);

            stmt.setLong(1, codigoOrgaoSetorOrigem);
            stmt.setLong(2, codigoOrgaoSetorDestino);
            stmt.setString(3, tipoOrgaoSetorOrigem.toString());
            stmt.setString(4, tipoOrgaoSetorDestino.toString());
            stmt.setLong(5, idProcesso);
            stmt.setString(6, isDeslocamentoAutomatico ? "S" : "N");
            if(codigoAndamento != null) {
            	stmt.setLong(7, codigoAndamento);
            }else {
            	stmt.setString(7, null);
            }

            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void deslocarProcessoApensos(Long idProcesso,
            Long codigoOrgaoSetorOrigem,
            Long codigoOrgaoSetorDestino,
            Integer tipoOrgaoSetorOrigem,
            Integer tipoOrgaoSetorDestino,
            boolean isDeslocamentoAutomatico,
            Long codigoAndamento) throws DaoException {
        try {
            CallableStatement stmt = montaChamadaDaProcedure(CODIGO_DESLOCA_INCIDENTE_APENSOS);

            stmt.setLong(1, codigoOrgaoSetorOrigem);
            stmt.setLong(2, codigoOrgaoSetorDestino);
            stmt.setString(3, tipoOrgaoSetorOrigem.toString());
            stmt.setString(4, tipoOrgaoSetorDestino.toString());
            stmt.setLong(5, idProcesso);
            stmt.setString(6, isDeslocamentoAutomatico ? "S" : "N");
            stmt.setLong(7, codigoAndamento);

            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private CallableStatement montaChamadaDaProcedure(String codigoDaProcedure)
            throws DaoException, SQLException {
        SessionImplementor session = (SessionImplementor) retrieveSession();
        CallableStatement stmt = session.getBatcher().prepareCallableStatement(
                codigoDaProcedure);
        return stmt;
    }
}
