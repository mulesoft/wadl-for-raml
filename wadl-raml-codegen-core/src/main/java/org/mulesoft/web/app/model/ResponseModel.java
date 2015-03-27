package org.mulesoft.web.app.model;

import java.util.ArrayList;
import java.util.List;

public class ResponseModel extends HasRepresentation {

    private String status;

    private String schema;
    
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

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}
    
}
