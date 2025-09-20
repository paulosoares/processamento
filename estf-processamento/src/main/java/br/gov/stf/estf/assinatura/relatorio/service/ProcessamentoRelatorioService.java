package br.gov.stf.estf.assinatura.relatorio.service;

import java.io.IOException;
import java.util.List;

import br.gov.stf.estf.assinatura.relatorio.RelatorioAcordaoPublicado;
import br.gov.stf.estf.assinatura.relatorio.RelatorioAutosEmprestadosAdvogados;
import br.gov.stf.estf.assinatura.relatorio.RelatorioAutosEmprestadosOrgaosExternos;
import br.gov.stf.estf.assinatura.relatorio.RelatorioGerirCargaAutos;
import br.gov.stf.estf.assinatura.relatorio.RelatorioProcessoInteresse;
import br.gov.stf.estf.assinatura.visao.util.InfoPecaVinculadoAndamentoDTO;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.expedicao.model.util.CertidaoTransitoDTO;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.stfoffice.handler.HandlerException;

public interface ProcessamentoRelatorioService {

	public abstract byte[] criarRelatorioGuiaDeslocamentoProcesso(Long numeroDaGuia, Short anoDaGuia,
			Long codigoOrgaoOrigem, boolean postal) throws ServiceException;
	public abstract byte[] criarRelatorioVariasGuiasDeslocamentoProcesso(List<Guia> guias) throws ServiceException;
	public abstract byte[] criarRelatorioGuiaDeslocamentoPeticao(List<Guia> guias) throws ServiceException;
	
	public abstract byte[] criarRelatorioAutosEmprestadosAdvogados(String siglaClasseProcesso, String dataInicial,
			String dataFinal, List<RelatorioAutosEmprestadosAdvogados> listaAutos) throws ServiceException;
	public abstract byte[] criarRelatorioAutosEmprestadosOrgaosExternos(String siglaClasseProcesso, String dataInicial,
			String dataFinal, Long codigoOrgaoDestino, List<RelatorioAutosEmprestadosOrgaosExternos> listaAutos) throws ServiceException;

	public abstract List<RelatorioAutosEmprestadosAdvogados> recuperarAutosEmprestadosAdvogados(String siglaClasseProcesso, 
			String dataInicial,	String dataFinal) throws ServiceException;
	public abstract List<RelatorioAutosEmprestadosOrgaosExternos> recuperarAutosEmprestadosOrgaosExternos(String siglaClasseProcesso, 
			String dataInicial, String dataFinal, Long codigoOrgaoDestino) throws ServiceException;
	
	public abstract List<RelatorioAcordaoPublicado> recuperarAcordaoPublicado(String dataPublicacao, String codigoSetorPubAcordao, String deslocadoParaAcordao) throws ServiceException;
	public abstract byte[] criarRelatorioAcordaoPublicado(String dataPublicacao, 
			List<RelatorioAcordaoPublicado> listaAcordaosPublicados, String codigoSetorPubAcordao, String deslocadoParaAcordao) throws ServiceException;
	
	public abstract byte[] criarRelatorioGerirAutos(List<RelatorioGerirCargaAutos> listaAutos) throws ServiceException;

	public abstract byte[] criarRelatorioGuiaRetiradaAutosProcesso(Long numeroDaGuia, Short anoDaGuia,
			Long codigoOrgaoOrigem, boolean postal) throws ServiceException;

	public abstract byte[] criarRelatorioGuiaDevolucaoAutosProcesso(List<Guia> guias) throws ServiceException;

	public abstract byte[] criarRelatorioProcessoInteresse(Long seqJurisdicionado, List<RelatorioProcessoInteresse> dadosRelatorio) throws ServiceException;
	public abstract byte[] criarRelatorioProcessoInteresseSemMovimentada(Long seqJurisdicionado, List<RelatorioProcessoInteresse> dados) throws ServiceException;
	public abstract byte[] criarRelatorioGuiaAntigaDevolucaoAutosProcesso(List<Guia> guias) throws ServiceException;
    public String criarCertidaoBaixaProcessoEletronico(String idProcesso, String titulo,
            String corpo, String nomeSecretario, String descCargo, ObjetoIncidente<?> objetoIncidente,
            Setor setor, AndamentoProcesso andamentoProcesso, Usuario usuarioLogado) throws ServiceException, IOException;

	public String criarRelatorioGuiaDeslocamentoProcessoNaPastaTemp(Long numeroDaGuia, Short anoDaGuia,
			Long codigoOrgaoOrigem, boolean postal) throws ServiceException, IOException;
	public String criarRelatorioGuiaDeslocamentoPeticaoNaPastaTemp(List<Guia> guias) throws ServiceException, IOException;
	public String criarRelatorioGuiaAntigaDevolucaoProcessosNaPastaTemp(List<Guia> guias) throws ServiceException, IOException;
	public String criarRelatorioGuiaDevolucaoProcessosNaPastaTemp(List<Guia> guias) throws ServiceException, IOException;
	public String criarRelatorioGuiaRecebimentoProcessosNaPastaTemp(List<Guia> guias) throws ServiceException, IOException;
	public String criarRelatorioGuiaRecebimentoPeticoesNaPastaTemp(List<Guia> guias) throws ServiceException, IOException;
	public byte[] criarCertidaoTransito(CertidaoTransitoDTO certidao)	throws ServiceException;

	public String recuperarPathImagens(String uriImagem);
	public String gerarPDF(String nomepdf, byte[] arrayByteRel) throws IOException;
	ComunicacaoDocumentoResult gerarPecaVinculadaAoAndamentoSelecionado(InfoPecaVinculadoAndamentoDTO infoPecaVinculadoAndamentoDTO) throws ServiceException, HandlerException;
	public byte[] gerarPdfComunicacaoDeVista(ObjetoIncidente<?> oi, Setor setor, String destinatario ) throws ServiceException;
	public byte[] gerarPdfComunicacaoAutosDisp(ObjetoIncidente<?> oi, Setor setor, String destinatario ) throws ServiceException;
}