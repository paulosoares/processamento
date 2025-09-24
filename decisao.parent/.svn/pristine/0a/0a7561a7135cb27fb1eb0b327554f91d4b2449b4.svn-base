package br.jus.stf.estf.decisao.documento.support;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que contém o resultado de uma validação da permissão
 * para assinatura ou não de um documento (Texto ou Comunicação)
 * 
 * @author Tomas.Godoi
 *
 * @param <T>
 */
public class ValidacaoPermissaoAssinaturaDocumento<T extends Documento> {

	private boolean podeAssinar;
	private T documento;
	private DocumentoNaoAssinadoDto<T> naoAssinado;
	private List<T> devemSerAssinadosJunto;

	public static <M extends Documento> ValidacaoPermissaoAssinaturaDocumento<M> podeAssinar(M documento) {
		return podeAssinar(documento, new ArrayList<M>());
	}
	
	public static <M extends Documento> ValidacaoPermissaoAssinaturaDocumento<M> podeAssinar(M documento, List<M> deveSerAssinadoJunto) {
		ValidacaoPermissaoAssinaturaDocumento<M> validacao = new ValidacaoPermissaoAssinaturaDocumento<M>();
		validacao.podeAssinar = true;
		validacao.documento = documento;
		validacao.devemSerAssinadosJunto = deveSerAssinadoJunto;
		return validacao;
	}

	public static <M extends Documento> ValidacaoPermissaoAssinaturaDocumento<M> naoPodeAssinar(M documento, String motivo, String descricao) {
		ValidacaoPermissaoAssinaturaDocumento<M> validacao = new ValidacaoPermissaoAssinaturaDocumento<M>();
		validacao.podeAssinar = false;
		DocumentoNaoAssinadoDto<M> naoAssinado = new DocumentoNaoAssinadoDto<M>();
		naoAssinado.setDocumento(documento);
		naoAssinado.setMotivo(motivo);
		naoAssinado.setDescricao(descricao);
		validacao.naoAssinado = naoAssinado;
		return validacao;
	}

	private ValidacaoPermissaoAssinaturaDocumento() {

	}

	public boolean isPodeAssinar() {
		return podeAssinar;
	}

	public void setPodeAssinar(boolean podeAssinar) {
		this.podeAssinar = podeAssinar;
	}

	public T getDocumento() {
		return documento;
	}

	public void setDocumento(T documento) {
		this.documento = documento;
	}

	public DocumentoNaoAssinadoDto<T> getNaoAssinado() {
		return naoAssinado;
	}

	public void setNaoAssinado(DocumentoNaoAssinadoDto<T> naoAssinado) {
		this.naoAssinado = naoAssinado;
	}

	public List<T> getDevemSerAssinadosJunto() {
		return devemSerAssinadosJunto;
	}

	public void setDevemSerAssinadosJunto(List<T> devemSerAssinadosJunto) {
		this.devemSerAssinadosJunto = devemSerAssinadosJunto;
	}
	
}
