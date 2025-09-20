package br.gov.stf.estf.localizacao.model.dataaccess;

// default package
// Generated 18/03/2008 11:02:23 by Hibernate Tools 3.1.0.beta5
import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.processostf.Orgao;
import br.gov.stf.estf.entidade.processostf.Procedencia;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * DAO interface for domain model class Origem.
 * 
 * @see .Origem
 * @author Hibernate Tools
 */

public interface OrigemDao extends GenericDao<Origem, Long> {

	List<Origem> recuperarOrigemPorIdOuDescricao(String id) throws DaoException;
	
	public List<Origem> recuperarOrigemPorId(Long id,Boolean ativo) throws DaoException;
	
	public List<Origem> recuperarOrigemPorDescricao(String id, Boolean ativo) throws DaoException;

	List<Origem> recuperarApenasPgr() throws DaoException;
	
	public List<Origem> recuperarApenasAgu() throws DaoException;
	
	public List<Origem> recuperarApenasDpf() throws DaoException;
	
	List<Origem> pesquisarOrigensAtivas(Orgao orgao, Procedencia procedencia) throws DaoException;

	public Boolean isOrigemIntegrada(Origem origem) throws DaoException;
	
	public List recuperaUsuarioExternoESTF(Long origem) throws DaoException;

	public Long recuperaUsuarioExternoOrigemESTF(Long seqUsuarioExterno) throws DaoException;

	public List<Origem> recuperarTodasOrigens(Boolean ativo) throws DaoException;
	
	public Boolean isOrigemAptaParaParaNotificacao(Long id) throws DaoException;

	public Procedencia pesquisarProcedenciaPadrao(Origem origem) throws DaoException;

	
}
