package filter.concurrent;

import java.util.Scanner;
import java.util.List;
import java.util.LinkedList;

import filter.CurrentWorkingDirectory;
import filter.Message;
/**
 * The main implementation of the REPL loop (read-eval-print loop). It reads
 * commands from the user, parses them, executes them, and displays the result.
 */

/**
 * 
 * @author Tianling Hou
 *
 */
public class ConcurrentREPL {
	
	/**
	 * pipe string
	 */
	static final String PIPE = "|";

	/**
	 * redirect string
	 */
	static final String REDIRECT = ">";
	
	/**
	 * current working directory
	 */
    static String currentWorkingDirectory;
    
    /**
     * background task list
     */
    static LinkedList<BackgroundCmd> backgroundTask;
    
    /**
     * The main entry point of the program. It initializes variables, reads user commands,
     * and manages the execution of background commands, including the ability to list them,
     * kill specific ones, and process regular commands.
     *
     * @param args The command-line arguments (not used in this program).
     */
    public static void main(String[] args) {
        currentWorkingDirectory = CurrentWorkingDirectory.get();
        backgroundTask = new LinkedList<BackgroundCmd>();
        Scanner consoleReader = new Scanner(System.in);
        System.out.print(Message.WELCOME);
        while (true) {
            System.out.print(Message.NEWCOMMAND);
            String cmd = consoleReader.nextLine();
            if (cmd.trim().isEmpty()) {
				continue;
			}
            if (cmd.equals("exit")) {
                break;
            } else if (cmd.equals("repl_jobs")) {
                repl_jobs();
            } else if (cmd.startsWith("kill")) {
                kill(cmd);
            } else {
            	try {
            		processCommand(cmd);
            } catch (InvalidCommandException e) {
            		System.out.print(e.getMessage());
				}
            }
        } 
        consoleReader.close();
        System.out.print(Message.GOODBYE);
    }
    
    /**
     * Processes a command, potentially running it in the background, by creating and executing
     * a series of filter threads.
     *
     * @param cmd The command to process, which may contain one or more filters.
     */
    public static void processCommand(String cmd) {
        // Trim and check if the command is empty or whitespace
        if (cmd.trim().isEmpty()) {
            return; 
        }
        boolean background = false;
        // Check if the command is to run in the background
        if (cmd.trim().endsWith("&")) {
            background = true;
            // Add the background command to the task list
            backgroundTask.add(new BackgroundCmd(backgroundTask.size() + 1, cmd));
            cmd = cmd.substring(0, cmd.length() - 1);
        }
        // Create filters from the command
        List<ConcurrentFilter> filters = ConcurrentCommandBuilder.createFiltersFromCommand(cmd);
        // Check if there are filters to process
        if (filters == null) {
            return; 
        }
//        // Initialize a list to hold the filter threads
//        LinkedList<Thread> threadList = new LinkedList<Thread>();
        // Execute each filter concurrently in its own thread
        for (ConcurrentFilter filter : filters) {
        	Runnable filterRunnable = new Runnable() {
                @Override
                public void run() {
                    filter.process();
                }
            };
            Thread t = new Thread(filterRunnable);
            t.start();
//            threadList.add(t);
            // If not a background command, wait for the filter thread to complete
            if (!background) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                // Add the thread to the background command's thread list
                backgroundTask.getLast().add(t);
            }
        }
    }
    
    /**
     * Lists the background commands and their statuses.
     * Prints the commands that are still running.
     */
    public static void repl_jobs() {
        for (BackgroundCmd bcmd : backgroundTask) {
            if (bcmd.areAnyThreadsAlive()) {
                System.out.println(bcmd.getCommand());
            }
        }
    }
    
    /**
     * Kills a background command by its index.
     * 
     * @param command The "kill" command along with the index of the background command to kill.
     */
    public static void kill(String command) {
        // Split the command into components
        String[] sub = command.trim().split(" ");
        // Ensure the command has the correct number of arguments
        if (sub.length != 2) {
            System.out.printf(Message.COMMAND_NOT_FOUND.toString(), command);
            return;
        }
        int index;
        try {
            // Attempt to parse the index from the command
            index = Integer.parseInt(sub[1]);
        } catch (NumberFormatException e) {
            System.out.printf(Message.COMMAND_NOT_FOUND.toString(), command);
            return;
        }
        // Check if the index is out of the valid range of background commands
        if (index < 1 || index > backgroundTask.size()) {
            System.out.printf(Message.COMMAND_NOT_FOUND.toString(), command);
            return;
        }
        // Retrieve the background command by its index and kill it
        BackgroundCmd bcmd = backgroundTask.get(index - 1);
        bcmd.kill();
    }
}
