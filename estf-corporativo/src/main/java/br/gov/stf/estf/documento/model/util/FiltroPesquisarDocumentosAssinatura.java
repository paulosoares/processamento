package br.gov.stf.estf.documento.model.util;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Setor;

public class FiltroPesquisarDocumentosAssinatura {
	
	private Setor setor;
	private List<Long> listaTipoSituacaoDocumento;
	private Long faseDocumento; 
	private List<Long> ids;
	private Date dataDocumento;
	private boolean carregarFilhos = true; // o padrão é carregar os filhos
	private String tela;
	private boolean apenasSigilosos = false;
	
	public Setor getSetor() {
		return setor;
	}
	
	public void setSetor(Setor setor) {
		this.setor = setor;
	}
	
	public List<Long> getListaTipoSituacaoDocumento() {
		return listaTipoSituacaoDocumento;
	}
	
	public void setListaTipoSituacaoDocumento(List<Long> listaTipoSituacaoDocumento) {
		this.listaTipoSituacaoDocumento = listaTipoSituacaoDocumento;
	}
	
	public Long getFaseDocumento() {
		return faseDocumento;
	}
	
	public void setFaseDocumento(Long faseDocumento) {
		this.faseDocumento = faseDocumento;
	}
	
	public List<Long> getIds() {
		return ids;
	}
	
	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
	
	public Date getDataDocumento() {
		return dataDocumento;
	}
	
	public void setDataDocumento(Date dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	public boolean isCarregarFilhos() {
		return carregarFilhos;
	}

	public void setCarregarFilhos(boolean carregarFilhos) {
		this.carregarFilhos = carregarFilhos;
	}
	
	public String getTela() {
		return tela;
	}
	
	public void setTela(String tela) {
		this.tela = tela;
	}

	public boolean isApenasSigilosos() {
		return apenasSigilosos;
	}

	public void setApenasSigilosos(boolean apenasSigilosos) {
		this.apenasSigilosos = apenasSigilosos;
	}

}
