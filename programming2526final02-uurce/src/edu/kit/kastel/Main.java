package edu.kit.kastel;

import edu.kit.kastel.commandhandler.routecommands.AbortCommand;
import edu.kit.kastel.commandhandler.routecommands.AlternativeCommand;
import edu.kit.kastel.commandhandler.Command;
import edu.kit.kastel.commandhandler.CommandExecutionException;
import edu.kit.kastel.commandhandler.skiercommands.DislikeCommand;
import edu.kit.kastel.commandhandler.skiercommands.LikeCommand;
import edu.kit.kastel.commandhandler.outputcommands.ListCommand;
import edu.kit.kastel.commandhandler.LoadAreaCommand;
import edu.kit.kastel.commandhandler.routecommands.NextCommand;
import edu.kit.kastel.commandhandler.routecommands.PlanCommand;
import edu.kit.kastel.commandhandler.skiercommands.ResetPreferenceCommand;
import edu.kit.kastel.commandhandler.skiercommands.SetCommand;
import edu.kit.kastel.commandhandler.outputcommands.ShowRouteCommand;
import edu.kit.kastel.commandhandler.routecommands.TakeCommand;
import edu.kit.kastel.logic.SkiResort;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The main class, which reads the user input for the ski resort program.
 * @author uurce
 */
public final class Main {
    private static final String COMMAND_NOT_FOUND_ERROR = "Error, This command does not exist.";
    private static final Map<String, Command> COMMANDS = new HashMap<>();
    private static final String QUIT_COMMAND = "quit";
    private static final String COMMAND_INPUT_SEPARATOR = " ";

    private Main() {

    }

    /**
     * Main method, which runs the program.
     * @param args The terminal inputs. Unused in the ski resort program.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        initializeCommands(new SkiResort());

        String userInput = scanner.nextLine();
        while (!userInput.equals(QUIT_COMMAND)) {
            handleInput(userInput);
            userInput = scanner.nextLine();
        }

        scanner.close();
    }

    /**
     * Assigns the input to an applicable command handler object, and executes the command, printing out the output or error directly.
     * @param input The user input given.
     */
    private static void handleInput(String input) {
        for (Command command : COMMANDS.values()) {
            if (input.split(COMMAND_INPUT_SEPARATOR)[0].equals(command.getCommandName())) {
                String output = null;
                try {
                    output = command.execute(input);
                } catch (CommandExecutionException commandOperationError) {
                    System.out.println(commandOperationError.getMessage());
                }
                if (output != null) {
                    System.out.println(output);
                }
                return;
            }
        }

        System.out.println(COMMAND_NOT_FOUND_ERROR);
    }

    /**
     * Initializes all of the command handler objects for each command.
     * @param skiResort The ski resort instance for which the commands are ran for.
     */
    private static void initializeCommands(SkiResort skiResort) {
        addCommand(new LoadAreaCommand(skiResort));
        addCommand(new ListCommand(skiResort));
        addCommand(new SetCommand(skiResort));
        addCommand(new LikeCommand(skiResort));
        addCommand(new DislikeCommand(skiResort));
        addCommand(new ResetPreferenceCommand(skiResort));
        addCommand(new PlanCommand(skiResort));
        addCommand(new AbortCommand(skiResort));
        addCommand(new NextCommand(skiResort));
        addCommand(new TakeCommand(skiResort));
        addCommand(new AlternativeCommand(skiResort));
        addCommand(new ShowRouteCommand(skiResort));
    }

    private static void addCommand(Command command) {
        COMMANDS.put(command.getCommandName(), command);
    }
}
