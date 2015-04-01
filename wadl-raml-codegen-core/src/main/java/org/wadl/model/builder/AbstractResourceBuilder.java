package org.wadl.model.builder;

import java.util.List;

import org.mulesoft.web.app.model.MethodModel;
import org.mulesoft.web.app.model.ParameterModel;
import org.mulesoft.web.app.model.ResourceModel;
import org.mulesoft.web.app.model.ResourceTypeModel;
import org.w3c.dom.Element;

public class AbstractResourceBuilder<T extends ResourceTypeModel> extends AbstractBuilder<T> {
    
    public AbstractResourceBuilder(Class<T> modelClass) {
		super(modelClass);
	}

	void fillModel(T resourceType,Element element) throws Exception{
        
        extractDocumentation(element, resourceType);
        
        String idStr = element.getAttribute("id");
        if(!idStr.isEmpty()){
            resourceType.setId(idStr);
        }        
           
        String pathStr = element.getAttribute("path");
        pathStr = Utils.refinePath(pathStr);
        if(!pathStr.isEmpty()){
            resourceType.setPath(pathStr);
        }
        
        MethodBuilder methodBuilder = getBuildManager().getBuilder(MethodBuilder.class);
        List<Element> methodElements = Utils.extractElements(element, "method");
        for(Element methodElement : methodElements){
			MethodModel method = methodBuilder.build(methodElement);
            resourceType.addMethod(method);
        }
        
        ResourceBuilder resourceBuilder = getBuildManager().getBuilder(ResourceBuilder.class);
        
        List<Element> resourceTypeElements = Utils.extractElements(element,"resource");
        for(Element resourceTypeElement : resourceTypeElements){
            
            ResourceModel res = resourceBuilder.build(resourceTypeElement);
            resourceType.addResource(res);
        }
        
        ParameterBuilder paramBuilder = getBuildManager().getBuilder(ParameterBuilder.class);
        List<Element> paramElements = Utils.extractElements(element, "param");
        for(Element paramElement : paramElements){            
			ParameterModel param = paramBuilder.build(paramElement);
            String style = param.getStyle();
            if("query".equals(style)){
                resourceType.addQueryParam(param);
            }
            else if("header".equals(style)){
                resourceType.addHeader(param);
            }
        }
        
    }

}
