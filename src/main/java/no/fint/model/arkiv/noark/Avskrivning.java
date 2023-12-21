package no.fint.model.arkiv.noark;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;
import no.fint.model.FintComplexDatatypeObject;
import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Avskrivning implements FintComplexDatatypeObject {
    
    private String avskrevetAv;
    @NotNull
    private Date avskrivningsdato;
    
    private String avskrivningsmate;
}
