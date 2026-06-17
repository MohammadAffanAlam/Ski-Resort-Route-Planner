package edu.kit.kastel.commandhandler.routecommands;

import edu.kit.kastel.logic.ResortLogicException;
import edu.kit.kastel.logic.SkiResort;
import edu.kit.kastel.commandhandler.Command;
import edu.kit.kastel.commandhandler.CommandExecutionException;

/**
 * Command handler for finding an alternate route.
 * @author uurce
 */
public class AlternativeCommand extends Command {
    private static final String ALTERNATIVE_COMMAND_NAME = "alternative";

    /**
     * Creates a new alternative command handler object.
     * @param resort The resort instance to run the command for.
     */
    public AlternativeCommand(SkiResort resort) {
        super(ALTERNATIVE_COMMAND_NAME, resort);
    }

    @Override
    public String execute(String input) throws CommandExecutionException {
        if (input.equals(ALTERNATIVE_COMMAND_NAME)) {
            try {
                return resort.planAlternative();
            } catch (ResortLogicException resortLogicException) {
                throw new CommandExecutionException(resortLogicException.getMessage());
            }
        }
        throw new CommandExecutionException(INVALID_INPUT_ERROR);
    }
}
