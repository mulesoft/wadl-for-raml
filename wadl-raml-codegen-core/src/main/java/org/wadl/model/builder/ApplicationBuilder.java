package org.wadl.model.builder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xerces.dom.DOMInputImpl;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSImplementation;
import org.apache.xerces.xs.XSLoader;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.commons.io.IOUtils;
import org.mulesoft.web.app.model.ApplicationModel;
import org.mulesoft.web.app.model.DocumentationModel;
import org.mulesoft.web.app.model.ResourceModel;
import org.w3c.dom.Element;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.LSInput;


public class ApplicationBuilder {
    
	private final String DEFAULT_API_TITLE = "RESTful API spec";
	
    DocumentationExtractor docExtractor = new DocumentationExtractor();
    
    ResourceBuilder resourceBuilder = new ResourceBuilder();
    
    IPathResolver pathResolver;
    
    public ApplicationModel buildApplication(Element element) throws Exception{
    	
    	
        ApplicationModel app = new ApplicationModel();        
        
        Map <String, String> includedSchemas = getSchemas(element, pathResolver);
        app.setIncludedSchemas(includedSchemas);
        
        DocumentationModel doc = docExtractor.extractDocumentation(element);
        app.setDoc(doc);
        
        List<Element> resourcesElements = Utils.extractElements(element, "resources");
        for(Element resourcesElement : resourcesElements){
            
            String baseUri = resourcesElement.getAttribute("base");
            String title = resourcesElement.getAttribute("title");
            if(app.getBaseUri()==null){
                app.setBaseUri(baseUri);
            }
            if (app.getTitle() == null && !title.equals("")){
            	app.setTitle(title);
            } 
            else {
            	app.setTitle(DEFAULT_API_TITLE);
            }
            
            
            List<Element> resourceElements = Utils.extractElements(resourcesElement,"resource");
            for(Element resourceElement : resourceElements){
                
                ResourceModel res = resourceBuilder.buildResource(resourceElement);
                Utils.setParentUri(res,"");
                app.addResource(res);
            }
        }

        return app;
    }
    
    private Map<String, String> getSchemas(Element element, IPathResolver pathResolver) throws IOException {
    	
    	List<String> includePaths = getIncludePaths(element);
    	
    	Map<String,String> schemas = new HashMap<String, String>();
    	
    	for (String path : includePaths){

    		String schemaContent = pathResolver.getContent(path);
    		String schemaName = getSchemaName(schemaContent);
    		
    		if(schemaName!=null)
    			schemas.put(schemaName, schemaContent);
    	}
    	return schemas;
	}
    
	private String getSchemaName(String content) {
		
		String propertyValue = System.getProperty(DOMImplementationRegistry.PROPERTY);

		String schemaName = null;
		System.setProperty(DOMImplementationRegistry.PROPERTY,"org.apache.xerces.dom.DOMXSImplementationSourceImpl");
		DOMImplementationRegistry registry;
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes("UTF-8"));
			LSInput input = new DOMInputImpl();
			input.setByteStream(bais);

			registry = DOMImplementationRegistry.newInstance();

			XSImplementation impl = (XSImplementation) registry.getDOMImplementation("XS-Loader");
			XSLoader schemaLoader = impl.createXSLoader(null);
			XSModel model = schemaLoader.load(input);

			XSNamedMap declarations = model.getComponents(XSConstants.ELEMENT_DECLARATION);
			schemaName = declarations.item(0).getName();

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(propertyValue!=null){
				System.setProperty(DOMImplementationRegistry.PROPERTY,propertyValue);
			}
			else{
				System.clearProperty(DOMImplementationRegistry.PROPERTY);
			}
		}
		return schemaName;
	}

	private List<String> getIncludePaths (Element element){
    	List<String> includePaths = new ArrayList<String>();
    	List<Element> grammars = Utils.extractElements(element, "grammars");
    	 
    	for (Element grammar : grammars){
    		List<Element> includes = Utils.extractElements(grammar, "include");  
    		for (Element include : includes)
    			includePaths.add(include.getAttribute("href"));
    	}
    	return includePaths;
    }

	public void setPathResolver(IPathResolver pathResolver) {
		this.pathResolver = pathResolver;
	}

}
