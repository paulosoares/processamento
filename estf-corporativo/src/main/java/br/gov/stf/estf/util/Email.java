package br.gov.stf.estf.util;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Email {

	public static void enviar(String from, String[] to, String[] cc, String[] cco, String assunto, String corpoEmail, File[] anexos) throws MessagingException {	

		
		MimeMessage msg = enviar(from, to, cc, cco, assunto, corpoEmail, anexos, Boolean.FALSE);		

		// envia a mensagem
		Transport.send(msg);
	}
	
	public static void enviar(Boolean htmlContent, String from, String[] to, String[] cc, String[] cco, String assunto, String corpoEmail, File[] anexos) throws MessagingException {
		MimeMessage msg = enviar(from, to, cc, cco, assunto, corpoEmail, anexos, htmlContent);		

		// envia a mensagem
		Transport.send(msg);
	
	}
	
	private static MimeMessage enviar(String from, String[] to, String[] cc, String[] cco, String assunto, String corpoEmail, File[] anexos, Boolean htmlContent) throws MessagingException {
		Properties props = new Properties();
		Authenticator auth = null;
		// props.put("mail.smtp.auth", "true");
		// props.put("mail.smtp.starttls.enable", "true");
		// props.put("mail.smtp.host", "correio.stf.jus.br");
		// props.put("mail.smtp.port", "25");
		//
		// auth = new Authenticator() {
		// protected PasswordAuthentication getPasswordAuthentication() {
		// return new PasswordAuthentication("dev2sti@gmail.com",
		// "testedev2sti");
		// }
		// };

		Session session = Session.getInstance(props, auth);

		// cria a mensagem
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(from));

		// Destinatário
		if (to != null && to.length > 0) {
			InternetAddress[] addressTO = new InternetAddress[to.length];
			for (int i = 0; i < to.length; i++)
				addressTO[i] = new InternetAddress(to[i]);
			msg.setRecipients(Message.RecipientType.TO, addressTO);
		}

		// Cópia
		if (cc != null && cc.length > 0) {
			InternetAddress[] addressCC = new InternetAddress[cc.length];
			for (int i = 0; i < cc.length; i++)
				addressCC[i] = new InternetAddress(cc[i]);
			msg.setRecipients(Message.RecipientType.CC, addressCC);
		}

		// Cópia oculta
		if (cco != null && cco.length > 0) {
			InternetAddress[] addressCCO = new InternetAddress[cco.length];
			for (int i = 0; i < cco.length; i++)
				addressCCO[i] = new InternetAddress(cco[i]);
			msg.setRecipients(Message.RecipientType.BCC, addressCCO);
		}

		msg.setSubject(assunto);

		// cria a primeira parte da mensagem
		MimeBodyPart mbp1 = new MimeBodyPart();
		if (htmlContent)
			mbp1.setContent(corpoEmail, "text/html; charset=utf-8");
		else
			mbp1.setText(corpoEmail);

		// cria a Multipart
		Multipart mp = new MimeMultipart();
		mp.addBodyPart(mbp1);

		// adiciona os anexos
		if (anexos != null && anexos.length > 0) {
			for (int i = 0; i < anexos.length; i++) {
				FileDataSource fds = new FileDataSource(anexos[i]);
				MimeBodyPart mbp2 = new MimeBodyPart();
				mbp2.setDataHandler(new DataHandler(fds));
				mbp2.setFileName(fds.getFile().getName());
				mbp2.setDisposition(Part.ATTACHMENT);
				mp.addBodyPart(mbp2);
			}
		}

		// adiciona a Multipart na mensagem
		msg.setContent(mp);		
		
		// configura a data: cabecalho
		msg.setSentDate(new Date());

		// Salva as mudanças
		msg.saveChanges();
		
		return msg;

	}
}
