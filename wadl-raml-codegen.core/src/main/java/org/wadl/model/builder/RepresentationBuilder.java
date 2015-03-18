package org.wadl.model.builder;

import java.util.List;

import org.mulesoft.web.app.model.ParameterModel;
import org.mulesoft.web.app.model.RepresentationModel;
import org.w3c.dom.Element;

public class RepresentationBuilder {
    
    private ParameterBuilder paramBuilder = new ParameterBuilder();
    
    public RepresentationModel buildRepresentation(Element element){
        
        RepresentationModel representation = new RepresentationModel();
        String mediaType = element.getAttribute("mediaType");
        representation.setMediaType(mediaType);
        
        String id = element.getAttribute("id");
        representation.setId(id);
        
        List<Element> paramElements = Utils.extractElements(element, "param");
        for(Element paramElement : paramElements){
            ParameterModel param = paramBuilder.buildParameter(paramElement);
            String style = param.getStyle();
            if("queryr".equals(style)){
                representation.addFormParameter(param);
            }
        }
        
        
        return representation;
    }

}
