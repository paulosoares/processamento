package br.gov.stf.estf.assinatura.visao.jsf.beans.ambiente;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resource.adapter.jdbc.WrapperDataSource;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.ambiente.Ambiente;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;

/**
 * Bean que mantém informações a respeito do ambiente de execução atual.
 * 
 * @author thiago.miranda
 */
public class BeanAmbiente extends AssinadorBaseBean {

	private static final long serialVersionUID = 3524019929237111365L;
	private static final Log LOG = LogFactory.getLog(BeanAmbiente.class);

	private Ambiente ambiente;

	public BeanAmbiente() {
		detectarAmbiente();

		if (LOG.isInfoEnabled()) {
			LOG.info(MessageFormat.format("Ambiente atual: {0}.", ambiente));
		}
	}

	private void detectarAmbiente() {
		String urlConexao = obterURLConexao();

		if (StringUtils.isNotVazia(urlConexao)) {
			detectarAmbientePelaURLConexaoBD(urlConexao.toLowerCase());
		}

		if (ambiente == null) {
			LOG.warn("O ambiente não pôde ser detectado a partir da conexão com o banco e será detectado a partir da URL do servidor.");
			detectarAmbientePeloNomeServidor();
		}

		if (ambiente == null) {
			LOG.warn("O ambiente não pôde ser detectado a partir do nome do servidor. Podem ocorrer problemas ao se acessar sistemas externos.");
			ambiente = Ambiente.DESCONHECIDO;
		}
	}

	private String obterURLConexao() {
		String urlConexao = null;
		Object obj = getService("dataSourceSTF");

		if (obj instanceof BasicDataSource) {
			BasicDataSource bds = (BasicDataSource) obj;
			urlConexao = bds.getUrl();
		} else if (obj instanceof WrapperDataSource) {
			WrapperDataSource wds = (WrapperDataSource) obj;
			try {
				Connection con = wds.getConnection();
				DatabaseMetaData dbmd = con.getMetaData();
				urlConexao = dbmd.getURL();
				con.close();
			} catch (SQLException e) {
				LOG.error("Erro ao recuperar url de conexão com o banco.", e);
			} catch (Exception e) {
				LOG.error("Falha na conexão com o banco de dados.", e);
			}
		}

		return urlConexao;
	}

	private void detectarAmbientePelaURLConexaoBD(String urlConexao) {
		Ambiente[] ambientes = Ambiente.values();
		for (Ambiente ambiente : ambientes) {
			if (urlConexao.contains(ambiente.getNomeBanco())) {
				this.ambiente = ambiente;
				break;
			}
		}
	}

	private void detectarAmbientePeloNomeServidor() {
		HttpServletRequest request = (HttpServletRequest) getRequest();
		if (request != null && request.getServerName() != null) {
			String serverName = request.getServerName().toLowerCase();

			Ambiente[] ambientes = Ambiente.values();
			for (Ambiente ambiente : ambientes) {
				if (ambiente.contemNomeServidor(serverName)) {
					this.ambiente = ambiente;
					break;
				}
			}
		}
	}

	public Ambiente getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(Ambiente ambiente) {
		this.ambiente = ambiente;
	}

	public String getNomeBanco() {
		return ambiente.getNomeBanco();
	}
	
	public String getPortalStf() {
		return ambiente.getPortalStf();
	}

	public String getNomeServidorPadrao() {
		return ambiente.getNomeServidorPadrao();
	}

	public boolean getIsAmbienteDesconhecido() {
		return getAmbiente() == null || getAmbiente() == Ambiente.DESCONHECIDO;
	}

	public boolean getIsAmbienteDesenvolvimento() {
		return getAmbiente() == Ambiente.DESENVOLVIMENTO;
	}

	public boolean getIsAmbienteHomologacao() {
		return getAmbiente() == Ambiente.HOMOLOGACAO;
	}

	public boolean getIsAmbienteProducao() {
		return getAmbiente() == Ambiente.PRODUCAO;
	}
}
