package no.fintlabs.coregraphql.config;

import graphql.GraphQL;
import graphql.schema.GraphQLCodeRegistry;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class SchemaConfig {

    @Bean
    public GraphQL graphQL(GraphQLSchema graphQLSchema) {
        return GraphQL.newGraphQL(graphQLSchema).build();
    }

    @Bean
    public GraphQLSchema graphQLSchema(GraphQLObjectType graphQLObjectType, GraphQLCodeRegistry graphQLCodeRegistry, Set<GraphQLType> additionalTypes) {
        return GraphQLSchema.newSchema()
                .query(graphQLObjectType)
                .codeRegistry(graphQLCodeRegistry)
                .additionalTypes(additionalTypes)
                .build();
    }

}
