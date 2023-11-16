package filter.concurrent;

/**
 * Implements printing as a {@link ConcurrentFilter} - overrides necessary
 * behavior of ConcurrentFilter
 * 
 */

/**
 * 
 * @author Tianling Hou
 *
 */
public class PrintFilter extends ConcurrentFilter {

	/**
	 * Overrides ConcurrentFilter.processLine() to just print the line to stdout.
	 */
	@Override
	protected String processLine(String line) {

		System.out.println(line);
		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
