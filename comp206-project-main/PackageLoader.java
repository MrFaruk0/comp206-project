import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class PackageLoader {

    public static ArrayList<Package> loadPackages(String fileName) {

        ArrayList<Package> packages = new ArrayList<>();

        try {

            Scanner scanner = new Scanner(new File(fileName));

            while (scanner.hasNextLine()) {

                String line = scanner.nextLine().trim();

                // for cases of empty or comment lines
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split("\\s+");

                String packageID = parts[0];
                String destination = parts[1];

                packages.add(
                        new Package(
                                packageID,
                                destination
                        )
                );
            }

            scanner.close();

            System.out.println("Packages loaded successfully.");

        } catch (FileNotFoundException e) {

            System.out.println(
                    "Error: package file not found -> " + fileName
            );
        }

        return packages;
    }
}