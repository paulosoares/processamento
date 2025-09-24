package br.jus.stf.estf.decisao.texto.persistence;

import java.util.Collection;
import java.util.List;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.security.Principal;


/**
 * Interface DAO para serviços persistentes relacionados à entidade Texto.
 * 
 * @author Rodrigo.Barreiros
 * @since 15.04.2010
 */
public interface TextoDao {
	
	/**
	 * Recupera a lista de textos relacionados a um dado objeto incidente.
	 * 
	 * <p>Se o flag "textosDoMinistro" for true, somente os textos do ministro informado
	 * serão recuperados, caso contrário, somente textos que não seja do ministro
	 * informado e que sejam público.
	 * 
	 * <p>Só serão listados textos que não sejam de decisão.
	 * 
	 * @param objetoIncidente o objeto incidente dos textos
	 * @param ministro o ministro corrente
	 * @param textosDoMinistro flag descrita acima
	 * 
	 * @return a lista de textos do objeto incidente
	 */
	List<TextoDto> recuperarTextos(ObjetoIncidente<?> objetoIncidente, Ministro ministro, boolean textosDoMinistro, Principal principal, boolean incluirTextosDisponibilizados);

	void marcarComoFavoritos(List<Long> idsTextos) throws DaoException;

	void desmarcarComoFavoritos(List<Long> idsTextos) throws DaoException;
	
	List<Texto> recuperarListaTextos(Collection<TextoDto> dtos) throws DaoException;

}
