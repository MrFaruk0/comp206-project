// TestDispatch.java
// Local test for Kisi 3 (Dispatch / Queue + Stack)
// Tests PackageQueue (FIFO - Standard Delivery) and
//       PackageStack (LIFO - Truck Loading)
// This file is for development testing only; not required in the final submission.

public class TestDispatch {
    public static void main(String[] args) {

        System.out.println("==========================================");
        System.out.println("      KYS Logistics - Dispatch Test");
        System.out.println("==========================================\n");

        // ----- Sample packages -----
        Package p1 = new Package("PKG_KYS_001", "Talas");
        Package p2 = new Package("PKG_KYS_002", "Belsin");
        Package p3 = new Package("PKG_KYS_003", "Ildem");
        Package p4 = new Package("PKG_KYS_004", "Anbar");
        Package p5 = new Package("PKG_KYS_005", "Erkilet");

        // ========================================================
        // TEST 1 – Standard Delivery Queue (FIFO)
        // ========================================================
        System.out.println("--- TEST 1: Standard Delivery Queue (FIFO) ---\n");

        PackageQueue standardDelivery = new PackageQueue();

        System.out.println("Enqueuing 4 packages to the standard delivery queue...");
        standardDelivery.enqueue(p1);
        standardDelivery.enqueue(p2);
        standardDelivery.enqueue(p3);
        standardDelivery.enqueue(p4);

        System.out.println();
        standardDelivery.displayQueue();

        System.out.println("\nDequeuing 2 packages (FIFO order expected)...");
        standardDelivery.dequeue(); // should return PKG_KYS_001
        standardDelivery.dequeue(); // should return PKG_KYS_002

        System.out.println();
        standardDelivery.displayQueue();

        System.out.println("\nDequeuing from empty queue test...");
        standardDelivery.dequeue();
        standardDelivery.dequeue();
        standardDelivery.dequeue(); // should print "Queue is empty"

        System.out.println();

        // ========================================================
        // TEST 2 – Truck Loading Stack (LIFO)
        // ========================================================
        System.out.println("--- TEST 2: Truck Loading Stack (LIFO) ---\n");

        PackageStack truckLoading = new PackageStack();

        System.out.println("Pushing 5 packages into the van (LIFO)...");
        truckLoading.push(p1);
        truckLoading.push(p2);
        truckLoading.push(p3);
        truckLoading.push(p4);
        truckLoading.push(p5);

        System.out.println();
        truckLoading.displayStack();

        System.out.println("\nPeeking at top package (no removal)...");
        Package peeked = truckLoading.peek();
        if (peeked != null) {
            System.out.println("  Top of stack: " + peeked);
        }

        System.out.println("\nPopping 3 packages (LIFO order expected)...");
        truckLoading.pop(); // should return PKG_KYS_005
        truckLoading.pop(); // should return PKG_KYS_004
        truckLoading.pop(); // should return PKG_KYS_003

        System.out.println();
        truckLoading.displayStack();

        System.out.println("\nPopping remaining packages then testing empty stack...");
        truckLoading.pop();
        truckLoading.pop();
        truckLoading.pop(); // should print "Stack is empty"

        System.out.println();
        truckLoading.displayStack();

        System.out.println("\n==========================================");
        System.out.println("         Dispatch Test Complete");
        System.out.println("==========================================");
    }
}
