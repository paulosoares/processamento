package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.ClasseUnificada;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ProcessoImagem;
import br.gov.stf.estf.entidade.processostf.ProcessoImagem.ProcessoImagemId;
import br.gov.stf.estf.processostf.model.util.ProcessoImagemSearchData;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;
import br.gov.stf.framework.util.SearchResult;

public interface ProcessoImagemDao extends GenericDao<ProcessoImagem, Long> {

	public ProcessoImagem recuperarProcessoImagem(ObjetoIncidente<?> objetoIncidente) throws DaoException;

	public SearchResult<ProcessoImagem> pesquisarProcessoImagem(ProcessoImagemSearchData processoImagemSearchData) throws DaoException;

	public ClasseUnificada recuperarClasseUnificada(ProcessoImagem processoImagem) throws DaoException;
	
	public List<ProcessoImagem> pesquisarProcessoImagemPorClasseNumero(String siglaClasse, Long numeroProcesso) throws DaoException;
	
	public ProcessoImagem recuperarProcessoImagemLiberadoPorId(ProcessoImagemId id) throws DaoException;


}
