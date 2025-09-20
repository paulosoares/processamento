package br.gov.stf.estf.documento.model.util;

import java.util.Date;

import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.processostf.model.util.Dispositivo;

public class ControleDeVotoDTO {

	private ObjetoIncidente objetoIncidente;
	private Date dataSessao;
	private Ministro ministro;
	private TipoSessaoControleVoto tipoSessaoControleVoto;
	private Dispositivo dispositivo;
	private JulgamentoProcesso julgamentoProcesso;
	private Sessao sessao;

	public Date getDataSessao() {
		return dataSessao;
	}

	public void setDataSessao(Date dataSessao) {
		this.dataSessao = dataSessao;
	}

	public ObjetoIncidente getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	public Ministro getMinistro() {
		return ministro;
	}

	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}

	public TipoSessaoControleVoto getTipoSessaoControleVoto() {
		return tipoSessaoControleVoto;
	}

	public void setTipoSessaoControleVoto(TipoSessaoControleVoto tipoSessaoControleVoto) {
		this.tipoSessaoControleVoto = tipoSessaoControleVoto;
	}

	public Dispositivo getDispositivo() {
		return dispositivo;
	}

	public void setDispositivo(Dispositivo dispositivo) {
		this.dispositivo = dispositivo;
	}

	public JulgamentoProcesso getJulgamentoProcesso() {
		return julgamentoProcesso;
	}

	public void setJulgamentoProcesso(JulgamentoProcesso julgamentoProcesso) {
		this.julgamentoProcesso = julgamentoProcesso;
	}

	public void setSessaoJulgamento(Sessao sessao) {
		this.sessao = sessao;
	}
	
	public Sessao getSessao() {
		return sessao;
	}

}
