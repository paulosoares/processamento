package br.gov.stf.estf.entidade.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;

import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.Protocolo;
import br.gov.stf.estf.entidade.processostf.RecursoProcesso;
import br.gov.stf.estf.entidade.processostf.TipoIncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.TipoRecurso;
import br.gov.stf.framework.util.SearchData;

public class ObjetoIncidenteUtil {

	private static final Set<TipoObjetoIncidente> objetosComDadosProcessuais = new HashSet<TipoObjetoIncidente>(Arrays.asList(
			TipoObjetoIncidente.INCIDENTE_JULGAMENTO, TipoObjetoIncidente.PROCESSO, TipoObjetoIncidente.RECURSO));

	// public static String getIdentificacao(String identificacaoPrincipal,
	// String siglaCadeiaIncidente) {
	// identificacaoPrincipal = identificacaoPrincipal + "-";
	// return siglaCadeiaIncidente.replaceFirst("([A-Za-z]*)-",
	// identificacaoPrincipal);
	// }
	//
	// public static String getIdentificacaoCompleta(String
	// identificacaoCompleta, String descricaoCadeiaIncidente) {
	// String numeroDoProcesso = identificacaoCompleta.replaceAll("([A-Za-z]*)",
	// "");
	// return descricaoCadeiaIncidente + " " + numeroDoProcesso;
	// }

	public static String getIdentificacao(String siglaCadeiaCompleta, Long numeroProcesso) {
		Validate.notNull(numeroProcesso, "Número do processo não informado.");
		
		String numeroProcessoFormatado = numeroProcesso.toString();
		StringBuffer identificacao = new StringBuffer();
		if (SearchData.stringNotEmpty(siglaCadeiaCompleta) && siglaCadeiaCompleta.contains("-")) {
			int indexPrimeiroHifen = siglaCadeiaCompleta.indexOf('-');
			String siglaClasse = siglaCadeiaCompleta.substring(0, indexPrimeiroHifen);
			String siglaCadeia = siglaCadeiaCompleta.substring(indexPrimeiroHifen + 1);

			identificacao.append(siglaClasse).append(" ").append(numeroProcessoFormatado).append(" ").append(siglaCadeia);
		} else if (SearchData.stringNotEmpty(siglaCadeiaCompleta) && !siglaCadeiaCompleta.contains("-")) {
			identificacao.append(siglaCadeiaCompleta).append(" ").append(numeroProcessoFormatado);
		} else {
			identificacao.append(numeroProcessoFormatado);
		}
		return identificacao.toString();
	}

	public static String getIdentificacao(String siglaCadeiaCompleta, Integer numSequencia, Long numeroProcesso) {
		return getIdentificacao(siglaCadeiaCompleta, numeroProcesso);
	}

	public static String getIdentificacaoCompleta(String descricaoCadeia, Long numeroProcesso) {
		return descricaoCadeia + " " + numeroProcesso;
	}

	/**
	 * Retorna os dados processuais do ObjetoIncidente, caso ele seja um
	 * IncidenteJulgamento, ProcessoRecurso ou Processo. Nos demais casos,
	 * retorna null.
	 * 
	 * @param objetoIncidente
	 * @return Número, Classe e TipoRecurso, encapsulados pela classe
	 *         {@link DadosProcessuais}
	 */
	public static DadosProcessuais getDadosProcessuais(ObjetoIncidente<Processo> objetoIncidente) {
		if (objetosComDadosProcessuais.contains(objetoIncidente.getTipoObjetoIncidente())) {
			DadosProcessuais dadosProcessuais = new DadosProcessuais();
			Processo processo = objetoIncidente.getPrincipal();
			dadosProcessuais.setClasseProcessual(processo.getClasseProcessual());
			dadosProcessuais.setNumeroProcesso(processo.getNumeroProcessual());
			dadosProcessuais.setTipoRecurso(getTipoRecurso(objetoIncidente));
			dadosProcessuais.setTipoJulgamento(getTipoJulgamento(objetoIncidente));
			return dadosProcessuais;
		}
		return null;
	}

	public static TipoIncidenteJulgamento getTipoJulgamento(ObjetoIncidente<?> objetoIncidente) {
		if (objetoIncidente != null && objetoIncidente instanceof IncidenteJulgamento)
			return ((IncidenteJulgamento) objetoIncidente).getTipoJulgamento();

		return TipoIncidenteJulgamento.MERITO;
	}

	/**
	 * Retorna o TipoRecurso de um ObjetoIncidente, caso ele o tenha. Caso
	 * contrário, retorna null.
	 * 
	 * @param objetoIncidente
	 * @return TipoRecurso
	 */
	public static TipoRecurso getTipoRecurso(ObjetoIncidente<?> objetoIncidente) {
		if (objetoIncidente instanceof RecursoProcesso) {
			return ((RecursoProcesso) objetoIncidente).getTipoRecursoProcesso();
		} else if (objetoIncidente instanceof Peticao) {
			return ((Peticao) objetoIncidente).getTipoRecurso();
		} else if (objetoIncidente instanceof IncidenteJulgamento) {
			return ((IncidenteJulgamento) objetoIncidente).getTipoJulgamento();
		} else {
			return null;
		}

	}

	public static RecursoProcesso getRecursoProcesso(ObjetoIncidente<?> objetoIncidente) {
		if (objetoIncidente instanceof RecursoProcesso) {
			return (RecursoProcesso) objetoIncidente;
		} else {
			return null;
		}
	}

	public static IncidenteJulgamento getIncidenteJulgamento(ObjetoIncidente<?> objetoIncidente) {
		if (objetoIncidente instanceof IncidenteJulgamento) {
			return (IncidenteJulgamento) objetoIncidente;
		} else {
			return null;
		}
	}

	/**
	 * Recupera o Processo de um objetoIncidente. Retorna null se não houver
	 * 
	 * @param objetoIncidente
	 * @return
	 */
	public static Processo getProcesso(ObjetoIncidente<?> objetoIncidente) {
		if (objetoIncidente instanceof Processo) {
			return (Processo) objetoIncidente;
		} else if (objetoIncidente == null) {
			return null;
		} else {
			return (Processo) objetoIncidente.getPrincipal();
		}
	}

	public static Protocolo getProtocolo(ObjetoIncidente<?> objetoIncidente) {
		if (objetoIncidente instanceof Processo) {
			Processo p = (Processo) objetoIncidente;
			if (p.getAnterior() instanceof Protocolo) // pode ser uma
				// PeticaoEletronica
				return (Protocolo) p.getAnterior();
			else
				return null;
		} else if (objetoIncidente instanceof RecursoProcesso || objetoIncidente instanceof IncidenteJulgamento) {
			return getProtocolo(objetoIncidente.getPrincipal());
		} else if (objetoIncidente instanceof Protocolo) {
			return (Protocolo) objetoIncidente;
		} else {
			return null;
		}
	}

	public static Peticao getPeticao(ObjetoIncidente<?> objetoIncidente) {
		if (objetoIncidente != null) {
			if (objetoIncidente.getAnterior() instanceof Peticao) {
				return ((Peticao) objetoIncidente.getAnterior());
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * retorna a sigla da cadeia do objeto incidente contrário, retorna null.
	 * 
	 * @param objetoIncidente
	 * @return TipoRecurso
	 */
	public static String getSiglaCadeiaIncidente(ObjetoIncidente<?> objetoIncidente) {
		if (objetoIncidente instanceof RecursoProcesso) {
			return ((RecursoProcesso) objetoIncidente).getSiglaCadeiaIncidente();
		} else if (objetoIncidente instanceof IncidenteJulgamento) {
			return ((IncidenteJulgamento) objetoIncidente).getSiglaCadeiaIncidente();
		} else if (objetoIncidente instanceof Processo) {
			return ((Processo) objetoIncidente).getSiglaClasseProcessual();
		} else {
			return null;
		}

	}

}
