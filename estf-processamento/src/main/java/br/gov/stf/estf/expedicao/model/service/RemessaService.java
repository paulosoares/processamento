package br.gov.stf.estf.expedicao.model.service;

import java.util.List;

import br.gov.stf.estf.expedicao.entidade.Remessa;
import br.gov.stf.estf.expedicao.model.dataaccess.RemessaDao;
import br.gov.stf.estf.expedicao.model.util.PesquisaListaRemessaDto;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface RemessaService extends GenericService<Remessa, Long, RemessaDao> {

    /**
     * Realiza uma pesquisa baseada na "imagem" do registro informada, buscando
     * por todas as informações passadas na imagem nos registros do repositório,
     * cada um no seu respectivo campo, em qualquer posição deste.
     *
     * @param pesquisaRemessaDto {@link br.gov.stf.estf.expedicao.model.util.PesquisaListaRemessaDto }
     * @return 
     * @throws br.gov.stf.framework.model.service.ServiceException 
     */
    List<Remessa> pesquisar(PesquisaListaRemessaDto pesquisaListaRemessaDto) throws ServiceException;

    /**
     * Realiza uma pesquisa baseada na "imagem" do registro informada, buscando
     * por todas as informações passadas na imagem nos registros do repositório,
     * cada um no seu respectivo campo, em qualquer posição deste.
     * Retorna apenas remessas enviadas.
     *
     * @param pesquisaListaRemessaDto {@link br.gov.stf.estf.expedicao.model.util.PesquisaListaRemessaDto }
     * @return
     * @throws br.gov.stf.framework.model.service.ServiceException 
     */
    List<Remessa> pesquisarEnviadas(PesquisaListaRemessaDto pesquisaListaRemessaDto) throws ServiceException;
}