package filter.concurrent;

/**
 * Implements head command - overrides necessary behavior of ConcurrentFilter
 * 
 *
 */

/**
 * 
 * @author Tianling Hou
 *
 */
public class HeadFilter extends ConcurrentFilter {

	/**
	 * number of lines read so far
	 */
	private int numRead;

	/**
	 * number of lines passed to output via head
	 */
	private static int LIMIT = 10;

	/**
	 * Constructs a head filter.
	 */
	public HeadFilter() {
		super();
		numRead = 0;
	}

	/**
	 * Overrides {@link ConcurrentFilter#process()} to only add up to 10 lines to
	 * the output queue.
	 */
	@Override
	 public void process() {
        try {
            String line;
            while ((line = input.readAndWait()) != null && numRead < LIMIT) {
                output.writeAndWait(line);
                numRead++;
            }
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
}
