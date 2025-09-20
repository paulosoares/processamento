/**
 * 
 */
package br.gov.stf.estf.julgamento.model.service.impl;

import java.util.List;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.ManifestacaoLeitura;
import br.gov.stf.estf.entidade.julgamento.ManifestacaoRepresentante;
import br.gov.stf.estf.entidade.julgamento.ProcessoListaJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.julgamento.model.dataaccess.ProcessoListaJulgamentoDao;
import br.gov.stf.estf.julgamento.model.service.ManifestacaoLeituraService;
import br.gov.stf.estf.julgamento.model.service.ManifestacaoRepresentanteService;
import br.gov.stf.estf.julgamento.model.service.ProcessoListaJulgamentoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("processoListaJulgamentoService")
public class ProcessoListaJulgamentoServiceImpl extends GenericServiceImpl<ProcessoListaJulgamento, Long, ProcessoListaJulgamentoDao> implements ProcessoListaJulgamentoService {

	@Autowired
	public ManifestacaoRepresentanteService manifestacaoRepresentanteService;
	
	@Autowired
	public ManifestacaoLeituraService manifestacaoLeituraService;
	
	public ProcessoListaJulgamentoServiceImpl(ProcessoListaJulgamentoDao dao) {
		super(dao);
	}

	@Override
	public ProcessoListaJulgamento recuperar(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		ProcessoListaJulgamento retorno = null;
		try {
			retorno = dao.recuperar(objetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return retorno;
	}

	@Override
	public ProcessoListaJulgamento recuperarProcessoListaJulgamento(ObjetoIncidente<?> incidente, ListaJulgamento listaJulgamento) throws ServiceException {
		try {
			return dao.recuperarProcessoListaJulgamento(incidente, listaJulgamento);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<ProcessoListaJulgamento> listarProcessos(ListaJulgamento listaJulgamento) throws ServiceException {
		List<ProcessoListaJulgamento> retorno = null;
		try {
			retorno = dao.listarProcessos(listaJulgamento);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return retorno;
	}

	@Override
	public void clonarManifestacoes(ListaJulgamento novaListaJulgamento, ListaJulgamento listaJulgamentoOriginal) throws ServiceException {
		for (ProcessoListaJulgamento pljNovo : novaListaJulgamento.getProcessosListaJulgamento()) {
			for (ProcessoListaJulgamento pljVelho : listaJulgamentoOriginal.getProcessosListaJulgamento()) {
				pljVelho = recuperarPorId(pljVelho.getId());
				if (pljNovo.getObjetoIncidente().equals(pljVelho.getObjetoIncidente()) && pljVelho.getManifestacoes() != null && pljVelho.getManifestacoes().size() > 0) {

					pljNovo.setManifestacoes(new TreeSet<ManifestacaoRepresentante>());

					for (ManifestacaoRepresentante mrVelho : pljVelho.getManifestacoes()) {
						ManifestacaoRepresentante mr = new ManifestacaoRepresentante();
						mr.setObjetoIncidente(mrVelho.getObjetoIncidente());
						mr.setAndamentoProcesso(mrVelho.getAndamentoProcesso());
						mr.setDataEnvio(mrVelho.getDataEnvio());
						mr.setListaJulgamento(novaListaJulgamento);
						mr.setPecaProcessual(mrVelho.getPecaProcessual());
						mr.setRepresentado(mrVelho.getRepresentado());
						mr.setRepresentante(mrVelho.getRepresentante());
						mr.setTipoManifestacao(mrVelho.getTipoManifestacao());
						mr.setHashArquivo(mrVelho.getHashArquivo());
						mr.setSituacao(mrVelho.getSituacao());
						mr.setEnviadoPor(mrVelho.getEnviadoPor());
						mr.setManifestacaoLeitura(new TreeSet<ManifestacaoLeitura>());
						mr = manifestacaoRepresentanteService.salvar(mr);
						
						for (ManifestacaoLeitura mlVelho : mrVelho.getManifestacaoLeitura()) {
							ManifestacaoLeitura ml = new ManifestacaoLeitura();
							ml.setManifestacaoRepresentante(mr);
							ml.setDataLeitura(mlVelho.getDataLeitura());
							ml.setMinistro(mlVelho.getMinistro());
							mr.getManifestacaoLeitura().add(ml);
							ml = manifestacaoLeituraService.salvar(ml);
						}
						
						mr = manifestacaoRepresentanteService.salvar(mr);

						pljNovo.getManifestacoes().add(mr);
						
						salvar(pljNovo);
					}
				}
			}
		}

	}
}
