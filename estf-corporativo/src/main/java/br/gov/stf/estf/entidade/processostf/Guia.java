package br.gov.stf.estf.entidade.processostf;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.localizacao.EnderecoDestinatario;

@Entity
@Table(name = "GUIAS", schema = "STF")
public class Guia extends ESTFAuditavelBaseEntity<Guia.GuiaId> implements java.io.Serializable {

	private static final long serialVersionUID = -5190464077805418963L;
	public static final int CODIGO_TIPO_ORGAO_PARA_GUIA = 2;
	
	public static final int CODIGO_ORIGEM_ADVOGADO = 1;
	public static final int CODIGO_ORIGEM_INTERNO = 2;
	public static final int CODIGO_ORIGEM_EXTERNO = 3;
	
	public static final int CODIGO_DESTINO_ADVOGADO = 1;
	public static final int CODIGO_DESTINO_INTERNO = 2;
	public static final int CODIGO_DESTINO_EXTERNO = 3;

	private Date dataRemessa;
	private Integer tipoOrgaoDestino;
	private Long codigoOrgaoDestino;
	private Integer tipoOrgaoOrigem;
	private Integer quantidadeProcesso;
	private Integer quantidadePeticao;
	// recuperar o destino da guia que pode estar em três origens
	private String descricaoDestinoSetor;
	private String descricaoDestinoOrgao;
	private String descricaoDestinoAdvogado;
	private String descricaoDestino;
	private String descricaoOrigemSetor;
	private String descricaoOrigemOrgao;
	private String descricaoOrigemAdvogado;
	private String descricaoOrigem;
	private String tipoGuia;
	private String observacao;
	private Integer quantidadeInternaProcesso;
	private String tipoMeio;
	private int firstValue;
	private int maxValue;
	private boolean paginacao;
	private String dataRecebimento;
	private EnderecoDestinatario enderecoDestinatario;
	private String situacao;
	//

	@Column(name="QTD_PROCESSO")
	public Integer getQuantidadeInternaProcesso() {
		return quantidadeInternaProcesso;
	}

	public void setQuantidadeInternaProcesso(Integer quantidadeInternaProcesso) {
		this.quantidadeInternaProcesso = quantidadeInternaProcesso;
	}

	@Id
	public GuiaId getId() {
		return this.id;
	}

	public Guia() {
		super();
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_REMESSA")
	public Date getDataRemessa() {
		return this.dataRemessa;
	}

	public void setDataRemessa(Date dataRemessa) {
		this.dataRemessa = dataRemessa;
	}

	@Column(name = "TIP_ORGAO_DESTINO")
	public Integer getTipoOrgaoDestino() {
		return this.tipoOrgaoDestino;
	}

	public void setTipoOrgaoDestino(Integer tipoOrgaoDestino) {
		this.tipoOrgaoDestino = tipoOrgaoDestino;
	}

	@Column(name = "COD_ORGAO_DESTINO")
	public Long getCodigoOrgaoDestino() {
		return this.codigoOrgaoDestino;
	}

	public void setCodigoOrgaoDestino(Long codigoOrgaoDestino) {
		this.codigoOrgaoDestino = codigoOrgaoDestino;
	}

	@Column(name = "TIP_ORGAO_ORIGEM")
	public Integer getTipoOrgaoOrigem() {
		return this.tipoOrgaoOrigem;
	}

	public void setTipoOrgaoOrigem(Integer tipoOrgaoOrigem) {
		this.tipoOrgaoOrigem = tipoOrgaoOrigem;
	}

	@Column(name = "DSC_OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	
//	@Column(name = "QTD_PROCESSO", unique = false, nullable = false, insertable = true, updatable = true, precision = 6, scale = 0)
//	public Integer getQuantidadeProcesso() {
//		return this.quantidadeProcesso;
//	}

//	public void setQuantidadeProcesso(Integer quantidadeProcesso) {
//		this.quantidadeProcesso = quantidadeProcesso;
//	}

	// Destino ----------------------------
	
	@Formula("(SELECT s.dsc_setor from stf.setores s where s.cod_setor = COD_ORGAO_DESTINO)")
	public String getDescricaoDestinoSetor() {
		return descricaoDestinoSetor;
	}
	
	public void setDescricaoDestinoSetor(String descricaoDestinoSetor) {
		this.descricaoDestinoSetor = descricaoDestinoSetor;
	}

	@Formula("(SELECT o.dsc_origem from judiciario.origem o where o.cod_origem = COD_ORGAO_DESTINO)")
	public String getDescricaoDestinoOrgao() {
		return descricaoDestinoOrgao;
	}
	
	public void setDescricaoDestinoOrgao(String descricaoDestinoOrgao) {
		this.descricaoDestinoOrgao = descricaoDestinoOrgao;
	}
	
	@Formula("(SELECT j.NOM_JURISDICIONADO from judiciario.jurisdicionado j where j.seq_jurisdicionado = COD_ORGAO_DESTINO)")
	public String getDescricaoDestinoAdvogado() {
		return descricaoDestinoAdvogado;
	}
	
	public void setDescricaoDestinoAdvogado(String descricaoDestinoAdvogado) {
		this.descricaoDestinoAdvogado = descricaoDestinoAdvogado;
	}
	
	@Transient
	public String getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(String dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}
	
	@Transient
	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	@Transient
	public String getDescricaoDestino() {
		if (tipoOrgaoDestino.equals(new Integer(1))) {
			setDescricaoDestino(descricaoDestinoAdvogado);
		} else if (tipoOrgaoDestino.equals(new Integer(3))) {
			if (enderecoDestinatario != null && enderecoDestinatario.getDestinatario() != null) {
				setDescricaoDestino( descricaoDestinoOrgao + " / " + enderecoDestinatario.getDestinatario().getId() + "-" +
						enderecoDestinatario.getDestinatario().getNomDestinatario() );
			} else {
				setDescricaoDestino(descricaoDestinoOrgao);
			}
		} else if (tipoOrgaoDestino.equals(new Integer(2))) {
			setDescricaoDestino(descricaoDestinoSetor);
		}
		return descricaoDestino;
	}
	
	public void setDescricaoDestino(String descricaoDestino) {
		this.descricaoDestino = descricaoDestino;
	}

	// origem ----------------------------------
	
	@Formula("(SELECT s.dsc_setor from stf.setores s where s.cod_setor = COD_ORGAO_ORIGEM)")
	public String getDescricaoOrigemSetor() {
		return descricaoOrigemSetor;
	}
	
	public void setDescricaoOrigemSetor(String descricaoOrigemSetor) {
		this.descricaoOrigemSetor = descricaoOrigemSetor;
	}

	@Formula("(SELECT o.dsc_origem from judiciario.origem o where o.cod_origem = COD_ORGAO_ORIGEM)")
	public String getDescricaoOrigemOrgao() {
		return descricaoOrigemOrgao;
	}
	
	public void setDescricaoOrigemOrgao(String descricaoOrigemOrgao) {
		this.descricaoOrigemOrgao = descricaoOrigemOrgao;
	}
	
	@Formula("(SELECT j.NOM_JURISDICIONADO from judiciario.jurisdicionado j where j.seq_jurisdicionado = COD_ORGAO_ORIGEM)")
	public String getDescricaoOrigemAdvogado() {
		return descricaoOrigemAdvogado;
	}
	
	public void setDescricaoOrigemAdvogado(String descricaoOrigemAdvogado) {
		this.descricaoOrigemAdvogado = descricaoOrigemAdvogado;
	}
	
	@Transient
	public String getDescricaoOrigem() {
		if (tipoOrgaoOrigem.equals(new Integer(1))) {
			setDescricaoOrigem(descricaoOrigemAdvogado);
		} else if (tipoOrgaoOrigem.equals(new Integer(3))) {
			setDescricaoOrigem(descricaoOrigemOrgao);
		} else if (tipoOrgaoOrigem.equals(new Integer(2))) {
			setDescricaoOrigem(descricaoOrigemSetor);
		}
		return descricaoOrigem;
	}
	
	public void setDescricaoOrigem(String descricaoOrigem) {
		this.descricaoOrigem = descricaoOrigem;
	}

	/**
	 * O método que deve ser utilizado é o GuiaService.recuperarTotalProcesso()
	 * 
	 */
	@Deprecated
	// Retorno !=0 => Guia de Processo; Retorno = 0 => Guia de Petição.
	@Formula("(SELECT count(dp.num_guia) FROM stf.desloca_processos dp WHERE dp.num_guia = num_guia AND dp.ano_guia = ano_guia " +
             "AND dp.cod_orgao_origem = cod_orgao_origem)")
	public Integer getQuantidadeProcesso() {
		return quantidadeProcesso;
	}
	
	public void setQuantidadeProcesso(Integer quantidadeProcesso) {
		this.quantidadeProcesso = quantidadeProcesso;
	}

	/**
	 * O método que deve ser utilizado é o GuiaService.recuperarTotalPeticao()
	 * 
	 */
	@Deprecated
	// Retorno !=0 => Guia de Petição; Retorno = 0 => Guia de Processo.
	@Formula("(SELECT count(dp.num_guia) FROM stf.desloca_peticaos dp WHERE dp.num_guia = num_guia AND dp.ano_guia = ano_guia " +
            "AND dp.cod_orgao_origem = cod_orgao_origem)")
	public Integer getQuantidadePeticao() {
		return quantidadePeticao;
	}
	
	public void setQuantidadePeticao(Integer quantidadePeticao) {
		this.quantidadePeticao = quantidadePeticao;
	}
	
	/**
	 * O método que deverá ser utilizado é o GuiaServide.isEletronico(guia)
	 * 
	 */
	// retorna se uma guia (de processo) é de processos eletrônicos (E) ou físicos (F)
	@Deprecated
	@Formula("(select distinct P.TIP_MEIO_PROCESSO from stf.desloca_processos dp, judiciario.processo p " + 
             " where dp.ANO_GUIA = ANO_GUIA and dp.NUM_GUIA = NUM_GUIA and dp.COD_ORGAO_ORIGEM = COD_ORGAO_ORIGEM and " +
             " DP.NUM_PROCESSO = P.NUM_PROCESSO AND DP.SIG_CLASSE_PROCES = P.SIG_CLASSE_PROCES and ROWNUM = 1)")
	public String getTipoMeio(){
		return this.tipoMeio;
	}
	
	public void setTipoMeio(String tipoMeio) {
		this.tipoMeio = tipoMeio;
	}
	
	@Transient
	public String getTipoGuia(){
		return this.tipoGuia;
	}
	public void setTipoGuia(String tipoGuia){
		this.tipoGuia = tipoGuia;
	}

	// TODO verify numbers
	@Transient
	public Long getNumeroGuia() {
		return getId() != null ? getId().getNumeroGuia() : null;
	}

	@Transient
	public Short getAnoGuia() {
		return getId() != null ? getId().getAnoGuia() : null;
	}
	
	@Transient
	public Long getCodigoOrgaoOrigem() {
		return getId() != null ? getId().getCodigoOrgaoOrigem() : null;
	}
	
	public void setAnoGuia(Short anoGuia) {
		if (getId() == null) {
			setId(new GuiaId());
		}
		getId().setAnoGuia(anoGuia);
	}

	public void setNumeroGuia(Long numeroGuia) {
		if (getId() == null) {
			setId(new GuiaId());
		}
		getId().setNumeroGuia(numeroGuia);
	}
	
	public void setCodigoOrgaoOrigem(Long codigoOrgaoOrigem) {
		if (getId() == null) {
			setId(new GuiaId());
		}
		getId().setCodigoOrgaoOrigem(codigoOrgaoOrigem);
	}
	
	/**
	 * O método que deverá ser utilizado é GuiaService.isPeticao()
	 * 
	 */
	@Deprecated
	@Transient
	public boolean getIsPeticao(){
		return (quantidadePeticao!=null && quantidadePeticao > 0);
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_ENDERECO_DESTINATARIO")
	public EnderecoDestinatario getEnderecoDestinatario() {
		return enderecoDestinatario;
	}

	public void setEnderecoDestinatario(EnderecoDestinatario enderecoDestinatario) {
		this.enderecoDestinatario = enderecoDestinatario;
	}
	
	
	
	@Embeddable
	public static class GuiaId implements Serializable {

		private static final long serialVersionUID = -3571792909028296027L;
		private Long codigoOrgaoOrigem;
		private Long numeroGuia;
		private Short anoGuia;

		@Column(name = "COD_ORGAO_ORIGEM")
		public Long getCodigoOrgaoOrigem() {
			return this.codigoOrgaoOrigem;
		}

		public void setCodigoOrgaoOrigem(Long codigoOrgaoOrigem) {
			this.codigoOrgaoOrigem = codigoOrgaoOrigem;
		}

		@Column(name = "NUM_GUIA")
		public Long getNumeroGuia() {
			return this.numeroGuia;
		}

		public void setNumeroGuia(Long numeroGuia) {
			this.numeroGuia = numeroGuia;
		}

		@Column(name = "ANO_GUIA")
		public Short getAnoGuia() {
			return this.anoGuia;
		}

		public void setAnoGuia(Short anoGuia) {
			this.anoGuia = anoGuia;
		}
		
		public String toString() {
			return getClass().getName();
		}

		public boolean equals(Object other) {
			if ((this == other))
				return true;
			if ((other == null))
				return false;
			if (!(other instanceof GuiaId))
				return false;
			GuiaId castOther = (GuiaId) other;

			return ((this.getCodigoOrgaoOrigem() == castOther.getCodigoOrgaoOrigem()) || (this.getCodigoOrgaoOrigem() != null
					&& castOther.getCodigoOrgaoOrigem() != null && this.getCodigoOrgaoOrigem().equals(
					castOther.getCodigoOrgaoOrigem())))
					&& ((this.getNumeroGuia() == castOther.getNumeroGuia()) || (this.getNumeroGuia() != null
							&& castOther.getNumeroGuia() != null && this.getNumeroGuia().equals(castOther.getNumeroGuia())))
					&& ((this.getAnoGuia() == castOther.getAnoGuia()) || (this.getAnoGuia() != null
							&& castOther.getAnoGuia() != null && this.getAnoGuia().equals(castOther.getAnoGuia())));
		}

		public int hashCode() {
			int result = 17;

			result = 37 * result + (getCodigoOrgaoOrigem() == null ? 0 : this.getCodigoOrgaoOrigem().hashCode());
			result = 37 * result + (getNumeroGuia() == null ? 0 : this.getNumeroGuia().hashCode());
			result = 37 * result + (getAnoGuia() == null ? 0 : this.getAnoGuia().hashCode());
			return result;
		}

	}


	/**
	 * O método que deverá ser utilizado é o GuiaService.recuperarTotalItem()
	 * 
	 */
	@Deprecated
	// quantidade de itens da guia (processos ou petições)
	@Transient
	public Integer getQuantidade() {
		return getIsPeticao() ? getQuantidadePeticao() : getQuantidadeProcesso(); 
	}

	public void setFirstValue(int firstValue) {
		this.firstValue = firstValue;
	}

	@Transient
	public int getFirstValue() {
		return firstValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	@Transient
	public int getMaxValue() {
		return maxValue;
	}

	public void setPaginacao(boolean paginacao) {
		this.paginacao = paginacao;
	}

	@Transient
	public boolean getPaginacao() {
		return paginacao;
	}

}
