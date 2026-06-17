package edu.kit.kastel.commandhandler.routecommands;

import edu.kit.kastel.logic.ResortLogicException;
import edu.kit.kastel.logic.SkiResort;
import edu.kit.kastel.commandhandler.Command;
import edu.kit.kastel.commandhandler.CommandExecutionException;

/**
 * Command handler for displaying the next lift/slope in the route.
 * @author uurce
 */
public class NextCommand extends Command {
    private static final String NEXT_COMMAND_NAME = "next";

    /**
     * Creates a new next command handler object.
     * @param resort The resort instance to run the command for.
     */
    public NextCommand(SkiResort resort) {
        super(NEXT_COMMAND_NAME, resort);
    }

    @Override
    public String execute(String input) throws CommandExecutionException {
        if (input.equals(NEXT_COMMAND_NAME)) {
            try {
                return resort.getNextInRoute();
            } catch (ResortLogicException resortLogicException) {
                throw new CommandExecutionException(resortLogicException.getMessage());
            }
        }
        throw new CommandExecutionException(INVALID_INPUT_ERROR);
    }
}
