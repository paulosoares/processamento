/**
 * 
 */
package br.gov.stf.estf.julgamento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.InformacaoPautaProcesso;
import br.gov.stf.estf.entidade.julgamento.PrevisaoSustentacaoOral;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 05.07.2011
 */
public interface PrevisaoSustentacaoOralDao extends GenericDao<PrevisaoSustentacaoOral, Long> {

	List<PrevisaoSustentacaoOral> pesquisar(
			InformacaoPautaProcesso informacaoPautaProcesso) throws DaoException;

}
