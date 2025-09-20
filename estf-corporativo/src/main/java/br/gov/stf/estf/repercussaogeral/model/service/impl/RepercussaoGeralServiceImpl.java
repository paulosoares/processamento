package br.gov.stf.estf.repercussaogeral.model.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleConstants.ColorConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.rtf.RTFEditorKit;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.stf.estf.documento.model.service.ArquivoEletronicoService;
import br.gov.stf.estf.documento.model.service.ControleVistaService;
import br.gov.stf.estf.documento.model.service.ControleVotoTextoService;
import br.gov.stf.estf.documento.model.service.DocumentoTextoService;
import br.gov.stf.estf.documento.model.service.TextoAndamentoProcessoService;
import br.gov.stf.estf.documento.model.service.TextoService;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.Texto.TipoRestricao;
import br.gov.stf.estf.entidade.documento.TextoAndamentoProcesso;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.EnvolvidoSessao;
import br.gov.stf.estf.entidade.julgamento.EventoSessao;
import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.ProcessoTema;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoJulgamentoVirtual;
import br.gov.stf.estf.entidade.julgamento.SituacaoJulgamento;
import br.gov.stf.estf.entidade.julgamento.Tema;
import br.gov.stf.estf.entidade.julgamento.TipoCompetenciaEnvolvido;
import br.gov.stf.estf.entidade.julgamento.TipoCompetenciaEnvolvido.TipoAtuacaoConstante;
import br.gov.stf.estf.entidade.julgamento.TipoOcorrencia;
import br.gov.stf.estf.entidade.julgamento.TipoOcorrencia.TipoOcorrenciaConstante;
import br.gov.stf.estf.entidade.julgamento.TipoSituacaoJulgamento;
import br.gov.stf.estf.entidade.julgamento.TipoSituacaoJulgamento.TipoSitucacaoJulgamentoConstant;
import br.gov.stf.estf.entidade.julgamento.TipoSituacaoProcessoSessao;
import br.gov.stf.estf.entidade.julgamento.TipoTema;
import br.gov.stf.estf.entidade.julgamento.TipoTema.TipoTemaConstante;
import br.gov.stf.estf.entidade.julgamento.TipoVoto;
import br.gov.stf.estf.entidade.julgamento.TipoVoto.TipoVotoConstante;
import br.gov.stf.estf.entidade.julgamento.VinculoProcessoTema;
import br.gov.stf.estf.entidade.julgamento.VotoJulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.VotoJulgamentoProcesso.TipoSituacaoVoto;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.AfastamentoMinistroView;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.ministro.MinistroPresidente;
import br.gov.stf.estf.entidade.ministro.TipoOcorrenciaMinistro;
import br.gov.stf.estf.entidade.processostf.Agendamento;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Assunto;
import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.OrigemAndamentoDecisao;
import br.gov.stf.estf.entidade.processostf.OrigemAndamentoDecisao.ConstanteOrigemDecisao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.RecursoProcesso;
import br.gov.stf.estf.entidade.processostf.TipoIncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.TipoRecurso;
import br.gov.stf.estf.entidade.publicacao.EstruturaPublicacao;
import br.gov.stf.estf.entidade.tarefa.TipoCampoTarefa.TipoCampoTarefaContante;
import br.gov.stf.estf.entidade.tarefa.TipoCampoTarefaValor;
import br.gov.stf.estf.entidade.usuario.NotificacaoLogon;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.entidade.usuario.UsuarioEGab;
import br.gov.stf.estf.entidade.util.DadosTextoDecisao;
import br.gov.stf.estf.entidade.util.ObjetoIncidenteUtil;
import br.gov.stf.estf.julgamento.model.exception.TemaException;
import br.gov.stf.estf.julgamento.model.service.ColegiadoService;
import br.gov.stf.estf.julgamento.model.service.EnvolvidoService;
import br.gov.stf.estf.julgamento.model.service.JulgamentoProcessoService;
import br.gov.stf.estf.julgamento.model.service.ProcessoTemaService;
import br.gov.stf.estf.julgamento.model.service.SessaoService;
import br.gov.stf.estf.julgamento.model.service.SituacaoJulgamentoService;
import br.gov.stf.estf.julgamento.model.service.TemaService;
import br.gov.stf.estf.julgamento.model.service.TipoCompetenciaEnvolvidoService;
import br.gov.stf.estf.julgamento.model.service.TipoSituacaoJulgamentoService;
import br.gov.stf.estf.julgamento.model.service.VinculoProcessoTemaService;
import br.gov.stf.estf.julgamento.model.service.VotoJulgamentoProcessoService;
import br.gov.stf.estf.julgamento.model.util.JulgamentoProcessoSearchData;
import br.gov.stf.estf.ministro.model.service.MinistroPresidenteService;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.estf.processostf.model.service.AgendamentoService;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoService;
import br.gov.stf.estf.processostf.model.service.IncidenteJulgamentoService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.OrigemAndamentoDecisaoService;
import br.gov.stf.estf.processostf.model.service.SituacaoMinistroProcessoService;
import br.gov.stf.estf.processostf.model.service.TipoRecursoService;
import br.gov.stf.estf.processostf.model.service.exception.IncidenteJulgamentoException;
import br.gov.stf.estf.processostf.model.util.ConstanteAndamento;
import br.gov.stf.estf.publicacao.model.service.FeriadoService;
import br.gov.stf.estf.publicacao.model.util.IConsultaDePautaDeJulgamento;
import br.gov.stf.estf.repercussaogeral.model.dataaccess.RepercussaoGeralDao;
import br.gov.stf.estf.repercussaogeral.model.service.RepercussaoGeralException;
import br.gov.stf.estf.repercussaogeral.model.service.RepercussaoGeralService;
import br.gov.stf.estf.repercussaogeral.model.util.RepercussaoGeralSearchData;
import br.gov.stf.estf.tarefa.model.service.TipoTarefaSetorService;
import br.gov.stf.estf.usuario.model.service.NotificacaoLogonService;
import br.gov.stf.estf.usuario.model.service.UsuarioService;
import br.gov.stf.estf.util.DataUtil;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.EqualCriterion;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.SearchCriterion;
import br.gov.stf.framework.model.entity.BaseEntity;
import br.gov.stf.framework.model.entity.TipoSexo;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;
import br.gov.stf.framework.util.DateTimeHelper;
import br.gov.stf.framework.util.SearchData;
import br.gov.stf.framework.util.SearchResult;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Service("repercussaoGeralService")
public class RepercussaoGeralServiceImpl extends GenericServiceImpl<BaseEntity, Long, RepercussaoGeralDao> implements RepercussaoGeralService {

	private static Log log = LogFactory.getLog(RepercussaoGeralServiceImpl.class);
	
	public static final int NOTIFICACAO_MANIFESTACAO_DIVERGENTE = 3;
	public static final int NOTIFICACAO_PRAZO_RESTANTE = 2;

	TipoTarefaSetorService tipoTarefaSetorService;
	DocumentoTextoService documentoTextoService;
	MinistroPresidenteService ministroPresidenteService;
	MinistroService ministroService;
	TipoSituacaoJulgamentoService tipoSituacaoJulgamentoService;
	TemaService temaService;
	JulgamentoProcessoService julgamentoProcessoService;
	ColegiadoService colegiadoService;
	EnvolvidoService envolvidoService;
	AndamentoProcessoService andamentoProcessoService;
	SessaoService sessaoService;
	TipoCompetenciaEnvolvidoService tipoCompetenciaEnvolvidoService;
	OrigemAndamentoDecisaoService origemAndamentoDecisaoService;
	NotificacaoLogonService notificacaoLogonService;
	TipoRecursoService tipoRecursoService;
	IncidenteJulgamentoService incidenteJulgamentoService;
	SituacaoMinistroProcessoService situacaoMinistroProcessoService;
	ProcessoTemaService processoTemaService;
	ObjetoIncidenteService objetoIncidenteService;
	ArquivoEletronicoService arquivoEletronicoService;
	VinculoProcessoTemaService vinculoProcessoTemaService;
	@Autowired
	TextoService textoService;
	@Autowired
	TextoAndamentoProcessoService textoAndamentoProcessoService;
	@Autowired
	ControleVistaService controleVistaService;
	@Autowired
	ControleVotoTextoService controleVotoTextoService;
	
	@Autowired
	private VotoJulgamentoProcessoService votoJulgamentoProcessoService;
	
	@Autowired
	private SituacaoJulgamentoService situacaoJulgamentoService;
	
	@Autowired
	private FeriadoService feriadoService;
	
	@Autowired
	private AgendamentoService agendamentoService;

	private static final String dsc_perfil = "Notificacao Agendador RG";
	
	public static int DURACAO_SESSAO_REPERCUSSAO_GERAL = 6; // Quantidade de dias úteis de duração da sessão

	public RepercussaoGeralServiceImpl(RepercussaoGeralDao dao,
			TipoTarefaSetorService tipoTarefaSetorService,
			DocumentoTextoService documentoTextoService,
			UsuarioService usuarioService, TemaService temaService,
			JulgamentoProcessoService julgamentoProcessoService,
			ColegiadoService colegiadoService,
			EnvolvidoService envolvidoService,
			AndamentoProcessoService andamentoProcessoService,
			SessaoService sessaoService,
			MinistroPresidenteService ministroPresidenteService,
			MinistroService ministroService,
			TipoSituacaoJulgamentoService tipoSituacaoJulgamentoService,
			TipoCompetenciaEnvolvidoService tipoCompetenciaEnvolvidoService,
			OrigemAndamentoDecisaoService origemAndamentoDecisaoService,
			NotificacaoLogonService notificacaoLogonService,
			TipoRecursoService tipoRecursoService,
			IncidenteJulgamentoService incidenteJulgamentoService,
			SituacaoMinistroProcessoService situacaoMinistroProcessoService,
			ProcessoTemaService processoTemaService,
			ObjetoIncidenteService objetoIncidenteService,
			ArquivoEletronicoService arquivoEletronicoService,
			VinculoProcessoTemaService vinculoProcessoTemaService) {

		super(dao);

		this.tipoTarefaSetorService = tipoTarefaSetorService;
		this.documentoTextoService = documentoTextoService;
		this.ministroPresidenteService = ministroPresidenteService;
		this.temaService = temaService;
		this.tipoRecursoService = tipoRecursoService;
		this.julgamentoProcessoService = julgamentoProcessoService;
		this.colegiadoService = colegiadoService;
		this.envolvidoService = envolvidoService;
		this.andamentoProcessoService = andamentoProcessoService;
		this.sessaoService = sessaoService;
		this.ministroService = ministroService;
		this.tipoSituacaoJulgamentoService = tipoSituacaoJulgamentoService;
		this.tipoCompetenciaEnvolvidoService = tipoCompetenciaEnvolvidoService;
		this.origemAndamentoDecisaoService = origemAndamentoDecisaoService;
		this.notificacaoLogonService = notificacaoLogonService;
		this.incidenteJulgamentoService = incidenteJulgamentoService;
		this.situacaoMinistroProcessoService = situacaoMinistroProcessoService;
		this.processoTemaService = processoTemaService;
		this.objetoIncidenteService = objetoIncidenteService;
		this.arquivoEletronicoService = arquivoEletronicoService;
		this.vinculoProcessoTemaService = vinculoProcessoTemaService;
	
	}

	// #################################### INCLUSAO TEMA REPERCUSSAO GERAL
	// ####################################//
	public void persistirTemaRepercussaoGeral(ObjetoIncidente<?> objetoIncidente, Processo processo) throws ServiceException {

		try {
			List<ProcessoTema> lista = processoTemaService.pesquisarProcessoTema(null, processo.getClasseProcessual().getId(), processo.getNumeroProcessual(),
					TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL, TipoOcorrenciaConstante.JULGAMENTO_LEADING_CASE.getCodigo(), null);

			if (lista == null || lista.size() == 0) {

				/* Verificar se o Processo Tema é um representativo de
				 * controvérsia (Tipo de tema Cód : 2 ) e também se não está em outro tema existente. Se
				 * for afirmativo para os dois casos, deve-se retirar o processo
				 * do tema anterior, antes de gerar o novo tema. */
				Long idTipoTema = 2L;

				List<VinculoProcessoTema> listaVPT = vinculoProcessoTemaService.pesquisarVinculoProcessoTema(null, processo.getPrincipal().getId(), idTipoTema);

				List<ProcessoTema> listaPT = processoTemaService.pesquisarProcessoTema(null, processo.getSiglaClasseProcessual(),
						processo.getNumeroProcessual(), "", TipoOcorrenciaConstante.PROCESSO_RELACIONADO_POR_CONTROVERSIA.getCodigo(), null);

				retirarProcessoSeForControversiaRelacionadoATermaRG(listaVPT, listaPT);
				
				Tema temaIncidente = null;
				if(listaVPT != null && listaVPT.size() > 0 && listaVPT.get(0).getTema() != null){
					temaIncidente = listaVPT.get(0).getTema();
				}

				List<TipoTema> listaTipoTema = temaService.pesquisarTipoTema(TipoTemaConstante.REPERCUSSAO_GERAL.getCodigo(), null);
				List<TipoOcorrencia> listaTipoOcorrencia = temaService.pesquisarTipoOcorrencia(TipoOcorrenciaConstante.JULGAMENTO_LEADING_CASE.getCodigo(),
						null);
				if (listaTipoTema != null && listaTipoTema.size() > 0 && listaTipoOcorrencia != null && listaTipoOcorrencia.size() > 0) {
					Tema tema = new Tema();
					if (tema.getAssuntos() == null) {
						tema.setAssuntos(new LinkedList<Assunto>());
					}
					tema.getAssuntos().clear();
					tema.getAssuntos().addAll(processo.getAssuntos());
					tema.setTipoTema(listaTipoTema.get(0));
					tema.setProcessosTema(new LinkedList<ProcessoTema>());
					ProcessoTema proc = new ProcessoTema();
					proc.setDataOcorrencia(Calendar.getInstance().getTime());
					proc.setTema(tema);
					proc.setTipoOcorrencia(listaTipoOcorrencia.get(0));
					proc.setObjetoIncidente(objetoIncidente);
					tema.getProcessosTema().add(proc);
					tema.setNumeroSequenciaTema(buscaNumeroMaximoTema());
					tema.setTituloTema(recuperarTituloTemaControversia(temaIncidente));
					tema.setDescricao(recuperarDescricaoTemaControversia(temaIncidente));
					tema.setSituacaoTema(recuperarSituacaoTemaControversia(temaIncidente));
					tema.setDataSituacaoTema(recuperarDataSituacaoTemaControversia(temaIncidente));
					tema.setSituacaoTema("A");
					temaService.persistirTema(tema);
				}
			}

		} catch (TemaException e) {
			throw new ServiceException(e);
		}

	}
	
	private Date recuperarDataSituacaoTemaControversia(Tema tema) {
		if (tema != null && tema.getDataSituacaoTema() != null) {
			return tema.getDataSituacaoTema();
		}
		return null;
	}
	
	private String recuperarSituacaoTemaControversia(Tema tema) {
		if (tema != null && tema.getSituacaoTema() != null) {
			return tema.getSituacaoTema();
		}
		return null;
	}
	
	private String recuperarTituloTemaControversia(Tema tema) {
		if (tema != null && tema.getTituloTema() != null) {
			return tema.getTituloTema();
		}
		return "";
	}

	private String recuperarDescricaoTemaControversia(Tema tema) {
		if (tema != null && tema.getDescricao() != null) {
			return tema.getDescricao();
		}
		return "";
	}


	private void retirarProcessoSeForControversiaRelacionadoATermaRG(List<VinculoProcessoTema> listaVPT, List<ProcessoTema> listaPT) throws ServiceException {
		if (listaVPT != null && listaVPT.size() > 0 && listaPT != null && listaPT.size() > 0) {
			processoTemaService.excluir(listaPT.get(0));
		}
	}

	/**
	 * método responsavel por recuperar a data inicio e fim da repercussão geral
	 * no recesso
	 * 
	 * @author ViniciusK,GUilhermea
	 */
	public Date recuperarData(boolean recuperarDataInicio) throws ServiceException {

		Date dataInicio = null;
		Date dataFim = null;

		try {

			String valor = "";
			String recesso = "";
			Date data = new Date();
			List<TipoCampoTarefaValor> lista = recuperarTipoCampoTarefaValor(TipoCampoTarefaContante.RECESSO);

			for (TipoCampoTarefaValor campo : lista) {
				int mes = (DateTimeHelper.getMes(data) + 1);
				String mesCorrente = mes > 9 ? "" + mes : "0" + mes;
				if (campo.getDescricao().contains("/" + mesCorrente)) {
					recesso = campo.getDescricao().trim();
				}
			}
			if (recesso != null && recesso.trim().length() > 0) {
				for (int i = 0; i < recesso.trim().length(); i++) {

					Character letra = recesso.charAt(i);
					if (recesso.codePointAt(i) != 45) {
						valor = valor + letra;
					} else {
						int anoInicio = DateTimeHelper.getMes(data) == 0 ? (DateTimeHelper.getAno(data) - 1) : DateTimeHelper.getAno(data);
						dataInicio = DateTimeHelper.getDataHora(valor + "/" + anoInicio + " 00:00:00");
						valor = "";
					}
				}
				int anoFim = DateTimeHelper.getAno(data);
				if (valor.contains("/01")) {
					anoFim = DateTimeHelper.getMes(data) == 11 ? (DateTimeHelper.getAno(data) + 1) : DateTimeHelper.getAno(data);
				}
				dataFim = DateTimeHelper.getDataHora(valor + "/" + anoFim + " 23:59:59");
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return recuperarDataInicio ? dataInicio : dataFim;
	}

	/**
	 * Método reponsánvel em buscar o número máximo da tabela STF.TEMA na coluna
	 * NUM_TEMA quando for criado um novo tema para a Repercussão Geral
	 */
	public Long buscaNumeroMaximoTema() throws ServiceException {
		Long numeroMaximo = 0L;
		try {
			numeroMaximo = dao.buscarMaxNumeroTema();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return numeroMaximo + 1;
	}

	/**
	 * Metodo responsavel por verificar se a data atual é recesso
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public Boolean verificaDiaAtualRecesso() throws ServiceException {
		try {
			return verificaDataRecesso(new Date());
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * método responsavel por verificar se a data passar por parâmetro está no
	 * periodo de recesso
	 * 
	 * @param data
	 * @return
	 * @throws ServiceException
	 */
	private boolean verificaDataRecesso(Date data) throws ServiceException {
		try {

			Date dataInicio = null;
			Date dataFim = null;
			String valor = "";
			String recesso = "";
			boolean retorno = false;
			List<TipoCampoTarefaValor> lista = recuperarTipoCampoTarefaValor(TipoCampoTarefaContante.RECESSO);

			for (TipoCampoTarefaValor campo : lista) {
				recesso = campo.getDescricao().trim();
				valor = "";
				if (recesso != null && recesso.trim().length() > 0) {
					for (int i = 0; i < recesso.trim().length(); i++) {

						Character letra = recesso.charAt(i);
						if (recesso.codePointAt(i) != 45) {
							valor = valor + letra;
						} else {
							int anoInicio = DateTimeHelper.getMes(data) == 0 ? (DateTimeHelper.getAno(data) - 1) : DateTimeHelper.getAno(data);
							dataInicio = DateTimeHelper.getDataHora(valor + "/" + anoInicio + " 00:00:00");
							valor = "";
						}

					}
					int anoFim = DateTimeHelper.getAno(data);
					if (valor.contains("/01")) {
						anoFim = DateTimeHelper.getMes(data) == 11 ? (DateTimeHelper.getAno(data) + 1) : DateTimeHelper.getAno(data);
					}

					dataFim = DateTimeHelper.getDataHora(valor + "/" + anoFim + " 23:59:59");

					if (data.compareTo(dataInicio) >= 0 && data.compareTo(dataFim) <= 0) {
						retorno = true;
						break;
					}
				}
			}
			return retorno;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	// #################################### FIM RECESSO REPERCUSSAO GERAL

	/**
	 * metodo resposanvel por recuperar a hora (0 as 23) de acordo com a data
	 * informada
	 * 
	 * @param data
	 * @return hora
	 * @author GuilhermeA
	 */
	public int recuperarHoraAtual(Date data) throws ServiceException {
		try {

			Locale localidade = new Locale("pt", "BR");
			DateFormat dateFormat = new SimpleDateFormat("HH", localidade);
			return Integer.parseInt(dateFormat.format(data));
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public void validarRepercussaoGeral(Processo processo, Ministro ministroAutenticado, Ministro ministroRelator,
			List<VotoJulgamentoProcesso> listaTipoVotoMinistro, JulgamentoProcesso julgamentoProcessoRG, JulgamentoProcesso julgamentoProcessoQCRG,
			JulgamentoProcesso julgamentoProcessoRJ) throws ServiceException, RepercussaoGeralException {

		try {

			if (processo == null) {
				throw new RepercussaoGeralException("O processo deve ser informado.");
			}

			if (ministroAutenticado == null) {
				throw new RepercussaoGeralException("O ministro autenticado deve ser informado.");
			}


			// verifica se a manifestação sobre RG e QCRG foi informado
			if (listaTipoVotoMinistro == null || listaTipoVotoMinistro.size() == 0)
				throw new RepercussaoGeralException("O voto deve ser informado.");
			
			boolean rg = false;
			boolean qcrg = false;

			for (VotoJulgamentoProcesso votoJulgamentoProcesso : listaTipoVotoMinistro) {

				if (votoJulgamentoProcesso.getTipoVoto().getId().equals(TipoVoto.TipoVotoConstante.HA.getCodigo())
						|| votoJulgamentoProcesso.getTipoVoto().getId().equals(TipoVoto.TipoVotoConstante.NAO_HA.getCodigo())
						|| votoJulgamentoProcesso.getTipoVoto().getId().equals(TipoVoto.TipoVotoConstante.SIM.getCodigo())
						|| votoJulgamentoProcesso.getTipoVoto().getId().equals(TipoVoto.TipoVotoConstante.NAO.getCodigo())) {

					if (votoJulgamentoProcesso.getTipoIncidenteJulgamento().equals(TipoIncidenteJulgamento.SIGLA_QUESTAO_CONSTITUCIONAL) || votoJulgamentoProcesso.getTipoIncidenteJulgamento().equals(TipoIncidenteJulgamento.SIGLA_QUESTAO_CONSTITUCIONAL_SEGUNDO_JULGAMENTO)) {
						qcrg = true;
					} else if (votoJulgamentoProcesso.getTipoIncidenteJulgamento().equals(TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL) || votoJulgamentoProcesso.getTipoIncidenteJulgamento().equals(TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL_SEGUNDO_JULGAMENTO)) {
						rg = true;
					}
				} else if (votoJulgamentoProcesso.getTipoVoto().getId().equals(TipoVoto.TipoVotoConstante.IMPEDIDO.getCodigo())) {
					qcrg = true;
					rg = true;
					break;
				}
			}

			// se o ministro relator informar que a matéria é constitucional
			// os demais ministros não terão a opção de informar que a
			// matéria é constitucional.
			if (julgamentoProcessoQCRG != null && julgamentoProcessoQCRG.getListVotoJulgamentoProcessoValido() != null)
				for (VotoJulgamentoProcesso voto : julgamentoProcessoQCRG.getListVotoJulgamentoProcessoValido())
					if (voto.getMinistro().getId().equals(ministroRelator.getId()) && voto.getTipoVoto().getId().equals(TipoVotoConstante.HA.getCodigo()))
						qcrg = true;
			

			if (!rg)
				throw new RepercussaoGeralException("O voto sobre repercussão geral deve ser informado.");

			if (!qcrg)
				throw new RepercussaoGeralException("O voto sobre questão constitucional deve ser informado.");

		} catch (RepercussaoGeralException e) {
			throw e;
		}
	}
	
	@Transactional
	public Boolean persistirRepercussaoGeralJulgamento(Processo processo, Ministro ministroAutenticado, List<VotoJulgamentoProcesso> listaTipoVotoMinistro,
			Texto texto, boolean segundoJulgamento, UsuarioEGab usuario, Boolean ignorarCpc, Sessao sessao) throws ServiceException, RepercussaoGeralException {

		if (sessao.getListaEnvolvidoSessao() == null || sessao.getListaEnvolvidoSessao().size() == 0)
			sessao.setListaEnvolvidoSessao(recuperarMinistrosEnvolvidos(sessao));
		
		sessao = sessaoService.salvar(sessao);
		
		try {
			JulgamentoProcesso julgamentoProcessoRG = null;
			JulgamentoProcesso julgamentoProcessoQCRG = null;
			JulgamentoProcesso julgamentoProcessoRJ = null;
			
			for (VotoJulgamentoProcesso voto : listaTipoVotoMinistro) {
				if (voto.getJulgamentoProcesso() != null) {
					if (segundoJulgamento) {
						if (TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL_SEGUNDO_JULGAMENTO.equals(voto.getJulgamentoProcesso().getTipoJulgamento()))
							julgamentoProcessoRG = voto.getJulgamentoProcesso();
						
						if (TipoIncidenteJulgamento.SIGLA_QUESTAO_CONSTITUCIONAL_SEGUNDO_JULGAMENTO.equals(voto.getJulgamentoProcesso().getTipoJulgamento()))
							julgamentoProcessoQCRG = voto.getJulgamentoProcesso();
						
					} else {
						
						if (TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL.equals(voto.getJulgamentoProcesso().getTipoJulgamento()))
							julgamentoProcessoRG = voto.getJulgamentoProcesso();
						
						if (TipoIncidenteJulgamento.SIGLA_QUESTAO_CONSTITUCIONAL.equals(voto.getJulgamentoProcesso().getTipoJulgamento()))
							julgamentoProcessoQCRG = voto.getJulgamentoProcesso();
						
						if (TipoIncidenteJulgamento.SIGLA_MERITO.equals(voto.getJulgamentoProcesso().getTipoJulgamento()))
							julgamentoProcessoRJ = voto.getJulgamentoProcesso();
					}
				}
			}
			
			// Remove RJ cadastrado anteriormente caso o usuário não tenha marcado nem SIM e nem NÃO
			if (julgamentoProcessoRJ == null) {
				JulgamentoProcesso jpRJ = julgamentoProcessoService.recuperar(processo, julgamentoProcessoRG.getSessao()); // tem que ser a sessão da RG pq nova sessão pode ser diferente da sessão já registrada
				if (jpRJ != null) {
					Collection<VotoJulgamentoProcesso> votos = jpRJ.getListaVotoJulgamentoProcesso();
					
					for (VotoJulgamentoProcesso voto : votos) {
						if (voto.getMinistro().equals(ministroAutenticado) && TipoIncidenteJulgamento.SIGLA_MERITO.equals(voto.getJulgamentoProcesso().getTipoJulgamento())) {
							votoJulgamentoProcessoService.excluir(voto);
							julgamentoProcessoService.refresh(jpRJ);
						}
					}
				}
			}
			
			Ministro ministroRelator = processo.getMinistroRelatorAtual(); //situacaoMinistroProcessoService.recuperarMinistroRelatorAtual(processo);

			// se o processo estiver registrado a Presidência o sistema carrega
			// o Ministro Presidente como relator
			if (ministroRelator != null && ministroRelator.getId().equals(Ministro.COD_MINISTRO_PRESIDENTE)) {

				// se for agendamento de julgamento na sexta utiliza a data atual. Se não, utilizada a data prevista para o fim do julgamento
				if (julgamentoProcessoRG == null) {
					ministroRelator = ministroService.recuperarMinistroPresidente(Calendar.getInstance().getTime());
				} else {
					ministroRelator = ministroService.recuperarMinistroPresidente(sessao.getDataPrevistaFim());
				}
			}

			validarRepercussaoGeral(processo, ministroAutenticado, ministroRelator, listaTipoVotoMinistro, julgamentoProcessoRG, julgamentoProcessoQCRG, julgamentoProcessoRJ);

			for (VotoJulgamentoProcesso tipoVotoMinistro : listaTipoVotoMinistro) {

				if (tipoVotoMinistro.getTipoIncidenteJulgamento().equals(TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL)
						|| tipoVotoMinistro.getTipoIncidenteJulgamento().equals(TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL_SEGUNDO_JULGAMENTO))
					julgamentoProcessoRG = getInstanciaJugamentoProcesso(julgamentoProcessoRG, processo, ministroAutenticado, tipoVotoMinistro, sessao);

				if (tipoVotoMinistro.getTipoIncidenteJulgamento().equals(TipoIncidenteJulgamento.SIGLA_QUESTAO_CONSTITUCIONAL)
								|| tipoVotoMinistro.getTipoIncidenteJulgamento().equals(TipoIncidenteJulgamento.SIGLA_QUESTAO_CONSTITUCIONAL_SEGUNDO_JULGAMENTO))
					julgamentoProcessoQCRG = getInstanciaJugamentoProcesso(julgamentoProcessoQCRG, processo, ministroAutenticado, tipoVotoMinistro, sessao);

				if (!segundoJulgamento && (tipoVotoMinistro.getTipoIncidenteJulgamento().equals(TipoIncidenteJulgamento.SIGLA_TIPO_REAFIRMACAO_JURISPRUDENCIA)
						|| tipoVotoMinistro.getTipoIncidenteJulgamento().equals(TipoIncidenteJulgamento.SIGLA_MERITO))) {
					julgamentoProcessoRJ = getInstanciaJugamentoProcesso(julgamentoProcessoRJ, processo, ministroAutenticado, tipoVotoMinistro, sessao);
					
					// Registra o andamento "Incluído em Pauta" quando o ministro vota SIM na RJ
					VotoJulgamentoProcesso voto = julgamentoProcessoRJ.getVotoDoMinistro(ministroRelator);
					
					if (ministroAutenticado.getId().equals(ministroRelator.getId()) && voto != null && voto.getTipoVoto().getId().equals(TipoVotoConstante.SIM.getCodigo())) {
						IConsultaDePautaDeJulgamento consulta = new ConsultaDePautaDeJulgamento(julgamentoProcessoRJ.getObjetoIncidente().getId(), EstruturaPublicacao.COD_CAPITULO_PLENARIO, TipoJulgamentoVirtual.REPERCUSSAO_GERAL.getCodigo());
						List<Agendamento> agendamentos = agendamentoService.consultaAgendamentosParaPauta(consulta);
						
						if (agendamentos.size() == 0) {
							incidenteJulgamentoService.pautarRJ((julgamentoProcessoRJ.getObjetoIncidente()));
							incluirAndamentoProcesso(julgamentoProcessoRJ.getObjetoIncidente(),julgamentoProcessoRJ.getSessao(), usuario);
						}
					}
				}
			}
			
			if (ministroAutenticado.getId().equals(ministroRelator.getId()) && texto != null) {
				if (texto.getArquivoEletronico() != null) {
					arquivoEletronicoService.salvar(texto.getArquivoEletronico());
					texto.setCodigoBrs(texto.getArquivoEletronico().getId().intValue());
				}
				texto.setObjetoIncidente(julgamentoProcessoRG.getIncidenteJulgamento());
				textoService.salvar(texto);
			}

		} catch (RepercussaoGeralException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		}
		return true;
	}
	
	public void incluirAndamentoProcesso(ObjetoIncidente<?> objetoIncidente, Sessao sessao, UsuarioEGab usuario) throws ServiceException {
		Long numeroUltimoAndamento = andamentoProcessoService.recuperarProximoNumeroSequencia(objetoIncidente);
		Date dataAtual = new Date();
		AndamentoProcesso andamentoProcesso = new AndamentoProcesso();
		andamentoProcesso.setCodigoAndamento(AndamentoProcesso.COD_ANDAMENTO_INCLUA_EM_PAUTA_MIN_EXT);
		andamentoProcesso.setObjetoIncidente(objetoIncidente);
		andamentoProcesso.setCodigoUsuario(usuario.getUsername());
		andamentoProcesso.setDataAndamento(dataAtual);
		andamentoProcesso.setDataHoraSistema(dataAtual);
		andamentoProcesso.setOrigemAndamentoDecisao(origemAndamentoDecisaoService.recuperarPorId(ConstanteOrigemDecisao.PLENARIO_VIRTUAL.getCodigo()));
		andamentoProcesso.setNumeroSequencia(numeroUltimoAndamento);
		andamentoProcesso.setSetor(usuario.getSetor());
		
		Calendar prazoSustentacaoOral = DataUtil.date2Calendar(sessao.getDataPrevistaInicio());
		prazoSustentacaoOral.add(Calendar.DAY_OF_MONTH, -2);
		prazoSustentacaoOral.add(Calendar.SECOND, -1);
		
		String dataLimite = DataUtil.date2String(prazoSustentacaoOral.getTime(), false);
		
		String observacao = "Julgamento Virtual da Repercussão Geral: " + getSiglaCadeiaIncidente(objetoIncidente) + " - Agendado para: "+ DataUtil.date2String(sessao.getDataPrevistaInicio(), true) + ", podendo os advogados e procuradores apresentar sustentações orais até às 23h59 do dia "+dataLimite+".";
		
		andamentoProcesso.setDescricaoObservacaoAndamento(observacao);
		
		andamentoProcessoService.incluir(andamentoProcesso);
	}
	
	private String getSiglaCadeiaIncidente(ObjetoIncidente<?> objetoIncidente) {
		String siglaCadeiaIncidente = "";
		if (objetoIncidente instanceof RecursoProcesso) {
			siglaCadeiaIncidente =  ((RecursoProcesso) objetoIncidente).getSiglaCadeiaIncidente();
		} else if (objetoIncidente instanceof IncidenteJulgamento) {
			siglaCadeiaIncidente =  ((IncidenteJulgamento) objetoIncidente).getSiglaCadeiaIncidente();
		} else if (objetoIncidente instanceof Processo) {
			siglaCadeiaIncidente =  ((Processo) objetoIncidente).getSiglaClasseProcessual();
		}
		return siglaCadeiaIncidente;
	}

	/**
	 * Método responsavel por montar um instância para persistir o objeto
	 * JulgamentoProcesso
	 * 
	 * @param julgamentoProcesso
	 * @param tipoJulgamento
	 * @param processo
	 * @param ministroAutenticado
	 * @param votoMinistro
	 * @param agendamento
	 * @return
	 * @throws ServiceException
	 * @throws RepercussaoGeralException
	 */
	private JulgamentoProcesso getInstanciaJugamentoProcesso(JulgamentoProcesso julgamentoProcesso, Processo processo, Ministro ministroAutenticado, VotoJulgamentoProcesso votoMinistro, Sessao sessao)
			throws ServiceException {
		
		julgamentoProcesso.setSessao(sessao);
		
		julgamentoProcesso = julgamentoProcessoService.salvar(julgamentoProcesso);

		if (julgamentoProcesso.getSituacaoAtual() == null)
			julgamentoProcesso.adicionarSituacaoJulgamento(tipoSituacaoJulgamentoService.recuperarPorId(TipoSitucacaoJulgamentoConstant.AGENDADO.getCodigo()));
		
		if (TipoSitucacaoJulgamentoConstant.AGENDADO.getCodigo().equals(String.valueOf(julgamentoProcesso.getSituacaoAtual().getId())) && sessao.getDataInicio() != null)
			julgamentoProcesso.adicionarSituacaoJulgamento(tipoSituacaoJulgamentoService.recuperarPorId(TipoSitucacaoJulgamentoConstant.EM_ANDAMENTO.getCodigo()));
		
		
		VotoJulgamentoProcesso ultimoVotoMinistro = recuperaUltimoVotoMinistro(ministroAutenticado,	julgamentoProcesso.getListVotoJulgamentoProcessoValido());

		if (ultimoVotoMinistro == null || !ultimoVotoMinistro.getTipoVoto().equals(votoMinistro.getTipoVoto())) {

			if (ultimoVotoMinistro != null)
				ultimoVotoMinistro.setTipoSituacaoVoto(TipoSituacaoVoto.CANCELADO.getSigla());

			VotoJulgamentoProcesso voto = new VotoJulgamentoProcesso();
			voto.setDataVoto(Calendar.getInstance().getTime());
			voto.setJulgamentoProcesso(julgamentoProcesso);
			voto.setMinistro(ministroAutenticado);
			voto.setNumeroOrdemVotoSessao(recuperarProximaOrdemVoto(julgamentoProcesso.getListVotoJulgamentoProcessoValido()));
			voto.setTipoSituacaoVoto(TipoSituacaoVoto.VALIDO.getSigla());
			voto.setTipoVoto(votoMinistro.getTipoVoto());
			votoJulgamentoProcessoService.salvar(voto);
		}
		
		julgamentoProcessoService.refresh(julgamentoProcesso);

		return julgamentoProcesso;
	}

	/**
	 * Método para recuperar ou persistir o objeto incidente através do serviço
	 * da SSPJ
	 * 
	 * @param obj
	 * @param tipoRecurso
	 * @return
	 * @throws ServiceException
	 * @throws IncidenteJulgamentoException
	 */
	public IncidenteJulgamento recuperarIncidenteJulgamento(ObjetoIncidente obj, String sigTipoRecurso) throws ServiceException, IncidenteJulgamentoException {

		IncidenteJulgamento incidente = recuperarIncidente(obj, sigTipoRecurso);

		if (incidente != null) {
			return incidente;
		} else {

			IncidenteJulgamento incidenteRG = recuperarIncidente(obj, TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL);
			
			if (incidenteRG == null)
				incidenteRG = (IncidenteJulgamento) insereIncidente(obj, TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL);

			if (sigTipoRecurso.equals(TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL))
				return incidenteRG;

			IncidenteJulgamento incidente2 = recuperarIncidente(obj, sigTipoRecurso);
			
			if (incidente2 == null && incidenteRG != null)
				incidente2 = (IncidenteJulgamento) insereIncidente(incidenteRG, sigTipoRecurso);

			return incidente2;
		}
	}

	/**
	 * Método para recuperar o incidente do julgamento
	 * @param obj
	 * @param sigTipoRecurso
	 * @return
	 * @throws ServiceException
	 */
	public IncidenteJulgamento recuperarIncidente(ObjetoIncidente obj, String sigTipoRecurso) throws ServiceException {
		List<IncidenteJulgamento> incidentes = incidenteJulgamentoService.pesquisar(obj.getId(), sigTipoRecurso);
		if (incidentes != null && incidentes.size() > 0) {
			return incidentes.get(0);
		}
		return null;
	}
	
	/**
	 * Recuperar o tipo de recurso
	 * @param obj
	 * @param siglaTipoRecurso
	 * @return
	 * @throws ServiceException
	 * @throws IncidenteJulgamentoException
	 */
	public IncidenteJulgamento insereIncidente(ObjetoIncidente obj, String siglaTipoRecurso) throws ServiceException, IncidenteJulgamentoException {
		TipoRecurso tipoRecurso = tipoRecursoService.recuperarTipoRecurso(siglaTipoRecurso);
		return (IncidenteJulgamento) incidenteJulgamentoService.inserirIncidenteJulgamento(obj.getId(), tipoRecurso.getId(), 1);
	}

	/**
	 * metodo responsavel por recuperar a próxima ordem do voto a ser cadastrado
	 * 
	 * @author GuilhermeA
	 */
	public Long recuperarProximaOrdemVoto(
			List<VotoJulgamentoProcesso> listaVotoMinistro)
			throws ServiceException {
		try {

			long maiorValor = 0;
			if (listaVotoMinistro != null && listaVotoMinistro.size() > 0) {
				for (VotoJulgamentoProcesso voto : listaVotoMinistro) {
					if (maiorValor < voto.getNumeroOrdemVotoSessao()) {
						maiorValor = voto.getNumeroOrdemVotoSessao();
					}
				}

			}
			return maiorValor + 1;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * recupera o ultimo voto do ministro para aquele processo
	 * 
	 * @param ministro
	 * @param listaVotoMinistro
	 * @return
	 * @throws ServiceException
	 */
	public VotoJulgamentoProcesso recuperaUltimoVotoMinistro(Ministro ministro, List<VotoJulgamentoProcesso> listaVotoMinistro) throws ServiceException {

		try {

			if (listaVotoMinistro != null && listaVotoMinistro.size() > 0) {
				for (VotoJulgamentoProcesso voto : listaVotoMinistro) {
					if (voto.getMinistro() != null && voto.getMinistro().getId().equals(ministro.getId())) {
						return voto;
					}
				}
			}

			return null;
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * instancia uma novo sessao para o julgamento no plenário virtual.
	 * 
	 * @param ministroRelator
	 * @param ignorarCpc 
	 * @return
	 * @throws ServiceException
	 */
	public Sessao getInstanciaSessao(Ministro ministroRelator, JulgamentoProcesso julgamento, Boolean ignorarCpc) throws ServiceException {
		try {
			Sessao sessao = julgamento.getSessao();
			
			if (sessao == null) {
				Colegiado colegiado = colegiadoService.recuperarPorId(Colegiado.TRIBUNAL_PLENO);
				sessao = sessaoService.recuperarSessao(Calendar.getInstance(), colegiado, ignorarCpc, TipoJulgamentoVirtual.REPERCUSSAO_GERAL);
				sessao.setDataInicio(sessao.getDataPrevistaInicio());
				sessao.setListaEnvolvidoSessao(recuperarMinistrosEnvolvidos(sessao));
				sessao.setColegiado(colegiado);
			}
			
			if (sessao.getId() != null)
				return sessao;

			if (sessao.getListaEnvolvidoSessao() == null || sessao.getListaEnvolvidoSessao().size() == 0)
				sessao.setListaEnvolvidoSessao(recuperarMinistrosEnvolvidos(sessao));
			
			return sessao;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * recuperar os ministros que participarão da sessão.
	 * 
	 * @param ministroRelator
	 * @param sessao
	 * @return
	 * @throws ServiceException
	 * @author GuilhermeA
	 */
	public List<EnvolvidoSessao> recuperarMinistrosEnvolvidos(Sessao sessao) throws ServiceException {
		try {

			List<EnvolvidoSessao> lista = new LinkedList<EnvolvidoSessao>();
			List<Ministro> listaMinistro = ministroService.pesquisarMinistros(true, false, null, null, null);
			TipoCompetenciaEnvolvido competenciaPresidente = tipoCompetenciaEnvolvidoService.recuperarPorId(TipoAtuacaoConstante.MINISTRO_PRESIDENTE.getCodigo());
			TipoCompetenciaEnvolvido competenciaMinistro = tipoCompetenciaEnvolvidoService.recuperarPorId(TipoAtuacaoConstante.MINISTRO.getCodigo());
			MinistroPresidente ministroPresidente = ministroPresidenteService.recuperarMinistroPresidenteAtual();

			if (listaMinistro != null && listaMinistro.size() > 0) {
				for (Ministro ministro : listaMinistro) {
					EnvolvidoSessao envolvido = new EnvolvidoSessao();
					envolvido.setMinistro(ministro);
					envolvido.setPresente(true);
					envolvido.setSessao(sessao);
					envolvido.setTipoCompetenciaEnvolvido(ministro.getId().equals(ministroPresidente.getId().getMinistro().getId()) ? competenciaPresidente : competenciaMinistro);
					lista.add(envolvido);
				}

			}

			return lista;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * método responsavel por verificar quais processo irão finalizar durante o
	 * recesso e com isso o sistema sistema irá prorrogar o prazo para depois do
	 * recesso
	 * 
	 * @author GuilhermeA
	 */
	public Boolean persistirRepercussaoGeralJulgamentoRecesso() throws ServiceException {
		try {
			Date dataInicioRecesso = recuperarData(true);
			Date dataFimRecesso = recuperarData(false);

			if (dataInicioRecesso != null && dataFimRecesso != null) {
				List<JulgamentoProcesso> lista = dao.pesquisarRepercussaoGeralRecesso(dataInicioRecesso, dataFimRecesso);

				if (lista != null && lista.size() > 0) {

					for (JulgamentoProcesso julgamento : lista) {

						Calendar cal = Calendar.getInstance();
						cal.setTime(dataFimRecesso);
						cal.add(Calendar.DATE, recuperarDiasRestantes(julgamento.getSessao(), dataInicioRecesso));
						julgamento.getSessao().setDataPrevistaFim(DateTimeHelper.getDataHora(DateTimeHelper.getDataString(cal.getTime()) + " 23:59:59"));
						sessaoService.salvar(julgamento.getSessao());

					}
					return true;
				}
			}

		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return false;
	}

	/**
	 * método responsalvel por recuperar a quantidade de dias restante para a
	 * finalização do processo
	 * 
	 * @param sessao
	 *            sessao de julgamento
	 * @param dataAtual
	 * @return
	 */

	protected int recuperarDiasRestantesNotificacao(Sessao sessao) throws ServiceException {
		try {

			if (sessao != null && sessao.getDataPrevistaFim() != null) {

				Date dataFim = sessao.getDataFim() != null ? sessao.getDataFim() : sessao.getDataPrevistaFim();
				Date dataPrevistaTruncada = DateUtils.truncate(dataFim, Calendar.DAY_OF_MONTH);
				Date dataAtualTruncada = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);

				if (dataPrevistaTruncada.compareTo(dataAtualTruncada) == 1) {

					long diferenca = dataPrevistaTruncada.getTime() - dataAtualTruncada.getTime();

					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(diferenca);

					int diasRestantes = calendar.get(Calendar.DAY_OF_YEAR);

					return diasRestantes;

				} else if (dataPrevistaTruncada.compareTo(dataAtualTruncada) == 0 && sessao.getDataFim() == null) {
					return 0;
				}
			}
			return 20;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	private int recuperarDiasRestantes(Sessao sessao, Date dataInicioRecesso) throws ServiceException {
		try {

			if (sessao != null && sessao.getDataPrevistaFim() != null) {

				Date dataPrevistaTruncada = DateUtils.truncate(sessao.getDataPrevistaFim(), Calendar.DAY_OF_MONTH);

				Date dataInicioRecessoTruncada = DateUtils.truncate(dataInicioRecesso, Calendar.DAY_OF_MONTH);

				if (dataPrevistaTruncada.compareTo(dataInicioRecesso) == 1) {
					long diferenca = dataPrevistaTruncada.getTime() - dataInicioRecessoTruncada.getTime();
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(diferenca);
					int diasRestantes = calendar.get(Calendar.DAY_OF_YEAR);

					return diasRestantes;
				} else if (dataPrevistaTruncada.compareTo(dataInicioRecesso) == 0) {
					return 1;
				}
			}
			return 0;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * metodo responsavel por recuperar os processos agendado e iniciar a
	 * analise de repercussão geral na proxima data que é permitida a liberação
	 * 
	 * @author GuilhermeA
	 */
	public void persistirRepercussaoGeralJulgamentoAgendado(Date dataAgendado) throws ServiceException {
		try {

			Date inicio = new Date(dataAgendado.getTime());
			inicio.setHours(0);
			inicio.setMinutes(0);
			inicio.setSeconds(0);
			
			Date fim = new Date(dataAgendado.getTime());
			fim.setHours(23);
			fim.setMinutes(59);
			fim.setSeconds(59);
					
			JulgamentoProcessoSearchData searchData = new JulgamentoProcessoSearchData();
			searchData.dataPrevistaInicioSessaoDateRange = new SearchData.DateRange();
			searchData.dataPrevistaInicioSessaoDateRange.initialDate = inicio;
			searchData.dataPrevistaInicioSessaoDateRange.finalDate = fim;
			searchData.tipoAmbiente = TipoAmbienteConstante.VIRTUAL;
			searchData.tipoSituacaoJulgamento = TipoSitucacaoJulgamentoConstant.AGENDADO;

			SearchResult<JulgamentoProcesso> result = julgamentoProcessoService.pesquisarJulgamentoProcesso(searchData);

			if (result != null && result.getTotalResult() > 0) {

				List<JulgamentoProcesso> lista = (List<JulgamentoProcesso>) result.getResultCollection();

				for (JulgamentoProcesso julgamento : lista) {

					julgamento.adicionarSituacaoJulgamento(tipoSituacaoJulgamentoService.recuperarPorId(TipoSitucacaoJulgamentoConstant.EM_ANDAMENTO.getCodigo()));
					julgamento.getSessao().setDataInicio(julgamento.getSessao().getDataPrevistaInicio());
					
					//Exclui as informações de destaque e vista ao iniciar um novo julgamento
					julgamento.setMinistroDestaque(null);
					julgamento.setMinistroVista(null);
					julgamento.setSituacaoProcessoSessao(TipoSituacaoProcessoSessao.NAO_JULGADO);
					
					julgamentoProcessoService.salvar(julgamento);
					if (julgamento.getTipoJulgamento().equals(TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL) || TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL_SEGUNDO_JULGAMENTO.equals(julgamento.getTipoJulgamento())) {
						persistirAndamentoProcesso(julgamento, null, false, false);
						persistirTemaRepercussaoGeral(julgamento.getObjetoIncidente(), (Processo) julgamento.getObjetoIncidente().getPrincipal());
					}
				}

			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * metodo responsavel por relacionar o julgamento processo com o andamento
	 * processo de DECISAO_EXISTENCIA_REPERCUSSAO_GERAL e
	 * DECISAO_INEXISTENCIA_REPERCUSSAO_GERAL
	 * 
	 * @param andamento
	 * @throws ServiceException
	 */
	public void persistirJulgamentoProcessoAndamento(AndamentoProcesso andamento) throws ServiceException {

		try {

			JulgamentoProcesso julgamento = julgamentoProcessoService.recuperarJulgamentoProcesso(null, andamento.getObjetoIncidente().getId(),
					TipoAmbienteConstante.VIRTUAL, Colegiado.TRIBUNAL_PLENO);
			if (julgamento != null) {
				julgamento.setAndamentoProcesso(andamento);
				julgamento.adicionarSituacaoJulgamento(tipoSituacaoJulgamentoService.recuperarPorId(TipoSitucacaoJulgamentoConstant.FINALIZADO.getCodigo()));
				julgamentoProcessoService.salvar(julgamento);
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public SearchResult pesquisarRepercussaoGeralSQL(RepercussaoGeralSearchData searchData, String ordem) throws ServiceException {
		try {
			return dao.pesquisarRepercussaoGeralSQL(searchData, ordem);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * método responsavel por pesquisar os processo da repercussão geral
	 * 
	 * @author GuilhermeA
	 * TODO: Verificar se esse método é utilizado em algum sistema de gabinetes e julgamentos. Se não for, deve ser excluído no DAO também. Está gerando dificuldade de manutenção.
	 */
	
	public SearchResult<Processo> pesquisarRepercussaoGeral(RepercussaoGeralSearchData searchData) throws ServiceException {
		try {

			return dao.pesquisarRepercussaoGeral(searchData);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * método responsavel por pesquisar os processo da repercussão geral
	 * 
	 * @author GuilhermeA
	 */
	public SearchResult<Processo> pesquisarRepercussaoGeralPlenarioVirtual(RepercussaoGeralSearchData searchData) throws ServiceException {
		try {

			return dao.pesquisarRepercussaoGeralPlenarioVirtual(searchData);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * metodo responsavel por registrar o andamento de inicio
	 * 
	 * @author ViniciusK,guilhermea
	 */
	public Boolean persistirAndamentoFimRepercusaoGeral() throws ServiceException {
		Boolean registrado = false;
		try {

			List<JulgamentoProcesso> lista = dao.pesquisarRepercussaoGeralFinalizadosSemAndamento();

			if (lista != null && lista.size() > 0) {
				for (JulgamentoProcesso julgamentoProcesso : lista) {

					if (julgamentoProcesso.getTipoJulgamento().equals(TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL)
							|| julgamentoProcesso.getTipoJulgamento().equals(TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL_SEGUNDO_JULGAMENTO)) {

						// julgamento da repercussao geral
						JulgamentoProcesso julgamentoProcessoRG = julgamentoProcesso;

						// recuperar o julgamento da questão constitucional
						JulgamentoProcesso julgamentoProcessoQCRG = recuperarJulgamentoProcesso(julgamentoProcessoRG, lista, TipoIncidenteJulgamento.SIGLA_QUESTAO_CONSTITUCIONAL);

						if (julgamentoProcessoQCRG == null)
							julgamentoProcessoQCRG = recuperarJulgamentoProcesso(julgamentoProcessoRG, lista, TipoIncidenteJulgamento.SIGLA_QUESTAO_CONSTITUCIONAL_SEGUNDO_JULGAMENTO);
						
						// recuperar o julgamento da reafirmação de jurisprudência
						JulgamentoProcesso julgamentoProcessoRJ = recuperarJulgamentoProcesso(julgamentoProcessoRG, lista, TipoIncidenteJulgamento.SIGLA_MERITO);

						// recuperar o resultado do julgamento da questão constitucional
						Decisao decisao = instanciaDecisao(julgamentoProcessoQCRG, julgamentoProcessoRG, julgamentoProcessoRJ);

						if (!decisao.decisaoQC.equals(Decisao.TipoDecisao.EM_ABERTO) && !decisao.decisaoRG.equals(Decisao.TipoDecisao.EM_ABERTO)) {

							// verifica se existe votacao de merito
							boolean meritoJulgado;
							if (julgamentoProcessoRJ != null) {
								if (decisao.decisaoRJ.equals(Decisao.TipoDecisao.SIM_RJ)){
									meritoJulgado = true;
								}else{
									meritoJulgado = false;
								}
							} else {
								meritoJulgado = false;
							}

							// persiste o andamento para o julgamento da RG e QC
							AndamentoProcesso andamento = persistirAndamentoProcesso(julgamentoProcessoRG, decisao, true, meritoJulgado);

							if (andamento != null) {
								// finaliza o julgamento da questão constitucional
								registrado = persistirJulgamentoProcesso(julgamentoProcessoQCRG, andamento, TipoSitucacaoJulgamentoConstant.FINALIZADO);

								// finaliza o julgamento da repercussão geral
								registrado = persistirJulgamentoProcesso(julgamentoProcessoRG, andamento, TipoSitucacaoJulgamentoConstant.FINALIZADO);

								// finaliza o julgamento da reafirmação de jurisprudência
								registrado = persistirJulgamentoProcesso(julgamentoProcessoRJ, andamento, TipoSitucacaoJulgamentoConstant.FINALIZADO);
							}
							controleVotoTextoService.criarControleVotoRepercussaoGeral(julgamentoProcessoRG.getIncidenteJulgamento().getId());
						} else {
							AndamentoProcesso andamento = new AndamentoProcesso();
							andamento.setCodigoAndamento(Andamento.SUSPENSAO_E_REINICIO_DE_JULGAMENTO.getId());

							ObjetoIncidente<?> objetoIncidentePrincipal = julgamentoProcesso.getObjetoIncidente().getPrincipal();

							andamento.setObjetoIncidente(objetoIncidentePrincipal);
							Ministro ministroRelator = situacaoMinistroProcessoService.recuperarMinistroRelatorAtual((Processo) objetoIncidentePrincipal);
							andamento.setSetor(ministroRelator.getSetor());
							andamento.setDescricaoObservacaoAndamento(montarTextoAndamentoJulgamentoReiniciado(decisao));
							OrigemAndamentoDecisao origem = origemAndamentoDecisaoService.recuperarPorId(ConstanteOrigemDecisao.PLENARIO_VIRTUAL.getCodigo());
							andamento.setOrigemAndamentoDecisao(origem);
							andamento.setDataAndamento(Calendar.getInstance().getTime());
							andamento.setDataHoraSistema(Calendar.getInstance().getTime());
							andamento.setNumeroSequencia(andamentoProcessoService.recuperarProximoNumeroSequencia((Processo) objetoIncidentePrincipal));
							andamento.setLancamentoIndevido(Boolean.FALSE);
							andamentoProcessoService.salvar(andamento);
							
							registrado = persistirJulgamentoProcesso(julgamentoProcessoRG, andamento, TipoSitucacaoJulgamentoConstant.EM_ANDAMENTO);
							registrado = persistirJulgamentoProcesso(julgamentoProcessoQCRG, andamento, TipoSitucacaoJulgamentoConstant.EM_ANDAMENTO);
							registrado = persistirJulgamentoProcesso(julgamentoProcessoRJ, andamento, TipoSitucacaoJulgamentoConstant.EM_ANDAMENTO);
						}
					}
				}

			}

		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return registrado;
	}

	/**
	 * método responsavel por alterar o julgamento processo após finalizar o
	 * prazo da repercussão geral.
	 * 
	 * @param julgamentoProcesso
	 * @param andamento
	 * @param tipoSituacao
	 * @return
	 * @throws ServiceException
	 */
	private Boolean persistirJulgamentoProcesso(JulgamentoProcesso julgamentoProcesso, AndamentoProcesso andamento, TipoSitucacaoJulgamentoConstant tipoSituacao) throws ServiceException {
		try {

			if (julgamentoProcesso != null) {
				if (andamento != null)
					julgamentoProcesso.setAndamentoProcesso(andamento);

				if (tipoSituacao.equals(TipoSitucacaoJulgamentoConstant.FINALIZADO))
					julgamentoProcesso.getSessao().setDataFim(julgamentoProcesso.getSessao().getDataPrevistaFim());
				
				if (tipoSituacao.equals(TipoSitucacaoJulgamentoConstant.EM_ANDAMENTO)) {
					
					GregorianCalendar hoje = new GregorianCalendar();
					GregorianCalendar dataSessao = new GregorianCalendar();
					dataSessao.setTime(julgamentoProcesso.getSessao().getDataPrevistaFim());
					
					if (hoje.after(dataSessao)) {
						int dias = 6; // quantidade de dias a serem adicionados na dataPrevistaFim da sessão de julgamento
						dataSessao.add(Calendar.DAY_OF_MONTH, dias);
						julgamentoProcesso.getSessao().setDataPrevistaFim(dataSessao.getTime());
					}
				}
				
				julgamentoProcesso.adicionarSituacaoJulgamento(tipoSituacaoJulgamentoService.recuperarPorId(tipoSituacao.getCodigo()));
				julgamentoProcessoService.salvar(julgamentoProcesso);
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		return true;
	}

	/**
	 * metodo responsavel por recuperar o julgamento do processo da questão
	 * constitucional
	 * 
	 * @param julgamentoProcessoRG
	 * @param lista
	 * @return
	 * @author ViniciusK,Guilhermea
	 */
	public JulgamentoProcesso recuperarJulgamentoProcesso(JulgamentoProcesso julgamentoProcessoRG, List<JulgamentoProcesso> lista, String tipoJulgamento) throws ServiceException {
		for (JulgamentoProcesso julgamento : lista)
			if (julgamento.getSessao().getId() == julgamentoProcessoRG.getSessao().getId() && julgamento.getTipoJulgamento().equals(tipoJulgamento))
				return julgamento;

		return null;
	}

	/**
	 * método responsael por instanciar o andamento processo
	 * 
	 * @param julgamentoProcesso
	 * @param andamentoFim
	 * @return
	 * @throws ServiceException
	 */
	public AndamentoProcesso persistirAndamentoProcesso(JulgamentoProcesso julgamentoProcesso, Decisao decisao, boolean andamentoFim, boolean meritoJulgado)
			throws ServiceException {
		try {

			ConstanteAndamento constanteAndamento = null;
			constanteAndamento = recuperarConstanteAndamento(decisao, andamentoFim, meritoJulgado);

			if (constanteAndamento != null
					&& (julgamentoProcesso.getTipoJulgamento().equals(TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL) 
							|| julgamentoProcesso.getTipoJulgamento().equals(TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL_SEGUNDO_JULGAMENTO))) {

				AndamentoProcesso andamento = new AndamentoProcesso();
				andamento.setCodigoAndamento(constanteAndamento.getCodigo());

				ObjetoIncidente<?> objetoIncidentePrincipal = julgamentoProcesso.getObjetoIncidente().getPrincipal();

				andamento.setObjetoIncidente(objetoIncidentePrincipal);
				Ministro ministroRelator = situacaoMinistroProcessoService.recuperarMinistroRelatorAtual((Processo) objetoIncidentePrincipal);
				andamento.setSetor(ministroRelator.getSetor());
				if (andamentoFim) {
					andamento.setDescricaoObservacaoAndamento(montarTextoDecisaoJulgamento(decisao));
					OrigemAndamentoDecisao origem = origemAndamentoDecisaoService.recuperarPorId(ConstanteOrigemDecisao.PLENARIO_VIRTUAL.getCodigo());
					if (origem == null) {
						throw new ServiceException("Origem de decisão não encontrada para gerar o andamento.");
					} else {
						andamento.setOrigemAndamentoDecisao(origem);
					}
				}
				andamento.setDataAndamento(Calendar.getInstance().getTime());
				andamento.setDataHoraSistema(Calendar.getInstance().getTime());
				andamento.setNumeroSequencia(andamentoProcessoService.recuperarProximoNumeroSequencia((Processo) objetoIncidentePrincipal));
				andamento.setLancamentoIndevido(Boolean.FALSE);
				andamentoProcessoService.salvar(andamento);
				
				return andamento;
			}

		} catch (ServiceException e) {
			throw e;
		}
		return null;
	}

	/**
	 * recuperar o andamento que deve ser laçado para o processo;
	 * 
	 * @param julgamentoProcesso
	 * @param andamentoFim
	 * @return
	 * @throws ServiceException
	 */
	private ConstanteAndamento recuperarConstanteAndamento(Decisao decisao, boolean andamentoFim, boolean merito) throws ServiceException {
		try {

			if (andamentoFim) {

				// quando a matéria for considerada constitucional,
				// irá retornar o andamento de acordo com a decisão da
				// repercussão geral (há,Não há)
				if (decisao.decisaoQC.equals(Decisao.TipoDecisao.HA_QC)) {
					if (merito && decisao.decisaoRG.equals(Decisao.TipoDecisao.HA_RG)){
						return ConstanteAndamento.DECISAO_EXISTENCIA_REPERCUSSAO_GERAL_JULGADO_MERITO;
					}else if (decisao.decisaoRG.equals(Decisao.TipoDecisao.HA_RG)){
						return ConstanteAndamento.DECISAO_EXISTENCIA_REPERCUSSAO_GERAL;
					}else if (decisao.decisaoRG.equals(Decisao.TipoDecisao.NAO_HA_RG)){
						return ConstanteAndamento.DECISAO_INEXISTENCIA_REPERCUSSAO_GERAL;
					}
					// quando a matéria for considerada infraconstitucional
					// automenticamente a é considerado que não existe
					// repercussão geral.
				} else if (decisao.decisaoQC.equals(Decisao.TipoDecisao.NAO_HA_QC) && decisao.decisaoRG.equals(Decisao.TipoDecisao.NAO_HA_RG)) {
					return ConstanteAndamento.DECISAO_INEXISTENCIA_CONTITUCIONAL_REPERCUSSAO_GERAL;
				}

			} else {
				return ConstanteAndamento.INICIADA_ANALISE_REPERCUSSAO_GERAL;
			}

			return null;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * método responsavel por recuperar se há ou não constitucionalidade
	 * 
	 * @param julgamentoProcessoQCRG
	 * @return
	 * @throws ServiceException
	 * @author ViniciusK,guilhermea
	 */
	private Decisao instanciaDecisao(JulgamentoProcesso julgamentoProcessoQCRG, JulgamentoProcesso julgamentoProcessoRG, JulgamentoProcesso julgamentoProcessoRJ) throws ServiceException {
		try {
			Decisao decisao = new Decisao();
			julgamentoProcessoRG.setObjetoIncidente(objetoIncidenteService.deproxy(julgamentoProcessoRG.getObjetoIncidente()));
			Ministro ministroRelator = ministroService.recuperarPorId(julgamentoProcessoRG.getIncidenteJulgamento().getPrincipal().getRelatorIncidenteId());

			// Se o processo estiver registrado a Presidência o sistema carrega o Ministro Presidente como relator
			if (Ministro.COD_MINISTRO_PRESIDENTE.equals(ministroRelator.getId()))
				ministroRelator = ministroService.recuperarMinistroPresidente(julgamentoProcessoRG.getSessao().getDataPrevistaFim());

			decisao.ministroRelator = ministroRelator;
			decisao.ministroRedator = ministroService.recuperarRedatorAcordao(julgamentoProcessoRG.getIncidenteJulgamento());
			decisao.ministrosDivergentes = dao.recuperarMinistroSemManifestacaoComTexto(julgamentoProcessoRG.getObjetoIncidente().getId());

			// calcula a quantidade de votos da Questão Constitucional
			for (VotoJulgamentoProcesso voto : julgamentoProcessoQCRG.getListVotoJulgamentoProcessoValido()) {
				if (voto.getTipoVoto().getId().equals(TipoVotoConstante.HA.getCodigo()))
					decisao.ministrosHaQC.add(voto.getMinistro());
				else if (voto.getTipoVoto().getId().equals(TipoVotoConstante.NAO_HA.getCodigo()))
					decisao.ministrosNaoHaQC.add(voto.getMinistro());
				else if (voto.getTipoVoto().getId().equals(TipoVotoConstante.IMPEDIDO.getCodigo()))
					decisao.ministrosImpedidosQC.add(voto.getMinistro());
			}

			// Contando os votos da Repercussão Geral
			for (VotoJulgamentoProcesso voto : julgamentoProcessoRG.getListVotoJulgamentoProcessoValido()) {
				if (voto.getTipoVoto().getId().equals(TipoVotoConstante.HA.getCodigo()))
					decisao.ministrosHaRG.add(voto.getMinistro());
				else if (voto.getTipoVoto().getId().equals(TipoVotoConstante.NAO_HA.getCodigo()))
					decisao.ministrosNaoHaRG.add(voto.getMinistro());
				else if (voto.getTipoVoto().getId().equals(TipoVotoConstante.IMPEDIDO.getCodigo()))
					decisao.ministrosImpedidosRG.add(voto.getMinistro());
			}
			
			// Contabiliza os votos da Reafirmação de Jurisprudência
			if (julgamentoProcessoRJ != null)
				for (VotoJulgamentoProcesso voto : julgamentoProcessoRJ.getListVotoJulgamentoProcessoValido()) {
					if (voto.getTipoVoto().getId().equals(TipoVotoConstante.SIM.getCodigo()))
						decisao.ministrosSimRJ.add(voto.getMinistro());
					else if (voto.getTipoVoto().getId().equals(TipoVotoConstante.NAO.getCodigo()))
						decisao.ministrosNaoRJ.add(voto.getMinistro());
					else if (voto.getTipoVoto().getId().equals(TipoVotoConstante.IMPEDIDO.getCodigo()))
						decisao.ministrosImpedidosRJ.add(voto.getMinistro());
				}
			
			decisao.ministrosAusentesQC = recuperarMinistrosSemManifestacao((ObjetoIncidente<Processo>) julgamentoProcessoRG.getIncidenteJulgamento().getPrincipal(), julgamentoProcessoQCRG.getSessao(), true);
			decisao.ministrosAusentesRG = recuperarMinistrosSemManifestacao((ObjetoIncidente<Processo>) julgamentoProcessoRG.getIncidenteJulgamento().getPrincipal(), julgamentoProcessoRG.getSessao(), false);
			
			if (julgamentoProcessoRJ != null)
				decisao.ministroAusentesRJ = recuperarMinistrosSemManifestacao((ObjetoIncidente<Processo>) julgamentoProcessoRJ.getObjetoIncidente(), julgamentoProcessoRJ.getSessao(), false);

			// se o ministro manifestar de forma divergente o seu voto divergente desconsiderará o voto de sem manifestação.
			if (decisao.getPossuiDivergente()) {
				decisao.ministrosAusentesQC.removeAll(decisao.ministrosDivergentes);
				decisao.ministrosAusentesRG.removeAll(decisao.ministrosDivergentes);
				decisao.ministroAusentesRJ.removeAll(decisao.ministrosDivergentes);
			}
			
			decisao.decisaoQC = placarQC(decisao.ministrosHaQC.size(), decisao.ministrosNaoHaQC.size(), decisao.ministrosImpedidosQC.size(), decisao.ministrosAusentesQC.size());
			decisao.decisaoRG = placarRG(decisao.ministrosHaRG.size(), decisao.ministrosNaoHaRG.size(), decisao.ministrosImpedidosRG.size(), decisao.ministrosAusentesRG.size());
			decisao.decisaoRJ = placarRJ(decisao.ministrosSimRJ.size(), decisao.ministrosNaoRJ.size(), decisao.ministrosImpedidosRJ.size(), decisao.ministroAusentesRJ.size());

			if (decisao.decisaoQC.equals(Decisao.TipoDecisao.NAO_HA_QC)) {
				decisao.decisaoRG = Decisao.TipoDecisao.NAO_HA_RG;
				decisao.decisaoRJ = Decisao.TipoDecisao.NAO_RJ;
			} 
			
			if (decisao.decisaoQC.equals(Decisao.TipoDecisao.HA_QC) && decisao.decisaoRG.equals(Decisao.TipoDecisao.NAO_HA_RG))
				decisao.decisaoRJ = Decisao.TipoDecisao.NAO_RJ;
			
			return decisao;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Para o naoHa ganhar ele precisa ter maioria absoluta. 
	 * O julgamento ficará em aberto caso o naoHa puder chegar 
	 * à maioria absoluta quando considerados os ausentes
	 * @param ha é a quantidade de ministros que votaram dizendo que HÁ questão constitucional
	 * @param naoHa é a quantidade de ministros que votaram dizendo que NÃO HÁ questão constitucional
	 * @param impedidos é a quantidade de ministros impedidos
	 * @param ausentes é a quantidade de minsitros ausentes
	 * @return TipoDecisao informando se há ou não questão constitucional ou se a decisão ficou em aberto
	 */
	public static Decisao.TipoDecisao placarQC(int ha, int naoHa, int impedidos, int ausentes) {
		int maioria=6;
		
		if (naoHa>=maioria)
			return Decisao.TipoDecisao.NAO_HA_QC;
		else if(ha>=maioria)
			return Decisao.TipoDecisao.HA_QC; 
		else
			return Decisao.TipoDecisao.EM_ABERTO;
	}
	
	public static Decisao.TipoDecisao placarRG(int ha, int naoHa, int impedidos, int ausentes) {
		int maioria=8;
		
		if (naoHa>=maioria)
			return Decisao.TipoDecisao.NAO_HA_RG;
		else if (naoHa+ausentes>=maioria)
			return Decisao.TipoDecisao.EM_ABERTO;
		else
			return Decisao.TipoDecisao.HA_RG;
	}
	
	public static Decisao.TipoDecisao placarRJ(int ha, int naoHa, int impedidos, int ausentes) {
		int maioria=6;
		
		if (ha>=maioria)
			return Decisao.TipoDecisao.SIM_RJ;
		else if (ha+ausentes>=maioria)
			return Decisao.TipoDecisao.EM_ABERTO;
		else
			return Decisao.TipoDecisao.NAO_RJ;
	}

	/**
	 * metodo reponsável por recuperar o ministros que não se manifestram e que
	 * não estavam de licença.
	 * 
	 * @param processo
	 * @param sessao
	 * @return
	 * @throws ServiceException
	 * @author ViniciusK,Guilhermea
	 */
	private List<Ministro> recuperarMinistrosSemManifestacao(ObjetoIncidente<Processo> processo, Sessao sessao, boolean licenciado) throws ServiceException {

		List<Ministro> ministrosNaoManifesto = new LinkedList<Ministro>();
		
		try {
			// recupera os ministros que não se manifestaram
			ministrosNaoManifesto = dao.recuperarMinistrosNaoManifesto(processo.getId(), sessao.getId());

			Date dataFimJulgamento = DateTimeHelper.getData(DateTimeHelper.getDataString(sessao.getDataPrevistaFim()));
			if (ministrosNaoManifesto != null && ministrosNaoManifesto.size() > 0) {
				List<AfastamentoMinistroView> afastados = dao.recuperarAfastamentoMinistro(ministrosNaoManifesto, sessao.getDataPrevistaInicio());

				if (afastados != null && licenciado) {
					for (AfastamentoMinistroView afastamento : afastados) {

						Date dataFimAfastamento = DateTimeHelper.getData(DateTimeHelper.getDataString(afastamento.getDataFim()));

						// verifica se a data fim do avafastamento é menor ou igual a data fim do julgamento
						if (dataFimJulgamento.compareTo(dataFimAfastamento) == 0 || dataFimJulgamento.compareTo(dataFimAfastamento) == -1) {

							for (Ministro ministro : ministrosNaoManifesto) {
								// remove da lista de ministros que não se manifestaram o ministro que estiver de licença.
								if (ministro.getUsuario().getMatricula().equals(afastamento.getMatricula())) {
									ministrosNaoManifesto.remove(ministro);
									break;
								}
							}
						}
					}
				}
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return ministrosNaoManifesto;
	}

	// gera o texto para a repercussão geral
	private StringBuffer gerarTextoRepercussaoGeral(Decisao decisao) throws ServiceException {
		
		StringBuffer resultado = new StringBuffer("");
		
		boolean isUnanimidade = (decisao.getQtdHaRG() == 0 || decisao.getQtdNaoHaRG() == 0) ? true : false;

		// verificando a quantidade de votos não há
		if (decisao.getQtdNaoHaRG() == 0)
			resultado.append(TextoPadrao.RG_ZERO_VOTOS_NAO_HA.getTextoPadrao(isUnanimidade));
		
		if (decisao.getQtdNaoHaRG()>=1 && decisao.getQtdNaoHaRG()<=7) {
			resultado.append(TextoPadrao.RG_1_7_VOTOS_NAO_HA.getTextoPadrao(isUnanimidade));			
			 
			//recuperando os ministros vencidos. Como existiu RG os vencidos votaram NÃO HÁ RG
			resultado.append(", ");			
			resultado.append(recuperarNomeMinistros(decisao.ministrosNaoHaRG,decisao.ministroRelator, TipoRetornoMinistro.VENCIDO));
		} 
		
		if (decisao.getQtdNaoHaRG()>=8 && decisao.getQtdNaoHaRG()<=10) {
			resultado.append(TextoPadrao.RG_8_10_VOTOS_NAO_HA.getTextoPadrao(isUnanimidade));

			// recuperando os ministros vencidos. Como não existiu RG os vencidos votaram HÁ RG
			if (decisao.ministrosHaRG != null && decisao.ministrosHaRG.size()>0) {
				resultado.append(", ");			
				resultado.append(recuperarNomeMinistros(decisao.ministrosHaRG,decisao.ministroRelator, TipoRetornoMinistro.VENCIDO));
			} else {
				resultado.append(". ");
			} 									
		} 
		
		if (decisao.getQtdNaoHaRG()==11)
			resultado.append(TextoPadrao.RG_11_VOTOS_NAO_HA.getTextoPadrao(isUnanimidade));
			
		// Gerando o texto dos ministros que não se manifestaram - só gera o texto nos casos que não ocorrer a unanimidade
		if (decisao.getQtdSemManifestacaoRG() > 0)
			resultado.append(recuperarNomeMinistros(decisao.ministrosAusentesRG,decisao.ministroRelator,TipoRetornoMinistro.SEM_MANIFESTACAO));

		// Gerando o texto dos ministros impedidos - só gera o texto nos casos que não ocorrer a unanimidade 
		if (decisao.getQtdImpedidoRG() > 0)
			resultado.append(recuperarNomeMinistros(decisao.ministrosImpedidosRG,decisao.ministroRelator,TipoRetornoMinistro.IMPEDIDO));

		return resultado;
	}

	// gera os textos para as materias constitucionais
	private StringBuffer gerarTextoMateriaConstitucional(Decisao decisao) throws ServiceException {
		StringBuffer resultado = new StringBuffer("");
		
		boolean isUnanimidade = (decisao.getQtdHaQC() == 0 || decisao.getQtdNaoHaQC() == 0) ? true : false;
		
		// verificando a quantidade de votos não há - só computa os votos dos ausentes como NÃO HÁ QC quando o relator votou NÃO HÁ QC
			if (decisao.getQtdNaoHaQC() == 0)
				resultado.append(TextoPadrao.QC_ZERO_VOTOS_NAO_HA.getTextoPadrao(isUnanimidade));
			
			if (decisao.getQtdNaoHaQC()>=1 && decisao.getQtdNaoHaQC()<=5) {
				resultado.append(TextoPadrao.QC_1_5_VOTOS_NAO_HA.getTextoPadrao(isUnanimidade));
				 
				//recuperando os ministros vencidos. Como existiu QC os vencidos votaram NÃO HÁ QC
				resultado.append(", ");				
				resultado.append(recuperarNomeMinistros(decisao.ministrosNaoHaQC,decisao.ministroRelator, TipoRetornoMinistro.VENCIDO));
			} 
			
			if (decisao.getQtdNaoHaQC()>=6 && decisao.getQtdNaoHaQC()<=10) {
				resultado.append(TextoPadrao.QC_6_10_VOTOS_NAO_HA.getTextoPadrao(isUnanimidade));
				
				// recuperando os ministros vencidos. Como não existiu QC os vencidos votaram HÁ QC
				if (decisao.ministrosHaQC != null && decisao.ministrosHaQC.size()>0) {
					resultado.append(", ");			
					resultado.append(recuperarNomeMinistros(decisao.ministrosHaQC,decisao.ministroRelator, TipoRetornoMinistro.VENCIDO));
				} else {
					resultado.append(". ");
				} 													
			} 
			
			if (decisao.getQtdNaoHaQC()==11)
				resultado.append(TextoPadrao.QC_11_VOTOS_NAO_HA.getTextoPadrao(isUnanimidade));
				
			// Gerando o texto dos ministros que não se manifestaram - só gera o texto nos casos que não ocorrer a unanimidade
			if (decisao.getQtdSemManifestacaoQC() > 0)
				resultado.append(recuperarNomeMinistros(decisao.ministrosAusentesQC,decisao.ministroRelator,TipoRetornoMinistro.SEM_MANIFESTACAO));

			// Gerando o texto dos ministros impedidos - só gera o texto nos casos que não ocorrer a unanimidade  
			if (decisao.getQtdImpedidoQC() > 0)
				resultado.append(recuperarNomeMinistros(decisao.ministrosImpedidosQC,decisao.ministroRelator,TipoRetornoMinistro.IMPEDIDO));

			return resultado;
	}

	
	// gera o texto de decisão a reafirmação de jurisprudência (mérito)
	private StringBuffer gerarTextoReafirmacaoJurisprudencia(Decisao decisao) throws ServiceException {
		
		StringBuffer resultado = new StringBuffer("");
		
		boolean isUnanimidade = (decisao.getQtdSimRJ() == 0 || decisao.getQtdNaoRJ() == 0) ? true : false;
		
		if (decisao.getQtdSimRJ()>=1 && decisao.getQtdSimRJ()<=5) {
			resultado.append(TextoPadrao.RJ_1_5_VOTOS_SIM.getTextoPadrao(isUnanimidade));			
		} 
		
		if (decisao.getQtdSimRJ()>=6 && decisao.getQtdSimRJ()<=10 && decisao.ministrosNaoRJ.size() > 0) {
			resultado.append(TextoPadrao.RJ_6_10_VOTOS_SIM.getTextoPadrao(isUnanimidade));
			
			// recuperando os ministros vencidos. Como existiu a RJ os vencidos votaram NÃO RJ
			if (decisao.ministrosNaoRJ != null && decisao.ministrosNaoRJ.size()>0) {
				resultado.append(", ");			
				resultado.append(recuperarNomeMinistros(decisao.ministrosNaoRJ,decisao.ministroRelator, TipoRetornoMinistro.VENCIDO));
			} else {
				resultado.append(". ");
			} 													
		} 
		
		if (decisao.getQtdSimRJ()>=6 && decisao.ministrosNaoRJ.size() == 0) {
			resultado.append(TextoPadrao.RJ_11_VOTOS_SIM.getTextoPadrao(isUnanimidade));
		}
			
		// Gerando o texto dos ministros que não se manifestaram - só gera quando existir maioria qualificada
		if (decisao.getQtdSimRJ()>=1 && decisao.getQtdSemManifestacaoRJ() > 0){
			resultado.append(recuperarNomeMinistros(decisao.ministroAusentesRJ,decisao.ministroRelator,TipoRetornoMinistro.SEM_MANIFESTACAO));
		}

		// Gerando o texto dos ministros impedidos - só gera quando existir maioria qualificada  
		if (decisao.getQtdImpedidoQC() > 0) {
			resultado.append(recuperarNomeMinistros(decisao.ministrosImpedidosQC,decisao.ministroRelator,TipoRetornoMinistro.IMPEDIDO));
		}

		return resultado;
	}
	/**
	 * método responsavel por montar o texto de decisão quando a matéria é
	 * considerada constitucional
	 * 
	 * @param julgamentoProcesso
	 * @return
	 * @throws ServiceException
	 */

	private String montarTextoDecisaoJulgamento(Decisao decisao) throws ServiceException {
		try {
			// Rotina alterada pelo PROCJUD-477 - 05/08/2012 - Júlio César
			if (decisao != null) {

				StringBuffer textoPadrao = new StringBuffer("");
				
				// Gerando o texto da matéria constitucional
				if (decisao.decisaoQC != null) {
					textoPadrao.append(gerarTextoMateriaConstitucional(decisao));
				}
				
				//Gerando o texto da repercussão geral - não gera o texto se não existir QC
				if (decisao.decisaoRG != null && !decisao.decisaoQC.equals(Decisao.TipoDecisao.NAO_HA_QC)) {
					textoPadrao.append(gerarTextoRepercussaoGeral(decisao));
				}
				
				// Gerando o texto da reafirmação de jurisprudência (mérito) - não gera o texto se não existir QC e RG
				if (decisao.decisaoRJ != null && !decisao.decisaoQC.equals(Decisao.TipoDecisao.NAO_HA_QC) && !decisao.decisaoRG.equals(Decisao.TipoDecisao.NAO_HA_RG)) {
					textoPadrao.append(gerarTextoReafirmacaoJurisprudencia(decisao));
				}
				

				// Ministros que não se manifestaram e que possuem texto de manifestação
				if (decisao.getPossuiDivergente()) {
					textoPadrao.append(recuperarNomeMinistros(decisao.ministrosDivergentes,decisao.ministroRelator,TipoRetornoMinistro.DIVERGENTE));
				}
				
				
				return textoPadrao.toString();
			} else {
				throw new NullPointerException("O processo não possui voto cadastrado");
			}

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	private String montarTextoAndamentoJulgamentoReiniciado(Decisao decisao) throws ServiceException {
		try {
			if (decisao != null) {
				String textoInicio = "";
				String textoFim = "";
				String textoPadrao = "Após %s, o julgamento foi suspenso e reiniciado automaticamente na sessão subsequente para aguardar os votos dos Ministros que não se "
						+ "manifestaram (art. 324, § 4º, do Regimento Interno do Supremo Tribunal Federal). %s";
				
				// Ministros que votaram
				List<Ministro> ministrosVotantes = new ArrayList();
				ministrosVotantes.addAll(decisao.ministrosHaQC);
				ministrosVotantes.addAll(decisao.ministrosNaoHaQC);
				
				// Ministros impedidos
				List<Ministro> ministrosImpedidos = new ArrayList(decisao.ministrosImpedidosQC);
				
				textoInicio = recuperarNomeMinistros(ministrosVotantes, decisao.ministroRelator, TipoRetornoMinistro.SOMENTE_MINISTRO_VOTO, false);
				
				// Ministros que não se manifestaram e que possuem texto de manifestação
				if (!ministrosImpedidos.isEmpty())
					textoFim = recuperarNomeMinistros(decisao.ministrosDivergentes,decisao.ministroRelator, TipoRetornoMinistro.IMPEDIDO);
				
				return String.format(textoPadrao, textoInicio, textoFim);
			} else {
				throw new NullPointerException("O processo não possui nenhum voto cadastrado.");
			}

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * ENUM PARA RECUPERAR O TEXTO PADRÂO
	 * 
	 * @Guilhermea
	 */
	private enum TextoPadrao {
		QC_ZERO_VOTOS_NAO_HA("Decisão: O Tribunal, por %s, reputou constitucional a questão. "),
		QC_1_5_VOTOS_NAO_HA("Decisão: O Tribunal, por %s, reputou constitucional a questão"),
		QC_6_10_VOTOS_NAO_HA("Decisão: O Tribunal, por %s, reconheceu a inexistência de repercussão geral da questão, por não se tratar de matéria constitucional"),
		QC_11_VOTOS_NAO_HA("Decisão: O Tribunal, por %s, reconheceu a inexistência de repercussão geral da questão, por não se tratar de matéria constitucional. "),
		RG_ZERO_VOTOS_NAO_HA("O Tribunal, por %s, reconheceu a existência de repercussão geral da questão constitucional suscitada. "),
		RG_1_7_VOTOS_NAO_HA("O Tribunal, por %s, reconheceu a existência de repercussão geral da questão constitucional suscitada"),
		RG_8_10_VOTOS_NAO_HA("O Tribunal, por %s, reconheceu a inexistência de repercussão geral da questão constitucional suscitada"),
		RG_11_VOTOS_NAO_HA("O Tribunal, por %s, reconheceu a inexistência de repercussão geral da questão constitucional suscitada. "),
		RJ_1_5_VOTOS_SIM("No mérito, não reafirmou a jurisprudência dominante sobre a matéria, que será submetida a posterior julgamento no Plenário físico. "),
		RJ_6_10_VOTOS_SIM("No mérito, por %s, reafirmou a jurisprudência dominante sobre a matéria"),
		RJ_11_VOTOS_SIM("No mérito, por %s, reafirmou a jurisprudência dominante sobre a matéria. ");

		private String textoPadrao;

		private TextoPadrao(String textoPadrao) {
			this.textoPadrao = textoPadrao;
		}

		public String getTextoPadrao(boolean isUnanimidade) {
			if (isUnanimidade)
				return String.format(textoPadrao, "unanimidade");
			else
				return String.format(textoPadrao, "maioria");
		}
	}

	private String recuperarNomeMinistros(List<Ministro> listaMinistro, Ministro ministroRelator, TipoRetornoMinistro tipoRetornoMinistro) throws ServiceException {
		return recuperarNomeMinistros(listaMinistro, ministroRelator, tipoRetornoMinistro, true);
	}

		
	/**
	 * Monta a descrição do texto de acordo com a Tipo de retorno passado
	 * 
	 * @param listaMinistro
	 * @param ministroRelator
	 * @param tvm
	 * @param somenteMinistro
	 * @return
	 * @throws ServiceException
	 */
	private String recuperarNomeMinistros(List<Ministro> listaMinistro, Ministro ministroRelator, TipoRetornoMinistro tipoRetornoMinistro, boolean finalFrase) throws ServiceException {

		try {

			if (listaMinistro != null && listaMinistro.size() > 0) {
				if (listaMinistro.contains(ministroRelator)) {
					listaMinistro.remove(ministroRelator);
					listaMinistro.add(0, ministroRelator);
				}

				boolean masculino = false;
				int qtdMinistro = 0;
				String nomeMinistro = "";
				int i = 0;
				for (Ministro ministro : listaMinistro) {
					i++;

					// se o registro de ministro estiver como Presidente,
					// recupera o nome do presidente
					if (ministro.getSigla().equals("GP")) {
						MinistroPresidente ministroPresidenteGP = ministroPresidenteService.recuperarMinistro(TipoOcorrenciaMinistro.MP,
								ministro.getDataPosse());
						Ministro ministroPresidente = null;
						if (ministroPresidenteGP != null)
							ministroPresidente = ministroService.recuperarPorId(ministroPresidenteGP.getId().getMinistro().getId());

						if (ministroPresidente != null)
							ministro = ministroPresidente;
					}
					// se o registro de ministro estiver como Vice-Presidente,
					// recupera o nome do vice-presidente
					if (ministro.getSigla().equals("VP")) {
						MinistroPresidente ministroPresidenteVP = ministroPresidenteService.recuperarMinistro(TipoOcorrenciaMinistro.VP,
								ministro.getDataPosse());
						Ministro ministroVicePresidente = null;
						if (ministroPresidenteVP != null)
							ministroVicePresidente = ministroService.recuperarPorId(ministroPresidenteVP.getId().getMinistro().getId());

						if (ministroVicePresidente != null)
							ministro = ministroVicePresidente;
					}

					if (ministro.getTipoSexo().getValor().equals(TipoSexo.MASCULINO)) {
						masculino = true;
					}
					if (qtdMinistro == 0) {
						nomeMinistro = ministro.getNomeMinistroCapsulado(false);
					} else {
						if (i < listaMinistro.size()) {
							nomeMinistro = nomeMinistro + ", " + ministro.getNomeMinistroCapsulado(false);
						} else {
							nomeMinistro = nomeMinistro + " e " + ministro.getNomeMinistroCapsulado(false);

						}
					}

					qtdMinistro++;
				}
				
				String retorno =  tipoRetornoMinistro.getTexto(masculino, qtdMinistro > 1) + nomeMinistro;
				
				if (finalFrase)
					retorno += ". ";
				
				return retorno;
			}

			return "";
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	

	/**
	 * Enumeratede para auxiliar os paramentros a serem passados no metodo para
	 * montar o texto de decisao, quando recuperar os ministros que irão ser
	 * inseridos no texto
	 * 
	 * @author GuilhermeA
	 * 
	 */
	private enum TipoRetornoMinistro {
		HA(
				"Manifestaram - se pelo reconhecimento da repercussão geral no caso os Ministros ",
				"Manifestou - se pelo reconhecimento da repercussão geral no caso o Ministro",
				"Manifestaram - se pelo reconhecimento da repercussão geral no caso as Ministras ",
				"Manifestou - se pelo reconhecimento da repercussão geral no caso a Ministra "),

		IMPEDIDO(" Impedidos os Ministros ", " Impedido o Ministro ",
				" Impedidas as Ministras ", " Impedida a Ministra "),

		SEM_MANIFESTACAO(" Não se manifestaram os Ministros ",
				" Não se manifestou o Ministro ",
				" Não se manifestaram as Ministras ",
				" Não se manifestou a Ministra "),

		VENCIDO(" vencidos os Ministros ", " vencido o Ministro ",
				" vencidas as Ministras ", " vencida a Ministra "),

		DIVERGENTE(" Votaram de forma divergente os Ministros ",
				" Votou de forma divergente o Ministro ",
				" Votaram de forma divergente as Ministras ",
				" Votou de forma divergente a Ministra "),

		SOMENTE_MINISTRO(" os Ministros ", " o Ministro ", " as Ministras ",
				" a Ministra "),
		
		SOMENTE_MINISTRO_VOTO("os votos dos Ministros ", "o voto do Ministro ", "os votos das Ministras ",
				"o voto da Ministra ");

		private String singularM, pluralM, singularF, pluralF;

		private TipoRetornoMinistro(String pluralM, String singularM, String pluralF, String singularF) {
			this.singularF = singularF;
			this.pluralF = pluralF;
			this.singularM = singularM;
			this.pluralM = pluralM;
		}

		public String getTexto(boolean masculino, boolean plural) {
			if (masculino) {
				return plural ? pluralM : singularM;
			} else {
				return plural ? pluralF : singularF;
			}
		}
	}

	/**
	 * método responsavel por inserir um tema para a repercussão com a descrição
	 * em vazio, caso já não haja o tem cadastrado
	 * 
	 * @author Guilhermea
	 */
	// TODO
	public Boolean persistirTemaRepercussaoGeral(Tema tema, Date dataSessao) throws ServiceException, TemaException {
		try {

			if (dataSessao != null
					&& !tema.getProcessoTemaLeadingCase().getIncidenteJulgamento().getTipoJulgamento().getSigla()
							.equals(TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL)) {

				JulgamentoProcesso julgamentoProcesso = recuperarJulgamentoProcesso(tema.getProcessoTemaLeadingCase());
				Sessao sessao = null;
				if (julgamentoProcesso == null) {
					julgamentoProcesso = new JulgamentoProcesso();
					julgamentoProcesso.setSessao(getInstanciaSessao(dataSessao));

					if (julgamentoProcesso.getSessao().getListaJulgamentoProcesso() == null) {
						julgamentoProcesso.getSessao().setListaJulgamentoProcesso(new LinkedList<JulgamentoProcesso>());
					}
					julgamentoProcesso.getSessao().getListaJulgamentoProcesso().add(julgamentoProcesso);
					julgamentoProcesso.setObjetoIncidente(recuperarIncidenteJulgamento(tema.getProcessoTemaLeadingCase().getIncidenteJulgamento()
							.getPrincipal(), tema.getProcessoTemaLeadingCase().getIncidenteJulgamento().getTipoJulgamento().getSigla()));

				} else if (julgamentoProcesso.getSessao().getDataFim() != null) {

					Date dataFim = DateTimeHelper.getData(DateTimeHelper.getDataString(julgamentoProcesso.getSessao().getDataFim()));
					Date data = DateTimeHelper.getData(DateTimeHelper.getDataString(dataSessao));
					if (dataFim.compareTo(data) != 0) {

						if (julgamentoProcesso.getSessao().getListaJulgamentoProcesso().size() == 1) {
							sessao = julgamentoProcesso.getSessao();
						}
						julgamentoProcesso.setSessao(getInstanciaSessao(dataSessao));

					}
				}

				if (julgamentoProcesso.getSituacaoAtual() == null
						|| !julgamentoProcesso.getSituacaoAtual().getTipoSituacaoJulgamento().getId()
								.equals(TipoSitucacaoJulgamentoConstant.FINALIZADO.getCodigo())) {
					julgamentoProcesso.adicionarSituacaoJulgamento(tipoSituacaoJulgamentoService.recuperarPorId(TipoSitucacaoJulgamentoConstant.FINALIZADO
							.getCodigo()));
				}
				sessaoService.salvar(julgamentoProcesso.getSessao());
				if (julgamentoProcesso.getId() == null) {
					julgamentoProcessoService.incluir(julgamentoProcesso);
				} else {
					julgamentoProcessoService.salvar(julgamentoProcesso);
				}

				if (sessao != null) {
					sessaoService.excluir(sessao);
				}
			}

			for (ProcessoTema processoTema : tema.getProcessosTema()) {
				if (processoTema.getIncidenteJulgamento() != null && processoTema.getIncidenteJulgamento().getId() == null) {
					processoTema.setObjetoIncidente(recuperarIncidenteJulgamento(processoTema.getIncidenteJulgamento().getPrincipal(), processoTema
							.getIncidenteJulgamento().getTipoJulgamento().getSigla()));
				}
			}

			if (tema.getId() == null || tema.getId() == 0L) {
				tema.setNumeroSequenciaTema(buscaNumeroMaximoTema());
			}

			return temaService.persistirTema(tema);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public Sessao getInstanciaSessao(Date dataSessao) throws ServiceException {
		try {
			Sessao sessao = new Sessao();
			sessao.setDataInicio(dataSessao);
			sessao.setDataFim(dataSessao);
			sessao.setDataPrevistaInicio(dataSessao);
			sessao.setDataPrevistaFim(dataSessao);
			sessao.setTipoSessao(Sessao.TipoSessaoConstante.ORDINARIA.getSigla());
			sessao.setColegiado(colegiadoService.recuperarPorId(Colegiado.TRIBUNAL_PLENO));
			sessao.setTipoAmbiente(TipoAmbienteConstante.PRESENCIAL.getSigla());
			return sessao;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public JulgamentoProcesso recuperarJulgamentoProcesso(ProcessoTema processoTema) throws ServiceException {
		try {

			Long id = processoTema.getIncidenteJulgamento().getPrincipal().getId();
			List<JulgamentoProcesso> julgamentos = julgamentoProcessoService.pesquisarJulgamentoProcesso(id, null, null);

			if (julgamentos != null && julgamentos.size() > 0) {
				for (JulgamentoProcesso julgamento : julgamentos) {
					if (julgamento.getIncidenteJulgamento() != null
							&& julgamento.getIncidenteJulgamento().getTipoJulgamento().getSigla()
									.equals(processoTema.getIncidenteJulgamento().getTipoJulgamento().getSigla())
							&& julgamento.getSessao().getTipoAmbiente().equals(TipoAmbienteConstante.PRESENCIAL.getSigla())) {
						return julgamento;
					}
				}
			}
			return null;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public List<TipoCampoTarefaValor> recuperarTipoCampoTarefaValor(TipoCampoTarefaContante tipoConstante) throws ServiceException {
		return tipoTarefaSetorService.pesquisarTipoCampoTarefaValor(null, tipoConstante.getCodigo());
	}

	// ############################################## Notificação repercussao
	// geral ############################################## //
	/**
	 * metodo responsavel por inserir um registro de notificacao para o inicio
	 * da análise da repercussão geral. esse método irá ser chamado pelo
	 * agendador RepercussaoGeralJob.java
	 * 
	 * @author ViniciusK,Guilhermea
	 */
	public void persistirNotificacaoRepercussaoGeral() throws ServiceException {
		try {

			List<NotificacaoLogonMinistro> listaNotificacaoLogon = new LinkedList<NotificacaoLogonMinistro>();

			// Pesquisa os Ministros para efeitos de notificação
			List<Ministro> listaMinistro = ministroService.pesquisarMinistros(true, false, null, null, null);

			for (Ministro ministro : listaMinistro) {
				// Pesquisa os julgamentos pendentes de votação por ministro
				List<JulgamentoProcesso> listaProcessosPendentes = dao.processoPendentesMinistroNotificacao(ministro.getId());

				if (listaProcessosPendentes != null && listaProcessosPendentes.size() > 0) {

					// Recuperar a sessão com a maior data de início entre as
					// sessões dos julgamentos pendentes de votação
					// do Ministro em questão.
					Sessao sessao = recuperarUltimaSessaoJulgamento(listaProcessosPendentes);

					NotificacaoLogon notificacao = new NotificacaoLogon();
					notificacao.setDataInicioNotificacao(sessao.getDataInicio());
					notificacao.setDataFimNotificacao(sessao.getDataPrevistaFim());
					notificacao.setGrupoTopico(ministro.getUsuario().getId());

					notificacao.setDescricao(montaCorpoMensagem(ministro, listaProcessosPendentes));

					listaNotificacaoLogon.add(new NotificacaoLogonMinistro(notificacao, ministro.getSetor()));
				}
			}

			persistirNotificacaoUsuario(listaNotificacaoLogon, NOTIFICACAO_PRAZO_RESTANTE);

		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * método responsavel por recuperar a ultima sessão de julgamento dos
	 * processos pendentes do ministro
	 * 
	 * @param listaJulgamentoProcesso
	 * @return
	 */
	private Sessao recuperarUltimaSessaoJulgamento(List<JulgamentoProcesso> listaJulgamentoProcesso) throws ServiceException {
		try {
			Collections.sort(listaJulgamentoProcesso, new JulgamentoProcessoComparatorDecrescente());
			return listaJulgamentoProcesso.get(0).getSessao();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * método responsavel por montar o html que vai mostrar a insformação dos
	 * processo para o ministro
	 * 
	 * @param listaProcesso
	 * @return
	 * @author ViniciusK,Guilhermea
	 */
	private String montaCorpoMensagem(Ministro ministro,
			List<JulgamentoProcesso> listaProcessosPendentes)
			throws ServiceException {

		try {

			StringBuffer corpo = new StringBuffer();

			corpo.append("<html>");
			corpo.append("	<head>");
			corpo.append("	<style type=\"text/css\">");
			corpo.append("  .PadraoVermelho{font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 10px;font-weight: bold;color: red;}");
			corpo.append(" 	.PadraoAmarelo{font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 10px;font-weight: bold;color: #FFA500;}");
			corpo.append("	.PadraoVerde{font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 10px;font-weight: bold;color: green;}");
			corpo.append("	.Padrao{font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 10px;color: black;}");
			corpo.append(" </style>");
			corpo.append("	<title>Plenário Virtual</title>");
			corpo.append("	</head>");
			corpo.append("	<body bgcolor=\"#FAFAF1\">");
			corpo.append(" <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
			corpo.append("<tr><td>");
			corpo.append("<img src=\"http://apl-soa:8080/stf-esb-imagens/semaforo_anim.gif\" width=\"128\" height=\"128\" alt=\"\" border=\"0\" align=\"middle\" />");
			corpo.append("</td>");
			corpo.append("<td>");

			if (ministro != null) {

				if (ministro.getTipoSexo().getValor().equals(TipoSexo.FEMININO)) {
					corpo.append("	<b>Sra. Ministra, tomamos a liberdade de lembrar a Vossa Excelência que estão em curso prazos para manifestação,");
					corpo.append("     no Plenário Virtual, sobre repercussão geral nos seguintes processos: <b>");
				} else {
					corpo.append("	<b>Sr. Ministro, tomamos a liberdade de lembrar a Vossa Excelência que estão em curso prazos para manifestação,");
					corpo.append("     no Plenário Virtual, sobre repercussão geral nos seguintes processos: <b>");
				}

			} else {
				corpo.append("	<b>Sr(a). Ministro(a), tomamos a liberdade de lembrar a Vossa Excelência que estão em curso prazos para manifestação,");
				corpo.append("     no Plenário Virtual, sobre repercussão geral nos seguintes processos: <b>");
			}
			corpo.append("<ul>");

			for (JulgamentoProcesso processoPendente : listaProcessosPendentes) {
				int diasRestantes = recuperarDiasRestantesNotificacao(processoPendente.getSessao());

				if (diasRestantes <= 5) {
					corpo.append("<li> <i class=\"PadraoVermelho\" >");
				} else if (diasRestantes <= 15) {
					corpo.append("<li> <i class=\"PadraoAmarelo\" >");
				} else if (diasRestantes <= 20) {
					corpo.append("<li> <i class=\"PadraoVerde\" >");
				} else if (diasRestantes > 20) {
					corpo.append("<li> <i class=\"PadraoAzul\" >");
				}
				corpo.append(montarTempoRestante(processoPendente.getSessao()) + " </i>&nbsp; <i class=\"Padrao\"> - Processo <b> ");

				corpo.append(processoPendente.getObjetoIncidente().getIdentificacao());
				corpo.append("</b> </i></li>");
			}

			corpo.append("</ul>");
			corpo.append("	Se desejar fazê-lo agora, clique <a href=http://apl-ssgj:8080/repgeral/jsp/controlarrepercussaogeral/ControlarRepercussaoGeral.jsf>aqui</a>.");
			corpo.append("</td>");
			corpo.append("</tr>");
			corpo.append("	</body>");
			corpo.append("</html>");

			return corpo.toString();
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	/**
	 * método responsavel por perstir uma notificacao para o ministro relator
	 * quando houver um ministro que criou o texto de manifestação mas não
	 * lançou seu voto no plenário virtual. Esta notificação e para alertar o
	 * ministro que poderá alterar o texto de de decisão no momento em que
	 * gera-lo.
	 */
	public void persistirNotificacaoDivergenteMinsitro() throws ServiceException {

		try {
			List<JulgamentoProcesso> listaJulgamentoDivergente = dao.recuperarJulgamentoSemManifestacaoComTexto();

			if (listaJulgamentoDivergente != null && listaJulgamentoDivergente.size() > 0) {

				List<NotificacaoLogonMinistro> listaNotificacaoLogon = new LinkedList<NotificacaoLogonMinistro>();

				// Recupera a lista dos julgamentos com voto divergente por
				// ministro relator
				List<NotificacaoMinistro> julgamentosDivergentesPorRelator = recuperarNotificacaoMinistros(listaJulgamentoDivergente);

				if (julgamentosDivergentesPorRelator != null && julgamentosDivergentesPorRelator.size() > 0) {
					for (NotificacaoMinistro julgamentosDivergentesDoRelator : julgamentosDivergentesPorRelator) {

						if (julgamentosDivergentesDoRelator != null) {

							NotificacaoLogon notificacao = new NotificacaoLogon();
							Calendar cal = Calendar.getInstance();
							notificacao.setDataInicioNotificacao(cal.getTime());
							cal.add(Calendar.DATE, 3);
							notificacao.setDataFimNotificacao(cal.getTime());

							// se o relator for presidência será necessário
							// setar o usuário
							// do ministro presidente para não causar
							// nullPointer
							// TODO
							if (julgamentosDivergentesDoRelator.relator.getId() == 1L) {
								notificacao.setGrupoTopico(ministroService.retornaMinistroPresidenteAtual().getUsuario().getId());
							} else {
								notificacao.setGrupoTopico(julgamentosDivergentesDoRelator.relator.getUsuario().getId());
							}

							StringBuffer msg = new StringBuffer();

							msg.append("<html>	<head>");
							msg.append("<style type='text/css'>");
							msg.append(".Padrao{font-family: Verdana, Arial, Helvetica, sans-serif;");
							msg.append("font-size: 10px;color: black;} ");
							msg.append("</style>	");
							msg.append("<title>Plenário Virtual</title>");
							msg.append("</head>	");
							msg.append("<body bgcolor='#FAFAF1'>");
							
							if (julgamentosDivergentesDoRelator.relator != null) {
								if (julgamentosDivergentesDoRelator.relator.getTipoSexo().getValor().equals(TipoSexo.FEMININO)) {
									msg.append("<b>Sra. Ministra, tomamos a liberdade de informá-la ");
								} else {
									msg.append("<b>Sr. Ministro, tomamos a liberdade de informá-lo ");
								}
							} else {
								msg.append("<b>Sr(a). Ministro(a), tomamos a liberdade de informá-lo(a) ");
							}

							msg.append("que no(s) processo(s) de repercussão geral abaixo há voto(s) divergente(s) e a(s) decisão(ões) precisa(m) ser editada(s) manualmente.");
							msg.append(" Em seguida, o PDF deverá ser gerado e o texto liberado para publicação. </b>");
							msg.append("<ul>");

							for (JulgamentoProcesso julgamento : julgamentosDivergentesDoRelator.listaJulgamentos) {
								msg.append("<li><i class='Padrao'><b>" + julgamento.getIdentificacao()	+ "</b></i></li>");
							}

							msg.append("</ul>	");
							msg.append("</body></html>");
							notificacao.setDescricao(msg.toString());

							listaNotificacaoLogon.add(new NotificacaoLogonMinistro(notificacao, julgamentosDivergentesDoRelator.relator.getSetor()));
						}
					}

					persistirNotificacaoUsuario(listaNotificacaoLogon,NOTIFICACAO_MANIFESTACAO_DIVERGENTE);
				}
			}

		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Monta lista de julgamentos agrupados por ministro. Cada resultado da
	 * lista irá contar os julgamentos agrupados por Ministro Relator.
	 * 
	 * @param listaJulgamentoDivergente
	 * @return
	 */
	private List<NotificacaoMinistro> recuperarNotificacaoMinistros(List<JulgamentoProcesso> listaJulgamentoDivergente) {
		List<NotificacaoMinistro> notificacaoMinistros = new LinkedList<NotificacaoMinistro>();

		Map<Ministro, NotificacaoMinistro> julgamentosPorRelator = new HashMap<Ministro, NotificacaoMinistro>();

		for (JulgamentoProcesso julgDiverge : listaJulgamentoDivergente) {
			// boolean possui = false;

			// Recuperar o relator do julgamento divergente
			Ministro relator = ObjetoIncidenteUtil.getProcesso(julgDiverge.getObjetoIncidente()).getMinistroRelatorAtual();

			// Se o processo com julgamento divergente possui relator
			if (relator != null) {

				// Recupera as notificações do relator encontrado
				NotificacaoMinistro notificacaoMinistro = julgamentosPorRelator.get(relator);

				// Se não encontrou notificações para o relator encontrado, cria
				// um novo
				if (notificacaoMinistro == null) {
					notificacaoMinistro = new NotificacaoMinistro(relator);
					julgamentosPorRelator.put(relator, notificacaoMinistro);
					notificacaoMinistros.add(notificacaoMinistro);
				}

				// Adiciona o julgamento divergente à lista de julgametnos do
				// relator encontrado
				notificacaoMinistro.listaJulgamentos.add(julgDiverge);
			}
		}

		return notificacaoMinistros;
	}

	private void persistirNotificacaoUsuario(List<NotificacaoLogonMinistro> listaNotificacaoLogon,	Integer prioridade) throws DaoException, ServiceException {
		// Recupera a lista dos usuarios com o perfil que permite a notificação
		// através do aplicativo STFNotificador
		List<Usuario> listaUsuarios = dao.buscaUsuariosNotificacao(dsc_perfil);

		if (listaUsuarios != null) {
			for (Usuario usuario : listaUsuarios) {
				// Recupera a primeira notificação encontrada já salva em banco
				// de dados para o Ministro (usuario) e
				// prioridade informados e aplica os valores da nova notificação
				// montada.
				// Essa função, portanto, evita um novo update (quando for o
				// caso), ou seja, faz com que o antigo registro
				// de notificação seja atualizado com os novos valores
				// informados.
				NotificacaoLogon notificacao = recuperarOuCriarNotificacaoMinistro(usuario, listaNotificacaoLogon, prioridade);
				// NotificacaoLogon notificacao =
				// recuperarNotificacaoUsuario(usuario, listaNotificacaoLogon,
				// prioridade);

				// Verifica se há notificação para o Ministro em questão e
				// salva.
				// Se não possuir notificacao, ou seja, a variável 'notificacao'
				// é nula, irá entrar no ELSE.
				if (notificacao != null) {
					notificacaoLogonService.salvar(notificacao);
				} else {
					// Irá processar o ELSE caso o Ministro em questão não tem
					// mais notificação a receber.
					// Dessa forma, a data fim das notificações encontradas
					// serão atualizadas para a data atual
					// de tal forma que não receberá mais notificações.
					List<NotificacaoLogon> listaNotificacao = notificacaoLogonService.pesquisaNotificacao(usuario.getId(), null, null,	new Date(), "AGENDADOREGAB", prioridade);
					if (listaNotificacao != null && listaNotificacao.size() > 0){
						for (NotificacaoLogon not : listaNotificacao) {
							notificacaoLogonService.excluir(not);

							/*
							 * not.setDataFimNotificacao(Calendar.getInstance().
							 * getTime()); notificacaoLogonService.salvar(not);
							 */
						}
					}
				}
			}
		}
	}

	private NotificacaoLogon recuperarOuCriarNotificacaoMinistro(
			Usuario usuarioMinistro,
			List<NotificacaoLogonMinistro> listaNovasNotificacoesDosMinistros,
			int prioridade) throws ServiceException {
		try {

			NotificacaoLogon notificacao = null;

			for (NotificacaoLogonMinistro novaNotificacaoMinistro : listaNovasNotificacoesDosMinistros) {
				// Verifica se a notificação diz respeito ao Ministro passado
				// como parâmetro
				if (novaNotificacaoMinistro.setor.getId().equals(usuarioMinistro.getSetor().getId())
						|| novaNotificacaoMinistro.notificacaoLogon.getGrupoTopico().equals(usuarioMinistro.getId())) {

					// Recupera as notificações encontradas do Ministro e
					// prioridade passados como parâmetros
					// que ainda estão ativas (ou seja, a data fim da
					// notificação é maior que a data de hoje).
					List<NotificacaoLogon> listaNotificacoesEmAbertoDoMinistro = notificacaoLogonService.pesquisaNotificacao(usuarioMinistro.getId(), null,
									null, new Date(), "AGENDADOREGAB", prioridade);

					// Se encontrou alguma notificação para o Ministro e
					// prioridades informados, recupera a primeira da lista.
					if (listaNotificacoesEmAbertoDoMinistro != null
							&& listaNotificacoesEmAbertoDoMinistro.size() > 0) {
						notificacao = listaNotificacoesEmAbertoDoMinistro.get(0);
					} else {
						// Se não encontrar nenhuma notificação já salva, irá
						// criar uma nova.
						notificacao = new NotificacaoLogon();
					}

					notificacao.setDataFimNotificacao(novaNotificacaoMinistro.notificacaoLogon.getDataFimNotificacao());
					notificacao.setDataInicioNotificacao(novaNotificacaoMinistro.notificacaoLogon.getDataInicioNotificacao());
					notificacao.setDescricao(novaNotificacaoMinistro.notificacaoLogon.getDescricao());
					notificacao.setPrioridade(prioridade);
					notificacao.setGrupoTopico(usuarioMinistro.getId());
				}
			}

			return notificacao;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}


	// ############################################## FIM NOTIFICAÇÂO
	// REPERCUSSÂO GERAL ############################################## //

	/**
	 * método reponsavel por recuperar os dias restantes para a finalização da
	 * repercussão
	 * 
	 * @param Sessao
	 * @return
	 */
	protected int recuperarDiasRestantes(Sessao sessao) throws ServiceException {
		try {

			if (sessao != null && sessao.getDataPrevistaFim() != null) {

				Date dataFim = sessao.getDataFim() != null ? sessao.getDataFim() : sessao.getDataPrevistaFim();
				Date dataPrevista = DateUtils.truncate(dataFim, Calendar.DAY_OF_MONTH);
				Date dataAtual = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);

				if (dataPrevista.compareTo(dataAtual) == 1) {

					long diferenca = dataPrevista.getTime() - dataAtual.getTime();

					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(diferenca);

					int diasRestantes = calendar.get(Calendar.DAY_OF_YEAR);

					return diasRestantes;

				} else if (dataPrevista.compareTo(dataAtual) == 0 && sessao.getDataFim() == null) {
					return 0;
				}
			}
			return 20;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * metodo responsavel por montar o tempo restante da repercussão geral
	 * 
	 * @param tarefaSetor
	 * @return
	 */
	private String montarTempoRestante(Sessao sessao) throws ServiceException {
		try {

			String tempoRestante = "";

			if (sessao.getDataInicio() != null) {
				int diasRestantes = recuperarDiasRestantesNotificacao(sessao);

				if (diasRestantes == 1) {
					tempoRestante = diasRestantes + " Dia";
				} else if (diasRestantes > 1) {
					tempoRestante = diasRestantes + " Dias";
				} else if (diasRestantes == 0) {
					Date dataFim = sessao.getDataFim() != null ? sessao.getDataFim() : sessao.getDataPrevistaFim();
					tempoRestante = "Finaliza as " + DateTimeHelper.getHoraString(dataFim);
				}
			} else {
				tempoRestante = "Tarefa não iniciada";
			}
			return tempoRestante;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}



	/**
	 * Método responsavel por ordenar a lista de JulgamentoProcesso peda data de
	 * inicio do julgamento
	 * 
	 * @author ViniciusK,Guilhermea
	 */
	private static class JulgamentoProcessoComparatorDecrescente implements Comparator<JulgamentoProcesso> {
		public int compare(JulgamentoProcesso obj, JulgamentoProcesso obj2) {
			return obj2.getSessao().getDataInicio().compareTo(obj.getSessao().getDataInicio());
		}
	}

	/**
	 * Metodo responsavel por montar o texto de decisão sobre repercussão geral
	 * 
	 * @Author Guilhermea
	 */
	public byte[] recuperarTextoDecisao(Processo processo, String fontFamily, String fontSize) throws ServiceException {

		// Recuperar a decisão final do julgamento
		Decisao decisao = recuperarDecisao(processo);

		try {
			if (decisao != null) {
				RTFEditorKit rtfEdidor = new RTFEditorKit();
				Document doc = rtfEdidor.createDefaultDocument();

				MutableAttributeSet negrito = new SimpleAttributeSet();
				negrito.addAttribute(StyleConstants.Alignment, StyleConstants.ALIGN_CENTER);
				negrito.addAttribute(ColorConstants.Bold, true);
				negrito.addAttribute(StyleConstants.FontFamily, fontFamily);
				negrito.addAttribute(StyleConstants.FontSize, new Integer(fontSize.replace("pt", "")).intValue());

				MutableAttributeSet font = new SimpleAttributeSet();
				font.addAttribute(StyleConstants.Alignment, StyleConstants.ALIGN_CENTER);
				font.addAttribute(StyleConstants.FontFamily, fontFamily);
				font.addAttribute(StyleConstants.FontSize, new Integer(fontSize.replace("pt", "")).intValue());

				MutableAttributeSet centralizado = new SimpleAttributeSet();
				centralizado.addAttribute(StyleConstants.Alignment, StyleConstants.ALIGN_CENTER);
				centralizado.addAttribute(StyleConstants.FontFamily, fontFamily);
				centralizado.addAttribute(StyleConstants.FontSize, new Integer(fontSize.replace("pt", "")).intValue());

				doc.insertString(0, "Decisão: ", negrito);
				String textoDecisao = montarTextoDecisaoJulgamento(decisao);
				textoDecisao = textoDecisao.replace("Decisão: ", "");
				doc.insertString(doc.getLength(), textoDecisao, font);
				doc.insertString(doc.getLength(), "\n \n \n \n \n \n \n" + getNomeMinistroRelator(decisao), centralizado);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				rtfEdidor.write(baos, doc, 0, doc.getLength());

				return baos.toByteArray();
			}

		} catch (BadLocationException e) {
			throw new ServiceException(e);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		return null;
	}

	/**
	 * Metodo responsavel por montar o DadosTextoDecisao com os dados
	 * necessários para elaborar o texto de decisão sobre repercussão geral
	 * 
	 * @param Processo
	 * @return DadosTextoDecisao
	 * @throws ServiceException
	 * @Author Rodrigo.Lisboa
	 */
	public DadosTextoDecisao recuperarDadosTextoDecisao(Processo processo) throws ServiceException {

		// Recuperar a decisão final do julgamento
		Decisao decisao = recuperarDecisao(processo);
		DadosTextoDecisao dadosTextoDecisao = new DadosTextoDecisao();
		String textoDecisao = null;
		if (decisao != null) {
			textoDecisao = montarTextoDecisaoJulgamento(decisao);
			textoDecisao = textoDecisao.replace("Decisão: ", "");
		}
		dadosTextoDecisao.setTextoDecisao(textoDecisao);
		dadosTextoDecisao.setNomeMinistroRelator(getNomeMinistroRelator(decisao));
		dadosTextoDecisao.setRelator(getRelatorOuRelatora(decisao));

		return dadosTextoDecisao;
	}
	
	public DadosTextoDecisao recuperarDadosTextoDecisao(Long objetoIncidenteId) throws ServiceException {
		Processo processo = new Processo();
		processo.setId(objetoIncidenteId);
		return recuperarDadosTextoDecisao(processo);
	}

	/**
	 * Metodo responsavel por recuperar se é Relator ou Relatora
	 * 
	 * @throws ServiceException
	 * @Author Rodrigo.Lisboa
	 */
	private String getRelatorOuRelatora(Decisao decisao) throws ServiceException {
		String res = null;
		if (decisao != null && decisao.ministroRelator != null && decisao.ministroRelator.getTipoSexo() != null) {
			if (decisao.ministroRelator.getTipoSexo().getValor().equals(TipoSexo.FEMININO))
				res = "Relatora";
			else
				res = "Relator";
			
			if (decisao.ministroRedator != null)
				if (decisao.ministroRedator.getTipoSexo().getValor().equals(TipoSexo.FEMININO))
					res = "Redatora do acórdão";
				else
					res = "Redator do acórdão";
		}
		return res;
	}

	private String getNomeMinistroRelator(Decisao decisao) {
		String nome = null;
		if (decisao != null && decisao.ministroRelator != null && decisao.ministroRelator.getTipoSexo() != null) {
			nome = decisao.ministroRelator.getNomeMinistroCapsulado(true, true);
			
			if (decisao.ministroRedator != null)
				nome = decisao.ministroRedator.getNomeMinistroCapsulado(true, true);
		}
		return nome;
	}

	/**
	 * médodo responsável por recuperar a decisão do julgamento da repercussão
	 * geral do processo informado
	 * 
	 * @param processo
	 * @return
	 * @throws ServiceException
	 */
	private Decisao recuperarDecisao(Processo processo) throws ServiceException {
		JulgamentoProcesso JPQCRG = null;
		JulgamentoProcesso JPRG = null;
		JulgamentoProcesso JPRJ = null;

		// recupera julgamentoProcesso da questão constitucional e da repercussão geral
		List<JulgamentoProcesso> listaJP = julgamentoProcessoService.pesquisarJulgamentoProcesso(processo.getId(), null,
				TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL, TipoIncidenteJulgamento.SIGLA_QUESTAO_CONSTITUCIONAL, TipoIncidenteJulgamento.SIGLA_MERITO, TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL_SEGUNDO_JULGAMENTO, TipoIncidenteJulgamento.SIGLA_QUESTAO_CONSTITUCIONAL_SEGUNDO_JULGAMENTO);
		
		if (listaJP != null && listaJP.size() > 0) {
			for (JulgamentoProcesso jp : listaJP) {
				if (jp.getTipoJulgamento().equals(TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL))
					JPRG = jp;

				if (jp.getTipoJulgamento().equals(TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL_SEGUNDO_JULGAMENTO)) {
					JPRG = jp;
					break;
				}
			}
		}
		
		JPQCRG = recuperarJulgamentoProcesso(JPRG, listaJP, TipoIncidenteJulgamento.SIGLA_QUESTAO_CONSTITUCIONAL_SEGUNDO_JULGAMENTO);
		
		if (JPQCRG == null)
			JPQCRG = recuperarJulgamentoProcesso(JPRG, listaJP, TipoIncidenteJulgamento.SIGLA_QUESTAO_CONSTITUCIONAL);
		
		JPRJ = recuperarJulgamentoProcesso(JPRG, listaJP, TipoIncidenteJulgamento.SIGLA_MERITO);
		
		// Recuperar a decisão final do julgamento
		if (JPRG != null && JPQCRG != null && JPRJ != null) {
			return instanciaDecisao(JPQCRG, JPRG, JPRJ);
		} else if (JPRG != null && JPQCRG != null && JPRJ == null) {
			return instanciaDecisao(JPQCRG, JPRG, null);
		} else {
			return null;
		}
	}

	/**
	 * recupera a situacao atual do julgamento da repercussão geral.
	 * 
	 * @author Guilhermea
	 */
	public JulgamentoProcesso recuperarJulgamentoRepercussaGeral(Processo processo) throws ServiceException {

		// Se tiver incidnete de segundo julgamento da repercussão geral, retorna o julgamentoProcesso dele, se não, retorna o julgamentoProcesso do primeiro julgamento
		List<IncidenteJulgamento> incidentes = incidenteJulgamentoService.pesquisar(processo.getId(), TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL_SEGUNDO_JULGAMENTO);
		
		if (incidentes.size() > 0) { // tem incidente 
			List<JulgamentoProcesso> lista = julgamentoProcessoService.pesquisarJulgamentoProcesso(processo.getId(), null, TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL_SEGUNDO_JULGAMENTO);
			
			if (lista.size() > 0)
				return lista.get(0);
			
			return null;
		}
		
		List<JulgamentoProcesso> lista = julgamentoProcessoService.pesquisarJulgamentoProcesso(processo.getId(), null, TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL);
		if (lista != null && lista.size() > 0) {
			JulgamentoProcesso jp = lista.get(0);
			return jp;
		}

		return null;
	}

	/**
	 * método responsavel por verificar se o texto de decisão da repercussão
	 * geral permiti alteração. Essa caso somente irá acontecer se um dos
	 * ministros tenha votado de forma divergente, ou seja, tenha criado o texto
	 * de manifestação sobre repercussão geral e não tenho lançado a
	 * manistefação no prenário virtual.
	 */
	public Boolean naoPermiteAlteracaoTextoDecisao(ObjetoIncidente<?> oi) throws ServiceException {
		Processo processo = ObjetoIncidenteUtil.getProcesso(oi);
		Decisao decisao = recuperarDecisao(processo);
		Boolean resultado = Boolean.FALSE;
		if (decisao != null) {
			if (!decisao.getPossuiDivergente()){
				resultado = Boolean.TRUE;
			}
		}
		Texto texto = textoService.recuperar(oi, TipoTexto.DECISAO_SOBRE_REPERCURSAO_GERAL);
		Boolean julgamentoAberto = sessaoService.recuperarJulgamentoEmAberto(oi.getId());
		if ((texto != null && texto.getTipoFaseTextoDocumento() != null && texto.getTipoFaseTextoDocumento().getCodigoFase()
				.equals(FaseTexto.LIBERADO_PUBLICACAO.getCodigoFase()))
				&& (julgamentoAberto != null && julgamentoAberto)) {
			resultado = Boolean.TRUE;
		}
		return resultado;
	}

	public Boolean julgamentoFinalizado(Processo processo) throws ServiceException {
		if (processo.getRepercussaoGeral()) {
			JulgamentoProcesso julgamento = recuperarJulgamentoRepercussaGeral(processo);
			SituacaoJulgamento situacao = julgamento != null ? julgamento.getSituacaoAtual() : null;
			return situacao != null
					&& situacao.getTipoSituacaoJulgamento().getId().equals(TipoSituacaoJulgamento.TipoSitucacaoJulgamentoConstant.FINALIZADO.getCodigo());
		}

		return false;
	}

	// ############################################## ENUM
	// ############################################## //

	/**
	 * Classe para guardar as informação sobre o julgamento da repercussão geral
	 * e da questão constitucional, auxilia na criação do texto de decisão.
	 */
	public static class Decisao {
		// repercussao geral
		public List<Ministro> ministrosHaQC = new LinkedList<Ministro>();
		public List<Ministro> ministrosNaoHaQC = new LinkedList<Ministro>();
		public List<Ministro> ministrosImpedidosQC = new LinkedList<Ministro>();
		public List<Ministro> ministrosAusentesQC = new LinkedList<Ministro>();

		// questao constitucional
		public List<Ministro> ministrosHaRG = new LinkedList<Ministro>();
		public List<Ministro> ministrosNaoHaRG = new LinkedList<Ministro>();
		public List<Ministro> ministrosImpedidosRG = new LinkedList<Ministro>();
		public List<Ministro> ministrosAusentesRG = new LinkedList<Ministro>();

		// reafirmação da jurisprudência
		public List<Ministro> ministrosSimRJ = new LinkedList<Ministro>();
		public List<Ministro> ministrosNaoRJ = new LinkedList<Ministro>();
		public List<Ministro> ministrosImpedidosRJ = new LinkedList<Ministro>();
		public List<Ministro> ministroAusentesRJ = new LinkedList<Ministro>();

		// ministros que manifestaram por escrito mas não manifestaram no plenário virtual
		public List<Ministro> ministrosDivergentes = new LinkedList<Ministro>();

		public Ministro ministroRelator;
		public Ministro ministroRedator;

		public TipoDecisao decisaoRG;
		public TipoDecisao decisaoQC;
		public TipoDecisao decisaoRJ;

		public static enum TipoDecisao {
			HA_QC, NAO_HA_QC, EM_ABERTO, HA_RG, NAO_HA_RG, SIM_RJ, NAO_RJ
		}

		public int getQtdHaQC() {
			return ministrosHaQC.size();
		}
		
		public int getQtdNaoHaQC() {
			return ministrosNaoHaQC.size();
		}

		public int getQtdImpedidoQC() {
			return ministrosImpedidosQC != null && ministrosImpedidosQC.size() > 0 ? ministrosImpedidosQC.size() : 0;
		}

		public int getQtdSemManifestacaoQC() {
			return ministrosAusentesQC != null && ministrosAusentesQC.size() > 0 ? ministrosAusentesQC.size() : 0;
		}

		// Se o relator votou que NÃO HÁ Questão Constitucional os votos ausentes (presunção) serão contabilizados como NÃO HÁ Repercussão Geral
		public int getQtdNaoHaRG() {
			return ministrosNaoHaRG.size();
		}
		
		public int getQtdHaRG() {
			return ministrosHaRG.size();
		}

		public int getQtdImpedidoRG() {
			return ministrosImpedidosRG != null && ministrosImpedidosRG.size() > 0 ? ministrosImpedidosRG.size() : 0;
		}

		public int getQtdSemManifestacaoRG() {
			return ministrosAusentesRG != null && ministrosAusentesRG.size() > 0 ? ministrosAusentesRG.size() : 0;
		}

		// reafirmação de jurisprudência
		public int getQtdSimRJ() {
			return ministrosSimRJ != null && ministrosSimRJ.size() > 0 ? ministrosSimRJ.size() : 0;
		}

		public int getQtdNaoRJ() {
			return ministrosNaoRJ != null && ministrosNaoRJ.size() > 0 ? ministrosNaoRJ.size() : 0;
		}

		public int getQtdSemManifestacaoRJ() {
			return ministroAusentesRJ != null && ministroAusentesRJ.size() > 0 ? ministroAusentesRJ.size() : 0;
		}
		
		public boolean getPossuiDivergente() {
			return ministrosDivergentes != null && ministrosDivergentes.size() > 0;
		}

	}

	private static class NotificacaoMinistro {
		Ministro relator;
		List<JulgamentoProcesso> listaJulgamentos = new LinkedList<JulgamentoProcesso>();

		public NotificacaoMinistro(Ministro ministro) {
			this.relator = ministro;
		}

	}

	private static class NotificacaoLogonMinistro {
		NotificacaoLogon notificacaoLogon;
		Setor setor;

		public NotificacaoLogonMinistro(NotificacaoLogon notificacaoLogon, Setor setor) {
			this.notificacaoLogon = notificacaoLogon;
			this.setor = setor;
		}
	}

	public Boolean recuperarPossuiVotoDivergente(Processo processo) throws ServiceException {
		try {
			Decisao decisao = recuperarDecisao(processo);
			if (decisao != null && decisao.getPossuiDivergente())
				return Boolean.TRUE;
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

	@Override
	public boolean verificarPodeCriarDecisaoRepercussaoGeral(Long seqObjetoIncidente) throws ServiceException {
		try {
			if (seqObjetoIncidente != null && seqObjetoIncidente.longValue() > 0) {
				ObjetoIncidente<?> oi = objetoIncidenteService.recuperarPorId(seqObjetoIncidente);
				Texto textoDecisao = textoService.recuperar(oi, TipoTexto.DECISAO_SOBRE_REPERCURSAO_GERAL);
				if (textoDecisao == null) {
					return true;
				}
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * Persiste o registro de substituição da opção JULGAMENTO DE MÉRITO EM
	 * OUTRO PROCESSO
	 */
	public void persistirRegistroSubstituicaoRepercussaoGeral(List<AndamentoProcesso> listaAndamentoProcesso, List<ProcessoTema> listaProcessoTema,
			String usuario, Setor setor) throws ServiceException {
		try {

			// Lançar Andamentos do Processo Origem e Processo Destino
			if (listaAndamentoProcesso != null && listaAndamentoProcesso.size() > 0) {
				for (AndamentoProcesso andamentoProcesso : listaAndamentoProcesso) {
					Processo objetoIncidente = (Processo) andamentoProcesso.getObjetoIncidente();
					andamentoProcesso.setNumeroSequencia(andamentoProcessoService.recuperarProximoNumeroSequencia(objetoIncidente));
					andamentoProcesso.setCodigoUsuario(usuario);
					andamentoProcesso.setSetor(setor);
					andamentoProcessoService.incluir(andamentoProcesso);
				}
			}

			// Lançar Alterações no Processo Origem e Processo Destino
			if (listaProcessoTema != null && listaProcessoTema.size() > 0) {
				for (ProcessoTema processoTema : listaProcessoTema) {
					// *** Gerando o incidente julgamento, se nao existir
					if (processoTema.getTipoOcorrencia().getId() == TipoOcorrenciaConstante.JULGAMENTO_LEADING_CASE.getCodigo()) {
						if (processoTema.getIncidenteJulgamento() != null && processoTema.getIncidenteJulgamento().getId() == null) {
							processoTema.setObjetoIncidente(this.recuperarIncidenteJulgamento(processoTema.getIncidenteJulgamento().getPrincipal(),
									processoTema.getIncidenteJulgamento().getTipoJulgamento().getSigla()));
						}
					}
					processoTemaService.salvar(processoTema);
				}
			}
		} catch (ServiceException e) {
			throw new ServiceException(e);
		} catch (IncidenteJulgamentoException e) {
			throw new ServiceException(e);
		}
	}
	
	@SuppressWarnings("unused")
	private void vincularAndamentoRGTextoMeritoJulgado(AndamentoProcesso andProc, String obs, TipoTexto tipoTexto, String tipoJulgamento)
			throws ServiceException {

		/***
		 * Se a Observação for do JULGADO_MERITO_DE_TEMA_COM_REPERCUSSAO_GERAL
		 * sera gerado também um arquivo na TABELA TEXTOS, vinculado a esse
		 * andamento.
		 * ***/
		Texto tex = montarTextoMeritoJulgadoRG(extrairByteRTFString(obs), andProc, tipoTexto, tipoJulgamento);
		TextoAndamentoProcesso texAndamento = new TextoAndamentoProcesso();
		texAndamento.setAndamentoProcesso(andProc);
		texAndamento.setTexto(tex);
		textoAndamentoProcessoService.persistirTextoAndamentoProcesso(texAndamento);
	}

	private Texto montarTextoMeritoJulgadoRG(byte[] bytes, AndamentoProcesso ap, TipoTexto tipoTexto, String tipoJulgamento) throws ServiceException {
		ArquivoEletronico arquivoEletronico = new ArquivoEletronico();
		arquivoEletronico.setConteudo(bytes);
		arquivoEletronico.setFormato("RTF");
		arquivoEletronicoService.incluir(arquivoEletronico);

		Texto texto = new Texto();
		texto.setObjetoIncidente(ap.getObjetoIncidente());
		texto.setSiglaClasseProcessual(ap.getSigClasseProces());
		texto.setNumeroProcessual(ap.getNumProcesso());
		texto.setArquivoEletronico(arquivoEletronico);
		texto.setTipoRestricao(TipoRestricao.P);
		texto.setDataCriacao(Calendar.getInstance().getTime());
		texto.setMinistro(situacaoMinistroProcessoService.recuperarMinistroRelatorAtual(ap.getObjetoIncidente()));
		texto.setPublico(true);
		texto.setTextosIguais(false);
		texto.setSalaJulgamento(false);
		texto.setPubliccaoRTJ(false);

		texto.setTipoTexto(tipoTexto);
		texto.setTipoJulgamento(tipoJulgamento);

		textoService.persistir(texto);
		return texto;
	}

	/**
	 * Método responsável por extrair uma STRING e retornar um objeto do tipo
	 * BYTE
	 * 
	 * @param String
	 * @return
	 * @throws ServiceException
	 */
	protected byte[] extrairByteRTFString(String texto) throws ServiceException {

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			RTFEditorKit rtfKit = new RTFEditorKit();

			// Setando para padrão de fontes.
			StyledDocument doc = (StyledDocument) rtfKit.createDefaultDocument();
			Style baseStyle = doc.addStyle("base", null);
			StyleConstants.setFontFamily(baseStyle, "Courier New");

			// Inserindo texto com stilo padronizado.
			doc.insertString(0, texto, doc.getStyle("base"));

			rtfKit.write(baos, doc, 0, doc.getLength());
			byte[] bytes = baos.toByteArray();
			baos.close();
			return bytes;

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	private ProcessoTema gerarTemaEProcessoTema(JulgamentoProcesso jp, Processo processo, String tipoJulgamento) throws ServiceException {
		/* Gerar Tema e Processo Tema */
		Tema tema = this.gerarInstanciaTema(processo);
		ProcessoTema procTema = new ProcessoTema();
		procTema.setTema(tema);
		procTema.setObjetoIncidente(jp.getObjetoIncidente());
		return gerarInstanciaProcessoTema(procTema, tipoJulgamento);
	}

	/*
	 * Persiste o registro de JULGAMENTO dos Processos na Repercussão Geral
	 */

	public Long persistirRegistroJulgamentoRepercussaoGeral(String tipoJulgamento, String dataSessaoJulgamento, Long origemDecisao, Processo processo,
			int tipoRegistroJulgamento, String observacao, boolean jaEhLeadingCase, String usuario, Setor setor) throws ServiceException {

		Long numSequenciaTema = 0L;
		JulgamentoProcesso jp = null;
		ProcessoTema processoTemaCriado = null;
		AndamentoProcesso andamentoProcesso = null;

		Colegiado colegiado = getColegiado(origemDecisao);
		try {
			switch (tipoRegistroJulgamento) {
			case 1: // RG_RECONHECIDA("1", "Repercussão Geral Reconhecida")
			{
				/*
				 * Gerando o Andamento do Processo
				 * DECISAO_EXISTENCIA_REPERCUSSAO_GERAL
				 */
				andamentoProcesso = gerarAndamentoProcesso(processo, ConstanteAndamento.DECISAO_EXISTENCIA_REPERCUSSAO_GERAL.getCodigo(), origemDecisao,
						observacao, usuario, setor);
				/*
				 * Somente gerar Julgamento, Tema e Processo Tema se o Processo
				 * não for Leading Case
				 */
				if (!jaEhLeadingCase) {
					/* Gerando Julgamento do tipo RG ou QO */
					jp = this.gerarJulgamentoProcessoTema(dataSessaoJulgamento, colegiado, tipoJulgamento, processo, andamentoProcesso);
					processoTemaCriado = this.gerarTemaEProcessoTema(jp, processo, tipoJulgamento);
				}
				break;
			}
			case 2: // RG_RECONHECIDA_E_MERITO_JULGADO ("2",
					// "Repercussão Geral Reconhecida e Mérito Julgado")
			{
				if (processo.getSiglaClasseProcessual().equalsIgnoreCase("RE")) {
					/*
					 * Gerando o Andamento do Processo
					 * DECISAO_EXISTENCIA_REPERCUSSAO_GERAL_JULGADO_MERITO
					 */
					andamentoProcesso = gerarAndamentoProcesso(processo, ConstanteAndamento.DECISAO_EXISTENCIA_REPERCUSSAO_GERAL_JULGADO_MERITO.getCodigo(),
							origemDecisao, observacao, usuario, setor);
					/*
					 * Somente gerar Julgamento, Tema e Processo Tema se o
					 * Processo não for Leading Case
					 */
					if (!jaEhLeadingCase) {
						/* Gerando Julgamento do tipo RG ou QO */
						jp = this.gerarJulgamentoProcessoTema(dataSessaoJulgamento, colegiado, tipoJulgamento, processo, andamentoProcesso);
						processoTemaCriado = this.gerarTemaEProcessoTema(jp, processo, tipoJulgamento);
					}
				} else if (processo.getSiglaClasseProcessual().equalsIgnoreCase("AI") || processo.getSiglaClasseProcessual().equalsIgnoreCase("ARE")) {
					/*
					 * Gerar o andamento
					 * DECISAO_EXISTENCIA_REPERCUSSAO_GERAL_JULGADO_MERITO
					 */
					andamentoProcesso = gerarAndamentoProcesso(processo, ConstanteAndamento.DECISAO_EXISTENCIA_REPERCUSSAO_GERAL.getCodigo(), origemDecisao,
							observacao, usuario, setor);
					/*
					 * Somente gerar Julgamento, Tema e Processo Tema se o
					 * Processo não for Leading Case
					 */
					if (!jaEhLeadingCase) {
						/* Gerando Julgamento do tipo RG ou QO */
						jp = this.gerarJulgamentoProcessoTema(dataSessaoJulgamento, colegiado, tipoJulgamento, processo, andamentoProcesso);
						processoTemaCriado = this.gerarTemaEProcessoTema(jp, processo, tipoJulgamento);
					}
					/*
					 * Gerar andamento para o
					 * AGRAVO_PROVIDO_E_JULGADO_MERITO_TEMA_REPERCUSSAO_GERAL
					 */
					andamentoProcesso = gerarAndamentoProcesso(processo, ConstanteAndamento.AGRAVO_PROVIDO_E_JULGADO_MERITO_TEMA_REPERCUSSAO_GERAL.getCodigo(),
							origemDecisao, observacao, usuario, setor);
					/* Julgamento do tipo MÉRITO */
					/*
					 * Vincular o Andamento do Processo com o Texto Gerado da
					 * Decisão
					 */
					this.gerarJulgamentoProcessoTema(dataSessaoJulgamento, colegiado, "M", processo, andamentoProcesso);
				}
				break;
			}
			case 4: // MATERIA_INFRA("4", "Matéria Infra")
			{
				/*
				 * Gerando o Andamento do Processo
				 * DECISAO_INEXISTENCIA_CONTITUCIONAL_REPERCUSSAO_GERAL
				 */
				andamentoProcesso = gerarAndamentoProcesso(processo, ConstanteAndamento.DECISAO_INEXISTENCIA_CONTITUCIONAL_REPERCUSSAO_GERAL.getCodigo(),
						origemDecisao, observacao, usuario, setor);
				/*
				 * Somente gerar Julgamento, Tema e Processo Tema se o Processo
				 * não for Leading Case
				 */
				if (!jaEhLeadingCase) {
					/* Gerando Julgamento do tipo RG ou QO */
					jp = this.gerarJulgamentoProcessoTema(dataSessaoJulgamento, colegiado, tipoJulgamento, processo, andamentoProcesso);
					processoTemaCriado = this.gerarTemaEProcessoTema(jp, processo, tipoJulgamento);
				}
				break;
			}
			case 5: // RG_NAO_RECONHECIDA("5",
					// "Repercussão Geral Não Reconhecida")
			{
				/*
				 * Gerando o Andamento do Processo -->
				 * DECISAO_INEXISTENCIA_REPERCUSSAO_GERAL
				 */
				andamentoProcesso = gerarAndamentoProcesso(processo, ConstanteAndamento.DECISAO_INEXISTENCIA_REPERCUSSAO_GERAL.getCodigo(), origemDecisao,
						observacao, usuario, setor);
				/*
				 * Somente gerar Julgamento, Tema e Processo Tema se o Processo
				 * não for Leading Case
				 */
				if (!jaEhLeadingCase) {
					/* Gerando Julgamento do tipo RG ou QO */
					jp = this.gerarJulgamentoProcessoTema(dataSessaoJulgamento, colegiado, tipoJulgamento, processo, andamentoProcesso);
					processoTemaCriado = this.gerarTemaEProcessoTema(jp, processo, tipoJulgamento);
				}
				break;
			}

			case 6: // "6", "Julgado mérito de tema com repercussão geral");
			{
				/*
				 * Gerar andamento para o Julgado mérito de tema com repercussão
				 * geral
				 */
				andamentoProcesso = gerarAndamentoProcesso(processo, ConstanteAndamento.JULGADO_MERITO_DE_TEMA_COM_REPERCUSSAO_GERAL.getCodigo(),
						origemDecisao, observacao, usuario, setor);
				/* Julgamento do tipo MÉRITO */
				this.gerarJulgamentoProcessoTema(dataSessaoJulgamento, colegiado, "M", processo, andamentoProcesso);
				/*
				 * Vincular o Andamento do Processo com o Texto Gerado da
				 * Decisão
				 */
				break;
			}
			
			case 7: // "7", "Julgado mérito de tema com repercussão geral sem tese");
			{
				/*
				 * Gerar andamento para o Julgado mérito de tema com repercussão
				 * geral
				 */
				andamentoProcesso = gerarAndamentoProcesso(processo, ConstanteAndamento.JULGADO_MERITO_DE_TEMA_COM_REPERCUSSAO_GERAL_SEM_TESE.getCodigo(),
						origemDecisao, observacao, usuario, setor);
				/* Julgamento do tipo MÉRITO */
				this.gerarJulgamentoProcessoTema(dataSessaoJulgamento, colegiado, "M", processo, andamentoProcesso);
				/*
				 * Vincular o Andamento do Processo com o Texto Gerado da
				 * Decisão
				 */
				break;
			}
			}
			if (processoTemaCriado != null && processoTemaCriado.getId() > 0L)
				numSequenciaTema = processoTemaCriado.getTema().getNumeroSequenciaTema();
		} catch (ServiceException e) {
			throw new ServiceException(e);
		} catch (ParseException e) {
			throw new ServiceException(e);
		} catch (IncidenteJulgamentoException e) {
			throw new ServiceException(e);
		}

		return numSequenciaTema;
	}

	private Colegiado getColegiado(Long origemJulgamento) {

		Colegiado colegiado = new Colegiado();
		switch (origemJulgamento.intValue()) {
		case 2:
			colegiado.setId(Colegiado.TRIBUNAL_PLENO);
			break;
		case 3:
			colegiado.setId(Colegiado.PRIMEIRA_TURMA);
			break;
		case 4:
			colegiado.setId(Colegiado.SEGUNDA_TURMA);
			break;
		default:
			colegiado = null;
			break;
		}
		return colegiado;
	}

	private Tema gerarInstanciaTema(Processo processo) throws ServiceException {
		Tema tema = new Tema();
		tema.setAssuntos(new LinkedList<Assunto>());
		tema.getAssuntos().clear();
		tema.getAssuntos().addAll(processo.getAssuntos());
		tema.setTipoTema(temaService.pesquisarTipoTema(TipoTemaConstante.REPERCUSSAO_GERAL.getCodigo(), null).get(0));
		tema.setNumeroSequenciaTema(this.buscaNumeroMaximoTema());
		tema.setSituacaoTema("A");
		temaService.salvar(tema);

		return tema;
	}

	private JulgamentoProcesso gerarJulgamentoProcessoTema(String dataSessao, Colegiado colegiado, String tipoJulgamento, Processo processo,
			AndamentoProcesso ap) throws ServiceException, ParseException, IncidenteJulgamentoException {

		ObjetoIncidente<?> oi = null;

		if (ap.getCodigoAndamento() == ConstanteAndamento.JULGADO_MERITO_DE_TEMA_COM_REPERCUSSAO_GERAL.getCodigo()
						|| ap.getCodigoAndamento() == ConstanteAndamento.JULGADO_MERITO_DE_TEMA_COM_REPERCUSSAO_GERAL_SEM_TESE.getCodigo()
				|| (ap.getCodigoAndamento() == ConstanteAndamento.DECISAO_EXISTENCIA_REPERCUSSAO_GERAL_JULGADO_MERITO.getCodigo()
						&& tipoJulgamento.equalsIgnoreCase("M"))) {
			/*
			 * Não gerar um incidente julgamento, o julgamento do merito do tema
			 * é o julgamento do processo - TIPO "M" Mérito
			 */
			oi = processo.getPrincipal();
		} else {
			oi = this.recuperarIncidenteJulgamento(processo, tipoJulgamento.equalsIgnoreCase("RG-QO") ? "QO" : tipoJulgamento);
		}

		Sessao sessao = gerarSessaoTema(dataSessao, colegiado);

		JulgamentoProcesso julgamentoProcesso = julgamentoProcessoService.recuperar(oi, sessao);

		if (julgamentoProcesso == null) {
			julgamentoProcesso = new JulgamentoProcesso();
			julgamentoProcesso.setSessao(sessao);
			julgamentoProcesso.setObjetoIncidente(oi);
			julgamentoProcesso.adicionarSituacaoJulgamento(tipoSituacaoJulgamentoService.recuperarPorId(TipoSitucacaoJulgamentoConstant.FINALIZADO.getCodigo()));
			julgamentoProcesso.setTipoJulgamento(tipoJulgamento);
			julgamentoProcesso.setAndamentoProcesso(ap);
			julgamentoProcessoService.incluir(julgamentoProcesso);
		} else {
			julgamentoProcesso.setAndamentoProcesso(ap);
			julgamentoProcessoService.salvar(julgamentoProcesso);
		}
		return julgamentoProcesso;

	}

	private Sessao gerarSessaoTema(String dataSessao, Colegiado colegiado) throws ServiceException, ParseException {
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		Date data = formatador.parse(dataSessao);
		Sessao sessao = sessaoService.pesquisarSessao(data, TipoAmbienteConstante.PRESENCIAL, null, colegiado.getId());
		if (sessao == null) {
			sessao = new Sessao();
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date data1 = format.parse(dataSessao + " 16:00:00");
			sessao.setDataInicio(data1);
			sessao.setDataFim(data1);
			sessao.setDataPrevistaInicio(data1);
			sessao.setDataPrevistaFim(data1);
			sessao.setTipoSessao(Sessao.TipoSessaoConstante.ORDINARIA.getSigla());
			sessao.setColegiado(colegiadoService.recuperarPorId(colegiado.getId()));
			sessao.setTipoAmbiente(TipoAmbienteConstante.PRESENCIAL.getSigla());
			sessao.setListaEventoSessao(new LinkedList<EventoSessao>());
			sessao.setListaEnvolvidoSessao(new LinkedList<EnvolvidoSessao>());
			sessao.setListaJulgamentoProcesso(new LinkedList<JulgamentoProcesso>());
			sessaoService.salvar(sessao);
		}
		return sessao;
	}

	private ProcessoTema gerarInstanciaProcessoTema(ProcessoTema procTema, String tipoJulgamento) throws ServiceException {
		String siglaTipoRecurso = "";
		procTema.setDataOcorrencia(Calendar.getInstance().getTime());
		if (tipoJulgamento.equalsIgnoreCase("RG-QO")){
			siglaTipoRecurso = TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL_QUESTAO_ORDEM;
		}else{
			siglaTipoRecurso = TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL;
		}
		procTema.setTipoOcorrencia(temaService.pesquisarTipoOcorrencia(TipoOcorrenciaConstante.JULGAMENTO_LEADING_CASE.getCodigo(), null).get(0));
		procTema.setNumProcesso(procTema.getNumProcesso());
		procTema.setSiglaClasse(procTema.getSiglaClasse());
		procTema.setDataOcorrencia(Calendar.getInstance().getTime());
		procTema.setTipoJulgamento(siglaTipoRecurso);
		processoTemaService.salvar(procTema);

		return procTema;
	}

	public AndamentoProcesso gerarAndamentoProcesso(Processo processo, Long CodAndamentoProcesso, Long origemDecisao, String observacao, String usuario,
			Setor setor) throws ServiceException {
		AndamentoProcesso andamento = new AndamentoProcesso();
		andamento.setCodigoAndamento(CodAndamentoProcesso);
		andamento.setNumProcesso(processo.getNumeroProcessual());
		andamento.setSigClasseProces(processo.getSiglaClasseProcessual());
		andamento.setObjetoIncidente(processo.getPrincipal());
		andamento.setCodigoUsuario(usuario);
		andamento.setSetor(setor);
		if (origemDecisao > 0) {
			OrigemAndamentoDecisao origemAndamentoDecisao = new OrigemAndamentoDecisao();
			origemAndamentoDecisao.setId(origemDecisao);
			andamento.setOrigemAndamentoDecisao(origemAndamentoDecisao);
		}
		andamento.setDescricaoObservacaoAndamento(observacao);
		Processo principal = (Processo) processo.getPrincipal();
		andamento.setNumeroSequencia(andamentoProcessoService.recuperarProximoNumeroSequencia(principal));
		andamentoProcessoService.incluir(andamento);
		return andamento;
	}

	/*
	 * Persiste o registro de início/suspensão do julgamento da RG/MÉRITO
	 */
	public Long persistirRegistroInicioSuspensaoJulgamentoRGMerito(List<Long> listaCodAndamentoProcesso, String tipoSuspensao, String dataSessaoJulgamento,
			Long origemDecisao, Processo processo, String motivoSuspensao, String julgamento, boolean jaEhLeadingCase, String usuario, Setor setor)
			throws ServiceException {
		Long numSequenciaTema = 0L;

		try {
			// Lançar Andamentos
			if (listaCodAndamentoProcesso != null && listaCodAndamentoProcesso.size() > 0) {
				for (Long codAndamentoProcesso : listaCodAndamentoProcesso) {

					if ((motivoSuspensao.equalsIgnoreCase("PV") && (codAndamentoProcesso != ConstanteAndamento.INICIADA_ANALISE_REPERCUSSAO_GERAL.getCodigo()))) {
						origemDecisao = 0L;
					}

					AndamentoProcesso andamentoProcesso = gerarAndamentoProcesso(processo, codAndamentoProcesso, origemDecisao, "", usuario, setor);
					/*
					 * Somente gerar Julgamento, Tema e Processo Tema se o
					 * Processo não for Leading Case para a Suspensao do
					 * julgamento da RG
					 */
					if ((!jaEhLeadingCase) && (andamentoProcesso.getCodigoAndamento() == ConstanteAndamento.INICIADA_ANALISE_REPERCUSSAO_GERAL.getCodigo())) {
						/* Gerando Julgamento do tipo RG ou QO */
						JulgamentoProcesso jp;

						jp = this.gerarJulgamentoProcessoTema(dataSessaoJulgamento, getColegiado(origemDecisao), julgamento, processo, andamentoProcesso);

						ProcessoTema processoTemaCriado = this.gerarTemaEProcessoTema(jp, processo, julgamento);
						if (processoTemaCriado != null && processoTemaCriado.getId() > 0L){
							numSequenciaTema = processoTemaCriado.getTema().getNumeroSequenciaTema();
						}
					}
				}
			}
		} catch (ServiceException e) {
			throw new ServiceException(e);
		} catch (ParseException e) {
			throw new ServiceException(e);
		} catch (IncidenteJulgamentoException e) {
			throw new ServiceException(e);
		}

		return numSequenciaTema;
	}

	public String consultarDecisaoRGPackage(Long idObjetoIncidente) throws ServiceException {
		try {
			return dao.pesquisarDecisaoRGPackage(idObjetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	
	public Long pesquisarManifestacaoRGPackage(String siglaClasse, Long numeroProcesso,  Long objetoIncidente) throws ServiceException {
		try {
			return dao.pesquisarManifestacaoRGPackage(siglaClasse,numeroProcesso,objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public VotoJulgamentoProcessoService getVotoJulgamentoProcessoService() {
		return votoJulgamentoProcessoService;
	}

	public void setVotoJulgamentoProcessoService(VotoJulgamentoProcessoService votoJulgamentoProcessoService) {
		this.votoJulgamentoProcessoService = votoJulgamentoProcessoService;
	}

	public SituacaoJulgamentoService getSituacaoJulgamentoService() {
		return situacaoJulgamentoService;
	}

	public void setSituacaoJulgamentoService(SituacaoJulgamentoService situacaoJulgamentoService) {
		this.situacaoJulgamentoService = situacaoJulgamentoService;
	}

	public FeriadoService getFeriadoService() {
		return feriadoService;
	}

	public void setFeriadoService(FeriadoService feriadoService) {
		this.feriadoService = feriadoService;
	}

	@Override
	public IncidenteJulgamento recuperarIncidentePeloPai(ObjetoIncidente<?> objetoIncidente, TipoIncidenteJulgamento tipoIncidenteJulgamento) throws ServiceException {
		IncidenteJulgamento filtroPesquisa = new IncidenteJulgamento();
//		filtroPesquisa.setPai(objetoIncidente);
//		filtroPesquisa.setTipoJulgamento(tipoIncidenteJulgamento);
		
		List<SearchCriterion> criterion = new ArrayList<SearchCriterion>();
		criterion.add(new EqualCriterion<ObjetoIncidente>("pai", objetoIncidente));
		criterion.add(new EqualCriterion<TipoIncidenteJulgamento>("tipoJulgamento", tipoIncidenteJulgamento));
		
		List<IncidenteJulgamento> incidentes = incidenteJulgamentoService.pesquisarPorExemplo(filtroPesquisa, criterion);
		
		if (incidentes != null && incidentes.size() > 0)
			return incidentes.get(0);
		
		return null;
	}
	

}