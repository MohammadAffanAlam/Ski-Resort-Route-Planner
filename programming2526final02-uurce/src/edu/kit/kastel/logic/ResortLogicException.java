package edu.kit.kastel.logic;

/**
 * Exception class for errors caused within the logic of the ski resort program.
 * @author uurce
 */
public class ResortLogicException extends Exception {
    /**
     * Creates a new logic operation exception.
     * @param message The message associated with the exception.
     */
    public ResortLogicException(String message) {
        super(message);
    }
}
