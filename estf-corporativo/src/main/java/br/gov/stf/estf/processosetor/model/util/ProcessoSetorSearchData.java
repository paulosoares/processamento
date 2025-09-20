package br.gov.stf.estf.processosetor.model.util;

import java.util.List;

import br.gov.stf.estf.model.util.TipoOrdem;
import br.gov.stf.estf.model.util.TipoOrdemProcesso;
import br.gov.stf.framework.util.SearchData;

public class ProcessoSetorSearchData extends SearchData {

	/**
	 * Se inserir um novo parâmetro de pesquisa, lembrar de adicionar na classe ProcessoSetorXMLBind
	 */
	private static final long serialVersionUID = -6272190092778078252L;

	public Short anoProtocolo;
	public Short recurso;
	public Short numeroAno;

	public Long numeroProtocolo;
	public Long numeroProcesso;
	public Long codigoMinistroRelator;
	public Long numeroPeticao;
	public Long idSecaoUltimoDeslocamento;
	public Long idGrupoProcessoSetor;
	public Long idSetor;
	public Long idTipoUltimaFaseSetor;
	public Long idTipoUltimoStatusSetor;
	public Long idCategoriaPartePesquisa;
	public Long idTipoTarefa;
	public Long numeroLegislacao;
	public Long normaProcesso;
	
	public Long origem;

	public String siglasClassesProcessuaisAgrupadas;
	public String sigla;
	public String classesProcessuaisPorVirgula;
	public String canetaOtica;
	public String descricaoRecurso;
	public String siglaRecursoUnificada;
	public String siglaTipoJulgamento;
	public List<Long> motivoInaptidao;
	public String codigoTipoMeioProcesso;
	public String codigosAssuntosVirgula;
	public String descricaoAssunto;
	public String complementoAssunto;
	public String numeroSala;
	public String numeroArmario;
	public String numeroEstante;
	public String numeroPrateleira;
	public String numeroColuna;
	public String obsDeslocamento;
	public String siglaUsuarioDistribuicao;
	public String nomeParte;
	public String numeroArtigo;
	public String numeroInciso;
	public String numeroParagrafo;
	public String numeroAlinea;
	public String tipoRelatorio;
	public String andamentosProcessuais;

	public DateRange dataDistribuicaoMinistro;
	public DateRange dataDistribuicao;
	public DateRange dataFase;
	public DateRange dataRemessa;
	public DateRange dataRecebimento;
	public DateRange dataEntrada;
	public DateRange dataSaida;
	public DateRange dataIncluirAndamentos;
	public DateRange dataNaoIncluirAndamentos;
	public DateRange dataAndamentos;
	public Boolean faseProcessualAtual;
	public Boolean decisaoDigital;
	public Boolean decisaoDigitalTodos;
	public Boolean decisaoOutrosSistemas;
	public Boolean todosAndamentos;
	public Boolean repercussaoGeral;
	public Boolean protocoloNaoAutuado;
	public Boolean semLocalizacao;
	public Boolean semFase;
	public Boolean semDistribuicao;
	public Boolean semVista;
	public Boolean localizadosNoSetor;
	public Boolean emTramiteNoSetor;
	public Boolean possuiLiminar;
	public Boolean possuiPreferencia;
	public Boolean sobrestado;
	public Boolean julgado;
	public Boolean possuiRecurso;
	public Boolean processosDistribuidosInativos;
	public Boolean processosDistribuidosForaDoSetor;
	public Boolean mostraProcessoReautuadoRejeitado;

	public List<Long> listaIncluirAndamentos;
	public List<Long> listaNaoIncluirAndamentos;

	public TipoOrdemProcesso tipoOrderProcesso;
	public TipoOrdem tipoOrdem;
	public TipoGroupBy tipoGroupBy;

	public ProcessoSetorSearchData(Short anoProtocolo, Short recurso, Short numeroAno, Long numeroProtocolo, Long numeroProcesso, Long codigoMinistroRelator, Long numeroPeticao,
			Long idSecaoUltimoDeslocamento, Long idGrupoProcessoSetor, Long idSetor, Long idTipoUltimaFaseSetor, Long idTipoUltimoStatusSetor, Long idCategoriaPartePesquisa,
			Long idTipoTarefa, Long numeroLegislacao, Long normaProcesso, Long origem, String siglasClassesProcessuaisAgrupadas, String sigla, String classesProcessuaisPorVirgula,
			String canetaOtica, String descricaoRecurso, String siglaRecursoUnificada, String siglaTipoJulgamento, String codigoTipoMeioProcesso, String codigosAssuntosVirgula,
			String descricaoAssunto, String complementoAssunto, String numeroSala, String numeroArmario, String numeroEstante, String numeroPrateleira, String numeroColuna,
			String obsDeslocamento, String siglaUsuarioDistribuicao, String nomeParte, String numeroArtigo, String numeroInciso, String numeroParagrafo, String numeroAlinea,
			String tipoRelatorio, String andamentosProcessuais, DateRange dataDistribuicaoMinistro, DateRange dataDistribuicao, DateRange dataFase, DateRange dataRemessa,
			DateRange dataRecebimento, DateRange dataEntrada, DateRange dataSaida, DateRange dataIncluirAndamentos, DateRange dataNaoIncluirAndamentos, DateRange dataAndamentos,
			Boolean faseProcessualAtual, Boolean repercussaoGeral, Boolean processosDistribuidosInativos, Boolean processosDistribuidosForaDoSetor, Boolean protocoloNaoAutuado,
			Boolean semLocalizacao, Boolean semFase, Boolean semDistribuicao, Boolean semVista, Boolean localizadosNoSetor, Boolean emTramiteNoSetor, Boolean possuiLiminar,
			Boolean possuiPreferencia, Boolean sobrestado, Boolean julgado, Boolean possuiRecurso, Boolean mostraProcessoReautuadoRejeitado, List<Long> listaIncluirAndamentos,
			List<Long> listaNaoIncluirAndamentos, TipoOrdemProcesso tipoOrderProcesso, TipoOrdem tipoOrdem, TipoGroupBy tipoGroupBy, List<Long> motivoInaptidao, Boolean decisaoDigital, Boolean decisaoDigitalTodos,Boolean decisaoOutrosSistemas) {
		this.anoProtocolo = anoProtocolo;
		this.recurso = recurso;
		this.numeroAno = numeroAno;
		this.numeroProtocolo = numeroProtocolo;
		this.numeroProcesso = numeroProcesso;
		this.codigoMinistroRelator = codigoMinistroRelator;
		this.numeroPeticao = numeroPeticao;
		this.idSecaoUltimoDeslocamento = idSecaoUltimoDeslocamento;
		this.idGrupoProcessoSetor = idGrupoProcessoSetor;
		this.idSetor = idSetor;
		this.idTipoUltimaFaseSetor = idTipoUltimaFaseSetor;
		this.idTipoUltimoStatusSetor = idTipoUltimoStatusSetor;
		this.idCategoriaPartePesquisa = idCategoriaPartePesquisa;
		this.idTipoTarefa = idTipoTarefa;
		this.numeroLegislacao = numeroLegislacao;
		this.normaProcesso = normaProcesso;
		this.origem = origem;
		this.siglasClassesProcessuaisAgrupadas = siglasClassesProcessuaisAgrupadas;
		this.sigla = sigla;
		this.classesProcessuaisPorVirgula = classesProcessuaisPorVirgula;
		this.canetaOtica = canetaOtica;
		this.descricaoRecurso = descricaoRecurso;
		this.siglaRecursoUnificada = siglaRecursoUnificada;
		this.siglaTipoJulgamento = siglaTipoJulgamento;
		this.motivoInaptidao = motivoInaptidao;
		this.codigoTipoMeioProcesso = codigoTipoMeioProcesso;
		this.codigosAssuntosVirgula = codigosAssuntosVirgula;
		this.descricaoAssunto = descricaoAssunto;
		this.complementoAssunto = complementoAssunto;
		this.numeroSala = numeroSala;
		this.numeroArmario = numeroArmario;
		this.numeroEstante = numeroEstante;
		this.numeroPrateleira = numeroPrateleira;
		this.numeroColuna = numeroColuna;
		this.obsDeslocamento = obsDeslocamento;
		this.siglaUsuarioDistribuicao = siglaUsuarioDistribuicao;
		this.nomeParte = nomeParte;
		this.numeroArtigo = numeroArtigo;
		this.numeroInciso = numeroInciso;
		this.numeroParagrafo = numeroParagrafo;
		this.numeroAlinea = numeroAlinea;
		this.tipoRelatorio = tipoRelatorio;
		this.andamentosProcessuais = andamentosProcessuais;
		this.dataDistribuicaoMinistro = dataDistribuicaoMinistro;
		this.dataDistribuicao = dataDistribuicao;
		this.dataFase = dataFase;
		this.dataRemessa = dataRemessa;
		this.dataRecebimento = dataRecebimento;
		this.dataEntrada = dataEntrada;
		this.dataSaida = dataSaida;
		this.dataIncluirAndamentos = dataIncluirAndamentos;
		this.dataNaoIncluirAndamentos = dataNaoIncluirAndamentos;
		this.dataAndamentos = dataAndamentos;
		this.faseProcessualAtual = faseProcessualAtual;
		this.decisaoDigital = decisaoDigital;
		this.decisaoDigital = decisaoDigitalTodos;
		this.decisaoOutrosSistemas = decisaoOutrosSistemas;
		this.repercussaoGeral = repercussaoGeral;
		this.processosDistribuidosInativos = processosDistribuidosInativos;
		this.processosDistribuidosForaDoSetor = processosDistribuidosForaDoSetor;
		this.protocoloNaoAutuado = protocoloNaoAutuado;
		this.mostraProcessoReautuadoRejeitado = mostraProcessoReautuadoRejeitado;
		this.semLocalizacao = semLocalizacao;
		this.semFase = semFase;
		this.semDistribuicao = semDistribuicao;
		this.semVista = semVista;
		this.localizadosNoSetor = localizadosNoSetor;
		this.emTramiteNoSetor = emTramiteNoSetor;
		this.possuiLiminar = possuiLiminar;
		this.possuiPreferencia = possuiPreferencia;
		this.sobrestado = sobrestado;
		this.julgado = julgado;
		this.possuiRecurso = possuiRecurso;
		this.listaIncluirAndamentos = listaIncluirAndamentos;
		this.listaNaoIncluirAndamentos = listaNaoIncluirAndamentos;
		this.tipoOrderProcesso = tipoOrderProcesso;
		this.tipoOrdem = tipoOrdem;
		this.tipoGroupBy = tipoGroupBy;
	}

	public ProcessoSetorSearchData() {
		super();
	}

	public static Object verificaObjetoMaiorQZero(Object obj) {
		if (obj instanceof Long) {
			Long valor = (Long) obj;
			return valor != null && valor > 0L ? valor : null;
		} else if (obj instanceof Short) {
			Short valor = (Short) obj;
			return valor != null && valor > (short) 0 ? valor : null;
		} else {
			return null;
		}
	}

	public boolean getPesquisaLegislacao() {

		if ((normaProcesso != null && normaProcesso > 0) || (numeroAno != null && numeroAno > 0) || (numeroLegislacao != null && numeroLegislacao > 0)
				|| stringNotEmpty(numeroArtigo) || stringNotEmpty(numeroInciso) || stringNotEmpty(numeroParagrafo) || stringNotEmpty(numeroAlinea)) {
			return true;
		}

		return false;
	}

	public boolean utilizarViewProcessoSetor() {
		if ((SearchData.stringNotEmpty(sigla)) || (numeroProcesso != null && numeroProcesso > 0) || (anoProtocolo != null && anoProtocolo > 0)
				|| (numeroProtocolo != null && numeroProtocolo > 0) || (SearchData.stringNotEmpty(siglasClassesProcessuaisAgrupadas)) || (possuiRecurso != null)
				|| (recurso != null && recurso > 0) || (SearchData.stringNotEmpty(siglaTipoJulgamento)) || (faseProcessualAtual != null && faseProcessualAtual)
				|| (SearchData.stringNotEmpty(codigoTipoMeioProcesso)) || (repercussaoGeral != null && repercussaoGeral.booleanValue())
				|| (protocoloNaoAutuado != null && protocoloNaoAutuado) || (idTipoTarefa != null && idTipoTarefa > 0) || (possuiLiminar != null)
				|| (tipoOrderProcesso.equals(TipoOrdemProcesso.PROCESSO)) || (tipoOrderProcesso.equals(TipoOrdemProcesso.PROTOCOLO))
				|| (tipoOrderProcesso.equals(TipoOrdemProcesso.ASSUNTO)) || mostraProcessoReautuadoRejeitado != null && mostraProcessoReautuadoRejeitado)
			return true;
		else
			return false;
	}

}
