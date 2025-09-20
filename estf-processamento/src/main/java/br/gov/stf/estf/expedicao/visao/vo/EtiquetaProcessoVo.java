package br.gov.stf.estf.expedicao.visao.vo;

import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.expedicao.entidade.ConfiguracaoEncaminhamento;
import br.gov.stf.estf.expedicao.entidade.DestinatarioListaRemessa;
import br.gov.stf.estf.expedicao.entidade.VwServidorAssinador;

/**
 *
 * @author roberio.fernandes
 */
public class EtiquetaProcessoVo {

	private VwServidorAssinador usuario;
	private ConfiguracaoEncaminhamento encaminhamento;
	private Andamento andamento;
	private DestinatarioListaRemessa destinatarioListaRemessa;
	private boolean exibe;

	public EtiquetaProcessoVo() {
	}

	public EtiquetaProcessoVo(VwServidorAssinador usuario, ConfiguracaoEncaminhamento encaminhamento, Andamento andamento, DestinatarioListaRemessa destinatarioListaRemessa) {
		super();
		this.usuario = usuario;
		this.encaminhamento = encaminhamento;
		this.andamento = andamento;
		this.destinatarioListaRemessa = destinatarioListaRemessa;
		this.exibe = true;
	}

	public EtiquetaProcessoVo(boolean exibe) {
		this.exibe = exibe;
	}

	public VwServidorAssinador getUsuario() {
		return usuario;
	}

	public ConfiguracaoEncaminhamento getEncaminhamento() {
		return encaminhamento;
	}

	public Andamento getAndamento() {
		return andamento;
	}

	public DestinatarioListaRemessa getDestinatarioListaRemessa() {
		return destinatarioListaRemessa;
	}

	public boolean isExibe() {
		return exibe;
	}

	public void setExibe(boolean exibe) {
		this.exibe = exibe;
	}
}