/**
 * 
 */
package br.jus.stf.estf.decisao.objetoincidente.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.estf.documento.model.service.ControleVotoService;
import br.gov.stf.estf.documento.model.util.TipoSessaoControleVoto;
import br.gov.stf.estf.entidade.documento.ControleVoto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoSituacaoTexto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.TipoIncidenteJulgamento;
import br.gov.stf.estf.entidade.util.ObjetoIncidenteUtil;
import br.gov.stf.estf.repercussaogeral.model.service.RepercussaoGeralService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.support.security.PermissionChecker;
import br.jus.stf.estf.decisao.texto.service.TextoService;

/**
 * @author Paulo.Estevao
 *
 */
@Action(id="novoControleVotosActionFacesBean", 
		name="Novo Controle de Votos", 
		view="/acoes/objetoincidente/novoControleVotos.xhtml",
		height=320)
@Restrict({ActionIdentification.CRIAR_CONTROLE_DE_VOTOS})
@RequiresResources(Mode.One)
public class NovoControleVotosActionFacesBean extends ActionSupport<ObjetoIncidenteDto> {
	
	@Qualifier("objetoIncidenteServiceLocal") 
	@Autowired 
	private ObjetoIncidenteService objetoIncidenteService;
	
	@Qualifier("textoServiceLocal") 
	@Autowired 
	private TextoService textoService;
	
	@Autowired
	private ControleVotoService controleVotoService;
	
	@Autowired
	private RepercussaoGeralService repercussaoGeralService;
	
	@Autowired
	private PermissionChecker permissionChecker;
	
	private String idTipoSessao;
	private Date dataSessao;
	private Long idTipoTexto;
	private String anotacoes;
	private String oralEscrito;
	private List<OralEscrito> itensOralEscrito;
	private List<TipoTexto> itensTipoTexto;
	private List<TipoSessaoControleVoto> itensTipoSessao;

	public static enum OralEscrito {
		ORAL("O", "Oral"), ESCRITO("E", "Escrito");

		private String codigo;
		private String descricao;
		
		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}

		public void setCodigo(String sessao) {
			this.codigo = sessao;
		}

		public String getCodigo() {
			return codigo;
		}
		
		private OralEscrito(String codigo, String descricao) {
			setCodigo(codigo);
			setDescricao(descricao);
		}
	}
	
	public void execute() {
		try {
			ObjetoIncidente<?> oi = objetoIncidenteService.recuperarObjetoIncidentePorId(getObjetoIncidente().getId());
			
			ControleVoto controleVoto = new ControleVoto();
			controleVoto.setObjetoIncidente(oi);
			controleVoto.setSequenciaVoto(controleVotoService.recuperarProximaSequenciaVoto(oi));
			controleVoto.setTipoSituacaoTexto(TipoSituacaoTexto.ATIVO_NO_CONTROLE_DE_VOTOS);
			controleVoto.setMinistro(getMinistro());
			controleVoto.setTipoTexto(getTipoTexto());
			controleVoto.setSessao(getTipoSessao());
			controleVoto.setDataSessao(getDataSessao());
			controleVoto.setAnotacoes(getAnotacoes());
			controleVoto.setOralEscrito(getOralEscrito());

			validate(controleVoto);
			

			List<Texto> textos = textoService.pesquisar(getObjetoIncidente(), controleVoto.getTipoTexto(), controleVoto.getMinistro());
			for (Texto texto : textos) {
				if (texto.getSequenciaVoto() != null && texto.getSequenciaVoto() != 0L)
					textos.remove(texto);
			}
			if (textos != null && textos.size() > 0) {
				if (textos.size() > 1)
					throw new ServiceException(
							"Não é possível criar controle de votos para esse tipo de texto pois já existe mais de um texto com esse tipo de texto.");
				else {
					Texto texto = textos.get(0);
					
					// A partir da fase assinado não é permitido criar controle de votos
					if (FaseTexto.fasesComTextoAssinado.contains(texto.getTipoFaseTextoDocumento()))
						throw new ServiceException("Não é permitido criar o controle de votos, pois o texto \""
								+ texto.getTipoTexto().getDescricao() + "\" encontra-se na fase \""
								+ texto.getTipoFaseTextoDocumento().getDescricao() + "\".");
					controleVoto.setTexto(texto);
				}
			}
			
			controleVotoService.incluir(controleVoto);
		} catch (ServiceException e) {
			addError(e.getMessage());
		} catch (Exception e) {
			addError(e.getMessage());
		}
		
		if(hasMessages()) {
			sendToErrors();
		} else {
			sendToConfirmation();
		}
		
		setRefresh(true);
		
	}
	
	public void validate(ControleVoto cv) throws ServiceException {
		if (cv.getDataSessao() == null)
			throw new ServiceException("A Data da Sessão deve ser informada.");
		if (cv.getSessao() == null)
			throw new ServiceException("O Tipo de Sessão deve ser informado.");
		if (cv.getTipoTexto() == null)
			throw new ServiceException("O Tipo de Texto deve ser informado.");
		if (cv.getOralEscrito() == null)
			throw new ServiceException("Deve ser informado se o Controle de Votos é Escrito ou Oral.");

		controleVotoService.validarTipoTextoNovoControleVoto(cv);
	}
	
	public void voltar() {
		getDefinition().setFacet("principal");
	}
	
	public ObjetoIncidenteDto getObjetoIncidente() {
		if (getResources() != null && !getResources().isEmpty()) {
			if (getResources().size() == 1) {
				return getResources().iterator().next();
			}
		}
		return null;
	}
	
	public TipoTexto getTipoTexto() {
		return TipoTexto.valueOf(idTipoTexto);
	}

	public TipoSessaoControleVoto getTipoSessao() {
		return TipoSessaoControleVoto.valueOfCodigo(idTipoSessao);
	}

	public Date getDataSessao() {
		return dataSessao;
	}

	public void setDataSessao(Date dataSessao) {
		this.dataSessao = dataSessao;
	}

	public String getAnotacoes() {
		return anotacoes;
	}

	public void setAnotacoes(String anotacoes) {
		this.anotacoes = anotacoes;
	}

	public String getOralEscrito() {
		return oralEscrito;
	}

	public void setOralEscrito(String oralEscrito) {
		this.oralEscrito = oralEscrito;
	}

	public List<OralEscrito> getItensOralEscrito() {
		if(itensOralEscrito == null) {
			itensOralEscrito = new ArrayList<OralEscrito>();
			itensOralEscrito.add(OralEscrito.ORAL);
			itensOralEscrito.add(OralEscrito.ESCRITO);
		}		
		return itensOralEscrito;
	}

	public void setItensOralEscrito(List<OralEscrito> itensOralEscrito) {
		this.itensOralEscrito = itensOralEscrito;
	}

	public List<TipoTexto> getItensTipoTexto() {
		try {
			if (itensTipoTexto == null) {
				itensTipoTexto = new LinkedList<TipoTexto>();
				if (permissionChecker.hasPermission(getPrincipal(), ActionIdentification.CRIAR_CONTROLE_DE_VOTOS)) {
					itensTipoTexto.addAll(Arrays.asList(TipoTexto.values()));
					itensTipoTexto.remove(TipoTexto.REVISAO_DE_APARTES);
					itensTipoTexto.remove(TipoTexto.DESPACHO);
					itensTipoTexto.remove(TipoTexto.DECISAO_MONOCRATICA);
	
					// Somente serão incluídos caso o incidente seja de Repercussão
					// Geral
					itensTipoTexto.remove(TipoTexto.DECISAO_SOBRE_REPERCURSAO_GERAL);
					itensTipoTexto.remove(TipoTexto.EMENTA_SOBRE_REPERCURSAO_GERAL);
					itensTipoTexto.remove(TipoTexto.MANIFESTACAO_SOBRE_REPERCUSAO_GERAL);
	
					// Inclusão dos tipos de texto acima caso o incidente seja de Repercussão Geral
					ObjetoIncidente<?> oi = objetoIncidenteService.recuperarObjetoIncidentePorId(getObjetoIncidente().getId());
					Processo processo = ObjetoIncidenteUtil.getProcesso(oi);
					TipoIncidenteJulgamento tipoIncidenteJulgamento = ObjetoIncidenteUtil.getTipoJulgamento(oi);
					if (tipoIncidenteJulgamento != null && tipoIncidenteJulgamento.getSigla() != null && (tipoIncidenteJulgamento.getSigla().equals(TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL)
							|| tipoIncidenteJulgamento.getSigla().equals(TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL_SEGUNDO_JULGAMENTO))) {
						// recupera se o julgametno da repercussão geral foi finalizado.
						Boolean finalizado = repercussaoGeralService.julgamentoFinalizado(processo);
	
						if (!finalizado) {
							itensTipoTexto.add(TipoTexto.MANIFESTACAO_SOBRE_REPERCUSAO_GERAL);
						}
						if (processo.getMinistroRelatorAtual().getId().equals(getMinistro().getId())) {
							if (finalizado) {
								itensTipoTexto.add(TipoTexto.DECISAO_SOBRE_REPERCURSAO_GERAL);
							}
							itensTipoTexto.add(TipoTexto.EMENTA_SOBRE_REPERCURSAO_GERAL);
						}
					}
				} else {
					itensTipoTexto.add(TipoTexto.ACORDAO);
					itensTipoTexto.add(TipoTexto.RELATORIO);
					itensTipoTexto.add(TipoTexto.EMENTA);
					itensTipoTexto.add(TipoTexto.VOTO);
				}
				if (itensTipoTexto != null && itensTipoTexto.size() > 0)
					Collections.sort(itensTipoTexto);
	
			}
		} catch (Exception e) {
			addError(e.getMessage());
		}
		return itensTipoTexto;
	}

	public void setItensTipoTexto(List<TipoTexto> itensTipoTexto) {
		this.itensTipoTexto = itensTipoTexto;
	}

	public List<TipoSessaoControleVoto> getItensTipoSessao() {
		if(itensTipoSessao == null) {
			itensTipoSessao = new ArrayList<TipoSessaoControleVoto>();
			itensTipoSessao.add(TipoSessaoControleVoto.PLENARIO);
			itensTipoSessao.add(TipoSessaoControleVoto.PRIMEIRA_TURMA);
			itensTipoSessao.add(TipoSessaoControleVoto.SEGUNDA_TURMA);
		}
		return itensTipoSessao;
	}

	public void setItensTipoSessao(List<TipoSessaoControleVoto> itensTipoSessao) {
		this.itensTipoSessao = itensTipoSessao;
	}

	public String getIdTipoSessao() {
		return idTipoSessao;
	}

	public void setIdTipoSessao(String idTipoSessao) {
		this.idTipoSessao = idTipoSessao;
	}

	public Long getIdTipoTexto() {
		return idTipoTexto;
	}

	public void setIdTipoTexto(Long idTipoTexto) {
		this.idTipoTexto = idTipoTexto;
	}
	
	@Override
	public String getErrorTitle() {
		return "Erro ao criar Controle de Votos";
	}
	
}
