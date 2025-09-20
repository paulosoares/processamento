package br.gov.stf.estf.julgamento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.InscricoesJulgamento;
import br.gov.stf.estf.entidade.julgamento.ManifestacaoRepresentante;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ManifestacaoRepresentanteDao extends GenericDao<ManifestacaoRepresentante, Long> {

	List<ManifestacaoRepresentante> listarManifestacoesPorIncidente(ObjetoIncidente<?> objetoIncidente) throws DaoException;
	
	public List<InscricoesJulgamento> recuperarInscritosSessaoJulgamento(Long sessaoId, boolean sustentacaoOral, boolean participacaoEmJulgamento,
			boolean julgamentoPresencial, boolean julgamentoVideoConferencia) throws DaoException;

}
