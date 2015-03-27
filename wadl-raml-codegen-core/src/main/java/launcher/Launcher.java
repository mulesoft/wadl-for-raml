package launcher;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.mulesoft.raml.builder.RamlBuilder;
import org.mulesoft.web.app.model.ApplicationModel;
import org.raml.emitter.RamlEmitterV2;
import org.raml.model.Raml2;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.wadl.model.builder.ApplicationBuilder;

public class Launcher {
    
    public static void main(String[] args) throws FileNotFoundException{
        
//        HashMap<String,String> argsMap = parseArgs(args);
//        
//        String inputFilePath = argsMap.get("input");        
//        String outputFilePath = argsMap.get("output");
//        
//        File inputFile = new File(inputFilePath);
//        File outputFile = new File(outputFilePath);
        File inputFile = new File("/Translator2.wsdl");
//        InputStream is = new FileInputStream(inputFile);
        File outputFile = new File("/Translator.raml");
        
        try {
            process(inputFile, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void process(File inputFile, File outputFile) throws Exception {
        
        ApplicationBuilder appBuilder = new ApplicationBuilder();
        RamlBuilder ramlBuilder = new RamlBuilder();
        
        ApplicationModel app = appBuilder.buildApplication(inputFile);
        Raml2 raml = ramlBuilder.buildRaml(app);
        
		Map<String, String>  schemasBodys = app.getIncludedSchemas();
		for (String schemaName : schemasBodys.keySet()){
			String body = schemasBodys.get(schemaName);
			raml.addGlobalSchema(schemaName, body, true, true);
		}
        
        RamlEmitterV2 emmitter = new RamlEmitterV2();
        emmitter.setSingle(true);
        String dump = emmitter.dump(raml);
        
        saveFile(dump,outputFile);
    }

    private static void saveFile(String dump, File file) throws Exception {
        
        if(!file.exists()){
            file.createNewFile();
        }
        
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(dump.getBytes("UTF-8"));
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

}
