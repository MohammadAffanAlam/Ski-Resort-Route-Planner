package edu.kit.kastel.commandhandler.skiercommands;

import edu.kit.kastel.logic.SkiResort;
import edu.kit.kastel.logic.skierlogic.SkierGoals;
import edu.kit.kastel.logic.skierlogic.SkierSkillLevel;
import edu.kit.kastel.commandhandler.Command;
import edu.kit.kastel.commandhandler.CommandExecutionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Command handler for setting the skill or goal of the current skier.
 * @author uurce
 */
public class SetCommand extends Command {
    private static final String SET_COMMAND_NAME = "set";
    private static final String SET_SKILL_REGEX = "set skill (BEGINNER|INTERMEDIATE|EXPERT)";
    private static final String SET_GOAL_REGEX = "set goal (ALTITUDE|DISTANCE|NUMBER|UNIQUE)";
    private static final int PARAMETER_MATCHER_INDEX = 1;

    /**
     * Creates a new set command handler object.
     * @param resort The resort instance to run the command for.
     */
    public SetCommand(SkiResort resort) {
        super(SET_COMMAND_NAME, resort);
    }

    @Override
    public String execute(String input) throws CommandExecutionException {
        if (input.matches(SET_SKILL_REGEX)) {
            Matcher inputMatcher = Pattern.compile(SET_SKILL_REGEX).matcher(input);
            inputMatcher.matches(); // Matches command to find and store the matcher group.
            resort.setSkierSkill(SkierSkillLevel.getSkierSkillLevel(inputMatcher.group(PARAMETER_MATCHER_INDEX)));
        } else if (input.matches(SET_GOAL_REGEX)) {
            Matcher inputMatcher = Pattern.compile(SET_GOAL_REGEX).matcher(input);
            inputMatcher.matches(); // Matches command to find and store the matcher group.
            resort.setSkierGoal(SkierGoals.getSkierGoals(inputMatcher.group(PARAMETER_MATCHER_INDEX)));
        } else {
            throw new CommandExecutionException(INVALID_INPUT_ERROR);
        }
        return null;
    }
}
