package br.gov.stf.estf.julgamento.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.julgamento.Tema;
import br.gov.stf.estf.entidade.julgamento.TipoOcorrencia;
import br.gov.stf.estf.entidade.julgamento.TipoTema;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.julgamento.model.dataaccess.TemaDao;
import br.gov.stf.estf.julgamento.model.exception.TemaException;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TemaService extends GenericService<Tema, Long, TemaDao> {

	public List<TipoTema> pesquisarTipoTema( Long codigo, String descricao)throws ServiceException;
	
	public List<TipoOcorrencia> pesquisarTipoOcorrencia( Long codigo, String descricao)throws ServiceException;
	
	public Boolean persistirTema(Tema tema) throws ServiceException,TemaException;
	public Boolean excluirTema(Tema tema) throws ServiceException;
	public Tema recuperarTemas(Long idObjetoIncidente) throws ServiceException;
	public void registarSuspensaoNacional(Tema tema, Processo processo,	Date dataInicial, AndamentoProcesso andamento) throws ServiceException;
	public void encerrarSuspensaoNacional(Tema tema, Processo processo, Date dataFinal) throws ServiceException;
}
