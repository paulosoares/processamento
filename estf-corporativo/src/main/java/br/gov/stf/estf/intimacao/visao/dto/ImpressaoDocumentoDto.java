package br.gov.stf.estf.intimacao.visao.dto;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.processostf.ModeloComunicacaoEnum;

public class ImpressaoDocumentoDto {

	private Long numeroDJ;
	private long seqPessoa;
	private long seqParteProcessual;
	private String dataDivulgacaoExtenso;
	private String dataAtualExtenso;
	private String nomeParte;
	private String descricaoSetor;
	private ModeloComunicacaoEnum modeloComunicacaoEnum;
	private List<PecaDTO> pecas;
	private Date dataDivulgacao;
	
			
	public Long getNumeroDJ() {
		return numeroDJ;
	}
	public void setNumeroDJ(Long numeroDJ) {
		this.numeroDJ = numeroDJ;
	}
	public long getSeqPessoa() {
		return seqPessoa;
	}
	public void setSeqPessoa(long seqPessoa) {
		this.seqPessoa = seqPessoa;
	}
	public long getSeqParteProcessual() {
		return seqParteProcessual;
	}
	public void setSeqParteProcessual(long seqParteProcessual) {
		this.seqParteProcessual = seqParteProcessual;
	}
	public String getDataDivulgacaoExtenso() {
		return dataDivulgacaoExtenso;
	}
	public void setDataDivulgacaoExtenso(String dataDivulgacaoExtenso) {
		this.dataDivulgacaoExtenso = dataDivulgacaoExtenso;
	}
	public String getDataAtualExtenso() {
		return dataAtualExtenso;
	}
	public void setDataAtualExtenso(String dataAtualExtenso) {
		this.dataAtualExtenso = dataAtualExtenso;
	}
	public String getNomeParte() {
		return nomeParte;
	}
	public void setNomeParte(String nomeParte) {
		this.nomeParte = nomeParte;
	}
	public List<PecaDTO> getPecas() {
		return pecas;
	}
	public void setPecas(List<PecaDTO> pecas) {
		this.pecas = pecas;
	}
	public String getDescricaoSetor() {
		return descricaoSetor;
	}
	public void setDescricaoSetor(String descricaoSetor) {
		this.descricaoSetor = descricaoSetor;
	}
	public ModeloComunicacaoEnum getModeloComunicacaoEnum() {
		return modeloComunicacaoEnum;
	}
	public void setModeloComunicacaoEnum(ModeloComunicacaoEnum modeloComunicacaoEnum) {
		this.modeloComunicacaoEnum = modeloComunicacaoEnum;
	}
	public Date getDataDivulgacao() {
		return dataDivulgacao;
	}
	public void setDataDivulgacao(Date dataDivulgacao) {
		this.dataDivulgacao = dataDivulgacao;
	}
}
