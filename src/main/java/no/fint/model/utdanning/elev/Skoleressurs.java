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

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Skoleressurs implements FintMainObject {
    public enum Relasjonsnavn {
            PERSON("no.fint.model.felles.Person", "0..1"),
            PERSONALRESSURS("no.fint.model.administrasjon.personal.Personalressurs", "1"),
            UNDERVISNINGSFORHOLD("no.fint.model.utdanning.elev.Undervisningsforhold", "0..*"),
            SKOLE("no.fint.model.utdanning.utdanningsprogram.Skole", "0..*"),
            SENSOR("no.fint.model.utdanning.vurdering.Sensor", "0..*");
	
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

    private Identifikator feidenavn;
    @NotNull
    private Identifikator systemId;
}
