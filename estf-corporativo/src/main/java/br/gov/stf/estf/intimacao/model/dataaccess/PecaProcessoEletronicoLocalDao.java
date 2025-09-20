package br.gov.stf.estf.intimacao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.TextoAndamentoProcesso;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface PecaProcessoEletronicoLocalDao extends GenericDao<PecaProcessoEletronico, Long> {

	public List<PecaProcessoEletronico> pesquisarPecasPorProcessoIncidente(Long idProcessoIncidente,
			Boolean incluirCancelados) throws DaoException;
	
	public List<PecaProcessoEletronico> pesquisarPecasAndamento8507( Long idProcessoIncidente )throws DaoException;

	public List<TextoAndamentoProcesso> pesquisarPecasUtilizadasEmVista(Long idProcessoIncidente) throws DaoException;
}
