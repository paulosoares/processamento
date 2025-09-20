package br.gov.stf.estf.julgamento.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.InscricoesJulgamento;
import br.gov.stf.estf.entidade.julgamento.ManifestacaoRepresentante;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.julgamento.model.dataaccess.ManifestacaoRepresentanteDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ManifestacaoRepresentanteService extends GenericService<ManifestacaoRepresentante, Long, ManifestacaoRepresentanteDao> {

	List<ManifestacaoRepresentante> listarManifestacoesPorIncidente(ObjetoIncidente<?> objetoIncidente) throws ServiceException;

	void alterarSituacaoSustentacaoOral(ManifestacaoRepresentante m, AndamentoProcesso andamento) throws ServiceException;

	List<InscricoesJulgamento> recuperarInscritosSessaoJulgamento(Sessao sessao, boolean sustentacaoOral, boolean participacaoEmJulgamento,
			boolean julgamentoPresencial, boolean julgamentoVideoConferencia) throws ServiceException;

	List<InscricoesJulgamento> recuperarInscritosSessaoJulgamento(Long sessaoId, boolean sustentacaoOral, boolean participacaoEmJulgamento,
			boolean julgamentoPresencial, boolean julgamentoVideoConferencia) throws ServiceException;
}
