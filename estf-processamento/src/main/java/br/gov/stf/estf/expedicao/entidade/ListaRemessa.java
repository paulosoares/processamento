package br.gov.stf.estf.expedicao.entidade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util;
import br.gov.stf.estf.expedicao.model.util.TipoEntregaEnum;
import br.gov.stf.estf.expedicao.model.util.TipoServicoEnum;

/**
 *
 * @author Roberio.Fernandes
 */
@Entity
@Table(schema = "EXPEDICAO", name = "LISTA_REMESSA")
public class ListaRemessa extends ESTFBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private int anoListaRemessa;
    private long numeroListaRemessa;
    private String usuarioCriacao;
    private Date dataCriacao;
    private String usuarioEnvio;
    private Date dataEnvio;
    private Date dataFinalizacao;
 
	private byte[] imagemListaRemessa;
    private String observacao;
    private UnidadePostagem unidadePostagem;
    private TipoServico tipoServico;
    private List<Remessa> remessas;
    private ContratoPostagem contratoPostagem;

    public ListaRemessa() {
    }

    @Id
    @Column(name = "SEQ_LISTA_REMESSA")
    @GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sequence", sequenceName = "EXPEDICAO.SEQ_LISTA_REMESSA", allocationSize = 1)
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Basic(optional = false)
    @Column(name = "NUM_ANO_LISTA")
    public int getAnoListaRemessa() {
        return anoListaRemessa;
    }

    public void setAnoListaRemessa(int anoListaRemessa) {
        this.anoListaRemessa = anoListaRemessa;
    }

    @Basic(optional = false)
    @Column(name = "NUM_LISTA_REMESSA")
    public long getNumeroListaRemessa() {
        return numeroListaRemessa;
    }

    public void setNumeroListaRemessa(long numeroListaRemessa) {
        this.numeroListaRemessa = numeroListaRemessa;
    }

    @Column(name = "SIG_USUARIO_CRIACAO")
    public String getUsuarioCriacao() {
        return usuarioCriacao;
    }

    public void setUsuarioCriacao(String usuarioCriacao) {
        this.usuarioCriacao = usuarioCriacao;
    }

    @Column(name = "DAT_CRIACAO")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Column(name = "SIG_USUARIO_ENVIO")
    public String getUsuarioEnvio() {
        return usuarioEnvio;
    }

    public void setUsuarioEnvio(String usuarioEnvio) {
        this.usuarioEnvio = usuarioEnvio;
    }

    @Column(name = "DAT_ENVIO")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(Date dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    @Column(name = "DAT_FINALIZACAO")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDataFinalizacao() {
        return dataFinalizacao;
    }

    public void setDataFinalizacao(Date dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }

    @Lob
    @Column(name = "IMG_LISTA_REMESSA")
    public byte[] getImagemListaRemessa() {
		return imagemListaRemessa;
	}

	public void setImagemListaRemessa(byte[] imagemListaRemessa) {
		this.imagemListaRemessa = imagemListaRemessa;
	}

    @Column(name = "TXT_OBSERVACAO", length = 500)
    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @JoinColumn(name = "SEQ_UNIDADE_POSTAGEM", referencedColumnName = "SEQ_UNIDADE_POSTAGEM")
    @ManyToOne
    public UnidadePostagem getUnidadePostagem() {
        return unidadePostagem;
    }

    public void setUnidadePostagem(UnidadePostagem unidadePostagem) {
        this.unidadePostagem = unidadePostagem;
    }

    @JoinColumn(name = "SEQ_TIPO_SERVICO", referencedColumnName = "SEQ_TIPO_SERVICO")
    @ManyToOne(optional = false)
    public TipoServico getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(TipoServico tipoServico) {
        this.tipoServico = tipoServico;
    }

    @Transient
	public List<TipoServico> getTiposServicosAdicionais() {
		List<TipoServico> tiposServicoNecessarios = null;
		if (tipoServico != null) {
			tiposServicoNecessarios = new ArrayList<TipoServico>();
			for (TipoServicoCompativel tipoServicoCompativel : tipoServico.getTiposServicosPrincipais()) {
				TipoServico tipoServicoNecessario = tipoServicoCompativel.getTipoServicoNecessario();
				if (tipoServicoNecessario != null) {
					tiposServicoNecessarios.add(tipoServicoNecessario);
				}
			}
		}
		return tiposServicoNecessarios;
	}

    @OneToMany(cascade = { CascadeType.ALL }, mappedBy = "listaRemessa")
    public List<Remessa> getRemessas() {
        return remessas;
    }

    public void setRemessas(List<Remessa> remessas) {
        this.remessas = remessas;
    }

    @JoinColumn(name = "SEQ_CONTRATO_POSTAGEM", referencedColumnName = "SEQ_CONTRATO_POSTAGEM")
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
	public ContratoPostagem getContratoPostagem() {
		return contratoPostagem;
	}

	public void setContratoPostagem(ContratoPostagem contratoPostagem) {
		this.contratoPostagem = contratoPostagem;
	}

	@Transient
	public String getNumeroListaRemessaAnoFormato() {
		return this.numeroListaRemessa + "/" + Util.getAno(this.dataCriacao);
	}

    @Transient
    public boolean isFinalizada() {
    	return dataEnvio != null;
    } 

	@Transient
    public TipoEntregaEnum getTipoEntrega() {
    	TipoEntregaEnum tipoEntrega = null;
    	if (tipoServico != null) {
    		if (tipoServico.isServicoCorreios() != null) {
	    		if (tipoServico.isServicoCorreios()) {
	    			if (tipoServico.getTipoServicoCorreios() != null) {
	    				if (tipoServico.getTipoServicoCorreios().equals(TipoServicoEnum.POSTAGEM.getCodigo())) {
	    					tipoEntrega = TipoEntregaEnum.CORREIOS;
	    				} else if (tipoServico.getTipoServicoCorreios().equals(TipoServicoEnum.MALOTE.getCodigo())) {
	    					tipoEntrega = TipoEntregaEnum.MALOTE;
	    				}
	    			}
	    		} else {
	    			tipoEntrega = TipoEntregaEnum.ENTREGA_PORTARIA;
	    		}
    		}
    	}
    	return tipoEntrega;
    }

	@Transient
	public String getDescricaoTipoEntrega() {
		String retorno = "";
		TipoEntregaEnum tipo = getTipoEntrega();
		if (tipo.equals(TipoEntregaEnum.CORREIOS)) {
			retorno = "Correios";
		} else if (tipo.equals(TipoEntregaEnum.ENTREGA_PORTARIA)) {
			retorno = "Entrega Portaria";
		} else if (tipo.equals(TipoEntregaEnum.MALOTE)) {
			retorno = "Malote";
		}
		return retorno;
	}
}