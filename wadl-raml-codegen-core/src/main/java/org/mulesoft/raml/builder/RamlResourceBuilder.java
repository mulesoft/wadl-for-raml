package org.mulesoft.raml.builder;

import java.util.LinkedHashMap;
import java.util.List;

import org.mulesoft.web.app.model.MethodModel;
import org.mulesoft.web.app.model.ParameterModel;
import org.mulesoft.web.app.model.ResourceModel;
import org.raml.model.Action;
import org.raml.model.Resource;
import org.raml.model.parameter.Header;
import org.raml.model.parameter.QueryParameter;
import org.wadl.model.builder.Utils;

public class RamlResourceBuilder {
    
    private RamlActionBuilder actionBuilder = new RamlActionBuilder();
    
    private RamlParameterBuilder paramBuilder = new RamlParameterBuilder();
    
    public Resource buildResource(ResourceModel resourceModel){
        
        Resource ramlResource = new Resource();
        
        Utils.setDocumentation(resourceModel, ramlResource);
        
        String path = resourceModel.getPath();
        ramlResource.setRelativeUri(path);
        
        for(MethodModel method : resourceModel.getMethods()){
            Action action = actionBuilder.buildRamlAction(method);
            ramlResource.getActions().put(action.getType(), action);
        }
        
        List<ParameterModel> queryParams = resourceModel.getQueryParams();
        for(ParameterModel paramModel : queryParams){
            String name = paramModel.getName();
            QueryParameter qParam = paramBuilder.buildQueryParameter(paramModel);
            for(Action action : ramlResource.getActions().values()){
                action.getQueryParameters().put(name, qParam);
            }
        }
        
        List<ParameterModel> headers = resourceModel.getHeaders();
        for(ParameterModel paramModel: headers){
            String name = paramModel.getName();
            Header header = paramBuilder.buildHeader(paramModel);
            for(Action action : ramlResource.getActions().values()){
                action.getHeaders().put(name, header);
            }
        }
        
        LinkedHashMap<String, ResourceModel> resources = resourceModel.getResources();
        for(ResourceModel resource: resources.values()){
            Resource res = buildResource(resource);
            res.setParentUri(path);
            String path0 = res.getUri();
            ramlResource.getResources().put(path0, res);
        }
        
        return ramlResource;
        
    }
}
