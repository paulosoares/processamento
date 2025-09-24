package br.jus.stf.estf.decisao.uri;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Esta classe serve  de base para implementar URIBuilders que precisam codificar parâmetros 
 * na parte "query" da URI.
 * 
 * @author João Rafael Moraes Nicola
 *
 */
public class AbstractParamsURIBuilder  {
	public static final String DEFAULT_ENCODING="latin1";

	private final String encoding;
	
	private final Map<String,List<String>> params = new TreeMap<String,List<String>>(); 

	public AbstractParamsURIBuilder(String encoding) {
		this.encoding = encoding;
	}
	
	public void addParam(String key,Object value) {
		if(!params.containsKey(key)) {
			params.put(key,new LinkedList<String>());			
		}
		params.get(key).add(value == null ? "" : value.toString());	
	}
	
	public String encodeQuery() throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String,List<String>> e : params.entrySet()) {
			for(String v : e.getValue()) {
				if(sb.length() > 0) sb.append('&');
				sb.append(URLEncoder.encode(e.getKey(),encoding));
				sb.append('=');
				sb.append(URLEncoder.encode(v,encoding));
			}
		}
		return sb.toString();
	}	
}
