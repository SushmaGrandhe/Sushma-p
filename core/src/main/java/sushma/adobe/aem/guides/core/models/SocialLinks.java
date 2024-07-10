package sushma.adobe.aem.guides.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SocialLinks {
	@ValueMapValue
	public String icon;
	
	@ValueMapValue
	public String navigationpath;
	
	public String getIcon() {
		return icon;
	}

	public String getNavigationPath() {
		return navigationpath;
	}
}
