package edu.kit.kastel.commandhandler.routecommands;

import edu.kit.kastel.logic.ResortLogicException;
import edu.kit.kastel.logic.SkiResort;
import edu.kit.kastel.commandhandler.Command;
import edu.kit.kastel.commandhandler.CommandExecutionException;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Command handler for planning a new route for the current skier.
 * @author uurce
 */
public class PlanCommand extends Command {
    private static final String PLAN_COMMAND_NAME = "plan";
    private static final String PLAN_COMMAND_REGEX = "plan ([A-Za-z]+) ([0-9]{2}:[0-9]{2}) ([0-9]{2}:[0-9]{2})";
    private static final String ROUTE_PLANNED_OUTPUT = "route planned";
    private static final String INVALID_DATE_ERROR = "Error, The inputted date is invalid and cannot be parsed.";
    private static final int LIFT_ID_INPUT_MATCHER_INDEX = 1;
    private static final int START_TIME_INPUT_MATCHER_INDEX = 2;
    private static final int END_TIME_INPUT_MATCHER_INDEX = 3;

    /**
     * Creates a new plan command handler object.
     * @param resort The resort instance to run the command for.
     */
    public PlanCommand(SkiResort resort) {
        super(PLAN_COMMAND_NAME, resort);
    }

    @Override
    public String execute(String input) throws CommandExecutionException {
        Matcher inputMatcher = Pattern.compile(PLAN_COMMAND_REGEX).matcher(input);
        if (!inputMatcher.matches()) {
            throw new CommandExecutionException(INVALID_INPUT_ERROR);
        }

        LocalTime startTime;
        LocalTime endTime;

        try {
            startTime = LocalTime.parse(inputMatcher.group(START_TIME_INPUT_MATCHER_INDEX));
            endTime = LocalTime.parse(inputMatcher.group(END_TIME_INPUT_MATCHER_INDEX));
        } catch (DateTimeParseException invalidDateException) {
            throw new CommandExecutionException(INVALID_DATE_ERROR);
        }

        try {
            resort.startRoutePlanning(inputMatcher.group(LIFT_ID_INPUT_MATCHER_INDEX), startTime, endTime);
        } catch (ResortLogicException resortLogicException) {
            throw new CommandExecutionException(resortLogicException.getMessage());
        }
        return ROUTE_PLANNED_OUTPUT;
    }
}
