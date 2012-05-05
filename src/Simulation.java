/********************************/
// Operating Systems Simulation //
//   Created by Trevor Suarez   //
/********************************/

// Imports (libraries and utilities)
import java.util.List;
import java.util.Arrays;
import java.util.Random;

// External imports
import com.google.common.collect.*;

// Simulation main engine class
public class Simulation {
	// Declare final variables (constants)
	public static final int MAX_MEMORY = 2048; // Total available user memory
	public static final int MAX_EVENTS = 500; // Maximum number of events to be fired before quitting

	private static final String[] STATE_NAMES = {"Hold", "Ready", "Run", "Suspend_System", "Suspend_User", "Blocked", "Done"}; // The names of each possible state

	private static final int[] INITIAL_JOB_STATES = {1, 3, 5}; // The initially active job states (correspond with the state names key/index)
	private static final int INITIAL_JOB_SIZE = 320; // The amount of memory that each initial ACTIVE job has
	private static final int INITIAL_JOB_TIME = 6; // The CPU time requirement of each initially ACTIVE job
	private static final int INITIAL_NUM_HELD = 10; // The number of initially inactive/held jobs
	private static final int TOTAL_NUM_JOBS = INITIAL_JOB_STATES.length + INITIAL_NUM_HELD; // The total number of jobs in the OS Simulation

	private static final int PROCESS_RUN_TIME = 3; // The number of "CPU Time Units" that the currently running process uses on each event cycle

	// Program wide objects
	public static Random random;

	// Class wide objects
	public static boolean debugMode;
	private static EventManager states;
	private static MemoryManager memory;
	private static List<Event> events;
	private static boolean systemRunning;
	private static int totalEventCount;

	// Constructor
	private static void run() {
		// Let's create/start our event manager
		states = new EventManager();

		// Let's create our memory manager
		memory = new MemoryManager();

		// Let's fill our event array list with our randomized events
		buildEventsList();

		// Let's initialize the system with our initial conditions
		initialConditions();

		// Ok. Everything's set up, so let's run the system
		startSystem();
	}

	// Private function to setup the initial conditions
	private static void initialConditions() {
		// Let's create our initially active processes
		for (int state : INITIAL_JOB_STATES) {
			// Create the process
			Process job = new Process(INITIAL_JOB_SIZE, INITIAL_JOB_TIME);

			// Add the process to the event manager's map
			states.addProcess(job, STATE_NAMES[state]);

			// Only show if debugMode is on
			if (debugMode) {
				System.out.println("Process created at state: \"" + STATE_NAMES[state] + "\" with ID: " + job.getId() + ", Size: " + job.getSize() + "k, and Time: " + job.getReqTime());
			}

			// Add the process to the system's memory
			memory.addProcess(job);

			// Only show if debugMode is on
			if (debugMode) {
				System.out.println("Process " + job.getId() + " added to memory with size " + job.getSize() + "k");
			}
		}

		// Now, let's create our initially inactive/held jobs
		for (int i = 0; i < INITIAL_NUM_HELD; i++) {
			// Create the process
			Process job = new Process();

			// Add the process to the event manager's map
			states.addProcess(job, "Hold");

			// Only show if debugMode is on
			if (debugMode) {
				System.out.println("Process created at state: \"Hold\" with ID: " + job.getId() + ", Size: " + job.getSize() + "k, and Time: " + job.getReqTime());
			}
		}
	}

	// Private function to build the event list
	private static void buildEventsList() {
		// First of all, let's instanciate an array list
		events = Lists.newArrayList();

		// Let's add our events to the array list
		events.add(new Event("Hold", "Ready")); // Event from and to
		events.add(new Event("Ready", "Run"));
		events.add(new Event("Run", "Blocked"));
		events.add(new Event("Blocked", "Ready"));
		events.add(new Event("Run", "Suspend_User")); // User
		events.add(new Event("Run", "Suspend_System")); // Timer/System
		events.add(new Event("Blocked", "Done")); // System killed
		events.add(new Event("Suspend", "Done")); // User killed
		events.add(new Event("Suspend_User", "Ready")); // User
		events.add(new Event("Suspend_System", "Ready")); // Timer/System
		events.add(new Event("Run", "Done"));
		events.add(new Event("Ready", "Hold"));
	}

	// Private function to run the process that is currently granted the CPU
	private static void runProcess() {
		// Let's get the process in the Run state
		Process process = states.getProcess("Run"); // May be null

		// If we actually got back a process
		if (process != null) {
			// Only show if debugMode is on
			if (debugMode) {
				System.out.println("Running process: " + process.toString());
			}

			// Let's run the process for a set "time"
			process.useTime(PROCESS_RUN_TIME);

			// If the process is "DONE" (its used time has reached its required time)
			if (process.isDone()) {
				// We need to fire a Run->Done event
				fireEvent(new Event("Run", "Done"));

				// Only show if debugMode is on
				if (debugMode) {
					System.out.println("Process finished after running");
				}
			}
		}
	}

	// Private function to generate a random event from the events list
	private static Event generateRandomEvent() {
		// Create the number's maximum range
		int randMax = events.size();

		// Generate a random int within the constraints
		int n = random.nextInt(randMax);

		// Let's get the event at that random position n
		Event generatedEvent = events.get(n);

		// Return the randomly generated event
		return generatedEvent;
	}

	// Private function to fire the event passed to it
	private static boolean fireEvent(Event event) {
		// Let's get the first process in the "from" location
		Process process = states.getProcess(event.from); // May be null

		// If we actually got back a process
		if (process != null) {
			// Only show if debugMode is on
			if (debugMode) {
				System.out.println("Event " + event.toString() + " firing on process: " + process.toString());
			}

			// For a hold->ready event, let's make sure the system has enough memory to hold the new process
			if (event.from == "Hold" && event.to == "Ready") {
				// Let's make sure this is all possible
				if (memory.isAddPossible(process) && states.isAddPossible(process, event.to)) {
					// If the process is successfully added to memory AND the process successfully changed state 
					if (memory.addProcess(process) && states.changeProcessState(event)) {
						// If we made it here, the event has succeeded
						return true;
					}
				}
			}
			// For a Ready->Run event
			else if (event.from == "Ready" && event.to == "Run") {
				// Let's make sure this is all possible
				if (states.isAddPossible(process, event.to)) {
					// Let's first make sure that the Ready state isn't empty
					if (states.isStateEmpty(event.from)) {
						// We need to fire an event to get a process in the ready state
						// Let's see if we can grab one from Suspend_System
						if (fireEvent(new Event("Suspend_System", event.from)) != true) {
							// If we couldn't grab one from Suspend_System, we should try to grab one from Hold
							fireEvent(new Event("Hold", event.from));
						}
					}

					// Let's first make sure that the Run state isn't full
					if (states.isStateFull(event.to)) {
						// We need to fire an event to get the process out of the run state
						fireEvent(new Event(event.to, "Suspend_System"));
					}

					// If the process successfully changed state 
					if (states.changeProcessState(event)) {
						// If we made it here, the event has succeeded
						return true;
					}
				}
			}
			// For a Run->Blocked event
			else if (event.from == "Run" && event.to == "Blocked") {
				// Let's make sure this is all possible
				if (states.isAddPossible(process, event.to)) {
					// Let's first make sure that the Blocked state isn't full
					if (states.isStateFull(event.to)) {
						// We need to fire an event to get the process out of the blocked state
						fireEvent(new Event(event.to, "Done"));
					}

					// If the process successfully changed state 
					if (states.changeProcessState(event)) {
						// Ok, it succeeded, but now there's nothing "running" (in the run event), so let's fix that
						fireEvent(new Event("Ready", event.from));

						// If we made it here, the event has succeeded
						return true;
					}
				}
			}
			// For a Blocked->Ready event
			else if (event.from == "Blocked" && event.to == "Ready") {
				// Let's make sure this is all possible
				if (states.isAddPossible(process, event.to)) {
					// Let's first make sure that the Ready state isn't full
					if (states.isStateFull(event.to)) {
						// We need to fire an event to get the process out of the blocked state
						fireEvent(new Event(event.to, "Hold"));
					}

					// If the process successfully changed state 
					if (states.changeProcessState(event)) {
						// If we made it here, the event has succeeded
						return true;
					}
				}
			}
			// For a Run->Suspend_User event
			else if (event.from == "Run" && event.to == "Suspend_User") {
				// Let's make sure this is all possible
				if (states.isAddPossible(process, event.to)) {
					// If the process successfully changed state 
					if (states.changeProcessState(event)) {
						// Ok, it succeeded, but now there's nothing "running" (in the run event), so let's fix that
						fireEvent(new Event("Ready", event.from));

						// If we made it here, the event has succeeded
						return true;
					}
				}
			}
			// For a Run->Suspend_System event
			else if (event.from == "Run" && event.to == "Suspend_System") {
				// Let's make sure this is all possible
				if (states.isAddPossible(process, event.to)) {
					// If the process successfully changed state 
					if (states.changeProcessState(event)) {
						// Ok, it succeeded, but now there's nothing "running" (in the run event), so let's fix that
						fireEvent(new Event("Ready", event.from));

						// If we made it here, the event has succeeded
						return true;
					}
				}
			}
			// For a Blocked->Done event
			else if (event.from == "Blocked" && event.to == "Done") {
				// Let's make sure this is all possible
				if (states.isAddPossible(process, event.to)) {
					// If the process is successfully removed from memory AND the process successfully changed state 
					if (memory.removeProcess(process) && states.changeProcessState(event)) {
						// Ok, it succeeded, but now there's nothing "running" (in the run event), so let's fix that
						fireEvent(new Event("Ready", event.from));

						// If we made it here, the event has succeeded
						return true;
					}
				}
			}
			// For a Suspend_User->Done event
			else if (event.from == "Suspend_User" && event.to == "Done") {
				// Let's make sure this is all possible
				if (states.isAddPossible(process, event.to)) {
					// If the process is successfully removed from memory AND the process successfully changed state 
					if (memory.removeProcess(process) && states.changeProcessState(event)) {
						// Ok, it succeeded, but now there's nothing "running" (in the run event), so let's fix that
						fireEvent(new Event("Ready", event.from));

						// If we made it here, the event has succeeded
						return true;
					}
				}
			}
			// For a Suspend_User->Ready event
			else if (event.from == "Suspend_User" && event.to == "Ready") {
				// Let's make sure this is all possible
				if (states.isAddPossible(process, event.to)) {
					// Let's first make sure that the Ready state isn't full
					if (states.isStateFull(event.to)) {
						// We need to fire an event to get the process out of the blocked state
						fireEvent(new Event(event.to, "Hold"));
					}

					// If the process successfully changed state 
					if (states.changeProcessState(event)) {
						// If we made it here, the event has succeeded
						return true;
					}
				}
			}
			// For a Suspend_System->Ready event
			else if (event.from == "Suspend_System" && event.to == "Ready") {
				// Let's make sure this is all possible
				if (states.isAddPossible(process, event.to)) {
					// Let's first make sure that the Ready state isn't full
					if (states.isStateFull(event.to)) {
						// We need to fire an event to get the process out of the blocked state
						fireEvent(new Event(event.to, "Hold"));
					}

					// If the process successfully changed state 
					if (states.changeProcessState(event)) {
						// If we made it here, the event has succeeded
						return true;
					}
				}
			}
			// For a Run->Done event
			else if (event.from == "Run" && event.to == "Done") {
				// Let's make sure this is all possible
				if (states.isAddPossible(process, event.to)) {
					// If the process is successfully removed from memory AND the process successfully changed state 
					if (memory.removeProcess(process) && states.changeProcessState(event)) {
						// Ok, it succeeded, but now there's nothing "running" (in the run event), so let's fix that
						fireEvent(new Event("Ready", event.from));

						// If we made it here, the event has succeeded
						return true;
					}
				}
			}
			// For a Ready->Hold event, let's remove the largest process in memory
			else if (event.from == "Ready" && event.to == "Hold") {
				// Let's get the largest process in the "from" location
				Process largestReadyProcess = states.getLargestProcess(event.from); // May be null

				// If we actually got back a process
				if (largestReadyProcess != null) {
					// Let's make sure this is all possible
					if (states.isAddPossible(process, event.to)) {
						// If the process is successfully removed from memory AND the process successfully changed state 
						if (memory.removeProcess(largestReadyProcess) && states.changeProcessStateToHold(largestReadyProcess, event.from)) {
							// If we made it here, the event has succeeded
							return true;
						}
					}
				}
			}
			// All other events
			else {
				// Let's make sure this is all possible
				if (states.isAddPossible(process, event.to)) {
					// Let's change the processes state
					if (states.changeProcessState(event)) {
						// If we made it here, the event has succeeded
						return true;
					}
				}
			}
		}
		
		return false;
	}

	// Private function to check if the system has finished its job
	private static boolean checkFinished() {
		// If the total number of generated events has hit 500
		if (totalEventCount == MAX_EVENTS) {
			// Only show if debugMode is on
			if (debugMode) {
				System.out.println("STOPPING! The maximum number of events: " + MAX_EVENTS + " have been generated. We're not getting anywhere.");
			}

			return true;
		}

		// If every job is in the "Done" state
		if (TOTAL_NUM_JOBS == states.getProcessCount("Done")) {
			// Only show if debugMode is on
			if (debugMode) {
				System.out.println("STOPPING! The OS is \"finished\". Every process is in the \"Done\" state.");
			}

			return true;
		}

		// If it got here, the system hasn't finished yet
		return false;
	}

	// Private function to actually start the system
	private static void startSystem() {
		// Mark the system as running
		systemRunning = true;

		// While the system is still running
		while (systemRunning) {
			// First, let's run our process
			runProcess();

			// Let's generate a random event
			Event generatedEvent = generateRandomEvent();

			// Let's increment the total number of events that have been generated
			totalEventCount++;

			// Let's actually fire the event that's been generated
			boolean eventSucceeded = fireEvent(generatedEvent);

			// If the event succeeded
			if (eventSucceeded) {
			}
			else {
				// Only show if debugMode is on
				if (debugMode) {
					System.out.println("Event failed: " + generatedEvent.toString());
				}
			}

			// Let's check to see if the system has finished its job
			if (checkFinished()) {
				systemRunning = false;
			}
		}
	}

	// Main function
	public static void main(String[] args) {
		// Instanciate program wide objects
		random = new Random();

		// Let's get all the arguments as an array
		List<String> arguments = Arrays.asList(args);
		
		// If debug mode has been passed, lets enable it
		if (arguments.contains("debug")) {
			debugMode = true;
		}

		// Begin the simulation
		run();
	}

} // End Simulation class
