package br.gov.stf.estf.expedicao.visao;

import static br.gov.stf.estf.expedicao.visao.Util.mandarRespostaDeDownloadDoArquivoPdf;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.richfaces.component.html.HtmlDataTable;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.expedicao.entidade.ContratoPostagem;
import br.gov.stf.estf.expedicao.entidade.ListaRemessa;
import br.gov.stf.estf.expedicao.entidade.Remessa;
import br.gov.stf.estf.expedicao.entidade.Remetente;
import br.gov.stf.estf.expedicao.entidade.TipoServico;
import br.gov.stf.estf.expedicao.model.service.ListaRemessaService;
import br.gov.stf.estf.expedicao.model.service.RemessaService;
import br.gov.stf.estf.expedicao.model.service.impl.ExpedicaoRelatorioServiceLocal;
import br.gov.stf.estf.expedicao.model.util.RelatorioRemessaDTO;
import br.gov.stf.estf.expedicao.model.util.TipoEntregaEnum;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * Bean que visualiza informações a respeito da lista de remessa da expedição.
 *
 * @author roberio.fernandes
 */
public class BeanVisualizarListaRemessa extends AssinadorBaseBean {

	private static final long serialVersionUID = 1L;

	private static final Log LOG = LogFactory.getLog(BeanVisualizarListaRemessa.class);

	private PesquisaListaRemessa pesquisaListaRemessa;
	private HtmlDataTable tabelaRemessasCriadas;
	private Long idListaRemessa;
	private Long idRemessa;
	private ListaRemessa listaRemessa;
	private ListaRemessa listaRemessaVisualizar;
	private Remessa remessa;
	private List<ListaRemessa> pilhaListasRemessa;
	private Integer quantidadeVolume;
	private List<Remessa> consultaRemessa;
	private List<Remessa> relatorioRemessaRegistrosRemover;
	private List<Integer> comboEtiquetas;
	private String selecaoQtdEtiquetas = "1";
	private boolean posicionamentoEtiquetas = false;
	private RelatorioRemessaDTO relatorioRemessaDTO;
	private boolean exibirBotaoVoltar;

	public String getPais() {
		return BeanListaRemessa.PAIS;
	}

	public PesquisaListaRemessa getPesquisaListaRemessa() {
		return pesquisaListaRemessa;
	}

	public void setPesquisaListaRemessa(PesquisaListaRemessa pesquisaListaRemessa) {
		this.pesquisaListaRemessa = pesquisaListaRemessa;
	}

	public HtmlDataTable getTabelaRemessasCriadas() {
		return tabelaRemessasCriadas;
	}

	public void setTabelaRemessasCriadas(HtmlDataTable tabelaRemessasCriadas) {
		this.tabelaRemessasCriadas = tabelaRemessasCriadas;
	}

	public List<Remessa> getConsultaRemessa() {
		return consultaRemessa;
	}

	public void setConsultaRemessa(List<Remessa> consultaRemessa) {
		this.consultaRemessa = consultaRemessa;
	}

	public List<Remessa> getRelatorioRemessaRegistrosRemover() {
		return relatorioRemessaRegistrosRemover;
	}

	public void setRelatorioRemessaRegistrosRemover(List<Remessa> relatorioRemessaRegistrosRemover) {
		this.relatorioRemessaRegistrosRemover = relatorioRemessaRegistrosRemover;
	}

	public Long getIdListaRemessa() {
		return idListaRemessa;
	}

	public void setIdListaRemessa(Long idListaRemessa) {
		this.idListaRemessa = idListaRemessa;
	}

	public Long getIdRemessa() {
		return idRemessa;
	}

	public void setIdRemessa(Long idRemessa) {
		this.idRemessa = idRemessa;
	}

	public ListaRemessa getListaRemessa() {
		return listaRemessa;
	}

	public void setListaRemessa(ListaRemessa listaRemessa) {
		this.listaRemessa = listaRemessa;
	}

	public ListaRemessa getListaRemessaVisualizar() {
		return listaRemessaVisualizar;
	}

	public void setListaRemessaVisualizar(ListaRemessa listaRemessaVisualizar) {
		this.listaRemessaVisualizar = listaRemessaVisualizar;
	}

	public Remessa getRemessa() {
		return remessa;
	}

	public void setRemessa(Remessa remessa) {
		this.remessa = remessa;
	}

	public List<ListaRemessa> getPilhaListasRemessa() {
		if (pilhaListasRemessa == null) {
			pilhaListasRemessa = new ArrayList<ListaRemessa>();
		}
		return pilhaListasRemessa;
	}

	public void setPilhaListasRemessa(List<ListaRemessa> pilhaListasRemessa) {
		this.pilhaListasRemessa = pilhaListasRemessa;
	}

	public Integer getQuantidadeVolume() {
		if (remessa != null){
			if (remessa.getVolumes() != null) {
				return remessa.getVolumes().size();
			}
		}
		return quantidadeVolume;
	}

	public void setQuantidadeVolume(Integer quantidadeVolume) {
		this.quantidadeVolume = quantidadeVolume;
	}

	private void empilharListaRemessa(ListaRemessa listaRemessa) {
		if (listaRemessa != null) {
			getPilhaListasRemessa().add(listaRemessa);
		}
	}

	private ListaRemessa desempilharListaRemessa() {
		ListaRemessa ultimaLista = null;
		int ultimaPosicao = getPilhaListasRemessa().size() - 1;
		if (ultimaPosicao >= 0) {
			ultimaLista = pilhaListasRemessa.get(ultimaPosicao);
			pilhaListasRemessa.remove(ultimaPosicao);
		}
		return ultimaLista;
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

	public boolean isPosicionamentoEtiquetas() {
		return posicionamentoEtiquetas;
	}

	public void setPosicionamentoEtiquetas(boolean posicionamentoEtiquetas) {
		this.posicionamentoEtiquetas = posicionamentoEtiquetas;
	}

	public RelatorioRemessaDTO getRelatorioRemessaDTO() {
		return relatorioRemessaDTO;
	}

	public void setRelatorioRemessaDTO(RelatorioRemessaDTO relatorioRemessaDTO) {
		this.relatorioRemessaDTO = relatorioRemessaDTO;
	}

	public boolean isExibirBotaoVoltar() {
		return exibirBotaoVoltar;
	}

	public void setExibirBotaoVoltar(boolean exibirBotaoVoltar) {
		this.exibirBotaoVoltar = exibirBotaoVoltar;
	}

	public String visualizarListaRemessa() {
		visualizarListaRemessa(listaRemessaVisualizar);
		exibirBotaoVoltar = true;
		selecaoQtdEtiquetas = "1";
		return "visualizarRemessa";
	}

	public void visualizarListaRemessa(ActionEvent event) {
		HtmlDataTable tabela = (HtmlDataTable) event.getComponent().getParent().getParent();
		Object rowData = tabela.getRowData();
		selecaoQtdEtiquetas = "1";
		listaRemessa = null;
		remessa = null;
		relatorioRemessaDTO = null;
		exibirBotaoVoltar = true;
		if (rowData instanceof RelatorioRemessaDTO) {
			getPilhaListasRemessa().clear();
			relatorioRemessaDTO = (RelatorioRemessaDTO) rowData;
			remessa = relatorioRemessaDTO.getRemessa();
			listaRemessaVisualizar = remessa.getListaRemessa();
			Remessa remessaVisualizar = remessa;
			visualizarListaRemessa(listaRemessaVisualizar, remessaVisualizar);
		} else if (rowData instanceof ListaRemessa) {
			visualizarListaRemessa((ListaRemessa) rowData);
		} else if (idListaRemessa != null) {
			try {
				visualizarListaRemessa(getListaRemessaService().recuperarPorId(idListaRemessa));
			} catch (ServiceException e) {
			}
		}
	}

	public void visualizarListaRemessa(ListaRemessa listaRemessa) {
		exibirBotaoVoltar = false;
		selecaoQtdEtiquetas = "1";
		visualizarListaRemessa(listaRemessa, listaRemessa.getRemessas().get(0));
	}

	private void visualizarListaRemessa(ListaRemessa listaRemessa, Remessa remessa) {
		empilharListaRemessa(this.listaRemessa);
		exibirListaRemessa(listaRemessa, remessa);
	}

	private void exibirListaRemessa(ListaRemessa listaRemessa, Remessa remessa) {
		if (listaRemessa == null) {
			reportarErro("Lista de remessas não informada ou inválida.");
			return;
		}

		this.listaRemessa = listaRemessa;
		this.remessa = remessa;
		consultaRemessa = new ArrayList<Remessa>(this.listaRemessa.getRemessas());
		relatorioRemessaRegistrosRemover = new ArrayList<Remessa>();
		carregarComboEtiqueta();
	}

	private void carregarComboEtiqueta() {
		try {
			comboEtiquetas = new ArrayList<Integer>();
			TipoServico tipoServico = listaRemessa.getTipoServico();
			for(int indice = 1; indice <= (int) tipoServico.getQuantidadeEtiquetasPagina(); indice++) {
				comboEtiquetas.add(indice);
			}
			setPosicionamentoEtiquetas(comboEtiquetas.size() > 4);
		} catch (Exception e) {
			reportarErro("Erro ao carregar combo Qtd de etiq.", e, LOG);
		}
	}

	public boolean isExibirCampoServicoAdicional() {
		return !remessa.getTiposServicoNaoObrigatorios().isEmpty();
	}

	public boolean isExibirCorreiosOuPortaria() {
		TipoEntregaEnum tipoEntregaEnum = listaRemessa.getTipoEntrega();
		boolean retorno = (TipoEntregaEnum.CORREIOS.equals(tipoEntregaEnum) || TipoEntregaEnum.ENTREGA_PORTARIA.equals(tipoEntregaEnum));
		return retorno;
	}

	public boolean isExibirMalote() {
		boolean retorno = false;
		if (TipoEntregaEnum.MALOTE.equals(listaRemessa.getTipoEntrega()) || TipoEntregaEnum.ENTREGA_PORTARIA.equals(listaRemessa.getTipoEntrega())) {
			retorno = true;
		}
		return retorno;
	}

	public boolean isVerificarTipoDocumento() {
		return remessa.getNumeroComunicacao() != null;
	}

	public boolean isMalote() {
		TipoEntregaEnum tipoEntregaEnum = listaRemessa.getTipoEntrega();
		boolean retorno = (TipoEntregaEnum.MALOTE.equals(tipoEntregaEnum));
		return retorno;
	}

	public void excluirRemessa(ActionEvent event) {
		Remessa remessaSelecionada = getRemessaSelecionada();
		consultaRemessa.remove(remessaSelecionada);
		relatorioRemessaRegistrosRemover.add(remessaSelecionada);
	}

	private Remessa getRemessaSelecionada() {
		Remessa remessaSelecionada = (Remessa) tabelaRemessasCriadas.getRowData();
		return remessaSelecionada;
	}

	public void imprimirLista(ActionEvent evt) {
		byte[] arquivo;
		ExpedicaoRelatorioServiceLocal expedicaoRelatorioService = new ExpedicaoRelatorioServiceLocal();
		if (listaRemessa == null || listaRemessa.getRemessas() == null || listaRemessa.getRemessas().isEmpty()) {
			reportarAviso("Lista vazia.");
			return;
		}

		try {
			Remetente remetente = getRemetente();
			ContratoPostagem contratoPostagem = getContratoPostagemVigente();

			String nomeUsuario = getNomeUsuario(listaRemessa.getUsuarioCriacao());

			if (listaRemessa.getTipoEntrega().equals(TipoEntregaEnum.CORREIOS)) {
				arquivo = expedicaoRelatorioService.criarListaCorreios(listaRemessa,
						nomeUsuario,
						contratoPostagem.getCartao(),
						contratoPostagem.getNumero(),
						contratoPostagem.getCodigoAdministrativo(),
						remetente);
				ByteArrayInputStream input = new ByteArrayInputStream(arquivo);
				mandarRespostaDeDownloadDoArquivoPdf(input, "ListaEntregaCorreios");
			} else if (listaRemessa.getTipoEntrega().equals(TipoEntregaEnum.ENTREGA_PORTARIA)) {
				arquivo = expedicaoRelatorioService.criarListaPortaria(listaRemessa, nomeUsuario);
				ByteArrayInputStream input = new ByteArrayInputStream(arquivo);
				mandarRespostaDeDownloadDoArquivoPdf(input, "ListaEntregaPortaria");
			} else if (listaRemessa.getTipoEntrega().equals(TipoEntregaEnum.MALOTE)) {
				arquivo = expedicaoRelatorioService.criarListaMalote(listaRemessa);
				ByteArrayInputStream input = new ByteArrayInputStream(arquivo);
				mandarRespostaDeDownloadDoArquivoPdf(input, "ListaMalote");
			}
		} catch (Exception e) {
			reportarErro("Erro ao Criar Relatório", e, LOG);
		}
	}

	private ContratoPostagem getContratoPostagemVigente() throws ServiceException {
		ContratoPostagem contratoPostagem = getContratoPostagemService().buscarVigente();
		if (contratoPostagem == null) {
			throw new ServiceException("Nenhum contrato de cartão de postagem vigênte encontrado.");
		}
		return contratoPostagem;
	}

	private Remetente getRemetente() throws ServiceException {
		List<Remetente> remetentes = getRemetenteService().listarTodos();
		if (remetentes.size() != 1) {
			throw new ServiceException("Número de remetentes cadastrados diferente de 1 (" + remetentes.size() + ")");
		}
		Remetente remetente = remetentes.get(0);
		return remetente;
	}

	public String salvarLista() {
		String resultado = "";
		try {
			if (listaRemessa.getId() != null && consultaRemessa.isEmpty()) {
				relatorioRemessaRegistrosRemover = null;
				remessa = null;
				ListaRemessaService listaRemessaService = getListaRemessaService();
				listaRemessaService.excluir(listaRemessa);
				reportarInformacao("Lista excluída por não possuir Remessas!");
				resultado = "remessaPesquisa";
				if (pesquisaListaRemessa != null) {
					pesquisaListaRemessa.executarPesquisa();
				}
			} else {
				RemessaService remessaService = getRemessaService();
				List<Remessa> remessasExcluidas = listarRemessasExcluidas();
				listaRemessa.getRemessas().removeAll(remessasExcluidas);
				remessaService.excluirTodos(remessasExcluidas);
				reportarInformacao("Lista salva com sucesso!");
			}
		} catch (Exception e) {
			reportarErro("Erro ao Salvar Lista de Remessas", e, LOG);
		}
		return resultado;
	}

	private List<Remessa> listarRemessasExcluidas() {
		List<Remessa> remessasExcluidas = new ArrayList<Remessa>();
		for (Remessa remessaLocal : relatorioRemessaRegistrosRemover) {
			remessasExcluidas.add(remessaLocal);
		}
		return remessasExcluidas;
	}

	public void imprimirEtiquetasIndividual(ActionEvent evt) {
		int posicaoEtiquetas = Integer.parseInt(selecaoQtdEtiquetas);

		Remessa remessaSelecionada = getRemessaSelecionada();

		byte[] arquivo;
		ExpedicaoRelatorioServiceLocal expedicaoRelatorioService = new ExpedicaoRelatorioServiceLocal();
		try {
			ContratoPostagem contratoPostagem = getContratoPostagemVigente();

			if (TipoEntregaEnum.CORREIOS.equals(listaRemessa.getTipoEntrega())) {
				arquivo = expedicaoRelatorioService.criarEtiquetasCorreioIndividual(remessaSelecionada,
								contratoPostagem.getCartao(),
								contratoPostagem.getNumero(),
								posicaoEtiquetas,
								getRemetente());
				ByteArrayInputStream input = new ByteArrayInputStream(arquivo);
				mandarRespostaDeDownloadDoArquivoPdf(input, "etiquetaCorreios");
			} else if (TipoEntregaEnum.ENTREGA_PORTARIA.equals(listaRemessa.getTipoEntrega())) {
				arquivo = expedicaoRelatorioService.criarEtiquetasPortariaIndividual(remessa, posicaoEtiquetas);
				ByteArrayInputStream input = new ByteArrayInputStream(arquivo);
				mandarRespostaDeDownloadDoArquivoPdf(input, "etiquetaEntregaMaos");
			}

		} catch (Exception e) {
			reportarErro("Erro ao Criar Relatório", e, LOG);
		}
	}

	public void imprimirEtiquetasLista(ActionEvent evt) {
		int posicaoEtiquetas = Integer.parseInt(selecaoQtdEtiquetas);
		byte[] arquivo;
		ExpedicaoRelatorioServiceLocal expedicaoRelatorioService = new ExpedicaoRelatorioServiceLocal();
		if (listaRemessa == null || listaRemessa.getRemessas() == null || listaRemessa.getRemessas().isEmpty()) {
			reportarAviso("Lista vazia.");
			return;
		}
		try {
			ContratoPostagem contratoPostagem = getContratoPostagemVigente();

			if (listaRemessa.getTipoEntrega().equals(TipoEntregaEnum.CORREIOS)) {
				arquivo = expedicaoRelatorioService.criarEtiquetasCorreio(listaRemessa,
								contratoPostagem.getCartao(),
								contratoPostagem.getNumero(),
								posicaoEtiquetas,
								getRemetente());

				ByteArrayInputStream input = new ByteArrayInputStream(arquivo);
				mandarRespostaDeDownloadDoArquivoPdf(input, "etiquetaCorreios");
			} else if (listaRemessa.getTipoEntrega().equals(TipoEntregaEnum.ENTREGA_PORTARIA)) {

				arquivo = expedicaoRelatorioService.criarEtiquetasPortaria(listaRemessa, posicaoEtiquetas);

				ByteArrayInputStream input = new ByteArrayInputStream(arquivo);
				mandarRespostaDeDownloadDoArquivoPdf(input, "etiquetaEntregaMaos");
			}

		} catch (Exception e) {
			reportarErro("Erro ao Criar Relatório", e, LOG);
		}
	}

	public void baixarImagem(ActionEvent evt) {
		HtmlDataTable tabela = (HtmlDataTable) evt.getComponent().getParent().getParent();
		RelatorioRemessaDTO relatorioRemessaDTOLocal = (RelatorioRemessaDTO) tabela.getRowData();
		ByteArrayInputStream input = new ByteArrayInputStream(relatorioRemessaDTOLocal.getRemessa().getListaRemessa().getImagemListaRemessa());
		mandarRespostaDeDownloadDoArquivoPdf(input, "documento");
	}

	public boolean isExibirImprimirEtiquetaIndividual() {
		Remessa remessaLocal = getRemessaSelecionada();
        return remessaLocal.getId() != null;
	}

	public void visualizarRemessa(ActionEvent event) {
		Remessa remessaSelecionada = getRemessaSelecionada();
		remessa = remessaSelecionada;
	}

	public String voltar() {
		String resultado;
		listaRemessa = desempilharListaRemessa();
		if (listaRemessa == null) {
			resultado = "remessaPesquisa";
			if (pesquisaListaRemessa != null) {
				pesquisaListaRemessa.executarPesquisa();
			}
		} else {
			exibirListaRemessa(listaRemessa, listaRemessa.getRemessas().get(0));
			resultado = "visualizarRemessa";
		}
		return resultado;
	}
}

interface PesquisaListaRemessa {

	void executarPesquisa();
}