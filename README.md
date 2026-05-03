# KYS Logistics - Urban Distribution System

A Java implementation of an urban logistics management system for Kayseri, utilizing data structures (Linked Lists, Stacks, Queues, AVL Trees) and graph algorithms (Dijkstra's, MST).

## Project Structure

- **Muhammed Sıtkı Küçük**: Project setup and integration (Package.java, configuration)
- **Hüsna Betül Patat**: Warehouse - SLL + DLL (SinglyLinkedList.java, DoublyLinkedList.java)
- **Ömer Faruk Akdağ**: Dispatch - Queue + Stack (PackageQueue.java, PackageStack.java)
- **Necip Fazıl Taşpınar**: Address Directory - AVL Tree (AVLNode.java, AVLTree.java)
- **Fatmanur Şevval Yücel**: City Routing - Graph Algorithms (Graph.java, MapLoader.java, PackageLoader.java)

## Compile & Run

```bash
# Compile all Java files
javac *.java

# Run the main application
java Main
```

## Team Standards

- **Company Name**: KYS Logistics
- **Warehouse Hub**: Meydan
- **Neighborhoods**: Meydan, Alpaslan, Talas, Erkilet, Belsin, Ildem, Mimsin, Anbar
- **Package ID Format**: PKG_KYS_001, PKG_KYS_002, ...
- **Customer ID Format**: CUST_001, CUST_002, ...
- **Important**: Use "Ildem" (not "İldem", "ildem", or "ILDEM") for consistency in string comparisons

## Data Files

- **mapData.txt**: City map with format `Source Destination Distance_KM`
- **packageData.txt**: Package list with format `PackageID Destination`

## Testing

Test your part locally before pushing:

```bash
javac *.java
java Main  # or your specific test class
```

Ensure no compilation errors and basic functionality works as expected.

## Final Submission

1. All Java source files (.java)
2. mapData.txt and packageData.txt
3. Report with complexity analysis (PDF)
