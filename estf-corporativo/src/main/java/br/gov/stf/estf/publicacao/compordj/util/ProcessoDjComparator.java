package br.gov.stf.estf.publicacao.compordj.util;

import java.util.Comparator;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.TextoPeticao;
import br.gov.stf.estf.entidade.processostf.AcordaoAgendado;
import br.gov.stf.estf.entidade.processostf.IncidenteDistribuicao;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProtocoloPublicado;
import br.gov.stf.estf.entidade.processostf.RecursoProcesso;
import br.gov.stf.estf.entidade.publicacao.EstruturaPublicacao;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;

public class ProcessoDjComparator implements Comparator<ESTFBaseEntity> {
	
	/**
	 * Dependendendo do capítulo, a ordem será a definida pela SEJ ou será a padrão, implementada
	 * antes pelo Textual. Talvez essa ordem PADRAO poderá vir a ser a da SEJ. Para isso deve-se
	 * conversar com a SES.
	 * 
	 * @param oi
	 * @param codigoCapitulo
	 * @return
	 */
	private String getDescricaoObjetoIncidenteParaOrdenacao(ObjetoIncidente oi, Integer codigoCapitulo) {		
		return getDescricaoObjetoIncidenteParaOrdenacao_SEJ(oi);
	}
	
	
	/**
	 * Recupera a descrição do "processo" (processo em si, recurso ou incidente julgamento)
	 * para efeitos de comparação. 
	 * 
	 * Em conversa com o Lúcio e Tiago Peixoto no dia 06/10/2009, a regra é:
	 * - Se for processo, utilizar a descrição da classe e o número para ordenação;
	 * - Se for recurso, utilizar a descrição do recurso mais a descrição do processo e o número para ordenação;
	 * - Se for incidente julgamento, NÃO utilizar a descrição do incidente para ordenação. Basear nas duas 
	 * regras anteriores. Dica: se for diferente de processo, buscar o objeto incidente pai;
	 * 
	 * @param O processo a ser publicado
	 * @return A descrição para efeitos de ordenação
	 */		
	private String getDescricaoObjetoIncidenteParaOrdenacao_SEJ(ObjetoIncidente oi) {
		StringBuffer sbDesc = new StringBuffer();
		
		if (oi instanceof Processo) {
			sbDesc.append(((Processo)oi).getClasseProcessual().getDescricao());
		} else  {

			ObjetoIncidente oiCadeia = oi;
			while (oiCadeia != null) {

				if ( oiCadeia instanceof Processo ) {
					Processo oiProcesso = (Processo) oiCadeia;
					sbDesc.append(oiProcesso.getClasseProcessual().getDescricao());
					
					// Incluído o break por questão de segurança, ou seja, se um processo
					// for cadastrado como pai dele mesmo. Isso evita loop infinito caso 
					// a base de dados esteja inconsistente.
					break;
				} else if ( oiCadeia instanceof RecursoProcesso ) {
					RecursoProcesso oiRecursoProcesso = (RecursoProcesso) oiCadeia;
					sbDesc.append(oiRecursoProcesso.getTipoRecursoProcesso().getDescricaoTipoRecursoCadeia());
				} 
				
				oiCadeia = oiCadeia.getPai();
			}
		}
		
		
		return sbDesc.toString();			
	}
	
	private int compararDescricaoDoObjetoIncidente(String descricao1, String descricao2, 
			Processo processo1, Processo processo2) {
		int comp = descricao1.toLowerCase().replace('.', ' ').replace('ç', 'c').compareTo(
				descricao2.toLowerCase().replace('.', ' ').replace('ç', 'c'));

		if (comp == 0) {
			Long n1 = processo1.getNumeroProcessual();
			Long n2 = processo2.getNumeroProcessual();
			
			comp = n1.compareTo(n2);
		}	
		
		return comp;
	}
	
	public int compare(ESTFBaseEntity o1, ESTFBaseEntity o2) {
		String descricao1 = null;
		String descricao2 = null;
		int comp = 0;

		if (o1 instanceof ProcessoPublicado && o2 instanceof ProcessoPublicado) {
			ProcessoPublicado pp1 = (ProcessoPublicado) o1;
			ProcessoPublicado pp2 = (ProcessoPublicado) o2;
			
			descricao1 = getDescricaoObjetoIncidenteParaOrdenacao(pp1.getObjetoIncidente(),
					pp1.getCodigoCapitulo());
			descricao2 = getDescricaoObjetoIncidenteParaOrdenacao(pp2.getObjetoIncidente(),
					pp2.getCodigoCapitulo());
			
			comp = compararDescricaoDoObjetoIncidente(descricao1, descricao2, 
					(Processo) pp1.getObjetoIncidente().getPrincipal(), 
					(Processo) pp2.getObjetoIncidente().getPrincipal());

		} else if (o1 instanceof AcordaoAgendado && o2 instanceof AcordaoAgendado) {
			AcordaoAgendado aa1 = (AcordaoAgendado) o1;
			AcordaoAgendado aa2 = (AcordaoAgendado) o2;

			// O valor 2 como parâmetro é o capitulo ao qual diz respeito o Acórdão Agendado
			descricao1 = getDescricaoObjetoIncidenteParaOrdenacao(aa1.getObjetoIncidente(), 
					EstruturaPublicacao.COD_CAPITULO_PLENARIO);
			descricao2 = getDescricaoObjetoIncidenteParaOrdenacao(aa2.getObjetoIncidente(), 
					EstruturaPublicacao.COD_CAPITULO_PLENARIO);
			

			comp = compararDescricaoDoObjetoIncidente(descricao1, descricao2, 
					(Processo) aa1.getObjetoIncidente().getPrincipal(), 
					(Processo) aa2.getObjetoIncidente().getPrincipal());
			
		} else if (o1 instanceof IncidenteDistribuicao && o2 instanceof IncidenteDistribuicao) {
			IncidenteDistribuicao d1 = (IncidenteDistribuicao) o1;
			IncidenteDistribuicao d2 = (IncidenteDistribuicao) o2;
			
			descricao1 = getDescricaoObjetoIncidenteParaOrdenacao(d1.getObjetoIncidente(), 
					EstruturaPublicacao.COD_CAPITULO_PRESIDENCIA);
			descricao2 = getDescricaoObjetoIncidenteParaOrdenacao(d2.getObjetoIncidente(), 
					EstruturaPublicacao.COD_CAPITULO_PRESIDENCIA);
			
			comp = compararDescricaoDoObjetoIncidente(descricao1, descricao2, 
					(Processo) d1.getObjetoIncidente().getPrincipal(), 
					(Processo) d2.getObjetoIncidente().getPrincipal());
			
		} else if (o1 instanceof TextoPeticao && o2 instanceof TextoPeticao) {
			TextoPeticao tp1 = (TextoPeticao) o1;
			TextoPeticao tp2 = (TextoPeticao) o2;

			comp = tp1.getAno().compareTo(tp2.getAno());
			if (comp == 0) {
				comp = tp1.getNumero().compareTo(tp2.getNumero());
			}

		} else if (o1 instanceof ProtocoloPublicado && o2 instanceof ProtocoloPublicado) {
			ProtocoloPublicado tp1 = (ProtocoloPublicado) o1;
			ProtocoloPublicado tp2 = (ProtocoloPublicado) o2;

			comp = tp1.getProtocolo().getAnoProtocolo().compareTo(tp2.getProtocolo().getAnoProtocolo());
			if (comp == 0) {
				comp = tp1.getProtocolo().getNumeroProtocolo().compareTo(tp2.getProtocolo().getNumeroProtocolo());
			}
		}
		
		return comp;
	}
}
