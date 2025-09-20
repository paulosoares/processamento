package br.gov.stf.estf.expedicao.model.util;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.expedicao.entidade.ListaRemessa;


public class FinalizarRemessaDTO {
	
	
	private final ListaRemessa listaRemessa;		
	private final Setor setorUsuarioAutenticado;
	private final Andamento andamento;
	private final Andamento andamentoDefaut;
	private final String idUsuarioLogado;
	
	private FinalizarRemessaDTO(
			final ListaRemessa listaRemessa,		
			final Setor setorUsuarioAutenticado,
			final Andamento andamento,
			final Andamento andamentoDefaut,
			final String idUsuarioLogado){
		this.listaRemessa = listaRemessa;
		this.setorUsuarioAutenticado = setorUsuarioAutenticado;
		this.andamento = andamento;
		this.andamentoDefaut = andamentoDefaut;
		this.idUsuarioLogado = idUsuarioLogado;
	}	
	
	public ListaRemessa getListaRemessa() {
		return listaRemessa;
	}
	
	public Setor getSetorUsuarioAutenticado() {
		return setorUsuarioAutenticado;
	}

	public Andamento getAndamento() {
		return andamento;
	}
	
	public Andamento getAndamentoDefaut() {
		return andamentoDefaut;
	}
	
	public String getIdUsuarioLogado() {
		return idUsuarioLogado;
	}

	public static class Builder{
		private ListaRemessa listaRemessa;		
		private Setor setorUsuarioAutenticado;
		private Andamento andamento;
		private Andamento andamentoDefaut;
		private String idUsuarioLogado;		
		
		public Builder setListaRemessa(ListaRemessa listaRemessa){
			this.listaRemessa = listaRemessa;
			return this;
		}
		
		public Builder setSetorUsuarioAutenticado(Setor setorUsuarioAutenticado){
			this.setorUsuarioAutenticado = setorUsuarioAutenticado;
			return this;
		}
		
		public Builder setAndamento(Andamento andamento){
			this.andamento = andamento;
			return this;
		}
		
		public Builder setIdUsuarioLogado(String idUsuarioLogado){
			this.idUsuarioLogado = idUsuarioLogado;
			return this;
		}	
		
		public Builder setAndamentoDefaut(Andamento andamentoDefaut) {
			this.andamentoDefaut = andamentoDefaut;
			return this;
		}
		
		public FinalizarRemessaDTO builder(){
			return new FinalizarRemessaDTO(listaRemessa,setorUsuarioAutenticado,andamento,andamentoDefaut,idUsuarioLogado);
		}

	}
	
	

}
