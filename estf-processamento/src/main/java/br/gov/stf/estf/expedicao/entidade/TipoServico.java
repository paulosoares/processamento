package br.gov.stf.estf.expedicao.entidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.framework.model.entity.BaseEntity;

/**
 *
 * @author Roberio.Fernandes
 */
@Entity
@Table(schema = "EXPEDICAO", name = "TIPO_SERVICO")
public class TipoServico extends ESTFBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private String nome;
    private Boolean servicoCorreios;
    private String tipoServicoCorreios;
    private String codigoServicoCorreios;
    private Serializable imagemChancela;
    private Short quantidadeEtiquetasPagina;
    private Boolean ativo;
    private List<ListaRemessa> listaRemessa;
    private Set<TipoServicoCompativel> tiposServicosNecessarios;
    private Set<TipoServicoCompativel> tiposServicosAdicionais;
    private Set<TipoServicoCompativel> tiposServicosPrincipais;
    private Set<TipoServicoEmbalagem> tiposServicoEmbalagens;

    public TipoServico() {
    }

    @Id
    @Column(name = "SEQ_TIPO_SERVICO")
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Basic(optional = false)
    @Column(name = "NOM_TIPO_SERVICO")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Basic(optional = false)
    @Column(name = "FLG_SERVICO_CORREIOS")
    @Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
    public Boolean isServicoCorreios() {
        return servicoCorreios;
    }

    public void setServicoCorreios(Boolean servicoCorreios) {
        this.servicoCorreios = servicoCorreios;
    }

    @Column(name = "TIP_SERVICO_CORREIOS")
    public String getTipoServicoCorreios() {
        return tipoServicoCorreios;
    }

    public void setTipoServicoCorreios(String tipoServicoCorreios) {
        this.tipoServicoCorreios = tipoServicoCorreios;
    }

    @Column(name = "COD_SERVICO_CORREIOS")
    public String getCodigoServicoCorreios() {
        return codigoServicoCorreios;
    }

    public void setCodigoServicoCorreios(String codigoServicoCorreios) {
        this.codigoServicoCorreios = codigoServicoCorreios;
    }

    @Lob
    @Column(name = "IMG_CHANCELA")
    public Serializable getImagemChancela() {
        return imagemChancela;
    }

    public void setImagemChancela(Serializable imagemChancela) {
        this.imagemChancela = imagemChancela;
    }

    @Column(name = "QTD_ETIQUETA_PAGINA")
    public Short getQuantidadeEtiquetasPagina() {
        return quantidadeEtiquetasPagina;
    }

    public void setQuantidadeEtiquetasPagina(Short quantidadeEtiquetasPagina) {
        this.quantidadeEtiquetasPagina = quantidadeEtiquetasPagina;
    }

    @Basic(optional = false)
    @Column(name = "FLG_ATIVO")
    @Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
    public Boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @OneToMany(mappedBy = "id", cascade = { CascadeType.ALL })
    public List<ListaRemessa> getListaRemessa() {
        return listaRemessa;
    }

    public void setListaRemessa(List<ListaRemessa> listaRemessa) {
        this.listaRemessa = listaRemessa;
    }

    @OneToMany(mappedBy = "tipoServicoNecessario", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    public Set<TipoServicoCompativel> getTiposServicosNecessarios() {
        return tiposServicosNecessarios;
    }

    public void setTiposServicosNecessarios(Set<TipoServicoCompativel> tiposServicosNecessarios) {
        this.tiposServicosNecessarios = tiposServicosNecessarios;
    }

    @OneToMany(mappedBy = "tipoServicoAdicional", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    public Set<TipoServicoCompativel> getTiposServicosAdicionais() {
        return tiposServicosAdicionais;
    }

    public void setTiposServicosAdicionais(Set<TipoServicoCompativel> tiposServicosAdicionais) {
        this.tiposServicosAdicionais = tiposServicosAdicionais;
    }

    @OneToMany(mappedBy = "tipoServicoPrincipal", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    public Set<TipoServicoCompativel> getTiposServicosPrincipais() {
        return tiposServicosPrincipais;
    }

    public void setTiposServicosPrincipais(Set<TipoServicoCompativel> tiposServicosPrincipais) {
        this.tiposServicosPrincipais = tiposServicosPrincipais;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "tipoServico")
    public Set<TipoServicoEmbalagem> getTiposServicoEmbalagens() {
        return tiposServicoEmbalagens;
    }

    public void setTiposServicoEmbalagens(Set<TipoServicoEmbalagem> tiposServicoEmbalagens) {
        this.tiposServicoEmbalagens = tiposServicoEmbalagens;
    }

    @Transient
    public List<TipoEmbalagem> getTiposEmbalagem() {
    	List<TipoEmbalagem> tiposEmbalagem = new ArrayList<TipoEmbalagem>();
    	if (tiposServicoEmbalagens != null) {
        	for (TipoServicoEmbalagem tipoServicoEmbalagem : tiposServicoEmbalagens) {
    			tiposEmbalagem.add(tipoServicoEmbalagem.getTipoEmbalagem());
    		}
    	}
        return tiposEmbalagem;
    }

    public void setTiposEmbalagem(List<TipoEmbalagem> tiposEmbalagem) {
        if (tiposServicoEmbalagens == null) {
        	tiposServicoEmbalagens = new HashSet<TipoServicoEmbalagem>();
        } else {
        	tiposServicoEmbalagens.clear();
        }
        for (TipoEmbalagem tipoEmbalagem : tiposEmbalagem) {
        	addTipoEmbalagem(tipoEmbalagem);
		}
    }

    @Transient
    public void addTipoEmbalagem(TipoEmbalagem tipoEmbalagem) {
        if (tiposServicoEmbalagens == null) {
        	tiposServicoEmbalagens = new HashSet<TipoServicoEmbalagem>();
        }
        if (!getTiposEmbalagem().contains(tipoEmbalagem)) {
	        TipoServicoEmbalagem tipoServicoEmbalagem = new TipoServicoEmbalagem();
	    	tipoServicoEmbalagem.setTipoEmbalagem(tipoEmbalagem);
	    	tipoServicoEmbalagem.setTipoServico(this);
	    	tiposServicoEmbalagens.add(tipoServicoEmbalagem);
        }
    }

    @Transient
    public List<TipoServico> getTiposServicoAdicionais() {
    	List<TipoServico> tiposServicoAdicionais = new ArrayList<TipoServico>();
    	for (TipoServicoCompativel tipoServicoCompativel : getTiposServicosPrincipais()) {
			TipoServico tipoServicoAdicional = tipoServicoCompativel.getTipoServicoAdicional();
			tiposServicoAdicionais.add(tipoServicoAdicional);
		}
		return tiposServicoAdicionais;
    }

    @Transient
    public List<TipoServico> getTiposServicoObrigatorios() {
    	List<TipoServico> tiposServicoObrigatorios = new ArrayList<TipoServico>();
		for (TipoServicoCompativel tipoServicoCompativel : getTiposServicosPrincipais()) {
			TipoServico tipoServicoAdicional = tipoServicoCompativel.getTipoServicoAdicional();
			if (tipoServicoCompativel.isObrigatorio()) {
				tiposServicoObrigatorios.add(tipoServicoAdicional);
			}
		}
		return tiposServicoObrigatorios;
    }

    @Transient
    public TipoServico getTipoServicoNecessarioAoAdicional(TipoServico tipoServicoAdicional) {
    	return getTipoServicoNecessarioAoAdicional(tipoServicoAdicional.getId());
    }

    @Transient
    public TipoServico getTipoServicoNecessarioAoAdicional(Long idTipoServicoAdicional) {
    	TipoServico tipoServicoNecessarioAoAdicional = null;
    	for (TipoServicoCompativel tipoServicoCompativel : getTiposServicosPrincipais()) {
			TipoServico tipoServicoAdicionalAux = tipoServicoCompativel.getTipoServicoAdicional();
			if (tipoServicoAdicionalAux.getId().equals(idTipoServicoAdicional)) {
				tipoServicoNecessarioAoAdicional = tipoServicoCompativel.getTipoServicoNecessario();
				break;
			}
		}
    	return tipoServicoNecessarioAoAdicional;
    }

    @Transient
    public TipoServico getTipoServicoAdicionalAoNecessario(Long idTipoServicoNecessario) {
    	TipoServico tipoServicoNecessarioAoAdicional = null;
    	for (TipoServicoCompativel tipoServicoCompativel : getTiposServicosPrincipais()) {
			TipoServico tipoServicoNecessarioAux = tipoServicoCompativel.getTipoServicoNecessario();
			if (tipoServicoNecessarioAux != null && tipoServicoNecessarioAux.getId().equals(idTipoServicoNecessario)) {
				tipoServicoNecessarioAoAdicional = tipoServicoCompativel.getTipoServicoAdicional();
				break;
			}
		}
    	return tipoServicoNecessarioAoAdicional;
    }

    @Override
	public int compareTo(BaseEntity object) {
		if (object == null)
			return 1;
		if (getId() == null && object.getId() == null)
			return 0;
		if (object.getId() == null)
			return -1;
		return this.getId().compareTo((Long) object.getId());
	}
}