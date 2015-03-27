package org.mulesoft.web.app.model;

import java.util.ArrayList;
import java.util.List;

public class RepresentationModel extends AbstractElement{
    
    private String mediaType;

    private String schema;
    
    private List<ParameterModel> formParameters = new ArrayList<ParameterModel>();


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
