/**
 * Location class representing a place on the University of Ghana campus
 */
public class Location {
    private String name;
    private String type;
    private double x; // x coordinate
    private double y; // y coordinate
    
    /**
     * Constructor for Location
     * @param name The name of the location
     * @param type The type of location (e.g., "Building", "Gate", "Library", etc.)
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public Location(String name, String type, double x, double y) {
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getType() {
        return type;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s) at coordinates (%.2f, %.2f)", name, type, x, y);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Location location = (Location) obj;
        return name.equals(location.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    /**
     * Calculate the Euclidean distance between this location and another location.
     * Uses the standard distance formula: √((x2-x1)² + (y2-y1)²)
     * 
     * @param other the other location to calculate distance to
     * @return the distance between the two locations
     */
    public double distanceTo(Location other) {
        double deltaX = this.x - other.x;
        double deltaY = this.y - other.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}
