/**
 * 
 */
package br.jus.stf.estf.decisao.objetoincidente.support;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.gov.stf.estf.entidade.julgamento.InformacaoPautaProcesso;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;

/**
 * @author Paulo.Estevao 
 * @since 18.02.2013. */
public class InformacoesJulgamentoReport {

	public static final String TEMPLATE_RELATORIO = "/relatorio/template/informacoesJulgamentoReportTemplate.html";
	
	private List<SessaoReport> sessoes;
	private List<ObjetoIncidenteReport> objetosIncidente;
	private List<ObjetoIncidenteReport> objetosIncidenteSemSessao;
	
	public List<SessaoReport> getSessoes() {
		if (sessoes == null) {
			sessoes = new ArrayList<SessaoReport>();
		}
		return sessoes;
	}
	
	public void setSessoes(List<SessaoReport> sessoes) {
		this.sessoes = sessoes;
	}
	
	public List<ObjetoIncidenteReport> getObjetosIncidente() {
		if (objetosIncidente == null) {
			objetosIncidente = new ArrayList<ObjetoIncidenteReport>();
		}
		return objetosIncidente;
	}
	
	public void setObjetosIncidente(List<ObjetoIncidenteReport> objetosIncidente) {
		this.objetosIncidente = objetosIncidente;
	}
	
	public List<ObjetoIncidenteReport> getObjetosIncidenteSemSessao() {
		if (objetosIncidenteSemSessao == null) {
			objetosIncidenteSemSessao = new ArrayList<ObjetoIncidenteReport>();
		}
		return objetosIncidenteSemSessao;
	}
	
	public void setObjetosIncidenteSemSessao(List<ObjetoIncidenteReport> objetosIncidenteSemSessao) {
		this.objetosIncidenteSemSessao = objetosIncidenteSemSessao;
	}
	
	public boolean hasSessao(Sessao sessao) {
		return getSessaoReport(sessao) != null;
	}
	
	public SessaoReport getSessaoReport(Sessao sessao) {
		SessaoReport obj = new SessaoReport();
		obj.setSessao(sessao);
		for (SessaoReport sessaoReport : getSessoes()) {
			if (sessaoReport.equals(obj)) {
				return sessaoReport;
			}
		}
		return null;
	}
	
	public static class SessaoReport {
		private Sessao sessao;
		private List<ObjetoIncidenteReport> objetosIncidente;
		
		public Sessao getSessao() {
			return sessao;
		}
		
		public void setSessao(Sessao sessao) {
			this.sessao = sessao;
		}
		
		public List<ObjetoIncidenteReport> getObjetosIncidente() {
			if (objetosIncidente == null) {
				objetosIncidente = new ArrayList<ObjetoIncidenteReport>();
			}
			return objetosIncidente;
		}
		
		public void setObjetosIncidente(List<ObjetoIncidenteReport> objetosIncidente) {
			this.objetosIncidente = objetosIncidente;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof SessaoReport) {
				return sessao.equals(((SessaoReport) obj).getSessao());
			}
			return false;
		}
		
		public String getCabecalhoSessao() {
			if (sessao == null) {
				return null;
			}
			StringBuffer cabecalhoSessao = new StringBuffer();
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - EEEE - ");
			cabecalhoSessao.append(formatter.format(sessao.getDataInicio()));
			cabecalhoSessao.append("INÍCIO ÀS ");
			formatter = new SimpleDateFormat("HH:mm");
			cabecalhoSessao.append(formatter.format(sessao.getDataInicio()));
			return cabecalhoSessao.toString().toUpperCase();
		}
	}
	
	public static class ObjetoIncidenteReport {
		
		private ObjetoIncidente<?> objetoIncidente;
		private InformacaoPautaProcesso informacaoPautaProcesso;
		private Ministro relator;
		private List<Ministro> ministrosVista;
		
		public ObjetoIncidente<?> getObjetoIncidente() {
			return objetoIncidente;
		}
		
		public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
			this.objetoIncidente = objetoIncidente;
		}
		
		public InformacaoPautaProcesso getInformacaoPautaProcesso() {
			return informacaoPautaProcesso;
		}
		
		public void setInformacaoPautaProcesso(InformacaoPautaProcesso informacaoPautaProcesso) {
			this.informacaoPautaProcesso = informacaoPautaProcesso;
		}
		
		public Ministro getRelator() {
			return relator;
		}
		
		public void setRelator(Ministro relator) {
			this.relator = relator;
		}
		
		public List<Ministro> getMinistrosVista() {
			return ministrosVista;
		}
		
		public void setMinistrosVista(List<Ministro> ministrosVista) {
			this.ministrosVista = ministrosVista;
		}
		
		public String getPrimeiroParagrafoTese() {
			if (informacaoPautaProcesso == null || StringUtils.isBlank(informacaoPautaProcesso.getTeseEspelho())) {
				return null;
			}
			
			String[] paragrafos = informacaoPautaProcesso.getTeseEspelho().split("\n");
			
			return paragrafos[0]; 
		}
		
		public String getInformacoesEspelho() {
			if (informacaoPautaProcesso == null) {
				return null;
			}
			
			return informacaoPautaProcesso.getInformacoesEspelho();
		}
		
		public String getCabecalhoObjetoIncidente() {
			StringBuffer cabecalho = new StringBuffer();
			
			// Identificação do objeto incidente 
			cabecalho.append(objetoIncidente.getIdentificacao());
			
			// Relator e pedido de vista
			if (relator != null || (ministrosVista != null && ministrosVista.size() > 0)) {
				cabecalho.append(" (");
				if (relator != null) {
					cabecalho.append("R: " + relator.getSigla());
					if (ministrosVista != null && ministrosVista.size() > 0) {
						cabecalho.append("; ");
						cabecalho.append("V: " );
						cabecalho.append(getMinistrosVistaString());
						
					}
					
				} else {
					cabecalho.append("V: ");
					cabecalho.append(getMinistrosVistaString());
				}
				cabecalho.append(")");
			}
			
			// Pauta Temática
			if (informacaoPautaProcesso != null && informacaoPautaProcesso.getSubtemaPauta() != null) {
				cabecalho.append(" [P");
				cabecalho.append(informacaoPautaProcesso.getSubtemaPauta().getTemaPauta().getPautaPlenario().getNumero());
				cabecalho.append(".");
				cabecalho.append(informacaoPautaProcesso.getSubtemaPauta().getTemaPauta().getOrdem());
				cabecalho.append(".");
				cabecalho.append(informacaoPautaProcesso.getSubtemaPauta().getOrdem());
				cabecalho.append("]");
			}
			return cabecalho.toString();
		}

		private String getMinistrosVistaString() {
			StringBuffer vista = new StringBuffer();
			for (Ministro ministro : ministrosVista) {
				if (ministrosVista.indexOf(ministro) > 0) {
					if (ministrosVista.indexOf(ministro) == ministrosVista.size() - 1) {
						vista.append(" e ");
					} else {
						vista.append(", ");
					}
				}
				vista.append(ministro.getSigla());
			}
			return vista.toString();
		}
	}
	
	public static class SessaoReportComparator implements Comparator<SessaoReport> {

		@Override
		public int compare(SessaoReport o1, SessaoReport o2) {
			return o1.getSessao().getDataInicio().compareTo(o2.getSessao().getDataInicio());
		}
		
	}
	
	public static class ObjetoIncidenteReportComparator implements Comparator<ObjetoIncidenteReport> {

		@Override
		public int compare(ObjetoIncidenteReport o1, ObjetoIncidenteReport o2) {
			// Ordenação de ministro por antiguidade - data de posse
			int comparision = o1.getRelator().getDataPosse().compareTo(o2.getRelator().getDataPosse());
			
			if (comparision != 0) {
				return comparision;
			}
			
			// Ordenação de ministro por antiguidade - código (se empatar a data de posse
			comparision = o1.getRelator().getId().compareTo(o2.getRelator().getId());
			
			if (comparision != 0) {
				return comparision;
			}
			
			// Após ordenar por ministro, ordenar por processo
			comparision = ((Processo) o1.getObjetoIncidente().getPrincipal()).getSiglaClasseProcessual().compareTo(
							((Processo) o2.getObjetoIncidente().getPrincipal()).getSiglaClasseProcessual());
			
			if (comparision != 0) {
				return comparision;
			}
			
			comparision = ((Processo) o1.getObjetoIncidente().getPrincipal()).getNumeroProcessual().compareTo(
							((Processo) o2.getObjetoIncidente().getPrincipal()).getNumeroProcessual());
			
			if (comparision != 0) {
				return comparision;
			}
			
			comparision = o1.getObjetoIncidente().getIdentificacao().compareTo(
							o2.getObjetoIncidente().getIdentificacao());
			
			return comparision;
		}
		
	}
}
