package br.gov.stf.estf.expedicao.visao;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.richfaces.component.html.HtmlDataTable;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.expedicao.entidade.ConfiguracaoEncaminhamento;
import br.gov.stf.estf.expedicao.entidade.DestinatarioListaRemessa;
import br.gov.stf.estf.expedicao.entidade.VwServidorAssinador;
import br.gov.stf.estf.expedicao.model.service.impl.ExpedicaoRelatorioServiceLocal;
import br.gov.stf.estf.expedicao.visao.vo.EtiquetaProcessoVo;
import br.gov.stf.framework.model.service.ServiceException;
import net.sf.jasperreports.engine.JRException;

/**
 * Bean que gera as etiquetas dos processos.
 *
 * @author roberio.fernandes
 */
public class BeanGeracaoEtiquetaProcesso extends AssinadorBaseBean implements SelecionaBeanDestinatario {

	private static final long serialVersionUID = 1L;

	private static final Log LOG = LogFactory.getLog(BeanGeracaoEtiquetaProcesso.class);

	private BeanDestinatario beanDestinatario;
	private List<EtiquetaProcessoVo> lista;
	private String codigoServidor;
	private List<SelectItem> listaAssinadores;
	private String codigoAndamento;
	private List<SelectItem> listaAndamentos;
	private String descricaoDestinatarioSelecionado;
	private String selecaoQtdEtiquetas = "1";
	private List<Integer> comboEtiquetas;
	private List<VwServidorAssinador> servidores;
	private List<ConfiguracaoEncaminhamento> encaminhamentos;
	private EtiquetaProcessoVo etiqueta;
	private DestinatarioListaRemessa destinatarioListaRemessa;

	public String gerarEtiquetaProcesso() {
		carregarComboEtiqueta();
		carregarComboAssinadores();
		carregarComboAndamentos();
		lista = new ArrayList<EtiquetaProcessoVo>();
		limparCampos(true);
		return "etiquetaProcesso";
	}

	private void limparCampos(boolean todos) {
		if (todos) {
			codigoServidor = "";
			codigoAndamento = "";
			beanDestinatario.setCampoPesquisa("");
			selecaoQtdEtiquetas = "1";
			lista.clear();
		}
		etiqueta = new EtiquetaProcessoVo();
		descricaoDestinatarioSelecionado = "";
	}

	public void limpar(ActionEvent event) {
		limparCampos(true);
	}

	public BeanDestinatario getBeanDestinatario() {
		return beanDestinatario;
	}

	public void setBeanDestinatario(BeanDestinatario beanDestinatario) {
		this.beanDestinatario = beanDestinatario;
	}

	public List<EtiquetaProcessoVo> getLista() {
		return lista;
	}

	public void setLista(List<EtiquetaProcessoVo> lista) {
		this.lista = lista;
	}

	public String getCodigoServidor() {
		return codigoServidor;
	}

	public void setCodigoServidor(String codigoServidor) {
		this.codigoServidor = codigoServidor;
	}

	public List<SelectItem> getListaAssinadores() {
		return listaAssinadores;
	}

	public void setListaAssinadores(List<SelectItem> listaAssinadores) {
		this.listaAssinadores = listaAssinadores;
	}

	public String getCodigoAndamento() {
		return codigoAndamento;
	}

	public void setCodigoAndamento(String codigoAndamento) {
		this.codigoAndamento = codigoAndamento;
	}

	public List<SelectItem> getListaAndamentos() {
		return listaAndamentos;
	}

	public void setListaAndamentos(List<SelectItem> listaAndamentos) {
		this.listaAndamentos = listaAndamentos;
	}

	public String getDescricaoDestinatarioSelecionado() {
		return descricaoDestinatarioSelecionado;
	}

	public void setDescricaoDestinatarioSelecionado(String descricaoDestinatarioSelecionado) {
		this.descricaoDestinatarioSelecionado = descricaoDestinatarioSelecionado;
	}

	public List<Integer> getComboEtiquetas() {
		return comboEtiquetas;
	}

	public void setComboEtiquetas(List<Integer> comboEtiquetas) {
		this.comboEtiquetas = comboEtiquetas;
	}

	public String getSelecaoQtdEtiquetas() {
		return selecaoQtdEtiquetas;
	}

	public void setSelecaoQtdEtiquetas(String selecaoQtdEtiquetas) {
		this.selecaoQtdEtiquetas = selecaoQtdEtiquetas;
	}
	
	public boolean isExibirLista() {
		return (lista == null ? false : (lista.size() == 0 ? false : true));
	}

	private void carregarComboEtiqueta() {
		try {
			comboEtiquetas = new ArrayList<Integer>();

			for(int indice = 1; indice <= 11; indice++) {
				comboEtiquetas.add(indice);
			}
		} catch (Exception e) {
			reportarErro("Erro ao carregar combo Qtd de etiq.", e, LOG);
		}
	}

	private void carregarComboAssinadores() {
		listaAssinadores = new ArrayList<SelectItem>();
		listaAssinadores.add(new SelectItem(null, null));
		try {
			servidores = getVwServidorAssinadorService().listar();
			if (servidores != null) {
				Collections.sort(servidores, new Comparator<VwServidorAssinador>() {  
					 public int compare(VwServidorAssinador assinador1, VwServidorAssinador assinador2) {  
						 String nome1 = assinador1.getNomeFuncionario();  
						 String nome2 = assinador2.getNomeFuncionario();
						 int resultado = 0;
						 if (nome1 != null || nome2 != null) {
							 if (nome2 == null) {
								 resultado = -1;
							 } else if (nome1 == null) {
								 resultado = 1;
							 } else {
								 resultado = nome1.compareToIgnoreCase(nome2);
							 }
						 }
						 return resultado;  
					 }  
					});
				for (VwServidorAssinador servidor : servidores) {
					listaAssinadores.add(new SelectItem(servidor.getId(), servidor.getNomeFuncionario()));
				}
			}
		} catch (Exception e) {
			reportarErro("Erro ao carregar combo de assinadores.", e, LOG);
		}
	}

	private void carregarComboAndamentos() {
		listaAndamentos = new ArrayList<SelectItem>();
		listaAndamentos.add(new SelectItem(null, null));
		try {
			encaminhamentos = getConfiguracaoEncaminhamentoService().listar();
			Collections.sort(encaminhamentos, new Comparator<ConfiguracaoEncaminhamento>() {  
				 public int compare(ConfiguracaoEncaminhamento configuracaoEncaminhamento1, ConfiguracaoEncaminhamento configuracaoEncaminhamento2) {  
					 int comparar = (int) configuracaoEncaminhamento1.getCodigoAndamento() - configuracaoEncaminhamento2.getCodigoAndamento();
					 return comparar;  
					 }  
				});
			for (ConfiguracaoEncaminhamento encaminhamento : encaminhamentos) {
				listaAndamentos.add(new SelectItem(encaminhamento.getCodigoAndamento(), encaminhamento.getCodigoAndamento() + " - " + encaminhamento.getTitulo()));
			}
		} catch (Exception e) {
			reportarErro("Erro ao carregar combo de assinadores.", e, LOG);
		}
	}

	public void adicionarEtiqueta(ActionEvent event) {
		if(!isErroValidacaoAdicionarEtiqueta()){
			VwServidorAssinador servidorSelecionado = null;
			ConfiguracaoEncaminhamento encaminhamentoSelecionado = null;
			Andamento andamentoSelecionado = null;
			if (servidores != null) {
				for (VwServidorAssinador servidorAux : servidores) {
					if (servidorAux.getId().equals(codigoServidor)) {
						servidorSelecionado = servidorAux;
						break;
					}
				}
			}
			try {
				Long codigoAndamentoAux = Long.parseLong(codigoAndamento);
				encaminhamentoSelecionado = getConfiguracaoEncaminhamentoService().buscar(codigoAndamentoAux);
				andamentoSelecionado = getAndamentoService().recuperarPorId(codigoAndamentoAux);
			} catch (NumberFormatException e) {
				reportarErro("Erro ao pesquisar Andamento.", e, LOG);
			} catch (ServiceException e) {
				reportarErro("Erro ao pesquisar Andamento.", e, LOG);
			}
			etiqueta = new EtiquetaProcessoVo(servidorSelecionado, encaminhamentoSelecionado, andamentoSelecionado, destinatarioListaRemessa);
			lista.add(etiqueta);
			limparCampos(false);
		}
	}

	public void excluirEtiqueta(ActionEvent event) {
		HtmlDataTable tabela = (HtmlDataTable) event.getComponent().getParent().getParent();
		Object rowData = tabela.getRowData();

		if(rowData instanceof EtiquetaProcessoVo) {
			etiqueta = (EtiquetaProcessoVo) rowData;
		}

		if (lista == null) {
			lista = new ArrayList<EtiquetaProcessoVo>();
		}

		lista.remove(etiqueta);
		
	}

	@Override
	public String getNomeComponenteRerenderizar() {
		return "panelEtiqueta";
	}

	@Override
	public void limparCamposDestinatario() {
	}

	@Override
	public void receberDestinatarioSelecionado(DestinatarioListaRemessa destinatarioListaRemessa) {
		if (destinatarioListaRemessa != null) {
			this.destinatarioListaRemessa = destinatarioListaRemessa;
			descricaoDestinatarioSelecionado = destinatarioListaRemessa.getDescricaoPrincipal();
		}
	}

	public void gerarEtiquetasPDF(ActionEvent event) {
		if (lista.size() <= 0) {
			reportarAviso("Favor inserir um andamento para geração da(s) etiquetas.");
		} else {
			ExpedicaoRelatorioServiceLocal expedicaoRelatorioService = new ExpedicaoRelatorioServiceLocal();
			byte[] arquivo = null;
			int posicao = Integer.parseInt(selecaoQtdEtiquetas);

			List<EtiquetaProcessoVo> listaAux = new ArrayList<EtiquetaProcessoVo>(lista);
			for (int indice = 0; indice < posicao - 1; indice++) {
				etiqueta = new EtiquetaProcessoVo(false);
				listaAux.add(indice, etiqueta);
			}
			
			try {
				arquivo = expedicaoRelatorioService.criarEtiquetasProcesso(listaAux, Util.getSiglasNomeFuncionario(getNomeUsuarioAutenticado()));
				ByteArrayInputStream input = new ByteArrayInputStream(arquivo);
				Util.mandarRespostaDeDownloadDoArquivoPdf(input, "EtiquetasProcesso");
			} catch (FileNotFoundException e) {
				LOG.error("Falha ao criar etiquetas.", e);
				reportarErro("Falha ao criar etiquetas.");
			} catch (JRException e) {
				LOG.error("Falha ao criar etiquetas.", e);
				reportarErro("Falha ao criar etiquetas.");
			} catch (ServiceException e) {
				LOG.error("Falha ao criar etiquetas.", e);
				reportarErro("Falha ao criar etiquetas.");
			}
		}
	}
	
	private String getMensagemCampoObrigatorio(String nomeCampo) {
		return "Favor preencher o campo obrigatório '" + nomeCampo + "'.";
	}
	
	private boolean isErroValidacaoAdicionarEtiqueta() {
		String mensagemErro = "";
		if (codigoServidor == null) {
			mensagemErro = getMensagemCampoObrigatorio("Assinador");
		} else if (codigoAndamento == null) {
			mensagemErro = getMensagemCampoObrigatorio("Código Andamento");
		} else if (descricaoDestinatarioSelecionado == null || descricaoDestinatarioSelecionado.equals("")) {
			mensagemErro = "É necessário selecionar um destinatário.";
		}

		if (!mensagemErro.isEmpty()) {
			reportarAviso(mensagemErro);
		}

		return !mensagemErro.isEmpty();
	}
}