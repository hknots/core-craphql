package no.fintlabs.coregraphql.reflection;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class FintClass {

    private Class<?> clazz;
    private Set<String> identifikatorFields;

    public String getClazzSimpleName() {
        return clazz.getSimpleName();
    }

}
