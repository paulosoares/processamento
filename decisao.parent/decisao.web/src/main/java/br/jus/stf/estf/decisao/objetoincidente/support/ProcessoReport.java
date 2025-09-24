/**
 * 
 */
package br.jus.stf.estf.decisao.objetoincidente.support;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.RecursoProcesso;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.report.model.Field;

/**
 * @author Paulo.Estevao
 *
 */
public class ProcessoReport {

	private String processo;
	private String siglaClasseProcesso = "";
	private String numeroProcesso = "";
	private String tipoRecurso = "";
	private String tipoJulgamento = "";
	private String possuiLiminar = "";
	private String pendenteBaixa = "";
	private String repercussaoGeral = "";
	private String setor = "";
	private String siglaSetor = "";
	private String ministro = "";
	private String tipoMeioProcesso = "";
//	private String dataJulgamento = "";
	
	private final ObjetoIncidente<?> objetoIncidente;
	
	public ProcessoReport(ObjetoIncidente<?> objetoIncidente, Ministro ministro){
		this.objetoIncidente = objetoIncidente;
		
		processo = ObjetoIncidenteDto.valueOf(objetoIncidente).getIdentificacao();
		
		if(objetoIncidente.getTipoObjetoIncidente().equals(TipoObjetoIncidente.PROCESSO)){
			initProcesso((Processo) objetoIncidente);
		} else if(objetoIncidente.getTipoObjetoIncidente().equals(TipoObjetoIncidente.RECURSO)){
			initRecursoProcesso((RecursoProcesso) objetoIncidente);
		} else if(objetoIncidente.getTipoObjetoIncidente().equals(TipoObjetoIncidente.INCIDENTE_JULGAMENTO)){
			initIncidenteJulgamento((IncidenteJulgamento) objetoIncidente);
		}
		
		if (ministro != null){
			this.setor = ministro.getSetor().getNome();
			this.siglaSetor = ministro.getSetor().getSigla();
			this.ministro = ministro.getNome();
		}else{
			this.setor = "";
			this.siglaSetor = "";
			this.ministro = "";
		}
	}
	
	private void initRecursoProcesso(RecursoProcesso recursoProcesso){
		initProcesso(recursoProcesso.getPrincipal());
		this.tipoRecurso = recursoProcesso.getTipoRecursoProcesso().getDescricao();
	}
	
	private void initIncidenteJulgamento(IncidenteJulgamento incidenteJulgamento){
		initProcesso(incidenteJulgamento.getPrincipal());
		this.tipoJulgamento = incidenteJulgamento.getTipoJulgamento().getDescricao();
	}
	
	public static List<Field> getFields(List<ProcessoRecursoRelatorioEnum> camposRelatorio){
		
		List<Field> fields = new LinkedList<Field>();
		
		//FIXME Pesquisar uma solução para a largura dos campos
		for(ProcessoRecursoRelatorioEnum processoRecursoRelatorioEnum : camposRelatorio){
			fields.add(getField(processoRecursoRelatorioEnum, processoRecursoRelatorioEnum.getTamanho(), 10));
		}
		
		return fields;
	}
	
	private void initProcesso(Processo processo) {
		
		this.siglaClasseProcesso = processo.getClasseProcessual().getId();
		this.numeroProcesso = processo.getNumeroProcessual() + "";
		this.possuiLiminar = processo.getPossuiLiminar()? "Sim" : "Não";
		if(processo.getBaixa() != null){
			this.pendenteBaixa =  processo.getBaixa() ? "Sim" : "Não";
		}
		this.repercussaoGeral = processo.getRepercussaoGeral()?"Sim":"Não";
		this.tipoMeioProcesso = processo.getTipoMeioProcesso().getDescricao();
//		this.dataJulgamento = null;
	}
	
	public String getProcesso() {
		return processo;
	}
	public String getSiglaClasseProcesso() {
		return siglaClasseProcesso;
	}
	public String getNumeroProcesso() {
		return numeroProcesso;
	}
	public String getTipoRecurso() {
		return tipoRecurso;
	}
	public String getPossuiLiminar() {
		return possuiLiminar;
	}
	public String getPendenteBaixa() {
		return pendenteBaixa;
	}
	public String getRepercussaoGeral() {
		return repercussaoGeral;
	}
	public String getSetor() {
		return setor;
	}
	public String getSiglaSetor() {
		return siglaSetor;
	}

	public String getMinistro() {
		return ministro;
	}
	public String getTipoMeioProcesso() {
		return tipoMeioProcesso;
	}
//	public String getDataJulgamento() {
//		return dataJulgamento;
//	}
	public String getTipoJulgamento() {
		return tipoJulgamento;
	}
	
	public boolean equals(Object obj) {
		return new EqualsBuilder().append(objetoIncidente.getId().longValue(), 37).equals(obj);
	}
	
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(objetoIncidente.getId()).toHashCode();
	}
	
	private static Field getField(ProcessoRecursoRelatorioEnum processoRecursoRelatorioEnum, int width, int height){
		Field f = new Field(width,
			height,
			processoRecursoRelatorioEnum.getAtributo(),
			processoRecursoRelatorioEnum.getDescricao()) {
		};
		return f;
	}

}
