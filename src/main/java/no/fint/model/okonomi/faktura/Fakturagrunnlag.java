package no.fint.model.okonomi.faktura;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import no.fint.model.FintMainObject;
import no.fint.model.felles.kompleksedatatyper.Identifikator;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Fakturagrunnlag implements FintMainObject {
    public enum Relasjonsnavn {
        FAKTURA("no.fint.model.okonomi.faktura.Faktura", "0..*"),
        FAKTURAUTSTEDER("no.fint.model.okonomi.faktura.Fakturautsteder", "1");

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

    private Long avgiftsbelop;
    private List<Fakturalinje> fakturalinjer;
    private Date leveringsdato;
    @NotNull
    private Fakturamottaker mottaker;
    private Long nettobelop;
    @NotNull
    private Identifikator ordrenummer;
    private Long totalbelop;
}
