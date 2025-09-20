package br.gov.stf.estf.assinatura.visao.jsf.beans.consultardocumentosexterno;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import br.gov.stf.estf.assinatura.visao.jsf.beans.ambiente.BeanAmbiente;
import br.gov.stf.estf.assinatura.visao.util.externo.SistemasExternosUtils;

/**
 * Bean que permite o acesso a sistemas externos para consultas.
 * 
 * @author thiago.miranda
 * @since 3.14.7
 */
public class BeanConsultaExterna implements Serializable {

	private static final long serialVersionUID = 5211407341987174196L;

	private BeanAmbiente beanAmbiente;
	private ConsultaExternaVO consultaExternaVO;

	@SuppressWarnings("deprecation")
	public static BeanConsultaExterna getInstanciaJSF() {
		FacesContext context = FacesContext.getCurrentInstance();
		BeanConsultaExterna instancia = (BeanConsultaExterna) context.getApplication().createValueBinding("#{beanConsultaExterna}").getValue(context);
		return instancia;
	}

	public String consultarObjetoIncidenteEJudConsulta() {
		verificarSeAmbienteDesconhecido();

		Long seqObjetoIncidente = consultaExternaVO.getSeqObjetoIncidente();
		String urlDestino = SistemasExternosUtils.gerarLinkEJudConsulta(getNomeServidor(), seqObjetoIncidente);

		return urlDestino;
	}
	
	public String consultarObjetoIncidenteDigital(String siglaNumero) {
		verificarSeAmbienteDesconhecido();
		String urlDestino = SistemasExternosUtils.gerarLinkDigital(siglaNumero);

		return urlDestino;
	}
	
	public String consultarObjetoIncidenteDigitalPecas(String siglaNumero) {
		verificarSeAmbienteDesconhecido();
		String urlDestino = SistemasExternosUtils.gerarLinkDigitalPecas(siglaNumero);

		return urlDestino;
	}
	
	public String consultarObjetoIncidenteSupremo() {
		verificarSeAmbienteDesconhecido();

		Long seqObjetoIncidente = consultaExternaVO.getSeqObjetoIncidente();
		String urlDestino = SistemasExternosUtils.gerarLinkSupremo(getNomeServidor(), seqObjetoIncidente);

		return urlDestino;
	}

	public String consultarProcessoAndamento(){
		verificarSeAmbienteDesconhecido();
		
		Long seqObjetoIncidente = consultaExternaVO.getSeqObjetoIncidente();
		String urlDestino = SistemasExternosUtils.gerarLinkPortalStf(getPortalStf(), seqObjetoIncidente);
		return urlDestino;	
	}
	private String getNomeServidor() {
		return beanAmbiente.getNomeServidorPadrao();
	}
	
	private String getPortalStf() {
		return beanAmbiente.getPortalStf();
	}
	private void verificarSeAmbienteDesconhecido() {
		if (beanAmbiente.getIsAmbienteDesconhecido()) {
			throw new IllegalStateException("O ambiente atual é desconhecido!");
		}
	}

	public BeanAmbiente getBeanAmbiente() {
		return beanAmbiente;
	}

	public void setBeanAmbiente(BeanAmbiente beanAmbiente) {
		this.beanAmbiente = beanAmbiente;
	}

	public ConsultaExternaVO getConsultaExternaVO() {
		return consultaExternaVO;
	}

	public void setConsultaExternaVO(ConsultaExternaVO consultaExternaVO) {
		this.consultaExternaVO = consultaExternaVO;
	}
}
