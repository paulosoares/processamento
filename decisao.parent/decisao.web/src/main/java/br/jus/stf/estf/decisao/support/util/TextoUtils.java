package br.jus.stf.estf.decisao.support.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.rtf.RTFEditorKit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.gov.stf.estf.entidade.documento.Texto;


/**
 * Oferece métodos utilitários para manipulação de textos.
 * 
 * @author Rodrigo Barreiros
 * @since 30.04.2010
 */
public final class TextoUtils {
	
	private static Log logger = LogFactory.getLog(TextoUtils.class);
	
	private static final String MENSAGEM_ERRO_RTF = "<html><body><span style=\"font-weight: bold; color: red; text-align: left;\">O arquivo do texto está apresentando problemas. Para tentar visualizá-lo neste painel, edite o texto (abrir e salvar somente) através do STF-Office.</span></body></html>";
	
	/**
	 * Construtor de classes utilitárias deve ser escondido.
	 */
	private TextoUtils() {
	}

	/**
	 * Converte um texto no formato RTF para um texto no formato HTML.
	 * 
	 * @param text o texto em rtf.
	 * @return o texto em html
	 */
	public static String convertRtfToHtml(String text) {
		//Comentado em função do DECISAO-2460, porém não foi definido a situação do DECISAO-2191
		//String textEnDash = text.replace("\\'96", "\\endash").replace("\\'93", "\\ldblquote").replace("\\'94", "\\rdblquote");
		//StringReader reader = new StringReader(textEnDash);
		
		StringReader reader = new StringReader(text);
		StringWriter writer = new StringWriter();
		RTFEditorKit rtfEditorKit = new RTFEditorKit();
		HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
		Document doc = rtfEditorKit.createDefaultDocument();
		try {
			rtfEditorKit.read(reader, doc, 0);
			htmlEditorKit.write(writer, doc, 0, doc.getLength());
		} catch (Exception ex) {
			logger.error("Erro ao converter rtf para html.", ex);
			return MENSAGEM_ERRO_RTF;
		}
		return writer.toString();
	}
	
	/** Converte um arquivo RTF em String. */
	public static String converterRtfToString(byte[] arquivoEletronico){
		String retorno = new String();
		RTFEditorKit rtfParser = new RTFEditorKit();
		Document document = rtfParser.createDefaultDocument();
		try {
			rtfParser.read(new ByteArrayInputStream( arquivoEletronico ), document, 0);
			retorno = document.getText(0, document.getLength());
		} catch (IOException e) {
			logger.error("Erro ao converter o arquivo RTF para String.", e);
		} catch (BadLocationException e) {
			logger.error("Erro ao converter o arquivo RTF para String.", e);						
		}
		return retorno;
	}
	
	
	public static String montarNomeDoTexto(Texto texto) {
		StringBuffer nome = new StringBuffer();
		nome.append(texto.getObjetoIncidente().getIdentificacao());
		nome.append("-");
		nome.append(texto.getTipoTexto().getDescricao());
		return nome.toString();
	}

}
