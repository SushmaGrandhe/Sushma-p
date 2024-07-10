package sushma.adobe.aem.guides.core.servlets;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
@SuppressWarnings({ "serial", "unused" })
@Component(service = Servlet.class)
@SlingServletPaths(value ="/bin/apps/demo")
public class Demo extends SlingAllMethodsServlet
{
	public void  doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException
	{
		 String parameter = request.getParameter("session");
	     if(parameter != null )
	     {
	    	 response.setContentType("text/plain");
//	         String randomWord = generateRandomWord(6);
//	         PrintWriter writer = response.getWriter();
//	         writer.write(randomWord);
	    	 response.getWriter().write(generateRandomWord(6));
	     }
	     else 
	     {
	        response.getWriter().write("no parameter");
	     }
	}
	private String generateRandomWord(int length)
	{
	  //String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
		StringBuilder randomWord = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++)
		{
			//int index = random.nextInt(allowedCharacters.length());
		    //randomWord.append(allowedCharacters.charAt(index));
			char randomchar=(char)('A'+random.nextInt(26));
			randomWord.append(randomchar);
		}
		return randomWord.toString();
	}
}
