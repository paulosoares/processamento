package br.gov.stf.estf.intimacao.model.dataaccess;

import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.processostf.TipoIncidentePreferencia;
import br.gov.stf.estf.intimacao.model.vo.TipoRecebimentoComunicacaoEnum;
import br.gov.stf.estf.intimacao.visao.dto.ComunicacaoExternaDTO;
import br.gov.stf.framework.model.dataaccess.DaoException;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Roberio.Fernandes
 */
public interface ComunicacaoLocalDao {

    /**
     * Realiza uma consulta de comunicações, filtrando pelos parâmetros
     * informados. Caso algum parâmetro não seja informado, este será ignorado
     * como filtro.
     *
     * @param idParte
     * @param tipoRecebimentoComunicacaoEnum
     * @param descricaoTipoComunicacao
     * @param descricaoModelo
     * @param periodoEnvioInicio
     * @param periodoEnvioFim
     * @param idProcesso
     * @return
     * @throws DaoException
     */
    List<ComunicacaoExternaDTO> buscar(String idParte,
            TipoRecebimentoComunicacaoEnum tipoRecebimentoComunicacaoEnum,
            String descricaoTipoComunicacao,
            String descricaoModelo,
            Date periodoEnvioInicio,
            Date periodoEnvioFim,
            Long idProcesso,
            Long idPreferemcia) throws DaoException;
    
    public List<TipoIncidentePreferencia> buscaTodasPreferencias() throws DaoException;
    
    Long recuperarCodigoOrigemDestinatario(Long idComunicacao) throws DaoException;
    
    @Deprecated
    List<Comunicacao> buscar(Long idParte,
            // todo: falta adicionar o filtro de situação
            Long idTipoComunicacao,
            Date periodoEnvioInicio,
            Date periodoEnvioFim,
            Long idProcesso);    
}