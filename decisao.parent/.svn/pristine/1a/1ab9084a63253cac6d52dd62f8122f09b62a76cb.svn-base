package br.jus.stf.estf.decisao;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.jus.stf.estf.decisao.exception.ServerException;
import br.jus.stf.estf.decisao.inter.IServer;
import br.jus.stf.stfoffice.client.remoting.RemotingException;
import br.jus.stf.stfoffice.servlet.DocumentoId;
import br.jus.stf.stfoffice.support.STFOfficeFileManager;

public class DecisaoService {
	private static final Log log = LogFactory.getLog(DecisaoService.class);
	private static final long MAXIMO_TEMPO_OCIOSO = 5 * 60 * 60 * 1000;
	private final IServer server;
	private long ultimoComando;
	private final SimpleDateFormat simpleDateFormat;
	private static final Long CODIGO_DECISAO_SOBRE_REPERCURSAO_GERAL = 55L;

	public DecisaoService(IServer server) throws RemotingException {
		this.server = server;
		this.ultimoComando = System.currentTimeMillis();
		simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		System.out.println("verificando atualização do codigo");
		new KeepAlive().start();

	}

	class KeepAlive extends Thread {
	
		public KeepAlive() {
			setDaemon(true);
		}
		@Override
		public void run() {
			while (true) {
				long tempoEspera = System.currentTimeMillis() - ultimoComando;
				if (tempoEspera > MAXIMO_TEMPO_OCIOSO) {
					try {
						log.info("Enviando requisição para o servidor: 'keep alive'");
						manterSessaoUsuarioServidor();
					} catch (ServerException e) {
						log.error("Erro ao manter sessão do usuário", e);
					}
				}
				tempoEspera = System.currentTimeMillis() - ultimoComando;
				long espera = MAXIMO_TEMPO_OCIOSO - tempoEspera;
				log.info("Esperando por mais " + espera + " ms para verificar próximo ociosidade");
				try {
					Thread.sleep(espera);
				} catch (Exception e) {
					log.error("Erro ao esperar thread", e);
				}
			}
		}
	}

	public void recuperarTemplateDespacho(File out) throws ServerException, IOException {
		byte[] template = null;
		try {
			// template =
			// IOUtils.toByteArray(server.recuperarTemplateDespacho());
			template = IOUtils.toByteArray(recuperarTemplateDespacho());
			this.ultimoComando = System.currentTimeMillis();
		} catch (Exception e) {
			throw new ServerException("Erro ao recuperarTemplateDespacho", e);
		}
		ByteArrayInputStream is = new ByteArrayInputStream(template);
		FileOutputStream fos = new FileOutputStream(out);
		IOUtils.copy(is, fos);
	}

	public InputStream recuperarTemplateDespacho() throws ServerException {

		try {
			return DecisaoService.class.getClassLoader().getResourceAsStream("cabecalho_template.odt");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServerException("Arquivo não encontrado.", e);
		}

	}

	public Boolean verificaDocumentoReadOnlyDecisaoRepercussaoGeral(Long seqObjetoIncidente) throws ServerException {
		return server.verificaDocumentoReadOnlyDecisaoRepercussaoGeral(seqObjetoIncidente);
	}
	
	public Boolean verificaPorTextoDocumentoReadOnlyDecisaoRepercussaoGeral(Long seqTexto) throws ServerException {
		return server.verificaPorTextoDocumentoReadOnlyDecisaoRepercussaoGeral(seqTexto);
	}
	
	public Boolean recuperarPossuiVotoDivergente(Long seqObjetoIncidente) throws ServerException {
		return server.recuperarPossuiVotoDivergente(seqObjetoIncidente);
	}	
	
	public boolean verificarPodeCriarDecisaoRepercussaoGeral(Long seqObjetoIncidente) throws ServerException {
		return server.verificarPodeCriarDecisaoRepercussaoGeral(seqObjetoIncidente);
	}

	public void salvarDocumento(DocDecisaoId id, File file) throws FileNotFoundException, ServerException {
		if (log.isTraceEnabled()) {
			log.trace("salvarDocumento: id = " + id);
		}

		Long seqTexto = null;
		if (id instanceof DocNovaDecisaoId && id.getSeqTexto() == null) {
			try {
				DocNovaDecisaoId novaId = (DocNovaDecisaoId) id;
				String tipoVotoId = novaId.getTipoVotoId();
				seqTexto = server.salvarNovoArquivo(novaId.getObjetoIncidente(), novaId.getTipoTexto(), tipoVotoId, novaId
						.getObjetoIncidentePai(), novaId.getIdTipoIncidenteJulgamento(), novaId.getResponsavel(), novaId
						.getObservacao(), novaId.getMinistroDivergente(), new BufferedInputStream(new FileInputStream(file)));
				this.ultimoComando = System.currentTimeMillis();
				id.setSeqTexto(seqTexto);
			} catch (Exception e) {
				e.printStackTrace();
				throw new ServerException("Erro em salvar novo documento", e);
			}
		} else {
			try {
				seqTexto = id.getSeqTexto();
				this.ultimoComando = System.currentTimeMillis();
				
				if (id.getSomenteLeitura())
					log.trace("salvarDocumento: documento aberto no modo somente leitura não será salvo no banco de dados");
				else
					server.salvarArquivo(seqTexto, new BufferedInputStream(new FileInputStream(file)));
			} catch (Exception e) {
				e.printStackTrace();
				throw new ServerException("Erro em salvar documento existente", e);
			}
		}			
	}

	public void salvarPDF(DocDecisaoId id, File pdf) throws FileNotFoundException, ServerException {
		if (log.isTraceEnabled()) {
			log.trace("salvarPDF: file = " + pdf);
		}
		try {
			server.salvarPDF(id.getSeqTexto(), new BufferedInputStream(new FileInputStream(pdf)));
			this.ultimoComando = System.currentTimeMillis();
		} catch (Exception e) {
			throw new ServerException("Erro em recuperarDocumento", e);
		}
	}

	public List<DecisaoVersaoInfo> recuperarVersoesDocumento(DocDecisaoId id) throws ServerException, IOException {
		log.info("recuperarDocumento: server = " + server + ", id = " + id);
		try {
			List<DecisaoVersaoInfo> versoesDocumento = server.recuperarVersoesArquivo(id.getSeqTexto());
			this.ultimoComando = System.currentTimeMillis();
			for (Object o : versoesDocumento) {
				log.info("   >>>>  " + o);
			}

			for (DecisaoVersaoInfo idVersao : versoesDocumento) {
				idVersao.setNome(id.getNome() + " - " + idVersao.getDescricaoFaseTexto() + " - "
						+ simpleDateFormat.format(idVersao.getDataInclusao()));

			}

			return versoesDocumento;
		} catch (Exception e) {
			throw new ServerException("Erro em recuperarDocumento", e);
		}
	}

	public ConfiguracaoTexto recuperarConfiguracaoTextoSetor(DocDecisaoId id) throws ServerException {
		this.ultimoComando = System.currentTimeMillis();
		return server.recuperarConfiguracaoTextoSetor(id.getCodigoSetor());
	}

	public void manterSessaoUsuarioServidor() throws ServerException {
		this.ultimoComando = System.currentTimeMillis();
		server.manterSessaoUsuario();
	}

	public String verificaDocumentoBloqueado(Long seqTexto, boolean somenteLeitura) throws ServerException {
		try {
			return server.verificaDocumentoBloqueado(seqTexto, somenteLeitura);
		} catch (Exception e) {
			throw new ServerException(e);
		}
	}

	public void desbloquearDocumento(Long seqTexto) throws ServerException {
		try {
			server.desbloquearDocumento(seqTexto);
		} catch (Exception e) {
			throw new ServerException(e);
		}
	}

	public File recuperarTemplateDoTexto(DocDecisaoId id) throws ServerException {
		if (log.isTraceEnabled()) {
			log.trace("recuperarTemplateDoTexto: " + id);
		}
		InputStream template = null;
		try {
			if (id instanceof DocNovaDecisaoId) {
				DocNovaDecisaoId novaId = (DocNovaDecisaoId) id;
				template = server.recuperarTemplateDoObjetoIncidente(novaId.getObjetoIncidente(), novaId.getTipoTexto(), id
						.getSeqFaseTextoProcesso());
				this.ultimoComando = System.currentTimeMillis();
			} else {
				template = server.recuperarTemplateDoTexto(id.getSeqTexto(), id.getSeqFaseTextoProcesso(), id.getTipoTexto());
				this.ultimoComando = System.currentTimeMillis();
			}
//			File arquivoTemplate = File.createTempFile("template-fill-decisao", ".odt");
			File arquivoTemplate = STFOfficeFileManager.getInstancia().criaArquivoTemporario(id, ".odt");

			FileOutputStream fos = new FileOutputStream(arquivoTemplate);
			IOUtils.copy(template, fos);

			return arquivoTemplate;
		} catch (Exception e) {
			throw new ServerException("Erro em recuperarTemplateDoTexto", e);
		}
	}

	public File removerCabecalhoDoTexto(File arquivoComCabecalho, DocumentoId docId) throws ServerException {
		try {
			FileInputStream arquivo = new FileInputStream(arquivoComCabecalho);
			ByteArrayInputStream input = new ByteArrayInputStream(IOUtils.toByteArray(arquivo));
			InputStream textoSemCabecalho = server.excluirCabecalhoDoDocumento(input);
//			File arquivoSemCabecalho = File.createTempFile("decisao_sem_cabecalho", ".odt");
			File arquivoSemCabecalho = STFOfficeFileManager.getInstancia().criaArquivoTemporario(docId,".odt","decisao_sem_cabecalho");
			// ByteArrayInputStream is = new ByteArrayInputStream(template);
			FileOutputStream fos = new FileOutputStream(arquivoSemCabecalho);
			IOUtils.copy(textoSemCabecalho, fos);
			return arquivoSemCabecalho;

		} catch (Exception e) {
			throw new ServerException("Erro em removerCabecalhoDoTexto", e);
		}

	}
	
	public File removerCabecalhoDaRepercussaoGeral(File arquivoComCabecalho, DocumentoId docId) throws ServerException {
		try {
			FileInputStream arquivo = new FileInputStream(arquivoComCabecalho);
			ByteArrayInputStream input = new ByteArrayInputStream(IOUtils.toByteArray(arquivo));
			InputStream textoSemCabecalho = server.excluirCabecalhoDoDocumento(input);
//			File arquivoSemCabecalho = File.createTempFile("decisao_rep_geral_sem_cabecalho", ".odt");
			File arquivoSemCabecalho = STFOfficeFileManager.getInstancia().criaArquivoTemporario(docId, ".odt", "sem_cabecalho");
			FileOutputStream fos = new FileOutputStream(arquivoSemCabecalho);
			IOUtils.copy(textoSemCabecalho, fos);
			return arquivoSemCabecalho;

		} catch (Exception e) {
			throw new ServerException("Erro em removerCabecalhoDoTexto", e);
		}

	}
	
	public File excluirEmentaDoDocumento(File arquivoComEmenta, DocumentoId docId) throws ServerException {
		try {
			FileInputStream arquivo = new FileInputStream(arquivoComEmenta);
			ByteArrayInputStream input = new ByteArrayInputStream(IOUtils.toByteArray(arquivo));
			InputStream textoSemEmenta = server.excluirEmentaDoDocumento(input);
//			File arquivoSemEmenta = File.createTempFile("decisao_rep_geral_sem_ementa", ".odt");
			File arquivoSemEmenta = STFOfficeFileManager.getInstancia().criaArquivoTemporario(docId, ".odt");
			FileOutputStream fos = new FileOutputStream(arquivoSemEmenta);
			IOUtils.copy(textoSemEmenta, fos);
			return arquivoSemEmenta;

		} catch (Exception e) {
			throw new ServerException("Erro em remover ementa da repercussão geral", e);
		}

	}
	
	public void odtToRtf(File odtSemCabecalho, File novoArquivoRtf) throws ServerException {
		try {
			FileInputStream stream = new FileInputStream(odtSemCabecalho);
			InputStream arquivoRtf = server.converteOdtParaRtf(stream);
			FileOutputStream arquivoConvertido = new FileOutputStream(novoArquivoRtf);
			IOUtils.copy(arquivoRtf, arquivoConvertido);
		} catch (Exception e) {
			throw new ServerException("Erro em odtToRtf", e);
		}

	}
	
	public String verificaPerfilEditarTexto(Long idTexto) throws ServerException {
		try {
			return server.verificaPerfilEditarTexto(idTexto);
		} catch (Exception e) {
			throw new ServerException(e);
		}
	}

	public String getUsuarioBloqueadorTexto(Long seqTexto) {
		return server.getUsuarioBloqueadorTexto(seqTexto);
	}
	
	public void desbloquearDocumentoAdmin(Long seqTexto) throws ServerException {
		try {
			server.desbloquearDocumentoAdmin(seqTexto);
		} catch (Exception e) {
			throw new ServerException(e);
		}
	}
	
	public boolean isTextoEmEdicaoConcorrente(Long seqTexto) throws ServerException {
		return server.isTextoEmEdicaoConcorrente(seqTexto);
	}
	
	public boolean isUsuarioDesbloqueadorTextos() {
		return server.isUsuarioDesbloqueadorTextos();
	}

	public void bloquearTexto(DocDecisaoId docId) throws ServerException {
		server.bloquearTexto(docId);
	}

}