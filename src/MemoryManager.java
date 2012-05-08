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

		// Now, let's create a dead process signifying the amount of empty space in the memory
		Process deadProcess = new Process(Simulation.MAX_MEMORY, true);

		// Ok, let's add the dead process to memory as our initially completely empty single block
		this.systemMemory.add(deadProcess);
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

	// Private function to get the address of a dead/empty process block that the given process may fit into 
	private int getFittingBlockAddress(Process process) {
		// Let's loop through our entire memory set
		for (Process processBlock : this.systemMemory) {
			// If we find an empty process block (a block with a "dead" process)
			// AND the block is large enough for the size of the passed process
			if (processBlock.isProcessDead() == true && processBlock.getSize() >= process.getSize()) {
				// Let's return the index of the empty/dead process block that allows the given process to fit into
				return this.systemMemory.indexOf(processBlock);
			}
		}

		// Ok, for a "hey we didn't find one" scenario, let's return a -1
		return -1;
	}

	// Public function to detect if adding the process to memory is possible
	public boolean isAddPossible(Process process) {
		// If the address of the next fitting block is -1, there is no fitting block
		if (this.getFittingBlockAddress(process) > -1) {
			// If we got here, there must be ample room in an empty block for the given process to fit
			return true;
		}

		return false;
	}

	// Public function to add processes to the Memory manager
	public boolean addProcess(Process process) {
		// Let's get the address of a block that the given process can fit into
		int destinationAddress = this.getFittingBlockAddress(process);

		// If the address of the next fitting block is greater than -1, we've found a fitting block
		if (destinationAddress > -1) {
			try {
				// Ok, so we have a destination address for the process to go
				// So, let's add the given process to memory at the destination address
				this.systemMemory.add(destinationAddress, process);

				// Now that its added, let's either redude the size of the empty block or remove it
				// Let's first get the process from the now shifted (by 1) address of the original empty block
				Process oldEmptyBlock = this.systemMemory.get(destinationAddress + 1);

				// If the newly address process is the same size as the old empty block
				if (process.getSize() == oldEmptyBlock.getSize()) {
					// Let's just remove the old empty block
					this.systemMemory.remove(oldEmptyBlock);
				}
				else {
					// Let's create our new size
					int newSize = oldEmptyBlock.getSize() - process.getSize();

					// Let's resize the empty process block
					oldEmptyBlock.resize(newSize);
				}

				// If it worked, return true
				return true;
			}
			catch (Exception exception) {
				// Only show if debugMode is on
				if (Simulation.debugMode) {
					System.out.println("Process failed to add with exception: " + exception);
				}
			}
		}

		// It must not have worked
		return false;
	}

	// Private function to merge two empty memory blocks into one empty memory block with the size of the sum of the two empty blocks
	private boolean mergeEmptyBlocks(Process emptyBlockOne, Process emptyBlockTwo) {
		// DON'T EVEN ATTEMPT to do this unless both of the processes are empty/dead processes
		if (emptyBlockOne.isProcessDead() && emptyBlockTwo.isProcessDead()) {
			// Let's figure out the new block size
			int newSize = emptyBlockOne.getSize() + emptyBlockTwo.getSize();

			try {
				// Let's just remove the second empty block
				this.systemMemory.remove(emptyBlockTwo);

				// Let's resize the empty process block
				emptyBlockOne.resize(newSize);

				return true;
			}
			catch (Exception exception) {
				// Only show if debugMode is on
				if (Simulation.debugMode) {
					System.out.println("Problem merging empty blocks with exception: " + exception);
				}
			}
		}

		return false;
	}

	// Private function to clean our memory's blocks by checking for adjacent free memory blocks and removing them
	private void cleanUpMemory() {
		// Let's keep track of the process being compared to
		Process comparedProcess = null; // Start as null... we don't have a process to compare to yet
		int comparedAddress = -1; // Start with an impossible address

		// Now, to clean up our memory blocks, let's merge adjacent "free" memory (dead process) blocks
		// Let's loop through our entire memory set
		for (Process processBlock : this.systemMemory) {
			// If we find an empty process block (a block with a "dead" process)
			if (processBlock.isProcessDead()) {
				// Ok, we found a dead process (empty memory block)

				// Let's get the index of this process
				int deadAddress = this.systemMemory.indexOf(processBlock);
				
				// While we're at it, let's get the address difference of the two processes
				int addressDifference = (deadAddress - comparedAddress);

				// If the comparedProcess is still null 
				if (comparedProcess == null) {
					// Let's make the newly found dead process the comparedProcess
					comparedProcess = processBlock;

					// And let's also set the comparedAddress to this processes address
					comparedAddress = deadAddress;
				}
				// Otherwise, we may have just found two adjacent dead blocks
				// Let's check by seeing if the addresses are only 1 address different
				else if (addressDifference == 1) {
					// If we got here, there must be two adjacent dead blocks. So... let's "merge" them
					this.mergeEmptyBlocks(comparedProcess, processBlock);

					// There may be more adjacent blocks.. let's call it again
					this.cleanUpMemory();
				}
			}
		}
	}

	// Public function to remove a process from memory
	public boolean removeProcess(Process process) {
		// Let's get the index of the process in the array list
		int index = this.systemMemory.indexOf(process);

		// Ok, so instead of destroying our memory configuration by simply removing the process from memory...
		// Let's create a dead process to replace our current process
		Process deadProcess = new Process(process.getSize(), true);

		try {
			// Let's replace the given process's block with an empty/dead process block of the same size
			this.systemMemory.add(index, deadProcess);

			// Now, let's clean up the adjacent memory blocks
			this.cleanUpMemory();

			// Only show if debugMode is on
			if (Simulation.debugMode) {
				System.out.println("Process " + process.toString() + " removed from memory");
			}

			// Cool, it worked
			return true;
		}
		catch (IndexOutOfBoundsException exception) {
			// Only show if debugMode is on
			if (Simulation.debugMode) {
				System.out.println("Dead process failed to be replaced with exception: " + exception);
			}
		}

		return false;
	}

}
