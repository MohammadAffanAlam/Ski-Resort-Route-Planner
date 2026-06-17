package edu.kit.kastel.commandhandler;

/**
 * Exception class for errors caused when running a command.
 * @author uurce
 */
public class CommandExecutionException extends Exception {
    /**
     * Creates a new command execution exception.
     * @param message The message associated with the exception.
     */
    public CommandExecutionException(String message) {
        super(message);
    }
}
