package edu.kit.kastel.logic.graphlogic.liftlogic;

/**
 * The enum class storing every type of lift.
 * @author uurce
 */
public enum LiftTypes {
    /**
     * The enum value for a chairlift type of lift.
     */
    CHAIRLIFT,
    /**
     * The enum value for a gondola type of lift.
     */
    GONDOLA;

    private static final String CHAIRLIFT_STRING_REPRESENTATION = "CHAIRLIFT";
    private static final String GONDOLA_STRING_REPRESENTATION = "GONDOLA";

    /**
     * Returns the liftType enum value for a specified string.
     * @param representation The string to search and return the liftType enum of.
     * @return The liftType enum of the inputted string, or null if there wasn't one found.
     */
    public static LiftTypes getLiftType(String representation) {
        if (representation.matches(CHAIRLIFT_STRING_REPRESENTATION)) {
            return CHAIRLIFT;
        }
        if (representation.matches(GONDOLA_STRING_REPRESENTATION)) {
            return GONDOLA;
        }
        return null;
    }
}
