<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<h:panelGrid id="painelPesquisa">
	<rich:panel bodyClass="inpanelBody">
		<f:facet name="header">
			<b>PESQUISA:</b>
		</f:facet>
		<t:inputText id="idCampoPesquisa" size="60" value="#{beanDestinatario.campoPesquisa}" />
		<rich:hotKey selector="#idCampoPesquisa"
					key="return"
					handler="document.getElementById('btnPesquisaSimplesDestinatario').onclick()" />
	</rich:panel>
</h:panelGrid>