package sushma.adobe.aem.guides.core.configuration;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "JsonOsgiConfigure",description = "Json Osgi Configuration Description")
public @interface OsgiConfig
{
	@AttributeDefinition(name = "url", description = "Url description")
	 public String getUrl() default "https://jsonplaceholder.typicode.com/users/1"; 
}