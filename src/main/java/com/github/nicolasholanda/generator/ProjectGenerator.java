package com.github.nicolasholanda.generator;

import java.io.IOException;
import java.nio.file.Paths;

import javax.lang.model.element.Modifier;

import com.github.nicolasholanda.model.Entity;
import com.github.nicolasholanda.model.EntityField;
import com.github.nicolasholanda.model.Project;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

public class ProjectGenerator {

    public static void generate(Project project) {
        for (Entity entity : project.getEntities()) {
            generateEntity(entity);
        }
    }

    private static void generateEntity(Entity entity) {
        var classBuilder = TypeSpec.classBuilder(entity.getName())
                .addModifiers(Modifier.PUBLIC);
        
        for (EntityField field : entity.getFields()) {
            try {
                var classType = Class.forName("java.lang." + field.getType());
                var fieldSpec = FieldSpec.builder(classType, field.getName())
                    .addModifiers(Modifier.PRIVATE)
                    .build();
                classBuilder.addField(fieldSpec);

                JavaFile javaFile = JavaFile.builder("com.example", classBuilder.build()).build();
                javaFile.writeTo(Paths.get("src/main/java/generated/"));
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
