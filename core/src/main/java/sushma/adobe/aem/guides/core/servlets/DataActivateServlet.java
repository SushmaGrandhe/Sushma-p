package sushma.adobe.aem.guides.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import sushma.adobe.aem.guides.core.service.ServiceWriter;




@SuppressWarnings("serial")
@Component(service =  Servlet.class)
@SlingServletPaths(value = "/bin/users/data")
public class DataActivateServlet extends SlingSafeMethodsServlet{
	
	@Reference
	ServiceWriter writerData;
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		writerData.getDataWriter();
		response.getWriter().write("Users created Successfully");
	}
}