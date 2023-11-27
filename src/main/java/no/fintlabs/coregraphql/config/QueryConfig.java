package no.fintlabs.coregraphql.config;

import graphql.Scalars;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import lombok.RequiredArgsConstructor;
import no.fintlabs.coregraphql.reflection.FintClass;
import no.fintlabs.coregraphql.reflection.ReflectionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class QueryConfig {

    private final ReflectionService reflectionService;

    @Bean
    public GraphQLObjectType buildQueryType() {
        List<GraphQLFieldDefinition> fieldDefinitions = reflectionService.getFintClasses().values().stream()
                .map(this::buildFieldDefinition)
                .collect(Collectors.toList());

        return GraphQLObjectType.newObject()
                .name("Query")
                .fields(fieldDefinitions)
                .build();
    }

    private GraphQLFieldDefinition buildFieldDefinition(FintClass fintClass) {
        GraphQLObjectType.Builder objectTypeBuilder = GraphQLObjectType.newObject()
                .name(fintClass.getClazz().getSimpleName());

        // Define fields for the object type
        // Assuming all fields are of type String for simplicity
        fintClass.getIdentifikatorFields().forEach(field ->
                objectTypeBuilder.field(GraphQLFieldDefinition.newFieldDefinition()
                        .name(field)
                        .type(Scalars.GraphQLString)
                        .build()));

        GraphQLObjectType objectType = objectTypeBuilder.build();

        return GraphQLFieldDefinition.newFieldDefinition()
                .name(fintClass.getClazz().getSimpleName().toLowerCase())
                .arguments(buildArguments(fintClass.getIdentifikatorFields()))
                .type(objectType)
                .build();
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

}
