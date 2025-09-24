/**
 * 
 */
package br.jus.stf.estf.decisao.pesquisa.domain;

import java.text.SimpleDateFormat;

import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.publicacao.FaseTextoProcesso;
import br.jus.stf.estf.decisao.support.query.Dto;

/**
 * @author Paulo.Estevao
 * @since 16.12.2010
 */
public class FaseTextoProcessoDto implements Dto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1102617610524836828L;
	
	private boolean selected;
	private Long id;
	private String nomeUsuario;
	private String dataFase;
	private FaseTexto faseOrigem;
	private FaseTexto faseDestino;
	private String observacao;
	private String assinadorCertificado;
	
	/* (non-Javadoc)
	 * @see br.jus.stf.estf.decisao.support.query.Selectable#isSelected()
	 */
	@Override
	public boolean isSelected() {
		return selected;
	}

	/* (non-Javadoc)
	 * @see br.jus.stf.estf.decisao.support.query.Selectable#setSelected(boolean)
	 */
	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/* (non-Javadoc)
	 * @see br.jus.stf.estf.decisao.support.query.Dto#getId()
	 */
	@Override
	public Long getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see br.jus.stf.estf.decisao.support.query.Dto#isFake()
	 */
	@Override
	public boolean isFake() {
		return false;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getDataFase() {
		return dataFase;
	}

	public void setDataFase(String dataFase) {
		this.dataFase = dataFase;
	}

	public FaseTexto getFaseOrigem() {
		return faseOrigem;
	}

	public void setFaseOrigem(FaseTexto faseOrigem) {
		this.faseOrigem = faseOrigem;
	}

	public FaseTexto getFaseDestino() {
		return faseDestino;
	}

	public void setFaseDestino(FaseTexto faseDestino) {
		this.faseDestino = faseDestino;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	public String getAssinadorCertificado() {
		return assinadorCertificado;
	}

	public void setAssinadorCertificado(String assinadorCertificado) {
		this.assinadorCertificado = assinadorCertificado;
	}
	
	public static FaseTextoProcessoDto valueOf(FaseTextoProcesso faseTextoProcesso) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
		
		FaseTextoProcessoDto dto = new FaseTextoProcessoDto();
		dto.setId(faseTextoProcesso.getId());
		dto.setFaseOrigem(faseTextoProcesso.getTipoFaseTextoDocumento());
		dto.setFaseDestino(faseTextoProcesso.getTipoFaseTextoDocumentoDestino());
		dto.setDataFase(simpleDateFormat.format(faseTextoProcesso.getDataTransicao()));
		if (faseTextoProcesso.getUsuarioTransicao() != null) {
			dto.setNomeUsuario(faseTextoProcesso.getUsuarioTransicao().getNome());
		}
		dto.setObservacao(faseTextoProcesso.getObservacao());
		dto.setAssinadorCertificado(faseTextoProcesso.getAssinadorCertificado());
		return dto;
	}
}