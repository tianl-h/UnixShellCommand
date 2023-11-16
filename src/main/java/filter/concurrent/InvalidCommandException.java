package filter.concurrent;
/**
 * Custom unchecked exception used to pass information on command errors (syntax
 * + piping errors) between filters, command builder and the REPL.
 * 
 *
 */

/**
 * 
 * @author Tianling Hou
 *
 */
public class InvalidCommandException extends RuntimeException {

	public InvalidCommandException(String errorMessage) {
		super(errorMessage);
	}

}
