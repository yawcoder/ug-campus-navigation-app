import java.util.Scanner;
import java.util.List;

/**
 * UG Navigate - Optimal Routing Solution for University of Ghana Campus
 * Main class to handle user input and navigation requests
 */
public class UGNavigate {
    private Graph campusGraph;
    private Scanner scanner;
    private static final double DEFAULT_WALKING_SPEED_KMH = 5.0; // Average walking speed
    
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
            
            // Ask user for preference
            System.out.println("\\nRoute Options:");
            System.out.println("1. Show fastest route only");
            System.out.println("2. Show top 3 routes sorted by time");
            System.out.print("Choose an option (1 or 2): ");
            
            String choice = scanner.nextLine().trim();
            if ("2".equals(choice)) {
                findAndDisplayMultipleRoutes(source, destination);
            } else {
                findAndDisplayShortestRoute(source, destination);
            }
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
     * Find and display the shortest route between two locations using Dijkstra's algorithm
     * @param source The starting location name
     * @param destination The destination location name
     */
    private void findAndDisplayShortestRoute(String source, String destination) {
        System.out.println("\\n🔍 Calculating shortest route using Dijkstra's algorithm...");
        
        List<String> path = campusGraph.findShortestPath(source, destination);
        
        if (!path.isEmpty()) {
            double totalDistance = campusGraph.getPathDistance(path);
            
            System.out.println("\\n✅ Shortest Route Found!");
            System.out.println("======================");
            
            // Display the route step by step
            System.out.println("📍 Route Details:");
            for (int i = 0; i < path.size(); i++) {
                String locationName = path.get(i);
                Location location = campusGraph.getLocation(locationName);
                
                if (i == 0) {
                    System.out.println("🚀 START: " + location);
                } else if (i == path.size() - 1) {
                    System.out.println("🎯 END:   " + location);
                } else {
                    System.out.println("📌 STEP " + i + ": " + location);
                }
                
                // Show distance to next location
                if (i < path.size() - 1) {
                    String nextLocation = path.get(i + 1);
                    double segmentDistance = campusGraph.getDistance(locationName, nextLocation);
                    System.out.printf("    ↓ %.2f units\\n", segmentDistance);
                }
            }
            
            System.out.println("\\n📊 Route Summary:");
            System.out.printf("   • Total Distance: %.2f units\\n", totalDistance);
            System.out.printf("   • Number of Stops: %d locations\\n", path.size());
            System.out.printf("   • Route Segments: %d\\n", path.size() - 1);
            
        } else {
            System.out.println("\\n❌ Route Calculation Failed!");
            System.out.println("Reason: No path found between the locations");
            System.out.println("Please verify that both locations are connected on the campus network.");
        }
    }
    
    /**
     * Find and display multiple route options sorted by estimated travel time
     * @param source The starting location name
     * @param destination The destination location name
     */
    private void findAndDisplayMultipleRoutes(String source, String destination) {
        System.out.println("\\n🔍 Finding multiple route options sorted by travel time...");
        System.out.printf("Walking speed: %.1f km/h (average human walking pace)\\n", DEFAULT_WALKING_SPEED_KMH);
        
        List<Graph.RouteOption> routes = campusGraph.findRouteOptionsSortedByTime(source, destination, DEFAULT_WALKING_SPEED_KMH);
        
        if (!routes.isEmpty()) {
            System.out.println("\\n🏃‍♂️ Top Route Options (Sorted by Fastest Time):");
            System.out.println("=====================================================");
            
            for (int i = 0; i < routes.size(); i++) {
                Graph.RouteOption route = routes.get(i);
                System.out.printf("\\n🥇 OPTION %d: %s\\n", i + 1, route.getDescription());
                System.out.println("─".repeat(50));
                
                // Display route path
                List<String> path = route.getPath();
                for (int j = 0; j < path.size(); j++) {
                    String locationName = path.get(j);
                    Location location = campusGraph.getLocation(locationName);
                    
                    if (j == 0) {
                        System.out.println("🚀 START: " + location);
                    } else if (j == path.size() - 1) {
                        System.out.println("🎯 END:   " + location);
                    } else {
                        System.out.println("📌 STEP " + j + ": " + location);
                    }
                    
                    // Show distance and time to next location
                    if (j < path.size() - 1) {
                        String nextLocation = path.get(j + 1);
                        double segmentDistance = campusGraph.getDistance(locationName, nextLocation);
                        double segmentTime = campusGraph.calculateTravelTime(List.of(locationName, nextLocation), DEFAULT_WALKING_SPEED_KMH);
                        System.out.printf("    ↓ %.0f units (%.1f min)\\n", segmentDistance, segmentTime);
                    }
                }
                
                // Display summary for this route
                System.out.println("\\n📊 Route Summary:");
                System.out.printf("   • Total Distance: %.2f units\\n", route.getDistance());
                System.out.printf("   • Estimated Time: %s\\n", route.getFormattedTime());
                System.out.printf("   • Number of Stops: %d locations\\n", path.size());
                System.out.printf("   • Route Segments: %d\\n", path.size() - 1);
                
                if (i == 0) {
                    System.out.println("   ⭐ FASTEST ROUTE!");
                }
            }
            
            // Display comparison summary
            System.out.println("\\n📈 Route Comparison Summary:");
            System.out.println("=============================");
            for (int i = 0; i < routes.size(); i++) {
                Graph.RouteOption route = routes.get(i);
                System.out.printf("Option %d: %s - %.2f units\\n", 
                    i + 1, route.getFormattedTime(), route.getDistance());
            }
            
        } else {
            System.out.println("\\n❌ No routes found!");
            System.out.println("Please verify that both locations are connected on the campus network.");
        }
    }
    
    /**
     * Main method to run the UG Navigate application
     */
    public static void main(String[] args) {
        UGNavigate navigator = new UGNavigate();
        navigator.getUserInput();
    }
}
