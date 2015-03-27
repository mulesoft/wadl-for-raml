package org.wadl.model.builder;

import java.util.HashMap;
import java.util.Map;

import org.mulesoft.web.app.model.AbstractElement;
import org.mulesoft.web.app.model.ApplicationModel;
import org.mulesoft.web.app.model.DocumentationModel;
import org.mulesoft.web.app.model.MethodModel;
import org.mulesoft.web.app.model.ParameterModel;
import org.mulesoft.web.app.model.RepresentationModel;
import org.mulesoft.web.app.model.RequestModel;
import org.mulesoft.web.app.model.ResourceModel;
import org.mulesoft.web.app.model.ResponseModel;
import org.w3c.dom.Element;

public class BuildManager {
	
	private HashMap<Class<? extends AbstractBuilder<?>>, AbstractBuilder<?>> builderMap;
	
	private HashMap<Class<? extends AbstractElement>, HashMap<String, AbstractElement>> modelMap
		= new HashMap<Class<? extends AbstractElement>, HashMap<String,AbstractElement>>();
	
	public BuildManager() {
		init();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends AbstractElement>T getModelElement(Class<T> clazz, Element element){
		
		String id = null;
		if(element.hasAttribute("id")){
			id = element.getAttribute("id");
		}
		else if(element.hasAttribute("href")){
			id = element.getAttribute("href");
			if(id.startsWith("#")){
				id = id.substring(1);
			}
		}
		
		if(id==null){
			try {
				T newInstance = clazz.newInstance();
				return newInstance;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		HashMap<String, AbstractElement> map = modelMap.get(clazz);
		if(map==null){
			map = new HashMap<String, AbstractElement>();
			modelMap.put(clazz, map);
		}
		AbstractElement instance = map.get(id);
		if(instance==null){
			try {
				instance = clazz.newInstance();
				instance.setId(id);
				map.put(id, instance);				
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return (T) instance;
	}

	public ApplicationModel process(Element element) throws Exception{
		ApplicationBuilder appBuilder = getBuilder(ApplicationBuilder.class);
		ApplicationModel appModel = appBuilder.build(element);
		return appModel;
	}
	
	
	public void setPathResolver(IPathResolver pathResolver){
		for(AbstractBuilder<?> bld : builderMap.values()){
			bld.setPathResolver(pathResolver);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public <T>T getBuilder(Class<T> clazz){
		return (T) builderMap.get(clazz) ;
	}
	
	private static final HashMap<Class<?>,Class<?>> builderToModelMap = new HashMap<Class<?>, Class<?>>();
	static{
		builderToModelMap.put(ApplicationBuilder.class, ApplicationModel.class);
		builderToModelMap.put(MethodBuilder.class, MethodModel.class);
		builderToModelMap.put(ParameterBuilder.class, ParameterModel.class);
		builderToModelMap.put(RepresentationBuilder.class,	RepresentationModel.class);
		builderToModelMap.put(RequestBuilder.class, RequestModel.class);
		builderToModelMap.put(ResourceBuilder.class, ResourceModel.class);
		builderToModelMap.put(ResponseBuilder.class, ResponseModel.class);
		builderToModelMap.put(DocumentationExtractor.class, DocumentationModel.class);
	}


	@SuppressWarnings("unchecked")
	private void init() {
		builderMap = new HashMap<Class<? extends AbstractBuilder<?>>, AbstractBuilder<?>>();
		for(Map.Entry<Class<?>,Class<?>> entry : builderToModelMap.entrySet()){
			Class<? extends AbstractBuilder<?>> builderClass = (Class<? extends AbstractBuilder<?>>) entry.getKey();
			Class<?> modelClass = entry.getValue();
			try {
				AbstractBuilder<?> builder = builderClass.getConstructor(Class.class).newInstance(modelClass);
				builder.setBuildManager(this);
				builderMap.put(builderClass, builder);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
