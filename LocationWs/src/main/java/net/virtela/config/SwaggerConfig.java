package net.virtela.config;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.wordnik.swagger.jaxrs.config.BeanConfig;

public class SwaggerConfig extends HttpServlet {

	private static final long serialVersionUID = -818386386982397665L;
	
	public static final String NAME = "myapp-api-docs";
	
	private static final String PORT_APACHE = ":80";
	private static final String PORT_TOMCAT = ":8080";

	@Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.1.0");
        beanConfig.setTitle("MyAppWs");
        beanConfig.setDescription("Sample Application for Rest MyBatis");
        beanConfig.setContact("Richard Reyles<rreyles@virtela.net>");
       
        beanConfig.setHost(ServerConfig.getHost() + ":" + ServerConfig.WS_PORT);
        beanConfig.setBasePath(ServerConfig.BASE_PATH);
        
        beanConfig.setResourcePackage("net.virtela.rest.api");
        beanConfig.setScan(true);
        beanConfig.setPrettyPrint(true);
    }
	
	public static String getSwaggerPort() {
		if (ServerConfig.isDevMode()) {
			return PORT_TOMCAT;
		}
		return PORT_APACHE;
	}
    
}