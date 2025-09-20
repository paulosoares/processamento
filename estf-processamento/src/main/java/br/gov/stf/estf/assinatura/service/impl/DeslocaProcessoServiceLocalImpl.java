package br.gov.stf.estf.assinatura.service.impl;

import br.gov.stf.estf.assinatura.dataaccess.DeslocaProcessoDaoLocal;
import br.gov.stf.estf.assinatura.service.DeslocaProcessoServiceLocal;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author roberio.fernandes
 */
@Service("deslocaProcessoServiceLocal")
public class DeslocaProcessoServiceLocalImpl implements DeslocaProcessoServiceLocal {

    @Autowired
    private DeslocaProcessoDaoLocal dao;

    @Override
    public void deslocarProcesso(Long idProcesso,
            Long codigoOrgaoSetorOrigem,
            Long codigoOrgaoSetorDestino,
            Integer tipoOrgaoSetorOrigem,
            Integer tipoOrgaoSetorDestino,
            boolean isDeslocamentoAutomatico,
            Long codigoAndamento) throws ServiceException {
        try {
            dao.deslocarProcesso(idProcesso,
                    codigoOrgaoSetorOrigem,
                    codigoOrgaoSetorDestino,
                    tipoOrgaoSetorOrigem,
                    tipoOrgaoSetorDestino,
                    isDeslocamentoAutomatico,
                    codigoAndamento);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }


    @Override
    public void deslocarProcessoApensos(Long idProcesso,
            Long codigoOrgaoSetorOrigem,
            Long codigoOrgaoSetorDestino,
            Integer tipoOrgaoSetorOrigem,
            Integer tipoOrgaoSetorDestino,
            boolean isDeslocamentoAutomatico,
            Long codigoAndamento) throws ServiceException {
        try {
            dao.deslocarProcessoApensos(idProcesso,
                    codigoOrgaoSetorOrigem,
                    codigoOrgaoSetorDestino,
                    tipoOrgaoSetorOrigem,
                    tipoOrgaoSetorDestino,
                    isDeslocamentoAutomatico,
                    codigoAndamento);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

}
