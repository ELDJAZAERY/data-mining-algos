package Algos.DBSCAN;


import Algos.Cluster;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DBSCANClusterer<T> {

    private final double eps;

    private final int   minPts;


    private enum PointStatus {
        NOISE,
        PART_OF_CLUSTER
    }

    public DBSCANClusterer(final double eps, final int minPts){
        this.eps = eps;
        this.minPts = minPts;
    }

    public double getEps() {
        return eps;
    }

    public int getMinPts() {
        return minPts;
    }

    public List<Cluster<T>> cluster(final Collection<T> points) {

        final List<Cluster<T>> clusters = new ArrayList<Cluster<T>>();

        for (final T point : points) {
            final List<T> neighbors = getNeighbors(point, points);
            if (neighbors.size() >= minPts) {
                // DBSCAN does not care about center points
                final Cluster<T> cluster = new Cluster<T>(null);
                clusters.add(expandCluster(cluster, point, neighbors, points));
            }
        }
        return clusters;
    }

    private Cluster<T> expandCluster(final Cluster<T> cluster,
                                     final T point,
                                     final List<T> neighbors,
                                     final Collection<T> points) {
        cluster.addPoint(point);

        List<T> seeds = new ArrayList<T>(neighbors);
        int index = 0;
        while (index < seeds.size()) {
            final T current = seeds.get(index);
            PointStatus pStatus = null ;
            // only check non-visited points
            if (pStatus == null) {
                final List<T> currentNeighbors = getNeighbors(current, points);
                if (currentNeighbors.size() >= minPts) {
                    seeds = merge(seeds, currentNeighbors);
                }
            }

            if (pStatus != PointStatus.PART_OF_CLUSTER) {
                cluster.addPoint(current);
            }

            index++;
        }
        return cluster;
    }


    private List<T> getNeighbors(final T point, final Collection<T> points) {
        final List<T> neighbors = new ArrayList<T>();
        for (final T neighbor : points) {
            if (point != neighbor ) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    private List<T> merge(final List<T> one, final List<T> two) {
        final Set<T> oneSet = new HashSet<T>(one);
        for (T item : two) {
            if (!oneSet.contains(item)) {
                one.add(item);
            }
        }
        return one;
    }

}