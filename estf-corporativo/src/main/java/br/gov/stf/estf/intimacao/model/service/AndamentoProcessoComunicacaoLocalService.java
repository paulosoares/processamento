package br.gov.stf.estf.intimacao.model.service;

import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.AndamentoProcessoComunicacao;
import br.gov.stf.framework.model.service.ServiceException;

import java.util.List;

public interface AndamentoProcessoComunicacaoLocalService {

    /**
     * Salva o andamento associado a comunicação
     *
     * @param andamentoProcessoComunicacao
     * @throws ServiceException
     */
    void salvar(AndamentoProcessoComunicacao andamentoProcessoComunicacao) throws ServiceException;

    List<AndamentoProcesso> pesquisarAndamentoProcesso(String sigla, Long numero) throws ServiceException;
    
    List<AndamentoProcesso> pesquisarAndamentosProcessoIncidente(Long idProcessoIncidente) throws ServiceException;
    
    AndamentoProcessoComunicacao recuperarAndamentoProcessoGeradoPelaComunicacao(Long idcomunicacao, Long idCodigoAndamento) throws ServiceException;
}