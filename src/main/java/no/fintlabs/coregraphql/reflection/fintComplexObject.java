package no.fintlabs.coregraphql.reflection;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;

@Data
@Builder
public class fintComplexObject {

    private Class<?> clazz;
    private List<Field> fields;
    private String packageName;

    public String getClazzSimpleName() {
        return clazz.getSimpleName();
    }

}
