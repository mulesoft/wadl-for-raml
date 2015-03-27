package org.wadl.model.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSImplementation;
import org.apache.xerces.xs.XSLoader;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObject;
import org.apache.commons.io.IOUtils;
import org.mulesoft.web.app.model.ApplicationModel;
import org.mulesoft.web.app.model.DocumentationModel;
import org.mulesoft.web.app.model.ResourceModel;
import org.mulesoft.web.app.model.ResourceOwner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;


public class ApplicationBuilder {
    
	private final String DEFAULT_API_TITLE = "RESTful API spec";
	
    DocumentationExtractor docExtractor = new DocumentationExtractor();
    
    ResourceBuilder resourceBuilder = new ResourceBuilder();
    
    public ApplicationModel buildApplication(File inputFile) throws Exception{
        Document document = buildDocument(inputFile);
        Element element = document.getDocumentElement();
    	
        ApplicationModel app = new ApplicationModel();
        
        List<String> includePaths = getIncludePaths(element);
        Map <String, String> includedSchemas = getSchemas(includePaths, inputFile.getParent());
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
    
    private Map<String, String> getSchemas(List<String> includePaths, String parentFilePath) throws IOException {
    	Map<String,String> schemas = new HashMap<String, String>();
    	
    	for (String path : includePaths){
    		File file = new File(parentFilePath + path);
    		InputStream in = new FileInputStream(file);
    		//System.out.println(IOUtils.toString(in));
    		String schemaName = getSchemaName(file);
    		String schemaContent = IOUtils.toString(in);
    		if(!schemaName.equals(""))
    			schemas.put(schemaName, schemaContent);
    	}
    	return schemas;
	}
    
    private String getSchemaName(File file){
    	String schemaName = "";
    	System.setProperty(DOMImplementationRegistry.PROPERTY, "org.apache.xerces.dom.DOMXSImplementationSourceImpl");
    	   DOMImplementationRegistry registry;
    	   try {
    	    registry = DOMImplementationRegistry.newInstance();
    	    
    	    XSImplementation impl = (XSImplementation) registry.getDOMImplementation("XS-Loader");
    	    XSLoader schemaLoader = impl.createXSLoader(null);
    	    XSModel model = schemaLoader.loadURI(file.toURI().getPath());
    	    
    	    XSNamedMap declarations = model.getComponents(XSConstants.ELEMENT_DECLARATION);
    	    schemaName = declarations.item(0).getName();
    	       	    	
    	   } catch (Exception e) {
    		   e.printStackTrace();
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

    private static Document buildDocument(File inputFile) throws Exception {
        
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document document = dBuilder.parse(inputFile);
        return document;
    }
}
