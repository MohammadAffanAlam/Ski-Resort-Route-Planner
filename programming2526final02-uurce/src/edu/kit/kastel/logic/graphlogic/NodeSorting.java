package edu.kit.kastel.logic.graphlogic;

import java.util.Comparator;

/**
 * A class which dictates the sorting behavior for outputting a list of all slopes and lifts.
 * @author uurce
 */
public class NodeSorting implements Comparator<Node> {
    @Override
    public int compare(Node o1, Node o2) {
        return o1.getId().compareTo(o2.getId());
    }
}
