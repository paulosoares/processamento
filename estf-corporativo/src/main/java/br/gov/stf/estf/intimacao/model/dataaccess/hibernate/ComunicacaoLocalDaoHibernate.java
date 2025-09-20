package br.gov.stf.estf.intimacao.model.dataaccess.hibernate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.processostf.TipoIncidentePreferencia;
import br.gov.stf.estf.intimacao.model.dataaccess.ComunicacaoLocalDao;
import br.gov.stf.estf.intimacao.model.vo.TipoRecebimentoComunicacaoEnum;
import br.gov.stf.estf.intimacao.visao.dto.ComunicacaoExternaDTO;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.model.entity.BaseEntity;

@Repository
public class ComunicacaoLocalDaoHibernate extends GenericHibernateDao implements
		ComunicacaoLocalDao {

	public static final long serialVersionUID = 1L;

	private static final Log LOG = LogFactory
			.getLog(ComunicacaoLocalDaoHibernate.class);

	@SuppressWarnings("unchecked")
	public ComunicacaoLocalDaoHibernate() {
		super(BaseEntity.class);
	}

	@Override
	public List<ComunicacaoExternaDTO> buscar(String idParte,
			TipoRecebimentoComunicacaoEnum tipoRecebimentoComunicacaoEnum,
			String descricaoTipoComunicacao, String descricaoModelo,
			Date periodoEnvioInicio, Date periodoEnvioFim, Long idProcesso,
			Long idPreferemcia) throws DaoException {

		Session session = retrieveSession();

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT DISTINCT ");

		sql.append(" c.SEQ_COMUNICACAO, ");
		sql.append(" mc.DSC_MODELO, ");

		sql.append(" c.DAT_COMUNICACAO_ENVIADA, ");
		sql.append(" c.DAT_COMUNICACAO_RECEBIDA, ");
		sql.append(" c.SIG_USUARIO_RECEBIMENTO, ");
		sql.append(" pe.NOM_PESSOA, ");
		sql.append(" (select distinct P.Nom_Pessoa from CORPORATIVO.PESSOA p where p.SEQ_PESSOA = u.SEQ_PESSOA) as parte_intimada, ");
		

		sql.append(" trc.DSC_TIPO_RECEBIMENTO, ");
		sql.append(" fc.SEQ_TIPO_FASE_COMUNICACAO, ");
		sql.append(" tc.DSC_TIPO_COMUNICACAO, ");
		sql.append(" coi.SEQ_OBJETO_INCIDENTE ");

		sql.append(" from JUDICIARIO.COMUNICACAO c  ");
		sql.append(" left outer join JUDICIARIO.COMUNICACAO_OBJETO_INCIDENTE coi on (c.SEQ_COMUNICACAO = coi.SEQ_COMUNICACAO) ");
		sql.append(" inner join JUDICIARIO.MODELO_COMUNICACAO mc on (c.SEQ_MODELO_COMUNICACAO = mc.SEQ_MODELO_COMUNICACAO) ");
		sql.append(" inner join JUDICIARIO.TIPO_COMUNICACAO tc on (mc.SEQ_TIPO_COMUNICACAO = tc.SEQ_TIPO_COMUNICACAO) ");

		sql.append(" left outer join CORPORATIVO.PESSOA pe on c.SEQ_PESSOA_DESTINATARIA = pe.SEQ_PESSOA ");
		sql.append(" left outer join JUDICIARIO.TIPO_RECEBIMENTO_COMUNICACAO trc on (trc.COD_TIPO_RECEBIMENTO = c.COD_TIPO_RECEBIMENTO) ");
		sql.append(" left outer join JUDICIARIO.FASE_COMUNICACAO fc on (c.SEQ_COMUNICACAO = fc.SEQ_COMUNICACAO AND fc.SEQ_TIPO_FASE_COMUNICACAO = 9) ");
		sql.append(" left outer join CORPORATIVO.USUARIO u on (u.SIG_USUARIO = c.SIG_USUARIO_RECEBIMENTO) ");
		//sql.append(" left outer join CORPORATIVO.PESSOA p on (p.SEQ_PESSOA = u.SEQ_PESSOA) ");

		if (idPreferemcia != null) {
			sql.append(" inner join JUDICIARIO.INCIDENTE_PREFERENCIA ip on (ip.SEQ_OBJETO_INCIDENTE = coi.SEQ_OBJETO_INCIDENTE)");
			sql.append(" inner join JUDICIARIO.TIPO_PREFERENCIA tp on (tp.SEQ_TIPO_PREFERENCIA = ip.SEQ_TIPO_PREFERENCIA)");
		}

		String where = " WHERE ";
		String and = " ";

		sql.append(where);
		sql.append(and);

		where = "";
		and = "";

		if (descricaoTipoComunicacao != null) {
			sql.append(where);
			sql.append(and);
			sql.append(" tc.DSC_TIPO_COMUNICACAO in (");
			sql.append(descricaoTipoComunicacao);
			sql.append(")");
			where = "";
			and = " AND ";

		}

		if (descricaoModelo != null) {
			sql.append(where);
			sql.append(and);
			sql.append(" mc.DSC_MODELO = '" + descricaoModelo + "'");
			where = "";
			and = " AND ";
		}

		if (tipoRecebimentoComunicacaoEnum != null) {
			sql.append(where);
			sql.append(and);

			if (tipoRecebimentoComunicacaoEnum == TipoRecebimentoComunicacaoEnum.NAO_LIDA) {
				sql.append(" trc.COD_TIPO_RECEBIMENTO IS NULL  AND (fc.SEQ_TIPO_FASE_COMUNICACAO = 6 OR fc.SEQ_TIPO_FASE_COMUNICACAO IS NULL) ");
			} else if (tipoRecebimentoComunicacaoEnum == TipoRecebimentoComunicacaoEnum.CANCELADA) {
				sql.append(" trc.COD_TIPO_RECEBIMENTO IS NULL AND fc.SEQ_TIPO_FASE_COMUNICACAO = 9 ");
			} else {
				sql.append(" trc.COD_TIPO_RECEBIMENTO = "
						+ tipoRecebimentoComunicacaoEnum
								.getTipoRecebimentoComunicacao().getCodigo()
						+ " ");
			}

			where = "";
			and = " AND ";
		}

		if (idProcesso != null) {
			sql.append(where);
			sql.append(and);
			sql.append("coi.Seq_Objeto_Incidente = " + idProcesso);
			where = "";
			and = " AND ";
		}

		if (idParte != null) {
			sql.append(where);
			sql.append(and);
			sql.append(" pe.SEQ_PESSOA = '" + idParte + "' ");
			where = "";
			and = " AND ";
		}

		if (idPreferemcia != null) {
			sql.append(where);
			sql.append(and);
			sql.append("tp.SEQ_TIPO_PREFERENCIA = " + idPreferemcia);
			where = "";
			and = " AND ";
		}

		if (periodoEnvioInicio != null && periodoEnvioFim != null) {
			sql.append(where);
			sql.append(and);

			sql.append(" TO_DATE(TO_CHAR(c.Dat_Comunicacao_Enviada, 'DD/MM/YYYY'), 'DD/MM/YYYY') between TO_DATE( '"
					+ getDataFormatada(periodoEnvioInicio)
					+ "' ,'DD/MM/YYYY') and TO_DATE( '"
					+ getDataFormatada(periodoEnvioFim) + "' ,'DD/MM/YYYY') ");

		} else if (periodoEnvioInicio == null && periodoEnvioFim != null) {
			sql.append(where);
			sql.append(and);

			sql.append(" TO_DATE(TO_CHAR(c.Dat_Comunicacao_Enviada, 'DD/MM/YYYY'), 'DD/MM/YYYY') "
					+ "<= TO_DATE( '"
					+ getDataFormatada(periodoEnvioFim)
					+ "' ,'DD/MM/YYYY') ");
		} else if (periodoEnvioInicio != null && periodoEnvioFim == null) {
			sql.append(where);
			sql.append(and);

			sql.append(" TO_DATE(TO_CHAR(c.Dat_Comunicacao_Enviada, 'DD/MM/YYYY'), 'DD/MM/YYYY') "
					+ " >= TO_DATE( '"
					+ getDataFormatada(periodoEnvioInicio)
					+ "' ,'DD/MM/YYYY') ");
		}

		sql.append("  ORDER BY DAT_COMUNICACAO_ENVIADA desc  ");

		SQLQuery query = session.createSQLQuery(sql.toString());

		query.setResultTransformer(new ComunicacaoResultTransformer());

		return query.list();
	}

	private String getDataFormatada(Date data) {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		return formato.format(data);
	}

	private String gerarFormaRecebimentoPorSituacao(String situacao) {
		String resultado = "";
		if (situacao.equals("Ciência")) {
			resultado = "Leitura";
		}
		if (situacao.equals("Ciência pelo Sistema")) {
			resultado = "Graça (prazo vencido)";
		}
		return resultado;
	}

	private class ComunicacaoResultTransformer extends
			ComunicacaoResultTransformerAbstract {

		private static final long serialVersionUID = 1L;

		@Override
		public List transformList(List arg0) {
			return arg0;
		}

		@Override
		public Object transformTuple(Object[] rowData, String[] aliases) {
			ComunicacaoExternaDTO comunicacaoExternaDTO = new ComunicacaoExternaDTO();

			comunicacaoExternaDTO.setIdComunicacao(getLong(rowData[0]));
			comunicacaoExternaDTO.setModelo(getString(rowData[1]));
			comunicacaoExternaDTO.setDataEnvio(getDate(rowData[2]));
			comunicacaoExternaDTO.setDataRecebimento(getDate(rowData[3]));
			comunicacaoExternaDTO
					.setUsuarioRecebeuComunicacao(getString(rowData[4]));
			
			comunicacaoExternaDTO.setOrgaoIntimado(getString(rowData[5]));
			comunicacaoExternaDTO
			.setNomePessoaRecebeuComunicacao(getString(rowData[6]));

			if (rowData[7] != null) {
				comunicacaoExternaDTO.setSituacao(getString(rowData[7]));
				comunicacaoExternaDTO
						.setFormaRecebimento(gerarFormaRecebimentoPorSituacao(comunicacaoExternaDTO
								.getSituacao()));
			} else {

				if (rowData[8] != null && getLong(rowData[8]) == 9) {
					comunicacaoExternaDTO.setFormaRecebimento("Cancelada");
					comunicacaoExternaDTO.setSituacao("Cancelada");
				} else {
					comunicacaoExternaDTO.setFormaRecebimento("Não Lida");
					comunicacaoExternaDTO.setSituacao("Não Lida");
				}
			}

			comunicacaoExternaDTO
					.setDescricaoTipoComunicacao(getString(rowData[9]));

			if (rowData[10] != null) {
				comunicacaoExternaDTO
						.setIdProcessoIncidente(getLong(rowData[10]));
				comunicacaoExternaDTO
						.setTiposPreferencias(retornaTipoIncidentePorProcesso(getLong(rowData[10])));
			}

			return comunicacaoExternaDTO;
		}
	}

	private List<TipoIncidentePreferencia> retornaTipoIncidentePorProcesso(
			Long objetoIncidente) {

		List<TipoIncidentePreferencia> tipoIncidentePreferencia = null;
		StringBuffer sql = new StringBuffer();

		try {
			sql.append(" SELECT tip.* FROM JUDICIARIO.INCIDENTE_PREFERENCIA ip ");
			sql.append(" INNER JOIN JUDICIARIO.TIPO_PREFERENCIA tip on (tip.SEQ_TIPO_PREFERENCIA = ip.SEQ_TIPO_PREFERENCIA) ");
			sql.append(" WHERE  ip.SEQ_OBJETO_INCIDENTE =:objetoIncidente ");

			SQLQuery sqlQuery = retrieveSession()
					.createSQLQuery(sql.toString());
			sqlQuery.setLong("objetoIncidente", objetoIncidente);

			sqlQuery.addEntity(TipoIncidentePreferencia.class);
			tipoIncidentePreferencia = sqlQuery.list();
		} catch (DaoException e) {
			LOG.error("Erro ao buscar tipos de preferência do processo: ", e);
		}
		return tipoIncidentePreferencia;

	}

	public List<TipoIncidentePreferencia> buscaTodasPreferencias()
			throws DaoException {
		Session session = retrieveSession();
		Criteria c = session.createCriteria(TipoIncidentePreferencia.class);
		return c.list();

	}
	
	/**
	 * Recupera o codigo origem da pessoa destinatária da comunicação.
	 *
	 * @param comunicacao
	 * @return long
	 * @throws DaoException
	 */
	public Long recuperarCodigoOrigemDestinatario(Long idComunicacao) throws DaoException{		
		BigDecimal codigoOrigem = null; 
		
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT DISTINCT o.COD_ORIGEM");
		sql.append(" FROM JUDICIARIO.ORIGEM o ");
		sql.append(" INNER JOIN JUDICIARIO.COMUNICACAO c on ");
		sql.append(" (c.SEQ_PESSOA_DESTINATARIA = o.SEQ_PESSOA) ");
		sql.append(" WHERE  c.SEQ_COMUNICACAO = :idComunicacao ");		
		
		SQLQuery sqlQuery = retrieveSession()
				.createSQLQuery(sql.toString());
		sqlQuery.setLong("idComunicacao", idComunicacao);

		codigoOrigem = (BigDecimal) sqlQuery.uniqueResult();
		
		return codigoOrigem != null ? codigoOrigem.longValue() : null;
	}
	
	@Deprecated
    @Override
    public List<Comunicacao> buscar(Long idParte, Long idTipoComunicacao, Date periodoEnvioInicio, Date periodoEnvioFim, Long idProcesso) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
