package br.gov.stf.estf.processostf.model.util;

public enum Dispositivo {
	AGRAVO_REGIMENTAL_NAO_PROVIDO(1L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ negou provimento ao agravo regimental, nos termos do voto @@RELATOR@@.","Agravo Regimental N�o Provido", 6311L, "Agravo Regimental N�o Provido"),
	AGRAVO_REGIMENTAL_NAO_CONHECIDO(2L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ n�o conheceu do agravo regimental, nos termos do voto @@RELATOR@@.","Agravo Regimental N�o Conhecido", 6310L, "Agravo Regimental N�o Conhecido"),
	AGRAVO_REGIMENTAL_PROVIDO(3L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ deu provimento ao agravo regimental, nos termos do voto @@RELATOR@@.","Agravo Regimental Provido", 6313L, "Agravo Regimental Provido"),
	AGRAVO_REGIMENTAL_PROVIDO_EM_PARTE(4L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ deu parcial provimento ao agravo regimental, nos termos do voto @@RELATOR@@.","Agravo Regimental Provido em parte", 6312L, "Agravo Regimental Provido em parte"),
	EMBARGOS_DE_DECLARACAO_REJEITADOS(5L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ rejeitou os embargos de declara��o, nos termos do voto @@RELATOR@@.","Embargos de Declara��o Rejeitados", 6315L, "Embargos Rejeitados"),
	EMBARGOS_DE_DECLARACAO_NAO_CONHECIDOS(6L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ n�o conheceu dos embargos de declara��o, nos termos do voto @@RELATOR@@.","Embargos de Declara��o n�o Conhecidos", 6314L, "Embargos N�o Conhecidos"),
	EMBARGOS_DE_DECLARACAO_ACOLHIDOS(7L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ acolheu os embargos de declara��o, nos termos do voto @@RELATOR@@.","Embargos de Declara��o acolhidos", 6317L, "Embargos Recebidos"),
	EMBARGOS_DE_DECLARACAO_ACOLHIDOS_EM_PARTE(8L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ acolheu, em parte, os embargos de declara��o, nos termos do voto @@RELATOR@@.","Embargos de Declara��o acolhidos em parte", 6316L, "Embargos Recebidos em Parte"),
	EMBARGOS_DE_DECLARACAO_CONVERTIDOS_EM_AGRAVO_REGIMENTAL_A_QUE_SE_NEGA_PROVIMENTO(9L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ converteu os embargos de declara��o em agravo regimental e negou-lhe provimento, nos termos do voto @@RELATOR@@.","Embargos de Declara��o convertidos em agravo regimental, a que se nega provimento", 6318L, "ED recebidos como AGR. REGI. e n�o provido"),
	EMBARGOS_DE_DECLARACAO_CONVERTIDOS_EM_AGRAVO_REGIMENTAL_A_QUE_SE_D�_PROVIMENTO(10L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ converteu os embargos de declara��o em agravo regimental e deu-lhe provimento, nos termos do voto @@RELATOR@@.","Embargos de Declara��o convertidos em agravo regimental, a que se d� provimento", 6319L, "ED recebidos como AGR. REGI. e provido"),
	EMBARGOS_DE_DECLARACAO_CONVERTIDOS_EM_AGRAVO_REGIMENTAL_DO_QUAL_NAO_SE_CONHECE(11L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ converteu os embargos de declara��o em agravo regimental e dele n�o conheceu, nos termos do voto @@RELATOR@@.","Embargos de Declara��o convertidos em agravo regimental, do qual n�o se conhece", 6320L, "ED recebidos como AGR. REGI. e n�o conhecido"),
	EMBARGOS_DE_DECLARACAO_CONVERTIDOS_EM_AGRAVO_REGIMENTAL_A_QUE_SE_NEGA_PROVIMENTO_POR_MAIORIA(12L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ converteu os embargos de declara��o em agravo regimental, vencido o Ministro Marco Aur�lio, e, por unanimidade, negou provimento ao recurso, nos termos do voto @@RELATOR@@.","Embargos de Declara��o convertidos em agravo regimental, a que se nega provimento (por maioria),", 6318L, "ED recebidos como AGR. REGI. e n�o provido"),
	EMBARGOS_DE_DECLARACAO_NAO_PROVIDOS(13L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ negou provimento aos embargos de declara��o, nos termos do voto @@RELATOR@@.","Embargos de Declara��o N�o Providos", 6315L, "Embargos Rejeitados"),
	EMBARGOS_DE_DECLARACAO_PROVIDOS(14L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ deu provimento aos embargos de declara��o, nos termos do voto @@RELATOR@@.","Embargos de Declara��o providos", 6317L, "Embargos Recebidos"),
	EMBARGOS_DE_DECLARACAO_PROVIDOS_EM_PARTE(15L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ deu parcial provimento aos embargos de declara��o, nos termos do voto @@RELATOR@@.","Embargos de Declara��o providos em parte", 6316L, "Embargos Recebidos em Parte"),
	PEDIDO_DE_VISTA(16L,"","Pedido de vista", 8304L, "Vista ao(�), Ministro(a),"),
	ACOLHIDA_PROPOSTA_DE_EDICAO_DE_SV(17L,"","Acolhida proposta de edi��o de SV", 6400L, "Acolhida proposta de edi��o de SV"),
	CONCEDIDA_A_ORDEM(18L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ concedeu a ordem @@RELATOR@@.","Concedida a ordem", 6203L, "Concedida a ordem"),
	CONCEDIDA_A_ORDEM_DE_OF�CIO(19L,"","Concedida a ordem de of�cio", 6243L, "Concedida a ordem de of�cio"),
	CONCEDIDA_A_SEGURANCA(20L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ concedeu a ordem, de of�cio, @@RELATOR@@.","Concedida a seguran�a", 6204L, "Concedida a seguran�a"),
	CONCEDIDA_EM_PARTE_A_ORDEM(21L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ concedeu parcialmente a ordem @@RELATOR@@.","Concedida em parte a ordem", 6205L, "Concedida em parte a ordem"),
	CONCEDIDA_EM_PARTE_A_SEGURANCA(22L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ concedeu parcialmente a seguran�a @@RELATOR@@.","Concedida em parte a seguran�a", 6206L, "Concedida em parte a seguran�a"),
	CONHECIDA_E_JULGADA_SEM_PRON�NCIA_DE_INCONSTITUCIONALIDADE(23L,"","Conhecida e julgada sem pron�ncia de inconstitucionalidade", 6335L, "Conhecida e julgada sem pron�ncia de inconstitucionalidade"),
	CONHECIDO_E_NEGADO_PROVIMENTO(24L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ conheceu do [recurso/agravo] e negou-lhe provimento, nos termos do voto @@RELATOR@@.","Conhecido e negado provimento", 6207L, "Conhecido e negado provimento"),
	CONHECIDO_E_PROVIDO(25L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ conheceu do [recurso/agravo] e deu-lhe provimento, nos termos do voto @@RELATOR@@.","Conhecido e provido", 6208L, "Conhecido e provido"),
	CONHECIDO_E_PROVIDO_EM_PARTE(26L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ conheceu do [recurso/agravo] e deu-lhe parcial provimento, nos termos do voto @@RELATOR@@.","Conhecido e provido em parte", 6209L, "Conhecido e provido em parte"),
	CONHECIDO_EM_PARTE_E_NESSA_PARTE_NEGADO_PROVIMENTO(27L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ conheceu parcialmente do [recurso/agravo] e, nessa parte, negou-lhe provimento, nos termos do voto @@RELATOR@@.","Conhecido em parte e nessa parte negado provimento", 6210L, "Conhecido em parte e nessa parte negado provimento"),
	CONHECIDO_EM_PARTE_E_NESSA_PARTE_PARCIALMENTE_PROVIDO(28L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ conheceu parcialmente do [recurso/agravo] e, nessa parte, deu-lhe parcial provimento, nos termos do voto @@RELATOR@@.","Conhecido em parte e nessa parte parcialmente provido", 6212L, "Conhecido em parte e nessa parte parcialmente provido"),
	CONHECIDO_EM_PARTE_E_NESSA_PARTE_PROVIDO(29L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ conheceu parcialmente do [recurso/agravo] e, nessa parte, deu-lhe provimento, nos termos do voto @@RELATOR@@.","Conhecido em parte e nessa parte provido", 6211L, "Conhecido em parte e nessa parte provido"),
	CONVERTIDO_EM_DILIG�NCIA(30L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ converteu o julgamento em dilig�ncia @@RELATOR@@.","Convertido em dilig�ncia", 6001L, "Convertido em dilig�ncia"),
	DECISAO_PELA_EXIST�NCIA_DE_REPERCUSSAO_GERAL(31L,"","Decis�o pela exist�ncia de repercuss�o geral", 6300L, "Decis�o pela exist�ncia de repercuss�o geral"),
	DECISAO_PELA_INEXIST�NCIA_DE_REPERCUSSAO_GERAL(32L,"","Decis�o pela inexist�ncia de repercuss�o geral", 6301L, "Decis�o pela inexist�ncia de repercuss�o geral"),
	DECISAO_PELA_INEXIST�NCIA_DE_REPERCUSSAO_GERAL_POR_SE_TRATAR_DE_MAT�RIA_INFRACONSTITUCIONAL(33L,"","Decis�o pela inexist�ncia de repercuss�o geral por se tratar de mat�ria infraconstitucional", 6306L, "Decis�o pela inexist�ncia de repercuss�o geral por se tratar de mat�ria infraconstitucional"),
	DECISAO_RATIFICADA(34L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ ratificou a decis�o @@RELATOR@@.","Decis�o Ratificada", 8518L, "Decis�o Ratificada"),
	DECISAO_REFERENDADA(35L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ referendou a decis�o @@RELATOR@@.","Decis�o Referendada", 8517L, "Decis�o Referendada"),
	DECLARADA_A_EXTINCAO_DA_PUNIBILIDADE(36L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ declarou extinta a punibilidade, nos termos do voto @@RELATOR@@.","Declarada a extin��o da punibilidade", 6213L, "Declarada a extin��o da punibilidade"),
	DECLINADA_A_COMPET�NCIA(37L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ declinou da compet�ncia, nos termos do voto @@RELATOR@@.","Declinada a compet�ncia", 6215L, "Declinada a compet�ncia"),
	DEFERIDO(38L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ deferiu o pedido, nos termos do voto @@RELATOR@@.","Deferido", 6003L, "Deferido"),
	DEFERIDO_EM_PARTE(39L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ deferiu parcialmente o pedido, nos termos do @@RELATOR@@.","Deferido em parte", 6004L, "Deferido em parte"),
	DENEGADA_A_ORDEM(40L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ denegou a ordem, nos termos do voto @@RELATOR@@.","Denegada a ordem", 6217L, "Denegada a ordem"),
	DENEGADA_A_SEGURANCA(41L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ denegou a seguran�a, nos termos do @@RELATOR@@.","Denegada a seguran�a", 6218L, "Denegada a seguran�a"),
	DETERMINADA_A_DEVOLUCAO(42L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ determinou a devolu��o dos autos, nos termos do voto @@RELATOR@@.","Determinada a devolu��o", 6012L, "Determinada a devolu��o"),
	DETERMINADA_A_DEVOLUCAO_PELO_REGIME_DA_REPERCUSSAO_GERAL(43L,"","Determinada a devolu��o pelo regime da repercuss�o geral", 6510L, "Determinada a devolu��o pelo regime da repercuss�o geral"),
	DETERMINADO_ARQUIVAMENTO(44L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ determinou o arquivamento dos autos, nos termos do voto @@RELATOR@@.","Determinado arquivamento", 6219L, "Determinado arquivamento"),
	EXTINTO_O_PROCESSO(45L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ julgou extinto o processo @@RELATOR@@.","Extinto o processo", 6220L, "Extinto o processo"),
	HOMOLOGADA_A_DESIST�NCIA(46L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ homologou o pedido de desist�ncia, nos termos do voto @@RELATOR@@.","Homologada a desist�ncia", 6221L, "Homologada a desist�ncia"),
	IMPROCEDENTE(47L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ julgou improcedente _____, nos termos do voto @@RELATOR@@.","Improcedente", 6222L, "Improcedente"),
	INDEFERIDO(48L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ indeferiu o pedido, nos termos do voto @@RELATOR@@.","Indeferido", 6005L, "Indeferido"),
	JULGADO_M�RITO_DE_TEMA_COM_REPERCUSSAO_GERAL(49L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ apreciando o tema [ ] da repercuss�o geral, [negou/deu provimento] ao recurso extraordin�rio, nos termos do voto [do/da] [relator/relatora]. Em seguida, [por maioria]/[por unanimidade], fixou-se a seguinte tese: [ ]. @@RELATOR@@.","Julgado m�rito de tema com repercuss�o geral", 6307L, "Julgado m�rito de tema com repercuss�o geral"),
	LIMINAR_DEFERIDA(50L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ deferiu a liminar, nos termos do voto @@RELATOR@@.","Liminar deferida", 6100L, "Liminar deferida"),
	LIMINAR_DEFERIDA_EM_PARTE(51L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ deferiu parcialmente a liminar, nos termos do voto @@RELATOR@@.","Liminar deferida em parte", 6101L, "Liminar deferida em parte"),
	LIMINAR_INDEFERIDA(52L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ indeferiu a liminar, nos termos do voto @@RELATOR@@.","Liminar indeferida", 6102L, "Liminar indeferida"),
	LIMINAR_REFERENDADA(53L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ referendou a liminar concedida, nos termos do voto @@RELATOR@@.","Liminar referendada", 6105L, "Liminar referendada"),
	LIMINAR_REFERENDADA_EM_PARTE(54L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ referendou, em parte, a liminar concedida, nos termos do voto @@RELATOR@@.","Liminar referendada em parte", 6106L, "Liminar referendada em parte"),
	NAO_CONHECIDO(55L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ n�o conheceu do/da [ ], nos termos do voto @@RELATOR@@.","N�o conhecido(s)", 6224L, "N�o conhecido(s)"),
	NAO_PROVIDO(56L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ negou provimento ao recurso, nos termos do voto @@RELATOR@@.","N�o provido", 6225L, "N�o provido"),
	NEGADO_SEGUIMENTO(57L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ negou seguimento ao [ ], nos termos do voto @@RELATOR@@.","Negado seguimento", 6226L, "Negado seguimento"),
	PREJUDICADO(58L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ julgou prejudicado(a), o(a), [ ], nos termos do voto @@RELATOR@@.","Prejudicado", 6227L, "Prejudicado"),
	PROCEDENTE(59L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ julgou procedente _____, nos termos do voto @@RELATOR@@.","Procedente", 6228L, "Procedente"),
	PROCEDENTE_EM_PARTE(60L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ julgou parcialmente procedente _____, nos termos do voto @RELATOR@@.","Procedente em parte", 6229L, "Procedente em parte"),
	PROVIDO(61L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ deu provimento ao recurso, nos termos do voto @@RELATOR@@.","Provido", 6230L, "Provido"),
	PROVIDO_EM_PARTE(62L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ deu parcial provimento ao recurso, nos termos do @@RELATOR@@.","Provido em parte", 6231L, "Provido em parte"),
	QUESTAO_DE_ORDEM(63L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ resolveu a quest�o de ordem (...),, nos termos do voto @@RELATOR@@.","Quest�o de ordem", 6232L, "Quest�o de ordem"),
	RECEBIDA_A_QUEIXA(64L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ recebeu a queixa-crime, nos termos do voto @@RELATOR@@.","Recebida a queixa", 6233L, "Recebida a queixa"),
	RECEBIDA_A_QUEIXA_EM_PARTE(65L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ recebeu parcialmente a queixa-crime, nos termos do voto @@RELATOR@@.","Recebida a queixa em parte", 6234L, "Recebida a queixa em parte"),
	RECEBIDA_DEN�NCIA(66L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ recebeu a den�ncia, nos termos do voto @@RELATOR@@.","Recebida den�ncia", 6235L, "Recebida den�ncia"),
	RECEBIDA_DEN�NCIA_EM_PARTE(67L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ recebeu parcialmente a den�ncia, nos termos do voto @@RELATOR@@.","Recebida den�ncia em parte", 6236L, "Recebida den�ncia em parte"),
	RECEBIDOS(68L,"","Recebidos", 6237L, "Recebidos"),
	RECEBIDOS_EM_PARTE(69L,"","Recebidos em parte", 6238L, "Recebidos em parte"),
	RECONSIDERO_E_JULGO_PREJUDICADO_O_RECURSO_INTERNO(70L,"","Reconsidero e julgo prejudicado o recurso interno", 6321L, "Reconsidero e julgo prejudicado o recurso interno"),
	REJEITADA_A_DEN�NCIA(71L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ rejeitou a den�ncia, nos termos do voto @@RELATOR@@.","Rejeitada a den�ncia", 6239L, "Rejeitada a den�ncia"),
	REJEITADA_A_QUEIXA(72L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ rejeitou a queixa-crime, nos termos do voto @@RELATOR@@.","Rejeitada a queixa", 6240L, "Rejeitada a queixa"),
	REJEITADA_PROPOSTA_DE_CANCELAMENTO_DE_SV(73L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ rejeitou a proposta de cancelamento da S�mula Vinculante n� @@RELATOR@@.","Rejeitada proposta de cancelamento de SV", 6405L, "Rejeitada proposta de cancelamento de SV"),
	REJEITADA_PROPOSTA_DE_REVISAO_DE_SV(74L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ rejeitou a proposta de revis�o da S�mula Vinculante n� @@RELATOR@@.","Rejeitada proposta de revis�o de SV", 6403L, "Rejeitada proposta de revis�o de SV"),
	REJEITADA_PROPOSTA_DE_EDICAO_DE_SV(75L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ rejeitou a proposta de edi��o de S�mula Vinculante @@RELATOR@@.","Rejeitada proposta de edi��o de SV", 6401L, "Rejeitada proposta de edi��o de SV"),
	REJEITADOS(76L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ rejeitou os embargos de declara��o, nos termos do voto @@RELATOR@@.","Rejeitados", 6241L, "Rejeitados"),
	SOBRESTADO(77L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ sobrestou o julgamento do processo, nos termos do voto @@RELATOR@@.","Sobrestado", 6007L, "Sobrestado"),
	SOBRESTADO_AGUARDANDO_DECISAO_DO_STJ(78L,"","Sobrestado, aguardando decis�o do STJ", 7000L, "Sobrestado, aguardando decis�o do STJ"),
	SUSPENSO_O_JULGAMENTO(79L,"","Suspenso o Julgamento", 7205L, "Suspenso o Julgamento");

	private Long id;
	private String texto;
	private String nome;
	private Long andamentoId;
	private String andamentoDescricao;
	
	
	Dispositivo(Long id, String texto, String nome, Long andamentoId, String andamentoDescricao) {
		this.id = id;
		this.texto = texto;
		this.nome = nome;
		this.andamentoId = andamentoId;
		this.andamentoDescricao = andamentoDescricao;
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getTexto() {
		return texto;
	}


	public void setTexto(String texto) {
		this.texto = texto;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public Long getAndamentoId() {
		return andamentoId;
	}


	public void setAndamentoId(Long andamentoId) {
		this.andamentoId = andamentoId;
	}


	public String getAndamentoDescricao() {
		return andamentoDescricao;
	}


	public void setAndamentoDescricao(String andamentoDescricao) {
		this.andamentoDescricao = andamentoDescricao;
	}


	public static Dispositivo valueOf(Long codigo) {
		if (codigo != null)
			for (Dispositivo dispositivo : Dispositivo.values())
				if (dispositivo.getId().equals(codigo))
					return dispositivo;
		
		return null;
	}
}
