<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/bubble-tooltip.css" />
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/default.css" />
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/dtree.css" />
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/links.css" />
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/tree-view-0.1.0.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/default.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery-1.10.2.min.js"></script>

<script>
jq1_10_2 = jQuery.noConflict(true);
jq1_10_2( document ).ready(function() {
     jq1_10_2( "*" ).bind('keydown', 
        function(e) { 
            if(e.ctrlKey && e.keyCode == 74) {
				e.stopPropagation();
                e.preventDefault();
				return false;
            }
        }
    );
});
</script>

<script>
(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function()
{ (i[r].q=i[r].q||[]).push(arguments)}
,i[r].l=1*new Date();a=s.createElement(o),
m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
ga('create', 'UA-9066701-22', 'auto');
ga('send', 'pageview');
</script>