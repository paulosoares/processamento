package br.gov.stf.estf.entidade.documento;

import java.util.ArrayList;
import java.util.List;

import br.gov.stf.framework.util.GenericEnum;

public class TipoTexto extends GenericEnum<Long, TipoTexto> implements Comparable<TipoTexto> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1571492520518307380L;
	public static final long CODIGO_DESPACHO = 60L;
	public static final long CODIGO_DECISAO_MONOCRATICA = 65L;
	public static final long CODIGO_ACORDAO = 70L;
	public static final long CODIGO_EMENTA = 80L;
	public static final long CODIGO_EMENTA_SOBRE_REPERCUSSAO_GERAL = 87L;
	public static final long CODIGO_RELATORIO = 100L;
	public static final long CODIGO_VOTO = 200L;
	public static final long CODIGO_MINUTA = 525L;
	public static final long CODIGO_MEMORIA_DE_CASO = 527L;
	public static final long CODIGO_VOTO_VISTA = 223L;
	public static final long CODIGO_VOTO_VOGAL = 524L;
	public static final long CODIGO_COMPLEMENTO_AO_VOTO = 528L;
	public static final long CODIGO_ANTECIPACAO_AO_VOTO = 210L;
	
	public static final TipoTexto ABERTURA = new TipoTexto(1, "Abertura", null);
	public static final TipoTexto ADIAMENTO = new TipoTexto(2, "Adiamento", null);
	public static final TipoTexto AGRADECIMENTO = new TipoTexto(3, "Agradecimento", null);
	public static final TipoTexto ANULACAO_DE_JULGAMENTO = new TipoTexto(4, "Anulação de Julgamento", null);
	public static final TipoTexto COMUNICACAO = new TipoTexto(5, "Comunicação", null);
	public static final TipoTexto DEBATE = new TipoTexto(6, "Debate", null);
	public static final TipoTexto DILIGENCIA = new TipoTexto(7, "Diligência", null);
	public static final TipoTexto ELEICAO = new TipoTexto(8, "Eleição", null);
	public static final TipoTexto ENCERRAMENTO = new TipoTexto(9, "Encerramento", null);
	public static final TipoTexto ESCLARECIMENTO_SEM_MATERIA_DE_FATO = new TipoTexto(10,
			"Esclarecimento s/ Matéria de Fato (Advogado)", null);
	public static final TipoTexto EXPLICACAO = new TipoTexto(11, "Explicação", null);
	public static final TipoTexto HOMENAGEM = new TipoTexto(12, "Homenagem", null);
	public static final TipoTexto PARECER = new TipoTexto(13, "Parecer", null);
	public static final TipoTexto PARECER_CONFIRMACAO_PARECER = new TipoTexto(14, "Parecer - Confirmação de Parecer", null);
	public static final TipoTexto PARECER_RETIFICACAO_PARECER = new TipoTexto(15, "Parecer- Retificação de Parecer", null);
	public static final TipoTexto PARECER_ADITAMENTO_PARECER = new TipoTexto(16, "Parecer - Aditamento ao Parecer", null);
	public static final TipoTexto PRELIMINAR = new TipoTexto(17, "Preliminar", null);
	public static final TipoTexto PROPOSTA_REMESSA_PLENO = new TipoTexto(18, "Proposta de Remessa ao Pleno", null);
	public static final TipoTexto PROPOSTA_REMESSA_TURMA = new TipoTexto(19, "Proposta de Remessa à Turma", null);
	public static final TipoTexto QUESTAO_DE_ORDEM = new TipoTexto(20, "Questão de Ordem", null);
	public static final TipoTexto RENOVAXAO_DE_JULGAMENTO = new TipoTexto(21, "Renovação de Julgamento", null);
	public static final TipoTexto RETIFICACAO_DE_DECISAO = new TipoTexto(22, "Retificação de Decisão", null);
	public static final TipoTexto SOLENE = new TipoTexto(23, "Solene", null);
	public static final TipoTexto VISITA = new TipoTexto(24, "Vista", null);
	public static final TipoTexto NOTAS_PARA_O_VOTO = new TipoTexto(25, "Notas para o Voto", null);
	public static final TipoTexto ADITAMENTO_A_QUESTAO_DE_ORDEM = new TipoTexto(26, "Aditamento à Questão de Ordem", null);
	public static final TipoTexto PROPOSTA_DE_VISTA = new TipoTexto(27, "Proposta de Vista", null);
	public static final TipoTexto ANTECIPACAO_AO_PARECER = new TipoTexto(28, "Antecipação ao Parecer", null);
	public static final TipoTexto IMPEDIMENTO = new TipoTexto(29, "Impedimento", null);
	public static final TipoTexto REMETIDO_AO_PLENO = new TipoTexto(30, "Remetido ao Pleno", null);
	public static final TipoTexto RETIFICACAO_DE_PRELIMINAR = new TipoTexto(31, "Retificação de Preliminar", null);
	public static final TipoTexto REMETIDO_A_TURMA = new TipoTexto(32, "Remetido à Turma", null);
	public static final TipoTexto OFICIO = new TipoTexto(40, "Ofício", null);
	public static final TipoTexto DECISAO = new TipoTexto(50, "Decisão de Julgamento", null);
	public static final TipoTexto DESPACHO = new TipoTexto(CODIGO_DESPACHO, "Despacho", null);
	public static final TipoTexto DECISAO_MONOCRATICA = new TipoTexto(CODIGO_DECISAO_MONOCRATICA, "Decisão Monocrática", null);
	public static final TipoTexto ACORDAO = new TipoTexto(CODIGO_ACORDAO, "Acórdão", null);
	public static final TipoTexto EMENTA = new TipoTexto(CODIGO_EMENTA, "Ementa", null);
	public static final TipoTexto RELATORIO = new TipoTexto(CODIGO_RELATORIO, "Relatório", null);
	public static final TipoTexto DECISAO_SOBRE_REPERCURSAO_GERAL = new TipoTexto(55, "Decisão sobre Repercussão Geral", null);
	public static final TipoTexto EMENTA_RELATORIO_VOTO = new TipoTexto(82, "Ementa, Relatório e Voto", null);
	public static final TipoTexto EMENTA_RELATORIO = new TipoTexto(83, "Ementa, Relatório", null);
	public static final TipoTexto EMENTA_RELATORIO_VOTO_SEM_PRELIMINAR = new TipoTexto(84,
			"Ementa,Relatório e Voto s/ Preliminar", null);
	public static final TipoTexto EMENTA_RELATORIO_VOTO_PRELIMINAR = new TipoTexto(85, "Ementa, Relatório e Voto Preliminar",
			null);
	public static final TipoTexto EMENTA_SOBRE_REPERCURSAO_GERAL = new TipoTexto(87, "Ementa sobre Repercussão Geral", null);
	public static final TipoTexto EMENTA_E_ACORDAO = new TipoTexto(86, "Ementa e Acórdão", null);
	public static final TipoTexto RETIRADO_DE_PAUTA = new TipoTexto(90, "Retirado de Pauta", null);
	public static final TipoTexto RETIRADO_DE_MESA = new TipoTexto(91, "Retirado de Mesa", null);
	public static final TipoTexto ANTECIPACAO_AO_RELATORIO = new TipoTexto(101, "Antecipação ao Relatório", null);
	public static final TipoTexto ADITAMENTO_AO_RELATORIO = new TipoTexto(102, "Aditamento ao Relatório", null);
	public static final TipoTexto RELATORIO_E_VOTO = new TipoTexto(105, "Relatório e Voto", null);
	public static final TipoTexto RELATORIO_VOTO_SEM_PRELIMINAR = new TipoTexto(106, "Relatório, Voto s/ Preliminar", null);
	public static final TipoTexto RELATORIO_VOTO_PRELIMINAR = new TipoTexto(107, "Relatório, Voto Preliminar", null);
	public static final TipoTexto RELATORIO_ANTECIPACAO_AO_VOTO = new TipoTexto(108, "Relatório, Antecipação ao Voto", null);
	public static final TipoTexto RELATORIO_VOTO_SEM_QUESTAO_DE_ORDEM = new TipoTexto(109, "Relatório, Voto s/ Questão de Ordem",
			null);
	public static final TipoTexto RELATORIO_EXPLICACAO = new TipoTexto(110, "Relatório, Explicação", null);
	public static final TipoTexto RELATORIO_PROPOSTA_DE_REMESSA_AO_PLENO = new TipoTexto(111,
			"Relatório, Proposta de Remessa ao Pleno", null);
	public static final TipoTexto RELATORIO_PROPOSTA_DE_REMESSA_A_TURMA = new TipoTexto(112,
			"Relatório, Proposta de Remessa à Turma", null);
	public static final TipoTexto RELATORIO_VOTO_SEM_DILIGENCIA = new TipoTexto(113, "Relatório, Voto s/ Diligência", null);
	public static final TipoTexto RELATORIO_SEM_QUESTAO_DE_ORDEM = new TipoTexto(114, "Relatório s/ Questão de Ordem", null);
	public static final TipoTexto REFERENDO_RELATORIO = new TipoTexto(115, "Referendo Relatório", null);
	public static final TipoTexto RELATORIO_TURMA = new TipoTexto(116, "Relatório - Turma", null);
	public static final TipoTexto VOTO = new TipoTexto(CODIGO_VOTO, "Voto", null);
	public static final TipoTexto VOTO_TURMA = new TipoTexto(201, "Voto - Turma", null);
	public static final TipoTexto ANTECIPACAO_AO_VOTO = new TipoTexto(210, "Antecipação ao Voto", null);
	public static final TipoTexto ADITAMENTO_AO_VOTO = new TipoTexto(211, "Aditamento ao Voto", null);
	public static final TipoTexto VOTO_PRELIMINAR = new TipoTexto(212, "Voto Preliminar", null);
	public static final TipoTexto VOTO_SEM_PRELIMINAR = new TipoTexto(213, "Voto s/ Preliminar", null);
	public static final TipoTexto VOTO_SEM_DILIGENCIA = new TipoTexto(214, "Voto s/ Diligência", null);
	public static final TipoTexto VOTO_SEM_QUESTAO_DE_ORDEM = new TipoTexto(215, "Voto s/ Questão de Ordem", null);
	public static final TipoTexto VOTO_DESEMPATE = new TipoTexto(216, "Voto Desempate", null);
	public static final TipoTexto CONFIRMACAO_DE_VOTO = new TipoTexto(217, "Confirmação de Voto", null);
	public static final TipoTexto RETIFICACAO_DE_VOTO = new TipoTexto(218, "Retificação de Voto", null);
	public static final TipoTexto INCIDENCIAS_AO_VOTO = new TipoTexto(219, "Incidências ao Voto", null);
	public static final TipoTexto VOTO_SEM_MATERIA_DE_FATO = new TipoTexto(220, "Voto s/ Matéria de Fato", null);
	public static final TipoTexto EXTRATO_DE_ATA = new TipoTexto(221, "Extrato de Ata", null);
	public static final TipoTexto RETIFICACAO_DE_ATA = new TipoTexto(222, "Retificação de Ata", null);
	public static final TipoTexto VOTO_VISTA = new TipoTexto(223, "Voto Vista", null);
	public static final TipoTexto QUESTAO_PRELIMINAR = new TipoTexto(224, "Questão Preliminar", null);
	public static final TipoTexto VOTO_SEM_MERITO = new TipoTexto(225, "Voto s/ Mérito", null);
	public static final TipoTexto ESCLARECIMENTO = new TipoTexto(226, "Esclarecimento", null);
	public static final TipoTexto PROPOSTA_DE_DILIGENCIA = new TipoTexto(227, "Proposta de Diligência", null);
	public static final TipoTexto REQUERIMENTO = new TipoTexto(228, "Requerimento", null);
	public static final TipoTexto VOTO_SEM_PRELIMINAR_DE_CONHECIMENTO = new TipoTexto(229, "Voto s/ Preliminar de Conhecimento",
			null);
	public static final TipoTexto HOMOLOGACAO_DE_DESISTENCIA = new TipoTexto(230, "Homologação de Desistência", null);
	public static final TipoTexto PROPOSTA = new TipoTexto(231, "Proposta", null);
	public static final TipoTexto VOTO_SEM_CONHECIMENTO = new TipoTexto(232, "Voto s/ Conhecimento", null);
	public static final TipoTexto VOTO_SEM_ITEM_1 = new TipoTexto(233, "Voto s/ item I", null);
	public static final TipoTexto VOTO_SEM_ITEM_2 = new TipoTexto(234, "Voto s/ item II", null);
	public static final TipoTexto VOTO_SEM_ITEM_3 = new TipoTexto(235, "Voto s/ item III", null);
	public static final TipoTexto VOTO_SEM_ITEM_4 = new TipoTexto(236, "Voto s/ item IV", null);
	public static final TipoTexto VOTO_SEM_ITEM_5 = new TipoTexto(237, "Voto s/ item V", null);
	public static final TipoTexto VOTO_SEM_ITEM_6 = new TipoTexto(238, "Voto s/ item VI", null);
	public static final TipoTexto VOTO_SEM_ITEM_7 = new TipoTexto(239, "Voto s/ item VII", null);
	public static final TipoTexto VOTO_SEM_ITEM_8 = new TipoTexto(240, "Voto s/ item VIII", null);
	public static final TipoTexto VOTO_SEM_PROPOSTA_REMESSA_PLENO = new TipoTexto(241, "Voto s/ Proposta de Remessao ao Pleno",
			null);
	public static final TipoTexto VOTO_SEM_PROPOSTA = new TipoTexto(242, "Voto s/ Proposta", null);
	public static final TipoTexto QUESTAO_PREJUDICIAL = new TipoTexto(243, "Questão Prejudicial", null);
	public static final TipoTexto VOTO_SEM_QUESTAO_PREJUDICIAL = new TipoTexto(244, "Voto s/ Questão Prejudicial", null);
	public static final TipoTexto RENOVACAO_DE_DILIGENCIA = new TipoTexto(245, "Renovação de Diligência", null);
	public static final TipoTexto VOTO_SEM_ADITAMENTO = new TipoTexto(246, "Voto s/ Adiamento", null);
	public static final TipoTexto VOTO_SEM_EXPLICACAO = new TipoTexto(247, "Voto s/ Explicação", EspecieTexto.valueOf(3L));
	public static final TipoTexto VOTO_SOBRE_MODULACAO = new TipoTexto(248, "Voto s/ modulação", EspecieTexto.valueOf(3L));
	public static final TipoTexto ADITAMENTO_AO_VOTO_VISTA = new TipoTexto(250, "Aditamento ao Voto Vista", null);
	public static final TipoTexto ADITAMENTO_AO_VOTO_SEM_PRELIMINAR = new TipoTexto(251, "Aditamento ao Voto s/ Preliminar", null);
	public static final TipoTexto VOTO_SEM_SEGUNDA_QUESTAO_DE_ORDEM = new TipoTexto(252, "Voto s/ 2ª Questão de Ordem", null);
	public static final TipoTexto VOTO_SEM_3_QUESTAO_ORDEM = new TipoTexto(253, "Voto s/3ª Questão de Ordem", null);
	public static final TipoTexto VOTO_SEM_4_QUESTAO_ORDEM = new TipoTexto(254, "Voto s/4ª Questão de Ordem", null);
	public static final TipoTexto AUDIENCIA_PUBLICA = new TipoTexto(255, "Audiência Pública", null);
	public static final TipoTexto ESCLARECIMENTO_AUDIENCIA_PUBLICA = new TipoTexto(256, "Esclarecimento - Audiência Pública",
			null);
	public static final TipoTexto VOTO_SEM_ITEM_9 = new TipoTexto(257, "Voto s/ item IX", EspecieTexto.valueOf(3L));
	public static final TipoTexto VOTO_SEM_ITEM_10 = new TipoTexto(258, "Voto s/ item X", EspecieTexto.valueOf(3L));
	public static final TipoTexto VOTO_SEM_ITEM_11 = new TipoTexto(259, "Voto s/ item XI", EspecieTexto.valueOf(3L));
	public static final TipoTexto VOTO_SEM_ITEM_12 = new TipoTexto(260, "Voto s/ item XII", EspecieTexto.valueOf(3L));
	public static final TipoTexto VOTO_SEM_ITEM_13 = new TipoTexto(261, "Voto s/ item XIII", EspecieTexto.valueOf(3L));
	public static final TipoTexto VOTO_SEM_ITEM_14 = new TipoTexto(262, "Voto s/ item XIV", EspecieTexto.valueOf(3L));
	public static final TipoTexto VOTO_SEM_ITEM_15 = new TipoTexto(263, "Voto s/ item XV", EspecieTexto.valueOf(3L));
	public static final TipoTexto MANIFESTACAO_SOBRE_REPERCUSAO_GERAL = new TipoTexto(270,
			"Manifestação sobre a Repercussão Geral", null);
	public static final TipoTexto VOTO_REVISOR = new TipoTexto(271, "Voto do(a) Revisor(a)", null);
	public static final TipoTexto REFERENDO_VOTO = new TipoTexto(273, "Referendo Voto", null);
	public static final TipoTexto VOTO_SEGUNDA_PRELIMINAR = new TipoTexto(274, "Voto s/ 2ª Preliminar", null);
	public static final TipoTexto VOTO_TERCEIRA_PRELIMINAR = new TipoTexto(275, "Voto s/ 3ª Preliminar", null);
	public static final TipoTexto VOTO_QUARTA_PRELIMINAR = new TipoTexto(276, "Voto s/ 4ª Preliminar", null);
	public static final TipoTexto VOTO_QUINTA_PRELIMINAR = new TipoTexto(277, "Voto s/ 5ª Preliminar", null);
	public static final TipoTexto VOTO_SEXTA_PRELIMINAR = new TipoTexto(278, "Voto s/ 6ª Preliminar", null);
	public static final TipoTexto VOTO_SETIMA_PRELIMINAR = new TipoTexto(279, "Voto s/ 7ª Preliminar", null);
	public static final TipoTexto VOTO_OITAVA_PRELIMINAR = new TipoTexto(280, "Voto s/ 8ª Preliminar", null);
	public static final TipoTexto VOTO_NOVA_PRELIMINAR = new TipoTexto(281, "Voto s/ 9ª Preliminar", null);
	public static final TipoTexto VOTO_DECIMA_PRELIMINAR = new TipoTexto(282, "Voto s/ 10ª Preliminar", null);
	public static final TipoTexto VOTO_DECIMA_PRIMEIRA_PRELIMINAR = new TipoTexto(283, "Voto s/ 11ª Preliminar", null);
	public static final TipoTexto VOTO_DECIMA_SEGUNDA_PRELIMINAR = new TipoTexto(284, "Voto s/ 12ª Preliminar", null);
	public static final TipoTexto VOTO_DECIMA_TERCEIRA_PRELIMINAR = new TipoTexto(285, "Voto s/ 13ª Preliminar", null);
	public static final TipoTexto VOTO_DECIMA_QUARTA_PRELIMINAR = new TipoTexto(286, "Voto s/ 14ª Preliminar", null);
	public static final TipoTexto VOTO_DECIMA_QUINTA_PRELIMINAR = new TipoTexto(287, "Voto s/ 15ª Preliminar", null);
	public static final TipoTexto VOTO_DECIMA_SEXTA_PRELIMINAR = new TipoTexto(288, "Voto s/ 16ª Preliminar", null);
	public static final TipoTexto VOTO_DECIMA_SETIMA_PRELIMINAR = new TipoTexto(289, "Voto s/ 17ª Preliminar", null);
	public static final TipoTexto VOTO_DECIMA_OITAVA_PRELIMINAR = new TipoTexto(290, "Voto s/ 18ª Preliminar", null);
	public static final TipoTexto VOTO_DECIMA_NONA_PRELIMINAR = new TipoTexto(291, "Voto s/ 19ª Preliminar", null);
	public static final TipoTexto VOTO_VIGESIMA_PRELIMINAR = new TipoTexto(292, "Voto s/ 20ª Preliminar", null);
	public static final TipoTexto VOTO_SOBRE_DOSIMETRIA = new TipoTexto(295, "Voto s/ Dosimetria", EspecieTexto.valueOf(3L));
	public static final TipoTexto VOTO_SOBRE_PERDA_DE_MANDATO = new TipoTexto(296, "Voto s/ Perda de Mandato", null);
	public static final TipoTexto VOTO_SOBRE_CONTINUIDADE_DELITIVA = new TipoTexto(297, "Voto s/ Continuidade Delitiva", null);
	public static final TipoTexto VOTO_SOBRE_ART_387_IV_DO_CPP = new TipoTexto(298, "Voto s/ art. 387, IV, do CPP", null);
	public static final TipoTexto REVISAO_DE_APARTES = new TipoTexto(299, "Revisão de Apartes", null);
	public static final TipoTexto SOBRESTADO = new TipoTexto(300, "Sobrestado", null);
	public static final TipoTexto ADITAMENTO_A_PROPOSTA = new TipoTexto(301, "Aditamento à proposta", null);
	public static final TipoTexto ADITAMENTO_A_DECISAO = new TipoTexto(302, "Aditamento à Decisão", null);
	public static final TipoTexto REFERENDO = new TipoTexto(303, "Referendo", null);
	public static final TipoTexto DEBATE_2 = new TipoTexto(304, "Debate II", null);
	public static final TipoTexto DEBTATE_3 = new TipoTexto(305, "Debate III", null);
	public static final TipoTexto DEBATE_4 = new TipoTexto(306, "Debate IV", null);
	public static final TipoTexto ADITAMENTO_A_PRELIMINAR = new TipoTexto(307, "Aditamento à preliminar", null);
	public static final TipoTexto QUESTAO_ORDEM_2 = new TipoTexto(308, "2ª Questão de Ordem", null);
	public static final TipoTexto QUESTAO_ORDEM_3 = new TipoTexto(309, "3ª Questão de Ordem", null);
	public static final TipoTexto QUESTAO_ORDEM_4 = new TipoTexto(310, "4ª Questão de Ordem", null);
	public static final TipoTexto RETIFICACAO_DE_PREGAO = new TipoTexto(311, "Retificação de Pregão", null);
	public static final TipoTexto TEXTO_DE_ABERTURA_CERTIDAO_JULGAMENTO = new TipoTexto(401,
			"Texto de Abertura da Certidão de Julgamento", EspecieTexto.valueOf(1L));
	public static final TipoTexto TERMO_DE_JUNTADA_DE_CERTIDAO_DE_JULGAMENTO = new TipoTexto(402,
			"Termo de Juntada da Certidão de Julgamento", EspecieTexto.valueOf(2L));
	public static final TipoTexto CERTIDAO_DE_DATA = new TipoTexto(403, "Certidão de Data", EspecieTexto.valueOf(1L));
	public static final TipoTexto CERTIDAO_DE_PUBLICACAO = new TipoTexto(404, "Certidão de Publicação", EspecieTexto.valueOf(1L));
	public static final TipoTexto EDITAL_PROPOSTA_SUMULA_VINCULANTE = new TipoTexto(405,
			"Edital de Proposta de Súmula Vinculante", null);
	public static final TipoTexto TERMO_JUNTADA_ACORDAO = new TipoTexto(406, "Termo de Juntada do Acórdão", EspecieTexto
			.valueOf(2L));
	public static final TipoTexto DEGRAVACAO = new TipoTexto(500, "Degravação", null);
	public static final TipoTexto DEPOIMENTO = new TipoTexto(501, "Depoimento", null);
	public static final TipoTexto ENTREVISTA = new TipoTexto(502, "Entrevista", null);
	public static final TipoTexto CANCELADO_PREGAO = new TipoTexto(503, "Cancelado o pregão", null);
	public static final TipoTexto CONCLUSAO = new TipoTexto(504, "Conclusão", null);
	public static final TipoTexto CONTINUACAO_RELATORIO = new TipoTexto(505, "Continuação do relatório", null);
	public static final TipoTexto CONTINUACAO_VOTO = new TipoTexto(506, "Continuação do voto", null);
	public static final TipoTexto ESCRUTINADOR = new TipoTexto(507, "Escrutinador", null);
	public static final TipoTexto LEITURA_ATA = new TipoTexto(508, "Leitura de ata", null);
	public static final TipoTexto LEITURA_TEXTOS = new TipoTexto(509, "Leitura de textos", null);
	public static final TipoTexto MATERIA_FATO = new TipoTexto(510, "Matéria de fato", null);
	public static final TipoTexto OBSERVAÇÃO = new TipoTexto(511, "Observação", null);
	public static final TipoTexto PREGAO = new TipoTexto(512, "Pregão", null);
	public static final TipoTexto REABERTURA = new TipoTexto(513, "Reabertura", null);
	public static final TipoTexto RESULTADO = new TipoTexto(514, "Resultado", null);
	public static final TipoTexto RESULTADO_PARCIAL = new TipoTexto(515, "Resultado parcial", null);
	public static final TipoTexto SUSPENSA = new TipoTexto(516, "Suspensa", null);
	public static final TipoTexto SUSTENTACAO_ORAL = new TipoTexto(517, "Sustentação oral", null);
	public static final TipoTexto SUSPENSAO_JULGAMENTO = new TipoTexto(518, "Suspensão de julgamento", null);
	public static final TipoTexto VISTA_EM_MESA = new TipoTexto(519, "Vista em mesa", null);
	public static final TipoTexto NOMEACAO = new TipoTexto(520, "Nomeação", null);
	public static final TipoTexto APARTE = new TipoTexto(521, "Aparte", null);
	public static final TipoTexto SUSTENTACAO_ESCRITA = new TipoTexto(522, "Sustentação escrita", null);
	public static final TipoTexto MANIFESTACAO_SOBRE_PROPOSTA_SUMULA_VINCULANTE = new TipoTexto(523, "Manifestação sobre proposta de súmula vinculante", null);
	public static final TipoTexto VOTO_VOGAL = new TipoTexto(524, "Voto Vogal", null);
	public static final TipoTexto MINUTA = new TipoTexto(525, "Minuta", null);
	public static final TipoTexto TERMO_DE_ABERTURA_CERTIDAO_JULGAMENTO_VIRTUAL = new TipoTexto(526, "Termo de Abertura da Certidão de Julgamento Virtual", EspecieTexto.valueOf(1L));
	public static final TipoTexto COMPLEMENTO_AO_VOTO = new TipoTexto(CODIGO_COMPLEMENTO_AO_VOTO, "Complemento ao voto", null);
	
	public static final TipoTexto MINISTRO_VENCIDO = new TipoTexto(999, "Ministro vencido", null);
	public static final TipoTexto MEMORIA_DE_CASO = new TipoTexto(527, "Memória de Caso", null);
	
	
	private final String descricao;
	private final EspecieTexto especieTexto;

	private TipoTexto(Long codigo) {
		this(codigo, "Tipo Texto " + codigo, null);
	}

	private TipoTexto(long codigo, String descricao, EspecieTexto especieTexto) {
		super(codigo);
		this.descricao = descricao;
		this.especieTexto = especieTexto;
	}

	public String getDescricao() {
		return descricao;
	}

	public EspecieTexto getEspecieTexto() {
		return especieTexto;
	}

	public static TipoTexto valueOf(Long codigo) {
		return valueOf(TipoTexto.class, codigo);
	}

	public static TipoTexto[] values() {
		return values(new TipoTexto[0], TipoTexto.class);
	}

	public static List<TipoTexto> pesquisarTipoTexto(Long... codigosTipoTexto) {
		List<TipoTexto> tipos = new ArrayList<TipoTexto>();
		for (Long codigo : codigosTipoTexto) {
			tipos.add(valueOf(codigo));
		}
		return tipos;
	}

	@Override
	public int compareTo(TipoTexto o) {
		return this.getDescricao().compareTo(o.getDescricao());
	}
	
	public static TipoTexto getTipoTextoVazio(){
		return new TipoTexto(0, "", null);
	}

}
