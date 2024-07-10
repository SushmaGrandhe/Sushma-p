package sushma.adobe.aem.guides.core.servlets;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.metatype.annotations.Designate;

import sushma.adobe.aem.guides.core.configuration.OsgiConfig;



@SuppressWarnings({ "serial", "unused" })
@Component(service = Servlet.class,immediate = true,
    property = {
        "sling.servlet.paths=/bin/ConfigServlet"
//        "service.description=My Servlet",
//        "service.vendor=Adobe AEM Guides"
    }
)
@Designate(ocd = OsgiConfig.class)
public class ServletTask extends SlingAllMethodsServlet{
	  private String url;
      @Activate
	  protected void activate(OsgiConfig config) {
    	  url = config.getUrl();
      }
      

	protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse resp) throws ServletException, IOException {
    	  resp.setContentType("text/html");
    	  resp.getWriter().write("Google :"+"<a href='" + url + "'>" + url + "</a>");
//    	  PrintWriter out = resp.getWriter();
//	      out.println("<html><body>");
//	      out.println("<a href='" + url + "'>" + url + "</a>");
//	      out.println("</body></html>");
	  }
}
