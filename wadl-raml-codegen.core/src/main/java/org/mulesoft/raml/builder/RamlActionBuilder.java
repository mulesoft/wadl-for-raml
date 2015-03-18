package org.mulesoft.raml.builder;

import java.util.List;
import java.util.Map;

import org.mulesoft.web.app.model.MethodModel;
import org.mulesoft.web.app.model.ParameterModel;
import org.mulesoft.web.app.model.RepresentationModel;
import org.mulesoft.web.app.model.RequestModel;
import org.mulesoft.web.app.model.ResponseModel;
import org.raml.model.Action;
import org.raml.model.ActionType;
import org.raml.model.MimeType;
import org.raml.model.Response;
import org.raml.model.parameter.Header;
import org.raml.model.parameter.QueryParameter;
import org.wadl.model.builder.Utils;

public class RamlActionBuilder {
    
    private RamlParameterBuilder paramBuilder = new RamlParameterBuilder();
    
    private MimeTypeBuilder mimeTypeBuilder = new MimeTypeBuilder();
    
    public Action buildRamlAction(MethodModel methodModel){
        Action action = new Action();
        
        try{
            String typeString = methodModel.getName();        
            ActionType type = Enum.valueOf(ActionType.class, typeString);
            action.setType(type);
        }
        catch(Exception e){
            
        }
        Utils.setDocumentation(methodModel, action);
        
        Map<String, MimeType> bodyMap = action.getBody();
        Map<String, QueryParameter> queryParamMap = action.getQueryParameters();
        Map<String, Response> responsesMap = action.getResponses();
        Map<String, Header> headersMap = action.getHeaders();
        
        List<RequestModel> requests = methodModel.getRequests();
        for(RequestModel requestModel : requests){
            
            List<RepresentationModel> representations = requestModel.getRepresentations();
            for(RepresentationModel representation : representations){
                
                MimeType mimeType = mimeTypeBuilder.buildMimeType(representation);
                String mediaType = mimeType.getType();
                bodyMap.put(mediaType, mimeType);
            }
            
            List<ParameterModel> queryParams = requestModel.getQueryParams();
            for(ParameterModel paramModel : queryParams){
                String name = paramModel.getName();
                QueryParameter qParam = paramBuilder.buildQueryParameter(paramModel);
                queryParamMap.put(name, qParam);
            }
            
            List<ParameterModel> headers = requestModel.getHeaders();
            for(ParameterModel paramModel: headers){
                String name = paramModel.getName();
                Header header = paramBuilder.buildHeader(paramModel);
                headersMap.put(name, header);
            }
        }
        
        Map<String, List<ResponseModel>> responses = methodModel.getResponses();
        for(List<ResponseModel> list : responses.values()){
            ResponseModel responseModel = list.get(0);
            String status = responseModel.getStatus();
            if(Utils.isEmptyString(status)){
                status = "200";
            }
            Response response = new Response();
            responsesMap.put(status, response);
            
            Utils.setDocumentation(responseModel, response);
            
            Map<String, Header> responseHeadersMap = response.getHeaders();
            Map<String, MimeType> responseBodyMap = response.getBody();            
            
            List<RepresentationModel> representations = responseModel.getRepresentations();
            for(RepresentationModel representation:representations){
                MimeType mimeType = mimeTypeBuilder.buildMimeType(representation);
                String mediaType = mimeType.getType();
                responseBodyMap.put(mediaType, mimeType);
            }
            
            List<ParameterModel> responseHeaders = responseModel.getHeaders();
            for(ParameterModel paramModel: responseHeaders){
                String name = paramModel.getName();
                Header header = paramBuilder.buildHeader(paramModel);
                responseHeadersMap.put(name, header);
            }
        }
        
        return action;        
    }

}
