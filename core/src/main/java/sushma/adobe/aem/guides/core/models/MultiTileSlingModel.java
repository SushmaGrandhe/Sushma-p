package sushma.adobe.aem.guides.core.models;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MultiTileSlingModel {
	@ValueMapValue
	public String title;

	public String getTitle() {
		return title;
	}
	
	@ChildResource
	public List<Multifield> multifield;

	public List<Multifield> getMultifield() {
		return multifield;
	}
	
	@ChildResource
	public List<Multifields> multifields;

	public List<Multifields> getMultifields() {
		return multifields;
	}
}
