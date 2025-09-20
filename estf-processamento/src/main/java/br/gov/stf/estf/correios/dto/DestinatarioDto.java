package br.gov.stf.estf.correios.dto;

public class DestinatarioDto {
	private String nome;
	private String dddTelefone;
	private String telefone;
	private String email;
	private EnderecoDto endereco;
	
	public DestinatarioDto(String nome, String dddTelefone, String telefone, String email, String logradouro, String numero, String bairro, String complemento, String cep, String cidade, String uf) {
		this.nome = nome;
		this.dddTelefone = dddTelefone;
		this.telefone = telefone;
		this.email = email;
		this.endereco = new EnderecoDto(logradouro, numero, bairro, complemento, cep, cidade, uf);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public EnderecoDto getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoDto endereco) {
		this.endereco = endereco;
	}

	public String getDddTelefone() {
		return dddTelefone;
	}

	public void setDddTelefone(String dddTelefone) {
		this.dddTelefone = dddTelefone;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
