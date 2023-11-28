package no.fintlabs.coregraphql.reflection.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@SuperBuilder
public class FintMainObject extends FintObject {

    private Set<String> identifikatorFields;

}
