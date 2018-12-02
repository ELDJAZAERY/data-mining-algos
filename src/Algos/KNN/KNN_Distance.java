package Algos.KNN;

/*
 * This class is for calculating  Euclidean and Manhattan distance. 
 *   */

public class KNN_Distance {
	public double getEuclideanDistance( double[] features1,  double[] features2) {
	    KNN_Implementation imp = new KNN_Implementation();
        double sum = 0;
        for (int i = 0; i < features1.length; i++)
        {  //System.out.println(features1[i]+" "+features2[i]);
        	//applied Euclidean distance formula
            sum += Math.pow(features1[i] - features2[i], 2);
        }return Math.sqrt(sum);
    }

}