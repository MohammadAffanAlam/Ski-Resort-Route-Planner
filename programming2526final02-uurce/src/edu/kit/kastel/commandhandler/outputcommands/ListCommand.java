package edu.kit.kastel.commandhandler.outputcommands;

import edu.kit.kastel.logic.ResortLogicException;
import edu.kit.kastel.logic.SkiResort;
import edu.kit.kastel.commandhandler.Command;
import edu.kit.kastel.commandhandler.CommandExecutionException;

/**
 * Command handler for listing out either slopes or lifts in the ski resort.
 * @author uurce
 */
public class ListCommand extends Command {
    private static final String LIST_COMMAND_NAME = "list";
    private static final String LIST_LIFTS_INPUT = "list lifts";
    private static final String LIST_SLOPES_INPUT = "list slopes";

    /**
     * Creates a new list command handler object.
     * @param resort The resort instance to run the command for.
     */
    public ListCommand(SkiResort resort) {
        super(LIST_COMMAND_NAME, resort);
    }

    @Override
    public String execute(String input) throws CommandExecutionException {
        if (input.equals(LIST_LIFTS_INPUT)) {
            try {
                return resort.listLifts();
            } catch (ResortLogicException resortLogicException) {
                throw new CommandExecutionException(resortLogicException.getMessage());
            }
        }
        if (input.equals(LIST_SLOPES_INPUT)) {
            try {
                return resort.listSlopes();
            } catch (ResortLogicException resortLogicException) {
                throw new CommandExecutionException(resortLogicException.getMessage());
            }
        }
        throw new CommandExecutionException(INVALID_INPUT_ERROR);
    }
}
