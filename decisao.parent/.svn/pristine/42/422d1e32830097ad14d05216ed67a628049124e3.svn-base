package br.jus.stf.estf.decisao.pesquisa.domain;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;

import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.IncidentePreferencia;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.RecursoProcesso;
import br.gov.stf.estf.entidade.processostf.TipoIncidentePreferencia.TipoPreferenciaContante;
import br.gov.stf.estf.entidade.processostf.TipoMeioProcesso;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.jus.stf.estf.decisao.support.query.Dto;

/**
 * DTO para os dados de Objeto Incidente retornados na pesquisa avançada.
 * 
 * <p>
 * O objetivo é tornar a pesquisa mais eficiente, retornando somente os dados
 * utilizados na apresentação do resultado.
 * 
 * @author Rodrigo Barreiros
 * @since 07.04.2010
 */
public class ObjetoIncidenteDto implements Dto {

	private static final long serialVersionUID = -1129474336470352890L;

	private Long id;
	private TipoObjetoIncidente tipo;
	private TipoMeioProcesso tipoProcesso;
	private String siglaProcesso;
	private Long numeroProcesso;
	private Long idRelator;
	private String nomeRelator;
	private String cadeia;
	private String assunto;
	private boolean selected;
	private boolean fake;
	private boolean criminal;
	private String identificacao;
	private Integer ordemNaLista;
	private String identificacaoLista;
	private boolean temVistas;
	private String tipoAmbiente;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoMeioProcesso getTipoProcesso() {
		return tipoProcesso;
	}

	public void setTipoProcesso(TipoMeioProcesso tipoProcesso) {
		this.tipoProcesso = tipoProcesso;
	}

	public TipoObjetoIncidente getTipo() {
		return tipo;
	}

	public void setTipo(TipoObjetoIncidente tipo) {
		this.tipo = tipo;
	}

	public String getSiglaProcesso() {
		return siglaProcesso;
	}

	public void setSiglaProcesso(String siglaProcesso) {
		this.siglaProcesso = siglaProcesso;
	}

	public Long getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(Long numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public Long getIdRelator() {
		return idRelator;
	}

	public void setIdRelator(Long idRelator) {
		this.idRelator = idRelator;
	}

	public String getNomeRelator() {
		return nomeRelator;
	}

	public void setNomeRelator(String nomeRelator) {
		this.nomeRelator = nomeRelator;
	}

	/**
	 * Retorna a cadeia de incidentes do processo. Para a apresentação, deve
	 * retirar a sigla do processo, que precede toda a cadeia.
	 * 
	 * @return
	 */
	public String getCadeia() {
		if (cadeia != null && siglaProcesso != null) {
			return cadeia.replaceFirst(siglaProcesso + "-", "");
		}
		return cadeia;
	}

	public void setCadeia(String cadeia) {
		this.cadeia = cadeia;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isFake() {
		return fake;
	}

	public void setFake(boolean fake) {
		this.fake = fake;
	}

	public boolean isEletronico() {
		return tipoProcesso.equals(TipoMeioProcesso.ELETRONICO);
	}

	public String getIdentificacao() {
		if (isFake()) {
			return String.format("%s %s %s", siglaProcesso, numeroProcesso,
					getCadeia());
		} else {
			String cadeiaFormatada = StringUtils.isNotBlank(getCadeia()) ? getCadeia()
					: "Mérito";

			return String.format("%s %s %s", siglaProcesso, numeroProcesso,
					cadeiaFormatada);
		}
	}

	public boolean equals(Object other) {
		if (!(other instanceof ObjetoIncidenteDto))
			return false;
		ObjetoIncidenteDto castOther = (ObjetoIncidenteDto) other;
		return this.getId().equals(castOther.getId());
	}

	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public String toString() {
		return getIdentificacao();
	}

	/**
	 * Cria uma instância de ObjetoIncidente a partir dos valores presente em um
	 * dado {@link ObjetoIncidente}
	 * 
	 * @param objetoIncidente
	 *            o objetoIncidente de entrada
	 * @return a instância de ObjetoIncidenteDto
	 */
	public static ObjetoIncidenteDto valueOf(ObjetoIncidente<?> objetoIncidente) {
		return valueOf(objetoIncidente, false);
	}

	/**
	 * Recupera uma instância do ObjetoIncidenteDto, informando se precisará
	 * apenas do Id, pois o carregamento de todos os dados provoca uma redução
	 * de performance.
	 * 
	 * @param objetoIncidente
	 *            O objetoIncidente de entrada
	 * @param somenteId
	 *            Indicador se será carregado apenas o id do objetoIncidente.
	 * @return a instância de ObjetoIncidenteDto
	 */
	public static ObjetoIncidenteDto valueOf(ObjetoIncidente<?> objetoIncidente, boolean somenteId) {
		ObjetoIncidenteDto dto = new ObjetoIncidenteDto();
		dto.setId(objetoIncidente.getId());

		if (!somenteId) {
			Hibernate.initialize(((Processo) objetoIncidente.getPrincipal()).getSiglaClasseProcessual());
			dto.setSiglaProcesso(((Processo) objetoIncidente.getPrincipal()).getSiglaClasseProcessual());
			dto.setNumeroProcesso(((Processo) objetoIncidente.getPrincipal()).getNumeroProcessual());
			dto.setTipo(objetoIncidente.getTipoObjetoIncidente());
			
			if (dto.getTipo() != null) {
				if (dto.getTipo().equals(TipoObjetoIncidente.PROCESSO)) {
					dto.setCadeia("Mérito");
				} else if (dto.getTipo().equals(TipoObjetoIncidente.RECURSO)) {
					String cadeia = ((RecursoProcesso) objetoIncidente).getSiglaCadeiaIncidente();
					dto.setCadeia(cadeia);
				} else if (dto.getTipo().equals(TipoObjetoIncidente.INCIDENTE_JULGAMENTO)) {
					String cadeia = ((IncidenteJulgamento) objetoIncidente).getSiglaCadeiaIncidente();
					dto.setCadeia(cadeia);
				}
			}
			
			dto.setTipoProcesso(((Processo) objetoIncidente.getPrincipal()).getTipoMeioProcesso());
			
			if (((Processo) objetoIncidente.getPrincipal()).getMinistroRelatorAtual() != null) {
				Hibernate.initialize(((Processo) objetoIncidente.getPrincipal()).getMinistroRelatorAtual());
				dto.setIdRelator(((Processo) objetoIncidente.getPrincipal()).getMinistroRelatorAtual().getId());
				dto.setNomeRelator(((Processo) objetoIncidente.getPrincipal()).getMinistroRelatorAtual().getNome());
			}

			dto.setCriminal(objetoIncidente.getPrincipal().isCriminal());
		}
		
		return dto;
	}

	public static ObjetoIncidenteDto valueOfForMobile(
			ObjetoIncidente<?> objetoIncidente) {
		ObjetoIncidenteDto dto = new ObjetoIncidenteDto();
		dto.setId(objetoIncidente.getId());

		dto.setSiglaProcesso(((Processo) objetoIncidente.getPrincipal())
				.getSiglaClasseProcessual());
		dto.setNumeroProcesso(((Processo) objetoIncidente.getPrincipal())
				.getNumeroProcessual());
		dto.setTipo(objetoIncidente.getTipoObjetoIncidente());
		if (dto.getTipo() != null) {
			if (dto.getTipo().equals(TipoObjetoIncidente.PROCESSO)) {
				dto.setCadeia("Mérito");
			} else if (dto.getTipo().equals(TipoObjetoIncidente.RECURSO)) {
				dto.setCadeia(((RecursoProcesso) objetoIncidente)
						.getSiglaCadeiaIncidente());
			} else if (dto.getTipo().equals(
					TipoObjetoIncidente.INCIDENTE_JULGAMENTO)) {
				dto.setCadeia(((IncidenteJulgamento) objetoIncidente)
						.getSiglaCadeiaIncidente());
			}
		}
		dto.setTipoProcesso(((Processo) objetoIncidente.getPrincipal())
				.getTipoMeioProcesso());
		return dto;
	}

	public Boolean getEletronico() {
		return TipoMeioProcesso.ELETRONICO.equals(getTipoProcesso());
	}

	public boolean isCriminal() {
		return criminal;
	}

	public void setCriminal(boolean criminal) {
		this.criminal = criminal;
	}

	public void setIdentificacao(String identificacao) {
		this.identificacao = identificacao;
	}

	public Integer getOrdemNaLista() {
		return ordemNaLista;
	}

	public void setOrdemNaLista(Integer ordemNaLista) {
		this.ordemNaLista = ordemNaLista;
	}

	public String getIdentificacaoLista() {
		return identificacaoLista;
	}

	public void setIdentificacaoLista(String identificacaoLista) {
		this.identificacaoLista = identificacaoLista;
	}

	public boolean isTemVistas() {
		return temVistas;
	}

	public void setTemVistas(boolean temVistas) {
		this.temVistas = temVistas;
	}

	public String getTipoAmbiente() {
		return tipoAmbiente;
	}

	public void setTipoAmbiente(String tipoAmbiente) {
		this.tipoAmbiente = tipoAmbiente;
	}

}
