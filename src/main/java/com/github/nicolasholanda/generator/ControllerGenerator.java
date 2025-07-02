package com.github.nicolasholanda.generator;

import com.github.nicolasholanda.model.Entity;
import com.github.nicolasholanda.util.PackageConstants;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.nio.file.Paths;

/**
 * ControllerGenerator is a class that generates a controller class for a given entity.
 */
public class ControllerGenerator {
    private static final String OUTPUT_PATH = "output/src/main/java/";

    /**
     * Generate a controller class for the given entity.
     * @param entity The entity to generate a controller for.
     */
    public void generate(Entity entity) {
        String controllerName = entity.getName() + "Controller";
        String entityClassName = entity.getName();
        String entityPackage = entity.getPackageName();
        String controllerPackage = entityPackage + "." + PackageConstants.CONTROLLER;

        ClassName serviceClass = ClassName.get(entityPackage + "." + PackageConstants.SERVICE, entityClassName + "Service");
        ClassName controllerAnnotation = ClassName.get("org.springframework.web.bind.annotation", "RestController");
        ClassName requestMapping = ClassName.get("org.springframework.web.bind.annotation", "RequestMapping");

        FieldSpec serviceField = FieldSpec.builder(serviceClass, "service", Modifier.PRIVATE)
                .addAnnotation(ClassName.get("org.springframework.beans.factory.annotation", "Autowired"))
                .build();

        TypeSpec controllerClass = TypeSpec.classBuilder(controllerName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(controllerAnnotation)
                .addAnnotation(requestMapping)
                .addField(serviceField)
                .build();

        JavaFile javaFile = JavaFile.builder(controllerPackage, controllerClass).build();
        try {
            javaFile.writeTo(Paths.get(OUTPUT_PATH));
        } catch (Exception e) {
            throw new RuntimeException("Error generating controller: " + entity.getName(), e);
        }
    }
} 