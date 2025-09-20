package br.gov.stf.estf.expedicao.model.util;

import java.util.List;

import br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.andamento.InforParte;


public class ContrarrazaoDTO {
	
	
	private final String processo;		
	private final String corpo;
	private final String nomeFuncaoSecretaria;
	private final String nomeSecao;
	private final List<InforParte> partes;
	
	private ContrarrazaoDTO(
			final String processo,		
			final String corpo,
			final String nomeFuncaoSecretaria,
			final String nomeSecao,
			final List<InforParte> partes){
		this.processo = processo;
		this.corpo = corpo;
		this.nomeFuncaoSecretaria = nomeFuncaoSecretaria;
		this.nomeSecao = nomeSecao;
		this.partes = partes;
	}	
	

	public String getProcesso() {
		return processo;
	}

	public String getCorpo() {
		return corpo;
	}

	public String getNomeFuncaoSecretaria() {
		return nomeFuncaoSecretaria;
	}

	public String getNomeSecao() {
		return nomeSecao;
	}
	
	public List<InforParte> getPartes(){
		return partes;
	}


	public static class Builder{
		private String processo;		
		private String corpo;
		private String nomeFuncaoSecretaria;
		private String nomeSecao;	
		private List<InforParte> partes;
		
		public Builder processo(String processo){
			this.processo = processo;
			return this;
		}
		
		public Builder corpo(String corpo){
			this.corpo = corpo;
			return this;
		}
		
		public Builder nomeFuncaoSecretaria(String nomeFuncaoSecretaria){
			this.nomeFuncaoSecretaria = nomeFuncaoSecretaria;
			return this;
		}		

		public Builder nomeSecao(String nomeSecao){
			this.nomeSecao = nomeSecao;
			return this;
		}
		
		public Builder partes(List<InforParte> partes){
			this.partes = partes;
			return this;
		}
		
		public ContrarrazaoDTO builder(){
			return new ContrarrazaoDTO(processo,corpo,nomeFuncaoSecretaria,nomeSecao, partes);
		}

	}
	
	

}
