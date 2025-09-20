package br.gov.stf.estf.entidade.documento;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.FaseComunicacao.FlagFaseAtual;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao.FlagGenericaModeloComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.usuario.Pessoa;

@Entity
@Table(name = "COMUNICACAO", schema = "JUDICIARIO")
public class Comunicacao extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = -5918602363986409141L;
	private Long id;
	private Long numeroComunicacao;
	private String usuarioCriacao;
	private String dscNomeDocumento;
	private String obsComunicacao;
	private String obsAndamento;
	private Date dataCriacao;
	private Date dataEnvio;
	private Date dataRecebimento;
	private Date dataResposta;
	private String numeroArCorreios;
	private Texto texto;
	private ArquivoEletronico arquivoEletronico;
	private Setor setor;
	private ModeloComunicacao modeloComunicacao;
	private String usuarioRecebimento;
	private String usuarioDestinatario;
	private TipoRecebimentoComunicacao tipoRecebimentoComunicacao;
	private String numeroProtocoloDestino;
	private Pessoa pessoaDestinataria;
	private List<FaseComunicacao> fases;
	private List<DeslocamentoComunicacao> deslocamentos;
	private List<PecaProcessoEletronicoComunicacao> pecasProcessoEletronico;
	private List<ComunicacaoIncidente> comunicacaoIncidente;
	private String nomeMinistroRelatorAtual;
	private String nomeLocalSituacao;
	private String nomeSituacaoPesquisa;
	private String usuarioResponsavel;
	private Date dataAtribuicao;
	private String nomeUsuarioResponsavel;
	private String nomeUsuarioCriacao;
	private FlagGenericaModeloComunicacao flagJuntadaPecaProc;
	private FlagGenericaModeloComunicacao flagSemAndamento;
	
	
	@Override
	@Id
	@Column(name = "SEQ_COMUNICACAO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_COMUNICACAO", allocationSize = 1)
	public Long getId() {

		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "SEQ_ARQUIVO_ELETRONICO", unique = false, insertable = true, updatable = true)
	public ArquivoEletronico getArquivoEletronico() {
		return arquivoEletronico;
	}

	public void setArquivoEletronico(ArquivoEletronico arquivoEletronico) {
		this.arquivoEletronico = arquivoEletronico;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "comunicacao")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public List<ComunicacaoIncidente> getComunicacaoIncidente() {
		return comunicacaoIncidente;
	}

	public void setComunicacaoIncidente(List<ComunicacaoIncidente> comunicacaoIncidente) {
		this.comunicacaoIncidente = comunicacaoIncidente;
	}

	@Column(name = "DSC_COMUNICACAO", insertable = true, updatable = true, nullable = true, unique = false)
	public String getDscNomeDocumento() {
		return dscNomeDocumento;
	}

	public void setDscNomeDocumento(String dscNomeDocumento) {
		this.dscNomeDocumento = dscNomeDocumento;
	}	

	@Column(name = "NUM_AR_CORREIOS", insertable = true, updatable = true, nullable = true, unique = false)
	public String getNumeroArCorreios() {
		return numeroArCorreios;
	}

	public void setNumeroArCorreios(String numeroArCorreios) {
		this.numeroArCorreios = numeroArCorreios;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TEXTOS", unique = false, nullable = true, insertable = true, updatable = true)	
	public Texto getTexto() {
		return texto;
	}

	public void setTexto(Texto texto) {
		this.texto = texto;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_SETOR", unique = false, nullable = true, insertable = true, updatable = true)
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "SEQ_PESSOA_DESTINATARIA", referencedColumnName = "SEQ_PESSOA")
	public Pessoa getPessoaDestinataria() {
		return pessoaDestinataria;
	}
	
	public void setPessoaDestinataria(Pessoa pessoaDestinataria) {
		this.pessoaDestinataria = pessoaDestinataria;
	}
	

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_COMUNICACAO", referencedColumnName = "SEQ_COMUNICACAO", insertable = false, updatable = false)
	public List<FaseComunicacao> getFases() {
		return fases;
	}

	public void setFases(List<FaseComunicacao> fases) {
		this.fases = fases;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_COMUNICACAO", referencedColumnName = "SEQ_COMUNICACAO", insertable = false, updatable = false)
	public List<DeslocamentoComunicacao> getDeslocamentos() {
		return deslocamentos;
	}

	public void setDeslocamentos(List<DeslocamentoComunicacao> deslocamentos) {
		this.deslocamentos = deslocamentos;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "SEQ_MODELO_COMUNICACAO", unique = false, insertable = true, updatable = true)
	public ModeloComunicacao getModeloComunicacao() {
		return modeloComunicacao;
	}

	public void setModeloComunicacao(ModeloComunicacao modeloComunicacao) {
		this.modeloComunicacao = modeloComunicacao;
	}

	@Column(name = "SIG_USUARIO_CRIACAO", insertable = true, updatable = true, nullable = true, unique = false)
	public String getUsuarioCriacao() {
		return usuarioCriacao;
	}

	public void setUsuarioCriacao(String usuarioCriacao) {
		this.usuarioCriacao = usuarioCriacao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_INCLUSAO", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_COMUNICACAO_ENVIADA", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_COMUNICACAO_RECEBIDA", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_COMUNICACAO_RESPONDIDA", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataResposta() {
		return dataResposta;
	}

	public void setDataResposta(Date dataResposta) {
		this.dataResposta = dataResposta;
	}

	@OneToMany(cascade = { CascadeType.REMOVE }, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_COMUNICACAO", referencedColumnName = "SEQ_COMUNICACAO", insertable = false, updatable = false)
	public List<PecaProcessoEletronicoComunicacao> getPecasProcessoEletronico() {
		return pecasProcessoEletronico;
	}

	public void setPecasProcessoEletronico(List<PecaProcessoEletronicoComunicacao> pecasProcessoEletronico) {
		this.pecasProcessoEletronico = pecasProcessoEletronico;
	}

	@Column(name = "NUM_COMUNICACAO", insertable = true, updatable = true, nullable = true, unique = false)
	public Long getNumeroComunicacao() {
		return numeroComunicacao;
	}

	public void setNumeroComunicacao(Long numeroComunicacao) {
		this.numeroComunicacao = numeroComunicacao;
	}

	@Column(name = "TXT_OBSERVACAO", insertable = true, updatable = true, nullable = true, unique = false)
	public String getObsComunicacao() {
		return obsComunicacao;
	}

	public void setObsComunicacao(String obsComunicacao) {
		this.obsComunicacao = obsComunicacao;
	}

	@Column(name = "TXT_OBSERVACAO_ANDAMENTO", insertable = true, updatable = true, nullable = true, unique = false)
	public String getObsAndamento() {
		return obsAndamento;
	}

	public void setObsAndamento(String obsAndamento) {
		this.obsAndamento = obsAndamento;
	}
	
	
	@Column(name = "SIG_USUARIO_RECEBIMENTO", insertable = true, updatable = true, nullable = true, unique = false)
	public String getUsuarioRecebimento() {
		return usuarioRecebimento;
	}

	public void setUsuarioRecebimento(String usuarioRecebimento) {
		this.usuarioRecebimento = usuarioRecebimento;
	}

	@Column(name = "SIG_USUARIO_DESTINATARIO", insertable = true, updatable = true, nullable = true, unique = false)
	public String getUsuarioDestinatario() {
		return usuarioDestinatario;
	}

	
	public void setUsuarioDestinatario(String usuarioDestinatario) {
		this.usuarioDestinatario = usuarioDestinatario;
	}

	@Column(name = "COD_TIPO_RECEBIMENTO")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.documento.TipoRecebimentoComunicacao"),
			@Parameter(name = "idClass", value = "java.lang.Long"),
			@Parameter(name = "valueOfMethod", value = "valueOf") })
	public TipoRecebimentoComunicacao getTipoRecebimentoComunicacao() {
		return tipoRecebimentoComunicacao;
	}
	
	public void setTipoRecebimentoComunicacao(TipoRecebimentoComunicacao tipoRecebimentoComunicacao) {
		this.tipoRecebimentoComunicacao = tipoRecebimentoComunicacao;
	}

	@Column(name = "NUM_PROTOCOLO_DESTINO")
	public String getNumeroProtocoloDestino() {
		return numeroProtocoloDestino;
	}
	
	public void setNumeroProtocoloDestino(String numeroProtocoloDestino) {
		this.numeroProtocoloDestino = numeroProtocoloDestino;
	}
	
	@Transient
	public DeslocamentoComunicacao getDeslocamentoAtual() {
		List<DeslocamentoComunicacao> listaDeslocamentos = getDeslocamentos();
		try {
			if (CollectionUtils.isNotEmpty(listaDeslocamentos)) {
				for (DeslocamentoComunicacao deslocamento : listaDeslocamentos) {
					if (deslocamento.isDeslocamentoAtual()) {
						return deslocamento;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	@Transient
	public DeslocamentoComunicacao getDeslocamentoData(String strData, Long codigoSetorFase) {
		SimpleDateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
		Date data = null;
		
		try {
			if (strData != null && !strData.trim().isEmpty())
				data = fd.parse(strData);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return getDeslocamentoData(data, codigoSetorFase);
	}

	@Transient
	public DeslocamentoComunicacao getDeslocamentoData(Date data, Long codigoSetorFase) {
		DeslocamentoComunicacao deslocamentoFase = null;
		List<DeslocamentoComunicacao> listaDeslocamentos = getDeslocamentos();
		try {
			
			if (CollectionUtils.isNotEmpty(listaDeslocamentos)) {
				for (DeslocamentoComunicacao deslocamento : listaDeslocamentos) {
					Date dataSaida = deslocamento.getDataSaida();
					if (dataSaida == null)
						dataSaida = new Date();
					
					if  ( dataEntrePeriodo(data,deslocamento.getDataEntrada(), dataSaida) &&  (codigoSetorFase == null || codigoSetorFase.equals(deslocamento.getSetor().getId())) ) {
						if (deslocamentoFase == null || deslocamento.getDataEntrada().after(deslocamentoFase.getDataEntrada()))
							deslocamentoFase = deslocamento;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if ( deslocamentoFase == null )
			deslocamentoFase = getDeslocamentoAtual();
		
		return deslocamentoFase;
	}

	@Transient
	public FaseComunicacao getFasePeriodo(String strDataInicio, String strDataFinal, Long codigoFase) {
		SimpleDateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
		Date dataInicio = null;
		Date dataFinal = null;
		
		try {
			if (strDataInicio != null && !strDataInicio.trim().isEmpty())
				dataInicio = fd.parse(strDataInicio);
			if (strDataInicio != null && !strDataInicio.trim().isEmpty())
				dataInicio = fd.parse(strDataInicio);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return getFasePeriodo(dataInicio, dataFinal, codigoFase);
	}

	@Transient
	public FaseComunicacao getFasePeriodo(Date dataInicio, Date dataFinal, Long codigoFase) {
		FaseComunicacao faseSelecionada = null;
		List<FaseComunicacao> listaFases = getFases();
		try {
			
			
			if (CollectionUtils.isNotEmpty(listaFases)) {
				for (FaseComunicacao fase : listaFases) {
					if  ( (dataEntrePeriodo(fase.getDataLancamento(),dataInicio, dataFinal) ) &&
						  fase.getTipoFase().getCodigoFase().equals(codigoFase) ) { 
							if (faseSelecionada == null || fase.getDataLancamento().after(faseSelecionada.getDataLancamento()))
								faseSelecionada = fase;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if ( faseSelecionada == null )
			faseSelecionada = getFaseComunicacaoAtual();
		
		return faseSelecionada;
	}

	private boolean dataEntrePeriodo(Date data, Date dataInicio, Date dataFinal ) {
		return ((dataInicio.before(data) || dataInicio.equals(data) && (dataFinal.equals(data) || dataFinal.after(data)))); 	
	}
	
	@Transient
	public Date getDataEntradaDeslocamentoAtual() {
		if(getDeslocamentoAtual() != null){
			return getDeslocamentoAtual().getDataEntrada();
		}
		return null;
	}

	@Transient
	public String getFaseAtual() {
		List<FaseComunicacao> listaFases = getFases();
		String faseAtual = null;
		
		try {
			if (CollectionUtils.isNotEmpty(listaFases)) {
				faseAtual = getFaseAtualString(listaFases);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}


		return faseAtual;
	}
	
	@Transient
	public String getDescricaoFaseAtual() {
		List<FaseComunicacao> listaFases = getFases();
		String descricao = "-";
		for(FaseComunicacao fase : listaFases){
			if (fase.getFlagFaseAtual().equals(FlagFaseAtual.S)){
				descricao = (fase.getTipoFase().getDescricao() == null || fase.getTipoFase().getDescricao().isEmpty()) ? "-" : fase.getTipoFase().getDescricao() ;
				break;
			}
		}
		return descricao;
		
	}
	@Transient
	public String getObservacaoFaseAtual() {
		List<FaseComunicacao> listaFases = getFases();
		String observacao = null;
		for(FaseComunicacao fase : listaFases){
			if (fase.getFlagFaseAtual().equals(FlagFaseAtual.S)){
				observacao = (fase.getObservacao() == null || fase.getObservacao().isEmpty()) ? "-" : fase.getObservacao();
				break;
			}
		}
		return observacao;
	}

	@Transient
	public String getDataFaseAtual() {
		SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		List<FaseComunicacao> listaFases = getFases();
		Date dataFase = null;
		
		try {
			if (CollectionUtils.isNotEmpty(listaFases)) {
				dataFase = getDataFaseAtual(listaFases);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (dataFase != null)
			return formatDate.format(dataFase);
		
		return "";
	}

	private String getFaseAtualString(List<FaseComunicacao> listaFases) {
		String faseString = null;

		try {
			
			for (FaseComunicacao fase : listaFases) {
				if (fase.getFlagFaseAtual() == FlagFaseAtual.S) {
					if (fase.getObservacao() != null) {
						faseString = fase.getTipoFase().getDescricao() + " - " + fase.getObservacao();
						break;
					} else {
						faseString = fase.getTipoFase().getDescricao();
						break;
					}
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return faseString;
	}
	
	private Date getDataFaseAtual(List<FaseComunicacao> listaFases) {
		Date dataFase = null;

		try {
			
			for (FaseComunicacao fase : listaFases) {
				if (fase.getFlagFaseAtual() == FlagFaseAtual.S) {
					dataFase = fase.getDataLancamento();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return dataFase;
	}
	

	@Transient
	public FaseComunicacao getFaseComunicacaoAtual() {
		List<FaseComunicacao> listaFases = getFases();
		try {
			for (FaseComunicacao fase : listaFases) {
				if (fase.getFlagFaseAtual() == FlagFaseAtual.S) {
					return fase;
				}
			}

		} catch (Exception e) {
		}

		return null;
	}
		
		

	@Transient
	public String getConcatenaAnoNumeracaoUnica() {
		StringBuffer anoNumeracao = new StringBuffer();
		try {
			if (getNumeroComunicacao() != null && getNumeroComunicacao() > 0) {
				anoNumeracao.append(numeroComunicacao);
				anoNumeracao.append("/");
				SimpleDateFormat ano = new SimpleDateFormat("yyyy");
				anoNumeracao.append(ano.format(getDataCriacao()));
			} else {
				anoNumeracao.append("-");
			}

		} catch (Exception e) {
			// TODO: handle exception
		} 
		return anoNumeracao.toString();
	}

	@Transient
	public Boolean isFaseAtual(TipoFaseComunicacao fase) {
		return getFaseAtual().equals(fase.getDescricao());
	}

	@Transient
	public Boolean getAssinado() {
		return isFaseAtual(TipoFaseComunicacao.ASSINADO);
	}

	@Transient
	public Boolean getAguardandoEncaminhamentoEstfDecisao() {
		return isFaseAtual(TipoFaseComunicacao.AGUARDANDO_ENCAMINHAMENTO_ESTFDECISAO);
	}

	/**
	 * Obtém a identificação do {@link ObjetoIncidente} ao qual a comunicação se associa. Nos casos de comunicações geradas com processsos em lote, irá retornar
	 * a identificação do objeto incidente "cabeça" (principal).
	 * 
	 * @return
	 */
	@Transient
	public String getIdentificacaoProcessual() {
		String siglaNumeroProcesso = "";
		try {
			ObjetoIncidente<?> objetoIncidentePrincipal = getObjetoIncidenteUnico();
			if (objetoIncidentePrincipal != null)
				siglaNumeroProcesso = objetoIncidentePrincipal.getIdentificacao();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return siglaNumeroProcesso;
	}

	@Transient
	public String getConfidencialidade() {
		String confidencialidade = "";
		try {
			ObjetoIncidente<?> objetoIncidentePrincipal = getObjetoIncidenteUnico();
			if (objetoIncidentePrincipal != null && objetoIncidentePrincipal.getTipoConfidencialidade() != null)
				confidencialidade = objetoIncidentePrincipal.getTipoConfidencialidade().getDescricao();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return confidencialidade;
	}

	/**
	 * Obtém o {@link ObjetoIncidente} ao qual a comunicação se associa. Nos casos de comunicações geradas com processsos em lote, irá retornar o objeto
	 * incidente "cabeça" (principal).
	 */
	@Transient
	public ObjetoIncidente<?> getObjetoIncidenteUnico() {
		try {
			List<ComunicacaoIncidente> listaComunicacaoIncidente = getComunicacaoIncidente();
			if (listaComunicacaoIncidente != null && listaComunicacaoIncidente.size() == 1) {
				return getComunicacaoIncidentePrincipal().getObjetoIncidente();
			} else {
				return getComunicacaoIncidentePrincipal().getObjetoIncidente().getPrincipal();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	@Transient
	public String getIdentificacaoObjetoIncidenteUnico(){
		ObjetoIncidente<?> objIndicente = getObjetoIncidenteUnico();
		if (objIndicente != null)
			return objIndicente.getIdentificacao();
		else
			return null;
	}
	
	@Transient
	public Long  getIdObjetoIncidenteUnico(){
		ObjetoIncidente<?> objIncidente = getObjetoIncidenteUnico();
		if (objIncidente != null && objIncidente.getPrincipal() != null)
			return objIncidente.getPrincipal().getId();
		else
			return 0L;
	}
	
	/**
	 * Obtém o vínculo principal da comunicação com o {@link ObjetoIncidente} associado.
	 */
	@Transient
	public ComunicacaoIncidente getComunicacaoIncidentePrincipal() {
		ComunicacaoIncidente comunicacaoIncidentePrincipal = null;

		try {
			List<ComunicacaoIncidente> listaComunicacaoIncidente = getComunicacaoIncidente();

			if (CollectionUtils.isNotEmpty(listaComunicacaoIncidente)) {
				for (ComunicacaoIncidente ci : listaComunicacaoIncidente) {
					if (ci.isVinculoProcessoLotePrincipal()) {
						comunicacaoIncidentePrincipal = ci;
						break;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return comunicacaoIncidentePrincipal;
	}

	@Transient
	public Date getDataAssinaturaExpedirDocumentos() {
		try {
			List<FaseComunicacao> listaFases = getFases();
			if (listaFases != null) {
				for (FaseComunicacao fase : listaFases) {
					if (fase.getFlagFaseAtual() == FlagFaseAtual.S) {
						return fase.getDataCriacao();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transient
	public String getNomeMinistroRelatorAtual() {
		return nomeMinistroRelatorAtual;
	}

	public void setNomeMinistroRelatorAtual(String nomeMinistroRelatorAtual) {
		this.nomeMinistroRelatorAtual = nomeMinistroRelatorAtual;
	}

	@Transient
	public String getNomeLocalSituacao() {
		return nomeLocalSituacao;
	}

	public void setNomeLocalSituacao(String nomeLocalSituacao) {
		this.nomeLocalSituacao = nomeLocalSituacao;
	}

	@Transient
	public String getNomeSituacaoPesquisa() {
		return nomeSituacaoPesquisa;
	}

	public void setNomeSituacaoPesquisa(String nomeSituacaoPesquisa) {
		this.nomeSituacaoPesquisa = nomeSituacaoPesquisa;
	}
	
	@Column(name = "SIG_USUARIO_RESPONSAVEL", insertable = true, updatable = true, nullable = true, unique = false)
	public String getUsuarioResponsavel() {
		return usuarioResponsavel;
	}

	public void setUsuarioResponsavel(String usuarioResponsavel) {
			this.usuarioResponsavel = usuarioResponsavel;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_ATRIBUICAO_RESPONSAVEL", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataAtribuicao() {
		return dataAtribuicao;
	}

	public void setDataAtribuicao(Date dataAtribuicao) {
		this.dataAtribuicao = dataAtribuicao;
	}
	
	@Formula("(SELECT UU.NOM_USUARIO FROM STF.USUARIOS UU WHERE UU.SIG_USUARIO = SIG_USUARIO_RESPONSAVEL)")
	public String getNomeUsuarioResponsavel() {
		if (nomeUsuarioResponsavel == null) {
			return usuarioResponsavel;
		}else {
			return nomeUsuarioResponsavel;	
		}
	}
	
	public void  setNomeUsuarioResponsavel(String nomeUsuarioResponsavel) {
		this.nomeUsuarioResponsavel = nomeUsuarioResponsavel;
	}
	
	@Formula("(SELECT UU.NOM_USUARIO FROM STF.USUARIOS UU WHERE UU.SIG_USUARIO = SIG_USUARIO_CRIACAO)")
	public String getNomeUsuarioCriacao() {
		return nomeUsuarioCriacao;
	}

	public void setNomeUsuarioCriacao(String nomeUsuarioCriacao) {
		this.nomeUsuarioCriacao = nomeUsuarioCriacao;
	}
	
	@Column(name = "FLG_JUNTADA_PECA_PROC")
	@Enumerated(EnumType.STRING)
	public FlagGenericaModeloComunicacao getFlagJuntadaPecaProc() {
		return flagJuntadaPecaProc;
	}

	public void setFlagJuntadaPecaProc(FlagGenericaModeloComunicacao flagJuntadaPecaProc) {
		this.flagJuntadaPecaProc = flagJuntadaPecaProc;
	}

	@Column(name = "FLG_SEM_ANDAMENTO")
	@Enumerated(EnumType.STRING)
	public FlagGenericaModeloComunicacao getFlagSemAndamento() {
		return flagSemAndamento;
	}

	public void setFlagSemAndamento(FlagGenericaModeloComunicacao flagSemAndamento) {
		this.flagSemAndamento = flagSemAndamento;
	}
	
	
}
