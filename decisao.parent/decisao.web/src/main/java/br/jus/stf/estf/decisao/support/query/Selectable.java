package br.jus.stf.estf.decisao.support.query;

/**
 * Interface que deve ser implementada por objetos "selecion�veis", por 
 * exemplo em uma cole��o de resultados.
 * 
 * @author Rodrigo Barreiros
 * @since 07.04.2010
 */
public interface Selectable {
    
	/**
	 * Indica se o objeto est�, ou n�o, selecionado. Retorna um valor boleano.
	 * 
	 * @return true, se selecionado, false, caso contr�rio.
	 */
    boolean isSelected();
    
    /**
     * Seta a flag de sele��o do objeto corrente.
     * 
     * @param selected o indicador de sele��o
     */
    void setSelected(boolean selected);

}
