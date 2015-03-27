package org.wadl.model.builder;

import java.util.List;

import org.mulesoft.web.app.model.ParameterModel;
import org.mulesoft.web.app.model.RepresentationModel;
import org.mulesoft.web.app.model.RequestModel;
import org.w3c.dom.Element;

public class RequestBuilder extends AbstractBuilder<RequestModel> {
    
    public RequestBuilder(Class<RequestModel> modelClass) {
		super(modelClass);
	}

	public void fillModel(RequestModel requestModel,Element element) throws Exception{
        
        extractDocumentation(element, requestModel);
        
        ParameterBuilder paramBuilder = getBuildManager().getBuilder(ParameterBuilder.class);
        List<Element> paramElements = Utils.extractElements(element, "param");
        for(Element paramElement : paramElements){
			ParameterModel param = paramBuilder.build(paramElement);
            String style = param.getStyle();
            if("query".equals(style)){
                requestModel.addQueryParam(param);
            }
            else if("header".equals(style)){
                requestModel.addHeader(param);
            }
        }
        RepresentationBuilder representationBuilder = getBuildManager().getBuilder(RepresentationBuilder.class);
        List<Element> representationElements = Utils.extractElements(element, "representation");
        for(Element representationElement : representationElements){
			RepresentationModel representation = representationBuilder.build(representationElement);
            requestModel.addRepresentation(representation);
        }
    }

}
