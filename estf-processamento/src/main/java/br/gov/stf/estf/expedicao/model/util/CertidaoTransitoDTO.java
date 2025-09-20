package br.gov.stf.estf.expedicao.model.util;

import java.util.List;

import br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.andamento.InforParte;


public class CertidaoTransitoDTO {
	
	
	private final String processo;		
	private final String corpo;
	private final String nomeServidor;
	private final String matServidor;
	private final List<InforParte> partes;
	
	private CertidaoTransitoDTO(
			final String processo,		
			final String corpo,
			final String nomeServidor,
			final String matServidor,
			final List<InforParte> partes){
		this.processo = processo;
		this.corpo = corpo;
		this.nomeServidor = nomeServidor;
		this.matServidor = matServidor;
		this.partes = partes;
	}	
	

	public String getProcesso() {
		return processo;
	}

	public String getCorpo() {
		return corpo;
	}

	public String getNomeServidor() {
		return nomeServidor;
	}

	public String getMatServidor() {
		return matServidor;
	}
	
	public List<InforParte> getPartes(){
		return partes;
	}


	public static class Builder{
		private String processo;		
		private String corpo;
		private String nomeServidor;
		private String matServidor;	
		private List<InforParte> partes;
		
		public Builder processo(String processo){
			this.processo = processo;
			return this;
		}
		
		public Builder corpo(String corpo){
			this.corpo = corpo;
			return this;
		}
		
		public Builder nomeServidor(String nomeServidor){
			this.nomeServidor = nomeServidor;
			return this;
		}		

		public Builder matServidor(String matServidor){
			this.matServidor = matServidor;
			return this;
		}
		
		public Builder partes(List<InforParte> partes){
			this.partes = partes;
			return this;
		}
		
		public CertidaoTransitoDTO builder(){
			return new CertidaoTransitoDTO(processo,corpo,nomeServidor,matServidor, partes);
		}

	}
	
	

}
