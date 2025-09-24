package br.jus.stf.estf.decisao.texto.web;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.eprocesso.servidorpdf.servico.modelo.ExtensaoEnum;
import br.gov.stf.estf.converter.DocumentConversionException;
import br.gov.stf.estf.converter.DocumentConverterService;
import br.gov.stf.estf.converter.DocumentSource;
import br.gov.stf.estf.converter.source.ByteArrayDocumentSource;
import br.gov.stf.estf.converter.target.ByteArrayDocumentTarget;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.publicacao.AtaJulgamento;
import br.gov.stf.estf.entidade.publicacao.AtaJulgamento.CategoriaAta;
import br.gov.stf.estf.publicacao.model.service.AtaJulgamentoService;
import br.jus.stf.estf.decisao.pesquisa.domain.AllResourcesDto;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionInterface;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.support.util.FormatoArquivo;
import br.jus.stf.estf.decisao.support.util.NestedRuntimeException;
import br.jus.stf.estf.decisao.support.util.ReportUtils;

/**
 * Consulta a Ata de Julgamento dadas a data de julgamento, a categoria e o colegiado.
 * 
 * @author Rodrigo Barreiros
 * @see 21.07.2010
 */
@Action(id="consultaAtaJulgamentoActionFacesBean", name="Consultar Ata de Julgamento", view="/acoes/objetoincidente/consultarAtaJulgamento.xhtml", height=250, width=450)
@Restrict({ActionIdentification.CONSULTAR_ATA_DE_JULGAMENTO})
public class ConsultaAtaJulgamentoActionFacesBean extends ActionSupport<AllResourcesDto> implements ActionInterface<AllResourcesDto> {
	
	@Autowired
	private AtaJulgamentoService ataJulgamentoService;
	
	@Autowired
	private DocumentConverterService converterService;
	
	private Date dataSessao = new Date();
	private String categoriaAta;
	private int colegiado;
	private String texto;
	private String formatoArquivo;
	
	/**
	 * Executa a consulta com os critérios informados.
	 */
	public void execute() {
		try {
			AtaJulgamento ataJulgamento = ataJulgamentoService.recuperarPor(dataSessao, colegiado, CategoriaAta.valueOf(categoriaAta));
			
			if (ataJulgamento == null) {
				getFacesMessages().add("Não foi encontrada nenhuma ata de julgamento para os critérios informados.");
				return;
			}
			
			ArquivoEletronico ae = ataJulgamento.getArquivoEletronico();
			
			if (getFormatoArquivo().equals(FormatoArquivo.RTF.getNome())) {
				
				ReportUtils.report(new ByteArrayInputStream(ae.getConteudo()), FormatoArquivo.RTF);
				
			} else if (getFormatoArquivo().equals(FormatoArquivo.PDF.getNome())){
				byte pdf[] = convertRtfToPdf(ae.getConteudo());
				
				ReportUtils.report(new ByteArrayInputStream(pdf), FormatoArquivo.PDF);
			}
						
		} catch (DocumentConversionException e) {
			throw new NestedRuntimeException(e);
		}
	}
	
	private byte[] convertRtfToPdf(byte[] rtf) throws DocumentConversionException {
		DocumentSource source = new ByteArrayDocumentSource(rtf);
		ByteArrayDocumentTarget target = new ByteArrayDocumentTarget();
		converterService.convertDocument(source, ExtensaoEnum.RTF.getContentType(), target, FormatoArquivo.PDF.getMimeType());
		return target.getByteArray();
	}

	/**
	 * Retorna a lista de possíveis colegiados.
	 * 
	 * @return a lista de colegiados
	 */
	public List<SelectItem> getColegiados() {
		List<SelectItem> itens =  new ArrayList<SelectItem>();
		itens.add(new SelectItem(AtaJulgamento.PRIMEIRA_TURMA, "Primeira Turma"));
		itens.add(new SelectItem(AtaJulgamento.SEGUNDA_TURMA, "Segunda Turma"));
		itens.add(new SelectItem(AtaJulgamento.PLENO, "Pleno"));
		return itens;
	}
	
	/**
	 * Retorna a lista de possíveis categorias.
	 * 
	 * @return a lista de categorias
	 */
	public List<SelectItem> getCategoriasAta() {
		List<SelectItem> itens =  new ArrayList<SelectItem>();
		itens.add(new SelectItem(AtaJulgamento.CategoriaAta.ATA.toString(), "Ata"));
		itens.add(new SelectItem(AtaJulgamento.CategoriaAta.INDICE.toString(), "Índice"));
		itens.add(new SelectItem(AtaJulgamento.CategoriaAta.PAUTA.toString(), "Pauta"));
		itens.add(new SelectItem(AtaJulgamento.CategoriaAta.ATAORDINARIA.toString(), "Ata Ordinária"));
		itens.add(new SelectItem(AtaJulgamento.CategoriaAta.ATAEXTRAORDINARIA.toString(), "Ata Extraordinária"));
		itens.add(new SelectItem(AtaJulgamento.CategoriaAta.SESSAOVIRTUAL.toString(), "Sessão Virtual"));
		
		return itens;
	}
	
	/**
	 * Retorna a lista com os possíveis tipos de arquivo.
	 * 
	 * @return a lista de tipos de arquivo
	 */
	public List<SelectItem> getFormatosArquivo() {
		List<SelectItem> itens =  new ArrayList<SelectItem>();
		itens.add(new SelectItem(FormatoArquivo.PDF.getNome(), FormatoArquivo.PDF.getNome()));
		itens.add(new SelectItem(FormatoArquivo.RTF.getNome(), FormatoArquivo.RTF.getNome()));
		return itens;
	}
	
	public void setCategoriaAta(String codigoTipoAta) {
		this.categoriaAta = codigoTipoAta;
	}
	
	public String getCategoriaAta() {
		return categoriaAta;
	}
	
	public void setColegiado(int colegiado) {
		this.colegiado = colegiado;
	}
	
	public int getColegiado() {
		return colegiado;
	}
	
	public void setDataSessao(Date dataSessao) {
		this.dataSessao = dataSessao;
	}
	
	public Date getDataSessao() {
		return dataSessao;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public String getTexto() {
		return texto;
	}

	public String getFormatoArquivo() {
		return formatoArquivo;
	}

	public void setFormatoArquivo(String formatoArquivo) {
		this.formatoArquivo = formatoArquivo;
	}

}
