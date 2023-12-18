package no.fint.model.ressurser.tilganger;

import java.util.Arrays;
import java.util.List;

public enum TilgangerActions {
	
	GET_IDENTITET,
	GET_ALL_IDENTITET,
	UPDATE_IDENTITET,
	GET_RETTIGHET,
	GET_ALL_RETTIGHET,
	UPDATE_RETTIGHET
	;


    /**
     * Gets a list of all enums as string
     *
     * @return A string list of all enums
     */
    public static List<String> getActions() {
        return Arrays.asList(
                Arrays.stream(TilgangerActions.class.getEnumConstants()).map(Enum::name).toArray(String[]::new)
        );
    }

}

