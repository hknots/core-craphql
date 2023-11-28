package no.fintlabs.coregraphql.reflection.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.lang.reflect.Field;
import java.util.List;

@SuperBuilder
@Data
public abstract class FintObject {

    private Class<?> clazz;
    private List<Field> fields;
    private List<String> relations;
    private String packageName;


    public String getClazzSimpleName() {
        return clazz.getSimpleName();
    }

}
