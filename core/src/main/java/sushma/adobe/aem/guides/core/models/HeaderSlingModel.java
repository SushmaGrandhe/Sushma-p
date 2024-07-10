package sushma.adobe.aem.guides.core.models;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderSlingModel {
	@ValueMapValue
	public String pathfield;
	
	@ValueMapValue
	public String textfield;
	
	@ValueMapValue
	public String checkbox;

	public String getPathfield() {
		return pathfield;
	}

	public String getTextfield() {
		return textfield;
	}

	public String getCheckbox() {
		return checkbox;
	}
	
	@ChildResource
	public List<HeaderMultifield> multifield;

	public List<HeaderMultifield> getMultifield() {
		return multifield;
	}
	
	
}
