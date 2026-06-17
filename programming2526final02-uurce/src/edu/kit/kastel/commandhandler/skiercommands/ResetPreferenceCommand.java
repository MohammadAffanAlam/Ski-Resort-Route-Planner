package edu.kit.kastel.commandhandler.skiercommands;

import edu.kit.kastel.logic.SkiResort;
import edu.kit.kastel.commandhandler.Command;
import edu.kit.kastel.commandhandler.CommandExecutionException;

/**
 * Command handler for resetting the preferences of the skier.
 * @author uurce
 */
public class ResetPreferenceCommand extends Command {
    private static final String RESET_PREFERENCE_COMMAND_NAME = "reset";
    private static final String RESET_PREFERENCE_INPUT = "reset preferences";

    /**
     * Creates a new reset preference command handler object.
     * @param resort The resort instance to run the command for.
     */
    public ResetPreferenceCommand(SkiResort resort) {
        super(RESET_PREFERENCE_COMMAND_NAME, resort);
    }

    @Override
    public String execute(String input) throws CommandExecutionException {
        if (input.equals(RESET_PREFERENCE_INPUT)) {
            resort.resetSkierPreference();
            return null;
        }
        throw new CommandExecutionException(INVALID_INPUT_ERROR);
    }
}
