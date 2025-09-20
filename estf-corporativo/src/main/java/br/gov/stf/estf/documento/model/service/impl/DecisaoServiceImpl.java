package br.gov.stf.estf.documento.model.service.impl;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.text.BadLocationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.stf.estf.documento.model.service.ArquivoEletronicoService;
import br.gov.stf.estf.documento.model.service.ControleVotoService;
import br.gov.stf.estf.documento.model.service.ControleVotoTextoService;
import br.gov.stf.estf.documento.model.service.DecisaoService;
import br.gov.stf.estf.documento.model.service.TextoAndamentoProcessoService;
import br.gov.stf.estf.documento.model.service.TextoService;
import br.gov.stf.estf.documento.model.util.ControleDeVotoDTO;
import br.gov.stf.estf.documento.model.util.TipoSessaoControleVoto;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.Texto.TipoRestricao;
import br.gov.stf.estf.entidade.documento.TextoAndamentoProcesso;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.ProcessoListaJulgamento;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.ministro.Ocorrencia;
import br.gov.stf.estf.entidade.processostf.Agendamento;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.OrigemAndamentoDecisao;
import br.gov.stf.estf.entidade.processostf.SituacaoMinistroProcesso;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.estf.processostf.model.service.AgendamentoService;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoService;
import br.gov.stf.estf.processostf.model.service.IncidenteJulgamentoService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.OrigemAndamentoDecisaoService;
import br.gov.stf.estf.processostf.model.service.SituacaoMinistroProcessoService;
import br.gov.stf.estf.processostf.model.util.Dispositivo;
import br.gov.stf.estf.publicacao.compordj.builder.BuilderHelper;
import br.gov.stf.estf.publicacao.model.service.ProcessoPublicadoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.DateTimeHelper;

@Service("decisaoService")
public class DecisaoServiceImpl implements DecisaoService {

	public static final String MSG_ERRO_OBJETO_INCIDENTE_NULO = "Objeto incidente nulo";
	@Autowired
	private ProcessoPublicadoService processoPublicadoService;
	@Autowired
	private TextoService textoService;
	@Autowired
	private ArquivoEletronicoService arquivoEletronicoService;
	@Autowired
	private AgendamentoService agendamentoService;
	@Autowired
	private AndamentoProcessoService andamentoProcessoService;
	@Autowired
	private TextoAndamentoProcessoService textoAndamentoProcessoService;
	@Autowired
	private ControleVotoTextoService controleVotoTextoService;
	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;
	@Autowired
	private IncidenteJulgamentoService incidenteJulgamentoService;
	@Autowired
	private MinistroService ministroService;
	@Autowired
	private SituacaoMinistroProcessoService situacaoMinistroProcessoService;
	@Autowired
	private OrigemAndamentoDecisaoService origemAndamentoDecisaoService;
	@Autowired
	private ControleVotoService controleVotoService;
	
	/**
	 * 
	 * @param processoPublicadoService
	 * @param textoService
	 * @param arquivoEletronicoService
	 * @param agendamentoService
	 * @param andamentoProcessoService
	 * @param textoAndamentoProcessoService
	 * @param controleVotoTextoService
	 * @param objetoIncidenteService
	 * @param incidenteJulgamentoService
	 * @param ministroService
	 * @param situacaoMinistroProcessoService
	 */
	public DecisaoServiceImpl() {
		super();
	}
	
	@Deprecated
	public void salvarDecisao(String siglaProcesso , Long numeroProcesso   ,Long tipoRecurso
			                 ,Long tipoJulgamento  , Integer codigoCapitulo,Integer codigoMateria
			                 ,Integer numeroMateria, Short anoMateria      ,Date dataSessao
			                 ,Ministro ministro    , TipoSessaoControleVoto sessao ,Boolean criarControleVoto
			                 ,Long codigoAndamento , Setor setor           ,String siglaUsuario
			                 ,byte[] decisao) throws ServiceException {
		this.salvarDecisaoListasJulgamentoEmLista(null
						  						,siglaProcesso
						  						,numeroProcesso
						  						,tipoRecurso
						  						,tipoJulgamento
						  						,codigoCapitulo
						  						,codigoMateria
						  						,numeroMateria
						  						,anoMateria
						  						,dataSessao
						  						,ministro
						  						,sessao
						  						,criarControleVoto
						  						,codigoAndamento
						  						,setor
						  						,null
						  						,siglaUsuario
						  						,decisao);
	}

	@SuppressWarnings("rawtypes")
	@Deprecated
	@Transactional(rollbackFor=ServiceException.class)
	public void salvarDecisaoListasJulgamentoEmLista(ObjetoIncidente objetoIncidenteListaJulgamento
			                 						,String siglaProcesso
			                 						,Long numeroProcesso
			                 						,Long tipoRecurso
			                 						,Long tipoJulgamento
			                 						,Integer codigoCapitulo
			                 						,Integer codigoMateria
			                 						,Integer numeroMateria
			                 						,Short anoMateria
			                 						,Date dataSessao
			                 						,Ministro ministro
			                 						,TipoSessaoControleVoto sessao
			                 						,Boolean criarControleVoto
			                 						,Long codigoAndamento
			                 						,Setor setor
			                 						,ListaJulgamento listaJulgamento
			                 						,String siglaUsuario
			                 						,byte[] decisao) throws ServiceException {
		

		ObjetoIncidente objetoIncidente = this.recuperaObjetoIncidente(objetoIncidenteListaJulgamento,siglaProcesso, numeroProcesso, tipoRecurso, tipoJulgamento);
		
		// CRIA O TEXTO DE DECISAO
		Texto texto = this.geraTextoDecisao(dataSessao, ministro, decisao,objetoIncidente);
		objetoIncidenteService.flushSession();
		// GERA ANDAMENTO
		this.geraAndamento(codigoAndamento, setor, listaJulgamento,siglaUsuario, objetoIncidente, texto); 
		objetoIncidenteService.flushSession();
		
		// Associa o texto de decisão ao ProcessoListaJulgamento correspondente
		associarTextoAoProcessoListaJulgamento(listaJulgamento, objetoIncidente, texto);
		
		// INCLUI O PROCESSO EM ATA
		this.incluiProcessoEmAta(codigoCapitulo, codigoMateria, numeroMateria,anoMateria, objetoIncidente);
		objetoIncidenteService.flushSession();
		// REMOVE PROCESSO DO AGENDAMENTO
		if(listaJulgamento.getIdMinistroPedidoVista().equals(-1L))
			this.removeObjetoIncidenteAgendamento(codigoCapitulo, objetoIncidente);
		objetoIncidenteService.flushSession();
		if(!listaJulgamento.getIdMinistroPedidoVista().equals(-1L))
			atualizaMinistroVistaAgendamento(codigoCapitulo, objetoIncidente, listaJulgamento);
		objetoIncidenteService.flushSession();
		// CRIAR CONTROLE DE VOTOS
		criarControleDeVotos(dataSessao, ministro, criarControleVoto, listaJulgamento, objetoIncidente);
		objetoIncidenteService.flushSession();
	}
	
	@Deprecated
	private void associarTextoAoProcessoListaJulgamento(ListaJulgamento listaJulgamento, ObjetoIncidente objetoIncidente, Texto texto) {
		if (texto != null && TipoTexto.DECISAO.equals(texto.getTipoTexto()))
			for (ProcessoListaJulgamento plj : listaJulgamento.getListaProcessoListaJulgamento())
				if (plj.getObjetoIncidente().equals(objetoIncidente))
					plj.setTexto(texto);
	}

	@Deprecated
	@SuppressWarnings("rawtypes")
	@Transactional(rollbackFor=ServiceException.class)
	public void salvarDecisaoListasJulgamentoMesmaDecisao(List<ObjetoIncidente> objetoIncidenteListaJulgamento			                 									                 						
			                 						,ConteudoPublicacao materia			                 						
			                 						,TipoSessaoControleVoto sessao
			                 						,Boolean criarControleVoto
			                 						,Long codigoAndamento
			                 						,Setor setor
			                 						,ListaJulgamento listaJulgamento
			                 						,String siglaUsuario
			                 						,byte[] decisao) throws ServiceException {
		
		ArquivoEletronico arquivoDecisao = this.criaArquivoEletronico(decisao);
		
		for (ObjetoIncidente oi : objetoIncidenteListaJulgamento){
			
			oi = objetoIncidenteService.recuperarPorId(oi.getId());
			ObjetoIncidente objetoIncidente = objetoIncidenteService.deproxy(oi);
			
			Date dataSessao = materia.getDataCriacao();
			Ministro ministro = listaJulgamento.getMinistro();
			
			
		
			// CRIA O TEXTO DE DECISAO
			Texto texto = this.geraTextoDecisaoMesmoArquivoEletronico(dataSessao, ministro, decisao,objetoIncidente, arquivoDecisao);
			
			// GERA ANDAMENTO
			this.geraAndamento(codigoAndamento, setor, listaJulgamento,siglaUsuario, objetoIncidente, texto); 

			// Associa o texto de decisão ao ProcessoListaJulgamento correspondente
			associarTextoAoProcessoListaJulgamento(listaJulgamento, objetoIncidente, texto);

			// INCLUI O PROCESSO EM ATA
			this.incluiProcessoEmAta(materia.getCodigoCapitulo(), materia.getCodigoMateria(), materia.getNumero(),
					Short.valueOf(String.valueOf(Calendar.getInstance().get(Calendar.YEAR))), objetoIncidente);
	
			// REMOVE PROCESSO DO AGENDAMENTO
			if(listaJulgamento.getIdMinistroPedidoVista().equals(-1L))
				this.removeObjetoIncidenteAgendamento(materia.getCodigoCapitulo(), objetoIncidente);
			
			if(!listaJulgamento.getIdMinistroPedidoVista().equals(-1L))
				atualizaMinistroVistaAgendamento(materia.getCodigoCapitulo(), objetoIncidente, listaJulgamento);
			
			// CRIAR CONTROLE DE VOTOS
			criarControleDeVotos(dataSessao, ministro, criarControleVoto,listaJulgamento, objetoIncidente);
			
			// INSERE ANDAMENTO DE JULGADO
			//TODO
		}
	}
	
	@SuppressWarnings("rawtypes")
	ObjetoIncidente recuperaObjetoIncidente(ObjetoIncidente objetoIncidenteListaJulgamento, String siglaProcesso,Long numeroProcesso, Long tipoRecurso, Long tipoJulgamento) throws ServiceException {
		ObjetoIncidente objetoIncidente = null;
		
		if(objetoIncidenteListaJulgamento != null){
			objetoIncidente = objetoIncidenteListaJulgamento;
		}else{
			if ( tipoJulgamento==null || tipoJulgamento==0 ) {
				objetoIncidente = objetoIncidenteService.recuperar(siglaProcesso, numeroProcesso, tipoRecurso, tipoJulgamento);
			} else {
				objetoIncidente = incidenteJulgamentoService.inserirIncidenteJulgamento(siglaProcesso, numeroProcesso, tipoRecurso, tipoJulgamento);
			}
		}
		
		if ( objetoIncidente==null ) {
			throw new ServiceException(MSG_ERRO_OBJETO_INCIDENTE_NULO);
		}
		return objetoIncidente;
	}	

	@Deprecated
	@SuppressWarnings("rawtypes")
	Texto geraTextoDecisao(Date dataSessao, Ministro ministro, byte[] decisao,ObjetoIncidente objetoIncidente) throws ServiceException {
		ArquivoEletronico ae = this.criaArquivoEletronico(decisao);
		
		TipoTexto tipoTexto = TipoTexto.DECISAO;
		Texto t = this.criarTexto(dataSessao, ministro, objetoIncidente, ae,tipoTexto);
		return t;
	}
	
	@Deprecated
	Texto geraTextoDecisaoMesmoArquivoEletronico(Date dataSessao, Ministro ministro, byte[] decisao, ObjetoIncidente objetoIncidente, ArquivoEletronico ae) throws ServiceException{		
		TipoTexto tipoTexto = TipoTexto.DECISAO;
				
		Texto t = this.criarTexto(dataSessao, ministro, objetoIncidente, ae,tipoTexto, Boolean.TRUE);		
		return t;
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private void geraTextoAcordao(Date dataSessao, Ministro ministro,ObjetoIncidente objetoIncidente) throws ServiceException {
		byte[] decisaoConteudo = this.montarTextoDecisaoJulgamento(" ", " ");
		ArquivoEletronico ae   = this.criaArquivoEletronico(decisaoConteudo);
		TipoTexto tipoTexto = TipoTexto.ACORDAO;
		Texto t = this.criarTexto(dataSessao, ministro, objetoIncidente, ae,tipoTexto);
	}	

	@Deprecated
	@SuppressWarnings("rawtypes")
	Texto criarTexto(Date dataSessao, Ministro ministro,ObjetoIncidente objetoIncidente, ArquivoEletronico ae,TipoTexto tipoTexto) throws ServiceException {
		return criarTexto(dataSessao, ministro, objetoIncidente, ae, tipoTexto, Boolean.FALSE);
	}	
	
	@Deprecated
	Texto criarTexto(Date dataSessao, Ministro ministro,ObjetoIncidente objetoIncidente, ArquivoEletronico ae,TipoTexto tipoTexto, Boolean textosIguais ) throws ServiceException {
		Texto t = new Texto();
		
		t.setDataCriacao(DateTimeHelper.getData(DateTimeHelper.getDataString(new Date())));
		t.setArquivoEletronico(ae);
		t.setDataSessao(dataSessao);
		t.setMinistro(ministro);
		t.setObjetoIncidente(objetoIncidente);
		t.setPublico(true);			
		t.setTipoTexto(tipoTexto);
		t.setTipoRestricao(TipoRestricao.P);
		t.setTextosIguais(textosIguais);
		t = textoService.incluir(t);
		return t;
	}

	@Deprecated
	ArquivoEletronico criaArquivoEletronico(byte[] conteudo) throws ServiceException {
		ArquivoEletronico ae = new ArquivoEletronico();
		ae.setConteudo(conteudo);		
		ae.setFormato("RTF");
		arquivoEletronicoService.incluir(ae);
		
		arquivoEletronicoService.flushSession();
		
		return ae;
	}

	@Deprecated
	@SuppressWarnings("rawtypes")
	void geraAndamento(Long codigoAndamento, Setor setor,ListaJulgamento listaJulgamento, String siglaUsuario,ObjetoIncidente objetoIncidente, Texto texto) throws ServiceException {
		if (codigoAndamento != null && codigoAndamento > 0 && setor != null && siglaUsuario != null && siglaUsuario.trim().length() > 0) {
			
			String descricaoObservacaoAndamento = "Decisão: " + listaJulgamento.getTextoDecisao();
			OrigemAndamentoDecisao origemAndamentoDecisao = this.getOrigemAndamentoDecisao(listaJulgamento);
			Long numSeq = andamentoProcessoService.recuperarProximoNumeroSequencia(objetoIncidente);
			
			AndamentoProcesso andamentoProcesso = new AndamentoProcesso();
			andamentoProcesso.setCodigoAndamento(codigoAndamento);
			andamentoProcesso.setCodigoUsuario(siglaUsuario.toUpperCase());
			andamentoProcesso.setDataAndamento(new Date());
			andamentoProcesso.setDataHoraSistema(new Date());
			andamentoProcesso.setSetor(setor);
			andamentoProcesso.setObjetoIncidente(objetoIncidente);
			andamentoProcesso.setDescricaoObservacaoAndamento(descricaoObservacaoAndamento);
			andamentoProcesso.setOrigemAndamentoDecisao(origemAndamentoDecisao);
			andamentoProcesso.setNumeroSequencia(numSeq);
			andamentoProcesso.setLancamentoIndevido(false);
			
			andamentoProcesso = andamentoProcessoService.salvar(andamentoProcesso);
			//andamentoProcessoService.persistirAndamentoProcesso(andamentoDecisao);
			andamentoProcessoService.flushSession();

			this.setTextoAndamentoProcesso(texto, andamentoProcesso);
		}
	}
	
	@Deprecated
	OrigemAndamentoDecisao getOrigemAndamentoDecisao(ListaJulgamento listaJulgamento) {
		OrigemAndamentoDecisao origemDecisao = new OrigemAndamentoDecisao();
		
		if(listaJulgamento.getIdMinistroPedidoVista().equals(-1L)) {
			if(("F").equals(listaJulgamento.getSessao().getTipoAmbiente())) {
				if (listaJulgamento.getSessao().getColegiado().getId().equals(Colegiado.PRIMEIRA_TURMA)) 
					origemDecisao.setId(OrigemAndamentoDecisao.ConstanteOrigemDecisao.PRIMEIRA_TURMA.getCodigo());
				if (listaJulgamento.getSessao().getColegiado().getId().equals(Colegiado.SEGUNDA_TURMA)) 
					origemDecisao.setId(OrigemAndamentoDecisao.ConstanteOrigemDecisao.SEGUNDA_TURMA.getCodigo());
				if (listaJulgamento.getSessao().getColegiado().getId().equals(Colegiado.TRIBUNAL_PLENO)) 
					origemDecisao.setId(OrigemAndamentoDecisao.ConstanteOrigemDecisao.TRIBUNAL_PLENO.getCodigo());
			}
			
			if(("V").equals(listaJulgamento.getSessao().getTipoAmbiente())) {
				if (listaJulgamento.getSessao().getColegiado().getId().equals(Colegiado.PRIMEIRA_TURMA))
					origemDecisao.setId(OrigemAndamentoDecisao.ConstanteOrigemDecisao.PRIMEIRA_TURMA_SESSAO_VIRTUAL.getCodigo());
				if (listaJulgamento.getSessao().getColegiado().getId().equals(Colegiado.SEGUNDA_TURMA)) 
					origemDecisao.setId(OrigemAndamentoDecisao.ConstanteOrigemDecisao.SEGUNDA_TURMA_SESSAO_VIRTUAL.getCodigo());
				if (listaJulgamento.getSessao().getColegiado().getId().equals(Colegiado.TRIBUNAL_PLENO)) 
					origemDecisao.setId(OrigemAndamentoDecisao.ConstanteOrigemDecisao.TRIBUNAL_PLENO_SESSAO_VIRTUAL.getCodigo());
			}
		}
		else
			origemDecisao.setId(listaJulgamento.getIdMinistroPedidoVista());
		
		return origemDecisao;
	}	
	
	@Deprecated
	@SuppressWarnings("rawtypes")
	void incluiProcessoEmAta(Integer codigoCapitulo, Integer codigoMateria,Integer numeroMateria, Short anoMateria,ObjetoIncidente objetoIncidente) throws ServiceException {
		ProcessoPublicado pp = new ProcessoPublicado();
		pp.setAnoMateria(anoMateria);
		pp.setCodigoCapitulo(codigoCapitulo);
		pp.setCodigoMateria(codigoMateria);
		pp.setNumeroMateria(numeroMateria);
		pp.setObjetoIncidente(objetoIncidente);
		processoPublicadoService.incluir(pp);
//		processoPublicadoService.flushSession();
	}	

	/**
	 * Verica se Objeto Indicdente da STF.AGENDAMENTO, se está remove. 
	 * @param codigoCapitulo
	 * @param objetoIncidente
	 * @throws ServiceException
	 */
	@SuppressWarnings("rawtypes")
	@Deprecated
	void removeObjetoIncidenteAgendamento(Integer codigoCapitulo,ObjetoIncidente objetoIncidente) throws ServiceException {
		Agendamento agendamento = agendamentoService.recuperar(codigoCapitulo, 2, objetoIncidente);
		
		if (agendamento != null)
			agendamentoService.excluir(agendamento);
		
//		agendamentoService.flushSession();
		
	}
	
	@SuppressWarnings("rawtypes")
	@Deprecated
	void atualizaMinistroVistaAgendamento(Integer codigoCapitulo,ObjetoIncidente objetoIncidente, ListaJulgamento listaJulgamento) throws ServiceException {
		Agendamento agendamento = agendamentoService.recuperar(codigoCapitulo, 2, objetoIncidente);
		
		if (agendamento != null) {
			List<OrigemAndamentoDecisao> origensComMinistroAtivo = origemAndamentoDecisaoService.pesquisarOrigensComMinistroAtivo();
			Ministro ministroPediuVista = null;
			for(OrigemAndamentoDecisao oad : origensComMinistroAtivo) {
				if(listaJulgamento.getIdMinistroPedidoVista().equals(oad.getId().longValue()))
						ministroPediuVista = oad.getMinistro();
			}
			agendamento.setMinistro(ministroPediuVista);
			agendamento.setVista(Boolean.TRUE);
			agendamentoService.alterar(agendamento);	
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Deprecated
	void criarControleDeVotos(Date dataSessao, Ministro ministro,Boolean criarControleVoto, ListaJulgamento listaJulgamento,ObjetoIncidente objetoIncidente) throws ServiceException {
		if (criarControleVoto != null && criarControleVoto) {
			String idColegiado = listaJulgamento.getSessao().getColegiado().getId();
			
			ControleDeVotoDTO controleVoto = new ControleDeVotoDTO();
			controleVoto.setDispositivo(Dispositivo.valueOf(listaJulgamento.getDispositivoId()));
			controleVoto.setDataSessao(dataSessao);
			controleVoto.setMinistro(ministro);
			controleVoto.setSessaoJulgamento(listaJulgamento.getSessao());
			if (TipoColegiadoConstante.PRIMEIRA_TURMA.getSigla().equals(idColegiado))
				controleVoto.setTipoSessaoControleVoto(TipoSessaoControleVoto.PRIMEIRA_TURMA);
			else if (TipoColegiadoConstante.SEGUNDA_TURMA.getSigla().equals(idColegiado))
				controleVoto.setTipoSessaoControleVoto(TipoSessaoControleVoto.SEGUNDA_TURMA);
			else if (TipoColegiadoConstante.SESSAO_PLENARIA.getSigla().equals(idColegiado))
				controleVoto.setTipoSessaoControleVoto(TipoSessaoControleVoto.PLENARIO);
			controleVoto.setObjetoIncidente(objetoIncidente);
			controleVotoTextoService.criarControleVoto(controleVoto);
		}
	}
	
	@Deprecated
	void setTextoAndamentoProcesso(Texto t, AndamentoProcesso andamentoProcesso) throws ServiceException {
		TextoAndamentoProcesso tap = new TextoAndamentoProcesso();
		tap.setAndamentoProcesso(andamentoProcesso);
		tap.setTexto(t);
		textoAndamentoProcessoService.incluir(tap);
	}
	
	@Deprecated
	public byte[] montarTextoDecisaoJulgamento(String tipo, String textoDecisao) throws ServiceException {
		byte[] decisaoConteudo = null;
		try {
			decisaoConteudo = BuilderHelper.montarTextoDecisaoJulgamento(tipo, textoDecisao);
		} catch (BadLocationException e) {
			throw new ServiceException(e);
		} catch (IOException e) {
			throw new ServiceException(e);			
		}
		return decisaoConteudo;
	}
	
	@SuppressWarnings("rawtypes")
	public void incluirRelatorAcordao(String siglaProcesso
									 ,Long numeroProcesso
									 ,Long tipoRecurso
									 ,Long tipoJulgamento
									 ,Long codigoMinistroAcordao
									 ,Date dataSessao
									 ,TipoSessaoControleVoto tipoSessaoControleVoto) throws ServiceException {
		
		ObjetoIncidente objetoIncidente = objetoIncidenteService.recuperar(siglaProcesso, numeroProcesso, tipoRecurso, tipoJulgamento);
		Ministro ministroAcordao = ministroService.recuperarPorId( codigoMinistroAcordao );
		
		// INSERE A NOVA SITUACAO DO MINISTRO NO PROCESSO
		SituacaoMinistroProcesso smp = new SituacaoMinistroProcesso();
		smp.setMinistroRelator( ministroAcordao );
		smp.setObjetoIncidente( objetoIncidente );		
		smp.setOcorrencia( Ocorrencia.REDATOR_ACORDAO );
		smp.setDataOcorrencia( new Date() );
		situacaoMinistroProcessoService.incluir( smp );
		
		controleVotoTextoService.criarControleVotoEmentaAcordao(objetoIncidente, dataSessao, tipoSessaoControleVoto, TipoTexto.EMENTA, null);
		controleVotoTextoService.criarControleVotoEmentaAcordao(objetoIncidente, dataSessao, tipoSessaoControleVoto, TipoTexto.ACORDAO, null);
		
	}
	
	@SuppressWarnings("rawtypes")
	public ObjetoIncidente<?> objetoIncidenteRecuperarPorId(Long id) throws ServiceException {		
		ObjetoIncidente recuperarPorId = objetoIncidenteService.recuperarPorId(id);
		return recuperarPorId;
	}


	public ControleVotoService getControleVotoService() {
		return controleVotoService;
	}


	public void setControleVotoService(ControleVotoService controleVotoService) {
		this.controleVotoService = controleVotoService;
	}

}
