package br.gov.stf.estf.julgamento.model.service.impl;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRException;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.stf.estf.documento.model.service.ArquivoProcessoEletronicoService;
import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.documento.model.service.PecaProcessoEletronicoService;
import br.gov.stf.estf.documento.model.service.impl.AssinaturaDigitalServiceImpl;
import br.gov.stf.estf.entidade.InscricoesJulgamento;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.TipoArquivo;
import br.gov.stf.estf.entidade.documento.TipoPecaProcesso;
import br.gov.stf.estf.entidade.documento.TipoSituacaoPeca;
import br.gov.stf.estf.entidade.julgamento.ManifestacaoLeitura;
import br.gov.stf.estf.entidade.julgamento.ManifestacaoRepresentante;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.processostf.Andamento.Andamentos;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.julgamento.model.dataaccess.ManifestacaoRepresentanteDao;
import br.gov.stf.estf.julgamento.model.dataaccess.hibernate.ManifestacaoRepresentanteDaoHibernate;
import br.gov.stf.estf.julgamento.model.service.ManifestacaoRepresentanteService;
import br.gov.stf.estf.julgamento.model.service.SessaoService;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;
import br.jus.stf.util.jasperreports.UtilJasperReports;

@Service("manifestacaoRepresentanteService")
public class ManifestacaoRepresentanteServiceImpl extends GenericServiceImpl<ManifestacaoRepresentante, Long, ManifestacaoRepresentanteDao> implements
		ManifestacaoRepresentanteService {

	@Autowired
	private AndamentoProcessoService andamentoProcessoService;

	@Autowired
	private ArquivoProcessoEletronicoService arquivoProcessoEletronicoService;

	@Autowired
	private DocumentoEletronicoService documentoEletronicoService;

	@Autowired
	private PecaProcessoEletronicoService pecaProcessoEletronicoService;

	@Autowired
	private SessaoService sessaoService;

	private static final String TEXTO_SUSTENTACAO_ORAL = "Em desacordo com o previsto na Resolução nº 642, de 14 de junho de 2019 (art. 5º-A, § 3º); e no Procedimento Judiciário nº 11, de 04 de agosto de 2020 (art. 5º, incs I e II).";

	protected ManifestacaoRepresentanteServiceImpl(ManifestacaoRepresentanteDaoHibernate dao) {
		super(dao);
		// TODO Auto-generated constructor stub
	}

	public List<ManifestacaoRepresentante> listarManifestacoesPorIncidente(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		try {
			List<ManifestacaoRepresentante> lista = dao.listarManifestacoesPorIncidente(objetoIncidente);

			if (lista != null && lista.size() > 0)
				for (ManifestacaoRepresentante mr : lista)
					for (ManifestacaoLeitura ml : mr.getManifestacaoLeitura())
						Hibernate.initialize(ml.getMinistro());

			return lista;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional
	public void alterarSituacaoSustentacaoOral(ManifestacaoRepresentante m, AndamentoProcesso andamento) throws ServiceException {
		try {
			if (andamento != null) {
				Long numeroUltimoAndamento = andamentoProcessoService.recuperarUltimoNumeroSequencia(m.getObjetoIncidente());
				andamento.setNumeroSequencia(++numeroUltimoAndamento);
				andamento.setDescricaoObservacaoAndamento(TEXTO_SUSTENTACAO_ORAL);
				andamento.setLancamentoIndevido(false);
				andamentoProcessoService.salvar(andamento);

				PecaProcessoEletronico ppe = new PecaProcessoEletronico();
				ppe.setDescricaoPeca(Andamentos.ARQUIVO_DE_SUSTENTACAO_ORAL_REJEITADO.getDescricao());
				ppe.setObjetoIncidente(andamento.getObjetoIncidente());
				ppe.setTipoSituacaoPeca(TipoSituacaoPeca.JUNTADA);
				ppe.setTipoOrigemPeca(PecaProcessoEletronico.TIPO_ORIGEM_INTERNA);
				ppe.setTipoPecaProcesso(TipoPecaProcesso.TERMO_REJEICAO_SUSTENTACAO_ORAL);
				List<PecaProcessoEletronico> pecas = pecaProcessoEletronicoService.listarPecas(m.getObjetoIncidente().getPrincipal());
				ppe.setNumeroOrdemPeca(recuperarProximoNumeroOrdemPeca(pecas));
				pecaProcessoEletronicoService.salvar(ppe);

				byte[] pdf = criarArquivoPdf(andamento);

				DocumentoEletronico documentoEletronico = new DocumentoEletronico();
				documentoEletronico.setArquivo(pdf);
				documentoEletronico.setTipoArquivo(TipoArquivo.PDF);
				documentoEletronico.setTipoAcesso(DocumentoEletronico.TIPO_ACESSO_PUBLICO);
				documentoEletronico.setDescricaoStatusDocumento(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_DIGITALIZADO);
				documentoEletronico.setHashValidacao(AssinaturaDigitalServiceImpl.gerarHashValidacao());
				documentoEletronico.setSiglaSistema("ESTFREPGERAL");
				documentoEletronico = documentoEletronicoService.salvar(documentoEletronico);

				ArquivoProcessoEletronico arquivoProcessoEletronico = new ArquivoProcessoEletronico();
				arquivoProcessoEletronico.setPecaProcessoEletronico(ppe);
				arquivoProcessoEletronico.setDocumentoEletronico(documentoEletronico);
				arquivoProcessoEletronico = arquivoProcessoEletronicoService.salvar(arquivoProcessoEletronico);
			}

			m = dao.salvar(m);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	private Long recuperarProximoNumeroOrdemPeca(List<PecaProcessoEletronico> pecasProcesso) {
		long numeroOrdemPeca = 0;

		for (PecaProcessoEletronico pecaProcessoEletronico : pecasProcesso) {
			Long numero = pecaProcessoEletronico.getNumeroOrdemPeca();

			if (numero != null && numero.longValue() > numeroOrdemPeca)
				numeroOrdemPeca = numero.longValue();
		}

		return numeroOrdemPeca + 1;
	}

	private byte[] criarArquivoPdf(AndamentoProcesso andamento) throws ServiceException {
		try {
			String relatorio = "relatorios/textoProcessoSetor.jasper";

			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("BRASAO", this.getClass().getClassLoader().getResource("relatorios/Brasao_Republica_STF.jpg").toString());
			params.put("SETOR", andamento.getSetor().getNome());
			params.put("PROCESSO", andamento.getObjetoIncidente().getIdentificacao());
			params.put("TITULO", "TERMO DE REJEIÇÃO DE SUSTENTAÇÃO ORAL");
			params.put("CORPO", TEXTO_SUSTENTACAO_ORAL);
			return UtilJasperReports.criarRelatorioPdf(relatorio, null, params);
		} catch (FileNotFoundException e) {
			throw new ServiceException(e);
		} catch (JRException e) {
			e.printStackTrace();
			throw new ServiceException("Ocorreu um erro ao gerar a contrarrazão!", e);
		}
	}

	public List<InscricoesJulgamento> recuperarInscritosSessaoJulgamento(Long sessaoId, boolean sustentacaoOral, boolean participacaoEmJulgamento,
			boolean julgamentoPresencial, boolean julgamentoVideoConferencia) throws ServiceException {
		try {
			return dao
					.recuperarInscritosSessaoJulgamento(sessaoId, sustentacaoOral, participacaoEmJulgamento, julgamentoPresencial, julgamentoVideoConferencia);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public List<InscricoesJulgamento> recuperarInscritosSessaoJulgamento(Sessao sessao, boolean sustentacaoOral, boolean participacaoEmJulgamento,
			boolean julgamentoPresencial, boolean julgamentoVideoConferencia) throws ServiceException {
		return recuperarInscritosSessaoJulgamento(sessao.getId(), sustentacaoOral, participacaoEmJulgamento, julgamentoPresencial, julgamentoVideoConferencia);
	}

}