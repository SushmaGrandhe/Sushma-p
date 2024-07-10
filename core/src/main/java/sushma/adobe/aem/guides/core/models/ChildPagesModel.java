package sushma.adobe.aem.guides.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.adobe.cq.export.json.ExporterConstants;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = "yourapp/components/content/enpage")
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ChildPagesModel {
    @SlingObject
    private ResourceResolver resourceResolver;

    @Self
    private Resource resource;

    @ValueMapValue
    private String pathfield;

    public String getPathfield() {
        return pathfield;
    }

    public List<ChildPage> getChildPages() {
        if (pathfield == null || pathfield.isEmpty()) {
            return Collections.emptyList();
        }

        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        Page parentPage = pageManager.getPage(pathfield);

        if (parentPage == null) {
            return Collections.emptyList();
        }

        List<ChildPage> childPages = new ArrayList<>();
        Iterator<Page> childIterator = parentPage.listChildren();
        while (childIterator.hasNext()) {
            Page childPage = childIterator.next();
            childPages.add(new ChildPage(childPage.getPath(), childPage.getTitle()));
        }

        return childPages;
    }

    public static class ChildPage {
        private final String path;
        private final String title;

        public ChildPage(String path, String title) {
            this.path = path;
            this.title = title;
        }

        public String getPath() {
            return path;
        }

        public String getTitle() {
            return title;
        }
    }
}