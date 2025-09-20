package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.DeslocaPeticao;
import br.gov.stf.estf.entidade.processostf.DeslocaPeticao.DeslocaPeticaoId;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface DeslocamentoPeticaoDao extends GenericDao<DeslocaPeticao, DeslocaPeticaoId> {

		//public DeslocamentoProcesso recuperarUltimoDeslocamentoProcesso(String siglaClasse, Long numeroProcesso)throws DaoException;
		public void persistirDeslocamentoPeticao(DeslocaPeticao deslocamentoPeticao)throws DaoException;
		public List<DeslocaPeticao> recuperarDeslocamentoPeticaos(Guia guia) throws DaoException;
		
		public void removerPeticao(DeslocaPeticao deslocaPeticao) throws DaoException;
		
		public List<DeslocaPeticao> pesquisarDataRecebimentoGuiaPeticao(Guia guia) throws DaoException;
		public Long pesquisarSetorUltimoDeslocamento(Long seqObjetoIncidente) throws DaoException;
		public DeslocaPeticao recuperarUltimoDeslocamentoPeticao(Peticao peticao) throws DaoException;
		List<DeslocaPeticao> recuperarDeslocamentoPeticaoRecebimentoExterno(Guia guia) throws DaoException;
		List<DeslocaPeticao> recuperarDeslocamentoPeticaoRecebimentoExterno(Peticao peticao) throws DaoException;
		public Integer recuperarUltimaSequencia(Guia guia) throws DaoException;
}
