package br.gov.stf.estf.julgamento.model.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.ManifestacaoLeitura;
import br.gov.stf.estf.entidade.julgamento.ManifestacaoRepresentante;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.julgamento.model.dataaccess.ManifestacaoLeituraDao;
import br.gov.stf.estf.julgamento.model.dataaccess.hibernate.ManifestacaoLeituraDaoHibernate;
import br.gov.stf.estf.julgamento.model.service.ManifestacaoLeituraService;
import br.gov.stf.estf.julgamento.model.service.ManifestacaoRepresentanteService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("manifestacaoLeituraService")
public class ManifestacaoLeituraServiceImpl extends GenericServiceImpl<ManifestacaoLeitura, Long, ManifestacaoLeituraDao> implements ManifestacaoLeituraService {

	@Autowired
	ManifestacaoRepresentanteService manifestacaoRepresentanteService;

	protected ManifestacaoLeituraServiceImpl(ManifestacaoLeituraDaoHibernate dao) {
		super(dao);
	}

	@Override
	public ManifestacaoLeitura gravarLeituraSustentacaoOral(Long sustentacaoOralId, Ministro ministroAutenticado) throws ServiceException {
		ManifestacaoRepresentante so = manifestacaoRepresentanteService.recuperarPorId(sustentacaoOralId);

		ManifestacaoLeitura manifestacaoLeitura = null;

		for (ManifestacaoLeitura ml : so.getManifestacaoLeitura())
			if (ml.getMinistro().equals(ministroAutenticado))
				manifestacaoLeitura = ml;

		if (manifestacaoLeitura == null) {
			manifestacaoLeitura = new ManifestacaoLeitura();
			manifestacaoLeitura.setMinistro(ministroAutenticado);
			manifestacaoLeitura.setManifestacaoRepresentante(so);
			manifestacaoLeitura.setDataLeitura(new Date());

			salvar(manifestacaoLeitura);

			so.getManifestacaoLeitura().add(manifestacaoLeitura);
			manifestacaoRepresentanteService.salvar(so);
		}

		return manifestacaoLeitura;
	}
}