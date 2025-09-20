package br.gov.stf.estf.entidade.documento;

import br.gov.stf.framework.util.GenericEnum;

public class TipoArquivo extends GenericEnum<Long, TipoArquivo> {
	public static final TipoArquivo HTML = new TipoArquivo(1L, "text/html", "HTML", ".html");
	public static final TipoArquivo PLAIN_TEXT = new TipoArquivo(2L, "text/plain", "Texto puro", ".txt");
	public static final TipoArquivo RTF = new TipoArquivo(3L, "text/richtext", "Rich Text Format  = new TipoArquivo(RTF)", ".rtf");
	public static final TipoArquivo BINARY = new TipoArquivo(4L, "application/octet-stream",
			"Binário  = new TipoArquivo(download)", "diversas");
	public static final TipoArquivo PDF = new TipoArquivo(5L, "application/pdf", "Adobe Acrobat PDF", ".pdf");
	public static final TipoArquivo ZIP = new TipoArquivo(6L, "application/zip", "Arquivo ZIP", ".zip");
	public static final TipoArquivo MSWORD = new TipoArquivo(7l, "application/msword", "Microsoft Word", ".doc, .dot");
	public static final TipoArquivo MSEXCEL = new TipoArquivo(8l, "application/vnd.ms-excel", "Microsoft Excel", ".xls");
	public static final TipoArquivo MSPOWERPOINT = new TipoArquivo(9l, "application/vnd.ms-powerpoint", "Microsoft PowerPoint",
			".ppt, .pps");
	public static final TipoArquivo MSDOWNLOAD = new TipoArquivo(10l, "application/x-msdownload", "Arquivo para Download",
			"diversas");
	public static final TipoArquivo JPEG = new TipoArquivo(11l, "image/jpeg", "Imagem JPEG", ".jpg, .jpeg");
	public static final TipoArquivo GIF = new TipoArquivo(12l, "image/gif", "Imagem GIF", ".gif");
	public static final TipoArquivo PNG = new TipoArquivo(13l, "image/png", "Imagem PNG", ".png");
	public static final TipoArquivo TIFF = new TipoArquivo(14l, "image/tiff", "Imagem TIFF", ".tif, .tiff");
	
	public static final TipoArquivo MOV = new TipoArquivo(15l, "video/mov", "Vídeo MOV", ".mov");
	public static final TipoArquivo MP4 = new TipoArquivo(16l, "video/mp4", "Vídeo MP4", ".mp4");
	public static final TipoArquivo AVI = new TipoArquivo(17l, "video/avi", "Vídeo AVI", ".avi");
	public static final TipoArquivo MP3 = new TipoArquivo(18l, "audio/mp3", "Audio MP3", ".mp3");
	public static final TipoArquivo WAV = new TipoArquivo(19l, "audio/wav", "Audio WAV", ".wav");
	
	private final String formatoArquivo;
	private final String descricao;
	private final String extensoes;

	private TipoArquivo(Long codigo) {
		this(codigo, "FORMATO_" + codigo, "FORMATO " + codigo, "");
	}

	private TipoArquivo(Long codigo, String formatoArquivo, String descricao, String extensoes) {
		super(codigo);
		this.formatoArquivo = formatoArquivo;
		this.descricao = descricao;
		this.extensoes = extensoes;
	}

	public String getFormatoArquivo() {
		return formatoArquivo;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getExtensoes() {
		return extensoes;
	}

	public static TipoArquivo valueOf(Long codigo) {
		return valueOf(TipoArquivo.class, codigo);
	}

	public TipoArquivo[] values() {
		return values(new TipoArquivo[0], TipoArquivo.class);
	}
}
