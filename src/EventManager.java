// Imports (libraries and utilities)
import java.util.ArrayList;
import java.util.HashMap;

// External imports
import com.google.common.collect.*;

// Event Manager class
public class EventManager {
	// Declare properties
	private Multimap<String, Process> systemStates;

	// Constructor
	public EventManager() {
		// Let's initialize the Event Manager
		buildStateMap();

	}

	// Private function to build the system state array/multimap
	private void buildStateMap() {
		// First of all, let's instanciate a multimap
		systemStates = ArrayListMultimap.create();
	}

	// Public function to add processes to the event manager
	public boolean addProcess(Process process, String initialState) {
		// Let's add the process to the state map
		systemStates.put(initialState, process);

		// Let's return true for now. Nothing can possibly fail.
		return true;
	}
}
