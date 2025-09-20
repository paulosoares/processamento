package br.gov.stf.estf.assinatura.visao.util;

import java.io.Serializable;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Ignore;

import br.gov.stf.estf.entidade.documento.Comunicacao;

@Ignore
public class ObjetoTeste implements Comparable<ObjetoTeste>, Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private Comunicacao comunicacao;

	public ObjetoTeste(String nome) {
		this.nome = nome;
		this.comunicacao = new Comunicacao();
	}

	public ObjetoTeste() {
		this(null);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Comunicacao getComunicacao() {
		return comunicacao;
	}

	public void setComunicacao(Comunicacao comunicacao) {
		this.comunicacao = comunicacao;
	}

	@Override
	public int compareTo(ObjetoTeste outroObjeto) {
		return new CompareToBuilder().append(getNome(), outroObjeto.getNome()).toComparison();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("nome", getNome()).toString();
	}
}