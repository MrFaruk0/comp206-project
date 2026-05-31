public class Package {
    private String packageID;
    private String destination;

    public Package(String packageID, String destination) {
        this.packageID = packageID;
        this.destination = destination;
    }

    public String getPackageID() {
        return packageID;
    }

    public String getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return packageID + " -> " + destination;
    }
}
