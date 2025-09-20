package br.gov.stf.estf.documento.model.util;

import java.util.Date;

import br.gov.stf.estf.entidade.documento.TipoSituacaoTexto;
import br.gov.stf.estf.entidade.documento.TipoTexto;

public interface IConsultaDeControleDeVotoInteiroTeor {

	Long getId();

	Long getIdObjetoIncidente();

	boolean isPesquisarPecaProcessual();

	String getInteiroTeor();

	Date getDataGeracao();
	
	Date getDataPublicacao();

	Integer getAnoMateria();

	Integer getNumeroMateria();

	String getSiglaClasseProcessual();

	Long getNumeroProcesso();

	Long getCodigoRecurso();

	Long getTipoJulgamento();

	String getTipoMeioProcesso();

	String getSessao();

	TipoTexto getTipoTexto();

	Date getDataSessao();
	
	TipoSituacaoTexto getTipoSituacaoTexto();
	
	String getTipoColecao();
	
	String getStatusRevisao();
}