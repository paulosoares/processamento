/**
 * 
 */
package br.gov.stf.estf.julgamento.model.util;

import java.text.SimpleDateFormat;

import javax.persistence.Transient;

import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoSessaoConstante;

/**
 * @author Paulo.Estevao
 * @since 07.06.2011
 */
public class SessaoResult {

	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	private Sessao sessao;
	private Long quantidadeProcessos;
	private Long quantidadeListas;
	private String colegiado;	
	private Long quantidadeProcessosMaisProcessosDeLista;
	
	public SessaoResult(Sessao sessao, Long quantidadeProcessos, Long quantidadeListas, String colegiado, Long quantidadeProcessosDasListas) {
		this.sessao = sessao;
		this.quantidadeProcessos = quantidadeProcessos;
		this.colegiado = colegiado;
		this.quantidadeListas = quantidadeListas;
		this.quantidadeProcessosMaisProcessosDeLista = quantidadeProcessosDasListas;
	}

	public SessaoResult(Sessao sessao, Long quantidadeProcessos, Long quantidadeListas, String colegiado) {
		this.sessao = sessao;
		this.quantidadeProcessos = quantidadeProcessos;
		this.colegiado = colegiado;
		this.quantidadeListas = quantidadeListas;
	}
	
	public SessaoResult(Sessao sessao, Long quantidadeProcessos) {
		this.sessao = sessao;
		this.quantidadeProcessos = quantidadeProcessos;
	}
	
	public Sessao getSessao() {
		return sessao;
	}
	
	public void setSessao(Sessao sessao) {
		this.sessao = sessao;
	}
	
	public Long getQuantidadeProcessos() {
		return quantidadeProcessos;
	}
	
	public void setQuantidadeProcessos(Long quantidadeProcessos) {
		this.quantidadeProcessos = quantidadeProcessos;
	}

	public Long getQuantidadeListas() {
		return quantidadeListas;
	}
	
	public void setQuantidadeListas(Long quantidadeListas) {
		this.quantidadeListas = quantidadeListas;
	}
	
	public String getDataSessao() {
		if (sessao != null) {
			return dateFormatter.format(sessao.getDataInicio());
		} else {
			return "Não agendados";
		}
	}
	
	public TipoSessaoConstante getTipoSessao() {
		if (sessao != null) {
			if (TipoSessaoConstante.ORDINARIA.getSigla().equals(sessao.getTipoSessao())) {
				return TipoSessaoConstante.ORDINARIA;
			} else if (TipoSessaoConstante.EXTRAORDINARIA.getSigla().equals(sessao.getTipoSessao())) {
				return TipoSessaoConstante.EXTRAORDINARIA;
			} else if (TipoSessaoConstante.SOLENE.getSigla().equals(sessao.getTipoSessao())) {
				return TipoSessaoConstante.SOLENE;
			}
		}
		return null;
	}

	public Long getId() {
		if (sessao != null) {
			return sessao.getId();
		}
		return -1L;
	}
	
	public String getColegiado() {
		return colegiado;
	}
	
	public void setColegiado(String colegiado) {
		this.colegiado = colegiado;
	}

	public boolean isDisponibilizadoInternet() {
		return sessao.getDisponibilizadoInternet() != null && sessao.getDisponibilizadoInternet().booleanValue();
	}
	
	public Long getQuantidadeProcessosMaisProcessosDeLista() {
		return quantidadeProcessosMaisProcessosDeLista;
	}

	public void setQuantidadeProcessosMaisProcessosDeLista(
			Long quantidadeProcessosMaisProcessosDeLista) {
		this.quantidadeProcessosMaisProcessosDeLista = quantidadeProcessosMaisProcessosDeLista;
	}
	
	@Transient
	public Long getQuantidadeProcessosListas(){
		if (sessao == null || sessao.getListasJulgamento() == null || sessao.getListasJulgamento().isEmpty())
			return 0L;
		Long quantidade = 0L;
		for (ListaJulgamento lista : sessao.getListasJulgamento()){
			quantidade += lista.getQuantidadeProcessos();
		}
		return quantidade;
	}

}
