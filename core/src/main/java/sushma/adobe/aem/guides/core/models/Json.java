package sushma.adobe.aem.guides.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Json {

    @ValueMapValue
    @Default(values = "abc")
    private String text1;

    @ValueMapValue
    @Default(values = "xyz")
    private String text2;
    
    @ValueMapValue
    @Default(values = "mno")
    private String text3;

    @ValueMapValue
    @Default(values = "stu")
    private String text4;

    @ValueMapValue
    @Default(values = "pqr")
    private String text5;


    public String getText1() {
        return text1;
    }

    public String getText2() {
        return text2;
    }

    public String getText3() {
        return text3;
    }

    public String getText4() {
        return text4;
    }

    public String getText5() {
        return text5;
    }
}
