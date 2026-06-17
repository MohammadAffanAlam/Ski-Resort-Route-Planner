package edu.kit.kastel.logic.graphlogic.slopelogic;

import edu.kit.kastel.logic.graphlogic.Node;

/**
 * The class which stores the data for a slope in the ski resort.
 * @author uurce
 */
public class Slope extends Node {
    private static final String ATTRIBUTE_SEPARATOR = " ";
    private final SlopeDifficulty difficulty;
    private final SlopeSurface surface;
    private final int distance;
    private final int altitude;

    /**
     * Creates a new slope object.
     * @param id The id of the slope.
     * @param difficulty The difficulty of the slope.
     * @param surface The surface characteristics of the slope.
     * @param distance The distance of the slope.
     * @param altitude The altitude of the slope.
     */
    public Slope(String id, SlopeDifficulty difficulty, SlopeSurface surface, int distance, int altitude) {
        super(id);
        this.difficulty = difficulty;
        this.surface = surface;
        this.distance = distance;
        this.altitude = altitude;
    }

    /**
     * Returns the enum value for the slope's difficulty.
     * @return The enum value for the slope's difficulty.
     */
    public SlopeDifficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Returns the enum value for the slope's surface characteristics.
     * @return The enum value for the slope's surface characteristics.
     */
    public SlopeSurface getSurface() {
        return surface;
    }

    /**
     * Returns the distance of the slope.
     * @return The distance as an integer value.
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Returns the altitude of the slope.
     * @return The altitude as an integer value.
     */
    public int getAltitude() {
        return altitude;
    }

    @Override
    public boolean isSlope() {
        return true;
    }

    @Override
    public String toString() {
        return getId() + ATTRIBUTE_SEPARATOR + difficulty + ATTRIBUTE_SEPARATOR
                + surface + ATTRIBUTE_SEPARATOR + distance + ATTRIBUTE_SEPARATOR + altitude;
    }
}
