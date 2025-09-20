package br.gov.stf.estf.expedicao.visao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.ibm.icu.util.Calendar;

/**
 *
 * @author roberio.fernandes
 */
public class Util {

    private static final String DELIMITADOR_NOME = " ";
    private static final Map<Integer, String> mapaMes = new HashMap<Integer, String>();
    static {
    	int indice = 0;
    	mapaMes.put(indice++, "Janeiro");
    	mapaMes.put(indice++, "Fevereiro");
    	mapaMes.put(indice++, "Março");
    	mapaMes.put(indice++, "Abril");
    	mapaMes.put(indice++, "Maio");
    	mapaMes.put(indice++, "Junho");
    	mapaMes.put(indice++, "Julho");
    	mapaMes.put(indice++, "Agosto");
    	mapaMes.put(indice++, "Setembro");
    	mapaMes.put(indice++, "Outubro");
    	mapaMes.put(indice++, "Novembro");
    	mapaMes.put(indice++, "Dezembro");
    }

	public static void mandarRespostaDeDownloadDoArquivoExcel(ByteArrayInputStream input, String nomeArquivo, String contentType) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.setHeader("Content-disposition", "attachment; filename=\"" + nomeArquivo + "\"");
		response.setContentType(contentType.trim());
		try {
			IOUtils.copy(input, response.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(input);
		}
		facesContext.responseComplete();
	}

	public static void mandarRespostaDeDownloadDoArquivoExcel(ByteArrayInputStream input, String nomeRel) {
		mandarRespostaDeDownloadDoArquivoExcel(input, nomeRel + ".xls", "application/vnd.ms-excel");
	}

	public static void mandarRespostaDeDownloadDoArquivoPdf(ByteArrayInputStream input, String nomeRel) {
		mandarRespostaDeDownloadDoArquivoExcel(input, nomeRel + ".pdf", "application/pdf");
	}
	
	public static String dataExtenso(Date data) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		int dia = calendar.get(Calendar.DAY_OF_MONTH);
		int mes = calendar.get(Calendar.MONTH);
		int ano = calendar.get(Calendar.YEAR);
		String retorno = dia + " de " + mapaMes.get(mes) + " de " + ano;
		return retorno;
	}

	public static String getSiglasNomeFuncionario(String nomeFuncionario) {
    	StringBuilder siglas = new StringBuilder();
    	String[] nomes = nomeFuncionario.split(DELIMITADOR_NOME);
    	for (String nome : nomes) {
    		if (nome.length() > 3) {
    			siglas.append(nome.substring(0, 1).toLowerCase());
    		}
		}
    	return siglas.toString();
    }
}