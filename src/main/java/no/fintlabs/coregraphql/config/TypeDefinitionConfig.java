package no.fintlabs.coregraphql.config;

import graphql.kickstart.tools.TypeDefinitionFactory;
import lombok.RequiredArgsConstructor;
import no.fintlabs.coregraphql.reflection.ReflectionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TypeDefinitionConfig {

    @Bean
    public TypeDefinitionFactory typeDefinitionFactory(ReflectionService reflectionService) {
        return new FintTypeDefinitionFactory(reflectionService);
    }

}
