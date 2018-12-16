package Algos.DBSCAN;


import weka.core.Instances;
import java.util.*;


public class DBSCANClusterer {


    private enum PointStatus {
        /**
         * The point has is considered to be noise.
         */
        NOISE,
        /**
         * The point is already part of a cluster.
         */
        CLUSTERED
    }

    private final double epsilon;
    private final double minPts;
    private TreeMap<String, PointStatus> visitedPoints = new TreeMap<>();
    private List<Cluster> clusters = new ArrayList<>();
    private Collection<Point> allPoints = new ArrayList<>();
    private TreeMap<String, BitSet> mapAttributeValueToCodification = new TreeMap<>();

    public int nbNoisePoints = 0 ;

    public DBSCANClusterer(double epsilon, double minPts, Instances instances) {
        this.epsilon = epsilon;
        this.minPts  = minPts;

        for (int i = 0; i < instances.numAttributes() - 1; i++) {
            if (instances.attribute(i).isNominal()) {
                Enumeration a = instances.attribute(i).enumerateValues();
                int codif = 0;
                if (a == null)
                    return;
                while (a.hasMoreElements()) {
                    mapAttributeValueToCodification.put(
                            instances.attribute(i).name().toLowerCase() + " : " + a.nextElement().toString().toLowerCase(),
                            BitSet.valueOf(new long[]{codif++}));
                }
            }
        }

        Point.mapAttributeValueToCodification = mapAttributeValueToCodification;

        for (int i = 0; i < instances.numInstances(); i++) {
            this.allPoints.add(new Point(instances.instance(i)));
        }

    }

    public void start() {
        for (Point point : allPoints) {
            if (visitedPoints.get(point.toString()) != null) {
                System.out.println("Matcheeeeeeed !!");
                continue;
            }
            List<Point> neighbors = getDensityReachableNeighbors(point, allPoints);
            if (neighbors.size() >= minPts) {
                Cluster cluster = new Cluster();
                clusters.add(exploreNeighbors(cluster, point, neighbors, allPoints, visitedPoints));
            } else {
                visitedPoints.put(point.toString(), PointStatus.NOISE);
                nbNoisePoints ++;
            }
        }

        // filtre
        for(int i=0;i<clusters.size();i++){
            if(clusters.get(i).getElements().size() < minPts){
                clusters.remove(i);
                i--;
            }
        }

    }

    private Cluster exploreNeighbors(Cluster cluster,
                                     Point point,
                                     List<Point> neighbors,
                                     Collection<Point> allPoints,
                                     Map<String, PointStatus> visited) {

        cluster.addToCluster(point);
        visited.put(point.toString(), PointStatus.CLUSTERED);

        List<Point> startingNeighbors = new ArrayList<>(neighbors);
        int index = 0;
        while (index < startingNeighbors.size()) {
            Point current = startingNeighbors.get(index);
            PointStatus pointStatus = visited.get(current.toString());
            if (pointStatus == null) {
                List<Point> currentNeighbors = getDensityReachableNeighbors(current, allPoints);
                if (currentNeighbors.size() >= minPts) {
                    startingNeighbors = fusion(startingNeighbors, currentNeighbors);
                }
            }

            if (pointStatus != PointStatus.CLUSTERED) {
                visited.put(current.toString(), PointStatus.CLUSTERED);
                cluster.addToCluster(current);
            }
            index++;
        }
        return cluster;
    }

    private List<Point> fusion(List<Point> one, List<Point> two) {

        final Set<Point> oneSet = new HashSet<>(one);
        for (Point item : two) {
            if (!oneSet.contains(item)) {
                one.add(item);
            }
        }
        return one;
    }

    private List<Point> getDensityReachableNeighbors(Point point, Collection<Point> points) {
        List<Point> neighbors = new ArrayList<>();
        for (Point neighbor : points) {
            double dst = Point.distance(point, neighbor, 2);
            if ( point != neighbor && dst <= epsilon) {
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

    public double getIntraClassScore() {
        if (clusters.size() == 0)
            return -1;
        double sum = 0;
        for (Cluster cl : clusters) {
            sum += cl.getClassScore();
        }
        return sum;
    }

    public double getInterClassScore() {
        if (clusters == null)
            return -1;

        Cluster allGs = new Cluster();
        for (Cluster cl : clusters) {
            allGs.addToCluster(cl.getCenterOfCluster());
        }

        return allGs.getClassScore();
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public double getMinPts() {
        return minPts;
    }

}
