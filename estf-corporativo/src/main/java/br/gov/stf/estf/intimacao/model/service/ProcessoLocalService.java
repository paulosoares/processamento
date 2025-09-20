package br.gov.stf.estf.intimacao.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.framework.model.service.ServiceException;

/**
 *
 * @author Roberio.Fernandes
 */
public interface ProcessoLocalService {

    /**
     * Recupera do repositório todos os processos que sejam eletrônicos, buscando pela classe, número
     * e setor informados.
     *
     * @param classe
     * @param numero
     * @param idSetor
     * @return
     * @throws ServiceException 
     */
    Processo recuperarProcessoEletronico(String classe, Long numero, Long idSetor) throws ServiceException;
    
    List<ObjetoIncidente<?>> recuperarIncidentesDoProcessoEletronico(Long idObjetoIncidentePrincipal) throws ServiceException;
    
    Processo recuperarProcessoEletronicoPorIdProcessoPrincipal(Long idObjetoIncidentePrincipal) throws ServiceException;
}