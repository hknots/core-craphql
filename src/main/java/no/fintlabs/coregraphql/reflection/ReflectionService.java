package no.fintlabs.coregraphql.reflection;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.FintComplexDatatypeObject;
import no.fintlabs.coregraphql.reflection.model.FintComplexObject;
import no.fintlabs.coregraphql.reflection.model.FintMainObject;
import no.fintlabs.coregraphql.reflection.model.FintObject;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Service
@Slf4j
public class ReflectionService {

    private final Map<String, FintMainObject> fintMainObjects = createFintMainObjects();
    private final Map<String, FintComplexObject> fintComplexObjects = createFintComplexDataTypes();

    private Map<String, FintComplexObject> createFintComplexDataTypes() {
        Map<String, FintComplexObject> fintComplexObjectMap = new Reflections("no.fint.model")
                .getSubTypesOf(FintComplexDatatypeObject.class)
                .stream()
                .collect(Collectors.toMap(Class::getName, FintComplexObject::new));

        fintComplexObjectMap.values().forEach(this::updateFintObjectUniqueNames);

        return fintComplexObjectMap;
    }

    private Map<String, FintMainObject> createFintMainObjects() {
        Map<String, FintMainObject> fintMainObjectMap = new Reflections("no.fint.model")
                .getSubTypesOf(no.fint.model.FintMainObject.class)
                .stream()
                .collect(Collectors.toMap(Class::getName, FintMainObject::new));

        fintMainObjectMap.values().forEach(this::updateFintObjectUniqueNames);

        return fintMainObjectMap;
    }

    public void updateFintObjectUniqueNames(FintObject fintObject) {
        String simpleName = fintObject.getSimpleName();
        long count = fintMainObjects.values().stream()
                .map(FintMainObject::getSimpleName)
                .filter(simpleName::equals)
                .count();
        if (count > 1) {
            fintObject.setUniqueName(fintObject.getPackageName());
        } else {
            fintObject.setUniqueName(simpleName);
        }
    }

}
