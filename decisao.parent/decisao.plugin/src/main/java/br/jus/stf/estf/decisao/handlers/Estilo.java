package br.jus.stf.estf.decisao.handlers;

import org.apache.commons.lang.*;
import org.apache.commons.lang.builder.*;

public class Estilo {
	private String nome;
	private String nomeFonte;
	private String tamanhoFonte;
	private AlinhamentoParagrafo alinhamentoParagrafo;
	private boolean foraDoPadrao = false;
	private boolean negrito;
	private boolean italico;
	private boolean sublinhado;
	private String espacamentoParagrafo;
	private String versalete;
	private String nomeDoEstiloPai;
	private String quebraDePagina;
	private String familia;

	public String getFamilia() {
		return familia;
	}

	public void setFamilia(String familia) {
		this.familia = familia;
	}

	public String getQuebraDePagina() {
		return quebraDePagina;
	}

	public void setQuebraDePagina(String quebraDePagina) {
		this.quebraDePagina = quebraDePagina;
	}

	public String getNomeDoEstiloPai() {
		return nomeDoEstiloPai;
	}

	public void setNomeDoEstiloPai(String nomeDoEstiloPai) {

		this.nomeDoEstiloPai = nomeDoEstiloPai;
	}

	public String getVersalete() {
		return versalete;
	}

	public void setVersalete(String versalete) {
		this.versalete = versalete;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeFonte() {
		return nomeFonte;
	}

	public void setNomeFonte(String nomeFonte) {
		this.nomeFonte = nomeFonte;
	}

	public String getTamanhoFonte() {
		return tamanhoFonte;
	}

	public void setTamanhoFonte(String tamanhoFonte) {
		this.tamanhoFonte = tamanhoFonte;
	}

	public AlinhamentoParagrafo getAlinhamentoParagrafo() {
		return alinhamentoParagrafo;
	}

	public void setAlinhamentoParagrafo(AlinhamentoParagrafo alinhamentoParagrafo) {
		this.alinhamentoParagrafo = alinhamentoParagrafo;
	}

	public boolean isForaDoPadrao() {
		return foraDoPadrao;
	}

	public void setForaDoPadrao(boolean foraDoPadrao) {
		this.foraDoPadrao = foraDoPadrao;
	}

	public boolean isNegrito() {
		return negrito;
	}

	public void setNegrito(boolean negrito) {
		this.negrito = negrito;
	}

	public boolean isItalico() {
		return italico;
	}

	public void setItalico(boolean italico) {
		this.italico = italico;
	}

	public boolean isSublinhado() {
		return sublinhado;
	}

	public void setSublinhado(boolean sublinhado) {
		this.sublinhado = sublinhado;
	}

	public String getEspacamentoParagrafo() {
		return espacamentoParagrafo;
	}

	public void setEspacamentoParagrafo(String espacamentoParagrafo) {
		this.espacamentoParagrafo = espacamentoParagrafo;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(nomeFonte).append(tamanhoFonte).append(alinhamentoParagrafo).append(negrito)
				.append(italico).append(sublinhado).append(espacamentoParagrafo).append(versalete).toHashCode();

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Estilo other = (Estilo) obj;
		if (!ObjectUtils.equals(nomeFonte, other.nomeFonte)) {
			return false;
		}
		if (!ObjectUtils.equals(tamanhoFonte, other.tamanhoFonte)) {
			return false;
		}
		if (!ObjectUtils.equals(alinhamentoParagrafo, other.alinhamentoParagrafo)) {
			return false;
		}
		if (!ObjectUtils.equals(negrito, other.negrito)) {
			return false;
		}
		if (!ObjectUtils.equals(italico, other.italico)) {
			return false;
		}
		if (!ObjectUtils.equals(sublinhado, other.sublinhado)) {
			return false;
		}
		if (!ObjectUtils.equals(espacamentoParagrafo, other.espacamentoParagrafo)) {
			return false;
		}
		if (!ObjectUtils.equals(versalete, other.versalete)) {
			return false;
		}
		return true;
	}

}