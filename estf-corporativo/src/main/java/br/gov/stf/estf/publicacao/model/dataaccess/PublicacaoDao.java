package br.gov.stf.estf.publicacao.model.dataaccess;


import java.util.List;

import br.gov.stf.estf.entidade.publicacao.Publicacao;
import br.gov.stf.estf.publicacao.model.util.AdvogadoVO;
import br.gov.stf.estf.publicacao.model.util.PecaVO;
import br.gov.stf.estf.publicacao.model.util.ProcessoProtocoloPublicacaoSearchData;
import br.gov.stf.estf.publicacao.model.util.ProcessoPublicadoVO;
import br.gov.stf.estf.publicacao.model.util.ProtocoloVO;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;
import br.gov.stf.framework.util.SearchResult;

public interface PublicacaoDao extends GenericDao<Publicacao, Long> {
	public List<Publicacao> pesquisarProcessoPublicado (String siglaClasse, Integer numero, Long codigoRecurso, String tipoJulgamento) throws DaoException;
	
	public Short recuperarNumeroUltimoDj() throws DaoException;
	
	public List<Publicacao> pesquisarDjNaoPublicado () throws DaoException;
	
	public Publicacao recuperar (Short anoEdicaoDje, Short numeroEdicaoDje) throws DaoException;
	
	public SearchResult pesquisarProcessoProtocoloPublicacao(ProcessoProtocoloPublicacaoSearchData sd)throws DaoException;
	
	public void atualizarDataPublicacao(long id, int numeroEdicaoDje)throws DaoException;
	
	public List<ProcessoPublicadoVO> pesquisarProcessosConfirmacao(Long id)throws DaoException;
	
	public int recuperarProximoNumeroSequenciaProcesso(int objetoIncidente)throws DaoException;
	
	public int recuperarSeqObjetoIncidenteConfirmacao(int objetoIncidente, String tipoObjetoIncidente)throws DaoException;
	
	public int inserirAndamentoProcesso(int codigoAndamento, String siglaUsuario, java.sql.Date dataHoje, java.sql.Date dataHoje2, String descricaoObservacao, int numeroSequencia, int objetoIncidente, long codigoSetor)throws DaoException;
	
	public void alterarSituacaoPecaInteiroTeor(int objetoIncidente, java.sql.Date dataHoje, Long idProcessoPublicado)throws DaoException;
	
	public void alterarDeslocamentoProcessoEletronico(int objetoIncidente, long codigoSetor)throws DaoException;
	
	public void inserirTextoAndamentoAcordao(int objetoIncidente, int seqAndamentoProcesso)throws DaoException;
	
	public void inserirTextoAndamento(int objetoIncidente, int seqArquivoEletronico, int seqAndamentoProcesso)throws DaoException;
	
	public void alterarSituacaoPecaEletronica(int objetoIncidente, int seqArquivoEletronico)throws DaoException;	
	
	public List<ProtocoloVO> pesquisarProtocolosConfirmacao(Long id)throws DaoException;
	
	public int recuperarProximoNumeroSequenciaProtocolo(int objetoIncidente)throws DaoException;
	
	public void inserirAndamentoProtocolo(int codigoAndamento, java.sql.Date dataHoje, java.sql.Date dataHoje2, int numeroSequencia, int objetoIncidente, String siglaUsuario, long codigoSetor, String descricaoObservacaoProtocolo)throws DaoException;
	
	public List<ProtocoloVO> pesquisarProtocolosRepublicadosConfirmacao(Long id)throws DaoException;
	
	public void inserirControlePrazoIntimacao(int andamentoProcesso, java.sql.Date dataHoje, long idParte)throws DaoException;;
	
	public void inserirProcessoIntegracao( int andamentoProcesso, int origem, int processo, int peca, int parte, java.sql.Date dataAtual) throws DaoException;

	public List<AdvogadoVO> pesquisarAdvogadosIntimaveisDJ(long seqDJ) throws DaoException;

	public List<PecaVO> pesquisarPecas(int objetoIncidente, java.sql.Date dataComposicao) throws DaoException;

	public void alterarSituacaoPecaJuntada(int seqPeca) throws DaoException;

	void alterarTipoAcessoDocumento(ProcessoPublicadoVO processoPublicado)
			throws DaoException;

	public void refresh(Publicacao publicacao) throws DaoException;
}
