package br.jus.stf.estf.decisao.support.controller.context;

import br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa;
import br.jus.stf.estf.decisao.pesquisa.web.texto.TextoFacesBean;
import br.jus.stf.estf.decisao.support.controller.faces.datamodel.PagedList;

/**
 * Estabelece o contrato entre os Beans de negócio e o mecanismo de pesquisa.
 * 
 * <p>Interface utilizada para pesquisar e visualizar informações. Por exemplo, 
 * no caso de uma pesquisa por Textos, devemos carregar o Bean {@link TextoFacesBean} 
 * que  deve implementar essa interface e delegar a pesquisa via
 * {@link #search(Pesquisa, int, int)}
 * 
 * 
 * @author Rodrigo.Barreiros
 * @since 05.05.2010
 * @param <T> o tipo da entidade pesquisada ou visualizada.
 */
public interface FacesBean<T> {
	
	/**
	 * Executa a pesquisa paginada, dado os critério de busca.
	 * 
	 * @param pesquisa objeto com os critérios de busca
	 * @param first index pra o primeiro resultado
	 * @param max quantidade máxima de resultados
	 * @return a lista paginada de resultados
	 */
	PagedList<T> search(Pesquisa pesquisa, int first, int max);
	
	/**
	 * Carrega o objeto para visualização das informações.
	 * 
	 * @param entidade a entidade que será visualizada
	 * @return a entidade carregada
	 */
	T load(T entidade);
	
}
