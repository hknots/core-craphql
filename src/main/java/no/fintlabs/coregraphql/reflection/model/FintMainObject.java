package no.fintlabs.coregraphql.reflection.model;

import lombok.Getter;
import no.fint.model.felles.kompleksedatatyper.Identifikator;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@Getter
public class FintMainObject extends FintObject {

    private final Set<String> identifikatorFields;

    public FintMainObject(Class<?> clazz) {
        super(clazz);
        identifikatorFields = getIdentifikatorFields(clazz);
    }

    private Set<String> getIdentifikatorFields(Class<?> clazz) {
        Set<String> identifikatorFields = new HashSet<>();
        for (Field field : super.getFields()) {
            if (field.getType().equals(Identifikator.class)) {
                identifikatorFields.add(field.getName());
            }
        }
        return identifikatorFields;
    }

}
