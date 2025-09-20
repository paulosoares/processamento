package br.gov.stf.estf.publicacao.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.publicacao.FaseTextoProcesso;
import br.gov.stf.estf.publicacao.model.dataaccess.FaseTextoProcessoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * Fornece servi�os (m�todos) para manipula��o de objetos do tipo
 * <code>FaseTextoProcesso</code>.
 * 
 * @author Rodrigo.Barreiros
 * @since 25.05.2009
 * 
 * @see FaseTextoProcesso
 */
public interface FaseTextoProcessoService extends GenericService<FaseTextoProcesso, Long, FaseTextoProcessoDao> {

	/**
	 * Recupera a �ltima fase pela qual o texto passou antes de chegar � fase
	 * atual.
	 * 
	 * @param texto
	 *            o texto alvo
	 * @return a �ltima fase do texto
	 */
	public FaseTextoProcesso recuperarUltimaFaseDoTexto(Texto texto) throws ServiceException;

	void excluirFasesDoTexto(Texto texto) throws ServiceException;
	
	public List<FaseTextoProcesso> pesquisarFasesDoTexto(Texto texto) throws ServiceException;

}
