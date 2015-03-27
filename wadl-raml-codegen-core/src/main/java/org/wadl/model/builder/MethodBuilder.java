package org.wadl.model.builder;

import java.util.List;

import org.mulesoft.web.app.model.MethodModel;
import org.mulesoft.web.app.model.RequestModel;
import org.mulesoft.web.app.model.ResponseModel;
import org.w3c.dom.Element;

public class MethodBuilder extends AbstractBuilder<MethodModel> {
    
    public MethodBuilder(Class<MethodModel> modelClass) {
		super(modelClass);
	}
    
    public void fillModel(MethodModel method, Element element) throws Exception{
        
        extractDocumentation(element, method);
        
        String typeStr = element.getAttribute("name");
        if(!typeStr.isEmpty()){
            method.setName(typeStr);
        }
        
        RequestBuilder requestBuilder = getBuildManager().getBuilder(RequestBuilder.class);        
        List<Element> requestElements = Utils.extractElements(element, "request");
        for(Element requestElement : requestElements){
            RequestModel request = requestBuilder.build(requestElement);
            method.addRequest(request);
        }
        
        ResponseBuilder responseBuilder = getBuildManager().getBuilder(ResponseBuilder.class);
        List<Element> responseElements = Utils.extractElements(element, "response");
        for(Element responseElement : responseElements){
            ResponseModel response = responseBuilder.build(responseElement);
            method.addResponse(response);
        }
    }

}
