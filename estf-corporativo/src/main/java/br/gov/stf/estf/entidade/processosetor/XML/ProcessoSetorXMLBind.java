package br.gov.stf.estf.entidade.processosetor.XML;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProcessoSetorXMLBind {

/**
 * Classe para fazer o Bind com o XML
 * @author Rodrigo.Lisboa
 * @since 10/11/2009
 */
	
	private Short anoProtocolo; 
	private Short recurso;
	private Short numeroAno;
	
	private Long numeroProtocolo; 
	private Long numeroProcesso; 
	private Long codigoMinistroRelator; 
	private Long numeroPeticao; 
	private Long idSecaoUltimoDeslocamento; 
	private Long idGrupoProcessoSetor; 
	private Long idSetor; 
	private Long idTipoUltimaFaseSetor; 
	private Long idTipoUltimoStatusSetor;
	private Long idCategoriaPartePesquisa;
	private Long idTipoTarefa;
	private Long numeroLegislacao;
	private Long normaProcesso;
	
	private Long origem;
	
	private String siglasClassesProcessuaisAgrupadas; 
	private String sigla; 
	private String classesProcessuaisPorVirgula;
	private String canetaOtica;
	private String descricaoRecurso;
	private String siglaRecursoUnificada; 
	private String siglaTipoJulgamento; 
	private List<Long> motivoInaptidao; 
	private String codigoTipoMeioProcesso; 
	private String codigosAssuntosVirgula; 
	private String descricaoAssunto; 
	private String complementoAssunto; 
	private String numeroSala; 
	private String numeroArmario; 
	private String numeroEstante; 
	private String numeroPrateleira; 
	private String numeroColuna;
	private String obsDeslocamento;
	private String siglaUsuarioDistribuicao; 
	private String nomeParte;
	private String numeroArtigo;
	private String numeroInciso;
	private String numeroParagrafo;
	private String numeroAlinea;
	private String contemAndamentos;
	private String naoContemAndamentos;
	private String dataDeslocamento;
	private String tipoOrdemProcesso;
	private String tipoOrdem;
	private String andamentosProcessuais;
	
	private Date dataDistribuicaoMinistroInicial;
	private Date dataDistribuicaoMinistroFinal;
	private Date dataDistribuicaoInicial;
	private Date dataDistribuicaoFinal;  
	private Date dataFaseInicial;
	private Date dataFaseFinal; 
	private Date dataRemessaInicial;
	private Date dataRemessaFinal;
	private Date dataRecebimentoInicial;
	private Date dataRecebimentoFinal;
	private Date dataEntradaInicial;
	private Date dataEntradaFinal; 
	private Date dataSaidaInicial;
	private Date dataSaidaFinal;
	private Date dataIncluirAndamentosInicial;
	private Date dataIncluirAndamentosFinal;
	private Date dataNaoIncluirAndamentosInicial;
	private Date dataNaoIncluirAndamentosFinal;
	private Date dataAndamentosInicial;
	private Date dataAndamentosFinal;
	
	//private Boolean semRecurso;
	private Boolean faseProcessualAtual;
	private Boolean decisaoDigital;
	private Boolean decisaoDigitalTodos;
	private Boolean decisaoOutrosSistemas;
	private Boolean todosAndamentos;
	private Boolean repercussaoGeral; 
	private Boolean protocoloNaoAutuado;
	private Boolean semLocalizacao; 
	private Boolean semFase; 
	private Boolean semDistribuicao; 
	private Boolean semVista;
	private Boolean localizadosNoSetor; 
	private Boolean emTramiteNoSetor;
	private Boolean possuiLiminar; 
	private Boolean possuiPreferencia;
	private Boolean sobrestado;
	private Boolean julgado;
	private Boolean possuiRecurso;
	private Boolean processosDistribuidosInativos;
	private Boolean processosDistribuidosForaDoSetor;
	private Boolean mostraProcessoReautuadoRejeitado;
	

	/*
	public static enum TipoOrdemProcesso{
		PROCESSO("Processo"),
		PROTOCOLO("Protocolo"),
		VALOR_GUT("Valor GxUxT"),
		DT_ENTRADA("Dt. entrada"),
		ASSUNTO("Assunto");

		private String descricao;

		private TipoOrdemProcesso(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return this.descricao;
		}
		
		public static TipoOrdemProcesso getTipoOrdemProcesso(String descricao){
			if( PROCESSO.getDescricao().equals(descricao) ){
				return PROCESSO;
			}else if( VALOR_GUT.getDescricao().equals(descricao) ){
				return VALOR_GUT;
			}else if( PROTOCOLO.getDescricao().equals(descricao) ){
				return PROTOCOLO;
			}else if( DT_ENTRADA.getDescricao().equals(descricao) ){
				return DT_ENTRADA;
			}else if( ASSUNTO.getDescricao().equals(descricao) ){
				return ASSUNTO;
			}else{
				return null;
			}
		}
	}

	public static enum TipoOrdem{
		CRESCENTE("Crescente"),
		DECRESCENTE("Decrescente");

		private String descricao;

		private TipoOrdem(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return this.descricao;
		}
		
		public static TipoOrdem getTipoOrdem(String descricao){
			if( CRESCENTE.getDescricao().equals(descricao) ){
				return CRESCENTE;
			}else if( DECRESCENTE.getDescricao().equals(descricao) ){
				return DECRESCENTE;
			}else{
				return null;
			}
		}
		
	}
	*/
	//------------------------------- GET and SET -----------------------------
	
	public Short getAnoProtocolo() {
		return anoProtocolo;
	}

	public void setAnoProtocolo(Short anoProtocolo) {
		this.anoProtocolo = anoProtocolo;
	}

	public Short getRecurso() {
		return recurso;
	}

	public void setRecurso(Short recurso) {
		this.recurso = recurso;
	}

	public Short getNumeroAno() {
		return numeroAno;
	}

	public void setNumeroAno(Short numeroAno) {
		this.numeroAno = numeroAno;
	}

	public Long getNumeroProtocolo() {
		return numeroProtocolo;
	}

	public void setNumeroProtocolo(Long numeroProtocolo) {
		this.numeroProtocolo = numeroProtocolo;
	}

	public Long getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(Long numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public Long getCodigoMinistroRelator() {
		return codigoMinistroRelator;
	}

	public void setCodigoMinistroRelator(Long codigoMinistroRelator) {
		this.codigoMinistroRelator = codigoMinistroRelator;
	}

	public Long getNumeroPeticao() {
		return numeroPeticao;
	}

	public void setNumeroPeticao(Long numeroPeticao) {
		this.numeroPeticao = numeroPeticao;
	}

	public Long getIdSecaoUltimoDeslocamento() {
		return idSecaoUltimoDeslocamento;
	}

	public void setIdSecaoUltimoDeslocamento(Long idSecaoUltimoDeslocamento) {
		this.idSecaoUltimoDeslocamento = idSecaoUltimoDeslocamento;
	}

	public Long getIdGrupoProcessoSetor() {
		return idGrupoProcessoSetor;
	}

	public void setIdGrupoProcessoSetor(Long idGrupoProcessoSetor) {
		this.idGrupoProcessoSetor = idGrupoProcessoSetor;
	}

	public Long getIdSetor() {
		return idSetor;
	}

	public void setIdSetor(Long idSetor) {
		this.idSetor = idSetor;
	}

	public Long getIdTipoUltimaFaseSetor() {
		return idTipoUltimaFaseSetor;
	}

	public void setIdTipoUltimaFaseSetor(Long idTipoUltimaFaseSetor) {
		this.idTipoUltimaFaseSetor = idTipoUltimaFaseSetor;
	}

	public Long getIdTipoUltimoStatusSetor() {
		return idTipoUltimoStatusSetor;
	}

	public void setIdTipoUltimoStatusSetor(Long idTipoUltimoStatusSetor) {
		this.idTipoUltimoStatusSetor = idTipoUltimoStatusSetor;
	}

	public Long getIdCategoriaPartePesquisa() {
		return idCategoriaPartePesquisa;
	}

	public void setIdCategoriaPartePesquisa(Long idCategoriaPartePesquisa) {
		this.idCategoriaPartePesquisa = idCategoriaPartePesquisa;
	}

	public Long getIdTipoTarefa() {
		return idTipoTarefa;
	}

	public void setIdTipoTarefa(Long idTipoTarefa) {
		this.idTipoTarefa = idTipoTarefa;
	}

	public Long getNumeroLegislacao() {
		return numeroLegislacao;
	}

	public void setNumeroLegislacao(Long numeroLegislacao) {
		this.numeroLegislacao = numeroLegislacao;
	}

	public Long getNormaProcesso() {
		return normaProcesso;
	}

	public void setNormaProcesso(Long normaProcesso) {
		this.normaProcesso = normaProcesso;
	}

	public String getSiglasClassesProcessuaisAgrupadas() {
		return siglasClassesProcessuaisAgrupadas;
	}

	public void setSiglasClassesProcessuaisAgrupadas(
			String siglasClassesProcessuaisAgrupadas) {
		this.siglasClassesProcessuaisAgrupadas = siglasClassesProcessuaisAgrupadas;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getClassesProcessuaisPorVirgula() {
		return classesProcessuaisPorVirgula;
	}

	public void setClassesProcessuaisPorVirgula(String classesProcessuaisPorVirgula) {
		this.classesProcessuaisPorVirgula = classesProcessuaisPorVirgula;
	}
	
	public String getDescricaoRecurso() {
		return descricaoRecurso;
	}

	public void setDescricaoRecurso(String descricaoRecurso) {
		this.descricaoRecurso = descricaoRecurso;
	}

	public String getSiglaRecursoUnificada() {
		return siglaRecursoUnificada;
	}

	public void setSiglaRecursoUnificada(String siglaRecursoUnificada) {
		this.siglaRecursoUnificada = siglaRecursoUnificada;
	}

	public String getSiglaTipoJulgamento() {
		return siglaTipoJulgamento;
	}

	public void setSiglaTipoJulgamento(String siglaTipoJulgamento) {
		this.siglaTipoJulgamento = siglaTipoJulgamento;
	}
	
	public List<Long> getMotivoInaptidao() {
		return motivoInaptidao;
	}

	public void setMotivoInaptidao(List<Long> motivoInaptidao) {
		this.motivoInaptidao = motivoInaptidao;
	}

	public String getCodigoTipoMeioProcesso() {
		return codigoTipoMeioProcesso;
	}

	public void setCodigoTipoMeioProcesso(String codigoTipoMeioProcesso) {
		this.codigoTipoMeioProcesso = codigoTipoMeioProcesso;
	}

	public String getCodigosAssuntosVirgula() {
		return codigosAssuntosVirgula;
	}

	public void setCodigosAssuntosVirgula(String codigosAssuntosVirgula) {
		this.codigosAssuntosVirgula = codigosAssuntosVirgula;
	}

	public String getDescricaoAssunto() {
		return descricaoAssunto;
	}

	public void setDescricaoAssunto(String descricaoAssunto) {
		this.descricaoAssunto = descricaoAssunto;
	}

	public String getComplementoAssunto() {
		return complementoAssunto;
	}

	public void setComplementoAssunto(String complementoAssunto) {
		this.complementoAssunto = complementoAssunto;
	}

	public String getNumeroSala() {
		return numeroSala;
	}

	public void setNumeroSala(String numeroSala) {
		this.numeroSala = numeroSala;
	}

	public String getNumeroArmario() {
		return numeroArmario;
	}

	public void setNumeroArmario(String numeroArmario) {
		this.numeroArmario = numeroArmario;
	}

	public String getNumeroEstante() {
		return numeroEstante;
	}

	public void setNumeroEstante(String numeroEstante) {
		this.numeroEstante = numeroEstante;
	}

	public String getNumeroPrateleira() {
		return numeroPrateleira;
	}

	public void setNumeroPrateleira(String numeroPrateleira) {
		this.numeroPrateleira = numeroPrateleira;
	}

	public String getNumeroColuna() {
		return numeroColuna;
	}

	public void setNumeroColuna(String numeroColuna) {
		this.numeroColuna = numeroColuna;
	}

	public String getObsDeslocamento() {
		return obsDeslocamento;
	}

	public void setObsDeslocamento(String obsDeslocamento) {
		this.obsDeslocamento = obsDeslocamento;
	}

	public String getSiglaUsuarioDistribuicao() {
		return siglaUsuarioDistribuicao;
	}

	public void setSiglaUsuarioDistribuicao(String siglaUsuarioDistribuicao) {
		this.siglaUsuarioDistribuicao = siglaUsuarioDistribuicao;
	}

	public String getNomeParte() {
		return nomeParte;
	}

	public void setNomeParte(String nomeParte) {
		this.nomeParte = nomeParte;
	}

	public String getNumeroArtigo() {
		return numeroArtigo;
	}

	public void setNumeroArtigo(String numeroArtigo) {
		this.numeroArtigo = numeroArtigo;
	}

	public String getNumeroInciso() {
		return numeroInciso;
	}

	public void setNumeroInciso(String numeroInciso) {
		this.numeroInciso = numeroInciso;
	}

	public String getNumeroParagrafo() {
		return numeroParagrafo;
	}

	public void setNumeroParagrafo(String numeroParagrafo) {
		this.numeroParagrafo = numeroParagrafo;
	}

	public String getNumeroAlinea() {
		return numeroAlinea;
	}

	public void setNumeroAlinea(String numeroAlinea) {
		this.numeroAlinea = numeroAlinea;
	}

	public Date getDataDistribuicaoMinistroInicial() {
		return dataDistribuicaoMinistroInicial;
	}

	public void setDataDistribuicaoMinistroInicial(
			Date dataDistribuicaoMinistroInicial) {
		this.dataDistribuicaoMinistroInicial = dataDistribuicaoMinistroInicial;
	}

	public Date getDataDistribuicaoMinistroFinal() {
		return dataDistribuicaoMinistroFinal;
	}

	public void setDataDistribuicaoMinistroFinal(Date dataDistribuicaoMinistroFinal) {
		this.dataDistribuicaoMinistroFinal = dataDistribuicaoMinistroFinal;
	}

	public Date getDataDistribuicaoInicial() {
		return dataDistribuicaoInicial;
	}

	public void setDataDistribuicaoInicial(Date dataDistribuicaoInicial) {
		this.dataDistribuicaoInicial = dataDistribuicaoInicial;
	}

	public Date getDataDistribuicaoFinal() {
		return dataDistribuicaoFinal;
	}

	public void setDataDistribuicaoFinal(Date dataDistribuicaoFinal) {
		this.dataDistribuicaoFinal = dataDistribuicaoFinal;
	}

	public Date getDataFaseInicial() {
		return dataFaseInicial;
	}

	public void setDataFaseInicial(Date dataFaseInicial) {
		this.dataFaseInicial = dataFaseInicial;
	}

	public Date getDataFaseFinal() {
		return dataFaseFinal;
	}

	public void setDataFaseFinal(Date dataFaseFinal) {
		this.dataFaseFinal = dataFaseFinal;
	}

	public Date getDataRemessaInicial() {
		return dataRemessaInicial;
	}

	public void setDataRemessaInicial(Date dataRemessaInicial) {
		this.dataRemessaInicial = dataRemessaInicial;
	}

	public Date getDataRemessaFinal() {
		return dataRemessaFinal;
	}

	public void setDataRemessaFinal(Date dataRemessaFinal) {
		this.dataRemessaFinal = dataRemessaFinal;
	}

	public Date getDataRecebimentoInicial() {
		return dataRecebimentoInicial;
	}

	public void setDataRecebimentoInicial(Date dataRecebimentoInicial) {
		this.dataRecebimentoInicial = dataRecebimentoInicial;
	}

	public Date getDataRecebimentoFinal() {
		return dataRecebimentoFinal;
	}

	public void setDataRecebimentoFinal(Date dataRecebimentoFinal) {
		this.dataRecebimentoFinal = dataRecebimentoFinal;
	}

	public Date getDataEntradaInicial() {
		return dataEntradaInicial;
	}

	public void setDataEntradaInicial(Date dataEntradaInicial) {
		this.dataEntradaInicial = dataEntradaInicial;
	}

	public Date getDataEntradaFinal() {
		return dataEntradaFinal;
	}

	public void setDataEntradaFinal(Date dataEntradaFinal) {
		this.dataEntradaFinal = dataEntradaFinal;
	}

	public Date getDataSaidaInicial() {
		return dataSaidaInicial;
	}

	public void setDataSaidaInicial(Date dataSaidaInicial) {
		this.dataSaidaInicial = dataSaidaInicial;
	}

	public Date getDataSaidaFinal() {
		return dataSaidaFinal;
	}

	public void setDataSaidaFinal(Date dataSaidaFinal) {
		this.dataSaidaFinal = dataSaidaFinal;
	}

	public Date getDataAndamentosInicial() {
		return dataAndamentosInicial;
	}

	public void setDataAndamentosInicial(Date dataAndamentosInicial) {
		this.dataAndamentosInicial = dataAndamentosInicial;
	}
	
	public Date getDataAndamentosFinal() {
		return dataAndamentosFinal;
	}

	public void setDataAndamentosFinal(Date dataAndamentosFinal) {
		this.dataAndamentosFinal = dataAndamentosFinal;
	}
	
	public Date getDataIncluirAndamentosInicial() {
		return dataIncluirAndamentosInicial;
	}

	public void setDataIncluirAndamentosInicial(Date dataIncluirAndamentosInicial) {
		this.dataIncluirAndamentosInicial = dataIncluirAndamentosInicial;
	}

	public Date getDataIncluirAndamentosFinal() {
		return dataIncluirAndamentosFinal;
	}

	public void setDataIncluirAndamentosFinal(Date dataIncluirAndamentosFinal) {
		this.dataIncluirAndamentosFinal = dataIncluirAndamentosFinal;
	}

	public Date getDataNaoIncluirAndamentosInicial() {
		return dataNaoIncluirAndamentosInicial;
	}

	public void setDataNaoIncluirAndamentosInicial(
			Date dataNaoIncluirAndamentosInicial) {
		this.dataNaoIncluirAndamentosInicial = dataNaoIncluirAndamentosInicial;
	}

	public Date getDataNaoIncluirAndamentosFinal() {
		return dataNaoIncluirAndamentosFinal;
	}

	public void setDataNaoIncluirAndamentosFinal(Date dataNaoIncluirAndamentosFinal) {
		this.dataNaoIncluirAndamentosFinal = dataNaoIncluirAndamentosFinal;
	}
	
	

	public Boolean getFaseProcessualAtual() {
		return faseProcessualAtual;
	}

	public void setFaseProcessualAtual(Boolean faseProcessualAtual) {
		this.faseProcessualAtual = faseProcessualAtual;
	}

	public Boolean getDecisaoDigital() {
		return decisaoDigital;
	}

	public void setDecisaoDigital(Boolean decisaoDigital) {
		this.decisaoDigital = decisaoDigital;
	}
	
	public Boolean getDecisaoDigitalTodos() {
		return decisaoDigitalTodos;
	}

	public void setDecisaoDigitalTodos(Boolean decisaoDigitalTodos) {
		this.decisaoDigitalTodos = decisaoDigitalTodos;
	}
	
	
	public Boolean getDecisaoOutrosSistemas() {
		return decisaoOutrosSistemas;
	}

	public void setDecisaoOutrosSistemas(Boolean decisaoOutrosSistemas) {
		this.decisaoOutrosSistemas = decisaoOutrosSistemas;
	}
	
	public Boolean getTodosAndamentos() {
		return todosAndamentos;
	}

	public void setTodosAndamentos(Boolean todosAndamentos) {
		this.todosAndamentos = todosAndamentos;
	}
	
	
	public Boolean getRepercussaoGeral() {
		return repercussaoGeral;
	}

	public void setRepercussaoGeral(Boolean repercussaoGeral) {
		this.repercussaoGeral = repercussaoGeral;
	}

	public Boolean getProtocoloNaoAutuado() {
		return protocoloNaoAutuado;
	}

	public void setProtocoloNaoAutuado(Boolean protocoloNaoAutuado) {
		this.protocoloNaoAutuado = protocoloNaoAutuado;
	}

	public Boolean getSemLocalizacao() {
		return semLocalizacao;
	}

	public void setSemLocalizacao(Boolean semLocalizacao) {
		this.semLocalizacao = semLocalizacao;
	}

	public Boolean getSemFase() {
		return semFase;
	}

	public void setSemFase(Boolean semFase) {
		this.semFase = semFase;
	}

	public Boolean getSemDistribuicao() {
		return semDistribuicao;
	}

	public void setSemDistribuicao(Boolean semDistribuicao) {
		this.semDistribuicao = semDistribuicao;
	}

	public Boolean getSemVista() {
		return semVista;
	}

	public void setSemVista(Boolean semVista) {
		this.semVista = semVista;
	}

	public Boolean getLocalizadosNoSetor() {
		return localizadosNoSetor;
	}

	public void setLocalizadosNoSetor(Boolean localizadosNoSetor) {
		this.localizadosNoSetor = localizadosNoSetor;
	}

	public Boolean getEmTramiteNoSetor() {
		return emTramiteNoSetor;
	}

	public void setEmTramiteNoSetor(Boolean emTramiteNoSetor) {
		this.emTramiteNoSetor = emTramiteNoSetor;
	}

	public Boolean getPossuiLiminar() {
		return possuiLiminar;
	}

	public void setPossuiLiminar(Boolean possuiLiminar) {
		this.possuiLiminar = possuiLiminar;
	}

	public Boolean getPossuiPreferencia() {
		return possuiPreferencia;
	}

	public void setPossuiPreferencia(Boolean possuiPreferencia) {
		this.possuiPreferencia = possuiPreferencia;
	}

	public Boolean getSobrestado() {
		return sobrestado;
	}

	public void setSobrestado(Boolean sobrestado) {
		this.sobrestado = sobrestado;
	}

	public Boolean getJulgado() {
		return julgado;
	}

	public void setJulgado(Boolean julgado) {
		this.julgado = julgado;
	}

	public Boolean getPossuiRecurso() {
		return possuiRecurso;
	}

	public void setPossuiRecurso(Boolean possuiRecurso) {
		this.possuiRecurso = possuiRecurso;
	}
	
	
	public Boolean getProcessosDistribuidosInativos() {
		return processosDistribuidosInativos;
	}

	public void setProcessosDistribuidosInativos(Boolean processosDistribuidosInativos) {
		this.processosDistribuidosInativos = processosDistribuidosInativos;
	}

	public Boolean getProcessosDistribuidosForaDoSetor() {
		return processosDistribuidosForaDoSetor;
	}

	public void setProcessosDistribuidosForaDoSetor(Boolean processosDistribuidosForaDoSetor) {
		this.processosDistribuidosForaDoSetor = processosDistribuidosForaDoSetor;
	}

	public String getContemAndamentos() {
		return contemAndamentos;
	}

	public void setContemAndamentos(String contemAndamentos) {
		this.contemAndamentos = contemAndamentos;
	}

	public String getNaoContemAndamentos() {
		return naoContemAndamentos;
	}

	public void setNaoContemAndamentos(String naoContemAndamentos) {
		this.naoContemAndamentos = naoContemAndamentos;
	}

	public String getDataDeslocamento() {
		return dataDeslocamento;
	}

	public void setDataDeslocamento(String dataDeslocamento) {
		this.dataDeslocamento = dataDeslocamento;
	}

	public void setTipoOrdemProcesso(String tipoOrdemProcesso) {
		this.tipoOrdemProcesso = tipoOrdemProcesso;
	}

	public void setTipoOrdem(String tipoOrdem) {
		this.tipoOrdem = tipoOrdem;
	}

	public String getTipoOrdemProcesso() {
		return tipoOrdemProcesso;
	}

	public String getTipoOrdem() {
		return tipoOrdem;
	}

	public String getCanetaOtica() {
		return canetaOtica;
	}

	public void setCanetaOtica(String canetaOtica) {
		this.canetaOtica = canetaOtica;
	}

	public String getAndamentosProcessuais() {
		return andamentosProcessuais;
	}

	public void setAndamentosProcessuais(String andamentosProcessuais) {
		this.andamentosProcessuais = andamentosProcessuais;
	}

	public Boolean getMostraProcessoReautuadoRejeitado() {
		return mostraProcessoReautuadoRejeitado;
	}

	public void setMostraProcessoReautuadoRejeitado(Boolean mostraProcessoReautuadoRejeitado) {
		this.mostraProcessoReautuadoRejeitado = mostraProcessoReautuadoRejeitado;
	}

	public Long getOrigem() {
		return origem;
	}

	public void setOrigem(Long origem) {
		this.origem = origem;
	}
	
		
}
