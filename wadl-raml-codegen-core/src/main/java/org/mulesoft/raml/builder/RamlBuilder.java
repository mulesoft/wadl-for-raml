package org.mulesoft.raml.builder;

import java.util.LinkedHashMap;

import org.mulesoft.web.app.model.ApplicationModel;
import org.mulesoft.web.app.model.ResourceModel;
import org.raml.model.Raml2;
import org.raml.model.Resource;

public class RamlBuilder {
    
    private RamlResourceBuilder resourceBuilder = new RamlResourceBuilder();
    
    public Raml2 buildRaml(ApplicationModel app){
        
        Raml2 raml = new Raml2();
        raml.setBaseUri(app.getBaseUri());
        raml.setTitle(app.getTitle());
        
        LinkedHashMap<String, ResourceModel> resources = app.getResources();
        for(ResourceModel resource: resources.values()){
            Resource ramlResource = resourceBuilder.buildResource(resource);            
            ramlResource.setParentUri("");
            String path = ramlResource.getUri();
            raml.getResources().put(path, ramlResource);
        }
        
        return raml;
    }

}