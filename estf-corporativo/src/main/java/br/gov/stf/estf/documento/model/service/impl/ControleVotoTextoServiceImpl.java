package br.gov.stf.estf.documento.model.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.service.ControleVotoService;
import br.gov.stf.estf.documento.model.service.ControleVotoTextoService;
import br.gov.stf.estf.documento.model.service.TextoService;
import br.gov.stf.estf.documento.model.util.ControleDeVotoDTO;
import br.gov.stf.estf.documento.model.util.TipoSessaoControleVoto;
import br.gov.stf.estf.entidade.documento.ControleVoto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoSituacaoTexto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.julgamento.model.service.SessaoService;
import br.gov.stf.estf.julgamento.model.service.VotoJulgamentoProcessoService;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.util.Dispositivo;
import br.gov.stf.framework.model.service.ServiceException;

@Service("controleVotoTextoService")
public class ControleVotoTextoServiceImpl implements ControleVotoTextoService {
	private final ControleVotoService controleVotoService;
	private final TextoService textoService;
	private final MinistroService ministroService;
	private final ObjetoIncidenteService objetoIncidenteService;
	private final SessaoService sessaoService;
	
	@Autowired
	private VotoJulgamentoProcessoService votoJulgamentoProcessoService;

	public ControleVotoTextoServiceImpl(ControleVotoService controleVotoService, TextoService textoService, MinistroService ministroService, 
			ObjetoIncidenteService objetoIncidenteService, SessaoService sessaoService) {
		super();
		this.controleVotoService = controleVotoService;
		this.textoService = textoService;
		this.ministroService = ministroService;
		this.objetoIncidenteService = objetoIncidenteService;
		this.sessaoService = sessaoService;
	}

	public void criarControleVoto(ControleDeVotoDTO controleVoto) throws ServiceException {		
		ObjetoIncidente objetoIncidente = objetoIncidenteService.recuperarPorId(controleVoto.getObjetoIncidente().getId());
		Date dataSessao = controleVoto.getDataSessao();
		TipoSessaoControleVoto tipoSessaoControleVoto = controleVoto.getTipoSessaoControleVoto();
		Ministro relator = ministroService.recuperarPorId(objetoIncidente.getRelatorIncidenteId());
		
		Sessao sessao = controleVoto.getSessao();
		
		// Não cria controles de votos específicos para determinados dispositivos
		List<Dispositivo> dispositivosParaNaoCriarControleDeVoto = Arrays.asList(Dispositivo.PEDIDO_DE_VISTA, Dispositivo.SUSPENSO_O_JULGAMENTO);
		
		if (!dispositivosParaNaoCriarControleDeVoto.contains(controleVoto.getDispositivo())) {
			criarControleVotoEmentaAcordao(objetoIncidente, dataSessao, tipoSessaoControleVoto, TipoTexto.EMENTA, controleVoto.getSessao());
			criarControleVotoEmentaAcordao(objetoIncidente, dataSessao, tipoSessaoControleVoto, TipoTexto.ACORDAO, controleVoto.getSessao());
		}
		
		Texto relatorio = textoService.recuperar(objetoIncidente, TipoTexto.RELATORIO, relator.getId());
		
		if (relatorio != null && (relatorio.getSequenciaVoto() == null || relatorio.getSequenciaVoto() == 0))
			criarControleVotoRelatorioVoto(objetoIncidente, dataSessao, tipoSessaoControleVoto, TipoTexto.RELATORIO, relator, relatorio, null, sessao);
		
		Texto voto = textoService.recuperar(objetoIncidente, TipoTexto.VOTO, relator.getId());
		
		if (voto != null && (voto.getSequenciaVoto() == null || voto.getSequenciaVoto() == 0))
			criarControleVotoRelatorioVoto(objetoIncidente, dataSessao, tipoSessaoControleVoto, TipoTexto.VOTO, relator, voto, null, sessao);
		
		// Cria controle de votos para textos do tipo VOTO_VOGAL e VOTO_VISTA
		List<Texto> textos = textoService.pesquisar(objetoIncidente, TipoTexto.VOTO_VOGAL, TipoTexto.VOTO_VISTA);
		
		for (Texto texto : textos) {
			Ministro ministro = texto.getMinistro();
			
			if (temVotoMinistroProcesso(objetoIncidente, ministro) && texto.getTipoFaseTextoDocumento().getCodigoFase() >= FaseTexto.REVISADO.getCodigoFase())
				if (TipoTexto.VOTO_VOGAL.equals(texto.getTipoTexto()) || TipoTexto.VOTO_VISTA.equals(texto.getTipoTexto()))
					criarControleVotoRelatorioVoto(objetoIncidente, dataSessao, tipoSessaoControleVoto, texto.getTipoTexto(), ministro, texto, null, sessao);
			
		}
	}
	
	private boolean temVotoMinistroProcesso(ObjetoIncidente objetoIncidente, Ministro ministro) throws ServiceException {
		return votoJulgamentoProcessoService.temVotoMinistroProcesso(objetoIncidente, ministro);
	}

	public boolean criarControleVotoEmentaAcordao(ObjetoIncidente objetoIncidente, Date dataSessao,TipoSessaoControleVoto tipoSessao, TipoTexto tipoTexto, Sessao sessao) throws ServiceException {
		objetoIncidente = objetoIncidenteService.recuperarPorId(objetoIncidente.getId());
		Ministro relatorAcordao = ministroService.recuperarRelatorAcordao(objetoIncidente);
		Ministro relator = ministroService.recuperarPorId(objetoIncidente.getRelatorIncidenteId());
		ControleVoto cv = controleVotoService.recuperar(objetoIncidente, tipoTexto, relator);
		
		if (cv != null)
			throw new ServiceException(String.format("O processo %s já possui controle de votos e, por isso, não é possível publicar a decisão.", objetoIncidente.getIdentificacao()));
		
		Texto texto = textoService.recuperarTextoSemControleVoto(objetoIncidente, tipoTexto, relator);
		boolean criado = false;
		if ( relatorAcordao==null ) {			
			if ( cv==null ) {
				cv = inserirNovoControleVoto(objetoIncidente, dataSessao, relator, texto, tipoSessao, tipoTexto, null, sessao);
				criado = true;
			}
			if ( texto!=null ) {
				this.sincronizaControleVotoComTexto(dataSessao, cv, texto);
			}
		} else {
			if ( cv==null ) {
				cv = controleVotoService.recuperarControleDeVotoSemSessao(objetoIncidente, tipoTexto, relatorAcordao);
				if ( cv==null ) {
					cv = inserirNovoControleVoto(objetoIncidente, dataSessao, relatorAcordao, texto, tipoSessao, tipoTexto, null, sessao);
					criado = true;
				}
			}
			if ( texto!=null ) {
				this.sincronizaControleVotoComTexto(dataSessao, cv, texto);
			} else {
				texto = textoService.recuperar(objetoIncidente, tipoTexto, relatorAcordao.getId());
				if ( texto!=null ) {
					this.sincronizaControleVotoComTexto(dataSessao, cv, texto);
				}
			}
		}
		return criado;
	}

	void sincronizaControleVotoComTexto(Date dataSessao, ControleVoto cv,Texto texto) throws ServiceException {
		cv.setDataSessao(dataSessao);
		cv.setTexto(texto);
		controleVotoService.alterar(cv);
		
		texto = textoService.recarregar(texto);
		texto.setSequenciaVoto( cv.getSequenciaVoto() );
		texto.setDataSessao(dataSessao);
		texto.setControleVoto(cv);
		textoService.alterar(texto);
	}
	
	@SuppressWarnings("rawtypes")
	private boolean criarControleVotoRelatorioVoto(ObjetoIncidente objetoIncidente, Date dataSessao, TipoSessaoControleVoto tipoSessao, TipoTexto tipoTexto, Ministro ministro, Texto texto, Long seqVotos, Sessao sessao) throws ServiceException {
		boolean criado = false;
		
		if (texto.getSequenciaVoto() == null || texto.getSequenciaVoto() == 0L) { // Só cria controle de votos para texto não sincronizado (sem seq_votos)
			ControleVoto cv = controleVotoService.recuperar(objetoIncidente, tipoTexto, ministro);
			
			if (cv == null) {
				cv = inserirNovoControleVoto(objetoIncidente, dataSessao, ministro, texto, tipoSessao, tipoTexto, seqVotos, sessao);
				criado = true;
			}
			
			if (texto != null)
				this.sincronizaControleVotoComTexto(dataSessao, cv, texto);
		}

		return criado;
	}

	@SuppressWarnings("rawtypes")
	private ControleVoto inserirNovoControleVoto(ObjetoIncidente objetoIncidente, Date dataSessao, Ministro ministro, Texto texto, TipoSessaoControleVoto tipoSessao, TipoTexto tipoTexto, Long seqVotos, Sessao sessao) throws ServiceException {
		
		List<TipoTexto> tiposTextoUnicos = Arrays.asList(TipoTexto.EMENTA, TipoTexto.ACORDAO);
		List<ControleVoto> listaCVs = null;
		
		if (tiposTextoUnicos.contains(tipoTexto))
			listaCVs = controleVotoService.pesquisarControleVoto(objetoIncidente, null, null, tipoTexto, null);
		
		if (listaCVs == null || listaCVs.size() == 0) {
			if (seqVotos == null)
				seqVotos = controleVotoService.recuperarProximaSequenciaVoto(objetoIncidente);
			
			ControleVoto cv = new ControleVoto();
			cv.setObjetoIncidente(objetoIncidente);
			cv.setDataSessao(dataSessao);
			cv.setSequenciaVoto(seqVotos);
			cv.setMinistro(ministro);			
			cv.setTexto(texto);		
			cv.setTipoSituacaoTexto(TipoSituacaoTexto.ATIVO_NO_CONTROLE_DE_VOTOS);
			cv.setSessao(tipoSessao);		
			cv.setTipoTexto(tipoTexto);
			cv.setSessaoJulgamento(sessao);
			
			controleVotoService.incluir(cv);
			controleVotoService.flushSession();
			
			return cv;
		}
		
		return null;
	}

	@SuppressWarnings("rawtypes")
	public void criarControleVotoRepercussaoGeral(Long seqObjetoIncidente) throws ServiceException {

		if(seqObjetoIncidente == null || seqObjetoIncidente.longValue() <= 0)
			throw new ServiceException("Nenhum processo para gerar o controle de voto da repercussão geral.");

		ObjetoIncidente oi = objetoIncidenteService.recuperarPorId(seqObjetoIncidente);
		
		Sessao sessao = sessaoService.recuperar(seqObjetoIncidente);
		
		if(sessao == null)
			throw new ServiceException("Não existe nenhuma sessão para o objeto incidente: " + seqObjetoIncidente);
		
		Ministro ministroRelator = ministroService.recuperarMinistroRelator(oi);
		Ministro ministroRedator = ministroService.recuperarRedatorAcordao(oi);
		
		if (ministroRelator != null && ministroRedator != null && !ministroRelator.equals(ministroRedator))
			ministroRelator = ministroRedator;
		
		List<Texto> listaTextosDoProcesso = textoService.pesquisarTextoRepercussaoGeralVotoValido(seqObjetoIncidente);
		
		Set<Texto> listaTextoMinistroRelator = new HashSet<Texto>();
		Set<Texto> listaTextoOutrosMinistros = new HashSet<Texto>();
		
		for(Texto texto : listaTextosDoProcesso)
			if(ministroRelator.equals(texto.getMinistro()))
				listaTextoMinistroRelator.add(texto);
			else
				listaTextoOutrosMinistros.add(texto);
		
		if(listaTextoMinistroRelator == null || listaTextoMinistroRelator.size() != 3) {
			boolean possuiEmentaRepGeral = false;
			boolean possuiDecisaoRepGeral = false;
			boolean possuiManifestacaoRepGeral = false;
			
			if (listaTextoMinistroRelator != null) {
				for(Texto texto : listaTextoMinistroRelator) {
					if(TipoTexto.EMENTA_SOBRE_REPERCURSAO_GERAL.equals(texto.getTipoTexto()))
						possuiEmentaRepGeral = true;
					
					if(TipoTexto.DECISAO_SOBRE_REPERCURSAO_GERAL.equals(texto.getTipoTexto()))
						possuiDecisaoRepGeral = true;
					
					if(TipoTexto.MANIFESTACAO_SOBRE_REPERCUSAO_GERAL.equals(texto.getTipoTexto()))
						possuiManifestacaoRepGeral = true;
				}
			}
			
			if (!possuiEmentaRepGeral)
				salvarControleVotoRepercussaoGeral(oi, ministroRelator, TipoTexto.EMENTA_SOBRE_REPERCURSAO_GERAL, sessao, false, null);
			
			if (!possuiDecisaoRepGeral)
				salvarControleVotoRepercussaoGeral(oi, ministroRelator, TipoTexto.DECISAO_SOBRE_REPERCURSAO_GERAL, sessao, false, null);
			
			if (!possuiManifestacaoRepGeral)
				salvarControleVotoRepercussaoGeral(oi, ministroRelator, TipoTexto.MANIFESTACAO_SOBRE_REPERCUSAO_GERAL, sessao, false, null);
		}
		
		//insere o controle de votos para os textos do ministro relator
		for(Texto texto : listaTextoMinistroRelator)
			salvarControleVotoRepercussaoGeral(oi, ministroRelator, texto.getTipoTexto(), sessao, texto.getPublico(), texto);
		
		//insere o controle de votos para os textos dos outros ministros
		for(Texto texto : listaTextoOutrosMinistros)
			salvarControleVotoRepercussaoGeral(oi, texto.getMinistro(), texto.getTipoTexto(), sessao, texto.getPublico(), texto);
	}

	private void salvarControleVotoRepercussaoGeral(ObjetoIncidente oi, Ministro ministro, TipoTexto tipoTexto, Sessao sessao, Boolean publico, Texto texto) throws ServiceException {
		ControleVoto cv = null;
		
		cv = controleVotoService.recuperar(oi, tipoTexto, ministro);
		
		if(cv == null) {
			Long proximoControleVoto = null;
			Date dataRecebimentoGabinete = null;
			
			proximoControleVoto = controleVotoService.recuperarProximaSequenciaVotoRepercussaoGeral(oi);
			
			if(publico)
				dataRecebimentoGabinete = new Date();
			
			ControleVoto novoControleVoto = criarNovoControleVoto(oi, sessao.getDataFim(), ministro, TipoSessaoControleVoto.PLENARIO, 
																	proximoControleVoto, tipoTexto, dataRecebimentoGabinete, "E", 
																	"RG", TipoSituacaoTexto.ATIVO_NO_CONTROLE_DE_VOTOS, texto);
			
			ControleVoto cvIncluido = controleVotoService.incluir(novoControleVoto);
			
			Texto textoAtualizarControleVoto = textoService.recuperar(oi.getId(), "RG", tipoTexto, ministro.getId());
			
			if (textoAtualizarControleVoto != null) {
				Date dataSessao = cvIncluido.getDataSessao();
				dataSessao.setHours(0);
				dataSessao.setMinutes(0);
				dataSessao.setSeconds(0);
				textoAtualizarControleVoto.setDataSessao(dataSessao);
				textoAtualizarControleVoto.setSequenciaVoto(cvIncluido.getSequenciaVoto());
				textoService.alterar(textoAtualizarControleVoto);
			}
		}
	}
	
	private ControleVoto criarNovoControleVoto(
			ObjetoIncidente<?> objetoIncidente, Date dataSessao, Ministro relator, TipoSessaoControleVoto tipoSessao, Long seqVotos, TipoTexto tipoTexto, Date dataRecebimentoGabinete,
			String oralEscrito, String tipoJulgamento, TipoSituacaoTexto tipoSituacaoTexto,	Texto texto) throws ServiceException {
		
		ControleVoto cv = new ControleVoto();
		cv.setObjetoIncidente(objetoIncidente);
		cv.setDataSessao(dataSessao);
		cv.setSessao(tipoSessao);
		cv.setSequenciaVoto(seqVotos);
		cv.setMinistro(relator);
		cv.setTipoTexto(tipoTexto);
		cv.setDataRecebimentoGab(dataRecebimentoGabinete);
		cv.setDataPublico(dataRecebimentoGabinete);
		cv.setOralEscrito(oralEscrito);
		cv.setTipoJulgamento(tipoJulgamento);
		cv.setTipoSituacaoTexto(tipoSituacaoTexto);
		cv.setTexto(texto);		
		
		return cv;
	}

}
