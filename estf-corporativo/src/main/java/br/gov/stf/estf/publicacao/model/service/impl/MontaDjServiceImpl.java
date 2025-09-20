package br.gov.stf.estf.publicacao.model.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleConstants.ColorConstants;
import javax.swing.text.rtf.RTFEditorKit;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.service.ArquivoEletronicoService;
import br.gov.stf.estf.documento.model.service.TextoPeticaoService;
import br.gov.stf.estf.documento.model.service.TextoProtocoloService;
import br.gov.stf.estf.documento.model.service.TextoService;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TextoPeticao;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.AcordaoAgendado;
import br.gov.stf.estf.entidade.processostf.Assunto;
import br.gov.stf.estf.entidade.processostf.IncidenteDistribuicao;
import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProtocoloPublicado;
import br.gov.stf.estf.entidade.processostf.TextoAssociadoProtocolo;
import br.gov.stf.estf.entidade.processostf.TextoAssociadoProtocolo.TipoAssociacao;
import br.gov.stf.estf.entidade.processostf.TipoIncidenteJulgamento;
import br.gov.stf.estf.entidade.publicacao.AtaDistribuicao;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;
import br.gov.stf.estf.entidade.publicacao.TipoModoDistribuicao;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.estf.processostf.model.service.AcordaoAgendadoService;
import br.gov.stf.estf.processostf.model.service.AssuntoProcessoService;
import br.gov.stf.estf.processostf.model.service.IncidenteDistribuicaoService;
import br.gov.stf.estf.processostf.model.service.ProtocoloPublicadoService;
import br.gov.stf.estf.processostf.model.service.TextoAssociadoProtocoloService;
import br.gov.stf.estf.publicacao.compordj.builder.BuilderHelper;
import br.gov.stf.estf.publicacao.compordj.builder.Constantes;
import br.gov.stf.estf.publicacao.compordj.util.ProcessoDjComparator;
import br.gov.stf.estf.publicacao.model.service.AtaDistribuicaoService;
import br.gov.stf.estf.publicacao.model.service.ConteudoPublicacaoService;
import br.gov.stf.estf.publicacao.model.service.MontaDjService;
import br.gov.stf.estf.publicacao.model.service.ProcessoPublicadoService;
import br.gov.stf.estf.publicacao.model.util.OrdinalUtil;
import br.gov.stf.estf.publicacao.util.NomesDJE;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.Mensagens;

@Service("montaDjService")
public class MontaDjServiceImpl implements MontaDjService {
	public static final Long TEXTO_ABERTURA_SESSAO_ESPECIAL = 190L;
	public static final Long TEXTO_FECHAMENTO_SESSAO_ESPECIAL = 191L;
	public static final Long TEXTO_ABERTURA_SESSAO_ESPECIAL_ADPF = 192L;

	private final ConteudoPublicacaoService conteudoPublicacaoService;
	// private final IncidenteDistribuicaoService distribuicaoService;
	private final AtaDistribuicaoService ataDistribuicaoService;
	private final ProcessoPublicadoService processoPublicadoService;
	private final TextoService textoService;
	private final TextoPeticaoService textoPeticaoService;
	private final TextoProtocoloService textoProtocoloService;
	private final AcordaoAgendadoService acordaoAgendadoService;
	private final TextoAssociadoProtocoloService textoAssociadoProtocoloService;
	private final ProtocoloPublicadoService protocoloPublicadoService;
	private final ArquivoEletronicoService arquivoEletronicoService;
	private final AssuntoProcessoService assuntoProcessoService;
	private final MinistroService ministroService;

	public MontaDjServiceImpl(
			ConteudoPublicacaoService conteudoPublicacaoService,
			IncidenteDistribuicaoService distribuicaoService,
			AtaDistribuicaoService ataDistribuicaoService,
			ProcessoPublicadoService processoPublicadoService,
			TextoService textoService, TextoPeticaoService textoPeticaoService,
			TextoProtocoloService textoProtocoloService,
			AcordaoAgendadoService acordaoAgendadoService,
			TextoAssociadoProtocoloService textoAssociadoProtocoloService,
			ProtocoloPublicadoService protocoloPublicadoService,
			ArquivoEletronicoService arquivoEletronicoService,
			AssuntoProcessoService assuntoProcessoService,
			MinistroService ministroService) {
		super();
		this.conteudoPublicacaoService = conteudoPublicacaoService;
		// this.distribuicaoService = distribuicaoService;
		this.ataDistribuicaoService = ataDistribuicaoService;
		this.processoPublicadoService = processoPublicadoService;
		this.textoService = textoService;
		this.textoPeticaoService = textoPeticaoService;
		this.textoProtocoloService = textoProtocoloService;
		this.acordaoAgendadoService = acordaoAgendadoService;
		this.textoAssociadoProtocoloService = textoAssociadoProtocoloService;
		this.protocoloPublicadoService = protocoloPublicadoService;
		this.arquivoEletronicoService = arquivoEletronicoService;
		this.assuntoProcessoService = assuntoProcessoService;
		this.ministroService = ministroService;
	}

	/**
	 * Comparator para ordenar: ProcessoPublicado, AcordaoAgendado,
	 * Distribuicao, TextoPeticao, ProtocoloPublicado
	 */
	private ProcessoDjComparator processoDjComparator = new ProcessoDjComparator();

	public List<IncidenteDistribuicao> pesquisarRelacaoProcessoDistribuido(
			boolean ordenar, ConteudoPublicacao conteudoPublicacao)
			throws ServiceException {
		AtaDistribuicao ataDistribuicao = ataDistribuicaoService.recuperar(
				conteudoPublicacao.getNumero(), conteudoPublicacao
						.getTipoSessao(), conteudoPublicacao
						.getDataComposicaoParcial());

		// List<IncidenteDistribuicao> distribuicoes =
		// ataDistribuicao.getDistribuicoes();
		List<IncidenteDistribuicao> distribuicoes = ataDistribuicaoService
				.pesquisarIncidenteDistribuicao(ataDistribuicao, Boolean.FALSE);

		// ORDENA OS PROCESSOS PELA SUA DESCRICAO
		if (ordenar) {
			Collections.sort(distribuicoes, processoDjComparator);
		}

		return distribuicoes;

	}

	public List<ProcessoPublicado> pesquisarRelacaoProcessoPublicado(
			boolean ordenar, ConteudoPublicacao conteudoPublicacao)
			throws ServiceException {
		List<ProcessoPublicado> listaProcessos = processoPublicadoService
				.pesquisarProcessosDj(conteudoPublicacao.getCodigoCapitulo(),
						conteudoPublicacao.getCodigoMateria(),
						conteudoPublicacao.getAno(), conteudoPublicacao
								.getNumero(),
								Boolean.FALSE);

		// ORDENA OS PROCESSOS PELA SUA DESCRICAO
		if (ordenar) {
			Collections.sort(listaProcessos, processoDjComparator);
		}

		return listaProcessos;
	}

	public byte[] recuperarTextoAberturaDistribuicao(
			ConteudoPublicacao conteudoPublicacao) throws ServiceException {
		Mensagens mensagens = new Mensagens(
				Constantes.DISTRIBUICAO_TEXTO_PROPERTIES);
		AtaDistribuicao ataDistribuicao = ataDistribuicaoService.recuperar(
				conteudoPublicacao.getNumero(), conteudoPublicacao
						.getTipoSessao(), conteudoPublicacao
						.getDataComposicaoParcial());

		// StringBuffer texto = new StringBuffer();
		String[] parametros = null;
		byte[] array = null;

		try {
			if (ataDistribuicao.getTipoModoDistribuicao() == TipoModoDistribuicao.MANUAL) {
				parametros = new String[6];
				parametros[0] = OrdinalUtil.ordinal(ataDistribuicao
						.getNumeroAta());
				parametros[1] = mensagens.getMensagem("tipoDistribuicao"
						+ ataDistribuicao.getTipoSessao().getSigla());
				parametros[2] = BuilderHelper.getDataAtual(ataDistribuicao
						.getDataAta());
				parametros[3] = mensagens
						.getMensagem("pronome"
								+ ataDistribuicao.getMinistroPresidente()
										.getTipoSexo());
				parametros[4] = ataDistribuicao.getMinistroPresidente()
						.getNome().toUpperCase();
				parametros[5] = mensagens
						.getMensagem(ataDistribuicao.getMinistroPresidente()
								.getId().equals(new Integer(1)) ? Constantes.ART66
								: Constantes.ART33);
				// texto.append(mensagens.getMensagem("textoDistribuicaoAberturaMA",
				// parametros));
				array = montarTextoAberturaManual(parametros[0], parametros[1],
						parametros[2], parametros[3], parametros[4],
						parametros[5]);
			} else {
				parametros = new String[2];
				parametros[0] = OrdinalUtil.ordinal(ataDistribuicao
						.getNumeroAta());
				parametros[1] = BuilderHelper.getDataAtual(ataDistribuicao
						.getDataAta());
				// texto.append(mensagens.getMensagem("textoDistribuicaoAberturaAU",
				// parametros));
				array = montarTextoAberturaAutomatica(parametros[0],
						parametros[1]);
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return array;
	}

	public byte[] recuperarTextoAberturaDistribuicaoPresidente(
			ConteudoPublicacao conteudoPublicacao) throws ServiceException {
		AtaDistribuicao ataDistribuicao = ataDistribuicaoService.recuperar(
				conteudoPublicacao.getNumero(), conteudoPublicacao
						.getTipoSessao(), conteudoPublicacao
						.getDataComposicaoParcial());
		Date dataAta = ataDistribuicao.getDataAta();
		byte[] texto = null;
		try {
			RTFEditorKit rtfEdidor = new RTFEditorKit();
			javax.swing.text.Document doc = rtfEdidor.createDefaultDocument();
			doc.insertString(0,
					"\tRelação dos processos registrados à Presidência, em "
							+ BuilderHelper.getDataAtual(dataAta) + ":", null);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			rtfEdidor.write(baos, doc, 0, doc.getLength());
			texto = baos.toByteArray();
		} catch (Exception e) {
			throw new ServiceException(
					"Erro ao montar texto de abertura da distribuição", e);
		}
		return texto;
	}

	public byte[] recuperarTextoAberturaSessaoEspecial()
			throws ServiceException {
		return arquivoEletronicoService.recuperarArquivoEletronico(
				TEXTO_ABERTURA_SESSAO_ESPECIAL).getConteudo();
	}

	public byte[] recuperarTextoFechamentoSessaoEspecial()
			throws ServiceException {
		return arquivoEletronicoService.recuperarArquivoEletronico(
				TEXTO_FECHAMENTO_SESSAO_ESPECIAL).getConteudo();
	}

	public byte[] recuperarTextoAberturaSessaoEspecialADPF()
			throws ServiceException {
		return arquivoEletronicoService.recuperarArquivoEletronico(
				TEXTO_ABERTURA_SESSAO_ESPECIAL_ADPF).getConteudo();
	}

	/**
	 * Cria o texto de fechamento da distribuicao
	 */
	public byte[] recuperarTextoFechamentoDistribuicao(
			ConteudoPublicacao conteudoPublicacao) throws ServiceException {
		AtaDistribuicao ataDistribuicao = ataDistribuicaoService.recuperar(
				conteudoPublicacao.getNumero(), conteudoPublicacao
						.getTipoSessao(), conteudoPublicacao
						.getDataComposicaoParcial());

		Mensagens mensagens = new Mensagens(
				Constantes.DISTRIBUICAO_TEXTO_PROPERTIES);

		// StringBuffer texto = new StringBuffer();
		byte[] texto = null;
		String[] parametros = null;
		try {
			if (ataDistribuicao.getTipoModoDistribuicao() == TipoModoDistribuicao.MANUAL) {
				parametros = new String[4];
				parametros[0] = mensagens.getMensagem("cidade");
				parametros[1] = BuilderHelper.getDataAtual(ataDistribuicao
						.getDataAta());
				parametros[2] = ataDistribuicao.getMinistroPresidente()
						.getNome().toUpperCase();
				parametros[3] = null;
				if (ataDistribuicao.getMinistroPresidente().getId().equals(
						new Integer(1))) {
					parametros[3] = mensagens.getMensagem(Constantes.ART33);
				}
				// texto.append(mensagens.getMensagem("textoDistribuicaoFechamentoMA",
				// parametros));
				texto = montarTextoFechamentoManual(parametros[1],
						parametros[2], parametros[3]);
			} else {
				parametros = new String[2];
				parametros[0] = mensagens.getMensagem("cidade");
				parametros[1] = BuilderHelper.getDataAtual(ataDistribuicao
						.getDataAta());
				// texto.append(mensagens.getMensagem("textoDistribuicaoFechamentoAU",
				// parametros));
				texto = montarTextoFechamentoAutomatica(parametros[1]);
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}

		return texto;
	}

	private byte[] montarTextoAberturaManual(String ordinal,
			String tipoDistribuicao, String dataAtual, String proNome,
			String nomeMinistro, String artigo) throws BadLocationException,
			IOException {
		RTFEditorKit rtfEdidor = new RTFEditorKit();
		javax.swing.text.Document doc = rtfEdidor.createDefaultDocument();
		MutableAttributeSet m = new SimpleAttributeSet();
		m.addAttribute(ColorConstants.Bold, true);

		// {0} Audiência de Distribuição {2}, realizada em {3}, {4} \b {5} }
		// {6}.\par

		doc.insertString(0, "\tAta da  ", null);
		doc.insertString(doc.getLength(), ordinal, null);
		doc.insertString(doc.getLength(), " Audiência de Distribuição ", null);
		doc.insertString(doc.getLength(), tipoDistribuicao, null);
		doc.insertString(doc.getLength(), ", realizada em ", null);
		doc.insertString(doc.getLength(), dataAtual, null);
		doc.insertString(doc.getLength(), ", ", null);
		doc.insertString(doc.getLength(), proNome, null);
		doc.insertString(doc.getLength(), " ", null);
		doc.insertString(doc.getLength(), nomeMinistro, m);
		doc.insertString(doc.getLength(), " ", null);
		doc.insertString(doc.getLength(), artigo, null);
		doc.insertString(doc.getLength(), ".\n", null);
		doc
				.insertString(
						doc.getLength(),
						"\tForam distribuídos os seguintes feitos, pelo sistema de processamento de dados:\n",
						null);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		rtfEdidor.write(baos, doc, 0, doc.getLength());
		return baos.toByteArray();
	}

	private byte[] montarTextoAberturaAutomatica(String ordinal,
			String dataAtual) throws BadLocationException, IOException {
		RTFEditorKit rtfEdidor = new RTFEditorKit();
		javax.swing.text.Document doc = rtfEdidor.createDefaultDocument();
		MutableAttributeSet m = new SimpleAttributeSet();
		m.addAttribute(ColorConstants.Bold, true);

		doc.insertString(0, "\tAta da  ", null);
		doc.insertString(doc.getLength(), ordinal, null);
		doc.insertString(doc.getLength(), " Distribuição realizada em ", null);
		doc.insertString(doc.getLength(), dataAtual, null);
		doc.insertString(doc.getLength(), ".\n", null);
		doc
				.insertString(
						doc.getLength(),
						"\tForam distribuídos os seguintes feitos, pelo sistema de processamento de dados:\n",
						null);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		rtfEdidor.write(baos, doc, 0, doc.getLength());
		return baos.toByteArray();
	}

	private byte[] montarTextoFechamentoManual(String dataAtual,
			String nomeMinistro, String artigo) throws BadLocationException,
			IOException {
		RTFEditorKit rtfEdidor = new RTFEditorKit();

		javax.swing.text.Document doc = rtfEdidor.createDefaultDocument();

		MutableAttributeSet negrito = new SimpleAttributeSet();
		negrito.addAttribute(ColorConstants.Bold, true);

		MutableAttributeSet centralizado = new SimpleAttributeSet();
		centralizado.addAttribute(StyleConstants.Alignment,
				StyleConstants.ALIGN_CENTER);

		MutableAttributeSet negEcent = new SimpleAttributeSet();
		negEcent.addAttribute(StyleConstants.Alignment,
				StyleConstants.ALIGN_CENTER);
		negEcent.addAttribute(ColorConstants.Bold, true);

		doc
				.insertString(
						0,
						"\tNada mais havendo, foi encerrada a presente Ata de Distribuição. ",
						null);
		doc.insertString(doc.getLength(), 
				NomesDJE.TITULAR_COORDENADORIA_PROCESSAMENTO_INICIAL, negrito); 
		doc.insertString(doc.getLength(),
				", " + NomesDJE.CARGO_COORDENADORIA_PROCESSAMENTO_INICIAL + ", ", null);
		doc.insertString(doc.getLength(), 
				NomesDJE.TITULAR_SECRETARIA_JUDICIARIA, negrito);
		doc.insertString(doc.getLength(), ", " + NomesDJE.CARGO_SECRETARIA_JUDICIARIA + ".\n", null);

		doc.insertString(doc.getLength(), "Brasília, ", centralizado);
		doc.insertString(doc.getLength(), dataAtual, centralizado);
		doc.insertString(doc.getLength(), ".\n", centralizado);
		doc.insertString(doc.getLength(), nomeMinistro, negEcent);
		doc.insertString(doc.getLength(), "\nPresidente\n", centralizado);
		if (artigo != null) {
			doc.insertString(doc.getLength(), artigo, centralizado);
			doc.insertString(doc.getLength(), "\n", centralizado);
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		rtfEdidor.write(baos, doc, 0, doc.getLength());
		return baos.toByteArray();
	}

	private byte[] montarTextoFechamentoAutomatica(String dataAtual)
			throws BadLocationException, IOException {
		RTFEditorKit rtfEdidor = new RTFEditorKit();
		javax.swing.text.Document doc = rtfEdidor.createDefaultDocument();

		MutableAttributeSet negrito = new SimpleAttributeSet();
		negrito.addAttribute(ColorConstants.Bold, true);

		MutableAttributeSet centralizado = new SimpleAttributeSet();
		centralizado.addAttribute(StyleConstants.Alignment,
				StyleConstants.ALIGN_CENTER);

		MutableAttributeSet negEcent = new SimpleAttributeSet();
		negEcent.addAttribute(StyleConstants.Alignment,
				StyleConstants.ALIGN_CENTER);
		negEcent.addAttribute(ColorConstants.Bold, true);

		doc
				.insertString(
						0,
						"\tNada mais havendo, foi encerrada a presente Ata de Distribuição. ",
						null);
		doc.insertString(doc.getLength(), NomesDJE.TITULAR_COORDENADORIA_PROCESSAMENTO_INICIAL,
				negrito);
		doc.insertString(doc.getLength(),
				", " + NomesDJE.CARGO_COORDENADORIA_PROCESSAMENTO_INICIAL + ", ", null);
		doc.insertString(doc.getLength(), NomesDJE.TITULAR_SECRETARIA_JUDICIARIA,
				negrito);
		doc.insertString(doc.getLength(), ", " + NomesDJE.CARGO_SECRETARIA_JUDICIARIA + ".\n", null);

		doc.insertString(doc.getLength(), "Brasília, ", centralizado);
		doc.insertString(doc.getLength(), dataAtual, centralizado);
		doc.insertString(doc.getLength(), ".\n", centralizado);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		rtfEdidor.write(baos, doc, 0, doc.getLength());
		return baos.toByteArray();
	}

	public Texto recuperarTextoDecisaoProcessoPublicadoAta(
			ProcessoPublicado processoPublicado) throws ServiceException {
		Date dataSessao = conteudoPublicacaoService.recuperarDataAta(
				processoPublicado.getCodigoCapitulo(), processoPublicado
						.getCodigoMateria(), processoPublicado
						.getNumeroMateria(), processoPublicado.getAnoMateria());
		Texto texto = textoService.recuperarDecisaoAta(processoPublicado
				.getObjetoIncidente(), dataSessao);

		return texto;
	}

	public List<Texto> pesquisarTextoDecisaoProcessoPublicado(
			ProcessoPublicado processoPublicado, Date dataSessao)
			throws ServiceException {

		List<Texto> textos = textoService.pesquisarTextosPublicacao(
				processoPublicado.getObjetoIncidente(), TipoTexto.DECISAO,
				true, dataSessao);

		return textos;
	}

	public List<Texto> pesquisarTextoEditalProcessoPublicado(
			ProcessoPublicado processoPublicado, Date dataSessao)
			throws ServiceException {
		List<Texto> textos = textoService.pesquisarTextosPublicacao(
				processoPublicado.getObjetoIncidente(),
				TipoTexto.EDITAL_PROPOSTA_SUMULA_VINCULANTE, true, dataSessao);

		return textos;
	}

	public List<Texto> pesquisarRelacaoTextoDecisaoAcordaoAgendado(
			AcordaoAgendado acordaoAgendado, Date dataSessao)
			throws ServiceException {
		List<Texto> textos = textoService.pesquisarTextosPublicacao(
				acordaoAgendado.getObjetoIncidente(), TipoTexto.DECISAO, true,
				dataSessao);

		return textos;
	}

	public List<Texto> pesquisarTextoDecisaoRepercussaoGeral(
			ProcessoPublicado processoPublicado, Date dataSessao)
			throws ServiceException {
		TipoTexto tipoTexto = null;
		TipoIncidenteJulgamento tipoJulgamento = null;
		ObjetoIncidente<?> oi = processoPublicado.getObjetoIncidente();
		if (oi instanceof IncidenteJulgamento) {
			tipoJulgamento = ((IncidenteJulgamento) oi).getTipoJulgamento();
		}

		if (TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL
				.equalsIgnoreCase(tipoJulgamento.getSigla())) {
			if (isRGQO(oi)) {
				//Alterado em 16/12/2009 - Demétrius Jubé
				// O tipo de Julgamento é RG-QO, nesse caso, deve retornar o
				// texto de decisão
				tipoTexto = TipoTexto.DECISAO;
			} else {

			tipoTexto = TipoTexto.DECISAO_SOBRE_REPERCURSAO_GERAL;
			}
		} else if (TipoIncidenteJulgamento.SIGLA_QUESTAO_ORDEM
				.equalsIgnoreCase(tipoJulgamento.getSigla())) {
			tipoTexto = TipoTexto.DECISAO;
		} else {
			return null;
		}

		List<Texto> textos = textoService.pesquisarTextosPublicacao(
				processoPublicado.getObjetoIncidente(), tipoTexto, true,
				dataSessao);

		return textos;
	}

	public Texto recuperarTextoEmentaProcessoPublicado(
			ProcessoPublicado processoPublicado) throws ServiceException {

		Texto texto = textoService.recuperarTextoPublicacao(processoPublicado
				.getObjetoIncidente(), TipoTexto.EMENTA);

		return texto;
	}

	public Texto recuperarTextoEmentaAcordaoAgendado(
			AcordaoAgendado acordaoAgendado) throws ServiceException {
		Texto texto = textoService.recuperarTextoPublicacao(acordaoAgendado
				.getObjetoIncidente(), TipoTexto.EMENTA);

		return texto;
	}

	public Texto recuperarTextoEmentaRepercussaoGeral(
			ProcessoPublicado processoPublicado) throws ServiceException {
		TipoTexto tipoTexto = null;
		ObjetoIncidente<?> oi = processoPublicado.getObjetoIncidente();
		TipoIncidenteJulgamento tipoJulgamento = recuperaTipoDeJulgamentoDoObjetoIncidente(oi);

		if (TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL
				.equalsIgnoreCase(tipoJulgamento.getSigla())) {
			if (isRGQO(oi)) {
				//Alterado em 16/12/2009 - Demétrius Jubé
				//O tipo de Julgamento é RG-QO, nesse caso, deve retornar o
				// texto de ementa
				tipoTexto = TipoTexto.EMENTA;
			} else {
				tipoTexto = TipoTexto.EMENTA_SOBRE_REPERCURSAO_GERAL;
			}
		} else if (TipoIncidenteJulgamento.SIGLA_QUESTAO_ORDEM
				.equalsIgnoreCase(tipoJulgamento.getSigla())) {
			tipoTexto = TipoTexto.EMENTA;
		} else {
			return null;
		}

		Texto texto = textoService.recuperarTextoPublicacao(processoPublicado
				.getObjetoIncidente(), tipoTexto);

		return texto;
	}
	
	private boolean isRGQO(ObjetoIncidente<?> objetoIncidente){
		TipoIncidenteJulgamento tipoJulgamentoPai = recuperaTipoDeJulgamentoDoObjetoIncidente(objetoIncidente.getPai());
		return TipoIncidenteJulgamento.SIGLA_QUESTAO_ORDEM
				.equalsIgnoreCase(tipoJulgamentoPai.getSigla());
	
	}

	private TipoIncidenteJulgamento recuperaTipoDeJulgamentoDoObjetoIncidente(
			ObjetoIncidente<?> oi) {
		TipoIncidenteJulgamento tipoJulgamento = new TipoIncidenteJulgamento();
		if (oi instanceof IncidenteJulgamento) {
			tipoJulgamento = ((IncidenteJulgamento) oi).getTipoJulgamento();
		}
		return tipoJulgamento;
	}

	public List<ProtocoloPublicado> pesquisarRelacaoProtocoloPublicado(
			boolean ordenar, ConteudoPublicacao conteudoPublicacao)
			throws ServiceException {
		List<ProtocoloPublicado> protocolos = protocoloPublicadoService
				.pesquisar(conteudoPublicacao, Boolean.FALSE);

		if (ordenar) {
			Collections.sort(protocolos, processoDjComparator);
		}

		return protocolos;
	}

	public TextoAssociadoProtocolo recuperarTextoObservacaoProtocoloPublicado(
			ProtocoloPublicado protocoloPublicado) throws ServiceException {
		return textoAssociadoProtocoloService.recuperar(protocoloPublicado,
				TipoAssociacao.O);
	}

	public TextoAssociadoProtocolo recuperarTextoRepublicacaoProtocoloPublicado(
			ProtocoloPublicado protocoloPublicado) throws ServiceException {
		return textoAssociadoProtocoloService.recuperar(protocoloPublicado,
				TipoAssociacao.R);
	}

	public List<AcordaoAgendado> pesquisarAcordaosSessaoEspecial(
			boolean ordenar, String... siglaClasseProcessual)
			throws ServiceException {
		List<AcordaoAgendado> processos = acordaoAgendadoService
				.pesquisarSessaoEspecial(Boolean.FALSE, siglaClasseProcessual);

		// ORDENA OS PROCESSOS PELA SUA DESCRICAO
		if (ordenar) {
			Collections.sort(processos, processoDjComparator);
		}

		return processos;
	}

	public List<ProcessoPublicado> pesquisarProcessosSessaoEspecial(
			boolean ordenar, String... siglaClasseProcessual)
			throws ServiceException {
		List<ProcessoPublicado> processos = processoPublicadoService
				.pesquisarSessaoEspecial(Boolean.FALSE, siglaClasseProcessual);
		if (ordenar) {
			Collections.sort(processos, processoDjComparator);
		}
		return processos;
	}

	public List<TextoPeticao> pesquisarTextoPeticao(boolean ordenar,
			ConteudoPublicacao conteudoPublicacao) throws ServiceException {
		List<TextoPeticao> peticoes = textoPeticaoService.pesquisar(
				conteudoPublicacao.getNumero(), conteudoPublicacao.getAno());

		// ORDENA AS PETICOES PELO NUMERO E ANO
		if (ordenar) {
			Collections.sort(peticoes, processoDjComparator);
		}

		return peticoes;
	}

	public List<TextoPeticao> pesquisarTextoProtocolo(boolean ordenar,
			ConteudoPublicacao conteudoPublicacao) throws ServiceException {
		List<TextoPeticao> protocolos = textoProtocoloService.pesquisar(
				conteudoPublicacao.getNumero(), conteudoPublicacao.getAno());

		// ORDENA OS PROTOCOLOS PELO NUMERO E ANO
		if (ordenar) {
			Collections.sort(protocolos, processoDjComparator);
		}

		return protocolos;
	}

	public Assunto recuperarAssuntoProcesso(Processo processo)
			throws ServiceException {

		return assuntoProcessoService.recuperarAssuntoProcesso(processo);
	}

	public Date recuperarDataCriacaoMateria(ProcessoPublicado processoPublicado)
			throws ServiceException {
		return conteudoPublicacaoService.recuperarDataAta(processoPublicado
				.getCodigoCapitulo(), processoPublicado.getCodigoMateria(),
				processoPublicado.getNumeroMateria(), processoPublicado
						.getAnoMateria());
	}

	public Ministro recuperarMinistroRelator(Processo processo)
			throws ServiceException {

		return ministroService.recuperarMinistroRelator(processo);
	}

}
