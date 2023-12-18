package no.fintlabs.coregraphql.reflection;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.FintComplexDatatypeObject;
import no.fintlabs.coregraphql.reflection.model.FintComplexObject;
import no.fintlabs.coregraphql.reflection.model.FintMainObject;
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
        return new Reflections("no.fint.model")
                .getSubTypesOf(FintComplexDatatypeObject.class)
                .stream()
                .collect(Collectors.toMap(Class::getName, FintComplexObject::new));
    }

    private Map<String, FintMainObject> createFintMainObjects() {
        return new Reflections("no.fint.model")
                .getSubTypesOf(FintMainObject.class)
                .stream()
                .collect(Collectors.toMap(Class::getName, FintMainObject::new));
    }

}
