package org.wadl.model.builder;

import java.util.List;

import org.mulesoft.web.app.model.ResourceModel;
import org.mulesoft.web.app.model.ResourceTypeModel;
import org.w3c.dom.Element;

public class ResourceBuilder extends AbstractResourceBuilder<ResourceModel>{

	public ResourceBuilder(Class<ResourceModel> modelClass) {
		super(modelClass);
	}

	public void fillModel(ResourceModel resource, Element element) throws Exception {
	       
		super.fillModel(resource, element);
		
		String type = element.getAttribute("type");
		if(!type.isEmpty()){
			if (type.startsWith("#"))
				type = type.substring("#".length());
			
			ResourceTypeBuilder resourceTypeBuilder = getBuildManager().getBuilder(ResourceTypeBuilder.class);
			ResourceTypeModel resourceTypeModel = resourceTypeBuilder.build(element.getAttribute("type"));
			resource.setType(resourceTypeModel);
		}
	}
}
