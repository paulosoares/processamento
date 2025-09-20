package br.gov.stf.estf.documento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.documento.model.util.ArquivoProcessoEletronicoSearchData;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ArquivoProcessoEletronicoDao extends GenericDao<ArquivoProcessoEletronico, Long> {
	/**
	 * @deprecated Utilizar o método {@link #pesquisarArquivoProcessoEletronico(ArquivoProcessoEletronicoSearchData)} ao invés deste.
	 */
	@Deprecated
	public List<ArquivoProcessoEletronico> pesquisarArquivoProcessoEletronico(String siglaClasseProcessual, Long numeroProcessual, Long numeroProtocolo,
			Short anoProtocolo, Long idDocumentoEletronico) throws DaoException;

	public List<ArquivoProcessoEletronico> pesquisarArquivoProcessoEletronico(ArquivoProcessoEletronicoSearchData searchData) throws DaoException;

	public ArquivoProcessoEletronico recuperarDocumentoPeca(ObjetoIncidente objetoIncidente, String siglaTipoPecaProcesso) throws DaoException;

	public ArquivoProcessoEletronico recuperarDocumentoPeca(String siglaClasse, Long numeroProcesso, Long codigoRecurso, String tipoJulgamento,
			String siglaTipoPecaProcesso) throws DaoException;

	public ArquivoProcessoEletronico recuperarArquivoDoDocumentoTexto(DocumentoTexto documentoTexto);

	public List<ArquivoProcessoEletronico> pesquisarPecas(ObjetoIncidente objetoIncidente, String... siglaTipoPeca) throws DaoException;

	public List<ArquivoProcessoEletronico> pesquisarPecasPeloIdObjetoIncidente(Long idObjetoIncidentePrincipal) throws DaoException;
	
	public List<ArquivoProcessoEletronico> pesquisarPecasPeloIdObjetoIncidente(List<ObjetoIncidente<?>> listaIdsObjetoIncidente) throws DaoException;
	
	public List<Long> pesquisarPecasSetor(Long codSetor, List<String> tiposAcesso, Boolean gabineteSEJ, String siglaClasse, Long numeroProcesso)
			throws DaoException;

	public List<Long> pesquisarPecasSetor(Long codSetor, List<String> tiposAcesso) throws DaoException;

	public List<ArquivoProcessoEletronico> pesquisar(List<Long> idsArquivos) throws DaoException;

	public int countPecasProcesso(Processo processo) throws DaoException;
	
	public List<PecaProcessoEletronico> pesquisarPecasPelosDocumentos(List<Long> listaSeqDocumento) throws DaoException;

	public List<ArquivoProcessoEletronico> recuperarDocumentosPeca(Long id) throws DaoException;
	
	
}
