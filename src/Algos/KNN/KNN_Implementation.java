package Algos.KNN;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/*
 * Knn algorithm implementation
 */
public class KNN_Implementation {

	private List<double[]> trainfeatures = new ArrayList<>();
	private List<String> trainlabel = new ArrayList<>();

	private List<double[]> testfeatures = new ArrayList<>();

	Scanner sc = new Scanner(System.in);
	int knn_value = 1;
	int DistanceMetricsSelction = 0;


	void distanceCalcualte() throws IOException {
		KnnParams.voising = null;
		KnnParams.voisin = null;
		if (DistanceMetricsSelction == 1) {
			euclideanDistance();
			// calling accuracy method to show accuracy of model.
		}

		else if (DistanceMetricsSelction == 2) {
			manhattanDistance();
		}

		else {
			// if user selecting invalid options then they must select correct option.
			getKValueandDistMetrics();
			distanceCalcualte();
		}
	}


	void euclideanDistance() throws FileNotFoundException {
		KNN_Distance euclidean = new KNN_Distance();

		Iterator<double[]> testITR = testfeatures.iterator();

		PrintWriter pw = new PrintWriter("EuclideanResult.txt");

		while (testITR.hasNext()) {
			double testF[] = testITR.next();
			Iterator<double[]> trainITR = trainfeatures.iterator();
			int noOfobject = 0;
			while (trainITR.hasNext()) {
				double trainF[] = trainITR.next();
				double dist = 0;
				dist = euclidean.getEuclideanDistance(trainF, testF);

				String trainFeat = trainlabel.get(noOfobject);
				noOfobject++;

			}

			/*
			 * counting top predicted label based on k value
			 */
			int flag = 0, IR_Setsosa = 0, IR_Versicolor = 0, IR_Virginica = 0;


			/*
			 * counting label and selecting highest label count as prediction label and
			 * writing to output file.
			 */
			if (IR_Setsosa > IR_Versicolor && IR_Setsosa > IR_Virginica) {
				pw.println("Iris-setosa");

			} else if (IR_Versicolor > IR_Setsosa && IR_Versicolor > IR_Virginica) {
			}

			else if (IR_Virginica > IR_Setsosa && IR_Virginica > IR_Versicolor) {
			}
		}
		KnnParams.cluster = null;
		pw.close();
	}

	void manhattanDistance() throws FileNotFoundException {
		KNN_Distance euclidean = new KNN_Distance();

		Iterator<double[]> testITR = testfeatures.iterator();

		PrintWriter pw = new PrintWriter("ManhattanResult.txt");

		while (testITR.hasNext()) {
			double testF[] = testITR.next();
			Iterator<double[]> trainITR = trainfeatures.iterator();
			int noOfobject = 0;
			while (trainITR.hasNext()) {
				double trainF[] = trainITR.next();
				double dist = 0;

				String trainFeat = trainlabel.get(noOfobject);
				noOfobject++;

			}

			/*
			 * counting top predicted label based on k value
			 */

			int flag = 0, IR_Setsosa = 0, IR_Versicolor = 0, IR_Virginica = 0;


			/*
			 * counting label and selecting highest label count as prediction label and
			 * writing to output file.
			 */

			if (IR_Setsosa > IR_Versicolor && IR_Setsosa > IR_Virginica) {
				pw.println("Iris-setosa");

			} else if (IR_Versicolor > IR_Setsosa && IR_Versicolor > IR_Virginica) {
				pw.println("Iris-versicolor");
			}

			else if (IR_Virginica > IR_Setsosa && IR_Virginica > IR_Versicolor) {
				pw.println("Iris-virginica");
			}
		}
		pw.close();
	}

	/*
	 * method to get K value and Distance metrics.
	 */

	void getKValueandDistMetrics() {

		knn_value = sc.nextInt();
		// Restricted k value less 50.
		if (knn_value > 50) {
			getKValueandDistMetrics();
		} else {

			DistanceMetricsSelction = sc.nextInt();

		}
		try{distanceCalcualte();}catch (Exception e){}
	}


}
