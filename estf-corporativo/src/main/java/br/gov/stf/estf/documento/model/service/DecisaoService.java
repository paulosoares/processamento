package br.gov.stf.estf.documento.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.documento.model.util.TipoSessaoControleVoto;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.framework.model.service.ServiceException;

public interface DecisaoService {
	public void salvarDecisao (String siglaProcesso, Long numeroProcesso, Long tipoRecurso, Long tipoJulgamento, Integer codigoCapitulo,
			Integer codigoMateria, Integer numeroMateria, Short anoMateria, Date dataSessao, Ministro ministro, TipoSessaoControleVoto sessao, Boolean criarControleVoto,
			Long codigoAndamento, Setor setor, String siglaUsuario, byte[] decisao) throws ServiceException;
	
	@SuppressWarnings("rawtypes")
	@Deprecated
	public void salvarDecisaoListasJulgamentoEmLista (ObjetoIncidente objetoIncidenteListaJulgamento
													 ,String siglaProcesso
													 ,Long numeroProcesso
													 ,Long tipoRecurso
													 ,Long tipoJulgamento
													 ,Integer codigoCapitulo
													 ,Integer codigoMateria
													 ,Integer numeroMateria
													 ,Short anoMateria
													 ,Date dataSessao
													 ,Ministro ministro
													 ,TipoSessaoControleVoto sessao
													 ,Boolean criarControleVoto
													 ,Long codigoAndamento
													 ,Setor setor
													 ,ListaJulgamento listaJulgamento
													 ,String siglaUsuario
													 ,byte[] decisao) throws ServiceException;
	
	public void incluirRelatorAcordao(String siglaProcesso,
			Long numeroProcesso, Long tipoRecurso, Long tipoJulgamento,
			Long codigoMinistroAcordao, Date dataSessao, TipoSessaoControleVoto tipoSessao) throws ServiceException;

	@Deprecated
	public byte[] montarTextoDecisaoJulgamento(String tipo, String textoDecisao) throws ServiceException;
	
	public ObjetoIncidente<?> objetoIncidenteRecuperarPorId(Long id) throws ServiceException;
	
	@Deprecated
	public void salvarDecisaoListasJulgamentoMesmaDecisao(List<ObjetoIncidente> objetoIncidenteListaJulgamento			                 									                 						
				,ConteudoPublicacao materia			                 						
				,TipoSessaoControleVoto sessao
				,Boolean criarControleVoto
				,Long codigoAndamento
				,Setor setor
				,ListaJulgamento listaJulgamento
				,String siglaUsuario
				,byte[] decisao) throws ServiceException;
}
