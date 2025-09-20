package br.gov.stf.estf.intimacao.visao.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.processostf.ModeloComunicacaoEnum;

public class ParteIntimacaoDto {

    private long seqPessoa;
    private String nomeParte;
    private String descricaoMeioIntimacao;
    private String tipoMeioIntimacao;
    private ModeloComunicacaoEnum modeloComunicacaoEnum;
    private Long numeroDJ;
    private Date dataDivulgacao;
    private List<ProcessoIntimacaoDto> processos;
    private String isUsuarioExterno;

    private List<ProcessoIntimacaoDto> processosEletronicosIntimados;
    private List<ProcessoIntimacaoDto> processosEletronicosNaoIntimados;
    private List<ProcessoIntimacaoDto> processosFisicosIntimados;
    private List<ProcessoIntimacaoDto> processosFisicosNaoIntimados;

    private Boolean selected;

    public ParteIntimacaoDto() {
    }

    public long getSeqPessoa() {
        return seqPessoa;
    }

    public void setSeqPessoa(long seqPessoa) {
        this.seqPessoa = seqPessoa;
    }

    public String getNomeParte() {
        return nomeParte;
    }

    public void setNomeParte(String nomeParte) {
        this.nomeParte = nomeParte;
    }

    public String getDescricaoMeioIntimacao() {
        return descricaoMeioIntimacao;
    }

    public void setDescricaoMeioIntimacao(String descricaoMeioIntimacao) {
        this.descricaoMeioIntimacao = descricaoMeioIntimacao;
    }

    public String getTipoMeioIntimacao() {
        return tipoMeioIntimacao;
    }

    public void setTipoMeioIntimacao(String tipoMeioIntimacao) {
        this.tipoMeioIntimacao = tipoMeioIntimacao;
    }

    public ModeloComunicacaoEnum getModeloComunicacaoEnum() {
        return modeloComunicacaoEnum;
    }

    public void setModeloComunicacaoEnum(ModeloComunicacaoEnum modeloComunicacaoEnum) {
        this.modeloComunicacaoEnum = modeloComunicacaoEnum;
    }

    public Long getNumeroDJ() {
        return numeroDJ;
    }

    public void setNumeroDJ(Long numeroDJ) {
        this.numeroDJ = numeroDJ;
    }

    public Date getDataDivulgacao() {
        return dataDivulgacao;
    }

    public void setDataDivulgacao(Date dataDivulgacao) {
        this.dataDivulgacao = dataDivulgacao;
    }

    public List<ProcessoIntimacaoDto> getProcessos() {
        return processos;
    }

    public void setProcessos(List<ProcessoIntimacaoDto> processos) {
        this.processos = processos;
    }

    public String getIsUsuarioExterno() {
        return isUsuarioExterno;
    }

    public void setIsUsuarioExterno(String isUsuarioExterno) {
        this.isUsuarioExterno = isUsuarioExterno;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public List<ProcessoIntimacaoDto> getProcessosEletronicosIntimados() {
        return processosEletronicosIntimados;
    }

    public void setProcessosEletronicosIntimados(List<ProcessoIntimacaoDto> processosEletronicosIntimados) {
        this.processosEletronicosIntimados = processosEletronicosIntimados;
    }

    public List<ProcessoIntimacaoDto> getProcessosEletronicosNaoIntimados() {
        return processosEletronicosNaoIntimados;
    }

    public void setProcessosEletronicosNaoIntimados(List<ProcessoIntimacaoDto> processosEletronicosNaoIntimados) {
        this.processosEletronicosNaoIntimados = processosEletronicosNaoIntimados;
    }

    public List<ProcessoIntimacaoDto> getProcessosFisicosIntimados() {
        return processosFisicosIntimados;
    }

    public void setProcessosFisicosIntimados(List<ProcessoIntimacaoDto> processosFisicosIntimados) {
        this.processosFisicosIntimados = processosFisicosIntimados;
    }

    public List<ProcessoIntimacaoDto> getProcessosFisicosNaoIntimados() {
        return processosFisicosNaoIntimados;
    }

    public void setProcessosFisicosNaoIntimados(List<ProcessoIntimacaoDto> processosFisicosNaoIntimados) {
        this.processosFisicosNaoIntimados = processosFisicosNaoIntimados;
    }

    public void agrupar() {
        processosEletronicosIntimados = new ArrayList<ProcessoIntimacaoDto>();
        processosEletronicosNaoIntimados = new ArrayList<ProcessoIntimacaoDto>();
        processosFisicosIntimados = new ArrayList<ProcessoIntimacaoDto>();
        processosFisicosNaoIntimados = new ArrayList<ProcessoIntimacaoDto>();
        for (ProcessoIntimacaoDto processoIntimacaoDto : processos) {
            processoIntimacaoDto.agrupar();
            if (processoIntimacaoDto.getTipoMeioProcesso().equals("E")) {
                if (processoIntimacaoDto.getPecasNaoIntimadas().isEmpty()) {
                	processoIntimacaoDto.setSelected(false);
                    processosEletronicosIntimados.add(processoIntimacaoDto);
                } else {
                    processosEletronicosNaoIntimados.add(processoIntimacaoDto);
                }
            } else {
                if (processoIntimacaoDto.getPecasNaoIntimadas().isEmpty()) {
                	processoIntimacaoDto.setSelected(false);
                    processosFisicosIntimados.add(processoIntimacaoDto);
                } else {
                    processosFisicosNaoIntimados.add(processoIntimacaoDto);
                }
            }
        }
    }
}