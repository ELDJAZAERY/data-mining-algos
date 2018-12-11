package Algos;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Cluster<T> implements Serializable {

    private static final long serialVersionUID = -3442297081515880464L;

    private final List<T> points;

    private final T center;


    public Cluster(final T center) {
        this.center = center;
        points = new ArrayList<T>();
    }

    public void addPoint(final T point) {
        points.add(point);
    }

    public List<T> getPoints() {
        return points;
    }


    public T getCenter() {
        return center;
    }

}

