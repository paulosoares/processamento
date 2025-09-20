package br.gov.stf.estf.assinatura.visao.jsf.beans.consultardocumentosexterno;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.util.ObjetoIncidenteUtil;

/**
 * Objetos dessa classe são utilizados para encapsular dados utilizados em consultas externas.
 * 
 * @author thiago.miranda
 * @since 3.14.7
 */
public class ConsultaExternaVO {

	private Long seqObjetoIncidente;

	private ConsultaExternaVO() {

	}

	public static ConsultaExternaVO criarAPartirDe(Object objeto) {
		ConsultaExternaVO vo = new ConsultaExternaVO();

		if (objeto instanceof ObjetoIncidente) {
			ObjetoIncidente<?> objetoIncidente = (ObjetoIncidente<?>) objeto;
			vo.seqObjetoIncidente = ObjetoIncidenteUtil.getProcesso(objetoIncidente).getId();
		} else if (objeto instanceof Long) {
			vo = criarAPartirDe((Long) objeto);
		}

		return vo;
	}

	public static ConsultaExternaVO criarAPartirDe(Long seqObjetoIncidente) {
		ConsultaExternaVO vo = new ConsultaExternaVO();
		vo.seqObjetoIncidente = seqObjetoIncidente;
		return vo;
	}

	public Long getSeqObjetoIncidente() {
		return seqObjetoIncidente;
	}
}
