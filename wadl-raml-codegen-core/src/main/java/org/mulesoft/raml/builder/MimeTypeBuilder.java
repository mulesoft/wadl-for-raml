package org.mulesoft.raml.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mulesoft.web.app.model.ParameterModel;
import org.mulesoft.web.app.model.RepresentationModel;
import org.raml.model.MimeType;
import org.raml.model.parameter.FormParameter;

public class MimeTypeBuilder {
    
    private RamlParameterBuilder paramBuilder = new RamlParameterBuilder();
    
    public MimeType buildMimeType(RepresentationModel representationModel){
        MimeType mimeType = new MimeType();
        String mediaType = representationModel.getMediaType();
        mimeType.setType(mediaType);
        List<ParameterModel> formParameters = representationModel.getFormParameters();
        
        Map<String, List<FormParameter>> formParamMap = mimeType.getFormParameters();
        for(ParameterModel paramModel : formParameters){
            String name = paramModel.getName();
            FormParameter formParam = paramBuilder.buildFormParameter(paramModel);
            List<FormParameter> list = formParamMap.get(name);
            if(list == null){
                list = new ArrayList<FormParameter>();
                formParamMap.put(name, list);
            }
            list.add(formParam);
        }
        return mimeType;
    }

}
