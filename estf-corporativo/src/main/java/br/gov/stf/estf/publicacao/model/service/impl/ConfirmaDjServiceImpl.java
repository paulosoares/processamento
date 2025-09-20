package br.gov.stf.estf.publicacao.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.cabecalho.model.InformacoesParte.InformacaoParte;
import br.gov.stf.estf.cabecalho.service.CabecalhoObjetoIncidenteService;
import br.gov.stf.estf.documento.model.service.PecaProcessoEletronicoService;
import br.gov.stf.estf.documento.model.service.TextoAndamentoProcessoService;
import br.gov.stf.estf.documento.model.service.TextoProtocoloService;
import br.gov.stf.estf.documento.model.service.TextoService;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TextoAndamentoProcesso;
import br.gov.stf.estf.entidade.documento.TextoPeticao;
import br.gov.stf.estf.entidade.documento.TipoSituacaoPeca;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.localizacao.Advogado;
import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.AndamentoProtocolo;
import br.gov.stf.estf.entidade.processostf.ControlePrazoIntimacao;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoIntegracao;
import br.gov.stf.estf.entidade.processostf.Protocolo;
import br.gov.stf.estf.entidade.processostf.ProtocoloPublicado;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;
import br.gov.stf.estf.localizacao.model.service.AdvogadoService;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoService;
import br.gov.stf.estf.processostf.model.service.AndamentoProtocoloService;
import br.gov.stf.estf.processostf.model.service.ControlePrazoIntimacaoService;
import br.gov.stf.estf.processostf.model.service.IntegracaoOrgaoParteService;
import br.gov.stf.estf.processostf.model.service.ProcessoIntegracaoService;
import br.gov.stf.estf.processostf.model.service.ProcessoService;
import br.gov.stf.estf.processostf.model.service.ProtocoloPublicadoService;
import br.gov.stf.estf.publicacao.model.service.ConfirmaDjService;
import br.gov.stf.estf.publicacao.model.service.ProcessoPublicadoService;
import br.gov.stf.framework.model.service.ServiceException;

@Service("confirmaDjService")
public class ConfirmaDjServiceImpl implements ConfirmaDjService {
	
	private  final ProcessoPublicadoService processoPublicadoService;
	private  final AndamentoProcessoService andamentoProcessoService;
	private  final AndamentoProtocoloService andamentoProtocoloService;
	private  final ProcessoService processoService;
	private  final TextoProtocoloService textoProtocoloService;
	private  final TextoService textoService;
	private  final TextoAndamentoProcessoService textoAndamentoProcessoService;
	private  final PecaProcessoEletronicoService pecaProcessoEletronicoService;
	private  final AdvogadoService advogadoService;
	private  final ControlePrazoIntimacaoService controlePrazoIntimacaoService;
	private  final ProtocoloPublicadoService protocoloPublicadoService;
	private  final IntegracaoOrgaoParteService integracaoOrgaoParteService;
	private  final ProcessoIntegracaoService processoIntegracaoService;
	private  final CabecalhoObjetoIncidenteService cabecalhoObjetoIncidenteService;

	public ConfirmaDjServiceImpl(
			ProcessoPublicadoService processoPublicadoService,
			AndamentoProcessoService andamentoProcessoService,
			AndamentoProtocoloService andamentoProtocoloService,
			ProcessoService processoService,
			TextoProtocoloService textoProtocoloService,
			TextoService textoService,
			TextoAndamentoProcessoService textoAndamentoProcessoService,
			PecaProcessoEletronicoService pecaProcessoEletronicoService,			
			AdvogadoService advogadoService,
			ControlePrazoIntimacaoService controlePrazoIntimacaoService,
			ProtocoloPublicadoService protocoloPublicadoService,
			IntegracaoOrgaoParteService integracaoOrgaoParteService,
			ProcessoIntegracaoService processoIntegracaoService,
			CabecalhoObjetoIncidenteService cabecalhoObjetoIncidenteService) {
		super();
		this.processoPublicadoService = processoPublicadoService;
		this.andamentoProcessoService = andamentoProcessoService;
		this.andamentoProtocoloService = andamentoProtocoloService;
		this.processoService = processoService;
		this.textoProtocoloService = textoProtocoloService;
		this.textoService = textoService;
		this.textoAndamentoProcessoService = textoAndamentoProcessoService;
		this.pecaProcessoEletronicoService = pecaProcessoEletronicoService;		
		this.advogadoService = advogadoService;
		this.controlePrazoIntimacaoService = controlePrazoIntimacaoService;
		this.protocoloPublicadoService = protocoloPublicadoService;
		this.integracaoOrgaoParteService = integracaoOrgaoParteService;
		this.processoIntegracaoService = processoIntegracaoService;
		this.cabecalhoObjetoIncidenteService = cabecalhoObjetoIncidenteService;
	}

	public List<ProcessoPublicado> pesquisarProcessosPublicados(
			ConteudoPublicacao conteudoPublicacao) throws ServiceException {
		return processoPublicadoService.pesquisarProcessosDj(conteudoPublicacao.getCodigoCapitulo(), conteudoPublicacao.getCodigoMateria(), 
				conteudoPublicacao.getAno(), conteudoPublicacao.getNumero(), Boolean.FALSE);
	}
	
	@SuppressWarnings("rawtypes")
	public Long recuperarNumeroSequencial(ObjetoIncidente objetoIncidente) throws ServiceException {
		Long seq = andamentoProcessoService.recuperarProximoNumeroSequencia(objetoIncidente);
		return seq;
	}
	public Long recuperarNumeroSequencial(Protocolo protocolo)
		throws ServiceException {
		return andamentoProtocoloService.recuperarUltimoNumeroSequencia(protocolo)+1;
	}
	
	public void inserirAndamentoProcesso(AndamentoProcesso andamentoProcesso)
		throws ServiceException {
		andamentoProcessoService.incluir(andamentoProcesso);
	
	}
	
	public void inserirAndamentoProtocolo(AndamentoProtocolo andamentoProtocolo)
		throws ServiceException {
		andamentoProtocoloService.incluir(andamentoProtocolo);
	
	}

	public void inserirAndamentoIntimacao(ProcessoPublicado processoPublicado, String siglaUsuario, Setor setor)
			throws ServiceException {
		List<InformacaoParte> partes = cabecalhoObjetoIncidenteService.recuperarCabecalho( processoPublicado.getObjetoIncidente().getId() ).getInformacoesParte().getInformacaoParte();
		
		if (partes != null && partes.size() > 0) {
			for (InformacaoParte cp : partes) {
				if ( cp.getCategoriaParte().startsWith("ADV") ) {
					Advogado ad = advogadoService.recuperarPorId( cp.getIdParte() );
					if (ad != null && ad.getTipoIntimacao().equals(Advogado.TIPO_INTIMACAO_ELETRONICA)) {
						ObjetoIncidente<?> principal = processoPublicado.getObjetoIncidente().getPrincipal();
						Long numSequencia = andamentoProcessoService.recuperarProximoNumeroSequencia(principal);
						AndamentoProcesso ap = new AndamentoProcesso();
						ap.setCodigoAndamento(8403L);
						ap.setCodigoUsuario(siglaUsuario);
						ap.setDataAndamento(new Date());
						ap.setDataHoraSistema(new Date());
						ap.setDescricaoObservacaoAndamento(ad.getNome());
						ap.setNumeroSequencia(numSequencia);
						ap.setObjetoIncidente( processoPublicado.getObjetoIncidente() );
						ap.setSetor(setor);
						andamentoProcessoService.incluir(ap);

						ControlePrazoIntimacao cpi = new ControlePrazoIntimacao();
						cpi.setAndamentoProcesso(ap);
						cpi.setDataIntimacao(new Date());
						cpi.setParte(cp.getIdParte());
						cpi.setPrazo(10);
						controlePrazoIntimacaoService.incluir(cpi);

						Origem origem = integracaoOrgaoParteService.recuperarOrigem(cp.getIdParte());

						if (origem != null) {
							ProcessoIntegracao processoIntegracao = new ProcessoIntegracao();
							processoIntegracao.setAndamentoProcesso(ap);
							processoIntegracao.setCodigoOrgao(origem.getId());
							processoIntegracao.setPecaProcessoEletronico(null);
							processoIntegracao.setProcesso(processoPublicado.getObjetoIncidente().getPrincipal() );
							processoIntegracao.setTipoSituacao(ProcessoIntegracao.TIPO_SITUACAO_NAO_ENVIADO);

							processoIntegracaoService.incluir(processoIntegracao);

						}
					}
				} else {

					Origem origem = integracaoOrgaoParteService.recuperarOrigem(cp.getIdParte());

					if (origem != null) {
						ObjetoIncidente<?> principal = processoPublicado.getObjetoIncidente().getPrincipal();
						Long numSequencia = andamentoProcessoService.recuperarProximoNumeroSequencia(principal);
						AndamentoProcesso ap = new AndamentoProcesso();
						ap.setCodigoAndamento(8403L);
						ap.setCodigoUsuario(siglaUsuario);
						ap.setDataAndamento(new Date());
						ap.setDataHoraSistema(new Date());
						ap.setDescricaoObservacaoAndamento(cp.getApresentacaoParte());
						ap.setNumeroSequencia(numSequencia);
						ap.setSetor(setor);
						ap.setObjetoIncidente( processoPublicado.getObjetoIncidente() );
						andamentoProcessoService.incluir(ap);

						ProcessoIntegracao processoIntegracao = new ProcessoIntegracao();
						processoIntegracao.setAndamentoProcesso(ap);
						processoIntegracao.setCodigoOrgao(origem.getId());
						processoIntegracao.setPecaProcessoEletronico(null);
						processoIntegracao.setProcesso(processoPublicado.getObjetoIncidente().getPrincipal());
						processoIntegracao.setTipoSituacao(ProcessoIntegracao.TIPO_SITUACAO_NAO_ENVIADO);

						processoIntegracaoService.incluir(processoIntegracao);

					}
				}

			}
		}

	}
	
	public void alterarDeslocamentoProcessoEletronico(Processo processo, Setor setor) throws ServiceException {
		processoService.alterarDeslocamentoProcessoEletronico(processo, setor);
	}

	public void alterarSituacaoPecaProcessoEletronico(ProcessoPublicado processoPublicado)
			throws ServiceException {
		List<PecaProcessoEletronico> pecas = pecaProcessoEletronicoService.pesquisar(processoPublicado.getObjetoIncidente(), TipoSituacaoPeca.PENDENTE);
		if ( pecas!=null && pecas.size()>0 ) {
			for ( PecaProcessoEletronico ppe: pecas ) {
				ppe.setTipoSituacaoPeca( TipoSituacaoPeca.JUNTADA );
				pecaProcessoEletronicoService.alterar(ppe);
			}
		}
	
	}
	
	public void inserirTextoAndamentoAcordao(ProcessoPublicado processoPublicado, AndamentoProcesso andamentoProcesso) throws ServiceException {
		List<Texto> textos = textoService.pesquisar(processoPublicado.getObjetoIncidente(), TipoTexto.DECISAO, true);
		if ( textos!=null && textos.size()>0 ) {
			Texto t = textos.get( textos.size()-1 );
			if ( t.getPublico() ) {
				TextoAndamentoProcesso tap = new TextoAndamentoProcesso();
				tap.setAndamentoProcesso(andamentoProcesso);
				tap.setTexto(t);
				textoAndamentoProcessoService.incluir(tap);
			}
		}
		textos = textoService.pesquisar(processoPublicado.getObjetoIncidente(), TipoTexto.EMENTA, true);
		if ( textos!=null && textos.size()>0 ) {
			Texto t = textos.get( textos.size()-1 );
			if ( t.getPublico() ) {
				TextoAndamentoProcesso tap = new TextoAndamentoProcesso();
				tap.setAndamentoProcesso(andamentoProcesso);
				tap.setTexto(t);
				textoAndamentoProcessoService.incluir(tap);
			}
		}
	
	}

	public void inserirTextoAndamento( ProcessoPublicado processoPublicado, AndamentoProcesso andamentoProcesso) throws ServiceException {
		List<Texto> textos = textoService.pesquisar(processoPublicado.getObjetoIncidente(), null, false);
		
		if ( textos!=null && textos.size()>0 ) {
			for ( Texto t: textos ) {
				if ( t.getPublico() && t.getArquivoEletronico().getId().equals( processoPublicado.getArquivoEletronicoTexto().getId() ) ) {
					TextoAndamentoProcesso tap = new TextoAndamentoProcesso();
					tap.setAndamentoProcesso(andamentoProcesso);
					tap.setTexto(t);
					textoAndamentoProcessoService.incluir(tap);
				}
			}
		}
	
	}
	
	public void alterarBaixaProcesso(Processo processo) throws ServiceException {
		processoService.alterarBaixaProcesso(processo);
	
	}	
	
	public List<TextoPeticao> pesquisarTextoProtocolos(
			ConteudoPublicacao conteudoPublicacao) throws ServiceException {
		return textoProtocoloService.pesquisar(conteudoPublicacao.getNumero(), conteudoPublicacao.getAno());
	}
	
	public List<ProtocoloPublicado> pesquisarProtocoloPublicado(
			ConteudoPublicacao conteudoPublicacao) throws ServiceException {
		return protocoloPublicadoService.pesquisar(conteudoPublicacao, Boolean.FALSE);
	}
		
}
