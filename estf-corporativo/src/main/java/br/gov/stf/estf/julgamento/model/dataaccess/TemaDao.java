package br.gov.stf.estf.julgamento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.ProcessoTema;
import br.gov.stf.estf.entidade.julgamento.Tema;
import br.gov.stf.estf.entidade.julgamento.TipoOcorrencia;
import br.gov.stf.estf.entidade.julgamento.TipoTema;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TemaDao extends GenericDao<Tema, Long> {
	
	public Tema recuperarTema(Long id)throws DaoException;
	
	public List<TipoTema> pesquisarTipoTema( Long codigo, String descricao)throws DaoException;
	
	public List<TipoOcorrencia> pesquisarTipoOcorrencia( Long codigo, String descricao)throws DaoException;
	
	public Boolean persistirTema(Tema tema) throws DaoException;
	
	public Boolean excluirTema(Tema tema) throws DaoException;
	
	public Boolean excluirProcessoTema(ProcessoTema processoTema) throws DaoException;
	
	public Tema recuperarTemas(Long idObjetoIncidente) throws DaoException;

}
