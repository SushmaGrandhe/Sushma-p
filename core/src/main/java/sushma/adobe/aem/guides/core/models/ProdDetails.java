package sushma.adobe.aem.guides.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProdDetails {

    @Inject
    private Resource resource;

    @SlingObject
    private SlingHttpServletRequest request;

    @ValueMapValue
    private String productName;

    @ValueMapValue
    private String productDescription;

    @ValueMapValue
    private String productPrice;

    @ValueMapValue
    private String productImage;

    // You can add more fields as needed, like SKU, category, etc.

    private String imagePath;

    @PostConstruct
    protected void init() {
        // Resolve the image path
        if (productImage != null && !productImage.isEmpty()) {
            Resource imageResource = resource.getResourceResolver().getResource(productImage);
            if (imageResource != null) {
                imagePath = imageResource.getPath();
            }
        }
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductImage() {
        return imagePath;
    }
}
