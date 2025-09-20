package br.gov.stf.estf.entidade.documento;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;

@Entity
@Table(name="CONFIGURA_TEXTO_SETOR", schema="DOC")
public class ConfiguracaoTextoSetor extends ESTFBaseEntity<Long> {
	
	private static final long serialVersionUID = 6776046178801324986L;
	
	
	private Setor setor;
	private byte[] estilo;
	private byte[] macro;
	private byte[] atalho;
	
	@Id
	@Column(name="SEQ_CONFIGURA_TEXTO_SETOR")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "DOC.SEQ_CONFIGURA_TEXTO_SETOR", allocationSize = 1)
	public Long getId() {
		return this.id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn( name="COD_SETOR")
	public Setor getSetor() {
		return setor;
	}
	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "BIN_ESTILO")
	public byte[] getEstilo() {
		return estilo;
	}
	public void setEstilo(byte[] estilo) {
		this.estilo = estilo;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "BIN_MACRO")
	public byte[] getMacro() {
		return macro;
	}
	public void setMacro(byte[] macro) {
		this.macro = macro;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "BIN_ATALHO")
	public byte[] getAtalho() {
		return atalho;
	}
	public void setAtalho(byte[] atalho) {
		this.atalho = atalho;
	}

	

}
