package sushma.adobe.aem.guides.core.models;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class JobsPromoSlingModel {
	@ValueMapValue
	public String title;
	
	public String getTitle() {
		return title;
	}

	@ChildResource
	public List<Multifield3> multifield3;

	public List<Multifield3> getMultifield3() {
		return multifield3;
	}
}
