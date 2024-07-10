package sushma.adobe.aem.guides.core.servlets;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
@SuppressWarnings("serial")
//@Component(service=Servlet.class,immediate=true,property= {"sling.servlet.paths=/bin/example/demo"})
@Component(service=Servlet.class,immediate=true,property= {"sling.servlet.resourceTypes=wknd/components/page"})
@SlingServletResourceTypes(resourceTypes="wknd/components/page",selectors="sample",extensions="json")
@SlingServletPaths(value="/bin/example/demo")
public class ServletExample extends SlingSafeMethodsServlet{
	public void doGet(SlingHttpServletRequest req,SlingHttpServletResponse res) throws IOException
	{
		res.setContentType("application/json");
		JsonObjectBuilder job=Json.createObjectBuilder();
		job.add("name","surge");
		job.add("url","www.google.com");
		job.add("location","banglore");
		//res.getWriter().write(job.build().toString());
		res.getWriter().println(job.build().toString());
	}
}
