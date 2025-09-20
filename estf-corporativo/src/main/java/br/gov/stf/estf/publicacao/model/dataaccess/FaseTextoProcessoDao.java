package br.gov.stf.estf.publicacao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.publicacao.FaseTextoProcesso;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * Fornece métodos para manipulação persistente de objetos do tipo
 * <code>FaseTextoProcesso</code>.
 * 
 * @author Rodrigo.Barreiros
 * @since 25.05.2009
 * 
 * @see FaseTextoProcesso
 */
public interface FaseTextoProcessoDao extends GenericDao<FaseTextoProcesso, Long> {

	/**
	 * @see br.gov.stf.estf.publicacao.model.service.FaseTextoProcessoService#recuperarUltimaFaseDoTexto(br.gov.stf.estf.entidade.documento.Texto)
	 */
	public FaseTextoProcesso recuperarUltimaFaseDoTexto(Texto texto) throws DaoException;

	public List<FaseTextoProcesso> pesquisarFasesDoTexto(Texto texto) throws DaoException;

}
