public class AVLTree {
    private AVLNode root;

    public AVLTree() {
        this.root = null;
    }

    public void insert(String neighborhood, String customerID) {
        root = insertNode(root, neighborhood, customerID);
    }

    private AVLNode insertNode(AVLNode node, String neighborhood, String customerID) {
        if (node == null) {
            return new AVLNode(neighborhood, customerID);
        }

        int cmp = neighborhood.compareTo(node.neighborhood);
        if (cmp < 0) {
            node.left = insertNode(node.left, neighborhood, customerID);
        } else if (cmp > 0) {
            node.right = insertNode(node.right, neighborhood, customerID);
        } else {
            node.customerID = customerID;
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    public String search(String neighborhood) {
        AVLNode current = root;
        while (current != null) {
            int cmp = neighborhood.compareTo(current.neighborhood);
            if (cmp == 0) {
                return current.customerID;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null;
    }

    private int height(AVLNode node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    private int getBalance(AVLNode node) {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    private AVLNode rotateLeft(AVLNode node) {
        AVLNode newRoot = node.right;
        AVLNode movedSubtree = newRoot.left;

        newRoot.left = node;
        node.right = movedSubtree;

        node.height = 1 + Math.max(height(node.left), height(node.right));
        newRoot.height = 1 + Math.max(height(newRoot.left), height(newRoot.right));

        return newRoot;
    }

    private AVLNode rotateRight(AVLNode node) {
        AVLNode newRoot = node.left;
        AVLNode movedSubtree = newRoot.right;

        newRoot.right = node;
        node.left = movedSubtree;

        node.height = 1 + Math.max(height(node.left), height(node.right));
        newRoot.height = 1 + Math.max(height(newRoot.left), height(newRoot.right));

        return newRoot;
    }

    private AVLNode balance(AVLNode node) {
        int balanceFactor = getBalance(node);

        if (balanceFactor > 1 && getBalance(node.left) >= 0) {
            return rotateRight(node);
        }

        if (balanceFactor > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balanceFactor < -1 && getBalance(node.right) <= 0) {
            return rotateLeft(node);
        }

        if (balanceFactor < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    public int getRootHeight() {
        return height(root);
    }

    public String getRootKey() {
        return root == null ? null : root.neighborhood;
    }

    public void displayInOrder() {
        inOrderTraversal(root);
    }

    private void inOrderTraversal(AVLNode node) {
        if (node == null) {
            return;
        }
        inOrderTraversal(node.left);
        System.out.println(node.neighborhood + " -> " + node.customerID);
        inOrderTraversal(node.right);
    }
}
