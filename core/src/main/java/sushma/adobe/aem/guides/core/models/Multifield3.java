package sushma.adobe.aem.guides.core.models;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Multifield3 {
	@ValueMapValue
	public String cardtitle;
	
	public String getCardTitle() {
		return cardtitle;
	}
	
	@ValueMapValue
	public String carddesc;
	
	public String getCardDesc() {
		return carddesc;
	}
}
