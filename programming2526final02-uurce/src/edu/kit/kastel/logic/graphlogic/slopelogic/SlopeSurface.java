package edu.kit.kastel.logic.graphlogic.slopelogic;

/**
 * An enum class storing the various surface characteristics a slope can have.
 * @author uurce
 */
public enum SlopeSurface {
    /**
     * The enum value for a regular surface for a slope.
     */
    REGULAR(1.00f, "REGULAR"),
    /**
     * The enum value for a bumpy surface for a slope.
     */
    BUMPY(1.20f, "BUMPY"),
    /**
     * The enum value for an icy surface for a slope.
     */
    ICY(1.30f, "ICY");

    private final String surfaceRepresentation;
    private final float surfaceModifier;
    SlopeSurface(float surfaceModifier, String surfaceRepresentation) {
        this.surfaceRepresentation = surfaceRepresentation;
        this.surfaceModifier = surfaceModifier;
    }

    /**
     * Returns the surface modifier, which is used for the time spent in slopes calculation.
     * @return The surface modifier of the surface enum value.
     */
    public float getSurfaceModifier() {
        return surfaceModifier;
    }

    /**
     * Parses a slope surface enum value from an inputted string.
     * @param representation The string to parse the slope surface enum value from.
     * @return Returns the slope surface enum value if one was successfully parsed from the string, null if otherwise.
     */
    public static SlopeSurface getSlopeSurface(String representation) {
        for (SlopeSurface surface : values()) {
            if (surface.surfaceRepresentation.equals(representation)) {
                return surface;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return surfaceRepresentation;
    }
}
