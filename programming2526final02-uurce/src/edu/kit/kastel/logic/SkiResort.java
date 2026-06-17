package edu.kit.kastel.logic;

import edu.kit.kastel.logic.graphlogic.liftlogic.Lift;
import edu.kit.kastel.logic.graphlogic.liftlogic.LiftTypes;
import edu.kit.kastel.logic.graphlogic.Node;
import edu.kit.kastel.logic.graphlogic.NodeSorting;
import edu.kit.kastel.logic.graphlogic.slopelogic.Slope;
import edu.kit.kastel.logic.graphlogic.slopelogic.SlopeDifficulty;
import edu.kit.kastel.logic.graphlogic.slopelogic.SlopeSurface;
import edu.kit.kastel.logic.skierlogic.Skier;
import edu.kit.kastel.logic.skierlogic.SkierGoals;
import edu.kit.kastel.logic.skierlogic.SkierSkillLevel;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class that handles the logic for the ski resort.
 * @author uurce
 */
public class SkiResort {
    private static final String EXISTING_PLANNED_ROUTE_ERROR = "Error, There is already an existing planned route.";
    private static final String START_TIME_AFTER_END_TIME_ERROR = "Error, Start time must be before end time.";
    private static final String NO_GOAL_ERROR = "Error, No goal selected for the skier.";
    private static final String NO_SKILL_LEVEL_ERROR = "Error, No skill level selected for the skier.";
    private static final String NOT_AFTER_NEXT_COMMAND_ERROR = "Error, this command can only be used right after the next command.";
    private static final String NO_ROUTE_PLANNED_ERROR = "Error, No route has been currently planned.";
    private static final String NO_LIFTS_LOADED_ERROR = "Error, No lifts have been loaded into the ski resort";
    private static final String NO_SLOPES_LOADED_ERROR = "Error, No slopes have been loaded into the ski resort";
    private static final String EMPTY_FILE_ERROR = "Error, The inputted file is empty.";
    private static final String INVALID_FILE_FORMAT_ERROR = "Error, The input of the file is in an invalid format.";
    private static final String UNCONNECTED_GRAPH_ERROR = "Error, Graph is not connected.";
    private static final String NO_LIFT_IN_FILE_ERROR = "Error, There was no lift inputted in the file.";
    private static final String NO_SLOPE_IN_FILE_ERROR = "Error, There was no slope inputted in the file.";
    private static final String NO_TRANSIT_LIFT_IN_FILE_ERROR = "Error, There was no transit lift inputted in the file.";
    private static final String EDGE_FROM_NONEXISTENT_NODE_ERROR = "Error, Edge cannot be made from nonexistent node.";
    private static final String INVALID_DATE_ERROR = "Error, The inputted date is invalid and cannot be parsed.";
    private static final String LIFT_START_TIME_AFTER_END_TIME_ERROR = "Error, The starting time for a lift is past it's ending time.";
    private static final String FILE_FIRST_LINE_INPUT = "graph";
    private static final String NO_ALTERNATIVE_OUTPUT = "no alternative found";
    private static final String ROUTE_AVOIDED_OUTPUT = "avoided ";
    private static final String ROUTE_FINISHED_OUTPUT = "route finished!";
    private static final String TRANSIT_LIFT_REGEX = " {4}([A-Za-z]+)\\[\\[([A-Za-z]+)<br/>(CHAIRLIFT|GONDOLA);([0-9]{2}:[0-9]{2});"
            + "([0-9]{2}:[0-9]{2});([0-9]+);([0-9]+)]]";
    private static final String NON_TRANSIT_LIFT_REGEX = " {4}([A-Za-z]+)\\[([A-Za-z]+)<br/>(CHAIRLIFT|GONDOLA);([0-9]{2}:[0-9]{2});"
            + "([0-9]{2}:[0-9]{2});([0-9]+);([0-9]+)]";
    private static final String SLOPE_REGEX = " {4}([A-Za-z0-9]+)\\(\\[([A-Za-z0-9]+)<br/>(RED|BLUE|BLACK);"
            + "(REGULAR|ICY|BUMPY);([0-9]+);([0-9]+)]\\)";
    private static final String EDGE_REGEX = " {4}([A-Za-z0-9]+) --> ([A-Za-z0-9]+)";
    private static final int NEW_NODE_CREATION_STARTING_MATCHER_INDEX = 2;
    private final Skier currentSkier;
    private RoutePlanner routePlanner;
    private Queue<Lift> lifts;
    private Queue<Slope> slopes;
    private boolean wasNextCommandUsed = false;
    /**
     * Creates a new ski resort object.
     */
    public SkiResort() {
        currentSkier = new Skier();
    }
    /**
     * Starts planning a new route, creating a new route planner object.
     * @param id The id of the transit lift to begin and end the route from.
     * @param startTime The starting time for the route.
     * @param endTime The end time for the route.
     * @throws ResortLogicException Throws exceptions if there is missing information
     *     for the skier or if the start time is after the end time.
     */
    public void startRoutePlanning(String id, LocalTime startTime, LocalTime endTime) throws ResortLogicException {
        wasNextCommandUsed = false;
        if (routePlanner != null) {
            throw new ResortLogicException(EXISTING_PLANNED_ROUTE_ERROR);
        }
        if (startTime.isAfter(endTime)) {
            throw new ResortLogicException(START_TIME_AFTER_END_TIME_ERROR);
        } else if (currentSkier.getGoal() == null) {
            throw new ResortLogicException(NO_GOAL_ERROR);
        } else if (currentSkier.getSkillLevel() == null) {
            throw new ResortLogicException(NO_SKILL_LEVEL_ERROR);
        }
        routePlanner = new RoutePlanner(currentSkier, getLiftByID(id, lifts), startTime, endTime);
    }
    /**
     * Plans an alternative route.
     * @return A string displaying the result of the planning of the alternative route.
     * @throws ResortLogicException Throws and exception if a route wasn't already planned or started.
     */
    public String planAlternative() throws ResortLogicException {
        if (!wasNextCommandUsed) {
            throw new ResortLogicException(NOT_AFTER_NEXT_COMMAND_ERROR);
        }
        wasNextCommandUsed = false;
        isRoutePlanned();
        String output = ROUTE_AVOIDED_OUTPUT + routePlanner.nextInRoute();
        if (routePlanner.findAlternative()) {
            return output;
        }
        return NO_ALTERNATIVE_OUTPUT;
    }
    /**
     * Returns the id of the next node in the route.
     * @return The id of the next node in the route.
     * @throws ResortLogicException Throws an exception if there was no route planned.
     */
    public String getNextInRoute() throws ResortLogicException {
        isRoutePlanned();
        if (routePlanner.isRouteFinished()) {
            routePlanner = null;
            return ROUTE_FINISHED_OUTPUT;
        }
        wasNextCommandUsed = true;
        return routePlanner.nextInRoute();
    }
    /**
     * Takes the next node in the route. Ends the route if it is finished.
     * @throws ResortLogicException Throws an exception if there is no route planned
     */
    public void takeNextInRoute() throws ResortLogicException {
        if (!wasNextCommandUsed) {
            throw new ResortLogicException(NOT_AFTER_NEXT_COMMAND_ERROR);
        }
        wasNextCommandUsed = false;
        isRoutePlanned();
        routePlanner.takeNextInRoute();
    }
    /**
     * Aborts the route.
     * @throws ResortLogicException Throws an exception if there is no route planned
     */
    public void abortRoute() throws ResortLogicException {
        wasNextCommandUsed = false;
        if (routePlanner == null || routePlanner.isRouteFinished()) {
            throw new ResortLogicException(NO_ROUTE_PLANNED_ERROR);
        }
        routePlanner = null;
    }
    /**
     * Displays the id's of all the nodes in the route.
     * @return The list of all id's in the route.
     * @throws ResortLogicException Throws an exception if no route was planned.
     */
    public String showRoute() throws ResortLogicException {
        wasNextCommandUsed = false;
        if (routePlanner == null || routePlanner.isRouteFinished()) {
            throw new ResortLogicException(NO_ROUTE_PLANNED_ERROR);
        }
        return routePlanner.toString();
    }

    private void isRoutePlanned() throws ResortLogicException {
        if (routePlanner == null) {
            throw new ResortLogicException(NO_ROUTE_PLANNED_ERROR);
        }
    }

    /**
     * Lists all the lifts in the ski resort in alphabetic order.
     * @return A list of all the lifts in the ski resort.
     * @throws ResortLogicException Throws an exception if no lifts were loaded from a file.
     */
    public String listLifts() throws ResortLogicException {
        wasNextCommandUsed = false;
        if (lifts == null) {
            throw new ResortLogicException(NO_LIFTS_LOADED_ERROR);
        }
        String output = "";
        for (Lift lift : lifts) {
            output += lift.toString() + System.lineSeparator();
        }
        return output.substring(0, output.length() - 1);
    }

    /**
     * Lists all the slopes in the ski resort in alphabetic order.
     * @return A list of all the slopes in the ski resort.
     * @throws ResortLogicException Throws an exception if no lifts were loaded from a file.
     */
    public String listSlopes() throws ResortLogicException {
        wasNextCommandUsed = false;
        if (slopes == null) {
            throw new ResortLogicException(NO_SLOPES_LOADED_ERROR);
        }
        String output = "";
        for (Slope slope : slopes) {
            output += slope.toString() + System.lineSeparator();
        }
        return output.substring(0, output.length() - 1);
    }

    /**
     * Creates lifts and slopes for the ski resort based on the input from a file.
     * @param fileInput The input of each line from the file.
     * @throws ResortLogicException Throws an exception if the input from the file is invalid.
     */
    public void loadArea(List<String> fileInput) throws ResortLogicException {
        wasNextCommandUsed = false;
        routePlanner = null;
        if (fileInput.isEmpty()) {
            throw new ResortLogicException(EMPTY_FILE_ERROR);
        }
        Queue<Lift> newLifts = new PriorityQueue<>(new NodeSorting());
        Queue<Slope> newSlopes = new PriorityQueue<>(new NodeSorting());
        String lineInput = fileInput.removeFirst();
        if (lineInput.equals(FILE_FIRST_LINE_INPUT)) {
            lineInput = fileInput.removeFirst();
            int numOfNodes = 0;
            int numOfEdges = 0;
            boolean wasTransitLiftCreated = false;
            while (lineInput != null && (lineInput.matches(TRANSIT_LIFT_REGEX) || lineInput.matches(NON_TRANSIT_LIFT_REGEX)
                    || lineInput.matches(SLOPE_REGEX))) {
                wasTransitLiftCreated = createNewNode(lineInput, newLifts, newSlopes) || wasTransitLiftCreated;
                if (!fileInput.isEmpty()) {
                    lineInput = fileInput.removeFirst();
                    numOfNodes++;
                } else {
                    lineInput = null;
                }
            }
            while (lineInput != null && lineInput.matches(EDGE_REGEX)) {
                Matcher inputMatcher = Pattern.compile(EDGE_REGEX).matcher(lineInput);
                inputMatcher.matches(); // Matches command to find and store the matcher group.

                int matcherIndex = 1;
                Node startNode = getNodeByID(inputMatcher.group(matcherIndex), newLifts, newSlopes);
                matcherIndex++;
                Node endNode = getNodeByID(inputMatcher.group(matcherIndex), newLifts, newSlopes);
                if (startNode == null || endNode == null) {
                    throw new ResortLogicException(EDGE_FROM_NONEXISTENT_NODE_ERROR);
                }
                startNode.addAdjacentNode(endNode);
                if (!fileInput.isEmpty()) {
                    lineInput = fileInput.removeFirst();
                    numOfEdges++;
                } else {
                    lineInput = null;
                }
            }
            if (!fileInput.isEmpty()) {
                throw new ResortLogicException(INVALID_FILE_FORMAT_ERROR);
            } else if (numOfEdges < numOfNodes - 1) {
                throw new ResortLogicException(UNCONNECTED_GRAPH_ERROR);
            } else if (newLifts.isEmpty()) {
                throw new ResortLogicException(NO_LIFT_IN_FILE_ERROR);
            } else if (newSlopes.isEmpty()) {
                throw new ResortLogicException(NO_SLOPE_IN_FILE_ERROR);
            } else if (!wasTransitLiftCreated) {
                throw new ResortLogicException(NO_TRANSIT_LIFT_IN_FILE_ERROR);
            }
            lifts = newLifts;
            slopes = newSlopes;
        } else {
            throw new ResortLogicException(INVALID_FILE_FORMAT_ERROR);
        }
    }
    private boolean createNewNode(String lineInput, Queue<Lift> lifts, Queue<Slope> slopes) throws ResortLogicException {
        int matcherIndex = NEW_NODE_CREATION_STARTING_MATCHER_INDEX;
        if (lineInput.matches(TRANSIT_LIFT_REGEX) || lineInput.matches(NON_TRANSIT_LIFT_REGEX)) {
            Matcher inputMatcher = Pattern.compile(TRANSIT_LIFT_REGEX).matcher(lineInput);
            if (!inputMatcher.matches()) {
                inputMatcher = Pattern.compile(NON_TRANSIT_LIFT_REGEX).matcher(lineInput);
            }
            inputMatcher.matches(); // Matches command to find and store the matcher group.

            String id = inputMatcher.group(matcherIndex);
            matcherIndex++;
            LiftTypes type = LiftTypes.getLiftType(inputMatcher.group(matcherIndex));
            matcherIndex++;

            LocalTime startTime;
            LocalTime endTime;

            try {
                startTime = LocalTime.parse(inputMatcher.group(matcherIndex));
                matcherIndex++;
                endTime = LocalTime.parse(inputMatcher.group(matcherIndex));
                matcherIndex++;
            } catch (DateTimeParseException invalidDateException) {
                throw new ResortLogicException(INVALID_DATE_ERROR);
            }
            if (startTime.isAfter(endTime)) {
                throw new ResortLogicException(LIFT_START_TIME_AFTER_END_TIME_ERROR);
            }
            int travelTime = Integer.parseInt(inputMatcher.group(matcherIndex));
            matcherIndex++;
            int waitingTime = Integer.parseInt(inputMatcher.group(matcherIndex));
            boolean isTransitLift = lineInput.matches(TRANSIT_LIFT_REGEX);
            lifts.add(new Lift(id, isTransitLift, type, startTime, endTime, travelTime, waitingTime));
            return isTransitLift;
        } else if (lineInput.matches(SLOPE_REGEX)) {
            Matcher inputMatcher = Pattern.compile(SLOPE_REGEX).matcher(lineInput);
            inputMatcher.matches(); // Matches command to find and store the matcher group.

            String id = inputMatcher.group(matcherIndex);
            matcherIndex++;
            SlopeDifficulty difficulty = SlopeDifficulty.getSlopeDifficulty(inputMatcher.group(matcherIndex));
            matcherIndex++;
            SlopeSurface surface = SlopeSurface.getSlopeSurface(inputMatcher.group(matcherIndex));
            matcherIndex++;
            int distance = Integer.parseInt(inputMatcher.group(matcherIndex));
            matcherIndex++;
            int altitude = Integer.parseInt(inputMatcher.group(matcherIndex));
            slopes.add(new Slope(id, difficulty, surface, distance, altitude));
        }
        return false;
    }

    /**
     * Sets the skill level of the skier.
     * @param skillLevel The skill level to set to.
     */
    public void setSkierSkill(SkierSkillLevel skillLevel) {
        currentSkier.setSkillLevel(skillLevel);
        dynamicRouteUpdate();
    }

    /**
     * Sets the goal for the skier.
     * @param goal The goal to set to.
     */
    public void setSkierGoal(SkierGoals goal) {
        currentSkier.setGoal(goal);
        dynamicRouteUpdate();
    }

    /**
     * Adds a new slope difficulty preference to the skier.
     * @param slopeDifficulty The difficulty to add as a preference.
     */
    public void addPreference(SlopeDifficulty slopeDifficulty) {
        currentSkier.addPreference(slopeDifficulty);
        dynamicRouteUpdate();
    }

    /**
     * Adds a new slope surface preference to the skier.
     * @param slopeSurface The surface to add as a preference.
     */
    public void addPreference(SlopeSurface slopeSurface) {
        currentSkier.addPreference(slopeSurface);
        dynamicRouteUpdate();
    }

    /**
     * Adds a new slope difficulty dislike to the skier.
     * @param slopeDifficulty The difficulty to add as a dislike.
     */
    public void addDislike(SlopeDifficulty slopeDifficulty) {
        currentSkier.addDislike(slopeDifficulty);
        dynamicRouteUpdate();
    }

    /**
     * Adds a new slope surface dislike to the skier.
     * @param slopeSurface The surface to add as a dislike.
     */
    public void addDislike(SlopeSurface slopeSurface) {
        currentSkier.addDislike(slopeSurface);
        dynamicRouteUpdate();
    }
    /**
     * Resets the preferences of the skier.
     */
    public void resetSkierPreference() {
        currentSkier.resetPreferences();
        dynamicRouteUpdate();
    }
    private void dynamicRouteUpdate() {
        if (routePlanner != null) {
            routePlanner.updateRoute();
        }
    }
    private Node getNodeByID(String id, Queue<Lift> lifts, Queue<Slope> slopes) {
        if (getLiftByID(id, lifts) != null) {
            return getLiftByID(id, lifts);
        }
        return getSlopeByID(id, slopes);
    }
    private Lift getLiftByID(String id, Queue<Lift> lifts) {
        for (Lift lift : lifts) {
            if (lift.getId().equals(id)) {
                return lift;
            }
        }
        return null;
    }
    private Slope getSlopeByID(String id, Queue<Slope> slopes) {
        for (Slope slope : slopes) {
            if (slope.getId().equals(id)) {
                return slope;
            }
        }
        return null;
    }
}
