package br.gov.stf.estf.entidade.usuario;

import java.util.Arrays;
import java.util.List;

/**
 * TODO Glehn mover para o pacote do dom�nio. <br>
 *  
 * Identifica o tipo de pessoa. Fornece servi�os que permitem identificar qual o tipo da pessoa
 * e os detalhes de sua identifica��o. <br>
 * Obs.: enum migrado do peticionamento v2.
 *  
 * @author Rodrigo Barreiros
 * @since 17.03.2011
 */
public enum TipoPessoa {
	
	PESSOA_PUBLICA("CNPJ", "Pessoa P�blica", "PP"), 
	PESSOA_JURIDICA("CNPJ", "Pessoa Jur�dica", "PJ"), 
	PESSOA_FISICA("CPF", "Pessoa F�sica", "PF"), 
	OUTRAS("", "", "");
	
	private String identificador;
	private String descricao;
	private String sigla;
	
	/**
	 * Constroi a inst�ncia com os detalhes da identifica��o
	 * 
	 * @param identificador o identificador
	 * @param descricao a descri��o do identificador
	 * @param sigla a sigla da pessoa
	 */
	private TipoPessoa(String identificador, String descricao, String sigla) {
		this.identificador = identificador;
		this.descricao = descricao;
		this.sigla = sigla;
	}
	
	/**
	 * Retorna a lista de pessoas que s�o "identificaveis", ou seja, que possuem
	 * identifica��es.
	 * 
	 * @return a lista de "identific�veis"
	 */
	public static List<TipoPessoa> getIdentificaveis() {
		return Arrays.asList(new TipoPessoa[] {TipoPessoa.PESSOA_PUBLICA, TipoPessoa.PESSOA_JURIDICA, TipoPessoa.PESSOA_FISICA});
	}
	
	/**
	 * Determina qual � o tipo de pessoa de um dado jurisdicionado.
	 * 
	 * @param jurisdicionado o jurisdicionado verificado
	 * @return o tipo do jurisdicionado
	 */
	public static TipoPessoa getTipoPessoaPorSigla(String sigla) {
		for (TipoPessoa tipoPessoa : TipoPessoa.values()) {
			if (tipoPessoa.getSigla().equals(sigla)) {
				return tipoPessoa;
			}
		}
		return OUTRAS;
	}
	
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	
	public String getIdentificador() {
		return identificador;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	public String getSigla() {
		return sigla;
	}

	public boolean isJuridica() {
		return PESSOA_JURIDICA.equals(this);
	}

	public boolean isFisica() {
		return PESSOA_FISICA.equals(this);
	}

	public boolean isPublica() {
		return PESSOA_PUBLICA.equals(this);
	}
	
	
}

