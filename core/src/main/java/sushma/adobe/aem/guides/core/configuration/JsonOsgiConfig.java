package sushma.adobe.aem.guides.core.configuration;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = JsonOsgiConfigMethod.class,immediate = true)
@Designate(ocd = OsgiConfig.class)
public class JsonOsgiConfig implements JsonOsgiConfigMethod
{
	private String url;
	public static final Logger log=LoggerFactory.getLogger(JsonOsgiConfig.class);
	@Activate
	public void activate(OsgiConfig config)
	{
		url = config.getUrl();
	}
	@Override
	public String getUrl() 
	{
		return url;
	}
}
