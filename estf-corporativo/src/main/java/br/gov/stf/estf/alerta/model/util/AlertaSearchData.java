package br.gov.stf.estf.alerta.model.util;

import java.util.Date;

import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.model.util.ESTFSearchData;
import br.gov.stf.estf.model.util.TipoOrdemProcesso;

/**
 * Objeto que encapsula os diversos parâmetros que podem ser utilizados na consulta de alertas.
 * 
 * @author thiago.miranda
 * @since 3.82.0
 */
public class AlertaSearchData extends ESTFSearchData {

	private static final long serialVersionUID = -6996704624371121915L;

	public Andamento andamento;
	public Usuario usuario;
	public Date dataNotificado;
	public Date dataAndamento;
	public Short anoProtocolo;
	public Long numeroProtocolo;
	public String siglaClasseProcessual;
	public Long numeroProcessual;
	public Long idSetor;
	public TipoOrdemProcesso tipoOrdemProcesso;
	
	/*
	 * Construtor utilizado apenas para facilitar a transição dos métodos que listam todos os parâmetros para listarem apenas um objeto TarefaSetorSearchData.
	 * Deverá ser descartado no futuro.
	 */
	public AlertaSearchData(Andamento andamento, Usuario usuario, Date dataNotificado, Date dataAndamento, Short anoProtocolo, Long numeroProtocolo, String siglaClasseProcessual,
			Long numeroProcessual, Long idSetor, TipoOrdemProcesso tipoOrdemProcesso) {
		this.andamento = andamento;
		this.usuario = usuario;
		this.dataNotificado = dataNotificado;
		this.dataAndamento = dataAndamento;
		this.anoProtocolo = anoProtocolo;
		this.numeroProtocolo = numeroProtocolo;
		this.siglaClasseProcessual = siglaClasseProcessual;
		this.numeroProcessual = numeroProcessual;
		this.idSetor = idSetor;
		this.tipoOrdemProcesso = tipoOrdemProcesso;
	}
	
	public AlertaSearchData() {
		super();
	}
}
