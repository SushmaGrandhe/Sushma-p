package sushma.adobe.aem.guides.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.framework.Constants;

import javax.servlet.Servlet;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("unused")
@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Text to Speech API Integration Servlet",
                "sling.servlet.methods=" + "POST",
                "sling.servlet.paths=" + "/bin/integrate/tts"
        })
public class TextToSpeechServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        // Hardcoded text for testing
        String text = "Hey its me, Suneetha";
        String voiceCode = "en-US-1";
        String speed = "1.00";
        String pitch = "1.00";
        String outputType = "audio_url";

        String apiUrl = "https://cloudlabs-text-to-speech.p.rapidapi.com/voices?language_code=en-US";
        String urlParameters = String.format("voice_code=%s&text=%s&speed=%s&pitch=%s&output_type=%s",
                voiceCode, text, speed, pitch, outputType);

        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("X-RapidAPI-Key", "b8609f80dcmsh9557877b2ba1c8cp1d2422jsnb8dea6c16469");
        connection.setRequestProperty("X-RapidAPI-Host", "cloudlabs-text-to-speech.p.rapidapi.com");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = urlParameters.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int status = connection.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            try (java.io.InputStream is = connection.getInputStream()) {
                String jsonResponse = new String(is.readAllBytes(), StandardCharsets.UTF_8);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonResponse);
            }
        } else {
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Failed to get response from TTS API.\"}");
        }
    }
}