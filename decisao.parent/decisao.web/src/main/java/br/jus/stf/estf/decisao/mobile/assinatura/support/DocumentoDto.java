package br.jus.stf.estf.decisao.mobile.assinatura.support;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import br.jus.stf.estf.decisao.documento.support.Documento;
import br.jus.stf.estf.decisao.documento.support.DocumentoNaoAssinadoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ComunicacaoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;

public class DocumentoDto<T extends Documento> implements Serializable {

	private static final long serialVersionUID = 9041246574666899549L;

	public static final String TIPO_TEXTO = "texto";
	public static final String TIPO_COMUNICACAO = "comunicacao";
	
	private final static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	private Long id;
	private String tipo;

	private Boolean assinar = Boolean.TRUE;
	private String motivoNaoAssinar;

	private String processo;
	

	private String descricao;
	private String responsavel;

	private String conteudo;
	private Boolean conteudoDisponivel = Boolean.FALSE;
	private Long paginasConteudo;
	private Date data;
	
	private Boolean textosIguais = Boolean.FALSE;
	private Long idArquivoEletronico;
	
	private List<DocumentoDto<T>> devemSerAssinadosJunto = new ArrayList<DocumentoDto<T>>();
	
	private T documentoFonte;
	
	private DocumentoDto() {
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Boolean getAssinar() {
		return assinar;
	}

	public void setAssinar(Boolean assinar) {
		this.assinar = assinar;
	}

	public String getMotivoNaoAssinar() {
		return motivoNaoAssinar;
	}

	public void setMotivoNaoAssinar(String motivoNaoAssinar) {
		this.motivoNaoAssinar = motivoNaoAssinar;
	}
	
	public String getProcesso() {
		return processo;
	}

	public void setProcesso(String processo) {
		this.processo = processo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public Date getData() {
		return data;
	}
	
	public String getDataFormatada() {
		if (data != null) {
			return sdf.format(getData());
		} else {
			return "";
		}
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public Boolean getConteudoDisponivel() {
		return conteudoDisponivel;
	}

	public void setConteudoDisponivel(Boolean conteudoDisponivel) {
		this.conteudoDisponivel = conteudoDisponivel;
	}
	
	public List<DocumentoDto<T>> getDevemSerAssinadosJunto() {
		return devemSerAssinadosJunto;
	}

	public void setDevemSerAssinadosJunto(List<DocumentoDto<T>> devemSerAssinadosJunto) {
		this.devemSerAssinadosJunto = devemSerAssinadosJunto;
	}
	
	public Long getPaginasConteudo() {
		return paginasConteudo;
	}

	public void setPaginasConteudo(Long paginasConteudo) {
		this.paginasConteudo = paginasConteudo;
	}
	
	public Boolean getTextosIguais() {
		return textosIguais;
	}

	public void setTextosIguais(Boolean textosIguais) {
		this.textosIguais = textosIguais;
	}
	
	public Long getIdArquivoEletronico() {
		return idArquivoEletronico;
	}

	public void setIdArquivoEletronico(Long idArquivoEletronico) {
		this.idArquivoEletronico = idArquivoEletronico;
	}

	@JsonIgnore
	public T getDocumentoFonte() {
		return documentoFonte;
	}
	
	public DocumentoDto<T> createDocumentoSemConteudo() {
		DocumentoDto<T> newDto = new DocumentoDto<T>();
		try {
			BeanUtils.copyProperties(newDto, this);
			newDto.setConteudo("");
			return newDto;
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static DocumentoDto<TextoDto> createDocumentoFromTexto(TextoDto cDto) {
		DocumentoDto<TextoDto> dDto = new DocumentoDto<TextoDto>();
		dDto.documentoFonte = cDto;
		dDto.setTipo(TIPO_TEXTO);
		return dDto;
	}
	
	public static DocumentoDto<TextoDto> from(TextoDto cDto) {
		DocumentoDto<TextoDto> dDto = createDocumentoFromTexto(cDto);
		dDto.setId(cDto.getId());
		dDto.setProcesso(cDto.getProcesso());
		dDto.setDescricao(cDto.getTipoTexto().getDescricao());
		dDto.setResponsavel(cDto.getResponsavel());
		dDto.setData(cDto.getDataInicio());
		dDto.setTextosIguais(cDto.isTextosIguais());
		dDto.setIdArquivoEletronico(cDto.getIdArquivoEletronico());
		return dDto;
	}
	
	public static DocumentoDto<TextoDto> from(TextoDto cDto, List<TextoDto> devemSerAssinadosJunto) {
		DocumentoDto<TextoDto> dDto = from(cDto);
		List<DocumentoDto<TextoDto>> docsDevemSerAssinadosJunto = new ArrayList<DocumentoDto<TextoDto>>();
		for (TextoDto tDto : devemSerAssinadosJunto) {
			docsDevemSerAssinadosJunto.add(from(tDto));
		}
		dDto.setDevemSerAssinadosJunto(docsDevemSerAssinadosJunto);
		return dDto;
	}

	public static DocumentoDto<TextoDto> detalhadoFrom(TextoDto cDto, String conteudo) {
		DocumentoDto<TextoDto> dDto = from(cDto);
		dDto.setConteudo(conteudo);
		dDto.setConteudoDisponivel(StringUtils.isNotEmpty(conteudo));
		return dDto;
	}
	
	public static DocumentoDto<TextoDto> fromNaoAssinadoTexto(
			DocumentoNaoAssinadoDto<TextoDto> dnad) {
		DocumentoDto<TextoDto> dDto = from(dnad.getDocumento());
		dDto.setAssinar(false);
		dDto.setMotivoNaoAssinar(dnad.getMotivo());
		return dDto;
	}
	
	public static DocumentoDto<TextoDto> detalhadoFromNaoAssinadoTexto(
			DocumentoNaoAssinadoDto<TextoDto> dnad, String conteudo) {
		DocumentoDto<TextoDto> dDto = detalhadoFrom(dnad.getDocumento(), conteudo);
		dDto.setAssinar(false);
		dDto.setMotivoNaoAssinar(dnad.getMotivo());
		return dDto;
	}

	private static DocumentoDto<ComunicacaoDto> createDocumentoFromComunicacao(ComunicacaoDto cDto) {
		DocumentoDto<ComunicacaoDto> dDto = new DocumentoDto<ComunicacaoDto>();
		dDto.documentoFonte = cDto;
		dDto.setTipo(TIPO_COMUNICACAO);
		return dDto;
	}
	
	public static DocumentoDto<ComunicacaoDto> from(ComunicacaoDto cDto) {
		DocumentoDto<ComunicacaoDto> dDto = createDocumentoFromComunicacao(cDto);
		dDto.setId(cDto.getId());
		dDto.setProcesso(cDto.getProcesso());
		dDto.setDescricao(cDto.getDscNomeDocumento());
		dDto.setData(cDto.getDataDisponibilizacaoGabinete());
		return dDto;
	}

	public static DocumentoDto<ComunicacaoDto> withPdfPageCountFrom(ComunicacaoDto cDto, long pages) {
		DocumentoDto<ComunicacaoDto> dDto = from(cDto);
		dDto.setPaginasConteudo(pages);
		return dDto;
	}
	
	public static DocumentoDto<ComunicacaoDto> fromNaoAssinadoComunicacao(
			DocumentoNaoAssinadoDto<ComunicacaoDto> dnad) {
		DocumentoDto<ComunicacaoDto> dDto = from(dnad.getDocumento());
		dDto.setAssinar(false);
		dDto.setMotivoNaoAssinar(dnad.getMotivo());
		return dDto;
	}
	
}
