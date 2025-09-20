package br.gov.stf.estf.expedicao.visao;

import static br.gov.stf.estf.expedicao.visao.Util.mandarRespostaDeDownloadDoArquivoExcel;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.jsf.beans.usuario.BeanUsuario;
import br.gov.stf.estf.corp.entidade.Municipio;
import br.gov.stf.estf.corp.entidade.UnidadeFederacao;
import br.gov.stf.estf.corp.model.service.MunicipioService;
import br.gov.stf.estf.corp.model.service.UnidadeFederacaoService;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.expedicao.entidade.DestinatarioListaRemessa;
import br.gov.stf.estf.expedicao.entidade.VwEndereco;
import br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util;
import br.gov.stf.estf.expedicao.model.service.DestinatarioListaRemessaService;
import br.gov.stf.estf.expedicao.model.service.impl.ExpedicaoRelatorioServiceLocal;
import br.gov.stf.estf.localizacao.model.service.OrigemService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.security.AcegiSecurityUtils;

/**
 * Bean que mantém informações a respeito do Destinatário da expedição .
 *
 * @author thiago.miranda
 * @author Filipe.Gomes
 */
public class BeanDestinatario extends AssinadorBaseBean implements Converter, SelecionaCep {

	private static final long serialVersionUID = 1L;

	private static final String SEPARADOR_MUNICIPIO = " - ";
	private static final String MENSAGEM_ERRO_PESQUISA = "Erro ao pesquisar Destinatário. ";
	private static final String MENSAGEM_ERRO_CADASTRO = "Erro ao salvar Destinatário. ";
	private static final String MENSAGEM_ERRO_COPIA = "Erro ao copiar Destinatário. ";
	private static final String MENSAGEM_ERRO_ALTERACAO = "Erro ao alterar Destinatário.";

	private static final Log LOG = LogFactory.getLog(BeanDestinatario.class);

	private boolean pesquisaSimples;
	private String campoPesquisa;
	private String qtdRegistros = "0";
	private DestinatarioListaRemessa bean;
	private List<String> listaUF;
	private List<Municipio> listaCidade;
	private String ufSelecionada;
	private List<DestinatarioListaRemessa> listaDestinatarioListaRemessa;
	private boolean copia = false;
	private boolean flagPesquisaAvancada = false;
	private boolean flagModoPesquisaDialogo = false;
	private SelecionaBeanDestinatario selecionaBeanDestinatario;
	private DestinatarioListaRemessa destinatarioSelecionado;
	private boolean flagExibirModalPesquisa = false;
	private boolean flagExibirModalPesquisaCep = false;
	private boolean mostrarPesquisa = false;
	private String completarCadastro = "Richfaces.hideModalPanel('idPnlDestinatarioCadastro');";
	private boolean visualizacao = true;
	private Short reconfiguracaoCodOrigem;
	private boolean exibirBotaoExportarExcel = true;

	public BeanDestinatario() {
		this.mostrarPesquisa = false;
	}
	
	public boolean isExibirBotaoExportarExcel() {
		return exibirBotaoExportarExcel;
	}

	public void setExibirBotaoExportarExcel(boolean exibirBotaoExportarExcel) {
		this.exibirBotaoExportarExcel = exibirBotaoExportarExcel;
	}

	public Short getReconfiguracaoCodOrigem() {
		return reconfiguracaoCodOrigem;
	}

	public void setReconfiguracaoCodOrigem(Short reconfiguracaoCodOrigem) {
		this.reconfiguracaoCodOrigem = reconfiguracaoCodOrigem;
	}

	public boolean isVisualizacao() {
		return visualizacao;
	}

	public void setVisualizacao(boolean visualizacao) {
		this.visualizacao = visualizacao;
	}

	public String getCompletarCadastro() {
		return completarCadastro;
	}

	public void setCompletarCadastro(String completarCadastro) {
		this.completarCadastro = completarCadastro;
	}

	public String abrirTelaPesquisa() {
		limparCampos();
		campoPesquisa = "";
		flagModoPesquisaDialogo = false;
		flagExibirModalPesquisaCep = false;
		return "destinatarioPesquisa";
	}

	public String abrirModoPesquisaDialogo() {
		if (selecionaBeanDestinatario != null) {
			selecionaBeanDestinatario.limparCamposDestinatario();
		}
		limparCampos();
		flagModoPesquisaDialogo = true;
		destinatarioSelecionado = null;
		listaDestinatarioListaRemessa.clear();
		executarPesquisaSimplesDestinatario(false);
		if (listaDestinatarioListaRemessa.size() == 1) {
			destinatarioSelecionado = listaDestinatarioListaRemessa.get(0);
			selecionar();
			flagExibirModalPesquisa = false;
			setExibirBotaoExportarExcel(false);
		} else {
			flagExibirModalPesquisa = true;
			setExibirBotaoExportarExcel(false);
			setCampoPesquisa("");
		}
		return "";
	}

	private void limparCampos() {
		qtdRegistros = "";
		bean = new DestinatarioListaRemessa();
		listaDestinatarioListaRemessa = new ArrayList<DestinatarioListaRemessa>();
                pesquisaSimples = !flagPesquisaAvancada;
		flagExibirModalPesquisa = false;
		setMostrarPesquisa(false);
		setUfSelecionada(null);

		setDestinatarioSelecionado(null);
	}

	public boolean isMostrarPesquisa() {
		return mostrarPesquisa;
	}

	public void setMostrarPesquisa(boolean mostrarPesquisa) {
		this.mostrarPesquisa = mostrarPesquisa;
	}

	public boolean isPesquisaSimples() {
		return pesquisaSimples;
	}

	public void setPesquisaSimples(boolean pesquisaSimples) {
		setMostrarPesquisa(false);
		this.pesquisaSimples = pesquisaSimples;
	}

	public DestinatarioListaRemessa getBean() {
		return bean;
	}

	public void setBean(DestinatarioListaRemessa bean) {
		this.bean = bean;
	}

	public String getUfSelecionada() {
		if (ufSelecionada == null || ufSelecionada.equals("")) {
			if (bean != null && bean.getMunicipio() != null) {
				ufSelecionada = bean.getMunicipio().getSiglaUf();
			}
		}
		return ufSelecionada;
	}

	public void setUfSelecionada(String ufSelecionada) {
		this.ufSelecionada = ufSelecionada;
	}

	public List<String> getListaUF() {
		return listaUF;
	}

	public void setListaUF(List<String> listaUF) {
		this.listaUF = listaUF;
	}

	public List<Municipio> getListaCidade() {
		return listaCidade;
	}

	public void setListaCidade(List<Municipio> listaCidade) {
		this.listaCidade = listaCidade;
	}

	public String getQtdRegistros() {
		return qtdRegistros;
	}

	public void setQtdRegistros(String qtdRegistros) {
		this.qtdRegistros = qtdRegistros;
	}

	public String getCampoPesquisa() {
		return campoPesquisa;
	}

	public void setCampoPesquisa(String campoPesquisa) {
		this.campoPesquisa = campoPesquisa;
	}

	public List<DestinatarioListaRemessa> getListaDestinatarioListaRemessa() {
		return listaDestinatarioListaRemessa;
	}

	public void setListaDestinatarioListaRemessa(List<DestinatarioListaRemessa> listaDestinatarioListaRemessa) {
		this.listaDestinatarioListaRemessa = listaDestinatarioListaRemessa;
	}

	public boolean isCopia() {
		return copia;
	}

	public void setCopia(boolean copia) {
		this.copia = copia;
	}

	public boolean isFlagPesquisaAvancada() {
		return flagPesquisaAvancada;
	}

	public void setFlagPesquisaAvancada(boolean flagPesquisaAvancada) {
		this.flagPesquisaAvancada = flagPesquisaAvancada;
	}

	public boolean isFlagModoPesquisaDialogo() {
		return flagModoPesquisaDialogo;
	}

	public void setFlagModoPesquisaDialogo(boolean flagModoPesquisaDialogo) {
		this.flagModoPesquisaDialogo = flagModoPesquisaDialogo;
	}

	public SelecionaBeanDestinatario getSelecionaBeanDestinatario() {
		return selecionaBeanDestinatario;
	}

	public void setSelecionaBeanDestinatario(SelecionaBeanDestinatario selecionaBeanDestinatario) {
		this.selecionaBeanDestinatario = selecionaBeanDestinatario;
	}

	public DestinatarioListaRemessa getDestinatarioSelecionado() {
		return destinatarioSelecionado;
	}

	public void setDestinatarioSelecionado(DestinatarioListaRemessa destinatarioSelecionado) {
		this.destinatarioSelecionado = destinatarioSelecionado;
	}

	public boolean isFlagExibirModalPesquisa() {
		return flagExibirModalPesquisa;
	}

	public void setFlagExibirModalPesquisa(boolean flagExibirModalPesquisa) {
		this.flagExibirModalPesquisa = flagExibirModalPesquisa;
	}

	public boolean isFlagExibirModalPesquisaCep() {
		return flagExibirModalPesquisaCep;
	}

	public void setFlagExibirModalPesquisaCep(boolean flagExibirModalPesquisaCep) {
		this.flagExibirModalPesquisaCep = flagExibirModalPesquisaCep;
	}

	public String getTitulo() {
		String titulo;
		if (flagModoPesquisaDialogo) {
			titulo = "Pesquisar Destinatário";
		} else {
			titulo = "Cadastrar Destinatário - Pesquisa";
		}
		return titulo;
	}

	public void executarPesquisaSimplesDestinatario(ActionEvent event) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String paginaAberta = facesContext.getViewRoot().getViewId();
		
		if(paginaAberta.contains("remessaCadastro.jsp") || paginaAberta.contains("etiquetaProcesso.jsp")){
			setExibirBotaoExportarExcel(false);
		}else{
			setExibirBotaoExportarExcel(true);
		}
		executarPesquisaSimplesDestinatario(true);
	}

	public void executarPesquisaSimplesDestinatario(boolean exibirMensagens) {
		DestinatarioListaRemessaService destinatarioListaRemessaService = getDestinatarioListaRemessaService();
		
		if (validarCampoPesquisaSimples()) {
			try {
				listaDestinatarioListaRemessa = destinatarioListaRemessaService
						.pesquisarVariosCampos(getCampoPesquisa());
				setQtdRegistros(String.valueOf(listaDestinatarioListaRemessa
						.size()));
				setCampoPesquisa(null);
				if (listaDestinatarioListaRemessa.isEmpty()) {
					if (exibirMensagens) {
						reportarAviso("Nenhum registro foi encontrado!");
					}
					setCampoPesquisa(null);
					listaDestinatarioListaRemessa.clear();
					setBean(new DestinatarioListaRemessa());
					setUfSelecionada(null);
					setQtdRegistros(null);
					setMostrarPesquisa(false);
				} else {
					setMostrarPesquisa(true);
				}
			} catch (ServiceException ex) {
				LOG.warn(MENSAGEM_ERRO_PESQUISA, ex);
				reportarAviso(MENSAGEM_ERRO_PESQUISA);
			}
		} else {
			LOG.warn("Campo de pesquisa vazio ou com menos de 2 caracteres.");
			if (exibirMensagens) {
				reportarAviso("Campo de pesquisa vazio ou com menos de 2 caracteres.");
			}
		}
	}

	private boolean validarCampoPesquisaSimples() {
		return (getCampoPesquisa() != null && (getCampoPesquisa().length() >= 2));
	}

	public void executarPesquisaAvancadaDestinatario(ActionEvent event) {
		DestinatarioListaRemessaService destinatarioListaRemessaService = getDestinatarioListaRemessaService();
		try {
			listaDestinatarioListaRemessa = destinatarioListaRemessaService
					.pesquisar(bean, ufSelecionada);
			setQtdRegistros(String
					.valueOf(listaDestinatarioListaRemessa.size()));
			if (listaDestinatarioListaRemessa.isEmpty()) {
				reportarAviso("Destinatário não localizado.");
				setMostrarPesquisa(false);
			} else {
				setMostrarPesquisa(true);
			}
		} catch (ServiceException ex) {
			LOG.warn("");
			reportarAviso(MENSAGEM_ERRO_PESQUISA);
		}
	}

	public void pesquisaDestinatario(ActionEvent e) {
		if (getCampoPesquisa() == null || (getCampoPesquisa().length() < 2)) {
			reportarAviso("Favor informar no mínimo 2 caracteres para realizar a pesquisa.");
		} else if (listaDestinatarioListaRemessa.isEmpty()) {
			reportarInformacao("Destinatário não localizado.");
		}
	}

	public void pesquisarCEP(ActionEvent event) {
		try {
			flagExibirModalPesquisaCep = false;
			limparCamposSelecaoCep();
			String cepBusca = bean.getCep().trim();
			if (cepBusca.isEmpty()) {
				flagExibirModalPesquisaCep = true;
			} else {
				List<VwEndereco> listaEnderecos = getVwEnderecoService().pesquisar(cepBusca);
				if (listaEnderecos != null && !listaEnderecos.isEmpty()) {
					VwEndereco endereco = listaEnderecos.get(0);
					exibirEndereco(endereco);
				} else {
					if (cepBusca.length() < 8) {
						reportarAviso("Por favor, preencha corretamente o campo CEP para a realização da pesquisa");
					} else {
						reportarAviso("CEP não localizado.");
					}
				}
			}
		} catch (Exception e) {
			reportarErro("Erro ao pesquisar CEP", e, LOG);
		}
	}

	private void exibirEndereco(VwEndereco endereco) throws ServiceException {
		if (endereco != null) {
			Municipio municipio = getMunicipioService().buscarMunicipioCorrespondente(endereco.getSeqMunicipio());

			bean.setBairro(endereco.getBairro() != null ? endereco.getBairro() : "");
			bean.setCep(endereco.getCep());
			bean.setLogradouro(endereco.getLogradouro());
			bean.setMunicipio(municipio);
			setUfSelecionada(endereco.getUf());
		}
	}

	public void inicializaPesquisaAvancada(ActionEvent e)
			throws ServiceException {
		configurarUF(e);
		bean = new DestinatarioListaRemessa();
		setUfSelecionada(null);
	}

	public void inicializaCadastro(ActionEvent e) throws ServiceException {
		setVisualizacao(true);
		inicializaPesquisaAvancada(e);
	}

	public void configurarUF(ActionEvent e) throws ServiceException {
		UnidadeFederacaoService unidadeFederacaoService = getUnidadeFederacaoService();
		listaUF = new ArrayList<String>();
		for (UnidadeFederacao uf : unidadeFederacaoService.listarAtivos()) {
			listaUF.add(uf.getSigla());
		}
		setListaUF(listaUF);
	}

	public void configurarMunicipio(ActionEvent e) throws ServiceException {
		bean.setMunicipio(null);
		MunicipioService municipioService = getMunicipioService();
		listaCidade = new ArrayList<Municipio>();

		for (Municipio municipio : municipioService.listarAtivosTipoMunicipio(getUfSelecionada())) {
			listaCidade.add(municipio);
		}
		setListaCidade(listaCidade);
	}

	public void limparPesquisa(ActionEvent e) {
		setCampoPesquisa(null);
		setBean(new DestinatarioListaRemessa());
		setQtdRegistros(null);
	}

	private boolean validarCamposObrigatorios() {
		String mensagem = null;
		if (bean != null) {
			if (Util.verificarObjetoNulloOuTextoVazio(true, bean.getDescricaoPrincipal(),
					bean.getCep(), bean.getNumero(), bean.getBairro())) {
				mensagem = "Favor preencher os campos obrigatórios.";
			} else if (bean.getMunicipio() == null) {
				mensagem = "Favor preencher os campos obrigatórios.";
			} else if (bean.getMunicipio().getNome().equals("Inexistente")) {
				setCompletarCadastro("");
				mensagem = "Cidade inexistente!";
			}
		}
		boolean resultado = mensagem == null;
		if (mensagem != null && !mensagem.isEmpty()) {
			reportarInformacao(mensagem);
		}
		return resultado;
	}

	public boolean isModoCadastro() {
		boolean resultado = true;
		if (bean != null) {
			resultado = bean.getId() == null;
		}
		return resultado;
	}

	public void salvar(ActionEvent event) {
		if (validarCamposObrigatorios()) {
			DestinatarioListaRemessaService destinatarioListaRemessaService = getDestinatarioListaRemessaService();
			try {
				boolean validacaoOrigem = true;
				if (bean.getCodigoOrigem() != null) {
					OrigemService origemService = getOrigemService();
					Origem origem = origemService.recuperarPorId(Long
							.parseLong(bean.getCodigoOrigem().toString()));
					if (origem == null) {
						validacaoOrigem = false;
					}
				}

				if (validacaoOrigem) {
					destinatarioListaRemessaService.incluir(bean);
					setCompletarCadastro("Richfaces.hideModalPanel('idPnlDestinatarioCadastro');");
					reportarInformacao("Destinatário salvo com sucesso!");
					bean = new DestinatarioListaRemessa();
					limparBean(null);
				} else {
					setCompletarCadastro("");
					reportarInformacao("O campo 'Cód. Órgão é inválido!");
				}
			} catch (NumberFormatException e) {
				LOG.error(e.getMessage());
				LOG.error(e.getStackTrace()[0]);
				reportarAviso(MENSAGEM_ERRO_CADASTRO);
			} catch (ServiceException e) {
				LOG.error(e.getMessage());
				LOG.error(e.getStackTrace()[0]);
				reportarAviso(MENSAGEM_ERRO_CADASTRO);
                    }
		}
	}

	public void copiar(ActionEvent event) throws ServiceException {
		setVisualizacao(true);
		configurarUF(event);
		String idDestinatario = ((HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest())
				.getParameter("identificadorDestinatario");
		DestinatarioListaRemessaService destinatarioListaRemessaService = getDestinatarioListaRemessaService();
		try {
			bean = destinatarioListaRemessaService.copiar(Long
					.parseLong(idDestinatario));
			setReconfiguracaoCodOrigem(bean.getCodigoOrigem());
		} catch (ServiceException e) {
			LOG.error(e.getMessage());
			LOG.error(e.getStackTrace()[0]);
			reportarAviso(MENSAGEM_ERRO_COPIA);
		}
	}

	public void alterar(ActionEvent event) {
		DestinatarioListaRemessaService destinatarioListaRemessaService = getDestinatarioListaRemessaService();
		try {
			boolean validacaoOrigem = true;
			if (bean.getCodigoOrigem() != null) {
				OrigemService origemService = getOrigemService();
				Origem origem = origemService.recuperarPorId(Long
						.parseLong(bean.getCodigoOrigem().toString()));
				if (origem == null) {
					validacaoOrigem = false;
				}
			}

			if (validacaoOrigem) {
				destinatarioListaRemessaService.alterar(bean);
				reportarInformacao("Destinatário alterado com sucesso!");
				bean = new DestinatarioListaRemessa();
				limparBean(null);
				setCompletarCadastro("Richfaces.hideModalPanel('idPnlDestinatarioCadastro');");

			} else {
				bean.setCodigoOrigem(getReconfiguracaoCodOrigem());
				setCompletarCadastro("");
				reportarInformacao("O campo 'Cód. Órgão é inválido!");
			}

		} catch (ServiceException e) {
			LOG.warn(MENSAGEM_ERRO_ALTERACAO, e);
			reportarAviso(MENSAGEM_ERRO_ALTERACAO);
		}
	}

	public void excluir(ActionEvent event) {
		DestinatarioListaRemessaService destinatarioListaRemessaService = getDestinatarioListaRemessaService();
		try {
			destinatarioListaRemessaService.excluir(bean);
			listaDestinatarioListaRemessa.remove(bean);
			setQtdRegistros(String.valueOf(listaDestinatarioListaRemessa.size()));
			bean = new DestinatarioListaRemessa();
			reportarInformacao("Operação realizada com sucesso.");
			setUfSelecionada(null);
		} catch (ServiceException e) {
			LOG.error(e.getMessage());
			LOG.error(e.getStackTrace()[0]);
		}
	}

	public String selecionar() {
		if (selecionaBeanDestinatario != null) {
			try {
				selecionaBeanDestinatario.receberDestinatarioSelecionado(destinatarioSelecionado);
			} catch (ServiceException e) {
				reportarAviso("Erro ao selecionar destinatário.");
				LOG.error(e.getMessage());
			}
		}
		setCampoPesquisa("");
		flagExibirModalPesquisa = false;
		setExibirBotaoExportarExcel(true);
		return "";
	}

	public String cancelar() {
		flagExibirModalPesquisa = false;
		setExibirBotaoExportarExcel(true);
		return "";
	}

	public void limparBean(ActionEvent event) {
		setCampoPesquisa(null);
		listaDestinatarioListaRemessa.clear();
		setBean(new DestinatarioListaRemessa());
		setUfSelecionada(null);
		setQtdRegistros(null);
		setMostrarPesquisa(false);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		Object resultado = null;
		if (value != null && !value.isEmpty()) {
			String[] dadosMunicipio = value.split(SEPARADOR_MUNICIPIO);
			if (dadosMunicipio.length == 2) {
				String nome = dadosMunicipio[0];
				String uf = dadosMunicipio[1];
				MunicipioService municipioService = getMunicipioService();
				try {
					resultado = municipioService.listarAtivo(uf, nome);
				} catch (ServiceException e) {
					LOG.warn("Erro ao converter Municipio.", e);
					reportarAviso("Erro ao converter Municipio.");
				}
			} else {
				Municipio mun = new Municipio();
				mun.setNome("Inexistente");
				resultado = mun;
			}
		}
		return resultado;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		String resultado = null;
		if (value != null) {
			if (value instanceof Municipio) {
				Municipio municipio = (Municipio) value;
				String retorno = municipio.getNome() + SEPARADOR_MUNICIPIO
						+ municipio.getSiglaUf();
				resultado = retorno;
			}
		}
		return resultado;
	}

	public boolean isUsuarioComAcessoAlteracaoDados() {
		return (AcegiSecurityUtils
				.isUserInRole(BeanUsuario.RS_MASTER_PROCESSAMENTO) || AcegiSecurityUtils
				.isUserInRole(BeanUsuario.RS_EXPEDICAO_ADM));
	}

	public void voltarCamposVazios(ActionEvent event) {		
		bean = new DestinatarioListaRemessa();
		setUfSelecionada(null);
		setListaDestinatarioListaRemessa(listaDestinatarioListaRemessa);
	}

	public void abrirVisualizacao(ActionEvent event) {
		setVisualizacao(false);
	}

	public void relatorioExcel(ActionEvent event) {
		byte[] arquivo;
		ExpedicaoRelatorioServiceLocal expedicaoRelatorioService = new ExpedicaoRelatorioServiceLocal();
		arquivo = expedicaoRelatorioService.criarRelatorioExcel(listaDestinatarioListaRemessa,
				Integer.parseInt(qtdRegistros),
				"rel_consulta_destinatario");
		ByteArrayInputStream input = new ByteArrayInputStream(arquivo);
		mandarRespostaDeDownloadDoArquivoExcel(input, "ConsultaDestinatario");
	}

	@Override
	public String getNomeComponenteRerenderizarSelecaoCep() {
		return "painelCadastroEndereco";
	}

	@Override
	public void limparCamposSelecaoCep() {
		bean.setBairro("");
		bean.setLogradouro("");
		bean.setMunicipio(null);
		setUfSelecionada("");
	}

	@Override
	public void receberObjetoSelecionadoSelecaoCep(VwEndereco endereco) {
		flagExibirModalPesquisaCep = false;
		try {
			exibirEndereco(endereco);
		} catch (Exception e) {
			reportarErro("Erro ao pesquisar por CEP", e, LOG);
		}
	}

	@Override
	public void manipularErroAoExecutarPesquisaSelecaoCep(Exception exception) {
		reportarErro("Erro ao pesquisar por CEP", exception, LOG);
	}

	@Override
	public void cancelarPesquisa() {
		flagExibirModalPesquisaCep = false;
	}
}

interface SelecionaBeanDestinatario extends Serializable {

	long serialVersionUID = 1L;

	String getNomeComponenteRerenderizar();

	void limparCamposDestinatario();

	void receberDestinatarioSelecionado(DestinatarioListaRemessa destinatarioListaRemessa) throws ServiceException;
}