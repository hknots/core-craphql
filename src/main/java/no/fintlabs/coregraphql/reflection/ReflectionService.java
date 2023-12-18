package no.fintlabs.coregraphql.reflection;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.FintComplexDatatypeObject;
import no.fintlabs.coregraphql.reflection.model.FintComplexObject;
import no.fintlabs.coregraphql.reflection.model.FintMainObject;
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
    private final Map<String, FintComplexObject> fintComplexObjects;
    private final Map<String, Integer> simpleNameCounts = new HashMap<>();

    public ReflectionService() {
        gatherSimpleNameCounts();
        fintMainObjects = createFintMainObjects();
        fintComplexObjects = createFintComplexObjects();
        fintMainObjects.forEach((key, value) -> log.info("FintMainObject: {} - {}", key, value.getUniqueName()));
    }

    private void gatherSimpleNameCounts() {
        new Reflections("no.fint.model")
                .getSubTypesOf(no.fint.model.FintObject.class)
                .forEach(clazz -> {
                    String simpleName = clazz.getSimpleName();
                    simpleNameCounts.put(simpleName, simpleNameCounts.getOrDefault(simpleName, 0) + 1);
                });
    }

    public int getSimpleNameCount(String simpleName) {
        return simpleNameCounts.getOrDefault(simpleName, 0);
    }

    private Map<String, FintComplexObject> createFintComplexObjects() {
        return new Reflections("no.fint.model")
                .getSubTypesOf(FintComplexDatatypeObject.class)
                .stream()
                .collect(Collectors.toMap(Class::getName, clazz -> new FintComplexObject(clazz, this)));
    }

    private Map<String, FintMainObject> createFintMainObjects() {
        return new Reflections("no.fint.model")
                .getSubTypesOf(no.fint.model.FintMainObject.class)
                .stream()
                .collect(Collectors.toMap(
                        Class::getName,
                        clazz -> new FintMainObject(clazz, this)));
    }
}
