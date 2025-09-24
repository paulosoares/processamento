package br.jus.stf.estf.decisao.pesquisa.domain;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronicoComunicacao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.TipoConfidencialidade;
import br.gov.stf.estf.entidade.processostf.TipoMeioProcesso;
import br.jus.stf.estf.decisao.documento.support.Documento;
import br.jus.stf.estf.decisao.support.query.Dto;

public class ComunicacaoDto implements Dto, Documento {	
	
	private static final long serialVersionUID = -3188252336226511543L;
	
	private Long id;
	private Long idDocumentoComunicacao;
	private String dscNomeDocumento;
	private String processo;
	private String faseComunicacaoAtual;
	private String nomeMinistroRelator;
	private Long idSetorMinistro;
	private String ministro;
	private String nomeMinistro;	
	private Date dataCriacao;
	private Date dataDisponibilizacaoGabinete;
	private boolean selected;
	private Long idArquivoEletronico;
	private Long idObjetoIncidente;
	private String usuarioCriacao;
	private TipoMeioProcesso tipoMeioProcesso;
	private TipoConfidencialidade tipoConfidencialidade;
	private List<PecaProcessoEletronicoComunicacao> pecasProcessoEletronico;
	private String descricaoStatusDocumento;
	private Long idModeloComunicacao;

	/**
	 * Monta um Objeto ComunicacaoDto, por meio de um
	 * ComunicacaoDocumentoResult
	 * @param comunicacaoDocumentoResult
	 * @return
	 */
	public static ComunicacaoDto valueOf(ComunicacaoDocumentoResult comunicacaoDocumentoResult, String nomeRelator) {
		ComunicacaoDto dto = new ComunicacaoDto();
		dto.setId(comunicacaoDocumentoResult.getComunicacao().getId());
		dto.setIdDocumentoComunicacao(comunicacaoDocumentoResult.getDocumentoComunicacao().getId());
		dto.setProcesso(ObjetoIncidenteDto.valueOf(comunicacaoDocumentoResult.getComunicacao().getObjetoIncidenteUnico()).getIdentificacao());
		dto.setNomeMinistroRelator(nomeRelator);
		dto.setUsuarioCriacao(comunicacaoDocumentoResult.getComunicacao().getUsuarioCriacao());
		dto.setDscNomeDocumento(comunicacaoDocumentoResult.getComunicacao().getDscNomeDocumento());
		dto.setFaseComunicacaoAtual(comunicacaoDocumentoResult.getComunicacao().getFaseAtual());
		dto.setDataDisponibilizacaoGabinete(comunicacaoDocumentoResult.getComunicacao().getDeslocamentoAtual().getDataEntrada());
		dto.setPecasProcessoEletronico(comunicacaoDocumentoResult.getComunicacao().getPecasProcessoEletronico());		
		dto.setDescricaoStatusDocumento(comunicacaoDocumentoResult.getDocumentoComunicacao().getDocumentoEletronico().getDescricaoStatusDocumento());
		dto.setIdModeloComunicacao(comunicacaoDocumentoResult.getComunicacao().getModeloComunicacao().getId());
		dto.setTipoConfidencialidade(comunicacaoDocumentoResult.getComunicacao().getObjetoIncidenteUnico().getTipoConfidencialidade());
		return dto;
	}
	
	public static ComunicacaoDto valueOfForMobile(ComunicacaoDocumentoResult comunicacaoDocumentoResult) {
		ComunicacaoDto dto = new ComunicacaoDto();
		dto.setId(comunicacaoDocumentoResult.getComunicacao().getId());		
		dto.setProcesso(ObjetoIncidenteDto.valueOfForMobile(comunicacaoDocumentoResult.getComunicacao().getObjetoIncidenteUnico()).getIdentificacao());				
		dto.setDscNomeDocumento(comunicacaoDocumentoResult.getComunicacao().getDscNomeDocumento());		
		dto.setDataDisponibilizacaoGabinete(comunicacaoDocumentoResult.getComunicacao().getDeslocamentoAtual().getDataEntrada());		
		return dto;
	}
		
	
	/**
	 * @see br.jus.stf.estf.decisao.support.query.Dto#getId()
	 **/
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getIdDocumentoComunicacao() {
		return idDocumentoComunicacao;
	}

	public void setIdDocumentoComunicacao(Long idDocumentoComunicacao) {
		this.idDocumentoComunicacao = idDocumentoComunicacao;
	}

	public String getDscNomeDocumento() {
		return dscNomeDocumento;
	}

	public void setDscNomeDocumento(String dscNomeDocumento) {
		this.dscNomeDocumento = dscNomeDocumento;
	}

	public String getNomeMinistroRelator() {
		return nomeMinistroRelator;
	}

	public void setNomeMinistroRelator(String nomeMinistroRelator) {
		this.nomeMinistroRelator = nomeMinistroRelator;
	}
	
	public Long getIdSetorMinistro() {
		return idSetorMinistro;
	}
	
	public void setIdSetorMinistro(Long idSetorMinistro) {
		this.idSetorMinistro = idSetorMinistro;
	}
	
	public Long getIdModeloComunicacao() {
		return idModeloComunicacao;
	}
	
	public void setIdModeloComunicacao(Long idModeloComunicacao) {
		this.idModeloComunicacao = idModeloComunicacao;
	}
	public String getNomeMinistro() {
		return nomeMinistro;
	}

	public void setNomeMinistro(String nomeMinistro) {
		this.nomeMinistro = nomeMinistro;
	}
	
	public String getMinistro() {
		return ministro;
	}

	public void setMinistro(String ministro) {
		this.ministro = ministro;
	}

	public String getFaseComunicacaoAtual() {
		return faseComunicacaoAtual;
	}

	public void setFaseComunicacaoAtual(String faseComunicacaoAtual) {
		this.faseComunicacaoAtual = faseComunicacaoAtual;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	
	
	public Date getDataDisponibilizacaoGabinete() {
		return dataDisponibilizacaoGabinete;
	}

	public void setDataDisponibilizacaoGabinete(Date dataDisponibilizacaoGabinete) {
		this.dataDisponibilizacaoGabinete = dataDisponibilizacaoGabinete;
	}


	public void setIdArquivoEletronico(Long idArquivoEletronico) {
		this.idArquivoEletronico = idArquivoEletronico;
	}

	public Long getIdArquivoEletronico() {
		return idArquivoEletronico;
	}

	/**
	 * @see br.jus.stf.estf.decisao.support.query.Selectable#isSelected()
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @see br.jus.stf.estf.decisao.support.query.Selectable#setSelected(boolean)
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getProcesso() {
		return processo;
	}

	public void setProcesso(String objetoIncidente) {
		this.processo = objetoIncidente;
	}
	
	public Long getIdObjetoIncidente() {
		return idObjetoIncidente;
	}

	public void setIdObjetoIncidente(Long idObjetoIncidente) {
		this.idObjetoIncidente = idObjetoIncidente;
	}
	

	private static void adicionaTipoMeioProcesso(ComunicacaoDto dto, Comunicacao comunicacao) {
		Processo processo = (Processo) comunicacao.getObjetoIncidenteUnico().getPrincipal();
		if (processo != null){
			dto.setTipoMeioProcesso(processo.getTipoMeioProcesso());
		}
	}

	private static void adicionaTipoConfidencialidade(ComunicacaoDto dto, Comunicacao comunicacao) {
		Processo processo = (Processo) comunicacao.getObjetoIncidenteUnico().getPrincipal();
		if (processo != null){
			dto.setTipoConfidencialidade(processo.getTipoConfidencialidade());
		}
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (!(other instanceof ComunicacaoDto)) {
			return false;
		}
		ComunicacaoDto castOther = (ComunicacaoDto) other;
		if (getId() != null && castOther.getId() != null) {
			return this.getId().equals(castOther.getId());
		}
		return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return (getId() != null) ? getId().hashCode() : super.hashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String descricao = processo;
		if (getDscNomeDocumento() != null && !getDscNomeDocumento().equals("")) {
			descricao += " - " + getDscNomeDocumento();
		}
		
		if (descricao != null) {
			return descricao;
		} else {
			return getId().toString();
		}
	}

	public String getUsuarioCriacao() {
		return usuarioCriacao;
	}

	public void setUsuarioCriacao(String usuarioCriacao) {
		this.usuarioCriacao = usuarioCriacao;
	}
	
	public TipoMeioProcesso getTipoMeioProcesso() {
		return tipoMeioProcesso;
	}
	
	public void setTipoMeioProcesso(TipoMeioProcesso tipoMeioProcesso) {
		this.tipoMeioProcesso = tipoMeioProcesso;
	}
	
	public TipoConfidencialidade getTipoConfidencialidade() {
		return tipoConfidencialidade;
	}
	
	public void setTipoConfidencialidade(TipoConfidencialidade tipoConfidencialidade) {
		this.tipoConfidencialidade = tipoConfidencialidade;
	}
	
	public Boolean getEletronico(){
		return TipoMeioProcesso.ELETRONICO.equals(getTipoMeioProcesso());
	}
	
	
	public List<PecaProcessoEletronicoComunicacao> getPecasProcessoEletronico() {
		return pecasProcessoEletronico;
	}

	public void setPecasProcessoEletronico(
			List<PecaProcessoEletronicoComunicacao> pecasProcessoEletronico) {
		this.pecasProcessoEletronico = pecasProcessoEletronico;
	}

	@Override
	public boolean isFake() {
		return false;
	}

	public String getDescricaoStatusDocumento() {
		return descricaoStatusDocumento;
	}

	public void setDescricaoStatusDocumento(String descricaoStatusDocumento) {
		this.descricaoStatusDocumento = descricaoStatusDocumento;
	}
	
	
}
