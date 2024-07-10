package sushma.adobe.aem.guides.core.workflow;


import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.Route;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.replication.Replicator;
import com.day.cq.replication.ReplicationOptions;
import com.day.cq.replication.ReplicationActionType;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.LoginException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session; // Make sure this import is used
import java.util.List;
import java.util.Map;

@Component(service = WorkflowProcess.class, property = {
        "process.label=Sequential Page Publish Process"
})
public class SequentialPagePublishProcess implements WorkflowProcess {

    private static final Logger log = LoggerFactory.getLogger(SequentialPagePublishProcess.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private Replicator replicator;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
            throws WorkflowException {
        ResourceResolver resourceResolver = null;
        try {
            Map<String, Object> authInfo = Map.of(ResourceResolverFactory.SUBSERVICE, "datawrite");
            resourceResolver = resolverFactory.getServiceResourceResolver(authInfo);
            PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
            String payloadPath = workItem.getWorkflowData().getPayload().toString();
            Page page = pageManager.getPage(payloadPath);

            if (page != null) {
                // Logic to publish the page
                publishPage(page, resourceResolver);

                // Logic to trigger the next workflow step
                triggerNextStep(workItem, workflowSession);
            }
        } catch (LoginException e) {
            log.error("LoginException while obtaining ResourceResolver: ", e);
        } finally {
            if (resourceResolver != null) {
                resourceResolver.close();
            }
        }
    }

    private void publishPage(Page page, ResourceResolver resourceResolver) {
        try {
            ReplicationOptions replicationOptions = new ReplicationOptions();
            replicationOptions.setSuppressStatusUpdate(false);
            replicationOptions.setSuppressVersions(false);

            replicator.replicate(resourceResolver.adaptTo(Session.class), ReplicationActionType.ACTIVATE,
                    page.getPath(), replicationOptions);
            log.info("Published page: {}", page.getPath());
        } catch (Exception e) {
            log.error("Error while publishing page: {}", page.getPath(), e);
        }
    }

    private void triggerNextStep(WorkItem workItem, WorkflowSession workflowSession) {
        try {
            List<Route> routes = workflowSession.getRoutes(workItem, false);
            if (routes != null && !routes.isEmpty()) {
                workflowSession.complete(workItem, routes.get(0)); // Proceed to the next step using the first available
                                                                   // route
                log.info("Triggered next step for work item: {}", workItem.getId());
            } else {
                log.warn("No routes available to proceed to the next step for work item: {}", workItem.getId());
            }
        } catch (Exception e) {
            log.error("Error while triggering next step for work item: {}", workItem.getId(), e);
        }
    }
}