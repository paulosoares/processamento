package br.gov.stf.estf.intimacao.model.service.exception;

import br.gov.stf.framework.model.service.ServiceException;

/**
 *
 * @author Roberio.Fernandes
 */
public class PessoaNaoHabilitadaParaIntimacaoEletronicaException extends ServiceException {

    public PessoaNaoHabilitadaParaIntimacaoEletronicaException(String msg) {
        super(msg);
    }

    public PessoaNaoHabilitadaParaIntimacaoEletronicaException() {
        this("A pessoa informada não está apta a receber intimação eletronica.");
    }
}