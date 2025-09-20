package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.IncidentePreferencia;
import br.gov.stf.estf.entidade.processostf.MapeamentoClasseSetor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.util.ObjetoIncidenteUtil;
import br.gov.stf.estf.localizacao.model.service.exception.NaoExisteSetorParaDeslocamentoException;
import br.gov.stf.estf.processostf.model.dataaccess.IncidentePreferenciaDao;
import br.gov.stf.estf.processostf.model.dataaccess.MapeamentoClasseSetorDao;
import br.gov.stf.estf.processostf.model.service.MapeamentoClasseSetorService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service ("mapeamentoClasseSetorService")
public class MapeamentoClasseSetorServiceImpl extends GenericServiceImpl<MapeamentoClasseSetor, Long, MapeamentoClasseSetorDao>
		implements MapeamentoClasseSetorService {

	@Autowired
	private IncidentePreferenciaDao incidentePreferenciaDao;

	protected MapeamentoClasseSetorServiceImpl(MapeamentoClasseSetorDao dao, IncidentePreferenciaDao incidentePreferenciaDao) {
		super(dao);
		this.incidentePreferenciaDao = incidentePreferenciaDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.gov.stf.estf.processostf.model.service.impl.MapeamentoClasseSetorService
	 * #
	 * recuperarSetorDeDestinoDoDeslocamento(br.gov.stf.estf.entidade.processostf
	 * .ObjetoIncidente)
	 */
	public Setor recuperarSetorDeDestinoDoDeslocamento(ObjetoIncidente<?> objetoIncidente) throws ServiceException,
			NaoExisteSetorParaDeslocamentoException {
		try {
			Processo processo = ObjetoIncidenteUtil.getProcesso(objetoIncidente);
			List<MapeamentoClasseSetor> mapeamentos = dao.recuperarMapeamentosDaClasse(processo.getClasseProcessual().getId());
			if (mapeamentos.size() == 1) {
				return mapeamentos.get(0).getSetor();
			}
			List<IncidentePreferencia> incidentesPreferencia = incidentePreferenciaDao
					.recuperarPreferenciasDoIncidente(processo.getPrincipal().getId());

			for (MapeamentoClasseSetor mapeamentoClasseSetor : mapeamentos) {
				if( mapeamentoClasseSetor.getTipoPreferencia() == null )
					continue;
				for (IncidentePreferencia incidentePreferencia : incidentesPreferencia) {
					if (mapeamentoClasseSetor.getTipoPreferencia() != null
							&& incidentePreferencia.getTipoPreferencia().equals(mapeamentoClasseSetor.getTipoPreferencia())) {
						return mapeamentoClasseSetor.getSetor();
					}
				}
			}			
			
//			for (IncidentePreferencia incidentePreferencia : incidentesPreferencia) {
//				for (MapeamentoClasseSetor mapeamentoClasseSetor : mapeamentos) {
//					if (incidentePreferencia.getTipoPreferencia() != null
//							&& incidentePreferencia.getTipoPreferencia().equals(mapeamentoClasseSetor.getTipoPreferencia())) {
//						return mapeamentoClasseSetor.getSetor();
//					}
//				}
//			}
			for (MapeamentoClasseSetor mapeamentoClasseSetor : mapeamentos) {
				if (mapeamentoClasseSetor.getTipoPreferencia() == null) {
					return mapeamentoClasseSetor.getSetor();
				}
			}
			throw new NaoExisteSetorParaDeslocamentoException(
					"O sistema não encontrou a mesa correspondente para o deslocamento do processo!");

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}
	
	public List<String> buscaClasseDoSetor(Setor setorDoUsuario) throws ServiceException{
		List<String> listaClasseProcessoDoUsuario = null;
		try {
			listaClasseProcessoDoUsuario = dao.buscaClasseDoSetor(setorDoUsuario);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return listaClasseProcessoDoUsuario;
	}

}
