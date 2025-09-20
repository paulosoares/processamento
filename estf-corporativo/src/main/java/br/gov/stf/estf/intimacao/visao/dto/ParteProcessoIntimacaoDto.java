package br.gov.stf.estf.intimacao.visao.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import br.gov.stf.estf.entidade.processostf.ModeloComunicacaoEnum;

public class ParteProcessoIntimacaoDto {

    private Long numeroDJ;
    private long seqPessoa;
    private long seqParteProcessual;
    private long seqObjetoIncidente;
    private long seqPecaProcessoEletronico;
    private long seqDocumento;
    private long seqDocumentoAcordao;
    private Long idTipoPecaProcessual;
    private String descricaoTipoPecaProcessual;
    private String nomeParte;
    private String ufParte;
    private String siglaClasseProcesso;
    private String descricaoClasseProcesso;
    private String tipoMeioIntimacao;
    private String descricaoMeioIntimacao;
    private String tipoMeioProcesso;
    private String usuarioExterno;
    private String sigUsuario;
    private long numeroProcesso;
    private long numeroMateria;
    private Long codigoSetor;
    private long codigoMateria;
    private Date dataDivulgacao;
    private ModeloComunicacaoEnum modeloComunicacaoEnum;

    private String descricaoClasseProcessual;
    private String descricaoSetor;
    private boolean intimado;
    private boolean selected;

    private TreeMap<String, List<PecaDTO>> mapProcessosFisicos;
    private TreeMap<String, List<PecaDTO>> mapProcessosEletronicos;

    private Set<String> listaProcessoFisico = new HashSet<String>();
    private Set<String> listaProcessoEletronico = new HashSet<String>();
    
    private Set<PecaDTO> listaPecaProcessoFisicoIntimado = new HashSet<PecaDTO>();
    private Set<PecaDTO> listaPecaProcessoFisicoNaoIntimado = new HashSet<PecaDTO>();
    private Set<PecaDTO> listaPecaProcessoEletronicoIntimado = new HashSet<PecaDTO>();
    private Set<PecaDTO> listaPecaProcessoEletronicoNaoIntimado = new HashSet<PecaDTO>();

    private List<PecaDTO> listaProcessos = new ArrayList<PecaDTO>();

    private String nomeProcesso;

    public Long getNumeroDJ() {
        return numeroDJ;
    }

    public void setNumeroDJ(Long numeroDJ) {
        this.numeroDJ = numeroDJ;
    }

    public long getSeqPessoa() {
        return seqPessoa;
    }

    public void setSeqPessoa(long seqPessoa) {
        this.seqPessoa = seqPessoa;
    }

    public long getSeqParteProcessual() {
        return seqParteProcessual;
    }

    public void setSeqParteProcessual(long seqParteProcessual) {
        this.seqParteProcessual = seqParteProcessual;
    }

    public long getSeqObjetoIncidente() {
        return seqObjetoIncidente;
    }

    public void setSeqObjetoIncidente(long seqObjetoIncidente) {
        this.seqObjetoIncidente = seqObjetoIncidente;
    }

    public long getSeqPecaProcessoEletronico() {
        return seqPecaProcessoEletronico;
    }

    public void setSeqPecaProcessoEletronico(long seqPecaProcessoEletronico) {
        this.seqPecaProcessoEletronico = seqPecaProcessoEletronico;
    }

    public long getSeqDocumento() {
        return seqDocumento;
    }

    public void setSeqDocumento(long seqDocumento) {
        this.seqDocumento = seqDocumento;
    }

    public long getSeqDocumentoAcordao() {
        return seqDocumentoAcordao;
    }

    public void setSeqDocumentoAcordao(long seqDocumentoAcordao) {
        this.seqDocumentoAcordao = seqDocumentoAcordao;
    }

    public Long getIdTipoPecaProcessual() {
        return idTipoPecaProcessual;
    }

    public void setIdTipoPecaProcessual(Long idTipoPecaProcessual) {
        this.idTipoPecaProcessual = idTipoPecaProcessual;
    }

    public String getDescricaoTipoPecaProcessual() {
        return descricaoTipoPecaProcessual;
    }

    public void setDescricaoTipoPecaProcessual(String descricaoTipoPecaProcessual) {
        this.descricaoTipoPecaProcessual = descricaoTipoPecaProcessual;
    }

    public String getNomeParte() {
        return nomeParte;
    }

    public void setNomeParte(String nomeParte) {
        this.nomeParte = nomeParte;
    }

    public String getUfParte() {
        return ufParte;
    }

    public void setUfParte(String ufParte) {
        this.ufParte = ufParte;
    }

    public String getSiglaClasseProcesso() {
        return siglaClasseProcesso;
    }

    public void setSiglaClasseProcesso(String siglaClasseProcesso) {
        this.siglaClasseProcesso = siglaClasseProcesso;
    }

    public String getDescricaoClasseProcesso() {
        return descricaoClasseProcesso;
    }

    public void setDescricaoClasseProcesso(String descricaoClasseProcesso) {
        this.descricaoClasseProcesso = descricaoClasseProcesso;
    }

    public String getTipoMeioIntimacao() {
        return tipoMeioIntimacao;
    }

    public void setTipoMeioIntimacao(String tipoMeioIntimacao) {
        this.tipoMeioIntimacao = tipoMeioIntimacao;
    }

    public String getDescricaoMeioIntimacao() {
        return descricaoMeioIntimacao;
    }

    public void setDescricaoMeioIntimacao(String descricaoMeioIntimacao) {
        this.descricaoMeioIntimacao = descricaoMeioIntimacao;
    }

    public String getTipoMeioProcesso() {
        return tipoMeioProcesso;
    }

    public void setTipoMeioProcesso(String tipoMeioProcesso) {
        this.tipoMeioProcesso = tipoMeioProcesso;
    }

    public String getUsuarioExterno() {
        return usuarioExterno;
    }

    public void setUsuarioExterno(String usuarioExterno) {
        this.usuarioExterno = usuarioExterno;
    }

	public String getSigUsuario() {
		return sigUsuario;
	}

	public void setSigUsuario(String sigUsuario) {
		this.sigUsuario = sigUsuario;
	}

    public long getNumeroProcesso() {
        return numeroProcesso;
    }

    public void setNumeroProcesso(long numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public long getNumeroMateria() {
        return numeroMateria;
    }

    public void setNumeroMateria(long numeroMateria) {
        this.numeroMateria = numeroMateria;
    }

    public Long getCodigoSetor() {
        return codigoSetor;
    }

    public void setCodigoSetor(Long codigoSetor) {
        this.codigoSetor = codigoSetor;
    }

    public long getCodigoMateria() {
        return codigoMateria;
    }

    public void setCodigoMateria(long codigoMateria) {
        this.codigoMateria = codigoMateria;
    }

    public Date getDataDivulgacao() {
        return dataDivulgacao;
    }

    public void setDataDivulgacao(Date dataDivulgacao) {
        this.dataDivulgacao = dataDivulgacao;
    }

    public ModeloComunicacaoEnum getModeloComunicacaoEnum() {
        return modeloComunicacaoEnum;
    }

    public void setModeloComunicacaoEnum(ModeloComunicacaoEnum modeloComunicacaoEnum) {
        this.modeloComunicacaoEnum = modeloComunicacaoEnum;
    }

    public String getDescricaoClasseProcessual() {
        return descricaoClasseProcessual;
    }

    public void setDescricaoClasseProcessual(String descricaoClasseProcessual) {
        this.descricaoClasseProcessual = descricaoClasseProcessual;
    }

    public String getDescricaoSetor() {
        return descricaoSetor;
    }

    public void setDescricaoSetor(String descricaoSetor) {
        this.descricaoSetor = descricaoSetor;
    }

    public boolean isIntimado() {
        return intimado;
    }

    public void setIntimado(boolean intimado) {
        this.intimado = intimado;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public TreeMap<String, List<PecaDTO>> getMapProcessosFisicos() {
        return mapProcessosFisicos;
    }

    public void setMapProcessosFisicos(TreeMap<String, List<PecaDTO>> mapProcessosFisicos) {
        this.mapProcessosFisicos = mapProcessosFisicos;
    }

    public TreeMap<String, List<PecaDTO>> getMapProcessosEletronicos() {
        return mapProcessosEletronicos;
    }

    public void setMapProcessosEletronicos(TreeMap<String, List<PecaDTO>> mapProcessosEletronicos) {
        this.mapProcessosEletronicos = mapProcessosEletronicos;
    }

    public Set<PecaDTO> getListaPecaProcessoFisicoIntimado() {
        return listaPecaProcessoFisicoIntimado;
    }

    public void setListaPecaProcessoFisicoIntimado(Set<PecaDTO> listaPecaProcessoFisicoIntimado) {
        this.listaPecaProcessoFisicoIntimado = listaPecaProcessoFisicoIntimado;
    }

    public Set<PecaDTO> getListaPecaProcessoFisicoNaoIntimado() {
        return listaPecaProcessoFisicoNaoIntimado;
    }

    public void setListaPecaProcessoFisicoNaoIntimado(Set<PecaDTO> listaPecaProcessoFisicoNaoIntimado) {
        this.listaPecaProcessoFisicoNaoIntimado = listaPecaProcessoFisicoNaoIntimado;
    }

    public Set<PecaDTO> getListaPecaProcessoEletronicoIntimado() {
        return listaPecaProcessoEletronicoIntimado;
    }

    public void setListaPecaProcessoEletronicoIntimado(Set<PecaDTO> listaPecaProcessoEletronicoIntimado) {
        this.listaPecaProcessoEletronicoIntimado = listaPecaProcessoEletronicoIntimado;
    }

    public Set<PecaDTO> getListaPecaProcessoEletronicoNaoIntimado() {
        return listaPecaProcessoEletronicoNaoIntimado;
    }

    public void setListaPecaProcessoEletronicoNaoIntimado(Set<PecaDTO> listaPecaProcessoEletronicoNaoIntimado) {
        this.listaPecaProcessoEletronicoNaoIntimado = listaPecaProcessoEletronicoNaoIntimado;
    }

    public List<PecaDTO> getListaProcessos() {
        return listaProcessos;
    }

    public void setListaProcessos(List<PecaDTO> listaProcessos) {
        this.listaProcessos = listaProcessos;
    }

    public String getNomeProcesso() {
        return nomeProcesso;
    }

    public void setNomeProcesso(String nomeProcesso) {
        this.nomeProcesso = nomeProcesso;
    }

	public Set<String> getListaProcessoFisico() {
		return listaProcessoFisico;
	}

	public void setListaProcessoFisico(Set<String> listaProcessoFisico) {
		this.listaProcessoFisico = listaProcessoFisico;
	}

	public Set<String> getListaProcessoEletronico() {
		return listaProcessoEletronico;
	}

	public void setListaProcessoEletronico(Set<String> listaProcessoEletronico) {
		this.listaProcessoEletronico = listaProcessoEletronico;
	}
    
    
}