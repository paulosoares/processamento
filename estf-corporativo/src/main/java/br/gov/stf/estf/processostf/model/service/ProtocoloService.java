package br.gov.stf.estf.processostf.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.processostf.Protocolo;
import br.gov.stf.estf.processostf.model.dataaccess.ProtocoloDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ProtocoloService extends GenericService<Protocolo, Long, ProtocoloDao>  {

	public Protocolo recuperarProtocolo(Long numero, Short ano) throws ServiceException;


	public void persistirProtocolo(Protocolo protocolo) throws ServiceException;


	/**
	 * Faz uma chamada à função stf.FNC_RETORNA_ANO_NUM_PETICAO que gera um novo número de petição inicial/protocolo na
	 * tabela JUDICIARIO.NUMERO_PETICAO.
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public Protocolo gerarProtocoloAnoNumero() throws ServiceException;


	/**
	 * Recupera um protocolo com base nas informações da origem. Nenhum dos parâmetros pode ser nulo.
	 * 
	 * @param siglaClasseProcedencia
	 * @param numeroProcessoProcedencia
	 * @param codigoOrgao
	 * @return
	 * @throws ServiceException
	 */
	public Protocolo recuperarProtocolo(String siglaClasseProcedencia, String numeroProcessoProcedencia,
			Integer codigoOrgao) throws ServiceException;
	
   public List<Protocolo> recuperarProtocolo(Integer codigoOrigem, String siglaClasseProcedencia,String numeroProcessoProcedencia, 
			 String siglaClasse, Long numeroProcesso, Long numeroProtocolo, 
			Short anoProtocolo,String tipoMeioFisico, Date dataInicial, Date dataFinal) throws ServiceException;

   void removerObjetoSessaoHibernate(Object objeto, boolean executarFlush);
   
   public List<Protocolo> recuperar(String tipoMeioProcesso) throws ServiceException;
   
}
