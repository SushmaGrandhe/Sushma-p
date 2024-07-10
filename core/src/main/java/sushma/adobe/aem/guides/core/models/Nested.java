package sushma.adobe.aem.guides.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = { Resource.class },defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Nested {
	@ValueMapValue
	public String navigation;

	public String getNavigation() {
		return navigation;
	}
	
	
}
