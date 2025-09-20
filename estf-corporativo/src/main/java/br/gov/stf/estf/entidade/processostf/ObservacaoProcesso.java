package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.documento.Adendo;
import br.gov.stf.estf.entidade.localizacao.Setor;

@Entity
@Table(name = "OBSERVACAO_PROCESSO", schema = "JUDICIARIO")
@PrimaryKeyJoinColumn(name = "SEQ_ADENDO_TEXTUAL")
public class ObservacaoProcesso extends Adendo {

	private static final long serialVersionUID = 1L;
	
	private ObjetoIncidente<?> objetoIncidente;
	private Setor setor;
	private String observacao;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", referencedColumnName = "SEQ_OBJETO_INCIDENTE")
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}
	
	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
	
	@ManyToOne
	@JoinColumn(name = "COD_SETOR", referencedColumnName = "COD_SETOR")
	public Setor getSetor() {
		return setor;
	}
	public void setSetor(Setor setor) {
		this.setor = setor;
	}
	
	@Column(name = "TXT_OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}
	
	public void setObservacao(String observacao) {
		this.observacao = observacao.replaceAll("\\p{Cntrl}", "");
	}
	
}
