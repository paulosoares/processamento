package br.gov.stf.estf.entidade.tarefa;

import java.util.List;

public class RelatorioAnaliticoTarefaSetor implements java.io.Serializable {
	private String identiciacoesProcessuais;
	private String localizacaoAtual;
	private String usuariosResponsaveis;
	private String descricao;
	private String situacao;
	private String dataInicio;
	private String dataFim;
	private String dataPrevistaInicio;
	private String dataPrevistaFim;
	private String prioridade;
	private String sigiloso;
	private String urgente;
	private List<ValorCampoTipoTarefaRelatorio> valoresCampoTipoTarefa;
	
	
	public String getIdenticiacoesProcessuais() {
		return identiciacoesProcessuais;
	}


	public void setIdenticiacoesProcessuais(String identiciacoesProcessuais) {
		this.identiciacoesProcessuais = identiciacoesProcessuais;
	}


	public String getUsuariosResponsaveis() {
		return usuariosResponsaveis;
	}


	public void setUsuariosResponsaveis(String usuariosResponsaveis) {
		this.usuariosResponsaveis = usuariosResponsaveis;
	}


	public List<ValorCampoTipoTarefaRelatorio> getValoresCampoTipoTarefa() {
		return valoresCampoTipoTarefa;
	}


	public void setValoresCampoTipoTarefa(List<ValorCampoTipoTarefaRelatorio> valoresCampoTipoTarefa) {
		this.valoresCampoTipoTarefa = valoresCampoTipoTarefa;
	}
	
	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


		
	public static class ValorCampoTipoTarefaRelatorio implements java.io.Serializable {
		private Long codigo;
		private String descricao;
		private String valor;
		private String classificacao;
		
		public String getClassificacao() {
			return classificacao;
		}
		public void setClassificacao(String classificacao) {
			this.classificacao = classificacao;
		}
		public Long getCodigo() {
			return codigo;
		}
		public void setCodigo(Long codigo) {
			this.codigo = codigo;
		}
		public String getDescricao() {
			return descricao;
		}
		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
		public String getValor() {
			return valor;
		}
		public void setValor(String valor) {
			this.valor = valor;
		}
	}



	public String getSituacao() {
		return situacao;
	}


	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}


	public String getDataInicio() {
		return dataInicio;
	}


	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}


	public String getDataFim() {
		return dataFim;
	}


	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}


	public String getDataPrevistaInicio() {
		return dataPrevistaInicio;
	}


	public void setDataPrevistaInicio(String dataPrevistaInicio) {
		this.dataPrevistaInicio = dataPrevistaInicio;
	}


	public String getDataPrevistaFim() {
		return dataPrevistaFim;
	}


	public void setDataPrevistaFim(String dataPrevistaFim) {
		this.dataPrevistaFim = dataPrevistaFim;
	}


	public String getPrioridade() {
		return prioridade;
	}


	public void setPrioridade(String prioridade) {
		this.prioridade = prioridade;
	}


	public String getSigiloso() {
		return sigiloso;
	}


	public void setSigiloso(String sigiloso) {
		this.sigiloso = sigiloso;
	}


	public String getUrgente() {
		return urgente;
	}


	public void setUrgente(String urgente) {
		this.urgente = urgente;
	}


	public String getLocalizacaoAtual() {
		return localizacaoAtual;
	}


	public void setLocalizacaoAtual(String localizacaoAtual) {
		this.localizacaoAtual = localizacaoAtual;
	}


	


	
}
