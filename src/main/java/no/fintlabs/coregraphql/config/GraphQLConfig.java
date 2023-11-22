package no.fintlabs.coregraphql.config;

import graphql.GraphQL;
import graphql.Scalars;
import graphql.schema.*;
import lombok.RequiredArgsConstructor;
import no.fintlabs.coregraphql.reflection.ReflectionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class GraphQLConfig {

    private final ReflectionService reflectionService;

    @Bean
    public List<GraphQLFieldDefinition> graphQLSchemaBeanDefinitions() {
        return reflectionService.getPackageClassMap().values().stream()
                .flatMap(Set::stream)
                .map(clazz -> GraphQLFieldDefinition.newFieldDefinition()
                        .name(clazz.getSimpleName())
                        .type(Scalars.GraphQLString)
                        .build())
                .collect(Collectors.toList());
    }

    @Bean
    public GraphQLCodeRegistry graphQLCodeRegistry(List<GraphQLFieldDefinition> graphQLSchemaBeanDefinitions) {
        GraphQLCodeRegistry.Builder codeRegistryBuilder = GraphQLCodeRegistry.newCodeRegistry();

        graphQLSchemaBeanDefinitions.forEach(fieldDefinition -> {
            DataFetcher<?> dataFetcher = createDataFetcherForClass(fieldDefinition.getName());
            codeRegistryBuilder.dataFetcher(FieldCoordinates.coordinates("query", fieldDefinition.getName()), dataFetcher);
        });

        return codeRegistryBuilder.build();
    }

    @Bean
    public GraphQLObjectType query(List<GraphQLFieldDefinition> graphQLSchemaBeanDefinitions) {
        return GraphQLObjectType.newObject()
                .name("query")
                .fields(graphQLSchemaBeanDefinitions)
                .build();
    }

    @Bean
    public GraphQLSchema graphQLSchema(GraphQLObjectType queryData, GraphQLCodeRegistry graphQLCodeRegistry) {
        return GraphQLSchema.newSchema()
                .query(queryData)
                .codeRegistry(graphQLCodeRegistry)
                .build();
    }

    @Bean
    public GraphQL graphQL(GraphQLSchema graphQLSchema) {
        return GraphQL.newGraphQL(graphQLSchema).build();
    }

    private DataFetcher<?> createDataFetcherForClass(String className) {
        return environment -> {
            return "Data for " + className;
        };
    }

}
