package no.fintlabs.coregraphql.config;

import graphql.Scalars;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.fintlabs.coregraphql.reflection.ReflectionService;
import no.fintlabs.coregraphql.reflection.model.FintMainObject;
import no.fintlabs.coregraphql.reflection.model.FintObject;
import no.fintlabs.coregraphql.reflection.model.FintRelation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class QueryConfig {

    private final Map<String, GraphQLObjectType> processedTypes = new HashMap<>();
    private final ReflectionService reflectionService;

    @Bean
    public GraphQLObjectType buildQueryType(List<GraphQLFieldDefinition> fieldDefinitions) {
        return GraphQLObjectType.newObject()
                .name("Query")
                .fields(fieldDefinitions)
                .build();
    }

    @Bean
    public List<GraphQLFieldDefinition> fieldDefinitions() {
        return reflectionService.getFintMainObjects().values().stream()
                .map(this::buildFieldDefinition)
                .collect(Collectors.toList());
    }

    private GraphQLFieldDefinition buildFieldDefinition(FintMainObject fintMainObject) {
        return GraphQLFieldDefinition.newFieldDefinition()
                .name(fintMainObject.getSimpleName().toLowerCase())
                .arguments(buildArguments(fintMainObject.getIdentifikatorFields()))
                .type(getOrCreateObjectType(fintMainObject))
                .build();
    }

    private List<GraphQLArgument> buildArguments(Set<String> identifikatorFields) {
        return identifikatorFields.stream()
                .map(field -> GraphQLArgument.newArgument()
                        .name(field)
                        .type(Scalars.GraphQLString)
                        .build())
                .collect(Collectors.toList());
    }

    private GraphQLObjectType getOrCreateObjectType(FintObject fintObject) {
        String packageName = fintObject.getPackageName();
        if (processedTypes.containsKey(packageName)) {
            log.info("Using processed type: {}", packageName);
            return processedTypes.get(packageName);
        }

        GraphQLObjectType objectType = createObjectType(fintObject);
        processedTypes.put(packageName, objectType);
        return objectType;
    }

    private GraphQLObjectType createObjectType(FintObject fintObject) {
        log.info("Creating object: {}", fintObject.getPackageName());
        GraphQLObjectType.Builder objectBuilder = GraphQLObjectType.newObject()
                .name(fintObject.getUniqueName());

        for (FintRelation relation : fintObject.getRelations()) {
            log.info("Relation: {}", relation.packageName());
            FintObject relatedObject = findRelatedFintObject(relation.packageName());
            objectBuilder.field(GraphQLFieldDefinition.newFieldDefinition()
                    .name(relation.relationName())
                    .type(getOrCreateObjectType(relatedObject))
                    .build());
        }

        fintObject.getFields().forEach(field -> {
            GraphQLFieldDefinition.Builder fieldBuilder = GraphQLFieldDefinition.newFieldDefinition()
                    .name(field.getName());
            if (typeIsFromJava(field.getType())) {
                objectBuilder.field(fieldBuilder.type(Scalars.GraphQLString).build());
            } else {
                FintObject fieldTypeObject = findRelatedFintObject(field.getType().getName());
                objectBuilder.field(fieldBuilder.type(getOrCreateObjectType(fieldTypeObject)).build());
            }
        });

        GraphQLObjectType builtType = objectBuilder.build();
        processedTypes.put(fintObject.getPackageName(), builtType);
        return builtType;
    }

    private FintObject findRelatedFintObject(String packageName) {
        if (reflectionService.getFintMainObjects().containsKey(packageName)) {
            return reflectionService.getFintMainObjects().get(packageName);
        } else if (reflectionService.getAllFintObjects().containsKey(packageName)) {
            return reflectionService.getAllFintObjects().get(packageName);
        }
        throw new RuntimeException("FintObject with package name '" + packageName + "' not found");
    }

    private boolean typeIsFromJava(Class<?> clazz) {
        return clazz.getClassLoader() == null || clazz.getPackage().getName().startsWith("java") || clazz.getPackage().getName().startsWith("javax");
    }
}
