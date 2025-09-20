package br.gov.stf.estf.entidade.usuario;

import java.util.Comparator;
import java.util.List;

import br.gov.stf.estf.entidade.processosetor.ControleDistribuicao;

public class UsuarioDistribuicao extends UsuarioEGab {

	private Long carga;
    private Long cargaComPeso;
	private Long cargaVirtual;
	private List<ControleDistribuicao> listaMapaDistribuicao;

	
	public Long getCargaVirtual() {
		return cargaVirtual;
	}
	public void setCargaVirtual(Long cargaVirtual) {
		this.cargaVirtual = cargaVirtual;
	}
	public Long getCarga() {
		return carga;
	}
	public void setCarga(Long carga) {
		this.carga = carga;
	}
    public Long getCargaComPeso() {
        return cargaComPeso;
    }
    public void setCargaComPeso(Long cargaComPeso) {
        this.cargaComPeso = cargaComPeso;
    }       
	public List<ControleDistribuicao> getListaMapaDistribuicao() {
		return listaMapaDistribuicao;
	}
	public void setListaMapaDistribuuicao(
			List<ControleDistribuicao> listaMapaDistribuicao) {
		this.listaMapaDistribuicao = listaMapaDistribuicao;
	}
    
    
    
    
	public static class UsuarioDistribuicaoCargaComparator implements Comparator {

		public int compare(Object obj1, Object obj2) {
			
			if( !(obj1 instanceof UsuarioDistribuicao) || !(obj2 instanceof UsuarioDistribuicao) )
				return 0;
			
			UsuarioDistribuicao usuario1 = (UsuarioDistribuicao) obj1;
			UsuarioDistribuicao usuario2 = (UsuarioDistribuicao) obj2;
			
			if( usuario1.getCargaVirtual() == null || usuario2.getCargaVirtual() == null )
				return 0;
			
			return usuario1.getCargaVirtual().compareTo(usuario2.getCargaVirtual());
		}
		
	}
    
    public static class UsuarioDistribuicaoCargaComPesoComparator implements Comparator {

        public int compare(Object obj1, Object obj2) {
            
            if( !(obj1 instanceof UsuarioDistribuicao) || !(obj2 instanceof UsuarioDistribuicao) )
                return 0;
            
            UsuarioDistribuicao usuario1 = (UsuarioDistribuicao) obj1;
            UsuarioDistribuicao usuario2 = (UsuarioDistribuicao) obj2;
            
            if( usuario1.getCargaComPeso() == null || usuario2.getCargaComPeso() == null )
                return 0;
            
            return usuario1.getCargaComPeso().compareTo(usuario2.getCargaComPeso());
        }
        
    }



}
