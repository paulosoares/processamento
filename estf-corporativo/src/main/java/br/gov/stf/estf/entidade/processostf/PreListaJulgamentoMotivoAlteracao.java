package br.gov.stf.estf.entidade.processostf;

import br.gov.stf.framework.util.GenericEnum;

public class PreListaJulgamentoMotivoAlteracao extends GenericEnum<Long, PreListaJulgamentoMotivoAlteracao> {
	private static final long serialVersionUID = 1L;
	public static final PreListaJulgamentoMotivoAlteracao AUTOMATICA = new PreListaJulgamentoMotivoAlteracao(1L, "Inclusão automática");
	public static final PreListaJulgamentoMotivoAlteracao MANUAL = new PreListaJulgamentoMotivoAlteracao(2L, "Inclusão manual"); 
	public static final PreListaJulgamentoMotivoAlteracao JA_JULGADO = new PreListaJulgamentoMotivoAlteracao(3L, "Processo já julgado"); 
	public static final PreListaJulgamentoMotivoAlteracao SEM_VOTO = new PreListaJulgamentoMotivoAlteracao(4L, "Processo sem voto");
	public static final PreListaJulgamentoMotivoAlteracao SEM_EMENTA = new PreListaJulgamentoMotivoAlteracao(5L, "Processo sem ementa");
	public static final PreListaJulgamentoMotivoAlteracao OUTRO_GABINETE = new PreListaJulgamentoMotivoAlteracao(6L, "Processo de outro gabiente");
	public static final PreListaJulgamentoMotivoAlteracao SEM_RELATORIO = new PreListaJulgamentoMotivoAlteracao(6L, "Processo sem relatório");
	
	private String descricao;
	
	private PreListaJulgamentoMotivoAlteracao (Long codigo) {
		super(codigo);
	} 
	
	private PreListaJulgamentoMotivoAlteracao (Long codigo, String descricao) {
		super(codigo);
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public static PreListaJulgamentoMotivoAlteracao valueOf(Long codigo) {
		return valueOf(PreListaJulgamentoMotivoAlteracao.class, codigo);
	}
	
	public static PreListaJulgamentoMotivoAlteracao valueOf(Integer codigo) {
		return valueOf(PreListaJulgamentoMotivoAlteracao.class, Long.parseLong(codigo.toString()));
	}

	public PreListaJulgamentoMotivoAlteracao[] values() {
		return values(new PreListaJulgamentoMotivoAlteracao[0], PreListaJulgamentoMotivoAlteracao.class);
	}
}