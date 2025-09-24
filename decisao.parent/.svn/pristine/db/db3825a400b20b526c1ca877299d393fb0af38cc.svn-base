/**
 * 
 */
package br.jus.stf.estf.decisao.objetoincidente.support;

/**
 * @author Paulo.Estevao
 * @since 01.09.2010
 */
public enum ProcessoRecursoRelatorioEnum{
	PROCESSO("processo", "Processo", 80, false),
	SIGLA_CLASSE_PROCESSO("siglaClasseProcesso", "Classe Processual", 60, false),
	NUMERO_PROCESSO("numeroProcesso", "Número Processual", 60, false),
	RECURSO("tipoRecurso", "Recurso", 80, false),
	TIPO_JULGAMENTO("tipoJulgamento", "Tipo Julgamento", 80, false),
	LIMINAR("possuiLiminar", "Liminar", 50, false),
	BAIXA("pendenteBaixa", "Pendente de baixa", 60, false),
//	JULGAMENTO("julgamento", "Julgado", 40, false),
	REPERCUSSAO_GERAL("repercussaoGeral", "Repercussão Geral", 70, false),
	SETOR("setor", "Setor", 40, false),
	SIGLA_SETOR("siglaSetor", "Sigla Setor", 40, false),
	MINISTRO("ministro", "Ministro", 130, false),
	TIPO_MEIO_PROCESSO("tipoMeioProcesso", "Meio", 50, false);
//	DATA_JULGAMENTO("dataJulgamento", "Data Julgamento", 50, false);
	
	/**
	 * Atributo da classe.
	 */
	private final String atributo;
	
	/**
	 * Descrição da coluna.
	 */
	private final String descricao;
	
	/**
	 * Largura minima da coluna.
	 */
	private final int tamanho;
	
	/**
	 * É um atributo multi-valorado
	 */
	private final boolean multiValorado;
	
	private ProcessoRecursoRelatorioEnum(String atributo, String descricao, int tamanho, boolean multiValorado){
		this.atributo = atributo;
		this.descricao = descricao;
		this.tamanho = tamanho;
		this.multiValorado = multiValorado;
	}
	
	public String getAtributo() {
		return atributo;
	}

	public String getDescricao() {
		return descricao;
	}

	public int getTamanho() {
		return tamanho;
	}

	public boolean isMultiValorado() {
		return multiValorado;
	}
}


