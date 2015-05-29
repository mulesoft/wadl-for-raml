package launcher;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.mulesoft.raml.builder.RamlBuilder;
import org.mulesoft.web.app.model.ApplicationModel;
import org.raml.emitter.IRamlHierarchyTarget;
import org.raml.emitter.RamlEmitterV2;
import org.raml.model.Raml2;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.wadl.model.builder.BasicPathResolver;
import org.wadl.model.builder.BuildManager;

public class Launcher {
    
    public static void main(String[] args){
        
        HashMap<String,String> argsMap = parseArgs(args);
        
        String inputFilePath = argsMap.get("input");        
        String outputFilePath = argsMap.get("output");
        
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);
        
        try {
            process(inputFile, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void process(File inputFile, File outputFile) throws Exception {
        
    	BasicPathResolver pathResolver = new BasicPathResolver(inputFile.getParentFile(), outputFile.getParentFile());
    	
        BuildManager buildManger = new BuildManager();
        buildManger.setPathResolver(pathResolver);
        
        RamlBuilder ramlBuilder = new RamlBuilder();
        
        Document document = buildDocument(inputFile);
        Element element = document.getDocumentElement();
        
        ApplicationModel app = buildManger.process(element);
        Raml2 raml = ramlBuilder.buildRaml(app);
        
        saveRaml(outputFile, raml);
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

    private static void saveFile(String content, File file) throws Exception {
        
        if(!file.exists()){
        	file.getParentFile().mkdirs();
            file.createNewFile();
        }
        
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(content.getBytes("UTF-8"));
        bos.close();
    }
    
    private static HashMap<String, String> parseArgs(String[] args) {
        HashMap<String,String> map = new HashMap<String, String>();
        for(int i = 0 ; i < args.length ; i+=2){
            String key = args[i];
            if(key.startsWith("-")){
                key=key.substring(1);
            }
            if(i<args.length-1){
                String value = args[i+1];
                map.put(key, value);
            }
        }
        return map;
    }
    

    private static Document buildDocument(File inputFile) throws Exception {
        
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document document = dBuilder.parse(inputFile);
        return document;
    }

}
