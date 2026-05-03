public class TestWarehouse {
    public static void main(String[] args) {

        SinglyLinkedList masterRegistry = new SinglyLinkedList();
        DoublyLinkedList intakeBuffer = new DoublyLinkedList();

        Package p1 = new Package("PKG_KYS_001", "Talas");
        Package p2 = new Package("PKG_KYS_002", "Belsin");
        Package p3 = new Package("PKG_KYS_003", "Ildem");
        Package p4 = new Package("PKG_KYS_004", "Anbar");

        System.out.println("New packages are arriving to the warehouse...\n");

        intakeBuffer.insertAtTail(p1);
        intakeBuffer.insertAtTail(p2);
        intakeBuffer.insertAtTail(p3);
        intakeBuffer.insertAtTail(p4);

        masterRegistry.addRecord(p1);
        masterRegistry.addRecord(p2);
        masterRegistry.addRecord(p3);
        masterRegistry.addRecord(p4);

        intakeBuffer.displayBuffer();

        System.out.println("\nRemoving first package from intake buffer...");
        Package removed = intakeBuffer.removeFromHead();

        if (removed != null) {
            System.out.println("Removed package: " + removed);
        }

        System.out.println();
        intakeBuffer.displayBuffer();

        System.out.println("\nRemoving package with ID PKG_KYS_003...");
        boolean isRemoved = intakeBuffer.removePackage("PKG_KYS_003");

        if (isRemoved) {
            System.out.println("Package removed successfully.");
        } else {
            System.out.println("Package could not be found.");
        }

        System.out.println();
        intakeBuffer.displayBuffer();

        System.out.println();
        masterRegistry.displayLog();
    }
}