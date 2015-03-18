package org.wadl.model.builder;

import java.util.List;

import org.mulesoft.web.app.model.ApplicationModel;
import org.mulesoft.web.app.model.DocumentationModel;
import org.mulesoft.web.app.model.ResourceModel;
import org.mulesoft.web.app.model.ResourceOwner;
import org.w3c.dom.Element;

public class ApplicationBuilder {
    
    DocumentationExtractor docExtractor = new DocumentationExtractor();
    
    ResourceBuilder resourceBuilder = new ResourceBuilder();
    
    public ApplicationModel buildApplication(Element element){
        
        ApplicationModel app = new ApplicationModel();
        
        
        DocumentationModel doc = docExtractor.extractDocumentation(element);
        app.setDoc(doc);
        
        List<Element> resourcesElements = Utils.extractElements(element, "resources");
        for(Element resourcesElement : resourcesElements){
            
            String baseUri = resourcesElement.getAttribute("base");
            if(app.getBaseUri()==null){
                app.setBaseUri(baseUri);
            }
            
            List<Element> resourceElements = Utils.extractElements(resourcesElement,"resource");
            for(Element resourceElement : resourceElements){
                
                ResourceModel res = resourceBuilder.buildResource(resourceElement);
                Utils.setParentUri(res,"");
                app.addResource(res);
            }
        }
        
        
        
        return app;
    }

}
