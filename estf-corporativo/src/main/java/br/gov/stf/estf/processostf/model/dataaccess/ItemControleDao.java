package br.gov.stf.estf.processostf.model.dataaccess;

import java.sql.SQLException;
import java.util.List;

import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.processostf.ItemControle;
import br.gov.stf.estf.entidade.processostf.TipoSituacaoControle;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ItemControleDao extends GenericDao<ItemControle, Long> {

	public List<ItemControle> buscaListaDeItemUsuario(List<String> listaFiltroUsuarioItensControle, 
		TipoSituacaoControle tipoSC, Long idSetor, boolean permissaoGabineteSEJ, String sigUsuario, 
		boolean permissaoManterGrupo) throws DaoException;
	
	public String pesquisaUltimoDeslocamentoProcesso(Long idObjetoIncidente) throws DaoException;
	
	public ArquivoProcessoEletronico existePecaProcessoEletronicoJuntada(Long iidPecaProcEletronico) throws DaoException;
	
	public void chamaPackageItemControle() throws DaoException, SQLException;

	public boolean isItemControleRepublicacao(Long itemControleId) throws DaoException;
}
