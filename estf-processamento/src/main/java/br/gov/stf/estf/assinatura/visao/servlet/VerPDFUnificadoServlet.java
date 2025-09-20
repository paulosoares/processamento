package br.gov.stf.estf.assinatura.visao.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.gov.stf.estf.documento.model.service.ComunicacaoService;
import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.documento.model.service.PecaProcessoEletronicoService;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronicoComunicacao;
import br.gov.stf.estf.entidade.documento.TipoArquivo;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.TipoConfidencialidade;
import br.gov.stf.framework.util.ApplicationFactory;
import br.jus.stf.estf.montadortexto.impl.OpenOfficeMontadorTextoServiceImpl;

public class VerPDFUnificadoServlet extends VerPDFBaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7500517172407444487L;
	public static final String PARAM_SEQ_COMUNICACAO = "seqComunicacao";
	public static final String PARAM_SEQ_DOCUMENTO_COMUNICACAO = "seqDocComunicacao";
	public static final String PARAM_TIPO_ARQUIVO= "tipoArquivo";
	private static final int TAMANHO_BUFFER = 4096; // 4kb - 4096
    public static final String ASPA = "'";
    public static final String TRANSLATE_ORIGEM  = ASPA + "¡«…Õ”⁄¿»Ã“Ÿ¬ Œ‘€√’À‹·ÁÈÌÛ˙‡ËÏÚ˘‚ÍÓÙ˚„ıÎ¸:" + ASPA;
    public static final String TRANSLATE_DESTINO = ASPA + "ACEIOUAEIOUAEIOUAOEUaceiouaeiouaeiouaoeu " + ASPA;
    private static final Long  TAM_MAX_ZIP = 1500000000L; // 1.5GB de arquivos PDF
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		OutputStream os = resp.getOutputStream();
		byte[] conteudo = null;
		String nomeArquivo ="pdfUnificado";
		if(req.getParameter(PARAM_TIPO_ARQUIVO)== null || req.getParameter(PARAM_TIPO_ARQUIVO).equals("PDF")) {
			try {
				List<InputStream> pdfs = new ArrayList<InputStream>();
				ComunicacaoService comunicacaoService = (ComunicacaoService) ApplicationFactory.getInstance().getServiceLocator().getService("comunicacaoService");
				DocumentoEletronicoService documentoEletronicoService = (DocumentoEletronicoService) ApplicationFactory.getInstance().getServiceLocator().getService("documentoEletronicoService");
				String sComunicacao = req.getParameter(PARAM_SEQ_COMUNICACAO);
				String sSeqDocumentoEletronico = req.getParameter(PARAM_SEQ_DOCUMENTO_COMUNICACAO);
				if ( sSeqDocumentoEletronico!=null ) {
					Long seqDocumentoEletronico = new Long ( sSeqDocumentoEletronico );
					DocumentoEletronico documentoEletronico = documentoEletronicoService.recuperarPorId(seqDocumentoEletronico);
					if(documentoEletronico.getTipoArquivo().getCodigo().equals(TipoArquivo.PDF.getCodigo())) {
						conteudo = documentoEletronico.getArquivo();
						pdfs.add(new ByteArrayInputStream(conteudo));
					}
				} 

				Comunicacao comunicacao = null;
				Long tamArquivo = 0L;
				if ( sComunicacao!=null ) {
					Long seqComunicacao = new Long (sComunicacao);
					comunicacao = comunicacaoService.recuperarPorId(seqComunicacao);
				}

				if (comunicacao.getPecasProcessoEletronico() != null &&
						comunicacao.getPecasProcessoEletronico().size() > 0){

					for(PecaProcessoEletronicoComunicacao peca : comunicacao.getPecasProcessoEletronico()){
						if(peca.getPecaProcessoEletronico().getDocumentos().get(0).getDocumentoEletronicoView().getTipoArquivo().getCodigo().equals(TipoArquivo.PDF.getCodigo()) && tamArquivo < TAM_MAX_ZIP) {
							byte[] resPdf;
							resPdf = peca.getPecaProcessoEletronico().getDocumentos().get(0).getDocumentoEletronico().getArquivo();
							pdfs.add(new ByteArrayInputStream(resPdf));
							tamArquivo = tamArquivo + peca.getPecaProcessoEletronico().getDocumentos().get(0).getDocumentoEletronicoView().getNumTamanhoDocumento();
						}
					}
				}

				ByteArrayOutputStream resultado = new ByteArrayOutputStream();

				OpenOfficeMontadorTextoServiceImpl.concatPDFs(pdfs, resultado, false); 

				conteudo = resultado.toByteArray();                	
				if (TipoConfidencialidade.SEGREDO_JUSTICA.getDescricao().equals(comunicacao.getConfidencialidade()))
					conteudo = incluirTagConfidencialidade(conteudo);
				//resp.setContentType("application/octet-stream");
				resp.setContentType("application/x-pdf");
				resp.setHeader("Content-disposition","attachment; filename=" + nomeArquivo + ".pdf");	
				resp.setHeader("cache-control", "no-cache");			
				resp.setBufferSize(1024);
				os.write(conteudo);		


			} catch ( Exception e ) {
				throw new ServletException(e);
			} finally {
				os.flush();
				resp.flushBuffer();
			}
		}else {
			if(req.getParameter(PARAM_TIPO_ARQUIVO).equals("MID")) {
			try {
				
				//
				// Download Zip com MÌdias
				//
				
				
				List<InputStream> pdfs = new ArrayList<InputStream>();
				ComunicacaoService comunicacaoService = (ComunicacaoService) ApplicationFactory.getInstance().getServiceLocator().getService("comunicacaoService");
				PecaProcessoEletronicoService pecaProcessoEletronicoService = (PecaProcessoEletronicoService) ApplicationFactory.getInstance().getServiceLocator().getService("pecaProcessoEletronicoService");
				
				DocumentoEletronicoService documentoEletronicoService = (DocumentoEletronicoService) ApplicationFactory.getInstance().getServiceLocator().getService("documentoEletronicoService");
				String sComunicacao = req.getParameter(PARAM_SEQ_COMUNICACAO);
				String sSeqDocumentoEletronico = req.getParameter(PARAM_SEQ_DOCUMENTO_COMUNICACAO);
				if ( sSeqDocumentoEletronico!=null ) {
					Long seqDocumentoEletronico = new Long ( sSeqDocumentoEletronico );
					DocumentoEletronico documentoEletronico = documentoEletronicoService.recuperarPorId(seqDocumentoEletronico);
					if(documentoEletronico.getTipoArquivo().getCodigo().equals(TipoArquivo.PDF.getCodigo())) {
						conteudo = documentoEletronico.getArquivo();
						pdfs.add(new ByteArrayInputStream(conteudo));
					}
				} 
				int cont =1;
				Comunicacao comunicacao = null;
				
				if ( sComunicacao!=null ) {
					Long seqComunicacao = new Long (sComunicacao);
					comunicacao = comunicacaoService.recuperarPorId(seqComunicacao);
				}
				List<PecaProcessoEletronico>  todasPecasProcesso = null;
				if(req.getParameter(PARAM_TIPO_ARQUIVO).equals("TODAS")){
					todasPecasProcesso = pecaProcessoEletronicoService.pesquisarPorProcesso((Processo) comunicacao.getComunicacaoIncidentePrincipal().getObjetoIncidente().getPrincipal(), true);
				}
			
				String nomeArquivoProcesso = "pecas-" + comunicacao.getComunicacaoIncidentePrincipal().getObjetoIncidente().getPrincipal().getIdentificacao().toUpperCase().replace(" ","");
				String nomeArquivoComunicacao = aplicarTranslateEmValor(comunicacao.getDscNomeDocumento());
				
				
				int conta;
				byte[] dados = new byte[TAMANHO_BUFFER];

				BufferedInputStream origem = null;
				FileInputStream streamDeEntrada = null;
				FileOutputStream destino = null;
				ZipOutputStream saida = null;
				ZipEntry entry = null;
				destino = new FileOutputStream(new File(nomeArquivoProcesso+".zip"));
				saida = new ZipOutputStream(new BufferedOutputStream(destino));

				

				if (comunicacao.getPecasProcessoEletronico() != null &&
						comunicacao.getPecasProcessoEletronico().size() > 0){


					if(todasPecasProcesso != null) {
						int innn =1;
					}
					Long tamArquivo = 0L;
					for(PecaProcessoEletronicoComunicacao peca : comunicacao.getPecasProcessoEletronico()){
							if(peca.getPecaProcessoEletronico().getDocumentos().get(0).getDocumentoEletronicoView().getTipoArquivo().getCodigo().equals(TipoArquivo.PDF.getCodigo()) && tamArquivo < TAM_MAX_ZIP) {	
							byte[] resArq; 
								resArq = peca.getPecaProcessoEletronico().getDocumentos().get(0).getDocumentoEletronico().getArquivo();
								String nomeMidia = aplicarTranslateEmValor(cont +"_" + peca.getPecaProcessoEletronico().getTipoPecaProcesso().getDescricao() +""+peca.getPecaProcessoEletronico().getDocumentos().get(0).getDocumentoEletronico().getTipoArquivo().getExtensoes().substring(0, 4));
								File file1 = new File(nomeMidia); //Criamos um nome para o arquivo
								BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file1)); //Criamos o arquivo
								bos.write(resArq); //Gravamos os bytes l·
								bos.close(); //Fechamos o stream.
								cont ++;
								tamArquivo = tamArquivo + peca.getPecaProcessoEletronico().getDocumentos().get(0).getDocumentoEletronicoView().getNumTamanhoDocumento();
	
								try {
	
									File file = new File(nomeMidia);
									streamDeEntrada = new FileInputStream(file);
									origem = new BufferedInputStream(streamDeEntrada, TAMANHO_BUFFER);
									entry = new ZipEntry(file.getName());
									saida.putNextEntry(entry);
	
									while((conta = origem.read(dados, 0, TAMANHO_BUFFER)) != -1) {
										saida.write(dados, 0, conta);
									}
									origem.close();
									bos.flush();
									file.delete();
								} catch(IOException e) {
									throw new IOException(e.getMessage());
								}}
					}
				}

				ByteArrayOutputStream resultado = new ByteArrayOutputStream();

				OpenOfficeMontadorTextoServiceImpl.concatPDFs(pdfs, resultado, false); 

				conteudo = resultado.toByteArray();                	
				if (TipoConfidencialidade.SEGREDO_JUSTICA.getDescricao().equals(comunicacao.getConfidencialidade())) {
					conteudo = incluirTagConfidencialidade(conteudo);
				}
	
				
				File someFile = new File(nomeArquivoComunicacao+".pdf");
				FileOutputStream fos = new FileOutputStream(someFile);
				fos.write(conteudo);
				fos.flush();
				fos.close();



				File file2 = new File(someFile.getName());
				streamDeEntrada = new FileInputStream(file2);
				origem = new BufferedInputStream(streamDeEntrada, TAMANHO_BUFFER);



				entry = new ZipEntry(someFile.getName());
				saida.putNextEntry(entry);
				while((conta = origem.read(dados, 0, TAMANHO_BUFFER)) != -1) {
					saida.write(dados, 0, conta);
				}
				streamDeEntrada.close();
				fos.close();
				origem.close();


				saida.close();
				file2.delete();
				someFile.delete();

				
				int BUFF_SIZE = 1024;
				byte[] buffer = new byte[BUFF_SIZE];
				File fileMp3 = new File(nomeArquivoProcesso+".zip");
				FileInputStream fis = new FileInputStream(fileMp3);
				
			
				
				resp.setContentType("application/zip");
				resp.setHeader("Content-disposition","attachment; filename=\"" + nomeArquivoProcesso+".zip");
				resp.setHeader("cache-control", "no-cache");			
				resp.setBufferSize(1024);
				resp.setContentLength((int) fileMp3.length());
				int byteRead = 0;
				
				//Download do arquivo
				while ((byteRead = fis.read(buffer, 0, 1024)) != -1) {
					os.write(buffer, 0, byteRead);

				}
				fis.close();
				fileMp3.delete();
			} catch ( Exception e ) {
				throw new ServletException(e);
			} finally {
				os.flush();
				resp.flushBuffer();
			}
		}
			else {
				if(req.getParameter(PARAM_TIPO_ARQUIVO).equals("TODAS")) {
					try {
						
						//
						// Download Zip com MÌdias
						//
						
						
						List<InputStream> pdfs = new ArrayList<InputStream>();
						ComunicacaoService comunicacaoService = (ComunicacaoService) ApplicationFactory.getInstance().getServiceLocator().getService("comunicacaoService");
						PecaProcessoEletronicoService pecaProcessoEletronicoService = (PecaProcessoEletronicoService) ApplicationFactory.getInstance().getServiceLocator().getService("pecaProcessoEletronicoService");
						
						DocumentoEletronicoService documentoEletronicoService = (DocumentoEletronicoService) ApplicationFactory.getInstance().getServiceLocator().getService("documentoEletronicoService");
						String sComunicacao = req.getParameter(PARAM_SEQ_COMUNICACAO);
						String sSeqDocumentoEletronico = req.getParameter(PARAM_SEQ_DOCUMENTO_COMUNICACAO);
						if ( sSeqDocumentoEletronico!=null ) {
							Long seqDocumentoEletronico = new Long ( sSeqDocumentoEletronico );
							DocumentoEletronico documentoEletronico = documentoEletronicoService.recuperarPorId(seqDocumentoEletronico);
							if(documentoEletronico.getTipoArquivo().getCodigo().equals(TipoArquivo.PDF.getCodigo())) {
								conteudo = documentoEletronico.getArquivo();
								pdfs.add(new ByteArrayInputStream(conteudo));
							}
						} 
						int cont =1;
						Comunicacao comunicacao = null;
						
						if ( sComunicacao!=null ) {
							Long seqComunicacao = new Long (sComunicacao);
							comunicacao = comunicacaoService.recuperarPorId(seqComunicacao);
						}
						List<PecaProcessoEletronico>  todasPecasProcesso = null;
		
							todasPecasProcesso = pecaProcessoEletronicoService.pesquisarPorProcesso((Processo) comunicacao.getComunicacaoIncidentePrincipal().getObjetoIncidente().getPrincipal(), true);
						
						String nomeArquivoProcesso = "pecas-" + comunicacao.getComunicacaoIncidentePrincipal().getObjetoIncidente().getPrincipal().getIdentificacao().toUpperCase().replace(" ","");
						String nomeArquivoComunicacao = aplicarTranslateEmValor(comunicacao.getDscNomeDocumento());
						
						int conta;
						byte[] dados = new byte[TAMANHO_BUFFER];

						BufferedInputStream origem = null;
						FileInputStream streamDeEntrada = null;
						FileOutputStream destino = null;
						ZipOutputStream saida = null;
						ZipEntry entry = null;
						destino = new FileOutputStream(new File(nomeArquivoProcesso+".zip"));
						saida = new ZipOutputStream(new BufferedOutputStream(destino));

						

						if (comunicacao.getPecasProcessoEletronico() != null &&
								comunicacao.getPecasProcessoEletronico().size() > 0){

							Long tamArquivo = 0L;
							for(PecaProcessoEletronico peca : todasPecasProcesso){
								try {
								byte[] resArq; 
									if(peca.getDocumentos().get(0).getDocumentoEletronicoView().getTipoArquivo().getCodigo().equals(TipoArquivo.PDF.getCodigo()) && tamArquivo < TAM_MAX_ZIP) {
									resArq = peca.getDocumentos().get(0).getDocumentoEletronico().getArquivo();
									String nomeMidia = aplicarTranslateEmValor(cont +"-"+peca.getNumeroOrdemPeca() +"_" + peca.getTipoPecaProcesso().getDescricao() +""+peca.getDocumentos().get(0).getDocumentoEletronico().getTipoArquivo().getExtensoes().substring(0, 4));
									File file1 = new File(nomeMidia); 
									BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file1));
									bos.write(resArq); 
									bos.close(); 
									cont ++;
									tamArquivo = tamArquivo + peca.getDocumentos().get(0).getDocumentoEletronicoView().getNumTamanhoDocumento();

									

										File file = new File(nomeMidia);
										streamDeEntrada = new FileInputStream(file);
										origem = new BufferedInputStream(streamDeEntrada, TAMANHO_BUFFER);
										entry = new ZipEntry(file.getName());
										saida.putNextEntry(entry);

										while((conta = origem.read(dados, 0, TAMANHO_BUFFER)) != -1) {
											saida.write(dados, 0, conta);
										}
										origem.close();
										bos.flush();
										file.delete();
										}
									
									} catch(IOException e) {
										throw new IOException(e.getMessage());
									}
							}
						}

						ByteArrayOutputStream resultado = new ByteArrayOutputStream();

						OpenOfficeMontadorTextoServiceImpl.concatPDFs(pdfs, resultado, false); 

						conteudo = resultado.toByteArray();                	
						if (TipoConfidencialidade.SEGREDO_JUSTICA.getDescricao().equals(comunicacao.getConfidencialidade())) {
							conteudo = incluirTagConfidencialidade(conteudo);
						}
			
						
						File someFile = new File(aplicarTranslateEmValor(nomeArquivoComunicacao)+".pdf");
						FileOutputStream fos = new FileOutputStream(someFile);
						fos.write(conteudo);
						fos.flush();
						fos.close();



						File file2 = new File(someFile.getName());
						streamDeEntrada = new FileInputStream(file2);
						origem = new BufferedInputStream(streamDeEntrada, TAMANHO_BUFFER);



						entry = new ZipEntry(someFile.getName());
						saida.putNextEntry(entry);
						while((conta = origem.read(dados, 0, TAMANHO_BUFFER)) != -1) {
							saida.write(dados, 0, conta);
						}
						streamDeEntrada.close();
						fos.close();
						origem.close();


						saida.close();
						file2.delete();
						someFile.delete();

						
						int BUFF_SIZE = 1024;
						byte[] buffer = new byte[BUFF_SIZE];
						File fileMp3 = new File(nomeArquivoProcesso+".zip");
						FileInputStream fis = new FileInputStream(fileMp3);
						
					
						
						resp.setContentType("application/zip");
						resp.setHeader("Content-disposition","attachment; filename=\"" + nomeArquivoProcesso+".zip");
						resp.setHeader("cache-control", "no-cache");			
						resp.setBufferSize(1024);
						resp.setContentLength((int) fileMp3.length());
						int byteRead = 0;
						
						//Download do arquivo
						while ((byteRead = fis.read(buffer, 0, 1024)) != -1) {
							os.write(buffer, 0, byteRead);

						}
						fis.close();
						fileMp3.delete();
					} catch ( Exception e ) {
						throw new ServletException(e);
					} finally {
						os.flush();
						resp.flushBuffer();
					}
				}
			}
	}	}
	

    private static String aplicarTranslateEmValor(String valor) {
    	String resultado = valor;
    	for (int posicao = 0; posicao < TRANSLATE_ORIGEM.length(); posicao++) {
			char caracterOrigem = TRANSLATE_ORIGEM.charAt(posicao);
			char caracterDestino = TRANSLATE_DESTINO.charAt(posicao);
			resultado = resultado.replace(caracterOrigem, caracterDestino);
		}
    	resultado = resultado.replace("\\", "_");
    	
    	return resultado.replace("/", "_");
    }
	
	
}
