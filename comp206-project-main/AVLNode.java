public class AVLNode {
    String neighborhood;
    String customerID;
    int height;
    AVLNode left;
    AVLNode right;

    public AVLNode(String neighborhood, String customerID) {
        this.neighborhood = neighborhood;
        this.customerID = customerID;
        this.height = 1;
        this.left = null;
        this.right = null;
    }
}
