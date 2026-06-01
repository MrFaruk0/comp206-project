public class SinglyLinkedList {

    private class Node {
        Package data;
        Node next;

        Node(Package data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;

    public SinglyLinkedList() {
        this.head = null;
        this.tail = null;
    }

    // Adds a package record to the end of the master registry.
    // Time Complexity: O(1), because tail pointer is used.
    public void addRecord(Package pkg) {
        Node newNode = new Node(pkg);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
    }

    // Displays all package records in the daily log.
    // Time Complexity: O(n)
    public void displayLog() {
        if (head == null) {
            System.out.println("Master Registry is empty.");
            return;
        }

        System.out.println("=== MASTER REGISTRY DAILY LOG ===");

        Node current = head;
        int count = 1;

        while (current != null) {
            System.out.println(count + ". " + current.data);
            current = current.next;
            count++;
        }
    }

    public boolean isEmpty() {
        return head == null;
    }

    // Saves all package records to a file using PrintWriter
    // Time Complexity: O(n)
    public void saveToFile(java.io.PrintWriter writer) {
        Node current = head;
        while (current != null) {
            writer.println(current.data.getPackageID() + " " + current.data.getDestination());
            current = current.next;
        }
    }
}