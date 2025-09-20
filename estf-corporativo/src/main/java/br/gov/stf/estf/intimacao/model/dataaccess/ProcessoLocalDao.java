package br.gov.stf.estf.intimacao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.framework.model.dataaccess.DaoException;

/**
 *
 * @author Roberio.Fernandes
 */
public interface ProcessoLocalDao {

    /**
     * Recupera do repositório todos os processos que sejam eletrônicos, buscando pela classe, número
     * e setor informados.
     *
     * @param classe
     * @param numero
     * @param idSetor
     * @return
     * @throws DaoException 
     */
    Processo recuperarProcessoEletronico(String classe, Long numero, Long idSetor) throws DaoException;
    
    List<ObjetoIncidente<?>> recuperarIncidentesDoProcessoEletronico(Long idObjetoIncidentePrincipal) throws DaoException;
    
    Processo recuperarProcessoEletronicoPorIdProcessoPrincipal(Long idObjetoIncidentePrincipal) throws DaoException;
}