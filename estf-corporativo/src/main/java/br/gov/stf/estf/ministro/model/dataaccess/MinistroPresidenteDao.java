package br.gov.stf.estf.ministro.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.ministro.MinistroPresidente;
import br.gov.stf.estf.entidade.ministro.MinistroPresidente.MinistroPresidenteId;
import br.gov.stf.estf.entidade.ministro.TipoOcorrenciaMinistro;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface MinistroPresidenteDao extends
		GenericDao<MinistroPresidente, MinistroPresidenteId> {

	public MinistroPresidente recuperarMinistroPresidenteAtual()
			throws DaoException;

	List<MinistroPresidente> recuperarMinistrosPresidentesAtuais()
			throws DaoException;

	public MinistroPresidente recuperarMinistro(
			TipoOcorrenciaMinistro tipoOcorrenciaMinistro) throws DaoException;

	public MinistroPresidente recuperarMinistro(TipoOcorrenciaMinistro tipoOcorrenciaMinistro, Date dataPosse) throws DaoException;

	public MinistroPresidente recuperarMinistroPresidenteNoPeriodo(TipoOcorrenciaMinistro tipoOcorrenciaMinistro,
			Date dataInicio, Date dataFim); 
}
