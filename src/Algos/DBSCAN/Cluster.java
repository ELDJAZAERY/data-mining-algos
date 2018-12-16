package Algos.DBSCAN;

import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;

public class Cluster implements Comparable<Cluster> {

    private List<Point> elements;

    public Cluster() {
        this.elements = new ArrayList<>();
    }

    public void addToCluster(Point p) {
        if(elements.contains(p))
            return;
        this.elements.add(p);
    }


    @Override
    public int compareTo(Cluster cluster) {
        return this.elements.size() - cluster.elements.size();
    }

    @Override
    public String toString() {
        return elements.toString();
    }


    public List<Point> getElements() {
        return elements;
    }


    public double getClassScore() {

        Point G = getCenterOfCluster();
        if(G==null) {
            return -1;
        }
        double sum = 0;
        for (Point p : elements) {
            sum += Math.pow(Point.distance(p,G,2),2);
        }
        return sum/this.elements.size();
    }

    public Point getCenterOfCluster() {
        if (elements.size() == 0)
            return null;

        Instances data = new Instances(this.toString(), elements.get(0).getAttInfos(), elements.size());
        for (Point p : this.elements) {
            data.add(p.getPoint());
        }

        StatisticsRetriever sr = new StatisticsRetriever(data);

        Point G = new Point(elements.get(0));
        for (int i = 0; i < G.getPoint().numAttributes()-1; i++) {
            if (G.getPoint().attribute(i).isNumeric()) {
                G.getPoint().attribute(i).setStringValue(String.valueOf(sr.getMean(i)));
            } else {
                G.getPoint().attribute(i).setStringValue(sr.getMode(i));
            }
        }
        return G;
    }

}
