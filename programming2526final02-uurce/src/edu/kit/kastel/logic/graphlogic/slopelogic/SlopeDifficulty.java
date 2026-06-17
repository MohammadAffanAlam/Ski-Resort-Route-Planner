package edu.kit.kastel.logic.graphlogic.slopelogic;

/**
 * An enum class storing the various difficulties for slopes.
 * @author uurce
 */
public enum SlopeDifficulty {
    /**
     * The enum value for a blue difficulty of a slope.
     */
    BLUE(1.00f, "BLUE"),
    /**
     * The enum value for a red difficulty of a slope.
     */
    RED(1.15f, "RED"),
    /**
     * The enum value for a black difficulty of a slope.
     */
    BLACK(1.35f, "BLACK");

    private final String difficultyRepresentation;
    private final float difficultyModifier;
    SlopeDifficulty(float difficultyModifier, String difficultyRepresentation) {
        this.difficultyModifier = difficultyModifier;
        this.difficultyRepresentation = difficultyRepresentation;
    }

    /**
     * Returns the difficulty modifier, which is used for the time spent in slopes calculation.
     * @return The difficulty modifier of the difficulty enum value.
     */
    public float getDifficultyModifier() {
        return difficultyModifier;
    }

    /**
     * Parses a slope difficulty enum value from an inputted string.
     * @param representation The string to parse the slope difficulty enum value from.
     * @return Returns the slope difficulty enum value if one was successfully parsed from the string, null if otherwise.
     */
    public static SlopeDifficulty getSlopeDifficulty(String representation) {
        for (SlopeDifficulty difficulty : values()) {
            if (difficulty.difficultyRepresentation.equals(representation)) {
                return difficulty;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return difficultyRepresentation;
    }
}
