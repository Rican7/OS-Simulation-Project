// Imports (libraries and utilities)
import java.util.ArrayList;
import java.util.Collections;

// External imports
import com.google.common.collect.*;

// Memory Manager class
public class MemoryManager {
	// Declare properties
	private ArrayList<Process> systemMemory;

	// Constructor
	public MemoryManager() {
		// Let's initialize the Memory Manager
		this.buildMemoryList();

	}

	// Private function to build the system memory array list
	private void buildMemoryList() {
		// First of all, let's instanciate the memory manager
		this.systemMemory = Lists.newArrayList();
	}

	// Private function to get the amount of memory currently in use
	private int getMemoryUsed() {
		// Let's keep track of the used memory
		int usedMemory = 0;

		// Let's loop through each process in memory
		for (Process proc : this.systemMemory) {
			usedMemory += proc.getSize();
		}

		return usedMemory;
	}

	// Private function to get the amount of memory available/unused
	public int getMemoryAvailable() {
		return Simulation.MAX_MEMORY - this.getMemoryUsed();
	}

	// Public function to add processes to the Memory manager
	public boolean addProcess(Process process) {
		// Do we have enough free memory available for this process?
		if (process.getSize() <= this.getMemoryAvailable()) {
			// Let's add the process to memory
			if (this.systemMemory.add(process)) {
				// If we got here, the process has successfully been added to memory
				return true;
			}
		}

		// Let's return true for now. Nothing can possibly fail.
		return false;
	}

	// Private function to get the single largest process in memory
	private Process getLargestProcess() {
		return Collections.max(systemMemory);
	}
}
