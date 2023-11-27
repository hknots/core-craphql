package no.fintlabs.coregraphql.config;

import graphql.schema.DataFetcher;
import graphql.schema.FieldCoordinates;
import graphql.schema.GraphQLCodeRegistry;
import graphql.schema.GraphQLObjectType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CodeRegistryConfig {

    @Bean
    public GraphQLCodeRegistry buildCodeRegistry(GraphQLObjectType queryType) {
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

}
