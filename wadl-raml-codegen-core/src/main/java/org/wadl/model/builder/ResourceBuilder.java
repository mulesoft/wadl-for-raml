package org.wadl.model.builder;

import java.util.List;
import org.mulesoft.web.app.model.MethodModel;
import org.mulesoft.web.app.model.ParameterModel;
import org.mulesoft.web.app.model.ResourceModel;
import org.w3c.dom.Element;

public class ResourceBuilder extends AbstractBuilder<ResourceModel> {
    
    public ResourceBuilder(Class<ResourceModel> modelClass) {
		super(modelClass);
	}

	public void fillModel(ResourceModel resource,Element element) throws Exception{
        
        extractDocumentation(element, resource);
        
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
        
        MethodBuilder methodBuilder = getBuildManager().getBuilder(MethodBuilder.class);
        List<Element> methodElements = Utils.extractElements(element, "method");
        for(Element methodElement : methodElements){
			MethodModel method = methodBuilder.build(methodElement);
            resource.addMethod(method);
        }
        
        List<Element> resourceElements = Utils.extractElements(element,"resource");
        for(Element resourceElement : resourceElements){
            
            ResourceModel res = build(resourceElement);
            resource.addResource(res);
        }
        
        ParameterBuilder paramBuilder = getBuildManager().getBuilder(ParameterBuilder.class);
        List<Element> paramElements = Utils.extractElements(element, "param");
        for(Element paramElement : paramElements){            
			ParameterModel param = paramBuilder.build(paramElement);
            String style = param.getStyle();
            if("query".equals(style)){
                resource.addQueryParam(param);
            }
            else if("header".equals(style)){
                resource.addHeader(param);
            }
        }
        
    }

}
