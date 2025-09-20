package br.gov.stf.estf.assinatura.stfoffice.editor;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.cabecalho.model.CabecalhosObjetoIncidente.CabecalhoObjetoIncidente;
import br.gov.stf.estf.documento.model.util.TipoSessaoControleVoto;
import br.gov.stf.estf.entidade.documento.ControleVoto;
import br.gov.stf.estf.entidade.processostf.HistoricoProcessoOrigem;
import br.jus.stf.estf.montadortexto.SpecCabecalho;

public class DadosComunsDoCabecalhoHandler {
	
	private List<ControleVoto> controleVoto;
	private CabecalhoObjetoIncidente cabecalho;	
	private Date dataSessao;
	private String colegiado;
	private SpecCabecalho<Long> specCabecalho;
	private HistoricoProcessoOrigem historicoOrigem;
	private String numeracaoUnica;
	
    
    public String selecionaTipoSessaoControleVoto(){
    	
    	if (controleVoto != null && controleVoto.size() > 0){
    		if (controleVoto.get(0).getDataSessao() != null){
    			if (controleVoto.get(0).getSessao().getCodigo().equals(TipoSessaoControleVoto.PLENARIO.getCodigo())){ 
    				return TipoSessaoControleVoto.PLENARIO.getDescricao();
    			}else if (controleVoto.get(0).getSessao().getCodigo().equals(TipoSessaoControleVoto.PRIMEIRA_TURMA.getCodigo())){
    				return "1ª Turma";
    			}else if (controleVoto.get(0).getSessao().getCodigo().equals(TipoSessaoControleVoto.SEGUNDA_TURMA.getCodigo())){
    				return "2ª Turma";
    			}
    		}
    	}
		return null;
    }

	public List<ControleVoto> getControleVoto() {
		return controleVoto;
	}

	public void setControleVoto(List<ControleVoto> controleVoto) {
		this.controleVoto = controleVoto;
	}

	public CabecalhoObjetoIncidente getCabecalho() {
		return cabecalho;
	}

	public void setCabecalho(CabecalhoObjetoIncidente cabecalho) {
		this.cabecalho = cabecalho;
	}

	public Date getDataSessao() {
		return dataSessao;
	}

	public void setDataSessao(Date dataSessao) {
		this.dataSessao = dataSessao;
	}

	public String getColegiado() {
		return colegiado;
	}

	public void setColegiado(String colegiado) {
		this.colegiado = colegiado;
	}

	public SpecCabecalho<Long> getSpecCabecalho() {
		return specCabecalho;
	}

	public void setSpecCabecalho(SpecCabecalho<Long> specCabecalho) {
		this.specCabecalho = specCabecalho;
	}

	public HistoricoProcessoOrigem getHistoricoOrigem() {
		return historicoOrigem;
	}

	public void setHistoricoOrigem(HistoricoProcessoOrigem historicoOrigem) {
		this.historicoOrigem = historicoOrigem;
	}

	public String getNumeracaoUnica() {
		return numeracaoUnica;
	}

	public void setNumeracaoUnica(String numeracaoUnica) {
		this.numeracaoUnica = numeracaoUnica;
	}
}
