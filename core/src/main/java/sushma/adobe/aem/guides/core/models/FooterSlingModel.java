package sushma.adobe.aem.guides.core.models;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterSlingModel {
	@ValueMapValue
	public String title1;
	
	@ValueMapValue
	public String title2;
	
	@ValueMapValue
	public String title3;
	@ValueMapValue
	public String description;

	public String getTitle1() {
		return title1;
	}

	public String getTitle2() {
		return title2;
	}

	public String getTitle3() {
		return title3;
	}
	
	public String getDescription() {
		return description;
	}
	
	@ChildResource
	public List<QuickLinks> quicklinks;

	public List<QuickLinks> getQuickLinks() {
		return quicklinks;
	}
	
	@ChildResource
	public List<SocialLinks> sociallinks;

	public List<SocialLinks> getSocialLinks() {
		return sociallinks;
	}
}
