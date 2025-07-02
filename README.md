# YAML Code Generator

Generate a full Spring Boot CRUD structure from a simple YAML definition.

## Features
- Generates model, repository, service, and controller classes
- Uses Spring Boot and JavaPoet
- Clean package structure: `model`, `repository`, `service`, `controller`

## Usage
1. Define your entities in a YAML file:
   ```yaml
   packageName: com.example.demo
   entities:
     - name: User
       fields:
         - name: id
           type: Long
         - name: name
           type: String
   ```
2. Build the project:
   ```sh
   mvn clean package
   ```
3. Run the generator:
   ```sh
   java -jar target/yaml-code-generator-1.0-SNAPSHOT-shaded.jar your-entities.yml
   ```
4. Check the `output/src/main/java/` folder for generated code.

## Requirements
- Java 21+
- Maven