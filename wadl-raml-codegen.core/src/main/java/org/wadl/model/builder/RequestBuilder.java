package org.wadl.model.builder;

import java.util.List;

import org.mulesoft.web.app.model.ParameterModel;
import org.mulesoft.web.app.model.DocumentationModel;
import org.mulesoft.web.app.model.RepresentationModel;
import org.mulesoft.web.app.model.RequestModel;
import org.w3c.dom.Element;

public class RequestBuilder {
    
    private DocumentationExtractor docExtractor = new DocumentationExtractor();
    
    private ParameterBuilder paramBuilder = new ParameterBuilder();
    
    private RepresentationBuilder representationBuilder = new RepresentationBuilder();
    
    public RequestModel buildRequest(Element element){
        
        RequestModel requestModel = new RequestModel();
        DocumentationModel doc = docExtractor.extractDocumentation(element);
        requestModel.setDoc(doc);
        
        List<Element> paramElements = Utils.extractElements(element, "param");
        for(Element paramElement : paramElements){
            ParameterModel param = paramBuilder.buildParameter(paramElement);
            String style = param.getStyle();
            if("query".equals(style)){
                requestModel.addQueryParam(param);
            }
            else if("header".equals(style)){
                requestModel.addHeader(param);
            }
        }
        
        List<Element> representationElements = Utils.extractElements(element, "representation");
        for(Element representationElement : representationElements){
            RepresentationModel representation = representationBuilder.buildRepresentation(representationElement);
            requestModel.addRepresentation(representation);
        }
        
        return requestModel;
    }

}
