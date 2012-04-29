// Imports (libraries and utilities)
import java.util.ArrayList;
import java.util.HashMap;

// Event Manager class
public class EventManager {
	// Declare properties
	private HashMap<String, ArrayList<Integer>> systemStates;

	// Constructor
	public EventManager() {
		// Let's initialize the Event Manager
		buildStateMap();

	}

	// Private function to build the system state array/hashmap
	private void buildStateMap() {
		// First of all, let's instanciate a hashmap
		systemStates = new HashMap<String, ArrayList<Integer>>(6);

		// Create a simple array containing the names of the keys of the map
		String[] stateNames = {"Hold", "Ready", "Run", "Suspend", "Blocked", "Done"};

		// Let's loop through the name array
		for (String name : stateNames) {
			// Let's create an arraylist for each key
			systemStates.put(name, new ArrayList<Integer>());
			System.out.println("HashMap key: " + name + " added. HashMap size: " + systemStates.size());
		}
	}
}
