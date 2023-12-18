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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class QueryConfig {

    private final Map<String, GraphQLObjectType> procsessedTypes = new HashMap<>();
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
                .name(fintMainObject.getClazz().getSimpleName().toLowerCase())
                .arguments(buildArguments(fintMainObject.getIdentifikatorFields()))
                .type(createObjectType(fintMainObject))
                .build();
    }

    private GraphQLObjectType createObjectType(FintObject fintObject) {
        if (typeIsProcessed(fintObject.getSimpleName())) {
            return procsessedTypes.get(fintObject.getSimpleName());
        }
        log.info("Creating object: {}", fintObject.getSimpleName());
        GraphQLObjectType.Builder objectBuilder = GraphQLObjectType.newObject()
                .name(fintObject.getSimpleName());

        fintObject.getFields().forEach(field -> {
            GraphQLFieldDefinition.Builder fieldBuilder = GraphQLFieldDefinition.newFieldDefinition()
                    .name(field.getName());

            if (typeIsProcessed(field.getType().getSimpleName())) {
                log.info("Type: {} - {} has already been processed", field.getType().getSimpleName(), field.getName());
                objectBuilder.field(fieldBuilder.type(procsessedTypes.get(field.getType().getSimpleName())).build());
            } else if (typeIsFromJava(field.getType())) {
                log.info("Type: {} - {} is from java", field.getType().getSimpleName(), field.getName());
                objectBuilder.field(fieldBuilder.type(Scalars.GraphQLString).build());
            } else {
                log.warn("Type {} is not processed", field.getType().getSimpleName());
                objectBuilder.field(fieldBuilder.type(createNewType(field.getType().getSimpleName())).build());
            }

            log.info("Type: {} - {} is created", field.getType().getSimpleName(), field.getName());
        });
        GraphQLObjectType build = objectBuilder.build();
        procsessedTypes.put(fintObject.getSimpleName(), build);
        return build;

    }

    private GraphQLObjectType createNewType(String typeName) {
        if (reflectionService.getFintMainObjects().containsKey(typeName)) {
            return createObjectType(reflectionService.getFintMainObjects().get(typeName));
        } else if (reflectionService.getFintComplexObjects().containsKey(typeName)) {
            return createObjectType(reflectionService.getFintComplexObjects().get(typeName));
        } else {
            log.error("Type {} not found", typeName);
            return null;
        }
    }

    private List<GraphQLArgument> buildArguments(Set<String> identifikatorFields) {
        List<GraphQLArgument> arguments = new ArrayList<>();
        identifikatorFields.forEach(field ->
                arguments.add(GraphQLArgument.newArgument()
                        .name(field)
                        .type(Scalars.GraphQLString)
                        .build()));
        return arguments;
    }

    private boolean typeIsFromJava(Class<?> clazz) {
        if (clazz.getClassLoader() == null) {
            return true;
        }

        Package classPackage = clazz.getPackage();
        if (classPackage != null) {
            String packageName = classPackage.getName();
            return packageName.startsWith("java") || packageName.startsWith("javax");
        }

        return false;
    }

    private boolean typeIsProcessed(String typeName) {
        return procsessedTypes.containsKey(typeName);
    }

}
