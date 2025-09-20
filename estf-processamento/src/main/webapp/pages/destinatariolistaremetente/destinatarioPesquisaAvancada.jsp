<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<h:inputHidden id="pesquisaAvancada" value="#{beanDestinatario.flagPesquisaAvancada}" />
<script type="text/javascript">
function validacaoNumero(campo) {
	var digitos = "0123456789"
	var campoTemp
	for (var i = 0; i < campo.value.length; i++) {
		campoTemp = campo.value.substring(i, i + 1)
		
		if(campo.id == 'idCep'){
			campo.value = campo.value.substring(0, 8);
		}
		
		if(campo.id == 'idDDDTelefone') {
			if(i > 1){
				campo.value = campo.value.substring(0, 2);
			}
		}
		
		if(campo.id == 'idNumeroTelefone') {
			if(i > 9){
				campo.value = campo.value.substring(0, 10);
			}
		}
		
		if(campo.id == 'idDDDFax') {
			if(i > 1){
				campo.value = campo.value.substring(0, 2);
			}
		}
		
		if(campo.id == 'idNumeroFax') {
			if(i > 9){
				campo.value = campo.value.substring(0, 10);
			}
		}
		
		if(campo.id == 'idCodOrgao') {
			if(i > 3){
				campo.value = campo.value.substring(0, 4);
			}
		}
		
		if (digitos.indexOf(campoTemp) == -1) {
			campo.value = campo.value.substring(0, i);			
		}
	}
}

function validacaoAlfa(campo){	
	for (var i = 0; i < campo.value.length; i++) {
		if(campo.id == 'idNumero'){
			if(i > 4){
				campo.value = campo.value.substring(0, 5);
			}
		}
		
		if(campo.id == 'idDetalhePre'){
			if(i > 99){
				campo.value = campo.value.substring(0, 100);
			}
		}
		
		if(campo.id == 'idDetalhePrincipal'){
			if(i > 199){
				campo.value = campo.value.substring(0, 200);
			}
		}
		
		if(campo.id == 'idDetalhePos'){
			if(i > 99){
				campo.value = campo.value.substring(0, 100);
			}
		}
		
		if(campo.id == 'idBairro'){
			if(i > 199){
				campo.value = campo.value.substring(0, 200);
			}
		}
		
		if(campo.id == 'idLogradouro'){
			if(i > 249){
				campo.value = campo.value.substring(0, 250);
			}
		}
		
		if(campo.id == 'idComplemento'){
			if(i > 29){
				campo.value = campo.value.substring(0, 30);
			}
		}
		
		if(campo.id == 'idAgrupador'){
			if(i > 9){
				campo.value = campo.value.substring(0, 20);
			}
		}
		
		if(campo.id == 'idEmail'){
			if(i > 49){
				usuario = campo.value.substring(0, campo.value.indexOf("@"));
				dominio = campo.value.substring(campo.value.indexOf("@")+ 1, campo.value.length);
					if ((usuario.length >=1) &&
					    (dominio.length >=3) && 
					    (usuario.search("@")==-1) && 
					    (dominio.search("@")==-1) &&
					    (usuario.search(" ")==-1) && 
					    (dominio.search(" ")==-1) &&
					    (dominio.search(".")!=-1) &&      
					    (dominio.indexOf(".") >=1)&& 
					    (dominio.lastIndexOf(".") < dominio.length - 1)) {					
					document.getElementById("msgEmailPesquisaAvancada").innerHTML="";
					}
					else{
						document.getElementById("msgEmailPesquisaAvancada").innerHTML="<font color='red'>Email inv√°lido</font>";	
					}
				campo.value = campo.value.substring(0, 50);
			}
		}
		
		if(campo.id == 'idContato'){
			if(i > 49){
				campo.value = campo.value.substring(0, 50);
			}
		}
	}	
}
</script>
<h:panelGrid id="painelPesquisaAvancadaDestinatario">
    <rich:panel bodyClass="inpanelBody">
        <f:facet name="header">
            <b>DESTINAT¡ÅRIO:</b>
        </f:facet>
        
        <ul>        
            <table>
                <tr>
                    <td><h:outputText value="Detalhe PrÈ " /></td>
                    <td>
                        <t:inputText id="idDetalhePre"
                                     size="60"
                                     value="#{beanDestinatario.bean.descricaoAnterior}" onkeyup="javascript:validacaoAlfa(this);"/>
                        <rich:hotKey selector="#idDetalhePre"
									key="return"
									handler="document.getElementById('btnPesquisaAvancadaDestinatario').onclick()" />
                    </td>
                </tr>
                <tr>
                    <td><h:outputText value="Destinat·rio" /></td>
                    <td>
                        <t:inputText id="idDetalhePrincipal"
                                     size="60"
                                     value="#{beanDestinatario.bean.descricaoPrincipal}" onkeyup="javascript:validacaoAlfa(this);"/>
                        <rich:hotKey selector="#idDetalhePrincipal"
									key="return"
									handler="document.getElementById('btnPesquisaAvancadaDestinatario').onclick()" />
                    </td>
                </tr>
                <tr>
                    <td><h:outputText value="Detalhe PÛs " /></td>
                    <td>
                        <t:inputText id="idDetalhePos"
                                     size="60"
                                     value="#{beanDestinatario.bean.descricaoPosterior}" onkeyup="javascript:validacaoAlfa(this);"/>
                        <rich:hotKey selector="#idDetalhePos"
									key="return"
									handler="document.getElementById('btnPesquisaAvancadaDestinatario').onclick()" />
                    </td>
                </tr>
            </table>
        </ul>
    </rich:panel>
</h:panelGrid>

<h:panelGrid columns="1" id="painelPesquisaAvancadaEndereco">
    <rich:panel bodyClass="inpanelBody">
        <f:facet name="header">
            <b>ENDERE«O:</b>
        </f:facet>
        <ul>
            <table>
                <tr>
                    <td><h:outputText value="CEP " /></td>
                    <td colspan="3">
                        <t:inputText id="idCep"
                                     size="20"
                                     value="#{beanDestinatario.bean.cep}" onkeyup="javascript:validacaoNumero(this);"/>
                        <rich:hotKey selector="#idCep"
									key="return"
									handler="document.getElementById('btnPesquisaAvancadaDestinatario').onclick()" />
                    </td>                    
                </tr>
                <tr>
                    <td><h:outputText value="PaÌs" /></td>
                    <td colspan="3">
                        <t:inputText size="20" id="idPais" value="Brasil" readonly="true" />
                        <rich:hotKey selector="#idPais"
									key="return"
									handler="document.getElementById('btnPesquisaAvancadaDestinatario').onclick()" />
                    </td>
                </tr>
                
                 <tr>
                    <td><h:outputText value="UF" /></td>
                    <td colspan="3">
                    	<rich:comboBox id="idComboUF"
                                   directInputSuggestions="true"
                                   defaultLabel="Digite a UF"
                                   value="#{beanDestinatario.ufSelecionada}" 
                                   suggestionValues="#{beanDestinatario.listaUF}">
                            <a4j:support event="onselect"
                                         reRender="idComboCidade"
                                         actionListener="#{beanDestinatario.configurarMunicipio}"/> 
                        </rich:comboBox>
                        <rich:hotKey selector="#idComboUF"
									key="return"
									handler="document.getElementById('btnPesquisaAvancadaDestinatario').onclick()" />
                    </td>
                    <td><h:outputText value="Cidade" /></td>
                    <td>
                    	<rich:comboBox id="idComboCidade"
                                   value="#{beanDestinatario.bean.municipio}" 
                                   suggestionValues="#{beanDestinatario.listaCidade}"
                                   directInputSuggestions="true" 
                                   defaultLabel="Digite a Cidade" 
                                   converter="beanDestinatarioConverter"/>
                        <rich:hotKey selector="#idComboCidade"
									key="return"
									handler="document.getElementById('btnPesquisaAvancadaDestinatario').onclick()" />
                    </td>
                </tr>
                
                <tr>
                    <td><h:outputText value="Bairro " /></td>
                    <td colspan="5">
                        <t:inputText size="50" id="idBairro" value="#{beanDestinatario.bean.bairro}" onkeyup="javascript:validacaoAlfa(this);"/>
                        <rich:hotKey selector="#idBairro"
									key="return"
									handler="document.getElementById('btnPesquisaAvancadaDestinatario').onclick()" />
                    </td>
                </tr>
                <tr>
                    <td><h:outputText value="Logradouro" /></td>
                    <td colspan="5">
                        <t:inputText size="50" id="idLogradouro"
                                     value="#{beanDestinatario.bean.logradouro}" onkeyup="javascript:validacaoAlfa(this);"/>
                        <rich:hotKey selector="#idLogradouro"
									key="return"
									handler="document.getElementById('btnPesquisaAvancadaDestinatario').onclick()" />
                    </td>
                </tr>
                <tr>
                    <td><h:outputText value="Complemento " /></td>
                    <td colspan="5">
                        <t:inputText id="idComplemento"
                                     size="50"
                                     value="#{beanDestinatario.bean.complemento}" onkeyup="javascript:validacaoAlfa(this);"/>
                        <rich:hotKey selector="#idComplemento"
									key="return"
									handler="document.getElementById('btnPesquisaAvancadaDestinatario').onclick()" />
                    </td>
                    <td><h:outputText value="N˙mero " /></td>
                    <td colspan="3">
                        <t:inputText size="10" id="idNumero"
                                     value="#{beanDestinatario.bean.numero}" onkeyup="javascript:validacaoAlfa(this);"/>
                        <rich:hotKey selector="#idNumero"
									key="return"
									handler="document.getElementById('btnPesquisaAvancadaDestinatario').onclick()" />
                    </td>
                </tr>
                <tr>
                    <td><h:outputText value="Agrupador " /></td>
                    <td colspan="5">
                        <t:inputText size="50" id="idAgrupador"
                                     value="#{beanDestinatario.bean.agrupador}" onkeyup="javascript:validacaoAlfa(this);"/>
                        <rich:hotKey selector="#idAgrupador"
									key="return"
									handler="document.getElementById('btnPesquisaAvancadaDestinatario').onclick()" />
                    </td>
                    <td><h:outputText value="CÛd. ”rg„o " /></td>
                    <td colspan="3">
                        <t:inputText size="10" id="idCodOrgao"
                                     value="#{beanDestinatario.bean.codigoOrigem}" onkeyup="javascript:validacaoNumero(this);"/>
                        <rich:hotKey selector="#idCodOrgao"
									key="return"
									handler="document.getElementById('btnPesquisaAvancadaDestinatario').onclick()" />
                    </td>
                </tr>
                <tr>
                    <td><h:outputText value="Telefone " /></td>
                    <td>
                        <t:inputText size="3" id="idDDDTelefone"
                                     value="#{beanDestinatario.bean.codigoAreaTelefone}" onkeyup="javascript:validacaoNumero(this);"/>
                        <rich:hotKey selector="#idDDDTelefone"
									key="return"
									handler="document.getElementById('btnPesquisaAvancadaDestinatario').onclick()" />
                    </td>
                    <td>
                        <t:inputText size="15" id="idNumeroTelefone"
                                     value="#{beanDestinatario.bean.numeroTelefone}" onkeyup="javascript:validacaoNumero(this);"/>
                        <rich:hotKey selector="#idNumeroTelefone"
									key="return"
									handler="document.getElementById('btnPesquisaAvancadaDestinatario').onclick()" />
                    </td>
                    <td>
                        <h:outputText value="Fax " />
                    </td>
                    <td>
                        <t:inputText size="3" id="idDDDFax"
                                     value="#{beanDestinatario.bean.codigoAreaFax}" onkeyup="javascript:validacaoNumero(this);"/>
                        <rich:hotKey selector="#idDDDFax"
									key="return"
									handler="document.getElementById('btnPesquisaAvancadaDestinatario').onclick()" />
                    </td>
                    <td>
                        <t:inputText size="15" id="idNumeroFax"
                                     value="#{beanDestinatario.bean.numeroFax}" onkeyup="javascript:validacaoNumero(this);"/>
                        <rich:hotKey selector="#idNumeroFax"
									key="return"
									handler="document.getElementById('btnPesquisaAvancadaDestinatario').onclick()" />
                    </td>
                </tr>
                <tr>
                    <td><h:outputText value="E-mail " /></td>
                    <td colspan="5">
                        <t:inputText size="50" id="idEmail"
                                     value="#{beanDestinatario.bean.email}" onkeyup="javascript:validacaoAlfa(this);"/>
                        <rich:hotKey selector="#idEmail"
									key="return"
									handler="document.getElementById('btnPesquisaAvancadaDestinatario').onclick()" />
                                     <div id="msgEmailPesquisaAvancada"></div>
                    </td>
                    <td><h:outputText value="Contato " /></td>
                    <td colspan="3">
                        <t:inputText size="20" id="idContato"
                                     value="#{beanDestinatario.bean.nomeContato}" onkeyup="javascript:validacaoAlfa(this);"/>
                        <rich:hotKey selector="#idContato"
									key="return"
									handler="document.getElementById('btnPesquisaAvancadaDestinatario').onclick()" />
                    </td>
                </tr>
            </table>
        </ul>
    </rich:panel>

</h:panelGrid>