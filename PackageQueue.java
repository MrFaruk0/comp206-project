// PackageQueue.java
// Standard Delivery Queue for KYS Logistics Dispatch System
// Uses FIFO (First-In, First-Out) logic: the first package to arrive
// at the sorting station is the first one moved to the loading dock.
//
// Internal implementation: Singly linked list with head (front) and tail (rear)
// pointers so that both enqueue and dequeue run in O(1).

public class PackageQueue {

    // Inner node class – visible only to PackageQueue
    private class Node {
        Package data;
        Node next;

        Node(Package data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node front; // dequeue end
    private Node rear;  // enqueue end
    private int size;

    public PackageQueue() {
        this.front = null;
        this.rear  = null;
        this.size  = 0;
    }

    // Adds a package to the back of the delivery queue.
    // Time Complexity: O(1) – rear pointer is used, no traversal needed.
    public void enqueue(Package pkg) {
        Node newNode = new Node(pkg);

        if (rear == null) {          // queue is empty
            front = newNode;
            rear  = newNode;
        } else {
            rear.next = newNode;
            rear      = newNode;
        }

        size++;
        System.out.println("[Queue] Enqueued: " + pkg);
    }

    // Removes and returns the package at the front of the queue (FIFO).
    // Time Complexity: O(1)
    public Package dequeue() {
        if (front == null) {
            System.out.println("[Queue] Queue is empty. Nothing to dequeue.");
            return null;
        }

        Package removedPackage = front.data;
        front = front.next;

        if (front == null) {         // queue became empty
            rear = null;
        }

        size--;
        System.out.println("[Queue] Dequeued: " + removedPackage);
        return removedPackage;
    }

    // Returns true when the queue contains no packages.
    // Time Complexity: O(1)
    public boolean isEmpty() {
        return front == null;
    }

    // Displays every package currently waiting in the delivery queue,
    // from front (next to be dispatched) to rear (last to arrive).
    // Time Complexity: O(n)
    public void displayQueue() {
        if (front == null) {
            System.out.println("=== STANDARD DELIVERY QUEUE ===");
            System.out.println("  Queue is empty.");
            return;
        }

        System.out.println("=== STANDARD DELIVERY QUEUE (front -> rear) ===");
        Node current = front;
        int position = 1;

        while (current != null) {
            System.out.println("  " + position + ". " + current.data);
            current = current.next;
            position++;
        }

        System.out.println("  Total packages in queue: " + size);
    }

    // Returns the number of packages currently in the queue.
    // Time Complexity: O(1)
    public int size() {
        return size;
    }
}
