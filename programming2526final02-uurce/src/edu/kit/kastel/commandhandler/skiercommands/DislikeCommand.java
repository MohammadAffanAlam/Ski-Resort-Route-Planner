package edu.kit.kastel.commandhandler.skiercommands;

import edu.kit.kastel.logic.SkiResort;
import edu.kit.kastel.logic.graphlogic.slopelogic.SlopeDifficulty;
import edu.kit.kastel.logic.graphlogic.slopelogic.SlopeSurface;
import edu.kit.kastel.commandhandler.Command;
import edu.kit.kastel.commandhandler.CommandExecutionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Command handler for adding dislikes of the current skier.
 * @author uurce
 */
public class DislikeCommand extends Command {
    private static final String DISLIKE_COMMAND_NAME = "dislike";
    private static final String DISLIKE_COMMAND_DIFFICULTY_REGEX = "dislike (BLUE|RED|BLACK)";
    private static final String DISLIKE_COMMAND_SURFACE_REGEX = "dislike (REGULAR|ICY|BUMPY)";
    private static final int PARAMETER_MATCHER_INDEX = 1;

    /**
     * Creates a new dislike command handler object.
     * @param resort The resort instance to run the command for.
     */
    public DislikeCommand(SkiResort resort) {
        super(DISLIKE_COMMAND_NAME, resort);
    }

    @Override
    public String execute(String input) throws CommandExecutionException {
        if (input.matches(DISLIKE_COMMAND_DIFFICULTY_REGEX)) {
            Matcher inputMatcher = Pattern.compile(DISLIKE_COMMAND_DIFFICULTY_REGEX).matcher(input);
            inputMatcher.matches(); // Matches command to find and store the matcher group.
            resort.addDislike(SlopeDifficulty.getSlopeDifficulty(inputMatcher.group(PARAMETER_MATCHER_INDEX)));
        } else if (input.matches(DISLIKE_COMMAND_SURFACE_REGEX)) {
            Matcher inputMatcher = Pattern.compile(DISLIKE_COMMAND_SURFACE_REGEX).matcher(input);
            inputMatcher.matches(); // Matches command to find and store the matcher group.
            resort.addDislike(SlopeSurface.getSlopeSurface(inputMatcher.group(PARAMETER_MATCHER_INDEX)));
        } else {
            throw new CommandExecutionException(INVALID_INPUT_ERROR);
        }
        return null;
    }
}
