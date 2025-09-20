package br.gov.stf.estf.publicacao.model.service.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.service.ArquivoEletronicoService;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Agendamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.publicacao.EstruturaPublicacao;
import br.gov.stf.estf.entidade.publicacao.Feriado;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;
import br.gov.stf.estf.entidade.publicacao.TipoSessao;
import br.gov.stf.estf.entidade.tarefa.TipoCampoTarefa.TipoCampoTarefaContante;
import br.gov.stf.estf.entidade.tarefa.TipoCampoTarefaValor;
import br.gov.stf.estf.jurisdicionado.model.service.impl.ConsultaConteudoPublicacao;
import br.gov.stf.estf.publicacao.BuilderDj;
import br.gov.stf.estf.publicacao.compordj.builder.BuilderHelper;
import br.gov.stf.estf.publicacao.compordj.builder.ConteudoBuilder;
import br.gov.stf.estf.publicacao.compordj.modelo.Conteudo;
import br.gov.stf.estf.publicacao.compordj.modelo.ConteudoRelacao;
import br.gov.stf.estf.publicacao.compordj.modelo.ItemRelacaoWrapper;
import br.gov.stf.estf.publicacao.model.dataaccess.ConteudoPublicacaoDao;
import br.gov.stf.estf.publicacao.model.service.ConteudoPublicacaoService;
import br.gov.stf.estf.publicacao.model.service.FeriadoService;
import br.gov.stf.estf.publicacao.model.service.ProcessoPublicadoService;
import br.gov.stf.estf.publicacao.model.util.ConteudoPublicacaoDynamicRestriction;
import br.gov.stf.estf.publicacao.model.util.DadosDePublicacaoDynamicQuery;
import br.gov.stf.estf.publicacao.model.util.IConsultaDeDadosDePublicacao;
import br.gov.stf.estf.tarefa.model.service.TipoTarefaSetorService;
import br.gov.stf.estf.util.DataUtil;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;
import br.gov.stf.framework.util.DateTimeHelper;

/**
 * Service implementation for inteface ConteudoPublicacaoService.
 * 
 * @see .ConteudoPublicacaoService
 * @author Hibernate Tools
 */

@SuppressWarnings("unchecked")
@Service("conteudoPublicacaoService")
public class ConteudoPublicacaoServiceImpl extends GenericServiceImpl<ConteudoPublicacao, Long, ConteudoPublicacaoDao> implements
		ConteudoPublicacaoService {
	

	private final Log logger = LogFactory.getLog(ConteudoPublicacaoServiceImpl.class);

	private UltimaMateriaComparator ultimaMateriaComparator;
	
	@Autowired
	public ProcessoPublicadoService processoPublicadoService;
	
	@Autowired
	public FeriadoService feriadoService;
	
	@Autowired
	TipoTarefaSetorService tipoTarefaSetorService;
	
	@Autowired
	private ArquivoEletronicoService arquivoEletronicoService;

	public ConteudoPublicacaoServiceImpl( ConteudoPublicacaoDao dao, ProcessoPublicadoService processoPublicadoService, 
										  FeriadoService feriadoService, TipoTarefaSetorService tipoTarefaSetorService ) {
		super(dao);
		ultimaMateriaComparator = new UltimaMateriaComparator();
		this.processoPublicadoService = processoPublicadoService;
		this.feriadoService = feriadoService;
		this.tipoTarefaSetorService = tipoTarefaSetorService;
	}

	public Date recuperarDataAta(Integer codigoCapitulo, Integer codigoMateria, Integer numero, Short ano)
			throws ServiceException {
		List<ConteudoPublicacao> conteudos = pesquisar(codigoCapitulo, codigoMateria, 50, numero, ano, null);
		Date dataAta = null;
		if (conteudos != null && conteudos.size() > 0) {
			for (ConteudoPublicacao cp : conteudos) {
				if (dataAta == null) {
					dataAta = cp.getDataCriacao();
				} else if (!dataAta.equals(cp.getDataCriacao())) {
					throw new ServiceException("A pesquisa retornou matérias com datas diferentes.");
				}
			}
		}
		return dataAta;
	}
	
	/**
	 * Pesquisar Materias 
	 * @param codigoCapitulo
	 * @param codigoMateria pesquisa apenas um codigo de Materia
	 * @param listCodigosMateria array com varios codigos de materia
	 * @param codigoConteudo
	 * @param numero
	 * @param anoMateria
	 * @param dataCriacao
	 * @param isPublicado se a data de publicação está ou não em branco
	 * @param isComposto se a data de composição parcial está ou não em branco
	 * @param tipoSessao
	 * @return
	 * @throws DaoException
	 */
	public List<ConteudoPublicacao> pesquisarMateria(Integer codigoCapitulo
													,Integer codigoMateria
													,List<Integer> listCodigosMateria
													,Integer codigoConteudo
													,Integer numero
													,Short anoMateria
													,Date dataCriacao
													,Boolean isPublicado
													,Boolean isComposto
													,TipoSessao tipoSessao) throws ServiceException	{
		
		List<ConteudoPublicacao> resp = null;
		try {
			resp = dao.pesquisarMateria(codigoCapitulo
					,codigoMateria
					,listCodigosMateria
					,codigoConteudo
					,numero
					,anoMateria
					,dataCriacao
					,isPublicado
					,isComposto
					,tipoSessao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return resp;		
	}
	

	public List<ConteudoPublicacao> pesquisar( Integer codigoCapitulo, List<Integer> codigoMateria, Integer codigoConteudo, 
			                                   Short ano, Boolean capituloJaComposto ) throws ServiceException {
		List<ConteudoPublicacao> resp = null;
		try {
			resp = dao.recuperar(codigoCapitulo, codigoMateria, codigoConteudo, ano, null, capituloJaComposto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return resp;
	}

	public List<ConteudoPublicacao> pesquisar(Integer codigoCapitulo, Integer codigoMateria, Integer codigoConteudo,
			Integer numero, Short ano, Date dataAta) throws ServiceException {
		List<ConteudoPublicacao> resp = null;
		try {
			resp = dao.pesquisar(codigoCapitulo, codigoMateria, codigoConteudo, numero, ano, dataAta, null);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return resp;
	}
	
	public ConteudoPublicacao recuperar(Integer codigoCapitulo, Integer codigoMateria, Integer codigoConteudo, Integer numero,
			Short ano) throws ServiceException {

		if (codigoCapitulo == null || codigoMateria == null || codigoConteudo == null || numero == null || ano == null) {
			throw new IllegalArgumentException();
		}

		List<ConteudoPublicacao> lista = pesquisar(codigoCapitulo, codigoMateria, codigoConteudo, numero, ano, null);

		if (lista == null || lista.size() == 0) {
			return null;
		} else if (lista.size() > 1) {
			throw new ServiceException("Mais de uma matéria encontrada.");
		} else {
			return lista.get(0);
		}
	}
	
	/* Recuperar a lista das Matérias */
	public List<ConteudoPublicacao> recuperar(Integer codigoCapitulo, List<Integer> codigosMateria, Integer codigoConteudo, Integer numero,
			Short anoMateria, Date dataCriacao) throws ServiceException {
		List<ConteudoPublicacao> materias = null;
		try {
			materias = dao.recuperar(codigoCapitulo, codigosMateria, codigoConteudo, anoMateria, dataCriacao, false);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return materias;
	}

	public List<ConteudoPublicacao> pesquisarMateriasDJ(Long idPublicacao) throws ServiceException {
		List<ConteudoPublicacao> materias = null;
		try {
			materias = dao.pesquisarMateriasDJ(idPublicacao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return materias;
	}

	public List<ConteudoPublicacao> pesquisarMateriasPublicacaoAntigo(Long idPublicacao) throws ServiceException {
		List<ConteudoPublicacao> materias = null;
		try {
			materias = dao.pesquisarMateriasPublicacaoAntigo(idPublicacao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return materias;
	}

	public List<ConteudoPublicacao> pesquisarMateriasPublicacao() throws ServiceException {
		List<ConteudoPublicacao> materias = null;
		try {
			materias = dao.pesquisarMateriasPublicacao();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return materias;
	}

	public List<ConteudoPublicacao> pesquisarMateriasPrevia(Date dataComposicao) throws ServiceException {
		List<ConteudoPublicacao> materias = null;
		try {
			materias = dao.pesquisarMateriasPrevia(dataComposicao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return materias;
	}

	@SuppressWarnings("rawtypes")
	public Integer recuperarNumeroProcessosMateria(ConteudoPublicacao conteudoPublicacao) throws ServiceException {
		ConteudoBuilder conteudoBuilder = BuilderDj.getConteudoBuilder(conteudoPublicacao);
		List<Conteudo> conteudos = conteudoBuilder.gerarConteudo(conteudoPublicacao, 0, false, false);
		int qtd = 0;
		if (conteudos != null && conteudos.size() > 0) {
			for (Conteudo conteudo : conteudos) {
				if (conteudo instanceof ConteudoRelacao) {
					List<ItemRelacaoWrapper> relacao = ((ConteudoRelacao) conteudo).getRelacao();
					qtd += relacao.size();

					/*
					 * for ( ItemRelacaoWrapper item: relacao ) { ESTFBaseEntity
					 * entity = item.getWrappedObject(); if ( entity instanceof
					 * Distribuicao ) { Distribuicao itemDis = (Distribuicao)
					 * entity; System.out.println(
					 * itemDis.getId().getSiglaClasseProces
					 * ()+"/"+itemDis.getId().getNumeroProcesso() ); } else if (
					 * entity instanceof TextoPeticao ) { TextoPeticao itemTP =
					 * (TextoPeticao) entity;
					 * System.out.println(itemTP.getNumero
					 * ()+"/"+itemTP.getAno()); } else if ( entity instanceof
					 * ProcessoPublicado ) { ProcessoPublicado itemPP =
					 * (ProcessoPublicado) entity;
					 * System.out.println(itemPP.getClasseUnificada
					 * ().getSiglaClasseUnificada
					 * ()+"/"+itemPP.getProcesso().getId
					 * ().getNumeroProcessual()); } else if ( entity instanceof
					 * ProtocoloPublicado ) { ProtocoloPublicado itemPP =
					 * (ProtocoloPublicado) entity;
					 * System.out.println(itemPP.getProtocolo
					 * ().getId().getNumero
					 * ()+"/"+itemPP.getProtocolo().getId().getAno()); } else {
					 * System.out.println(entity); } }
					 */
				}
			}

		}
		return qtd;
	}

	public Date recuperarDataPrevistaPublicacao(Integer codigoCapitulo, Integer codigoMateria, Integer numero, Short ano,
			Date dataCriacao) throws ServiceException {

		Date dataPrevista = null;

		try {
			dataPrevista = dao.recuperarDataPrevistaPublicacao(codigoCapitulo, codigoMateria, numero, ano, dataCriacao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return dataPrevista;
	}

	public List<ConteudoPublicacao> pesquisarNaoPublicado(Integer codigoCapitulo, Integer codigoMateria, Integer numero, Short ano)
			throws ServiceException {
		List<ConteudoPublicacao> materias = null;
		try {
			materias = dao.pesquisar(codigoCapitulo, codigoMateria, null, numero, ano, null, Boolean.FALSE);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return materias;
	}

	public Integer recuperarNumeroUltimaAta(Integer codigoCapitulo, Integer codigoMateria, Integer codigoConteudo)
			throws ServiceException {
		Integer numero = null;
		List<ConteudoPublicacao> materias = pesquisar(codigoCapitulo, codigoMateria, codigoConteudo, null, null, null);
		if (materias != null && materias.size() > 0) {
			Collections.sort(materias, ultimaMateriaComparator);
			numero = ((ConteudoPublicacao) materias.get(materias.size() - 1)).getNumero();
		} else {
			numero = 0;
		}

		return numero;
	}
	
	/* Recupera o número da última Ata aberto */
	public Integer recuperarNumeroUltimaAta( Integer codigoCapitulo, List<Integer> codigosMateria, Integer codigoConteudo, 
											 Short anoMateria, Boolean capituloJaComposto ) throws ServiceException {
		Integer numero = 0;
		List<ConteudoPublicacao> materias = pesquisar( codigoCapitulo, codigosMateria, codigoConteudo, anoMateria, capituloJaComposto );
		if (materias != null && materias.size() > 0) {
			Collections.sort(materias, ultimaMateriaComparator);
			numero = ((ConteudoPublicacao) materias.get(materias.size() - 1)).getNumero();
		} else {
			numero = 0;
		}
		return numero;
	}
	
	/* Recupera a menor Ata que ainda está sem o capítulo composto */
	public ConteudoPublicacao recuperarMenorAtaEmAberto( Integer codigoCapitulo, Integer materiaAlvo, Integer codigoConteudo, 
			                                             Short anoMateria, Date dataCriacao, Boolean capituloJaComposto ) throws ServiceException {
		
		List<Integer> codigosMateria = new LinkedList<Integer>();
		
		if ( materiaAlvo.equals( EstruturaPublicacao.COD_MATERIA_REPUBLICACOES ) ) {
			codigosMateria.add( EstruturaPublicacao.COD_MATERIA_REPUBLICACOES );
		} else {
			codigosMateria.add( EstruturaPublicacao.COD_MATERIA_ORIGINARIOS );
			codigosMateria.add( EstruturaPublicacao.COD_MATERIA_RECURSOS );
			codigosMateria.add( EstruturaPublicacao.COD_MATERIA_PROCESSOS_DE_COMPETENCIA_DA_PRESIDENCIA );
			codigosMateria.add( EstruturaPublicacao.COD_MATERIA_DECISOES_E_DESPACHOS_DO_PRESIDENTE );
		}
		
		List<ConteudoPublicacao> materias = recuperar( codigoCapitulo, codigosMateria, codigoConteudo, null, anoMateria, dataCriacao );
		List<ConteudoPublicacao> materiasValidas = new LinkedList<ConteudoPublicacao>();
		for ( ConteudoPublicacao materia: materias ) {
			if ( materia.getCodigoMateria().equals( materiaAlvo ) ) {
				materiasValidas.add( materia );
			}
		}
		
		if ( materiasValidas.size() == 0 && ( materias != null && materias.size() > 0) ) {
			Collections.sort(materias, ultimaMateriaComparator);
			return ((ConteudoPublicacao) materias.get(0));
		}
		
		if ( materiasValidas.size() > 0) {
			Collections.sort(materiasValidas, ultimaMateriaComparator);
			return ((ConteudoPublicacao) materiasValidas.get(0));
		} else {
			return null;
		}
	}
	

	public ConteudoPublicacao consultarDadosDaPublicacaoDoTexto(IConsultaDeDadosDePublicacao consulta) throws ServiceException {
		try {
			DadosDePublicacaoDynamicQuery consultaDinamica = montaConsultaDeDadosDePublicacao(consulta);
			return dao.consultarDadosDaPublicacaoDoTexto(consultaDinamica);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	public boolean isPautaDeJulgamentoFechada(ConsultaConteudoPublicacao consulta, Calendar dataLiberacao) throws ServiceException {
		try {
			ConteudoPublicacaoDynamicRestriction consultaDinamica = new ConteudoPublicacaoDynamicRestriction();
			consultaDinamica.setCodigoCapitulo(consulta.getCodigoCapitulo());
			consultaDinamica.setCodigoMateria(consulta.getCodigoMateria());
			consultaDinamica.setDataCriacao(DataUtil.dateSemHora(dataLiberacao.getTime()));
			List<ConteudoPublicacao> resultados = dao.pesquisar(consultaDinamica);
			
			for (ConteudoPublicacao cp : resultados)
				if (cp.getDataComposicaoParcial() == null)
					return false;
			
			return !resultados.isEmpty();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	private DadosDePublicacaoDynamicQuery montaConsultaDeDadosDePublicacao(IConsultaDeDadosDePublicacao consulta) {
		DadosDePublicacaoDynamicQuery consultaDinamica = new DadosDePublicacaoDynamicQuery();
		consultaDinamica.setMateriaCodigoCapitulo(consulta.getMateriaCodigoCapitulo());
		consultaDinamica.setProcessoCodigoCapitulo(consulta.getProcessoCodigoCapitulo());
		consultaDinamica.setSequencialDoArquivoEletronico(consulta.getSequencialDoArquivoEletronico());
		consultaDinamica.setSequencialObjetoIncidente(consulta.getSequencialObjetoIncidente());
		return consultaDinamica;
	}

	class UltimaMateriaComparator implements Comparator<ConteudoPublicacao> {

		public int compare(ConteudoPublicacao o1, ConteudoPublicacao o2) {
			int comp = o1.getAno().compareTo(o2.getAno());
			if (comp == 0) {
				comp = o1.getNumero().compareTo(o2.getNumero());
			}
			return comp;
		}

	}

	/**
	 * Define a Ata de Publicação no qual o texto será publicado.
	 * Esse classificação leva em conta a Estrutura de Publicação.
	 */
	public ConteudoPublicacao classificarTextoPorAta(Texto texto, EstruturaPublicacao estruturaPublicacao) throws ServiceException {
		/* Define uma data válida de Ata */
		GregorianCalendar dataAta = definirDataCriacaoAta();
		Short anoAta = definirAnoAta(dataAta);
		/* O valor false, busca as Atas com Capítulo ainda em aberto */
		ConteudoPublicacao ataEmAberto = recuperarMenorAtaEmAberto( estruturaPublicacao, anoAta, dataAta.getTime(), false ); 
		if ( ataEmAberto != null) {
			if ( estruturaPublicacao.getId().getCodigoCapitulo().equals( ataEmAberto.getCodigoCapitulo() ) && 
				 estruturaPublicacao.getId().getCodigoMateria().equals( ataEmAberto.getCodigoMateria() ) &&
				 estruturaPublicacao.getId().getCodigoConteudo().equals( ataEmAberto.getCodigoConteudo() ) ) {
				return ataEmAberto;
			} else {
				ConteudoPublicacao conteudoPublicacao = new ConteudoPublicacao();
				conteudoPublicacao.setNumero( ataEmAberto.getNumero() );
				conteudoPublicacao.setAno( ataEmAberto.getAno() );
				conteudoPublicacao.setDataCriacao( ataEmAberto.getDataCriacao() );
				conteudoPublicacao.setEstruturaPublicacao( estruturaPublicacao );
				conteudoPublicacao.setCodigoCapitulo( estruturaPublicacao.getId().getCodigoCapitulo() );
				conteudoPublicacao.setCodigoMateria( estruturaPublicacao.getId().getCodigoMateria() );
				conteudoPublicacao.setCodigoConteudo( estruturaPublicacao.getId().getCodigoConteudo() );
				return salvar( conteudoPublicacao );
			}
		} else {
			ConteudoPublicacao conteudoPublicacao = new ConteudoPublicacao();
			conteudoPublicacao.setNumero( definirNumeroNovaAtaPublicacao( estruturaPublicacao, anoAta, null ) );
			conteudoPublicacao.setAno( anoAta );
			conteudoPublicacao.setDataCriacao( dataAta.getTime() );
			conteudoPublicacao.setEstruturaPublicacao( estruturaPublicacao );
			conteudoPublicacao.setCodigoCapitulo( estruturaPublicacao.getId().getCodigoCapitulo() );
			conteudoPublicacao.setCodigoMateria( estruturaPublicacao.getId().getCodigoMateria() );
			conteudoPublicacao.setCodigoConteudo( estruturaPublicacao.getId().getCodigoConteudo() );
			return salvar( conteudoPublicacao );						
		}
	}
	
	/* Recupera a menor Ata, baseado em ANO_MATERIA e NUM_MATERIA, aberta para o dia. */
	private ConteudoPublicacao recuperarMenorAtaEmAberto( EstruturaPublicacao estruturaPublicacao, Short anoAta, 
			                                              Date dataAta, Boolean capituloJaComposto ) throws ServiceException {
		try {
			/* a informação do capituloJaComposto == false nos permite recuperar 
			 * apenas as Atas cujos capítulos ainda não foram compostos */
			return recuperarMenorAtaEmAberto( estruturaPublicacao.getId().getCodigoCapitulo(),
											  estruturaPublicacao.getId().getCodigoMateria(),
					   		  		   		  estruturaPublicacao.getId().getCodigoConteudo(),
					   		  		   		  anoAta,
					   		  		   		  dataAta,
					   		  		   	      capituloJaComposto );
		} catch (ServiceException e) {
			throw new ServiceException(e);
		}
	}
	
	private Integer definirNumeroNovaAtaPublicacao( EstruturaPublicacao estruturaPublicacao, Short anoAta, 
													Boolean capituloJaComposto ) throws ServiceException {
		List<Integer> codigoMateria = new LinkedList<Integer>();
		/* Todas as matérias abaixo seguem uma numeração sequencial e única por dia */
		codigoMateria.add( EstruturaPublicacao.COD_MATERIA_ORIGINARIOS );
		codigoMateria.add( EstruturaPublicacao.COD_MATERIA_RECURSOS );
		codigoMateria.add( EstruturaPublicacao.COD_MATERIA_REPUBLICACOES );
		codigoMateria.add( EstruturaPublicacao.COD_MATERIA_PROCESSOS_DE_COMPETENCIA_DA_PRESIDENCIA );
		codigoMateria.add( EstruturaPublicacao.COD_MATERIA_DECISOES_E_DESPACHOS_DO_PRESIDENTE );
		Integer numeroUltimaAta = 0;
		try {
			/* Recupera o número da última Ata composta */
			numeroUltimaAta = recuperarNumeroUltimaAta( estruturaPublicacao.getId().getCodigoCapitulo(), 
							              		        codigoMateria, 
							              		        estruturaPublicacao.getId().getCodigoConteudo(),
							              		        anoAta,
							              		        capituloJaComposto );
		} catch (ServiceException e) {
			throw new ServiceException(e);
		}
		/* Acrescenta 1 ao número da última Ata, 
		 * definindo o número da nova Ata.*/
		return numeroUltimaAta + 1;
	}
	
	/* Retorna uma data para a Ata, quando o dia em questão é válido */
	@SuppressWarnings("static-access")
	private GregorianCalendar definirDataCriacaoAta() throws ServiceException {
		GregorianCalendar dataAta = new GregorianCalendar( TimeZone.getTimeZone("GMT-3"),new Locale("pt_BR") );
		try {
			while ( !isDataValidaParaAta( dataAta ) ) {
				dataAta.set( dataAta.DATE, dataAta.get( dataAta.DATE ) + 1 );
			}
		} catch (ServiceException e) {
			throw new ServiceException( e );
		}
		return dataAta;	
	}
	
	/* Verifica se o Data da Ata é Feriado, Final de Semana ou Recesso Forense. 
	 * A ordem foi determinado de acordo com a periodicidade do evento. */
	private boolean isDataValidaParaAta(GregorianCalendar dataAta) throws ServiceException {
		try {
			if( !isDataEmHorarioLimiteCriacaoAta( dataAta ) ){
				return false;
			} else {
				if ( isFinalDeSemana( dataAta ) ){
					return false;	
				} else {
					if ( isFeriado( dataAta ) ) {
						return false;
					} else {
						if ( !isDiaValidoDeRecessoForense( dataAta ) ) {
							return false;
						}
					}
				}
			}
		} catch (ServiceException e) {
			throw new ServiceException( e ); 
		}
		return true;
	}
	
	/* Verifica se o horário limite da Ata foi atingido */
	@SuppressWarnings("static-access")
	public boolean isHorarioLimiteAta(Date dataCriacaoAta){
		GregorianCalendar horarioAtual = new GregorianCalendar(TimeZone.getTimeZone("GMT-3"),new Locale("pt_BR"));
		GregorianCalendar dhCriacaoAta = new GregorianCalendar(TimeZone.getTimeZone("GMT-3"),new Locale("pt_BR"));
		dhCriacaoAta.setTime(dataCriacaoAta);
		/* Verifica se a data de criação é a mesma da Data atual */
		if ( dhCriacaoAta.get(dhCriacaoAta.DATE)  ==  horarioAtual.get(horarioAtual.DATE)  &&
			 dhCriacaoAta.get(dhCriacaoAta.MONTH) ==  horarioAtual.get(horarioAtual.MONTH) &&
			 dhCriacaoAta.get(dhCriacaoAta.YEAR)  ==  horarioAtual.get(horarioAtual.YEAR)     ) {
			/* Valida se a Ata está em horário de limite para o dia */
			if( horarioAtual.getTime().before( horaLimiteAta() ) ){
	    		return true;
	    	} else {
	    		return false;
	    	}
		} else {
			/* Verifica se a Data de Criação da Ata é anterior a Data atual */
			if( dhCriacaoAta.before( horarioAtual ) ){
				return false;
			} else {
				return true;	
			}
		}
	}	

	/* Verifica se a data da Ata do dia está em um horário válido, até às 19h do dia,
	 * após esse horário, uma nova Ata deve ser criado com a data do próximo dia útil */
	@SuppressWarnings("static-access")
	private boolean isDataEmHorarioLimiteCriacaoAta(GregorianCalendar horarioAtual){
        GregorianCalendar dataHoraAtual = new GregorianCalendar(TimeZone.getTimeZone("GMT-3"),new Locale("pt_BR"));
        if( dataHoraAtual.get(dataHoraAtual.DATE)  == horarioAtual.get(dataHoraAtual.DATE)  &&
        	dataHoraAtual.get(dataHoraAtual.MONTH) == horarioAtual.get(dataHoraAtual.MONTH) &&
        	dataHoraAtual.get(dataHoraAtual.YEAR)  == horarioAtual.get(dataHoraAtual.YEAR)  ){
            if( horarioAtual.getTime().before( horaLimiteAta() ) ){
    			return true;
    		} else {
    			return false;
    		}
        } 
        return true;
	}	
	
	/* Determina o horário limite da Ata, atualmente em 19h */
	private Date horaLimiteAta() {
		GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT-3"),new Locale("pt_BR"));
		cal.set(Calendar.HOUR_OF_DAY, 19);
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 00);
		return cal.getTime();
	}

	/**
	 * Verifica se a data da Ata não é um dia de final de semana.
	 * @param dataAta
	 * @return
	 * @throws Exception
	 */
	private boolean isFinalDeSemana(GregorianCalendar dataAta) {   
		if ( ( dataAta.get( Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ) || 
			 ( dataAta.get( Calendar.DAY_OF_WEEK) == Calendar.SUNDAY   ) ) {     
			return true;   
		}   else  {     
			return false;   
		}    
	}

	/**
	 * Verifica se a Data da Ata é um feriado.
	 * @param dataAta
	 * @return
	 */
	@SuppressWarnings("static-access")
	private boolean isFeriado(GregorianCalendar dataAta) throws ServiceException {
		List<Feriado> feriados = null;
		String mesAnoFeriado =  String.valueOf(dataAta.get(dataAta.MONTH) + 1) + String.valueOf(dataAta.get(dataAta.YEAR));
		if( mesAnoFeriado.length() == 5 ) {
			mesAnoFeriado = "0"+mesAnoFeriado;
		}
		try {
			feriados = feriadoService.recuperar(mesAnoFeriado);
		} catch (ServiceException e) {
			 throw new ServiceException( e ); 
		}
		for( Feriado feriado: feriados ){
			if ( feriado.getDia().equalsIgnoreCase( String.valueOf( dataAta.get( dataAta.DATE ) ) ) &&
				 !( feriado.getDescricao().equalsIgnoreCase( Feriado.RECESSO_FORENSE ) ||
				    feriado.getDescricao().equalsIgnoreCase( Feriado.RECESSO_REGIMENTAL ) ) ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Delimitar as datas de início e fim(de recesso forense/regimental) 
	 * para publicação de Ata. O primeiro dia útil anterior a um desses
	 * recessos não é um dia válido para criação de Ata, no entanto, o
	 * último dia do recesso pode ser utilizado para criação da Ata.
	 * @param dataInicioFim
	 * @return Datas de Início e Fim
	 */
	@SuppressWarnings("static-access")
	private GregorianCalendar[] delimitarDatasRecessoAta( String dataInicioFim ) throws ServiceException {
		String[] datas    = dataInicioFim.split("-");
		String dataInicio = datas[0];
		String dataFim    = datas[1];
		GregorianCalendar[] retorno = new GregorianCalendar[2];
		try {
			/* A data de Iníco é formatada, cujo valor é dd/MM/yyyy 00:00:00 */
			GregorianCalendar calInicio = new GregorianCalendar(TimeZone.getTimeZone("GMT-3"),new Locale("pt_BR"));
			String dtInicio = dataInicio + "/" + calInicio.get( GregorianCalendar.YEAR );
			calInicio.set( GregorianCalendar.DATE,    Integer.parseInt( dtInicio.substring( 0, 2 ) ) );
			calInicio.set( GregorianCalendar.MONTH, ( Integer.parseInt( dtInicio.substring( 3, 5 ) ) -1 ) );
			calInicio.set( GregorianCalendar.YEAR,    Integer.parseInt( dtInicio.substring( 6, 10 ) ) );
			/* A data de Fim é formatada, cujo valor é dd/MM/yyyy 23:59:59 */
			GregorianCalendar calFim = new GregorianCalendar(TimeZone.getTimeZone("GMT-3"),new Locale("pt_BR"));
			String dtFim = dataFim  + "/" +  calFim.get( GregorianCalendar.YEAR );
			calFim.set( GregorianCalendar.DATE,    Integer.parseInt( dtFim.substring( 0, 2 ) ) );
			calFim.set( GregorianCalendar.MONTH, ( Integer.parseInt( dtFim.substring( 3, 5 ) ) -1 ) );
			calFim.set( GregorianCalendar.YEAR,    Integer.parseInt( dtFim.substring( 6, 10 ) ) );
			calFim.set( GregorianCalendar.HOUR_OF_DAY,    23 );
			calFim.set( GregorianCalendar.MINUTE,  59 );
			calFim.set( GregorianCalendar.SECOND,  59 );
			/* Retrocede um dia a fim de invalidar o primeiro dia útil prévio ao recesso */
			do {
				calInicio.set( GregorianCalendar.DATE, ( calInicio.get( GregorianCalendar.DATE ) - 1 ) );	
			} while ( isFeriado( calInicio ) || isFinalDeSemana( calInicio ) );
			retorno[0] = calInicio;

			if ( Integer.valueOf( calFim.get( GregorianCalendar.MONTH ) ).equals( GregorianCalendar.JANUARY ) ) {
				calFim.set( GregorianCalendar.YEAR, ( calFim.get( GregorianCalendar.YEAR ) + 1 ) );
			}
			/* Retrocede um dia a fim de validar o último dia útil do recesso */
			do {
				calFim.set( calFim.DATE, calFim.get( GregorianCalendar.DATE ) - 1 );
			} while ( isFeriado( calFim ) || isFinalDeSemana( calFim ) );
			retorno[1] = calFim;
		} catch (ServiceException e) {
			throw new ServiceException( e );
		}
		return retorno;
	}
	
	/**
	 * Valida uma Data de Ata, dentro dos limites dos recessos.
	 * O primeiro dia útil anterior ao início do recesso é valido 
	 * para criação da Ata, assim como o último dia de Recesso.
	 * @param dataAta
	 * @return true, caso o dia seja válido, 
	 *         false, inválido.
	 * @throws ServiceException
	 */
	private boolean isDiaValidoDeRecessoForense( GregorianCalendar dataAta ) throws ServiceException {
		/* Só efetua esse método, caso o mês seja um dos especificados */
		if ( dataAta.get( GregorianCalendar.MONTH ) != GregorianCalendar.JUNE &&
			 dataAta.get( GregorianCalendar.MONTH ) != GregorianCalendar.JULY &&
			 dataAta.get( GregorianCalendar.MONTH ) != GregorianCalendar.DECEMBER &&
			 dataAta.get( GregorianCalendar.MONTH ) != GregorianCalendar.JANUARY ) {
			return true;
		}
		try {
			String recesso = "";
			List<TipoCampoTarefaValor> lista = tipoTarefaSetorService.pesquisarTipoCampoTarefaValor(null, TipoCampoTarefaContante.RECESSO.getCodigo() );
			for (TipoCampoTarefaValor campo : lista) {
				recesso = campo.getDescricao().trim();
				GregorianCalendar[] limiteReceso = delimitarDatasRecessoAta( recesso );
				/* A posição 0 indica a Data de Início do Recesso, 1, Data de Fim do Recesso */
				if ( dataAta.after( limiteReceso[ 0 ] ) && dataAta.before( limiteReceso[ 1 ] ) ) {
					return false;
				}
			}
		} catch (Exception e) {
			throw new ServiceException( e );
		}
		return true;
	}
	
	/**
	 * Determina o ano da Ata, baseado na data da Ata.
	 * @return Ano da Ata no formato yyyy.
	 */
	private Short definirAnoAta(GregorianCalendar dataAta){
		return new Short(String.valueOf(dataAta.get(Calendar.YEAR)));
	}
	
	@Override
	public ConteudoPublicacao getAtaAberta(Setor setor, Short anoMateria) throws ServiceException {
		Integer codigoCapitulo = setor.getCodigoCapitulo();
		Integer codigoConteudo = 50;
		Integer codigoMateria = 12;
		TipoSessao tipoSessao = TipoSessao.ORDINARIA;
		List<Integer> listCodMaterias = Arrays.asList(codigoMateria);

		List<ConteudoPublicacao> listMaterias = pesquisarMateria(codigoCapitulo, null, listCodMaterias, codigoConteudo, null, anoMateria, null, false, false, tipoSessao);
		
		if (listMaterias != null && listMaterias.size() == 1)
			return listMaterias.get(0);

		return null;
	}


	@Override
	public boolean isPautaFechada(Colegiado colegiado, Calendar dataLiberacaoLista) {
		try {
			if (colegiado.getId() != null) {
				Integer capitulo = TipoColegiadoConstante.valueOfSigla(colegiado.getId()).getCodigoCapitulo();
				ConsultaConteudoPublicacao consulta = new ConsultaConteudoPublicacao();
				consulta.setCodigoCapitulo(capitulo);
				consulta.setCodigoMateria(Agendamento.COD_MATERIA_AGENDAMENTO_PAUTA);
				return isPautaDeJulgamentoFechada(consulta, dataLiberacaoLista);
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public ConteudoPublicacao criarMateriaAta(Integer codCapitulo, Integer codMateria, Integer codConteudo,
			Short anoMateria) throws ServiceException {

		ConteudoPublicacao materia = new ConteudoPublicacao();

		materia.setCodigoCapitulo(codCapitulo);
		materia.setCodigoMateria(codMateria);
		materia.setCodigoConteudo(codConteudo);
		materia.setAno(anoMateria);
		materia.setDataCriacao(DateTimeHelper.getDataAtual());
		materia.setDataComposicaoParcial(DateTimeHelper.getDataAtual());

		Integer numeroMateria = recuperarNumeroUltimaAta(materia.getCodigoCapitulo(),
				Arrays.asList(materia.getCodigoMateria()), materia.getCodigoConteudo(), materia.getAno(), null);

		Integer proximoNumeroMateria = numeroMateria + 1;

		materia.setNumero(proximoNumeroMateria);
		materia = incluir(materia);

		return materia;
	}

	public void incluirProcessoEmAta(ObjetoIncidente<?> oi, ConteudoPublicacao materia, Boolean republicacao) throws ServiceException {
		ProcessoPublicado pp = new ProcessoPublicado();
		pp.setAnoMateria(materia.getAno());
		pp.setCodigoCapitulo(materia.getCodigoCapitulo());
		pp.setCodigoMateria(materia.getCodigoMateria());
		pp.setNumeroMateria(materia.getNumero());
		pp.setObjetoIncidente(oi);
		
		try {
			if (republicacao) {
				ArquivoEletronico arquivoEletronicoObs = new ArquivoEletronico();
				arquivoEletronicoObs.setConteudo(BuilderHelper.stringToRtf("Processo republicado por incorreções no DJ."));
				arquivoEletronicoService.salvar(arquivoEletronicoObs);
				
				pp.setArquivoEletronicoObs(arquivoEletronicoObs);
			}
		} catch (Exception e) {
			new Throwable("Nao foi possivel localizar o arquivo de observacao para a republicacao do processo de id:" + oi.getId(), e);
		}
		
		processoPublicadoService.incluir(pp);

	}

	public ConteudoPublicacao pesquisarMateriaNaoCompostaNaoPublicadaPorSessao(Sessao sessao) throws ServiceException{
		
		ConteudoPublicacao materia = null;
		try {
			materia = dao.pesquisarMateriaNaoCompostaNaoPublicadaPorSessao(sessao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return materia;
		
	}
	

	public List<ConteudoPublicacao> pesquisarMateriaDataCriacao(Date dataCriacao) throws ServiceException{
		List<ConteudoPublicacao> materias = null;
		try {
			materias = dao.pesquisarMateriaDataCriacao(dataCriacao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return materias;
	}

	public List<ConteudoPublicacao> pesquisarMateriaChaveAntiga(Integer numero, Short anoMateria, Integer codigoCapitulo, Integer... codigoMateria)	throws ServiceException{
		
		List<ConteudoPublicacao> materias = null;
		try {
			materias = dao.pesquisarMateriaChaveAntiga(numero, anoMateria, codigoCapitulo, codigoMateria);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return materias;
		
	}
	
	public ConteudoPublicacao pesquisarUltimaMateriaOInoAcordao(ObjetoIncidente<?> oi) throws ServiceException{

		ConteudoPublicacao materias = null;
		try {
			materias = dao.pesquisarUltimaMateriaOInoAcordao(oi);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return materias;
		
	}
	
}
