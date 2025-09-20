package br.gov.stf.estf.documento.model.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.ControleVotoDao;
import br.gov.stf.estf.documento.model.service.ControleVotoService;
import br.gov.stf.estf.documento.model.service.PecaProcessoEletronicoService;
import br.gov.stf.estf.documento.model.service.exception.TextoSemControleDeVotosException;
import br.gov.stf.estf.documento.model.util.ControleVotoDto;
import br.gov.stf.estf.documento.model.util.ControleVotoDynamicQuery;
import br.gov.stf.estf.documento.model.util.ControleVotoDynamicRestriction;
import br.gov.stf.estf.documento.model.util.IConsultaDadosDoTexto;
import br.gov.stf.estf.documento.model.util.IConsultaDeControleDeVotoInteiroTeor;
import br.gov.stf.estf.documento.model.util.ResultadoControleVotoPDF;
import br.gov.stf.estf.entidade.documento.ControleVoto;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoSituacaoTexto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.TipoMeioProcesso;
import br.gov.stf.estf.processostf.model.service.DeslocaProcessoService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.exception.ValidationException;
import br.gov.stf.estf.processostf.model.util.ConsultaObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;
import br.gov.stf.framework.util.DateTimeHelper;

@Service("controleVotoService")
public class ControleVotoServiceImpl extends GenericServiceImpl<ControleVoto, Long, ControleVotoDao> implements ControleVotoService {

	private ObjetoIncidenteService objetoIncidenteService;
	
	@Autowired
	private DeslocaProcessoService deslocaProcessoService;

	@Autowired
	private PecaProcessoEletronicoService pecaProcessoEletronicoService;
	
	public ControleVotoServiceImpl(ControleVotoDao dao, ObjetoIncidenteService objetoIncidenteService) {
		super(dao);
		this.objetoIncidenteService = objetoIncidenteService;
	}

	@Override
	public List<ControleVoto> pesquisarControleVoto(String siglaClasseProcessual, Long numeroProcesso, Long idMinistro, Long tipoRecurso, Long tipoJulgamento,
			Date dataSessao, TipoTexto tipoTexto, Long idTexto) throws ServiceException {
		ObjetoIncidente<?> objetoIncidente = objetoIncidenteService.recuperar(siglaClasseProcessual, numeroProcesso, tipoRecurso, tipoJulgamento);
		return pesquisarControleVoto(objetoIncidente, idMinistro, dataSessao, tipoTexto, idTexto);
	}

	@Override
	public List<ControleVoto> pesquisar(String siglaClasse, Long numeroProcesso, Long tipoRecurso, Long tipoJulgamento, Date dataSessao, Long codigoMinistro)
			throws ServiceException {
		ConsultaObjetoIncidente coi = new ConsultaObjetoIncidente(siglaClasse, numeroProcesso, tipoRecurso, tipoJulgamento);
		List<ControleVoto> votos = new ArrayList<ControleVoto>();
		try {
			
			ControleVotoDto cvDto = new ControleVotoDto.Builder()
							.setDataSessaoMenor(dataSessao)
							.setCodigoMinistro(codigoMinistro)
							.setConsultaObjetoIncidente(coi)
							.setOperadorDataSessao("igual a")
							.builder();
			
			List<Object[]> resultado = dao.pesquisar(cvDto);

			for (Object[] res : resultado) {
				votos.add((ControleVoto) res[0]);
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return votos;
	}

	@Override
	public List<ResultadoControleVotoPDF> pesquisar(ControleVotoDto controleVotoDto) throws ServiceException, ValidationException {
		validarControleVotoDto(controleVotoDto);
		List<ResultadoControleVotoPDF> resultado = new ArrayList<ResultadoControleVotoPDF>();
		//ConsultaObjetoIncidente coi = new ConsultaObjetoIncidente(controleVotoDto.getIdObjetoIncidente());
		
		ControleVotoDto cvDto = new ControleVotoDto.Builder()
						.setDataSessaoMenor(controleVotoDto.getDataSessaoMenor())
						.setDataSessaoMaior(controleVotoDto.getDataSessaoMaior())
						.setDataGenericaMenor(controleVotoDto.getDataGenericaMenor())
						.setDataGenericaMaior(controleVotoDto.getDataGenericaMaior())
						.setIdObjetoIncidente(controleVotoDto.getIdObjetoIncidente())
						.setCodigoMinistro(controleVotoDto.getCodigoMinistro())
						.setOperadorDataSessao(controleVotoDto.getOperadorDataSessao())
						.setOperadorDataGenerica(controleVotoDto.getOperadorDataGenerica())
						.setCvLiberado(controleVotoDto.getCvLiberado())
						.setCvRecebido(controleVotoDto.getCvRecebido())
						.setCvAcordaoPublicado(controleVotoDto.getCvAcordaoPublicado())
						.setCvNaoLiberado(controleVotoDto.getCvNaoLiberado())
						.setCvNaoRecebido(controleVotoDto.getCvNaoRecebido())
						.setCvAcordaoNaoPublicado(controleVotoDto.getCvAcordaoNaoPublicado())		
						.setCvSemAcaoUmMinistro(controleVotoDto.getCvSemAcaoUmMinistro())
						.setCvPorTodosOsMinistros(controleVotoDto.getCvPorTodosOsMinistros())
						.setJulgamentoFinalizado(controleVotoDto.getJulgamentoFinalizado())
						.setRepercussaoGeral(controleVotoDto.getRepercussaoGeral())
						.setTipoProcesso(controleVotoDto.getTipoProcesso())
						.setCvDocAtivos(controleVotoDto.getCvDocAtivos())						
						.setCvCompleto(controleVotoDto.getCvCompleto())
						.setCodigoColegiado(controleVotoDto.getCodigoColegiado())
						//.setConsultaObjetoIncidente(coi)
						.setSegredoDeJustica(controleVotoDto.getSegredoDeJustica())
						.setTipoTexto(controleVotoDto.getTipoTexto())
						.setInteiroTeorGerado(controleVotoDto.getInteiroTeorGerado())
						.setIsMinistroPresidente(controleVotoDto.getIsMinistroPresidente())
						.setCodigoMinistroPresidente(controleVotoDto.getCodigoMinistroPresidente())
						.setTipoAmbiente(controleVotoDto.getTipoAmbiente())
						.setIsLeadingCase(controleVotoDto.getIsLeadingCase())
						.setCvAnotacoes(controleVotoDto.getCvAnotacoes())
						.setExclusivoDigital(controleVotoDto.getExclusivoDigital())
						.setExtratoAta(controleVotoDto.getExtratoAta())
						.setTipoAmbientePersonalizado(controleVotoDto.getTipoAmbientePersonalizado())
						.builder();
		
		try {
			List<Object[]> votos = dao.pesquisar(cvDto);
			trataListaVotos(cvDto, votos);
			
			Set<ObjetoIncidente> listaObjetoIncidente = new LinkedHashSet<ObjetoIncidente>();
			
			for (Object[] res : votos) {
				ControleVoto cv = (ControleVoto) res[0];
				listaObjetoIncidente.add(cv.getObjetoIncidente());
			}
			
			List<PecaProcessoEletronico> listaInteiroTeor = pecaProcessoEletronicoService.listarInteiroTeorObjetoIncidente(new ArrayList(listaObjetoIncidente));
			
			for (Object[] res : votos) {
				ControleVoto cv = (ControleVoto) res[0];
				
				Setor setor = null;
				if(!((Processo)cv.getObjetoIncidente().getPrincipal()).getTipoMeioProcesso().equals(TipoMeioProcesso.ELETRONICO)){
					setor = deslocaProcessoService.pesquisarSetorUltimoDeslocamento(cv.getObjetoIncidente().getPrincipal().getId());				
				}
				
				Long seqInteiroTeor = null;
				for (PecaProcessoEletronico ppe : listaInteiroTeor) {
					if (ppe.getObjetoIncidente().equals(cv.getObjetoIncidente())) {
						seqInteiroTeor = ppe.getId();
						break;
					}
				}
				
				resultado.add(new ResultadoControleVotoPDF(cv, (DocumentoTexto) res[1], null, setor, seqInteiroTeor));
			}
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
		return resultado;
	}

	private void trataListaVotos(ControleVotoDto cvDto, List<Object[]> votos) {
		if(cvDto.isControleVotosCompleto()){
			List<Long> listaObjIncParaRetirarDaLista = new ArrayList<Long>();
			for(Object[] cv1 : votos){					 
				if(cv1[1] == null){						
					listaObjIncParaRetirarDaLista.add(((ControleVoto)cv1[0]).getSeqObjetoIncidente());
				}
			}
			
			Iterator<Object[]> votosIt = votos.iterator();
			while(votosIt.hasNext()){
				Object[] cvIt = votosIt.next();
				if(listaObjIncParaRetirarDaLista.contains(((ControleVoto)cvIt[0]).getSeqObjetoIncidente())){
					votosIt.remove();
				}
			}
		}
	}


	private void validarControleVotoDto(ControleVotoDto controleVotoDto) throws ValidationException {
		if(!controleVotoDto.isDtoPreenchido()){
			throw new ValidationException("Nenhum parâmetro informado para pesquisa");
		}
			
		validaDatasPesquisaPorSituacao(controleVotoDto);		
		validaInformacaoMinima(controleVotoDto);
		validaOpcaoPesquisaPorSituacao(controleVotoDto);
		validaOpcaoPeriodoPesquisaPorSessao(controleVotoDto);		
		validaOpcaoPeriodoPesquisaPorSituacao(controleVotoDto);		
		validaPeriodoPesquisaPorSessao(controleVotoDto);
	}

	private void validaDatasPesquisaPorSituacao(ControleVotoDto controleVotoDto) throws ValidationException {
		if(controleVotoDto.isPublicado() || controleVotoDto.isLiberado() || controleVotoDto.isRecebido() || controleVotoDto.isJulgamentoFinalizado()){			
			if("menos".equals(controleVotoDto.getOperadorDataGenerica()) && controleVotoDto.getDataGenericaMenor() == null){				
				throw new ValidationException("Necessário informar a data da situação");
			}
			
			if("entre".equals(controleVotoDto.getOperadorDataGenerica()) && (controleVotoDto.getDataGenericaMenor() == null || controleVotoDto.getDataGenericaMaior() == null)){
				throw new ValidationException("Necessário informar o período de pesquisa por situação");
			}	
			
			if(controleVotoDto.getDataGenericaMenor() != null && controleVotoDto.getDataGenericaMaior() != null 
					&& controleVotoDto.getDataGenericaMenor().compareTo(controleVotoDto.getDataGenericaMaior())>0){
				throw new ValidationException("A primeira data informada deve ser menor do que a segunda");
			}			
		}
	}

	private void validaInformacaoMinima(ControleVotoDto controleVotoDto) throws ValidationException {
		if(controleVotoDto.getIdObjetoIncidente() == null && controleVotoDto.getDataGenericaMenor() == null && controleVotoDto.getDataSessaoMenor() == null){
			throw new ValidationException("Informe o periodo da sessão, o período para uma situação selecionada ou informe um incidente do processo");
		}
	}

	private void validaOpcaoPesquisaPorSituacao(ControleVotoDto controleVotoDto) throws ValidationException {
		if((controleVotoDto.getOperadorDataGenerica() != null && !controleVotoDto.getOperadorDataGenerica().equals("tudo")) || controleVotoDto.getDataGenericaMenor() != null){
			if(!controleVotoDto.isPublicado() && !controleVotoDto.isLiberado() && !controleVotoDto.isRecebido() && !controleVotoDto.isJulgamentoFinalizado()){
				throw new ValidationException("Informe a(s) situação(ões) a ser(em) pesquisada(s) ");
			}
		}
	}

	private void validaOpcaoPeriodoPesquisaPorSessao(ControleVotoDto controleVotoDto) throws ValidationException {
		if(controleVotoDto.getDataSessaoMenor() != null && controleVotoDto.getOperadorDataSessao() == null){
			throw new ValidationException("Informe o tipo de período da pesquisa por sessão");
		}
	}
	
	private void validaOpcaoPeriodoPesquisaPorSituacao(ControleVotoDto controleVotoDto) throws ValidationException {
		if((controleVotoDto.isPublicado() || controleVotoDto.isLiberado() || controleVotoDto.isRecebido() || controleVotoDto.isJulgamentoFinalizado()) && controleVotoDto.getOperadorDataGenerica() == null){
			throw new ValidationException("Informe o tipo de período da pesquisa por situação");
		}
	}	

	private void validaPeriodoPesquisaPorSessao(ControleVotoDto controleVotoDto) throws ValidationException {
		if(("menos".equals(controleVotoDto.getOperadorDataSessao()) || "igual a".equals(controleVotoDto.getOperadorDataSessao())) && controleVotoDto.getDataSessaoMenor() == null){
			throw new ValidationException("Necessário informar a data da sessão");
		}
		
		if("entre".equals(controleVotoDto.getOperadorDataSessao())){
			if(controleVotoDto.getDataSessaoMenor() == null || controleVotoDto.getDataSessaoMaior() == null){
				throw new ValidationException("Necessário informar o período de realização da sessão");
			}
	
			if(controleVotoDto.getDataSessaoMenor() != null && controleVotoDto.getDataSessaoMaior() != null 
					&& controleVotoDto.getDataSessaoMenor().compareTo(controleVotoDto.getDataSessaoMaior())>0){
				throw new ValidationException("A primeira data informada deve ser menor do que a segunda");
			}
		}
	}

	@Override
	public List<ControleVoto> pesquisarControleVoto(ObjetoIncidente<?> objetoIncidente, Long idMinistro, Date dataSessao, TipoTexto tipoTexto, Long idTexto)
			throws ServiceException {
		try {
			return dao.pesquisarControleVoto(objetoIncidente.getId(), idMinistro, dataSessao, tipoTexto, idTexto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public ControleVoto consultaControleDeVotosDoTexto(Texto texto) throws ServiceException, TextoSemControleDeVotosException {
		List<ControleVoto> controleVoto = pesquisarControleVoto(texto.getObjetoIncidente(), null, texto.getDataSessao(), texto.getTipoTexto(), texto.getId());
		if (controleVoto.size() == 0) {
			throw new TextoSemControleDeVotosException("O texto " + texto.getId() + " não está associado a um controle de votos!");
		}
		return controleVoto.get(0);
	}

	@Override
	public List<ControleVoto> pesquisarControleVoto(IConsultaDadosDoTexto consulta) throws ServiceException {
		try {
			ControleVotoDynamicRestriction consultaDinamica = montaConsultaDeControleDeVotos(consulta);
			return dao.pesquisarControleVoto(consultaDinamica);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	private ControleVotoDynamicRestriction montaConsultaDeControleDeVotos(IConsultaDadosDoTexto consulta) {
		ControleVotoDynamicRestriction consultaDinamica = new ControleVotoDynamicRestriction();
		consultaDinamica.setSequencialObjetoIncidente(consulta.getSequencialObjetoIncidente());
		consultaDinamica.setTipoTexto(consulta.getTipoDeTexto());
		consultaDinamica.setCodigoDoMinistro(consulta.getCodigoDoMinistro(), consulta.isIncluirPresidencia());
		return consultaDinamica;
	}

	@Override
	public List<ControleVoto> pesquisarParaInteiroTeor(IConsultaDeControleDeVotoInteiroTeor controleVotoSearchFilter) throws ServiceException {
		return pesquisarParaInteiroTeor(controleVotoSearchFilter, false);
	}

	@Override
	public List<ControleVoto> pesquisarParaInteiroTeor(IConsultaDeControleDeVotoInteiroTeor controleVotoSearchFilter, boolean pesquisarObjetosDoProcesso)
			throws ServiceException {

		return pesquisarParaInteiroTeor(controleVotoSearchFilter, pesquisarObjetosDoProcesso, false);
	}

	@Override
	public List<ControleVoto> pesquisarParaInteiroTeor(IConsultaDeControleDeVotoInteiroTeor controleVotoSearchFilter, boolean pesquisarObjetosDoProcesso,
			boolean filtrarPeloSetorComposicaoAcordao) throws ServiceException {
		try {
			if (controleVotoSearchFilter.getIdObjetoIncidente() == null) {
				ControleVotoDynamicQuery consultaDinamica = montaConsultaDeControleVoto(controleVotoSearchFilter, pesquisarObjetosDoProcesso,
						filtrarPeloSetorComposicaoAcordao);
				return dao.pesquisarControleVoto2(consultaDinamica);
			}
			
			return dao.pesquisarControleVoto(controleVotoSearchFilter, pesquisarObjetosDoProcesso, filtrarPeloSetorComposicaoAcordao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	private ControleVotoDynamicQuery montaConsultaDeControleVoto(IConsultaDeControleDeVotoInteiroTeor controleVotoSearchFilter,
			boolean pesquisarObjetosDoProcesso, boolean filtrarPeloSetorComposicaoAcordao) {
		ControleVotoDynamicQuery consultaDinamica = new ControleVotoDynamicQuery(pesquisarObjetosDoProcesso, filtrarPeloSetorComposicaoAcordao);
		consultaDinamica.setAnoMateria(controleVotoSearchFilter.getAnoMateria());
		consultaDinamica.setCodigoRecurso(controleVotoSearchFilter.getCodigoRecurso());
		consultaDinamica.setDataGeracao(controleVotoSearchFilter.getDataGeracao());
		consultaDinamica.setDataSessao(controleVotoSearchFilter.getDataSessao());
		consultaDinamica.setIdObjetoIncidente(controleVotoSearchFilter.getIdObjetoIncidente());
		consultaDinamica.setInteiroTeor(controleVotoSearchFilter.getInteiroTeor());
		consultaDinamica.setNumeroMateria(controleVotoSearchFilter.getNumeroMateria());
		consultaDinamica.setNumeroProcesso(controleVotoSearchFilter.getNumeroProcesso());
		consultaDinamica.setSessao(controleVotoSearchFilter.getSessao());
		consultaDinamica.setSiglaClasseProcessual(controleVotoSearchFilter.getSiglaClasseProcessual());
		consultaDinamica.setTipoJulgamento(controleVotoSearchFilter.getTipoJulgamento());
		consultaDinamica.setTipoMeioProcesso(controleVotoSearchFilter.getTipoMeioProcesso());
		consultaDinamica.setTipoTexto(controleVotoSearchFilter.getTipoTexto());
		consultaDinamica.setTipoSituacaoTexto(controleVotoSearchFilter.getTipoSituacaoTexto());

		return consultaDinamica;
	}

	@Override
	public List<ControleVoto> pesquisarControleVoto(ObjetoIncidente<?> objetoIncidente, Ministro ministro, Date dataSessao) throws ServiceException {

		try {
			return dao.pesquisarControleVoto(objetoIncidente, ministro, dataSessao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long recuperarProximaSequenciaVoto(ObjetoIncidente objetoIncidente) throws ServiceException {
		Long seq = null;
		try {
			seq = dao.recuperarUltimaSequenciaVoto(objetoIncidente);
			seq = incrementoSequenciaControleVoto(seq);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return seq;
	}

	@Override
	public Long incrementoSequenciaControleVoto(Long seq) {
		if (seq == null || seq == 0L) {
			seq = 5L;
		} else {
			seq += 10;
		}
		return seq;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long recuperarProximaSequenciaVotoRepercussaoGeral(ObjetoIncidente objetoIncidente) throws ServiceException {
		Long seq = null;
		try {
			seq = dao.recuperarUltimaSequenciaVoto(objetoIncidente);
			if (seq == null || seq == 0L) {
				seq = 15L;
			} else {
				seq += 10;
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return seq;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ControleVoto recuperar(ObjetoIncidente objetoIncidente, TipoTexto tipoTexto, Ministro ministro) throws ServiceException {
		try {
			return dao.recuperar(objetoIncidente, tipoTexto, ministro);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<ControleVoto> pesquisarControleVotoPorTipoTexto(ObjetoIncidente<?> oi,
			TipoTexto... tiposTextos) throws ServiceException {
		try {
			return dao.pesquisarControleVotoPorTipoTexto(oi, tiposTextos);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public List<ControleVoto> pesquisarControleVotoPorMinistro(ObjetoIncidente<?> oi, Ministro ministro) throws ServiceException {
		try {
			return dao.pesquisarControleVotoPorMinistro(oi, ministro);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	
	@Override
	public ControleVoto recuperarControleDeVotoSemSessao(ObjetoIncidente objetoIncidente, TipoTexto tipoTexto, Ministro ministro) throws ServiceException {
		try {
			return dao.recuperarControleDeVotoSemSessao(objetoIncidente, tipoTexto, ministro);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Boolean recuperarExistenciaPDFNaoAssinado(ObjetoIncidente objetoIncidente) throws ServiceException {
		Boolean possui = false;
		try {
			List<Long> cvs = dao.pesquisarControleVotoPDFNaoAssinado(objetoIncidente);
			if (cvs != null && cvs.size() > 0) {
				possui = true;
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return possui;
	}

	@Override
	public void excluirTextoDoControleDeVotos(Texto texto) throws ServiceException {
		try {
			ControleVotoDynamicRestriction consulta = new ControleVotoDynamicRestriction();
			consulta.setIdTexto(texto.getId());
			List<ControleVoto> controlesDeVoto = dao.pesquisarControleVoto(consulta);
			for (ControleVoto controleVoto : controlesDeVoto) {
				controleVoto.setTexto(null);
				alterar(controleVoto);
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void validarTipoTextoNovoControleVoto(ControleVoto controleVoto) throws ServiceException {
		if (controleVoto != null && controleVoto.getObjetoIncidente() != null) {

			List<ControleVoto> listaControleVoto = pesquisarControleVoto(controleVoto.getObjetoIncidente(), null, null, controleVoto.getTipoTexto(), null);
			// somente um texto por processo
			if (somenteUmTexto(controleVoto.getTipoTexto(), false)) {
				if (listaControleVoto != null && listaControleVoto.size() > 0) {
					throw new ServiceException("O controle de votos para texto \"" + controleVoto.getTipoTexto().getDescricao() + "\" já existe.");
				}
			}

			// pode possuir vários textos mas somente um por ministro
			if (somenteUmTexto(controleVoto.getTipoTexto(), true)) {
				if (listaControleVoto != null && listaControleVoto.size() > 0) {
					for (ControleVoto controleVotoMinistro : listaControleVoto) {
						if (controleVotoMinistro.getMinistro().getId().equals(controleVoto.getMinistro().getId()))
							throw new ServiceException("O controle de votos para texto \"" + controleVoto.getTipoTexto().getDescricao() + "\" já existe.");
					}
				}
			}

		}
	}

	private boolean somenteUmTexto(TipoTexto tipoTexto, boolean porMininstro) {

		if (tipoTexto != null) {
			if (!porMininstro) {
				return tipoTexto.equals(TipoTexto.EMENTA) || tipoTexto.equals(TipoTexto.ACORDAO) || tipoTexto.equals(TipoTexto.RELATORIO)
						|| tipoTexto.equals(TipoTexto.DECISAO_SOBRE_REPERCURSAO_GERAL) || tipoTexto.equals(TipoTexto.EMENTA_SOBRE_REPERCURSAO_GERAL);
			} else {
				return tipoTexto.equals(TipoTexto.MANIFESTACAO_SOBRE_REPERCUSAO_GERAL) || tipoTexto.equals(TipoTexto.VOTO);
			}

		}

		return false;

	}

	@Override
	public void sincronizaControleVotoComTexto(Long seqTexto, Long seqVotos,Date dataSessao) throws ServiceException {
		try {
			dao.sincronizaControleVotoComTexto(seqTexto, seqVotos, dataSessao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}		
	
	@Override
	public ControleVoto recuperarControleVoto(Long idControleVoto) throws ServiceException {
		try {
			return dao.recuperarControleVoto(idControleVoto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<ControleVoto> pesquisarControleVoto(ObjetoIncidente objetoIncidente) throws ServiceException {
		return pesquisarControleVoto(objetoIncidente, null, null);
	}
	
	
	public String inferirDataFimJulgamento(ObjetoIncidente objetoIncidente) throws ServiceException {

		List<ControleVoto> cvs = pesquisarControleVoto(objetoIncidente, null, null);

		String dataFimJulgamento = "";

		for (ControleVoto cv : cvs) {
			if (!cv.getTipoSituacaoTexto().equals(TipoSituacaoTexto.CANCELADO)) {
				if (cv.getTipoTexto().equals(TipoTexto.EMENTA) || cv.getTipoTexto().equals(TipoTexto.EMENTA_SOBRE_REPERCURSAO_GERAL)) {
					dataFimJulgamento = DateTimeHelper.getDataString( cv.getDataSessao());
				}
			}
		}
		return dataFimJulgamento;
	}
	
	public List<Object> verificarSigilosos() throws ServiceException {
		
		try {
			return dao.verificarSigilosos();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public Boolean existemCompletos() throws ServiceException {
		try {
			return dao.existemCompletos();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public Boolean existemSigilosos() throws ServiceException{
		try {
			return dao.existemSigilosos();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
}
