package edu.kit.kastel.logic.skierlogic;

import edu.kit.kastel.logic.graphlogic.slopelogic.Slope;
import edu.kit.kastel.logic.graphlogic.slopelogic.SlopeDifficulty;
import edu.kit.kastel.logic.graphlogic.slopelogic.SlopeSurface;

import java.util.HashSet;
import java.util.Set;

/**
 * The class holding the data for a skier.
 * @author uurce
 */
public class Skier {
    private SkierSkillLevel skillLevel;
    private final Set<SlopeDifficulty> preferredDifficulties;
    private final Set<SlopeSurface> preferredSurfaces;
    private final Set<SlopeDifficulty> unpreferredDifficulties;
    private final Set<SlopeSurface> unpreferredSurfaces;
    private SkierGoals goal;

    /**
     * Creates a new skier.
     */
    public Skier() {
        this.preferredDifficulties = new HashSet<>();
        this.preferredSurfaces = new HashSet<>();
        this.unpreferredDifficulties = new HashSet<>();
        this.unpreferredSurfaces = new HashSet<>();
    }

    /**
     * Sets the skill level of the skier.
     * @param skillLevel The skill level to set the skier to.
     */
    public void setSkillLevel(SkierSkillLevel skillLevel) {
        this.skillLevel = skillLevel;
    }

    /**
     * Sets the goal of the skier.
     * @param goal The goal to set the skier to.
     */
    public void setGoal(SkierGoals goal) {
        this.goal = goal;
    }

    /**
     * Returns the skill level of the skier.
     * @return The enum value representing the skill level of the skier.
     */
    public SkierSkillLevel getSkillLevel() {
        return skillLevel;
    }

    /**
     * Returns the goal of the skier.
     * @return The enum value representing the goal of the skier.
     */
    public SkierGoals getGoal() {
        return goal;
    }

    /**
     * Adds a new slope difficulty preference to the skier.
     * @param difficulty The difficulty to add as a preference.
     */
    public void addPreference(SlopeDifficulty difficulty) {
        preferredDifficulties.add(difficulty);
        unpreferredDifficulties.remove(difficulty);
    }

    /**
     * Adds a new slope surface preference to the skier.
     * @param surface The surface to add as a preference.
     */
    public void addPreference(SlopeSurface surface) {
        preferredSurfaces.add(surface);
        unpreferredSurfaces.remove(surface);
    }

    /**
     * Adds a new slope difficulty dislike to the skier.
     * @param difficulty The difficulty to add as a dislike.
     */
    public void addDislike(SlopeDifficulty difficulty) {
        preferredDifficulties.remove(difficulty);
        unpreferredDifficulties.add(difficulty);
    }

    /**
     * Adds a new slope surface dislike to the skier.
     * @param surface The surface to add as a dislike.
     */
    public void addDislike(SlopeSurface surface) {
        preferredSurfaces.remove(surface);
        unpreferredSurfaces.add(surface);
    }

    /**
     * Resets the preferences of the skier.
     */
    public void resetPreferences() {
        preferredDifficulties.clear();
        preferredSurfaces.clear();
        unpreferredDifficulties.clear();
        unpreferredSurfaces.clear();
    }

    /**
     * Returns a score value for how much the skier prefers a certain slope.
     * @param slope The slope to measure the preference score for.
     * @return The score value representing how much the skier prefers the slope.
     */
    public int getPreferenceScore(Slope slope) {
        int score = 0;
        if (preferredDifficulties.contains(slope.getDifficulty())) {
            score++;
        } else if (unpreferredDifficulties.contains(slope.getDifficulty())) {
            score--;
        }
        if (preferredSurfaces.contains(slope.getSurface())) {
            score++;
        } else if (unpreferredSurfaces.contains(slope.getSurface())) {
            score--;
        }
        return score;
    }
}
