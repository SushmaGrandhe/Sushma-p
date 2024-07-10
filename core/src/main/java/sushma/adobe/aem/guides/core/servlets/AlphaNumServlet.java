package sushma.adobe.aem.guides.core.servlets;

import java.io.IOException;
import java.util.Random;
import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
@Component(service = Servlet.class,immediate=true,
property= {"sling.servlet.paths=/bin/example/demo"
})
@SuppressWarnings({ "serial" })
public class AlphaNumServlet extends SlingAllMethodsServlet {
    public void doGet(SlingHttpServletRequest req,SlingHttpServletResponse resp) throws IOException {
        String parameter = req.getParameter("session");
        if (parameter!= null)
        {	
           if (parameter.equals("digits"))
            {
                resp.getWriter().write(generateRandomDigits(6));           
            } 
            else if (parameter.equals("alphabets"))
            {
            	resp.getWriter().write(generateRandomWord(6));
            } 
            else if (parameter.equals("mix")) 
            {
            	resp.getWriter().write(generateRandomMix(6));
            } 
            else 
            {
                resp.getWriter().write("Wrong parameter");
            }
        }
        else 
        {
        	resp.getWriter().write("No parameter");
        } 
    }
    
    private String generateRandomWord(int length)
    {
    	StringBuilder randomWord = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++)
		{
			char randomchar=(char)('A'+random.nextInt(26));
			randomWord.append(randomchar);
		}
		return randomWord.toString();
    }
    
    private String generateRandomDigits(int length) 
    {
    	StringBuilder randomWord = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++)
		{
			char randomchar=(char)('0'+random.nextInt(9));
			randomWord.append(randomchar);
		}
		return randomWord.toString();
    }
    
    private String generateRandomMix(int length) 
    {
    	StringBuilder randomWord = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++)
		{
			char randomChar;
			if(i<3)
			{
				randomChar=(char)('A'+random.nextInt(26));
			}
			else
			{
				randomChar=(char)('0'+random.nextInt(9));
			}
			randomWord.append(randomChar);
		}
		return randomWord.toString();
    }
}