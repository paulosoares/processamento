package br.gov.stf.estf.documento.model.util;

import java.util.Date;

import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.processostf.model.util.ConsultaObjetoIncidente;

public class ControleVotoDto {

	private final Date dataSessaoMenor;
	private final Date dataSessaoMaior;
	private final Date dataGenericaMenor;
	private final Date dataGenericaMaior;
	private final Long idObjetoIncidente;
	private final Long codigoMinistro;
	private final String operadorDataSessao;
	private final String operadorDataGenerica;
	private final Boolean cvLiberado;
	private final Boolean cvRecebido;
	private final Boolean cvAcordaoPublicado;
	private final Boolean cvNaoLiberado;
	private final Boolean cvNaoRecebido;
	private final Boolean cvAcordaoNaoPublicado;
	private final Boolean cvSemAcaoUmMinistro;
	private final Boolean cvPorTodosOsMinistros;
	private final String tipoProcesso;
	private final Long tipoTexto;
	//private final ConsultaObjetoIncidente consultaObjetoIncidente;
	private final Boolean julgamentoFinalizado;
	private final Boolean repercussaoGeral;
	private final Boolean cvDocAtivos;
	private final String codigoColegiado;	
	private final Boolean cvCompleto;
	private final Boolean inteiroTeorGerado;
	private final String segredoDeJustica;
	private final Boolean isMinistroPresidente;
	private final Long codigoMinistroPresidente;
	private TipoAmbienteConstante tipoAmbiente;
	private final Boolean isLeadingCase;
	private final String cvAnotacoes;
	private final String exclusivoDigital;
	private final String extratoAta;
	private String tipoAmbientePersonalizado;

	private ControleVotoDto(final Date dataSessaoMenor, final Date dataSessaoMaior, final Date dataGenericaMenor,
			final Date dataGenericaMaior, final Long idObjetoIncidente, final Long codigoMinistro,
			final String operadorDataSessao, final String operadorDataGenerica, final Boolean cvLiberado,
			final Boolean cvRecebido, final Boolean cvAcordaoPublicado, final Boolean cvNaoLiberado,
			final Boolean cvNaoRecebido, final Boolean cvAcordaoNaoPublicado, final Boolean cvSemAcaoUmMinistro,
			final Boolean cvPorTodosOsMinistros, final String tipoProcesso,
			//final ConsultaObjetoIncidente consultaObjetoIncidente, 
			final Boolean julgamentoFinalizado,
			final Boolean repercussaoGeral, final Boolean cvDocAtivos, final String codigoColegiado,
			final Boolean julgamentoVirtual, final Boolean cvCompleto, final Long tipoTexto,
			final Boolean inteiroTeorGerado,final String segredoDeJustica, final Boolean isMinistroPresidente, 
			final Long codigoMinistroPresidente, final TipoAmbienteConstante tipoAmbiente, final Boolean isLeadingCase, 
			final String cvAnotacoes, final String exclusivoDigital, final String extratoAta, final String tipoAmbientePersonalizado) {
		this.dataSessaoMenor = dataSessaoMenor;
		this.dataSessaoMaior = dataSessaoMaior;
		this.dataGenericaMenor = dataGenericaMenor;
		this.dataGenericaMaior = dataGenericaMaior;
		this.idObjetoIncidente = idObjetoIncidente;
		this.codigoMinistro = codigoMinistro;
		this.operadorDataSessao = operadorDataSessao;
		this.operadorDataGenerica = operadorDataGenerica;
		this.cvLiberado = cvLiberado;
		this.cvRecebido = cvRecebido;
		this.cvAcordaoPublicado = cvAcordaoPublicado;
		this.cvNaoLiberado = cvNaoLiberado;
		this.cvNaoRecebido = cvNaoRecebido;
		this.cvAcordaoNaoPublicado = cvAcordaoNaoPublicado;
		this.cvSemAcaoUmMinistro = cvSemAcaoUmMinistro;
		this.cvPorTodosOsMinistros = cvPorTodosOsMinistros;
		this.tipoProcesso = tipoProcesso;
	//	this.consultaObjetoIncidente = consultaObjetoIncidente;
		this.julgamentoFinalizado = julgamentoFinalizado;
		this.repercussaoGeral = repercussaoGeral;
		this.cvDocAtivos = cvDocAtivos;
		this.codigoColegiado = codigoColegiado;		
		this.cvCompleto = cvCompleto;
		this.tipoTexto = tipoTexto;
		this.inteiroTeorGerado = inteiroTeorGerado;
		this.segredoDeJustica = segredoDeJustica;
		this.isMinistroPresidente = isMinistroPresidente;
		this.codigoMinistroPresidente = codigoMinistroPresidente;
		this.tipoAmbiente = tipoAmbiente;
		this.isLeadingCase = isLeadingCase;
		this.cvAnotacoes = cvAnotacoes;
		this.exclusivoDigital = exclusivoDigital;
		this.extratoAta = extratoAta;
		this.tipoAmbientePersonalizado = tipoAmbientePersonalizado;
	}

	public Date getDataSessaoMenor() {
		return dataSessaoMenor;
	}

	public Date getDataSessaoMaior() {
		return dataSessaoMaior;
	}

	public Date getDataGenericaMenor() {
		return dataGenericaMenor;
	}

	public Date getDataGenericaMaior() {
		return dataGenericaMaior;
	}

	public Long getIdObjetoIncidente() {
		return idObjetoIncidente;
	}

	public Long getCodigoMinistro() {
		return codigoMinistro;
	}

	public String getOperadorDataSessao() {
		return operadorDataSessao;
	}

	public String getOperadorDataGenerica() {
		return operadorDataGenerica;
	}

	public Boolean getCvLiberado() {
		return cvLiberado;
	}

	public Boolean getCvRecebido() {
		return cvRecebido;
	}

	public Boolean getCvAcordaoPublicado() {
		return cvAcordaoPublicado;
	}

	public Boolean getCvNaoLiberado() {
		return cvNaoLiberado;
	}

	public Boolean getCvNaoRecebido() {
		return cvNaoRecebido;
	}

	public Boolean getCvAcordaoNaoPublicado() {
		return cvAcordaoNaoPublicado;
	}

	public Boolean getCvSemAcaoUmMinistro() {
		return cvSemAcaoUmMinistro;
	}

	public Boolean getCvPorTodosOsMinistros() {
		return cvPorTodosOsMinistros;
	}

	public String getTipoProcesso() {
		return tipoProcesso;
	}

//	public ConsultaObjetoIncidente getConsultaObjetoIncidente() {
//		return consultaObjetoIncidente;
//	}

	public Boolean getJulgamentoFinalizado() {
		return julgamentoFinalizado;
	}

	public Boolean getRepercussaoGeral() {
		return repercussaoGeral;
	}

	public Boolean getCvDocAtivos() {
		return cvDocAtivos;
	}

	public String getCodigoColegiado() {
		return codigoColegiado;
	}

	public Boolean getCvCompleto() {
		return cvCompleto;
	}
	
	public String getSegredoDeJustica() {
		return segredoDeJustica;
	}

	public boolean isDtoPreenchido() {
		return !(getDataSessaoMenor() == null && getDataSessaoMaior() == null && getDataGenericaMenor() == null
				&& getDataGenericaMaior() == null && getIdObjetoIncidente() == null && getCodigoMinistro() == null
				&& getOperadorDataSessao() == null && getOperadorDataGenerica() == null && getCvLiberado() == null
				&& getCvRecebido() == null && getCvAcordaoPublicado() == null && getCvNaoLiberado() == null
				&& getCvNaoRecebido() == null && getCvAcordaoNaoPublicado() == null
				&& getCvPorTodosOsMinistros() == null && getTipoProcesso() == null && getJulgamentoFinalizado() == null
				&& getRepercussaoGeral() == null && getCvDocAtivos() == null && getCodigoColegiado() == null
				&& getCvCompleto() == null && getTipoTexto() == null
				&& getInteiroTeorGerado() && getSegredoDeJustica() == null
				&& getIsLeadingCase() == null && getCvAnotacoes() == null);
	}

	public boolean isLiberado() {
		return this.getCvLiberado() != null && this.getCvLiberado().equals(Boolean.TRUE);
	}

	public boolean isRecebido() {
		return this.getCvRecebido() != null && this.getCvRecebido().equals(Boolean.TRUE);
	}

	public boolean isPublicado() {
		return this.getCvAcordaoPublicado() != null && this.getCvAcordaoPublicado().equals(Boolean.TRUE);
	}

	public boolean isNaoLiberado() {
		return this.getCvNaoLiberado() != null && this.getCvNaoLiberado().equals(Boolean.TRUE);
	}

	public boolean isNaoRecebido() {
		return this.getCvNaoRecebido() != null && this.getCvNaoRecebido().equals(Boolean.TRUE);
	}

	public boolean isNaoPublicado() {
		return this.getCvAcordaoNaoPublicado() != null && this.getCvAcordaoNaoPublicado().equals(Boolean.TRUE);
	}

	public boolean isSemAcaoApenasUmMinistro() {
		return this.getCvSemAcaoUmMinistro() != null && this.getCvSemAcaoUmMinistro().equals(Boolean.TRUE);
	}

	public boolean isPorTodosOsMinistros() {
		return this.getCvPorTodosOsMinistros() != null && this.getCvPorTodosOsMinistros().equals(Boolean.TRUE);
	}

	public boolean isJulgamentoFinalizado() {
		return this.getJulgamentoFinalizado() != null && this.getJulgamentoFinalizado().equals(Boolean.TRUE);
	}

	public boolean isRepercussaoGeral() {
		return this.getRepercussaoGeral() != null && this.getRepercussaoGeral().equals(Boolean.TRUE);
	}

	public boolean isDocAtivos() {
		return this.getCvDocAtivos() != null && this.getCvDocAtivos().equals(Boolean.TRUE);
	}

	public boolean isControleVotosCompleto() {
		return this.getCvCompleto() != null && this.getCvCompleto().equals(Boolean.TRUE);
	}
	
	public boolean isLeadingCase() {
		return this.getIsLeadingCase() != null && this.getIsLeadingCase().equals(Boolean.TRUE);
	}

	public Long getTipoTexto() {
		return tipoTexto;
	}

	public Boolean getInteiroTeorGerado() {
		return inteiroTeorGerado;
	}		

	public Boolean getIsMinistroPresidente() {
		return isMinistroPresidente;
	}

	public Long getCodigoMinistroPresidente() {
		return codigoMinistroPresidente;
	}
	
	public TipoAmbienteConstante getTipoAmbiente(){
		return tipoAmbiente;
	}

	public Boolean getIsLeadingCase() {
		return isLeadingCase;
	}

	public String getCvAnotacoes() {
		return cvAnotacoes;
	}

	public String getExclusivoDigital() {
		return exclusivoDigital;
	}

	public String getTipoAmbientePersonalizado() {
		return tipoAmbientePersonalizado;
	}

	public void setTipoAmbientePersonalizado(String tipoAmbientePersonalizado) {
		this.tipoAmbientePersonalizado = tipoAmbientePersonalizado;
	}

	public static class Builder {
		private Date dataSessaoMenor;
		private Date dataSessaoMaior;
		private Date dataGenericaMenor;
		private Date dataGenericaMaior;
		private Long idObjetoIncidente;
		private Long codigoMinistro;
		private String operadorDataSessao;
		private String operadorDataGenerica;
		private Boolean cvLiberado;
		private Boolean cvRecebido;
		private Boolean cvAcordaoPublicado;
		private Boolean cvNaoLiberado;
		private Boolean cvNaoRecebido;
		private Boolean cvAcordaoNaoPublicado;
		private Boolean cvSemAcaoUmMinistro;
		private Boolean cvPorTodosOsMinistros;
		private String tipoProcesso;
		private ConsultaObjetoIncidente consultaObjetoIncidente;
		private Boolean julgamentoFinalizado;
		private Boolean repercussaoGeral;
		private Boolean cvDocAtivos;
		private String codigoColegiado;
		private Boolean julgamentoVirtual;
		private Boolean cvCompleto;
		private Long tipoTexto;
		private Boolean inteiroTeorGerado;
		private String segredoDeJustica;
		private Boolean isMinistroPresidente;
		private Long codigoMinistroPresidente;
		private TipoAmbienteConstante tipoAmbiente;
		private Boolean isLeadingCase;
		private String cvAnotacoes;
		private String exclusivoDigital;
		private String extratoAta;
		private String tipoAmbientePersonalizado;		


		public ControleVotoDto builder() {
			return new ControleVotoDto(dataSessaoMenor, dataSessaoMaior, dataGenericaMenor, dataGenericaMaior,
					idObjetoIncidente, codigoMinistro, operadorDataSessao, operadorDataGenerica, cvLiberado, cvRecebido,
					cvAcordaoPublicado, cvNaoLiberado, cvNaoRecebido, cvAcordaoNaoPublicado, cvSemAcaoUmMinistro,
					cvPorTodosOsMinistros, tipoProcesso, 
					//consultaObjetoIncidente, 
					julgamentoFinalizado,
					repercussaoGeral, cvDocAtivos, codigoColegiado, julgamentoVirtual, cvCompleto, tipoTexto,
					inteiroTeorGerado,segredoDeJustica, isMinistroPresidente, codigoMinistroPresidente, tipoAmbiente,
					isLeadingCase, cvAnotacoes, exclusivoDigital, extratoAta, tipoAmbientePersonalizado);
		}

		public Builder setExclusivoDigital(String exclusivoDigital) {
			this.exclusivoDigital = exclusivoDigital;
			return this;
		}

		public Builder setCvAnotacoes(String cvAnotacoes) {
			this.cvAnotacoes = cvAnotacoes;
			return this;
		}

		public Builder setDataSessaoMenor(Date dataSessaoMenor) {
			this.dataSessaoMenor = dataSessaoMenor;
			return this;
		}

		public Builder setDataSessaoMaior(Date dataSessaoMaior) {
			this.dataSessaoMaior = dataSessaoMaior;
			return this;
		}

		public Builder setDataGenericaMenor(Date dataGenericaMenor) {
			this.dataGenericaMenor = dataGenericaMenor;
			return this;
		}

		public Builder setDataGenericaMaior(Date dataGenericaMaior) {
			this.dataGenericaMaior = dataGenericaMaior;
			return this;
		}

		public Builder setIdObjetoIncidente(Long idObjetoIncidente) {
			this.idObjetoIncidente = idObjetoIncidente;
			return this;
		}

		public Builder setCodigoMinistro(Long codigoMinistro) {
			this.codigoMinistro = codigoMinistro;
			return this;
		}

		public Builder setOperadorDataSessao(String operadorDataSessao) {
			this.operadorDataSessao = operadorDataSessao;
			return this;
		}

		public Builder setOperadorDataGenerica(String operadorDataGenerica) {
			this.operadorDataGenerica = operadorDataGenerica;
			return this;
		}

		public Builder setCvLiberado(Boolean cvLiberado) {
			this.cvLiberado = cvLiberado;
			return this;
		}

		public Builder setCvRecebido(Boolean cvRecebido) {
			this.cvRecebido = cvRecebido;
			return this;
		}

		public Builder setCvAcordaoPublicado(Boolean cvAcordaoPublicado) {
			this.cvAcordaoPublicado = cvAcordaoPublicado;
			return this;
		}

		public Builder setCvNaoLiberado(Boolean cvNaoLiberado) {
			this.cvNaoLiberado = cvNaoLiberado;
			return this;
		}

		public Builder setCvNaoRecebido(Boolean cvNaoRecebido) {
			this.cvNaoRecebido = cvNaoRecebido;
			return this;
		}

		public Builder setCvAcordaoNaoPublicado(Boolean cvAcordaoNaoPublicado) {
			this.cvAcordaoNaoPublicado = cvAcordaoNaoPublicado;
			return this;
		}

		public Builder setCvSemAcaoUmMinistro(Boolean cvSemAcaoUmMinistro) {
			this.cvSemAcaoUmMinistro = cvSemAcaoUmMinistro;
			return this;
		}

		public Builder setCvPorTodosOsMinistros(Boolean cvPorTodosOsMinistros) {
			this.cvPorTodosOsMinistros = cvPorTodosOsMinistros;
			return this;
		}

		public Builder setTipoProcesso(String tipoProcesso) {
			this.tipoProcesso = tipoProcesso;
			return this;
		}

		public Builder setConsultaObjetoIncidente(ConsultaObjetoIncidente consultaObjetoIncidente) {
			this.consultaObjetoIncidente = consultaObjetoIncidente;
			return this;
		}

		public Builder setJulgamentoFinalizado(Boolean julgamentoFinalizado) {
			this.julgamentoFinalizado = julgamentoFinalizado;
			return this;
		}

		public Builder setRepercussaoGeral(Boolean repercussaoGeral) {
			this.repercussaoGeral = repercussaoGeral;
			return this;
		}

		public Builder setCvDocAtivos(Boolean cvDocAtivos) {
			this.cvDocAtivos = cvDocAtivos;
			return this;
		}

		public Builder setCodigoColegiado(String codigoColegiado) {
			this.codigoColegiado = codigoColegiado;
			return this;
		}

		public Builder setJulgamentoVirtual(Boolean julgamentoVirtual) {
			this.julgamentoVirtual = julgamentoVirtual;
			return this;
		}

		public Builder setCvCompleto(Boolean cvCompleto) {
			this.cvCompleto = cvCompleto;
			return this;
		}

		public Builder setTipoTexto(Long tipoTexto) {
			this.tipoTexto = tipoTexto;
			return this;
		}

		public Builder setInteiroTeorGerado(Boolean inteiroTeorGerado) {
			this.inteiroTeorGerado = inteiroTeorGerado;
			return this;
		}
		
		public Builder setSegredoDeJustica(String segredoDeJustica) {
			this.segredoDeJustica = segredoDeJustica;
			return this;
		}
		
		public Builder setExtratoAta(String extratoAta) {
			this.extratoAta = extratoAta;
			return this;
		}

		public Builder setIsMinistroPresidente(Boolean isMinistroPresidente) {
			this.isMinistroPresidente = isMinistroPresidente;
			return this;
		}

		public Builder setCodigoMinistroPresidente(Long codigoMinistroPresidente) {
			this.codigoMinistroPresidente = codigoMinistroPresidente;
			return this;
		}
		
		public Builder setTipoAmbiente(TipoAmbienteConstante tipoAmbiente){
			this.tipoAmbiente = tipoAmbiente;
			return this;
		}
		
		public Builder setIsLeadingCase(Boolean isLeadingCase) {
			this.isLeadingCase = isLeadingCase;
			return this;
		}

		public Builder setTipoAmbientePersonalizado(String tipoAmbientePersonalizado) {
			this.tipoAmbientePersonalizado = tipoAmbientePersonalizado;
			return this;
		}
	}

	public String getExtratoAta() {
		return extratoAta;
	}
}
