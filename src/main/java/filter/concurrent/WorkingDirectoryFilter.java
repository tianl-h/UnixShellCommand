package filter.concurrent;

import filter.CurrentWorkingDirectory;
import filter.Filter;
import filter.Message;

/**
 * Implements pwd command - overrides necessary behavior of ConcurrentFilter
 */

/**
 * 
 * @author Tianling Hou
 *
 */
public class WorkingDirectoryFilter extends ConcurrentFilter {

	/**
	 * command that was used to construct this filter
	 */
	private String command;

	/**
	 * Constructs a pwd filter.
	 * @param cmd cmd is guaranteed to either be "pwd" or "pwd" surrounded by whitespace
	 */
	public WorkingDirectoryFilter(String cmd) {
		super();
		command = cmd;
	}

	/**
	 * Overrides {@link ConcurrentFilter#process()} by adding
	 * {@link SequentialREPL#currentWorkingDirectory} to the output queue
	 */
	@Override
	public void process() {

		try {
			output.writeAndWait(CurrentWorkingDirectory.get());
			output.writePoisonPill();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Overrides ConcurrentFilter.processLine() - doesn't do anything.
	 */
	@Override
	protected String processLine(String line) {
		return null;
	}

	/**
	 * Overrides equentialFilter.setPrevFilter() to not allow a {@link Filter} to be
	 * placed before {@link WorkingDirectoryFilter} objects.
	 * 
	 * @throws InvalidCommandException - always
	 */
	@Override
	public void setPrevFilter(Filter prevFilter) {
		throw new InvalidCommandException(Message.CANNOT_HAVE_INPUT.with_parameter(command));
	}
}
