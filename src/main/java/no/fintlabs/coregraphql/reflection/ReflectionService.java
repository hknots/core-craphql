package no.fintlabs.coregraphql.reflection;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.FintComplexDatatypeObject;
import no.fint.model.FintMainObject;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

@Getter
@Service
@Slf4j
public class ReflectionService {

    private final Map<String, fintMainObject> fintMainObjects = createFintMainObjects();
    private final Map<String, fintComplexObject> fintComplexObjects = createFintComplexDataTypes();

    @PostConstruct
    public void init() {
        fintMainObjects.forEach((key, value) -> log.info("Found FintMainObject: {}", key));
        fintComplexObjects.forEach((key, value) -> log.info("Found FintComplexDataType: {}", key));
    }

    private Map<String, fintComplexObject> createFintComplexDataTypes() {
        Reflections reflections = new Reflections("no.fint.model");
        Set<Class<? extends FintComplexDatatypeObject>> fintComplexTypes = reflections.getSubTypesOf(FintComplexDatatypeObject.class);

        Map<String, fintComplexObject> fintComplexObjects = new HashMap<>();

        for (Class<?> clazz : fintComplexTypes) {
            fintComplexObjects.put(clazz.getSimpleName(), fintComplexObject.builder()
                    .clazz(clazz)
                    .fields(getAllFields(clazz))
                    .packageName(clazz.getPackage().getName())
                    .build());
        }
        return fintComplexObjects;
    }

    private Map<String, fintMainObject> createFintMainObjects() {
        Reflections reflections = new Reflections("no.fint.model");
        Set<Class<? extends FintMainObject>> fintMainObjects = reflections.getSubTypesOf(FintMainObject.class);

        Map<String, fintMainObject> fintClasses = new HashMap<>();

        for (Class<?> clazz : fintMainObjects) {
            fintClasses.put(clazz.getSimpleName(), fintMainObject.builder()
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
