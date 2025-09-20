package br.gov.stf.estf.processostf.model.dataaccess;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;

import br.gov.stf.estf.entidade.localizacao.EnderecoDestinatario;
import br.gov.stf.estf.entidade.processostf.DeslocaPeticao;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Guia.GuiaId;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface GuiaDao extends GenericDao<Guia, GuiaId> {
	public Long persistirGuia(Guia guia)throws DaoException;
	
    public String callDeslocamento(Guia guia, Long codigoSetorUsuario) throws DaoException;
    public List<Guia> recuperarGuia(Guia guia) throws DaoException;
    public List<Guia> getListarDocumentosGuia(GuiaId guiaId) throws DaoException;
	public List<Guia> recuperarGuia(Guia guia, Peticao peticao,	boolean naoRecebidos) throws DaoException;
	public List<Guia> recuperarGuia(Guia guia, Processo processo, boolean naoRecebidos) throws DaoException;
	public void callInserirProcessoPeticaoNaGuia(Guia guia, String tipoObjetoIncidente) throws DaoException, HibernateException, SQLException;
	public void callRemoverProcessoPeticaoNaGuia(Guia guia, String tipoObjetoIncidente) throws DaoException, HibernateException, SQLException;
	void alterarGuia(Guia guia) throws DaoException;
	public Long recuperarProximoNumeroGuia() throws DaoException;
	public String callDeslocamento(Guia guia, Long codigoSetorUsuario, Boolean recebimentoAutomatico) throws DaoException;
	public Long recuperarTotalPeticao(Guia guia) throws DaoException;
	public Long recuperarTotalProcesso(Guia guia) throws DaoException;
	public Guia geraGuiaVazia(Guia guia) throws DaoException;
	public boolean isEletronico(Guia guia) throws DaoException;
	public void cancelarGuiaPeticao(DeslocaPeticao peticao) throws DaoException;
	public void cancelarGuiaProcesso(DeslocaProcesso processo) throws DaoException;
	public Boolean existeEndereco(EnderecoDestinatario end) throws DaoException;
	public Boolean temPermissaoAlterarGuia(Guia guia) throws DaoException;
}
