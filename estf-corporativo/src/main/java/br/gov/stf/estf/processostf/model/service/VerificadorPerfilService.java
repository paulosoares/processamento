package br.gov.stf.estf.processostf.model.service;

public interface VerificadorPerfilService {
	
	public boolean isUsuarioRegistrarAndamentoDistribuidoForaSetor();
	
	public boolean isUsuarioRegistrarAndamentoNaoDistribuido();
	
	public boolean isUsuarioRegistrarAndamentoIndevidoRG();
}