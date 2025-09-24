package br.jus.stf.estf.decisao.documento.support.assinador.timestamp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.bouncycastle.tsp.TSPAlgorithms;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.bouncycastle.tsp.TimeStampResponse;
import org.bouncycastle.tsp.TimeStampToken;

public class GeradorSelo {
	private static final Logger log = Logger.getLogger(GeradorSelo.class);
	
	public byte[] gerarCarimboTempo(ServidorCarimbo servidor, byte[] assinatura) throws URISyntaxException, IOException, TSPException, NoSuchAlgorithmException {
		TimeStampRequestGenerator reqGen = new TimeStampRequestGenerator();
		reqGen.setCertReq(true);
		log.info("Criando requisição para recuperar carimbo");
		
		TimeStampRequest request = reqGen.generate(TSPAlgorithms.SHA1, assinatura);
		log.info("Enviando requisição para "+servidor.getUrl());
		TimeStampResponse response = sendRequest(request, servidor.getUrl());
		TimeStampToken respToken = response.getTimeStampToken();
		byte[] token = respToken.getEncoded();
		if ( token==null ) {
			throw new TSPException("Nenhum token retornado");
		}
		log.info("Recebidos "+token.length+" bytes do carimbador");
		return token;
	}

	private TimeStampResponse sendRequest(TimeStampRequest timestampreq, String servidor) throws URISyntaxException, IOException, TSPException {
		URI uri = new URI(servidor);
		String host = uri.getHost();
		int porta = uri.getPort();
		
		byte[] token = timestampreq.getEncoded();
		
		TimeStampResponse tsptcpipresponse = null;
		Socket socket = new Socket();
		log.info("Criando socket em: host="+host+", porta="+porta);
		socket.connect(new InetSocketAddress(host, porta), 5000);
		log.debug("Socket conectada");
		DataInputStream datainputstream = new DataInputStream(socket.getInputStream());
		DataOutputStream dataoutputstream = new DataOutputStream(socket.getOutputStream());

		
		log.debug("Escrevendo na socket");
		dataoutputstream.writeInt(token.length + 1); // length (32-bits)
		dataoutputstream.writeByte(0); // flag (8-bits)
		dataoutputstream.write(token); // value (defined below)
		dataoutputstream.flush();
		log.debug("OutputStream atualizada");
		int i = datainputstream.readInt();
		byte byte0 = datainputstream.readByte();
		log.debug("Lendo primeiro byte do inputStream '"+byte0+"'");
		
		if ( byte0==5 ) {
			byte abyte1[] = new byte[i - 1];
			log.debug("Lendo todo o input stream");
			datainputstream.readFully(abyte1);
			log.debug("Criando novo time stam response: "+abyte1);
			tsptcpipresponse = new TimeStampResponse(abyte1);
			log.debug("Novo TimeStampResponde criado com sucesso: "+tsptcpipresponse);
		} else {
			datainputstream.close();
			dataoutputstream.close();
			socket.close();
			throw new TSPException("Token inválido");
		}
		
		log.debug("Fechando streams de entrada e saáda");
		datainputstream.close();
		dataoutputstream.close();
		log.info("Fechando conexão socket");
		socket.close();
		

		return tsptcpipresponse;

	}
}
