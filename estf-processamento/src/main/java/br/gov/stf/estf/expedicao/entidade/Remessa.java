package br.gov.stf.estf.expedicao.entidade;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util;

/**
 *
 * @author Roberio.Fernandes
 */
@Entity
@Table(schema = "EXPEDICAO", name = "REMESSA")
public class Remessa extends ESTFBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private String descricaoAnterior;
    private String descricaoPrincipal;
    private String descricaoPosterior;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String pais;
    private String cep;
    private String nomeContato;
    private String email;
    private String codigoAreaTelefone;
    private String numeroTelefone;
    private String codigoAreaFax;
    private String numeroFax;
    private String agrupador;
    private Short codigoOrigem;
    private String vinculo;

    private TipoComunicacaoExpedicao tipoComunicacao;
    private TipoEmbalagem tipoEmbalagem;

    private String numeroComunicacao;
    private String guiaDeslocamento;
    private String malote;
    private String lacre;
    private String observacao;
    private Long numeroPlpCorreios;
    private List<RemessaVolume> volumes;
    private Set<RemessaTipoServico> remessasTiposServico;
    private Set<RemessaListaRemessa> remessasListasEnviadas;

    private ListaRemessa listaRemessa;   

    public Remessa() {
    }

    @Id
    @Column(name = "SEQ_REMESSA")
    @GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sequence", sequenceName = "EXPEDICAO.SEQ_REMESSA", allocationSize = 1)
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "DSC_ANTERIOR")
    public String getDescricaoAnterior() {
        return descricaoAnterior;
    }

    public void setDescricaoAnterior(String descricaoAnterior) {
        this.descricaoAnterior = descricaoAnterior;
    }

    @Basic(optional = false)
    @Column(name = "DSC_PRINCIPAL")
    public String getDescricaoPrincipal() {
        return descricaoPrincipal;
    }

    public void setDescricaoPrincipal(String descricaoPrincipal) {
        this.descricaoPrincipal = descricaoPrincipal;
    }

    @Column(name = "DSC_POSTERIOR")
    public String getDescricaoPosterior() {
        return descricaoPosterior;
    }

    public void setDescricaoPosterior(String descricaoPosterior) {
        this.descricaoPosterior = descricaoPosterior;
    }

    @Basic(optional = false)
    @Column(name = "DSC_LOGRADOURO")
    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    @Basic(optional = false)
    @Column(name = "NUM_NUMERO")
    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Column(name = "DSC_COMPLEMENTO", length = 100)
    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    @Basic(optional = false)
    @Column(name = "DSC_BAIRRO")
    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    @Basic(optional = false)
    @Column(name = "NOM_CIDADE")
    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @Basic(optional = false)
    @Column(name = "SIG_UF")
    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    @Basic(optional = false)
    @Column(name = "NOM_PAIS")
    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    @Basic(optional = false)
    @Column(name = "NUM_CEP")
    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Column(name = "NOM_CONTATO")
    public String getNomeContato() {
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    @Column(name = "DSC_EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "COD_AREA_TELEFONE")
    public String getCodigoAreaTelefone() {
        return codigoAreaTelefone;
    }

    public void setCodigoAreaTelefone(String codigoAreaTelefone) {
        this.codigoAreaTelefone = codigoAreaTelefone;
    }

    @Column(name = "NUM_TELEFONE")
    public String getNumeroTelefone() {
        return numeroTelefone;
    }

    public void setNumeroTelefone(String numeroTelefone) {
        this.numeroTelefone = numeroTelefone;
    }

    @Column(name = "COD_AREA_FAX")
    public String getCodigoAreaFax() {
        return codigoAreaFax;
    }

    public void setCodigoAreaFax(String codigoAreaFax) {
        this.codigoAreaFax = codigoAreaFax;
    }

    @Column(name = "NUM_FAX")
    public String getNumeroFax() {
        return numeroFax;
    }

    public void setNumeroFax(String numeroFax) {
        this.numeroFax = numeroFax;
    }

    @Column(name = "COD_AGRUPADOR")
    public String getAgrupador() {
        return agrupador;
    }

    public void setAgrupador(String agrupador) {
        this.agrupador = agrupador;
    }

    @Column(name = "COD_ORIGEM")
    public Short getCodigoOrigem() {
        return codigoOrigem;
    }

    public void setCodigoOrigem(Short codigoOrigem) {
        this.codigoOrigem = codigoOrigem;
    }

    @Column(name = "NUM_VINCULO")
    public String getVinculo() {
        return vinculo;
    }

    public void setVinculo(String vinculo) {
        this.vinculo = vinculo;
    }

    @JoinColumn(name = "SEQ_TIPO_COMUNICACAO")
    @ManyToOne
    public TipoComunicacaoExpedicao getTipoComunicacao() {
        return tipoComunicacao;
    }

    public void setTipoComunicacao(TipoComunicacaoExpedicao tipoComunicacao) {
        this.tipoComunicacao = tipoComunicacao;
    }

    @JoinColumn(name = "SEQ_TIPO_EMBALAGEM")
    @ManyToOne
    public TipoEmbalagem getTipoEmbalagem() {
        return tipoEmbalagem;
    }

    public void setTipoEmbalagem(TipoEmbalagem tipoEmbalagem) {
        this.tipoEmbalagem = tipoEmbalagem;
    }

    @Column(name = "NUM_COMUNICACAO")
    public String getNumeroComunicacao() {
        return numeroComunicacao;
    }

    public void setNumeroComunicacao(String numeroComunicacao) {
        this.numeroComunicacao = numeroComunicacao;
    }

    @Transient
    public boolean isDadosComunicacaoValidos() {
    	boolean exigeComunicacao = tipoComunicacao != null && tipoComunicacao.isExigeNumeracao();
    	boolean comunicacaoInformada = !Util.isStringNulaOuVazia(numeroComunicacao);
    	return ((!exigeComunicacao && !comunicacaoInformada) || (exigeComunicacao && comunicacaoInformada));
    }

    @Column(name = "NUM_GUIA_DESLOCAMENTO")
    public String getGuiaDeslocamento() {
        return guiaDeslocamento;
    }

    public void setGuiaDeslocamento(String guiaDeslocamento) {
        this.guiaDeslocamento = guiaDeslocamento;
    }

    @Column(name = "NUM_MALOTE")
    public String getMalote() {
        return malote;
    }

    public void setMalote(String malote) {
        this.malote = malote;
    }

    @Column(name = "NUM_LACRE")
    public String getLacre() {
        return lacre;
    }

    public void setLacre(String lacre) {
        this.lacre = lacre;
    }
    
    @Column(name = "TXT_OBSERVACAO", length = 1000)
    public String getObservacao() {
        return observacao;
    }
    

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Column(name = "NUM_PLP_CORREIOS")
    public Long getNumeroPlpCorreios() {
        return numeroPlpCorreios;
    }

    public void setNumeroPlpCorreios(Long numeroPlpCorreios) {
        this.numeroPlpCorreios = numeroPlpCorreios;
    }

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "remessa", fetch = FetchType.EAGER)
    public List<RemessaVolume> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<RemessaVolume> volumes) {
        this.volumes = volumes;
    }

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "remessa", fetch = FetchType.EAGER)
    public Set<RemessaTipoServico> getRemessasTiposServico() {
        return remessasTiposServico;
    }

    public void setRemessasTiposServico(Set<RemessaTipoServico> remessasTiposServico) {
        this.remessasTiposServico = remessasTiposServico;
    }

    @Transient
    public List<TipoServico> getTiposServico() {
    	List<TipoServico> tiposServico = new ArrayList<TipoServico>();
    	if (remessasTiposServico != null) {
	    	for (RemessaTipoServico remessaTipoServico : remessasTiposServico) {
				tiposServico.add(remessaTipoServico.getTipoServico());
			}
    	}
        return tiposServico;
    }

    public void setTiposServico(List<TipoServico> tiposServico) {
        if (remessasTiposServico == null) {
        	remessasTiposServico = new HashSet<RemessaTipoServico>();
        } else {
        	remessasTiposServico.clear();
        }
        for (TipoServico tipoServico : tiposServico) {
        	addTipoServico(tipoServico);
		}
    }

    public void clearTiposServico() {
    	if (remessasTiposServico != null) {
    		remessasTiposServico.clear();
    	}
    }

    @Transient
    public List<TipoServico> getTiposServicoNaoObrigatorios() {
		List<TipoServico> tiposServicoRemessaNaoObrigatorios = null;
		if (listaRemessa != null) {
			List<TipoServico> tiposServicoRemessaObrigatoriosLista = listaRemessa.getTipoServico().getTiposServicoObrigatorios();
			tiposServicoRemessaNaoObrigatorios = new ArrayList<TipoServico>(getTiposServico());
			tiposServicoRemessaNaoObrigatorios.removeAll(tiposServicoRemessaObrigatoriosLista);
		}
		return tiposServicoRemessaNaoObrigatorios;
    }

    @Transient
	public List<TipoServico> getTiposServicosObrigatorios() {
		List<TipoServico> tiposServicoRemessaObrigatorios = null;
		if (listaRemessa != null) {
			List<TipoServico> tiposServicoRemessaNaoObrigatorios = getTiposServicoNaoObrigatorios();
			tiposServicoRemessaObrigatorios = new ArrayList<TipoServico>(getTiposServico());
			tiposServicoRemessaObrigatorios.removeAll(tiposServicoRemessaNaoObrigatorios);
		}
		return tiposServicoRemessaObrigatorios;
	}

    public void addTipoServico(TipoServico tipoServico) {
        if (remessasTiposServico == null) {
        	remessasTiposServico = new HashSet<RemessaTipoServico>();
        }
        if (!getTiposServico().contains(tipoServico)) {
	        RemessaTipoServico remessaTipoServico = new RemessaTipoServico();
	    	remessaTipoServico.setTipoServico(tipoServico);
	    	remessaTipoServico.setRemessa(this);
	    	remessasTiposServico.add(remessaTipoServico);
        }
    }

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "remessa")
    public Set<RemessaListaRemessa> getRemessasListasEnviadas() {
        return remessasListasEnviadas;
    }

    public void setRemessasListasEnviadas(Set<RemessaListaRemessa> remessasListasEnviadas) {
        this.remessasListasEnviadas = remessasListasEnviadas;
    }

    @Transient
    public List<ListaRemessa> getListasEnviadas() {
    	List<ListaRemessa> tiposEmbalagem = new ArrayList<ListaRemessa>();
    	if (remessasListasEnviadas != null) {
        	for (RemessaListaRemessa remessaListaRemessa : remessasListasEnviadas) {
    			tiposEmbalagem.add(remessaListaRemessa.getListaRemessa());
    		}
    	}
        return tiposEmbalagem;
    }

    public void setListasEnviadas(List<ListaRemessa> listasEnviadas) {
        if (remessasListasEnviadas == null) {
        	remessasListasEnviadas = new HashSet<RemessaListaRemessa>();
        } else {
        	remessasListasEnviadas.clear();
        }
        for (ListaRemessa listaRemessa : listasEnviadas) {
        	addListaEnviada(listaRemessa);
		}
    }

    public void addListaEnviada(ListaRemessa listaRemessa) {
        if (remessasListasEnviadas == null) {
        	remessasListasEnviadas = new HashSet<RemessaListaRemessa>();
        }
        if (!getListasEnviadas().contains(listaRemessa)) {
	        RemessaListaRemessa remessaListaRemessa = new RemessaListaRemessa();
	    	remessaListaRemessa.setListaRemessa(listaRemessa);
	    	remessaListaRemessa.setRemessa(this);
	    	remessasListasEnviadas.add(remessaListaRemessa);
	    }
    }

    @JoinColumn(name = "SEQ_LISTA_REMESSA", referencedColumnName = "SEQ_LISTA_REMESSA")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    public ListaRemessa getListaRemessa() {
        return listaRemessa;
    }

    public void setListaRemessa(ListaRemessa listaRemessa) {
        this.listaRemessa = listaRemessa;
    }
    
	@Transient
    public String getMostraEtiquetasSeperadasPorVirgula() {
		StringBuffer retorno = new StringBuffer();
		for (RemessaVolume remessaVolume : this.volumes) {
			if (remessaVolume.getNumeroEtiquetaCorreios() != null) {
				retorno.append(remessaVolume.getNumeroEtiquetaCorreios());
				retorno.append(", ");
			}
		}
		/* Retirando vírgula e espaço do último código*/
		String r = (retorno.toString().length() > 0 ? (retorno.toString().substring(0, retorno.toString().length() - 2)) : retorno).toString();
		return r;
	}	
}