package org.wadl.model.builder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
import org.apache.xerces.xs.XSObject;
import org.mulesoft.web.app.model.ApplicationModel;
import org.mulesoft.web.app.model.ResourceModel;
import org.w3c.dom.Element;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.LSInput;


public class ApplicationBuilder extends AbstractBuilder<ApplicationModel> {

	public ApplicationBuilder(Class<ApplicationModel> modelClass) {
		super(modelClass);
	}

	private final String DEFAULT_API_TITLE = "Enter API title here";

    @Override
    public void fillModel(ApplicationModel app, Element element) throws Exception{

        extractDocumentation(element, app);

        Map <String, String> includedSchemas = getSchemas(element, pathResolver);
        app.setIncludedSchemas(includedSchemas);

        ResourceBuilder resourceBuilder = getBuildManager().getBuilder(ResourceBuilder.class);
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
				ResourceModel res = resourceBuilder.build(resourceElement);
                Utils.setParentUri(res,"");
                app.addResource(res);
            }
        }

        ResourceTypeBuilder resourceTypeBuilder = getBuildManager().getBuilder(ResourceTypeBuilder.class);
        List<Element> resourceTypeElements = Utils.extractElements(element, "resource_type");
        for(Element resourceTypeElement : resourceTypeElements){
        	resourceTypeBuilder.build(resourceTypeElement);
        }

        MethodBuilder methodBuilder = getBuildManager().getBuilder(MethodBuilder.class);
        List<Element> methodElements = Utils.extractElements(element, "method");
        for(Element methodElement: methodElements){
        	methodBuilder.build(methodElement);
        }

        RepresentationBuilder representationBuilder = getBuildManager().getBuilder(RepresentationBuilder.class);
        List<Element> representationElements = Utils.extractElements(element, "representation");
        for(Element representationElement: representationElements){
        	representationBuilder.build(representationElement);
        }
    }

    private Map<String, String> getSchemas(Element element, IPathResolver pathResolver) throws IOException {

    	List<String> includePaths = getIncludePaths(element);

    	Map<String,String> schemas = new HashMap<String, String>();

    	for (String path : includePaths){

    		String schemaContent = pathResolver.getContent(path);
    		List<String> elementNames = getRootElementNames(schemaContent);

    		for(String str : elementNames){
    			schemas.put(str, schemaContent);
    		}
    	}
    	return schemas;
	}

	@SuppressWarnings("unchecked")
    private List<String> getRootElementNames(String content) {

		ArrayList<String> list = new ArrayList<String>();
		String propertyValue = System.getProperty(DOMImplementationRegistry.PROPERTY);

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

			int i = 0;
			XSObject object = declarations.item(i);
			while (object != null) {
			    list.add(object.getName());
				object = declarations.item(++i);
			}

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
		return list;
	}

	private List<String> getIncludePaths (Element element){
    	List<String> includePaths = new ArrayList<String>();
    	List<Element> grammars = Utils.extractElements(element, "grammars");

    	for (Element grammar : grammars){
    		List<Element> includes = Utils.extractElements(grammar, "include");
    		for (Element include : includes) {
                includePaths.add(include.getAttribute("href"));
            }
    	}
    	return includePaths;
    }

}
