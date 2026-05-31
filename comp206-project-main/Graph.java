import java.util.*;

public class Graph {

    private Map<String, List<Edge>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    // Edge class
    private static class Edge {
        String destination;
        int weight;

        Edge(String destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    // Node class for Dijkstra
    private static class Node {
        String name;
        int distance;

        Node(String name, int distance) {
            this.name = name;
            this.distance = distance;
        }
    }

    // adding undirected edge
    public void addEdge(String source, String destination, int weight) {

        adjacencyList.putIfAbsent(source, new ArrayList<>());
        adjacencyList.putIfAbsent(destination, new ArrayList<>());

        adjacencyList.get(source)
                .add(new Edge(destination, weight));

        adjacencyList.get(destination)
                .add(new Edge(source, weight));
    }

    // displays graph
    public void displayGraph() {

        System.out.println("=== CITY MAP ===");

        for (String node : adjacencyList.keySet()) {

            System.out.print(node + " -> ");

            for (Edge edge : adjacencyList.get(node)) {
                System.out.print("("
                        + edge.destination
                        + ", "
                        + edge.weight
                        + " km) ");
            }

            System.out.println();
        }
    }

    public void calculateShortestPath(String start, String end) {
        dijkstra(start, end);
    }

    public void calculateMST() {
        primMST();
    }

    // Dijkstra Algorithm
    private void dijkstra(String start, String end) {

        Map<String, Integer> distance = new HashMap<>();
        Map<String, String> previous = new HashMap<>();

        PriorityQueue<Node> pq =
                new PriorityQueue<>(
                        Comparator.comparingInt(n -> n.distance)
                );

        for (String city : adjacencyList.keySet()) {
            distance.put(city, Integer.MAX_VALUE);
        }

        distance.put(start, 0);
        pq.add(new Node(start, 0));

        while (!pq.isEmpty()) {

            Node current = pq.poll();

            for (Edge edge : adjacencyList.get(current.name)) {

                int newDistance =
                        distance.get(current.name)
                                + edge.weight;

                if (newDistance <
                        distance.get(edge.destination)) {

                    distance.put(
                            edge.destination,
                            newDistance
                    );

                    previous.put(
                            edge.destination,
                            current.name
                    );

                    pq.add(
                            new Node(
                                    edge.destination,
                                    newDistance
                            )
                    );
                }
            }
        }

        if (!distance.containsKey(end)
                || distance.get(end)
                == Integer.MAX_VALUE) {

            System.out.println(
                    "No path found between "
                            + start
                            + " and "
                            + end
            );

            return;
        }

        List<String> path =
                new ArrayList<>();

        String current = end;

        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }

        Collections.reverse(path);

        System.out.println("\n=== SHORTEST PATH ===");
        System.out.println(
                String.join(" -> ", path)
        );

        System.out.println(
                "Distance: "
                        + distance.get(end)
                        + " km"
        );
    }

    // Prim MST Algorithm
    private void primMST() {

        if (adjacencyList.isEmpty()) {
            return;
        }

        Set<String> visited =
                new HashSet<>();

        String start =
                adjacencyList
                        .keySet()
                        .iterator()
                        .next();

        visited.add(start);

        int totalWeight = 0;

        System.out.println(
                "\n=== MINIMUM SPANNING TREE ==="
        );

        while (
                visited.size()
                        < adjacencyList.size()
        ) {

            Edge bestEdge = null;
            String from = null;

            for (String node : visited) {

                for (Edge edge :
                        adjacencyList.get(node)) {

                    if (!visited.contains(
                            edge.destination)) {

                        if (bestEdge == null
                                || edge.weight
                                < bestEdge.weight) {

                            bestEdge = edge;
                            from = node;
                        }
                    }
                }
            }

            if (bestEdge == null) {
                break;
            }

            visited.add(bestEdge.destination);

            totalWeight += bestEdge.weight;

            System.out.println(
                    from
                            + " -> "
                            + bestEdge.destination
                            + " ("
                            + bestEdge.weight
                            + " km)"
            );
        }

        System.out.println(
                "Total MST Weight: "
                        + totalWeight
                        + " km"
        );
    }
}