package org.mulesoft.web.app.model;

import java.util.ArrayList;
import java.util.List;

import org.wadl.model.builder.HasRepresentation;

public class RequestModel extends HasRepresentation {
    
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

}
