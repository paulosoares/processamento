package br.gov.stf.estf.assinatura.relatorio.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.assinatura.relatorio.RelatorioAcordaoPublicado;
import br.gov.stf.estf.assinatura.relatorio.RelatorioAutosEmprestadosAdvogados;
import br.gov.stf.estf.assinatura.relatorio.RelatorioAutosEmprestadosOrgaosExternos;
import br.gov.stf.estf.assinatura.relatorio.RelatorioGerirCargaAutos;
import br.gov.stf.estf.assinatura.relatorio.RelatorioGuiaDeslocamentoPeticao;
import br.gov.stf.estf.assinatura.relatorio.RelatorioGuiaDeslocamentoProcesso;
import br.gov.stf.estf.assinatura.relatorio.RelatorioProcessoInteresse;
import br.gov.stf.estf.assinatura.relatorio.dataaccess.ProcessamentoRelatorioDao;
import br.gov.stf.estf.assinatura.relatorio.service.ProcessamentoRelatorioService;
import br.gov.stf.estf.assinatura.visao.util.InfoPecaVinculadoAndamentoDTO;
import br.gov.stf.estf.documento.model.service.ArquivoProcessoEletronicoService;
import br.gov.stf.estf.documento.model.service.ComunicacaoIncidenteService;
import br.gov.stf.estf.documento.model.service.ComunicacaoService;
import br.gov.stf.estf.documento.model.service.DeslocamentoComunicacaoService;
import br.gov.stf.estf.documento.model.service.DocumentoComunicacaoService;
import br.gov.stf.estf.documento.model.service.FaseComunicacaoService;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente.FlagProcessoLote;
import br.gov.stf.estf.entidade.documento.DeslocamentoComunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.FaseComunicacao;
import br.gov.stf.estf.entidade.documento.FaseComunicacao.FlagFaseAtual;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.TipoArquivo;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.documento.TipoPecaProcesso;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Guia.GuiaId;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.expedicao.model.util.ArquivoPdfDTO;
import br.gov.stf.estf.expedicao.model.util.CertidaoTransitoDTO;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.EmprestimoAutosProcessoDao;
import br.gov.stf.estf.processostf.model.service.GuiaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.stfoffice.handler.HandlerException;
import br.jus.stf.util.jasperreports.UtilJasperReports;

@Service("processamentoRelatorioService")
public class ProcessamentoRelatorioServiceLocal implements ProcessamentoRelatorioService {

	private static final int CODIGO_CERTIDAO_TRANSITO_JULGADO = 8219;
	private static final int CODIGO_CONTRARRAZOES = 8523;
	private static final int CODIGO_CONTRAMINUTA = 8524;
	private static final int CODIGO_CONTRARRAZOES_DECURSO_PRAZO = 8543;
	private static final int CODIGO_APRESENTAR_RESPOSTA_DECURSO_PRAZO = 8544;
	private static final int CODIGO_CONTRAMINUTA_DECURSO_PRAZO = 4543;
	private static final String NOME_SECRETARIA = "Secretaria Judiciária";
	private static final String NOME_FUNCAO_SECRETARIA = "Secretaria Judiciária";
	private static final String NOME_SECAO = "Seção de Agravos";
	public static final String DESTINATARIO_PGR = "Procurador-Geral da República";
	public static final String DESTINATARIO_AGU = "Advogado-Geral da União";
	public static final String DESTINATARIO_DPF = "Diretor-Geral da Polícia Federal";
	
	@Autowired
	private ProcessamentoRelatorioDao processamentoRelatorioDao;

	@Autowired
	private EmprestimoAutosProcessoDao emprestimoAutosProcessoDao;

	@Autowired
	private GuiaService guiaService;
	
	@Autowired
	private ArquivoProcessoEletronicoService arquivoProcessoEletronicoService;
	
	@Autowired
	private DocumentoComunicacaoService documentoComunicacaoService;
	
	@Autowired
	private DeslocamentoComunicacaoService deslocamentoComunicacaoService;
	
	@Autowired
	private FaseComunicacaoService faseComunicacaoService;
	
	@Autowired
	private ComunicacaoIncidenteService comunicacaoIncidenteService;
	
	@Autowired
	private ComunicacaoService comunicacaoService;
	
	public String recuperarPathImagens(String uriImagem){
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		return cl.getResource(uriImagem).toString();
	}
	
	/* (non-Javadoc)
	 * @see br.gov.stf.estf.assinatura.relatorio.service.ProcessamentoRelatorioService#criarRelatorioGuiaDeslocamentoProcesso(java.lang.Long, java.lang.Short, java.lang.Long)
	 * gera a guia de deslocamento interno e para órgãos externos.
	 */
	@Override
	public byte[] criarRelatorioVariasGuiasDeslocamentoProcesso(List<Guia> guias)
			throws ServiceException {
		try {
			String relatorio = "relatorios/RelatorioGuiaDeslocamentoProcesso.jasper";
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("imprimePostal", "N" );
			params.put("pathLogo", recuperarPathImagens("relatorios/logo_supremo.gif"));
			params.put("seta", recuperarPathImagens("relatorios/seta.gif"));
			
			List<RelatorioGuiaDeslocamentoProcesso> dados = processamentoRelatorioDao
					.consultarRelatorioVariasGuiasDeslocamentoProcesso(guias);
			return UtilJasperReports.criarRelatorioPdf(relatorio, dados, params);
		} catch (DaoException e) {
			throw new ServiceException(
					"Ocorreu um erro ao recuperar os dados do Relatório de Guia de Deslocamento de Processo!", e);
		} catch (FileNotFoundException e) {
			throw new ServiceException(e);
		} catch (JRException e) {
			e.printStackTrace();
			throw new ServiceException("Ocorreu um erro ao montar o Relatório de Guia de Deslocamento de Processo!", e);
		}
	}

	@Override
	public byte[] criarRelatorioGuiaDeslocamentoProcesso(Long numeroDaGuia, Short anoDaGuia, Long codigoOrgaoOrigem, boolean postal)
			throws ServiceException {
		try {
			
			String relatorio = "relatorios/RelatorioGuiaDeslocamentoProcesso.jasper";
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("imprimePostal", postal ? "S" : "N" );
			params.put("pathLogo", recuperarPathImagens("relatorios/logo_supremo.gif"));
			params.put("seta", recuperarPathImagens("relatorios/seta.gif"));
			
			List<RelatorioGuiaDeslocamentoProcesso> dados = processamentoRelatorioDao
					.consultarRelatorioGuiaDeslocamentoProcesso(numeroDaGuia, anoDaGuia, codigoOrgaoOrigem);
			return UtilJasperReports.criarRelatorioPdf(relatorio, dados, params);
		} catch (DaoException e) {
			throw new ServiceException(
					"Ocorreu um erro ao recuperar os dados do Relatório de Guia de Deslocamento de Processo!", e);
		} catch (FileNotFoundException e) {
			throw new ServiceException(e);
		} catch (JRException e) {
			e.printStackTrace();
			throw new ServiceException("Ocorreu um erro ao montar o Relatório de Guia de Deslocamento de Processo!", e);
		}
	}
	
	/**
	 *  Gera a guia de deslocamento para carga de autos de processos para advogados na retirada
	 */
	@Override
	public byte[] criarRelatorioGuiaRetiradaAutosProcesso(Long numeroDaGuia, Short anoDaGuia, Long codigoOrgaoOrigem, boolean postal)
			throws ServiceException {
		try {
			String relatorio = "relatorios/RelatorioGuiaDeslocamentoProcesso.jasper";
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("imprimePostal", postal ? "S" : "N" );
			params.put("pathLogo", recuperarPathImagens("relatorios/logo_supremo.gif"));
			params.put("seta", recuperarPathImagens("relatorios/seta.gif"));

			GuiaId guiaId = new GuiaId();
			guiaId.setAnoGuia(anoDaGuia);
			guiaId.setNumeroGuia(numeroDaGuia);
			guiaId.setCodigoOrgaoOrigem(codigoOrgaoOrigem);
			
			Guia guia = guiaService.recuperarPorId(guiaId);
			List<RelatorioGuiaDeslocamentoProcesso> dados;
			if (emprestimoAutosProcessoDao.existeEmprestimoNaGuiaDeAutos(guia)) {
			   dados = processamentoRelatorioDao.consultarRelatorioGuiaRetiradaAutosProcesso(numeroDaGuia, anoDaGuia, codigoOrgaoOrigem);
			} else {
			   dados = processamentoRelatorioDao.consultarRelatorioGuiaAntigaRetiradaAutosProcesso(numeroDaGuia, anoDaGuia, codigoOrgaoOrigem);
			}
			return UtilJasperReports.criarRelatorioPdf(relatorio, dados, params);
		} catch (DaoException e) {
			throw new ServiceException(
					"Ocorreu um erro ao recuperar os dados do Relatório de Guia de Deslocamento de Processo!", e);
		} catch (FileNotFoundException e) {
			throw new ServiceException(e);
		} catch (JRException e) {
			e.printStackTrace();
			throw new ServiceException("Ocorreu um erro ao montar o Relatório de Guia de Deslocamento de Processo!", e);
		}
	}
	
	/**
	 *  Gera a guia de deslocamento para carga de autos de processos para advogados na devolução
	 *  Guias antigas = criadas pelo sistema MAP.
	 */
	@Override
	public byte[] criarRelatorioGuiaAntigaDevolucaoAutosProcesso(List<Guia> guias)
			throws ServiceException {
		try {
			String relatorio = "relatorios/RelatorioGuiaDeslocamentoProcesso.jasper";
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("imprimePostal", "N" );
			params.put("pathLogo", recuperarPathImagens("relatorios/logo_supremo.gif"));
			params.put("seta", recuperarPathImagens("relatorios/seta.gif"));
			 
			List<RelatorioGuiaDeslocamentoProcesso> dados = processamentoRelatorioDao
					.consultarRelatorioGuiaAntigaDevolucaoAutosProcesso(guias);
			
			return UtilJasperReports.criarRelatorioPdf(relatorio, dados, params);
		} catch (DaoException e) {
			throw new ServiceException(
					"Ocorreu um erro ao recuperar os dados do Relatório de Guia de Deslocamento de Processo!", e);
		} catch (FileNotFoundException e) {
			throw new ServiceException(e);
		} catch (JRException e) {
			e.printStackTrace();
			throw new ServiceException("Ocorreu um erro ao montar o Relatório de Guia de Deslocamento de Processo!", e);
		}
	}
	/**
	 *  Gera a guia de deslocamento para carga de autos de processos para advogados na devolução
	 */
	@Override
	public byte[] criarRelatorioGuiaDevolucaoAutosProcesso(List<Guia> guias)
			throws ServiceException {
		try {
			String relatorio = "relatorios/RelatorioGuiaDeslocamentoProcesso.jasper";
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("imprimePostal", "N" );
			params.put("pathLogo", recuperarPathImagens("relatorios/logo_supremo.gif"));
			params.put("seta", recuperarPathImagens("relatorios/seta.gif"));

			List<RelatorioGuiaDeslocamentoProcesso> dados = processamentoRelatorioDao
					.consultarRelatorioGuiaDevolucaoAutosProcesso(guias);
			
			return UtilJasperReports.criarRelatorioPdf(relatorio, dados, params);
		} catch (DaoException e) {
			throw new ServiceException(
					"Ocorreu um erro ao recuperar os dados do Relatório de Guia de Deslocamento de Processo!", e);
		} catch (FileNotFoundException e) {
			throw new ServiceException(e);
		} catch (JRException e) {
			e.printStackTrace();
			throw new ServiceException("Ocorreu um erro ao montar o Relatório de Guia de Deslocamento de Processo!", e);
		}
	}
	@Override
	public byte[] criarRelatorioGuiaDeslocamentoPeticao(List<Guia> guias)
			throws ServiceException {
		try {
			String relatorio = "relatorios/RelatorioGuiaDeslocamentoPeticao.jasper";
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("imprimePostal", "N");
			params.put("pathLogo", recuperarPathImagens("relatorios/logo_supremo.gif"));
			List<RelatorioGuiaDeslocamentoPeticao> dados = processamentoRelatorioDao
					.consultarRelatorioGuiaDeslocamentoPeticao(guias);
			return UtilJasperReports.criarRelatorioPdf(relatorio, dados, params);
		} catch (DaoException e) {
			throw new ServiceException(
					"Ocorreu um erro ao recuperar os dados do Relatório de Guia de Deslocamento de Petição!", e);
		} catch (FileNotFoundException e) {
			throw new ServiceException(e);
		} catch (JRException e) {
			e.printStackTrace();
			throw new ServiceException("Ocorreu um erro ao montar o Relatório de Guia de Deslocamento de Peticao!", e);
		}
	}
	
	// autos emprestados
	@Override
	public List<RelatorioAutosEmprestadosAdvogados> recuperarAutosEmprestadosAdvogados(String siglaClasseProcesso, 
			String dataInicial,	String dataFinal)	throws ServiceException {
		List<RelatorioAutosEmprestadosAdvogados> dados = null;
		try {
			dados = processamentoRelatorioDao.consultarRelatorioAutosEmprestadosAdvogados(siglaClasseProcesso, dataInicial, dataFinal);
			return dados;
		} catch (DaoException e) {
			throw new ServiceException(
					"Ocorreu um erro ao recuperar os dados do Relatório de Autos Emprestados!", e);
		}
	}
	
	@Override
	public List<RelatorioAutosEmprestadosOrgaosExternos> recuperarAutosEmprestadosOrgaosExternos(
			String siglaClasseProcesso, String dataInicial,
			String dataFinal, Long codigoOrgaoDestino)	throws ServiceException {
		List<RelatorioAutosEmprestadosOrgaosExternos> dados = null;
		try {
			dados =	processamentoRelatorioDao.consultarRelatorioAutosEmprestadosOrgaosExternos(
							siglaClasseProcesso, dataInicial, dataFinal, codigoOrgaoDestino);
			return dados;
		} catch (DaoException e) {
			throw new ServiceException(
					"Ocorreu um erro ao recuperar os dados do Relatório de Autos Emprestados!", e);
		}
	}
	
	@Override
	public List<RelatorioAcordaoPublicado> recuperarAcordaoPublicado(String dataPublicacao, String codigoSetorPubAcordao, String deslocadoParaAcordao) throws ServiceException {
		List<RelatorioAcordaoPublicado> dados = null;
		try {
			dados =	processamentoRelatorioDao.consultarRelatorioAcordaoPublicado(dataPublicacao, codigoSetorPubAcordao, deslocadoParaAcordao);
			return dados;
		} catch (DaoException e) {
			throw new ServiceException(
					"Ocorreu um erro ao recuperar os dados do Relatório de Acórdãos Publicados!", e);
		}
	}
	
	@Override
	public byte[] criarRelatorioAutosEmprestadosAdvogados(String siglaClasseProcesso, String dataInicial,
			String dataFinal, List<RelatorioAutosEmprestadosAdvogados> listaAutos)
			throws ServiceException {
		List<RelatorioAutosEmprestadosAdvogados> dados = null;		
		try {
			String relatorio = "relatorios/RelatorioAutosEmprestadosAdvogados.jasper";
			// se a listaAutos já possuir dados (uma pesquisa já foi realizada), então imprimir sem recuperar novamente
			if ((listaAutos == null) || (listaAutos.isEmpty())) {
				dados = processamentoRelatorioDao
					.consultarRelatorioAutosEmprestadosAdvogados(siglaClasseProcesso, dataInicial, dataFinal);
				return UtilJasperReports.criarRelatorioPdf(relatorio, dados);
			} else {
				return UtilJasperReports.criarRelatorioPdf(relatorio, listaAutos);
			}
		} catch (DaoException e) {
			throw new ServiceException(
					"Ocorreu um erro ao recuperar os dados do Relatório de Autos Emprestados!", e);
		} catch (FileNotFoundException e) {
			throw new ServiceException(e);
		} catch (JRException e) {
			throw new ServiceException("Ocorreu um erro ao montar o Relatório de Autos Emprestados!", e);
		}
	}

	@Override
	public byte[] criarRelatorioAutosEmprestadosOrgaosExternos(String siglaClasseProcesso, String dataInicial,
			String dataFinal, Long codigoOrgaoDestino, List<RelatorioAutosEmprestadosOrgaosExternos> listaAutos)
			throws ServiceException {
		List<RelatorioAutosEmprestadosOrgaosExternos> dados = null;
		try {
			String relatorio = "relatorios/RelatorioAutosEmprestadosOrgaosExternos.jasper";
			// se a listaAutos já possuir dados (uma pesquisa já foi realizada), então imprimir sem recuperar novamente
			if ((listaAutos == null) || (listaAutos.isEmpty())){
				dados =	processamentoRelatorioDao.consultarRelatorioAutosEmprestadosOrgaosExternos(
								siglaClasseProcesso, dataInicial, dataFinal, codigoOrgaoDestino);
				return UtilJasperReports.criarRelatorioPdf(relatorio, dados);
			} else {
				return UtilJasperReports.criarRelatorioPdf(relatorio, listaAutos);
			}
		} catch (DaoException e) {
			throw new ServiceException(
					"Ocorreu um erro ao recuperar os dados do Relatório de Autos Emprestados!", e);
		} catch (FileNotFoundException e) {
			throw new ServiceException(e);
		} catch (JRException e) {
			throw new ServiceException("Ocorreu um erro ao montar o Relatório de Autos Emprestados!", e);
		}
	}
	
	@Override
	public byte[] criarRelatorioAcordaoPublicado(String dataPublicacao, 
			           List<RelatorioAcordaoPublicado> listaAcordaosPublicados, String codigoSetorPubAcordao, String deslocadoParaAcordao)
			throws ServiceException {
		List<RelatorioAcordaoPublicado> dados = null;
		try {
			String relatorio = "relatorios/RelatorioAcordaosPublicados.jasper";
			// se já possuir dados (uma pesquisa já foi realizada), então imprimir sem recuperar novamente
			if ((listaAcordaosPublicados == null) || (listaAcordaosPublicados.isEmpty())){
				dados =	processamentoRelatorioDao.consultarRelatorioAcordaoPublicado(dataPublicacao, codigoSetorPubAcordao, deslocadoParaAcordao);
				return UtilJasperReports.criarRelatorioPdf(relatorio, dados);
			} else {
				return UtilJasperReports.criarRelatorioPdf(relatorio, listaAcordaosPublicados);
			}
		} catch (DaoException e) {
			throw new ServiceException(
					"Ocorreu um erro ao recuperar os dados do Relatório de Acórdãos Publicados!", e);
		} catch (FileNotFoundException e) {
			throw new ServiceException(e);
		} catch (JRException e) {
			throw new ServiceException("Ocorreu um erro ao montar o Relatório de Acórdãos Publicados!", e);
		}
	}
	
	@Override
	public byte[] criarRelatorioGerirAutos(List<RelatorioGerirCargaAutos> listaAutos)
			throws ServiceException {
		try {
			String relatorio = "relatorios/RelatorioGerirCargaDosAutos.jasper";
			return UtilJasperReports.criarRelatorioPdf(relatorio, listaAutos);
		} catch (FileNotFoundException e) {
			throw new ServiceException(e);
		} catch (JRException e) {
			throw new ServiceException("Ocorreu um erro ao montar o Relatório de Autos Emprestados!", e);
		}
	}
	
	@Override
	public byte[] criarRelatorioProcessoInteresse(Long seqJurisdicionado, List<RelatorioProcessoInteresse> dados) throws ServiceException {
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("LOGO_RELATORIO", recuperarPathImagens("relatorios/logo_supremo.gif"));

			String relatorio = "relatorios/RelatorioProcessoInteresse.jasper";
			if (dados == null) {
				dados = processamentoRelatorioDao.consultarRelatorioProcessoInteresse(seqJurisdicionado);
			}
			return UtilJasperReports.criarRelatorioPdf(relatorio, dados, params);
		} catch (DaoException e) {
			throw new ServiceException(
					"Ocorreu um erro ao recuperar os dados do Relatório de Processos de Interesse!", e);
		} catch (FileNotFoundException e) {
			throw new ServiceException(e);
		} catch (JRException e) {
			throw new ServiceException("Ocorreu um erro ao montar o Relatório de Processos de interesse!", e);
		}
		
	}
	
	@Override
	public byte[] criarRelatorioProcessoInteresseSemMovimentada(Long seqJurisdicionado, List<RelatorioProcessoInteresse> dados) throws ServiceException {
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("LOGO_RELATORIO", recuperarPathImagens("relatorios/logo_supremo.gif"));
			
			String relatorio = "relatorios/RelatorioProcessoInteresseSemMovimentada.jasper";
			if (dados == null) {
				throw new ServiceException("Não há informações para o relatório");
			}
			return UtilJasperReports.criarRelatorioPdf(relatorio, dados, params);
		} catch (FileNotFoundException e) {
			throw new ServiceException(e);
		} catch (JRException e) {
			throw new ServiceException("Ocorreu um erro ao montar o Relatório de Processos de interesse!", e);
		}
		
	}
	
	@Override
    public String criarCertidaoBaixaProcessoEletronico(String idProcesso, String titulo,
            String corpo, String nomeSecretario, String descCargo, ObjetoIncidente<?> objetoIncidente,
            Setor setor, AndamentoProcesso andamentoProcesso, Usuario usuarioLogado) throws ServiceException, IOException {
        try {
            // Recuperar o caminho do brasao
            //ClassLoader cl = this.getClass().getClassLoader();
            String pathBrasao = recuperarPathImagens("images/brasao.gif"); //cl.getResource("images/brasao.gif").toString();
			String pathAssinatura = recuperarPathImagens("images/assinatura_secretario_judiciario.JPG"); //cl.getResource("images/assinatura_secretario_judiciario.JPG").toString();

            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("PATH_BRASAO", pathBrasao);
			params.put("PATH_ASSINATURA", pathAssinatura);
            params.put("ID_PROCESSO", idProcesso);
            params.put("TITULO", titulo);
            params.put("CORPO", corpo);
            params.put("NOME_SECRETARIO", nomeSecretario);
            params.put("DESCRICAO_CARGO", descCargo);
            params.put("NOME_USUARIO_AUTOR", usuarioLogado.getNome());
            params.put("MATRICULA_USUARIO_AUTOR", usuarioLogado.getMatricula());

            String relatorio = "relatorios/RelatorioTermoBaixa.jasper";
            //return UtilJasperReports.criarRelatorioPdf(relatorio, null, params);
            byte[] resultRel = UtilJasperReports.criarRelatorioPdf(relatorio, null, params);
            salvarPecaProcessual(resultRel, objetoIncidente, setor, andamentoProcesso);
            String nomeArq = "TERMO_BAIXA_" + idProcesso.replaceAll(" ", "") + "_";
            String nomeArqFinal = gerarPDF(nomeArq, resultRel);

            return nomeArqFinal;
        } catch (FileNotFoundException e) {
            throw new ServiceException(e);
        } catch (JRException e) {
            throw new ServiceException("Ocorreu um erro ao montar o Termo de Baixa/Remessa!", e);
        }
    }

	// gera o relatório e grava na pasta temp 
	@Override
	public String criarRelatorioGuiaDeslocamentoProcessoNaPastaTemp(Long numeroDaGuia, Short anoDaGuia,
			Long codigoOrgaoOrigem, boolean postal) throws ServiceException, IOException {
		byte[] resultRel = criarRelatorioGuiaDeslocamentoProcesso(numeroDaGuia, anoDaGuia, codigoOrgaoOrigem, postal);
		String nomeArq = "GUIA_DESLOCAMENTO_" + numeroDaGuia + anoDaGuia + "_"  ;
		String nomeArqFinal = gerarPDF(nomeArq, resultRel);
		return nomeArqFinal;
	}
	// gera o relatório e grava na pasta temp 
	@Override
	public String criarRelatorioGuiaDeslocamentoPeticaoNaPastaTemp(List<Guia> guias) throws ServiceException, IOException {
		byte[] resultRel = criarRelatorioGuiaDeslocamentoPeticao(guias);
		String nomeArq = "GUIA_DESLOCAMENTO_";
		String nomeArqFinal = gerarPDF(nomeArq, resultRel);
		return nomeArqFinal;
	}
	// gerar guias antigas no momento do recebimento de processos. Emprestimos efetuados à órgãos externos/advogados antes da migração para o novo sistema.
	@Override
	public String criarRelatorioGuiaAntigaDevolucaoProcessosNaPastaTemp(List<Guia> guias) throws ServiceException, IOException {
		byte[] resultRel = criarRelatorioGuiaAntigaDevolucaoAutosProcesso(guias);
		String nomeArq = "GUIA_DESLOCAMENTO_";
		String nomeArqFinal = gerarPDF(nomeArq, resultRel);
		return nomeArqFinal;
	}
	// gerar guias de dovolução de processos emprestados à órgãos externos ou advogados. 
	@Override
	public String criarRelatorioGuiaDevolucaoProcessosNaPastaTemp(List<Guia> guias) throws ServiceException, IOException {
		byte[] resultRel = criarRelatorioGuiaDevolucaoAutosProcesso(guias);
		String nomeArq = "GUIA_DESLOCAMENTO_";
		String nomeArqFinal = gerarPDF(nomeArq, resultRel);
		return nomeArqFinal;
	}
	// gerar guias de recebimento de processos relativo a deslocamento interno (de um setor para outro dentro do STF)
	@Override
	public String criarRelatorioGuiaRecebimentoProcessosNaPastaTemp(List<Guia> guias) throws ServiceException, IOException {
		byte[] resultRel = criarRelatorioVariasGuiasDeslocamentoProcesso(guias);
		String nomeArq = "GUIA_DESLOCAMENTO_";
		String nomeArqFinal = gerarPDF(nomeArq, resultRel);
		return nomeArqFinal;
	}
	// gerar guias de recebimento de petições relativo a deslocamento interno (de um setor para outro dentro do STF)
	@Override
	public String criarRelatorioGuiaRecebimentoPeticoesNaPastaTemp(List<Guia> guias) throws ServiceException, IOException {
		byte[] resultRel = criarRelatorioGuiaDeslocamentoPeticao(guias);
		String nomeArq = "GUIA_DESLOCAMENTO_";
		String nomeArqFinal = gerarPDF(nomeArq, resultRel);
		return nomeArqFinal;
	}
	
	public String gerarPDF(String nomepdf, byte[] arrayByteRel) throws IOException{
	    File tempFile = File.createTempFile(nomepdf, TipoArquivo.PDF.getExtensoes());
		FileOutputStream fos = new FileOutputStream(tempFile);
		fos.write(arrayByteRel);
		fos.close();
		return tempFile.getAbsolutePath();
	}
	
	private void salvarPecaProcessual(byte[] pdf, ObjetoIncidente<?> objetoIncidente, Setor setor, AndamentoProcesso andamentoProcesso) throws ServiceException {
		String siglaTipoPeca = "";
		if (andamentoProcesso.getCodigoAndamento().equals(7104L)) {
			siglaTipoPeca = TipoPecaProcesso.SIGLA_TIPO_PECA_TERMO_BAIXA;
		} else if (andamentoProcesso.getCodigoAndamento().equals(7101L) || (andamentoProcesso.getCodigoAndamento().equals(7108L)) ) {
			siglaTipoPeca = TipoPecaProcesso.SIGLA_TIPO_PECA_TERMO_REMESSA;
		}
		if (siglaTipoPeca.equals("")) {
			throw new ServiceException("Não foi possível recuperar a sigla do tipo da peça.");
		}
		arquivoProcessoEletronicoService.salvarPecaEletronica(pdf, siglaTipoPeca, objetoIncidente, setor, andamentoProcesso);
	}
	
	@Override
	public byte[] criarCertidaoTransito(CertidaoTransitoDTO certidao) throws ServiceException {
		try {
			String relatorio = "relatorios/CertidaoTransito.jasper";
			String pathBrasao = recuperarPathImagens("images/brasao.gif");
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			
			params.put("PATH_BRASAO", pathBrasao);
			params.put("SUBREPORT_DIR", "relatorios/");
			params.put("PROCESSO", certidao.getProcesso());		
			params.put("CORPO", certidao.getCorpo());
			params.put("NOME_SERVIDOR", certidao.getNomeServidor());
			params.put("MAT_SERVIDOR", certidao.getMatServidor());
			params.put("LISTA_PARTES", certidao.getPartes());

			return UtilJasperReports.criarRelatorioPdf(relatorio, null, params);
						
		} catch (FileNotFoundException e) {
			throw new ServiceException(e);
		} catch (JRException e) {
			e.printStackTrace();
			throw new ServiceException("Ocorreu um erro ao gerar a Certidão de Trânsito!", e);
		}
	}
	
	private byte[] criarArquivoPdf(ArquivoPdfDTO pdfDTO) throws ServiceException {
		try {
			String relatorio = "relatorios/"+pdfDTO.getNomeArquivoPdf()+".jasper";
			String pathBrasao = recuperarPathImagens("images/brasao.gif");
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			
			params.put("PATH_BRASAO", pathBrasao);
            
			ClassLoader carregarImagem = Thread.currentThread().getContextClassLoader();
            params.put("brasaoRepublicaSTF", carregarImagem.getResource("relatorios/Brasao_Republica_STF.jpg").toString());
            
			params.put("SUBREPORT_DIR", "relatorios/");
			params.put("PROCESSO", pdfDTO.getProcesso());		
			params.put("CORPO", pdfDTO.getCorpo());
			params.put("NOME_FUNCAO_SECRETARIA", pdfDTO.getNomeFuncaoSecretaria());
			params.put("NOME_SECAO", pdfDTO.getNomeSecao());
			params.put("LISTA_PARTES", pdfDTO.getPartes());

			return UtilJasperReports.criarRelatorioPdf(relatorio, null, params);
						
		} catch (FileNotFoundException e) {
			throw new ServiceException(e);
		} catch (JRException e) {
			e.printStackTrace();
			throw new ServiceException("Ocorreu um erro ao gerar a contrarrazão!", e);
		}
	}	
	
	private ComunicacaoDocumentoResult geraInformacoesReferenteAndamentoPeca(InfoPecaVinculadoAndamentoDTO dto) throws ServiceException{
		if(dto.isDtoPreenchido()){
			DocumentoEletronico docElet = arquivoProcessoEletronicoService.inserirArquivoProcessoEletronicoPendenteJuntadaComTextoAndamentoProcesso(dto.getDoc(), dto.getSiglaTipoPecaProcesso(), dto.getObjetoIncidente(), dto.getSetorUsuario(), dto.getAndamentoProcesso(), dto.getDescricaoPeca());
			Comunicacao com = gerarComunicacao(dto.getUsuario(), dto.getTipoPecaProcesso(), dto.getSetorUsuario(), dto.getModeloComunicacao());			
			ComunicacaoIncidente ci = gerarComunicacaoIncidente(dto.getObjetoIncidente(), dto.getAndamentoProcesso(), com);			
			gerarFaseComunicacao(com);			
			gerarDeslocamentoComunicacao(com, dto.getSetorUsuario());			
			DocumentoComunicacao docCom = gerarDocumentoComunicacao(docElet, com);
						
			List<ComunicacaoIncidente> listaCI = new ArrayList<ComunicacaoIncidente>();
			listaCI.add(ci);
			com.setComunicacaoIncidente(listaCI);
			return new ComunicacaoDocumentoResult(com, docCom, Boolean.TRUE, null, null, null);
		}
		return null;
	}
	
	private DocumentoComunicacao gerarDocumentoComunicacao(DocumentoEletronico documentoEletronico, Comunicacao com) throws ServiceException {
		DocumentoComunicacao dc = new DocumentoComunicacao();			
		dc.setDocumentoEletronico(documentoEletronico);
		dc.setComunicacao(com);	
		dc.setTipoSituacaoDocumento(TipoSituacaoDocumento.GERADO);
		return documentoComunicacaoService.incluir(dc);
	}

	private void gerarDeslocamentoComunicacao(Comunicacao com, Setor setorUsuario) throws ServiceException {
		DeslocamentoComunicacao desl = new DeslocamentoComunicacao();
		desl.setComunicacao(com);
		desl.setSetor(setorUsuario);
		desl.setDataEntrada(new Date());
		deslocamentoComunicacaoService.incluir(desl);
	}

	private void gerarFaseComunicacao(Comunicacao com) throws ServiceException {
		List<FaseComunicacao> fases = new ArrayList<FaseComunicacao>();
		FaseComunicacao fc = new FaseComunicacao();
		fc.setComunicacao(com);
		fc.setDataLancamento(new Date());
		fc.setFlagFaseAtual(FlagFaseAtual.S);
		fc.setTipoFase(TipoFaseComunicacao.AGUARDANDO_ASSINATURA);
		fases.add(fc);
		com.setFases(fases);
		faseComunicacaoService.incluir(fc);
	}

	private ComunicacaoIncidente gerarComunicacaoIncidente(ObjetoIncidente<?> objetoIncidente, AndamentoProcesso andamentoProcesso, Comunicacao com) throws ServiceException {
		ComunicacaoIncidente ci = new ComunicacaoIncidente();
		ci.setComunicacao(com);
		ci.setObjetoIncidente(objetoIncidente);
		ci.setAndamentoProcesso(andamentoProcesso);
		ci.setTipoVinculo(FlagProcessoLote.P);
		return comunicacaoIncidenteService.incluir(ci);
	}

	private Comunicacao gerarComunicacao(Usuario usuario, TipoPecaProcesso tipoPecaProcesso, Setor setorUsuario, ModeloComunicacao modelo) throws ServiceException {		
		Comunicacao com = new Comunicacao();
		com.setSetor(setorUsuario);
		com.setUsuarioCriacao(usuario.getId());
		com.setDscNomeDocumento(tipoPecaProcesso.getDescricao());
		com.setDataEnvio(new Date());			
		com.setModeloComunicacao(modelo);
		comunicacaoService.incluir(com);
		return com;
	}	
	
	@Override
	public ComunicacaoDocumentoResult gerarPecaVinculadaAoAndamentoSelecionado(InfoPecaVinculadoAndamentoDTO infoPecaVinculadoAndamentoDTO) throws ServiceException, HandlerException {
		if(infoPecaVinculadoAndamentoDTO.getIdAndamentoSelecionado() != null){
			switch(infoPecaVinculadoAndamentoDTO.getIdAndamentoSelecionado().intValue()){
				case CODIGO_CERTIDAO_TRANSITO_JULGADO:
					return gerarCertidaoTransito(infoPecaVinculadoAndamentoDTO);
				case CODIGO_CONTRARRAZOES:
					return gerarContrarrazao(infoPecaVinculadoAndamentoDTO);
				case CODIGO_CONTRAMINUTA:
					return gerarContraminuta(infoPecaVinculadoAndamentoDTO);
				case CODIGO_CONTRAMINUTA_DECURSO_PRAZO:
					return gerarContraminutaDecursoPrazo(infoPecaVinculadoAndamentoDTO);
				case CODIGO_CONTRARRAZOES_DECURSO_PRAZO:
					return gerarContrarrazaoDecursoPrazo(infoPecaVinculadoAndamentoDTO);
				case CODIGO_APRESENTAR_RESPOSTA_DECURSO_PRAZO:
					return gerarApresentarRespostaDecursoPrazo(infoPecaVinculadoAndamentoDTO);
			}
		}
		return null;
	}
	
	private ComunicacaoDocumentoResult gerarContrarrazao(InfoPecaVinculadoAndamentoDTO infoPecaVinculadoAndamentoDTO) throws HandlerException, ServiceException {
		
		StringBuilder corpo = new StringBuilder();
		corpo.append("Nos termos do art. 1º, inciso XI, da Resolução 478/2011, a ");
		corpo.append(NOME_SECRETARIA);
		corpo.append(" abre vista para manifestação da parte agravada, na forma do art. 1.021, §2º, do Código de Processo Civil.");
		
		ArquivoPdfDTO pdfDTO = new ArquivoPdfDTO.Builder().processo(infoPecaVinculadoAndamentoDTO.getObjetoIncidente().getIdentificacaoCompleta())
																		.corpo(corpo.toString())
																		.nomeFuncaoSecretaria(NOME_FUNCAO_SECRETARIA)
																		.nomeSecao(NOME_SECAO)
																		.nomeArquivoPdf("Contrarrazao")
																		.partes(infoPecaVinculadoAndamentoDTO.getListaPartes()).builder();
		
		
		return criarDocumento(infoPecaVinculadoAndamentoDTO, pdfDTO);	
	}	
	
	private ComunicacaoDocumentoResult gerarContrarrazaoDecursoPrazo(InfoPecaVinculadoAndamentoDTO infoPecaVinculadoAndamentoDTO) throws HandlerException, ServiceException {
		
		StringBuilder corpo = new StringBuilder();
		corpo.append("Certifico que, até a presente data, não houve manifestação da parte agravada em relação ao despacho/ato ordinatório publicado, na forma do art. 1021, § 2º do Código de Processo Civil.");
		
		ArquivoPdfDTO pdfDTO = new ArquivoPdfDTO.Builder().processo(infoPecaVinculadoAndamentoDTO.getObjetoIncidente().getIdentificacaoCompleta())
																		.corpo(corpo.toString())
																		.nomeFuncaoSecretaria(NOME_FUNCAO_SECRETARIA)
																		.nomeSecao(NOME_SECAO)
																		.nomeArquivoPdf("ContrarrazaoDecursoPrazo")
																		.partes(infoPecaVinculadoAndamentoDTO.getListaPartes()).builder();
		
		
		return criarDocumento(infoPecaVinculadoAndamentoDTO, pdfDTO);	
	}

	private ComunicacaoDocumentoResult gerarApresentarRespostaDecursoPrazo(InfoPecaVinculadoAndamentoDTO infoPecaVinculadoAndamentoDTO) throws HandlerException, ServiceException {
		
		StringBuilder corpo = new StringBuilder();
		corpo.append("Certifico que, até a presente data, não houve manifestação da parte embargada em relação ao despacho/ato ordinatório publicado, na forma do art. 1023, § 2º do Código de Processo Civil.");
		
		ArquivoPdfDTO pdfDTO = new ArquivoPdfDTO.Builder().processo(infoPecaVinculadoAndamentoDTO.getObjetoIncidente().getIdentificacaoCompleta())
																		.corpo(corpo.toString())
																		.nomeFuncaoSecretaria(NOME_FUNCAO_SECRETARIA)
																		.nomeSecao(NOME_SECAO)
																		.nomeArquivoPdf("ContrarrazaoDecursoPrazo")
																		.partes(infoPecaVinculadoAndamentoDTO.getListaPartes()).builder();
		
		
		return criarDocumento(infoPecaVinculadoAndamentoDTO, pdfDTO);	
	}	
	
	private ComunicacaoDocumentoResult gerarContraminuta(InfoPecaVinculadoAndamentoDTO infoPecaVinculadoAndamentoDTO) throws HandlerException, ServiceException {

		StringBuilder corpo = new StringBuilder();
		corpo.append("De ordem, a ");
		corpo.append(NOME_SECRETARIA);
		corpo.append(" abre vista para manifestação da parte embargada, na forma do art. 1023, §2º, do Código de Processo Civil.");

		ArquivoPdfDTO pdfDTO = new ArquivoPdfDTO.Builder().processo(infoPecaVinculadoAndamentoDTO.getObjetoIncidente().getIdentificacaoCompleta())
				.corpo(corpo.toString())
				.nomeFuncaoSecretaria(NOME_FUNCAO_SECRETARIA)
				.nomeSecao(NOME_SECAO)
				.nomeArquivoPdf("Contraminuta")
				.partes(infoPecaVinculadoAndamentoDTO.getListaPartes()).builder();


		return criarDocumento(infoPecaVinculadoAndamentoDTO, pdfDTO);		
	}	

	private ComunicacaoDocumentoResult gerarContraminutaDecursoPrazo(InfoPecaVinculadoAndamentoDTO infoPecaVinculadoAndamentoDTO) throws HandlerException, ServiceException {

		StringBuilder corpo = new StringBuilder();
		corpo.append("Certifico que, até a presente data, não houve manifestação da parte embargada em relação ao despacho/ato ordinatório publicado, na forma do art. 1023, § 2º do Código de Processo Civil.");

		ArquivoPdfDTO pdfDTO = new ArquivoPdfDTO.Builder().processo(infoPecaVinculadoAndamentoDTO.getObjetoIncidente().getIdentificacaoCompleta())
				.corpo(corpo.toString())
				.nomeFuncaoSecretaria(NOME_FUNCAO_SECRETARIA)
				.nomeSecao(NOME_SECAO)
				.nomeArquivoPdf("Contraminuta")
				.partes(infoPecaVinculadoAndamentoDTO.getListaPartes()).builder();


		return criarDocumento(infoPecaVinculadoAndamentoDTO, pdfDTO);		
	}	

	private ComunicacaoDocumentoResult criarDocumento(InfoPecaVinculadoAndamentoDTO infoPecaVinculadoAndamentoDTO,	ArquivoPdfDTO pdfDTO) throws ServiceException {
		byte[] doc = criarArquivoPdf(pdfDTO);
	
		if(doc != null){
			InfoPecaVinculadoAndamentoDTO info = new InfoPecaVinculadoAndamentoDTO.Builder().setDoc(doc)
					.setUsuario(infoPecaVinculadoAndamentoDTO.getUsuario())
					.setObjetoIncidente(infoPecaVinculadoAndamentoDTO.getObjetoIncidente())
					.setAndamentoProcesso(infoPecaVinculadoAndamentoDTO.getAndamentoProcesso())
					.setTipoPecaProcesso(infoPecaVinculadoAndamentoDTO.getTipoPecaProcesso())
					.setSetorUsuario(infoPecaVinculadoAndamentoDTO.getSetorUsuario())
					.setSiglaTipoPecaProcesso(infoPecaVinculadoAndamentoDTO.getTipoPecaProcesso().getSigla())
					.setModeloComunicacao(infoPecaVinculadoAndamentoDTO.getModeloComunicacao())
					.setIdAndamentoSelecionado(infoPecaVinculadoAndamentoDTO.getIdAndamentoSelecionado())
					.setDescricaoPeca(infoPecaVinculadoAndamentoDTO.getDescricaoPeca())
					.builder();
			return geraInformacoesReferenteAndamentoPeca(info);
		}
		return null;
	}
	
	private ComunicacaoDocumentoResult gerarCertidaoTransito(InfoPecaVinculadoAndamentoDTO infoPecaVinculadoAndamentoDTO) throws HandlerException, ServiceException {
		
		StringBuilder corpo = new StringBuilder();
		corpo.append("Certifico que o(a) acórdão/decisão transitou em julgado em ");
		corpo.append(infoPecaVinculadoAndamentoDTO.getAndamentoProcesso().getObservacao());
		corpo.append(".");
		
		CertidaoTransitoDTO certidao = new CertidaoTransitoDTO.Builder().processo(infoPecaVinculadoAndamentoDTO.getObjetoIncidente().getIdentificacaoCompleta())
																		.corpo(corpo.toString())
																		.nomeServidor(infoPecaVinculadoAndamentoDTO.getUsuario().getNome())
																		.matServidor(infoPecaVinculadoAndamentoDTO.getUsuario().getMatricula())
																		.partes(infoPecaVinculadoAndamentoDTO.getListaPartes()).builder();
		
		
		byte[] doc = criarCertidaoTransito(certidao);
		
		if(doc != null){
			InfoPecaVinculadoAndamentoDTO info = new InfoPecaVinculadoAndamentoDTO.Builder().setDoc(doc)
																			  .setUsuario(infoPecaVinculadoAndamentoDTO.getUsuario())
																			  .setObjetoIncidente(infoPecaVinculadoAndamentoDTO.getObjetoIncidente())
																			  .setAndamentoProcesso(infoPecaVinculadoAndamentoDTO.getAndamentoProcesso())
																			  .setTipoPecaProcesso(infoPecaVinculadoAndamentoDTO.getTipoPecaProcesso())
																			  .setSetorUsuario(infoPecaVinculadoAndamentoDTO.getSetorUsuario())
																			  .setSiglaTipoPecaProcesso(infoPecaVinculadoAndamentoDTO.getTipoPecaProcesso().getSigla())
																			  .setModeloComunicacao(infoPecaVinculadoAndamentoDTO.getModeloComunicacao())
																			  .setIdAndamentoSelecionado(infoPecaVinculadoAndamentoDTO.getIdAndamentoSelecionado())
																			  .setDescricaoPeca(infoPecaVinculadoAndamentoDTO.getDescricaoPeca())
																			  .builder();
			return geraInformacoesReferenteAndamentoPeca(info);
		}
		return null;	
	}	
	public byte[] gerarPdfComunicacaoDeVista(ObjetoIncidente<?> objetoIncidente, Setor setor, String destinatario ) throws ServiceException{
    	
    	StringBuilder corpo = new StringBuilder();
        corpo.append("De ordem, a ");
        corpo.append(NOME_SECRETARIA);
        corpo.append(" faz remessa destes autos com vista ao Excelentíssimo Senhor " + destinatario);
        Processo processo = (Processo) objetoIncidente.getPrincipal();
        if (processo.getClasseProcessual().getId().equals(Classe.SIGLA_RECURSO_HABEAS_CORPUS))
            corpo.append(", nos termos do artigo 311 do Regimento Interno do Supremo Tribunal Federal");

        corpo.append(".");

        ArquivoPdfDTO pdfDTO = new ArquivoPdfDTO.Builder().processo(processo.getSiglaClasseProcessual()+" "+ processo.getNumeroProcessual())
                .corpo(corpo.toString())
                .nomeFuncaoSecretaria(NOME_FUNCAO_SECRETARIA)
                .nomeSecao(setor.getNome())
                .nomeArquivoPdf("termoDeVista")
                .partes(null).builder();

        byte[] arquivoEletronico = criarArquivoPdf(pdfDTO);
    	
    	
    	return arquivoEletronico;
    }

	public byte[] gerarPdfComunicacaoAutosDisp(ObjetoIncidente<?> objetoIncidente, Setor setor, String destinatario ) throws ServiceException{
    	
    	StringBuilder corpo = new StringBuilder();
        corpo.append("A ");
        corpo.append(NOME_SECRETARIA);
        corpo.append(" informa que, nesta data, os autos foram disponibilizados à autoridade policial.");
        Processo processo = (Processo) objetoIncidente.getPrincipal();
        

        ArquivoPdfDTO pdfDTO = new ArquivoPdfDTO.Builder().processo(processo.getSiglaClasseProcessual()+" "+ processo.getNumeroProcessual())
                .corpo(corpo.toString())
                .nomeFuncaoSecretaria(NOME_FUNCAO_SECRETARIA)
                .nomeSecao(setor.getNome())
                .nomeArquivoPdf("termoAutosDisponibilizados")
                .partes(null).builder();

        byte[] arquivoEletronico = criarArquivoPdf(pdfDTO);
    	
    	
    	return arquivoEletronico;
    }
	
}
