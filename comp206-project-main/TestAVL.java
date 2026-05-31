public class TestAVL {
    public static void main(String[] args) {
        AVLTree directory = new AVLTree();

        directory.insert("Talas", "CUST_001");
        directory.insert("Belsin", "CUST_002");
        directory.insert("Ildem", "CUST_003");
        directory.insert("Erkilet", "CUST_004");
        directory.insert("Mimsin", "CUST_005");
        directory.insert("Anbar", "CUST_006");
        directory.insert("Alpaslan", "CUST_007");

        System.out.println("In-order traversal (sorted by neighborhood):");
        directory.displayInOrder();

        System.out.println();
        System.out.println("Search Ildem  : " + directory.search("Ildem"));
        System.out.println("Search Talas  : " + directory.search("Talas"));
        System.out.println("Search Anbar  : " + directory.search("Anbar"));
        System.out.println("Search Unknown: " + directory.search("Unknown"));

        System.out.println();
        System.out.println("--- Stress test: worst-case ascending insert ---");
        AVLTree ascending = new AVLTree();
        for (int i = 1; i <= 15; i++) {
            ascending.insert(String.format("N%02d", i), "C" + i);
        }
        System.out.println("15 ascending insertions done.");
        System.out.println("Tree height: " + ascending.getRootHeight() + " (perfect balanced = 4)");
        System.out.println("Root: " + ascending.getRootKey() + " (balanced ~middle expected)");

        System.out.println();
        System.out.println("--- Duplicate update test ---");
        AVLTree dup = new AVLTree();
        dup.insert("Talas", "OLD_ID");
        dup.insert("Talas", "NEW_ID");
        System.out.println("After re-insert Talas: " + dup.search("Talas") + " (expected NEW_ID)");
    }
}
