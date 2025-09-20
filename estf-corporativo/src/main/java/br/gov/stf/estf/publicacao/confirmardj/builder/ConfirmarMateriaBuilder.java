package br.gov.stf.estf.publicacao.confirmardj.builder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.TextoPeticao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.AndamentoProtocolo;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.Protocolo;
import br.gov.stf.estf.entidade.processostf.ProtocoloPublicado;
import br.gov.stf.estf.entidade.processostf.TipoMeioProcesso;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;
import br.gov.stf.estf.entidade.publicacao.Publicacao;
import br.gov.stf.estf.publicacao.model.service.ConfirmaDjService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.ApplicationFactory;

public abstract class ConfirmarMateriaBuilder<T extends ESTFBaseEntity> {
	private ConfirmaDjService confirmaDjService;
	private ConteudoPublicacao conteudoPublicacao;
	private SimpleDateFormat dateFormat;
	
	

	public ConfirmarMateriaBuilder (ConteudoPublicacao conteudoPublicacao) {
		confirmaDjService = (ConfirmaDjService) ApplicationFactory.getInstance().getServiceLocator().getService("confirmaDjService");
		this.conteudoPublicacao = conteudoPublicacao;
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	}
	
	protected List<ProcessoPublicado> pesquisarProcessosPublicados (ConteudoPublicacao conteudoPublicacao) throws ServiceException {
		return confirmaDjService.pesquisarProcessosPublicados(conteudoPublicacao);
	}
	
	@SuppressWarnings("rawtypes")
	protected Long getNumeroSequencial (ObjetoIncidente objetoIncidente) throws ServiceException {
		return confirmaDjService.recuperarNumeroSequencial(objetoIncidente);
	}
	protected Long getNumeroSequencial (Protocolo protocolo) throws ServiceException {
		return confirmaDjService.recuperarNumeroSequencial(protocolo);
	}
	protected List<TextoPeticao> pesquisarTextoPeticaos (ConteudoPublicacao conteudoPublicacao) throws ServiceException {
		return confirmaDjService.pesquisarTextoProtocolos (conteudoPublicacao);
	}
	protected List<ProtocoloPublicado> pesquisarProtocoloPublicado (ConteudoPublicacao conteudoPublicacao) throws ServiceException {
		return confirmaDjService.pesquisarProtocoloPublicado (conteudoPublicacao);
	}
	protected void inserirAndamentoProcesso (AndamentoProcesso andamentoProcesso, Publicacao publicacao, String observacao) throws ServiceException {
		String descricao = andamentoProcesso.getDescricaoObservacaoAndamento();
		
		if ( observacao!=null ) {
			descricao = descricao + " " +observacao;
		}
		descricao = descricao + " DJE nº "+publicacao.getNumeroEdicaoDje()+", divulgado em "+dateFormat.format(publicacao.getDataDivulgacaoDje()) ;
		andamentoProcesso.setDescricaoObservacaoAndamento( descricao.trim() );
		confirmaDjService.inserirAndamentoProcesso(andamentoProcesso);
	}
	protected void inserirAndamentoProtocolo (AndamentoProtocolo andamentoProtocolo) throws ServiceException {
		String descricao = andamentoProtocolo.getDescricaoObservacaoAndamento();
		andamentoProtocolo.setDescricaoObservacaoAndamento( descricao.trim() );
		confirmaDjService.inserirAndamentoProtocolo(andamentoProtocolo);
	}
	protected void inserirTextoAndamentoAcordao (ProcessoPublicado processoPublicado, AndamentoProcesso andamentoProcesso) throws ServiceException {
		confirmaDjService.inserirTextoAndamentoAcordao(processoPublicado, andamentoProcesso);
	}
	protected void inserirTextoAndamento( ProcessoPublicado processoPublicado, AndamentoProcesso andamentoProcesso) throws ServiceException {
		confirmaDjService.inserirTextoAndamento(processoPublicado, andamentoProcesso);
	}
	protected void alterarBaixaProcesso (Processo processo) throws ServiceException {
		confirmaDjService.alterarBaixaProcesso(processo);
	}
	protected void deslocarProcessoEletronico (Processo processo, Setor setor) throws ServiceException {
		confirmaDjService.alterarDeslocamentoProcessoEletronico(processo, setor);
	}
	protected void alterarSituacaoPecaProcessoEletronico (ProcessoPublicado processoPublicado) throws ServiceException {
		confirmaDjService.alterarSituacaoPecaProcessoEletronico(processoPublicado);
	}
	protected void inserirAndamentoIntimacao (ProcessoPublicado processoPublicado, String siglaUsuario, Setor setor) throws ServiceException {
		confirmaDjService.inserirAndamentoIntimacao(processoPublicado, siglaUsuario, setor);
	}
	
	protected String montarDescricaoObservacaoAcordaoRepublicado () {
		String dataPublicacao = dateFormat.format(new Date());
		return "DATA DE PUBLICAÇÃO DJE "+dataPublicacao+".";
	}
	protected String montarDescricaoObservacaoAcordao () {
		String dataPublicacao = dateFormat.format(new Date());
		return "DATA DE PUBLICAÇÃO DJE "+dataPublicacao+" - ATA Nº "+conteudoPublicacao.getNumero()+"/"+conteudoPublicacao.getAno()+".";
	}
	protected String montarDescricaoObservacaoAta () {
		return "ATA Nº "+conteudoPublicacao.getNumero()+", de "+dateFormat.format(conteudoPublicacao.getDataCriacao())+".";
	}
	protected String montarDescricaoObservacaoPauta () {
		return "PAUTA Nº "+conteudoPublicacao.getNumero()+"/"+conteudoPublicacao.getAno()+".";
	}
	protected String montarDescricaoObservacaoRepercussaoGeral () {
		return "DATA DE PUBLICAÇÃO DJE "+dateFormat.format(new Date())+" ATA Nº "+conteudoPublicacao.getNumero()+", de "+dateFormat.format(conteudoPublicacao.getDataCriacao())+".";
	}
	protected String montarDescricaoObservacaoProtocolo (Publicacao publicacao) {
		return "Publicado DJE nº "+publicacao.getNumeroEdicaoDje()+", divulgado em "+dateFormat.format(publicacao.getDataDivulgacaoDje())+".";
	}
	protected String montarDescricaoObservacaoProtocoloRepublicado (Publicacao publicacao) {
		return "DJE nº "+publicacao.getAnoEdicaoDje()+", divulgado em "+dateFormat.format(publicacao.getDataDivulgacaoDje())+".";
	}
	
	
	public void confirmar (Publicacao publicacao, String siglaUsuario, Setor setor, String observacao) throws ServiceException {
		List<T> lista = pesquisar(conteudoPublicacao);
		for ( T entidade: lista ) {
			confirmar(publicacao, siglaUsuario, setor, entidade, observacao);
			
			// VERIFICAR SE O PROCESSO É ELETRÔNICO
			if ( entidade instanceof ProcessoPublicado ) {
				ProcessoPublicado processoPublicado = (ProcessoPublicado) entidade;
				
				if ( ((Processo)processoPublicado.getObjetoIncidente().getPrincipal()).getTipoMeioProcesso().equals( TipoMeioProcesso.ELETRONICO ) ) {
					inserirAndamentoIntimacao(processoPublicado, siglaUsuario, setor);
					alterarSituacaoPecaProcessoEletronico(processoPublicado);
				}
			}
		}
	}
	
	public abstract List<T> pesquisar (ConteudoPublicacao conteudoPublicacao) throws ServiceException;
	public abstract void confirmar (Publicacao publicacao, String siglaUsuario, Setor setor, T entidade, String observacao) throws ServiceException;
	public abstract Long getCodigoAndamento (T entidade);
	
	
	public ConteudoPublicacao getConteudoPublicacao() {
		return conteudoPublicacao;
	}

	public void setConteudoPublicacao(ConteudoPublicacao conteudoPublicacao) {
		this.conteudoPublicacao = conteudoPublicacao;
	}
}
