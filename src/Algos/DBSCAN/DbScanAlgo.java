package Algos.DBSCAN;


import Algos.Cluster;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class DbScanAlgo {
    public static List <Vector>Data;
    public static List <Cluster> Clusters;
    public static List <Boolean > Pvisited;
    public static List <Boolean > clustered;
    public static List  neighborpts;
    public static List  neighbors;
    public static List  noise;


    public static void DBSCAN (int esp, int minPts){
        int c=0;
        //System.out.println(c);
        Clusters = new <Cluster> ArrayList();
        neighborpts= new ArrayList();
        for(int i=0 ; i<Data.size(); i++){
            neighborpts.add(null);
        }
        noise= new ArrayList();
        for(int i =0 ; i< Data.size(); i++){
            if(!Pvisited.get(i)){
                Pvisited.set(i,true);
                neighborpts.set(i,regionQuery(Data.get(i),esp));
                int size=neighborpts.size();
                if(size< minPts)
                    noise.add(i);
                else{
                    Clusters.addAll(Data.get(i));
                    c++;
                    Cluster C= new Cluster(c);
                    C.addPoint(Data.get(c));
                    Clusters.add(C);
                    for(int j= 0; j<size;j++){
                        if(!Pvisited.get(neighborpts.indexOf(j))){
                            Pvisited.set(j,true);
                            neighbors.add(regionQuery((Vector) neighborpts.get(j),esp));
                            int nSize=neighbors.size();
                            if (nSize>= minPts){
                                neighborpts.add(neighbors);
                            }
                        }

                        if(!clustered.get(neighborpts.indexOf(j))){
                            int x=(int) neighborpts.get(j);
                            Cluster f= Clusters.get(c);
                            //((List<Integer>) f).add(x);
                        }
                    }
                }
            }
        }
    }

    public static double ecluediean (Vector center, Vector L){
        Double result = (double) 0;
        for(int i =0; i< center.size(); i++){
            result +=Math.pow(((double) center.get(i))-(double)(L.get(i)), 2);
        }

        return Math.sqrt(result);
    }

    public static List regionQuery(Vector p, int eps){
        List <Vector> n = new ArrayList();
        for(int i=0 ; i<Data.size(); i++){
            n.add(null);
        }
        double dis=0;
        for(int i =0; i<Data.size();i++){
            dis=ecluediean(p,Data.get(i));

            if(dis<= eps){

                n.set(i,Data.get(i));
            }
        }
        //System.out.println(n);
        return n;
    }

}