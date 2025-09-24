package br.jus.stf.estf.decisao.support.util;

import java.util.Date;

import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoMotivoAlteracao;

public class ObjetoDragAndDrop<T> implements Comparable<ObjetoDragAndDrop<T>>{
	private String nome;
	private ColunaDragAndDrop<T> coluna;
	private T instancia;
	private Boolean revisado;
	private Boolean temAdvertencias;
	private PreListaJulgamentoMotivoAlteracao motivo;
	private String revisor;
	private Date dataRevisao;
	private String observacao;

	public ObjetoDragAndDrop(String nome, ColunaDragAndDrop<T> coluna, T instancia, Boolean revisado, PreListaJulgamentoMotivoAlteracao motivo, String revisor, Date dataRevisao, String observacao) {
		this.nome = nome;
		this.coluna = coluna;
		this.instancia = instancia;
		this.revisado = revisado;
		this.motivo = motivo;
		this.observacao = observacao;
		setRevisor(revisor);
		setDataRevisao(dataRevisao);
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public ColunaDragAndDrop<T> getColuna() {
		return this.coluna;
	}
	
	public void setColuna(ColunaDragAndDrop<T> coluna) {
		this.coluna = coluna;
	}
	
	public T getInstancia() {
		return this.instancia;
	}
	
	public void setInstancia(T instancia) {
		this.instancia = instancia;
	}
	
	public Boolean getRevisado() {
		return revisado;
	}

	public void setRevisado(Boolean revisado) {
		this.revisado = revisado;
	}

	public Boolean getTemAdvertencias() {
		temAdvertencias = Boolean.FALSE;
		if(mostraAdvertencia()){
			temAdvertencias = Boolean.TRUE;
		}		
		return temAdvertencias;
	}

	protected boolean mostraAdvertencia() {
		boolean automatico = PreListaJulgamentoMotivoAlteracao.AUTOMATICA.equals(motivo);
		boolean manual = PreListaJulgamentoMotivoAlteracao.MANUAL.equals(motivo);
		return !(automatico || manual);
	}

	public void setTemAdvertencias(Boolean temAdvertencias) {
		this.temAdvertencias = temAdvertencias;
	}	

	public PreListaJulgamentoMotivoAlteracao getMotivo() {
		return motivo;
	}

	public void setMotivo(PreListaJulgamentoMotivoAlteracao motivo) {
		this.motivo = motivo;
	}

	@Override
	public String toString() {
		return nome;
	}

	@Override
	public int compareTo(ObjetoDragAndDrop<T> o) {
		if (o == null)
			return 0;
		
		return nome.compareTo(o.getNome());
	}

	public String getRevisor() {
		return revisor;
	}

	public void setRevisor(String revisor) {
		this.revisor = revisor;
	}

	public Date getDataRevisao() {
		return dataRevisao;
	}

	public void setDataRevisao(Date dataRevisao) {
		this.dataRevisao = dataRevisao;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	
}