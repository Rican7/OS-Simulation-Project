// Process class 
public class Process {
	// Declare final variables (constants)
	private static final int min_size = 256; // Lowest possible memory size for a process
	private static final int max_size = 2048; // Highest possible memory size for a process
	private static final int size_stepping = 64; // Memory stepping
	private static final int min_time = 3; // Lowest possible cpu time
	private static final int max_time = 15; // Highest possible cpu time
	private static final int time_stepping = 3; // CPU time stepping

	// Declare static (global) variables
	public static int num_of_processes = 0;

	// Declare properties
	private int identifier;
	private int size;
	private int cpu_time;

	// Constructor
	public Process() {
		// Set the instances properties
		identifier = generate_id();
		size = generate_size();
		cpu_time = generate_time();

		// Increment the num_of_processes
		num_of_processes++;
	}

	private int generate_id() {
		// Return an id that is equal to the number of processes created
		return num_of_processes;
	}

	// Private function to generate a process size with these limitations:
	// Must be an int between min_size and max_size
	// Int must be randomly generated in steps of size_stepping
	private int generate_size() {
		// Create the random number's maximum range
		int rand_max = ( max_size / size_stepping ) - ( min_size / size_stepping ) + 1;

		// Generate a random int within the constraints
		int n = Simulation.random.nextInt( rand_max );

		// Use a y-intercept style function of n to calculate the int within its constraints
		int size = ( size_stepping * n ) + min_size;

		// Return the calculated size. :)
		return size;
	}

	// Private function to generate a process's required cpu time with these limitations:
	// Must be an int between min_time and max_time
	// Int must be randomly generated in steps of time_stepping
	private int generate_time() {
		// Create the random number's maximum range
		int rand_max = ( max_time / time_stepping ) - ( min_time / time_stepping ) + 1;

		// Generate a random int within the constraints
		int n = Simulation.random.nextInt( rand_max );

		// Use a y-intercept style function of n to calculate the int within its constraints
		int time = ( time_stepping * n ) + min_time;

		// Return the calculated time. :)
		return time;
	}

	// Public function to get the process's identifier
	public int get_id() {
		return identifier;
	}

	// Public function to get the process's memory size
	public int get_size() {
		return size;
	}

	// Public function to get the process's cpu time
	public int get_time() {
		return cpu_time;
	}

} // End Process class
