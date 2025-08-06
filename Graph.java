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
    
    /**
     * Calculate the distance between two locations
     * @param source The source location name
     * @param destination The destination location name
     * @return The distance between the locations, or Double.MAX_VALUE if not connected
     */
    public double getDistance(String source, String destination) {
        Location sourceLocation = locations.get(source);
        Location destLocation = locations.get(destination);
        
        if (sourceLocation == null || destLocation == null) {
            return Double.MAX_VALUE;
        }
        
        return sourceLocation.distanceTo(destLocation);
    }
    
    /**
     * Find the shortest path between two locations using Dijkstra's algorithm
     * @param source The starting location name
     * @param destination The destination location name
     * @return List of location names representing the shortest path (empty if no path found)
     */
    public List<String> findShortestPath(String source, String destination) {
        // Validate input locations
        if (!locationExists(source) || !locationExists(destination)) {
            return new ArrayList<>();
        }
        
        if (source.equals(destination)) {
            List<String> path = new ArrayList<>();
            path.add(source);
            return path;
        }
        
        // Initialize distances and previous nodes
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        Set<String> unvisited = new HashSet<>(locations.keySet());
        
        // Initialize all distances to infinity except source
        for (String locationName : locations.keySet()) {
            distances.put(locationName, Double.MAX_VALUE);
        }
        distances.put(source, 0.0);
        
        // Dijkstra's algorithm
        while (!unvisited.isEmpty()) {
            // Find unvisited node with minimum distance
            String current = null;
            double minDistance = Double.MAX_VALUE;
            for (String node : unvisited) {
                if (distances.get(node) < minDistance) {
                    minDistance = distances.get(node);
                    current = node;
                }
            }
            
            // If no reachable unvisited nodes, break
            if (current == null || minDistance == Double.MAX_VALUE) {
                break;
            }
            
            // Remove current from unvisited
            unvisited.remove(current);
            
            // Found destination
            if (current.equals(destination)) {
                break;
            }
            
            // Check all neighbors
            for (String neighbor : getNeighbors(current)) {
                if (unvisited.contains(neighbor)) {
                    double edgeWeight = getDistance(current, neighbor);
                    double newDistance = distances.get(current) + edgeWeight;
                    
                    if (newDistance < distances.get(neighbor)) {
                        distances.put(neighbor, newDistance);
                        previous.put(neighbor, current);
                    }
                }
            }
        }
        
        // Reconstruct path
        return reconstructPath(previous, source, destination);
    }
    
    /**
     * Get the total distance of a path
     * @param path List of location names representing the path
     * @return Total distance of the path
     */
    public double getPathDistance(List<String> path) {
        if (path.size() < 2) {
            return 0.0;
        }
        
        double totalDistance = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            totalDistance += getDistance(path.get(i), path.get(i + 1));
        }
        return totalDistance;
    }
    
    /**
     * Reconstruct the shortest path from the previous nodes map
     * @param previous Map of previous nodes
     * @param source Starting location
     * @param destination Ending location
     * @return List of location names representing the path
     */
    private List<String> reconstructPath(Map<String, String> previous, String source, String destination) {
        List<String> path = new ArrayList<>();
        String current = destination;
        
        // Build path backwards from destination to source
        while (current != null) {
            path.add(0, current); // Add to front
            current = previous.get(current);
        }
        
        // Check if path is valid (should start with source)
        if (path.isEmpty() || !path.get(0).equals(source)) {
            return new ArrayList<>(); // No valid path found
        }
        
        return path;
    }
    
    /**
     * Calculate estimated travel time for a path based on walking speed
     * @param path List of location names representing the path
     * @param walkingSpeedKmh Walking speed in km/h (default: 5 km/h)
     * @return Estimated travel time in minutes
     */
    public double calculateTravelTime(List<String> path, double walkingSpeedKmh) {
        double distanceKm = getPathDistance(path) / 1000.0; // Convert to km (assuming coordinates are in meters)
        double timeHours = distanceKm / walkingSpeedKmh;
        return timeHours * 60.0; // Convert to minutes
    }
    
    /**
     * Find multiple route options between two locations and sort by travel time
     * @param source The starting location name
     * @param destination The destination location name
     * @param walkingSpeedKmh Walking speed in km/h
     * @return List of RouteOption objects sorted by travel time (fastest first)
     */
    public List<RouteOption> findRouteOptionsSortedByTime(String source, String destination, double walkingSpeedKmh) {
        List<RouteOption> routeOptions = new ArrayList<>();
        
        // Get the shortest path (primary route)
        List<String> shortestPath = findShortestPath(source, destination);
        if (!shortestPath.isEmpty()) {
            double distance = getPathDistance(shortestPath);
            double time = calculateTravelTime(shortestPath, walkingSpeedKmh);
            routeOptions.add(new RouteOption(shortestPath, distance, time, "Shortest Distance Route"));
        }
        
        // Find alternative routes by exploring different intermediate points
        Set<String> allLocations = getAllLocationNames();
        for (String intermediate : allLocations) {
            if (!intermediate.equals(source) && !intermediate.equals(destination)) {
                // Try route through this intermediate location
                List<String> route1 = findShortestPath(source, intermediate);
                List<String> route2 = findShortestPath(intermediate, destination);
                
                if (!route1.isEmpty() && !route2.isEmpty()) {
                    // Combine the routes (remove duplicate intermediate location)
                    List<String> combinedPath = new ArrayList<>(route1);
                    combinedPath.addAll(route2.subList(1, route2.size()));
                    
                    double distance = getPathDistance(combinedPath);
                    double time = calculateTravelTime(combinedPath, walkingSpeedKmh);
                    
                    // Only add if it's a different route and not too much longer
                    if (!isDuplicateRoute(routeOptions, combinedPath) && 
                        time <= routeOptions.get(0).getTravelTime() * 1.5) { // Within 50% of shortest time
                        routeOptions.add(new RouteOption(combinedPath, distance, time, 
                            "Alternative Route via " + intermediate));
                    }
                }
            }
        }
        
        // Sort by travel time (fastest first)
        routeOptions.sort((r1, r2) -> Double.compare(r1.getTravelTime(), r2.getTravelTime()));
        
        // Return top 3 routes
        return routeOptions.subList(0, Math.min(3, routeOptions.size()));
    }
    
    /**
     * Check if a route is already in the list (to avoid duplicates)
     * @param existingRoutes List of existing route options
     * @param newPath New path to check
     * @return true if the route is a duplicate
     */
    private boolean isDuplicateRoute(List<RouteOption> existingRoutes, List<String> newPath) {
        for (RouteOption route : existingRoutes) {
            if (route.getPath().equals(newPath)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Inner class to represent a route option with distance, time, and description
     */
    public static class RouteOption {
        private final List<String> path;
        private final double distance;
        private final double travelTime;
        private final String description;
        
        public RouteOption(List<String> path, double distance, double travelTime, String description) {
            this.path = new ArrayList<>(path);
            this.distance = distance;
            this.travelTime = travelTime;
            this.description = description;
        }
        
        public List<String> getPath() {
            return new ArrayList<>(path);
        }
        
        public double getDistance() {
            return distance;
        }
        
        public double getTravelTime() {
            return travelTime;
        }
        
        public String getDescription() {
            return description;
        }
        
        public String getFormattedTime() {
            int minutes = (int) Math.round(travelTime);
            if (minutes < 60) {
                return minutes + " min";
            } else {
                int hours = minutes / 60;
                int remainingMinutes = minutes % 60;
                return String.format("%d hr %d min", hours, remainingMinutes);
            }
        }
    }
}
