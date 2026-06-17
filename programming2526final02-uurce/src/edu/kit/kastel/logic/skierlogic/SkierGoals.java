package edu.kit.kastel.logic.skierlogic;

/**
 * The enum class storing every possible goal of the skier.
 * @author uurce
 */
public enum SkierGoals {
    /**
     * The enum value for a goal of maximum altitude.
     */
    ALTITUDE,
    /**
     * The enum value for a goal of maximum distance.
     */
    DISTANCE,
    /**
     * The enum value for a goal of highest number of slopes.
     */
    NUMBER,
    /**
     * The enum value for a goal of highest number of unique slopes.
     */
    UNIQUE;

    private static final String ALTITUDE_GOAL_REPRESENTATION = "ALTITUDE";
    private static final String DISTANCE_GOAL_REPRESENTATION = "DISTANCE";
    private static final String NUMBER_GOAL_REPRESENTATION = "NUMBER";
    private static final String UNIQUE_GOAL_REPRESENTATION = "UNIQUE";

    /**
     * Gets the goal enum value for an inputted string.
     * @param representation The string from which a skier goal is to be parsed.
     * @return The skier goal enum value for the inputted string if one was found, null if otherwise.
     */
    public static SkierGoals getSkierGoals(String representation) {
        return switch (representation) {
            case ALTITUDE_GOAL_REPRESENTATION -> ALTITUDE;
            case DISTANCE_GOAL_REPRESENTATION -> DISTANCE;
            case NUMBER_GOAL_REPRESENTATION -> NUMBER;
            case UNIQUE_GOAL_REPRESENTATION -> UNIQUE;
            default -> null;
        };
    }
}
