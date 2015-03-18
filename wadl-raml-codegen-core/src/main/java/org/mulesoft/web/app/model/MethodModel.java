package org.mulesoft.web.app.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MethodModel extends AbstractElement {
    
    private String name;
    
    private String id;
    
    private Map<String,List<ResponseModel>> responses = new LinkedHashMap<String, List<ResponseModel>>();
    
    private List<RequestModel> requests = new ArrayList<RequestModel>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public void addRequest(RequestModel request){
        this.requests.add(request);
    }
    
    
    public void addResponse(ResponseModel response){
        String status = response.getStatus();
        List<ResponseModel> list = this.responses.get(status);
        if(list==null){
            list = new ArrayList<ResponseModel>();
            this.responses.put(status, list);
        }
        list.add(response);
    }
    

    public Map<String, List<ResponseModel>> getResponses() {
        return responses;
    }

    public List<RequestModel> getRequests() {
        return requests;
    }

}
