package org.wadl.model.builder;

import java.util.List;

import org.mulesoft.web.app.model.DocumentationModel;
import org.w3c.dom.Element;

public class DocumentationExtractor extends AbstractBuilder<DocumentationModel> {
    
    public DocumentationExtractor(Class<DocumentationModel> modelClass) {
		super(modelClass);
	}

	public void fillModel(DocumentationModel result, Element element){
        
        StringBuilder bld = new StringBuilder();
        List<Element> docElements = Utils.extractElements(element, "doc");
        for(Element docElement : docElements){
            String titleValue = docElement.getAttribute("title");
            bld.append(titleValue);
        }
        
        String str = bld.toString();
        result.setContent(str);
    }

}
