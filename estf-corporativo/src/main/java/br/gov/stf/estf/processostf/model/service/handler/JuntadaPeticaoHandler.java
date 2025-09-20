package br.gov.stf.estf.processostf.model.service.handler;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.Andamento.Andamentos;
import br.gov.stf.estf.entidade.processostf.AndamentoPeticao;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoDependencia;
import br.gov.stf.estf.entidade.processostf.ProcessoDependencia.TipoProcessoDependenciaEnum;
import br.gov.stf.estf.processostf.model.service.AndamentoPeticaoService;
import br.gov.stf.estf.processostf.model.service.AndamentoService;
import br.gov.stf.estf.processostf.model.service.PeticaoService;
import br.gov.stf.estf.processostf.model.service.ProcessoDependenciaService;
import br.gov.stf.estf.processostf.model.service.exception.LancamentoIndevidoException;
import br.gov.stf.framework.model.service.ServiceException;

public class JuntadaPeticaoHandler extends AndamentoProcessoHandler {
	
	@Autowired
	private ProcessoDependenciaService processoDependenciaService; 
	
	@Autowired
	private PeticaoService peticaoService;

	@Autowired
	private AndamentoService andamentoService;
	
	@Autowired
	private AndamentoPeticaoService andamentoPeticaoService;

	@Override
	public void lancarAndamentoIndevido(Processo processoAndamento, AndamentoProcesso andamentoProcessoAnulado, AndamentoProcesso andamentoProcessoIndevido) throws ServiceException {
		
		ProcessoDependencia dependenciaProcesso = pesquisarDependenciaProcesso(andamentoProcessoAnulado);
		ProcessoDependencia dependenciaPeticao = pesquisarDependenciaPeticao(andamentoProcessoAnulado, dependenciaProcesso);

		Peticao peticao = peticaoService.recuperarPeticao(dependenciaPeticao.getIdObjetoIncidente());
		Andamento andamentoJuntadaAoProcesso = andamentoService.recuperarPorId(Andamentos.JUNTADA_AO_PROCESSO.getId());
		AndamentoPeticao andamentoPeticao = andamentoPeticaoService.pesquisar(andamentoJuntadaAoProcesso, peticao);
		
		// Muda a flag de válido do andamento petição.
		andamentoPeticao.setValido(false);
		andamentoPeticaoService.salvar(andamentoPeticao);

		AndamentoPeticao andamentoPeticaoIndevido = criarAndamentoPeticaoInvalida(peticao, andamentoPeticao);

		// Finalizar dependência petição.
		finalizarProcessoDependenciaPeticao(dependenciaPeticao, andamentoPeticaoIndevido);
	}
	
	@Override
	public void cancelarAndamentoIndevido(AndamentoProcesso andamentoProcessoAnulado) throws ServiceException {
		
		// Pesquisar as dependencias e desfazê-las.
		ProcessoDependencia dependenciaProcesso = pesquisarDependenciaProcesso(andamentoProcessoAnulado);
		ProcessoDependencia dependenciaPeticao = pesquisarDependenciaPeticao(andamentoProcessoAnulado, dependenciaProcesso);
		cancelarFinalizacaoProcessoDependenciaPeticao(dependenciaPeticao);		
		
		// Corrigir flag do andamento da petição.
		AndamentoPeticao andamentoPeticao = pesquisarAndamentoPeticao(dependenciaPeticao);
		andamentoPeticao.setValido(true);
		andamentoPeticaoService.salvar(andamentoPeticao);
		
		// Deletar o andamento da petição.
		AndamentoPeticao andamentoIndevidoPeticao = pesquisarAndamentoIndevidoPeticao(dependenciaPeticao);
		if (andamentoIndevidoPeticao == null) {
			throw new LancamentoIndevidoException("Não foi possível desfazer o lançamento indevido, pois não foi encontrado o andamento indevido da petição.");
		}
		andamentoPeticaoService.excluir(andamentoIndevidoPeticao);		
	}
	
	private ProcessoDependencia pesquisarDependenciaProcesso(AndamentoProcesso andamentoProcesso) throws ServiceException {
		
		ProcessoDependencia exemplo1 = new ProcessoDependencia();
		exemplo1.setTipoDependenciaProcesso(TipoProcessoDependenciaEnum.JUNTADA.getCodigo());
		exemplo1.setAndamentoProcesso(andamentoProcesso.getId());

		List<ProcessoDependencia> dependencias = processoDependenciaService.pesquisarPorExemplo(exemplo1);
		
		return (dependencias != null && dependencias.size() > 0) ? (ProcessoDependencia)dependencias.get(0) : null;
	}
	
	private ProcessoDependencia pesquisarDependenciaPeticao(AndamentoProcesso andamentoProcesso, ProcessoDependencia dependenciaProcesso) throws ServiceException {
		
		ProcessoDependencia exemplo2 = new ProcessoDependencia();
		exemplo2.setTipoDependenciaProcesso(TipoProcessoDependenciaEnum.JUNTADA.getCodigo());
		exemplo2.setIdObjetoIncidente(dependenciaProcesso.getIdObjetoIncidenteVinculado());
		exemplo2.setIdObjetoIncidenteVinculado(dependenciaProcesso.getIdObjetoIncidente());

		List<ProcessoDependencia> dependenciasPeticoes = processoDependenciaService.pesquisarPorExemplo(exemplo2);
		
		return (dependenciasPeticoes != null && dependenciasPeticoes.size() > 0) ? (ProcessoDependencia)dependenciasPeticoes.get(0) : null;
	}
	
	private AndamentoPeticao criarAndamentoPeticaoInvalida(Peticao peticao, AndamentoPeticao andamentoPeticao) throws ServiceException {
		
		AndamentoPeticao andamentoPeticaoInvalida = new AndamentoPeticao();

		Andamento andamentoInvalido = andamentoService.recuperarPorId(Andamentos.LANCAMENTO_INDEVIDO.getId());
		andamentoPeticaoInvalida.setTipoAndamento(andamentoInvalido);
		
		andamentoPeticaoInvalida.setDataAndamento(new Date());
		andamentoPeticaoInvalida.setAnoPeticao(peticao.getAnoPeticao());
		andamentoPeticaoInvalida.setNumeroPeticao(peticao.getNumeroPeticao());
		andamentoPeticaoInvalida.setValido(true);
		andamentoPeticaoInvalida.setNumeroSequencia(andamentoPeticaoService.recuperarUltimaSequencia(peticao.getObjetoIncidente()) + 1);
		andamentoPeticaoInvalida.setObjetoIncidente(peticao.getObjetoIncidente());
		andamentoPeticaoInvalida.setNumeroSequenciaErrado(andamentoPeticao.getNumeroSequencia());
		andamentoPeticaoInvalida.setUltimoAndamento(true);
		
		return andamentoPeticaoService.salvar(andamentoPeticaoInvalida);
	}
	
	private void finalizarProcessoDependenciaPeticao(ProcessoDependencia processoDependencia, AndamentoPeticao andamentoPeticao) throws LancamentoIndevidoException, ServiceException {
		
		if (processoDependencia == null) 
			throw new LancamentoIndevidoException("Nenhuma dependência foi encontrada para o processo ou petição.");
		
		if (processoDependencia.getDataFimDependencia() != null)
			throw new LancamentoIndevidoException("A dependência para o processo ou petição já foi finalizada.");

		processoDependencia.setDataFimDependencia(new Date());
		processoDependencia.setAndamentoPeticaoDesvinculador(andamentoPeticao.getId());
		processoDependenciaService.salvar(processoDependencia);
	}
	
	private void cancelarFinalizacaoProcessoDependenciaPeticao(ProcessoDependencia processoDependencia) throws LancamentoIndevidoException, ServiceException {

		if (processoDependencia == null) 
			throw new LancamentoIndevidoException("Nenhuma dependência foi encontrada para o processo ou petição.");
		
		if (processoDependencia.getDataFimDependencia() == null)
			throw new LancamentoIndevidoException("A dependência para o processo ou petição não está finalizada.");

		processoDependencia.setDataFimDependencia(null);
		processoDependencia.setAndamentoPeticaoDesvinculador(null);
		processoDependenciaService.salvar(processoDependencia);
	}
	
	private AndamentoPeticao pesquisarAndamentoPeticao(ProcessoDependencia dependenciaPeticao) throws LancamentoIndevidoException, ServiceException {

		Peticao peticao = peticaoService.recuperarPeticao(dependenciaPeticao.getIdObjetoIncidente());
		Andamento andamentoJuntadaAoProcesso = andamentoService.recuperarPorId(Andamentos.JUNTADA_AO_PROCESSO.getId());
		
		return andamentoPeticaoService.pesquisar(andamentoJuntadaAoProcesso, peticao);
	}
	
	private AndamentoPeticao pesquisarAndamentoIndevidoPeticao(ProcessoDependencia dependenciaPeticao) throws LancamentoIndevidoException, ServiceException {
	
		AndamentoPeticao andamentoPeticao = pesquisarAndamentoPeticao(dependenciaPeticao);
		Peticao peticao = peticaoService.recuperarPeticao(dependenciaPeticao.getIdObjetoIncidente());
		
		AndamentoPeticao exemplo = new AndamentoPeticao();
		exemplo.setNumeroSequenciaErrado(andamentoPeticao.getNumeroSequencia());
		exemplo.setAnoPeticao(peticao.getAnoPeticao());
		exemplo.setNumeroPeticao(peticao.getNumeroPeticao());
		
		List<AndamentoPeticao> andamentos = andamentoPeticaoService.pesquisarPorExemplo(exemplo);
		return (andamentos != null && andamentos.size() > 0) ? (AndamentoPeticao)andamentos.get(0) : null;
	}

	@Override
	public void posRegistroAndamento(AndamentoProcesso andamentoProcesso, Processo processoAndamento, List<Processo> processosPrincipais, Peticao peticao, Setor setor, String codigoUsuario, Origem origem, Comunicacao comunicacao) throws ServiceException {

		// Verificando se a petição já está vinculada ao processo.
		if (peticao.getObjetoIncidenteVinculado() == null || !peticao.getObjetoIncidenteVinculado().getId().equals(processoAndamento.getId())) {
			throw new ServiceException("Petição informada não está vinculada ao processo!");
		}
		
		// Verificar se a petição já foi juntada ao processo.
		if (processoDependenciaService.isPeticaoJuntada(processoAndamento, peticao)) {
			throw new ServiceException("A petição " + peticao.getIdentificacao() + " já foi juntada ao processo " + processoAndamento.getIdentificacao() + ".");
		}
		
		// Criar a dependência PROCESSO -> PETIÇÃO.
		processoDependenciaService.criarDependencia(andamentoProcesso, peticao, ProcessoDependencia.TipoProcessoDependenciaEnum.JUNTADA.getCodigo());
		
		// Criar andamento peticao.
		AndamentoPeticao andamentoPeticao = criarAndamentoPeticao(processoAndamento, peticao, setor, codigoUsuario);
		
		// Criar dependência PETIÇÃO -> PROCESSO.
		processoDependenciaService.criarDependencia(andamentoPeticao, peticao, processoAndamento,  ProcessoDependencia.TipoProcessoDependenciaEnum.JUNTADA.getCodigo());
	}
	
	private AndamentoPeticao criarAndamentoPeticao(Processo processo, Peticao peticao, Setor setor, String codigoUsuario) throws ServiceException {
		
		AndamentoPeticao andamentoPeticao = new AndamentoPeticao();
		
		Andamento andamentoJuntada = andamentoService.recuperarPorId(Andamentos.JUNTADA_AO_PROCESSO.getId());
		Date dataAtual = new Date();
		
		
		andamentoPeticao.setTipoAndamento(andamentoJuntada);
		andamentoPeticao.setDataAndamento(dataAtual);
		andamentoPeticao.setDataHoraSistema(dataAtual);
		andamentoPeticao.setDescricaoObservacaoAndamento(processo.getSiglaClasseProcessual() + "/" + processo.getNumeroProcessual());
		
		//TODO: O Processamento Inicial gera o sequencial 0(zero) para andamento de petição.
		andamentoPeticao.setNumeroSequencia(andamentoPeticaoService.recuperarUltimaSequencia(processo));
		andamentoPeticao.setSetor(setor);
		andamentoPeticao.setCodigoUsuario(codigoUsuario);
		andamentoPeticao.setDescricaoObservacaoInterna("");
		andamentoPeticao.setObjetoIncidente(peticao);
		andamentoPeticao.setValido(true);
		andamentoPeticao.setUltimoAndamento(true);
		
		andamentoPeticaoService.salvar(andamentoPeticao);
		
		return andamentoPeticao;
	}
	
	@Override
	public boolean precisaPeticao() {
		return true;
	}
}