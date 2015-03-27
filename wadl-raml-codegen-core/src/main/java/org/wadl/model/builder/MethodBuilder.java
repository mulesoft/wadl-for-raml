package org.wadl.model.builder;

import java.util.List;
import java.util.Map;

import org.mulesoft.web.app.model.DocumentationModel;
import org.mulesoft.web.app.model.MethodModel;
import org.mulesoft.web.app.model.RequestModel;
import org.mulesoft.web.app.model.ResponseModel;
import org.w3c.dom.Element;

public class MethodBuilder {
    
    private DocumentationExtractor docExtractor = new DocumentationExtractor();
    
    private ResponseBuilder responseBuilder = new ResponseBuilder();
    
    private RequestBuilder requestBuilder = new RequestBuilder();
    
    public MethodModel buildMethod(Element element){
        
        MethodModel method = new MethodModel();
        
        DocumentationModel doc = docExtractor.extractDocumentation(element);
        method.setDoc(doc);
        
        String idStr = element.getAttribute("id");
        if(!idStr.isEmpty()){
            method.setId(idStr);
        }        
        
        String typeStr = element.getAttribute("name");
        if(!typeStr.isEmpty()){
            method.setName(typeStr);
        }
        
        List<Element> requestElements = Utils.extractElements(element, "request");
        for(Element requestElement : requestElements){
            RequestModel request = requestBuilder.buildRequest(requestElement);
            method.addRequest(request);
        }
        
        List<Element> responseElements = Utils.extractElements(element, "response");
        for(Element responseElement : responseElements){
            ResponseModel response = responseBuilder.buildResponse(responseElement);
            method.addResponse(response);
        }
        
        return method;
    }

}
