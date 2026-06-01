public class DoublyLinkedList {

    private class Node {
        Package data;
        Node next;
        Node prev;

        Node(Package data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private Node head;
    private Node tail;

    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;
    }

    // Adds a newly arrived package to the end of the intake buffer.
    // Time Complexity: O(1), because tail pointer is used.
    public void insertAtTail(Package pkg) {
        Node newNode = new Node(pkg);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
    }

    // Removes the first package from the intake buffer.
    // Time Complexity: O(1)
    public Package removeFromHead() {
        if (head == null) {
            System.out.println("Intake Buffer is empty. No package to remove.");
            return null;
        }

        Package removedPackage = head.data;

        if (head == tail) {
            head = null;
            tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }

        return removedPackage;
    }

    // Removes a specific package from the buffer by package ID.
    // Time Complexity: O(n)
    public boolean removePackage(String packageID) {
        if (head == null) {
            return false;
        }

        Node current = head;

        while (current != null) {
            if (current.data.getPackageID().equals(packageID)) {

                if (current == head && current == tail) {
                    head = null;
                    tail = null;
                } else if (current == head) {
                    head = head.next;
                    head.prev = null;
                } else if (current == tail) {
                    tail = tail.prev;
                    tail.next = null;
                } else {
                    current.prev.next = current.next;
                    current.next.prev = current.prev;
                }

                return true;
            }

            current = current.next;
        }

        return false;
    }

    // Displays all packages waiting in the intake buffer.
    // Time Complexity: O(n)
    public void displayBuffer() {
        if (head == null) {
            System.out.println("Intake Buffer is empty.");
            return;
        }

        System.out.println("=== INTAKE BUFFER ===");

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

    // Gets all packages in the buffer as a list without removing them
    // Time Complexity: O(n)
    public java.util.List<Package> getAllPackages() {
        java.util.List<Package> packages = new java.util.ArrayList<>();
        Node current = head;
        
        while (current != null) {
            packages.add(current.data);
            current = current.next;
        }
        
        return packages;
    }
}