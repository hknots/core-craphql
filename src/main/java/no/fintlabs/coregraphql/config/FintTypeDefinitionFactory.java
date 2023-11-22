package no.fintlabs.coregraphql.config;

import graphql.kickstart.tools.TypeDefinitionFactory;
import graphql.language.Definition;
import graphql.language.FieldDefinition;
import graphql.language.ObjectTypeDefinition;
import graphql.language.TypeName;
import lombok.RequiredArgsConstructor;
import no.fintlabs.coregraphql.reflection.ReflectionService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public class FintTypeDefinitionFactory implements TypeDefinitionFactory {

    private final ReflectionService reflectionService;

    @NotNull
    @Override
    public List<Definition<?>> create(@NotNull final List<Definition<?>> existing) {
        existing.add(
                ObjectTypeDefinition.newObjectTypeDefinition()
                        .name("test")
                        .fieldDefinition(FieldDefinition.newFieldDefinition()
                                .name("bob")
                                .type(TypeName.newTypeName().name("String").build())
                                .build())
                        .build()
        );
        return existing;
    }

}
