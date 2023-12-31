package no.fint.model.utdanning.basisklasser;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;
import no.fint.model.FintAbstractObject;
import no.fint.model.felles.kompleksedatatyper.Periode;
import no.fint.model.felles.kompleksedatatyper.Identifikator;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class Gruppe implements FintAbstractObject {
    public enum Relasjonsnavn {
            GREPREFERANSE("no.fint.model.utdanning.kodeverk.Grepreferanse", "0..1"),
            VIGOREFERANSE("no.fint.model.utdanning.kodeverk.Vigoreferanse", "0..1"),
            MEDLEMSKAP("no.fint.model.utdanning.elev.Medlemskap", "0..*");
	
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

    
    private String beskrivelse;
    
    private String navn;
    @Deprecated
    private List<Periode> periode;
    @NotNull
    private Identifikator systemId;
}
