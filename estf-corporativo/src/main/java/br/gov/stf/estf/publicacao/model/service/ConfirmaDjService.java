package br.gov.stf.estf.publicacao.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.documento.TextoPeticao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.AndamentoProtocolo;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.Protocolo;
import br.gov.stf.estf.entidade.processostf.ProtocoloPublicado;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;
import br.gov.stf.framework.model.service.ServiceException;

public interface ConfirmaDjService {
	public List<ProcessoPublicado> pesquisarProcessosPublicados (ConteudoPublicacao conteudoPublicacao) throws ServiceException;
	public Long recuperarNumeroSequencial (ObjetoIncidente objetoIncidente) throws ServiceException;
	public void inserirAndamentoProcesso (AndamentoProcesso andamentoProcesso) throws ServiceException;
	public void inserirAndamentoIntimacao (ProcessoPublicado processoPublicado, String siglaUsuario, Setor setor) throws ServiceException;
	public void alterarBaixaProcesso(Processo processo) throws ServiceException;
	public void alterarDeslocamentoProcessoEletronico (Processo processo, Setor setor)  throws ServiceException;
	public void alterarSituacaoPecaProcessoEletronico (ProcessoPublicado processoPublicado)  throws ServiceException;
	public List<TextoPeticao> pesquisarTextoProtocolos (ConteudoPublicacao conteudoPublicacao) throws ServiceException;
	public List<ProtocoloPublicado> pesquisarProtocoloPublicado (ConteudoPublicacao conteudoPublicacao) throws ServiceException;
	public Long recuperarNumeroSequencial (Protocolo protocolo) throws ServiceException;
	public void inserirAndamentoProtocolo (AndamentoProtocolo andamentoProtocolo) throws ServiceException;
	public void inserirTextoAndamentoAcordao(ProcessoPublicado processoPublicado,AndamentoProcesso andamentoProcesso) throws ServiceException;
	public void inserirTextoAndamento(ProcessoPublicado processoPublicado,AndamentoProcesso andamentoProcesso) throws ServiceException;
}
