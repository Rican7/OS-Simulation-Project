/********************************/
// Operating Systems Simulation //
//   Created by Trevor Suarez   //
/********************************/

// Imports (libraries and utilities)
import java.util.Random;

// Simulation main engine class
public class Simulation {
	// Declare final variables (constants)
	private static final int MAX_MEMORY = 2048; // Total available user memory
	private static final int MAX_EVENTS = 500; // Maximum number of events to be fired before quitting

	// Program wide objects
	public static Random random;

	// Constructor
	private static void run() {
		EventManager events = new EventManager();
		System.exit(0);

		// To test the random process generation, let's create a bunch of processes and print them to the screen (log)
		while (Process.numOfProcesses < 100) {
			Process job = new Process();

			System.out.println("Process created with ID: " + job.getId() + ", Size: " + job.getSize() + "k, and Time: " + job.getTime());
		}
	}

	// Main function
	public static void main(String[] args) {
		// Instanciate program wide objects
		random = new Random();

		// Begin the simulation
		run();
	}

} // End Simulation class
