package no.fint.model.arkiv.noark;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;
import no.fint.model.FintComplexDatatypeObject;
import no.fint.model.arkiv.noark.Avskrivning;
import java.util.Date;
import no.fint.model.arkiv.noark.Registrering;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class Journalpost extends Registrering implements FintComplexDatatypeObject {
    public enum Relasjonsnavn {
            JOURNALPOSTTYPE("no.fint.model.arkiv.kodeverk.JournalpostType", "1"),
            JOURNALSTATUS("no.fint.model.arkiv.kodeverk.JournalStatus", "1"),
            JOURNALENHET("no.fint.model.arkiv.noark.AdministrativEnhet", "0..1");
	
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

    private Long antallVedlegg;
    private Avskrivning avskrivning;
    private Date dokumentetsDato;
    private Date forfallsDato;
    private String journalAr;
    private Date journalDato;
    private Long journalPostnummer;
    private Long journalSekvensnummer;
    private Date mottattDato;
    private Date offentlighetsvurdertDato;
    private Date sendtDato;
}
