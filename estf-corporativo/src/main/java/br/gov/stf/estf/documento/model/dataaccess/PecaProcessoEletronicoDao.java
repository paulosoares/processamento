package br.gov.stf.estf.documento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.documento.model.util.PecaProcessoEletronicoDoTextoDynamicQuery;
import br.gov.stf.estf.documento.model.util.PecaProcessoEletronicoDynamicRestriction;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface PecaProcessoEletronicoDao extends
		GenericDao<PecaProcessoEletronico, Long> {

	PecaProcessoEletronico recuperarPeca(DocumentoEletronico documentoEletronico)
			throws DaoException;
	
	List<PecaProcessoEletronico> recuperarListaPecasComunicacao(DocumentoEletronico documentoEletronico)
			throws DaoException;

	List<PecaProcessoEletronico> pesquisar(
			PecaProcessoEletronicoDoTextoDynamicQuery consulta)
			throws DaoException;

	Long recuperarNumeroDeOrdemMaximo(ObjetoIncidente<?> protocolo)
			throws DaoException;

	void persistirPecaProcessoEletronico(
			PecaProcessoEletronico pecaProcessoEletronico) throws DaoException;

	/**
	 * Método que realiza uma exclusão lógica da peça (o tipo da situação é
	 * modificado para "Excluído") e o cancelamento do documento.
	 * 
	 * @param pecaProcessoEletronico
	 * @param cancelarPDF
	 * @throws DaoException
	 */
	void excluirPecaProcessoEletronico(
			PecaProcessoEletronico pecaProcessoEletronico) throws DaoException;

	List<PecaProcessoEletronico> pesquisar(
			PecaProcessoEletronicoDynamicRestriction consulta)
			throws DaoException;

	void excluirPecaProcessoEletronico(
			PecaProcessoEletronico pecaProcessoEletronico, String motivo,
			boolean cancelarPDF) throws DaoException;
	
	void cancelaPdfsDaPeca(
			PecaProcessoEletronico pecaProcessoEletronico, String motivo)
			throws DaoException;

	public boolean temPecasPendenteVisualizacao(ObjetoIncidente<?> objetoIncidente) throws DaoException;
	
	public List<PecaProcessoEletronico> pesquisarPorProcesso(Processo processo,
			Boolean incluirCancelados) throws DaoException;

	List<PecaProcessoEletronico> pecasPendenteVisualizacao(ObjetoIncidente<?> objetoIncidente) throws DaoException;

	List<PecaProcessoEletronico> pecasJuntadasPendenteVisualizacao(ObjetoIncidente<?> objetoIncidente) throws DaoException;

	void tornarPublicasPecasPendenteVisualizacao(ObjetoIncidente<?> objetoIncidente) throws DaoException;

	List<PecaProcessoEletronico> listarPecas(ObjetoIncidente<?> objetoIncidente) throws DaoException;

	public PecaProcessoEletronico recuperaPecaComunicacao(Long idDocumento, Long idObjetoIncidente) throws DaoException;

	
}
