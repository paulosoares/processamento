package br.gov.stf.estf.entidade.documento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.publicacao.Publicacao;

@Entity
@Table(schema="STF", name="HISTORICO_PUBLICACAO_DOCUMENTO")
public class HistoricoPublicacaoDocumento extends ESTFAuditavelBaseEntity<Long>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4870992663681195899L;
	
	private ArquivoEletronico arquivoEletronico;
	private Publicacao publicacao;

	

	@Id
	@Column( name="SEQ_HISTORICO_PUBLICACAO_DOC", nullable = false)	
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="STF.SEQ_HISTORICO_PUBLICACAO_DOC", allocationSize=1)
	public Long getId() {
		return this.id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY )
	@JoinColumn(name = "SEQ_ARQUIVO_ELETRONICO")	
	public ArquivoEletronico getArquivoEletronico() {
		return arquivoEletronico;
	}

	public void setArquivoEletronico(ArquivoEletronico arquivoEletronico) {
		this.arquivoEletronico = arquivoEletronico;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_DATA_PUBLICACOES")
	public Publicacao getPublicacao() {
		return publicacao;
	}

	public void setPublicacao(Publicacao publicacao) {
		this.publicacao = publicacao;
	}

}
