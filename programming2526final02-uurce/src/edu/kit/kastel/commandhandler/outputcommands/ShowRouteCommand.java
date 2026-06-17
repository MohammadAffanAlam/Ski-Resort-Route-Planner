package edu.kit.kastel.commandhandler.outputcommands;

import edu.kit.kastel.logic.ResortLogicException;
import edu.kit.kastel.logic.SkiResort;
import edu.kit.kastel.commandhandler.Command;
import edu.kit.kastel.commandhandler.CommandExecutionException;

/**
 * Command handler for showing the lifts and slopes in the route.
 * @author uurce
 */
public class ShowRouteCommand extends Command {
    private static final String SHOW_COMMAND_NAME = "show";
    private static final String SHOW_ROUTE_COMMAND_INPUT = "show route";

    /**
     * Creates a new show route command handler object.
     * @param resort The resort instance to run the command for.
     */
    public ShowRouteCommand(SkiResort resort) {
        super(SHOW_COMMAND_NAME, resort);
    }

    @Override
    public String execute(String input) throws CommandExecutionException {
        if (input.equals(SHOW_ROUTE_COMMAND_INPUT)) {
            try {
                return resort.showRoute();
            } catch (ResortLogicException resortLogicException) {
                throw new CommandExecutionException(resortLogicException.getMessage());
            }
        }
        throw new CommandExecutionException(INVALID_INPUT_ERROR);
    }
}
