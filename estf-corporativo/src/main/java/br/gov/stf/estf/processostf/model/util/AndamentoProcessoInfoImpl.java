package br.gov.stf.estf.processostf.model.util;

import java.util.List;

import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.julgamento.ProcessoTema;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;

public class AndamentoProcessoInfoImpl implements AndamentoProcessoInfo {

	private Andamento andamento;

	private Setor setor;
	
	private List<Processo> processosPrincipais;

	private Peticao peticao;

	private Long idOrigemDecisao;

	private Long idTipoDevolucao;

	private String observacao;

	private String observacaoInterna;

	private String codigoUsuario;

	private Long idPresidenteInterino;

	private AndamentoProcesso ultimoAndamento;
	
	private Origem origem;
	
	private Comunicacao comunicacao;
	
	private List<ProcessoTema> processosTemas;
	
	private Processo processo;

	public Andamento getAndamento() {
		return andamento;
	}

	public void setAndamento(Andamento andamento) {
		this.andamento = andamento;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public Peticao getPeticao() {
		return peticao;
	}

	public void setPeticao(Peticao peticao) {
		this.peticao = peticao;
	}

	public Long getIdOrigemDecisao() {
		return idOrigemDecisao;
	}

	public void setIdOrigemDecisao(Long idOrigemDecisao) {
		this.idOrigemDecisao = idOrigemDecisao;
	}

	public Long getIdTipoDevolucao() {
		return idTipoDevolucao;
	}

	public void setIdTipoDevolucao(Long idTipoDevolucao) {
		this.idTipoDevolucao = idTipoDevolucao;
	}

	@Override
	public String getObservacaoInterna() {
		return observacaoInterna;
	}

	public void setObservacaoInterna(String observacaoInterna) {
		this.observacaoInterna = observacaoInterna;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public Long getIdPresidenteInterino() {
		return idPresidenteInterino;
	}

	public void setIdPresidenteInterino(Long idPersidenteInterino) {
		this.idPresidenteInterino = idPersidenteInterino;
	}

	public AndamentoProcesso getUltimoAndamento() {
		return ultimoAndamento;
	}

	public void setUltimoAndamento(AndamentoProcesso ultimoAndamento) {
		this.ultimoAndamento = ultimoAndamento;
	}

	@Override
	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	public void setProcessosPrincipais(List<Processo> processosPrincipais) {
		this.processosPrincipais = processosPrincipais;
	}

	@Override
	public List<Processo> getProcessosPrincipais() {
		return processosPrincipais;
	}
	
	public Comunicacao getComunicacao() {
		return comunicacao;
	}

	public void setComunicacao(Comunicacao comunicacao) {
		this.comunicacao = comunicacao;
	}	
	
		public List<ProcessoTema> getProcessosTemas() {
		return processosTemas;
	}

	public void setProcessosTemas(List<ProcessoTema> processosTemas) {
		this.processosTemas = processosTemas;
	}

	@Override
	public Processo getProcesso() {
		return processo;
	}	
	
	public void setProcesso(Processo processo){
		this.processo = processo;
	}

}
