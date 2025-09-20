package br.gov.stf.estf.assinatura.stfoffice.editor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.ServerException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.jdom.JDOMException;
import org.jopendocument.dom.template.TemplateException;

import br.gov.stf.eprocesso.servidorpdf.servico.modelo.ExtensaoEnum;
import br.gov.stf.estf.assinatura.service.TextoHandlerServiceLocal;
import br.gov.stf.estf.cabecalho.model.CabecalhosObjetoIncidente.CabecalhoObjetoIncidente;
import br.gov.stf.estf.cabecalho.service.CabecalhoObjetoIncidenteService;
import br.gov.stf.estf.converter.DocumentConversionException;
import br.gov.stf.estf.converter.DocumentConverterService;
import br.gov.stf.estf.converter.source.ByteArrayDocumentSource;
import br.gov.stf.estf.converter.target.ByteArrayDocumentTarget;
import br.gov.stf.estf.documento.model.service.ArquivoEletronicoService;
import br.gov.stf.estf.documento.model.service.ControleVotoService;
import br.gov.stf.estf.documento.model.service.ModeloComunicacaoService;
import br.gov.stf.estf.documento.model.service.TipoComunicacaoService;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.ControleVoto;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.TipoComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.HistoricoProcessoOrigem;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.processostf.model.service.HistoricoProcessoOrigemService;
import br.gov.stf.estf.processostf.model.service.MapeamentoClasseSetorService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.ApplicationFactory;
import br.gov.stf.framework.util.IServiceLocator;
import br.gov.stf.stfoffice.editor.handler.IEditorHandler;
import br.gov.stf.stfoffice.handler.HandlerException;
import br.jus.stf.estf.montadortexto.MontadorTextoServiceException;
import br.jus.stf.estf.montadortexto.OpenOfficeMontadorTextoService;
import br.jus.stf.estf.montadortexto.SpecParte;
import br.jus.stf.estf.montadortexto.tools.TextoUtil;

public class AbrirTextoHandler implements IEditorHandler {

	private RequisicaoAbrirTexto requisicaoNovoTexto;
	private ArquivoEletronicoService arquivoEletronicoService;
	private OpenOfficeMontadorTextoService openOfficeMontadorTextoService;
	private final DocumentConverterService converterService;
	private MapeamentoClasseSetorService mapeamentoClasseSetorService;
	private ControleVotoService controleVotoService;
	private CabecalhoObjetoIncidenteService cabecalhoObjetoIncidenteService;
	private HistoricoProcessoOrigemService historicoProcessoOrigemService;
	private TextoHandlerServiceLocal textoHandlerServiceLocal;
	private ModeloComunicacaoService modeloComunicacaoService;
	private TipoComunicacaoService tipoComunicacaoService;
	private Long numeracaoUnica;
	private ModeloComunicacao modeloComunicacao; 

	public AbrirTextoHandler() {
		IServiceLocator locator = ApplicationFactory.getInstance().getServiceLocator();
		arquivoEletronicoService = (ArquivoEletronicoService) locator.getService("arquivoEletronicoService");
		converterService = (DocumentConverterService) locator.getService("converterService");
		openOfficeMontadorTextoService = (OpenOfficeMontadorTextoService) locator.getService("openOfficeMontadorTextoService");
		mapeamentoClasseSetorService = (MapeamentoClasseSetorService) locator.getService("mapeamentoClasseSetorService");
		controleVotoService = (ControleVotoService) locator.getService("controleVotoService");
		cabecalhoObjetoIncidenteService = (CabecalhoObjetoIncidenteService) locator.getService("cabecalhoObjetoIncidenteService");
		historicoProcessoOrigemService = (HistoricoProcessoOrigemService) locator.getService("historicoProcessoOrigemService");
		textoHandlerServiceLocal = (TextoHandlerServiceLocal) locator.getService("textoHandlerServiceLocal");
		modeloComunicacaoService = (ModeloComunicacaoService) locator.getService("modeloComunicacaoService");
		tipoComunicacaoService = (TipoComunicacaoService) locator.getService("tipoComunicacaoService");
	}

	public void setAtributos(RequisicaoAbrirTexto requisicaoNovoTexto, HttpSession session) {
		this.requisicaoNovoTexto = requisicaoNovoTexto;
	}

	/**
	 * Recupera o modelo selecionado e subtitui todas as TAGs definidas no documento
	 */
	public byte[] recuperarDocumento() throws HandlerException {

		byte[] arquivoAlterado = null;
		InputStream arquivoSaida = null;
		DadosComunsDoCabecalhoHandler dadosCabecalho = new DadosComunsDoCabecalhoHandler();

		List<ControleVoto> listaControleVoto = new ArrayList<ControleVoto>();
		CabecalhoObjetoIncidente cabecalhoObjIncidente = new CabecalhoObjetoIncidente();
		List<HistoricoProcessoOrigem> listaHistoricoProcessoOrigem = new LinkedList<HistoricoProcessoOrigem>();
		modeloComunicacao = new ModeloComunicacao();

		if (requisicaoNovoTexto.getObjetoIncidenteProcurado() != null) {

			// pesquisa o controle votos para achar a data de sessão que será
			// preenchida no cabeçalho.
			try {
				listaControleVoto = controleVotoService.pesquisarControleVoto(requisicaoNovoTexto.getObjetoIncidenteProcurado(), null, null);
			} catch (ServiceException e) {
				throw new HandlerException("Erro ao pesquisar Controle Voto.");
			}

			if (listaControleVoto != null && listaControleVoto.size() > 0) {
				dadosCabecalho.setControleVoto(listaControleVoto);
			}
			
			try {
				modeloComunicacao = modeloComunicacaoService.recuperarPorId(requisicaoNovoTexto.getCodigoModelo());
			} catch (Exception e) {
				throw new HandlerException("Erro ao recuperar o modelo comunicacao.");
			}
			
			if (requisicaoNovoTexto.getIsNumeracaoUnica()){
			
				try {
					numeracaoUnica = tipoComunicacaoService.pesquisaNumeracaoUnicaModelo(modeloComunicacao.getTipoComunicacao().getId());
				} catch (Exception e) {
					throw new HandlerException("Erro ao recuperar a numeracaoUnica.");
				}
				Calendar cal = GregorianCalendar.getInstance();
				Integer anoCorrente = cal.get(Calendar.YEAR);
				
				if (numeracaoUnica != null){
					++numeracaoUnica;				
				}else{
					numeracaoUnica = 1L;
				}
				
				TipoComunicacao tipoComunicacao = modeloComunicacao.getTipoComunicacao();
				
				SimpleDateFormat ano = new SimpleDateFormat("yyyy");  
				Integer anoAlteracao = Integer.parseInt(ano.format(tipoComunicacao.getDataAlteracao())) ; 
				//sempre que virar o ano, a numeracao na tabela deverá ser zerada
				if (anoCorrente > anoAlteracao){
					numeracaoUnica = 1L;
				}
				tipoComunicacao.setNumeroComunicacaoAnterior(numeracaoUnica);
				
				
				try {
					numeracaoUnica = tipoComunicacaoService.pesquisaNumeracaoUnicaModelo(modeloComunicacao.getTipoComunicacao().getId());
					requisicaoNovoTexto.setNumeracao(numeracaoUnica);
					tipoComunicacaoService.alterar(tipoComunicacao);
				} catch (Exception e) {
					throw new HandlerException("Erro ao alterar a numeração única do tipo Comunicacao");
				}
				
				StringBuffer numeracaoUnicaAno = new StringBuffer();
				numeracaoUnicaAno.append(numeracaoUnica);
				numeracaoUnicaAno.append("/");
				numeracaoUnicaAno.append(anoCorrente);
				
				requisicaoNovoTexto.getDados().setNumeracaoUnica(numeracaoUnicaAno.toString());
			}

			// busca o cabeçalho do objeto incidente
			try {
				cabecalhoObjIncidente = cabecalhoObjetoIncidenteService.recuperarCabecalho(requisicaoNovoTexto.getObjetoIncidenteProcurado().getId());
			} catch (ServiceException e) {
				throw new HandlerException("Erro ao recuperar o cabeçalho objeto incidente.");
			}

			if (cabecalhoObjIncidente != null) {

				try {
					dadosCabecalho.setSpecCabecalho(cabecalhoObjetoIncidenteService.getSpecCabecalho(cabecalhoObjIncidente));
				} catch (ServiceException e) {
					throw new HandlerException("Erro ao montar as informações da parte.");
				}
			}

			// seta a lista de partes e deixa as partes em maiusculo
			for (SpecParte specParte : dadosCabecalho.getSpecCabecalho().getPartes()) {
				specParte.setCapitalizar(false);
				requisicaoNovoTexto.getDados().getPartes().add(specParte);
			}

			// seta a unidade responsavel de acordo com a mapeamento classe setor
			try {
				Setor setor = mapeamentoClasseSetorService.recuperarSetorDeDestinoDoDeslocamento(requisicaoNovoTexto.getObjetoIncidenteProcurado());
				requisicaoNovoTexto.getDados().setUnidadeResponsavel(TextoUtil.capitalizar(setor.getNome()));
			} catch (Exception e) {
				e.printStackTrace();
			}

			// busca os dados do processo de origem
			// LISTA TEMPORÁRIA PARA TRAZER MAIS DE UM REGISTRO PARA TABELA HISTORICOPROCESSOORIGEM
			// FUTURAMENTE DEVERÁ VIR SOMENTE UM REGISTRO DA QUERY
			try {
				listaHistoricoProcessoOrigem = historicoProcessoOrigemService.recuperarPorObjetoIncidente(requisicaoNovoTexto.getObjetoIncidenteProcurado()
						.getId());
			} catch (ServiceException e) {
				throw new HandlerException("Erro ao recuperar o histórico do processo origem.");
			}

			// VARIÁVEL TEMPORÁRIA PARA RESOLVER O PROBLEMA DOS REGISTROS DUPLICADOS NA
			// HISTORICOPROCESSOORIGEM. ESTE IF DEVERÁ SER RETIRA FUTURAMENTE
			HistoricoProcessoOrigem primeiroListaHistoricoProcessoOrigem = new HistoricoProcessoOrigem();
			if (listaHistoricoProcessoOrigem != null && listaHistoricoProcessoOrigem.size() > 0) {
				primeiroListaHistoricoProcessoOrigem = listaHistoricoProcessoOrigem.get(0);
			}

			if (primeiroListaHistoricoProcessoOrigem != null) {
				dadosCabecalho.setHistoricoOrigem(primeiroListaHistoricoProcessoOrigem);
				if (primeiroListaHistoricoProcessoOrigem.getSiglaClasseOrigem() != null
						&& primeiroListaHistoricoProcessoOrigem.getSiglaClasseOrigem().trim().length() > 0) {
					requisicaoNovoTexto.getDados().setSiglaProcessoOrigem(primeiroListaHistoricoProcessoOrigem.getSiglaClasseOrigem());
				}
				if (primeiroListaHistoricoProcessoOrigem.getNumeroProcessoOrigem() != null) {
					requisicaoNovoTexto.getDados().setNumeroProcessoOrigem(primeiroListaHistoricoProcessoOrigem.getNumeroProcessoOrigem());
				}
				if (primeiroListaHistoricoProcessoOrigem.getOrigem() != null && primeiroListaHistoricoProcessoOrigem.getOrigem().getDescricao() != null) {
					requisicaoNovoTexto.getDados().setTribunalOrigem(primeiroListaHistoricoProcessoOrigem.getOrigem().getDescricao());
				}
			}

			// carrega dos dados da descrição curta do cabeçalho
			requisicaoNovoTexto.getDados().setDescricaoCurtaProcesso(carregaDadosDescricaoCurta(requisicaoNovoTexto.getObjetoIncidenteProcurado()));
		}

		// seta a data de sessão se houver ou o colegiado. Estes dados serão
		// necessários para o cabeçalho do texto
		if (dadosCabecalho.getControleVoto() != null && dadosCabecalho.getControleVoto().size() > 0) {
			if (dadosCabecalho.getControleVoto().get(0).getDataSessao() != null) {
				requisicaoNovoTexto.getDados().setDataSessao(dadosCabecalho.getControleVoto().get(0).getDataSessao());
			}
			// seta o colegiado do cabeçalho
			requisicaoNovoTexto.getDados().setColegiado(dadosCabecalho.selecionaTipoSessaoControleVoto());
		}

		try {
			// subtitui as TAGs pelos campos recuperados.
			arquivoSaida = openOfficeMontadorTextoService.carregarTemplateDaSecretariaJudiciaria(dadosCabecalho.getSpecCabecalho(),
					requisicaoNovoTexto.getDocumento(), requisicaoNovoTexto.getDados());
		} catch (ServerException e) {
			throw new HandlerException("Erro ServerExcpetion.");
		} catch (IOException e) {
			throw new HandlerException("Erro de I/O.");
		} catch (TemplateException e) {
			throw new HandlerException("Erro TemplateException.");
		} catch (JDOMException e) {
			throw new HandlerException("Erro JDOMException.");
		} catch (MontadorTextoServiceException e) {
			throw new HandlerException("Erro no service do montador de texto.");
		}

		// transforma o InputStream para um byteArray
		try {
			arquivoAlterado = IOUtils.toByteArray(arquivoSaida);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// seta o novo conteudo do documento para que possa abrir o documento de
		// forma correta.
		requisicaoNovoTexto.setDocumento(arquivoAlterado);

		return requisicaoNovoTexto.getDocumento();
	}

	/**
	 * Carrega o dado da descrição curta para o cabeçalho
	 * @param objetoIncidente
	 * @return
	 */
	private String carregaDadosDescricaoCurta(ObjetoIncidente<?> objetoIncidente) {
		return objetoIncidente.getIdentificacao();
	}

	public void fecharDocumento() throws HandlerException {

	}

	public void gerarPDF(byte[] pdf) throws HandlerException {
		textoHandlerServiceLocal.gerarPDF(pdf, requisicaoNovoTexto);
	}

	public void salvarDocumento(byte[] odt) throws HandlerException {

		InputStream documentoODT = new ByteArrayInputStream(odt);
		byte[] arquivoODT = null;

		try {
			arquivoODT = IOUtils.toByteArray(documentoODT);
		} catch (IOException e) {
			throw new HandlerException("Erro ao converter o InputStream para ByteArray");
		}

		if (requisicaoNovoTexto.getComunicacao().getId() != null) {
			salvarTexto(arquivoODT);
		} else {
			salvarNovoTexto(arquivoODT);
		}

	}

	private void salvarTexto(byte[] odt) throws HandlerException {
		ArquivoEletronico comunicacao = requisicaoNovoTexto.getComunicacao().getArquivoEletronico();
		comunicacao.setConteudo(odt);

		try {
			arquivoEletronicoService.salvar(comunicacao);
		} catch (ServiceException e) {
			throw new HandlerException("Erro ao salvar documento");
		}

	}

	
	private void salvarNovoTexto(byte[] odt) throws HandlerException {
		requisicaoNovoTexto.getComunicacao().setNumeroComunicacao(numeracaoUnica);
		
		textoHandlerServiceLocal.salvarNovoTexto(odt, requisicaoNovoTexto);
	}

	public InputStream converteArquivo(byte[] source, ExtensaoEnum extensaoOriginal, ExtensaoEnum extensaoDestino) throws MontadorTextoServiceException,
			IOException {
		ByteArrayDocumentTarget target1 = new ByteArrayDocumentTarget();
		
		try {
			converterService.convertDocument(new ByteArrayDocumentSource(source), extensaoOriginal.getContentType(), target1, extensaoDestino.getContentType());
		} catch (DocumentConversionException e) {
			throw new MontadorTextoServiceException("Erro durante a conversão dos textos de ODT para RTF", e);
		}
		return new ByteArrayInputStream(target1.getByteArray());

	}

}
