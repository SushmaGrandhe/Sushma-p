package sushma.adobe.aem.guides.core.servlets;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//Servlet code to handle form submission and generate PDF
@WebServlet("/generate-pdf")
public class GeneratePDFServlet extends HttpServlet {
 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     // Generate the PDF
     // Code to generate PDF goes here

     // Set response content type
     response.setContentType("application/pdf");
     
     // Set the content-disposition header to force download the PDF
     response.setHeader("Content-Disposition", "attachment; filename=generated.pdf");
     
     // Write the PDF content to the response output stream
     OutputStream out = response.getOutputStream();
     // Code to write PDF content to output stream goes here
     out.close();
 }
}
