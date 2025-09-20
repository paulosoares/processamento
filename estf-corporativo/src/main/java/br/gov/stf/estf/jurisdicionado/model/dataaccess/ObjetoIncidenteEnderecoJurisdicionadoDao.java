package br.gov.stf.estf.jurisdicionado.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.jurisdicionado.ObjetoIncidenteEnderecoJurisdicionado;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ObjetoIncidenteEnderecoJurisdicionadoDao extends GenericDao<ObjetoIncidenteEnderecoJurisdicionado, Long>{

	public List<ObjetoIncidenteEnderecoJurisdicionado> recuperarEnderecos(Long seqEnderecoJurisdicionado) throws DaoException;
}
