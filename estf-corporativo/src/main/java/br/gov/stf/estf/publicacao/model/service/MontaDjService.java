package br.gov.stf.estf.publicacao.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TextoPeticao;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.AcordaoAgendado;
import br.gov.stf.estf.entidade.processostf.Assunto;
import br.gov.stf.estf.entidade.processostf.IncidenteDistribuicao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProtocoloPublicado;
import br.gov.stf.estf.entidade.processostf.TextoAssociadoProtocolo;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;
import br.gov.stf.framework.model.service.ServiceException;


public interface MontaDjService {
	
	public byte[] recuperarTextoAberturaDistribuicao (ConteudoPublicacao conteudoPublicacao) throws ServiceException;
	
	public byte[] recuperarTextoAberturaSessaoEspecial () throws ServiceException;
	
	public byte[] recuperarTextoFechamentoSessaoEspecial () throws ServiceException;
	
	public byte[] recuperarTextoAberturaSessaoEspecialADPF () throws ServiceException;
	
	public byte[] recuperarTextoFechamentoDistribuicao (ConteudoPublicacao conteudoPublicacao) throws ServiceException;
	
	public List<ProcessoPublicado> pesquisarRelacaoProcessoPublicado  (boolean ordenar, ConteudoPublicacao conteudoPublicacao) throws ServiceException;
	
	public List<IncidenteDistribuicao> pesquisarRelacaoProcessoDistribuido (boolean ordenar, ConteudoPublicacao conteudoPublicacao) throws ServiceException;
	
	public List<Texto> pesquisarTextoDecisaoProcessoPublicado (ProcessoPublicado processoPublicado, Date dataSessao) throws ServiceException;
	
	public List<Texto> pesquisarRelacaoTextoDecisaoAcordaoAgendado (AcordaoAgendado acordaoAgendado, Date dataSessao) throws ServiceException;
	
	public Texto recuperarTextoEmentaProcessoPublicado (ProcessoPublicado processoPublicado) throws ServiceException;
	
	public Texto recuperarTextoEmentaAcordaoAgendado (AcordaoAgendado acordaoAgendado) throws ServiceException;
	
	public List<Texto> pesquisarTextoDecisaoRepercussaoGeral (ProcessoPublicado processoPublicado, Date dataSessao) throws ServiceException;

	public Texto recuperarTextoEmentaRepercussaoGeral (ProcessoPublicado processoPublicado) throws ServiceException;
	
	public TextoAssociadoProtocolo recuperarTextoRepublicacaoProtocoloPublicado (ProtocoloPublicado protocoloPublicado) throws ServiceException;
	
	public TextoAssociadoProtocolo recuperarTextoObservacaoProtocoloPublicado (ProtocoloPublicado protocoloPublicado) throws ServiceException;
	
	public List<TextoPeticao> pesquisarTextoPeticao (boolean ordenar, ConteudoPublicacao conteudoPublicacao) throws ServiceException;
	
	public List<TextoPeticao> pesquisarTextoProtocolo (boolean ordenar, ConteudoPublicacao conteudoPublicacao) throws ServiceException;
	
	public List<AcordaoAgendado> pesquisarAcordaosSessaoEspecial (boolean ordenar, String ...siglaClasseProcessual) throws ServiceException;
	
	public List<ProcessoPublicado> pesquisarProcessosSessaoEspecial (boolean ordenar, String ...siglaClasseProcessual) throws ServiceException;
	
	public List<ProtocoloPublicado> pesquisarRelacaoProtocoloPublicado(boolean ordenar, ConteudoPublicacao conteudoPublicacao) throws ServiceException;

	public List<Texto> pesquisarTextoEditalProcessoPublicado(
			ProcessoPublicado processoPublicado, Date dataSessao) throws ServiceException;

	public Texto recuperarTextoDecisaoProcessoPublicadoAta(
			ProcessoPublicado processoPublicado) throws ServiceException;

	public byte[] recuperarTextoAberturaDistribuicaoPresidente(
			ConteudoPublicacao conteudoPublicacao) throws ServiceException;

	public Ministro recuperarMinistroRelator(Processo processo) throws ServiceException;

	public Assunto recuperarAssuntoProcesso(Processo processo) throws ServiceException;

	public Date recuperarDataCriacaoMateria(ProcessoPublicado processoPublicado) throws ServiceException;
	
}
