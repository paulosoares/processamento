package br.gov.stf.estf.assinatura.stfoffice.editor;

import java.util.List;

import br.gov.stf.estf.assinatura.security.UsuarioAssinatura;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.stfoffice.editor.web.requisicao.jnlp.RequisicaoJnlpDocumento;
import br.jus.stf.estf.montadortexto.SpecDadosDocumentoSecretaria;

public class RequisicaoAbrirTexto extends RequisicaoJnlpDocumento {
	private static final long serialVersionUID = 1L;
	
	
	private Long codigoModelo;
	private Long codigoMinistroTexto;
	private String nomeDoDocumento;
	private Boolean isNumeracaoUnica;
	private Long numeracao;
	private ObjetoIncidente<?> objetoIncidenteProcurado;
	private Comunicacao comunicacao;
	private ArquivoEletronico arquivoEletronico;
	private SpecDadosDocumentoSecretaria dados;
	private UsuarioAssinatura usuarioSetor;
	private String usuarioCriacao;
	private String obsComunicacao;
	private String observacaoAndamento;
	private List<ArquivoProcessoEletronico> listaPecasVinculadasAoDocumento;
	private List<ObjetoIncidente<?>> listaProcessosObjetoIncidenteLote;
	

	public RequisicaoAbrirTexto(Long codigoModelo,Long codigoMinistroTexto, 
			String nomeDoDocumento, ObjetoIncidente<?> objetoIncidenteProcurado,
			SpecDadosDocumentoSecretaria dados, UsuarioAssinatura usuarioSetor, String usuarioCriacao,
			List<ArquivoProcessoEletronico> listaPecasVinculadasAoDocumento,
			List<ObjetoIncidente<?>> listaProcessosObjetoIncidenteLote) {
		super();
		this.codigoModelo = codigoModelo;
		this.codigoMinistroTexto = codigoMinistroTexto;
		this.nomeDoDocumento = nomeDoDocumento;
		this.objetoIncidenteProcurado = objetoIncidenteProcurado;
		this.dados = dados;
		this.usuarioSetor = usuarioSetor;
		this.usuarioCriacao = usuarioCriacao;
		this.listaPecasVinculadasAoDocumento = listaPecasVinculadasAoDocumento;
		this.listaProcessosObjetoIncidenteLote = listaProcessosObjetoIncidenteLote;
		this.comunicacao = new Comunicacao();
	}
	
	public RequisicaoAbrirTexto (Comunicacao documento) {
		this.comunicacao = documento;
	}
	
	public RequisicaoAbrirTexto(ArquivoEletronico arquivoEletronico){
		this.arquivoEletronico = arquivoEletronico;
	}
	

	public Comunicacao getComunicacao() {
		return comunicacao;
	}

	public void setComunicacao(Comunicacao comunicacao) {
		this.comunicacao = comunicacao;
	}

	public ArquivoEletronico getArquivoEletronico() {
		return arquivoEletronico;
	}

	public void setArquivoEletronico(ArquivoEletronico arquivoEletronico) {
		this.arquivoEletronico = arquivoEletronico;
	}

	public Long getCodigoModelo() {
		return codigoModelo;
	}

	public void setCodigoModelo(Long codigoModelo) {
		this.codigoModelo = codigoModelo;
	}

	public Long getCodigoMinistroTexto() {
		return codigoMinistroTexto;
	}

	public void setCodigoMinistroTexto(Long codigoMinistroTexto) {
		this.codigoMinistroTexto = codigoMinistroTexto;
	}
	
	public String getNomeDoDocumento() {
		return nomeDoDocumento;
	}

	public void setNomeDoDocumento(String nomeDoDocumento) {
		this.nomeDoDocumento = nomeDoDocumento;
	}

	public ObjetoIncidente<?> getObjetoIncidenteProcurado() {
		return objetoIncidenteProcurado;
	}

	public void setObjetoIncidenteProcurado(
			ObjetoIncidente<?> objetoIncidenteProcurado) {
		this.objetoIncidenteProcurado = objetoIncidenteProcurado;
	}

	public SpecDadosDocumentoSecretaria getDados() {
		return dados;
	}

	public void setDados(SpecDadosDocumentoSecretaria dados) {
		this.dados = dados;
	}

	public UsuarioAssinatura getUsuarioSetor() {
		return usuarioSetor;
	}

	public void setUsuarioSetor(UsuarioAssinatura usuarioSetor) {
		this.usuarioSetor = usuarioSetor;
	}

	
	public String getUsuarioCriacao() {
		return usuarioCriacao;
	}

	public void setUsuarioCriacao(String usuarioCriacao) {
		this.usuarioCriacao = usuarioCriacao;
	}

	public List<ArquivoProcessoEletronico> getListaPecasVinculadasAoDocumento() {
		return listaPecasVinculadasAoDocumento;
	}

	public void setListaPecasVinculadasAoDocumento(
			List<ArquivoProcessoEletronico> listaPecasVinculadasAoDocumento) {
		this.listaPecasVinculadasAoDocumento = listaPecasVinculadasAoDocumento;
	}


	public List<ObjetoIncidente<?>> getListaProcessosObjetoIncidenteLote() {
		return listaProcessosObjetoIncidenteLote;
	}

	public void setListaProcessosObjetoIncidenteLote(
			List<ObjetoIncidente<?>> listaProcessosObjetoIncidenteLote) {
		this.listaProcessosObjetoIncidenteLote = listaProcessosObjetoIncidenteLote;
	}

	public Boolean getIsNumeracaoUnica() {
		return isNumeracaoUnica;
	}

	public void setIsNumeracaoUnica(Boolean isNumeracaoUnica) {
		this.isNumeracaoUnica = isNumeracaoUnica;
	}

	public Long getNumeracao() {
		return numeracao;
	}

	public void setNumeracao(Long numeracao) {
		this.numeracao = numeracao;
	}

	public String getObsComunicacao() {
		return obsComunicacao;
	}

	public void setObsComunicacao(String obsComunicacao) {
		this.obsComunicacao = obsComunicacao;
	}

	public String getObservacaoAndamento() {
		return observacaoAndamento;
	}

	public void setObservacaoAndamento(String observacaoAndamento) {
		this.observacaoAndamento = observacaoAndamento;
	}

	@Override
	public String toString() {
		String toString = "RequisicaoNovoTexto";
		
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((codigoMinistroTexto == null) ? 0 : codigoMinistroTexto
						.hashCode());
		result = prime
				* result
				+ ((codigoModelo == null) ? 0 : codigoModelo
						.hashCode());
		result = prime 
				* result 
				+ ((comunicacao == null) ? 0 : comunicacao.hashCode());
		result = prime 
			* result 
			+ ((dados == null) ? 0 : dados.hashCode());
		result = prime 
		* result 
		+ ((usuarioSetor == null) ? 0 : usuarioSetor.hashCode());		
		
		return toString+result;
	}


}
