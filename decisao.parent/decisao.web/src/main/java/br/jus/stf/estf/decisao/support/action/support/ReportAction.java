package br.jus.stf.estf.decisao.support.action.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.jus.stf.estf.decisao.pesquisa.web.MessageRefreshController;
import br.jus.stf.estf.decisao.support.util.FormatoArquivo;
import br.jus.stf.estf.decisao.support.util.ReportUtils;

/**
 * Classe de suporte para ações de geração e apresentação de relatório.
 * 
 * @author Rodrigo.Barreiros
 * @see 08.06.2010
 * @param <T> o tipo de recurso da ação
 */
public abstract class ReportAction<T> extends ActionSupport<T> {
	
	@Qualifier("messageRefreshController") 
	@Autowired 
	private MessageRefreshController messageRefreshController; 
	
	/**
	 * Gera o relatório e o disponibiliza em um Input Stream que será usado para envio ao cliente. 
	 * 
	 * @param resources os recursos necessários para geração do relatório
	 * @return o input stream de retorno
	 * @throws IOException caso ocorra algum problema.
	 */
	protected abstract InputStream doReport(Set<T> resources) throws IOException;

	/**
	 * Envia o relatório para apresentação no cliente. O cliente poderá abrir ou fazer
	 * o download do relatório gerado.
	 * 
	 * @throws IOException caso ocorra algum problema.
	 */
	public void report() throws IOException {
		messageRefreshController.executarRefreshPagina();
		
		InputStream is = doReport(getResources());
		if (is != null) {
			ReportUtils.report(is, getFormatoArquivo());
		}
	}
	
	protected FormatoArquivo getFormatoArquivo() {
		return FormatoArquivo.PDF;
	}
	

}
