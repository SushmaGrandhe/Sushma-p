package sushma.adobe.aem.guides.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

@Component(service = Servlet.class,property= {
		Constants.SERVICE_DESCRIPTION + "=Image Viewer Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.paths=" + "/bin/demoServlet"
})
public class DemoServlet extends SlingAllMethodsServlet{
	private static final long serialVersionUID=2598426539166789515L;
	@Override
	protected void doGet(final SlingHttpServletRequest request,final SlingHttpServletResponse response) throws IOException{
		final PrintWriter out=response.getWriter();
		try {
			final QueryManager queryManager= getQueryManager(request);
			out.println("No specific order:");
			printNodes(out,executeQuery(queryManager,"SELECT * FROM [cq:Page] AS page WHERE ISDESCENDANTNODE([/content/we-retail])"));
			out.println();
			
			out.println("Order by name");
			printNodes(out,executeQuery(queryManager,"SELECT * FROM [cq:Page] AS page WHERE ISDESCENDANTNODE([/content/we-retail]) ORDER BY NAME(page)"));
		}
		catch(final Exception e) {
			out.println("Error during execution:");
			out.println(e);
			Arrays.stream(e.getStackTrace()).forEach(out::println);
		}
	}
	private QueryManager getQueryManager(final SlingHttpServletRequest request) throws RepositoryException{
		return request.getResourceResolver().adaptTo(Session.class).getWorkspace().getQueryManager();
	}
	private void printNodes(final PrintWriter out,final QueryResult result) throws RepositoryException{
		final NodeIterator nodes = result.getNodes();
		while(nodes.hasNext()) {
			out.println("Path=>" + nodes.nextNode().getPath());
		}
	}
	private QueryResult executeQuery(final QueryManager queryManager,final String queryString) throws RepositoryException{
		final Query query=queryManager.createQuery(queryString, "JCR-SQL2");
		query.setLimit(4);
		return query.execute();
	}
}
