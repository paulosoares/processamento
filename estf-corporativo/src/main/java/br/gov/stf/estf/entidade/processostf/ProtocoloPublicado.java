package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.TextoPeticao;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;

@Entity
@Table(name = "PROTOCOLO_PUBLICADO", schema = "STF")
public class ProtocoloPublicado extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 2155675414197693229L;
	private TextoPeticao textoPeticao;
	private Protocolo protocolo;
	private ConteudoPublicacao conteudoPublicacao;

	@Id
	@Column(name = "SEQ_PROTOCOLO_PUBLICADO", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TEXTO_PETICAOS", unique = false, nullable = false, insertable = true, updatable = true)
	public TextoPeticao getTextoPeticao() {
		return this.textoPeticao;
	}

	public void setTextoPeticao(TextoPeticao textoPeticao) {
		this.textoPeticao = textoPeticao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	public Protocolo getProtocolo() {
		return this.protocolo;
	}

	public void setProtocolo(Protocolo protocolo) {
		this.protocolo = protocolo;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_MATERIAS", unique = false, nullable = false, insertable = true, updatable = true)
	public ConteudoPublicacao getConteudoPublicacao() {
		return this.conteudoPublicacao;
	}

	public void setConteudoPublicacao(ConteudoPublicacao conteudoPublicacao) {
		this.conteudoPublicacao = conteudoPublicacao;
	}

}
