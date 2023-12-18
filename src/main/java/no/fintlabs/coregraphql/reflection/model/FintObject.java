package no.fintlabs.coregraphql.reflection.model;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public abstract class FintObject {

    private String uniqueName;
    private final String packageName;
    private final String simpleName;
    private final Class<?> clazz;
    private final List<Field> fields;
    private final List<String> relations;

    public FintObject(Class<?> clazz) {
        this.clazz = clazz;
        packageName = clazz.getPackage().getName();
        simpleName = clazz.getSimpleName();
        fields = getAllFields(clazz);
        relations = getAllRelations(clazz);
    }

    private List<String> getAllRelations(Class<?> clazz) {
        List<String> relations = new ArrayList<>();
        Class<?>[] declaredClasses = clazz.getDeclaredClasses();

        for (Class<?> innerClass : declaredClasses) {
            if (innerClass.isEnum()) {
                Object[] enumConstants = innerClass.getEnumConstants();
                for (Object enumConstant : enumConstants) {
                    relations.add(enumConstant.toString());
                }
            }
        }
        return relations;
    }

    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

}
