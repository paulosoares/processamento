package br.gov.stf.estf.publicacao.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.publicacao.TipoSessao;
import br.gov.stf.estf.publicacao.model.util.ConteudoPublicacaoDynamicRestriction;
import br.gov.stf.estf.publicacao.model.util.DadosDePublicacaoDynamicQuery;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ConteudoPublicacaoDao extends GenericDao<ConteudoPublicacao, Long> {

	public List<ConteudoPublicacao> pesquisarMateriasDJ(Long idPublicacao) throws DaoException;

	public List<ConteudoPublicacao> pesquisarMateriasPublicacao() throws DaoException;

	public List<ConteudoPublicacao> pesquisarMateriasPrevia(Date dataComposicao) throws DaoException;

	public List<ConteudoPublicacao> pesquisarMateriasPublicacaoAntigo(Long idPublicacao) throws DaoException;

	public Date recuperarDataPrevistaPublicacao(Integer codigoCapitulo, Integer codigoMateria, Integer numero, Short ano,
			Date dataCriacao) throws DaoException;

	ConteudoPublicacao consultarDadosDaPublicacaoDoTexto(DadosDePublicacaoDynamicQuery consulta) throws DaoException;

	public List<ConteudoPublicacao> pesquisar(ConteudoPublicacaoDynamicRestriction consultaDinamica) throws DaoException;
	
	public List<ConteudoPublicacao> pesquisar( Integer codigoCapitulo, Integer codigoMateria, Integer codigoConteudo,
			                                   Integer numero, Short ano, Date dataAta, Boolean isPublicado) throws DaoException;

	public List<ConteudoPublicacao> recuperar( Integer codigoCapitulo, List<Integer> codigosMateria, Integer codigoConteudo, 
            								   Short anoMateria, Date dataCriacao, Boolean capituloJaComposto) throws DaoException;

	public ConteudoPublicacao pesquisarMateriaNaoCompostaNaoPublicadaPorSessao(Sessao sessao) throws DaoException;
	

	public List<ConteudoPublicacao> pesquisarMateriaDataCriacao(Date dataCriacao) throws DaoException;
	
	public List<ConteudoPublicacao> pesquisarMateriaChaveAntiga(Integer numero, Short anoMateria, Integer codigoCapitulo, Integer... codigoMateria)	throws DaoException;
	
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
			                                        ,TipoSessao tipoSessao) throws DaoException;

	public ConteudoPublicacao pesquisarUltimaMateriaOInoAcordao(ObjetoIncidente<?> oi)  throws DaoException;

}
