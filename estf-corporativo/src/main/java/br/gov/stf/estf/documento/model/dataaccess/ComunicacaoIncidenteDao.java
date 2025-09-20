package br.gov.stf.estf.documento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ComunicacaoIncidenteDao extends GenericDao<ComunicacaoIncidente, Long> {
	
	public ObjetoIncidente<?> selecionaObjetoIncidente(Long idDocumento) throws DaoException;
	
	public List<ComunicacaoIncidente> verificaSeExisteProcessosVinculados(Comunicacao comunicacao) throws DaoException;

	public ComunicacaoIncidente recuperarPorAndamento(Long idAndamento) throws DaoException;

}
