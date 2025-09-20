package br.gov.stf.estf.intimacao.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.intimacao.model.dataaccess.TipoMeioIntimacaoEnum;
import br.gov.stf.estf.intimacao.visao.dto.ParteIntimacaoDto;
import br.gov.stf.estf.intimacao.visao.dto.ParteProcessoIntimacaoDto;
import br.gov.stf.framework.model.service.ServiceException;

public interface PartesGerarIntimacaoLocalService {

    /**
     * Lista as partes que constarem no diário de justiça na data informada.
     *
     * @param dataPublicacaoDj
     * @param tipoMeioIntimacaoProcesso
     * @param intimacaoRealizada
     * @param representanteComPrerrogIntPess
     * @param tipoMeioIntimacaoRepresentanteParte
     * @return
     * @throws ServiceException
     */
    List<ParteIntimacaoDto> listarPartes(Date dataPublicacaoDj,
            TipoMeioIntimacaoEnum tipoMeioIntimacaoProcesso,
            Boolean intimacaoRealizada,
            Boolean representanteComPrerrogIntPess,
            TipoMeioIntimacaoEnum tipoMeioIntimacaoRepresentanteParte) throws ServiceException;
    
    List<ParteProcessoIntimacaoDto> listarPartes(Date dataPublicacao, TipoMeioIntimacaoEnum tipoMeioComunicacaoEnum) throws ServiceException;


    /**
     * Lista a Unidade Federativa de uma parte.
     *
     * @param seqPessoa
     * @return
     * @throws ServiceException
     */
    List<String> obterUFParte(long seqPessoa) throws ServiceException;

    /**
     * Descrição da classe do processo.
     *
     * @param siglaClasseProcesso
     * @return
     * @throws ServiceException
     */
    String obterClasseProcesso(String siglaClasseProcesso) throws ServiceException;

    /**
     * Lista o Setor conforme classe informada.
     *
     * @param sigClasse
     * @param numProcesso
     * @return
     * @throws ServiceException
     */
    Setor obterSetor(String sigClasse, long numProcesso) throws ServiceException;
}