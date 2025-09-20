package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import br.gov.stf.estf.entidade.julgamento.TipoSituacaoProcessoSessao;

public final class SubQuerySituacaoJulgamento {

	private SubQuerySituacaoJulgamento() {

	}

	public static String buildClausulaSituacaoJulgado(String oiAlias) {
		StringBuffer subQueryJulgado = new StringBuffer();

		subQueryJulgado.append(" (EXISTS ( ");
		subQueryJulgado.append(" SELECT 1 FROM ( ");
		subQueryJulgado.append(" SELECT jp.SEQ_OBJETO_INCIDENTE FROM JULGAMENTO.JULGAMENTO_PROCESSO jp ");
		subQueryJulgado.append(" WHERE jp.TIP_SITUACAO_PROC_SESSAO = '" + TipoSituacaoProcessoSessao.JULGADO.getSigla() + "' ");

		subQueryJulgado.append(" UNION ");

		subQueryJulgado.append(" SELECT plj.SEQ_OBJETO_INCIDENTE_CANDIDATO FROM ");
		subQueryJulgado.append(" JULGAMENTO.PROCESSO_LISTA_JULG plj ");
		subQueryJulgado.append(" INNER JOIN JULGAMENTO.LISTA_JULGAMENTO lj ");
		subQueryJulgado.append(" ON plj.SEQ_LISTA_JULGAMENTO = lj.SEQ_LISTA_JULGAMENTO ");
		subQueryJulgado.append(" WHERE lj.FLG_JULGADO = 'S' ");
		subQueryJulgado.append(" AND ASCII(LOWER(SUBSTR(TRIM(lj.dsc_lista_processo), -1))) NOT BETWEEN ASCII ('a') AND ASCII ('z') ");
		subQueryJulgado.append(" AND NOT EXISTS (SELECT NULL FROM julgamento.julgamento_processo jp WHERE jp.seq_julgamento_processo = plj.seq_julgamento_processo AND jp.tip_situacao_proc_sessao = 'D') ");

		subQueryJulgado.append(" UNION ");

		subQueryJulgado.append(" SELECT ap.SEQ_OBJETO_INCIDENTE FROM ");
		subQueryJulgado.append(" STF.ANDAMENTOS an ");
		subQueryJulgado.append(" INNER JOIN STF.GRUPO_ANDAMENTO ga ");
		subQueryJulgado.append(" ON ga.SEQ_GRUPO_ANDAMENTO = an.SEQ_GRUPO_ANDAMENTO ");
		subQueryJulgado.append(" INNER JOIN STF.ANDAMENTO_PROCESSOS ap ");
		subQueryJulgado.append(" ON ap.COD_ANDAMENTO = an.COD_ANDAMENTO ");
		subQueryJulgado.append(" WHERE ga.SEQ_GRUPO_ANDAMENTO IN (76, 27) ");
		subQueryJulgado.append(" AND ap.FLG_LANCAMENTO_INDEVIDO = 'N' ");

		subQueryJulgado.append(" ) julgados ");
		subQueryJulgado.append(" WHERE oi.SEQ_OBJETO_INCIDENTE = julgados.SEQ_OBJETO_INCIDENTE ");

		subQueryJulgado.append(" ) ) ");

		return subQueryJulgado.toString();
	}

}
