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

	private static final String[] STATE_NAMES = {"Hold", "Ready", "Run", "Suspend", "Blocked", "Done"};

	private static final int[] INITIAL_JOB_STATES = {1, 3, 4}; // The initially active job states (correspond with the state names key/index)
	private static final int INITIAL_JOB_SIZE = 320; // The amount of memory that each initial ACTIVE job has
	private static final int INITIAL_JOB_TIME = 6; // The CPU time requirement of each initially ACTIVE job
	private static final int INITIAL_NUM_HELD = 10; // The number of initially inactive/held jobs

	// Program wide objects
	public static Random random;

	// Class wide objects
	private static EventManager events;

	// Constructor
	private static void run() {
		// Let's create/start our event manager
		 events = new EventManager();

		// Let's initialize the system with our initial conditions
		initialConditions();
	}

	// Private function to setup the initial conditions
	private static void initialConditions() {
		// Let's create our initially active processes
		for (int state : INITIAL_JOB_STATES) {
			// Create the process
			Process job = new Process(INITIAL_JOB_SIZE, INITIAL_JOB_TIME);

			// Add the process to the event manager's map
			events.addProcess(job, STATE_NAMES[state]);
			System.out.println("Process created at state: \"" + STATE_NAMES[state] + "\" with ID: " + job.getId() + ", Size: " + job.getSize() + "k, and Time: " + job.getTime());
		}

		// Now, let's create our initially inactive/held jobs
		for (int i = 0; i < INITIAL_NUM_HELD; i++) {
			// Create the process
			Process job = new Process();

			// Add the process to the event manager's map
			events.addProcess(job, "Hold");
			System.out.println("Process created at state: \"Hold\" with ID: " + job.getId() + ", Size: " + job.getSize() + "k, and Time: " + job.getTime());
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
