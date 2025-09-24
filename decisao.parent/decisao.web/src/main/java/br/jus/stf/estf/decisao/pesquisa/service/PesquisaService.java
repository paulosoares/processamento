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
 * Interface DAO para pesquisa avançada de informações de Textos, 
 * Incidentes (Processos, Recursos e Incidentes de Julgamento), 
 * Listas de Textos e Listas de Incidentes.
 * 
 * @author Rodrigo Barreiros
 * @since 30.04.2010
 */
public interface PesquisaService {
	
	/**
	 * Pesquisa avançada de Objetos Incidente (Processos, Recursos e Incidentes de Julgamento).
	 * 
	 * <p>Filtra por diversos critérios de pesquisa.
	 * 
	 * @param pesquisa contém os critérios de pesquisa
	 * @return a lista de objetos incidente
	 */
	PagedList<ObjetoIncidenteDto> pesquisarObjetosIncidente(Pesquisa pesquisa);

	/**
	 * Pesquisa avançada de Textos.
	 * 
	 * <p>Filtra por diversos critérios de pesquisa.
	 * 
	 * @param pesquisa contém os critérios de pesquisa
	 * @return a lista de textos
	 */
	PagedList<TextoDto> pesquisarTextos(Pesquisa pesquisa);
	
	PagedList<ComunicacaoDto> pesquisarComunicacoes(Pesquisa pesquisa);
	
	PagedList<ComunicacaoDto> pesquisarComunicacoesMobile(Pesquisa pesquisa);	

	/**
	 * Pesquisa avançada de Listas de Objetos Incidente (Processos, Recursos e Incidentes de Julgamento).
	 * 
	 * <p>Filtra por diversos critérios de pesquisa.
	 * 
	 * @param pesquisa contém os critérios de pesquisa
	 * @return as listas
	 */
	PagedList<ListaIncidentesDto> pesquisarListasIncidentes(Pesquisa pesquisa);

	/**
	 * Pesquisa avançada de Listas de Textos.
	 * 
	 * <p>Filtra por diversos critérios de pesquisa.
	 * 
	 * @param pesquisa contém os critérios de pesquisa
	 * @return as listas
	 */
	PagedList<ListaTextosDto> pesquisarListasTextos(Pesquisa pesquisa);

	/**
	 * Pesquisa os Objetos Incidente dados a sigla e número do processo principal.
	 * 
	 * @param sigla a sigla do processo principal
	 * @param numero o número do processo principal
	 * @return a lista de objetos incidente
	 */
	List<ObjetoIncidenteDto> pesquisarObjetosIncidente(String identificacao);

	/**
	 * Pesquisa os Objetos Incidentes, sendo possível solicitar a criação de um objeto incidente fake 
	 * (Que trará todos os textos de todos os incidentes)
	 * @param identificacao
	 * @param incluirFake
	 * @return
	 */
	List<ObjetoIncidenteDto> pesquisarObjetosIncidentes(String identificacao, boolean incluirFake);

	/**
	 * Salva uma configuração de pesquisa avançada.
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
	 * Pesquisa as configurações de pesquisa gravadas disponíveis para o usuário.
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
	 * Recupera o objeto que representa o XML da configuração da pesquisa avançada desejada.
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
	 * Exclui uma configuração de pesquisa.
	 * @param idConfiguracaoPesquisa
	 * @param principal
	 */
	void excluirConfiguracaoPesquisa(Long idConfiguracaoPesquisa, Principal principal);

//	MinistroDto recuperarRelatorIncidente(Long idObjetoIncidente);
}
