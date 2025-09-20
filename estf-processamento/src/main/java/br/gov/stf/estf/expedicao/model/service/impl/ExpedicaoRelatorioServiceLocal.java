package br.gov.stf.estf.expedicao.model.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jfree.util.Log;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.expedicao.entidade.DestinatarioListaRemessa;
import br.gov.stf.estf.expedicao.entidade.ListaRemessa;
import br.gov.stf.estf.expedicao.entidade.Remessa;
import br.gov.stf.estf.expedicao.entidade.RemessaVolume;
import br.gov.stf.estf.expedicao.entidade.Remetente;
import br.gov.stf.estf.expedicao.entidade.TipoServico;
import br.gov.stf.estf.expedicao.model.service.ExpedicaoRelatorioService;
import br.gov.stf.estf.expedicao.model.util.ArDTO;
import br.gov.stf.estf.expedicao.model.util.CartaDTO;
import br.gov.stf.estf.expedicao.model.util.DataMatrix;
import br.gov.stf.estf.expedicao.model.util.ListaCorreiosDTO;
import br.gov.stf.estf.expedicao.model.util.ListaMaloteDTO;
import br.gov.stf.estf.expedicao.model.util.ListaPortariaDTO;
import br.gov.stf.estf.expedicao.model.util.RelatorioRemessaDTO;
import br.gov.stf.estf.expedicao.model.util.TipoServicoCodigoEnum;
import br.gov.stf.estf.expedicao.model.util.TipoServicoEnum;
import br.gov.stf.estf.expedicao.visao.Util;
import br.gov.stf.estf.expedicao.visao.vo.EtiquetaProcessoVo;
import br.jus.stf.util.jasperreports.UtilJasperReports;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

@Service("relatorioService")
public class ExpedicaoRelatorioServiceLocal implements ExpedicaoRelatorioService {

	private static final String RETORNO = "\n";
	private static final String VIRGULA = ",";
	private static final String PONTO_VIRGULA = "; ";

	private String recuperarPathImagens(String uriImagem) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		return cl.getResource(uriImagem).toString();
	}

	public byte[] Junta(JasperPrint rel1, JasperPrint rel2) throws Exception {
		List<JasperPrint> listaRelatorios = new ArrayList<JasperPrint>();

		listaRelatorios.add(rel1);
		listaRelatorios.add(rel2);

		JRPdfExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, listaRelatorios);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
		exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "ISO-8859-1");
		exporter.exportReport();

		return outputStream.toByteArray();
	}

	public JasperPrint criarRelatorioJasperPrint(String nomeDoArquivoDoRelatorio,
			Collection<?> dados,
			Map<String, Object> parametros) throws FileNotFoundException,
			JRException {
		JasperPrint jasperPrint = montaRelatorioComCollection(
				nomeDoArquivoDoRelatorio, dados, parametros);
		return jasperPrint;
	}

	public static byte[] criarRelatorioPdf(String nomeDoArquivoDoRelatorio,
			Collection<?> dados, Map<String, Object> parametros)
			throws FileNotFoundException, JRException {
		JasperPrint jasperPrint = montaRelatorioComCollection(nomeDoArquivoDoRelatorio, dados, parametros);
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}

	/**
	 * @param nomeDoArquivoDoRelatorio
	 * @param dados
	 * @param parametros
	 * @return baos.toByteArray
	 */
	public static byte[] criarRelatorioXls(String nomeDoArquivoDoRelatorio,
			Collection<?> dados,
			Map<String, Object> parametros) {
		JasperPrint jasperPrint = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			jasperPrint = montaRelatorioComCollection(nomeDoArquivoDoRelatorio, dados, parametros);
			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS,Boolean.FALSE);
			exporter.exportReport();
		} catch (JRException e) {
			Log.warn("Falha ao exportar relatorio para XLS. " + e.getMessage());
		} catch (FileNotFoundException e) {
			Log.error("Erro ao montar jasperPrint para exportacao em XLS. " + e.getMessage());
		}
		return baos.toByteArray();
	}

	private static JasperPrint montaRelatorioComCollection(
			String nomeDoArquivoDoRelatorio, Collection<?> dados,
			Map<String, Object> parametros) throws FileNotFoundException, JRException {
		InputStream arquivoJasperCompilado = getArquivoDoRelatorioCompilado(nomeDoArquivoDoRelatorio);
		parametros = getParametrosPadronizados(parametros);
		JRDataSource dataSource = getDadosPadronizados(dados);
		JasperPrint jasperPrint = JasperFillManager.fillReport(
				arquivoJasperCompilado, parametros, dataSource);
		return jasperPrint;
	}

	private static InputStream getArquivoDoRelatorioCompilado(String nomeDoArquivo) throws FileNotFoundException {
		InputStream arquivoJasperCompilado = Thread.currentThread().getContextClassLoader().getResourceAsStream(nomeDoArquivo);
		if (arquivoJasperCompilado != null) {
			return arquivoJasperCompilado;
		}
		throw new FileNotFoundException("Não foi possível encontrar o arquivo " + nomeDoArquivo + " no classpath!");
	}

	private static Map<String, Object> getParametrosPadronizados(Map<String, Object> parametros) {
		if (parametros == null) {
			parametros = new HashMap<String, Object>();
		}
		return parametros;
	}

	private static JRDataSource getDadosPadronizados(Collection<?> dados) {
		if (dados == null) {
			return new JREmptyDataSource();
		}
		return new JRBeanCollectionDataSource(dados);
	}

	/**
	 *
	 * @param colecao
	 * @param nomeRelatorio
	 * @return
	 */
	public byte[] criarRelatorioExcel(Collection<?> colecao, int qtdRegistros, String nomeRelatorio){
		String relatorio = "relatorios/" + nomeRelatorio + ".jasper";
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		List<RelatorioRemessaDTO> conjuntoRemessa = new ArrayList<RelatorioRemessaDTO>();

		for(Object obj : colecao){
			if (obj instanceof Remessa) {
				int qtdRegistrosVolume = 0;
				List<RelatorioRemessaDTO> remessasDtoRemessa = RelatorioRemessaDTO.criar((Remessa) obj);
				conjuntoRemessa.addAll(remessasDtoRemessa);
				if(conjuntoRemessa.size() > 0){
					qtdRegistrosVolume = conjuntoRemessa.size();
					colecao = conjuntoRemessa;
				}
				parametros.put("QTD_REGISTROS_VOLUME", qtdRegistrosVolume);
			}else if(obj instanceof DestinatarioListaRemessa){
				/*
				 * Logica para geração do relatorio de Destinatário AQUI
				 */
				break;
			}
		}

		parametros.put("QTD_REGISTROS", qtdRegistros);
		return criarRelatorioXls(relatorio, colecao, parametros);
	}

	public byte[] criarEtiquetasCorreio(ListaRemessa listaRemessa,
			String cartaoPostagem,
			String numeroContrato,
			int posicaoEtiquetas,
			Remetente remetente) throws Exception {
		if (listaRemessa.getTipoServico().getId()
				.equals(TipoServicoCodigoEnum.PAC.getCodigo())) {
			return criarEtiquetasDeQuatro(listaRemessa, "pac_v2", "pac_v2",
					cartaoPostagem, numeroContrato, posicaoEtiquetas, remetente);
		} else if (listaRemessa.getTipoServico().getId()
				.equals(TipoServicoCodigoEnum.SEDEX.getCodigo())) {
			return criarEtiquetasDeQuatro(listaRemessa, "sedex_v2", "rel_sedex_v2",
					cartaoPostagem, numeroContrato, posicaoEtiquetas, remetente);
		} else if (listaRemessa.getTipoServico().getId()
				.equals(TipoServicoCodigoEnum.SEDEX10.getCodigo())) {
			return criarEtiquetasDeQuatro(listaRemessa, "sedex10_v2",
					"rel_sedex10_v2", cartaoPostagem, numeroContrato,
					posicaoEtiquetas, remetente);
		} else if (listaRemessa.getTipoServico().getId()
				.equals(TipoServicoCodigoEnum.CARTA.getCodigo())) {
			return criarEtiquetasDeOito(listaRemessa, "carta", "rel_carta",
					numeroContrato, posicaoEtiquetas, numeroContrato, remetente);
		} else {
			return null;
		}
	}

	public byte[] criarEtiquetasCorreioIndividual(Remessa remessa,
			String cartaoPostagem,
			String numeroContrato,
			int posicaoEtiquetas,
			Remetente remetente)
			throws Exception {
		if (remessa.getListaRemessa().getTipoServico()
				.getId()
				.equals(TipoServicoCodigoEnum.PAC.getCodigo())) {
			return criarEtiquetasDeQuatroIndividual(remessa, "pac_v2", "pac_v2",
					cartaoPostagem, numeroContrato, posicaoEtiquetas, remetente);
		} else if (remessa.getListaRemessa().getTipoServico()
				.getId()
				.equals(TipoServicoCodigoEnum.SEDEX.getCodigo())) {
			return criarEtiquetasDeQuatroIndividual(remessa, "sedex_v2",
					"rel_sedex_v2", cartaoPostagem, numeroContrato,
					posicaoEtiquetas, remetente);
		} else if (remessa.getListaRemessa().getTipoServico()
				.getId()
				.equals(TipoServicoCodigoEnum.SEDEX10.getCodigo())) {
			return criarEtiquetasDeQuatroIndividual(remessa, "sedex10_v2",
					"rel_sedex10_v2", cartaoPostagem, numeroContrato,
					posicaoEtiquetas, remetente);
		} else if (remessa.getListaRemessa().getTipoServico()
				.getId()
				.equals(TipoServicoCodigoEnum.CARTA.getCodigo())) {
			return criarEtiquetasDeOitoIndividual(remessa, "carta",
					"rel_carta", numeroContrato, posicaoEtiquetas, remetente, cartaoPostagem);
		} else {
			return null;
		}
	}

	private byte[] criarEtiquetasDeOito(ListaRemessa listaRemessa,
			String logo,
			String nomeRelatorio,
			String numeroContrato,
			int posicaoEtiquetas,
			String cartaoPostagem,
			Remetente remetente) throws Exception {
		String relatorio = "relatorios/" + nomeRelatorio + ".jasper";
		List<CartaDTO> dados = deParaObjetosEtiquetaCorreios(listaRemessa, posicaoEtiquetas, remetente, cartaoPostagem);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("LOGO_REMETENTE", recuperarPathImagens("relatorios/logo_supremo.png"));
		params.put("LOGO", recuperarPathImagens("relatorios/" + logo + ".png"));
		params.put("NUMERO_CONTRATO", numeroContrato);
		return UtilJasperReports.criarRelatorioPdf(relatorio, dados, params);
	}

	private byte[] criarEtiquetasDeQuatro(ListaRemessa listaRemessa,
			String logo, String nomeRelatorio, String cartaoPostagem,
			String numeroContrato, int posicaoEtiquetas, Remetente remetente) throws Exception {
		String relatorio = "relatorios/" + nomeRelatorio + ".jasper";
		List<CartaDTO> dados = deParaObjetosEtiquetaCorreios(listaRemessa, posicaoEtiquetas, remetente, cartaoPostagem);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("LOGO_REMETENTE", recuperarPathImagens("relatorios/logo_supremo.png"));
		params.put("LOGO", recuperarPathImagens("relatorios/" + logo + ".png"));
		params.put("NUMERO_CONTRATO", numeroContrato);
		params.put("CARTAO_POSTAGEM", cartaoPostagem);
		params.put("UNIDADE_CORREIOS", listaRemessa.getUnidadePostagem().getNomeUnidadePostagem());
		params.put("REMETENTE", geraRemetente(remetente));
		return UtilJasperReports.criarRelatorioPdf(relatorio, dados, params);
	}

	private byte[] criarEtiquetasDeQuatroIndividual(Remessa remessa,
			String logo, String nomeRelatorio, String cartaoPostagem,
			String numeroContrato, int posicaoEtiquetas, Remetente remetente) throws Exception {
		String relatorio = "relatorios/" + nomeRelatorio + ".jasper";
		List<CartaDTO> dados = deParaObjetosEtiquetaCorreiosIndividual(remessa,
				remessa.getListaRemessa().getDataCriacao(),
				posicaoEtiquetas, remetente, cartaoPostagem);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("LOGO_REMETENTE", recuperarPathImagens("relatorios/logo_supremo.png"));
		params.put("LOGO", recuperarPathImagens("relatorios/" + logo + ".png"));
		params.put("NUMERO_CONTRATO", numeroContrato);
		params.put("CARTAO_POSTAGEM", cartaoPostagem);
		params.put("UNIDADE_CORREIOS", remessa.getListaRemessa().getUnidadePostagem().getNomeUnidadePostagem());
		params.put("REMETENTE", geraRemetente(remetente));
		return UtilJasperReports.criarRelatorioPdf(relatorio, dados, params);
	}

	private byte[] criarEtiquetasDeOitoIndividual(Remessa remessa, String logo,
			String nomeRelatorio, String numeroContrato, int posicaoEtiquetas, Remetente remetente, String cartaoPostagem)
			throws Exception {
		String relatorio = "relatorios/" + nomeRelatorio + ".jasper";
		List<CartaDTO> dados = deParaObjetosEtiquetaCorreiosIndividual(remessa,
				remessa.getListaRemessa().getDataCriacao(),
				posicaoEtiquetas,remetente, cartaoPostagem);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("LOGO_REMETENTE", recuperarPathImagens("relatorios/logo_supremo.png"));
		params.put("LOGO", recuperarPathImagens("relatorios/" + logo + ".png"));
		params.put("NUMERO_CONTRATO", numeroContrato);
		return UtilJasperReports.criarRelatorioPdf(relatorio, dados, params);
	}

	public byte[] criarEtiquetasPortaria(ListaRemessa listaRemessa,
			int posicaoEtiquetas) throws Exception {
		String relatorio = "relatorios/rel_portaria.jasper";
		List<CartaDTO> dados = deParaObjetosEtiquetaPortaria(listaRemessa,
				posicaoEtiquetas);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("LOGO_SUPREMO", recuperarPathImagens("relatorios/logo_supremo.png"));
		return UtilJasperReports.criarRelatorioPdf(relatorio, dados, params);
	}

	public byte[] criarEtiquetasPortariaIndividual(Remessa remessa,
			int posicaoEtiquetas) throws Exception {
		String relatorio = "relatorios/rel_portaria.jasper";
		List<CartaDTO> dados = deParaObjetosEtiquetaPortariaIndividual(remessa,
				remessa.getListaRemessa().getDataCriacao(),
				posicaoEtiquetas);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("LOGO_SUPREMO", recuperarPathImagens("relatorios/logo_supremo.png"));
		return UtilJasperReports.criarRelatorioPdf(relatorio, dados, params);
	}

	public byte[] criarEtiquetasProcesso(List<EtiquetaProcessoVo> dados, String siglasNomeUsuario) throws FileNotFoundException, JRException{
		String relatorio = "relatorios/etiqueta_processo.jasper";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("DATA_ATUAL", Util.dataExtenso(new Date()));
		params.put("INICIAIS_USUARIO", siglasNomeUsuario.trim());
		return UtilJasperReports.criarRelatorioPdf(relatorio, dados, params);
	}

	private List<CartaDTO> deParaObjetosEtiquetaPortaria(ListaRemessa listaRemessa, int posicaoEtiquetas) {
		List<CartaDTO> lista = new ArrayList<CartaDTO>();

		for (int i = 0; i < posicaoEtiquetas - 1; i++) {
			lista.add(new CartaDTO(false));
		}

		if (listaRemessa.getRemessas() != null) {
			for (Remessa remessa : listaRemessa.getRemessas()) {
				boolean maoPropria = isRemesssaMaoPropria(remessa);
				if (remessa.getVolumes() != null) {
					for (RemessaVolume volume : remessa.getVolumes()) {
						lista.add(new CartaDTO(
								null,
								getDataFormatada(listaRemessa.getDataCriacao()),
								geraDestinatario(remessa),
								geraEndereco(remessa), false, false,
								setaEspacoBrancoEmNuloEmString(volume
										.getPesoGramas().toString()), null,
								geraConteudo(remessa), null, true, maoPropria));
					}
				}
			}
		}
		return lista;
	}

	private List<CartaDTO> deParaObjetosEtiquetaPortariaIndividual(
			Remessa remessa, Date dataCriacao, int posicaoEtiquetas) {
		List<CartaDTO> lista = new ArrayList<CartaDTO>();

		for (int i = 0; i < posicaoEtiquetas - 1; i++) {
			lista.add(new CartaDTO(false));
		}

		boolean maoPropria = isRemesssaMaoPropria(remessa);
		
		if (remessa.getVolumes() != null) {
			for (RemessaVolume volume : remessa.getVolumes()) {
				lista.add(new CartaDTO(null, getDataFormatada(dataCriacao),
						geraDestinatario(remessa), geraEndereco(remessa),
						false, false, setaEspacoBrancoEmNuloEmString(volume
								.getPesoGramas().toString()), null,
						geraConteudo(remessa), null, true, maoPropria));
			}
		}
		return lista;
	}
	
	private boolean isRemesssaMaoPropria(Remessa remessa){
		for(TipoServico tipoServico : remessa.getTiposServicoNaoObrigatorios()){
			if (tipoServico.getId().equals(TipoServicoCodigoEnum.MAO_PROPRIA.getCodigo()))
				return true;
		}
		return false;
	}

	private List<CartaDTO> deParaObjetosEtiquetaCorreios(ListaRemessa listaRemessa, int posicaoEtiquetas,
			Remetente remetente, String cartaPostagem) {
		List<CartaDTO> lista = new ArrayList<CartaDTO>();
		for (int i = 0; i < posicaoEtiquetas - 1; i++) {
			lista.add(new CartaDTO(false));
		}

		if (listaRemessa.getRemessas() != null) {
			for (Remessa remessa : listaRemessa.getRemessas()) {
				boolean maoPropria = isRemesssaMaoPropria(remessa);

				int numVolumes = remessa.getVolumes().size();
				int i = 0;

				if (remessa.getVolumes() != null) {

					for (RemessaVolume volume : remessa.getVolumes()) {
						
						DataMatrix dm = getDataMatrix(remessa, remetente, volume.getNumeroEtiquetaCorreios(), cartaPostagem);
						
						lista.add(new CartaDTO(
								++i + "/" + numVolumes,
								getDataFormatada(listaRemessa.getDataCriacao()),
								geraDestinatario(remessa),
								geraEndereco(remessa), isRN(remessa),
								isAR(remessa),
								setaEspacoBrancoEmNuloEmString(volume
										.getPesoGramas().toString()),
								setaNuloEmStringVazia(volume
										.getNumeroEtiquetaCorreios()),
								geraConteudo(remessa), remessa.getCep(), true,
								maoPropria, dm.toString()));
					}
				}
			}
		}
		return lista;
	}

	private List<CartaDTO> deParaObjetosEtiquetaCorreiosIndividual(Remessa remessa, Date dataCriacao, int posicaoEtiquetas, 
			                                                      Remetente remetente, String cartaPostagem) {
		List<CartaDTO> lista = new ArrayList<CartaDTO>();
		for (int i = 0; i < posicaoEtiquetas - 1; i++) {
			lista.add(new CartaDTO(false));
		}
		
		int numVolumes = remessa.getVolumes().size();
		int i = 0;
		boolean maoPropria = isRemesssaMaoPropria(remessa);
		
		if (remessa.getVolumes() != null) {
			for (RemessaVolume volume : remessa.getVolumes()) {
				
				DataMatrix dm = getDataMatrix(remessa, remetente, volume.getNumeroEtiquetaCorreios(), cartaPostagem);
				
				lista.add(new CartaDTO(++i + "/" + numVolumes,
						getDataFormatada(dataCriacao),
						geraDestinatario(remessa),
						geraEndereco(remessa),
						isRN(remessa),
						isAR(remessa),
						setaEspacoBrancoEmNuloEmString(volume.getPesoGramas().toString()),
						setaNuloEmStringVazia(volume.getNumeroEtiquetaCorreios()),
						geraConteudo(remessa),
						remessa.getCep(),
						true, maoPropria, dm.toString()));
			}
		}
		return lista;
	}
	
	private DataMatrix getDataMatrix(Remessa remessa, Remetente remetente, String codigoRastreador, String cartaPostagem){
		DataMatrix dataMatrix = new DataMatrix();
		
		dataMatrix.setCepDestino(remessa.getCep());
		dataMatrix.setCepOrigem(remetente.getCep());
		dataMatrix.setIDV(String.valueOf(TipoServicoEnum.MALOTE.getValor()));
		dataMatrix.setCodigoRastreamento(codigoRastreador);
		
		String servicosAdicionais = (isRemesssaMaoPropria(remessa)?TipoServicoCodigoEnum.MAO_PROPRIA.getSigla():"00") + 
		                            (isAR(remessa)?TipoServicoCodigoEnum.AR.getSigla():"00");
		dataMatrix.setServicosAdicionais(servicosAdicionais);
		dataMatrix.setCartaoDePostagem(cartaPostagem);
		dataMatrix.setInformacaoAgrupamento(remessa.getAgrupador());
		
		dataMatrix.setNumeroLogradouro(remessa.getNumero());
		dataMatrix.setComplementoLogradouro(remessa.getComplemento());
		
		dataMatrix.setTelefoneCompletoDestinatario(remessa.getNumeroTelefone());
		
		return dataMatrix;
		
	}

	public byte[] criarListaMalote(ListaRemessa listaRemessa) throws Exception {
		String relatorio = "relatorios/rel_lista_malote.jasper";

		List<ListaMaloteDTO> dados = deParaObjetosMalote(listaRemessa);

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("NUMERO_LISTA", listaRemessa.getNumeroListaRemessa());
		params.put("DATA_FORMATADA", getDataAtual());
		params.put("DATA_CRIACAO_LISTA", getDataFormatada(listaRemessa.getDataCriacao()));
		params.put("DATA_EXTENSO", getDataAtualExtenso());
		params.put("LOGO_SUPREMO", recuperarPathImagens("relatorios/logo_supremo.png"));
		return UtilJasperReports.criarRelatorioPdf(relatorio, dados, params);
	}

	public byte[] criarListaPortaria(ListaRemessa listaRemessa, String userName)
			throws Exception {
		String relatorio = "relatorios/rel_lista_portaria.jasper";

		List<ListaPortariaDTO> dados = deParaObjetosListaPortaria(listaRemessa);

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("NUMERO_LISTA", listaRemessa.getNumeroListaRemessa());
		params.put("DATA_FORMATADA", getDataAtual());
		params.put("DATA_CRIACAO_LISTA", getDataFormatada(listaRemessa.getDataCriacao()));
		params.put("NOME_USUARIO", userName);
		params.put("LOGO_SUPREMO", recuperarPathImagens("relatorios/logo_supremo.png"));
		return UtilJasperReports.criarRelatorioPdf(relatorio, dados, params);
	}

	public byte[] criarListaCorreios(ListaRemessa listaRemessa,
			String userName, String cartaoPostagem, String numContrato,
			String codigoAdministrativo, Remetente remetente)
			throws Exception {
		List<Remessa> remessaComAR = retornaRemessasComAR(listaRemessa
				.getRemessas());

		if (remessaComAR.size() > 0) {
			JasperPrint jasperPrint1 = criarListaCorreiosInterno(listaRemessa,
					userName, cartaoPostagem, numContrato,
					codigoAdministrativo, remetente.getCep(), remetente.getNomeRemetente());
			JasperPrint jasperPrint2 = criarRelatorioGeracaoAR(remessaComAR,
					numContrato, remetente);
			return Junta(jasperPrint1, jasperPrint2);
		} else {
			return criarListaCorreiosByte(listaRemessa, userName,
					cartaoPostagem, numContrato, codigoAdministrativo, remetente.getCep(), remetente.getNomeRemetente());
		}
	}

	private boolean isAR(Remessa remessa) {
		boolean retorno = false;
		for (TipoServico tipoServico : remessa.getTiposServico()) {
			if (tipoServico.getId().equals(
					TipoServicoCodigoEnum.AR.getCodigo())) {
				retorno = true;
			}
		}
		return retorno;
	}

	private boolean isRN(Remessa remessa) {
		boolean retorno = false;
		for (TipoServico tipoServico : remessa.getTiposServico()) {
			if (tipoServico.getId().equals(
					TipoServicoCodigoEnum.RN.getCodigo())) {
				retorno = true;
			}
		}
		return retorno;
	}

	private List<Remessa> retornaRemessasComAR(List<Remessa> remessas) {
		List<Remessa> remessaComAR = new ArrayList<Remessa>();
		for (Remessa remessa : remessas) {
			for (TipoServico tipoServico : remessa.getTiposServico()) {
				if (tipoServico.getId().equals(
						TipoServicoCodigoEnum.AR.getCodigo())) {
					remessaComAR.add(remessa);
				}
			}
		}
		return remessaComAR;
	}

	private byte[] criarListaCorreiosByte(ListaRemessa listaRemessa,
			String userName, String cartaoPostagem, String numContrato,
			String codigoAdministrativo, String numCep, String nome)
			throws Exception {
		String relatorio = "relatorios/rel_lista_correios.jasper";

		List<ListaCorreiosDTO> dados = deParaObjetosListaCorreios(listaRemessa);

		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("UNIDADE_POSTAGEM", listaRemessa.getUnidadePostagem()
				.getNomeUnidadePostagem());
		params.put("DATA_POSTAGEM",
				getDataFormatada(listaRemessa.getDataCriacao()));
		params.put("NUMERO_LISTA", listaRemessa.getNumeroListaRemessa());
		params.put("CODIGO_ADMINISTRATIVO", codigoAdministrativo);
		params.put("CLIENTE", nome);
		params.put("CONTRATO", numContrato);
		params.put("CEP", numCep);
		params.put("DATA_FORMATADA", getDataAtual());
		params.put("USUARIO", userName);
		params.put("CARTAO_POSTAGEM", cartaoPostagem);
		params.put("LOGO_SUPREMO",
				recuperarPathImagens("relatorios/logo_supremo.png"));
		return UtilJasperReports.criarRelatorioPdf(relatorio, dados, params);

	}

	private JasperPrint criarListaCorreiosInterno(ListaRemessa listaRemessa,
			String userName, String cartaoPostagem, String numContrato,
			String codigoAdministrativo, String numCep, String nome)
			throws Exception {
		String relatorio = "relatorios/rel_lista_correios.jasper";

		List<ListaCorreiosDTO> dados = deParaObjetosListaCorreios(listaRemessa);

		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("UNIDADE_POSTAGEM", listaRemessa.getUnidadePostagem()
				.getNomeUnidadePostagem());
		params.put("DATA_POSTAGEM",
				getDataFormatada(listaRemessa.getDataCriacao()));
		params.put("NUMERO_LISTA", listaRemessa.getNumeroListaRemessa());
		params.put("CODIGO_ADMINISTRATIVO", codigoAdministrativo);
		params.put("CLIENTE", nome);
		params.put("CONTRATO", numContrato);
		params.put("CEP", numCep);
		params.put("DATA_FORMATADA", getDataAtual());
		params.put("USUARIO", userName);
		params.put("CARTAO_POSTAGEM", cartaoPostagem);
		params.put("LOGO_SUPREMO",
				recuperarPathImagens("relatorios/logo_supremo.png"));
		JasperPrint relatoriojasperPrint = criarRelatorioJasperPrint(relatorio,
				dados, params);
		return relatoriojasperPrint;
	}

	private JasperPrint criarRelatorioGeracaoAR(List<Remessa> remessas,
			String numContrato, Remetente remetente) throws Exception {

		String relatorio = "relatorios/rel_ar.jasper";
		List<ArDTO> dados = deParaObjetosAR(remessas);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("NUM_CONTRATO", numContrato);
		params.put("LOGO_CORREIOS",
				recuperarPathImagens("relatorios/logo_correios.png"));
		params.put("REMETENTE", geraRemetente(remetente));
		JasperPrint relatoriojasperPrint = criarRelatorioJasperPrint(relatorio,
				dados, params);
		return relatoriojasperPrint;
	}

	private List<ListaCorreiosDTO> deParaObjetosListaCorreios(ListaRemessa listaRemessa) {
		List<ListaCorreiosDTO> lista = new ArrayList<ListaCorreiosDTO>();
		if (listaRemessa.getRemessas() != null) {
			for (Remessa remessa : listaRemessa.getRemessas()) {
				int numVolumes = remessa.getVolumes().size();
				int i = 0;

				boolean maoPropriaNacional = isRemesssaMaoPropria(remessa);
			
				String numPLP = null;
				if (remessa.getNumeroPlpCorreios() != null) {
					numPLP = remessa.getNumeroPlpCorreios().toString();
				}

				if (remessa.getVolumes() != null) {
					for (RemessaVolume volume : remessa.getVolumes()) {
						lista.add(
								new ListaCorreiosDTO(
										geraDestinatario(remessa),
										setaEspacoBrancoEmNuloEmString(remessa.getCep()),
										geraConteudo(remessa),
										setaEspacoBrancoEmNuloEmString(volume.getNumeroEtiquetaCorreios()),
										geraTipoServico(listaRemessa),
										geraServicoAdicional(remessa),
										setaEspacoBrancoEmNuloEmString(volume.getPesoGramas().toString()), ++i + "/" + numVolumes,
										numPLP, maoPropriaNacional));
					}
				}
			}
		}
		return lista;
	}

	private List<ListaPortariaDTO> deParaObjetosListaPortaria(
			ListaRemessa listaRemessa) {
		List<ListaPortariaDTO> lista = new ArrayList<ListaPortariaDTO>();
		if (listaRemessa.getRemessas() != null) {
			for (Remessa remessa : listaRemessa.getRemessas()) {
				StringBuilder listasRemessa = new StringBuilder("");
				for(ListaRemessa r2 : remessa.getListasEnviadas()){
					listasRemessa.append(r2.getNumeroListaRemessaAnoFormato()).append(", ");
				}
				boolean maoPropriaNacional = isRemesssaMaoPropria(remessa);
				if (remessa.getVolumes() != null) {
					for (RemessaVolume volume : remessa.getVolumes()) {
						lista.add(
								new ListaPortariaDTO(
								geraDestinatario(remessa),
								setaEspacoBrancoEmNuloEmString(volume.getPesoGramas().toString()),
								geraConteudo(remessa),
								(listasRemessa.length() > 0 ? (listasRemessa.substring(0, listasRemessa.length() - 2)) : listasRemessa.toString()),
								maoPropriaNacional)
								);
					}
				}
			}
		}
		return lista;
	}

	private List<ListaMaloteDTO> deParaObjetosMalote(ListaRemessa listaRemessa) {
		List<ListaMaloteDTO> lista = new ArrayList<ListaMaloteDTO>();
		if (listaRemessa.getRemessas() != null) {
			int i = 0;
			for (Remessa remessa : listaRemessa.getRemessas()) {
				StringBuilder listasRemessa = new StringBuilder("");

				for(ListaRemessa r2 : remessa.getListasEnviadas()){
					listasRemessa.append(r2.getNumeroListaRemessaAnoFormato()).append(", ");
				}
				boolean maoPropriaNacional = isRemesssaMaoPropria(remessa);
				lista.add(new ListaMaloteDTO(++i + "",
						setaEspacoBrancoEmNuloEmString(remessa
								.getDescricaoPrincipal()),
						setaEspacoBrancoEmNuloEmString(remessa.getMalote()),
						setaEspacoBrancoEmNuloEmString(remessa.getLacre()),
						setaEspacoBrancoEmNuloEmString(remessa.getGuiaDeslocamento()),
						setaEspacoBrancoEmNuloEmString(remessa.getObservacao()),
						(listasRemessa.length() > 0 ? (listasRemessa.substring(0, listasRemessa.length() - 2)) : listasRemessa.toString()),
						maoPropriaNacional));
			}
		}
		return lista;
	}

	private List<ArDTO> deParaObjetosAR(List<Remessa> remessas) {
		List<ArDTO> lista = new ArrayList<ArDTO>();
		if (remessas != null) {
			for (Remessa remessa : remessas) {
				int numVolumes = remessa.getVolumes().size();
				int i = 0;
				boolean maoPropriaNacional = isRemesssaMaoPropria(remessa);
				
				if (remessa.getVolumes() != null) {
					for (RemessaVolume volume : remessa.getVolumes()) {
						lista.add(

								new ArDTO(
										geraDestinatarioCompleto(remessa),
										geraConteudo(remessa),
										setaNuloEmStringVazia(volume.getNumeroEtiquetaCorreios()),
										setaEspacoBrancoEmNuloEmString(++i + "/" + numVolumes),
										maoPropriaNacional
										)

								);
					}
				}
			}
		}
		return lista;
	}

	private String geraServicoAdicional(Remessa remessa) {
		StringBuffer retorno = new StringBuffer();
		String virgula = "";
		for (TipoServico tipoServico : remessa.getTiposServico()) {
			if (tipoServico.getId().equals(
					TipoServicoCodigoEnum.AR.getCodigo())) {
				retorno.append(virgula + "AR");
				virgula = ", ";
			} else if (tipoServico.getId().equals(
					TipoServicoCodigoEnum.MAO_PROPRIA.getCodigo())) {
				retorno.append(virgula + "MÃO PRÓPRIA");
				virgula = ", ";
			}
		}
		return retorno.toString();
	}

	private String geraTipoServico(ListaRemessa listaRemessa) {
		return listaRemessa.getTipoServico().getCodigoServicoCorreios() + "-"
				+ listaRemessa.getTipoServico().getNome();
	}

	private String geraDestinatarioCompleto(Remessa remessa) {
		String retorno = "";
		retorno = retorno
				+ colocaRetorno(setaEspacoBrancoEmNuloEmString(remessa
						.getDescricaoAnterior()));
		retorno = retorno
				+ colocaRetorno(setaEspacoBrancoEmNuloEmString(remessa
						.getDescricaoPrincipal()));
		retorno = retorno
				+ colocaRetorno(setaEspacoBrancoEmNuloEmString(remessa
						.getDescricaoPosterior()));
		retorno = retorno
				+ setaEspacoBrancoEmNuloEmString(remessa.getLogradouro())
				+ VIRGULA + setaEspacoBrancoEmNuloEmString(remessa.getNumero())
				+ VIRGULA
				+ setaEspacoBrancoEmNuloEmString(remessa.getComplemento())
				+ RETORNO;
		retorno = retorno + setaEspacoBrancoEmNuloEmString(remessa.getBairro())
				+ RETORNO;
		retorno = retorno + setaEspacoBrancoEmNuloEmString(remessa.getCep())
				+ "   " + setaEspacoBrancoEmNuloEmString(remessa.getCidade())
				+ "-" + setaEspacoBrancoEmNuloEmString(remessa.getUf());
		return retorno;
	}

	private String geraEndereco(Remessa remessa) {
		String retorno = "";
		retorno = retorno
				+ setaEspacoBrancoEmNuloEmString(remessa.getLogradouro())
				+ VIRGULA + setaEspacoBrancoEmNuloEmString(remessa.getNumero())
				+ VIRGULA
				+ setaEspacoBrancoEmNuloEmString(remessa.getComplemento())
				+ RETORNO;
		retorno = retorno + setaEspacoBrancoEmNuloEmString(remessa.getBairro())
				+ RETORNO;
		retorno = retorno + setaEspacoBrancoEmNuloEmString(remessa.getCep())
				+ "   " + setaEspacoBrancoEmNuloEmString(remessa.getCidade())
				+ "-" + setaEspacoBrancoEmNuloEmString(remessa.getUf());
		return retorno;
	}

	private String geraDestinatario(Remessa remessa) {
		String retorno = "";
		retorno = retorno
				+ colocaRetorno(setaEspacoBrancoEmNuloEmString(remessa
						.getDescricaoAnterior()));
		retorno = retorno
				+ colocaRetorno(setaEspacoBrancoEmNuloEmString(remessa
						.getDescricaoPrincipal()));
		retorno = retorno
				+ colocaRetorno(setaEspacoBrancoEmNuloEmString(remessa
						.getDescricaoPosterior()));
		return retorno;
	}

	private String geraConteudo(Remessa remessa) {
		String retorno = "";
		if (remessa.getTipoComunicacao() != null) {
			retorno = retorno + setaEspacoBrancoEmNuloEmString(remessa.getTipoComunicacao().getDescricao()) + " ";
		}
		retorno = retorno + colocaPontoVirgula(setaEspacoBrancoEmNuloEmString(remessa.getNumeroComunicacao()));
		retorno = retorno + colocaTipo(colocaPontoVirgula(setaEspacoBrancoEmNuloEmString(remessa.getVinculo())), "");

		StringBuilder listasRemessa = new StringBuilder("");

		for(ListaRemessa r2 : remessa.getListasEnviadas()){
			listasRemessa.append(r2.getNumeroListaRemessaAnoFormato()).append(", ");
		}

		retorno = retorno + colocaTipo(colocaPontoVirgula(setaEspacoBrancoEmNuloEmString(remessa.getGuiaDeslocamento())), "Guia(s)");
		retorno = retorno + colocaTipo(colocaPontoVirgula((listasRemessa.length() > 0 ? (listasRemessa.substring(0, listasRemessa.length() - 2)) : listasRemessa.toString())), "Lista(s)");
		retorno = retorno + colocaTipo(setaEspacoBrancoEmNuloEmString(remessa.getObservacao()), "Obs.");
		return retorno;
	}
	
	private String geraRemetente(Remetente remetente){
		StringBuilder retorno = new StringBuilder();
		retorno.delete(0, retorno.length());
		retorno.append(remetente.getNomeRemetente() + "\n");
		retorno.append(remetente.getLogradouro() + "\n");
		retorno.append((remetente.getComplemento()==null ? "" : remetente.getComplemento() + ", "));
		retorno.append(remetente.getNumero().equals("0") ? "S/N\n" : remetente.getNumero() + "\n");
		retorno.append(remetente.getBairro() + "\n");
		retorno.append(remetente.getCep() + " ");
		retorno.append(remetente.getMunicipio().getNome() + "-");
		retorno.append(remetente.getMunicipio().getSiglaUf());
		return retorno.toString();
	}

	private String colocaTipo(String campo, String tipo) {
		if (campo != null && !campo.equals("")) {
			return tipo + " " + campo;
		} else {
			return campo;
		}
	}

	private String colocaPontoVirgula(String campo) {
		String retorno = "";
		if (campo != null && !campo.equals("")) {
			retorno = campo + PONTO_VIRGULA;
		}
		return retorno;
	}

	private String colocaRetorno(String campo) {
		if (campo != null && !campo.equals("")) {
			return campo + RETORNO;
		} else {
			return campo;
		}
	}

	private String setaEspacoBrancoEmNuloEmString(String campo) {
		if (campo == null) {
			return "";
		} else {
			return campo;
		}
	}

	private String setaNuloEmStringVazia(String campo) {
		if (campo != null && campo.equals("")) {
			return null;
		} else {
			return campo;
		}
	}

	private String getDataAtual() {
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		Date dataAtual = new Date(System.currentTimeMillis());
		return sd.format(dataAtual);
	}

	private String getDataFormatada(Date data) {
		String retorno = "";
		if (data != null) {
			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
			retorno = sd.format(data);
		}
		return retorno;
	}

	private String getDataAtualExtenso() {
		Date dataAtual = new Date();
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL,
				new Locale("pt", "BR"));
		String dataExtenso = formatador.format(dataAtual);
		int index = dataExtenso.indexOf(",");
		int lenght = dataExtenso.length();
		return "Brasília, " + dataExtenso.substring(++index, lenght);
	}
}