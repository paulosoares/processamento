<f:subview id="viewMessages">
	<a4j:outputPanel id="outputPanelMessages" ajaxRendered="true" 
		keepTransient="false">
		
		<t:panelGrid id="pnlMessages" forceId="true" 
			rendered="#{not empty facesContext.maximumSeverity}" 
			cellpadding="0" cellspacing="0" columns="1" 
			style="width: 100%; text-align: center;">
			
			<t:messages errorClass="ErrorMessage" style="text-align: left;" 
				infoClass="InfoMessage" warnClass="WarningMessage"
				showSummary="true" showDetail="true" layout="table"  />
		</t:panelGrid>

	</a4j:outputPanel>

</f:subview>