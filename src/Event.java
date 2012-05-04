// Event class
class Event {
	// Declare properties
	public final String from;
	public final String to;

	// Constructor
	public Event(final String from, final String to) {
		// Set the properties
		this.from = from;
		this.to = to;
	}

	// Public function to convert the event into a string
	public String toString() {
		// Create a string from the object's properties
		String string = this.from + " -> " + this.to;

		return string;
	}
}
