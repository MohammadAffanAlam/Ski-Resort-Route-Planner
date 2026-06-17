package edu.kit.kastel.commandhandler;


import edu.kit.kastel.logic.SkiResort;

/**
 * Abstract class for command handler.
 * @author uurce
 */
public abstract class Command {
    protected static final String INVALID_INPUT_ERROR = "Error, The input given is invalid.";
    protected final SkiResort resort;
    private final String commandName;

    /**
     * Creates a new command handler object.
     * @param commandName The name of the command, as would be inputted by the user.
     * @param resort The ski resort instance to run the command for.
     */
    protected Command(String commandName, SkiResort resort) {
        this.commandName = commandName;
        this.resort = resort;
    }

    /**
     * Returns the name of the command, as inputted by the user.
     * @return Name of the command.
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * Executes the command associated with the command handler.
     * @param input The user's input for the specific command.
     * @return String output of the result of the command.
     * @throws CommandExecutionException An exception that occurs during the execution of a command.
     */
    public abstract String execute(String input) throws CommandExecutionException;
}
