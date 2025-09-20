package br.gov.stf.estf.assinatura.visao.jsf.beans.copiarPecas;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanCopiarPecas extends AssinadorBaseBean {

	private static final long serialVersionUID = 3374147835620099594L;

	private static final TipoObjetoIncidente[] TIPOS_OBJETOS_INCIDENTES = {
			TipoObjetoIncidente.PROCESSO, TipoObjetoIncidente.RECURSO,
			TipoObjetoIncidente.INCIDENTE_JULGAMENTO,
			TipoObjetoIncidente.PETICAO };

	private static final Object LISTA_ASSOCIADOS = new Object();

	private static final Object PROCESSO = new Object();
	private static final Object NUMERO_SEQUENCIA = new Object();
	private static final Object OBJETO_INCIDENTE = new Object();
	private static final Object SIGLA_NUMERO_ORIGEM = new Object();
	private static final Object SIGLA_NUMERO_DESTINO = new Object();
	private static final Object APAGAR_PECAS = new Object();
	private static final Object INSERIR_INFORMACAO = new Object();

	private List<CheckableDataTableRowWrapper> listaAssociados;

	private Long processo;
	private Long objetoIncidente;

	private String siglaNumeroOrigem;
	private String siglaNumeroDestino;

	private Integer numeroSequencia;

	private Boolean apagarPecas;
	private Boolean inserirInformacao;

	public BeanCopiarPecas() {
		restauraSessao();
	}

	private void restauraSessao() {
		if (getAtributo(PROCESSO) == null) {
			setAtributo(PROCESSO, processo);
		}
		setProcesso((Long) getAtributo(PROCESSO));

		if (getAtributo(LISTA_ASSOCIADOS) == null) {
			setAtributo(LISTA_ASSOCIADOS, listaAssociados);
		}
		setListaAssociados((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_ASSOCIADOS));

		if (getAtributo(NUMERO_SEQUENCIA) == null) {
			setAtributo(NUMERO_SEQUENCIA, numeroSequencia);
		}
		setNumeroSequencia((Integer) getAtributo(NUMERO_SEQUENCIA));

		if (getAtributo(OBJETO_INCIDENTE) == null) {
			setAtributo(OBJETO_INCIDENTE, objetoIncidente);
		}
		setObjetoIncidente((Long) getAtributo(OBJETO_INCIDENTE));

		if (getAtributo(SIGLA_NUMERO_ORIGEM) == null) {
			setAtributo(SIGLA_NUMERO_ORIGEM, siglaNumeroOrigem);
		}
		setSiglaNumeroOrigem((String) getAtributo(SIGLA_NUMERO_ORIGEM));

		if (getAtributo(SIGLA_NUMERO_DESTINO) == null) {
			setAtributo(SIGLA_NUMERO_DESTINO, siglaNumeroDestino);
		}
		setSiglaNumeroDestino((String) getAtributo(SIGLA_NUMERO_DESTINO));

		if (getAtributo(APAGAR_PECAS) == null) {
			setAtributo(APAGAR_PECAS, false);
		}
		setApagarPecas((Boolean) getAtributo(APAGAR_PECAS));

		if (getAtributo(INSERIR_INFORMACAO) == null) {
			setAtributo(INSERIR_INFORMACAO, false);
		}
		setInserirInformacao((Boolean) getAtributo(INSERIR_INFORMACAO));

	}

	private void atualizaSessao() {
		if(processo != null){
			setAtributo(PROCESSO, processo);
		}
		if(objetoIncidente != null){
			setAtributo(OBJETO_INCIDENTE, objetoIncidente);
		}
		
		setAtributo(LISTA_ASSOCIADOS, listaAssociados);
		setAtributo(NUMERO_SEQUENCIA, numeroSequencia);
		setAtributo(SIGLA_NUMERO_ORIGEM, siglaNumeroOrigem);
		setAtributo(SIGLA_NUMERO_DESTINO, siglaNumeroDestino);
		setAtributo(APAGAR_PECAS, apagarPecas);
		setAtributo(INSERIR_INFORMACAO, inserirInformacao);

		keepStateInHttpSession();
	}

	public void copiarPecasAction() {
		if (copiarPecas()) {
			limpar();
		}
		atualizaSessao();
	}

	public void limparAction() {
		limpar();
		atualizaSessao();
	}

	public List pesquisarObjetosIncidentesSuggestionBox(Object value) {
		List<ObjetoIncidente<?>> incidentes = new ArrayList<ObjetoIncidente<?>>();
		try {

			if (value == null || StringUtils.isVazia(value.toString())) {
				return new ArrayList<ObjetoIncidente<?>>();
			}

			List<Processo> processos = getProcessoService().pesquisarProcesso(
					value.toString().toUpperCase());

			if (processos != null && processos.size() != 1) {
				return processos;
			}

			for (Processo processo : processos) {
				incidentes.addAll(getObjetoIncidenteService().pesquisar(
						processo.getId(), TIPOS_OBJETOS_INCIDENTES));
			}

			return incidentes;
		} catch (Exception e) {
			reportarErro("Erro na pesquisa processual. ", e.getMessage());
			e.printStackTrace();
			return new ArrayList<ObjetoIncidente<?>>();
		}
	}

	public void marcarTodosDocumentos() {
		marcarOuDesmarcarTodas(listaAssociados);
		setListaAssociados(listaAssociados);
	}

	public List<SelectItem> getEscolherSequencia() {
		List<SelectItem> listaSequencia = new ArrayList<SelectItem>();

		listaSequencia.add(new SelectItem(1, "No início do processo"));
		listaSequencia.add(new SelectItem(2, "No fim do processo"));

		return listaSequencia;
	}

	private void carregarTipoPecaProcesso(Long processoId) {
		List<PecaProcessoEletronico> pecaProcessoEletronicos = new ArrayList<PecaProcessoEletronico>();
		try {
			Processo processo = getProcessoService().recuperarPorId(processoId);
			pecaProcessoEletronicos = getPecaProcessoEletronicoService().pesquisarPorProcesso(processo, false);
		} catch (ServiceException e) {
			e.printStackTrace();
			reportarErro("Erro ao recuperar as peças.");
		}
		setListaAssociados(getCheckableDataTableRowWrapperList(pecaProcessoEletronicos));
		setAtributo(LISTA_ASSOCIADOS, listaAssociados);
	}

	private boolean copiarPecas() {

		List<CheckableDataTableRowWrapper> listaSelecionados = retornarItensCheckableSelecionados(listaAssociados);

		if (processo == null || processo < 0L) {
			reportarAviso("É necessário selecionar uma origem.");
			return false;
		}

		if (objetoIncidente == null || objetoIncidente < 0L) {
			reportarAviso("É necessário selecionar um destino.");
			return false;
		}

		if (numeroSequencia == null || numeroSequencia == 0L) {
			reportarAviso("É nescessário escolher uma ordem de inserção.");
			return false;
		}

		if (listaSelecionados == null || listaSelecionados.size() == 0) {
			reportarAviso("É necessário selecionar pelo menos uma peça eletronica.");
			return false;
		}
		
		if(processo.equals(objetoIncidente)){
			reportarAviso("A origem e o destino não podem ser iguais.");
			return false;
		}

		List<PecaProcessoEletronico> pecasSelecionadas = new ArrayList<PecaProcessoEletronico>();

		for (CheckableDataTableRowWrapper check : listaSelecionados) {
			pecasSelecionadas.add((PecaProcessoEletronico) check
					.getWrappedObject());
		}
		 Boolean crescente = (numeroSequencia == 1) ? true : false;
		 
		try {
			Processo processo = getProcessoService().recuperarPorId(this.processo);
			ObjetoIncidente<?> objetoIncidente = getObjetoIncidenteService().recuperarPorId(this.objetoIncidente);
			getArquivoProcessoEletronicoService().copiarPecasEletronicas(processo, objetoIncidente, pecasSelecionadas, apagarPecas,	inserirInformacao, crescente);
		} catch (ServiceException e) {
			e.printStackTrace();
			reportarErro("Erro ao copiar as peças.");
			return false;
		}

		reportarInformacao("As peças foram copiadas com sucesso.");
		return true;

	}

	private void limpar() {
		processo = null;
		listaAssociados = null;
		numeroSequencia = null;
		objetoIncidente = null;
		siglaNumeroOrigem = null;
		siglaNumeroDestino = null;
		apagarPecas = null;
		inserirInformacao = null;
		
		setAtributo(PROCESSO, processo);
		setAtributo(OBJETO_INCIDENTE, objetoIncidente);

	}

	// Getter´s and Setter´s //

	public Long getProcesso() {
		return processo;
	}

	public void setProcesso(Long processo) {
		this.processo = processo;
		if (processo != null) {
			if(objetoIncidente != null && objetoIncidente.equals(processo)){
				reportarAviso("A origem e o destino não podem ser iguais");
				this.processo = null;	
				setAtributo(PROCESSO, this.processo);
				return ;
			}
			carregarTipoPecaProcesso(processo);
			atualizaSessao();
		}
	}

	public Long getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(Long objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
		if (objetoIncidente != null) {
			if(processo != null && processo.equals(objetoIncidente)){
				reportarAviso("A origem e o destino não podem ser iguais");
				this.objetoIncidente = null;	
				setAtributo(OBJETO_INCIDENTE, this.objetoIncidente);
			}
			atualizaSessao();
		}
	}

	public List<CheckableDataTableRowWrapper> getListaAssociados() {
		return listaAssociados;
	}

	public void setListaAssociados(
			List<CheckableDataTableRowWrapper> listaAssociados) {
		this.listaAssociados = listaAssociados;
	}

	public Integer getNumeroSequencia() {
		return numeroSequencia;
	}

	public void setNumeroSequencia(Integer numeroSequencia) {
		this.numeroSequencia = numeroSequencia;
	}

	public String getSiglaNumeroOrigem() {
		return siglaNumeroOrigem;
	}

	public void setSiglaNumeroOrigem(String siglaNumeroOrigem) {
		this.siglaNumeroOrigem = siglaNumeroOrigem;
	}

	public String getSiglaNumeroDestino() {
		return siglaNumeroDestino;
	}

	public void setSiglaNumeroDestino(String siglaNumeroDestino) {
		this.siglaNumeroDestino = siglaNumeroDestino;
	}

	public Boolean getApagarPecas() {
		return apagarPecas;
	}

	public void setApagarPecas(Boolean apagarPecas) {
		this.apagarPecas = apagarPecas;
	}

	public Boolean getInserirInformacao() {
		return inserirInformacao;
	}

	public void setInserirInformacao(Boolean inserirInformacao) {
		this.inserirInformacao = inserirInformacao;
	}

}
