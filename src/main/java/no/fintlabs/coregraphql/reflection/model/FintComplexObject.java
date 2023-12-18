package no.fintlabs.coregraphql.reflection.model;

import no.fintlabs.coregraphql.reflection.ReflectionService;

public class FintComplexObject extends FintObject {

    public FintComplexObject(Class<?> clazz, ReflectionService reflectionService) {
        super(clazz, reflectionService);
    }
}
