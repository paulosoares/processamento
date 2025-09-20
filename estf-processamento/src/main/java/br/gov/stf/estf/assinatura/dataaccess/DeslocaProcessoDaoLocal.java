package br.gov.stf.estf.assinatura.dataaccess;

import br.gov.stf.framework.model.dataaccess.DaoException;

/**
 *
 * @author roberio.fernandes
 */
public interface DeslocaProcessoDaoLocal {

    void deslocarProcesso(Long idProcesso,
            Long codigoOrgaoSetorOrigem,
            Long codigoOrgaoSetorDestino,
            Integer tipoOrgaoSetorOrigem,
            Integer tipoOrgaoSetorDestino,
            boolean isDeslocamentoAutomatico,
            Long codigoAndamento) throws DaoException;

	void deslocarProcessoApensos(Long idProcesso, 
			Long codigoOrgaoSetorOrigem,
			Long codigoOrgaoSetorDestino, 
			Integer tipoOrgaoSetorOrigem,
			Integer tipoOrgaoSetorDestino, 
			boolean isDeslocamentoAutomatico,
			Long codigoAndamento) throws DaoException;
}
