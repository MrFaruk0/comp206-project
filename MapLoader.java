import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MapLoader {

    public static void loadMap(String fileName, Graph graph) {

        try {

            Scanner scanner = new Scanner(new File(fileName));

            while (scanner.hasNextLine()) {

                String line = scanner.nextLine().trim();

                // for cases of empty or comment lines
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split("\\s+");

                String source = parts[0];
                String destination = parts[1];
                int distance = Integer.parseInt(parts[2]);

                graph.addEdge(source, destination, distance);
            }

            scanner.close();

            System.out.println("Map loaded successfully.");

        } catch (FileNotFoundException e) {

            System.out.println(
                    "Error: map file not found -> " + fileName
            );
        }
    }
}