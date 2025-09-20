package br.gov.stf.estf.expedicao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.expedicao.entidade.DestinatarioListaRemessa;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface DestinatarioListaRemessaDao extends GenericDao<DestinatarioListaRemessa, Long> {

    /**
     * Realiza uma busca no reposit�rio por registros que contenham o texto
     * informado em qualquer posi��o de qualquer um dos campos abaixo: Descri��o
     * Anterior, Descri��o Principal, Descri��o Posterior, Agrupador e C�digo Origem
     *
     * @param texto {@link String }
     * @return
     * @throws br.gov.stf.framework.model.dataaccess.DaoException
     */
    List<DestinatarioListaRemessa> pesquisarVariosCampos(String texto) throws DaoException;

    /**
     * Realiza uma pesquisa baseada na "imagem" do registro informada, buscando
     * por todas as informa��es passadas na imagem nos registros do reposit�rio,
     * cada um no seu respectivo campo, em qualquer posi��o deste.
     *
     * @param destinatario {@link br.gov.stf.estf.expedicao.entidade.DestinatarioListaRemessa
     * @param siglaUf {@link String }
     * }
     * @return
     * @throws br.gov.stf.framework.model.dataaccess.DaoException
     */
    List<DestinatarioListaRemessa> pesquisar(DestinatarioListaRemessa destinatario, String siglaUf) throws DaoException;
}