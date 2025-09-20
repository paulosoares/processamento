/**
 * 
 */
package br.gov.stf.estf.julgamento.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.InformacaoPautaProcesso;
import br.gov.stf.estf.entidade.julgamento.PrevisaoSustentacaoOral;
import br.gov.stf.estf.julgamento.model.dataaccess.PrevisaoSustentacaoOralDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 * @since 05.07.2011
 */
public interface PrevisaoSustentacaoOralService extends
		GenericService<PrevisaoSustentacaoOral, Long, PrevisaoSustentacaoOralDao> {

	List<PrevisaoSustentacaoOral> pesquisar(
			InformacaoPautaProcesso informacaoPautaProcesso) throws ServiceException;

}
