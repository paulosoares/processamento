/**
 * 
 */
package br.gov.stf.estf.entidade.julgamento;

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
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;

/**
 * @author Paulo.Estevao
 * @since 29.06.2011
 */
@Entity
@Table(name="PECA_INFORMACAO_PAUTA_PROC", schema="JULGAMENTO")
public class PecaInformacaoPautaProcesso extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1570983770174494645L;
	
	private Long id;
	private InformacaoPautaProcesso informacaoPautaProcesso;
	private PecaProcessoEletronico pecaProcessoEletronico;
	private String descricao;
	private byte[] arquivo;
	
	@Override
	@Id
	@Column( name="SEQ_PECA_INFORMACAO_PAUTA_PROC" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator(name = "sequence", sequenceName = "JULGAMENTO.SEQ_PECA_INFORMACAO_PAUTA_PROC", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_INFORMACAO_PAUTA_PROCESSO")
	public InformacaoPautaProcesso getInformacaoPautaProcesso() {
		return informacaoPautaProcesso;
	}
	
	public void setInformacaoPautaProcesso(InformacaoPautaProcesso informacaoPautaProcesso) {
		this.informacaoPautaProcesso = informacaoPautaProcesso;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_PECA_PROC_ELETRONICO")
	public PecaProcessoEletronico getPecaProcessoEletronico() {
		return pecaProcessoEletronico;
	}
	
	public void setPecaProcessoEletronico(PecaProcessoEletronico pecaProcessoEletronico) {
		this.pecaProcessoEletronico = pecaProcessoEletronico;
	}
	
	@Column(name = "DSC_PECA")
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "BIN_ARQUIVO")
	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}
	
}
