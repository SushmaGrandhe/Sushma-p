package sushma.adobe.aem.guides.core.servlets;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.model.WorkflowModel;

@Component(service=Servlet.class,immediate=true,
property= {
		"sling.servlet.paths=/bin/demo/exampleservlet"
})
public class WorkflowServlet extends SlingAllMethodsServlet
{
	public void doGet(SlingHttpServletRequest req,SlingHttpServletResponse res)
	{
		String status="";
		try {
			res.setContentType("text/plain");
			ResourceResolver resolver = req.getResourceResolver();
		    WorkflowSession workflowSession = resolver.adaptTo(WorkflowSession.class);
		    WorkflowModel model=workflowSession.getModel("/var/workflow/models/page-version-creation");
		    WorkflowData payload = workflowSession.newWorkflowData("JCR_PATH","/content/wknd/us");
		    status=workflowSession.startWorkflow(model,payload).getState();
		    res.getWriter().write("Workflow Exexcuted Successfully...{}"+status);
		}
		catch(Exception e){
			try {
				res.getWriter().write("workflow failed");
			}
			catch(Exception f)
			{
				f.printStackTrace();
			}
		}
	}
}