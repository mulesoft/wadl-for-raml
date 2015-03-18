This project provides a WADL to RAML converter

#Installation

Checkout the project and run `mvn clean install`.

#Running the converter

In the `target` subdirectory you can find a jar with complete dependencies. Run it the following way:
```
java -cp {jarName} launcher.Launcher -input {absolute path to inputFile} -output {absolute path to outputFile}
``` 