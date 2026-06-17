package edu.kit.kastel.logic.graphlogic;

import edu.kit.kastel.logic.ResortLogicException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The class which stores the data and handles the logic for a node in
 * the directed graph for the ski resort, containing lifts and slopes.
 * @author uurce
 */
public class Node {
    private static final String REFLEXIVE_EDGE_ERROR = "Error, An edge cannot be created from %s to itself.";
    private static final String SYMMETRICAL_EDGE_ERROR = "Error, A symmetrical edge can only be created between a lift and a slope.";
    private static final String DUPLICATE_EDGE_ERROR = "Error, An edge from %s to %s already exists and cannot be created again.";
    private final String id;
    private final Set<Node> adjacentNodes;

    /**
     * Creates a new node with a specific id.
     * @param id The id for the node.
     */
    public Node(String id) {
        this.id = id;
        adjacentNodes = new HashSet<>();
    }

    /**
     * Adds a directed edge from this node to an inputted adjacent node.
     * @param adjacentNode The node to which the edge is to be created.
     * @throws ResortLogicException Throws exceptions if the inputted adjacent node
     *     is the node itself, if there is a symmetrical connection between 2 lifts/slopes,
     *     or if there is an existing edge to this node.
     */
    public void addAdjacentNode(Node adjacentNode) throws ResortLogicException {
        if (adjacentNode == this) {
            throw new ResortLogicException(REFLEXIVE_EDGE_ERROR.formatted(id));
        }
        if (this.isLift() == adjacentNode.isLift() && adjacentNode.adjacentNodes.contains(this)) {
            throw new ResortLogicException(SYMMETRICAL_EDGE_ERROR);
        }
        if (!adjacentNodes.add(adjacentNode)) {
            throw new ResortLogicException(DUPLICATE_EDGE_ERROR.formatted(id, adjacentNode.id));
        }
    }

    /**
     * Gets all adjacent nodes to this node.
     * @return All adjacent nodes.
     */
    public Set<Node> getAdjacentNodes() {
        return Collections.unmodifiableSet(adjacentNodes);
    }

    /**
     * Checks whether the node is a lift or not.
     * @return True if the node is a lift, false otherwise.
     */
    public boolean isLift() {
        return false;
    }

    /**
     * Checks whether the node is a slope or not.
     * @return True if the node is a slope, false otherwise.
     */
    public boolean isSlope() {
        return false;
    }

    /**
     * Returns the id for the lift/slope.
     * @return The id of the node.
     */
    public String getId() {
        return id;
    }
}
