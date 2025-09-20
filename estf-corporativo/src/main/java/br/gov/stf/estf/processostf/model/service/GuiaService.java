package br.gov.stf.estf.processostf.model.service;

import java.sql.SQLException;
import java.util.List;

import br.gov.stf.estf.entidade.localizacao.EnderecoDestinatario;
import br.gov.stf.estf.entidade.processostf.DeslocaPeticao;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Guia.GuiaId;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processostf.model.dataaccess.GuiaDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface GuiaService extends GenericService<Guia, GuiaId, GuiaDao> {
	public Long persistirGuia (Guia guia)throws ServiceException;


	//
	// Tipo de órgão de destino/origem. Domínio: 1- Advogado, 2- Interno, 3- Externo.
	//
    public String callDeslocamento(Guia guia, Long codigoSetorUsuario) throws ServiceException;
    public String callDeslocamento(Guia guia, Long codigoSetorUsuario, Boolean recebimentoAutomatico) throws ServiceException;
    
    
	public List<Guia> recuperarGuia(Guia guia) throws ServiceException;
/*	public List<Guia> recuperarGuia(Guia guia,
			boolean naoRecebidos,
			String numeroPeticao, String anoPeticao, String numeroProcesso, String siglaProcesso) throws ServiceException;
*/	
	List<Guia> getListarDocumentosGuia(GuiaId guiaId) throws ServiceException;
//	public List<Guia> recuperarGuia(Guia guia,	boolean naoRecebidos) throws ServiceException;
	public List<Guia> recuperarGuia(Guia guia, Processo processo, Peticao peticao, boolean naoRecebidos, String tipoGuia, boolean isProceso, boolean isPeticao, boolean pesquisarTodos) throws ServiceException;
	public List<Guia> recuperarGuia(Guia guia, Processo processo, Peticao peticao) throws ServiceException;
	
	public void callInserirProcessoPeticaoNaGuia(Guia guia, String tipoObjetoIncidente) throws ServiceException, SQLException;
	public void callremoverProcessoPeticaoNaGuia(Guia guia, String tipoObjetoIncidente) throws ServiceException;
	void alterarGuia(Guia guia) throws ServiceException;
	void callRemoverGuia(Guia guia) throws ServiceException;
	public Long recuperarProximoNumeroGuia() throws ServiceException;
	public Long recuperarTotalPeticao(Guia guia) throws ServiceException;
	public Long recuperarTotalProcesso(Guia guia) throws ServiceException;
	public Long recuperarTotalItem(Guia guia) throws ServiceException;
	public boolean isPeticao(Guia guia) throws ServiceException;
	public Guia geraGuiaVazia(Guia guia) throws ServiceException;
	public boolean isEletronico(Guia guia) throws ServiceException;
	public void cancelarGuiaProcesso(DeslocaProcesso processo) throws ServiceException;
	public void cancelarGuiaPeticao(DeslocaPeticao peticao) throws ServiceException;
	public Boolean existeEndereco(EnderecoDestinatario end) throws ServiceException;
	public Boolean temPermissaoAlterarGuia(Guia guia) throws ServiceException;
}

