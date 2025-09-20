package br.gov.stf.estf.documento.model.service;

import java.util.Collection;
import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.PecaProcessoEletronicoDao;
import br.gov.stf.estf.documento.model.exception.TextoInvalidoParaPecaException;
import br.gov.stf.estf.documento.model.util.IConsultaPecaProcessoEletronicoDoTexto;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoSituacaoPeca;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface PecaProcessoEletronicoService extends
		GenericService<PecaProcessoEletronico, Long, PecaProcessoEletronicoDao> {

	PecaProcessoEletronico consultarPecaProcessoEletronicoDoTexto(Texto texto)
			throws ServiceException, TextoInvalidoParaPecaException;

	void excluir(PecaProcessoEletronico entidade, boolean exclusaoLogica)
			throws ServiceException;

	void excluir(PecaProcessoEletronico entidade, String motivo,
			boolean exclusaoLogica) throws ServiceException;

	void excluir(PecaProcessoEletronico entidade, boolean exclusaoLogica,
			boolean cancelarPDF) throws ServiceException;

	void excluir(PecaProcessoEletronico entidade, String motivo,
			boolean exclusaoLogica, boolean cancelarPDF)
			throws ServiceException;

	List<PecaProcessoEletronico> pesquisarPecaProcessoEletronicoDoTexto(
			IConsultaPecaProcessoEletronicoDoTexto consulta)
			throws ServiceException;

	PecaProcessoEletronico recuperarPeca(DocumentoTexto documentoTexto)
			throws ServiceException;

	List<PecaProcessoEletronico> recuperarListaPecasComunicacao(
			DocumentoComunicacao documentoComunicacao) throws ServiceException;

	Long recuperarProximoNumeroDeOrdem(ObjetoIncidente<?> objetoIncidente)
			throws ServiceException;

	PecaProcessoEletronico recuperarPecaProcessoEletronico(Long id)
			throws ServiceException;

	// NOVOS SERVICOS UTILIZANDO OBJETO INCIDENTE

	void excluirTodos(
			Collection<PecaProcessoEletronico> pecasProcessoEletronico,
			boolean exclusaoLogica) throws ServiceException;

	PecaProcessoEletronico montaPecaParaJuntada(String siglaTipoPecaProcesso,
			ObjetoIncidente<?> objetoIncidente,
			TipoSituacaoPeca tipoSituacaoPeca, Setor setor)
			throws ServiceException;

	List<PecaProcessoEletronico> pesquisar(ObjetoIncidente<?> objetoIncidente,
			TipoSituacaoPeca... tipoSituacaoPecao) throws ServiceException;

	List<PecaProcessoEletronico> pesquisar(ObjetoIncidente<?> objetoIncidente,
			Long tipoPecaProcesso, Boolean pesquisarCancelados)
			throws ServiceException;

	PecaProcessoEletronico recuperarPecaInteiroTeor(
			DocumentoEletronico documentoEletronico) throws ServiceException;

	public boolean temPecasPendenteVisualizacao(
			ObjetoIncidente<?> objetoIncidente) throws ServiceException;

	public PecaProcessoEletronico copiarPeca(
			PecaProcessoEletronico pecaProcessoEletronicoOriginal,
			ObjetoIncidente<?> objetoIncidenteDestino,
			Long numeroOrdem, String descricaoPeca)
			throws ServiceException;

	public List<PecaProcessoEletronico> pesquisarPorProcesso(Processo processo,
			Boolean incluirCancelados) throws ServiceException;

	List<PecaProcessoEletronico> pecaProcessoEletronicoPendenteVisualizacao(ObjetoIncidente<?> objetoIncidente) throws ServiceException, DaoException;

	List<PecaProcessoEletronico> pecaProcessoEletronicoJuntadaPendenteVisualizacao( ObjetoIncidente<?> objetoIncidente) throws ServiceException, DaoException;

	List<PecaProcessoEletronico> listarInteiroTeorObjetoIncidente(List<ObjetoIncidente> listaObjetoIncidente) throws ServiceException;

	void tornarPublicasPecasPendenteVisualizacao(ObjetoIncidente<?> objetoIncidente) throws ServiceException;

	void normalizaPecasObjetoIncidente(ObjetoIncidente<?> objetoIncidente) throws ServiceException, DaoException;

	List<PecaProcessoEletronico> listarPecas(ObjetoIncidente<?> objetoIncidente) throws ServiceException;

	PecaProcessoEletronico recuperaPecaComunicacao(Long idDocumento, Long idObjetoIncidente) throws ServiceException;
	

}
