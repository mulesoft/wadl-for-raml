package org.wadl.model.builder;

import java.util.List;
import java.util.Map;

import org.mulesoft.web.app.model.DocumentationModel;
import org.mulesoft.web.app.model.MethodModel;
import org.mulesoft.web.app.model.ParameterModel;
import org.mulesoft.web.app.model.ResourceModel;
import org.w3c.dom.Element;

public class ResourceBuilder {
    
    private DocumentationExtractor docExtractor = new DocumentationExtractor();
    
    private MethodBuilder methodBuilder = new MethodBuilder();
    
    private ParameterBuilder paramBuilder = new ParameterBuilder();
    
    public ResourceModel buildResource(Element element){
        
        ResourceModel resource = new ResourceModel();
        String idStr = element.getAttribute("id");
        if(!idStr.isEmpty()){
            resource.setId(idStr);
        }        
        
        String typeStr = element.getAttribute("type");
        if(!typeStr.isEmpty()){
            resource.setType(typeStr);
        }
        
        String pathStr = element.getAttribute("path");
        pathStr = Utils.refinePath(pathStr);
        if(!pathStr.isEmpty()){
            resource.setPath(pathStr);
        }
        
        DocumentationModel doc = docExtractor.extractDocumentation(element);
        resource.setDoc(doc);
        
        List<Element> methodElements = Utils.extractElements(element, "method");
        for(Element methodElement : methodElements){
            MethodModel method = methodBuilder.buildMethod(methodElement);
            resource.addMethod(method);
        }
        
        List<Element> resourceElements = Utils.extractElements(element,"resource");
        for(Element resourceElement : resourceElements){
            
            ResourceModel res = buildResource(resourceElement);
            resource.addResource(res);
        }
        
        List<Element> paramElements = Utils.extractElements(element, "param");
        for(Element paramElement : paramElements){
            ParameterModel param = paramBuilder.buildParameter(paramElement);
            String style = param.getStyle();
            if("query".equals(style)){
                resource.addQueryParam(param);
            }
            else if("header".equals(style)){
                resource.addHeader(param);
            }
        }
        
        return resource;
        
    }

}
