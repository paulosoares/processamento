package br.gov.stf.estf.publicacao.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.publicacao.FaseTextoProcesso;
import br.gov.stf.estf.publicacao.model.dataaccess.FaseTextoProcessoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * Fornece serviços (métodos) para manipulação de objetos do tipo
 * <code>FaseTextoProcesso</code>.
 * 
 * @author Rodrigo.Barreiros
 * @since 25.05.2009
 * 
 * @see FaseTextoProcesso
 */
public interface FaseTextoProcessoService extends GenericService<FaseTextoProcesso, Long, FaseTextoProcessoDao> {

	/**
	 * Recupera a última fase pela qual o texto passou antes de chegar à fase
	 * atual.
	 * 
	 * @param texto
	 *            o texto alvo
	 * @return a última fase do texto
	 */
	public FaseTextoProcesso recuperarUltimaFaseDoTexto(Texto texto) throws ServiceException;

	void excluirFasesDoTexto(Texto texto) throws ServiceException;
	
	public List<FaseTextoProcesso> pesquisarFasesDoTexto(Texto texto) throws ServiceException;

}
