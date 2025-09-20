package br.gov.stf.estf.publicacao.compordj.builder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleConstants.ColorConstants;
import javax.swing.text.rtf.RTFEditorKit;

import com.lowagie.text.Font;

import br.gov.stf.framework.util.ApplicationFactory;
import br.gov.stf.framework.util.DateTimeHelper;

public class BuilderHelper {
	
	public static byte[] stringToRtf(String texto) throws BadLocationException, IOException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			RTFEditorKit rtfKit = new RTFEditorKit();
			Document doc = rtfKit.createDefaultDocument();
			doc.insertString(0, texto, null);
			rtfKit.write(baos, doc, 0, doc.getLength());
			byte[] bytes = baos.toByteArray();
			baos.close();
			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	} 
	
	public static String rtfToString(byte[] rtf) {
		try {
			RTFEditorKit rtfEditor = new RTFEditorKit();
			Document doc = rtfEditor.createDefaultDocument();
			rtfEditor.read(new ByteArrayInputStream(rtf), doc, 0);
			return doc.getText(0, doc.getLength());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static byte[] apendarRtfTextoIgual (byte[] rtfPadrao, int indice) throws IOException, BadLocationException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ByteArrayInputStream bais = new ByteArrayInputStream( rtfPadrao );
		RTFEditorKit rtfKit = new RTFEditorKit();
		Document doc = rtfKit.createDefaultDocument();
		rtfKit.read(bais, doc, 0);
		doc.insertString(doc.getLength(), indice+"", null);
		rtfKit.write(baos, doc, 0, doc.getLength());
		byte[] bytes = baos.toByteArray();
		baos.close();
		return new String(bytes).replace("\\par", "").getBytes();
	}
	
	public static byte[] montarTextoPautaJulgamentoIdentico (String ...assuntos) throws BadLocationException, IOException {
		byte[] rtf = null;
		if ( assuntos!=null ) {
			MutableAttributeSet negrito = new SimpleAttributeSet();
			negrito.addAttribute(ColorConstants.Bold, true);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			RTFEditorKit rtfKit = new RTFEditorKit();
			Document doc = rtfKit.createDefaultDocument();
			doc.insertString(0, "Matéria:\n", negrito);
			for ( int i=0 ; i<assuntos.length ; i++ ) {
				doc.insertString(doc.getLength(), assuntos[i].trim()+"\n", null);
			}
			rtfKit.write(baos, doc, 0, doc.getLength());
			rtf = baos.toByteArray();
			baos.close();			
		}
		return rtf;		
		
	}
	
	public static byte[] montarTextoInicialDespachosIdenticos (String nomeMinistro, boolean isFemea) throws BadLocationException, IOException {
		byte[] rtf = null;
		if ( nomeMinistro!=null ) {
			MutableAttributeSet negrito = new SimpleAttributeSet();
			negrito.addAttribute(ColorConstants.Bold, true);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			RTFEditorKit rtfKit = new RTFEditorKit();
			Document doc = rtfKit.createDefaultDocument();
			String texto = "Processos com Despachos Idênticos:\nRELATOR";
			if ( isFemea ) {
				texto += "A";
			}
			
			doc.insertString(0, texto+": "+nomeMinistro, negrito);
			rtfKit.write(baos, doc, 0, doc.getLength());
			rtf = baos.toByteArray();
			baos.close();			
		}
		return rtf;
	}
	
	public static byte[] montarTextoInicialEmentasIdenticas (String nomeMinistro, boolean isFemea) throws BadLocationException, IOException {
		byte[] rtf = null;
		if ( nomeMinistro!=null ) {
			MutableAttributeSet negrito = new SimpleAttributeSet();
			negrito.addAttribute(ColorConstants.Bold, true);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			RTFEditorKit rtfKit = new RTFEditorKit();
			Document doc = rtfKit.createDefaultDocument();
			String texto = "Processos com Ementas Idênticas:\nRELATOR";
			if ( isFemea ) {
				texto += "A";
			}
			
			doc.insertString(0, texto+": "+nomeMinistro, negrito);
			rtfKit.write(baos, doc, 0, doc.getLength());
			rtf = baos.toByteArray();
			baos.close();			
		}
		return rtf;
	}
	
	public static byte[] montarTextoInicialDecisoesIdenticas (String nomeMinistro, boolean isFemea) throws BadLocationException, IOException {
		byte[] rtf = null;
		if ( nomeMinistro!=null ) {
			MutableAttributeSet negrito = new SimpleAttributeSet();
			negrito.addAttribute(ColorConstants.Bold, true);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			RTFEditorKit rtfKit = new RTFEditorKit();
			Document doc = rtfKit.createDefaultDocument();
			String texto = "Processos com Decisões Idênticas:\nRELATOR";
			if ( isFemea ) {
				texto += "A";
			}
			
			doc.insertString(0, texto+": "+nomeMinistro, negrito);
			rtfKit.write(baos, doc, 0, doc.getLength());
			rtf = baos.toByteArray();
			baos.close();			
		}
		return rtf;
	}
	
	public static byte[] montarTextoInicialTextoIdentico (String texto) throws BadLocationException, IOException {
		MutableAttributeSet negrito = new SimpleAttributeSet();
		negrito.addAttribute(ColorConstants.Bold, true);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		RTFEditorKit rtfKit = new RTFEditorKit();
		Document doc = rtfKit.createDefaultDocument();
		doc.insertString(0, texto, negrito);
		rtfKit.write(baos, doc, 0, doc.getLength());
		return baos.toByteArray();	
	}
	
	public static byte[] montarTextoDecisaoDespachoIdentico (String tipo, String complemento) throws BadLocationException, IOException {
		byte[] rtf = null;
		MutableAttributeSet format = new SimpleAttributeSet();
		format.addAttribute(ColorConstants.Bold, true);
		format.addAttribute(StyleConstants.Underline, true);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		RTFEditorKit rtfKit = new RTFEditorKit();
		Document doc = rtfKit.createDefaultDocument();
		doc.insertString(0, tipo, format);
		doc.insertString(doc.getLength(), complemento, null);
		
		rtfKit.write(baos, doc, 0, doc.getLength());
		rtf = baos.toByteArray();
		baos.close();	
		return rtf;
	}
	
	public static byte[] montarTextoDecisaoJulgamento (String tipo, String textoDecisao) throws BadLocationException, IOException {
		byte[] rtfIntermediario;
		ByteArrayOutputStream baosIntermediario = new ByteArrayOutputStream();
		com.lowagie.text.Phrase textoNegrito = new com.lowagie.text.Phrase(tipo, new Font(Font.COURIER, 12, Font.BOLD));	
		com.lowagie.text.Phrase textoConteudo = new com.lowagie.text.Phrase(textoDecisao, new Font(Font.COURIER, 12));
		com.lowagie.text.Document documentoIntermediario = new com.lowagie.text.Document();
		com.lowagie.text.rtf.RtfWriter2 writer = com.lowagie.text.rtf.RtfWriter2.getInstance(documentoIntermediario, baosIntermediario); 
		documentoIntermediario.open();
		com.lowagie.text.Paragraph p = new com.lowagie.text.Paragraph();
	    p.add(textoNegrito);
	    p.add(textoConteudo);	   
	    
	    p.setAlignment(com.lowagie.text.Element.ALIGN_JUSTIFIED);
	    p.setFirstLineIndent(70.91F); //2,5 cm  
		try {
			documentoIntermediario.add(p);
		} catch (com.lowagie.text.DocumentException e) {
				// TODO Auto-generated catch block
			throw new IOException(e);
		}
		documentoIntermediario.close();
		String textoFinal = baosIntermediario.toString().replaceAll("Courier", "Courier New");
		rtfIntermediario = textoFinal.getBytes();		
		return rtfIntermediario;

	}
	
	public static byte[] montarTextoDecisaoIdentico () throws BadLocationException, IOException {
		return montarTextoDecisaoDespachoIdentico("Decisão: ","Idêntica à de nº ");
	}
	
	public static byte[] montarTextoDespachoIdentico () throws BadLocationException, IOException {
		return montarTextoDecisaoDespachoIdentico("Despacho: ","Idêntico ao de nº ");
	}
	
	public static byte[] montarTextoEmentaIdentica () throws BadLocationException, IOException {
		return montarTextoDecisaoDespachoIdentico("Ementa: ","Idêntica ao de nº ");
	}
	
	public static String getDataAtual(Date date){
		StringBuffer buffer = new StringBuffer();
		buffer.append( DateTimeHelper.getDia(date));	
		buffer.append(" de "+ 	DateTimeHelper.getDescricaoMes(date).toLowerCase());
		buffer.append(" de "+ 	DateTimeHelper.getAno(date));
	
		return buffer.toString();
	}
	
	public static Object getServico(String servico){
		return ApplicationFactory.getInstance().getServiceLocator().getService(servico);
	}
}