package org.wadl.model.builder;

import org.mulesoft.web.app.model.AbstractElement;
import org.mulesoft.web.app.model.DocumentationModel;
import org.w3c.dom.Element;

public abstract class AbstractBuilder<T extends AbstractElement> {
	
	protected Class<T> modelClass;
	
	public AbstractBuilder(Class<T> modelClass) {
		this.modelClass = modelClass;
	}

	protected IPathResolver pathResolver;
	
	protected BuildManager buildManager;
	

	protected void extractDocumentation(Element xmlEelement, AbstractElement modelElement) throws Exception {
		DocumentationExtractor docExtractor = getBuildManager().getBuilder(DocumentationExtractor.class);
		DocumentationModel doc = docExtractor.build(xmlEelement);
	    modelElement.setDoc(doc);
	}
	
	protected T build(Element element) throws Exception{
		T modelElement = getBuildManager().getModelElement(modelClass, element);
		if(!element.hasAttribute("href")){
			fillModel(modelElement, element);
		}
		return modelElement;
	}

	protected T build(String element) throws Exception{
		T modelElement = getBuildManager().getModelElement(modelClass, element);
		
		return modelElement;
	}
	
	abstract void fillModel(T modelElement, Element element) throws Exception;

	public IPathResolver getPathResolver() {
		return pathResolver;
	}

	public void setPathResolver(IPathResolver pathResolver) {
		this.pathResolver = pathResolver;
	}

	public BuildManager getBuildManager() {
		return buildManager;
	}

	public void setBuildManager(BuildManager buildManager) {
		this.buildManager = buildManager;
	}
	}
