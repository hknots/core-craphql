package no.fintlabs.coregraphql.config;

import graphql.GraphQL;
import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import lombok.RequiredArgsConstructor;
import no.fintlabs.coregraphql.reflection.ReflectionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class GraphQLConfig {

    private final ReflectionService reflectionService;

    @Bean
    public GraphQL graphQL(GraphQLSchema graphQLSchema) {
        return GraphQL.newGraphQL(graphQLSchema).build();
    }

    @Bean
    public GraphQLSchema graphQLSchema(GraphQLObjectType queryData) {
        return GraphQLSchema.newSchema()
                .query(queryData)
                .build();
    }

    @Bean
    public GraphQLObjectType query(List<GraphQLFieldDefinition> graphQLSchemaBeanDefinitions) {
        return GraphQLObjectType.newObject()
                .name("query")
                .fields(graphQLSchemaBeanDefinitions)
                .build();
    }

    @Bean
    public List<GraphQLFieldDefinition> graphQLSchemaBeanDefinitions() {
        return List.of(
                GraphQLFieldDefinition.newFieldDefinition()
                        .name("hello")
                        .type(Scalars.GraphQLString)
                        .dataFetcher(environment -> "world")
                        .build()
        );
    }

}
