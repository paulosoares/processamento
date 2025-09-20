package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.ProtocoloPublicado;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ProtocoloPublicadoDao extends GenericDao<ProtocoloPublicado, Long> {
	public List<ProtocoloPublicado> pesquisar (ConteudoPublicacao conteudoPublicacao, Boolean recuperarOcultos) throws DaoException;
}
