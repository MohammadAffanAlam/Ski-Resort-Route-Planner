package edu.kit.kastel.logic.skierlogic;

/**
 * The enum class storing every possible skill level of the skier.
 * @author uurce
 */
public enum SkierSkillLevel {
    /**
     * The enum value for a beginner skill level.
     */
    BEGINNER(1.35f),
    /**
     * The enum value for an intermediate skill level.
     */
    INTERMEDIATE(1.10f),
    /**
     * The enum value for an expert skill level.
     */
    EXPERT(0.90f);

    private static final String BEGINNER_SKILL_REPRESENTATION = "BEGINNER";
    private static final String INTERMEDIATE_SKILL_REPRESENTATION = "INTERMEDIATE";
    private static final String EXPERT_SKILL_REPRESENTATION = "EXPERT";
    private final float skillModifier;

    SkierSkillLevel(float skillModifier) {
        this.skillModifier = skillModifier;
    }

    /**
     * Returns the skill modifier used for the time spent in slopes calculation.
     * @return The skill modifier for the enum.
     */
    public float getSkillModifier() {
        return skillModifier;
    }

    /**
     * Parses a skill level enum value from an inputted string.
     * @param representation The string to parse the enum value from.
     * @return The skill level enum value if one was successfully parsed from the string, null if otherwise.
     */
    public static SkierSkillLevel getSkierSkillLevel(String representation) {
        return switch (representation) {
            case BEGINNER_SKILL_REPRESENTATION -> BEGINNER;
            case INTERMEDIATE_SKILL_REPRESENTATION -> INTERMEDIATE;
            case EXPERT_SKILL_REPRESENTATION -> EXPERT;
            default -> null;
        };
    }
}
