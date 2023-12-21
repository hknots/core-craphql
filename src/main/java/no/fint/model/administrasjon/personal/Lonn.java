package no.fint.model.administrasjon.personal;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;
import no.fint.model.FintAbstractObject;
import java.util.Date;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.administrasjon.kompleksedatatyper.Kontostreng;
import no.fint.model.felles.kompleksedatatyper.Periode;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class Lonn implements FintAbstractObject {
    public enum Relasjonsnavn {
            ANVISER("no.fint.model.administrasjon.personal.Personalressurs", "0..1"),
            KONTERER("no.fint.model.administrasjon.personal.Personalressurs", "0..1"),
            ATTESTANT("no.fint.model.administrasjon.personal.Personalressurs", "0..1");
	
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

    private Date anvist;
    private Date attestert;
    
    private String beskrivelse;
    private Identifikator kildesystemId;
    private Date kontert;
    @NotNull
    private Kontostreng kontostreng;
    private Periode opptjent;
    @NotNull
    private Periode periode;
    private Identifikator systemId;
}
