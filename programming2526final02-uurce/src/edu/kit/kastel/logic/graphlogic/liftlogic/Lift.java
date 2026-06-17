package edu.kit.kastel.logic.graphlogic.liftlogic;

import edu.kit.kastel.logic.graphlogic.Node;

import java.time.LocalTime;

/**
 * Class which stores the data and handles the logic for a lift.
 * @author uurce
 */
public class Lift extends Node {
    private static final String ATTRIBUTE_SEPARATOR = " ";
    private static final String IS_TRANSIT_LIFT_STRING_DISPLAY = " TRANSIT";
    private final boolean transitLift;
    private final LiftTypes type;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final int travelTime;
    private final int waitingTime;

    /**
     * Creates a new lift object with the given attributes.
     * @param id The ID of the new lift object.
     * @param transitLift Whether the new lift object is a transit lift.
     * @param type The type of the lift.
     * @param startTime The opening time for the lift.
     * @param endTime The closing time for the lift.
     * @param travelTime The travelling time of the lift.
     * @param waitingTime The time that the skier has to wait to use the lift.
     */
    public Lift(String id, boolean transitLift, LiftTypes type, LocalTime startTime, LocalTime endTime, int travelTime, int waitingTime) {
        super(id);
        this.transitLift = transitLift;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.travelTime = travelTime;
        this.waitingTime = waitingTime;
    }

    @Override
    public boolean isLift() {
        return true;
    }

    /**
     * Checks whether a lift is a transit lift.
     * @return True if the lift is a transit lift, false otherwise.
     */
    public boolean isTransitLift() {
        return transitLift;
    }

    /**
     * Returns the waiting time for the lift.
     * @return The waiting time for the lift.
     */
    public int getWaitingTime() {
        return waitingTime;
    }

    /**
     * Returns the travel time for the lift.
     * @return The travel time for the lift.
     */
    public int getTravelTime() {
        return travelTime;
    }

    /**
     * Returns the opening time for the lift.
     * @return The opening time for the lift.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Returns the closing time for the lift.
     * @return The closing time for the lift.
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        String output = getId() + ATTRIBUTE_SEPARATOR + type + ATTRIBUTE_SEPARATOR + startTime
                + ATTRIBUTE_SEPARATOR + endTime + ATTRIBUTE_SEPARATOR + travelTime + ATTRIBUTE_SEPARATOR + waitingTime;
        if (transitLift) {
            output += IS_TRANSIT_LIFT_STRING_DISPLAY;
        }
        return output;
    }
}
