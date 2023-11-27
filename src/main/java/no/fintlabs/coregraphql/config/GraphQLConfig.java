package no.fintlabs.coregraphql.config;

import graphql.GraphQL;
import graphql.Scalars;
import graphql.schema.*;
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
public class GraphQLConfig {

    private final ReflectionService reflectionService;

    @Bean
    public GraphQLSchema graphQLSchema() {
        GraphQLObjectType queryType = buildQueryType();
        GraphQLCodeRegistry codeRegistry = buildCodeRegistry(queryType);

        return GraphQLSchema.newSchema()
                .query(queryType)
                .codeRegistry(codeRegistry)
                .build();
    }

    private GraphQLObjectType buildQueryType() {
        List<GraphQLFieldDefinition> fieldDefinitions = reflectionService.getFintClasses().stream()
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
                .argument(buildArguments(fintClass.getIdentifikatorFields()))
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

    private GraphQLCodeRegistry buildCodeRegistry(GraphQLObjectType queryType) {
        GraphQLCodeRegistry.Builder codeRegistryBuilder = GraphQLCodeRegistry.newCodeRegistry();

        queryType.getFieldDefinitions().forEach(fieldDefinition -> {
            DataFetcher<?> dataFetcher = createDataFetcherForClass(fieldDefinition.getName());
            codeRegistryBuilder.dataFetcher(FieldCoordinates.coordinates("Query", fieldDefinition.getName()), dataFetcher);
        });

        return codeRegistryBuilder.build();
    }

    private DataFetcher<?> createDataFetcherForClass(String className) {
        return environment -> {
            return "Data for " + className;
        };
    }

    @Bean
    public GraphQL graphQL(GraphQLSchema graphQLSchema) {
        return GraphQL.newGraphQL(graphQLSchema).build();
    }
}
