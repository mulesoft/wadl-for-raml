package launcher;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.mulesoft.raml.builder.RamlBuilder;
import org.mulesoft.web.app.model.ApplicationModel;
import org.mulesoft.web.app.model.MethodModel;
import org.mulesoft.web.app.model.ResourceModel;
import org.raml.emitter.IRamlHierarchyTarget;
import org.raml.emitter.RamlEmitterV2;
import org.raml.model.Raml2;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.wadl.model.builder.BasicPathResolver;
import org.wadl.model.builder.BuildManager;

import com.mulesoft.raml.optimizer.ResourceOptimizer;

public class FolderHandlerLauncher {
	public static void main (String [] args){
		Map<String, String> cliParams = getCLIParams(args);
		
		String inputDirPath = cliParams.get("inputFolder");
		String outputDirPath = cliParams.get("outputFolder");
		
		File inputDir = new File(inputDirPath);
		File outputDir = new File(outputDirPath);
		
		try {
			processDir(inputDir, outputDir);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	private static void processDir(File inputDir, File outputDir) throws Exception {
		
		File[] wadlFiles = inputDir.listFiles(new FileFilter() {
			
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".wadl");
			}
		});
		List<File> wadlFilesList = Arrays.asList(wadlFiles);
		
		ApplicationModel fullAppModel = new ApplicationModel();
		BasicPathResolver pathResolver = new BasicPathResolver(inputDir, outputDir);
		
		for (File wadlFile : wadlFilesList){
			BuildManager buildManger = new BuildManager();
			buildManger.setPathResolver(pathResolver);
			Document document = buildDocument(wadlFile);
			Element element = document.getDocumentElement();
			ApplicationModel app = buildManger.process(element);

			if (fullAppModel.getIncludedSchemas() == null)
				fullAppModel.setIncludedSchemas(new HashMap<String, String>());
			fullAppModel.getIncludedSchemas().putAll(app.getIncludedSchemas());
			fullAppModel.setBaseUri(app.getBaseUri());
			fullAppModel.setTitle(app.getTitle());			
			appendResources(fullAppModel.getResources(), app.getResources());
		}
		
		RamlBuilder ramlBuilder = new RamlBuilder();
        
        Raml2 raml = ramlBuilder.buildRaml(fullAppModel);
        
        ResourceOptimizer resourceOptimizer = new ResourceOptimizer(raml);
        resourceOptimizer.optimizeRaml();
        
        saveRaml(new File(outputDir, "api.raml"), raml);
	}
	
	private static void appendResources (Map<String,ResourceModel> parentMap, Map<String,ResourceModel> childMap){
		for(Map.Entry<String,ResourceModel> entry : childMap.entrySet()){
			if (parentMap.containsKey(entry.getKey())){
				ResourceModel parentResourceModel = parentMap.get(entry.getKey());
				ResourceModel childResourceModel = childMap.get(entry.getKey());
				
				if ((parentResourceModel.getMethods() == null) && (childResourceModel.getMethods() != null))
					parentResourceModel.setMethods(childResourceModel.getMethods());
				else if ((parentResourceModel.getMethods() != null) && (childResourceModel.getMethods() != null)){
					for (MethodModel method : childResourceModel.getMethods()){
						if (method.getName() != null){
							parentResourceModel.addMethod(method);
						}
					}
				}
				
				Map<String,ResourceModel> subParentMap = parentResourceModel.getResources();
				Map<String,ResourceModel> subChildMap = childResourceModel.getResources();
				
				appendResources(subParentMap, subChildMap);
			}
			else {
				parentMap.put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	private static void saveRaml(final File outputFile, Raml2 raml)
	{
    	final File root = outputFile.getParentFile();
    	
		RamlEmitterV2 emmitter = new RamlEmitterV2();
		emmitter.dump(new IRamlHierarchyTarget() {
			
			public void writeRoot(String content) {
				try {
					saveFile(content, outputFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			public void write(String path, String content) {
				File f = new File(root, path);
				f.getParentFile().mkdirs();
				try {
					saveFile(content, f);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		},raml);
	}
	
	private static void saveFile( String content, File file) throws IOException{
		
		if(!file.exists()){
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		
		FileOutputStream fos = new FileOutputStream(file);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		bos.write(content.getBytes("UTF-8"));
		bos.close();
	}

	private static Map<String, String> getCLIParams(String[] args) {
		
		Map<String, String> cliParams = new HashMap<String, String>();
		for (int i = 0; i < args.length; i+=2){
			String key = args[i];
			if (key.startsWith("-")){
				key = key.substring("-".length());
			}
			if (i < args.length-1){
				String value = args[i+1];
				cliParams.put(key,value);
			}
		}
		return cliParams;
	}
	
    private static Document buildDocument(File inputFile) throws Exception {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document document = dBuilder.parse(inputFile);
        return document;
    }
}
