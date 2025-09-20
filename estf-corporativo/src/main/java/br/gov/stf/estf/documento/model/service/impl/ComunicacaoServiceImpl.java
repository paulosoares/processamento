package br.gov.stf.estf.documento.model.service.impl;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.ComunicacaoDao;
import br.gov.stf.estf.documento.model.service.ArquivoEletronicoService;
import br.gov.stf.estf.documento.model.service.ComunicacaoIncidenteService;
import br.gov.stf.estf.documento.model.service.ComunicacaoService;
import br.gov.stf.estf.documento.model.service.DocumentoComunicacaoService;
import br.gov.stf.estf.documento.model.service.ModeloComunicacaoService;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoPaginatorResult;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.documento.model.util.ComunicacaoSearch;
import br.gov.stf.estf.documento.model.util.FiltroPesquisarDocumentosAssinatura;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente.FlagProcessoLote;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.FaseComunicacao;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronicoComunicacao;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.AndamentoProcessoComunicacao;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.entidade.processostf.TipoVinculoAndamento;
import br.gov.stf.estf.entidade.usuario.Pessoa;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.intimacao.model.service.AndamentoProcessoComunicacaoLocalService;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.estf.usuario.model.service.PessoaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("comunicacaoService")
public class ComunicacaoServiceImpl extends GenericServiceImpl<Comunicacao, Long, ComunicacaoDao> implements ComunicacaoService {

	private final DocumentoComunicacaoService documentoComunicacaoService;
	private final ArquivoEletronicoService arquivoEletronicoService;
	private final MinistroService ministroService;
	private final ModeloComunicacaoService modeloComunicacaoService;
	private static final Long CODIGO_ANDAMENTO_INCLUIDO_CALENDARIO_PLENARIO = 8534L;
	
	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private ComunicacaoIncidenteService comunicacaoIncidenteService;
	
	@Autowired
	private AndamentoProcessoComunicacaoLocalService andamentoProcessoComunicacaoLocalService;
	

	public ComunicacaoServiceImpl(ComunicacaoDao dao, DocumentoComunicacaoService documentoComunicacaoService,
			ArquivoEletronicoService arquivoEletronicoService, MinistroService ministroService, ModeloComunicacaoService modeloComunicacaoService) {
		super(dao);
		this.documentoComunicacaoService = documentoComunicacaoService;
		this.arquivoEletronicoService = arquivoEletronicoService;
		this.ministroService = ministroService;
		this.modeloComunicacaoService = modeloComunicacaoService;
	}

	@Override
	public int pesquisarPainelControle(TipoFaseComunicacao faseComunicacao, Setor setor, Usuario usuario) throws ServiceException {
		Validate.notNull(faseComunicacao, "A fase não pode ser nula!");
		Validate.notNull(setor, "O setor não pode ser nulo!");

		try {
			return dao.pesquisarPainelControle(faseComunicacao.getCodigoFase(), setor.getId(), usuario != null ? usuario.getId() : null);
		} catch (DaoException e) {
			throw new ServiceException(MessageFormat.format("Erro ao pesquisar painel de controle da fase {0} no setor {1} e usuário {2}", faseComunicacao,
					setor.getNome(), usuario != null ? usuario.getId() : null), e);
		}
	}
	
	@Override
	public int pesquisarDocumentosCount(Setor setor, Boolean buscaSomenteDocumentoFaseGerados) throws ServiceException {
		
		Validate.notNull(setor, "O setor não pode ser nulo!");
		
		try {
			return dao.pesquisarDocumentosCount(setor, buscaSomenteDocumentoFaseGerados);			
		} catch (DaoException e) {
			throw new ServiceException("Erro ao pesquisar painel de controle", e);
		}
		
	}
	
	@Override
	public int pesquisarDocumentosCorrecaoCount(Setor setor, String username) throws ServiceException {
		
		Validate.notNull(setor, "O setor não pode ser nulo!");
		
		try {
			return dao.pesquisarDocumentosCorrecaoCount(setor,username);			
		} catch (DaoException e) {
			throw new ServiceException("Erro ao pesquisar painel de controle", e);
		}
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ComunicacaoDocumentoResult> pesquisarDocumentos(Long idObjetoIncidente, Long codigoModelo, Setor setor, String usuario,
			Boolean buscaSomenteDocumentoFaseGerados) throws ServiceException {

		List<ComunicacaoDocumentoResult> listaDocumentos = Collections.emptyList();
		Boolean permissaoParaEditar = true;

		try {
			List<Object[]> idsDocumentos = dao.pesquisarDocumentos(idObjetoIncidente, codigoModelo, setor, usuario, buscaSomenteDocumentoFaseGerados);
			if (CollectionUtils.isNotEmpty(idsDocumentos)) {
				listaDocumentos = recuperarDocumentoResults(permissaoParaEditar, idsDocumentos);
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return listaDocumentos;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ComunicacaoDocumentoResult> pesquisarDocumentosCorrecao(Setor setor, String usuario) throws ServiceException {

		List<ComunicacaoDocumentoResult> listaDocumentos = Collections.emptyList();
		Boolean permissaoParaEditar = true;

		try {
			List<Object[]> idsDocumentos = dao.pesquisarDocumentosCorrecao(setor, usuario);
			if (CollectionUtils.isNotEmpty(idsDocumentos)) {
				listaDocumentos = recuperarDocumentoResults(permissaoParaEditar, idsDocumentos);
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return listaDocumentos;
	}
	
	@Override
	public List<ComunicacaoDocumentoResult> pesquisarDocumentosAssinatura(Setor setor, List<Long> listaTipoSituacaoDocumento, Long faseDocumento) throws ServiceException{
		FiltroPesquisarDocumentosAssinatura filtro = new FiltroPesquisarDocumentosAssinatura();
		filtro.setSetor(setor);
		filtro.setListaTipoSituacaoDocumento(listaTipoSituacaoDocumento);
		filtro.setFaseDocumento(faseDocumento);
		ComunicacaoDocumentoPaginatorResult result = pesquisarDocumentosAssinatura(filtro);
		return result.getLista();
	}

	@Override
	@SuppressWarnings("unchecked")
	public ComunicacaoDocumentoPaginatorResult pesquisarDocumentosAssinatura(FiltroPesquisarDocumentosAssinatura filtro) throws ServiceException {
		ComunicacaoDocumentoPaginatorResult result = new ComunicacaoDocumentoPaginatorResult();
		List<ComunicacaoDocumentoResult> listaDocumentos = Collections.emptyList();

		try {
			int total = dao.pesquisarDocumentosAssinaturaCount(filtro);
			result.setTotalResultSet(total);
			if(total > 0){
				List<Object[]> idsDocumentos = dao.pesquisarDocumentosAssinatura(filtro);
				if (CollectionUtils.isNotEmpty(idsDocumentos)) {
					listaDocumentos = recuperarDocumentoResults(null, idsDocumentos);
				}
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}		
		result.setLista(listaDocumentos);
		return result;
	}
	
	@Override
	public List<ComunicacaoDocumentoResult> pesquisarDocumentosAssinatura(Setor setor, List<Long> listaTipoSituacaoDocumento, Long faseDocumento, List<Long> ids) throws ServiceException {
		FiltroPesquisarDocumentosAssinatura filtro = new FiltroPesquisarDocumentosAssinatura();
		filtro.setSetor(setor);
		filtro.setListaTipoSituacaoDocumento(listaTipoSituacaoDocumento);
		filtro.setFaseDocumento(faseDocumento);
		filtro.setIds(ids);
		ComunicacaoDocumentoPaginatorResult result = pesquisarDocumentosAssinatura(filtro);
		return result.getLista();
	}
	
	private List<ComunicacaoDocumentoResult> recuperarDocumentoResults(Boolean permissaoParaEditar, List<Object[]> idsDocumentos) throws DaoException,
			ServiceException {
		List<ComunicacaoDocumentoResult> listaDocumentos;
		listaDocumentos = new ArrayList<ComunicacaoDocumentoResult>();
		for (Object[] registro : idsDocumentos) {
			Object registroIdComunicacao = registro[0];
			Object registroIdDocumentoComunicacao = registro[1];
			
			Object registroNumOrdem = null;
			Object registroSituacaoPeca= null;
			if(registro.length == 5) {
				registroNumOrdem = registro[2];
				registroSituacaoPeca= registro[3];
			}
			Long idComunicacao = ((BigDecimal) registroIdComunicacao).longValue();
			Long idDocumentoComunicacaoAtual;
			Long idNumOrdem = 0L;
			Long idSituacaoPeca = 0L;
			//Limpa o cache do hibernate
			Comunicacao comunicacao = recuperarPorId(idComunicacao);
			
			List<PecaProcessoEletronicoComunicacao> pecasEletronicas = comunicacao.getPecasProcessoEletronico();

			if (registroIdComunicacao != null && registroIdDocumentoComunicacao != null) {
				idDocumentoComunicacaoAtual = ((BigDecimal) registroIdDocumentoComunicacao).longValue();
				
				if(registroNumOrdem !=null) {
				idNumOrdem = ((BigDecimal) registroNumOrdem).longValue();}
				else {
					idNumOrdem =0L;
				}
				if(registroSituacaoPeca !=null) {
				idSituacaoPeca = ((BigDecimal) registroSituacaoPeca).longValue();}
				else {
					idSituacaoPeca = 0L;
				}
				
				DocumentoComunicacao documentoComunicacao = documentoComunicacaoService.recuperarPorId(idDocumentoComunicacaoAtual);
				documentoComunicacaoService.recarregarDocumentoComunicacao(documentoComunicacao);
				

				if (permissaoParaEditar != null && documentoComunicacao != null) {
					if (documentoComunicacao.isTipoSituacaoDocumento(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE)
							|| documentoComunicacao.isTipoSituacaoDocumento(TipoSituacaoDocumento.GERADO)
							|| documentoComunicacao.isTipoSituacaoDocumento(TipoSituacaoDocumento.JUNTADO)) {
						permissaoParaEditar = Boolean.FALSE;
					}
				}

				if (CollectionUtils.isNotEmpty(pecasEletronicas)) {
					listaDocumentos.add(new ComunicacaoDocumentoResult(comunicacao, documentoComunicacao, BooleanUtils.isTrue(permissaoParaEditar),
							pecasEletronicas, idNumOrdem, idSituacaoPeca));
				} else {
					listaDocumentos.add(new ComunicacaoDocumentoResult(comunicacao, documentoComunicacao, BooleanUtils.isTrue(permissaoParaEditar), null,  idNumOrdem, idSituacaoPeca));
				}
			} else if (registroIdComunicacao != null && registroIdDocumentoComunicacao == null) {
				idDocumentoComunicacaoAtual = null;

				if (CollectionUtils.isNotEmpty(pecasEletronicas)) {
					listaDocumentos.add(new ComunicacaoDocumentoResult(comunicacao, null, BooleanUtils.isTrue(permissaoParaEditar), pecasEletronicas,  idNumOrdem, idSituacaoPeca));
				} else {
					listaDocumentos.add(new ComunicacaoDocumentoResult(comunicacao, null, BooleanUtils.isTrue(permissaoParaEditar), null,  idNumOrdem, idSituacaoPeca));
				}
			}
		}

		return listaDocumentos;
	}

	/**
	 * Recupera a comunicação por meio do ID
	 * 
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Comunicacao recuperarPorId(Long idComunicacao) throws ServiceException {
		try {
			Comunicacao comunicacao = dao.recuperarPorId(idComunicacao);
			
			if (comunicacao != null && comunicacao.getObjetoIncidenteUnico() != null && comunicacao.getObjetoIncidenteUnico().getRelatorIncidenteId() != null)
				comunicacao.setNomeMinistroRelatorAtual(ministroService.recuperarPorId(comunicacao.getObjetoIncidenteUnico().getRelatorIncidenteId()).getNome());
			
			return comunicacao;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Retorna as comunicações elaboradas por unidade com base na data e no setor
	 * 
	 * @return Lista de Comunicações
	 * @throws ServiceException
	 */
	@Override
	public List<ComunicacaoDocumentoResult> pesquisarDocumentosUnidade(Date dataPesquisa, Long idSetor) throws ServiceException {

		List listaComunicacoes = null;
		List<ComunicacaoDocumentoResult> listaDocumentos = new ArrayList<ComunicacaoDocumentoResult>();

		try {
			listaComunicacoes = dao.pesquisarDocumentosUnidade(dataPesquisa, idSetor);
			if (listaComunicacoes != null && listaComunicacoes.size() > 0) {

				for (Object object : listaComunicacoes) {

					Object[] registro = (Object[]) object;
					Long idComunicacao = ((BigDecimal) registro[0]).longValue();
					String setorAtualProcesso = null;
					String acaoRealizadaUnidade = null;

					if (registro[1] != null) {
						setorAtualProcesso = ((String) registro[1]).toString();
					} else {
						setorAtualProcesso = "";
					}

/*					if (registro[2] != null) {
						acaoRealizadaUnidade = ((String) registro[2]).toString();
						if (acaoRealizadaUnidade.equals("PDF gerado")) {
							acaoRealizadaUnidade = "Elaborado";
						} else if (acaoRealizadaUnidade.equals("Aguardando assinatura")) {
							acaoRealizadaUnidade = "Enviado para Assinatura";
						}
					} else {
						acaoRealizadaUnidade = "";
					}
*/					
					acaoRealizadaUnidade = ((String) registro[2]).toString();

					if (idComunicacao != null) {

						ComunicacaoDocumentoResult comunicacaoResult = new ComunicacaoDocumentoResult(null, null, null, null, null, null);
						comunicacaoResult.setComunicacao(recuperarPorId(idComunicacao));
						comunicacaoResult.setLocalizacaoAtualProcesso(setorAtualProcesso);
						comunicacaoResult.setAcaoRealizadaUnidade(acaoRealizadaUnidade);

						listaDocumentos.add(comunicacaoResult);
					}

				}
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return listaDocumentos;
	}

	/**
	 * Retorna as comunicações elaboradas com base nos processos ou na fase
	 * 
	 * @return Lista de Comunicações
	 * @throws ServiceException
	 */
	@Override
	public List<ComunicacaoDocumentoResult> pesquisarDocumentosElaborados(String siglaClasse, Long numeroProcesso, Long codigoFase, Long idSetorAtual, 
			Long numeracaoUnica, Long anoNumeracaoUnica, String dataInicial, String dataFinal) throws ServiceException {
		return pesquisarDocumentosElaborados(siglaClasse, numeroProcesso, codigoFase, idSetorAtual, null, numeracaoUnica, anoNumeracaoUnica, dataInicial, dataFinal) ;
	}

	
	@Override
	public List<ComunicacaoDocumentoResult> pesquisarDocumentosElaborados(String siglaClasse, Long numeroProcesso, Long codigoFase, Long idSetorAtual, Long idSetorFase,
			Long numeracaoUnica, Long anoNumeracaoUnica, String dataInicial, String dataFinal) throws ServiceException {

		TipoFaseComunicacao tipoFaseComunicacaoPesquisa = null;
		if (codigoFase != null)
			tipoFaseComunicacaoPesquisa =  TipoFaseComunicacao.valueOf(codigoFase);
		
		List listaDocumentos = Collections.emptyList();
		Long idDoDocumentoComunicacao = null;
		try {

			
			List resp = dao.pesquisarDocumentosElaborados(siglaClasse, numeroProcesso, codigoFase, idSetorAtual, idSetorFase, numeracaoUnica, anoNumeracaoUnica,
					dataInicial, dataFinal);

			if (resp != null && resp.size() > 0) {
				listaDocumentos = new ArrayList();
				for (Object object : resp) {
					Object[] registro = (Object[]) object;
					Long idDoDocumento = ((BigDecimal) registro[0]).longValue();
					Comunicacao com = new Comunicacao();
					com = recuperarPorId(idDoDocumento);
					if (registro[1] != null && registro[0] != null) {
						idDoDocumentoComunicacao = ((BigDecimal) registro[1]).longValue();
						if (com.getPecasProcessoEletronico() != null && com.getPecasProcessoEletronico().size() > 0) {
							ComunicacaoDocumentoResult doc = new ComunicacaoDocumentoResult(com, documentoComunicacaoService.recuperarPorId(idDoDocumentoComunicacao),Boolean.FALSE, com.getPecasProcessoEletronico(), null, null);
							if (codigoFase != null) {
								FaseComunicacao fasePeriodo = doc.getComunicacao().getFasePeriodo(dataInicial, dataFinal, codigoFase);
								doc.getComunicacao().setNomeSituacaoPesquisa( tipoFaseComunicacaoPesquisa.getDescricao() );
								if(doc.getComunicacao().getDeslocamentoData(fasePeriodo.getDataLancamento(), idSetorFase) != null) {
									doc.getComunicacao().setNomeLocalSituacao( doc.getComunicacao().getDeslocamentoData(fasePeriodo.getDataLancamento(), idSetorFase) .getSetor().getNome());
								}
							}
							listaDocumentos.add(doc);
						} else {
							ComunicacaoDocumentoResult doc = new ComunicacaoDocumentoResult(com, documentoComunicacaoService.recuperarPorId(idDoDocumentoComunicacao),Boolean.FALSE, null, null, null);
							if (codigoFase != null) {
								FaseComunicacao fasePeriodo = doc.getComunicacao().getFasePeriodo(dataInicial, dataFinal, codigoFase);
								doc.getComunicacao().setNomeSituacaoPesquisa( tipoFaseComunicacaoPesquisa.getDescricao() );
								if(doc.getComunicacao().getDeslocamentoData(fasePeriodo.getDataLancamento(), idSetorFase) != null) {
									doc.getComunicacao().setNomeLocalSituacao( doc.getComunicacao().getDeslocamentoData(fasePeriodo.getDataLancamento(), idSetorFase) .getSetor().getNome() );
								}
							}
							listaDocumentos.add(doc);
						}
					} else if (registro[0] != null && registro[1] == null) {
						idDoDocumentoComunicacao = null;
						if (com.getPecasProcessoEletronico() != null && com.getPecasProcessoEletronico().size() > 0) {
							listaDocumentos.add(new ComunicacaoDocumentoResult(com, null, Boolean.FALSE, com.getPecasProcessoEletronico(), null, null));
						} else {
							listaDocumentos.add(new ComunicacaoDocumentoResult(com, null, Boolean.FALSE, null, null, null));
						}
					}
				}
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return listaDocumentos;
	}

	
	@Override
	public List pesquisarDocumentosElaboradosSigilosos(String siglaClasse, Long numeroProcesso, Long codigoFase, Long idSetorAtual, Long idSetorFase,
			Long numeracaoUnica, Long anoNumeracaoUnica, String dataInicial, String dataFinal) throws ServiceException {

		TipoFaseComunicacao tipoFaseComunicacaoPesquisa = null;
		if (codigoFase != null)
			tipoFaseComunicacaoPesquisa =  TipoFaseComunicacao.valueOf(codigoFase);
		
		List listaDocumentos = Collections.emptyList();
		Long idDoDocumentoComunicacao = null;
		List resp ;
		try {

			
			resp = dao.pesquisarDocumentosElaboradosSigilosos(siglaClasse, numeroProcesso, codigoFase, idSetorAtual, idSetorFase, numeracaoUnica, anoNumeracaoUnica,
					dataInicial, dataFinal);

			
			
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return resp;
	}
	
	
	
	/**
	 * Método criado para contemplar a funcionalidade do AssinadorWeb para criar textos pela Secretaria Judiciaria. Este método além de criar o texto, também
	 * cria o Arquivo Eletronico
	 * 
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public ComunicacaoIncidente salvaArquivoEletronico(Long idModelo, Long codigoMinistro, ObjetoIncidente<?> objetoIncidente, byte[] odt, String tipoArquivo)
			throws ServiceException {

		ArquivoEletronico arquivo = new ArquivoEletronico();
		Ministro ministro = new Ministro();
		ComunicacaoIncidente comunicacaoIncidente = new ComunicacaoIncidente();
		Comunicacao cm = new Comunicacao();
		ModeloComunicacao modeloComunicacao = new ModeloComunicacao();

		arquivo.setFormato(tipoArquivo);
		arquivo.setConteudo(odt);

		try {
			arquivoEletronicoService.salvar(arquivo);
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		if (codigoMinistro != null) {
			try {
				ministro = ministroService.recuperarMinistro(null, codigoMinistro);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}

		try {
			modeloComunicacao = modeloComunicacaoService.recuperarPorId(idModelo);
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		cm.setModeloComunicacao(modeloComunicacao);
		cm.setArquivoEletronico(arquivo);

		// seta os valores para posteriormente salvar no banco
		comunicacaoIncidente.setObjetoIncidente(objetoIncidente);
		comunicacaoIncidente.setComunicacao(cm);

		return comunicacaoIncidente;
	}
	
	public List pesquisarDocumentosAssinadosPorPeriodo(Long codigoSetor, String usuario, String dataInicial, String dataFinal) throws ServiceException {
		List listaDocumentos = Collections.emptyList();
		try {

			List resp = dao.pesquisarDocumentosAssinadosPorPeriodo(codigoSetor,usuario, dataInicial, dataFinal);
			if (resp != null && resp.size() > 0) {
				listaDocumentos = new ArrayList();
				for (Object object : resp) {
					listaDocumentos.add(object);
				}
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return listaDocumentos;
	}
	
	@Override
	public List<Comunicacao> comunicacoesDoPeriodo(ComunicacaoSearch search) throws ServiceException{
		try {
			return dao.comunicacoesDoPeriodo(search);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List pesquisarProcessosPorDocumento(Long tipoComunicacao, Long numeroDocumento, Long anoDocumento) throws ServiceException{
		try{
			return dao.pesquisarProcessosPorDocumento(tipoComunicacao, numeroDocumento, anoDocumento);
		}catch(DaoException e){
			throw new ServiceException(e);
		}
	}

	@Override
	public Comunicacao obterUltimaComunicacao(ObjetoIncidente objetoIncidente, Pessoa pessoa, ModeloComunicacao modeloComunicacao) throws ServiceException {
		try {
			return dao.obterUltimaComunicacao(objetoIncidente, pessoa, modeloComunicacao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void atualizarSituacaoPecaProcessual(Long idComunicacao) throws ServiceException {
		try {
			dao.atualizarSituacaoPecaProcessual(idComunicacao);
			return;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void notificarOrgaosIntegrados(ObjetoIncidente processo, ModeloComunicacao modeloComunicacao, Setor setor,
			Usuario usuario, AndamentoProcesso andamentoProcesso) throws ServiceException {

		List<Parte> partes = processo.getPartesVinculadas();
		List<Pessoa> partesIntegradas = pessoaService.recuperarApenasPartesIntegradas(partes);

		Pessoa pessoaPGR = pessoaService.recuperarPorId(Pessoa.PROCURADOR_GERAL_DA_REPUBLICA);
		Pessoa pessoaMPU = pessoaService.recuperarPorId(Pessoa.MINISTERIO_PUBLICO_DA_UNIAO);

		for (Pessoa pessoa : partesIntegradas) {
			if (ModeloComunicacao.EXCLUIDO_DA_PAUTA.equals(modeloComunicacao)) {
				Comunicacao comunicacao = obterUltimaComunicacao(processo, pessoa, ModeloComunicacao.INCLUIDO_NA_PAUTA);

				if (comunicacao != null)
					excluir(comunicacao);
			}
			String observacao = criarObservacao(modeloComunicacao);
			enviarComunicacao(processo, modeloComunicacao, setor, usuario, andamentoProcesso, pessoa, observacao);
		}

		if (andamentoProcesso.getCodigoAndamento().equals(CODIGO_ANDAMENTO_INCLUIDO_CALENDARIO_PLENARIO)) {
			String observacao = criarObservacao(modeloComunicacao);

			if (!partesIntegradas.contains(pessoaPGR)) {
				enviarComunicacao(processo, modeloComunicacao, setor, usuario, andamentoProcesso, pessoaPGR,
						observacao);
			}
			if (!partesIntegradas.contains(pessoaMPU)) {
				enviarComunicacao(processo, modeloComunicacao, setor, usuario, andamentoProcesso, pessoaMPU,
						observacao);
			}
		}
	}

	private String criarObservacao(ModeloComunicacao modeloComunicacao) {
		String observacao = "";

		if (ModeloComunicacao.INCLUIDO_NO_CALENDARIO.equals(modeloComunicacao)) {
			observacao = "A Assessoria do %s informa da inclusão do %s no calendário de julgamento.";
		} else if (ModeloComunicacao.INCLUIDO_NO_CALENDARIO_PELO_PRESIDENTE.equals(modeloComunicacao))
			observacao = "A Assessoria do %s informa da inclusão do %s no calendário de julgamento pelo Presidente.";

		return observacao;
	}

	private void enviarComunicacao(ObjetoIncidente processo, ModeloComunicacao modeloComunicacao, Setor setor,
			Usuario usuario, AndamentoProcesso andamentoProcesso, Pessoa pessoa, String observacao)
			throws ServiceException {
		Comunicacao comunicacao = new Comunicacao();
		comunicacao.setDscNomeDocumento(modeloComunicacao.getDscModelo());
		comunicacao.setModeloComunicacao(modeloComunicacao);
		comunicacao.setDataEnvio(new Date());
		comunicacao.setSetor(setor);
		comunicacao.setPessoaDestinataria(pessoa);
		comunicacao.setUsuarioCriacao(usuario.getId());
		comunicacao.setNumeroComunicacao(null);
		comunicacao.setObsComunicacao(String.format(observacao, setor.getNome(), processo.getIdentificacao()));
		comunicacao = salvar(comunicacao);

		AndamentoProcessoComunicacao andamentoProcessoComunicacao = new AndamentoProcessoComunicacao();
		andamentoProcessoComunicacao.setAndamentoProcesso(andamentoProcesso);
		andamentoProcessoComunicacao.setComunicacao(comunicacao);
		andamentoProcessoComunicacao.setTipoVinculoAndamento(TipoVinculoAndamento.RELACIONADO);
		andamentoProcessoComunicacaoLocalService.salvar(andamentoProcessoComunicacao);

		ComunicacaoIncidente comunicacaoIncidente = new ComunicacaoIncidente();
		comunicacaoIncidente.setObjetoIncidente(processo);
		comunicacaoIncidente.setTipoVinculo(FlagProcessoLote.P);
		comunicacaoIncidente.setComunicacao(comunicacao);
		comunicacaoIncidente.setAndamentoProcesso(andamentoProcesso);
		comunicacaoIncidenteService.salvar(comunicacaoIncidente);
	}
}
