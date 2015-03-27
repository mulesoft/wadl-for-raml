package org.mulesoft.web.app.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResourceModel extends ResourceOwner {

    private String path;
    
    private String type;
    
    private String parenUri;
    
    private List<MethodModel> methods = new ArrayList<MethodModel>();
    
    private List<ParameterModel> queryParams = new ArrayList<ParameterModel>();
    
    private List<ParameterModel> headers = new ArrayList<ParameterModel>();

    public List<ParameterModel> getQueryParams() {
        return queryParams;
    }

    public void addQueryParam(ParameterModel queryParam) {
        this.queryParams.add(queryParam);
    }

    public List<ParameterModel> getHeaders() {
        return headers;
    }

    public void addHeader(ParameterModel header) {
        this.headers.add(header);
    }

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addMethod(MethodModel method) {        
        methods.add(method);
    }

    public List<MethodModel> getMethods() {
        return methods;
    }
    
    @Override
    public String toString() {
        return path;
    }

    public String getParenUri() {
        return parenUri;
    }

    public void setParenUri(String parenUri) {
        this.parenUri = parenUri;
    }

}
