package Algos.DBSCAN;


import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;


public class EuclideanDoublePoint implements Serializable {


    /** Serializable version identifier. */
    private static final long serialVersionUID = 8026472786091227632L;

    /** Point coordinates. */
    private final double[] point;

    public EuclideanDoublePoint(final double[] point) {
        this.point = point;
    }

    public EuclideanDoublePoint centroidOf(final Collection<EuclideanDoublePoint> points) {
        final double[] centroid = new double[getPoint().length];
        for (final EuclideanDoublePoint p : points) {
            for (int i = 0; i < centroid.length; i++) {
                centroid[i] += p.getPoint()[i];
            }
        }
        for (int i = 0; i < centroid.length; i++) {
            centroid[i] /= points.size();
        }
        return new EuclideanDoublePoint(centroid);
    }


    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof EuclideanDoublePoint)) {
            return false;
        }
        return Arrays.equals(point, ((EuclideanDoublePoint) other).point);
    }

    public double[] getPoint() {
        return point;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(point);
    }

    @Override
    public String toString() {
        return Arrays.toString(point);
    }

}