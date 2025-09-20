package br.gov.stf.estf.documento.model.service.impl;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Calendar;

import javax.swing.text.MaskFormatter;

import org.springframework.stereotype.Service;

import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import br.gov.stf.estf.documento.model.dataaccess.AssinaturaDigitalDao;
import br.gov.stf.estf.documento.model.service.AssinaturaDigitalService;
import br.gov.stf.estf.entidade.documento.AssinaturaDigital;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("assinaturaDigitalService")
public class AssinaturaDigitalServiceImpl extends GenericServiceImpl<AssinaturaDigital, Long, AssinaturaDigitalDao> 
	implements AssinaturaDigitalService {
    public static final String TEXTO_ASSINATURA_ELETRONICA = "Documento assinado eletronicamente pelo(a) %s, conforme o Art. 205, § 2º, do CPC. O documento pode ser acessado pelo endereço http://www.stf.jus.br/portal/autenticacao/autenticarDocumento.asp sob o código %s e senha %s";
	public static final String TEXTO_ASSINATURA_DIGITAL = "Documento assinado digitalmente conforme MP n° 2.200-2/2001 de 24/08/2001. O documento pode ser acessado pelo endereço http://www.stf.jus.br/portal/autenticacao/autenticarDocumento.asp sob o código %s e senha %s";

	public AssinaturaDigitalServiceImpl(AssinaturaDigitalDao dao) { super(dao); }

	public static String getRodapeAssinaturaDigital(String hashValidacao) {
		return String.format(
				TEXTO_ASSINATURA_DIGITAL,
				formatarHashValidacao(hashValidacao.substring(0, 16)),
				formatarHashValidacao(hashValidacao.substring(16))
		);	
	}

	public static String getRodapeAssinaturaEletronica(Ministro ministro, String hashValidacao) {
		return String.format(
				TEXTO_ASSINATURA_ELETRONICA, 
				ministro.getNomeMinistroCapsulado(true, false, true), 
				formatarHashValidacao(hashValidacao.substring(0, 16)), 
				formatarHashValidacao(hashValidacao.substring(16))
		);	
	}

	public static String formatarHashValidacao(String hashValidacao) {
		try {
			if (hashValidacao !=null && hashValidacao.length() == 32) {
				MaskFormatter formatter = new MaskFormatter("AAAA-AAAA-AAAA-AAAA-AAAA-AAAA-AAAA-AAAA");
				formatter.setValueContainsLiteralCharacters(false);
				return formatter.valueToString(hashValidacao);
			}
			
			if (hashValidacao !=null && hashValidacao.length() == 16) {
				MaskFormatter formatter = new MaskFormatter("AAAA-AAAA-AAAA-AAAA");
				formatter.setValueContainsLiteralCharacters(false);
				return formatter.valueToString(hashValidacao);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return hashValidacao;
	}

	public static String gerarHashValidacao() {
		try {
			Double random = Math.random();
			random += Calendar.getInstance().getTimeInMillis();
		
			String plaintext = String.valueOf(random);
		
			MessageDigest m = MessageDigest.getInstance("MD5");
			
			m.reset();
			m.update(plaintext.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			String hashtext = bigInt.toString(16);
			
			while(hashtext.length() < 32)
			  hashtext = "0"+hashtext;
			
			return hashtext.toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static byte[] adicionarRodapePdf(byte[] pdf, String texto) throws ServiceException {
		byte[] novoPdf = null;

		try {
			int i = 0;
			PdfReader arquivoPDF = new PdfReader(pdf);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfStamper stp = new PdfStamper(arquivoPDF, baos);
			while (i < arquivoPDF.getNumberOfPages()) {
				i++;
				PdfContentByte conteudoPDF = stp.getOverContent(i);
				ColumnText rodape = new ColumnText(conteudoPDF);
				rodape.setSimpleColumn(new Phrase(texto, FontFactory.getFont(FontFactory.HELVETICA, 8)), 20, 11, arquivoPDF.getPageSize(i).getWidth() - 20, 35, 10, Element.ALIGN_LEFT);
				rodape.go();
			}
			stp.close();
			arquivoPDF.close();
			novoPdf = baos.toByteArray();
		} catch (Exception e) {
			throw new ServiceException("Erro ao inserir rodapé no PDF", e);
		}

		return novoPdf;
	}
}
