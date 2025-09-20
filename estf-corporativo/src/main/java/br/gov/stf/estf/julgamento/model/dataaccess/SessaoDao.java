package br.gov.stf.estf.julgamento.model.dataaccess;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;

import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoJulgamentoVirtual;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoSessaoConstante;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface SessaoDao extends GenericDao<Sessao, Long> {
	
	
	public List<Sessao> pesquisarSessaoSQL(Date dataInicio
										  ,Date dataFim
										  ,Date dataPrevistaInicio
										  ,Date dataPrevistaFim
										  ,Short ano
										  ,Long numero
										  ,String tipoSessao
										  ,String tipoColegiado
										  ,String tipoAmbiente
										  ,Boolean numeroAnoPreenchido) throws DaoException;	
	
	public List<Sessao> pesquisarSessao(Date dataInicio
									   ,Date dataFim
									   ,Date dataPrevistaInicio
									   ,Date dataPrevistaFim
									   ,Short ano
									   ,Long numero
									   ,String tipoSessao
									   ,String tipoColegiado
									   ,String tipoAmbiente
									   ,Boolean numeroAnoPreenchido) throws DaoException;
	
	public List<Sessao> pesquisarSessoesVirtuaisDeListaNaoIniciadas() throws DaoException;
	
	//public Boolean excluirSessao( Sessao sessao ) throws DaoException;
	
	public Date recuperarDataSessaoJulgamento(String siglaClasse
											 ,Long numeroProcesso
											 ,Long recurso
											 ,String tipoJulgamento) throws DaoException;
	
	public Long recuperarMaiorNumeroSessao() throws DaoException;
	
	public List<Sessao> pesquisarSessao( Date dataInicioSessao, Date dataFimSessao, 
			TipoAmbienteConstante tipoAmbiente, TipoSessaoConstante tipoSessao, String colegiado) throws DaoException;
	
	public List<Sessao> pesquisarSessaoVirtual( Date dataInicioSessao, Date dataFimSessao, 
			TipoAmbienteConstante tipoAmbiente, TipoSessaoConstante tipoSessao, String colegiado) throws DaoException;
	
	public Sessao pesquisarSessao(Date dataInicioSessao
								 ,TipoAmbienteConstante tipoAmbiente
								 ,TipoSessaoConstante tipoSessao
								 ,String colegiado) throws DaoException;	

	public Sessao recuperar(Long seqObjetoIncidente) throws DaoException;
	
	public List<Sessao> recuperarExclusivoDigital(Long seqObjetoIncidente) throws DaoException;

	public Sessao recuperar(ObjetoIncidente<?> objetoIncidente) throws DaoException;

	public List<Object[]> pesquisarSessao(String colegiado
										 ,TipoAmbienteConstante tipoAmbienteSessao
										 ,String tipoSessao
										 ,Date dataBase) throws DaoException;

	public List<Object[]> pesquisarSessao(TipoColegiadoConstante colegiado
										 ,TipoAmbienteConstante tipoAmbienteSessao
										 ,String tipoSessao
										 ,Date dataBase) throws DaoException;

	public void refresh(Sessao sessao) throws DaoException;

	public List<Sessao> pesquisar(TipoColegiadoConstante colegiado) throws DaoException;
	
	public List<Sessao> pesquisar(TipoColegiadoConstante colegiado, TipoAmbienteConstante ambiente, Boolean sessaoVirtualExtraordinaria) throws DaoException;
	
	Long recuperarUltimoNumeroSessao(Sessao novaSessao) throws DaoException;
	
	List<Sessao> pesquisarSessaoNaoEncerrada(TipoColegiadoConstante colegiado, TipoAmbienteConstante tipoAmbienteSessao, TipoSessaoConstante tipoSessaoConstante, TipoJulgamentoVirtual tipoJulgamentoVirtual) throws DaoException;
	
	Long pesquisarQuantidadeProcessosNaSessao(Sessao sessao) throws DaoException;
	
	Long pesquisarQuantidadeListasNaSessao(Sessao sessao) throws DaoException;

	public Sessao recuperarUltimaSessaoEncerrada(Colegiado colegiado)throws DaoException;

	Long pesquisarQuantidadeProcessosDasListas(Sessao sessao) throws DaoException;
	
	public List<Sessao> pesquisarSessoesVirtuais(TipoColegiadoConstante colegiado, Boolean possuiListaJulgamento, Boolean iniciada, Boolean finalizada) throws DaoException;
	
	public List<Sessao> pesquisarSessoesVirtuaisIndependePublicado(TipoColegiadoConstante colegiado, Boolean possuiListaJulgamento, Boolean iniciada, Boolean finalizada) throws DaoException;
	
	public Long recuperarMaiorNumeroSessaoVirtual(Colegiado colegiado, short ano) throws DaoException;

	public boolean isProducao() throws HibernateException, SQLException, DaoException;

	public List<Sessao> pesquisarSessoesPrevistas(Date dataBase, boolean maiorDataBase) throws DaoException;

	public List<Sessao> pesquisarSessoesEncerradas(Date dataLimite, String tipoAmbiente) throws DaoException;

	public boolean isExclusivoDigital(ObjetoIncidente oi) throws DaoException;
}