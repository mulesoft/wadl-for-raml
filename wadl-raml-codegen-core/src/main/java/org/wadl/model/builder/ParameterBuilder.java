package org.wadl.model.builder;

import java.util.HashMap;
import java.util.List;

import org.mulesoft.web.app.model.ParameterModel;
import org.w3c.dom.Element;

public class ParameterBuilder extends AbstractBuilder<ParameterModel> {
    
    public ParameterBuilder(Class<ParameterModel> modelClass) {
		super(modelClass);
	}

	private static final HashMap<String,String> typeMap = new HashMap<String, String>();
    {
        typeMap.put("int", "integer");
        typeMap.put("short", "number");
        typeMap.put("double", "number");
        typeMap.put("float", "number");
        typeMap.put("real", "number");        
        typeMap.put("bool", "boolean");
    }

    public void fillModel(ParameterModel param, Element element) throws Exception{
        
        extractDocumentation(element, param);
        
        String id = element.getAttribute("id");
        param.setId(id);
        
        String name = element.getAttribute("name");
        param.setName(name);
        
        String style = element.getAttribute("style");
        param.setStyle(style);
        
        String defaultValue = element.getAttribute("default");
        param.setDefaultValue(defaultValue);
        
        String path = element.getAttribute("path");
        param.setPath(path);
        
        String fixedValue = element.getAttribute("fixed");
        param.setFixedValue(fixedValue);
        
        String type = element.getAttribute("type");
        String refinedType = refineType(type);
        param.setType(refinedType);
        
        String requiredString = element.getAttribute("required");
        boolean isRequired = Boolean.parseBoolean(requiredString);
        param.setRequired(isRequired);
        
        String repeatingString = element.getAttribute("repeating");
        boolean isRepeating = Boolean.parseBoolean(repeatingString);
        param.setRepeating(isRepeating);
        
        List<Element> optionElements = Utils.extractElements(element, "option");
        for(Element optionElement: optionElements){
            String optionValue = optionElement.getAttribute("value");
            param.addOption(optionValue);
        }
    }

    private String refineType(String type) {
        
        int ind = type.indexOf(':');
        if(ind>=0){
            type= type.substring(ind+1);
        }
        String mapped = typeMap.get(type);
        if(mapped!=null){
            type= mapped;
        }
        return type;
    }

}
