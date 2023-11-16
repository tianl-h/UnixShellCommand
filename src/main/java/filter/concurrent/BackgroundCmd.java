package filter.concurrent;

import java.util.LinkedList;

/**
 * 
 * @author Tianling Hou
 *
 */

public class BackgroundCmd {
	boolean isAlive;
	String command;
	LinkedList<Thread> threadList;
	
	public BackgroundCmd(int index, String cmd){
		isAlive = true;
		command = "\t" + index + ". " + cmd;
		threadList = new LinkedList<Thread>();
	}
		
    /**
     * Add a thread to the list of threads associated with this background command.
     *
     * @param thread The thread to add.
     */
	public void add(Thread t){
		threadList.add(t);
	}
	
	/**
     * Check whether any of the associated threads are still alive.
     *
     * @return true if at least one thread is still alive, false otherwise.
     */
	public boolean areAnyThreadsAlive(){
		if (isAlive) {
			for (Thread t: threadList){
				if (t.isAlive()) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
     * Get the the background command.
     *
     * @return the background command.
     */
	public String getCommand(){
		return command;
	}

	/**
     * Kill the background command by interrupting all associated threads.
     */
	public void kill(){
		isAlive = false;
		for (Thread t: threadList){
			t.interrupt();
		}
	}
	
	/**
     * Get the list of threads associated with this background command.
     *
     * @return The list of threads.
     */
	public LinkedList<Thread> getThreadList() {
		return threadList;
	}
}
