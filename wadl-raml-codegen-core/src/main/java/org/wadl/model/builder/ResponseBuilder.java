package org.wadl.model.builder;

import java.util.List;

import org.mulesoft.web.app.model.ParameterModel;
import org.mulesoft.web.app.model.RepresentationModel;
import org.mulesoft.web.app.model.ResponseModel;
import org.w3c.dom.Element;

public class ResponseBuilder extends AbstractBuilder<ResponseModel> {
    
    public ResponseBuilder(Class<ResponseModel> modelClass) {
		super(modelClass);
	}

	public void fillModel(ResponseModel responseModel, Element element) throws Exception{
        
        extractDocumentation(element, responseModel);
        
        String status = element.getAttribute("status");
        responseModel.setStatus(status);
        
        List<Element> paramElements = Utils.extractElements(element, "param");
        ParameterBuilder paramBuilder = getBuildManager().getBuilder(ParameterBuilder.class);
        for(Element paramElement : paramElements){            
			ParameterModel param = paramBuilder.build(paramElement);
            String style = param.getStyle();
            if("header".equals(style)){
                responseModel.addHeader(param);
            }
        }
        
        RepresentationBuilder representationBuilder = getBuildManager().getBuilder(RepresentationBuilder.class);
        List<Element> representationElements = Utils.extractElements(element, "representation");
        for(Element representationElement : representationElements){
			RepresentationModel representation = representationBuilder.build(representationElement);
            responseModel.addRepresentation(representation);
        }
    }

}
