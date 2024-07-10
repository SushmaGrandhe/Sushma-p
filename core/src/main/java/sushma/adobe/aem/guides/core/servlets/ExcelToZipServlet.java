package sushma.adobe.aem.guides.core.servlets;

import org.apache.jackrabbit.vault.fs.api.PathFilterSet;
import org.apache.jackrabbit.vault.fs.config.DefaultWorkspaceFilter;
import org.apache.jackrabbit.vault.fs.io.ImportOptions;
import org.apache.jackrabbit.vault.packaging.JcrPackage;
import org.apache.jackrabbit.vault.packaging.JcrPackageDefinition;
import org.apache.jackrabbit.vault.packaging.JcrPackageManager;
import org.apache.jackrabbit.vault.packaging.PackageException;
import org.apache.jackrabbit.vault.packaging.Packaging;
import org.apache.jackrabbit.vault.util.DefaultProgressListener;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("serial")
@Component(
        service = {Servlet.class},
        property = {
                ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/zip/excelzip",
                ServletResolverConstants.SLING_SERVLET_METHODS + "=POST"
        }
)
public class ExcelToZipServlet extends SlingAllMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelToZipServlet.class);

    @Reference
    private Packaging packaging;

    private static final String VERSION_NODE_PATH = "/etc/packages/versionNode";
    private static final String VERSION_PROPERTY = "currentVersion";

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        Collection<Part> fileParts = request.getParts();

        if (fileParts.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("No files uploaded");
            return;
        }

        List<String> assetPaths = new ArrayList<>();

        try {
            for (Part filePart : fileParts) {
                if (filePart.getSize() == 0) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Uploaded file is empty");
                    return;
                }

                try (InputStream inputStream = filePart.getInputStream();
                     Workbook workbook = new XSSFWorkbook(inputStream)) {
                    Sheet sheet = workbook.getSheetAt(0);

                    for (Row row : sheet) {
                        Cell firstCell = row.getCell(0);
                        if (firstCell != null) {
                            String assetPath = firstCell.getStringCellValue();
                            assetPaths.add(assetPath);
                        }
                    }
                }
            }

            Session session = request.getResourceResolver().adaptTo(Session.class);
            if (session == null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Session not available");
                return;
            }

            JcrPackage jcrPackage = null;
            try {
                JcrPackageManager packageManager = packaging.getPackageManager(session);
                String packageName = "my-package-" + UUID.randomUUID();
                double packageVersion = getNextVersion(session);

                jcrPackage = packageManager.create("my-packages", packageName, String.valueOf(packageVersion));
                if (jcrPackage == null) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("Package creation failed");
                    return;
                }

                DefaultWorkspaceFilter filter = new DefaultWorkspaceFilter();
                for (String path : assetPaths) {
                    PathFilterSet filterSet = new PathFilterSet(path);
                    filter.add(filterSet);
                }

                JcrPackageDefinition packageDefinition = jcrPackage.getDefinition();
                if (packageDefinition != null) {
                    packageDefinition.setFilter(filter, true);

                    ImportOptions opts = new ImportOptions();
                    packageManager.assemble(jcrPackage, new DefaultProgressListener());
                }

                jcrPackage = packageManager.open(jcrPackage.getNode());
                if (jcrPackage == null || jcrPackage.getData() == null) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("Package reopening failed");
                    return;
                }

                try (InputStream inputStream = jcrPackage.getData().getBinary().getStream();
                     OutputStream outputStream = response.getOutputStream()) {
                    response.setContentType("application/zip");
                    response.setHeader("Content-Disposition", "attachment; filename=my-package-" + packageVersion + ".zip");

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    outputStream.flush();
                }

            } catch (RepositoryException | IOException | PackageException e) {
                LOGGER.error("Error while creating or downloading the package", e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error while creating or downloading the package: " + e.getMessage());
            } finally {
                if (jcrPackage != null) {
                    jcrPackage.close();
                }
                session.logout();
            }
        } catch (Exception e) {
            LOGGER.error("Error processing uploaded file", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error processing uploaded file: " + e.getMessage());
        }
    }

    private double getNextVersion(Session session) throws RepositoryException {
        Node versionNode;
        if (session.nodeExists(VERSION_NODE_PATH)) {
            versionNode = session.getNode(VERSION_NODE_PATH);
        } else {
            versionNode = session.getRootNode().addNode(VERSION_NODE_PATH.substring(1), "nt:unstructured");
        }

        double currentVersion = versionNode.hasProperty(VERSION_PROPERTY) ? versionNode.getProperty(VERSION_PROPERTY).getDouble() : 0.0;
        double nextVersion = currentVersion + 0.1;
        versionNode.setProperty(VERSION_PROPERTY, nextVersion);
        session.save();

        return nextVersion;
    }
}