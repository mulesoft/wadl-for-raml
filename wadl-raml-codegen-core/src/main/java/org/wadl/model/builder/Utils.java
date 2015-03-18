package org.wadl.model.builder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.mulesoft.web.app.model.AbstractElement;
import org.mulesoft.web.app.model.ResourceModel;
import org.mulesoft.web.app.model.ResponseModel;
import org.raml.model.Action;
import org.raml.model.Resource;
import org.raml.model.Response;
import org.raml.model.parameter.AbstractParam;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Utils {
    
    public static List<Element> extractElements(Element parent, String tag){
        
        ArrayList<Element> list = new ArrayList<Element>();
        
        NodeList children = parent.getChildNodes();
        int length = children.getLength();
        for(int i = 0 ; i < length ;i++){            
            Node node = children.item(i);            
            if(!(node instanceof Element)){
                continue;
            }
            Element el = (Element) node;
            String tagName = el.getTagName();
            if(!tagName.equals(tag)){
                continue;
            }
            
            list.add(el);
        }        
        return list;        
    }

    public static String refinePath(String path) {
        
        if(!path.startsWith("/")){
            return "/" + path;
        }        
        return path;
    }

    public static void setParentUri(ResourceModel resource, String uri) {
        resource.setParenUri(uri);
        
        String path = resource.getPath();
        LinkedHashMap<String, ResourceModel> resources = resource.getResources();
        for(ResourceModel res : resources.values()){
            setParentUri(res, uri+path);
        }
        
    }
    
    public static void setDocumentation(AbstractElement element, Resource resource) {
        String content = element.getDoc().getContent();
        if(content.trim().isEmpty()){
            return;
        }
        resource.setDescription(content);
    }
    

    public static void setDocumentation(AbstractElement element, Action action) {
        String content = element.getDoc().getContent();
        if(content.trim().isEmpty()){
            return;
        }
        action.setDescription(content);
    }

    public static void setDocumentation(AbstractElement element, Response response) {
        String content = element.getDoc().getContent();
        if(content.trim().isEmpty()){
            return;
        }
        response.setDescription(content);
    }
    
    public static void setDocumentation(AbstractElement element, AbstractParam param) {
        String content = element.getDoc().getContent();
        if(content.trim().isEmpty()){
            return;
        }
        param.setDescription(content);
    }
    
    public static boolean isEmptyString(String str){
        return str == null || str.trim().isEmpty();
    }

}
