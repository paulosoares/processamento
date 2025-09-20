package br.gov.stf.estf.publicacao.model.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.service.ArquivoEletronicoService;
import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.documento.model.service.HistoricoPublicacaoDocumentoService;
import br.gov.stf.estf.documento.model.service.TextoPeticaoService;
import br.gov.stf.estf.documento.model.service.impl.AssinaturaDigitalServiceImpl;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.HistoricoPublicacaoDocumento;
import br.gov.stf.estf.entidade.documento.TextoPeticao;
import br.gov.stf.estf.entidade.documento.TipoArquivo;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AcordaoAgendado;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.publicacao.Publicacao;
import br.gov.stf.estf.processostf.model.service.AcordaoAgendadoService;
import br.gov.stf.estf.publicacao.BuilderDj;
import br.gov.stf.estf.publicacao.compordj.builder.ConteudoBuilder;
import br.gov.stf.estf.publicacao.compordj.builderimpl.RelacaoPeticaoImpl;
import br.gov.stf.estf.publicacao.compordj.builderimpl.RelacaoProcessoPublicadoImpl;
import br.gov.stf.estf.publicacao.compordj.builderimpl.RelacaoProtocoloImpl;
import br.gov.stf.estf.publicacao.compordj.builderimpl.RelacaoSessaoEspecialADIADCADO;
import br.gov.stf.estf.publicacao.compordj.builderimpl.RelacaoSessaoEspecialADPF;
import br.gov.stf.estf.publicacao.model.dataaccess.PublicacaoDao;
import br.gov.stf.estf.publicacao.model.service.ConteudoPublicacaoService;
import br.gov.stf.estf.publicacao.model.service.PublicacaoService;
import br.gov.stf.estf.publicacao.model.util.AdvogadoVO;
import br.gov.stf.estf.publicacao.model.util.PecaVO;
import br.gov.stf.estf.publicacao.model.util.ProcessoProtocoloPublicacaoSearchData;
import br.gov.stf.estf.publicacao.model.util.ProcessoPublicadoVO;
import br.gov.stf.estf.publicacao.model.util.ProtocoloVO;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;
import br.gov.stf.framework.util.SearchResult;

@Service("publicacaoService")
public class PublicacaoServiceImpl extends GenericServiceImpl<Publicacao, Long, PublicacaoDao>
	implements PublicacaoService {
    

	
	private final ArquivoEletronicoService arquivoEletronicoService;
	private final DocumentoEletronicoService documentoEletronicoService;
	private final ConteudoPublicacaoService conteudoPublicacaoService;	
	private final HistoricoPublicacaoDocumentoService historicoPublicacaoDocumentoService;
	private final TextoPeticaoService textoPeticaoService;
	private final AcordaoAgendadoService acordaoAgendadoService;

	public PublicacaoServiceImpl(
			PublicacaoDao dao,
			ArquivoEletronicoService arquivoEletronicoService,
			DocumentoEletronicoService documentoEletronicoService,
			ConteudoPublicacaoService conteudoPublicacaoService,
			HistoricoPublicacaoDocumentoService historicoPublicacaoDocumentoService,
			TextoPeticaoService textoPeticaoService,
			AcordaoAgendadoService acordaoAgendadoService) {
		super(dao);
		this.arquivoEletronicoService = arquivoEletronicoService;
		this.documentoEletronicoService = documentoEletronicoService;		
		this.conteudoPublicacaoService = conteudoPublicacaoService;
		this.historicoPublicacaoDocumentoService = historicoPublicacaoDocumentoService;
		this.textoPeticaoService = textoPeticaoService;
		this.acordaoAgendadoService = acordaoAgendadoService;		
	}

	public Date recuperarDataPublicacaoProcesso(String siglaClasse,
			Integer numero, Long codigoRecurso, String tipoJulgamento)
			throws ServiceException {
		Date data = null;
		try {
			List<Publicacao> publicacoes = dao.pesquisarProcessoPublicado(siglaClasse, numero, codigoRecurso, tipoJulgamento);
			if ( publicacoes!=null && publicacoes.size()>0 ) {
				data = publicacoes.get(0).getDataPublicacaoDj();
			}
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return data;
	}

	public Short recuperarNumeroUltimoDj() throws ServiceException {
		Short numero = null;
		try {
			numero = dao.recuperarNumeroUltimoDj();
			if ( numero==null ) {
				numero = 0;
			}
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return numero;
	}

	public List<Publicacao> pesquisarDjNaoPublicado() throws ServiceException {
		List<Publicacao> publicacoes = null;
		try {
			publicacoes = dao.pesquisarDjNaoPublicado();
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return publicacoes;
	}
	
	public Publicacao alterarDj(Publicacao publicacao, byte[] odt) throws ServiceException {
		ArquivoEletronico arquivoEletronico = new ArquivoEletronico();
		arquivoEletronico.setConteudo( odt );
		arquivoEletronico.setFormato("ODT");
	
		arquivoEletronicoService.incluir(arquivoEletronico);
		
		HistoricoPublicacaoDocumento historicoPublicacaoDocumento = new HistoricoPublicacaoDocumento();
		historicoPublicacaoDocumento.setArquivoEletronico(arquivoEletronico);
		historicoPublicacaoDocumento.setPublicacao(publicacao);
		historicoPublicacaoDocumento.setDataInclusao(new Date());

		historicoPublicacaoDocumentoService.incluir(historicoPublicacaoDocumento);
		
		return publicacao;
	}
	
	public Publicacao inserirPdfDj(Publicacao publicacao, byte[] pdf) throws ServiceException {
				
		DocumentoEletronico documentoEletronico = publicacao.getDocumentoEletronico();
		
		if ( documentoEletronico==null ) {
			documentoEletronico = new DocumentoEletronico();
			documentoEletronico.setArquivo(pdf);
			documentoEletronico.setDescricaoStatusDocumento("RAS");
			documentoEletronico.setSiglaSistema("ESTFPUBLICACAO");
			documentoEletronico.setTipoArquivo( TipoArquivo.PDF );
			documentoEletronico.setTipoAcesso( DocumentoEletronico.TIPO_ACESSO_INTERNO );
			documentoEletronico.setHashValidacao(AssinaturaDigitalServiceImpl.gerarHashValidacao());
			documentoEletronicoService.incluir(documentoEletronico);
			
			publicacao.setDocumentoEletronico(documentoEletronico);
			
			try {
				dao.alterar(publicacao);
			} catch (DaoException e) {
				throw new ServiceException(e);
			}
			
		} else {
			documentoEletronico.setArquivo(pdf);
			documentoEletronicoService.alterar(documentoEletronico);
		} 
		
		return publicacao;
	}

	public Publicacao inserirNovoDj(Short numero, Short ano, Date divulgacao,
			Date previsao, byte[] odt, List<ConteudoPublicacao> materias) throws ServiceException {
		
		ArquivoEletronico arquivoEletronico = new ArquivoEletronico();
		arquivoEletronico.setConteudo( odt );
		arquivoEletronico.setFormato("ODT");
	
		arquivoEletronicoService.incluir(arquivoEletronico);
		
		Publicacao publicacao = new Publicacao();
		publicacao.setAnoEdicaoDje( ano );
		publicacao.setDataComposicaoDj( new Date() );
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(divulgacao);
		cal.set(Calendar.HOUR_OF_DAY,8);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		divulgacao = cal.getTime();		
		publicacao.setDataDivulgacaoDje( divulgacao );
		
		publicacao.setDataPrevistaDj( previsao );
		publicacao.setNumeroEdicaoDje( numero );
		
		try {
			publicacao = dao.incluir( publicacao );
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		
		HistoricoPublicacaoDocumento historicoPublicacaoDocumento = new HistoricoPublicacaoDocumento();
		historicoPublicacaoDocumento.setArquivoEletronico(arquivoEletronico);
		historicoPublicacaoDocumento.setPublicacao(publicacao);
		historicoPublicacaoDocumento.setDataInclusao(new Date());

		historicoPublicacaoDocumentoService.incluir(historicoPublicacaoDocumento);
		
		for ( ConteudoPublicacao conteudoPublicacao: materias ) {
			conteudoPublicacao.setDataComposicaoDj( publicacao.getDataComposicaoDj() );
			conteudoPublicacao.setPublicacao( publicacao );
			conteudoPublicacaoService.alterar( conteudoPublicacao );
			
			ConteudoBuilder<?> builderDj = BuilderDj.getConteudoBuilder(conteudoPublicacao);
			Date composicao = new Date();
			if ( builderDj instanceof RelacaoPeticaoImpl ) {
				List<TextoPeticao> peticoes = builderDj.getRelacaoTextoPeticao(false, conteudoPublicacao);
				alterarTextoPeticao(peticoes, composicao);
			} else if ( builderDj instanceof RelacaoProtocoloImpl ) {
				List<TextoPeticao> protocolos = builderDj.getRelacaoTextoProtocolo(false, conteudoPublicacao);
				alterarTextoPeticao(protocolos, composicao);
			} else if ( builderDj instanceof RelacaoSessaoEspecialADIADCADO ) {
				List<AcordaoAgendado> acordaos = builderDj.getRelacaoAcordaosSessaoEspecialADIADCADO(false);
				alterarAcordaoAgendado(acordaos, composicao, true);
			} else if ( builderDj instanceof RelacaoSessaoEspecialADPF ) {
				List<AcordaoAgendado> acordaos = builderDj.getRelacaoAcordaosSessaoEspecialADPF(false);
				alterarAcordaoAgendado(acordaos, composicao, true);
			} else if ( builderDj instanceof RelacaoProcessoPublicadoImpl ) {
				alterarTextoFechamento(conteudoPublicacao, publicacao.getDataComposicaoDj(), publicacao);
			}
		}
		return publicacao;
		
	}
	
	private void alterarTextoFechamento(ConteudoPublicacao conteudoPublicacao, Date dataComposicaoDj, Publicacao publicacao) throws ServiceException {
		List<ConteudoPublicacao> materias = conteudoPublicacaoService.pesquisar(conteudoPublicacao.getCodigoCapitulo(), conteudoPublicacao.getCodigoMateria(), null, conteudoPublicacao.getNumero(), conteudoPublicacao.getAno(), conteudoPublicacao.getDataCriacao());
		for ( ConteudoPublicacao cp: materias ) {
			if ( cp.getCodigoConteudo().equals(91) || cp.getCodigoConteudo().equals(92) ) {
				cp.setDataComposicaoDj( dataComposicaoDj );
				cp.setPublicacao( publicacao );
			}
		}
		conteudoPublicacaoService.alterarTodos(materias);
	}

	private void alterarAcordaoAgendado(List<AcordaoAgendado> acordaos, Date composicao, boolean publico) throws ServiceException {
		for ( AcordaoAgendado aa: acordaos ) {
			aa.setPublico(publico);
			aa.setComposicaoDj(composicao);
		}
		acordaoAgendadoService.alterarTodos(acordaos);
		
	}

	private void alterarTextoPeticao(List<TextoPeticao> peticoes, Date composicao) throws ServiceException {
		for ( TextoPeticao tp: peticoes ) {
			tp.setDataComposicaoDj(composicao);
		}
		textoPeticaoService.alterarTodos(peticoes);
		
	}

	public void alterarDescomposicaoDj(Publicacao publicacao) throws ServiceException {
		verificaDjAssinado(publicacao);
		List<ConteudoPublicacao> materias = conteudoPublicacaoService.pesquisarMateriasDJ( publicacao.getId() );
		if ( materias!=null && materias.size()>0 ) {
			for ( ConteudoPublicacao conteudoPublicacao: materias ) {
				conteudoPublicacao.setDataComposicaoDj(null);
				conteudoPublicacao.setPublicacao(null);
				conteudoPublicacaoService.alterar(conteudoPublicacao);
				
				ConteudoBuilder<?> builderDj = BuilderDj.getConteudoBuilder(conteudoPublicacao);
				if ( builderDj instanceof RelacaoPeticaoImpl ) {
					List<TextoPeticao> peticoes = builderDj.getRelacaoTextoPeticao(false, conteudoPublicacao);
					alterarTextoPeticao(peticoes, null);
				} else if ( builderDj instanceof RelacaoProtocoloImpl ) {
					List<TextoPeticao> protocolos = builderDj.getRelacaoTextoProtocolo(false, conteudoPublicacao);
					alterarTextoPeticao(protocolos, null);
				} else if ( builderDj instanceof RelacaoSessaoEspecialADIADCADO || builderDj instanceof RelacaoSessaoEspecialADPF ) {
					List<AcordaoAgendado> acordaos = acordaoAgendadoService.pesquisarComposto( publicacao.getDataComposicaoDj() );
					alterarAcordaoAgendado(acordaos, null, false);
				} else if ( builderDj instanceof RelacaoProcessoPublicadoImpl ) {
					alterarTextoFechamento(conteudoPublicacao, null, null);
				}
			}
		}
		
		List<HistoricoPublicacaoDocumento> historicos = historicoPublicacaoDocumentoService.pesquisar(publicacao);
		
		if ( historicos!=null && historicos.size()>0 ) {		
			historicoPublicacaoDocumentoService.excluirTodos(historicos);
		}
		
		if ( publicacao.getDocumentoEletronicoView()!=null ) {
			documentoEletronicoService.cancelarDocumento(publicacao.getDocumentoEletronico(), "DJ cancelado");
		}
		
		try {
			dao.excluir(publicacao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		
	}
	
	private void verificaDjAssinado(Publicacao publicacao) throws ServiceException {
		try {
			dao.refresh(publicacao);
			if (publicacao.getDocumentoEletronicoView() != null && publicacao.getDocumentoEletronicoView().getDescricaoStatusDocumento().equals(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_ASSINADO)) {
				throw new ServiceException("DJ não pode ser descomposto, pois já foi assinado.");
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	private int getCodigoAndamento(ResourceBundle prop, Integer codigoCapitulo, Integer codigoMateria) {
		try {
			return new Integer(prop.getString(codigoCapitulo+"."+codigoMateria));
		} catch ( MissingResourceException e ) {
			return -1;
		}
	}
	
	public void alterarConfirmacaoDj(Publicacao publicacao, String siglaUsuario, Setor setor, String observacao) throws ServiceException {
		try {
			try {
				ResourceBundle prop = ResourceBundle.getBundle("andamento");
				
				if ( observacao==null ) {
					observacao = "";
				}
				
				dao.atualizarDataPublicacao(publicacao.getId(), publicacao.getNumeroEdicaoDje());
				java.sql.Date dataHoje = new java.sql.Date(new java.util.Date().getTime());
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				long codigoSetor = setor.getId();
				
				List<ProcessoPublicadoVO> processos = dao.pesquisarProcessosConfirmacao(publicacao.getId());
				String observacaoPadrao = " DJE nº "+publicacao.getNumeroEdicaoDje()+", divulgado em "+dateFormat.format(publicacao.getDataDivulgacaoDje());
				for ( ProcessoPublicadoVO p: processos ) {
					int codigoAndamento = getCodigoAndamento(prop, p.getCodigoCapitulo(), p.getCodigoMateria());
					int objetoIncidente = dao.recuperarSeqObjetoIncidenteConfirmacao(p.getObjetoIncidente() , p.getTipoObjetoIncidente());
					if ( codigoAndamento>0 ) {
					
						
						int numeroSequencia = dao.recuperarProximoNumeroSequenciaProcesso(objetoIncidente);
						int seqArquivoEletronico = p.getSeqArquivoEletronico();
						if ( p.getCodigoMateria()==1 && ( p.getCodigoCapitulo()==2 || p.getCodigoCapitulo()==3 || p.getCodigoCapitulo()==4 )  ) {
							String descricaoObservacao = "PAUTA Nº "+p.getNumeroMateria()+"/"+p.getAnoMateria()+". "+observacao+observacaoPadrao;			
							dao.inserirAndamentoProcesso( codigoAndamento, siglaUsuario, dataHoje, dataHoje, descricaoObservacao, numeroSequencia, objetoIncidente, codigoSetor);
							
						} else if ( ( p.getCodigoMateria()==3 || p.getCodigoMateria()==4 || p.getCodigoMateria()==5 ) && ( p.getCodigoCapitulo()==2 || p.getCodigoCapitulo()==3 || p.getCodigoCapitulo()==4 ) ) {
							String descricaoObservacao = "ATA Nº "+p.getNumeroMateria()+", de "+dateFormat.format(p.getDataCriacaoMateria())+". "+observacao+observacaoPadrao;					
							dao.inserirAndamentoProcesso( codigoAndamento, siglaUsuario, dataHoje, dataHoje, descricaoObservacao, numeroSequencia, objetoIncidente, codigoSetor);
							
						} else if ( p.getCodigoCapitulo()==2 && p.getCodigoMateria()==7 ) {
							String descricaoObservacao = "DATA DE PUBLICAÇÃO DJE "+dateFormat.format(dataHoje)+" ATA Nº "+p.getNumeroMateria()+"/"+p.getAnoMateria()+" - "+observacao+observacaoPadrao;					
							dao.inserirAndamentoProcesso(codigoAndamento, siglaUsuario, dataHoje, dataHoje, descricaoObservacao, numeroSequencia, objetoIncidente, codigoSetor);
							dao.alterarSituacaoPecaInteiroTeor(p.getObjetoIncidente(), dataHoje, p.getIdProcessoPublicado());
							
						} else if ( p.getCodigoCapitulo()==5 && ( p.getCodigoMateria()==1 || p.getCodigoMateria()==2 || p.getCodigoMateria()==3 ) ) {
							String descricaoObservacao = "DATA DE PUBLICAÇÃO DJE "+dateFormat.format(dataHoje)+" - ATA Nº "+p.getNumeroMateria()+"/"+p.getAnoMateria()+". "+observacao+observacaoPadrao;					
							Integer seqAndamentoProcesso = dao.inserirAndamentoProcesso(codigoAndamento, siglaUsuario, dataHoje, dataHoje, descricaoObservacao, numeroSequencia, objetoIncidente, codigoSetor);
//							if ( p.tipoObjetoIncidente.equals("PR") ) {
//								alterarBaixaProcesso(con, objetoIncidente);
//							}
							if ( "E".equals( p.getTipoMeioProcesso() ) ) {
								dao.alterarDeslocamentoProcessoEletronico(objetoIncidente, codigoSetor);
							}
							dao.inserirTextoAndamentoAcordao(p.getObjetoIncidente(), seqAndamentoProcesso);
							
							dao.alterarSituacaoPecaInteiroTeor( p.getObjetoIncidente(), dataHoje, p.getIdProcessoPublicado());
							
						} else if ( p.getCodigoCapitulo()==5 && ( p.getCodigoMateria()==15 || p.getCodigoMateria()==16 || p.getCodigoMateria()==17 ) ) {
							String descricaoObservacao = "DATA DE PUBLICAÇÃO DJE "+dateFormat.format(dataHoje)+". "+observacao+observacaoPadrao;					
							int seqAndamentoProcesso = dao.inserirAndamentoProcesso( codigoAndamento, siglaUsuario, dataHoje, dataHoje, descricaoObservacao, numeroSequencia, objetoIncidente, codigoSetor);
							
							if ( p.getTipoMeioProcesso().equals("E") ) {
								dao.alterarDeslocamentoProcessoEletronico( objetoIncidente, codigoSetor);
							}
							dao.inserirTextoAndamentoAcordao( p.getObjetoIncidente(), seqAndamentoProcesso);
							
							dao.alterarSituacaoPecaInteiroTeor( p.getObjetoIncidente(), dataHoje, p.getIdProcessoPublicado());
							
						} else if ( (p.getCodigoCapitulo()==6 && ( p.getCodigoMateria()==2 || p.getCodigoMateria()==3 || p.getCodigoMateria()==5 || p.getCodigoMateria()==7 || p.getCodigoMateria()==10  )) || 
									( p.getCodigoCapitulo()==10 && p.getCodigoMateria()==14 ) ) {
							String descricaoObservacao = observacaoPadrao+observacao;					
							int seqAndamentoProcesso = dao.inserirAndamentoProcesso( codigoAndamento, siglaUsuario, dataHoje, dataHoje, descricaoObservacao, numeroSequencia, objetoIncidente, codigoSetor);
							
							dao.inserirTextoAndamento( p.getObjetoIncidente(), p.getSeqArquivoEletronico(), seqAndamentoProcesso);
							
							if ( p.getCodigoCapitulo()==6 && ( p.getCodigoMateria()==2 || p.getCodigoMateria()==3 || p.getCodigoMateria()==7 || p.getCodigoMateria()==10 ) ) {
								dao.alterarSituacaoPecaEletronica( p.getObjetoIncidente(), seqArquivoEletronico);
							}
							
						} else if ( p.getCodigoCapitulo()==7 && (p.getCodigoMateria()==6 || p.getCodigoMateria()==8) ) {
							String descricaoObservacao = observacaoPadrao;
							if ( p.getCodigoMateria()==8 ) {
								descricaoObservacao = "(COM PRAZO DE DEZ (10) DIAS)" + descricaoObservacao;
							}
							dao.inserirAndamentoProcesso( codigoAndamento, siglaUsuario, dataHoje, dataHoje, descricaoObservacao, numeroSequencia, objetoIncidente, codigoSetor);
							
						}
						//Adicionado por Demétrius Jubé - 02/12/2009
						//Método que altera o status dos despachos para público.
						//Retirado em 05/10/2011 conforme issue     
						//EPUB-36 - Modificar o aplicativo para tornar as peças relativas a textos públicas 
						//no momento da assinatura digital do DJE:
						//A alteração do tipo de acesso ao documento passa a ser realizada no momento da assinatura.
//						dao.alterarTipoAcessoDocumento(p);
					}
					
				}
				
				List<ProtocoloVO> protocolos = dao.pesquisarProtocolosConfirmacao ( publicacao.getId());
				int codigoAndamento = 7909;
				for ( ProtocoloVO protocolo: protocolos ) {
					String descricaoObservacaoProtocolo = "Publicado DJE nº "+publicacao.getNumeroEdicaoDje()+", divulgado em "+dateFormat.format(publicacao.getDataDivulgacaoDje())+". "+observacao;
					int objetoIncidente = protocolo.getObjetoIncidente();
					int numeroSequencia = dao.recuperarProximoNumeroSequenciaProtocolo( objetoIncidente);
					
					dao.inserirAndamentoProtocolo( codigoAndamento, dataHoje, dataHoje, numeroSequencia, objetoIncidente, siglaUsuario, codigoSetor, descricaoObservacaoProtocolo);
				}
				
				List<ProtocoloVO> protocolosRepublicados = dao.pesquisarProtocolosRepublicadosConfirmacao ( publicacao.getId());
				String descricaoObservacaoProtocoloRepublicado = "Publicado DJE nº "+publicacao.getNumeroEdicaoDje()+", divulgado em "+dateFormat.format(publicacao.getDataDivulgacaoDje())+". "+observacao;
				codigoAndamento = 7910;
				for ( ProtocoloVO protocolo: protocolosRepublicados ) {
					int objetoIncidente = protocolo.getObjetoIncidente();
					int numeroSequencia = dao.recuperarProximoNumeroSequenciaProtocolo( objetoIncidente);
					
					
					dao.inserirAndamentoProtocolo( codigoAndamento, dataHoje, dataHoje, numeroSequencia, objetoIncidente, siglaUsuario, codigoSetor, descricaoObservacaoProtocoloRepublicado);
				}
				
				inserirAndamentoIntimacaoAdvogadosDJ(publicacao.getId(), dataHoje, siglaUsuario, codigoSetor);
				
				
				
			} catch ( Exception e ) {
				throw new DaoException(e);
			}
		} catch ( DaoException e ) {
			throw new ServiceException("Erro ao confirmar DJ "+publicacao.getNumeroEdicaoDje()+"/"+publicacao.getAnoEdicaoDje(), e);
		}
		
		
		
	}
	

	private void inserirAndamentoIntimacaoAdvogadosDJ( long seqDJ, java.sql.Date dataHoje, String siglaUsuario, long setor) throws ServiceException, DaoException {
		List<AdvogadoVO> advogados = dao.pesquisarAdvogadosIntimaveisDJ(seqDJ);
		if ( advogados!=null && advogados.size()>0 ) {
			for ( AdvogadoVO adv: advogados ) {
				int numeroSequencia = dao.recuperarProximoNumeroSequenciaProcesso( adv.getObjetoIncidente() );
				int objetoIncidente = dao.recuperarSeqObjetoIncidenteConfirmacao(adv.getObjetoIncidente() , adv.getTipoObjetoIncidente());
				int andamentoProcesso = dao.inserirAndamentoProcesso( 8403, siglaUsuario, dataHoje, dataHoje, adv.getNome(), numeroSequencia, objetoIncidente, setor);
				
				if ( adv.isLancarIntimacaoEletronica() ) {
					
					List<PecaVO> pecas = dao.pesquisarPecas(adv.getObjetoIncidente(), adv.getDataComposicao());
					if ( pecas!=null && pecas.size()>0 ) {
						for ( PecaVO peca: pecas ) {
							dao.inserirProcessoIntegracao(andamentoProcesso, adv.getOrigem(), adv.getProcesso(), peca.getSeqPeca(), adv.getSeqJurisdicionado(), dataHoje);
							if ( peca.isAtualizarPeca() ) {
								dao.alterarSituacaoPecaJuntada(peca.getSeqPeca());
							}
						}
					}
					
				}
			}
		}
		
	}

	

	public Publicacao recuperar(Short anoEdicaoDje, Short numeroEdicaoDje)
			throws ServiceException {
		Publicacao publicacao = null;
		try {
			publicacao = dao.recuperar(anoEdicaoDje, numeroEdicaoDje);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return publicacao;
	}
	
	public SearchResult pesquisarProcessoProtocoloPublicacao(
			ProcessoProtocoloPublicacaoSearchData sd)
	throws ServiceException {
		try{
			return dao.pesquisarProcessoProtocoloPublicacao(sd);
		}catch(DaoException e){
			throw new ServiceException(e);
		}
	}

	@Override
	public void alterarTipoAcessoDocumentos(Long idPublicacao)
			throws ServiceException {
		try {
			ResourceBundle prop = ResourceBundle.getBundle("andamento");
			
			List<ProcessoPublicadoVO> processos = dao.pesquisarProcessosConfirmacao(idPublicacao);
			
			for (ProcessoPublicadoVO p : processos) {
				int codigoAndamento = getCodigoAndamento(prop, p.getCodigoCapitulo(), p.getCodigoMateria());
				if (codigoAndamento > 0) {
					dao.alterarTipoAcessoDocumento(p);
				}
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		
	}	
	
	@Override
	public void salvarDjAssinado(Long idPublicacao,
			DocumentoEletronico documentoEletronico, byte[] pdfAssinado,
			byte[] assinatura, byte carimboTempo[], Date dataCarimboTempo)
			throws ServiceException {
		documentoEletronicoService.salvarDocumentoEletronicoAssinadoPublico(documentoEletronico,
					pdfAssinado, assinatura, carimboTempo, dataCarimboTempo);

		alterarTipoAcessoDocumentos(idPublicacao);
	}

}
