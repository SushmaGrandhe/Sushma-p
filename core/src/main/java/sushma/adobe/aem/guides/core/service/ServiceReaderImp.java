package sushma.adobe.aem.guides.core.service;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;



@Component(immediate = true)
public class ServiceReaderImp implements ServiceReader{
	
	private static final Logger LOG=LoggerFactory.getLogger(ServiceReaderImp.class);

	@Override
	public JsonArray getReader() {
		HttpClient client=HttpClientBuilder.create().build();
		HttpGet request=new HttpGet("https://gorest.co.in/public/v2/users");
		JsonArray userData=null;
		
		try {
			HttpResponse response=client.execute(request);
			if(response.getStatusLine().getStatusCode()==200) {
				HttpEntity entity=response.getEntity();
				String str=EntityUtils.toString(entity);
				userData=new Gson().fromJson(str, JsonArray.class);
				LOG.info("UserData"+userData);
			}
		} catch (ClientProtocolException e) {
			LOG.error("Error while getting the Data to  the rest api url",e);
		} catch (IOException e) {
			LOG.error("Error while getting data",e);
		}
		
		return userData;
	}
	
	@Activate
	public void activate() {
		getReader();
	}

}