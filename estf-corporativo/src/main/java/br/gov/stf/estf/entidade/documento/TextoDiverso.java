package br.gov.stf.estf.entidade.documento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

@Entity
@Table(name = "TEXTO_DIVERSO", schema = "STF")
public class TextoDiverso extends ESTFAuditavelBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 175149635308411119L;
	public static final String SESSAO = "Sessão";
	
	private TipoTexto tipoTexto;
	private String descricao;
	private ArquivoEletronico arquivoEletronico;
	
	@Id
	@Column(name = "SEQ_TEXTO_DIVERSO", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	@Column(name = "COD_TIPO_TEXTO", insertable = true, updatable = true)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.documento.TipoTexto"),
			@Parameter(name = "identifierMethod", value = "getCodigo")})
	public TipoTexto getTipoTexto() {
		return this.tipoTexto;
	}

	public void setTipoTexto(TipoTexto tipoTexto) {
		this.tipoTexto = tipoTexto;
	}

	@Column(name = "DSC_TEXTO_DIVERSO", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_ARQUIVO_ELETRONICO", unique = false, nullable = true, insertable = true, updatable = true)
	public ArquivoEletronico getArquivoEletronico() {
		return this.arquivoEletronico;
	}

	public void setArquivoEletronico(ArquivoEletronico arquivoEletronico) {
		this.arquivoEletronico = arquivoEletronico;
	}

	public enum MotivoRepublicacao {

		REPUBLICADO_POR_DETERMINACAO_DO_MINISTRO("Republicado por determinação do Ministro", "RDMIN"),
		REPUBLICADO_POR_INCORRECAO("Republicado por incorreção", "RINCO");
		
		private String motivo;
		private String siglaMotivo;

		private MotivoRepublicacao(String motivo, String siglaMotivo){
			this.motivo = motivo; 
			this.siglaMotivo = siglaMotivo;
		}
		public String getMotivo() {
			return motivo;
		}
		public void setMotivo(String motivo) {
			this.motivo = motivo;
		}
		public String getSiglaMotivo() {
			return siglaMotivo;
		}
		public void setSiglaMotivo(String siglaMotivo) {
			this.siglaMotivo = siglaMotivo;
		}
	}



}
