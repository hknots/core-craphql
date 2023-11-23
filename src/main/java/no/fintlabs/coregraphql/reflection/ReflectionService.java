package no.fintlabs.coregraphql.reflection;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.FintMainObject;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

@Getter
@Service
@Slf4j
public class ReflectionService {

    private final Map<String, List<FintClass>> packageFintClassMap;

    public ReflectionService() {
        this.packageFintClassMap = getFintMainObjects();
        packageFintClassMap.values().forEach(fintClasses -> fintClasses.forEach(fintClass -> log.info("{}", fintClass.getRelations())));
    }

    private Map<String, List<FintClass>> getFintMainObjects() {
        Reflections reflections = new Reflections("no.fint.model");
        Set<Class<? extends FintMainObject>> fintMainObjects = reflections.getSubTypesOf(FintMainObject.class);

        Map<String, List<FintClass>> packageToFintClassesMap = new HashMap<>();

        for (Class<?> clazz : fintMainObjects) {
            String packageName = clazz.getPackage().getName();
            if (isValidPackageName(packageName)) {
                Set<String> identifikatorFields = getIdentifikatorFields(clazz);
                List<Field> fields = getAllFields(clazz);
                List<String> relations = getEnumRelations(clazz);

                FintClass fintClass = FintClass.builder()
                        .clazz(clazz)
                        .fields(fields)
                        .relations(relations)
                        .identifikatorFields(identifikatorFields)
                        .build();

                packageToFintClassesMap.computeIfAbsent(packageName, k -> new ArrayList<>()).add(fintClass);
            }
        }

        return packageToFintClassesMap;
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

    private boolean isValidPackageName(String packageName) {
        String[] parts = packageName.replace("no.fint.model.", "").split("\\.");
        return "felles".equals(parts[0]) || parts.length == 2;
    }
}
