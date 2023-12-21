package no.fint.model.utdanning.elev;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;
import no.fint.model.FintMainObject;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.felles.kompleksedatatyper.Adresse;
import no.fint.model.felles.kompleksedatatyper.Kontaktinformasjon;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Elev implements FintMainObject {
    public enum Relasjonsnavn {
            PERSON("no.fint.model.felles.Person", "1"),
            ELEVFORHOLD("no.fint.model.utdanning.elev.Elevforhold", "0..*");
	
		private final String typeName;
        private final String multiplicity;

        private Relasjonsnavn(String typeName, String multiplicity) {
            this.typeName = typeName;
            this.multiplicity = multiplicity;
        }

        public String getTypeName() {
            return typeName;
        }

        public String getMultiplicity() {
            return multiplicity;
        }
    }

    private Identifikator brukernavn;
    private Identifikator elevnummer;
    private Identifikator feidenavn;
    private Boolean gjest;
    private Adresse hybeladresse;
    private Kontaktinformasjon kontaktinformasjon;
    @NotNull
    private Identifikator systemId;
}
