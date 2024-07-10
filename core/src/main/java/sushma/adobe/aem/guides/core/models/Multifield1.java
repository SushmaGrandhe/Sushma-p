package sushma.adobe.aem.guides.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Multifield1 {
	@ValueMapValue
	public String text;
	
	@ValueMapValue
	public String image;

	public String getName() {
		return text;
	}

	public String getImage() {
		return image;
	}
	
	
}