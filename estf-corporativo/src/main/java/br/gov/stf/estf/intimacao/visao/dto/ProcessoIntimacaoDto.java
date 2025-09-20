package br.gov.stf.estf.intimacao.visao.dto;

import java.util.ArrayList;
import java.util.List;

public class ProcessoIntimacaoDto {

	private long seqObjetoIncidente;
	private String siglaClasseProcesso;
	private long numeroProcesso;
	private String tipoMeioProcesso;
	private String descricaoClasseProcessual;
	private Long codigoSetor;
	private String descricaoSetor;
	private List<PecaIntimacaoDto> pecas;

	private List<PecaIntimacaoDto> pecasIntimadas;
	private List<PecaIntimacaoDto> pecasNaoIntimadas;
	private int qtdPecasIntimadas;
	private int qtdPecasNaoIntimadas;
	private Boolean selected = true;

	private String nomeProcesso;

	public ProcessoIntimacaoDto() {
	}

	public long getSeqObjetoIncidente() {
		return seqObjetoIncidente;
	}

	public void setSeqObjetoIncidente(long seqObjetoIncidente) {
		this.seqObjetoIncidente = seqObjetoIncidente;
	}

	public String getSiglaClasseProcesso() {
		return siglaClasseProcesso;
	}

	public void setSiglaClasseProcesso(String siglaClasseProcesso) {
		this.siglaClasseProcesso = siglaClasseProcesso;
	}

	public long getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(long numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public String getTipoMeioProcesso() {
		return tipoMeioProcesso;
	}

	public void setTipoMeioProcesso(String tipoMeioProcesso) {
		this.tipoMeioProcesso = tipoMeioProcesso;
	}

	public String getDescricaoClasseProcessual() {
		return descricaoClasseProcessual;
	}

	public void setDescricaoClasseProcessual(String descricaoClasseProcessual) {
		this.descricaoClasseProcessual = descricaoClasseProcessual;
	}

	public Long getCodigoSetor() {
		return codigoSetor;
	}

	public void setCodigoSetor(Long codigoSetor) {
		this.codigoSetor = codigoSetor;
	}

	public String getDescricaoSetor() {
		return descricaoSetor;
	}

	public void setDescricaoSetor(String descricaoSetor) {
		this.descricaoSetor = descricaoSetor;
	}

	public List<PecaIntimacaoDto> getPecas() {
		return pecas;
	}

	public void setPecas(List<PecaIntimacaoDto> pecas) {
		this.pecas = pecas;
	}

	public String getNomeProcesso() {
		return nomeProcesso;
	}

	public void setNomeProcesso(String nomeProcesso) {
		this.nomeProcesso = nomeProcesso;
	}

	public List<PecaIntimacaoDto> getPecasIntimadas() {
		return pecasIntimadas;
	}

	public void setPecasIntimadas(List<PecaIntimacaoDto> pecasIntimadas) {
		this.pecasIntimadas = pecasIntimadas;
	}

	public List<PecaIntimacaoDto> getPecasNaoIntimadas() {
		return pecasNaoIntimadas;
	}

	public void setPecasNaoIntimadas(List<PecaIntimacaoDto> pecasNaoIntimadas) {
		this.pecasNaoIntimadas = pecasNaoIntimadas;
	}

	public void agrupar() {
		pecasIntimadas = new ArrayList<PecaIntimacaoDto>();
		pecasNaoIntimadas = new ArrayList<PecaIntimacaoDto>();
		for (PecaIntimacaoDto peca : pecas) {
			if (peca.isIntimado()) {
				if (!pecasIntimadas.contains(peca)) {
					pecasIntimadas.add(peca);
					qtdPecasIntimadas++;
				}
			} else {
				if (!pecasNaoIntimadas.contains(peca)) {
					pecasNaoIntimadas.add(peca);
					qtdPecasNaoIntimadas++;
				}

			}
		}
	}

	public int getQtdPecasIntimadas() {
		return qtdPecasIntimadas;
	}

	public void setQtdPecasIntimadas(int qtdPecasIntimadas) {
		this.qtdPecasIntimadas = qtdPecasIntimadas;
	}

	public int getQtdPecasNaoIntimadas() {
		return qtdPecasNaoIntimadas;
	}

	public void setQtdPecasNaoIntimadas(int qtdPecasNaoIntimadas) {
		this.qtdPecasNaoIntimadas = qtdPecasNaoIntimadas;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

}