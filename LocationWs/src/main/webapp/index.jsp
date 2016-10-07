<%@ page import="java.io.*,java.util.*"%>
<%@ page import="net.virtela.config.ServerConfig"%>
<%@ page import="net.virtela.config.SwaggerConfig"%>
<html>
<head>
<title>MyAppWs</title>
</head>
<body style="margin: 0px; padding: 0px; overflow: hidden">
	<iframe
		src="http://<%=ServerConfig.getHost() + SwaggerConfig.getSwaggerPort()%>/<%=SwaggerConfig.NAME%>"
		frameborder="0" style="overflow: hidden; height: 100%; width: 100%"
		height="100%" width="100%"></iframe>
</body>
</html>