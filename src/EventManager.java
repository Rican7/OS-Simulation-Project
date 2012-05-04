// Imports (libraries and utilities)
import java.util.Map;
import java.util.List;

// External imports
import com.google.common.collect.*;
import com.sun.istack.internal.Nullable;

// Event Manager class
public class EventManager {
	// Declare properties
	private ArrayListMultimap<String, Process> systemStates;
	private Map<String, Integer> stateLimits;

	// Constructor
	public EventManager() {
		// Let's initialize the Event Manager
		this.buildStateMap();

		// Let's initialize the state limits map
		this.buildStateLimitsMap();
	}

	// Private function to build the system state array/multimap
	private void buildStateMap() {
		// First of all, let's instanciate a multimap
		this.systemStates = ArrayListMultimap.create();
	}

	// Private function to build the system state limit map
	private void buildStateLimitsMap() {
		// First of all, let's instanciate a hashmap
		this.stateLimits = Maps.newHashMap();

		// Let's add our state limits
		this.stateLimits.put("Ready", 4);
		this.stateLimits.put("Blocked", 6);
	}

	// Private function to detect if the system state is full
	private boolean isStateFull(String state) {
		// Let's first see if the given state even has a limit
		if (this.stateLimits.containsKey(state)) {
			// Ok, the key must have a limit if it got here, so let's check to see if that limit's been reached
			// Get the states limit
			int stateLimit = this.stateLimits.get(state);

			// Let's get a Multiset of the keys found in the state map
			Multiset<String> keys = this.systemStates.keys();

			// Get a count of the number of times a key appears in that set
			int stateProcessCount = keys.count(state);

			// If the number of processes in that state are at the limit
			if (stateProcessCount == stateLimit) {
				// Return true. The state is full
				return true;
			}
		}

		return false;
	}

	// Public function to add processes to the event manager
	public boolean addProcess(Process process, String initialState) {
		// Let's first check if the state we're trying to put this process in is full
		if (this.isStateFull(initialState) != true) {
			// Let's add the process to the state map
			if (this.systemStates.put(initialState, process)) {
				// If we got here, the process has successfully been added to the state manager
				return true;
			}
		}

		return false;
	}

	// Public function to get the process from the given state
	@Nullable public Process getProcess(String state) {
		// Let's create a process to be returned
		Process process = null; // Process may be null. We may not get back a process

		// Get a list of processes in the given state
		List<Process> processes = this.systemStates.get(state);

		// Let's make sure the list of processes in that state aren't empty
		if (processes.isEmpty() != true) {
			// Return the first process in the list (at key/index 0)
			process = processes.get(0);
		}

		return process;
	}
}
