package br.gov.stf.estf.processostf.model.util;


import java.io.Serializable;

import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.processostf.ItemControle;

public class ItemControleResult implements Serializable{

	private static final long serialVersionUID = 428160160342623465L;
	
	private ItemControle itemControle;
	private String nomeSetorDestino;
	private ArquivoProcessoEletronico arquivoProcessoEletronico;
	
	public ItemControleResult(ItemControle itemControle, String nomeSetorDestino, ArquivoProcessoEletronico arquivoProcessoEletronico) {
		this.itemControle = itemControle;
		this.nomeSetorDestino = nomeSetorDestino;
		this.arquivoProcessoEletronico = arquivoProcessoEletronico;
	}
	
	public ItemControleResult(){
	}
	
	public ItemControle getItemControle() {
		return itemControle;
	}
	
	public void setItemControle(ItemControle itemControle) {
		this.itemControle = itemControle;
	}
	
	
	public String getNomeSetorDestino() {
		return nomeSetorDestino;
	}

	public void setNomeSetorDestino(String nomeSetorDestino) {
		this.nomeSetorDestino = nomeSetorDestino;
	}

	public ArquivoProcessoEletronico getArquivoProcessoEletronico() {
		return arquivoProcessoEletronico;
	}

	public void setArquivoProcessoEletronico(
			ArquivoProcessoEletronico arquivoProcessoEletronico) {
		this.arquivoProcessoEletronico = arquivoProcessoEletronico;
	}

}
