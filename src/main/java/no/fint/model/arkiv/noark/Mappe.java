package no.fint.model.arkiv.noark;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;
import no.fint.model.FintAbstractObject;
import java.util.Date;
import no.fint.model.arkiv.noark.Klasse;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.arkiv.noark.Merknad;
import no.fint.model.arkiv.noark.Part;
import no.fint.model.arkiv.noark.Skjerming;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class Mappe implements FintAbstractObject {
    public enum Relasjonsnavn {
            ARKIVDEL("no.fint.model.arkiv.noark.Arkivdel", "0..1"),
            AVSLUTTETAV("no.fint.model.arkiv.noark.Arkivressurs", "0..1"),
            OPPRETTETAV("no.fint.model.arkiv.noark.Arkivressurs", "1");
	
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

    private Date avsluttetDato;
    private String beskrivelse;
    private List<Klasse> klasse;
    private Identifikator mappeId;
    private List<Merknad> merknad;
    private List<String> noekkelord;
    private String offentligTittel;
    private Date opprettetDato;
    private List<Part> part;
    private Skjerming skjerming;
    private Identifikator systemId;
    private String tittel;
}
