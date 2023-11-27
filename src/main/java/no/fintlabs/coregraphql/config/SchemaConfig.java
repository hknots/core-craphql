package no.fintlabs.coregraphql.config;

import graphql.GraphQL;
import graphql.schema.GraphQLCodeRegistry;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SchemaConfig {

    @Bean
    public GraphQL graphQL(GraphQLSchema graphQLSchema) {
        return GraphQL.newGraphQL(graphQLSchema).build();
    }

    @Bean
    public GraphQLSchema graphQLSchema(GraphQLObjectType graphQLObjectType, GraphQLCodeRegistry graphQLCodeRegistry) {
        return GraphQLSchema.newSchema()
                .query(graphQLObjectType)
                .codeRegistry(graphQLCodeRegistry)
                .build();
    }

}
