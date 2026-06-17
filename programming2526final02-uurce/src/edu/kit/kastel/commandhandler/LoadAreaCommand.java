package edu.kit.kastel.commandhandler;

import edu.kit.kastel.logic.ResortLogicException;
import edu.kit.kastel.logic.SkiResort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Command handler for loading a graph of lifts and slopes to a ski resort.
 * @author uurce
 */
public class LoadAreaCommand extends Command {
    private static final String LOAD_AREA_COMMAND_NAME = "load";
    private static final String LOAD_AREA_INPUT_START = "load area ";
    private static final String UNREADABLE_FILE_ERROR = "Error, Unable to read inputted file.";

    /**
     * Creates a new load area command handler object.
     * @param resort The resort instance to run the command for.
     */
    public LoadAreaCommand(SkiResort resort) {
        super(LOAD_AREA_COMMAND_NAME, resort);
    }

    @Override
    public String execute(String input) throws CommandExecutionException {
        if (input.startsWith(LOAD_AREA_INPUT_START) && input.length() > LOAD_AREA_INPUT_START.length()) {
            List<String> fileInput;
            String output = "";
            try {
                fileInput = Files.readAllLines(Path.of(input.substring(LOAD_AREA_INPUT_START.length())));
            } catch (IOException exception) {
                throw new CommandExecutionException(UNREADABLE_FILE_ERROR);
            }
            for (String lineInput : fileInput) {
                output += lineInput + System.lineSeparator();
            }

            try {
                resort.loadArea(fileInput);
            } catch (ResortLogicException resortLogicException) {
                return output + resortLogicException.getMessage();
            }
            return output.substring(0, output.length() - 1);
        }

        throw new CommandExecutionException(INVALID_INPUT_ERROR);
    }
}