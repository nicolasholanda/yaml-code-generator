package com.github.nicolasholanda.generator;

import com.github.nicolasholanda.model.EntityField;
import com.squareup.javapoet.FieldSpec;

import javax.lang.model.element.Modifier;

/**
 * FieldGenerator is a class that generates a field spec for a given field.
 */
public class FieldGenerator {

    private static final String DEFAULT_PACKAGE = "java.lang.";

    /**
     * Generate a field spec for the given field.
     * @param field The field to generate a field spec for.
     * @return FieldSpec The field spec.
     */
    public FieldSpec generate(EntityField field) {
        try {
            var classFullName = getClassFullName(field);
            var classType = Class.forName(classFullName);
            var accessModifier = field.getAccessModifier() != null ? Modifier.valueOf(field.getAccessModifier().toUpperCase()) : Modifier.PRIVATE;
            return FieldSpec.builder(classType, field.getName())
                .addModifiers(accessModifier)
                .build();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found: " + field.getType(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unknown error generating field: " + field.getName(), e);
        }
    }

    /**
     * Get the full class name for the given field. If the field type contains a dot, it is considered a full class name.
     * @param field The field to get the full class name for.
     * @return String The full class name.
     */
    private String getClassFullName(EntityField field) {
        return field.getType().contains(".") ? field.getType() : DEFAULT_PACKAGE + field.getType();
    }
}
