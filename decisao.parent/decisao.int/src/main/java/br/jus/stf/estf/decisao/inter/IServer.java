package br.jus.stf.estf.decisao.inter;

import java.io.InputStream;
import java.util.List;

import br.jus.stf.estf.decisao.ConfiguracaoTexto;
import br.jus.stf.estf.decisao.DecisaoVersaoInfo;
import br.jus.stf.estf.decisao.DocDecisaoId;
import br.jus.stf.estf.decisao.exception.ServerException;

public interface IServer {

	public byte[] recuperarArquivo(Long seqTexto, Long seqFaseTextoProcesso) throws ServerException;

	public void salvarArquivo(Long seqTexto, InputStream arquivoStream) throws ServerException;

	public Long salvarNovoArquivo(Long seqObjetoIncidente, Long tipoTexto, String tipoVotoId, Long objetoIncidentePai,
			Long idTipoIncidenteJulgamento, String responsavel, String observacao, Long ministroDivergenteId, InputStream arquivoStream)
			throws ServerException;

	public InputStream recuperarTemplateDespacho() throws ServerException;

	public void salvarPDF(Long seqTexto, InputStream pdfStream) throws ServerException;

	public List<DecisaoVersaoInfo> recuperarVersoesArquivo(Long seqTexto) throws ServerException;

	public ConfiguracaoTexto recuperarConfiguracaoTextoSetor(Long codigoSetor) throws ServerException;

	public Boolean manterSessaoUsuario() throws ServerException;

//	public InputStream recuperarDocumentoRepercussaoGeral(Long seqTipoTexto, Long idObjetoIncidente) throws ServerException;

	public Boolean verificaDocumentoReadOnlyDecisaoRepercussaoGeral(Long idObjetoIncidente) throws ServerException;
	
	public Boolean verificaPorTextoDocumentoReadOnlyDecisaoRepercussaoGeral(Long seqObjetoIncidente) throws ServerException;

	public Boolean isDespacho(Long codigoTipoTexto);

	public Boolean isVoto(Long codigoTipoTexto);

	public String verificaDocumentoBloqueado(Long seqTexto, boolean somenteLeitura) throws ServerException;

	public void desbloquearDocumento(Long seqTexto) throws ServerException;
	
	public void desbloquearDocumentoAdmin(Long seqTexto) throws ServerException;

	public InputStream recuperarTemplateDoObjetoIncidente(Long seqObjetoIncidente, Long seqTipoTexto, Long seqFaseTexto)
			throws ServerException;

	public InputStream recuperarTemplateDoTexto(Long seqTexto, Long seqFaseTexto, Long seqTipoTexto) throws ServerException;

	public InputStream excluirCabecalhoDoDocumento(InputStream arquivo) throws ServerException;

	public InputStream converteOdtParaRtf(InputStream arquivoOdt) throws ServerException;

	public InputStream excluirEmentaDoDocumento(InputStream arquivo) throws ServerException;
	
	public InputStream excluirCabecalhoDaRepercussaoGeral(InputStream arquivo) throws ServerException;
	
	public Boolean recuperarPossuiVotoDivergente(Long seqObjetoIncidente) throws ServerException;
	
	public boolean verificarPodeCriarDecisaoRepercussaoGeral(Long seqObjetoIncidente) throws ServerException;

	public String verificaPerfilEditarTexto(Long idTexto) throws ServerException;
	
	public boolean isUsuarioDesbloqueadorTextos();

	public boolean isTextoEmEdicaoConcorrente(Long seqTexto) throws ServerException;

	public String getUsuarioBloqueadorTexto(Long seqTexto);

	public void bloquearTexto(DocDecisaoId docId) throws ServerException;
}
