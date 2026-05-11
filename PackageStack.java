// PackageStack.java
// Truck Loading Stack for KYS Logistics Dispatch System
// Uses LIFO (Last-In, First-Out) logic: the cargo space in a delivery van
// is narrow, so the last package loaded is the first one the driver grabs
// at the first stop.
//
// Internal implementation: Singly linked list whose head acts as the top
// of the stack, giving O(1) push and pop without the size limitations of
// an array-based stack.

public class PackageStack {

    // Inner node class – visible only to PackageStack
    private class Node {
        Package data;
        Node next;

        Node(Package data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node top;  // most recently loaded package
    private int size;

    public PackageStack() {
        this.top  = null;
        this.size = 0;
    }

    // Loads a package on top of the stack (into the van).
    // Time Complexity: O(1)
    public void push(Package pkg) {
        Node newNode = new Node(pkg);
        newNode.next = top;
        top          = newNode;
        size++;
        System.out.println("[Stack] Pushed (loaded into van): " + pkg);
    }

    // Unloads and returns the top package from the stack.
    // Time Complexity: O(1)
    public Package pop() {
        if (top == null) {
            System.out.println("[Stack] Stack is empty. No package to unload.");
            return null;
        }

        Package removedPackage = top.data;
        top = top.next;
        size--;
        System.out.println("[Stack] Popped (unloaded from van): " + removedPackage);
        return removedPackage;
    }

    // Peeks at the top package without removing it.
    // Time Complexity: O(1)
    public Package peek() {
        if (top == null) {
            System.out.println("[Stack] Stack is empty.");
            return null;
        }
        return top.data;
    }

    // Returns true when the stack contains no packages.
    // Time Complexity: O(1)
    public boolean isEmpty() {
        return top == null;
    }

    // Displays all packages currently loaded in the van,
    // from top (first to be unloaded) to bottom (last to be unloaded).
    // Time Complexity: O(n)
    public void displayStack() {
        if (top == null) {
            System.out.println("=== TRUCK LOADING STACK ===");
            System.out.println("  Stack is empty (van is empty).");
            return;
        }

        System.out.println("=== TRUCK LOADING STACK (top -> bottom) ===");
        Node current = top;
        int position = 1;

        while (current != null) {
            System.out.println("  " + position + ". " + current.data
                    + (position == 1 ? "  <- unloaded first (next stop)" : ""));
            current = current.next;
            position++;
        }

        System.out.println("  Total packages in van: " + size);
    }

    // Returns the number of packages currently in the stack.
    // Time Complexity: O(1)
    public int size() {
        return size;
    }
}
