package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.processostf.Protocolo;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ProtocoloDao extends GenericDao<Protocolo, Long> {

	public Protocolo recuperarProtocolo(Long numero, Short ano) throws DaoException;


	public void persistirProtocolo(Protocolo protocolo) throws DaoException;


	/**
	 * Faz uma chamada à função stf.FNC_RETORNA_ANO_NUM_PETICAO que gera um novo número de petição inicial/protocolo na
	 * tabela JUDICIARIO.NUMERO_PETICAO.
	 * 
	 * @return
	 * @throws DaoException
	 */
	public Protocolo gerarProtocoloAnoNumero() throws DaoException;


	/**
	 * Recupera um protocolo com base nas informações da origem. Nenhum dos parâmetros pode ser nulo.
	 * 
	 * @param siglaClasseProcedencia
	 * @param numeroProcessoProcedencia
	 * @param codigoOrgao
	 * @return
	 * @throws DaoException
	 */
	public Protocolo recuperarProtocolo(String siglaClasseProcedencia, String numeroProcessoProcedencia,
			Integer codigoOrgao) throws DaoException;
	
   public List<Protocolo> recuperarProtocolo(Integer codigoOrigem, String siglaClasseProcedencia,String numeroProcessoProcedencia, 
			 String siglaClasse, Long numeroProcesso, Long numeroProtocolo, 
			Short anoProtocolo,String tipoMeioFisico, Date dataInicial, Date dataFinal) throws DaoException;

   void removerObjetoSessaoHibernate(Object objeto, boolean executarFlush);
   
   public List<Protocolo> recuperar(String tipoMeioProcesso) throws DaoException;
}
