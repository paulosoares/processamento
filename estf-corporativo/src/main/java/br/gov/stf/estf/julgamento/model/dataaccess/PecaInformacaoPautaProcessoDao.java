package br.gov.stf.estf.julgamento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.julgamento.InformacaoPautaProcesso;
import br.gov.stf.estf.entidade.julgamento.PecaInformacaoPautaProcesso;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface PecaInformacaoPautaProcessoDao extends GenericDao<PecaInformacaoPautaProcesso, Long> {
	
	public List<PecaInformacaoPautaProcesso> recuperar(InformacaoPautaProcesso informacaoPautaProcesso, boolean somenteDocumentoExterno) throws DaoException;
	public List<PecaInformacaoPautaProcesso> recuperar(InformacaoPautaProcesso informacaoPautaProcesso, PecaProcessoEletronico pecaProcessoEletronico) throws DaoException;

}
