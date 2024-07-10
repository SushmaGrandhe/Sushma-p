package sushma.adobe.aem.guides.core.models;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Sidebarslingmodel{
	@Self
    private Resource resource;

    private ValueMap properties;

	@ValueMapValue
	public String logopathfield;
	
	@ValueMapValue
	public String logomobileimage;
	
	@ValueMapValue
	public String logolink;
	
	@ValueMapValue
	public String checkbox;
	
	@ValueMapValue
	public String country;
	
	@PostConstruct
    protected void init() {
        properties = resource.getValueMap();
        // Initialize other fields as needed
    }
	
	public String getLogopath() {
		return logopathfield;
	}

	public String getLogomobileimage() {
		return logomobileimage;
	}

	public String getLogolink() {
		return logolink;
	}

	public String getCheckbox() {
		return checkbox;
	}
	
	public String getCountry() {
		return country;
	}
	
	@ChildResource
    private List<Multifield1> Day1;

    public List<Multifield1> getDay1() {
		return Day1;
	}
    
    @ChildResource
    private List<Multifield2> Day2;

    public List<Multifield2> getDay2() {
		return Day2;
	}

}
