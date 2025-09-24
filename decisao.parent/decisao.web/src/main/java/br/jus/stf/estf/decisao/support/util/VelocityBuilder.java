package br.jus.stf.estf.decisao.support.util;

import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * Classe responsável por iniciar o contexto do Velocity, e fazer a substituição das variáveis do template.
 * 
 * @author Demetrius.Jube
 * 
 */
public class VelocityBuilder {

	public void iniciaVelocity() throws Exception {
		Velocity.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		Velocity.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
		Velocity.setProperty("resource.loader", "class");
		Velocity.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
		Velocity.setProperty("runtime.log.logsystem.log4j.category", "velocity");
		Velocity.setProperty("runtime.log.logsystem.log4j.logger", "velocity");
		Velocity.init();
	}

	/**
	 * Substitui os valores de variáveis marcados no arquivo de template pelos valores informados pelo usuário
	 * 
	 * @param resourceFileName
	 *            O nome do arquivo que o Velocity deverá processar
	 * @param valueMap
	 *            Um mapa de contendo o nome da variável e o valor que ela deverá ter.
	 * @return O conteúdo do arquivo processado
	 * @throws ParseErrorException
	 * @throws ResourceNotFoundException
	 * @throws Exception
	 */
	public String substituiVariaveisDoTemplate(String resourceFileName, Map<String, Object> valueMap) throws Exception {
		VelocityContext context = new VelocityContext();
		for (String chave : valueMap.keySet()) {
			context.put(chave, valueMap.get(chave));
		}
		StringWriter sw = new StringWriter();
		Template template = Velocity.getTemplate(resourceFileName, "ISO-8859-1");
		template.merge(context, sw);
		return sw.toString();
	}

}
