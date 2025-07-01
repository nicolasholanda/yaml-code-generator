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
 * ServiceGenerator is a class that generates a service class for a given entity.
 */
public class ServiceGenerator {
    private static final String OUTPUT_PATH = "output/src/main/java/";

    /**
     * Generate a service class for the given entity.
     * @param entity The entity to generate a service for.
     */
    public void generate(Entity entity) {
        String serviceName = entity.getName() + "Service";
        String entityClassName = entity.getName();
        String entityPackage = entity.getPackageName();
        String servicePackage = entityPackage + "." + PackageConstants.SERVICE;

        ClassName repositoryClass = ClassName.get(entityPackage + "." + PackageConstants.REPOSITORY, entityClassName + "Repository");
        ClassName serviceAnnotation = ClassName.get("org.springframework.stereotype", "Service");

        FieldSpec repositoryField = FieldSpec.builder(repositoryClass, "repository", Modifier.PRIVATE)
                .addAnnotation(ClassName.get("org.springframework.beans.factory.annotation", "Autowired"))
                .build();

        TypeSpec serviceClass = TypeSpec.classBuilder(serviceName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(serviceAnnotation)
                .addField(repositoryField)
                .build();

        JavaFile javaFile = JavaFile.builder(servicePackage, serviceClass).build();
        try {
            javaFile.writeTo(Paths.get(OUTPUT_PATH));
        } catch (Exception e) {
            throw new RuntimeException("Error generating service: " + entity.getName(), e);
        }
    }
} 