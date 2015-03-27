package org.mulesoft.web.app.model;

import java.util.ArrayList;
import java.util.List;

public class RepresentationModel {
    
    private String id;
    
    private String mediaType;

    private String schema;
    
    private List<ParameterModel> formParameters = new ArrayList<ParameterModel>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public List<ParameterModel> getFormParameters() {
        return formParameters;
    }

    public void addFormParameter(ParameterModel formParameter) {
        this.formParameters.add(formParameter);
    }

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

}
