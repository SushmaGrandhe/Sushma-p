package sushma.adobe.aem.guides.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import javax.jcr.Session;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;


@SuppressWarnings("serial")
@Component(service=Servlet.class,immediate=true,
property= {
		"sling.servlet.paths=/bin/example/ase"
})
public class QueryBuilderServlet extends SlingAllMethodsServlet{
	@Reference
	QueryBuilder queryBuilder;
	public void doGet(SlingHttpServletRequest req,SlingHttpServletResponse res) throws IOException
	{
		res.setContentType("application/json");
		Map<String,String> map=new HashMap<String,String>();
		map.put("type", "cq:Page");
		map.put("path", "/content/wknd");
	    map.put("orderby", "@jcr:content/jcr:created");
	    map.put("orderby.sort", "desc");
	    map.put("p.limit", "-1");
		Query query = queryBuilder.createQuery(PredicateGroup.create(map), req.getResourceResolver().adaptTo(Session.class));
	    SearchResult result = query.getResult();
	    List<Hit> hits = result.getHits();
	    JsonArrayBuilder jab = Json.createArrayBuilder();
	    Iterator<Hit> iterator = hits.iterator();
	    while(iterator.hasNext())
	    {
	    	try {
		        Hit next=iterator.next();
		        JsonObjectBuilder object = Json.createObjectBuilder();
				object.add("Title", next.getTitle().toString());
				object.add("Path", next.getPath().toString());
				jab.add(object);
			}
	    	catch (Exception e) {
				//res.getWriter().write("Getting error{}"+e1.getMessage());
	    		e.printStackTrace();
			}
	    }
	    res.getWriter().write(jab.build().toString());
//		for(Hit hit:hits)
//		{
//			try
//			{
//				Resource resource=hit.getResource();
//				JsonObjectBuilder obj = Json.createObjectBuilder();
//				obj.add("Name", resource.getName());
//				obj.add("Path", resource.getPath());
//			    jab.add(obj);
//			}
//			catch (Exception e) 
//			{
//				e.printStackTrace();			}
//		}
//		res.getWriter().write(jab.build().toString());   
	}
}
