package br.gov.stf.estf.repercussaogeral.model.util;

import java.util.List;

import br.gov.stf.framework.util.SearchData;

public class RepercussaoGeralSearchData extends SearchData{
	
	private static final long serialVersionUID = 1L;
	
	public String siglaClasse;
	public List<String> classesProcessuais;
	public Long numeroProcesso;
	public Long numeroTema;
	public Long codigoMinistroRelator;
	public Long codigoMinistroAutenticado;
	public Long codigoAssunto;
	public String descricaoAssunto;
	public DateRange dataInicioDateRange;
	public DateRange dataPrevistaFimDateRange;
	public Situacao situacao;
	public Boolean baixados;
	public Boolean publicados;
	public Boolean haRepercussaoGeral;
	public Boolean meritoJulgado;
	public Long normaPesquisa;
	public Long numeroLegislacaoPesquisa;
	public Short numeroAnoPesquisa;
	public String numeroArtigoPesquisa;
	public String numeroIncisoPesquisa;
	public String numeroParagrafoPesquisa;
	public String numeroAlineaPesquisa;
	public String tipoJulgamento;
	public String tipoJulgamentoDiferente;
	public List<Long> listaIdDocumentoTexto;
	public SituacaoRG situacaoRG;
	public Boolean suspensaoNacional;
	private Boolean exclusivoDigital;
	
	public enum Situacao{
		JULGAMENTO_NAO_INICIADO("Julgamento não iniciado"),
		MANIFESTACAO_PENDENTE_EM_JULGAMENTO("Manifestação pendente em julgamento"),
		EM_JULGAMENTO("Em julgamento"),
		JULGADO("Julgada"),
		PUBLICADO("Publicado"),
		JULGADO_EM_JULGAMENTO("Julgada ou em julgamento"),
		AGENDADO("Agendado"),
		SUSPENSO("Suspenso"),
		TODOS("Todos"), 
		REANALISE("Reanálise");

		private String descricao;

		private Situacao(String descricao){
			this.descricao = descricao;
		}

		public String getDescricao(){
			return this.descricao;
		}
	}
	
	/* enum criado para alterar a consulta da Repercussão Geral, sem alterar a consulta do Plenário Virtual*/
	public enum SituacaoRG{
		COM_REPERCUSSAO("Com Repercussão"),
		SEM_REPERCUSSAO("Sem Repercussão"),
		EM_JULGAMENTO("Em julgamento"),
		JULGADAS("Julgadas"),
		MATERIA_INFRA("Matéria Infra"),
		TODOS("Todos");

		private String descricao;

		private SituacaoRG(String descricao){
			this.descricao = descricao;
		}

		public String getDescricao(){
			return this.descricao;
		}
	}
	
	public enum AndamentoRG{
		NAOHARG(6301),// Decisão pela inexistência de repercussão geral
		NAOHARGQC(6306), // Matéria INFRA - Decisão pela inexistência de repercussão geral, art. 324, §2º do RISTF
		EMJULGAMENTO(7602), // Em Julgamento
		/********************************** Repercussão Geral ********************************************************/
		HARG(6300), // Decisão pela existência de repercussão geral
		HARGJM(6308), // Reconhecida a repercussão geral e julgado o mérito
		AIPROVIDOJMRG(6309),// Agravo provido e julgado mérito de tema com repercussão geral
		SUBSTITUIDORG(8251); // Substitui o paradigma de repercussão geral - processo nº
		private int codAndamento;

		private AndamentoRG(int codAndamento){
			this.codAndamento = codAndamento;
		}
		
		public int getCodigoAndamento(){
			return this.codAndamento;
		}
	}
	

	public boolean getPesquisaJulgamentoProcesso(){
		
		if( getPesquisaSituacaoJulgamento() || (situacao != null && situacao.equals(Situacao.MANIFESTACAO_PENDENTE_EM_JULGAMENTO))){
			return true;
		}else if( dataInicioDateRange != null || dataPrevistaFimDateRange != null ){
			return true;
		}else if( haRepercussaoGeral != null ){
			return true;
		}else if( SearchData.stringNotEmpty(tipoJulgamento) || SearchData.stringNotEmpty(tipoJulgamentoDiferente) ){
			return true;
		}else if( listaIdDocumentoTexto != null && listaIdDocumentoTexto.size() > 0 ){
			return true;
		}
		return false;
	}
	
	public boolean getPesquisaLegislacao(){
		
		if( (normaPesquisa != null && normaPesquisa > 0) || 
				(numeroLegislacaoPesquisa != null && numeroLegislacaoPesquisa > 0) || 
				 stringNotEmpty(numeroArtigoPesquisa) || stringNotEmpty(numeroIncisoPesquisa) || 
				 stringNotEmpty(numeroParagrafoPesquisa) || stringNotEmpty(numeroAlineaPesquisa)){
			return true;
		}
		
		return false;	
	}
	
	public boolean getPesquisaAssunto(){
		if( codigoAssunto != null || stringNotEmpty(descricaoAssunto) ){
			return true;
		}
		return false;
	}
	
	public boolean getPesquisaSituacaoJulgamento(){
		if(  situacao != null && (situacao.equals(Situacao.EM_JULGAMENTO) || situacao.equals(Situacao.JULGADO) || 
				  situacao.equals(Situacao.JULGADO_EM_JULGAMENTO) || situacao.equals(Situacao.AGENDADO) || 
				  situacao.equals(Situacao.SUSPENSO) ||  
				  situacao.equals(Situacao.MANIFESTACAO_PENDENTE_EM_JULGAMENTO))){
			return true;
		}
		
		if(situacaoRG != null && (!situacaoRG.equals(SituacaoRG.TODOS))){
			return true;
		}
		
		return false;
	}

	public Boolean getExclusivoDigital() {
		return exclusivoDigital;
	}

	public void setExclusivoDigital(Boolean exclusivoDigital) {
		this.exclusivoDigital = exclusivoDigital;
	}
}
