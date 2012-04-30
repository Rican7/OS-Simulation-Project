// Process class 
public class Process {
	// Declare final variables (constants)
	private static final int MIN_SIZE = 256; // Lowest possible memory size for a process
	private static final int MAX_SIZE = 1024; // Highest possible memory size for a process
	private static final int SIZE_STEPPING = 64; // Memory stepping
	private static final int MIN_TIME = 3; // Lowest possible cpu time
	private static final int MAX_TIME = 15; // Highest possible cpu time
	private static final int TIME_STEPPING = 3; // CPU time stepping

	// Declare static (global) variables
	public static int numOfProcesses = 0;

	// Declare properties
	private int identifier;
	private int size;
	private int cpuTime;

	// Constructor
	public Process(int setSize, int setTime) {
		// Let's generate a process ID
		identifier = generateId();

		// Set the instances properties
		size = setSize;
		cpuTime = setTime;

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
	// Must be an int between MIN_TIME and MAX_TIME
	// Int must be randomly generated in steps of TIME_STEPPING
	private static int generateTime() {
		// Create the random number's maximum range
		int randMax = ( MAX_TIME / TIME_STEPPING ) - ( MIN_TIME / TIME_STEPPING ) + 1;

		// Generate a random int within the constraints
		int n = Simulation.random.nextInt( randMax );

		// Use a y-intercept style function of n to calculate the int within its constraints
		int time = ( TIME_STEPPING * n ) + MIN_TIME;

		// Return the calculated time. :)
		return time;
	}

	// Public function to get the process's identifier
	public int getId() {
		return identifier;
	}

	// Public function to get the process's memory size
	public int getSize() {
		return size;
	}

	// Public function to get the process's cpu time
	public int getTime() {
		return cpuTime;
	}

} // End Process class
