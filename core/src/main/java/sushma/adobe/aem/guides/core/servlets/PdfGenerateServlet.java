package sushma.adobe.aem.guides.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import com.day.cq.dam.api.AssetManager;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("serial")
@Component(service = Servlet.class)
@SlingServletPaths(value = "/bin/mypdf/expdf") 
public class PdfGenerateServlet extends SlingAllMethodsServlet{

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        try (InputStream pdfStream = request.getInputStream()) {
            if(pdfStream != null) {
                String currentPageUrl = request.getHeader("Referer");

                if (currentPageUrl != null && currentPageUrl.contains("/content/")) {
                    String[] urlParts = currentPageUrl.split("/");
                    String damPath = "/content/dam" + currentPageUrl.substring(currentPageUrl.indexOf("/content/") + 8, currentPageUrl.lastIndexOf("/"));
                    String lastPart = urlParts[urlParts.length - 1].replaceAll("\\.html", "");

                    // Get the AssetManager from the SlingHttpServletRequest
                    AssetManager assetManager = request.getResourceResolver().adaptTo(AssetManager.class);

                    // Create a unique name for the PDF to avoid conflicts
                    String pdfName = lastPart + ".pdf";

                    // Use the AssetManager to create the asset
                    assetManager.createAsset(damPath + "/" + pdfName, pdfStream, "application/pdf", true);

                    response.getWriter().write("PDF saved to DAM as: " + pdfName);
                } else {
                    response.getWriter().write("Invalid URL or DAM path not found.");
                }
            } else {
                response.getWriter().write("No PDF received");
            }
        } catch (Exception e) {
            response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}