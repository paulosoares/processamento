package br.gov.stf.estf.julgamento.model.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.rtf.RTFEditorKit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.gov.stf.estf.documento.model.service.ArquivoEletronicoService;
import br.gov.stf.estf.documento.model.service.ControleVotoService;
import br.gov.stf.estf.documento.model.service.TextoAndamentoProcessoService;
import br.gov.stf.estf.documento.model.service.TextoService;
import br.gov.stf.estf.documento.model.service.exception.TextoSemControleDeVotosException;
import br.gov.stf.estf.documento.model.util.TipoSessaoControleVoto;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.ControleVoto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.Texto.TipoRestricao;
import br.gov.stf.estf.entidade.documento.TextoAndamentoProcesso;
import br.gov.stf.estf.entidade.documento.TipoSituacaoTexto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.ManifestacaoRepresentante;
import br.gov.stf.estf.entidade.julgamento.ProcessoListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.TipoResultadoJulgamento;
import br.gov.stf.estf.entidade.julgamento.TipoSituacaoProcessoSessao;
import br.gov.stf.estf.entidade.julgamento.TipoVoto;
import br.gov.stf.estf.entidade.julgamento.TipoVoto.TipoVotoConstante;
import br.gov.stf.estf.entidade.julgamento.VotoJulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.enuns.SituacaoListaJulgamento;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.ministro.Ocorrencia;
import br.gov.stf.estf.entidade.processostf.Agendamento;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.Andamento.Andamentos;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.OrigemAndamentoDecisao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;
import br.gov.stf.estf.entidade.publicacao.TipoSessao;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.entidade.util.NomeProprioUtil;
import br.gov.stf.estf.julgamento.model.dataaccess.ListaJulgamentoDao;
import br.gov.stf.estf.julgamento.model.service.ListaJulgamentoService;
import br.gov.stf.estf.julgamento.model.service.ManifestacaoRepresentanteService;
import br.gov.stf.estf.julgamento.model.service.ProcessoListaJulgamentoService;
import br.gov.stf.estf.julgamento.model.service.SessaoService;
import br.gov.stf.estf.julgamento.model.service.TipoListaJulgamentoService;
import br.gov.stf.estf.julgamento.model.service.VotoJulgamentoProcessoService;
import br.gov.stf.estf.julgamento.model.util.ListaJulgamentoSearchData;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.estf.processostf.model.service.AgendamentoService;
import br.gov.stf.estf.processostf.model.service.AndamentoProcessoService;
import br.gov.stf.estf.processostf.model.service.AndamentoService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.OrigemAndamentoDecisaoService;
import br.gov.stf.estf.processostf.model.service.SituacaoMinistroProcessoService;
import br.gov.stf.estf.publicacao.compordj.builder.BuilderHelper;
import br.gov.stf.estf.publicacao.model.service.ConteudoPublicacaoService;
import br.gov.stf.estf.publicacao.model.service.ProcessoPublicadoService;
import br.gov.stf.estf.util.Excel;
import br.gov.stf.estf.util.RTFUtils;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.entity.TipoSexo;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;
import br.gov.stf.framework.util.DateTimeHelper;
import br.gov.stf.framework.util.SearchResult;


/**
 * @author Paulo.Estevao
 *
 */
@Service("listaJulgamentoService")
public class ListaJulgamentoServiceImpl extends GenericServiceImpl<ListaJulgamento, Long, ListaJulgamentoDao>
		implements ListaJulgamentoService {
	
	private final Log logger = LogFactory.getLog(ListaJulgamentoServiceImpl.class);
	
	@Autowired
	private ConteudoPublicacaoService conteudoPublicacaoService;

	@Autowired
	private TextoService textoService;
	
	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;
	
	@Autowired
	MinistroService ministroService;
	
	@Autowired
	SessaoService sessaoService;
	
	@Autowired
	TipoListaJulgamentoService tipoListaJulgamentoService;
	
	@Autowired
	TextoAndamentoProcessoService textoAndamentoProcessoService;

	@Autowired
	ArquivoEletronicoService arquivoEletronicoService;

	@Autowired
	ProcessoPublicadoService processoPublicadoService;
	
	@Autowired
	AgendamentoService agendamentoService;

	@Autowired
	OrigemAndamentoDecisaoService origemAndamentoDecisaoService;
	
	@Autowired
	AndamentoProcessoService andamentoProcessoService;
	
	@Autowired
	AndamentoService andamentoService;

	@Autowired
	VotoJulgamentoProcessoService votoJulgamentoProcessoService;
	
	@Autowired
	ControleVotoService controleVotoService;
	
	@Autowired
	SituacaoMinistroProcessoService situacaoMinistroProcessoService;
	
	@Autowired
	ManifestacaoRepresentanteService manifestacaoRepresentanteService;
	
	@Autowired
	private ProcessoListaJulgamentoService processoListaJulgamentoService;
	
	public ListaJulgamentoServiceImpl(ListaJulgamentoDao dao) {
		super(dao);
	}
	
	@Override
	public List<ListaJulgamento> pesquisarListaPorObjetoIncidenteSessao(ObjetoIncidente<?> objetoIncidente,
			String colegiado) throws ServiceException {
		try {
			return dao.pesquisarListaPorObjetoIncidenteSessao(objetoIncidente, colegiado);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<ListaJulgamento> pesquisar(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		return pesquisar(objetoIncidente, false);
	}

	@Override
	public Integer recuperarMaiorOrdemSessao(Sessao sessao) throws ServiceException {
		try {
			return dao.recuperarMaiorOrdemSessao(sessao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Integer recuperarMaiorOrdemSessaoMinistro(Sessao sessao, Ministro ministro) throws ServiceException {
		try {
			return dao.recuperarMaiorOrdemSessaoMinistro(sessao, ministro);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public Integer recuperarMaiorOrdemSessaoMinistroListaJulgamentoAno(Ministro ministro, short ano) throws ServiceException {
		try {
			return dao.recuperarMaiorOrdemSessaoMinistroListaJulgamentoAno(ministro, ano);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	
	@Override
	public List<ListaJulgamento> pesquisar(ObjetoIncidente<?> objetoIncidente,
			boolean pesquisarApenasListasPrevistasParaJulgamento) throws ServiceException {
		try {
			return dao.pesquisar(objetoIncidente, pesquisarApenasListasPrevistasParaJulgamento);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<ListaJulgamento> pesquisar(Ministro ministro) throws ServiceException {
		try {
			return dao.pesquisar(ministro);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public ListaJulgamento pesquisar(ObjetoIncidente<?> objetoIncidente, JulgamentoProcesso julgamentoProcesso) throws ServiceException {
		try {
			return dao.pesquisar(objetoIncidente, julgamentoProcesso);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<ListaJulgamento> pesquisarPorColegiado(TipoColegiadoConstante colegiado) throws ServiceException {
		try {
			return dao.pesquisarPorColegiado(colegiado);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Long consultaQuantidadeListasSemSessao(TipoColegiadoConstante colegiado) throws ServiceException {
		try {
			return dao.consultaQuantidadeListasSemSessao(colegiado);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional
	public void definirSessaoJulgamento(ListaJulgamento listaJulgamento, Sessao sessao) throws ServiceException {
		Integer ordemSessao = definirOrdemSessao(sessao);
		listaJulgamento.setOrdemSessao(ordemSessao);
		Integer ordemSessaoMinistro = definirOrdemSessaoMinistro(sessao, listaJulgamento.getMinistro());
		listaJulgamento.setOrdemSessaoMinistro(ordemSessaoMinistro);
		listaJulgamento.setSessao(sessao);
		salvar(listaJulgamento);
	}

	private Integer definirOrdemSessaoMinistro(Sessao sessao, Ministro ministro) throws ServiceException {
		Integer ordemSessaoMinistro = recuperarMaiorOrdemSessaoMinistro(sessao, ministro);
		if (ordemSessaoMinistro == null) {
			ordemSessaoMinistro = 0;
		}
		return ++ordemSessaoMinistro;
	}

	private Integer definirOrdemSessao(Sessao sessao) throws ServiceException {
		Integer ordemSessao = recuperarMaiorOrdemSessao(sessao);
		if (ordemSessao == null) {
			ordemSessao = 0;
		}
		return ++ordemSessao;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void reordenarListas(ListaJulgamento listaJulgamentoBase, Integer ordemDestino) throws ServiceException {
		try {
			listaJulgamentoBase = recuperarPorId(listaJulgamentoBase.getId());
			dao.atualizarOrdenacaoListas(listaJulgamentoBase, ordemDestino);
			listaJulgamentoBase.setOrdemSessao(ordemDestino);
			salvar(listaJulgamentoBase);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void excluir(ListaJulgamento listaJulgamento) throws ServiceException {
		try {
			excluirProcessosDaLista(listaJulgamento);
			dao.excluir(listaJulgamento);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	private void excluirProcessosDaLista(ListaJulgamento listaJulgamento) throws ServiceException {
		try {
			if (listaJulgamento != null && listaJulgamento.getListaProcessoListaJulgamento() != null)
				for (ProcessoListaJulgamento plj : listaJulgamento.getListaProcessoListaJulgamento())
					for (ManifestacaoRepresentante mr : plj.getManifestacoes()) {
						mr.setListaJulgamento(null);
						manifestacaoRepresentanteService.alterar(mr);
					}
			
				Set<ObjetoIncidente<?>> processosRemover = listaJulgamento.getElementos();
				listaJulgamento.getElementos().removeAll(processosRemover);
				alterar(listaJulgamento);
				
		} catch (ServiceException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public ListaJulgamento alterar(ListaJulgamento listaJulgamento) throws ServiceException {
		listaJulgamento.setSituacaoListaJulgamento(SituacaoListaJulgamento.LISTA_MODIFICADA);
		return super.alterar(listaJulgamento);
	}

	@SuppressWarnings("unchecked")
	public SearchResult<ListaJulgamento> pesquisarListaJulgamentoPlenarioVirtual(ListaJulgamentoSearchData searchData) throws ServiceException {
		try {
			SearchResult resultado = dao.pesquisarListaJulgamentoPlenarioVirtual(searchData);
			
//			if(resultado.getResultCollection() != null)
//				for (ListaJulgamento lj : (List<ListaJulgamento>)resultado.getResultCollection()) {
//					for (ProcessoListaJulgamento plj : lj.getListaProcessoListaJulgamento()) {
//						Hibernate.initialize(plj.getObjetoIncidente().getIdentificacao());
//						Hibernate.initialize(plj.getObjetoIncidente().getPrincipal().getIdentificacao());
//					}
//				}
			return resultado;
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public ListaJulgamento carregarListaJulgamentoParaPlenarioVirtual(ListaJulgamento listaJulgamento) throws ServiceException {
		try {
			if (listaJulgamento != null) {
				ListaJulgamento lj = dao.recuperarPorId(listaJulgamento.getId());
				Hibernate.initialize(lj.getListaProcessoListaJulgamento());

				for (ProcessoListaJulgamento plj : lj.getListaProcessoListaJulgamento()) {
					Hibernate.initialize(plj.getObjetoIncidente().getIdentificacao());
					Hibernate.initialize(plj.getObjetoIncidente().getPrincipal().getIdentificacao());
					if (plj.getJulgamentoProcesso() != null) {
						Hibernate.initialize(plj.getJulgamentoProcesso());
						Hibernate.initialize(plj.getJulgamentoProcesso().getListaVotoJulgamentoProcesso());
					}
				}
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return listaJulgamento;
	}
	
	private String getStringMinistrosImpedidos(JulgamentoProcesso julgamentoProcesso) {
		
		if (julgamentoProcesso != null && julgamentoProcesso.isImpedimento()) {
			List<Ministro> ministrosImpedidos = new ArrayList<Ministro>();
			for (VotoJulgamentoProcesso voto : julgamentoProcesso.getListVotoJulgamentoProcessoValido()) {
				if (voto.getTipoVoto().getId()
						.equals(TipoVoto.TipoVotoConstante.IMPEDIDO.getCodigo()))
					ministrosImpedidos.add(voto.getMinistro());
			}
			String artigo = getArtigoIdentificadorMinistros(ministrosImpedidos);
			StringBuffer strMinistrosImpedidos = new StringBuffer();
			strMinistrosImpedidos.append(" Impedid" + artigo + " " + artigo + " Ministr" + artigo + " ");
			for (Iterator<Ministro> it = ministrosImpedidos.iterator(); it.hasNext();) {
				Ministro ministro = it.next();
				strMinistrosImpedidos.append(NomeProprioUtil.primeiraMaiuscula(ministro.getNome().toLowerCase().replaceFirst("min. ", "")));
				if (it.hasNext())
					strMinistrosImpedidos.append(", ");
				else
					strMinistrosImpedidos.append(".");
			}
			return strMinistrosImpedidos.toString();
		}
		return "";
	}

	private String getStringMinistrosSuspeitos(JulgamentoProcesso julgamentoProcesso) {

		if (julgamentoProcesso != null && julgamentoProcesso.isSuspeicao()) {
			List<Ministro> ministrosSuspeitos = new ArrayList<Ministro>();
			for (VotoJulgamentoProcesso voto : julgamentoProcesso.getListVotoJulgamentoProcessoValido()) {
				if (voto.getTipoVoto().getId()
						.equals(TipoVoto.TipoVotoConstante.SUSPEITO.getCodigo()))
					ministrosSuspeitos.add(voto.getMinistro());
			}
			String artigo = getArtigoIdentificadorMinistros(ministrosSuspeitos);
			StringBuffer strMinistrosSuspeitos = new StringBuffer();
			String pessoaVerbo = artigo.contains("s") ? "aram" : "ou";
			strMinistrosSuspeitos.append(" Afirm" + pessoaVerbo + " suspeição " + artigo + " Ministr" + artigo + " ");
			for (Iterator<Ministro> it = ministrosSuspeitos.iterator(); it.hasNext();) {
				Ministro ministro = it.next();
				strMinistrosSuspeitos.append(NomeProprioUtil.primeiraMaiuscula(ministro.getNome().toLowerCase().replaceFirst("min. ", "")));
				if (it.hasNext())
					strMinistrosSuspeitos.append(", ");
				else
					strMinistrosSuspeitos.append(".");
			}
			return strMinistrosSuspeitos.toString();
		}
		return "";
	}

	private String getArtigoIdentificadorMinistros(List<Ministro> ministros) {
		String artigo = ministros.size() > 1 ? "s" : "";
		String qualificador = "a";
		for (Ministro ministro : ministros) {
			if (ministro.getTipoSexo().getValor().equals(TipoSexo.MASCULINO))
				qualificador = "o";
		}
		artigo = qualificador + artigo;
		return artigo;
	}

	public void registrarResultadoJulgamento(List<ListaJulgamento> listasJulgamento) throws ServiceException {

		for (ListaJulgamento listaJulgamento : listasJulgamento) {
			TipoResultadoJulgamento tipoResultado = null;

			listaJulgamento.setJulgado(Boolean.TRUE);
			listaJulgamento.setPublicado(Boolean.FALSE);

			listaJulgamento.setPublicacaoAutomatica(Boolean.FALSE);
			listaJulgamento.setDecisaoUniforme(Boolean.FALSE);

			if (isResultadoUniforme(listaJulgamento)) {

				tipoResultado = calculaResultado(listaJulgamento);

				if (tipoResultado != null) {
					listaJulgamento.setDecisaoUniforme(Boolean.TRUE);
					if (tipoResultado.equals(TipoResultadoJulgamento.DECISAO_UNANIME))
						listaJulgamento.setPublicacaoAutomatica(Boolean.TRUE);
					else
						listaJulgamento.setPublicacaoAutomatica(Boolean.FALSE);
				}
			}

			listaJulgamento.setTipoResultadoJulgamento(tipoResultado);
			salvar(listaJulgamento);
		}
	}

	private TipoResultadoJulgamento calculaResultado(ListaJulgamento listaJulgamento) {
		ResultadoVotacao resultadoVotacao = new ResultadoVotacao();

		if (listaJulgamento.getListaProcessoListaJulgamento() == null
				|| listaJulgamento.getListaProcessoListaJulgamento().size() == 0) {
			return TipoResultadoJulgamento.DECISAO_UNANIME;
		}
		for (ProcessoListaJulgamento processoListaJulgamento : listaJulgamento.getListaProcessoListaJulgamento()) {
			JulgamentoProcesso julgamento = processoListaJulgamento.getJulgamentoProcesso();
			if (julgamento != null
					&& !julgamento.getSituacaoProcessoSessao().equals(TipoSituacaoProcessoSessao.DESTAQUE)
					&& !julgamento.getSituacaoProcessoSessao().equals(TipoSituacaoProcessoSessao.SUSPENSO)) {
				if (julgamento.getUnanime() != null && julgamento.getUnanime())
					return TipoResultadoJulgamento.DECISAO_UNANIME;
				contaVotosJulgamento(julgamento, resultadoVotacao);
				if (resultadoVotacao.quantidadeVotosProRelator > resultadoVotacao.quantidadeVotosContrariosRelator)
					return TipoResultadoJulgamento.MAIORIA_A_FAVOR_DO_RELATOR;
				if (resultadoVotacao.quantidadeVotosProRelator < resultadoVotacao.quantidadeVotosContrariosRelator)
					return TipoResultadoJulgamento.MAIORIA_CONTRA_O_RELATOR;
				if (resultadoVotacao.quantidadeVotosProRelator == resultadoVotacao.quantidadeVotosContrariosRelator)
					return TipoResultadoJulgamento.EMPATE;

			}
		}
		return null;
	}

	private void contaVotosJulgamento(JulgamentoProcesso julgamento, ResultadoVotacao resultadoVotacao) {
		resultadoVotacao.quantidadeEnvolvidosSessao = julgamento.getSessao().getListaEnvolvidoSessao().size();

		for (VotoJulgamentoProcesso voto : julgamento.getListaVotoJulgamentoDefinitivo()) {
			if (voto.getTipoVoto().getId()
					.equals(TipoVotoConstante.ACOMPANHO_RELATOR.getCodigo())
					|| voto.getTipoVoto().getId()
							.equals(TipoVotoConstante.ACOMPANHO_RELATOR_RESSALVA.getCodigo()))
				resultadoVotacao.quantidadeVotosProRelator = resultadoVotacao.quantidadeVotosProRelator + 1;

			if (voto.getTipoVoto().getId().equals(TipoVotoConstante.DIVERGENTE.getCodigo())
					|| voto.getTipoVoto().getId()
							.equals(TipoVotoConstante.ACOMPANHO_DIVERGENCIA.getCodigo())) {
				resultadoVotacao.quantidadeVotosContrariosRelator = resultadoVotacao.quantidadeVotosContrariosRelator
						+ 1;
				if (voto.getMinistro().getId().equals(Ministro.COD_MINISTRO_MARCO_AURELIO))
					resultadoVotacao.setVotoMinistroMA(Boolean.TRUE);
			}

			if (voto.getTipoVoto().getId().equals(TipoVotoConstante.IMPEDIDO.getCodigo()))
				resultadoVotacao.quantidadeImpedimentos = resultadoVotacao.quantidadeImpedimentos + 1;
			if (voto.getTipoVoto().getId().equals(TipoVotoConstante.SUSPEITO.getCodigo()))
				resultadoVotacao.quantidadeSuspeitos = resultadoVotacao.quantidadeSuspeitos + 1;
		}
		// voto do ministro relator
		resultadoVotacao.quantidadeVotosProRelator = resultadoVotacao.quantidadeVotosProRelator + 1;
		resultadoVotacao.quantidadeAbstencoes = resultadoVotacao.quantidadeEnvolvidosSessao
				- resultadoVotacao.quantidadeVotosContrariosRelator - resultadoVotacao.quantidadeVotosProRelator
				- resultadoVotacao.quantidadeImpedimentos - resultadoVotacao.quantidadeSuspeitos;
	}

	private boolean isResultadoUniforme(ListaJulgamento listaJulgamento) {
		List<VotoMinistro> resultadoInicial = null;
		List<VotoMinistro> resultadoASerComparado = null;
		for (ProcessoListaJulgamento processoListaJulgamento : listaJulgamento.getListaProcessoListaJulgamento()) {
			JulgamentoProcesso julgamento = processoListaJulgamento.getJulgamentoProcesso();
			if (null == resultadoInicial) {
				resultadoInicial = new ArrayList<VotoMinistro>();
				if (julgamento != null && julgamento.getListaVotoJulgamentoDefinitivo() != null)
					for (VotoJulgamentoProcesso voto : julgamento.getListaVotoJulgamentoDefinitivo())
						resultadoInicial
								.add(new VotoMinistro(voto.getMinistro(), voto.getTipoVoto()));
				Collections.sort(resultadoInicial);
			} else {
				resultadoASerComparado = new ArrayList<VotoMinistro>();
				if (julgamento != null && julgamento.getListaVotoJulgamentoDefinitivo() != null) {
					for (VotoJulgamentoProcesso voto : julgamento.getListaVotoJulgamentoDefinitivo())
						resultadoASerComparado
								.add(new VotoMinistro(voto.getMinistro(), voto.getTipoVoto()));
				}

				Collections.sort(resultadoASerComparado);
				if (!resultadoASerComparado.equals(resultadoInicial))
					return false;

			}
		}
		return true;
	}
	
	//verifica se a lista pode ser dada como publicada
	public Boolean verificarListaPublicada(ListaJulgamento listaJulgamento) throws ServiceException{
		ListaJulgamento lj = recuperarPorId(listaJulgamento.getId());
		for (ProcessoListaJulgamento plj : lj.getListaProcessoListaJulgamento()){	
			if (plj.getJulgamentoProcesso().isDestaque())
				continue;
			
			boolean existeDecisao = textoService.recuperarTextoSemControleVoto(plj.getObjetoIncidente(),TipoTexto.DECISAO, listaJulgamento.getMinistro()) != null;
			
			if (!existeDecisao)
				return false;
		}
		return true;
	}		

	@Transactional(rollbackFor = ServiceException.class)
	public ConteudoPublicacao publicarProcesso(ListaJulgamento listaJulgamento,
			List<ProcessoListaJulgamento> processosListaJulgamento, Usuario usuario, Setor setor,
			Long ministroParaRelatorAcordaoRA, String motivoDesignacaoRelatorAcordao) throws ServiceException {

		logger.info("O usuario " + usuario.getId() + " iniciou a publicacao da lista de julgamento de id igual: "
				+ listaJulgamento.getId());

		ConteudoPublicacao materia = criarMateriaAta(listaJulgamento, setor);

		List<ProcessoListaJulgamento> processosComDecisoesDiferentes = new ArrayList<ProcessoListaJulgamento>();
		List<ProcessoListaJulgamento> processosComDecisoesIguais = new ArrayList<ProcessoListaJulgamento>();

		for (ProcessoListaJulgamento processoListaJulgamento : processosListaJulgamento) {
			if (processosListaJulgamento.size() == 1 || processoListaJulgamento.getJulgamentoProcesso().isImpedimento()
					|| processoListaJulgamento.getJulgamentoProcesso().isSuspeicao()) {

				processosComDecisoesDiferentes.add(processoListaJulgamento);

			} else {
				processosComDecisoesIguais.add(processoListaJulgamento);
			}
		}

		if (!processosComDecisoesDiferentes.isEmpty()) {
			for (ProcessoListaJulgamento processoListaJulgamento : processosComDecisoesDiferentes) {

				String ministrosComImpedimentoSuspeicao = getStringMinistrosImpedidos(processoListaJulgamento.getJulgamentoProcesso())
						+ getStringMinistrosSuspeitos(processoListaJulgamento.getJulgamentoProcesso());

				ArquivoEletronico arquivoEletronico = criarArquivoEletronico(listaJulgamento,
						ministrosComImpedimentoSuspeicao);

				Texto texto = criarTexto(listaJulgamento, processoListaJulgamento.getObjetoIncidente(),
						arquivoEletronico, Boolean.FALSE, materia);
				
				logger.info(
						"----------------------------> Publicando o processo (id):" + processoListaJulgamento.getId());
				executarAcoesParaPublicarLista(listaJulgamento, processoListaJulgamento, usuario, materia, texto,
						ministroParaRelatorAcordaoRA, motivoDesignacaoRelatorAcordao);
			}
		}

		if (!processosComDecisoesIguais.isEmpty()) {

			ArquivoEletronico arquivoEletronico = criarArquivoEletronico(listaJulgamento, "");

			for (ProcessoListaJulgamento processoListaJulgamento : processosComDecisoesIguais) {
				Texto texto = criarTexto(listaJulgamento, processoListaJulgamento.getObjetoIncidente(),
						arquivoEletronico, (processosComDecisoesIguais.size() == 1 ? Boolean.FALSE : Boolean.TRUE), materia);

				logger.info(
						"----------------------------> Publicando o processo (id):" + processoListaJulgamento.getId());
				executarAcoesParaPublicarLista(listaJulgamento, processoListaJulgamento, usuario, materia, texto,
						ministroParaRelatorAcordaoRA, motivoDesignacaoRelatorAcordao);
			}
		}

		atualizarListaJulgamento(listaJulgamento);

		return materia;
	}
	
	private ArquivoEletronico criarArquivoEletronico(ListaJulgamento listaJulgamento, String ministrosComImpedimentoSuspeicao) throws ServiceException {
		
		String textoDecisao = listaJulgamento.getTextoDecisao() + ministrosComImpedimentoSuspeicao
				+ listaJulgamento.getDescricaoSessaoColegiadoPeriodo();
		
		byte[] decisaoConteudo = montarTextoDecisao("Decisão: ", textoDecisao);

		ArquivoEletronico arquivoEletronicoUnico = criaArquivoEletronico(decisaoConteudo);

		logger.info("Arquivo eletronico criado (id):" + arquivoEletronicoUnico.getId());
		
		return arquivoEletronicoUnico;
	}
	
	private void executarAcoesParaPublicarLista(ListaJulgamento listaJulgamento, ProcessoListaJulgamento processo,
			Usuario usuario, ConteudoPublicacao materia, Texto texto, Long ministroParaRelatorAcordaoRA, 
			String motivoRA) throws ServiceException {

		Ministro ministroRA = null;
		
		if(listaJulgamento.getTextoDecisao().length() > 4000) {
			listaJulgamento.setTextoDecisao(listaJulgamento.getTextoDecisao().substring(0, 3850));
		}

		if (ministroParaRelatorAcordaoRA != null) {
			logger.info("###Iniciada a designacao do relator para o acordao.###");
			
			ministroRA = ministroService.recuperarPorId(ministroParaRelatorAcordaoRA);
			
			executarAcoesParaDesignarRelatorAcordao(listaJulgamento, processo, ministroRA, motivoRA);

			logger.info("###Finalizada a designacao para o acordao dos processos selecionados.###");
		}
		
		associarTextoAoProcesso(processo, texto, listaJulgamento.getAndamentoPublicacao());

		criarAtualizarControleVoto(listaJulgamento, processo.getObjetoIncidente(), materia);

		incluirProcessoEmAta(processo.getObjetoIncidente(), materia);

		removerProcessoAgendamento(listaJulgamento, materia, processo.getObjetoIncidente());
		
		tratarDispositivoSuspensoJulgamento(listaJulgamento, materia, processo.getObjetoIncidente());

		atualizarVistaMinistro(materia, processo.getObjetoIncidente(), listaJulgamento);

		AndamentoProcesso andamentoProcesso = gerarAndamentoProcesso(listaJulgamento, processo.getObjetoIncidente(), texto, usuario);

		if (ministroParaRelatorAcordaoRA != null) {

			if (processo.getObjetoIncidente().getTipoObjetoIncidente().equals(TipoObjetoIncidente.PROCESSO)) {

				if (motivoRA == null || "RA".equalsIgnoreCase(motivoRA)) {
					andamentoProcessoService.gerarAndamentoBasico(Andamentos.SUBSTITUICAO_DO_RELATOR.getId(),
							processo.getObjetoIncidente(), usuario, ministroRA.getNome(), null);
				} else {
					andamentoProcessoService.gerarAndamentoBasico(Andamentos.SUBSTITUICAO_DO_RELATOR_IV_B.getId(),
							processo.getObjetoIncidente(), usuario, ministroRA.getNome(), null);
				}
			}
			logger.info("Andamento gerado - Substituicao de Relator para o acordao.");
		}
		
		associarAndametoTexto(texto, andamentoProcesso);
	}
	
	private void executarAcoesParaDesignarRelatorAcordao(ListaJulgamento listaJulgamento,
			ProcessoListaJulgamento processo, Ministro ministroRA, String motivoRA) throws ServiceException {

		try {

			ObjetoIncidente<?> oi = objetoIncidenteService.deproxy(processo.getObjetoIncidente());
			
			logger.info("Preservando os textos de ementa e acordao do relator atual.");
			textoService.preservarTextosRelatorAnteriorNaDesignacaoRedatorAcordao(oi);
		
			logger.info("Persistindo o relator para o acordao para o processo: " + oi.getIdentificacao());
			
			if(motivoRA == null || "RA".equalsIgnoreCase(motivoRA)) {
				situacaoMinistroProcessoService.designarNovoRelatorAcordao(oi, ministroRA, Ocorrencia.REDATOR_ACORDAO);
			}else {
				situacaoMinistroProcessoService.designarNovoRelatorAcordao(oi, ministroRA, Ocorrencia.REDATOR_ACORDAO_RISTF);
			}

			logger.info("Excluindo textos de ementa e acordao do controle de voto do relator atual.");
			excluirEmentaAcordaoControleVotos(oi);
		
			logger.info("Relator para o acórdão definido.");
			}
			
		catch (ServiceException e) {
			logger.error("Erro nao foi possivel ajustar o relator do acordao.", e);
			throw new ServiceException("Não foi possível ajustar o relator do acórdão.", e);
		}
	}
	
	private void excluirEmentaAcordaoControleVotos(ObjetoIncidente<?> oi) throws ServiceException {
		
		try {
			List<ControleVoto> controleVotos = controleVotoService.pesquisarControleVotoPorTipoTexto(oi,
					TipoTexto.EMENTA, TipoTexto.ACORDAO);
			
			for (ControleVoto controleVoto : controleVotos) {
				controleVotoService.excluir(controleVoto);
			}
			
		} catch (ServiceException e) {
			logger.error("Erro nao foi possivel excluir os textos existentes de ementa e acordao.", e);
			throw new ServiceException("Não foi possível  excluir os textos existentes de ementa e  acórdão.", e);
		}
	}

	private void associarAndametoTexto(Texto texto, AndamentoProcesso andamentoProcesso) throws ServiceException {
		TextoAndamentoProcesso tap = new TextoAndamentoProcesso();
		tap.setAndamentoProcesso(andamentoProcesso);
		tap.setTexto(texto);
		textoAndamentoProcessoService.incluir(tap);
		
		if(tap != null)
			logger.info("Andamento associado com o texto - id do andamento processo: " + tap.getId());
	}

	private Texto criarTexto(ListaJulgamento listaJulgamento, ObjetoIncidente<?> objetoIncidente, ArquivoEletronico ae,
			Boolean textosIguais, ConteudoPublicacao materia) throws ServiceException {
		Texto t = new Texto();

		t.setDataCriacao(new Date());
		t.setArquivoEletronico(ae);
		t.setDataSessao(DateTimeHelper.getData(DateTimeHelper.getDataString(materia.getDataCriacao())));
		t.setMinistro(listaJulgamento.getMinistro());
		t.setObjetoIncidente(objetoIncidente);
		t.setPublico(true);
		t.setTipoTexto(TipoTexto.DECISAO);
		t.setTipoRestricao(TipoRestricao.P);
		t.setTextosIguais(textosIguais);
		t = textoService.incluir(t);

		logger.info("Texto criado (id):" + t.getId());
		
		return t;
	}

	private ArquivoEletronico criaArquivoEletronico(byte[] conteudo) throws ServiceException {

		ArquivoEletronico ae = new ArquivoEletronico();
		ae.setConteudo(conteudo);
		ae.setFormato("RTF");
		arquivoEletronicoService.incluir(ae);

		return ae;
	}

	private void associarTextoAoProcesso(ProcessoListaJulgamento processo, Texto texto, String andamentoPublicacaoSelecinado) throws NumberFormatException, ServiceException {
		
		Andamento andamento = andamentoService.recuperarPorId(Long.parseLong(andamentoPublicacaoSelecinado));
		
		processo.setAndamento(andamento);
		processo.setTexto(texto);
		
		logger.info("Processo associado ao texto - id do processo lista julgamento: " + processo.getId());
	}

	public TipoSessaoControleVoto obterColegiadoSessao(ListaJulgamento listaJulgamento) {

		String idColegiado = listaJulgamento.getSessao().getColegiado().getId();

		if (TipoColegiadoConstante.PRIMEIRA_TURMA.getSigla().equals(idColegiado)) {
			return TipoSessaoControleVoto.PRIMEIRA_TURMA;
		} else if (TipoColegiadoConstante.SEGUNDA_TURMA.getSigla().equals(idColegiado)) {
			return TipoSessaoControleVoto.SEGUNDA_TURMA;
		} else {
			return TipoSessaoControleVoto.PLENARIO;
		}
	}
	
	private ConteudoPublicacao criarMateriaAta(ListaJulgamento listaJulgamento, Setor setor) throws ServiceException {

		ConteudoPublicacao materiaCriada = conteudoPublicacaoService.pesquisarMateriaNaoCompostaNaoPublicadaPorSessao(listaJulgamento.getSessao());
		
		if (materiaCriada != null) {
			logger.info("Utilizando materia nao publicada existente - id da materia: " + materiaCriada.getId());
			return materiaCriada;
		}
		
		TipoSessao tipoSessao  = listaJulgamento.getSessao().getTipoSessao().
				equals(TipoSessao.ORDINARIA.getSigla()) ? TipoSessao.ORDINARIA : TipoSessao.EXTRAORDINARIA;
		
		Integer numeroDaMateria = listaJulgamento.getSessao().getTipoSessao().
				equals(TipoSessao.ORDINARIA.getSigla()) ? 12 : 13;

		Integer ano = DateTimeHelper.getAno(listaJulgamento.getSessao().getDataFim());

		ConteudoPublicacao materia = new ConteudoPublicacao();
		materia.setCodigoCapitulo(setor.getCodigoCapitulo());
		materia.setCodigoMateria(numeroDaMateria);
		materia.setCodigoConteudo(50);
		materia.setAno(ano.shortValue());
		materia.setTipoSessao(tipoSessao);
		materia.setDataCriacao(DateTimeHelper.getDataAtual());

		Long numeroMateria = listaJulgamento.getSessao().getNumero();

		materia.setNumero(Integer.parseInt(numeroMateria.toString()));
		materia.setSessao(listaJulgamento.getSessao());
		materia = conteudoPublicacaoService.incluir(materia);
	
		logger.info("Materia criada - id da materia: " + materia.getId());

		return materia;
	}

	private void incluirProcessoEmAta(ObjetoIncidente<?> oi, ConteudoPublicacao materia) throws ServiceException {
		ProcessoPublicado pp = new ProcessoPublicado();
		pp.setAnoMateria(materia.getAno());
		pp.setCodigoCapitulo(materia.getCodigoCapitulo());
		pp.setCodigoMateria(materia.getCodigoMateria());
		pp.setNumeroMateria(materia.getNumero());
		pp.setObjetoIncidente(oi);
		processoPublicadoService.incluir(pp);
		
		if(pp != null)
			logger.info("Processo incluido na materia - id  processos publicados: " + pp.getId());

	}

	private void removerProcessoAgendamento(ListaJulgamento listaJulgamento, ConteudoPublicacao materia,
			ObjetoIncidente<?> oi) throws ServiceException {

		
		if(!(Andamentos.VISTA_AOS_MINISTROS.getId().equals(Long.parseLong(listaJulgamento.getAndamentoPublicacao())) || 
				Andamentos.SUSPENSO_JULGAMENTO.getId().equals(Long.parseLong(listaJulgamento.getAndamentoPublicacao())))){
		
			Agendamento agendamento = agendamentoService.recuperar(materia.getCodigoCapitulo(), 2, oi);
		
			if (agendamento != null) {
				agendamentoService.excluir(agendamento);
				
				logger.info("Processo removido do agendamento - seq_objeto_incidente: " + oi.getId());
			}
		}
	}
	
	private void tratarDispositivoSuspensoJulgamento(ListaJulgamento listaJulgamento, ConteudoPublicacao materia,
			ObjetoIncidente<?> oi) throws ServiceException {
		
		if(Andamentos.SUSPENSO_JULGAMENTO.getId().equals(Long.parseLong(listaJulgamento.getAndamentoPublicacao()))){
		
			TipoSessaoControleVoto colegiado = obterColegiadoSessao(listaJulgamento);
			Agendamento agendamento = agendamentoService.recuperar(materia.getCodigoCapitulo(), 2, oi);
			
			if(TipoSessaoControleVoto.PLENARIO.getCodigo().equals(colegiado.getCodigo())) {
				if (agendamento != null) {
					agendamentoService.excluir(agendamento);
					
					logger.info("Processo removido do agendamento - seq_objeto_incidente: " + oi.getId());
				}
			}else {
				if (agendamento != null) {
					agendamento.setMinistro(listaJulgamento.getMinistro());
					agendamento.setVista(Boolean.FALSE);
					agendamentoService.alterar(agendamento);
					
					logger.info("Processo removido do agendamento - seq_objeto_incidente: " + oi.getId());
				}
			}
		}
	}

	private void atualizarVistaMinistro(ConteudoPublicacao materia, ObjetoIncidente<?> oi,
			ListaJulgamento listaJulgamento) throws ServiceException {

		if (Andamentos.VISTA_AOS_MINISTROS.getId().equals(Long.parseLong(listaJulgamento.getAndamentoPublicacao()))) {
	
			Agendamento agendamento = agendamentoService.recuperar(materia.getCodigoCapitulo(), 2, oi);

			if (agendamento != null) {
				List<OrigemAndamentoDecisao> origensComMinistroAtivo = origemAndamentoDecisaoService
						.pesquisarOrigensComMinistroAtivo();

				Ministro ministroPediuVista = null;
				for (OrigemAndamentoDecisao oad : origensComMinistroAtivo) {
					if (listaJulgamento.getIdMinistroPedidoVista().equals(oad.getId().longValue()))
						ministroPediuVista = oad.getMinistro();
				}

				agendamento.setMinistro(ministroPediuVista);
				agendamento.setVista(Boolean.TRUE);
				agendamentoService.alterar(agendamento);
				
				logger.info("Vista atualizada para o ministro: " + agendamento.getMinistro().getSigla());
			}
		}
	}

	private AndamentoProcesso gerarAndamentoProcesso(ListaJulgamento listaJulgamento, ObjetoIncidente<?> oi,
			Texto texto, Usuario usuario) throws ServiceException {

			String descricaoObservacaoAndamento = "";
		 	descricaoObservacaoAndamento = RTFUtils.converterRtfToString(texto.getArquivoEletronico().getConteudo());
		 	if(descricaoObservacaoAndamento.length() >= 3850){
		 		 descricaoObservacaoAndamento = descricaoObservacaoAndamento.substring(0, 3850).concat("...");
		 	}
		
		 	OrigemAndamentoDecisao origemAndamentoDecisao = getOrigemAndamentoDecisao(listaJulgamento);

			Long numSeq = andamentoProcessoService.recuperarProximoNumeroSequencia(oi);

			AndamentoProcesso andamentoProcesso = new AndamentoProcesso();

			andamentoProcesso.setCodigoAndamento(Long.parseLong(listaJulgamento.getAndamentoPublicacao()));
			andamentoProcesso.setCodigoUsuario(usuario.getId().toUpperCase());
			andamentoProcesso.setDataAndamento(new Date());
			andamentoProcesso.setDataHoraSistema(new Date());
			andamentoProcesso.setSetor(usuario.getSetor());
			andamentoProcesso.setObjetoIncidente(oi);
			andamentoProcesso.setDescricaoObservacaoAndamento(descricaoObservacaoAndamento);
			andamentoProcesso.setOrigemAndamentoDecisao(origemAndamentoDecisao);
			andamentoProcesso.setNumeroSequencia(numSeq);
			andamentoProcesso.setLancamentoIndevido(false);

			andamentoProcesso = andamentoProcessoService.salvar(andamentoProcesso);
			
			if(andamentoProcesso != null)
				logger.info("Andamento gerado - de decisao - id do andamento processo: " + andamentoProcesso.getId());

			return andamentoProcesso;
	}

	private void atualizarListaJulgamento(ListaJulgamento listaJulgamento) throws ServiceException  {
		
		logger.info("Finalizando a publicacao da lista.");
		
		Boolean existeDecisaoPendentePublicacao = Boolean.FALSE;
		
		for (ProcessoListaJulgamento proc : listaJulgamento.getListaProcessoListaJulgamento()) {
			if (proc.getTexto() == null) {
				existeDecisaoPendentePublicacao = Boolean.TRUE;
			}
		}
		
		listaJulgamento.setTextoDecisao("");
	
		if (existeDecisaoPendentePublicacao) {
			
			logger.info("Lista pendende de publicacao . Existem processo(s) "
					+ "para publicar.");
		}else {
			
			listaJulgamento.setJulgado(Boolean.TRUE);
			listaJulgamento.setPublicado(Boolean.TRUE);

			salvar(listaJulgamento);

			logger.info("Lista publicada  - id da lista: " + listaJulgamento.getId());
		}
		
	}
	
	static Long seqVotoInstancia = 0L;

	public void criarAtualizarControleVoto(ListaJulgamento listaJulgamento, ObjetoIncidente<?> objetoincidente, 
			ConteudoPublicacao materia)	
			throws ServiceException {
		
		ObjetoIncidente<?> oi = objetoIncidenteService.deproxy(objetoincidente);

		List<ControleVoto> controleVotos = controleVotoService.pesquisarControleVotoPorMinistro(oi, 
				listaJulgamento.getMinistro());

		Boolean temEmenta = Boolean.FALSE; 	Boolean temAcordao = Boolean.FALSE; 
		Boolean temRelatorio = Boolean.FALSE; Boolean temVoto = Boolean.FALSE;

		for (ControleVoto controleVoto : controleVotos) {
			if (TipoTexto.EMENTA.equals(controleVoto.getTipoTexto())) {
				temEmenta = Boolean.TRUE;
			}
			if (TipoTexto.ACORDAO.equals(controleVoto.getTipoTexto())) {
				temAcordao = Boolean.TRUE;
			}
			if (TipoTexto.RELATORIO.equals(controleVoto.getTipoTexto())) {
				temRelatorio = Boolean.TRUE;
			}
			if (TipoTexto.VOTO.equals(controleVoto.getTipoTexto())) {
				temVoto = Boolean.TRUE;
			}
		}

		if (temEmenta || temAcordao)
			throw new ServiceException(String.format(
					"O processo %s já possui controle de votos.",
					oi.getIdentificacao()));

		Ministro relatorAcordao = ministroService.recuperarRelatorAcordao(oi);
		
		seqVotoInstancia = controleVotoService.recuperarProximaSequenciaVoto(oi);

		if(!(Andamentos.VISTA_AOS_MINISTROS.getId().equals(Long.parseLong(listaJulgamento.getAndamentoPublicacao())) || 
				Andamentos.DESTAQUE_DO_MINISTRO.getId().equals(Long.parseLong(listaJulgamento.getAndamentoPublicacao())) ||
				Andamentos.SUSPENSO_JULGAMENTO.getId().equals(Long.parseLong(listaJulgamento.getAndamentoPublicacao())) ||
				Andamentos.JULGADO_MERITO_TEMA_COM_RG_SEM_TESE.getId().equals(Long.parseLong(listaJulgamento.getAndamentoPublicacao())))){
		
			inserirNovoControleVoto(listaJulgamento, oi, materia,
					(relatorAcordao == null ? listaJulgamento.getMinistro() : relatorAcordao), 
						TipoTexto.EMENTA, seqVotoInstancia);
			seqVotoInstancia += 10;
			logger.info("Controle de Voto criado para a EMENTA.");
		}
		
		if(!(Andamentos.VISTA_AOS_MINISTROS.getId().equals(Long.parseLong(listaJulgamento.getAndamentoPublicacao())) || 
				Andamentos.DESTAQUE_DO_MINISTRO.getId().equals(Long.parseLong(listaJulgamento.getAndamentoPublicacao())) ||
				Andamentos.SUSPENSO_JULGAMENTO.getId().equals(Long.parseLong(listaJulgamento.getAndamentoPublicacao())) ||
				Andamentos.JULGADO_MERITO_TEMA_COM_RG_SEM_TESE.getId().equals(Long.parseLong(listaJulgamento.getAndamentoPublicacao())))){
					
			inserirNovoControleVoto(listaJulgamento, oi, materia,
					(relatorAcordao == null ? listaJulgamento.getMinistro() : relatorAcordao), 
						TipoTexto.ACORDAO, seqVotoInstancia);
			seqVotoInstancia += 10;
			logger.info("Controle de Voto criado para o ACORDAO.");
		}

		if (!temRelatorio) {
			
			inserirNovoControleVoto(listaJulgamento, oi, materia, listaJulgamento.getMinistro(), 
					TipoTexto.RELATORIO, seqVotoInstancia);
			seqVotoInstancia += 10;
			logger.info("Controle de Voto criado para o RELATORIO.");
		}

		if (!temVoto) {
			
			inserirNovoControleVoto(listaJulgamento, oi, materia, listaJulgamento.getMinistro(), 
					TipoTexto.VOTO, seqVotoInstancia);
			seqVotoInstancia += 10;
			logger.info("Controle de Voto criado para o VOTO.");
		}

		criarVotoVogalVistaComplemento(listaJulgamento, oi, materia);
		
		seqVotoInstancia = 0L;
		
	}

	private void criarVotoVogalVistaComplemento(ListaJulgamento listaJulgamento, ObjetoIncidente<?> oi,
			ConteudoPublicacao materia) throws ServiceException {

		List<Texto> textos = textoService.pesquisar(oi, TipoTexto.VOTO_VOGAL, TipoTexto.VOTO_VISTA,
				TipoTexto.COMPLEMENTO_AO_VOTO);

		for (Texto texto : textos) {
			Ministro ministro = texto.getMinistro();
			
			Boolean temVoto;
			Ministro ministroPresidente = ministroService.recuperarMinistroPresidente(texto.getDataCriacao());
			
			if(Ministro.COD_MINISTRO_PRESIDENTE.equals(ministro.getId()) & ministroPresidente != null) {
				 temVoto = votoJulgamentoProcessoService.temVotoMinistroProcesso(oi, ministroPresidente);
			}else {
				 temVoto = votoJulgamentoProcessoService.temVotoMinistroProcesso(oi, ministro);
			}
			

			if (texto.getTipoFaseTextoDocumento().getCodigoFase() >= FaseTexto.REVISADO.getCodigoFase()) {

				if (listaJulgamento.getMinistro() == texto.getMinistro()) {
					verificarExistenciaSalvarCV(listaJulgamento, oi, materia, texto, ministro);
				}

				if (temVoto) {
					verificarExistenciaSalvarCV(listaJulgamento, oi, materia, texto, ministro);
				}
			}
		}
	}

	private void verificarExistenciaSalvarCV(ListaJulgamento listaJulgamento, ObjetoIncidente<?> oi,
			ConteudoPublicacao materia, Texto texto, Ministro ministro) throws ServiceException {

		ControleVoto cv = controleVotoService.recuperar(oi, texto.getTipoTexto(), texto.getMinistro());

		if (cv == null) {
			inserirNovoControleVoto(listaJulgamento, oi, materia, ministro, texto.getTipoTexto(), seqVotoInstancia);
			seqVotoInstancia += 10;
			logger.info("Controle de Voto criado para o " + texto.getTipoTexto().getDescricao());
		}
	}

	private void inserirNovoControleVoto(ListaJulgamento listaJulgamento, ObjetoIncidente<?> oi, 
			ConteudoPublicacao materia, Ministro ministro,
			TipoTexto tipoTexto, Long seqVotos) throws ServiceException {
		
			ControleVoto cv = new ControleVoto();
			cv.setObjetoIncidente(oi);
			cv.setDataSessao(materia.getDataCriacao());
			cv.setSequenciaVoto(seqVotos);
			cv.setMinistro(ministro);
			cv.setTipoSituacaoTexto(TipoSituacaoTexto.ATIVO_NO_CONTROLE_DE_VOTOS);
			cv.setSessao(obterColegiadoSessao(listaJulgamento));
			cv.setTipoTexto(tipoTexto);
			cv.setSessaoJulgamento(listaJulgamento.getSessao());

			controleVotoService.incluir(cv);
			
	}
	
	public OrigemAndamentoDecisao getOrigemAndamentoDecisao(ListaJulgamento listaJulgamento) {
		OrigemAndamentoDecisao origemDecisao = new OrigemAndamentoDecisao();

		if (listaJulgamento.getIdMinistroPedidoVista().equals(-1L)) {
			if (("F").equals(listaJulgamento.getSessao().getTipoAmbiente())) {
				if (listaJulgamento.getSessao().getColegiado().getId().equals(Colegiado.PRIMEIRA_TURMA))
					origemDecisao.setId(OrigemAndamentoDecisao.ConstanteOrigemDecisao.PRIMEIRA_TURMA.getCodigo());
				if (listaJulgamento.getSessao().getColegiado().getId().equals(Colegiado.SEGUNDA_TURMA))
					origemDecisao.setId(OrigemAndamentoDecisao.ConstanteOrigemDecisao.SEGUNDA_TURMA.getCodigo());
				if (listaJulgamento.getSessao().getColegiado().getId().equals(Colegiado.TRIBUNAL_PLENO))
					origemDecisao.setId(OrigemAndamentoDecisao.ConstanteOrigemDecisao.TRIBUNAL_PLENO.getCodigo());
			}

			if (("V").equals(listaJulgamento.getSessao().getTipoAmbiente())) {
				if (listaJulgamento.getSessao().getColegiado().getId().equals(Colegiado.PRIMEIRA_TURMA))
					origemDecisao.setId(
							OrigemAndamentoDecisao.ConstanteOrigemDecisao.PRIMEIRA_TURMA_SESSAO_VIRTUAL.getCodigo());
				if (listaJulgamento.getSessao().getColegiado().getId().equals(Colegiado.SEGUNDA_TURMA))
					origemDecisao.setId(
							OrigemAndamentoDecisao.ConstanteOrigemDecisao.SEGUNDA_TURMA_SESSAO_VIRTUAL.getCodigo());
				if (listaJulgamento.getSessao().getColegiado().getId().equals(Colegiado.TRIBUNAL_PLENO))
					origemDecisao.setId(
							OrigemAndamentoDecisao.ConstanteOrigemDecisao.TRIBUNAL_PLENO_SESSAO_VIRTUAL.getCodigo());
			}
		} else
			origemDecisao.setId(listaJulgamento.getIdMinistroPedidoVista());

		return origemDecisao;
	}
	
	
	public byte[] montarTextoDecisao(String tipo, String textoDecisao) throws ServiceException {
		byte[] decisaoConteudo = null;
		try {
			decisaoConteudo = BuilderHelper.montarTextoDecisaoJulgamento(tipo, textoDecisao);
		} catch (BadLocationException e) {
			throw new ServiceException(e);
		} catch (IOException e) {
			throw new ServiceException(e);			
		}
		return decisaoConteudo;
	}
	
	public String montarDecisaoListaPublicacaoManual(ListaJulgamento listaJulgamento) throws ServiceException{
		StringBuffer decisaoRetorno = new StringBuffer();
		Map<ArquivoEletronico,List<ObjetoIncidente<?>>> mapaTextos = new HashMap<ArquivoEletronico,List<ObjetoIncidente<?>>>();
		for (ProcessoListaJulgamento plj : listaJulgamento.getListaProcessoListaJulgamento()){
			Texto decisao = plj.getTexto(); // textoService.recuperarTextoSemControleVoto(plj.getObjetoIncidente(), TipoTexto.DECISAO, listaJulgamento.getMinistro());
			List<ObjetoIncidente<?>> lista = null;
			if(decisao != null) {
				if(!mapaTextos.containsKey(decisao.getArquivoEletronico())){
					lista = new ArrayList<ObjetoIncidente<?>>();
					mapaTextos.put(decisao.getArquivoEletronico(), lista);				
				}
				else
					lista = mapaTextos.get(decisao.getArquivoEletronico());
				lista.add(plj.getObjetoIncidente());
			}
		}
		for (ArquivoEletronico ae : mapaTextos.keySet()){
			Iterator<ObjetoIncidente<?>> it =  mapaTextos.get(ae).iterator();
			while(it.hasNext()){
				ObjetoIncidente<?> oi = objetoIncidenteService.recuperarPorId(it.next().getId());
				decisaoRetorno.append(oi.getIdentificacao());
				if (it.hasNext())
					decisaoRetorno.append(", ");
				else
					decisaoRetorno.append(": ");
			}
			decisaoRetorno.append(extrairStringRTF(ae.getConteudo()) + "<br> ");
		}
		
		return (decisaoRetorno.length() > 4000 ? decisaoRetorno.toString().substring(0, 3999) : decisaoRetorno.toString());
	}
	
	private String extrairStringRTF(byte[] bytes) {
		RTFEditorKit rtfe = new RTFEditorKit();
		DefaultStyledDocument doc = new DefaultStyledDocument();

		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			rtfe.read(bais, doc, 0);
			return doc.getText(0, doc.getLength());
		} catch (BadLocationException e) {
			return "";
		} catch (IOException e) {
			return "";
		}
	}

	@Override
	public List<ListaJulgamento> pesquisarListasDeJulgamentoPorDataInicioSessao(ListaJulgamento listaJulgamentoExample,
			Date dataInicio, Date dataFim) {
		return dao.pesquisarListasDeJulgamentoPorDataInicioSessao(listaJulgamentoExample, dataInicio, dataFim);
	}

	@Override
	public byte[] gerarRelatorioExcel(List<ListaJulgamento> dadosRelatorio, String descricaoPesquisa, String caminhoArquivoModelo)
			throws ServiceException {
		try{
			
			InputStream modeloXls = ListaJulgamentoServiceImpl.class.getResource(caminhoArquivoModelo).openStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();			
			HSSFWorkbook workbook = new HSSFWorkbook(modeloXls);			
			
			Sheet sheet = workbook.getSheetAt(0);
			Excel.incluirCabecalhoArquivo(sheet, descricaoPesquisa);
			int numRow = 3;
			for(ListaJulgamento listaJulgamento : dadosRelatorio){
				Excel.incluirDadosLista(sheet, listaJulgamento, numRow++);
			}			
			
			workbook.write(baos);
			workbook.cloneSheet(0);
			return baos.toByteArray();
		}
		catch(IOException e){
			throw new ServiceException(e);
		}			
	}
	
	@Override
	public byte[] gerarRelatorioExcelProcesso(List<ListaJulgamento> dadosRelatorio, String descricaoPesquisa, String caminhoArquivoModelo, Ministro ministro)
			throws ServiceException {
		try{
			
			InputStream modeloXls = ListaJulgamentoServiceImpl.class.getResource(caminhoArquivoModelo).openStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();			
			HSSFWorkbook workbook = new HSSFWorkbook(modeloXls);			
			
			Sheet sheet = workbook.getSheetAt(0);
			
			CellStyle rowStyle = sheet.getWorkbook ().createCellStyle();
			CellStyle linkProcessoStyle = sheet.getWorkbook ().createCellStyle ();
			CellStyle style1 = sheet.getWorkbook ().createCellStyle();
			CellStyle AssuntosStyle = sheet.getWorkbook ().createCellStyle ();
			CellStyle AssuntoPaiStyle = sheet.getWorkbook ().createCellStyle ();
			
						
			Map<String, CellStyle> estilosDasCelulas = new HashMap<String, CellStyle>();
			estilosDasCelulas.put("rowStyle", rowStyle);
			estilosDasCelulas.put("linkProcessoStyle", linkProcessoStyle);
			estilosDasCelulas.put("style1", style1);
			estilosDasCelulas.put("AssuntosStyle", AssuntosStyle);
			estilosDasCelulas.put("AssuntoPaiStyle", AssuntoPaiStyle);
			
			HSSFFont font = (HSSFFont)sheet.getWorkbook().createFont();
			HSSFFont fontCabecalho = (HSSFFont)sheet.getWorkbook().createFont(); 
			
			Font linkProcessoFonte = sheet.getWorkbook ().createFont ();
			
			String[] colunas = {"Colegiado", "Data da Sessão", "Relator","Número da Lista", "Link","Peças","Ementa", "Classe","Número","Incidente/Recurso","Observação","Dispositivo","Rascunho","Ramo Direito Novo","Assuntos"};
			
			Excel.incluirCabecalhoArquivoProcesso(sheet, descricaoPesquisa, colunas, fontCabecalho);
			
			int numRow = 3;			
			
			for(ListaJulgamento listaJulgamento : dadosRelatorio){
				listaJulgamento = recuperarPorId(listaJulgamento.getId());
				List<ProcessoListaJulgamento> plj = new ArrayList<ProcessoListaJulgamento>();
				ArrayList<ProcessoListaJulgamento> listaProcessoListaJulgamento = new ArrayList<ProcessoListaJulgamento>(listaJulgamento.getListaProcessoListaJulgamento());
				plj.addAll(listaProcessoListaJulgamento);
				
				List<ObjetoIncidente<?>> incidentes = new ArrayList<ObjetoIncidente<?>>();
				for(ProcessoListaJulgamento obj : plj) {
					incidentes.add(obj.getObjetoIncidente());
				}
				
				for (int i = 0; i < incidentes.size(); i++) {
					ObjetoIncidente<?> oi = incidentes.get(i);
					VotoJulgamentoProcesso votosMinistro = new VotoJulgamentoProcesso() ;
					if (!listaProcessoListaJulgamento.isEmpty()) {
						ProcessoListaJulgamento processoListaJulgamento = listaProcessoListaJulgamento.get(i);
						if (!listaProcessoListaJulgamento.isEmpty() && processoListaJulgamento.getJulgamentoProcesso() != null) {
						List<VotoJulgamentoProcesso> votos = processoListaJulgamento.getJulgamentoProcesso().getListaVotoJulgamentoRascunho();
							for (int j = 0; j < votos.size(); j++) {
								if (votos.get(j) != null && votos.get(j).getMinistro().getId().equals(ministro.getId()) ) {
									votosMinistro = votos.get(j);
								}
							}
						}
					}
					
					try {
						Processo processo = (Processo) objetoIncidenteService.recuperarPorId(oi.getPrincipal().getId());
						Excel.incluirDadosListaProcesso(sheet, listaJulgamento, numRow++, oi,processo, plj.get(i), votosMinistro, estilosDasCelulas, font, linkProcessoFonte);
					}
						catch(Exception e) {
							logger.error("Erro nao foi possivel encontrar processo (usuário sem acesso a processos classificados SG): "+ oi.getId()+ " Ignorando processo", e);
					}
					
				}
				
			}
			
			for (int i=0; i<colunas.length; i++)
				sheet.autoSizeColumn(i);
			
			workbook.write(baos);
			workbook.cloneSheet(0);
			return baos.toByteArray();
		}
		catch(IOException e){
			throw new ServiceException(e);
		}			
	}	

	private class ResultadoVotacao {
		private Integer quantidadeEnvolvidosSessao = 0;
		private Integer quantidadeVotosProRelator;
		private Integer quantidadeVotosContrariosRelator;
		private Integer quantidadeAbstencoes;
		private Integer quantidadeImpedimentos;
		private Integer quantidadeSuspeitos;
		private Boolean votoMinistroMA;

		public ResultadoVotacao() {
			setQuantidadeEnvolvidosSessao(0);
			setQuantidadeAbstencoes(0);
			setQuantidadeImpedimentos(0);
			setQuantidadeSuspeitos(0);
			setQuantidadeVotosContrariosRelator(0);
			setQuantidadeVotosProRelator(0);
			setVotoMinistroMA(Boolean.FALSE);

		}

		public Integer getQuantidadeEnvolvidosSessao() {
			return quantidadeEnvolvidosSessao;
		}

		public void setQuantidadeEnvolvidosSessao(Integer quantidadeEnvolvidosSessao) {
			this.quantidadeEnvolvidosSessao = quantidadeEnvolvidosSessao;
		}

		public Integer getQuantidadeVotosProRelator() {
			return quantidadeVotosProRelator;
		}

		public void setQuantidadeVotosProRelator(Integer quantidadeVotosProRelator) {
			this.quantidadeVotosProRelator = quantidadeVotosProRelator;
		}

		public Integer getQuantidadeVotosContrariosRelator() {
			return quantidadeVotosContrariosRelator;
		}

		public void setQuantidadeVotosContrariosRelator(Integer quantidadeVotosContrariosRelator) {
			this.quantidadeVotosContrariosRelator = quantidadeVotosContrariosRelator;
		}

		public Integer getQuantidadeAbstencoes() {
			return quantidadeAbstencoes;
		}

		public void setQuantidadeAbstencoes(Integer quantidadeAbstencoes) {
			this.quantidadeAbstencoes = quantidadeAbstencoes;
		}

		public Integer getQuantidadeImpedimentos() {
			return quantidadeImpedimentos;
		}

		public void setQuantidadeImpedimentos(Integer quantidadeImpedimentos) {
			this.quantidadeImpedimentos = quantidadeImpedimentos;
		}

		public Integer getQuantidadeSuspeitos() {
			return quantidadeSuspeitos;
		}

		public void setQuantidadeSuspeitos(Integer quantidadeSuspeitos) {
			this.quantidadeSuspeitos = quantidadeSuspeitos;
		}

		public Boolean getVotoMinistroMA() {
			return votoMinistroMA;
		}

		public void setVotoMinistroMA(Boolean votoMinistroMA) {
			this.votoMinistroMA = votoMinistroMA;
		}
	}

	private class VotoMinistro implements Comparable<VotoMinistro> {
		private String ministro;
		private String tipoVoto;

		public VotoMinistro(Ministro ministro, TipoVoto tipoVoto) {
			if (tipoVoto.getId().equals(TipoVoto.TipoVotoConstante.ACOMPANHO_DIVERGENCIA.getCodigo())
					|| tipoVoto.getId().equals(TipoVoto.TipoVotoConstante.DIVERGENTE.getCodigo()))
				setMinistro(ministro.getNome());
			setTipoVoto(tipoVoto.getId());

		}

		public String getMinistro() {
			return ministro;
		}

		public void setMinistro(String ministro) {
			this.ministro = ministro;
		}

		public String getTipoVoto() {
			return tipoVoto;
		}

		public void setTipoVoto(String tipoVoto) {
			this.tipoVoto = tipoVoto;
		}

		public int compareTo(VotoMinistro o) {
			if (o.getMinistro() != null) {
				if (getMinistro() == null) {
					return -1;
				} else {
					return o.getMinistro().compareTo(o.getMinistro());
				}
			} else {
				if (getMinistro() != null) {
					return 1;
				} else {
					return o.getTipoVoto().compareTo(getTipoVoto());
				}
			}
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof VotoMinistro) {
				if (null == ((VotoMinistro) obj).getMinistro()) {
					if (getMinistro() != null) {
						return false;
					} else {
						if (!((VotoMinistro) obj).getTipoVoto().equals(getTipoVoto()))
							return false;
						else
							return true;
					}
				}

				if (getMinistro() == null) {
					if (((VotoMinistro) obj).getMinistro() != null) {
						return false;
					} else {
						if (!((VotoMinistro) obj).getTipoVoto().equals(getTipoVoto()))
							return false;
						else
							return true;
					}
				}
				if (!((VotoMinistro) obj).getMinistro().equals(getMinistro())
						|| !((VotoMinistro) obj).getTipoVoto().equals(getTipoVoto()))
					return false;

			} else {
				return false;
			}
			return true;
		}

	}

	@Override
	public Long obterQuantidadeRascunhosPorMinistro(ListaJulgamento lista, Ministro ministro) throws ServiceException {
		try {
			return dao.obterQuantidadeRascunhosPorMinistro(lista, ministro);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}