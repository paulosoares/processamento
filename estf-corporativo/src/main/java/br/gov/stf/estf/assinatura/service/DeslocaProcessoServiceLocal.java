package br.gov.stf.estf.assinatura.service;

import br.gov.stf.framework.model.service.ServiceException;

/**
 *
 * @author roberio.fernandes
 */
public interface DeslocaProcessoServiceLocal {

    void deslocarProcesso(Long idProcesso,
            Long codigoOrgaoSetorOrigem,
            Long codigoOrgaoSetorDestino,
            Integer tipoOrgaoSetorOrigem,
            Integer tipoOrgaoSetorDestino,
            boolean isDeslocamentoAutomatico,
            Long codigoAndamento) throws ServiceException;
}
