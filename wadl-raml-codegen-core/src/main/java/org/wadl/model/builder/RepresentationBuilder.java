package org.wadl.model.builder;

import java.util.List;

import org.mulesoft.web.app.model.ParameterModel;
import org.mulesoft.web.app.model.RepresentationModel;
import org.w3c.dom.Element;

public class RepresentationBuilder extends AbstractBuilder<RepresentationModel> {
    
    public RepresentationBuilder(Class<RepresentationModel> modelClass) {
		super(modelClass);
		// TODO Auto-generated constructor stub
	}

	public void fillModel(RepresentationModel representation, Element element) throws Exception{
        
        String mediaType = element.getAttribute("mediaType");
        representation.setMediaType(mediaType);
        
        String elementName = element.getAttribute("element");
    	if(elementName.contains(":")){
    		elementName = elementName.substring(elementName.lastIndexOf(":")+1);
    	}
		String schemaName = elementName;            
        representation.setSchema(schemaName);
        
        ParameterBuilder paramBuilder = getBuildManager().getBuilder(ParameterBuilder.class);
        List<Element> paramElements = Utils.extractElements(element, "param");
        for(Element paramElement : paramElements){
            ParameterModel param = paramBuilder.build(paramElement);
            String style = param.getStyle();
            if("queryr".equals(style)){
                representation.addFormParameter(param);
            }
        }
    }

}
