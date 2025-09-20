package br.gov.stf.estf.assinatura.relatorio.dataaccess;

import java.util.List;

import br.gov.stf.estf.assinatura.relatorio.RelatorioAcordaoPublicado;
import br.gov.stf.estf.assinatura.relatorio.RelatorioAutosEmprestadosAdvogados;
import br.gov.stf.estf.assinatura.relatorio.RelatorioAutosEmprestadosOrgaosExternos;
import br.gov.stf.estf.assinatura.relatorio.RelatorioGuiaDeslocamentoPeticao;
import br.gov.stf.estf.assinatura.relatorio.RelatorioGuiaDeslocamentoProcesso;
import br.gov.stf.estf.assinatura.relatorio.RelatorioProcessoInteresse;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.framework.model.dataaccess.DaoException;

public interface ProcessamentoRelatorioDao {

	public abstract List<RelatorioGuiaDeslocamentoProcesso> consultarRelatorioGuiaDeslocamentoProcesso(
			Long numeroDaGuia, Short anoDaGuia, Long codigoOrgaoOrigem) throws DaoException;

	public abstract List<RelatorioGuiaDeslocamentoPeticao> consultarRelatorioGuiaDeslocamentoPeticao(List<Guia> guias) throws DaoException;
	
	public abstract List<RelatorioAutosEmprestadosAdvogados> consultarRelatorioAutosEmprestadosAdvogados(String siglaClasseProcesso,
			String dataInicial, String dataFinal) throws DaoException;
	
	public abstract List<RelatorioAutosEmprestadosOrgaosExternos> consultarRelatorioAutosEmprestadosOrgaosExternos(String siglaClasseProcesso,
			String dataInicial, String dataFinal, Long codigoOrgaoExterno) throws DaoException;
	
	public abstract List<RelatorioAcordaoPublicado> consultarRelatorioAcordaoPublicado(String dataPublicacao, String codigoSetorPubAcordao, String deslocadoParaAcordao) throws DaoException;
	
	public abstract List<RelatorioGuiaDeslocamentoProcesso> consultarRelatorioGuiaRetiradaAutosProcesso(
			Long numeroDaGuia, Short anoDaGuia, Long codigoOrgaoOrigem) throws DaoException;

	public abstract List<RelatorioGuiaDeslocamentoProcesso> consultarRelatorioGuiaDevolucaoAutosProcesso(List<Guia> guias) throws DaoException;

	public abstract List<RelatorioProcessoInteresse> consultarRelatorioProcessoInteresse(
			Long seqJurisdicionado) throws DaoException;
	
	public abstract List<RelatorioGuiaDeslocamentoProcesso> consultarRelatorioGuiaAntigaRetiradaAutosProcesso(
			Long numeroDaGuia, Short anoDaGuia, Long codigoOrgaoOrigem) throws DaoException;

	public abstract List<RelatorioGuiaDeslocamentoProcesso> consultarRelatorioGuiaAntigaDevolucaoAutosProcesso(List<Guia> guias) throws DaoException;
	
	public List<RelatorioGuiaDeslocamentoProcesso> consultarRelatorioVariasGuiasDeslocamentoProcesso(List<Guia> guias) throws DaoException;
	
}