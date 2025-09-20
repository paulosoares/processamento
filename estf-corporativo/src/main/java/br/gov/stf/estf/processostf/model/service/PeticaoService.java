package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.processostf.model.dataaccess.PeticaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface PeticaoService extends GenericService<Peticao, Long, PeticaoDao> {
	
	public Peticao recuperarPeticao(Long numero, Short ano) throws ServiceException;
	
	public Peticao recuperarPeticao(Long idObjetoIndicente) throws ServiceException;
	
	/*
	public Long persistirPeticao(GuiaDeslocamento guia, Long numeroProcesso, String siglaClasseProcessual,
			Short codigoTipoIncidente, List <PartePeticao> partes,
			String descricaoPeticao) throws ServiceException;
	*/
	
	public Long persistirPeticao(Peticao peticao) throws ServiceException;
	
	public Peticao recuperarPeticaoProcesso( Long numeroPeticao, Short anoPeticao, 
													 String siglaProcessual, Long numeroProcessual, Short codRecurso,
													 Boolean flgJuntado ) throws ServiceException;
	
	public List<ObjetoIncidente<?>> recuperarListaObjetoPeloObjetoIncidentePrincipal(Long id) throws ServiceException;
	
	List<Peticao> recuperarPeticoes(Long numero, Short ano) throws ServiceException;
	Boolean isPendenteDigitalizacao(Peticao peticao) throws ServiceException;
    Boolean isRemessaIndevida(Peticao peticao) throws ServiceException;

}
