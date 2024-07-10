package sushma.adobe.aem.guides.core.service;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = JsonDataService.class,immediate = true )
public class JsonDataServiceImpl implements JsonDataService {
	private static final Logger LOG=LoggerFactory.getLogger(JsonDataServiceImpl.class);
	@Override
	public JSONObject getJsonUrl() {
		JSONObject jsonData=null;
		 try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
	            HttpGet request = new HttpGet("https://jsonplaceholder.typicode.com/users/1");
	            HttpResponse response = httpClient.execute(request);
	            String jsonString = EntityUtils.toString(response.getEntity());
	            JSONObject json = new JSONObject(jsonString);
	            jsonData = json;
	            LOG.info("JsonData "+jsonData);
	        } catch (IOException  e) { 
	            LOG.error("Errror while fetching the data",e);
	        } catch (JSONException e) {
	        	LOG.error("Errror while fetching the data",e);
			}
		
		return jsonData;
	}
	
//	@Activate
//	public void activate() {
//		getJsonUrl();
//	}
//	
}