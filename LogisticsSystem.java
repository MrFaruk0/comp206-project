import java.io.*;
import java.util.*;

public class LogisticsSystem {
    private SinglyLinkedList masterRegistry;
    private DoublyLinkedList intakeBuffer;
    private PackageQueue standardDelivery;
    private PackageStack truckLoading;
    private AVLTree addressDirectory;
    private Graph cityMap;
    private Scanner scanner;
    private Map<String, Set<String>> neighborhoodCustomers; // neighborhood -> set of customer IDs
    private Set<String> registeredCustomerIDs;
    private Set<String> registeredPackageIDs;  // Track which packages have been registered
    private int nextPackageNumber;
    private int lastCustomerNumber = 0; // Track highest CUST_XXX number

    /**
     * Constructs the logistics system and initializes all data structures.
     */
    public LogisticsSystem() {
        // Initialize all data structures
        masterRegistry = new SinglyLinkedList();
        intakeBuffer = new DoublyLinkedList();
        standardDelivery = new PackageQueue();
        truckLoading = new PackageStack();
        addressDirectory = new AVLTree();
        cityMap = new Graph();
        scanner = new Scanner(System.in);
        neighborhoodCustomers = new HashMap<>();
        registeredCustomerIDs = new HashSet<>();
        registeredPackageIDs = new HashSet<>();
        nextPackageNumber = 1;
    }

    /**
     * Initializes the system and starts the interactive menu.
     */
    public void start() {
        // Silent initialization
        if (!initializeSystem()) {
            System.out.println("ERROR: System initialization failed.");
            return;
        }

        System.out.println("\n===========================================");
        System.out.println("     System Initialized Successfully");
        System.out.println("===========================================\n");

        // Start interactive menu
        interactiveMenu();
    }

    /**
     * Loads the city map and builds the initial address directory.
     */
    private boolean initializeSystem() {
        try {
            MapLoader.loadMap("mapData.txt", cityMap);
            initializeAddressDirectory();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Initialize address directory with default neighborhoods
     */
    private void initializeAddressDirectory() {
        
        // Add Meydan (Warehouse)
        addressDirectory.insert("Meydan", "CUST_WAREHOUSE");
        registeredCustomerIDs.add("CUST_WAREHOUSE");
        neighborhoodCustomers.put("Meydan", new HashSet<>(Arrays.asList("CUST_WAREHOUSE")));
        
        // Add neighborhoods with customer IDs CUST_001 to CUST_007
        String[] neighborhoods = {"Talas", "Belsin", "Ildem", "Anbar", "Erkilet", "Mimsin", "Alpaslan"};
        for (int i = 0; i < neighborhoods.length; i++) {
            String customerID = String.format("CUST_%03d", i + 1);
            addressDirectory.insert(neighborhoods[i], customerID);
            registeredCustomerIDs.add(customerID);
            neighborhoodCustomers.put(neighborhoods[i], new HashSet<>(Arrays.asList(customerID)));
            lastCustomerNumber = i + 1;
        }
        
        // Load package data from file
        loadPackageDataFile();
    }

    /**
     * Interactive menu system for 15 operations
    */
    private void interactiveMenu() {
        boolean running = true;

        while (running) {
            displayMenu();
            System.out.print("Enter operation number (1-15): ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        operationReceiveDelivery();
                        break;
                    case 2:
                        operationDisplayWaitingDeliveries();
                        break;
                    case 3:
                        operationRegisterDeliveries();
                        break;
                    case 4:
                        operationDisplayLog();
                        break;
                    case 5:
                        operationProcessDeliveries();
                        break;
                    case 6:
                        operationDisplayProcessedDeliveries();
                        break;
                    case 7:
                        operationLoadTruck();
                        break;
                    case 8:
                        operationDisplayLoadedDeliveries();
                        break;
                    case 9:
                        operationDeliverPackages();
                        break;
                    case 10:
                        operationCreateAddress();
                        break;
                    case 11:
                        operationDisplayAddresses();
                        break;
                    case 12:
                        operationDisplayAllRoads();
                        break;
                    case 13:
                        operationDistanceBetweenNeighborhoods();
                        break;
                    case 14:
                        operationDisplayMostEfficientPaths();
                        break;
                    case 15:
                        operationExit();
                        running = false;
                        break;
                    default:
                        System.out.println("\nERROR: Invalid choice. Please enter a number between 1 and 15.\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nERROR: Please enter a valid number.\n");
                scanner.nextLine(); // consume invalid input
            }
        }
    }

    /**
     * Display the 15-operation menu
     */
    private void displayMenu() {
        System.out.println("\n===========================================");
        System.out.println("    KYS LOGISTICS - OPERATIONS MENU");
        System.out.println("===========================================");
        System.out.println("1.  Receiving a delivery");
        System.out.println("2.  Display deliveries waiting for processing");
        System.out.println("3.  Registration of deliveries");
        System.out.println("4.  Displaying the log of deliveries");
        System.out.println("5.  Processing the deliveries");
        System.out.println("6.  Display all processed deliveries");
        System.out.println("7.  Load the truck");
        System.out.println("8.  Displaying loaded deliveries");
        System.out.println("9.  Delivering packages to customers");
        System.out.println("10. Create a new address");
        System.out.println("11. Display all addresses");
        System.out.println("12. All roads (City Map)");
        System.out.println("13. Distance between neighborhoods");
        System.out.println("14. Display the most efficient paths");
        System.out.println("15. Exit");
        System.out.println("===========================================");
    }

    // ============ OPERATION IMPLEMENTATIONS ============

    /**
     * Operation 1: Receiving a delivery
     *
     * Time Complexity: O(c) (c stands for customers in one neighborhood)
     * Reason: HashMap/HashSet lookups are O(1); listing customers in a neighborhood scans c customers, and insertAtTail is O(1).
     */
    private void operationReceiveDelivery() {
        System.out.println();
        System.out.print("Enter destination neighborhood: ");
        String destination = scanner.nextLine().trim();

        // Check if destination exists in neighborhoods
        if (!neighborhoodCustomers.containsKey(destination)) {
            System.out.println("\nERROR: Destination '" + destination + "' does not exist.\n");
            pauseForUser();
            return;
        }

        // Ask if customer already exists
        System.out.print("Does customer already exist? (yes/no): ");
        String existsResponse = scanner.nextLine().trim().toLowerCase();
        
        String customerID;
        
        if (existsResponse.equals("yes")) {
            // Show customers in this neighborhood
            Set<String> destCustomers = neighborhoodCustomers.get(destination);
            System.out.println("\nCustomers in " + destination + ":");
            for (String cid : destCustomers) {
                System.out.println("  - " + cid);
            }
            
            // Ask for customer ID from this neighborhood
            System.out.print("Enter customer ID for " + destination + ": ");
            customerID = scanner.nextLine().trim();
            
            // Validate that customer ID exists in this specific neighborhood
            if (!destCustomers.contains(customerID)) {
                System.out.println("\nERROR: Customer ID '" + customerID + "' not found in " + destination + ".\n");
                pauseForUser();
                return;
            }
        } else if (existsResponse.equals("no")) {
            // Auto-generate customer ID
            customerID = getNextCustomerID();
            System.out.println("\nAuto-generated Customer ID: " + customerID);
            System.out.print("Do you accept this ID? (yes/no): ");
            String acceptResponse = scanner.nextLine().trim().toLowerCase();
            
            if (!acceptResponse.equals("yes")) {
                System.out.println("\nOperation cancelled.\n");
                pauseForUser();
                return;
            }
            
            // Accept and increment the counter
            acceptNextCustomerID();
            registeredCustomerIDs.add(customerID);
            Set<String> destCustomers = neighborhoodCustomers.getOrDefault(destination, new HashSet<>());
            destCustomers.add(customerID);
            neighborhoodCustomers.put(destination, destCustomers);
        } else {
            System.out.println("\nERROR: Please enter 'yes' or 'no'.\n");
            pauseForUser();
            return;
        }

        // Create package with auto-incremented ID
        String packageID = String.format("PKG_KYS_%03d", nextPackageNumber++);
        Package newPackage = new Package(packageID, destination);

        // Add to intake buffer
        intakeBuffer.insertAtTail(newPackage);

        System.out.println("\n[SUCCESS] Package received and added to intake buffer.");
        System.out.println("  Package ID: " + packageID);
        System.out.println("  Destination: " + destination);
        System.out.println("  Customer ID: " + customerID + "\n");
        pauseForUser();
    }

    /**
     * Operation 2: Display waiting deliveries
     *
     * Time Complexity: O(n) (n stands for packages in the intake buffer)
     * Reason: Traverses all n packages in the intake doubly linked list.
     */
    private void operationDisplayWaitingDeliveries() {
        System.out.println();
        intakeBuffer.displayBuffer();
        System.out.println();
        pauseForUser();
    }

    /**
     * Operation 3: Register deliveries
     *
     * Time Complexity: O(n) (n stands for packages in the intake buffer)
     * Reason: registerAllNonRegistered traverses the whole intake buffer for registration.
     */
    private void operationRegisterDeliveries() {
        System.out.println();

        if (intakeBuffer.isEmpty()) {
            System.out.println("No deliveries waiting in intake buffer.\n");
            pauseForUser();
            return;
        }

        System.out.print("Register all deliveries? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        int registered = 0;
        
        if (response.equals("yes")) {
            System.out.println("\n[INFO] Registering all deliveries...");
            // Get list of packages from buffer (need to implement a method to get all without removing)
            // For now, we'll iterate and mark as registered
            registered = registerAllNonRegistered();
        } else {
            System.out.print("Enter number of deliveries to register: ");
            try {
                int count = scanner.nextInt();
                scanner.nextLine();
                System.out.println("\n[INFO] Registering up to " + count + " non-registered deliveries...");
                registered = registerNonRegisteredCount(count);
            } catch (InputMismatchException e) {
                System.out.println("\nERROR: Invalid input.\n");
                scanner.nextLine();
                pauseForUser();
                return;
            }
        }
        
        System.out.println("\n[SUCCESS] " + registered + " deliveries registered in log.\n");
        pauseForUser();
    }
    
    /**
     * Register all non-registered deliveries in the buffer
     *
     * Time Complexity: O(n) (n stands for packages in the intake buffer)
     * Reason: getAllPackages visits every buffer node once.
     */
    private int registerAllNonRegistered() {
        int registered = 0;
        List<Package> allPackages = intakeBuffer.getAllPackages();
        
        for (Package pkg : allPackages) {
            String packageID = pkg.getPackageID();
            if (!registeredPackageIDs.contains(packageID)) {
                registeredPackageIDs.add(packageID);
                masterRegistry.addRecord(pkg);  // Add to master registry log
                registered++;
                System.out.println("  Registering: " + packageID);
            } else {
                System.out.println("  Skipping already registered: " + packageID);
            }
        }
        
        return registered;
    }
    
    /**
     * Register a specific number of non-registered deliveries from the buffer
     *
     * Time Complexity: O(n) (n stands for packages in the intake buffer)
     * Reason: Scans the buffer until count number of new registrations are done; worst case still walks all n packages.
     */
    private int registerNonRegisteredCount(int count) {
        int registered = 0;
        List<Package> allPackages = intakeBuffer.getAllPackages();
        
        // Iterate through all packages and register only non-registered ones
        for (Package pkg : allPackages) {
            // If we've registered enough, stop
            if (registered >= count) {
                break;
            }
            
            String packageID = pkg.getPackageID();
            // Check if this specific package is already registered
            if (registeredPackageIDs.contains(packageID)) {
                // Already registered, skip without counting toward limit
                System.out.println("  Skipping already registered: " + packageID);
                continue;
            }
            
            // Not registered, so register it and count it
            registeredPackageIDs.add(packageID);
            masterRegistry.addRecord(pkg);  // Add to master registry log
            registered++;
            System.out.println("  Registering: " + packageID);
        }
        
        return registered;
    }

    /**
     * Operation 4: Display log
     *
     * Time Complexity: O(N) (N stands for records in the master registry)
     * Reason: displayLog walks every node in the master registry singly linked list of N records.
     */
    private void operationDisplayLog() {
        System.out.println();
        masterRegistry.displayLog();
        System.out.println();
        pauseForUser();
    }

    /**
     * Operation 5: Process deliveries
     *
     * Time Complexity: O(k) (k stands for inputted count processed in that operation)
     * Reason: Removes k packages with O(1) removeFromHead and enqueue per package; optional addRecord is O(1) each.
     */
    private void operationProcessDeliveries() {
        System.out.println();

        if (intakeBuffer.isEmpty()) {
            System.out.println("No deliveries waiting in intake buffer.\n");
            pauseForUser();
            return;
        }

        System.out.print("Process all deliveries? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        int processed = 0;
        if (response.equals("yes")) {
            System.out.println("\n[INFO] Processing all deliveries...\n");
            while (!intakeBuffer.isEmpty()) {
                Package pkg = intakeBuffer.removeFromHead();
                if (pkg != null) {
                    standardDelivery.enqueue(pkg);
                    // Auto-register if not already registered
                    if (!registeredPackageIDs.contains(pkg.getPackageID())) {
                        registeredPackageIDs.add(pkg.getPackageID());
                        masterRegistry.addRecord(pkg);
                        System.out.println("  Auto-registering: " + pkg.getPackageID());
                    }
                    processed++;
                }
            }
        } else {
            System.out.print("Enter number of deliveries to process: ");
            try {
                int count = scanner.nextInt();
                scanner.nextLine();
                System.out.println();
                for (int i = 0; i < count && !intakeBuffer.isEmpty(); i++) {
                    Package pkg = intakeBuffer.removeFromHead();
                    if (pkg != null) {
                        standardDelivery.enqueue(pkg);
                        // Auto-register if not already registered
                        if (!registeredPackageIDs.contains(pkg.getPackageID())) {
                            registeredPackageIDs.add(pkg.getPackageID());
                            masterRegistry.addRecord(pkg);
                            System.out.println("  Auto-registering: " + pkg.getPackageID());
                        }
                        processed++;
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("\nERROR: Invalid input.\n");
                scanner.nextLine();
                return;
            }
        }

        System.out.println("[SUCCESS] " + processed + " deliveries processed.\n");
        pauseForUser();
    }

    /**
     * Operation 6: Display processed deliveries
     *
     * Time Complexity: O(q) (q stands for packages in the delivery queue)
     * Reason: displayQueue traverses all q packages in the FIFO delivery queue.
     */
    private void operationDisplayProcessedDeliveries() {
        System.out.println();
        standardDelivery.displayQueue();
        System.out.println();
        pauseForUser();
    }

    /**
     * Operation 7: Load truck
     *
     * Time Complexity: O(k) (k stands for inputted count loaded in that operation)
     * Reason: Dequeues and pushes k packages; both queue dequeue and stack push are O(1) per package.
     */
    private void operationLoadTruck() {
        System.out.println();

        if (standardDelivery.isEmpty()) {
            System.out.println("No processed deliveries in queue.\n");
            pauseForUser();
            return;
        }

        System.out.print("Load all deliveries? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        int loaded = 0;
        if (response.equals("yes")) {
            System.out.println("\n[INFO] Loading all deliveries to truck...\n");
            while (!standardDelivery.isEmpty()) {
                Package pkg = standardDelivery.dequeue();
                if (pkg != null) {
                    truckLoading.push(pkg);
                    loaded++;
                }
            }
        } else {
            System.out.print("Enter number of deliveries to load: ");
            try {
                int count = scanner.nextInt();
                scanner.nextLine();
                System.out.println();
                for (int i = 0; i < count && !standardDelivery.isEmpty(); i++) {
                    Package pkg = standardDelivery.dequeue();
                    if (pkg != null) {
                        truckLoading.push(pkg);
                        loaded++;
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("\nERROR: Invalid input.\n");
                scanner.nextLine();
                return;
            }
        }

        System.out.println("[SUCCESS] " + loaded + " deliveries loaded to truck.\n");
        pauseForUser();
    }

    /**
     * Operation 8: Display loaded deliveries
     *
     * Time Complexity: O(s) (s stands for packages in the truck stack)
     * Reason: displayStack visits all s packages currently on the truck stack.
     */
    private void operationDisplayLoadedDeliveries() {
        System.out.println();
        truckLoading.displayStack();
        System.out.println();
        pauseForUser();
    }

    /**
     * Operation 9: Deliver packages (LIFO order from truck stack).
     * Truck leaves Meydan, follows shortest paths per delivery, then returns to Meydan.
     *
     * Time Complexity: O(p * (E * log V)) (p stands for packages loaded on the truck, E for roads, V for neighborhoods)
     * Reason: Delivers p packages; each leg runs Dijkstra on the city graph (V vertices, E edges), and peek/pop are O(1).
     */
    private void operationDeliverPackages() {
        System.out.println();

        if (truckLoading.isEmpty()) {
            System.out.println("No deliveries loaded in truck.\n");
            pauseForUser();
            return;
        }

        System.out.println("\n[INFO] Truck departing from Meydan to deliver all loaded packages...\n");

        String currentLocation = "Meydan";
        int delivered = 0;
        int stopNumber = 1;
        int totalDistanceKm = 0;

        while (!truckLoading.isEmpty()) {
            Package pkg = truckLoading.peek();
            if (pkg == null) {
                break;
            }

            String destination = pkg.getDestination();

            System.out.println("--- Stop " + stopNumber + " ---");
            System.out.println("Package: " + pkg.getPackageID() + " -> " + destination);
            System.out.println("Truck at: " + currentLocation);

            System.out.println("\nRoute to delivery (" + currentLocation + " -> " + destination + "):");
            int legDistance = printShortestPathAndGetDistance(currentLocation, destination);

            if (legDistance < 0) {
                truckLoading.pop();
                System.out.println("[ERROR] Cannot reach " + destination + ". Package not delivered.\n");
            } else {
                truckLoading.pop();
                System.out.println("[DELIVERED] " + pkg);
                totalDistanceKm += legDistance;
                currentLocation = destination;
                delivered++;
            }

            System.out.println();
            stopNumber++;
        }

        if (!currentLocation.equals("Meydan")) {
            System.out.println("[INFO] All packages delivered. Truck returning to Meydan...\n");
            System.out.println("Return route (" + currentLocation + " -> Meydan):");
            int returnDistance = printShortestPathAndGetDistance(currentLocation, "Meydan");
            if (returnDistance >= 0) {
                totalDistanceKm += returnDistance;
                currentLocation = "Meydan";
            }
        } else if (delivered > 0) {
            System.out.println("[INFO] Truck is already at Meydan.\n");
        }

        System.out.println("\nTotal distance travelled: " + totalDistanceKm + " km");
        System.out.println("[SUCCESS] " + delivered + " package(s) delivered. Truck at: " + currentLocation + ".\n");
        pauseForUser();
    }

    /**
     * Operation 10: Create new address
     *
     * Time Complexity: O(log V + c) (V stands for neighborhoods in AVL tree, c for customers in the neighborhood)
     * Reason: AVL insert is O(log V) in tree size; HashSet updates for a neighborhood are O(1) average.
     */
    private void operationCreateAddress() {
        System.out.println();
        System.out.print("Enter neighborhood name: ");
        String neighborhood = scanner.nextLine().trim();

        // Check if neighborhood is empty
        if (neighborhood.isEmpty()) {
            System.out.println("\nERROR: Neighborhood name cannot be empty.\n");
            pauseForUser();
            return;
        }

        // Check if neighborhood matches existing neighborhoods
        if (!neighborhoodCustomers.containsKey(neighborhood)) {
            System.out.println("\nERROR: Neighborhood '" + neighborhood + "' does not match existing neighborhoods.\n");
            pauseForUser();
            return;
        }

        // Auto-generate customer ID
        String customerID = getNextCustomerID();
        System.out.println("\nGenerated Customer ID: " + customerID);
        System.out.print("Do you accept this ID? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        
        if (!response.equals("yes")) {
            System.out.println("\nOperation cancelled.\n");
            pauseForUser();
            return;
        }
        
        // Accept the customer ID - now increment the counter
        acceptNextCustomerID();

        // Check if customer ID already exists
        if (registeredCustomerIDs.contains(customerID)) {
            System.out.println("\nERROR: Customer ID already exists.\n");
            pauseForUser();
            return;
        }

        // Add to AVL tree and neighborhood tracking
        addressDirectory.insert(neighborhood, customerID);
        registeredCustomerIDs.add(customerID);
        
        Set<String> neighbors = neighborhoodCustomers.getOrDefault(neighborhood, new HashSet<>());
        neighbors.add(customerID);
        neighborhoodCustomers.put(neighborhood, neighbors);

        System.out.println("\n[SUCCESS] New address created.");
        System.out.println("  Neighborhood: " + neighborhood);
        System.out.println("  Customer ID: " + customerID + "\n");
        pauseForUser();
    }

    /**
     * Operation 11: Display all addresses
     *
     * Time Complexity: O(V + C) (V stands for neighborhoods, C for total customers across all neighborhoods)
     * Reason: Iterates every neighborhood V and every customer ID C across all HashSet entries.
     */
    private void operationDisplayAddresses() {
        System.out.println();
        System.out.println("=== ADDRESS DIRECTORY ===");
        
        if (neighborhoodCustomers.isEmpty()) {
            System.out.println("No addresses registered.\n");
        } else {
            for (Map.Entry<String, Set<String>> entry : neighborhoodCustomers.entrySet()) {
                String neighborhood = entry.getKey();
                Set<String> customerIDs = entry.getValue();
                System.out.println(neighborhood + ":");
                for (String customerID : customerIDs) {
                    System.out.println("  - " + customerID);
                }
            }
            System.out.println();
        }
        pauseForUser();
    }

    /**
     * Operation 12: Display all roads
     *
     * Time Complexity: O(V + E) (V stands for neighborhoods, E for roads in the city graph)
     * Reason: displayGraph prints each vertex and all adjacency-list edges in the city map.
     */
    private void operationDisplayAllRoads() {
        System.out.println();
        cityMap.displayGraph();
        System.out.println();
        pauseForUser();
    }

    /**
     * Operation 13: Distance between neighborhoods
     *
     * Time Complexity: O(E * log V) (E stands for roads, V for neighborhoods)
     * Reason: calculateShortestPath runs Dijkstra with a priority queue over the graph.
     */
    private void operationDistanceBetweenNeighborhoods() {
        System.out.println();
        System.out.print("Enter source neighborhood (default: Meydan): ");
        String source = scanner.nextLine().trim();
        if (source.isEmpty()) {
            source = "Meydan";
        }

        System.out.print("Enter destination neighborhood (default: Talas): ");
        String destination = scanner.nextLine().trim();
        if (destination.isEmpty()) {
            destination = "Talas";
        }

        System.out.println();
        cityMap.calculateShortestPath(source, destination);
        System.out.println();
        pauseForUser();
    }

    /**
     * Operation 14: Display most efficient paths
     *
     * Time Complexity: O(E * log V) (V stands for neighborhoods, E for roads in the city graph)
     * Reason: calculateMST uses Prim's algorithm with nested scans over visited nodes and their edges.
     */
    private void operationDisplayMostEfficientPaths() {
        System.out.println();
        cityMap.calculateMST();
        System.out.println();
        pauseForUser();
    }

    /**
     * Operation 15: Exit and save data
     *
     * Time Complexity: O(1) or O(N) if saving data (N stands for records in the master registry)
     * Reason: Closing the scanner is O(1); optional savePackageData traverses N registry records.
     */
    private void operationExit() {
        System.out.println();
        System.out.print("Save data to packageData.txt? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (response.equals("yes")) {
            savePackageData();
        }

        System.out.println("\n===========================================");
        System.out.println("Thank you for using KYS Logistics System!");
        System.out.println("===========================================\n");
        scanner.close();
    }

    /**
     * Save package data to packageData.txt
     *
     * Time Complexity: O(N) (N stands for the number of packages in the master registry)
     * Reason: saveToFile walks the entire master registry of N packages once.
     */
    private void savePackageData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("packageData.txt"))) {
            writer.println("# PackageID Destination");
            writer.println();

            // Save all packages from master registry (the source of truth)
            masterRegistry.saveToFile(writer);
            
            System.out.println("\n[SUCCESS] Data saved to packageData.txt\n");
        } catch (IOException e) {
            System.out.println("\n[ERROR] Failed to save packageData.txt: " + e.getMessage() + "\n");
        }
    }

    /**
     * Get the next customer ID by incrementing the last one
     * Note: This only returns the ID, doesn't increment. Increment happens after user accepts.
     *
     * Time Complexity: O(1)
     * Reason: Formats a single integer into a string with fixed-length work.
     */
    private String getNextCustomerID() {
        int nextNumber = lastCustomerNumber + 1;
        return String.format("CUST_%03d", nextNumber);
    }
    
    /**
     * Accept and register the next customer ID (increments the counter)
     *
     * Time Complexity: O(1)
     * Reason: Only increments an integer counter.
     */
    private void acceptNextCustomerID() {
        lastCustomerNumber++;
    }

    /**
     * Extract the numeric part from a customer ID (e.g., "CUST_007" returns 7)
     *
     * Time Complexity: O(1)
     * Reason: Customer IDs have fixed format; regex and parse work on bounded-length strings.
     */
    private int extractCustomerIDNumber(String customerID) {
        if (customerID.matches("CUST_\\d+")) {
            return Integer.parseInt(customerID.substring(5));
        }
        return 0;
    }

    /**
     * Load packages from packageData.txt and distribute them across processing levels
     *
     * Time Complexity: O(P) (P stands for lines in packageData.txt)
     * Reason: Reads P file lines and performs O(1) list/queue/stack operations per package when distributing.
     */
    private void loadPackageDataFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("packageData.txt"))) {
            String line;
            List<Package> allPackages = new ArrayList<>();
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                // Skip comments and empty lines
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                // Parse package data (format: PKG_KYS_XXX Destination)
                String[] parts = line.split("\\s+");
                if (parts.length >= 2) {
                    String packageID = parts[0];
                    String destination = parts[1];
                    
                    // Extract package number to ensure unique IDs
                    try {
                        String[] packageParts = packageID.split("_");
                        int packageNum = Integer.parseInt(packageParts[2]);
                        if (packageNum >= nextPackageNumber) {
                            nextPackageNumber = packageNum + 1;
                        }
                    } catch (Exception e) {
                        // Continue if parsing fails
                    }
                    
                    Package pkg = new Package(packageID, destination);
                    allPackages.add(pkg);
                }
            }
            
            if (!allPackages.isEmpty()) {
                // Mark all pre-loaded packages as registered (they're historical data)
                for (Package pkg : allPackages) {
                    registeredPackageIDs.add(pkg.getPackageID());
                }

                // All packages go to intake buffer first
                for (Package pkg : allPackages) {
                    intakeBuffer.insertAtTail(pkg);
                }

                // Move first 4 packages to queue (if they exist)
                for (int i = 0; i < Math.min(4, allPackages.size()); i++) {
                    Package pkg = intakeBuffer.removeFromHead();
                    if (pkg != null) {
                        standardDelivery.enqueue(pkg);
                    }
                }

                // Move first 3 from queue to truck stack (if they exist)
                for (int i = 0; i < Math.min(3, allPackages.size()); i++) {
                    Package pkg = standardDelivery.dequeue();
                    if (pkg != null) {
                        truckLoading.push(pkg);
                    }
                }
                
                // Add all to master registry
                for (Package pkg : allPackages) {
                    masterRegistry.addRecord(pkg);
                }
            }
            
        } catch (FileNotFoundException e) {
            // File not found is OK - system continues without pre-loaded packages
        } catch (IOException e) {
            // Ignore IO errors - system continues
        }
    }

    /**
     * Prints shortest path via Graph.calculateShortestPath and returns distance in km, or -1 if no path.
     *
     * Time Complexity: O(E * log V)
     * Reason: Delegates to Dijkstra in Graph; output parsing is linear in the small captured path string.
     */
    private int printShortestPathAndGetDistance(String start, String end) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream capture = new ByteArrayOutputStream();
        PrintStream captureOut = new PrintStream(capture);

        System.setOut(captureOut);
        try {
            cityMap.calculateShortestPath(start, end);
        } finally {
            System.setOut(originalOut);
        }

        String output = capture.toString();
        originalOut.print(output);

        if (output.contains("No path found between")) {
            return -1;
        }

        for (String line : output.split("\\R")) {
            line = line.trim();
            if (line.startsWith("Distance:") && line.endsWith(" km")) {
                String numberPart = line.substring("Distance:".length(), line.length() - 3).trim();
                return Integer.parseInt(numberPart);
            }
        }

        return -1;
    }

    /**
     * Pause for user to view output
     *
     * Time Complexity: O(1)
     * Reason: Waits for a single line of user input regardless of data size.
     */
    private void pauseForUser() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }
}

