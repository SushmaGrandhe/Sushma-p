package sushma.adobe.aem.guides.core.servlets;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.osgi.service.component.annotations.Component;

import sushma.adobe.aem.guides.core.models.SingleArticleModel;

import org.osgi.framework.Constants;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = { Servlet.class }, property = {
        Constants.SERVICE_DESCRIPTION + "=Component Path Servlet",
        ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/yourServletPath/removeproprty",
        ServletResolverConstants.SLING_SERVLET_METHODS + "=POST"
})
public class ArticleServlet extends SlingAllMethodsServlet {

	
	 SingleArticleModel singleArticleModel;
	
	 @Override
		protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
		    // Get the component path from the request
		    String receivedComponentPath = request.getParameter("componentPath");
		    

		    // Obtain the ResourceResolver and adapt it to a Resource
		    ResourceResolver resourceResolver = request.getResourceResolver();
		    Resource resource = resourceResolver.getResource(receivedComponentPath);

		    if (resource != null) {
		        ModifiableValueMap properties = resource.adaptTo(ModifiableValueMap.class);
		        if (properties != null && properties.containsKey("cssclass")) {
		            // Remove the cssclass property
		            properties.remove("cssclass");
		            // Commit the changes
		            resourceResolver.commit();
		        } else {
		            response.getWriter().write("The cssclass property does not exist on the node.");
		        }
		    } else {
		        response.getWriter().write("The specified component path does not resolve to a resource.");
		    }
		}
}
