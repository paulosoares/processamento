package br.gov.stf.estf.processosetor.model.util;

import java.util.Date;

import br.gov.stf.estf.entidade.processosetor.PeticaoSetor;
import br.gov.stf.estf.model.util.ESTFSearchData;
import br.gov.stf.estf.model.util.TipoOrdem;
import br.gov.stf.estf.model.util.TipoOrdemProcesso;

/**
 * Objeto que encapsula os diversos parâmetros que podem ser utilizados na consulta de {@link PeticaoSetor}.
 * 
 * @author thiago.miranda
 * @since 3.83.0
 */
public class PeticaoSetorSearchData extends ESTFSearchData {

	private static final long serialVersionUID = -6867663144862915481L;

	public Long numeroPeticao;
	public Short anoPeticao;
	public Long idSetor;
	public String siglaClasseProcessual;
	public Long numeroProcesso;
	public Short codigoRecurso;
	public Date dataEntradaInicial;
	public Date dataEntradaFinal;
	public Date dataRemessaInicial;
	public Date dataRemessaFinal;
	public Date dataRecebimentoInicial;
	public Date dataRecebimentoFinal;
	public Boolean juntado;
	public Boolean tratado;
	public Boolean vinculadoProcesso;
	public Boolean semLocalizacao;
	public String numeroSala;
	public String numeroArmario;
	public String numeroEstante;
	public String numeroPrateleira;
	public String numeroColuna;
	public Boolean deslocamentoPeticao;
	public Long idSecaoUltimoDeslocamento;
	public Boolean localizadoNoSetor;
	public String tipoMeioProcesso;
	public TipoOrdemProcesso tipoOrdemProcesso;
	public Boolean peticoesSemDeslocamento;
	
	/*
	 * Construtor utilizado apenas para facilitar a transição de métodos que utilizam todos os parâmetros para utilizarem apenas um objeto PeticaoSetorSearchData.
	 * Deve ser descartado no futuro.
	 * 16/12/2011
	 */
	public PeticaoSetorSearchData(Boolean limitarPesquisa, Boolean readOnlyQuery, TipoOrdem tipoOrdem, Long numeroPeticao, Short anoPeticao, Long idSetor,
			String siglaClasseProcessual, Long numeroProcesso, Short codigoRecurso, Date dataEntradaInicial, Date dataEntradaFinal, Date dataRemessaInicial, Date dataRemessaFinal,
			Date dataRecebimentoInicial, Date dataRecebimentoFinal, Boolean juntado, Boolean tratado, Boolean vinculadoProcesso, Boolean semLocalizacao, String numeroSala,
			String numeroArmario, String numeroEstante, String numeroPrateleira, String numeroColuna, Boolean deslocamentoPeticao, Long idSecaoUltimoDeslocamento,
			Boolean localizadoNoSetor, String tipoMeioProcesso, TipoOrdemProcesso tipoOrdemProcesso, Boolean peticoesSemDeslocamento) {
		super(limitarPesquisa, readOnlyQuery, tipoOrdem);
		this.numeroPeticao = numeroPeticao;
		this.anoPeticao = anoPeticao;
		this.idSetor = idSetor;
		this.siglaClasseProcessual = siglaClasseProcessual;
		this.numeroProcesso = numeroProcesso;
		this.codigoRecurso = codigoRecurso;
		this.dataEntradaInicial = dataEntradaInicial;
		this.dataEntradaFinal = dataEntradaFinal;
		this.dataRemessaInicial = dataRemessaInicial;
		this.dataRemessaFinal = dataRemessaFinal;
		this.dataRecebimentoInicial = dataRecebimentoInicial;
		this.dataRecebimentoFinal = dataRecebimentoFinal;
		this.juntado = juntado;
		this.tratado = tratado;
		this.vinculadoProcesso = vinculadoProcesso;
		this.semLocalizacao = semLocalizacao;
		this.numeroSala = numeroSala;
		this.numeroArmario = numeroArmario;
		this.numeroEstante = numeroEstante;
		this.numeroPrateleira = numeroPrateleira;
		this.numeroColuna = numeroColuna;
		this.deslocamentoPeticao = deslocamentoPeticao;
		this.idSecaoUltimoDeslocamento = idSecaoUltimoDeslocamento;
		this.localizadoNoSetor = localizadoNoSetor;
		this.tipoMeioProcesso = tipoMeioProcesso;
		this.tipoOrdemProcesso = tipoOrdemProcesso;
		this.peticoesSemDeslocamento = peticoesSemDeslocamento;
	}
}
