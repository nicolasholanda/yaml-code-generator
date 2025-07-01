package com.github.nicolasholanda.generator;

import com.github.nicolasholanda.model.Entity;
import com.github.nicolasholanda.util.PackageConstants;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.nio.file.Paths;

/**
 * RepositoryGenerator is a class that generates a repository interface for a given entity.
 */
public class RepositoryGenerator {
    private static final String OUTPUT_PATH = "output/src/main/java/";

    /**
     * Generate a repository interface for the given entity.
     * @param entity The entity to generate a repository for.
     */
    public void generate(Entity entity) {
        String repositoryName = entity.getName() + "Repository";
        String entityClassName = entity.getName();
        String entityPackage = entity.getPackageName();
        String repositoryPackage = entityPackage + "." + PackageConstants.REPOSITORY;

        ClassName entityClass = ClassName.get(entityPackage + "." + PackageConstants.MODEL, entityClassName);
        ClassName jpaRepository = ClassName.get("org.springframework.data.jpa.repository", "JpaRepository");
        ClassName repository = ClassName.get("org.springframework.stereotype", "Repository");

        ParameterizedTypeName jpaRepositoryType = ParameterizedTypeName.get(jpaRepository, entityClass, ClassName.get(Long.class));

        TypeSpec repositoryInterface = TypeSpec.interfaceBuilder(repositoryName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(repository)
                .addSuperinterface(jpaRepositoryType)
                .build();

        JavaFile javaFile = JavaFile.builder(repositoryPackage, repositoryInterface).build();
        try {
            javaFile.writeTo(Paths.get(OUTPUT_PATH));
        } catch (Exception e) {
            throw new RuntimeException("Error generating repository: " + entity.getName(), e);
        }
    }
} 