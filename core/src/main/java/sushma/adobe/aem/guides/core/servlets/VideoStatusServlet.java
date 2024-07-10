package sushma.adobe.aem.guides.core.servlets;


import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import sushma.adobe.aem.guides.core.service.VideoUploadService;



@SuppressWarnings("serial")
@Component(service = Servlet.class)
@SlingServletPaths(value = { "/bin/video/status" })
public class VideoStatusServlet extends SlingAllMethodsServlet {

    @Reference
    private VideoUploadService videoUploadService;

    public VideoStatusServlet(VideoUploadService videoUploadService) {
        this.videoUploadService = videoUploadService;
    }

    public void setVideoUploadService(VideoUploadService videoUploadService) {
    	this.videoUploadService = videoUploadService;
        }
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String videoPath = request.getParameter("path");
        if (videoPath != null && !videoPath.isEmpty()) {
            String status = videoUploadService.getVideoStatus(videoPath);
            response.getWriter().write("Video status: " + status);
        } else {
            response.getWriter().write("Video path not provided");
        }
    }
}