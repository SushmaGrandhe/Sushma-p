package sushma.adobe.aem.guides.core.workflow;

import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component(service = WorkflowProcess.class, property = {
        "process.label=Extract Pages Process"
})
public class ExtractPagesProcess implements WorkflowProcess {

    private static final Logger log = LoggerFactory.getLogger(ExtractPagesProcess.class);

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
            throws WorkflowException {
        ResourceResolver resourceResolver = null;
        try {
            // Obtain a ResourceResolver using the service user
            Map<String, Object> authInfo = Map.of(
                    ResourceResolverFactory.SUBSERVICE, "datawrite");
            resourceResolver = resolverFactory.getServiceResourceResolver(authInfo);

            String payloadPath = workItem.getWorkflowData().getPayload().toString();
            Resource payloadResource = resourceResolver.getResource(payloadPath);

            if (payloadResource != null) {
                List<String> pagesToPublish = new ArrayList<>();
                Iterator<Resource> childResources = payloadResource.listChildren();

                while (childResources.hasNext()) {
                    Resource childResource = childResources.next();
                    pagesToPublish.add(childResource.getPath());
                }

                workItem.getWorkflowData().getMetaDataMap().put("pagesToPublish", pagesToPublish);
                log.info("Extracted pages: {}", pagesToPublish);
            }
        } catch (Exception e) {
            log.error("Error extracting pages from payload", e);
        } finally {
            if (resourceResolver != null) {
                resourceResolver.close();
            }
        }
    }
}