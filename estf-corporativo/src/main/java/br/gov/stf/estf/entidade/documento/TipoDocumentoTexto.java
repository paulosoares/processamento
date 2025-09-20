package br.gov.stf.estf.entidade.documento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;

@Entity
@Table( schema="STF", name="TIPO_DOCUMENTO_TEXTO")
public class TipoDocumentoTexto extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 4585399080498630337L;
	
	public static final Long COD_TIPO_DOCUMENTO_TEXTO_EXTRADO_ATA = 50L;
	public static final Long COD_TIPO_DOCUMENTO_TEXTO_CERTIDAO_JULGAMENTO = 51L;
	public static final Long COD_TIPO_DOCUMENTO_TEXTO_TERMO_JUNTADA = 52L;
	public static final Long COD_TIPO_DOCUMENTO_TEXTO_CERTIDAO_PUBLICACAO = 53L;
	public static final Long COD_TIPO_DOCUMENTO_TEXTO_CERTIDAO_DATA = 54L;
	public static final Long COD_TIPO_DOCUMENTO_TEXTO_PADRAO = 21L;
	
	private String descricao;
	private Setor setor;
	private Long codigoEspacoClassificacaoDocumento;
	@Id
	@Column( name="COD_TIPO_DOCUMENTO_TEXTO" )
	public Long getId() {
		return id;
	}
	
	@Column(name="DSC_TIPO_DOCUMENTO_TEXTO", unique=false, nullable=true, insertable=true, updatable=true)
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricaoTipoDocumentoTexto) {
		this.descricao = descricaoTipoDocumentoTexto;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="COD_SETOR", unique=false, nullable=true, insertable=true, updatable=true)
	public Setor getSetor() {
		return setor;
	}
	public void setSetor(Setor setor) {
		this.setor = setor;
	}
	
	@Column(name="COD_ESPACO_CLASS_DOCUMENTO")
	public Long getCodigoEspacoClassificacaoDocumento() {
		return codigoEspacoClassificacaoDocumento;
	}

	public void setCodigoEspacoClassificacaoDocumento(Long codigoEspacoClassificacaoDocumento) {
		this.codigoEspacoClassificacaoDocumento = codigoEspacoClassificacaoDocumento;
	}

}
