package no.fintlabs.coregraphql.reflection;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class FintClass {

    private Class<?> clazz;
    private List<Field> fields;
    private List<String> relations;
    private Set<String> identifikatorFields;
    private String packageName;

    public String getClazzSimpleName() {
        return clazz.getSimpleName();
    }

}
