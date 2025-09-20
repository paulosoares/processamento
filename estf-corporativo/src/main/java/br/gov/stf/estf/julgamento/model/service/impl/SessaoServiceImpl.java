package br.gov.stf.estf.julgamento.model.service.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.gov.stf.estf.documento.model.service.ControleVotoService;
import br.gov.stf.estf.documento.model.util.TipoSessaoControleVoto;
import br.gov.stf.estf.entidade.documento.ControleVoto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.Texto.TipoRestricao;
import br.gov.stf.estf.entidade.documento.TipoSituacaoTexto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.julgamento.EnvolvidoSessao;
import br.gov.stf.estf.entidade.julgamento.InformacaoPautaProcesso;
import br.gov.stf.estf.entidade.julgamento.InformacaoPautaProcesso.TipoSituacaoPauta;
import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.ProcessoListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoJulgamentoVirtual;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoSessaoConstante;
import br.gov.stf.estf.entidade.julgamento.TipoSituacaoListaSessao;
import br.gov.stf.estf.entidade.julgamento.TipoSituacaoProcessoSessao;
import br.gov.stf.estf.entidade.julgamento.VotoJulgamentoProcesso;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Andamento.Andamentos;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.SessaoAudioEVideo;
import br.gov.stf.estf.julgamento.model.dataaccess.SessaoDao;
import br.gov.stf.estf.julgamento.model.service.EnvolvidoSessaoService;
import br.gov.stf.estf.julgamento.model.service.InformacaoPautaProcessoService;
import br.gov.stf.estf.julgamento.model.service.JulgamentoProcessoService;
import br.gov.stf.estf.julgamento.model.service.ListaJulgamentoService;
import br.gov.stf.estf.julgamento.model.service.ProcessoListaJulgamentoService;
import br.gov.stf.estf.julgamento.model.service.SessaoAudioEVideoService;
import br.gov.stf.estf.julgamento.model.service.SessaoService;
import br.gov.stf.estf.julgamento.model.util.SessaoResult;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoService;
import br.gov.stf.estf.publicacao.model.service.ConteudoPublicacaoService;
import br.gov.stf.estf.publicacao.model.service.FeriadoService;
import br.gov.stf.estf.usuario.model.util.TipoTurma;
import br.gov.stf.estf.util.DataUtil;
import br.gov.stf.estf.util.Email;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("sessaoService")
public class SessaoServiceImpl extends GenericServiceImpl<Sessao, Long, SessaoDao> implements SessaoService {

	@Autowired
	private ListaJulgamentoService listaJulgamentoService;
	
	@Autowired
	private JulgamentoProcessoService julgamentoProcessoService;
	
	@Autowired
	private InformacaoPautaProcessoService informacaoPautaProcessoService;
	
	@Autowired
	private SessaoAudioEVideoService sessaoAudioEVideoService;
	
	@Autowired
	private AndamentoProcessoService andamentoProcessoService;
	
	@Autowired
	private MinistroService ministroService;
	
	@Autowired
	private EnvolvidoSessaoService envolvidoSessaoService;
	
	@Autowired
	private ControleVotoService controleVotoService;

	@Autowired
	private ConteudoPublicacaoService conteudoPublicacaoService;
	
	@Autowired 
	private ProcessoListaJulgamentoService processoListaJulgamentoService;	
	
	@Autowired
	private FeriadoService feriadoService;
	
	private static final String MAIL_LIST_PLENARIO  	= "g-plenario@stf.jus.br";
	private static final String MAIL_LIST_TURMA01   	= "g-primeira-turma@stf.jus.br";
	private static final String MAIL_LIST_TURMA02   	= "g-segunda-turma@stf.jus.br";
	public  static final String MAIL_REMETENTE	 		= "G-SDEV4@stf.jus.br"; 
	public  static final String MAIL_DESENVOLVIMENTO 	= "G-SDEV4@stf.jus.br"; 
	private static final String MAIL_SUBJECT   = "Notificação de encerramento de Sessão Virtual";

	public SessaoServiceImpl(SessaoDao dao) { super(dao); } 
	
	public List<Sessao> pesquisarSessaoSQL( Date dataInicio, Date dataFim,
			Date dataPrevistaInicio, Date dataPrevistaFim, Short ano,
			Long numero, String tipoSessao, String tipoColegiado,
			String tipoAmbiente, Boolean numeroAnoPreenchido ) throws ServiceException {
		
		try {
			return dao.pesquisarSessaoSQL( dataInicio, dataFim, dataPrevistaInicio, dataPrevistaFim, ano, numero, tipoSessao, tipoColegiado, tipoAmbiente, numeroAnoPreenchido );
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
	}	
	
	public List<Sessao> pesquisarSessao( Date dataInicio, Date dataFim,
			Date dataPrevistaInicio, Date dataPrevistaFim, Short ano,
			Long numero, String tipoSessao, String tipoColegiado,
			String tipoAmbiente, Boolean numeroAnoPreenchido ) throws ServiceException {
		
		try {
			return dao.pesquisarSessao( dataInicio, dataFim, dataPrevistaInicio, dataPrevistaFim, ano, numero, tipoSessao, tipoColegiado, tipoAmbiente, numeroAnoPreenchido );
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}	
	}
	
	@Override
	public List<Sessao> pesquisarSessoesVirtuaisDeListaNaoIniciadas() throws ServiceException {
		try {
			return dao.pesquisarSessoesVirtuaisDeListaNaoIniciadas();
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}	
	}
	
	public List<Sessao> pesquisarSessoesPrevistas(Date dataBase, boolean maiorDataBase) throws ServiceException{
		try {
			return dao.pesquisarSessoesPrevistas(dataBase, maiorDataBase);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}	
	}
	
	public List<Sessao> pesquisarSessoesVirtuaisNaoIniciadas(Colegiado colegiado, TipoSessaoConstante tipoSessaoConstante, TipoJulgamentoVirtual tipoJulgamentoVirtual) throws ServiceException{
		List<Sessao> sessoesNaoIniciadas = new ArrayList<Sessao>();
		
		try{
			if (colegiado.getId() != null) {
				List<Sessao> sessoes = dao.pesquisarSessaoNaoEncerrada(TipoColegiadoConstante.valueOfSigla(colegiado.getId()), TipoAmbienteConstante.VIRTUAL, tipoSessaoConstante, tipoJulgamentoVirtual);
				
				for (Sessao sessao : sessoes){
					if(sessao.getDataInicio() == null)
						sessoesNaoIniciadas.add(sessao);
				}
			}
		}catch(DaoException e){
			throw new ServiceException(e);
		}
		
		return sessoesNaoIniciadas;
	}		

	public Date recuperarDataSessaoJulgamento(String siglaClasse,
			Long numeroProcesso, Long recurso, String tipoJulgamento)
			throws ServiceException {

		try {
			return dao.recuperarDataSessaoJulgamento( siglaClasse, numeroProcesso, recurso, tipoJulgamento );
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		
	}
	
	public Long recuperarMaiorNumeroSessao() throws ServiceException {
		try {
			return dao.recuperarMaiorNumeroSessao();
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public Long recuperarMaiorNumeroSessaoVirtual(Colegiado colegiado, short ano)
			throws ServiceException {
		try{
			return dao.recuperarMaiorNumeroSessaoVirtual(colegiado, ano);
		}catch(DaoException e){
			throw new ServiceException(e);
		}
	}

	public List<Sessao> pesquisarSessao(Date dataInicioSessao,
			Date dataFimSessao, TipoAmbienteConstante tipoAmbiente,
			TipoSessaoConstante tipoSessao, String colegiado)throws ServiceException {
		try {
			return dao.pesquisarSessao(dataInicioSessao, dataFimSessao, tipoAmbiente, tipoSessao, colegiado);
		} catch( DaoException e) {
			throw new ServiceException( e );
		}
	}
	
	
	public List<Sessao> pesquisarSessaoVirtual(Date dataInicioSessao,
			Date dataFimSessao, TipoAmbienteConstante tipoAmbiente,
			TipoSessaoConstante tipoSessao, String colegiado)throws ServiceException {
		try {
			return dao.pesquisarSessaoVirtual(dataInicioSessao, dataFimSessao, tipoAmbiente, tipoSessao, colegiado);
		} catch( DaoException e) {
			throw new ServiceException( e );
		}
	}
	
	public Sessao pesquisarSessao(Date dataInicioSessao,
			TipoAmbienteConstante tipoAmbiente,
			TipoSessaoConstante tipoSessao, String colegiado)throws ServiceException {
		try {
			return dao.pesquisarSessao(dataInicioSessao, tipoAmbiente, tipoSessao, colegiado);
		} catch( DaoException e) {
			throw new ServiceException( e );
		}
	}

	public Sessao recuperar(Long seqObjetoIncidente) throws ServiceException {
		try {
			return dao.recuperar(seqObjetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<Sessao> recuperarExclusivoDigital(Long seqObjetoIncidente) throws ServiceException {
		try {
			return dao.recuperarExclusivoDigital(seqObjetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public Sessao recuperar(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		try {
			return dao.recuperar( objetoIncidente );
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public Boolean recuperarJulgamentoEmAberto(Long seqObjetoIncidente)	throws ServiceException {
		try {
			Sessao sessao = dao.recuperar(seqObjetoIncidente);
			Date dataAtual = new Date();
			if(sessao != null && sessao.getDataInicio().before(dataAtual) && sessao.getDataFim().after(dataAtual)) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<SessaoResult> pesquisarSessao(String colegiado,
			TipoAmbienteConstante tipoAmbienteSessao, String tipoSessao, Date dataBase)
			throws ServiceException {
		try {
			List<Object[]> objetos = dao.pesquisarSessao(colegiado, tipoAmbienteSessao, tipoSessao, dataBase);
			List<SessaoResult> lista = new ArrayList<SessaoResult>();
			for (Object[] registro : objetos) {
				lista.add(new SessaoResult((Sessao) registro[0], (Long) registro[1]));
			}
			return lista;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<SessaoResult> pesquisarSessao(TipoColegiadoConstante colegiado, TipoAmbienteConstante tipoAmbienteSessao, String tipoSessao, Date dataBase)
			throws ServiceException {
		try {
			flushSession();
			List<Sessao> objetos = dao.pesquisarSessaoNaoEncerrada(colegiado, tipoAmbienteSessao, null, null);
			List<SessaoResult> lista = new ArrayList<SessaoResult>();
			Long quantidadeProcessosNaSessao;
			Long quantidadeProcessosDasListas;
						
			for (Sessao registro : objetos) {
				quantidadeProcessosNaSessao = dao.pesquisarQuantidadeProcessosNaSessao(registro);
				quantidadeProcessosDasListas = dao.pesquisarQuantidadeProcessosDasListas(registro);				
				lista.add(new SessaoResult(registro, quantidadeProcessosNaSessao, dao.pesquisarQuantidadeListasNaSessao(registro), registro.getColegiado().getId(),(quantidadeProcessosNaSessao+quantidadeProcessosDasListas)));
			}
			return lista;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void refresh(Sessao sessao) throws ServiceException {
		try {
			dao.refresh(sessao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<Sessao> pesquisar(TipoColegiadoConstante colegiado)	throws ServiceException {
		return this.pesquisar(colegiado,TipoAmbienteConstante.PRESENCIAL);
	}
	
	@Override
	public List<Sessao> pesquisar(TipoColegiadoConstante colegiado, TipoAmbienteConstante ambiente) throws ServiceException {
		return pesquisar(colegiado,ambiente,null);
	}


	@Override
	public List<Sessao> pesquisar(TipoColegiadoConstante colegiado,TipoAmbienteConstante ambiente, Boolean sessaoVirtualExtraordinaria)
			throws ServiceException {
		try {
			List<Sessao> listRetorno = dao.pesquisar(colegiado,ambiente, sessaoVirtualExtraordinaria);			
			return listRetorno;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Encerra uma sessão.
	 * @param sessao A sessão a ser finalizada.
	 * @param processos Map com os processos e suas Situações:
	 * 					JULGADO = Processo Julgado.
	 * 					NAO_JULGADO = Processo Não Julgado.
	 * 					SUSPENSO - Processo Suspenso.
	 * @param sessaoDestinoProcesso A Sessão de destino dos Processos.
	 * @param listas Map com as Listas e suas Situações
	 * 					JULGADA = Lista Julgada.
	 * 					NAO_JULGADA = Lista Não Julgada.
	 * @param sessaoDestinoLista A Sessão de destino das Listas. */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void encerrarSessao( Sessao sessaoAEncerrar, LinkedHashMap<JulgamentoProcesso, String> processos, Sessao sessaoDestinoProcessos, 
			                    Map<ListaJulgamento, String> listas, Sessao sessaoDestinoListas, Date dataEncerramentoSessao, 
			                    String nomeUsuario, Setor setor ) throws ServiceException {
		try {
			for ( Iterator<JulgamentoProcesso> itp = processos.keySet().iterator(); itp.hasNext();  ) {
				JulgamentoProcesso julgamentoProcesso = (JulgamentoProcesso) itp.next();  
			    String situacaoProcesso = (String) processos.get( julgamentoProcesso );
			    /* Define o destino do Processo com base na situação */
			    if ( situacaoProcesso.equalsIgnoreCase( TipoSituacaoProcessoSessao.JULGADO.toString() ) ) {
			    	/* Para a Situação de Julgado, apenas a Situação é alterada e persistida. */
			    	julgamentoProcesso.setSituacaoProcessoSessao( TipoSituacaoProcessoSessao.JULGADO );
			    	/* A Situação da Informação Pauta Processo deve ser definida como Confirmado */
			    	alterarSituacaoInformacaoPautaProcessoEPautaExtra( julgamentoProcesso.getObjetoIncidente(), TipoSituacaoPauta.C );
			    } else {
			    	if ( situacaoProcesso.equalsIgnoreCase( TipoSituacaoProcessoSessao.NAO_JULGADO.toString() ) ) {
		    			/* Para a Situação de Não_Julgado, o processo é COPIADO para a Sessão de destino. */
		    			julgamentoProcesso.setSituacaoProcessoSessao( TipoSituacaoProcessoSessao.NAO_JULGADO );
			    		/* Isso acontece quanto o destino Selecionado é do tipo Índice */
			    		if ( sessaoDestinoProcessos != null ){
			    			//julgamentoProcesso.setSessao( sessaoDestinoProcessos );
			    			/** Esse trecho é funcionalmente idêntico ao Processo em Situação SUSPENSO. **/
			    			if ( sessaoDestinoProcessos.getListaJulgamentoProcesso() == null ) {
			    				sessaoDestinoProcessos.setListaJulgamentoProcesso( new ArrayList<JulgamentoProcesso>() );
			    			}
			    			//sessaoDestinoProcessos.getListaJulgamentoProcesso().add( julgamentoProcesso );
							/* Define a Sessão para o Objeto Selecionado. */
			    			JulgamentoProcesso jp = new JulgamentoProcesso();
			    			jp.setObjetoIncidente( julgamentoProcesso.getObjetoIncidente() );
			    			jp.setSessao( sessaoDestinoProcessos );
			    			Integer ordemSessao = julgamentoProcessoService.definirOrdemSessao( sessaoDestinoProcessos );
			    			jp.setOrdemSessao( ordemSessao );
			    			jp.setSituacaoProcessoSessao( TipoSituacaoProcessoSessao.NAO_JULGADO );
			    			julgamentoProcessoService.incluir( jp );
			    		}
			    	} else {
			    		julgamentoProcesso.setSituacaoProcessoSessao( TipoSituacaoProcessoSessao.SUSPENSO );
			    	}
			    }
			}
			/* Essa situação é no Caso de Destino ÍNDICE */
			if ( sessaoDestinoProcessos != null ){
				this.alterarSessao(sessaoDestinoProcessos);
			}
			/* Salva as alterações efetuadas no Julgamento Processo. */
			for ( Iterator<JulgamentoProcesso> it = processos.keySet().iterator(); it.hasNext();  ) {
				julgamentoProcessoService.alterar( (JulgamentoProcesso) it.next() );
			}
			/* Varre as Listas, definindo se foram julgadas ou não. */
			for ( Iterator<ListaJulgamento> itl = listas.keySet().iterator(); itl.hasNext();  ) {
				ListaJulgamento listaJulgamento = (ListaJulgamento) itl.next();  
			    String situacaoLista = (String) listas.get( listaJulgamento );
			    if ( situacaoLista.equalsIgnoreCase( TipoSituacaoListaSessao.JULGADA.toString() ) ){
			    	/* A Situação da Informação Pauta Processo, para os processos da lista, deve ser definida como Confirmado */
			    	alterarSituacaoInformacaoPautaProcessoEPautaExtra( listaJulgamento.getElementos(), TipoSituacaoPauta.C );
			    	listaJulgamento.setJulgado( Boolean.TRUE );
			    } else {
			    	//TipoSituacaoListaSessao.NAO_JULGADA
			    	listaJulgamento.setJulgado( Boolean.FALSE );
			    	/* Move a Lista para uma Sessão de Destino */
					if ( sessaoDestinoListas != null ) {
						listaJulgamento.setSessao( sessaoDestinoListas );
					}
			    }
			}
			/* Altera a Sessão de Destino das Listas */
			if ( sessaoDestinoListas != null ){
				alterarSessao(sessaoDestinoListas);
			}
			/* Salva as alterações efetuadas na Lista de Julgamento. */
			for ( Iterator<ListaJulgamento> itl = listas.keySet().iterator(); itl.hasNext();  ) {
				listaJulgamentoService.alterar( (ListaJulgamento) itl.next() );
			}
			/* Altera a Sessão Selecionada */
			/* Define a Data fim da Sessão */
			if ( dataEncerramentoSessao != null ) {
				sessaoAEncerrar.setDataFim( dataEncerramentoSessao );
			} else {
				sessaoAEncerrar.setDataFim( new Date() );
			}
			/* Disponibilizar a Sessão na Internet */
			if ( sessaoAEncerrar != null ) {
				 if ( sessaoAEncerrar.getDisponibilizadoInternet() != null  ){
					 if ( !sessaoAEncerrar.getDisponibilizadoInternet() ){
						 sessaoAEncerrar.setDisponibilizadoInternet( Boolean.TRUE );
					 }
				 }
			}
			alterarSessao(sessaoAEncerrar);
			flushSession();
		} catch (DaoException e) {
			throw new ServiceException( e );
		}
	}

	/**
	 * Altera a situação da Informação Pauta Processo e retira marcação de pauta extra */
	private void alterarSituacaoInformacaoPautaProcessoEPautaExtra(Set<ObjetoIncidente<?>> processos, TipoSituacaoPauta tipoSituacaoPauta) throws ServiceException {
		if ( processos != null ){
			for (  Iterator<ObjetoIncidente<?>> oi = processos.iterator(); oi.hasNext(); ) {
				alterarSituacaoInformacaoPautaProcessoEPautaExtra( (ObjetoIncidente<?>) oi.next(), tipoSituacaoPauta );
			}
		}
	}
	
	/**
	 * Altera a situação da Informação Pauta Processo e retira marcação de pauta extra */
	private void alterarSituacaoInformacaoPautaProcessoEPautaExtra(ObjetoIncidente<?> objetoIncidente, TipoSituacaoPauta tipoSituacaoPauta) throws ServiceException {
		try {
			InformacaoPautaProcesso informacaoPautaProcesso = informacaoPautaProcessoService.recuperar( objetoIncidente );
			if ( informacaoPautaProcesso != null ){
				informacaoPautaProcesso.setSituacaoPauta( tipoSituacaoPauta );
				informacaoPautaProcesso.setPautaExtra(false);
				informacaoPautaProcessoService.alterar( informacaoPautaProcesso );
			}
		} catch (ServiceException e) {
			throw new ServiceException( e );
		}
	}
	
	@Override
	public Long recuperarUltimoNumeroSessao(Sessao novaSessao)
			throws ServiceException {
		try {
			Long numero = dao.recuperarUltimoNumeroSessao(novaSessao);
			if (numero == null) {
				numero = 0L;
			}
			return numero;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = ServiceException.class)
	public void reabrirUltimaSessao(Colegiado colegiado) throws ServiceException {
		Sessao ultimaSessaoEncerrada = recuperarUltimaSessaoEncerrada(colegiado);
		Hibernate.initialize(ultimaSessaoEncerrada.getListaJulgamentoProcesso());
		
		List<JulgamentoProcesso> listaJulgamentoProcesso = ultimaSessaoEncerrada.getListaJulgamentoProcesso();
		
		for (JulgamentoProcesso julgamentoProcesso : listaJulgamentoProcesso) {
			if (!julgamentoProcesso.getSituacaoProcessoSessao().equals(TipoSituacaoProcessoSessao.JULGADO)) {
				JulgamentoProcesso novoJulgamentoProcesso = julgamentoProcessoService.pesquisaSessaoNaoFinalizada(
						julgamentoProcesso.getObjetoIncidente(), TipoAmbienteConstante.PRESENCIAL);

				if (novoJulgamentoProcesso != null) {
					julgamentoProcessoService.removerJulgamentoProcesso(novoJulgamentoProcesso.getObjetoIncidente(), novoJulgamentoProcesso.getSessao());
				}
			}
		}
		
		ultimaSessaoEncerrada.setDataFim(null);
		salvar(ultimaSessaoEncerrada);
		
	}

	private Sessao recuperarUltimaSessaoEncerrada(Colegiado colegiado) throws ServiceException {
		try {
			return dao.recuperarUltimaSessaoEncerrada(colegiado);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public Sessao alterarSessaoJulgamentoAudioVideo(Sessao sessao, SessaoAudioEVideo sessaoAudioEVideo) throws ServiceException {
		sessao = alterar(sessao);
		sessaoAudioEVideoService.alterar(sessaoAudioEVideo);
		return sessao;
	}

	@Override
	public void excluirSessaoJulgamentoAudioVideo(Sessao sessao) throws ServiceException {
		SessaoAudioEVideo sessaoAudioEVideo = sessaoAudioEVideoService.recuperar(sessao);
		sessaoAudioEVideoService.excluir(sessaoAudioEVideo);
		excluir(sessao);
	}

	@Override
	@Transactional(rollbackFor=ServiceException.class)
	public void iniciarSessoesVirtuais() throws ServiceException {
		try{

			List<Sessao> sessoes = dao.pesquisarSessoesVirtuaisDeListaNaoIniciadas();
			
			for (Sessao sessao : sessoes){
				//pega apenas sessões ainda não iniciadas
				if (sessao.getDataInicio() != null || !TipoJulgamentoVirtual.LISTAS_DE_JULGAMENTO.getId().equals(sessao.getTipoJulgamentoVirtual()))
					continue;
								
				sessao.setDataInicio(sessao.getDataPrevistaInicio());								
				List<ListaJulgamento> listasJulgamento = sessao.getListasJulgamento();
				
				if (sessao.getListaJulgamentoProcesso() == null)
					sessao.setListaJulgamentoProcesso(new ArrayList<JulgamentoProcesso>());
				
				//Cria e preenche o objeto JulgamentoProcesso. 			
				for (ListaJulgamento listaJulgamento : listasJulgamento){									
					
					Set<ObjetoIncidente<?>> elementos = listaJulgamento.getElementos();		
					
					for (ObjetoIncidente<?> incidente : elementos){
						
						for (Texto texto : incidente.getTextos()) {
							if (texto.getTipoTexto().equals(TipoTexto.EMENTA)) {
								texto.setTipoRestricao(TipoRestricao.N);														
							}
						}												
						
						AndamentoProcesso andamento = new AndamentoProcesso();
						andamento.setCodigoAndamento(AndamentoProcesso.COD_ANDAMENTO_INICIADO_JULGAMENTO_VIRTUAL);						
						
												
						if (incidente instanceof IncidenteJulgamento){
							IncidenteJulgamento recurso  = (IncidenteJulgamento)incidente;
							andamento.setObjetoIncidente(null);
							andamento.setRecurso(recurso.getCodigoRecurso());
							andamento.setSigClasseProces(recurso.getPrincipal().getSiglaClasseProcessual());
							andamento.setNumProcesso(recurso.getPrincipal().getNumeroProcessual());
						}
						else{							
							andamento.setObjetoIncidente(incidente);															
						}
						
						andamento.setDataAndamento(Calendar.getInstance().getTime());
						andamento.setDataHoraSistema(Calendar.getInstance().getTime());
						andamento.setNumeroSequencia(andamentoProcessoService.recuperarProximoNumeroSequencia(incidente));
						andamento.setDescricaoObservacaoAndamento("Iniciado "+ defineObservacaoAndamentoTerminoSessao(sessao.getDataInicio()) + incidente.getIdentificacao() + ".");						
						andamento.setLancamentoIndevido(Boolean.FALSE);
						andamentoProcessoService.salvar(andamento);

						ProcessoListaJulgamento processoListaJulgamento = processoListaJulgamentoService.recuperarProcessoListaJulgamento(incidente, listaJulgamento);
						JulgamentoProcesso julgamentoProcesso = processoListaJulgamento.getJulgamentoProcesso();
						
						if (julgamentoProcesso == null) {
							julgamentoProcesso = new JulgamentoProcesso();						
							julgamentoProcesso.setSessao(sessao);
							julgamentoProcesso.setObjetoIncidente(incidente);
							julgamentoProcesso.setSituacaoProcessoSessao(TipoSituacaoProcessoSessao.NAO_JULGADO);
							julgamentoProcesso.setAndamentoProcesso(andamento);
							julgamentoProcessoService.incluir(julgamentoProcesso);	
						}
						
						sessao.getListaJulgamentoProcesso().add(julgamentoProcesso);
						processoListaJulgamento.setJulgamentoProcesso(julgamentoProcesso);
						processoListaJulgamentoService.alterar(processoListaJulgamento);																																			
					}					
				}
				
				Colegiado colegiado = sessao.getColegiado();				
				List<Ministro> ministrosComponentes = null;											
				TipoTurma turma = null;
				
				if (colegiado.getId().equals(Colegiado.PRIMEIRA_TURMA))
					turma = TipoTurma.PRIMEIRA_TURMA;									
				else if (colegiado.getId().equals(Colegiado.SEGUNDA_TURMA))
					turma = TipoTurma.SEGUNDA_TURMA;						
				else if(colegiado.getId().equals(Colegiado.TRIBUNAL_PLENO))
					turma = null;									
				else
					throw new ServiceException("Colegiado inválido!");
										
				ministrosComponentes = ministroService.pesquisarMinistro(null, null, null, null, turma, Boolean.TRUE,
						Boolean.TRUE);
				for(Ministro ministro : ministrosComponentes){
					EnvolvidoSessao envolvido = new EnvolvidoSessao();
					envolvido.setSessao(sessao);
					envolvido.setMinistro(ministro);
					envolvido.setPresente(Boolean.TRUE);					
					envolvidoSessaoService.incluir(envolvido);
					sessao.getListaEnvolvidoSessao().add(envolvido);					
				}
				this.alterarSessao(sessao);
			}
		} catch(DaoException e){
			e.printStackTrace();
			throw new ServiceException(e);
		} 
	}

	@Override
	@Transactional(rollbackFor=ServiceException.class)
	public void finalizarSessoesVirtuais() throws ServiceException {
		try{
			List<Sessao> sessoes = getListSessoesEncerradas();
			List<EmailNotificacaoEncerramentoSessao> emailsNotificacao = new ArrayList<EmailNotificacaoEncerramentoSessao>();
			for (Sessao sessao : sessoes){
				//pega apenas sessões ainda não finalizadas
				boolean sessaoFinalizada = (sessao.getDataFim() != null);
				Integer tipoJulgamentoVirtual = sessao.getTipoJulgamentoVirtual();
				if (sessaoFinalizada || !TipoJulgamentoVirtual.LISTAS_DE_JULGAMENTO.getId().equals(tipoJulgamentoVirtual))
					continue;
				Date dataPrevistaFim = sessao.getDataPrevistaFim();
				sessao.setDataFim(dataPrevistaFim);
				if (sessao.getListaJulgamentoProcesso() != null){
					this.finalizarSessaoVirtualListasJulgamento(sessao);
				}
				this.alterarSessao(sessao);
				emailsNotificacao.add(this.criarEmailResultadoSessaoVirtual(sessao));				
			}
			this.notificaResultadosSessaoEmail(emailsNotificacao);
		}catch(DaoException e){
			throw new ServiceException(e);
		}
	}

	//Criado para facilitar test jUnit
	void alterarSessao(Sessao sessao) throws DaoException {
		dao.alterar(sessao);
	}

	@SuppressWarnings("rawtypes")
	void finalizarSessaoVirtualListasJulgamento(Sessao sessao) throws ServiceException {
		//Cria e preenche o objeto JulgamentoProcesso
		for (JulgamentoProcesso julgamentoProcesso : sessao.getListaJulgamentoProcesso()){
			if (!julgamentoProcesso.isDestaque() && !julgamentoProcesso.isVista()){
				List<Texto> textos = this.getTextoEmentaVoto(julgamentoProcesso);
				int indexVotoVogal = 0;
				Long proximaSequenciaVoto = 0L;
				for (Texto texto : textos) {					
					if (texto.getTipoTexto().equals(TipoTexto.EMENTA)) {
						texto.setTipoRestricao(TipoRestricao.P);
					}else if (texto.getTipoTexto().equals(TipoTexto.VOTO_VOGAL)){						
						indexVotoVogal++;
						ObjetoIncidente objetoIncidente = julgamentoProcesso.getObjetoIncidente();
						proximaSequenciaVoto = this.incrementoSequenciaControleVoto(indexVotoVogal, proximaSequenciaVoto);
					}
				}
				AndamentoProcesso andamento = this.defineAndamentoProcessoSessaoVirtualListasJulgamento(julgamentoProcesso);
				andamento.setDescricaoObservacaoAndamento("Finalizado "+ defineObservacaoAndamentoTerminoSessao(sessao.getDataFim()) + julgamentoProcesso.getObjetoIncidente().getIdentificacao() + ".");
				andamento.setLancamentoIndevido(Boolean.FALSE);
				andamentoProcessoService.salvar(andamento);				
				if (!julgamentoProcesso.isVista())
					julgamentoProcesso.setSituacaoProcessoSessao(TipoSituacaoProcessoSessao.JULGADO);
				else
					julgamentoProcesso.setSituacaoProcessoSessao(TipoSituacaoProcessoSessao.SUSPENSO);
				julgamentoProcesso.setAndamentoProcesso(andamento);
				julgamentoProcessoService.alterar(julgamentoProcesso);
			}
		}
		listaJulgamentoService.registrarResultadoJulgamento(sessao.getListasJulgamento());
	}

	private String defineObservacaoAndamentoTerminoSessao(Date dataAndamento){
		if (dataAndamento == null)
			return "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd 'de' MMMMM 'de' yyyy '('EEEEE'), às 'HH:mm");		
		String dataFormatada = dateFormat.format(dataAndamento);
		String observacaoAndamento = "Julgamento Virtual em " + dataFormatada + ", ";
				
		return observacaoAndamento;
	}

	List<Texto> getTextoEmentaVoto(JulgamentoProcesso julgamentoProcesso) {
		List<Texto> listTextosRetorno = new ArrayList<Texto>();		
		List<Texto> textos = julgamentoProcesso.getObjetoIncidente().getTextos();
		Set<VotoJulgamentoProcesso> listaVotoJulgamentoProcesso = julgamentoProcesso.getListaVotoJulgamentoProcesso();
		
		//Textos da Ementas
		for (Texto texto : textos) {
			
			if(TipoTexto.EMENTA.equals(texto.getTipoTexto())){
				listTextosRetorno.add(texto);
			}		
			
			if(TipoTexto.VOTO_VOGAL.equals(texto.getTipoTexto())){
				for (VotoJulgamentoProcesso voto : listaVotoJulgamentoProcesso) {
					
					Ministro ministroVoto  = voto.getMinistro();
					Ministro ministroTexto = texto.getMinistro();
					if(ministroTexto.equals(ministroVoto)){
						listTextosRetorno.add(texto);
					}
				}
			}
		}
		return listTextosRetorno;
	}

	/**
	 * A sequencia do controle de voto deverá iniciar em 55 EREPGERAL-505
	 * @param indexVotoVogal
	 * @param proximaSequenciaVoto
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Long incrementoSequenciaControleVoto(int indexVotoVogal,Long proximaSequenciaVoto) throws ServiceException {
		if(indexVotoVogal==1){
			proximaSequenciaVoto = 55L;
		}else{
			proximaSequenciaVoto = controleVotoService.incrementoSequenciaControleVoto(proximaSequenciaVoto);
		}
		return proximaSequenciaVoto;
	}

	AndamentoProcesso defineAndamentoProcessoSessaoVirtualListasJulgamento(JulgamentoProcesso julgamentoProcesso) throws ServiceException {
		AndamentoProcesso andamento = new AndamentoProcesso();
		andamento.setCodigoAndamento(AndamentoProcesso.COD_ANDAMENTO_FINALIZADO_JULGAMENTO_VIRTUAL);
		if (julgamentoProcesso.getObjetoIncidente() instanceof IncidenteJulgamento){
			IncidenteJulgamento recurso  = (IncidenteJulgamento)julgamentoProcesso.getObjetoIncidente();
			andamento.setObjetoIncidente(null);
			andamento.setRecurso(recurso.getCodigoRecurso());
			Processo principal = recurso.getPrincipal();
			andamento.setSigClasseProces(principal.getSiglaClasseProcessual());
			andamento.setNumProcesso(principal.getNumeroProcessual());
		}else{
			andamento.setObjetoIncidente(julgamentoProcesso.getObjetoIncidente());															
		}
		andamento.setDataAndamento(Calendar.getInstance().getTime());
		andamento.setDataHoraSistema(Calendar.getInstance().getTime());
		Long proximoNumeroSequencia = andamentoProcessoService.recuperarProximoNumeroSequencia(julgamentoProcesso.getObjetoIncidente());
		andamento.setNumeroSequencia(proximoNumeroSequencia);
		return andamento;
	}

	List<Sessao> getListSessoesEncerradas() throws DaoException {
		Calendar calendar = Calendar.getInstance();
		Date dataLimite   = calendar.getTime();
		String tipoAmbiente = Sessao.TipoAmbienteConstante.VIRTUAL.getSigla();

		return dao.pesquisarSessoesEncerradas(dataLimite,tipoAmbiente);
	} 
	
	void gerarControleVotoTexto(Texto texto, ObjetoIncidente<?> oi, Sessao sessao, Long proximaSequenciaVoto) throws ServiceException{		
		Ministro ministro = texto.getMinistro();
		Date dataFimSessaoMaisUmDia = this.getDataFimSessaoMaisUmDiaSemHora(sessao);
		
		ControleVoto controleVoto = new ControleVoto();		
		controleVoto.setObjetoIncidente(oi);
		controleVoto.setSequenciaVoto(proximaSequenciaVoto);
		controleVoto.setTipoSituacaoTexto(TipoSituacaoTexto.ATIVO_NO_CONTROLE_DE_VOTOS);
		controleVoto.setMinistro(ministro);
		controleVoto.setTipoTexto(TipoTexto.VOTO_VOGAL);
		String id = sessao.getColegiado().getId();
		if (TipoColegiadoConstante.PRIMEIRA_TURMA.getSigla().equals(id))
			controleVoto.setSessao(TipoSessaoControleVoto.PRIMEIRA_TURMA);
		else if (TipoColegiadoConstante.SEGUNDA_TURMA.getSigla().equals(id))
			controleVoto.setSessao(TipoSessaoControleVoto.SEGUNDA_TURMA);
		else if (TipoColegiadoConstante.SESSAO_PLENARIA.getSigla().equals(id))
			controleVoto.setSessao(TipoSessaoControleVoto.PLENARIO);
		controleVoto.setDataSessao(dataFimSessaoMaisUmDia);
		controleVoto.setOralEscrito("E"); //Escrito
		controleVoto.setTexto(texto);
		
		if (texto != null && texto.getDateTransicaoAtual() != null){
			Date dataTextoPublico = texto.getDateTransicaoAtual();
			if (dataTextoPublico != null){
				if(texto.getTipoFaseTextoDocumento().getCodigoFase() == FaseTexto.LIBERADO_PUBLICACAO.getCodigoFase()){
					controleVoto.setDataPublico(dataTextoPublico);
				}
			}
		}
		controleVotoService.incluir(controleVoto);
		
		Long seqTexto = texto.getId();		
		controleVotoService.sincronizaControleVotoComTexto(seqTexto, proximaSequenciaVoto, dataFimSessaoMaisUmDia);
	}

	Date getDataFimSessaoMaisUmDiaSemHora(Sessao sessao) throws ServiceException {
		GregorianCalendar calendarDataSessao = new GregorianCalendar();
		calendarDataSessao.setTime(sessao.getDataFim());
		calendarDataSessao.add(Calendar.DAY_OF_YEAR, +1);		
		Date dataSessao = calendarDataSessao.getTime();
		dataSessao = DataUtil.dateSemHora(dataSessao);
		return dataSessao;
	}
	
	//------ Os meteodos abaixo devem ser revistos -----
	
	/**
	 * Recupera um LIST de todas a listas de julgamento, liberadas de uma determinada sessao e um determinado ministro
	 * @param idSessao
	 * @param idMinistro 
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<ListaJulgamento> recuperarListasJulgamentoMinistro (Long idSessao, Long idMinistro) throws ServiceException {
		Sessao sessao = this.recuperarPorId(idSessao);
		List<ListaJulgamento> listas = new ArrayList<ListaJulgamento>();
		
		for (ListaJulgamento lista : sessao.getListasJulgamento()) {
			if (lista.getMinistro().getId().equals(idMinistro))
				listas.add(lista);
			//Listas nas quais o ministro figurou como vistor também deve aparecer.
			else if ( (lista.getMinistroVistor()!=null) && (lista.getMinistroVistor().getId().equals(idMinistro)) )
				listas.add(lista);
		}
		
		return listas;
	}
	
	
	/**
	 * Recupera um LIST de todas a listas de julgamento, liberadas das sessoes em aberto 
	 * de um ministro para um determinado colegiado e ambiente. 
	 * @param idMinistro
	 * @param colegiado
	 * @param ambiente TipoAmbienteConstante.PRESENCIAL ou TipoAmbienteConstante.VIRTUAL
	 * @return
	 * @throws ServiceException
	 */	
	@Override
	public List<ListaJulgamento> recuperaListasLiberadasColegiado(long idMinistro, TipoColegiadoConstante colegiado, TipoAmbienteConstante ambiente) throws ServiceException {
		List<Sessao> sessoesJaLiberadasJulgamento = this.pesquisar(colegiado,ambiente);
		List<ListaJulgamento> listasDeListasLiberadasSessoesColegiado = new ArrayList<ListaJulgamento>();
		for (Sessao sessao : sessoesJaLiberadasJulgamento) {
			List<ListaJulgamento> listasListaJulgamento = this.recuperarListasJulgamentoMinistro(sessao.getId(), idMinistro);
			listasDeListasLiberadasSessoesColegiado.addAll(listasListaJulgamento);
		}
		return listasDeListasLiberadasSessoesColegiado;
	}
	
	public List<Sessao> pesquisarSessoesVirtuaisFinalizadas(TipoColegiadoConstante colegiado, Boolean possuiListaJulgamento) throws ServiceException {
		try {
			return dao.pesquisarSessoesVirtuais(colegiado,possuiListaJulgamento, null, Boolean.TRUE);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
	}
	
	public List<Sessao> pesquisarSessoesVirtuaisFinalizadasIndependePublicado(TipoColegiadoConstante colegiado, Boolean possuiListaJulgamento) throws ServiceException {
		try {
			return dao.pesquisarSessoesVirtuaisIndependePublicado(colegiado,possuiListaJulgamento, null, Boolean.TRUE);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<Sessao> pesquisarSessoesVirtuaisIniciadasOuAgendadas(
			TipoColegiadoConstante colegiado) throws ServiceException {
		try {
			return dao.pesquisarSessoesVirtuais(colegiado,  null, null,  Boolean.FALSE);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}	

	@Override
	public List<SessaoResult> pesquisarResultSessoesVirtuaisIniciadasOuAgendadas(
			TipoColegiadoConstante colegiado) throws ServiceException {
		try{
			List<Sessao> sessoes = pesquisarSessoesVirtuaisIniciadasOuAgendadas(colegiado);
			List<SessaoResult> lista = new ArrayList<SessaoResult>();
			Long quantidadeListas;
			Long quantidadeProcessos = 0L;
						
			for (Sessao registro : sessoes) {
				quantidadeListas = new Long(registro.getListasJulgamento().size());				
				for(ListaJulgamento listaJulgamento : registro.getListasJulgamento()){
					quantidadeProcessos += listaJulgamento.getQuantidadeProcessos();
				}
				lista.add(new SessaoResult(registro, quantidadeProcessos, quantidadeListas, registro.getColegiado().getId(), null));
			}
			
			return lista;
			
		}catch(ServiceException e){
			throw new ServiceException(e);
		}
	}


	protected EmailNotificacaoEncerramentoSessao criarEmailResultadoSessaoVirtual(Sessao sessao) throws ServiceException {
		String destinatario = null, destinatarioStr = null;
		//verifica se o ambiente é de produção
		try {
			if (dao.isProducao()){
				if (sessao.getColegiado().getId().equals(Colegiado.TRIBUNAL_PLENO)) {
					destinatario = MAIL_LIST_PLENARIO;
					destinatarioStr = "Plenário";
				}
				else if (sessao.getColegiado().getId().equals(Colegiado.PRIMEIRA_TURMA)) {
					destinatario = MAIL_LIST_TURMA01;
					destinatarioStr = "Primeira Turma";
				}
				else{
					destinatario = MAIL_LIST_TURMA02;
					destinatarioStr = "Segunda Turma";
				}
			} else {
				if (sessao.getColegiado().getId().equals(Colegiado.TRIBUNAL_PLENO)) {
					destinatario = MAIL_DESENVOLVIMENTO;
					destinatarioStr = "Plenário";
				} else if (sessao.getColegiado().getId().equals(Colegiado.PRIMEIRA_TURMA)) {
					destinatario = MAIL_DESENVOLVIMENTO;
					destinatarioStr = "Primeira Turma";
				} else {
					destinatario = MAIL_DESENVOLVIMENTO;
					destinatarioStr = "Segunda Turma";
				}
			}
		} catch (HibernateException e1) {
			throw new ServiceException("Erro ao verificar o ambiente de execução.", e1);
		} catch (SQLException e1) {
			throw new ServiceException("Erro ao verificar o ambiente de execução.", e1);
		} catch (DaoException e1) {
			throw new ServiceException("Erro ao verificar o ambiente de execução.", e1);
		}
		
		String corpoEmail = preparaCorpoEmailNotificacao(sessao, destinatarioStr);
		
		String [] destinatarios = {destinatario};
		
		EmailNotificacaoEncerramentoSessao email = new EmailNotificacaoEncerramentoSessao();
		email.setCorpoEmail(corpoEmail);
		email.setDestinatarioAddr(destinatarios);
		
		return email;
	}
	void notificaResultadosSessaoEmail(List<EmailNotificacaoEncerramentoSessao> emailsNotificacao) throws ServiceException{
						
		try{
			for (EmailNotificacaoEncerramentoSessao email : emailsNotificacao){
				Email.enviar(Boolean.TRUE, MAIL_REMETENTE, email.getDestinatarioAddr(), null, null, MAIL_SUBJECT, email.getCorpoEmail(), null);
			}
		}catch(MessagingException e){
				throw new ServiceException("Erro ao enviar email de notificação!", e);
		}
		
	}
	
	private String preparaCorpoEmailNotificacao(Sessao sessao, String destinatarioStr){
		
		Long qtdUnanimes = 0L;
		Long qtdDivergentes = 0L;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		for (ListaJulgamento lista : sessao.getListasJulgamento()){
			if (lista.getUnanime())
				qtdUnanimes++;
			else
				qtdDivergentes++;
		}			
	
		String mensagemEncerramentoSessao = encerramentoSessaoMailtemplate;
		Calendar dataPrazo = Calendar.getInstance();
		dataPrazo.setTime(sessao.getDataFim());
		dataPrazo.add(Calendar.DAY_OF_MONTH, 7);
		
		String unanimes = (qtdUnanimes == 0) ? "Nenhuma lista " : qtdUnanimes + (qtdUnanimes != 1 ? " listas " : " lista ");
		String divergentes = (qtdDivergentes == 0) ? "Nenhuma lista " : qtdDivergentes + (qtdDivergentes != 1 ? " listas " : " lista ");
		
		mensagemEncerramentoSessao = mensagemEncerramentoSessao.replace("#colegiado", destinatarioStr);
		mensagemEncerramentoSessao = mensagemEncerramentoSessao.replace("#sessao", sessao.getNumero().toString());
		
		if (sessao != null && sessao.getDataInicio() != null)
			mensagemEncerramentoSessao = mensagemEncerramentoSessao.replace("#dataInicio", dateFormat.format(sessao.getDataInicio()));
		
		if (sessao != null && sessao.getDataFim() != null)
			mensagemEncerramentoSessao = mensagemEncerramentoSessao.replace("#dataFim", dateFormat.format(sessao.getDataFim()));
		
		mensagemEncerramentoSessao = mensagemEncerramentoSessao.replace("#prazoLancamento", dateFormat.format(dataPrazo.getTime()));
		mensagemEncerramentoSessao = mensagemEncerramentoSessao.replace("#resultadoUnanimes", unanimes + "com julgamento unânime.");
		mensagemEncerramentoSessao = mensagemEncerramentoSessao.replace("#resultadoDivergentes", divergentes + "com julgamento com votos divergentes");
				
		return mensagemEncerramentoSessao;
	}
	
	private String encerramentoSessaoMailtemplate =   "<html> " 
													+ "	<head> " 
													+ "		<title>Encerramento de Julgamento Virtual</title>"
													+ "		<style> "
													+ "			*{"
													+ "				margin: 0;"
													+ "				padding: 0;"
													+ "				font-family: Verdana;"
													+ "				color: #2D3134;"
													+ "			}"
													+ "			.main{"
													+ "				background: #EEF3F7;"
													+ "			}"
													+ "			.main td{"
													+ "				padding: 10px;"
													+ "			}"
													+ "			.main h1{"
													+ "				font-size: 18px;"
													+ "			}"
													+ "			.info{"
													+ "				background-color: #FFFFFF;"
													+ "				padding: 10px;"
													+ "				width: 100%;"
													+ "			}"
													+ "			.info td{"
													+ "				padding: 5px;"
													+ "			}"
													+ "			.info h3{"
													+ "				font-size: 13px;"
													+ "			}"
													+ "			.info h2{"
													+ "				font-size: 14px;"
													+ "			}"
													+ "			.label-encerrado{"
													+ "				background-color: #9A0201;"
													+ "				text-align: center;"
													+ "				color: #FFFFFF;"
													+ "				padding: 2px;"
													+ "			}"
													+ "			.info-resultado a{"
													+ "				color: #0F64C1;"
													+ "				font-size: 12px;"
													+ "				font-weight: bold;"
													+ "			}"
													+ "			.info-resultado ul{"
													+ "				list-style: none;"
													+ "			}"
													+ "		</style>"
													+ "	</head>"
													+ "	<body>"
													+ "		<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"main\">"
													+ "			<tr>"
													+ "				<td><h1>Julgamento Virtual - #colegiado </h1></td> "
													+ "			</tr>"
													+ "			<tr>"
													+ "				<td>"
													+ "					<table class=\"info\"> "
													+ "						<tr>"
													+ "							<td><h3>#sessaoª Sessão. #dataInicio a #dataFim</h3></td>"
													+ "							<td width=\"150\" class=\"label-encerrado\">Encerrado</td>"
													+ "						</tr>"
													+ "					</table>"
													+ "				</td>"
													+ "			</tr>"
													+ "			<tr>"
													+ "				<td>"
													+ "					<table class=\"info\">"
													+ "						<tr>"
													+ "							<td height=\"10\"></td>"
													+ "						</tr>"
													+ "						<tr>"
													+ "							<td class=\"info-resultado\">"
													+ "								<ul>"
													+ "									<li><a href=\"http://sistemas.stf.jus.br/repgeral/jsp/principal/principal.jsf\">#resultadoUnanimes</a></li>"
													+ "									<li><a href=\"http://sistemas.stf.jus.br/repgeral/jsp/principal/principal.jsf\">#resultadoDivergentes</a></li>"
													+ "								</ul>"
													+ "							</td>"
													+ "						</tr>"
													+ "					</table>"
													+ "				</td>"
													+ "			</tr>"
													+ "		</table>"
													+ "	</body>"
													+ "</html>";

	public class EmailNotificacaoEncerramentoSessao{
		private String corpoEmail;
		private String[] destinatarioAddr;
		
		public String getCorpoEmail() {
			return corpoEmail;
		}
		public void setCorpoEmail(String corpoEmail) {
			this.corpoEmail = corpoEmail;
		}
		
		public String[] getDestinatarioAddr() {
			return destinatarioAddr;
		}
		
		public void setDestinatarioAddr(String[] destinatarioAddr) {
			this.destinatarioAddr = destinatarioAddr;
		}			
	}
	
	@Override
	public Boolean isMinistroParticipanteSessao(Sessao sessao, Ministro ministro) {
		List<EnvolvidoSessao> listaEnvolvidos = sessao.getListaEnvolvidoSessao();
		for (EnvolvidoSessao envolvido : listaEnvolvidos){
			if (ministro.getId().equals(envolvido.getMinistro().getId()))
					return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	private Sessao recuperaProximaSessaoVirtual(Calendar dataLiberacao, List<Calendar> feriados, Colegiado colegiado, TipoSessaoConstante tipoSessaoConstante, Boolean ignorarCpc, TipoJulgamentoVirtual tipoJulgamentoVirtual) throws ServiceException {
		
		if (TipoJulgamentoVirtual.REPERCUSSAO_GERAL.equals(tipoJulgamentoVirtual)) // cria uma sessão virtual por repercussão geral
			return null;
		
		List<Sessao> sessoesAbertas = pesquisarSessoesVirtuaisNaoIniciadas(colegiado, tipoSessaoConstante, tipoJulgamentoVirtual);
		
		for (Sessao sessao : sessoesAbertas)
			if (tipoJulgamentoVirtual.getId().equals(sessao.getTipoJulgamentoVirtual())) {
				StringBuffer memoriaCalculo = new StringBuffer();
				
				if (ignorarCpc == null)
					ignorarCpc = false;
				
				if (sessaoDentroDoPrazo(Andamentos.INCLUSAO_EM_PAUTA.getId(), sessao, dataLiberacao, feriados, ignorarCpc, memoriaCalculo, tipoJulgamentoVirtual)) {
					sessao.setMemoriaCalculo(memoriaCalculo.toString());
					return sessao;
				}
			}

		return null;
	}
	
	public boolean sessaoDentroDoPrazo(Long idTipoAndamento, Sessao sessao, Calendar dataLiberacao, List<Calendar> feriados, boolean ignorarCpc, StringBuffer memoriaCalculo, TipoJulgamentoVirtual tipoJulgamentoVirtual) {
		if (Andamentos.APRESENTACAO_EM_MESA.getId().equals(idTipoAndamento))
			return true;
		
		Date inicioSessao = DataUtil.getDataInicioSessao(sessao);
		
		boolean pautaFechada = false; // para Repercussão Geral a pauta estará sempre disponível
		
		if (TipoJulgamentoVirtual.LISTAS_DE_JULGAMENTO.equals(tipoJulgamentoVirtual))
			pautaFechada = conteudoPublicacaoService.isPautaFechada(sessao.getColegiado(), DataUtil.date2Calendar(dataLiberacao.getTime()));
		
		Date primeiroDiaLiberado = DataUtil.inicioDia(recuperaProximoDiaRespeitandoPrazo(dataLiberacao, feriados, pautaFechada, ignorarCpc, memoriaCalculo).getTime());
		
		return inicioSessao.getTime() >= primeiroDiaLiberado.getTime();
	}
	
	public Calendar recuperaProximoDiaRespeitandoPrazo(Calendar dataLiberacao, List<Calendar> feriados, boolean pautaFechada, boolean ignorarCpc, StringBuffer memoriaCalculo) {
		
		int qtdDiasEntreLiberacaoEJulgamento = 6;
		
		Calendar hoje = (Calendar) dataLiberacao.clone();
		
		if (ignorarCpc)
			qtdDiasEntreLiberacaoEJulgamento = 0;
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		if (pautaFechada)
			qtdDiasEntreLiberacaoEJulgamento++;
	
		memoriaCalculo.append(ignorarCpc ? "\nIgnorar CPC: Sim" : "\nIgnorar CPC: Não");
		memoriaCalculo.append(pautaFechada ? "\nPauta Fechada: Sim" : "\nPauta Fechada: Não");
		memoriaCalculo.append("\nDias úteis entre liberação e julgamento: " + qtdDiasEntreLiberacaoEJulgamento);
		memoriaCalculo.append("\n" +sdf.format(hoje.getTime()) + " - Liberação");
		
		int diasUteis = 0;

		while(diasUteis < qtdDiasEntreLiberacaoEJulgamento){
			hoje.add(Calendar.DAY_OF_MONTH, 1);
			if(DataUtil.isDiaUtil(hoje,feriados)) {
				diasUteis++;
				memoriaCalculo.append("\n" + DataUtil.date2String(hoje.getTime(), false) + " - " + diasUteis + "º dia útil");
			} else {
				memoriaCalculo.append("\n" + DataUtil.date2String(hoje.getTime(), false) + " - dia NÃO útil");
			}
		}
		
		// Retorna o próximo dia (útil ou não) após acumular a quantidade de dias a aguardar 
		hoje.add(Calendar.DAY_OF_MONTH, 1);
		
		SimpleDateFormat diaSemana = new SimpleDateFormat ("EEEE", new Locale("pt","BR"));
		Date primeiroDiaPermitido = hoje.getTime();
		Date dataPrevistaInicio = DataUtil.dateSessaoInicio(hoje.getTime());
		
		while (dataPrevistaInicio.getTime() > primeiroDiaPermitido.getTime() ) {
			memoriaCalculo.append(String.format("\n%s - %s - Não há sessão", sdf.format(primeiroDiaPermitido), diaSemana.format(primeiroDiaPermitido)));
			Calendar dataAux = Calendar.getInstance();
			dataAux.setTime(primeiroDiaPermitido);
			dataAux.add(Calendar.DAY_OF_MONTH, 1);
			primeiroDiaPermitido.setTime(DataUtil.inicioDia(dataAux.getTime()).getTime());
		}
		
		memoriaCalculo.append(String.format("\n%s - %s - DIA DE SESSÃO", sdf.format(primeiroDiaPermitido), diaSemana.format(primeiroDiaPermitido)));

		
		return hoje;
	}
	
	public Sessao montarSessaoVirtual(Calendar dataLiberacaoLista, List<Calendar> feriados, Colegiado colegiado, TipoJulgamentoVirtual tipoJulgamentoVirtual,
			boolean ignorarCpc) throws ServiceException {
		StringBuffer memoriaCalculo = new StringBuffer();

		boolean pautaFechada = false; // para Repercussão Geral a pauta estará sempre disponível

		if (TipoJulgamentoVirtual.LISTAS_DE_JULGAMENTO.equals(tipoJulgamentoVirtual))
			pautaFechada = conteudoPublicacaoService.isPautaFechada(colegiado, dataLiberacaoLista);

		Calendar primeiroDiaLiberado = recuperaProximoDiaRespeitandoPrazo(dataLiberacaoLista, feriados, pautaFechada, ignorarCpc, memoriaCalculo);

		Date dataPrevistaInicio = DataUtil.dateSessaoInicio(primeiroDiaLiberado.getTime());
		Short ano = DataUtil.getAnoSessaoVirtual(dataPrevistaInicio);
		Date dataPrevistaFim = DataUtil.datSessaoFim(dataPrevistaInicio, feriados);
		Long numeroUltimaSessao = recuperarMaiorNumeroSessaoVirtual(colegiado, ano);
		Long numeroSessao = (numeroUltimaSessao == null) ? 1 : numeroUltimaSessao + 1;

		Sessao sessao = new Sessao();
		sessao.setDataInicio(null);

		if (TipoJulgamentoVirtual.LISTAS_DE_JULGAMENTO.equals(tipoJulgamentoVirtual)) {
			Calendar dataPrevistaInicioComHora = DataUtil.date2Calendar(dataPrevistaInicio);
			dataPrevistaInicioComHora.set(Calendar.HOUR_OF_DAY, 11);
			sessao.setDataPrevistaInicio(dataPrevistaInicioComHora.getTime());
		} else {
			sessao.setDataPrevistaInicio(dataPrevistaInicio);
		}

		sessao.setAno(ano);
		sessao.setDataFim(null);
		sessao.setDataPrevistaFim(dataPrevistaFim);
		sessao.setTipoAmbiente(TipoAmbienteConstante.VIRTUAL.getSigla());
		sessao.setColegiado(colegiado);
		sessao.setTipoSessao(Sessao.TipoSessaoConstante.ORDINARIA.getSigla());
		sessao.setNumero(numeroSessao);
		sessao.setTipoJulgamentoVirtual(tipoJulgamentoVirtual.getId());
		sessao.setExclusivoDigital(false);
		sessao.setMemoriaCalculo(memoriaCalculo.toString());
		return sessao;
	}
	
	@Override
	@Transactional
	public Sessao recuperarSessao(Calendar dataLiberacao, Colegiado colegiado, Boolean ignorarCpc, TipoJulgamentoVirtual tipoJulgamentoVirtual) throws ServiceException {
		List<Calendar> feriados = feriadoService.recuperarProximosFeriados(dataLiberacao, 3);
		
		Sessao sessaoVirtual = recuperaProximaSessaoVirtual(dataLiberacao, feriados, colegiado, TipoSessaoConstante.ORDINARIA, ignorarCpc, tipoJulgamentoVirtual);
		
		Date proximaSexta = DataUtil.dateSessaoInicio(dataLiberacao.getTime(), feriados);

		if (sessaoVirtual == null || (ignorarCpc && sessaoVirtual.getDataPrevistaInicio().getTime() != proximaSexta.getTime()))
			sessaoVirtual = montarSessaoVirtual(dataLiberacao, feriados, colegiado, tipoJulgamentoVirtual, ignorarCpc);
		
		return sessaoVirtual;
	}
	
	public boolean isExclusivoDigital(ObjetoIncidente oi) throws ServiceException {
		try {
			return dao.isExclusivoDigital(oi);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}