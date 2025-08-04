/**
 * Main executable class for UG Navigate - Optimal Routing Solution
 * Entry point for the University of Ghana Campus Navigation System
 */
public class Main {
    
    /**
     * Main method - entry point of the application
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        System.out.println("🧭 Starting UG Navigate - Optimal Routing Solution");
        System.out.println("===================================================");
        
        // Create and run the navigation system
        UGNavigate navigator = new UGNavigate();
        navigator.getUserInput();
        
        System.out.println("\n🎯 Thank you for using UG Navigate!");
        System.out.println("Safe travels on campus!");
    }
}
