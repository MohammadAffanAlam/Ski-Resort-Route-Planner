package edu.kit.kastel.commandhandler.skiercommands;

import edu.kit.kastel.logic.SkiResort;
import edu.kit.kastel.logic.graphlogic.slopelogic.SlopeDifficulty;
import edu.kit.kastel.logic.graphlogic.slopelogic.SlopeSurface;
import edu.kit.kastel.commandhandler.Command;
import edu.kit.kastel.commandhandler.CommandExecutionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Command handler for adding preferences of the current skier.
 * @author uurce
 */
public class LikeCommand extends Command {
    private static final String LIKE_COMMAND_NAME = "like";
    private static final String LIKE_COMMAND_DIFFICULTY_REGEX = "like (BLUE|RED|BLACK)";
    private static final String LIKE_COMMAND_SURFACE_REGEX = "like (REGULAR|ICY|BUMPY)";
    private static final int PARAMETER_MATCHER_INDEX = 1;

    /**
     * Creates a new like command handler object.
     * @param resort The resort instance to run the command for.
     */
    public LikeCommand(SkiResort resort) {
        super(LIKE_COMMAND_NAME, resort);
    }

    @Override
    public String execute(String input) throws CommandExecutionException {
        if (input.matches(LIKE_COMMAND_DIFFICULTY_REGEX)) {
            Matcher inputMatcher = Pattern.compile(LIKE_COMMAND_DIFFICULTY_REGEX).matcher(input);
            inputMatcher.matches(); // Matches command to find and store the matcher group.
            resort.addPreference(SlopeDifficulty.getSlopeDifficulty(inputMatcher.group(PARAMETER_MATCHER_INDEX)));
        } else if (input.matches(LIKE_COMMAND_SURFACE_REGEX)) {
            Matcher inputMatcher = Pattern.compile(LIKE_COMMAND_SURFACE_REGEX).matcher(input);
            inputMatcher.matches(); // Matches command to find and store the matcher group.
            resort.addPreference(SlopeSurface.getSlopeSurface(inputMatcher.group(PARAMETER_MATCHER_INDEX)));
        } else {
            throw new CommandExecutionException(INVALID_INPUT_ERROR);
        }
        return null;
    }
}
