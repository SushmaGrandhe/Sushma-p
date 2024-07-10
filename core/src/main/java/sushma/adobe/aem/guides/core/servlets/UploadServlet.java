package sushma.adobe.aem.guides.core.servlets;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

@Component(service = Servlet.class, property = { "sling.servlet.paths=/bin/uploadexcel/ex" })
public class UploadServlet extends SlingAllMethodsServlet {
    private static final long serialVersionUID = 1L;

    private String getColumnAlphabet(int column) {
        StringBuilder columnName = new StringBuilder();
        while (column > 0) {
            int rem = column % 26;
            if (rem == 0) {
                columnName.append('Z');
                column = (column / 26) - 1;
            } else {
                columnName.append((char) ((rem - 1) + 'A'));
                column = column / 26;
            }
        }
        return columnName.reverse().toString();
    }

    String query = "SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([/conf/global/settings/dam/adminui-entension/metadataschema/portal-schema])";

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        try (InputStream inputStream = request.getRequestParameter("selectedfile").getInputStream();
             XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            Iterator<Row> rowIterator = sheet.iterator();
            if (!rowIterator.hasNext()) {
                response.getWriter().write("Excel file has no header row.");
                return;
            }
            Row headerRow = rowIterator.next();
            List<String> headerFields = new ArrayList<>();
            String[] multiFields = null;

            List<String> multiFieldslist = new ArrayList<>();
            
            
            
            for (Cell cell : headerRow) {
                String fieldValue = dataFormatter.formatCellValue(cell).trim();
                if (fieldValue.contains("[Multi]")) {
                    multiFields = fieldValue.split("\\[Multi\\]");
                    for (String multiField : multiFields) {
                        multiField = multiField.trim();
                        if (!multiField.isEmpty()) {
                            headerFields.add(multiField);
                           
                        }
                    }
                } else {
                    fieldValue = fieldValue.replace("[Multi]", "").trim();
                    if (!fieldValue.isEmpty()) {
                        headerFields.add(fieldValue);
                        multiFieldslist.add(fieldValue);
                       
                    }
                }
            }

            // Identify required fields based on a specific indicator (e.g., asterisk)
            List<String> requiredFields = new ArrayList<>();
            for (String field : headerFields) {
                if (field.contains("*")) {
                    requiredFields.add(field);
                }
            }
            
            
            if (requiredFields.isEmpty()) {
                response.getWriter().println("No required fields found in the Excel header.");
                return;
            }
            List<String> missingFields = new ArrayList<>();
            List<String> errorMessages = new ArrayList<>();
            // Process each row of data
            int rowNumber = 2; // Start from row 2 (assuming the header is in row 1)
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
               
                for (String requiredField : requiredFields) {
                    int fieldIndex = headerFields.indexOf(requiredField);
                    if (fieldIndex != -1) {
                        Cell cell = row.getCell(fieldIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        String cellValue = dataFormatter.formatCellValue(cell).trim();
                        if (cellValue.isEmpty()) {
                            missingFields.add("Column " + getColumnAlphabet(fieldIndex + 1) + " (" + requiredField + ")");
                        break;
                        }
                    } 
//                    else {
//                        missingFields.add("Required field " + requiredField + " not found.");
//                        
//                    }
                    
                    if (!missingFields.isEmpty()) {
                        errorMessages.add("Row " + rowNumber + ", " + String.join(", ", missingFields));
                    }
                }
                
                rowNumber++;
                break;
            }

            if (missingFields.isEmpty()) {
                response.getWriter().println("Got the data");
                executeQuery(  multiFieldslist ,headerFields, request,  response);
                

            } else {
                response.getWriter().println("Missing required fields:");
                response.getWriter().println("Row " + rowNumber + " missing required fields: " + String.join(", ", missingFields));
                response.getWriter().println("Please kindly enter the data :)");
            }

        } catch (IOException e) {
            e.printStackTrace();
            response.getWriter().write("Error processing Excel file: " + e.getMessage());
        }
    }


    private void executeQuery( List<String> multiFieldslist ,List<String> headerFields, SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        JsonArrayBuilder jab = Json.createArrayBuilder(); // Initialize JSON array builder
        Iterator<Resource> findResources = request.getResourceResolver().findResources(query, Query.JCR_SQL2);
        Map<String, String> propertiesMap = new HashMap<>();
        while (findResources.hasNext()) {
            Resource next = findResources.next();
            boolean hasMatchingLabel = false; // Flag to track if a matching label is found
            // Map to store property values

            // Get properties of the resource
            try {
                Node node = next.adaptTo(Node.class);
                if (node != null) {
                    Iterator<Property> properties = node.getProperties();
                    
                    while (properties.hasNext()) {
                        Property property = properties.next();
                        String propertyName = property.getName();

                        for (String headerField : headerFields) {
                            if (headerField.equals(property.getValue().getString())) {
                                hasMatchingLabel = true;
                                break; // Exit loop if a match is found
                            }
                        }

                       
                       
                    }

                    // Add properties map to JSON if there's a matching label
                    if (hasMatchingLabel) {
                    	addPropertyName(propertiesMap,node);
                        JsonObjectBuilder job = Json.createObjectBuilder();
                        for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
                            job.add(entry.getKey(), entry.getValue()); // Add key-value pairs to the JsonObjectBuilder
                            
                        }
                        jab.add(job.build()); // Add the JsonObject to the JsonArrayBuilder
                        
                    }
                }
            } catch (Exception e) {
                // Handle exception
                e.printStackTrace();
            }
        }
        response.getWriter().write(jab.build().toString()); // Write the JSON array to the response
        addproperties(  multiFieldslist ,propertiesMap,request, response);
        
    }

    private void addPropertyName(Map<String, String> propertiesMap, Node node) throws javax.jcr.RepositoryException {
        String Nameproperty = node.getProperty("name").getString();
        String fieldLabelproperty = node.getProperty("fieldLabel").getString();
        int lastSlashIndex = Nameproperty.lastIndexOf("/");
        if (lastSlashIndex != -1) {
            String extractedName = Nameproperty.substring(lastSlashIndex + 1);
            propertiesMap.put(extractedName,fieldLabelproperty);
        } else {
        	propertiesMap.put(Nameproperty,fieldLabelproperty);
        }
        
    }

    public void addproperties(List<String> multiFieldslist ,Map<String, String> propertiesMap, SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
    	 ResourceResolver resourceResolver = request.getResourceResolver();
         Session session = resourceResolver.adaptTo(Session.class);

         try {
             // Path to the node (resource) to be modified
             String path = "/content/dam/wknd/PORTAL_SCHEMA(1).xlsx/jcr:content/metadata";
             Resource resource = resourceResolver.getResource(path);
             
             if (resource != null) {
                 Node node = resource.adaptTo(Node.class);
                 if (node != null) {
                     // Adding or modifying properties using propertiesMap
                     ModifiableValueMap properties = resource.adaptTo(ModifiableValueMap.class);
                     for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
                         String key = entry.getKey();
                         String value = entry.getValue();

                         // Check if the key matches special conditions for array storage
                         if (!multiFieldslist.contains(value)) {
                             // Store the value directly as a String array with a single element
                             properties.put(key, new String[]{value});
                         } else {
                             // Store as a regular string
                             properties.put(key, value);
                         }
                     }
                     
                     session.save();
                     response.getWriter().write("Properties updated successfully.");
                 } else {
                     response.getWriter().write("Node not found.");
                 }
             } else {
                 response.getWriter().write("Resource not found.");
             }
         } catch (RepositoryException e) {
             response.getWriter().write("Error while updating properties: " + e.getMessage());
         } finally {
             if (session != null) {
                 session.logout();
             }
         }
    }
 
}