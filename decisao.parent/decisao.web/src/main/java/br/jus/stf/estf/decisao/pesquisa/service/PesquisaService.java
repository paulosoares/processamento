package br.jus.stf.estf.decisao.pesquisa.service;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.usuario.ConfiguracaoUsuario;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.ComunicacaoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ListaIncidentesDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ListaTextosDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.pesquisa.web.PesquisaXMLBind;
import br.jus.stf.estf.decisao.support.controller.faces.datamodel.PagedList;
import br.jus.stf.estf.decisao.support.security.Principal;

/**
 * Interface DAO para pesquisa avan�ada de informa��es de Textos, 
 * Incidentes (Processos, Recursos e Incidentes de Julgamento), 
 * Listas de Textos e Listas de Incidentes.
 * 
 * @author Rodrigo Barreiros
 * @since 30.04.2010
 */
public interface PesquisaService {
	
	/**
	 * Pesquisa avan�ada de Objetos Incidente (Processos, Recursos e Incidentes de Julgamento).
	 * 
	 * <p>Filtra por diversos crit�rios de pesquisa.
	 * 
	 * @param pesquisa cont�m os crit�rios de pesquisa
	 * @return a lista de objetos incidente
	 */
	PagedList<ObjetoIncidenteDto> pesquisarObjetosIncidente(Pesquisa pesquisa);

	/**
	 * Pesquisa avan�ada de Textos.
	 * 
	 * <p>Filtra por diversos crit�rios de pesquisa.
	 * 
	 * @param pesquisa cont�m os crit�rios de pesquisa
	 * @return a lista de textos
	 */
	PagedList<TextoDto> pesquisarTextos(Pesquisa pesquisa);
	
	PagedList<ComunicacaoDto> pesquisarComunicacoes(Pesquisa pesquisa);
	
	PagedList<ComunicacaoDto> pesquisarComunicacoesMobile(Pesquisa pesquisa);	

	/**
	 * Pesquisa avan�ada de Listas de Objetos Incidente (Processos, Recursos e Incidentes de Julgamento).
	 * 
	 * <p>Filtra por diversos crit�rios de pesquisa.
	 * 
	 * @param pesquisa cont�m os crit�rios de pesquisa
	 * @return as listas
	 */
	PagedList<ListaIncidentesDto> pesquisarListasIncidentes(Pesquisa pesquisa);

	/**
	 * Pesquisa avan�ada de Listas de Textos.
	 * 
	 * <p>Filtra por diversos crit�rios de pesquisa.
	 * 
	 * @param pesquisa cont�m os crit�rios de pesquisa
	 * @return as listas
	 */
	PagedList<ListaTextosDto> pesquisarListasTextos(Pesquisa pesquisa);

	/**
	 * Pesquisa os Objetos Incidente dados a sigla e n�mero do processo principal.
	 * 
	 * @param sigla a sigla do processo principal
	 * @param numero o n�mero do processo principal
	 * @return a lista de objetos incidente
	 */
	List<ObjetoIncidenteDto> pesquisarObjetosIncidente(String identificacao);

	/**
	 * Pesquisa os Objetos Incidentes, sendo poss�vel solicitar a cria��o de um objeto incidente fake 
	 * (Que trar� todos os textos de todos os incidentes)
	 * @param identificacao
	 * @param incluirFake
	 * @return
	 */
	List<ObjetoIncidenteDto> pesquisarObjetosIncidentes(String identificacao, boolean incluirFake);

	/**
	 * Salva uma configura��o de pesquisa avan�ada.
	 * @param nomeConfiguracaoPesquisa
	 * @param escopoConfiguracaoPesquisa
	 * @param pageSize
	 * @param principal
	 * @param pesquisa
	 * @return
	 */
	Long salvarConfiguracaoPesquisa(String nomeConfiguracaoPesquisa,
			String escopoConfiguracaoPesquisa, Class tipoPesquisa,
			Integer pageSize, Principal principal, Pesquisa pesquisa);

	/**
	 * Pesquisa as configura��es de pesquisa gravadas dispon�veis para o usu�rio.
	 * @param idUsuario
	 * @param idSetor
	 * @param tipoConfiguracaoUsuario
	 * @param subtipoConfiguracao
	 * @return
	 * @throws ServiceException
	 */
	List<ConfiguracaoUsuario> pesquisarConfiguracoesPesquisa(String idUsuario,
			Long idSetor, Long tipoConfiguracaoUsuario, String subtipoConfiguracao) throws ServiceException;
	
	/**
	 * Recupera o objeto que representa o XML da configura��o da pesquisa avan�ada desejada.
	 * @param idConfiguracaoUsuario
	 * @return
	 */
	PesquisaXMLBind recuperarObjetoPesquisaXMLBind(Long idConfiguracaoUsuario);

	/**
	 * Preenche a pesquisaAvancada a partir de um objeto pesquisaXMLBind.
	 * @param pesquisaAvancada
	 * @param pesquisaXMLBind
	 */
	void popularPesquisaAvancada(Pesquisa pesquisaAvancada,
			PesquisaXMLBind pesquisaXMLBind);
	
	/**
	 * Preenche a pesquisaAvancada a partir de um objeto Comunicacao.
	 * @param pesquisaAvancada
	 * @param setor
	 */
	void popularPesquisaComunicacao(Pesquisa pesquisa, Setor setor);

	/**
	 * Exclui uma configura��o de pesquisa.
	 * @param idConfiguracaoPesquisa
	 * @param principal
	 */
	void excluirConfiguracaoPesquisa(Long idConfiguracaoPesquisa, Principal principal);

//	MinistroDto recuperarRelatorIncidente(Long idObjetoIncidente);
}
