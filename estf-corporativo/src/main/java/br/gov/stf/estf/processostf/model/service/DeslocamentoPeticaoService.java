package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.DeslocaPeticao;
import br.gov.stf.estf.entidade.processostf.DeslocaPeticao.DeslocaPeticaoId;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.processostf.model.dataaccess.DeslocamentoPeticaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface DeslocamentoPeticaoService	extends	GenericService<DeslocaPeticao, DeslocaPeticaoId, DeslocamentoPeticaoDao> {

	// public DeslocamentoProcesso recuperarUltimoDeslocamentoProcesso(String
	// siglaClasse, Long numeroProcesso)throws ServiceException;
	public void persistirDeslocamentoPeticao(DeslocaPeticao deslocamentoPeticao)
			throws ServiceException;
	public List<DeslocaPeticao> recuperarDeslocamentoPeticaos(Guia guia) throws ServiceException;
	public DeslocaPeticao recuperarDeslocamentoPeticao(Guia guia) throws ServiceException;
	public void salvarRecebimentoPeticao(DeslocaPeticao deslocamentoPeticao) throws ServiceException;
	public void removerPeticao(DeslocaPeticao peticao) throws ServiceException;
	
	public List<DeslocaPeticao> pesquisarDataRecebimentoGuiaPeticao(Guia guia) throws ServiceException;
	public Long pesquisarSetorUltimoDeslocamento(Long seqObjetoIncidente) throws ServiceException;
	public DeslocaPeticao recuperarUltimoDeslocamentoPeticao(Peticao peticao) throws ServiceException;
	List<DeslocaPeticao> recuperarDeslocamentoPeticaoRecebimentoExterno(Guia guia) throws ServiceException;
	List<DeslocaPeticao> recuperarDeslocamentoPeticaoRecebimentoExterno(Peticao peticao) throws ServiceException;
	public Integer recuperarUltimaSequencia(Guia guia) throws ServiceException;
}
