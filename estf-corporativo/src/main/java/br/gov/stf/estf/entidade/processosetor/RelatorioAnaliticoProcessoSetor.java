package br.gov.stf.estf.entidade.processosetor;

import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.estf.entidade.util.ObjetoIncidenteUtil;
import br.gov.stf.framework.util.SearchData;


public class RelatorioAnaliticoProcessoSetor {
	
	private String identificacaoProcessual; //classe + nº processo + tipo julgamento
	private String identificacaoProtocolo; //nº protocolo + ano 
	private String siglaClasseUnificada;
	private String siglaCadeiaIncidente;
	private String siglaRecurso;
	private String numeroProcessual;
	private String numeroProtocolo;
	private String anoProtocolo;
	private String tipoJulgamento;
	private String dataEntrada;
	private String tipoMeioProcesso;
	private String assuntoEGAB; //assunto EGAB	
	private String descricaoAssuntoCompletoSTF;
	private Short ordemAssunto;
	private String assuntoInterno; //assunto atribuido pelo setor
	private String observacaoProcessoSetor; //observacao da tab. processo setor
	private String secaoOrigem;
	private String secaoDestino;
	private String dataRemessa;
	private String dataRecebimento;
	private String localizacao; //concatenação de sala, armario, estante, prateleira e coluna
	private String sala;
	private String armario;
	private String estante;
	private String prateleira;
	private String coluna;	
	private String observacaoDeslocamento; //relatoriodeslocamentoanaliticolocalizacao
	private String usuarioDistribuicao;
	private String siglaUsuarioDistribuicao;
	private String grupoUsuarioDistribuicao;
	private String dataDistribuicao;
	private String descricaoTipoStatusSetor; //compõe a situacaoAtual
	private String descricaoTipoFaseSetor; //compõe a situacaoAtual
	private String observacaoFase;
	private String tipoObjetoIncidente;
	private String nomeGrupoProcesso;
	private String seqObjetoIncidente;
	private String origem;
	private String tema;
	private String motivoInaptidao;

	
	public String getSiglaCompletaClasseCadeiaIncidente() {
		if( getTipoObjetoIncidente().equals(TipoObjetoIncidente.RECURSO.getCodigo()) ) {
			return getSiglaCadeiaIncidente();
		} else if( getTipoObjetoIncidente().equals(TipoObjetoIncidente.PROCESSO.getCodigo()) ||
				getTipoObjetoIncidente().equals(TipoObjetoIncidente.INCIDENTE_JULGAMENTO.getCodigo()) ) {
			return getSiglaClasseUnificada();
		} else {
			return "";
		}
	}

	public String getIdentificacaoProcessual() {
		
		StringBuffer processoTemp = new StringBuffer();
		
		if( getTipoObjetoIncidente().equals(TipoObjetoIncidente.PROCESSO.getCodigo()) ) {
			if( SearchData.stringNotEmpty(getSiglaClasseUnificada()) )
				processoTemp.append( getSiglaClasseUnificada() );
			
			if( SearchData.stringNotEmpty(getNumeroProcessual()) && (new Long(getNumeroProcessual()) > 0) )
				processoTemp.append(" ").append( getNumeroProcessual() );
				
		} else if( getTipoObjetoIncidente().equals(TipoObjetoIncidente.RECURSO.getCodigo()) || 
				getTipoObjetoIncidente().equals(TipoObjetoIncidente.INCIDENTE_JULGAMENTO.getCodigo()) ) {
			processoTemp.append( ObjetoIncidenteUtil.getIdentificacao(getSiglaCadeiaIncidente(), new Long(getNumeroProcessual())) );
		}
		
		setIdentificacaoProcessual( processoTemp.toString() );
		
		return identificacaoProcessual;
	}
	
	public void setIdentificacaoProcessual(String identificacaoProcessual) {
		this.identificacaoProcessual = identificacaoProcessual;
	}
	
	public String getIdentificacaoProtocolo() {
		
		StringBuffer protocoloTemp = new StringBuffer();
		
		if( getNumeroProtocolo() != null && getNumeroProtocolo().trim().length() > 0 )
			protocoloTemp.append( getNumeroProtocolo() );
			
		if( getAnoProtocolo() != null && getAnoProtocolo().trim().length() > 0 )	
			protocoloTemp.append( " / ").append( getAnoProtocolo() );
		
		setIdentificacaoProtocolo( protocoloTemp.toString() );
		
		return identificacaoProtocolo;
	}
	
	public void setIdentificacaoProtocolo(String identificacaoProtocolo) {
		this.identificacaoProtocolo = identificacaoProtocolo;
	}
	
	public String getSecaoOrigem() {
		return secaoOrigem != null ? secaoOrigem : "";
	}
	
	public void setSecaoOrigem(String secaoOrigem) {
		this.secaoOrigem = secaoOrigem;
	}
	
	public String getSecaoDestino() {
		return secaoDestino != null ? secaoDestino : "";
	}
	
	public void setSecaoDestino(String secaoDestino) {
		this.secaoDestino = secaoDestino;
	}
	
	public String getDataRemessa() {
		return dataRemessa != null ? dataRemessa : "";
	}
	
	public void setDataRemessa(String dataRemessa) {
		this.dataRemessa = dataRemessa;
	}
	
	public String getDataRecebimento() {
		return dataRecebimento != null ? dataRecebimento : "";
	}
	
	public void setDataRecebimento(String dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}
	
	public String getLocalizacao() {
		
		StringBuffer localizacaoTemp = new StringBuffer();
		
		if( getSala() != null && getSala().trim().length() > 0 )
			localizacaoTemp.append( "  Sala: " ).append( getSala() );
		
		if( getArmario() != null && getArmario().trim().length() > 0 )
			localizacaoTemp.append( "  Armário: " ).append( getArmario() );
		
		if( getEstante() != null && getEstante().trim().length() > 0 )
			localizacaoTemp.append( "  Estante: " ).append( getEstante() );
		
		if( getPrateleira() != null && getPrateleira().trim().length() > 0 )
			localizacaoTemp.append( "  Prateleira: " ).append( getPrateleira() );
		
		if ( getColuna() != null && getColuna().trim().length() > 0 )
			localizacaoTemp.append( "  Coluna: " ).append( getColuna() );
		
		setLocalizacao( localizacaoTemp.toString() );
		
		return localizacao;
	}
	
	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}
	
	public String getObservacaoDeslocamento() {
		return observacaoDeslocamento != null ? observacaoDeslocamento : "";
	}
	
	public void setObservacaoDeslocamento(String observacaoDeslocamento) {
		this.observacaoDeslocamento = observacaoDeslocamento;
	}
	
	public String getDataDistribuicao() {
		return dataDistribuicao != null ? dataDistribuicao : "";
	}
	
	public void setDataDistribuicao(String dataDistribuicao) {
		this.dataDistribuicao = dataDistribuicao;
	}
	
	public String getUsuarioDistribuicao() {
		return usuarioDistribuicao  != null ? usuarioDistribuicao : "";
	}

	public void setUsuarioDistribuicao(String usuarioDistribuicao) {
		this.usuarioDistribuicao = usuarioDistribuicao;
	}

	public String getObservacaoFase() {
		return observacaoFase != null ? observacaoFase : "";
	}

	public void setObservacaoFase(String observacaoFase) {
		this.observacaoFase = observacaoFase;
	}

	public String getTipoMeioProcesso() {
		return tipoMeioProcesso != null ? tipoMeioProcesso : "";
	}

	public void setTipoMeioProcesso(String tipoMeioProcesso) {
		this.tipoMeioProcesso = tipoMeioProcesso;
	}

	public String getGrupoUsuarioDistribuicao() {
		return grupoUsuarioDistribuicao != null ? grupoUsuarioDistribuicao : "";
	}

	public void setGrupoUsuarioDistribuicao(String grupoUsuarioDistribuicao) {
		this.grupoUsuarioDistribuicao = grupoUsuarioDistribuicao;
	}

	public String getAssuntoEGAB() {
		return assuntoEGAB != null ? assuntoEGAB : "";
	}

	public void setAssuntoEGAB(String assuntoEGAB) {
		this.assuntoEGAB = assuntoEGAB;
	}

	public String getSiglaUsuarioDistribuicao() {
		return siglaUsuarioDistribuicao != null ? siglaUsuarioDistribuicao : "";
	}

	public void setSiglaUsuarioDistribuicao(String siglaUsuarioDistribuicao) {
		this.siglaUsuarioDistribuicao = siglaUsuarioDistribuicao;
	}

	public String getDescricaoTipoStatusSetor() {
		return descricaoTipoStatusSetor != null ? descricaoTipoStatusSetor : "";
	}

	public void setDescricaoTipoStatusSetor(String descricaoTipoStatusSetor) {
		this.descricaoTipoStatusSetor = descricaoTipoStatusSetor;
	}

	public String getDescricaoTipoFaseSetor() {
		return descricaoTipoFaseSetor != null ? descricaoTipoFaseSetor : "";
	}

	public void setDescricaoTipoFaseSetor(String descricaoTipoFaseSetor) {
		this.descricaoTipoFaseSetor = descricaoTipoFaseSetor;
	}

	public String getSiglaClasseUnificada() {
		return siglaClasseUnificada != null ? siglaClasseUnificada : "";
	}

	public void setSiglaClasseUnificada(String siglaClasseUnificada) {
		this.siglaClasseUnificada = siglaClasseUnificada;
	}

	public String getNumeroProcessual() {
		return numeroProcessual  != null ? numeroProcessual : "";
	}

	public void setNumeroProcessual(String numeroProcessual) {
		this.numeroProcessual = numeroProcessual;
	}

	public String getTipoJulgamento() {
		return tipoJulgamento  != null ? tipoJulgamento : "";
	}

	public void setTipoJulgamento(String tipoJulgamento) {
		this.tipoJulgamento = tipoJulgamento;
	}
	
	public String getDataEntrada() {
		return dataEntrada  != null ? dataEntrada : "";
	}

	public void setDataEntrada(String dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public String getNumeroProtocolo() {
		return numeroProtocolo  != null ? numeroProtocolo : "";
	}

	public void setNumeroProtocolo(String numeroProtocolo) {
		this.numeroProtocolo = numeroProtocolo;
	}

	public String getAnoProtocolo() {
		return anoProtocolo != null ? anoProtocolo : "";
	}

	public void setAnoProtocolo(String anoProtocolo) {
		this.anoProtocolo = anoProtocolo;
	}

	public String getSala() {
		return sala != null ? sala : "";
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public String getArmario() {
		return armario != null ? armario : "";
	}

	public void setArmario(String armario) {
		this.armario = armario;
	}

	public String getEstante() {
		return estante != null ? estante : "";
	}

	public void setEstante(String estante) {
		this.estante = estante;
	}

	public String getPrateleira() {
		return prateleira != null ? prateleira : "";
	}

	public void setPrateleira(String prateleira) {
		this.prateleira = prateleira;
	}

	public String getColuna() {
		return coluna != null ? coluna : "";
	}

	public void setColuna(String coluna) {
		this.coluna = coluna;
	}

	public String getAssuntoInterno() {
		return assuntoInterno != null ? assuntoInterno : "";
	}

	public void setAssuntoInterno(String assuntoInterno) {
		this.assuntoInterno = assuntoInterno;
	}

	public String getObservacaoProcessoSetor() {
		return observacaoProcessoSetor != null ? observacaoProcessoSetor : "";
	}

	public void setObservacaoProcessoSetor(String observacaoProcessoSetor) {
		this.observacaoProcessoSetor = observacaoProcessoSetor;
	}

	public String getDescricaoAssuntoCompletoSTF() {
		return descricaoAssuntoCompletoSTF != null ? descricaoAssuntoCompletoSTF : "";
	}

	public void setDescricaoAssuntoCompletoSTF(String descricaoAssuntoCompletoSTF) {
		this.descricaoAssuntoCompletoSTF = descricaoAssuntoCompletoSTF;
	}

	public Short getOrdemAssunto() {
		return ordemAssunto;
	}

	public void setOrdemAssunto(Short ordemAssunto) {
		this.ordemAssunto = ordemAssunto;
	}

	public String getSiglaRecurso() {
		return siglaRecurso;
	}

	public void setSiglaRecurso(String siglaRecurso) {
		this.siglaRecurso = siglaRecurso;
	}

	public String getTipoObjetoIncidente() {
		return tipoObjetoIncidente;
	}

	public void setTipoObjetoIncidente(String tipoObjetoIncidente) {
		this.tipoObjetoIncidente = tipoObjetoIncidente;
	}

	public String getSiglaCadeiaIncidente() {
		return siglaCadeiaIncidente;
	}

	public void setSiglaCadeiaIncidente(String siglaCadeiaIncidente) {
		this.siglaCadeiaIncidente = siglaCadeiaIncidente;
	}

	public String getNomeGrupoProcesso() {
		return nomeGrupoProcesso != null ? nomeGrupoProcesso : "";
	}

	public void setNomeGrupoProcesso(String nomeGrupoProcesso) {
		this.nomeGrupoProcesso = nomeGrupoProcesso;
	}

	public String getSeqObjetoIncidente() {
		return seqObjetoIncidente;
	}

	public void setSeqObjetoIncidente(String seqObjetoIncidente) {
		this.seqObjetoIncidente = seqObjetoIncidente;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	public String getMotivoInaptidao() {
		return motivoInaptidao;
	}

	public void setMotivoInaptidao(String motivoInaptidao) {
		this.motivoInaptidao = motivoInaptidao;
	}

}
