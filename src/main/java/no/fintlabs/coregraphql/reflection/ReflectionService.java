package no.fintlabs.coregraphql.reflection;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.FintMainObject;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class ReflectionService {

    private final Map<String, Set<Class<?>>> packageClassMap;

    public ReflectionService() {
        this.packageClassMap = getFintMainObjects();
        packageClassMap.forEach((className, clazz) -> log.info(className + " " + clazz));
    }

    public Map<String, Set<Class<?>>> getFintMainObjects() {
        Reflections reflections = new Reflections("no.fint.model");
        Set<Class<? extends FintMainObject>> fintMainObjects = reflections.getSubTypesOf(FintMainObject.class);

        Map<String, Set<Class<?>>> packageToClassesMap = new HashMap<>();

        for (Class<?> clazz : fintMainObjects) {
            String packageName = clazz.getPackage().getName();
            String subPackage = packageName.substring("no.fint.model.".length());
            String[] packageParts = subPackage.split("\\.");

            if (isComponentOrCommon(subPackage, packageParts)) {
                packageToClassesMap.computeIfAbsent(subPackage, k -> new HashSet<>()).add(clazz);
            }
        }

        return packageToClassesMap;
    }

    private boolean isComponentOrCommon(String subPackage, String[] packageParts) {
        return packageParts.length == 2 || "felles".equals(subPackage);
    }

}
