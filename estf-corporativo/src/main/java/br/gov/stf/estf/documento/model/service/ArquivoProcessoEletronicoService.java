package br.gov.stf.estf.documento.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.ArquivoProcessoEletronicoDao;
import br.gov.stf.estf.documento.model.util.ArquivoProcessoEletronicoSearchData;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ArquivoProcessoEletronicoService extends GenericService<ArquivoProcessoEletronico, Long, ArquivoProcessoEletronicoDao> {

	/**
	 * @deprecated Utilizar o método {@link #pesquisarArquivoProcessoEletronico(ArquivoProcessoEletronicoSearchData)} ao invés deste.
	 */
	@Deprecated
	public List<ArquivoProcessoEletronico> pesquisarArquivoProcessoEletronico(String siglaClasseProcessual, Long numeroProcessual, Long numeroProtocolo, Short anoProtocolo,
			Long idDocumentoEletronico) throws ServiceException;

	public List<ArquivoProcessoEletronico> pesquisarArquivoProcessoEletronico(ArquivoProcessoEletronicoSearchData searchData) throws ServiceException;

	public ArquivoProcessoEletronico salvarArquivoProcessoEletronicoAssinado(ArquivoProcessoEletronico arquivoProcessoEletronico, byte[] pdfAssinado, byte[] assinatura,
			byte[] carimboTempo, Date dataCarimboTempo) throws ServiceException;

	public ArquivoProcessoEletronico inserirArquivoProcessoEletronicoPendenteJuntada(byte[] pdf, String siglaTipoPecaProcesso, ObjetoIncidente objetoIncidente, Setor setor) throws ServiceException;

	public void inserirArquivoProcessoEletronicoPendenteJuntada(byte[] pdf, String siglaTipoPecaProcesso, String siglaClasse, Long numeroProcesso, Long codigoRecurso,
			Long tipoJulgamento, Setor setor) throws ServiceException;

	public ArquivoProcessoEletronico recuperarDocumentoPeca(ObjetoIncidente objetoIncidente, String siglaTipoPecaProcesso) throws ServiceException;

	public ArquivoProcessoEletronico recuperarDocumentoPeca(String siglaClasse, Long numeroProcesso, Long codigoRecurso, String tipoJulgamento, String siglaTipoPecaProcesso) throws ServiceException;

	public void excluirPeca(ArquivoProcessoEletronico arquivoProcessoEletronico) throws ServiceException;

	ArquivoProcessoEletronico recuperarArquivoDoDocumentoTexto(DocumentoTexto documentoTexto);

	void inserirArquivoProcessoEletronicoJuntado(DocumentoTexto documentoTexto, String siglaTipoPecaProcesso, ObjetoIncidente objetoIncidente, Setor setor) throws ServiceException;

	public List<ArquivoProcessoEletronico> pesquisarPecas(ObjetoIncidente objetoIncidente, String... siglaTipoPeca) throws ServiceException;

	public List<ArquivoProcessoEletronico> pesquisarPecasPeloIdObjetoIncidente(Long iObjetoIncidente) throws ServiceException;

	public List<Long> pesquisarPecasSetor(Long codSetor, List<String> tiposAcesso, Boolean gabineteSEJ, String siglaClasse, Long numeroProcesso) throws ServiceException;

	public List<Long> pesquisarPecasSetor(Long codSetor, List<String> tiposAcesso) throws ServiceException;

	public List<ArquivoProcessoEletronico> pesquisar(List<Long> idsArquivos)  throws ServiceException;
	
	public int countPecasProcesso(Processo processo) throws ServiceException;
	
	public List<PecaProcessoEletronico> pesquisarPecasPelosDocumentos(List<Long> listaSeqDocumento) throws ServiceException;
	
	public void copiarPecasEletronicas(Processo processoOrigem, ObjetoIncidente<?> objetoIncidenteDestino, List<PecaProcessoEletronico> pecasSelecionadas, Boolean apagarOrigem, Boolean inserirDescricao, Boolean inicioFim) throws ServiceException;
	
	public ArquivoProcessoEletronico salvarPecaEletronica(byte[] pdf, String siglaTipoPecaProcesso, ObjetoIncidente<?> objetoIncidente, Setor setor, AndamentoProcesso andamentoProcesso) throws ServiceException;

	DocumentoEletronico inserirArquivoProcessoEletronicoPendenteJuntadaComTextoAndamentoProcesso(
			byte[] pdf, String siglaTipoPecaProcesso,
			ObjetoIncidente objetoIncidente, Setor setor,
			AndamentoProcesso andamentoProcesso, String descricaoPeca) throws ServiceException;
	
	public List<ArquivoProcessoEletronico> recuperarDocumentosPeca(Long id) throws ServiceException;
}
