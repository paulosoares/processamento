/**
 * 
 */
package br.gov.stf.estf.entidade.processostf;

/**
 * @author Paulo.Estevao
 * @since 21.03.2012
 */
public enum TipoDependenciaProcesso {
	
	SOBRESTADO(												1L, 	"Sobrestado"),
	REAUTUADO(												2L, 	"Reautudo"),
	DESAPENSADO_DO_PROCESSO(								3L, 	"Desapensado do Processo nº"),
	AUTOS_EM_RESTAURACAO(									4L, 	"Autos em Restauração"),
	APENSADO_AO_PROCESSO(									5L, 	"Apensado ao Processo nº"),
	DETERMINADA_A_DEVOLUCAO(							 	6L, 	"Determinada a devolução"),
	DETERMINADA_A_DEVOLUCAO_ART_543_B_DO_CPC(				7L, 	"Determinada a devolução, art. 543-B do CPC"),
	ANEXADO_O_PROTOCOLO(									8L, 	"Anexado o Protocolo nº"),
	AGRAVO_DE_INSTRUMENTO_APENSADO_AO_RE(					9L, 	"Agravo de Instrumento apensado ao RE nº"),
	JULGAMENTO_REPERCUSSAO_GERAL_SUBSTITUIDO_PELO_PROCESSO(	10L, 	"Julgamento repercussão geral substituído pelo processo nº"),
	MERITO_DA_REPERCUSSAO_GERAL_JULGADO_NO_PROCESSO(		11L, 	"Mérito da repercussão geral julgado no processo nº"),
	JUNTADA(												12L, 	"Juntada");

	private Long codigo;	
	private String descricao;
	
	private TipoDependenciaProcesso(Long codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public Long getCodigo() {
		return codigo;
	}
	
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public static TipoDependenciaProcesso valueOf(Long codigo) {
		for (TipoDependenciaProcesso tipoDependenciaProcesso : TipoDependenciaProcesso.values()) {
			if (tipoDependenciaProcesso.getCodigo().equals(codigo)) {
				return tipoDependenciaProcesso;
			}
		}
		return null;
	}
	
}
