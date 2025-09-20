package br.gov.stf.estf.expedicao.model.service;

import java.util.List;

import br.gov.stf.estf.expedicao.entidade.DestinatarioListaRemessa;
import br.gov.stf.estf.expedicao.model.dataaccess.DestinatarioListaRemessaDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface DestinatarioListaRemessaService extends GenericService<DestinatarioListaRemessa, Long, DestinatarioListaRemessaDao> {

    /**
     * Realiza uma busca no reposit�rio por registros que contenham o texto
     * informado em qualquer posi��o de qualquer um dos campos abaixo: Descri��o
     * Anterior Descri��o Principal Descri��o Posterior Descri��o agrupador
     * Descri��o codigoOrigem
     *
     * @param texto {@link String }
     * @return
     * @throws br.gov.stf.framework.model.service.ServiceException
     */
    List<DestinatarioListaRemessa> pesquisarVariosCampos(String texto) throws ServiceException;

    /**
     * Realiza uma pesquisa baseada na "imagem" do registro informada, buscando
     * por todas as informa��es passadas na imagem nos registros do reposit�rio,
     * cada um no seu respectivo campo, em qualquer posi��o deste.
     *
     * @param destinatario {@link br.gov.stf.estf.expedicao.entidade.DestinatarioListaRemessa }
     * @param siglaUf
     *
     * @return
     * @throws br.gov.stf.framework.model.service.ServiceException
     */
    List<DestinatarioListaRemessa> pesquisar(DestinatarioListaRemessa destinatario, String siglaUf) throws ServiceException;

    /**
     * Cria um objeto do tipo {@link br.gov.stf.estf.expedicao.entidade.DestinatarioListaRemessa } preenchido
     * com os dados do registro cujo ID seja o informado.
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    DestinatarioListaRemessa copiar(Long id) throws ServiceException;

    /**
     * Cria um objeto do tipo {@link br.gov.stf.estf.expedicao.entidade.DestinatarioListaRemessa } preenchido
     * com os dados do objeto informado.
     *
     * @param destinatarioListaRemessa
     * @return
     * @throws ServiceException
     */
    DestinatarioListaRemessa copiar(DestinatarioListaRemessa destinatarioListaRemessa) throws ServiceException;
}