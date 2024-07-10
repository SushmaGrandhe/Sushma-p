package sushma.adobe.aem.guides.core.models;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class)
public class SingleArticleModel {
    
    @Inject
    private Resource resource;

    public String getNodePath() {
        return resource.getPath();
    }
    
    @ValueMapValue
    private String cssclass;
    
    @ValueMapValue
    private String checkbox;

    String getResourcePath() {
        return resource.getPath();
    }
	
	public String getCssclass() {
		return cssclass;
	}

	public String getCheckbox() {
		return checkbox;
	}    
}