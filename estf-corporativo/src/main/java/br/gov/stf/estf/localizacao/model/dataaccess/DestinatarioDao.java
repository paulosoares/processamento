package br.gov.stf.estf.localizacao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Destinatario;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface DestinatarioDao extends GenericDao<Destinatario, Long> {

	List<Destinatario> recuperarDestinatarioDaOrigem(Long codOrigem, String id) throws DaoException;
	
	public List<Destinatario> pesquisarDestinatarioDescricao(String descricao) throws DaoException;

	public List pesquisarDestinatario(Long codOrigem, Long codDestinatario) throws DaoException;

}
