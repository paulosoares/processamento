package br.gov.stf.estf.correios.dto;

public class RemetenteDto {
	private String nome = "SUPREMO TRIBUNAL FEDERAL";
	private String cpfCnpj = "00531640000128";
	private EnderecoDto endereco = new EnderecoDto("PRAÇA DOS TRES PODERES EIXO MONUMENTAL", "0", "EIXO MONUMENTAL", "", "70175900", "BRASÍLIA", "DF");
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCpfCnpj() {
		return cpfCnpj;
	}
	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}
	public EnderecoDto getEndereco() {
		return endereco;
	}
	public void setEndereco(EnderecoDto endereco) {
		this.endereco = endereco;
	}
}
