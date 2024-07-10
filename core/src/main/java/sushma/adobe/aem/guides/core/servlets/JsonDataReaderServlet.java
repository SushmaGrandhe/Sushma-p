package sushma.adobe.aem.guides.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import sushma.adobe.aem.guides.core.service.ServiceReader;

@SuppressWarnings("serial")
@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/json/details")
public class JsonDataReaderServlet extends SlingSafeMethodsServlet
{
	@SuppressWarnings("unused")
	private static final Logger LOG=LoggerFactory.getLogger(JsonDataReaderServlet.class);
	@Reference
	ServiceReader reader;
	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException
	{
      JsonArray	userData=reader.getReader();
      
      String id=request.getParameter("id");
      String status=request.getParameter("status");
      
      response.setContentType("application/json");
      JsonObject user=null;
      if(StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(status)) {
    	  for(int i=0; i < userData.size(); i++) {
    		  JsonObject currentData=userData.get(i).getAsJsonObject();
    		  if(id.equals(currentData.get("id").getAsString()) &&
    				  status.equals(currentData.get("status").getAsString())) {
    			  user=currentData;
    		  }
    	  }
    	  if(user !=null) {
    		  response.getWriter().write(user.toString());
    	  }else {
    		  response.getWriter().write("No Employee available with the details given");
    	  }
    	  
      }else {
    	  response.getWriter().write("Required Parameters are not fully Provided...");      }
	}
}