package br.gov.stf.estf.publicacao;

import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;

import br.gov.stf.estf.entidade.publicacao.ConteudoPublicacao;
import br.gov.stf.estf.publicacao.compordj.builder.ConteudoBuilder;
import br.gov.stf.estf.publicacao.confirmardj.builder.ConfirmarMateriaBuilder;
import br.gov.stf.framework.model.service.ServiceException;

@SuppressWarnings("unchecked")
public class BuilderDj {
	/**
	 * Resource bundle que contém o arquivo.
	 */
	private static PropertyResourceBundle builderDjProperties;
	private static PropertyResourceBundle confirmacaoDjProperties;
	private static Map<String, ConteudoBuilder> mapaClasseObjetoConteudoBuilder;
	
	private static boolean iniciado = false;

	/**
	 * Carrega um determinado arquivo de propriedades.
	 * Caso não encontre o arquivo cria o resource vazio.
	 * @param poBundleName nome do arquivo
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void init() throws ServiceException {
		builderDjProperties = (PropertyResourceBundle) PropertyResourceBundle.getBundle("builderDj");
		confirmacaoDjProperties = (PropertyResourceBundle) PropertyResourceBundle.getBundle("confirmacaoDj");
		
		mapaClasseObjetoConteudoBuilder = new HashMap<String, ConteudoBuilder>();
		Enumeration<String> chaves = builderDjProperties.getKeys();
		while ( chaves.hasMoreElements() ) {
			String codigo = chaves.nextElement();
			String classe = builderDjProperties.getString(codigo);
			if ( !mapaClasseObjetoConteudoBuilder.containsKey(classe) ) {
				try {
					mapaClasseObjetoConteudoBuilder.put(classe, (ConteudoBuilder) loadClass(classe));
				} catch ( Exception e ) {
					throw new ServiceException(e);
				}
			}
		}
		
		iniciado = true;
	}
	
	private static Object loadClass (String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return Class.forName(className).newInstance();
	}
	
	public static ConteudoBuilder getConteudoBuilder (ConteudoPublicacao cp) throws ServiceException {
		if ( !iniciado ) {
			init();
		}
		String codigo = cp.getCodigoCapitulo()+"."+cp.getCodigoMateria()+"."+cp.getCodigoConteudo();
		String classe = builderDjProperties.getString(codigo);
		return mapaClasseObjetoConteudoBuilder.get(classe);
	}
	
	public static ConfirmarMateriaBuilder getConfirmacaoBuilder (ConteudoPublicacao cp) throws ServiceException {
		if ( !iniciado ) {
			init();
		}
		
		try {
			String codigo = cp.getCodigoCapitulo()+"."+cp.getCodigoMateria();
			String classe = confirmacaoDjProperties.getString(codigo);
			return loadConfirmacaoMateriaBuilder(classe, cp);
		} catch ( MissingResourceException e ) {
			return null;
		} catch ( Exception e ) {
			throw new ServiceException(e);
		}
		
	}
	
	private static ConfirmarMateriaBuilder loadConfirmacaoMateriaBuilder (String classe, ConteudoPublicacao conteudoPublicacao) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
		return (ConfirmarMateriaBuilder) Class.forName(classe).getConstructor( ConteudoPublicacao.class ).newInstance(conteudoPublicacao);
	}
}
