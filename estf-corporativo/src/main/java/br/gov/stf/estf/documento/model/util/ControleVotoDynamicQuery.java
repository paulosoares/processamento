package br.gov.stf.estf.documento.model.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoPecaProcesso;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.documento.TipoSituacaoPeca;
import br.gov.stf.estf.entidade.documento.TipoSituacaoTexto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.publicacao.ProcessoPublicado;
import br.gov.stf.estf.processostf.model.util.ObjetoIncidenteDynamicQuery;
import br.gov.stf.framework.model.util.query.DynamicQuery;
import br.gov.stf.framework.model.util.query.SQLCondition;
import br.gov.stf.framework.model.util.query.SQLConnective;

public class ControleVotoDynamicQuery extends DynamicQuery {

	public static final String ALIAS_CONTROLE_VOTO = "oEntidade";
	public static final String ALIAS_PECA_PROCESSO_ELETRONICO = "ppe";
	public static final String ALIAS_PROCESSO_PUBLICADO = "pp";
	public static final String ALIAS_DOCUMENTO_TEXTO = "dt";
	public static final String ALIAS_DESLOCA_PROCESSO = "dp";
	public static final String ALIAS_TEXTO = "t";
	private boolean pesquisarObjetosDoProcesso = false;
	private boolean filtrarPeloSetorComposicaoAcordao = true;

	private final List<Long> situacoesPermitidasDeDocumento = new ArrayList<Long>(Arrays.asList(new Long[] {
			TipoSituacaoDocumento.ASSINADO_DIGITALMENTE.getCodigo(), TipoSituacaoDocumento.ASSINADO_MANUALMENTE.getCodigo() }));

	private ObjetoIncidenteDynamicQuery consultaObjetoIncidente;

	private ObjetoIncidenteDynamicQuery getConsultaObjetoIncidente() {
		if (consultaObjetoIncidente == null) {
			consultaObjetoIncidente = new ObjetoIncidenteDynamicQuery(ALIAS_CONTROLE_VOTO, pesquisarObjetosDoProcesso);
			adicionaConsultaAninhada(consultaObjetoIncidente);
		}
		return consultaObjetoIncidente;
	}

	public void setInteiroTeor(String inteiroTeor) {
		if (isStringInformado(inteiroTeor)) {
			registraJoinPecaProcessoEletronico();
			if (inteiroTeor.equalsIgnoreCase("C")) {
				insereCondicao(ALIAS_PECA_PROCESSO_ELETRONICO + ".tipoPecaProcesso", TipoPecaProcesso.CODIGO_INTEIRO_TEOR);
				insereCondicao(ALIAS_PECA_PROCESSO_ELETRONICO + ".tipoSituacaoPeca", TipoSituacaoPeca.EXCLUIDA.getCodigo(),
						SQLCondition.DIFERENTE);
			} else if (inteiroTeor.equalsIgnoreCase("S")) {
				insereCondicaoTextual(" ((" + ALIAS_PECA_PROCESSO_ELETRONICO + ".tipoPecaProcesso <> ?) or ("
						+ ALIAS_PECA_PROCESSO_ELETRONICO + ".tipoPecaProcesso = ? and " + ALIAS_PECA_PROCESSO_ELETRONICO
						+ ".tipoSituacaoPeca = ?)) ", SQLConnective.AND, TipoPecaProcesso.CODIGO_INTEIRO_TEOR,
						TipoPecaProcesso.CODIGO_INTEIRO_TEOR, TipoSituacaoPeca.EXCLUIDA.getCodigo());
			}
		}
	}

	private void registraJoinPecaProcessoEletronico() {
		registraJoin(ALIAS_PECA_PROCESSO_ELETRONICO, PecaProcessoEletronico.class, ALIAS_CONTROLE_VOTO + ".objetoIncidente = "
				+ ALIAS_PECA_PROCESSO_ELETRONICO + ".objetoIncidente");
	}

	private boolean isStringInformado(String inteiroTeor) {
		return inteiroTeor != null && !inteiroTeor.trim().equals("");
	}

	public void setNumeroMateria(Integer numeroMateria) {
		if (isIntegerInformado(numeroMateria)) {
			registraJoinProcessoPublicado();
			insereCondicao(ALIAS_PROCESSO_PUBLICADO + ".numeroMateria", numeroMateria);
		}
	}

	private void registraJoinProcessoPublicado() {
		registraJoin(ALIAS_PROCESSO_PUBLICADO, ProcessoPublicado.class, ALIAS_CONTROLE_VOTO + ".objetoIncidente="
				+ ALIAS_PROCESSO_PUBLICADO + ".objetoIncidente");
	}

	public void setAnoMateria(Integer anoMateria) {
		if (isIntegerInformado(anoMateria)) {
			registraJoinProcessoPublicado();
			insereCondicao(ALIAS_PROCESSO_PUBLICADO + ".anoMateria", anoMateria);
		}
	}

	private boolean isIntegerInformado(Integer anoMateria) {
		return anoMateria != null && anoMateria.intValue() > 0;
	}

	public void setNumeroProcesso(Long numeroProcesso) {
		if (numeroProcesso != null) {
			getConsultaObjetoIncidente().setNumeroProcesso(numeroProcesso);
		}
	}

	public void setSiglaClasseProcessual(String sigla) {
		if (sigla != null) {
			getConsultaObjetoIncidente().setSiglaClasseProcessual(sigla);
		}
	}

	public void setCodigoRecurso(Long codigoRecurso) {
		if (codigoRecurso != null) {
			getConsultaObjetoIncidente().setCodigoRecurso(codigoRecurso);
		}
	}

	public void setTipoJulgamento(Long tipoJulgamento) {
		if (tipoJulgamento != null) {
			getConsultaObjetoIncidente().setTipoJulgamento(tipoJulgamento);
		}
	}

	public void setTipoMeioProcesso(String tipoMeioProcesso) {
		if (tipoMeioProcesso != null) {
			getConsultaObjetoIncidente().setTipoMeioProcesso(tipoMeioProcesso);
		}
	}

	public ControleVotoDynamicQuery(boolean pesquisarObjetosDoProcesso, boolean filtrarPeloSetorComposicaoAcordao) {
		this.pesquisarObjetosDoProcesso = pesquisarObjetosDoProcesso;
		this.filtrarPeloSetorComposicaoAcordao = filtrarPeloSetorComposicaoAcordao;
		init();
	}		
	
	public ControleVotoDynamicQuery(boolean pesquisarObjetosDoProcesso) {
		this.pesquisarObjetosDoProcesso = pesquisarObjetosDoProcesso;
		init();
	}
	
	public ControleVotoDynamicQuery() {
		init();
	}

	private void init() {
		registraJoinDocumentoTexto();
		insereCondicao(ALIAS_CONTROLE_VOTO + ".tipoSituacaoTexto", TipoSituacaoTexto.CANCELADO.getCodigo(),
				SQLCondition.DIFERENTE);
		insereCondicaoTextual(ALIAS_CONTROLE_VOTO + ".dataPublico is not null", SQLConnective.AND);
		
		if (filtrarPeloSetorComposicaoAcordao) {
			insereCondicaoDeDeslocamento();
		}
		
		insereCondicaoIn(ALIAS_DOCUMENTO_TEXTO + ".tipoSituacaoDocumento", situacoesPermitidasDeDocumento.iterator(),
				SQLConnective.AND);		
	}
	
	private void insereCondicaoDeDeslocamento() {
		registraJoinDeslocaProcesso();
		insereCondicao(ALIAS_DESLOCA_PROCESSO + ".codigoOrgaoDestino", Setor.CODIGO_SETOR_COMPOSICAO_ACORDAOS);
		insereCondicao(ALIAS_DESLOCA_PROCESSO + ".ultimoDeslocamento", "S");
	}

	private void registraJoinDeslocaProcesso() {
		registraJoin(ALIAS_DESLOCA_PROCESSO, DeslocaProcesso.class, ALIAS_CONTROLE_VOTO + ".objetoIncidente.principal.id="
				+ ALIAS_DESLOCA_PROCESSO + ".id.processo.id");
	}

	protected void registraJoinDocumentoTexto() {
		// TODO Enquanto a SEQ_TEXTOS do Controle de Votos não for confiável,
		// será preciso fazer um join explícito com Texto antes de fazer a
		// consulta. Quando isso não for mais problema, a seguinte forma
		// poderá ser utilizada:
		// insereCondicaoTextual(ALIAS_CONTROLE_VOTO + ".texto=" +
		// ALIAS_DOCUMENTO_TEXTO + ".texto", SQLConnective.AND);
		registraJoinControleVotoTexto();
		registraJoin(ALIAS_DOCUMENTO_TEXTO, DocumentoTexto.class);
		insereCondicaoTextual(ALIAS_TEXTO + ".id=" + ALIAS_DOCUMENTO_TEXTO + ".texto.id", SQLConnective.AND);
	}

	private void registraJoinControleVotoTexto() {
		// TODO Esse modo só poderá ser utilizado quando a seq_texto do Controle
		// de Voto for confiável,
		// conforme orientação dada pelo Paulo em 10/08/2009. Por enquanto, o
		// Join deve ser feito pela chave composta (Objeto Incidente, TipoTexto,
		// Ministro).
		if (!getJoinsUtilizados().containsKey(ALIAS_TEXTO)) {
			registraJoin(ALIAS_TEXTO, Texto.class);
			insereCondicaoTextual(ALIAS_TEXTO + ".objetoIncidente=" + ALIAS_CONTROLE_VOTO + ".objetoIncidente", SQLConnective.AND);
			insereCondicaoTextual(ALIAS_TEXTO + ".ministro=" + ALIAS_CONTROLE_VOTO + ".ministro", SQLConnective.AND);
			insereCondicaoTextual(ALIAS_TEXTO + ".tipoTexto=" + ALIAS_CONTROLE_VOTO + ".tipoTexto", SQLConnective.AND);
			insereCondicaoTextual(ALIAS_TEXTO + ".sequenciaVoto=" + ALIAS_CONTROLE_VOTO + ".sequenciaVoto", SQLConnective.AND);
			insereCondicaoTextual(ALIAS_TEXTO + ".dataSessao=" + ALIAS_CONTROLE_VOTO + ".dataSessao", SQLConnective.AND);
		}
	}

	public void setDataGeracao(Date dataGeracao) {
		if (dataGeracao != null) {
			insereCondicaoTextual("to_date(" + ALIAS_PECA_PROCESSO_ELETRONICO
					+ ".dataInclusao,'dd/MM/yyyy') = to_date(?,'dd/MM/yyyy')", SQLConnective.AND, dataGeracao);
			registraJoinPecaProcessoEletronico();
		}
	}

	public void setSessao(String sessao) {
		if (isStringInformado(sessao)) {
			insereCondicao(ALIAS_CONTROLE_VOTO + ".sessao", sessao);
		}
	}

	public void setTipoSituacaoTexto(TipoSituacaoTexto tipoSituacaoTexto) {
		if (tipoSituacaoTexto != null) {
			insereCondicao(ALIAS_CONTROLE_VOTO + ".tipoSituacaoTexto", tipoSituacaoTexto.getCodigo());
		}
	}

	public void setTipoTexto(TipoTexto tipoTexto) {
		if (tipoTexto != null) {
			insereCondicao(ALIAS_CONTROLE_VOTO + ".texto.tipoTexto", tipoTexto.getCodigo());
		}
	}

	public void setDataSessao(Date dataSessao) {
		if (dataSessao != null) {
			insereCondicao(ALIAS_CONTROLE_VOTO + ".dataSessao", dataSessao);
		}
	}

	public void setId(Long id) {
		if (id != null) {
			insereCondicao(ALIAS_CONTROLE_VOTO + ".id", id);
		}
	}

	public void setIdObjetoIncidente(Long idObjetoIncidente) {
		if (idObjetoIncidente != null) {
			insereCondicao(ALIAS_CONTROLE_VOTO + ".objetoIncidente.id", idObjetoIncidente);
		}
	}

}
