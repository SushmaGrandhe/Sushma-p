package sushma.adobe.aem.guides.core.models;


import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables={Resource.class,SlingHttpServletRequest.class},defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HelloWorldSlingModelEx {
	
	@ValueMapValue
	private String firstname;
	
	@ValueMapValue
	private String lastname;
	
	public String getFirstname() 
	{
		return firstname;
	}
	public String getLastname() 
	{
		return lastname;
	}
}
