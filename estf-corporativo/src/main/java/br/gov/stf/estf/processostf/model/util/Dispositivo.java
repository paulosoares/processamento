package br.gov.stf.estf.processostf.model.util;

public enum Dispositivo {
	AGRAVO_REGIMENTAL_NAO_PROVIDO(1L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ negou provimento ao agravo regimental, nos termos do voto @@RELATOR@@.","Agravo Regimental Não Provido", 6311L, "Agravo Regimental Não Provido"),
	AGRAVO_REGIMENTAL_NAO_CONHECIDO(2L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ não conheceu do agravo regimental, nos termos do voto @@RELATOR@@.","Agravo Regimental Não Conhecido", 6310L, "Agravo Regimental Não Conhecido"),
	AGRAVO_REGIMENTAL_PROVIDO(3L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ deu provimento ao agravo regimental, nos termos do voto @@RELATOR@@.","Agravo Regimental Provido", 6313L, "Agravo Regimental Provido"),
	AGRAVO_REGIMENTAL_PROVIDO_EM_PARTE(4L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ deu parcial provimento ao agravo regimental, nos termos do voto @@RELATOR@@.","Agravo Regimental Provido em parte", 6312L, "Agravo Regimental Provido em parte"),
	EMBARGOS_DE_DECLARACAO_REJEITADOS(5L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ rejeitou os embargos de declaração, nos termos do voto @@RELATOR@@.","Embargos de Declaração Rejeitados", 6315L, "Embargos Rejeitados"),
	EMBARGOS_DE_DECLARACAO_NAO_CONHECIDOS(6L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ não conheceu dos embargos de declaração, nos termos do voto @@RELATOR@@.","Embargos de Declaração não Conhecidos", 6314L, "Embargos Não Conhecidos"),
	EMBARGOS_DE_DECLARACAO_ACOLHIDOS(7L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ acolheu os embargos de declaração, nos termos do voto @@RELATOR@@.","Embargos de Declaração acolhidos", 6317L, "Embargos Recebidos"),
	EMBARGOS_DE_DECLARACAO_ACOLHIDOS_EM_PARTE(8L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ acolheu, em parte, os embargos de declaração, nos termos do voto @@RELATOR@@.","Embargos de Declaração acolhidos em parte", 6316L, "Embargos Recebidos em Parte"),
	EMBARGOS_DE_DECLARACAO_CONVERTIDOS_EM_AGRAVO_REGIMENTAL_A_QUE_SE_NEGA_PROVIMENTO(9L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ converteu os embargos de declaração em agravo regimental e negou-lhe provimento, nos termos do voto @@RELATOR@@.","Embargos de Declaração convertidos em agravo regimental, a que se nega provimento", 6318L, "ED recebidos como AGR. REGI. e não provido"),
	EMBARGOS_DE_DECLARACAO_CONVERTIDOS_EM_AGRAVO_REGIMENTAL_A_QUE_SE_DÁ_PROVIMENTO(10L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ converteu os embargos de declaração em agravo regimental e deu-lhe provimento, nos termos do voto @@RELATOR@@.","Embargos de Declaração convertidos em agravo regimental, a que se dá provimento", 6319L, "ED recebidos como AGR. REGI. e provido"),
	EMBARGOS_DE_DECLARACAO_CONVERTIDOS_EM_AGRAVO_REGIMENTAL_DO_QUAL_NAO_SE_CONHECE(11L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ converteu os embargos de declaração em agravo regimental e dele não conheceu, nos termos do voto @@RELATOR@@.","Embargos de Declaração convertidos em agravo regimental, do qual não se conhece", 6320L, "ED recebidos como AGR. REGI. e não conhecido"),
	EMBARGOS_DE_DECLARACAO_CONVERTIDOS_EM_AGRAVO_REGIMENTAL_A_QUE_SE_NEGA_PROVIMENTO_POR_MAIORIA(12L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ converteu os embargos de declaração em agravo regimental, vencido o Ministro Marco Aurélio, e, por unanimidade, negou provimento ao recurso, nos termos do voto @@RELATOR@@.","Embargos de Declaração convertidos em agravo regimental, a que se nega provimento (por maioria),", 6318L, "ED recebidos como AGR. REGI. e não provido"),
	EMBARGOS_DE_DECLARACAO_NAO_PROVIDOS(13L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ negou provimento aos embargos de declaração, nos termos do voto @@RELATOR@@.","Embargos de Declaração Não Providos", 6315L, "Embargos Rejeitados"),
	EMBARGOS_DE_DECLARACAO_PROVIDOS(14L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ deu provimento aos embargos de declaração, nos termos do voto @@RELATOR@@.","Embargos de Declaração providos", 6317L, "Embargos Recebidos"),
	EMBARGOS_DE_DECLARACAO_PROVIDOS_EM_PARTE(15L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ deu parcial provimento aos embargos de declaração, nos termos do voto @@RELATOR@@.","Embargos de Declaração providos em parte", 6316L, "Embargos Recebidos em Parte"),
	PEDIDO_DE_VISTA(16L,"","Pedido de vista", 8304L, "Vista ao(à), Ministro(a),"),
	ACOLHIDA_PROPOSTA_DE_EDICAO_DE_SV(17L,"","Acolhida proposta de edição de SV", 6400L, "Acolhida proposta de edição de SV"),
	CONCEDIDA_A_ORDEM(18L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ concedeu a ordem @@RELATOR@@.","Concedida a ordem", 6203L, "Concedida a ordem"),
	CONCEDIDA_A_ORDEM_DE_OFÍCIO(19L,"","Concedida a ordem de ofício", 6243L, "Concedida a ordem de ofício"),
	CONCEDIDA_A_SEGURANCA(20L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ concedeu a ordem, de ofício, @@RELATOR@@.","Concedida a segurança", 6204L, "Concedida a segurança"),
	CONCEDIDA_EM_PARTE_A_ORDEM(21L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ concedeu parcialmente a ordem @@RELATOR@@.","Concedida em parte a ordem", 6205L, "Concedida em parte a ordem"),
	CONCEDIDA_EM_PARTE_A_SEGURANCA(22L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ concedeu parcialmente a segurança @@RELATOR@@.","Concedida em parte a segurança", 6206L, "Concedida em parte a segurança"),
	CONHECIDA_E_JULGADA_SEM_PRONÚNCIA_DE_INCONSTITUCIONALIDADE(23L,"","Conhecida e julgada sem pronúncia de inconstitucionalidade", 6335L, "Conhecida e julgada sem pronúncia de inconstitucionalidade"),
	CONHECIDO_E_NEGADO_PROVIMENTO(24L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ conheceu do [recurso/agravo] e negou-lhe provimento, nos termos do voto @@RELATOR@@.","Conhecido e negado provimento", 6207L, "Conhecido e negado provimento"),
	CONHECIDO_E_PROVIDO(25L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ conheceu do [recurso/agravo] e deu-lhe provimento, nos termos do voto @@RELATOR@@.","Conhecido e provido", 6208L, "Conhecido e provido"),
	CONHECIDO_E_PROVIDO_EM_PARTE(26L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ conheceu do [recurso/agravo] e deu-lhe parcial provimento, nos termos do voto @@RELATOR@@.","Conhecido e provido em parte", 6209L, "Conhecido e provido em parte"),
	CONHECIDO_EM_PARTE_E_NESSA_PARTE_NEGADO_PROVIMENTO(27L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ conheceu parcialmente do [recurso/agravo] e, nessa parte, negou-lhe provimento, nos termos do voto @@RELATOR@@.","Conhecido em parte e nessa parte negado provimento", 6210L, "Conhecido em parte e nessa parte negado provimento"),
	CONHECIDO_EM_PARTE_E_NESSA_PARTE_PARCIALMENTE_PROVIDO(28L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ conheceu parcialmente do [recurso/agravo] e, nessa parte, deu-lhe parcial provimento, nos termos do voto @@RELATOR@@.","Conhecido em parte e nessa parte parcialmente provido", 6212L, "Conhecido em parte e nessa parte parcialmente provido"),
	CONHECIDO_EM_PARTE_E_NESSA_PARTE_PROVIDO(29L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ conheceu parcialmente do [recurso/agravo] e, nessa parte, deu-lhe provimento, nos termos do voto @@RELATOR@@.","Conhecido em parte e nessa parte provido", 6211L, "Conhecido em parte e nessa parte provido"),
	CONVERTIDO_EM_DILIGÊNCIA(30L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ converteu o julgamento em diligência @@RELATOR@@.","Convertido em diligência", 6001L, "Convertido em diligência"),
	DECISAO_PELA_EXISTÊNCIA_DE_REPERCUSSAO_GERAL(31L,"","Decisão pela existência de repercussão geral", 6300L, "Decisão pela existência de repercussão geral"),
	DECISAO_PELA_INEXISTÊNCIA_DE_REPERCUSSAO_GERAL(32L,"","Decisão pela inexistência de repercussão geral", 6301L, "Decisão pela inexistência de repercussão geral"),
	DECISAO_PELA_INEXISTÊNCIA_DE_REPERCUSSAO_GERAL_POR_SE_TRATAR_DE_MATÉRIA_INFRACONSTITUCIONAL(33L,"","Decisão pela inexistência de repercussão geral por se tratar de matéria infraconstitucional", 6306L, "Decisão pela inexistência de repercussão geral por se tratar de matéria infraconstitucional"),
	DECISAO_RATIFICADA(34L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ ratificou a decisão @@RELATOR@@.","Decisão Ratificada", 8518L, "Decisão Ratificada"),
	DECISAO_REFERENDADA(35L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ referendou a decisão @@RELATOR@@.","Decisão Referendada", 8517L, "Decisão Referendada"),
	DECLARADA_A_EXTINCAO_DA_PUNIBILIDADE(36L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ declarou extinta a punibilidade, nos termos do voto @@RELATOR@@.","Declarada a extinção da punibilidade", 6213L, "Declarada a extinção da punibilidade"),
	DECLINADA_A_COMPETÊNCIA(37L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ declinou da competência, nos termos do voto @@RELATOR@@.","Declinada a competência", 6215L, "Declinada a competência"),
	DEFERIDO(38L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ deferiu o pedido, nos termos do voto @@RELATOR@@.","Deferido", 6003L, "Deferido"),
	DEFERIDO_EM_PARTE(39L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ deferiu parcialmente o pedido, nos termos do @@RELATOR@@.","Deferido em parte", 6004L, "Deferido em parte"),
	DENEGADA_A_ORDEM(40L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ denegou a ordem, nos termos do voto @@RELATOR@@.","Denegada a ordem", 6217L, "Denegada a ordem"),
	DENEGADA_A_SEGURANCA(41L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ denegou a segurança, nos termos do @@RELATOR@@.","Denegada a segurança", 6218L, "Denegada a segurança"),
	DETERMINADA_A_DEVOLUCAO(42L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ determinou a devolução dos autos, nos termos do voto @@RELATOR@@.","Determinada a devolução", 6012L, "Determinada a devolução"),
	DETERMINADA_A_DEVOLUCAO_PELO_REGIME_DA_REPERCUSSAO_GERAL(43L,"","Determinada a devolução pelo regime da repercussão geral", 6510L, "Determinada a devolução pelo regime da repercussão geral"),
	DETERMINADO_ARQUIVAMENTO(44L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ determinou o arquivamento dos autos, nos termos do voto @@RELATOR@@.","Determinado arquivamento", 6219L, "Determinado arquivamento"),
	EXTINTO_O_PROCESSO(45L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ julgou extinto o processo @@RELATOR@@.","Extinto o processo", 6220L, "Extinto o processo"),
	HOMOLOGADA_A_DESISTÊNCIA(46L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ homologou o pedido de desistência, nos termos do voto @@RELATOR@@.","Homologada a desistência", 6221L, "Homologada a desistência"),
	IMPROCEDENTE(47L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ julgou improcedente _____, nos termos do voto @@RELATOR@@.","Improcedente", 6222L, "Improcedente"),
	INDEFERIDO(48L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ indeferiu o pedido, nos termos do voto @@RELATOR@@.","Indeferido", 6005L, "Indeferido"),
	JULGADO_MÉRITO_DE_TEMA_COM_REPERCUSSAO_GERAL(49L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ apreciando o tema [ ] da repercussão geral, [negou/deu provimento] ao recurso extraordinário, nos termos do voto [do/da] [relator/relatora]. Em seguida, [por maioria]/[por unanimidade], fixou-se a seguinte tese: [ ]. @@RELATOR@@.","Julgado mérito de tema com repercussão geral", 6307L, "Julgado mérito de tema com repercussão geral"),
	LIMINAR_DEFERIDA(50L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ deferiu a liminar, nos termos do voto @@RELATOR@@.","Liminar deferida", 6100L, "Liminar deferida"),
	LIMINAR_DEFERIDA_EM_PARTE(51L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ deferiu parcialmente a liminar, nos termos do voto @@RELATOR@@.","Liminar deferida em parte", 6101L, "Liminar deferida em parte"),
	LIMINAR_INDEFERIDA(52L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ indeferiu a liminar, nos termos do voto @@RELATOR@@.","Liminar indeferida", 6102L, "Liminar indeferida"),
	LIMINAR_REFERENDADA(53L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ referendou a liminar concedida, nos termos do voto @@RELATOR@@.","Liminar referendada", 6105L, "Liminar referendada"),
	LIMINAR_REFERENDADA_EM_PARTE(54L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ referendou, em parte, a liminar concedida, nos termos do voto @@RELATOR@@.","Liminar referendada em parte", 6106L, "Liminar referendada em parte"),
	NAO_CONHECIDO(55L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ não conheceu do/da [ ], nos termos do voto @@RELATOR@@.","Não conhecido(s)", 6224L, "Não conhecido(s)"),
	NAO_PROVIDO(56L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ negou provimento ao recurso, nos termos do voto @@RELATOR@@.","Não provido", 6225L, "Não provido"),
	NEGADO_SEGUIMENTO(57L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ negou seguimento ao [ ], nos termos do voto @@RELATOR@@.","Negado seguimento", 6226L, "Negado seguimento"),
	PREJUDICADO(58L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ julgou prejudicado(a), o(a), [ ], nos termos do voto @@RELATOR@@.","Prejudicado", 6227L, "Prejudicado"),
	PROCEDENTE(59L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ julgou procedente _____, nos termos do voto @@RELATOR@@.","Procedente", 6228L, "Procedente"),
	PROCEDENTE_EM_PARTE(60L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ julgou parcialmente procedente _____, nos termos do voto @RELATOR@@.","Procedente em parte", 6229L, "Procedente em parte"),
	PROVIDO(61L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ deu provimento ao recurso, nos termos do voto @@RELATOR@@.","Provido", 6230L, "Provido"),
	PROVIDO_EM_PARTE(62L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ deu parcial provimento ao recurso, nos termos do @@RELATOR@@.","Provido em parte", 6231L, "Provido em parte"),
	QUESTAO_DE_ORDEM(63L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ resolveu a questão de ordem (...),, nos termos do voto @@RELATOR@@.","Questão de ordem", 6232L, "Questão de ordem"),
	RECEBIDA_A_QUEIXA(64L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ recebeu a queixa-crime, nos termos do voto @@RELATOR@@.","Recebida a queixa", 6233L, "Recebida a queixa"),
	RECEBIDA_A_QUEIXA_EM_PARTE(65L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ recebeu parcialmente a queixa-crime, nos termos do voto @@RELATOR@@.","Recebida a queixa em parte", 6234L, "Recebida a queixa em parte"),
	RECEBIDA_DENÚNCIA(66L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ recebeu a denúncia, nos termos do voto @@RELATOR@@.","Recebida denúncia", 6235L, "Recebida denúncia"),
	RECEBIDA_DENÚNCIA_EM_PARTE(67L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ recebeu parcialmente a denúncia, nos termos do voto @@RELATOR@@.","Recebida denúncia em parte", 6236L, "Recebida denúncia em parte"),
	RECEBIDOS(68L,"","Recebidos", 6237L, "Recebidos"),
	RECEBIDOS_EM_PARTE(69L,"","Recebidos em parte", 6238L, "Recebidos em parte"),
	RECONSIDERO_E_JULGO_PREJUDICADO_O_RECURSO_INTERNO(70L,"","Reconsidero e julgo prejudicado o recurso interno", 6321L, "Reconsidero e julgo prejudicado o recurso interno"),
	REJEITADA_A_DENÚNCIA(71L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ rejeitou a denúncia, nos termos do voto @@RELATOR@@.","Rejeitada a denúncia", 6239L, "Rejeitada a denúncia"),
	REJEITADA_A_QUEIXA(72L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ rejeitou a queixa-crime, nos termos do voto @@RELATOR@@.","Rejeitada a queixa", 6240L, "Rejeitada a queixa"),
	REJEITADA_PROPOSTA_DE_CANCELAMENTO_DE_SV(73L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ rejeitou a proposta de cancelamento da Súmula Vinculante nº @@RELATOR@@.","Rejeitada proposta de cancelamento de SV", 6405L, "Rejeitada proposta de cancelamento de SV"),
	REJEITADA_PROPOSTA_DE_REVISAO_DE_SV(74L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ rejeitou a proposta de revisão da Súmula Vinculante nº @@RELATOR@@.","Rejeitada proposta de revisão de SV", 6403L, "Rejeitada proposta de revisão de SV"),
	REJEITADA_PROPOSTA_DE_EDICAO_DE_SV(75L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ rejeitou a proposta de edição de Súmula Vinculante @@RELATOR@@.","Rejeitada proposta de edição de SV", 6401L, "Rejeitada proposta de edição de SV"),
	REJEITADOS(76L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ rejeitou os embargos de declaração, nos termos do voto @@RELATOR@@.","Rejeitados", 6241L, "Rejeitados"),
	SOBRESTADO(77L,"@@TIPO-COLEGIADO@@@@TIPO-DECISAO@@ sobrestou o julgamento do processo, nos termos do voto @@RELATOR@@.","Sobrestado", 6007L, "Sobrestado"),
	SOBRESTADO_AGUARDANDO_DECISAO_DO_STJ(78L,"","Sobrestado, aguardando decisão do STJ", 7000L, "Sobrestado, aguardando decisão do STJ"),
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
