package br.gov.stf.estf.intimacao.model.dataaccess.hibernate;

import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.intimacao.model.dataaccess.ParteLocalDao;
import br.gov.stf.estf.intimacao.visao.dto.ParteDTO;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
public class ParteLocalDaoHibernate extends GenericHibernateDao<Parte, Long>
		implements ParteLocalDao {

	public ParteLocalDaoHibernate() {
		super(Parte.class);
	}

	public static final long serialVersionUID = 1L;

	@Override
	public List<ParteDTO> listarPartes(Boolean intimavel, Long objetoIncidente)
			throws DaoException {
		String sql = "SELECT DISTINCT j.nom_jurisdicionado, "
				+ "j.seq_jurisdicionado, " + "j.flg_cadastro_ratificado, "
				+ "j.flg_intimacao_pessoal, " + "j.tip_pessoa, "
				+ "ue.sig_usuario_externo, " + "ji.sig_categoria "
				+ "FROM judiciario.vw_jurisdicionado_incidente ji "
				+ "INNER JOIN judiciario.jurisdicionado j "
				+ "ON j.seq_jurisdicionado = ji.seq_jurisdicionado "
				+ "LEFT JOIN corp.usuario_externo ue "
				+ "ON ue.seq_jurisdicionado = j.seq_jurisdicionado ";
		String where = " WHERE ";
		String and = "";
		if (intimavel != null) {
			sql += where;
			sql += and;
			if (intimavel) {
				sql += " (ue.sig_usuario_externo IS NOT NULL AND ji.tip_meio_intimacao = 'E') ";
			} else {
				sql += " (ue.sig_usuario_externo IS NULL OR ji.tip_meio_intimacao <> 'E') ";
			}
			where = "";
			and = " AND ";
		}
		if (objetoIncidente != null) {
			sql += where;
			sql += and;
			sql += " ji.SEQ_OBJETO_INCIDENTE = :objetoIncidente ";
			where = "";
			and = " AND ";
		}
		Session session = retrieveSession();
		SQLQuery query = session.createSQLQuery(sql);
		if (objetoIncidente != null) {
			query.setLong("objetoIncidente", objetoIncidente);
		}
		query.setResultTransformer(new ParteDtoResultTransformer());
		
		List<ParteDTO> partes = trataPartesIntimacaoEletronica(query);
		
		return partes;
	}
	
	@SuppressWarnings("unchecked")
	private List<ParteDTO> trataPartesIntimacaoEletronica(SQLQuery query){
		List<ParteDTO> partes = new ArrayList<ParteDTO>();
		for (ParteDTO parteDTO : (List<ParteDTO>) query.list()) {
			if (!partes.contains(parteDTO)) {
				partes.add(parteDTO);
			}
		}
		return partes;
	}

	@Override
	public List<ParteDTO> listarPartesIntimacaoEletronica() throws DaoException {
		String sql = "SELECT *  "
				+ "FROM   ( "
				+ " SELECT p.SEQ_PESSOA, "
				+ "p.NOM_PESSOA, "
				+ "p.TIP_MEIO_INTIMACAO, "
				+ "COUNT(c.SEQ_COMUNICACAO) QTD_COMUNICACOES "
				+ "FROM   CORPORATIVO.PESSOA p "
				+ "LEFT OUTER JOIN JUDICIARIO.COMUNICACAO c ON "
				+ "(c.SEQ_PESSOA_DESTINATARIA = p.SEQ_PESSOA) "
				+ "LEFT OUTER JOIN JUDICIARIO.MODELO_COMUNICACAO mc ON "
				+ "(mc.SEQ_MODELO_COMUNICACAO = c.SEQ_MODELO_COMUNICACAO) "
				+ "LEFT OUTER JOIN JUDICIARIO.TIPO_COMUNICACAO tc ON "
				+ "(tc.SEQ_TIPO_COMUNICACAO = mc.SEQ_TIPO_COMUNICACAO) "
				+ "WHERE  tc.DSC_TIPO_COMUNICACAO = 'Intimação Eletrônica' "
				+ "OR "
				+ "(tc.DSC_TIPO_COMUNICACAO = 'Notificação' "
				+ "and (mc.DSC_MODELO = 'Notificação para download de peça' OR mc.DSC_MODELO = 'Notificação de Baixa dos Autos' OR mc.DSC_MODELO = 'Notificação de Vista' OR mc.DSC_MODELO = 'Vista')) "
				+ "GROUP BY p.SEQ_PESSOA, " + "p.NOM_PESSOA, "
				+ "p.TIP_MEIO_INTIMACAO ) "
				+ "WHERE  TIP_MEIO_INTIMACAO = 'E' OR QTD_COMUNICACOES > 0 ORDER BY NOM_PESSOA";

		Session session = retrieveSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setResultTransformer(new ParteDtoComboResultTransformer());
		List<ParteDTO> partes = query.list();
		return partes;
	}

	private class ParteDtoResultTransformer extends
			ParteProcessoResultTransformerAbstract {

		/**
         *
         */
		private static final long serialVersionUID = 1L;

		@Override
		public List transformList(List arg0) {
			return arg0;
		}

		@Override
		public Object transformTuple(Object[] rowData, String[] aliases) {
			ParteDTO parteDto = new ParteDTO();

			parteDto.setNomeJurisdicionado(getString(rowData[0]));
			parteDto.setSeqJurisdicionado(getLong(rowData[1]));
			parteDto.setId(getLong(rowData[1]));
			if (rowData[2] != null) {
				if (rowData[2].equals("S")) {
					parteDto.setCadastroRatificado("Sim");
				} else {
					parteDto.setCadastroRatificado("Não");
				}
			}

			if (rowData[3] != null) {
				if (rowData[3].equals("S")) {
					parteDto.setPrerrogativaIntimacao("Sim");
				} else {
					parteDto.setPrerrogativaIntimacao("Não");
				}
			}
			if (getString(rowData[4]).equals("PF")) {
				parteDto.setTipoPessoa("Pessoa Física");
			}
			if (getString(rowData[4]).equals("PJ")) {
				parteDto.setTipoPessoa("Pessoa Júridica");
			}
			if (getString(rowData[4]).equals("PP")) {
				parteDto.setTipoPessoa("Pessoa Pública");
			}
			parteDto.setLogin(getString(rowData[5]));
			parteDto.setQualificacao(getString(rowData[6]));

			return parteDto;
		}
	}

	private class ParteDtoComboResultTransformer extends
			ParteProcessoResultTransformerAbstract {

		/**
         *
         */
		private static final long serialVersionUID = 1L;

		@Override
		public List transformList(List arg0) {
			return arg0;
		}

		@Override
		public Object transformTuple(Object[] rowData, String[] aliases) {
			ParteDTO parteDto = new ParteDTO();
			parteDto.setSeqJurisdicionado(getLong(rowData[0]));
			parteDto.setNomeJurisdicionado(getString(rowData[1]));

			return parteDto;
		}
	}
}