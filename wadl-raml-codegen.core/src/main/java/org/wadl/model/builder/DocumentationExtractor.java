package org.wadl.model.builder;

import java.util.List;

import org.mulesoft.web.app.model.DocumentationModel;
import org.w3c.dom.Element;

public class DocumentationExtractor {
    
    public DocumentationModel extractDocumentation(Element element){
        
        StringBuilder bld = new StringBuilder();
        List<Element> docElements = Utils.extractElements(element, "doc");
        for(Element docElement : docElements){
            String titleValue = docElement.getAttribute("title");
            bld.append(titleValue);
        }
        
        String str = bld.toString();
        DocumentationModel result = new DocumentationModel(str);
        return result;
        
    }

}
