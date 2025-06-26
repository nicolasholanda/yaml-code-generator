package com.github.nicolasholanda.generator;

import com.github.nicolasholanda.model.Entity;
import com.github.nicolasholanda.model.EntityField;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.nio.file.Paths;
import javax.lang.model.element.Modifier;

/**
 * EntityGenerator is a class that generates an entity class for a given entity.
 */
public class EntityGenerator {

    private static final String OUTPUT_PATH = "output/src/main/java/";
    private final FieldGenerator fieldGenerator;

    public EntityGenerator(FieldGenerator fieldGenerator) {
        this.fieldGenerator = fieldGenerator;
    }

    /**
     * Generate an entity class for the given entity.
     * @param entity The entity to generate a class for.
     */
    public void generate(Entity entity) {
        var classModifier = entity.getAccessModifier() != null ? Modifier.valueOf(entity.getAccessModifier().toUpperCase()) : Modifier.PUBLIC;
        var classBuilder = TypeSpec.classBuilder(entity.getName())
                .addModifiers(classModifier);
        
        for (EntityField field : entity.getFields()) {
            try {
                var fieldSpec = fieldGenerator.generate(field);
                classBuilder.addField(fieldSpec);
                var javaFile = JavaFile.builder(entity.getPackageName(), classBuilder.build()).build();
                javaFile.writeTo(Paths.get(OUTPUT_PATH));
            } catch (Exception e) {
                throw new RuntimeException("Error generating entity: " + entity.getName(), e);
            }
        }
    }
}
