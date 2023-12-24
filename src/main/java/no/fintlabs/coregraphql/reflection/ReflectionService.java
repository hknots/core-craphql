package no.fintlabs.coregraphql.reflection;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fintlabs.coregraphql.reflection.model.FintMainObject;
import no.fintlabs.coregraphql.reflection.model.FintObject;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Service
@Slf4j
public class ReflectionService {

    private final Map<String, FintMainObject> fintMainObjects;
    private final Map<String, FintObject> otherFintObjects;
    private final Map<String, Integer> simpleNameCounts = new HashMap<>();

    public ReflectionService() {
        Reflections reflections = new Reflections("no.fint.model");
        gatherSimpleNameCounts(reflections);
        fintMainObjects = createFintMainObjects(reflections);
        otherFintObjects = createOtherFintObjects(reflections);
    }

    private Map<String, FintObject> createOtherFintObjects(Reflections reflections) {
        return reflections
                .getSubTypesOf(no.fint.model.FintObject.class)
                .stream()
                .filter(clazz -> !fintMainObjects.containsKey(clazz.getName()))
                .collect(Collectors.toMap(Class::getName, clazz -> new FintObject(clazz, this) {
                }));

    }

    private void gatherSimpleNameCounts(Reflections reflections) {
        reflections
                .getSubTypesOf(no.fint.model.FintObject.class)
                .forEach(clazz -> {
                    String simpleName = clazz.getSimpleName();
                    simpleNameCounts.put(simpleName, simpleNameCounts.getOrDefault(simpleName, 0) + 1);
                });
    }

    public int getSimpleNameCount(String simpleName) {
        return simpleNameCounts.getOrDefault(simpleName, 0);
    }

    private Map<String, FintMainObject> createFintMainObjects(Reflections reflections) {
        return reflections
                .getSubTypesOf(no.fint.model.FintMainObject.class)
                .stream()
                .collect(Collectors.toMap(
                        Class::getName,
                        clazz -> new FintMainObject(clazz, this)));
    }
}
