package no.fintlabs.coregraphql.reflection;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.FintComplexDatatypeObject;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fintlabs.coregraphql.reflection.model.FintComplexObject;
import no.fintlabs.coregraphql.reflection.model.FintMainObject;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

@Getter
@Service
@Slf4j
public class ReflectionService {

    private final Map<String, FintMainObject> fintMainObjects = createFintMainObjects();
    private final Map<String, FintComplexObject> fintComplexObjects = createFintComplexDataTypes();

    private Map<String, FintComplexObject> createFintComplexDataTypes() {
        Reflections reflections = new Reflections("no.fint.model");
        Set<Class<? extends FintComplexDatatypeObject>> fintComplexTypes = reflections.getSubTypesOf(FintComplexDatatypeObject.class);

        Map<String, FintComplexObject> fintComplexObjects = new HashMap<>();

        for (Class<?> clazz : fintComplexTypes) {
            fintComplexObjects.put(clazz.getSimpleName(), FintComplexObject.builder()
                    .clazz(clazz)
                    .fields(getAllFields(clazz))
                    .relations(getEnumRelations(clazz))
                    .packageName(clazz.getPackage().getName())
                    .build());
        }
        return fintComplexObjects;
    }

    private Map<String, FintMainObject> createFintMainObjects() {
        Reflections reflections = new Reflections("no.fint.model");
        Set<Class<? extends no.fint.model.FintMainObject>> fintMainObjects = reflections.getSubTypesOf(no.fint.model.FintMainObject.class);

        Map<String, FintMainObject> fintClasses = new HashMap<>();

        for (Class<?> clazz : fintMainObjects) {
            fintClasses.put(clazz.getSimpleName(), FintMainObject.builder()
                    .clazz(clazz)
                    .fields(getAllFields(clazz))
                    .relations(getEnumRelations(clazz))
                    .identifikatorFields(getIdentifikatorFields(clazz))
                    .packageName(clazz.getPackage().getName())
                    .build());
        }
        return fintClasses;
    }

    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    private List<String> getEnumRelations(Class<?> clazz) {
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

    private Set<String> getIdentifikatorFields(Class<?> clazz) {
        Set<String> identifikatorFields = new HashSet<>();
        for (Field field : getAllFields(clazz)) {
            if (field.getType().equals(Identifikator.class)) {
                identifikatorFields.add(field.getName());
            }
        }
        return identifikatorFields;
    }

}
