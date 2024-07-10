package sushma.adobe.aem.guides.core.models;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CarouselMultiField {
	@ValueMapValue
	public String text;
	
	@ValueMapValue
	public String path;
	
	public String getText() {
		return text;
	}

	public String getPath() {
		return path;
	}
	
	@ChildResource
	public List <CarouselNestedMultiField> nestedmultifield;

	public List<CarouselNestedMultiField> getNestedmultifield() {
		return nestedmultifield;
	}
	
}
