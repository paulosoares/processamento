package br.jus.stf.estf.decisao.pesquisa.persistence;

import java.util.List;

import br.jus.stf.estf.decisao.pesquisa.domain.ListaIncidentesDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ListaTextosDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.pesquisa.persistence.jdbc.NumeroProcessoNaoInformadoException;
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
public interface PesquisaDao {

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
	PagedList<TextoDto> pesquisarTextos(Pesquisa pesquisa, Principal principal);
	
	/**
	 * Pesquisa avan�ada de Textos.
	 * 
	 * <p>Filtra por diversos crit�rios de pesquisa.
	 * 
	 * @param pesquisa
	 * @param pesquisaTextualRapida
	 * @return a lista de textos
	 */
	PagedList<TextoDto> pesquisarTextos(Pesquisa pesquisa, boolean pesquisaTextualRapida, Principal principal);

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
	 * @throws NumeroProcessoNaoInformadoException Quando o n�mero do processo n�o for informado
	 */
	List<ObjetoIncidenteDto> pesquisarObjetosIncidente(String sigla, Long numero) throws NumeroProcessoNaoInformadoException;
	
//	MinistroDto recuperarRelatorIncidente(Long idObjetoIncidente);

}
