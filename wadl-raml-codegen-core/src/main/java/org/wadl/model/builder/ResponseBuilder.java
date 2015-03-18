package org.wadl.model.builder;

import java.util.List;

import org.mulesoft.web.app.model.ParameterModel;
import org.mulesoft.web.app.model.DocumentationModel;
import org.mulesoft.web.app.model.RepresentationModel;
import org.mulesoft.web.app.model.ResponseModel;
import org.w3c.dom.Element;

public class ResponseBuilder {
    
    private DocumentationExtractor docExtractor = new DocumentationExtractor();
    
    private ParameterBuilder paramBuilder = new ParameterBuilder();
    
    private RepresentationBuilder representationBuilder = new RepresentationBuilder();
    
    public ResponseModel buildResponse(Element element){
        
        ResponseModel responseModel = new ResponseModel();
        DocumentationModel doc = docExtractor.extractDocumentation(element);
        responseModel.setDoc(doc);
        
        String status = element.getAttribute("status");
        responseModel.setStatus(status);
        
        List<Element> paramElements = Utils.extractElements(element, "param");
        for(Element paramElement : paramElements){
            ParameterModel param = paramBuilder.buildParameter(paramElement);
            String style = param.getStyle();
            if("header".equals(style)){
                responseModel.addHeader(param);
            }
        }
        
        List<Element> representationElements = Utils.extractElements(element, "representation");
        for(Element representationElement : representationElements){
            RepresentationModel representation = representationBuilder.buildRepresentation(representationElement);
            responseModel.addRepresentation(representation);
        }
        
        return responseModel;
    }

}
