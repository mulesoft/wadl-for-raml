This project provides a WADL to RAML converter

#Installation

Checkout both projects and run `mvn clean install` on raml-java-parser then on wadl-raml-codegen-core.

#Running the converter

In the `wadl-raml-codegen-core/target` subdirectory you can find a jar with complete dependencies. Run it the following way:
```
java -cp {jarName} launcher.Launcher -input {absolute path to inputFile} -output {absolute path to outputFile}
``` 
