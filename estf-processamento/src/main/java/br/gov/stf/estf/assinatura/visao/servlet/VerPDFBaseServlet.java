package br.gov.stf.estf.assinatura.visao.servlet;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServlet;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.sun.star.awt.FontFamily;

public abstract class VerPDFBaseServlet extends HttpServlet {

	private static final long serialVersionUID = 5387726428211470512L;

	protected byte[] incluirTagConfidencialidade(byte[] pdf) throws IOException, DocumentException{
		ByteArrayOutputStream retorno = new ByteArrayOutputStream(); 
		PdfReader reader = new PdfReader(pdf);
		PdfStamper stamper = new PdfStamper(reader, retorno);		
        Font f = new Font(FontFamily.MODERN, 18, Font.BOLD, Color.WHITE);		
        Chunk textAsChunk = new Chunk("SEGREDO DE JUSTIÇA", f);        
        textAsChunk.setBackground(Color.RED, 5 , 8, 5, 10);
        //textAsChunk.setTextRise(-20);        
        for( int i = 1; i <= reader.getNumberOfPages(); i++){        
	        Rectangle pageSize = reader.getPageSize(i);
	        PdfContentByte over = stamper.getOverContent(i);       
	        Phrase p = new Phrase(textAsChunk); 
	        over.saveState();       
	        ColumnText.showTextAligned(over, Element.ALIGN_TOP, p, pageSize.getWidth() - 230, pageSize.getHeight() - 40, 0);
	        over.restoreState();
        }
        stamper.close();
        reader.close();
	    return retorno.toByteArray();    
	}
}
