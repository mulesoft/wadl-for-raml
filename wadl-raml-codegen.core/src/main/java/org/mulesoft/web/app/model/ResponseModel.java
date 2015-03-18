package org.mulesoft.web.app.model;

import java.util.ArrayList;
import java.util.List;

import org.wadl.model.builder.HasRepresentation;

public class ResponseModel extends HasRepresentation {

    private String status;
    
    private List<ParameterModel> headers = new ArrayList<ParameterModel>();
    
    public List<ParameterModel> getHeaders() {
        return headers;
    }

    public void addHeader(ParameterModel header) {
        this.headers.add(header);
    }
    
    
    public String getStatus() {
        return this.status;
    }


    public void setStatus(String status) {
        this.status = status;
    }
}
