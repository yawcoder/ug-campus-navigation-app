import java.util.Scanner;

/**
 * UG Navigate - Optimal Routing Solution for University of Ghana Campus
 * Main class to handle user input and navigation requests
 */
public class UGNavigate {
    private Graph campusGraph;
    private Scanner scanner;
    
    /**
     * Constructor for UGNavigate
     */
    public UGNavigate() {
        this.campusGraph = new Graph();
        this.scanner = new Scanner(System.in);
        initializeCampusLocations();
    }
    
    /**
     * Initialize the campus with sample locations
     */
    private void initializeCampusLocations() {
        // Add sample UG campus locations
        campusGraph.addLocation(new Location("Main Gate", "Gate", 0.0, 0.0));
        campusGraph.addLocation(new Location("Balme Library", "Library", 100.0, 50.0));
        campusGraph.addLocation(new Location("Great Hall", "Building", 150.0, 100.0));
        campusGraph.addLocation(new Location("Commonwealth Hall", "Residence", 200.0, 75.0));
        campusGraph.addLocation(new Location("Volta Hall", "Residence", 120.0, 120.0));
        campusGraph.addLocation(new Location("School of Medicine", "Academic", 180.0, 30.0));
        campusGraph.addLocation(new Location("Engineering Building", "Academic", 90.0, 80.0));
        campusGraph.addLocation(new Location("Administration Block", "Office", 110.0, 60.0));
        
        // Add sample paths between locations
        campusGraph.addPath("Main Gate", "Balme Library");
        campusGraph.addPath("Balme Library", "Great Hall");
        campusGraph.addPath("Balme Library", "Administration Block");
        campusGraph.addPath("Great Hall", "Commonwealth Hall");
        campusGraph.addPath("Great Hall", "Volta Hall");
        campusGraph.addPath("Administration Block", "Engineering Building");
        campusGraph.addPath("School of Medicine", "Commonwealth Hall");
        campusGraph.addPath("Engineering Building", "Volta Hall");
    }
    
    /**
     * Get user input for source and destination locations
     */
    public void getUserInput() {
        System.out.println("🧭 Welcome to UG Navigate - Optimal Routing Solution");
        System.out.println("===================================================");
        
        // Display available locations
        campusGraph.displayAllLocations();
        System.out.println();
        
        // Get source location
        String source = getValidLocation("Enter your current location: ");
        
        // Get destination location
        String destination = getValidLocation("Enter your destination: ");
        
        // Confirm the input
        System.out.println("\\nRoute Request:");
        System.out.println("From: " + campusGraph.getLocation(source));
        System.out.println("To: " + campusGraph.getLocation(destination));
        
        if (source.equals(destination)) {
            System.out.println("\\n⚠️ You are already at your destination!");
        } else {
            System.out.println("\\n✅ Route request validated successfully!");
            System.out.println("Ready to find the best route...");
        }
    }
    
    /**
     * Get a valid location from user input
     * @param prompt The prompt to display to the user
     * @return Valid location name
     */
    private String getValidLocation(String prompt) {
        String location;
        do {
            System.out.print(prompt);
            location = scanner.nextLine().trim();
            
            if (!campusGraph.locationExists(location)) {
                System.out.println("❌ Location '" + location + "' not found on campus.");
                System.out.println("Please choose from the available locations listed above.");
                System.out.println();
            }
        } while (!campusGraph.locationExists(location));
        
        return location;
    }
    
    /**
     * Main method to run the UG Navigate application
     */
    public static void main(String[] args) {
        UGNavigate navigator = new UGNavigate();
        navigator.getUserInput();
    }
}
