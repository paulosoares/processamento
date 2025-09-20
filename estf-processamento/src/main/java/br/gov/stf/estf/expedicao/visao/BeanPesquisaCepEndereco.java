package br.gov.stf.estf.expedicao.visao;

import java.util.List;

import javax.faces.event.ActionEvent;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.expedicao.entidade.VwEndereco;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * Bean que visualiza informações a respeito da lista de remessa da expedição.
 *
 * @author roberio.fernandes
 */
public class BeanPesquisaCepEndereco extends AssinadorBaseBean {

	private static final long serialVersionUID = 1L;

	private String filtro;
	private List<VwEndereco> enderecos;
	private VwEndereco endereco;
	private SelecionaCep selecionaCep;

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	public List<VwEndereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<VwEndereco> enderecos) {
		this.enderecos = enderecos;
	}

	public boolean isResultadosObtidos() {
		return enderecos != null;
	}

	public int getQtdRegistrosEncontrados() {
		int resultado = 0;
		if (isResultadosObtidos()) {
			resultado = enderecos.size();
		}
		return resultado;
	}

	public VwEndereco getEndereco() {
		return endereco;
	}

	public void setEndereco(VwEndereco endereco) {
		this.endereco = endereco;
	}

	public SelecionaCep getSelecionaCep() {
		return selecionaCep;
	}

	public void setSelecionaCep(SelecionaCep selecionaCep) {
		this.selecionaCep = selecionaCep;
	}

	private void limparDados() {
		filtro = "";
		enderecos = null;
		endereco = null;
	}

	public void abrirModoPesquisaDialogo() {
		limparDados();
		if (selecionaCep != null) {
			selecionaCep.limparCamposSelecaoCep();
		}
	}

	public void buscarCep() {
		buscarCep(null);
	}

	public void buscarCep(ActionEvent event) {
		enderecos = null;
		endereco = null;
		if (selecionaCep != null) {
			selecionaCep.limparCamposSelecaoCep();
			try {
				VwEndereco vwEnderecoPesquisa = new VwEndereco();
				vwEnderecoPesquisa.setCliente(filtro);
				vwEnderecoPesquisa.setMunicipio(filtro);
				vwEnderecoPesquisa.setBairro(filtro);
				vwEnderecoPesquisa.setLogradouro(filtro);
				enderecos = getVwEnderecoService().pesquisar(vwEnderecoPesquisa);
			} catch (ServiceException e) {
				selecionaCep.manipularErroAoExecutarPesquisaSelecaoCep(e);
			}
		}
	}

	public void selecionar() {
		if (selecionaCep != null) {
			selecionaCep.receberObjetoSelecionadoSelecaoCep(endereco);
		}
		limparDados();
	}

	public String getNomeComponenteAtualizar() {
		String resultado = "";
		if (selecionaCep != null) {
			resultado = selecionaCep.getNomeComponenteRerenderizarSelecaoCep();
		}
		return resultado;
	}

	public void cancelarPesquisa(ActionEvent event) {
		if (selecionaCep != null) {
			selecionaCep.cancelarPesquisa();
		}
		limparDados();
	}
}

interface SelecionaCep {

	String getNomeComponenteRerenderizarSelecaoCep();

	void limparCamposSelecaoCep();

	void receberObjetoSelecionadoSelecaoCep(VwEndereco endereco);

	void manipularErroAoExecutarPesquisaSelecaoCep(Exception exception);

	void cancelarPesquisa();
}