package br.gov.stf.estf.publicacao.model.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.publicacao.EstruturaPublicacao;
import br.gov.stf.estf.entidade.publicacao.TipoSessao;
import br.gov.stf.estf.jurisdicionado.model.service.impl.ConsultaConteudoPublicacao;
import br.gov.stf.estf.publicacao.model.dataaccess.ConteudoPublicacaoDao;
import br.gov.stf.estf.publicacao.model.util.IConsultaDeDadosDePublicacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ConteudoPublicacaoService extends GenericService<ConteudoPublicacao, Long, ConteudoPublicacaoDao> {

	public List<ConteudoPublicacao> pesquisarMateriasDJ(Long idPublicacao) throws ServiceException;

	public List<ConteudoPublicacao> pesquisarMateriasPublicacao() throws ServiceException;

	public List<ConteudoPublicacao> pesquisarMateriasPrevia(Date dataComposicao) throws ServiceException;

	public List<ConteudoPublicacao> pesquisarMateriasPublicacaoAntigo(Long idPublicacao) throws ServiceException;

	/**
	 * Pesquisar Materias 
	 * @param codigoCapitulo
	 * @param codigoMateria pesquisa apenas um codigo de Materia
	 * @param listCodigosMateria array com varios codigos de materia
	 * @param codigoConteudo
	 * @param numero
	 * @param anoMateria
	 * @param dataCriacao
	 * @param isPublicado se a data de publicação está ou não em branco
	 * @param isComposto se a data de composição parcial está ou não em branco
	 * @param tipoSessao
	 * @return
	 * @throws DaoException
	 */	
	public List<ConteudoPublicacao> pesquisarMateria(Integer codigoCapitulo
													,Integer codigoMateria
													,List<Integer> listCodigosMateria
													,Integer codigoConteudo
													,Integer numero
													,Short anoMateria
													,Date dataCriacao
													,Boolean isPublicado
													,Boolean isComposto
													,TipoSessao tipoSessao) throws ServiceException;	
	
	
	public List<ConteudoPublicacao> pesquisar( Integer codigoCapitulo
											 , Integer codigoMateria
											 , Integer codigoConteudo
											 , Integer numero
											 , Short ano
											 , Date dataCriacao) throws ServiceException;
	
	public List<ConteudoPublicacao> pesquisar( Integer codigoCapitulo, List<Integer> codigoMateria, 
			                                   Integer codigoConteudo, Short ano, Boolean capituloJaComposto) throws ServiceException;
	
	public List<ConteudoPublicacao> pesquisarNaoPublicado(Integer codigoCapitulo, Integer codigoMateria, Integer numero, Short ano)
			throws ServiceException;

	public Date recuperarDataAta(Integer codigoCapitulo, Integer codigoMateria, Integer numero, Short ano)
			throws ServiceException;

	public Date recuperarDataPrevistaPublicacao(Integer codigoCapitulo, Integer codigoMateria, Integer numero, Short ano,
			Date dataCriacao) throws ServiceException;

	public ConteudoPublicacao recuperar(Integer codigoCapitulo, Integer codigoMateria, Integer codigoConteudo, Integer numero, Short ano) throws ServiceException;

	public List<ConteudoPublicacao> recuperar(Integer codigoCapitulo, List<Integer> codigoMateria, Integer codigoConteudo, Integer numero, Short ano, Date dataCriacao) throws ServiceException;

	public Integer recuperarNumeroProcessosMateria(ConteudoPublicacao conteudoPublicacao) throws ServiceException;

	public Integer recuperarNumeroUltimaAta(Integer codigoCapitulo, Integer codigoMateria, Integer codigoConteudo) throws ServiceException;
	
	public Integer recuperarNumeroUltimaAta(Integer codigoCapitulo, List<Integer> codigosMateria, Integer codigoConteudo, Short anoMateria, Boolean capituloJaComposto)
		throws ServiceException;
	
	public ConteudoPublicacao recuperarMenorAtaEmAberto(Integer codigoCapitulo, Integer materiaAlvo, Integer codigoConteudo, Short anoMateria, Date dataCriacao, Boolean capituloJaComposto) throws ServiceException;

	ConteudoPublicacao consultarDadosDaPublicacaoDoTexto(IConsultaDeDadosDePublicacao consulta) throws ServiceException;

	boolean isPautaDeJulgamentoFechada(ConsultaConteudoPublicacao consulta, Calendar dataLiberacao) throws ServiceException;
	
	public ConteudoPublicacao classificarTextoPorAta(Texto texto, EstruturaPublicacao estruturaPublicacao) throws ServiceException;
	
	public boolean isHorarioLimiteAta(Date dataCriacaoAta);

	public ConteudoPublicacao getAtaAberta(Setor setor, Short anoMateria) throws ServiceException;

	public ConteudoPublicacao criarMateriaAta(Integer codCapituloPlenario,
			Integer codMateriaRepublicacaoRepercussaoGeral, Integer codConteudoRelacaoProcesso, Short anoAta)  throws ServiceException;

	public void incluirProcessoEmAta(ObjetoIncidente<?> oi, ConteudoPublicacao materia, Boolean republicacao) throws ServiceException;

	public boolean isPautaFechada(Colegiado colegiado, Calendar dataLiberacaoLista);
	
	public ConteudoPublicacao pesquisarMateriaNaoCompostaNaoPublicadaPorSessao(Sessao sessao) throws ServiceException;
	

	public List<ConteudoPublicacao> pesquisarMateriaDataCriacao(Date dataCriacao) throws ServiceException;

	public List<ConteudoPublicacao> pesquisarMateriaChaveAntiga(Integer numero, Short anoMateria, Integer codigoCapitulo, Integer... codigoMateria)	throws ServiceException;

	public ConteudoPublicacao pesquisarUltimaMateriaOInoAcordao(ObjetoIncidente<?> oi) throws ServiceException;
	
}