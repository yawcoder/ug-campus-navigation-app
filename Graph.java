import java.util.*;

/**
 * Graph class to hold all locations and paths for the UG campus navigation system
 */
public class Graph {
    private Map<String, Location> locations;
    private Map<String, List<String>> adjacencyList;
    
    /**
     * Constructor for Graph
     */
    public Graph() {
        this.locations = new HashMap<>();
        this.adjacencyList = new HashMap<>();
    }
    
    /**
     * Add a location to the graph
     * @param location The location to add
     */
    public void addLocation(Location location) {
        locations.put(location.getName(), location);
        adjacencyList.putIfAbsent(location.getName(), new ArrayList<>());
    }
    
    /**
     * Add a path between two locations
     * @param source The source location name
     * @param destination The destination location name
     */
    public void addPath(String source, String destination) {
        if (locations.containsKey(source) && locations.containsKey(destination)) {
            adjacencyList.get(source).add(destination);
            adjacencyList.get(destination).add(source); // Bidirectional path
        }
    }
    
    /**
     * Check if a location exists in the graph
     * @param locationName The name of the location to check
     * @return true if location exists, false otherwise
     */
    public boolean locationExists(String locationName) {
        return locations.containsKey(locationName);
    }
    
    /**
     * Get a location by name
     * @param locationName The name of the location
     * @return The Location object if found, null otherwise
     */
    public Location getLocation(String locationName) {
        return locations.get(locationName);
    }
    
    /**
     * Get all location names
     * @return Set of all location names
     */
    public Set<String> getAllLocationNames() {
        return locations.keySet();
    }
    
    /**
     * Get neighbors of a location
     * @param locationName The name of the location
     * @return List of neighboring location names
     */
    public List<String> getNeighbors(String locationName) {
        return adjacencyList.getOrDefault(locationName, new ArrayList<>());
    }
    
    /**
     * Get the total number of locations
     * @return Number of locations in the graph
     */
    public int getLocationCount() {
        return locations.size();
    }
    
    /**
     * Display all locations in the graph
     */
    public void displayAllLocations() {
        System.out.println("Available locations on UG Campus:");
        for (Location location : locations.values()) {
            System.out.println("- " + location);
        }
    }
}
