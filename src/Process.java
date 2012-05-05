// Process class 
public class Process implements Comparable<Process> {
	// Declare final variables (constants)
	private static final int MIN_SIZE = 256; // Lowest possible memory size for a process
	private static final int MAX_SIZE = 1024; // Highest possible memory size for a process
	private static final int SIZE_STEPPING = 64; // Memory stepping
	private static final int MIN_REQ_TIME = 3; // Lowest possible cpu time
	private static final int MAX_REQ_TIME = 15; // Highest possible cpu time
	private static final int TIME_STEPPING = 3; // CPU time stepping

	// Declare static (global) variables
	public static int numOfProcesses = 0;

	// Declare properties
	private int identifier;
	private int size;
	private int cpuReqTime;
	private int cpuUsedTime;

	// Constructor
	public Process(int setSize, int setTime) {
		// Let's generate a process ID
		this.identifier = generateId();

		// Set the instances properties
		this.size = setSize;
		this.cpuReqTime = setTime;

		// Increment the numOfProcesses
		numOfProcesses++;
	}

	// Empty parameter constructor
	public Process() {
		// Let's generate some parameters and call the normal function
		this(generateSize(), generateTime());
	}

	// Private function to generate a unique process identifier
	private int generateId() {
		// Return an id that is equal to the number of processes created
		return numOfProcesses;
	}

	// Private function to generate a process size with these limitations:
	// Must be an int between minSize and maxSize
	// Int must be randomly generated in steps of sizeStepping
	private static int generateSize() {
		// Create the random number's maximum range
		int randMax = ( MAX_SIZE / SIZE_STEPPING ) - ( MIN_SIZE / SIZE_STEPPING ) + 1;

		// Generate a random int within the constraints
		int n = Simulation.random.nextInt( randMax );

		// Use a y-intercept style function of n to calculate the int within its constraints
		int size = ( SIZE_STEPPING * n ) + MIN_SIZE;

		// Return the calculated size. :)
		return size;
	}

	// Private function to generate a process's required cpu time with these limitations:
	// Must be an int between MIN_REQ_TIME and MAX_REQ_TIME
	// Int must be randomly generated in steps of TIME_STEPPING
	private static int generateTime() {
		// Create the random number's maximum range
		int randMax = ( MAX_REQ_TIME / TIME_STEPPING ) - ( MIN_REQ_TIME / TIME_STEPPING ) + 1;

		// Generate a random int within the constraints
		int n = Simulation.random.nextInt( randMax );

		// Use a y-intercept style function of n to calculate the int within its constraints
		int time = ( TIME_STEPPING * n ) + MIN_REQ_TIME;

		// Return the calculated time. :)
		return time;
	}

	// Public function to get the process's identifier
	public int getId() {
		return this.identifier;
	}

	// Public function to get the process's memory size
	public int getSize() {
		return this.size;
	}

	// Public function to get the process's required cpu time
	public int getReqTime() {
		return this.cpuReqTime;
	}

	// Public function to get the process's used cpu time
	public int getUsedTime() {
		return this.cpuUsedTime;
	}

	// Public function for the process to "use cpu time"
	public void useTime(int time) {
		this.cpuUsedTime += time;
	}

	// Public function to check if the process is "DONE" ()
	public boolean isDone() {
		// If its used time has reached its required time
		if (this.cpuUsedTime == this.cpuReqTime) {
			// If it got here, It must be done
			return true;
		}

		// If it got here, it must not be done
		return false;
	}

	// Private function compareTo, implementing the Comparable interface
	public int compareTo(Process process) {
		if (this.getSize() < process.getSize()) {
			return -1;
		}
		else if (this.getSize() > process.getSize()) {
			return 1;
		}

		// If it got here, it must be equal
		return 0;
	}

	// Public function to convert the Process into a string
	public String toString() {
		// Create a string from the object's properties
		String string = "Process #" + this.getId() + "/" + this.getSize() + "k/" + this.getReqTime() + "t";

		return string;
	}

} // End Process class
