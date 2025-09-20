package br.gov.stf.estf.processostf.model.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.localizacao.model.service.OrigemService;
import br.gov.stf.estf.processostf.model.dataaccess.AndamentoDao;
import br.gov.stf.estf.processostf.model.service.AndamentoService;
import br.gov.stf.estf.processostf.model.service.DeslocaProcessoService;
import br.gov.stf.estf.processostf.model.service.ProcessoService;
import br.gov.stf.estf.processostf.model.service.VerificadorPerfilService;
import br.gov.stf.estf.processostf.model.service.exception.LancamentoIndevidoException;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("andamentoService")
public class AndamentoServiceImpl extends
		GenericServiceImpl<Andamento, Long, AndamentoDao> implements
		AndamentoService {
	
	private ProcessoService processoService;
	
	private DeslocaProcessoService deslocaProcessoService;
	
	private OrigemService origemService;
	
	public AndamentoServiceImpl(AndamentoDao dao, ProcessoService processoService, DeslocaProcessoService deslocaProcessoService, OrigemService origemService) {
		super(dao); 
		this.processoService = processoService;
		this.deslocaProcessoService = deslocaProcessoService;
		this.origemService = origemService;
	}

	@Override
	public List<Andamento> pesquisar(Long codigoSetor) throws ServiceException {
		List<Andamento> andamentos = null;
		try {
			andamentos = dao.pesquisar(codigoSetor);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return andamentos;
	}
	@Override
	public List<Andamento> pesquisarTipoAndamento(Long id, String descricao,
			Boolean ativo) throws ServiceException {
		List listaTipoAndamento = null;
		try {

			listaTipoAndamento = dao.pesquisarTipoAndamento(id, descricao,
					ativo);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return listaTipoAndamento;

	}

	@Override
	public Andamento recuperarTipoAndamento(Long id, String descricao)
			throws ServiceException {

		Andamento tipoAndamento = null;
		try {

			tipoAndamento = dao.recuperarTipoAndamento(id, descricao);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return tipoAndamento;

	}

	@Override
	public List<Andamento> pesquisarAndamentosAutorizados(Long codigoSetor, Processo processo) throws ServiceException {

		List<Andamento> andamentos;

		try {
			andamentos = dao.pesquisarAndamentosAutorizados(codigoSetor);
			//andamentos = dao.pesquisarAndamentosAutorizadosMock();
			filtrarAndamentoAgravoProvido(andamentos, processo);			
		
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		
		return andamentos;
	}
	
	private void filtrarAndamentoAgravoProvido(List<Andamento> andamentos, Processo processo) {
		
		// Apenas a classe processual ARE pode ter os andamentos 6249, 6247.
		if (!processo.getClasseProcessual().getId().equals("ARE")) {
			List<Andamento> andamentosAux = new ArrayList<Andamento>();
			andamentosAux.addAll(andamentos);
			
			for (Iterator iterator = andamentosAux.iterator(); iterator.hasNext();) {
				
				Andamento andamento = (Andamento) iterator.next();
				if (andamento.getId().longValue() == 6249 || andamento.getId().longValue() == 6247) {
					andamentos.remove(andamento);
				}
			}
		}
	}
	
	@Override
	public boolean podeLancarAndamentoIndevido(Long codigoSetor, Andamento andamento, VerificadorPerfilService verificadorPerfilService) throws ServiceException {
		try {

			boolean setorAutorizado = dao.podeLancarAndamentoIndevido(codigoSetor);

			if (andamento.isAndamentoAutomaticoPlenarioVirtual()) {
				return setorAutorizado && verificadorPerfilService.isUsuarioRegistrarAndamentoIndevidoRG();
			} else {
				return setorAutorizado;
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		
	}

	@Override
	public boolean podeLancarAndamentoIndevido(Long codigoSetor, Andamento andamento, VerificadorPerfilService verificadorPerfilService, Processo processo) throws ServiceException {
		try {

			boolean setorAutorizado = dao.podeLancarAndamentoIndevido(codigoSetor);

			//Entra aqui somente se o usuario estiver invalidando um andamento de baixa
			if (AndamentoProcesso.CODIGOS_ANDAMENTOS_BAIXA.contains(andamento.getId().intValue())){
				/* Verificar na judiciario.origem, 
				 * se o cod_orgao_destino do último deslocamento de baixa ou remessa possui a flag_baixa_mni marcada como 'S', 
				 */
				DeslocaProcesso recuperarUltimoDeslocamentoProcessoPorAndamento = deslocaProcessoService.recuperarUltimoDeslocamentoProcesso(processo);
				Long codOrgaoDestino = recuperarUltimoDeslocamentoProcessoPorAndamento.getCodigoOrgaoDestino();
				
				Origem origem = origemService.recuperarPorId(codOrgaoDestino);
				if (origem != null && origem.getBaixaMni()){
				
					/* Regra: Não deve ser possível indevidar um andamento de baixa 
					 * 7104 - Baixa definitiva dos autos, Guia nº, 
					 * 7100 - Baixa dos autos em diligência, Guia nº ou andamento de remessa 
					 * 7108 - Remessa dos autos ao juízo competente, Guia nº e 
					 * 7101 - Remessa externa dos autos, Guia nº, 
					 * quando esse processo tiver sido remetido via MNI 2.2.2.
					 */
					
					Boolean houveRemessa = processoService.houveRemessa(processo);
					if (houveRemessa) 
						throw new LancamentoIndevidoException("Não é possível lançar indevido no andamento de baixa/remessa, pois a operação utilizou o Webservice MNI 2.2.2, que desloca, de imediato, o processo ao tribunal destinatário. Para solução da questão, favor contatar o Coordenador respectivo.");
				}
			}

			if (andamento.isAndamentoAutomaticoPlenarioVirtual()) {
				return setorAutorizado && verificadorPerfilService.isUsuarioRegistrarAndamentoIndevidoRG();
			} else {
				return setorAutorizado;
			}
		
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		
	}

	@Override
	public boolean isGrupoDecisao(Andamento andamento) throws ServiceException {
		try {
			return dao.isGrupoDecisao(andamento);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public boolean isNotificadoTribunalOrigem(Long seqAndamentoProcesso, Long codAndamento){
		try{
			if(codAndamento ==7101L || codAndamento == 7104L || codAndamento == 7108L ){
				return dao.isNotificadoTribunalOrigem(seqAndamentoProcesso, codAndamento);
			}
			return true;
		}catch(ServiceException e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Andamento> pesquisarAndamentosAutorizadosParaLote(Setor setor) throws ServiceException {
		
		try {
			return dao.pesquisarAndamentosAutorizadosParaLote(setor);
		} catch (DaoException e) {
			throw new ServiceException("Erro ao pesquisa os andamentos para lote.", e);
		}
	}
}
