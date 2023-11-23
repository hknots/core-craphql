package no.fintlabs.coregraphql.reflection;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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

    private final Map<String, List<FintClass>> packageToFintClassesMap;

    public ReflectionService() {
        this.packageToFintClassesMap = getFintMainObjects();
        packageToFintClassesMap.forEach((packageName, fintClasses) ->
                fintClasses.forEach(fintClass ->
                        log.info("{}: {}", fintClass.getClazz().getSimpleName(), fintClass.getIdentifikatorFields())));
    }

    private Map<String, List<FintClass>> getFintMainObjects() {
        Reflections reflections = new Reflections("no.fint.model");
        Set<Class<? extends FintMainObject>> fintMainObjects = reflections.getSubTypesOf(FintMainObject.class);

        Map<String, List<FintClass>> packageToFintClassesMap = new HashMap<>();

        for (Class<?> clazz : fintMainObjects) {
            String packageName = clazz.getPackage().getName();
            if (isValidPackageName(packageName)) {
                Set<String> identifikatorFields = getIdentifikatorFields(clazz);

                FintClass fintClass = FintClass.builder()
                        .clazz(clazz)
                        .identifikatorFields(identifikatorFields)
                        .build();

                packageToFintClassesMap.computeIfAbsent(packageName, k -> new ArrayList<>()).add(fintClass);
            }
        }

        return packageToFintClassesMap;
    }

    private boolean isValidPackageName(String packageName) {
        String[] parts = packageName.replace("no.fint.model.", "").split("\\.");
        return "felles".equals(parts[0]) || parts.length == 2;
    }


    private Set<String> getIdentifikatorFields(Class<?> clazz) {
        Set<String> identifikatorFields = new HashSet<>();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getType().equals(Identifikator.class)) {
                    identifikatorFields.add(field.getName());
                }
            }
            clazz = clazz.getSuperclass();
        }
        return identifikatorFields;
    }
}
