/********************************/
// Operating Systems Simulation //
//   Created by Trevor Suarez   //
/********************************/

// Imports (libraries and utilities)
import java.util.Random;

// Simulation main engine class
public class Simulation {
	// Program wide objects
	public static Random random;

	// Constructor
	public Simulation() {
		// To test the random process generation, let's create a bunch of processes and print them to the screen (log)
		while (Process.num_of_processes < 100) {
			Process job = new Process();

			System.out.println("Process created with ID: " + job.get_id() + ", Size: " + job.get_size() + "k, and Time: " + job.get_time());
		}
	}

	// Main function
	public static void main(String[] args) {
		// Instanciate program wide objects
		random = new Random();

		// Begin the simulation
		Simulation simulation = new Simulation();
	}

} // End Simulation class
