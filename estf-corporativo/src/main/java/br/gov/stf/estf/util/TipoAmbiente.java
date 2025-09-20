package br.gov.stf.estf.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.support.JdbcUtils;

import br.gov.stf.framework.util.ApplicationFactory;

public enum TipoAmbiente {
	PRODUCAO("Produção", "sistemas.stf.jus.br", "portal.stf.jus.br", "http://redir.stf.jus.br",
			"https://digital.stf.jus.br", "http://digital-interno.stf.jus.br"), HOMOLOGACAO("Homologação",
					"sistemash.stf.jus.br", "portal.stf.jus.br", "http://redirh.stf.jus.br",
					"https://digitalh.stf.jus.br", "http://digital-interno-qa.stf.jus.br"), DESENVOLVIMENTO(
							"Desenvolvimento", "apld-sspj", "portal.stf.jus.br", "http://smeagold",
							"https://digitald.stf.jus.br", "http://digital-interno-qa.stf.jus.br"), INDETERMINADO(
									"Indeterminado", "apld-sspj", "dstf", "http://smeagold",
									"https://digital.stf.jus.br", "http://digital-interno-qa.stf.jus.br"), TESTE(
											"Testes", "sistemast.stf.jus.br", "portal.stf.jus.br",
											"http://redirh.stf.jus.br", "https://digitalqa.stf.jus.br",
											"http://digital-interno-qa.stf.jus.br"), PNOVO("PNovo",
													"sistemas.stf.jus.br", "portal.stf.jus.br",
													"http://redir.stf.jus.br", "https://digital.stf.jus.br",
													"http://digital-interno-qa.stf.jus.br");

	private String descricao;
	private String servidor;
	private String internet;
	private String redir;
	private String servidorDigitalHttps;
	private String servidorDigitalHttp;

	private static Log log = LogFactory.getLog(TipoAmbiente.class);

	private TipoAmbiente(String descricao, String servidor, String internet, String redir, String servidorDigitalHttps,
			String servidorDigitalHttp) {
		this.descricao = descricao;
		this.servidor = servidor;
		this.internet = internet;
		this.redir = redir;
		this.servidorDigitalHttps = servidorDigitalHttps;
		this.setServidorDigitalHttp(servidorDigitalHttp);
	}

	public String getDescricao() {
		return descricao;
	}

	public String getServidor() {
		return this.servidor;
	}

	public String getInternet() {
		return this.internet;
	}

	public String getRedir() {
		return this.redir;
	}

	public String getUrlInternet() {
		return "http://" + this.getInternet() + "/processos/detalhe.asp?incidente=";
	}
	
	public String getServidorDigitalHttp() {
		return servidorDigitalHttp;
	}

	public void setServidorDigitalHttp(String servidorDigitalHttp) {
		this.servidorDigitalHttp = servidorDigitalHttp;
	}

	public String getUrlAcompanhamentoSTFDigital() {
		return getServidorDigitalHttps() + "/informacoes/processos/";
	}

	public String getUrlPecas() {
		return "http://" + this.getServidor()
				+ "/estfvisualizador/jsp/consultarprocessoeletronico/ConsultarProcessoEletronico.jsf?seqobjetoincidente=";
	}

	public String getUrlPecasSupremo() {
		return "http://" + this.getServidor() + "/supremo/supremo.html#/visualizador/false/";
	}

	public String getUrlPecasSupremo(Long id) {
		return "http://" + this.getServidor() + "/supremo/supremo.html#/visualizador/false/" + id.toString();
	}

	public String getUrlPecasSupremoPopout(Long id) {
		return "http://" + this.getServidor() + "/supremo/visualizadorPopout.html#/visualizador/popout/" + id.toString()
				+ "/1";
	}

	public String getUrlDocumentoSTFDigital() {
		return getServidorDigitalHttps() + "/decisoes-monocraticas/api/public/votos/";
	}

	public String getServidorDigitalHttps() {
		return servidorDigitalHttps;
	}

	public void setServidorDigitalHttps(String servidorDigital) {
		this.servidorDigitalHttps = servidorDigital;
	}

	public String getUrlAnexosSTFDigital() {
		return getServidorDigitalHttps() + "/autosprocessuais-anexos/api/public/processos/anexos/";
	}

	public String getUrlConversorRtfPdfSTFDigital() {
		return getServidorDigitalHttp() + "/integracoes-conversor/api/conversor?formatoEntrada=rtf&formatoSaida=pdf";
	}

	public static TipoAmbiente getInstance() {
		Connection connection = null;
		try {
			DataSource datasource = (DataSource) ApplicationFactory.getInstance().getServiceLocator()
					.getService("dataSource");
			connection = datasource.getConnection();
			DatabaseMetaData dbmd = connection.getMetaData();
			String url = dbmd.getURL();

			if (url != null) {
				if (url.toUpperCase().contains("STFD")) {
					return TipoAmbiente.DESENVOLVIMENTO;
				} else if (url.toUpperCase().contains("STFH")) {
					return TipoAmbiente.HOMOLOGACAO;
				} else if (url.toUpperCase().contains("STFP")) {
					return TipoAmbiente.PRODUCAO;
				} else if (url.toUpperCase().contains("PNOVO")) {
					return TipoAmbiente.PNOVO;
				} else if (url.toUpperCase().contains("STFQ") || url.toUpperCase().contains("STFM")) {
					return TipoAmbiente.TESTE;
				}
			}
		} catch (SQLException e) {
			log.error("Erro ao tentar determinar base de dados alvo.", e);
		} finally {
			JdbcUtils.closeConnection(connection);
		}

		return TipoAmbiente.INDETERMINADO;
	}
}
