package org.mulesoft.web.app.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResourceModel extends ResourceOwner {
    
    private String id;
    
    private String path;
    
    private String type;
    
    private String parenUri;
    
    private Map<String,List<MethodModel>> methods = new LinkedHashMap<String, List<MethodModel>>();
    
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

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addMethod(MethodModel method) {
        String htrtpType = method.getName();
        List<MethodModel> list = this.methods.get(htrtpType);
        if(list==null){
            list =new ArrayList<MethodModel>();
            this.methods.put(htrtpType, list);
        }
        list.add(method);
    }

    public Map<String,List<MethodModel>> getMethods() {
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
