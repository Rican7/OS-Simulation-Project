// Imports (libraries and utilities)
import java.util.ArrayList;
import java.util.HashMap;

// External imports
import com.google.common.collect.*;

// Event Manager class
public class EventManager {
	// Declare properties
	private Multimap<String, Integer> systemStates;

	// Constructor
	public EventManager() {
		// Let's initialize the Event Manager
		buildStateMap();

	}

	// Private function to build the system state array/multimap
	private void buildStateMap() {
		// First of all, let's instanciate a multimap
		systemStates = ArrayListMultimap.create();

		// Create a simple array containing the names of the keys of the map
		String[] stateNames = {"Hold", "Ready", "Run", "Suspend", "Blocked", "Done"};

		// Let's loop through the name array
		for (String name : stateNames) {
			// Let's create an arraylist for each key
			systemStates.put(name, 0);
			System.out.println("Multimap key: " + name + " added. Multimap size: " + systemStates.size());
		}
	}
}
