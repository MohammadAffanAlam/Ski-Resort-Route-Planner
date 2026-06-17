package edu.kit.kastel.logic;

import edu.kit.kastel.logic.graphlogic.liftlogic.Lift;
import edu.kit.kastel.logic.graphlogic.Node;
import edu.kit.kastel.logic.graphlogic.slopelogic.Slope;
import edu.kit.kastel.logic.skierlogic.Skier;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * A class which stores planned routes and handles the logic for planning routes and their alternatives.
 * @author uurce
 */
public class RoutePlanner {
    private static final String SHOW_ROUTE_NODE_SEPARATOR = " ";
    private static final String NO_START_LIFT_ERROR = "Error, No lift given for start location of route.";
    private static final String INVALID_START_LIFT_ERROR = "Error, Route can only begin from a transit lift.";
    private static final String NO_ROUTE_FOUND_ERROR = "Error, No route can be found with the given constraints.";
    private static final String ROUTE_NOT_STARTED_ERROR = "Error, Route not started yet.";
    private static final int SLOPE_TIME_SPENT_DIVISOR = 8;
    private static final int GRADIENT_TIME_MULTIPLIER = 2;
    private static final int GRADIENT_TIME_ADDEND = 1;
    private final Lift targetTransitLift;
    private LocalTime currentTime;
    private final LocalTime endTime;
    private List<Node> plannedRoute;
    private Node currentNode;
    private final Skier skier;

    /**
     * Creates a new route planner object, which plans a new route.
     * @param skier The skier to plan the new route for.
     * @param lift The transit lift to start and end the route from.
     * @param startTime The start time for the route.
     * @param endTime The end time for the route.
     * @throws ResortLogicException Throws an exception if an existing transit lift is not inputted in the parameters.
     */
    public RoutePlanner(Skier skier, Lift lift, LocalTime startTime, LocalTime endTime) throws ResortLogicException {
        if (lift == null) {
            throw new ResortLogicException(NO_START_LIFT_ERROR);
        }
        if (!lift.isTransitLift()) {
            throw new ResortLogicException(INVALID_START_LIFT_ERROR);
        }

        this.skier = skier;
        targetTransitLift = lift;
        currentTime = startTime;
        this.endTime = endTime;
        updateRoute();
        if (plannedRoute.isEmpty()) {
            throw new ResortLogicException(NO_ROUTE_FOUND_ERROR);
        }
    }

    /**
     * Plans an alternative route from the current node.
     * @return True if an alternative was successfully found, false otherwise.
     * @throws ResortLogicException Throws an exception if the route hasn't been started yet.
     */
    public boolean findAlternative() throws ResortLogicException {
        if (currentNode == null) {
            throw new ResortLogicException(ROUTE_NOT_STARTED_ERROR);
        }
        List<Node> alternativeRoute = createNewRoute(true);
        if (alternativeRoute == null) {
            return false;
        }
        plannedRoute = alternativeRoute;
        return true;
    }

    /**
     * Updates the route.
     */
    protected void updateRoute() {
        plannedRoute = createNewRoute(false);
    }

    private List<Node> createNewRoute(boolean findingAlternativeRoute) {
        List<Node> route = new ArrayList<>();
        if (currentNode == null) {
            route.add(targetTransitLift);
            route = planRoute(route, currentTime);
        } else {
            for (Node node : currentNode.getAdjacentNodes()) {
                if (findingAlternativeRoute && node == plannedRoute.getFirst()) {
                    continue;
                }
                List<Node> newRoute = new ArrayList<>();
                newRoute.add(node);
                newRoute = planRoute(newRoute, currentTime);

                if (isNewRouteMorePreferred(route, newRoute)) {
                    route = newRoute;
                }
            }
        }
        if (route == null || route.isEmpty()) {
            return null;
        }
        route.removeLast();
        return route;
    }

    private List<Node> planRoute(List<Node> route, LocalTime currentTime) {
        List<Node> newRoute = route;

        LocalTime newTime = currentTime;
        if (newRoute.getLast().isLift()) {
            Lift lift = (Lift) newRoute.getLast();
            newTime = newTime.plusMinutes(lift.getWaitingTime());
            if (newTime.isBefore(lift.getEndTime())) {
                if (newTime.isBefore(lift.getStartTime())) {
                    newTime = lift.getStartTime();
                }
                newTime = newTime.plusMinutes(lift.getTravelTime());
            } else {
                if (lift == targetTransitLift) {
                    return newRoute;
                }
                return null;
            }
        } else {
            Slope slope = (Slope) newRoute.getLast();
            float timeSpent = slope.getDistance() * slope.getDifficulty().getDifficultyModifier();
            timeSpent *= slope.getSurface().getSurfaceModifier() * skier.getSkillLevel().getSkillModifier();
            timeSpent *= (GRADIENT_TIME_ADDEND + GRADIENT_TIME_MULTIPLIER * ((float) slope.getAltitude() / slope.getDistance()));
            timeSpent /= SLOPE_TIME_SPENT_DIVISOR;
            newTime = newTime.plusSeconds((int) Math.ceil(timeSpent));
        }

        if (newTime.isBefore(endTime)) {
            // Checks the continuation of the route for each adjacent node and checks for
            // validity and whether the new route is more preferred.
            for (Node connection : newRoute.getLast().getAdjacentNodes()) {
                List<Node> extendedRoute = new LinkedList<>(route);
                extendedRoute.add(connection);
                extendedRoute = planRoute(extendedRoute, newTime);

                if (isNewRouteMorePreferred(newRoute, extendedRoute)) {
                    newRoute = extendedRoute;
                }
            }
        }

        if (newRoute != null && newRoute.getLast() == targetTransitLift) {
            return newRoute; // Returns the new route if it is valid.
        }
        return null;
    }

    /**
     * Takes the next node in the route, removing it from the route and updating the current time.
     */
    public void takeNextInRoute() {
        currentNode = plannedRoute.removeFirst();

        if (currentNode.isLift()) {
            Lift lift = (Lift) currentNode;
            currentTime = currentTime.plusMinutes(lift.getWaitingTime());
            if (currentTime.isBefore(lift.getStartTime())) {
                currentTime = lift.getStartTime();
            }
            currentTime = currentTime.plusMinutes(lift.getTravelTime());
        } else {
            Slope slope = (Slope) currentNode;
            float timeSpent = slope.getDistance() * slope.getDifficulty().getDifficultyModifier();
            timeSpent *= slope.getSurface().getSurfaceModifier() * skier.getSkillLevel().getSkillModifier();
            timeSpent *= (GRADIENT_TIME_ADDEND + GRADIENT_TIME_MULTIPLIER * ((float) slope.getAltitude() / slope.getDistance()));
            timeSpent /= SLOPE_TIME_SPENT_DIVISOR;
            currentTime = currentTime.plusSeconds((int) Math.ceil(timeSpent));
        }
    }

    /**
     * Checks whether the route is finished.
     * @return True if there are no more nodes remaining in the route, false otherwise.
     */
    public boolean isRouteFinished() {
        return plannedRoute.isEmpty();
    }

    /**
     * Displays the next node in the route.
     * @return The ID of the next node in the route.
     */
    public String nextInRoute() {
        if (plannedRoute.isEmpty()) {
            return null;
        }
        return plannedRoute.getFirst().getId();
    }

    /**
     * Checks the score of 2 routes and returns a boolean value indicating which is preferred.
     * @param originalRoute The first route to check whether it is more preferred.
     * @param newRoute The second route to check whether it is more preferred.
     * @return True if the new route is more preferred than the old route, and false if otherwise.
     */
    private boolean isNewRouteMorePreferred(List<Node> originalRoute, List<Node> newRoute) {
        if (originalRoute == null || originalRoute.isEmpty() || originalRoute.getLast() != targetTransitLift) {
            return true;
        }
        if (newRoute == null || newRoute.getLast() != targetTransitLift) {
            return false;
        }
        int oldRouteScore = calculateRouteScore(originalRoute);
        int newRouteScore = calculateRouteScore(newRoute);

        if (oldRouteScore != newRouteScore) {
            return newRouteScore > oldRouteScore;
        } else {
            int oldRoutePreferenceBonus = 0;
            int newRoutePreferenceBonus = 0;

            for (Node node : originalRoute) {
                if (node.isSlope()) {
                    oldRoutePreferenceBonus += skier.getPreferenceScore((Slope) node);
                }
            }

            for (Node node : newRoute) {
                if (node.isSlope()) {
                    newRoutePreferenceBonus += skier.getPreferenceScore((Slope) node);
                }
            }

            if (oldRoutePreferenceBonus != newRoutePreferenceBonus) {
                return newRoutePreferenceBonus > oldRoutePreferenceBonus;
            }
        }
        return showRoute(originalRoute).compareTo(showRoute(newRoute)) > 0;
    }

    private int calculateRouteScore(List<Node> route) {
        int score = 0;
        Set<Slope> uniqueSlopes = new HashSet<>();
        for (Node node : route) {
            if (node.isSlope()) {
                switch (skier.getGoal()) {
                    case DISTANCE:
                        score += ((Slope) node).getDistance();
                        break;
                    case ALTITUDE:
                        score += ((Slope) node).getAltitude();
                        break;
                    case NUMBER:
                        score++;
                        break;
                    case UNIQUE:
                        if (uniqueSlopes.add(((Slope) node))) {
                            score++;
                        }
                        break;
                    default:
                        break; // Unreachable default case as a check is made to ensure that a goal is always selected.
                }
            }
        }
        return score;
    }

    private String showRoute(List<Node> route) {
        String output = "";
        for (Node node : route) {
            output += node.getId() + SHOW_ROUTE_NODE_SEPARATOR;
        }
        return output.substring(0, output.length() - 1);
    }

    @Override
    public String toString() {
        return showRoute(plannedRoute);
    }
}
