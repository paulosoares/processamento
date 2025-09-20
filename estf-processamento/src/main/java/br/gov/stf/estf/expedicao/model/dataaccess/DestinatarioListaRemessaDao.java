package br.gov.stf.estf.expedicao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.expedicao.entidade.DestinatarioListaRemessa;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface DestinatarioListaRemessaDao extends GenericDao<DestinatarioListaRemessa, Long> {

    /**
     * Realiza uma busca no repositório por registros que contenham o texto
     * informado em qualquer posição de qualquer um dos campos abaixo: Descrição
     * Anterior, Descrição Principal, Descrição Posterior, Agrupador e Código Origem
     *
     * @param texto {@link String }
     * @return
     * @throws br.gov.stf.framework.model.dataaccess.DaoException
     */
    List<DestinatarioListaRemessa> pesquisarVariosCampos(String texto) throws DaoException;

    /**
     * Realiza uma pesquisa baseada na "imagem" do registro informada, buscando
     * por todas as informações passadas na imagem nos registros do repositório,
     * cada um no seu respectivo campo, em qualquer posição deste.
     *
     * @param destinatario {@link br.gov.stf.estf.expedicao.entidade.DestinatarioListaRemessa
     * @param siglaUf {@link String }
     * }
     * @return
     * @throws br.gov.stf.framework.model.dataaccess.DaoException
     */
    List<DestinatarioListaRemessa> pesquisar(DestinatarioListaRemessa destinatario, String siglaUf) throws DaoException;
}