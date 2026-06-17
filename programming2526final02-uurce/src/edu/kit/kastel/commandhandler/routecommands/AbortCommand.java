package edu.kit.kastel.commandhandler.routecommands;

import edu.kit.kastel.logic.ResortLogicException;
import edu.kit.kastel.logic.SkiResort;
import edu.kit.kastel.commandhandler.Command;
import edu.kit.kastel.commandhandler.CommandExecutionException;

/**
 * Command handler for aborting a planned route.
 * @author uurce
 */
public class AbortCommand extends Command {
    private static final String ABORT_COMMAND_NAME = "abort";
    private static final String ROUTE_ABORTED_OUTPUT = "route aborted";

    /**
     * Creates a new abort command handler object.
     * @param resort The resort instance to run the command for.
     */
    public AbortCommand(SkiResort resort) {
        super(ABORT_COMMAND_NAME, resort);
    }

    @Override
    public String execute(String input) throws CommandExecutionException {
        if (input.equals(ABORT_COMMAND_NAME)) {
            try {
                resort.abortRoute();
            } catch (ResortLogicException resortLogicException) {
                throw new CommandExecutionException(resortLogicException.getMessage());
            }
            return ROUTE_ABORTED_OUTPUT;
        }
        throw new CommandExecutionException(INVALID_INPUT_ERROR);
    }
}
