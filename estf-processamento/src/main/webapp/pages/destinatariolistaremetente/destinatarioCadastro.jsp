<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<script type="text/javascript">
    function validarCampos() {

        if (document.getElementById('cadastro:idDetalhePrincipalDestinatario').value == '') {
            alert('Favor preencher os campos obrigatórios.');
            return false;
        }

        if (document.getElementById('cadastro:idCepDestinatario').value == '') {
            alert('Favor preencher os campos obrigatórios.');
            return false;
        }

        if (document
                .getElementById('cadastro:idComboUFDestinatariocomboboxValue').value == '') {
            alert('Favor preencher os campos obrigatórios.');
            return false;
        }

        if (document
                .getElementById('cadastro:idComboCidadeDestinatariocomboboxValue').value == '') {
            alert('Favor preencher os campos obrigatórios.');
            return false;
        }

        if (document.getElementById('cadastro:idLogradouroDestinatario').value == '') {
            alert('Favor preencher os campos obrigatórios.');
            return false;
        }

        if (document.getElementById('cadastro:idNumeroDestinatario').value == '') {
            alert('Favor preencher os campos obrigatórios.');
            return false;
        }

        if (document.getElementById('cadastro:idBairro').value == '') {
            alert('Favor preencher os campos obrigatórios.');
            return false;
        }

        return true;
    }

    function validacaoNumero(campo) {
        var digitos = "0123456789"
        var campoTemp

        if (campo.id == 'cadastro:idCepDestinatario') {
            if (campo.value == '') {
                document.getElementById("msgCEP").innerHTML = "";
            }
        }

        for (var i = 0; i < campo.value.length; i++) {
            campoTemp = campo.value.substring(i, i + 1)

            if (campo.id == 'cadastro:idCepDestinatario') {
                if (i > 7) {
                    campo.value = campo.value.substring(0, 8);
                }

                if (campo.value.length < 8) {
                    document.getElementById("msgCEP").innerHTML = "<font color='red'>campo incompleto</font>";
                } else {
                    document.getElementById("msgCEP").innerHTML = "<font color='green'>ok</font>";
                }
            }

            if (campo.id == 'cadastro:idDDDTelefoneDestinatario') {
                if (i > 1) {
                    campo.value = campo.value.substring(0, 2);
                }
            }

            if (campo.id == 'cadastro:idNumeroTelefoneDestinatario') {
                if (i > 9) {
                    campo.value = campo.value.substring(0, 10);
                }
            }

            if (campo.id == 'cadastro:idDDDFaxDestinatario') {
                if (i > 1) {
                    campo.value = campo.value.substring(0, 2);
                }
            }

            if (campo.id == 'cadastro:idNumeroFaxDestinatario') {
                if (i > 9) {
                    campo.value = campo.value.substring(0, 10);
                }
            }

            if (campo.id == 'cadastro:idCodOrgaoDestinatario') {
                if (i > 3) {
                    campo.value = campo.value.substring(0, 4);
                }
            }

            if (digitos.indexOf(campoTemp) == -1) {
                campo.value = campo.value.substring(0, i);
            }
        }
    }

    function validacaoAlfa(campo) {
        for (var i = 0; i < campo.value.length; i++) {
            if (campo.id == 'cadastro:idNumeroDestinatario') {
                if (i > 4) {
                    campo.value = campo.value.substring(0, 5);
                }
            }

            if (campo.id == 'cadastro:idDetalhePreDestinatario') {
                if (i > 99) {
                    campo.value = campo.value.substring(0, 100);
                }
            }

            if (campo.id == 'cadastro:idDetalhePrincipalDestinatario') {
                if (i > 199) {
                    campo.value = campo.value.substring(0, 200);
                }
            }

            if (campo.id == 'cadastro:idDetalhePosDestinatario') {
                if (i > 99) {
                    campo.value = campo.value.substring(0, 100);
                }
            }

            if (campo.id == 'cadastro:idBairro') {
                if (i > 199) {
                    campo.value = campo.value.substring(0, 200);
                }
            }

            if (campo.id == 'cadastro:idLogradouroDestinatario') {
                if (i > 249) {
                    campo.value = campo.value.substring(0, 250);
                }
            }

            if (campo.id == 'cadastro:idComplementoDestinatario') {
                if (i > 99) {
                    campo.value = campo.value.substring(0, 100);
                }
            }

            if (campo.id == 'cadastro:idAgrupadorDestinatario') {
                if (i > 9) {
                    campo.value = campo.value.substring(0, 20);
                }
            }

            if (campo.id == 'cadastro:idEmailDestinatario') {
                if (i > 49) {
                    usuario = campo.value.substring(0, campo.value.indexOf("@"));
                    dominio = campo.value.substring(campo.value.indexOf("@") + 1,
                            campo.value.length);
                    if ((usuario.length >= 1) && (dominio.length >= 3)
                            && (usuario.search("@") == -1)
                            && (dominio.search("@") == -1)
                            && (usuario.search(" ") == -1)
                            && (dominio.search(" ") == -1)
                            && (dominio.search(".") != -1)
                            && (dominio.indexOf(".") >= 1)
                            && (dominio.lastIndexOf(".") < dominio.length - 1)) {
                        document.getElementById("msgEmailCadastro").innerHTML = "";
                    } else {
                        document.getElementById("msgEmailCadastro").innerHTML = "<font color='red'>e-mail inválido</font>";
                    }
                    campo.value = campo.value.substring(0, 50);
                }
            }

            if (campo.id == 'cadastro:idContatoDestinatario') {
                if (i > 49) {
                    campo.value = campo.value.substring(0, 50);
                }
            }
        }
    }

    function verificarCEP() {
        var campo = document.getElementById("idCepDestinatario").value;
        alert(campo);
        if (campo == '') {
            document.getElementById("msgCEP").innerHTML = "É necessário informar o CEP";
        }
    }
</script>

<f:subview id="viewMessages1">
    <a4j:outputPanel id="outputPanelMessages1" ajaxRendered="true"
                     keepTransient="false">

        <t:panelGrid id="pnlMessages1" forceId="true"
                     rendered="#{not empty facesContext.maximumSeverity}" cellpadding="0"
                     cellspacing="0" columns="1" style="width: 100%; text-align: center;">

            <t:messages errorClass="ErrorMessage" style="text-align: left"
                        infoClass="InfoMessage" warnClass="WarningMessage"
                        showSummary="true" showDetail="true" layout="table" />
        </t:panelGrid>

    </a4j:outputPanel>

</f:subview>

<h:panelGrid columns="1" id="painelCadastroDestinatario">
    <rich:panel bodyClass="inpanelBody">
        <f:facet name="header">
            <b>DESTINATÁRIO:</b>
        </f:facet>
        <ul>
            <table>
                <tr>
                    <td><h:outputText value="Detalhe Pré " /></td>
                    <td><t:inputText size="60" id="idDetalhePreDestinatario"
                                 value="#{beanDestinatario.bean.descricaoAnterior}"
                                 onkeyup="javascript:validacaoAlfa(this);"
                                 disabled="#{not beanDestinatario.visualizacao}" /></td>
                </tr>
                <tr>
                    <td><h:outputText value="Destinatário* " /></td>
                    <td><h:inputText size="60" id="idDetalhePrincipalDestinatario"
                                 value="#{beanDestinatario.bean.descricaoPrincipal}"
                                 onkeyup="javascript:validacaoAlfa(this);"
                                 disabled="#{not beanDestinatario.visualizacao}" /></td>
                </tr>
                <tr>
                    <td><h:outputText value="Detalhe Pós " /></td>
                    <td><t:inputText size="60" id="idDetalhePosDestinatario"
                                 value="#{beanDestinatario.bean.descricaoPosterior}"
                                 onkeyup="javascript:validacaoAlfa(this);"
                                 disabled="#{not beanDestinatario.visualizacao}" /></td>
                </tr>
            </table>
        </ul>
    </rich:panel>
</h:panelGrid>

<h:panelGrid columns="1" id="painelCadastroEndereco">
    <rich:panel bodyClass="inpanelBody">
        <f:facet name="header">
            <b>ENDEREÇO:</b>
        </f:facet>
        <ul>
            <table>
                <tr>
                    <td>
                        <h:outputText value="CEP* " />
                    </td>
                    <td colspan="4">
                        <h:inputText id="idCepDestinatario"
                                     size="20"
                                     onkeyup="javascript:validacaoNumero(this);"
                                     value="#{beanDestinatario.bean.cep}"
                                     disabled="#{not beanDestinatario.visualizacao}" />
                        <div id="msgCEP"></div>
                    </td>
                    <td>
                        <a4j:commandButton id="btnPesquisarEnderecoCEP"
                                           styleClass="BotaoPadraoEstendido"
                                           value="Pesquisar CEP"
                                           reRender="painelCadastroEndereco,idPanelExibirModalPesquisaCepPorEndereco"
                                           process="idCepDestinatario"
                                           actionListener="#{beanDestinatario.pesquisarCEP}"
                                           rendered="#{beanDestinatario.visualizacao}">
                            <f:setPropertyActionListener value="#{beanDestinatario}" target="#{beanPesquisaCepEndereco.selecionaCep}" />
                        </a4j:commandButton>
                        <h:panelGroup id="idPanelExibirModalPesquisaCepPorEndereco">
                            <h:panelGroup rendered="#{beanDestinatario.flagExibirModalPesquisaCep}">
                                <script type="text/javascript">
                                    Richfaces.showModalPanel('idModalPanelPesquisaCepPorEndereco')
                                </script>
                            </h:panelGroup>
                        </h:panelGroup>
                    </td>
                </tr>
                <tr>
                    <td><h:outputText value="País* " /></td>
                    <td colspan="3">
                        <t:inputText id="idPaisDestinatario"
                                     size="20"
                                     value="Brasil"
                                     disabled="true" />
                    </td>
                </tr>
                <tr>
                    <td><h:outputText value="UF* " /></td>
                    <td colspan="3">
                        <rich:comboBox id="idComboUFDestinatario"
                                       value="#{beanDestinatario.ufSelecionada}"
                                       suggestionValues="#{beanDestinatario.listaUF}"
                                       directInputSuggestions="true" defaultLabel="Digite a UF"
                                       disabled="#{not beanDestinatario.visualizacao}">
                            <a4j:support event="onselect" actionListener="#{beanDestinatario.configurarMunicipio}" reRender="idComboCidadeDestinatario" />
                        </rich:comboBox>
                    </td>
                    <td><h:outputText value="Cidade* " /></td>
                    <td>
                        <rich:comboBox id="idComboCidadeDestinatario"
                                       value="#{beanDestinatario.bean.municipio}"
                                       suggestionValues="#{beanDestinatario.listaCidade}"
                                       directInputSuggestions="true" defaultLabel="Digite a Cidade"
                                       converter="beanDestinatarioConverter"
                                       disabled="#{not beanDestinatario.visualizacao}" />
                    </td>
                </tr>
                <tr>
                    <td><h:outputText value="Bairro* " /></td>
                    <td colspan="5">
                        <t:inputText id="idBairro"
                                     size="50"
                                     onkeyup="javascript:validacaoAlfa(this);"
                                     value="#{beanDestinatario.bean.bairro}"
                                     disabled="#{not beanDestinatario.visualizacao}" />
                    </td>
                </tr>
                <tr>
                    <td><h:outputText value="Logradouro* " /></td>
                    <td colspan="5">
                        <h:inputText id="idLogradouroDestinatario"
                                     size="50"
                                     onkeyup="javascript:validacaoAlfa(this);"
                                     value="#{beanDestinatario.bean.logradouro}"
                                     disabled="#{not beanDestinatario.visualizacao}" />
                    </td>
                </tr>
                <tr>
                    <td><h:outputText value="Complemento " /></td>
                    <td colspan="5"><t:inputText size="50"
                                 id="idComplementoDestinatario"
                                 value="#{beanDestinatario.bean.complemento}"
                                 onkeyup="javascript:validacaoAlfa(this);"
                                 disabled="#{not beanDestinatario.visualizacao}" /></td>
                    <td><h:outputText value="Número* " /></td>
                    <td colspan="3"><t:inputText size="10"
                                 id="idNumeroDestinatario" value="#{beanDestinatario.bean.numero}"
                                 onkeyup="javascript:validacaoAlfa(this);"
                                 disabled="#{not beanDestinatario.visualizacao}" /></td>
                </tr>
                <tr>
                    <td><h:outputText value="Agrupador " /></td>
                    <td colspan="5"><t:inputText size="50"
                                 id="idAgrupadorDestinatario"
                                 value="#{beanDestinatario.bean.agrupador}"
                                 onkeyup="javascript:validacaoAlfa(this);"
                                 disabled="#{not beanDestinatario.visualizacao}" /></td>
                    <td><h:outputText value="Cód. Órgão " /></td>
                    <td colspan="3"><t:inputText size="10"
                                 id="idCodOrgaoDestinatario"
                                 value="#{beanDestinatario.bean.codigoOrigem}" maxlength="4"
                                 onkeyup="javascript:validacaoNumero(this);"
                                 disabled="#{not beanDestinatario.visualizacao}" /></td>
                </tr>
                <tr>
                    <td><h:outputText value="Telefone " /></td>
                    <td><t:inputText size="3" id="idDDDTelefoneDestinatario"
                                 value="#{beanDestinatario.bean.codigoAreaTelefone}"
                                 onkeyup="javascript:validacaoNumero(this);"
                                 disabled="#{not beanDestinatario.visualizacao}" /></td>
                    <td><t:inputText size="15" id="idNumeroTelefoneDestinatario"
                                 value="#{beanDestinatario.bean.numeroTelefone}"
                                 onkeyup="javascript:validacaoNumero(this);"
                                 disabled="#{not beanDestinatario.visualizacao}" /></td>
                    <td><h:outputText value="Fax " /></td>
                    <td><t:inputText size="3" id="idDDDFaxDestinatario"
                                 value="#{beanDestinatario.bean.codigoAreaFax}"
                                 onkeyup="javascript:validacaoNumero(this);"
                                 disabled="#{not beanDestinatario.visualizacao}" /></td>
                    <td><t:inputText size="15" id="idNumeroFaxDestinatario"
                                 value="#{beanDestinatario.bean.numeroFax}"
                                 onkeyup="javascript:validacaoNumero(this);"
                                 disabled="#{not beanDestinatario.visualizacao}" /></td>
                </tr>
                <tr>
                    <td><h:outputText value="E-mail " /></td>
                    <td colspan="5"><t:inputText size="50"
                                 id="idEmailDestinatario" value="#{beanDestinatario.bean.email}"
                                 onkeyup="javascript:validacaoAlfa(this);"
                                 disabled="#{not beanDestinatario.visualizacao}" />
                        <div id="msgEmailCadastro"></div></td>
                    <td><h:outputText value="Contato " /></td>
                    <td colspan="3"><t:inputText size="20"
                                 id="idContatoDestinatario"
                                 value="#{beanDestinatario.bean.nomeContato}"
                                 onkeyup="javascript:validacaoAlfa(this);"
                                 disabled="#{not beanDestinatario.visualizacao}" /></td>
                </tr>
            </table>
        </ul>
    </rich:panel>
    <rich:panel bodyClass="inpanelBody">
        <table>
            <tr>
                <td><h:outputText value="Observação " /></td>
                <td>
                    <t:inputTextarea id="idObservacaoDestinatario"
                                     value="#{beanDestinatario.bean.observacao}"
                                     onkeyup="javascript:validacaoAlfa(this);"
                                     disabled="#{not beanDestinatario.visualizacao}" 
                                     cols="90"
                                     rows="4"/>
                </td>
            </tr>
        </table>
    </rich:panel>	
    <rich:panel bodyClass="inpanelBody">
        <table>
            <tr>
                <td><h:panelGrid rendered="#{beanDestinatario.visualizacao}">
                        <a4j:commandButton id="btnSalvar"
                                           styleClass="BotaoPadraoEstendido"
                                           value="Salvar"
                                           oncomplete="#{beanDestinatario.completarCadastro}"
                                           onclick="if(!validarCampos()){return;}else{Richfaces.showModalPanel('idPnlDestinatarioCadastro');}"
                                           actionListener="#{beanDestinatario.salvar}"
                                           rendered="#{beanDestinatario.modoCadastro || beanDestinatario.copia}" />
                        <a4j:commandButton id="btnAlterar"
                                           styleClass="BotaoPadraoEstendido"
                                           value="Alterar"
                                           oncomplete="#{beanDestinatario.completarCadastro}"
                                           onclick="if(!validarCampos()){return;}else{Richfaces.showModalPanel('idPnlDestinatarioCadastro');}"
                                           actionListener="#{beanDestinatario.alterar}" 
                                           rendered="#{not beanDestinatario.modoCadastro && not beanDestinatario.copia}" />
                    </h:panelGrid></td>
                <td><a4j:commandButton styleClass="BotaoPadrao" value="Fechar"
                                   oncomplete="Richfaces.hideModalPanel('idPnlDestinatarioCadastro')"
                                   actionListener="#{beanDestinatario.voltarCamposVazios}"
                                   reRender="tbPesquisaDestinatario" immediate="true" /></td>
            </tr>
        </table>
    </rich:panel>
</h:panelGrid>