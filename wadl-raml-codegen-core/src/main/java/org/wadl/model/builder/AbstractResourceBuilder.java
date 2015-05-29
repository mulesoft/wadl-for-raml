package org.wadl.model.builder;

import java.util.ArrayList;
import java.util.Arrays;
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

        String pathStr = element.getAttribute("path");
        pathStr = Utils.refinePath(pathStr);
        while (pathStr.startsWith("/"))
        	pathStr = pathStr.substring("/".length());
        while (pathStr.endsWith("/"))
        	pathStr = pathStr.substring(0, pathStr.length() - "/".length());        
    
        ResourceTypeModel currentResource = resourceType;
       	List<String> pathSegments = new ArrayList<String>(Arrays.asList(pathStr.split("/")));

       	ResourceTypeModel previousResource = null;
       	for (int i = 0; i < pathSegments.size() ; i++){
       		String segment = "/" + pathSegments.get(i);   
       		if (i != 0)
       			currentResource = new ResourceModel();
       		currentResource.setPath(segment);
       		if (i != 0)
       			previousResource.addResource((ResourceModel) currentResource);
       		previousResource = currentResource;
       	}
        
        String idStr = element.getAttribute("id");
        if(!idStr.isEmpty()){
            resourceType.setId(idStr);
        }        
        
        MethodBuilder methodBuilder = getBuildManager().getBuilder(MethodBuilder.class);
        List<Element> methodElements = Utils.extractElements(element, "method");
        for(Element methodElement : methodElements){
			MethodModel method = methodBuilder.build(methodElement);
            currentResource.addMethod(method);
        }
        
        ResourceBuilder resourceBuilder = getBuildManager().getBuilder(ResourceBuilder.class);
        
        List<Element> resourceTypeElements = Utils.extractElements(element,"resource");
        for(Element resourceTypeElement : resourceTypeElements){
            
            ResourceModel res = resourceBuilder.build(resourceTypeElement);
            currentResource.addResource(res);
        }
        
        ParameterBuilder paramBuilder = getBuildManager().getBuilder(ParameterBuilder.class);
        List<Element> paramElements = Utils.extractElements(element, "param");
        for(Element paramElement : paramElements){            
			ParameterModel param = paramBuilder.build(paramElement);
            String style = param.getStyle();
            if("query".equals(style)){
                currentResource.addQueryParam(param);
            }
            else if("header".equals(style)){
                currentResource.addHeader(param);
            }
        }
    }
}
