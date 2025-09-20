package br.gov.stf.estf.entidade.jurisdicionado.util;

import br.gov.stf.estf.entidade.jurisdicionado.EmprestimoAutosProcesso;
import br.gov.stf.estf.entidade.jurisdicionado.enuns.EnumSituacaoEmprestimo;

public class EmprestimoAutosResult {
	
	private EmprestimoAutosProcesso emprestimoAutosProcesso;
	private EnumSituacaoEmprestimo situacaoEmprestimo;
	private String contatosJurisdicionado;
	
	
	public EmprestimoAutosProcesso getEmprestimoAutosProcesso() {
		return emprestimoAutosProcesso;
	}
	
	public void setEmprestimoAutosProcesso(
			EmprestimoAutosProcesso emprestimoAutosProcesso) {
		this.emprestimoAutosProcesso = emprestimoAutosProcesso;
	}

	public EnumSituacaoEmprestimo getSituacaoEmprestimo() {
		return situacaoEmprestimo;
	}

	public void setSituacaoEmprestimo(EnumSituacaoEmprestimo situacaoEmprestimo) {
		this.situacaoEmprestimo = situacaoEmprestimo;
	}

	public String getContatosJurisdicionado() {
		return contatosJurisdicionado;
	}

	public void setContatosJurisdicionado(String contatosJurisdicionado) {
		this.contatosJurisdicionado = contatosJurisdicionado;
	}
	

	
	

}
