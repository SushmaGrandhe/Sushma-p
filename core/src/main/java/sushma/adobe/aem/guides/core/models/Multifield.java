package sushma.adobe.aem.guides.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Multifield {
	@ValueMapValue
	public String textfields;
	
	@ValueMapValue
	public String pathfield;

	public String getTextFields() {
		return textfields;
	}

	public String getPathField() {
		return pathfield;
	}
}
