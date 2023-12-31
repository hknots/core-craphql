package no.fintlabs.coregraphql.reflection.model;

import lombok.Getter;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fintlabs.coregraphql.reflection.ReflectionService;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@Getter
public class FintMainObject extends FintObject {

    private final Set<String> identifikatorFields;

    public FintMainObject(Class<?> clazz, ReflectionService reflectionService) {
        super(clazz, reflectionService);
        identifikatorFields = setIdentifikatorFields();
    }

    private Set<String> setIdentifikatorFields() {
        Set<String> identifikatorFields = new HashSet<>();
        for (Field field : super.getFields()) {
            if (field.getType().equals(Identifikator.class)) {
                identifikatorFields.add(field.getName());
            }
        }
        return identifikatorFields;
    }

}
