package br.gov.stf.estf.entidade.processosetor;

import java.io.Serializable;
import java.util.List;

import br.gov.stf.estf.entidade.usuario.UsuarioEGab;

@SuppressWarnings("serial")
public class UsuarioDistribuicaoClasseTipoJulgamento implements Serializable{

	private Boolean participante;
	private Boolean compensar;
	private Boolean naoParticipaNaoCompensa;
	private UsuarioEGab usuarioEGab;
	private List<ControleDistribuicao> controles;
	
	
	public Boolean getParticipante() {
		return participante;
	}
	public void setParticipante(Boolean participante) {
		this.participante = participante;
	}
	public Boolean getCompensar() {
		return compensar;
	}
	public void setCompensar(Boolean compensar) {
		this.compensar = compensar;
	}
	public UsuarioEGab getUsuarioEGab() {
		return usuarioEGab;
	}
	public void setUsuarioEGab(UsuarioEGab usuarioEGab) {
		this.usuarioEGab = usuarioEGab;
	}
	public List<ControleDistribuicao> getControles() {
		return controles;
	}
	public void setControles(List<ControleDistribuicao> controles) {
		this.controles = controles;
	}
	public Boolean getNaoParticipaNaoCompensa() {
		return naoParticipaNaoCompensa;
	}
	public void setNaoParticipaNaoCompensa(Boolean naoParticipaNaoCompensa) {
		this.naoParticipaNaoCompensa = naoParticipaNaoCompensa;
	}
	public int getQtdTotalProcessosDistribuidos(){
		int distribuido = 0;
		if( controles != null ){
			for( ControleDistribuicao dist : controles ){
				distribuido = distribuido + dist.getDistribuido();
			}
		}
		return distribuido;	
	}
}
