package br.gov.stf.estf.documento.model.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente.FlagProcessoLote;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronicoComunicacao;
import br.gov.stf.estf.entidade.documento.TipoArquivo;

public class ComunicacaoDocumentoResult
    implements Serializable
{

    private static final long serialVersionUID = 0x3424cc290d03691aL;
    private Comunicacao comunicacao;
    private DocumentoComunicacao documentoComunicacao;
    private String nomeDocumento;
    private Boolean permissaoParaEditar;
    private String localizacaoAtualProcesso;
    private String acaoRealizadaUnidade;
    private List<PecaProcessoEletronicoComunicacao> listaPecasProcessoEletronicoComunicacao;
    private List<String> listaProcessoLoteVinculados;
    private Long numeroOrdemPeca;
    private Long descricaoSituaoPeca;
    private Boolean possuiArquivoMidia = false;

    public ComunicacaoDocumentoResult(Comunicacao comunicacao, DocumentoComunicacao documentoComunicacao, Boolean permissaoParaEditar,
    		List<PecaProcessoEletronicoComunicacao> listaPecasProcessoEletronicoComunicacao, Long numeroOrdemPeca , Long descricaoSituaoPeca)
    {
        this.comunicacao = comunicacao;
        this.documentoComunicacao = documentoComunicacao;
        this.permissaoParaEditar = permissaoParaEditar;
        this.listaPecasProcessoEletronicoComunicacao = listaPecasProcessoEletronicoComunicacao;
        this.numeroOrdemPeca = numeroOrdemPeca;
        this.descricaoSituaoPeca = descricaoSituaoPeca;
    }

	public Comunicacao getComunicacao() {
		return comunicacao;
	}

	public void setComunicacao(Comunicacao comunicacao) {
		this.comunicacao = comunicacao;
	}

	public DocumentoComunicacao getDocumentoComunicacao() {
		return documentoComunicacao;
	}

	public void setDocumentoComunicacao(DocumentoComunicacao documentoComunicacao) {
		this.documentoComunicacao = documentoComunicacao;
	}

	public String getNomeDocumento() {
		return nomeDocumento;
	}

	public void setNomeDocumento(String nomeDocumento) {
		this.nomeDocumento = nomeDocumento;
	}

	public Boolean getPermissaoParaEditar() {
		return permissaoParaEditar;
	}

	public void setPermissaoParaEditar(Boolean permissaoParaEditar) {
		this.permissaoParaEditar = permissaoParaEditar;
	}

	public String getLocalizacaoAtualProcesso() {
		return localizacaoAtualProcesso;
	}

	public void setLocalizacaoAtualProcesso(String localizacaoAtualProcesso) {
		this.localizacaoAtualProcesso = localizacaoAtualProcesso;
	}

	public List<PecaProcessoEletronicoComunicacao> getListaPecasProcessoEletronicoComunicacao() {
		return listaPecasProcessoEletronicoComunicacao;
	}

	public void setListaPecasProcessoEletronicoComunicacao(
			List<PecaProcessoEletronicoComunicacao> listaPecasProcessoEletronicoComunicacao) {
		this.listaPecasProcessoEletronicoComunicacao = listaPecasProcessoEletronicoComunicacao;
	}

	public String getAcaoRealizadaUnidade() {
		return acaoRealizadaUnidade;
	}

	public void setAcaoRealizadaUnidade(String acaoRealizadaUnidade) {
		this.acaoRealizadaUnidade = acaoRealizadaUnidade;
	}

	public List<String> getListaProcessoLoteVinculados() {
		List<String> identicacaoProcessoVinculados = new ArrayList<String>();
		for (ComunicacaoIncidente ci : getComunicacao().getComunicacaoIncidente()){
			if (ci.getTipoVinculo().equals(FlagProcessoLote.V)){
				identicacaoProcessoVinculados.add(ci.getObjetoIncidente().getIdentificacao());
			}
		}
		return identicacaoProcessoVinculados;
	}

	public void setListaProcessoLoteVinculados(
			List<String> listaProcessoLoteVinculados) {
		this.listaProcessoLoteVinculados = listaProcessoLoteVinculados;
	}

	public Long getNumeroOrdemPeca() {
		return numeroOrdemPeca;
	}

	public void setNumeroOrdemPeca(Long numeroOrdemPeca) {
		this.numeroOrdemPeca = numeroOrdemPeca;
	}

	public Long getDescricaoSituaoPeca() {
		return descricaoSituaoPeca;
	}

	public void setDescricaoSituaoPeca(Long descricaoSituaoPeca) {
		this.descricaoSituaoPeca = descricaoSituaoPeca;
	}

	public Boolean getPossuiArquivoMidia() {
		possuiArquivoMidia = false;
		if (listaPecasProcessoEletronicoComunicacao != null) {
			for (PecaProcessoEletronicoComunicacao peca: listaPecasProcessoEletronicoComunicacao) {
				if(!peca.getPecaProcessoEletronico().getDocumentos().get(0).getDocumentoEletronicoView().getTipoArquivo().equals(TipoArquivo.PDF)) {
					possuiArquivoMidia = true;
					break;
				}
			}
		}
		return possuiArquivoMidia;
	}

	public void setPossuiArquivoMidia(Boolean possuiArquivoMidia) {
		this.possuiArquivoMidia = possuiArquivoMidia;
	}

	    
    
}
