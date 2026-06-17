package edu.kit.kastel.commandhandler.routecommands;

import edu.kit.kastel.logic.ResortLogicException;
import edu.kit.kastel.logic.SkiResort;
import edu.kit.kastel.commandhandler.Command;
import edu.kit.kastel.commandhandler.CommandExecutionException;

/**
 * Command handler for taking the next lift/slope on the route.
 * @author uurce
 */
public class TakeCommand extends Command {
    private static final String TAKE_COMMAND_NAME = "take";

    /**
     * Creates a new take command handler object.
     * @param resort The resort instance to run the command for.
     */
    public TakeCommand(SkiResort resort) {
        super(TAKE_COMMAND_NAME, resort);
    }

    @Override
    public String execute(String input) throws CommandExecutionException {
        if (input.equals(TAKE_COMMAND_NAME)) {
            try {
                resort.takeNextInRoute();
            } catch (ResortLogicException resortLogicException) {
                throw new CommandExecutionException(resortLogicException.getMessage());
            }
            return null;
        }
        throw new CommandExecutionException(INVALID_INPUT_ERROR);
    }
}
