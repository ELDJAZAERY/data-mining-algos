package Algos.Apriori;

import Algos.DataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Apriori_ALGO {

	public static int NC = 75, SUPPORT = 100;

	public static String outInstance = "";
	public static String outItems = "";
	public static String outRegles = "";
	public static String Time = "";

	public static void process(String path) {
		outInstance = Time = outItems = outRegles = "";
		long startTime = System.currentTimeMillis();

		DataSet DataS = DataSet.RecupDonnees(path);
		Apriori_ALGO.ExtractionItems(DataS, NC, SUPPORT);

		long time = (System.currentTimeMillis() - startTime) / 1000;
		Time = "" + time;
	}

	public static void process(DMweKa.DataSet data) {
		outInstance = Time = outItems = outRegles = "";
		long startTime = System.currentTimeMillis();

		DataSet DataS = DataSet.RecupDonnees(data);
		Apriori_ALGO.ExtractionItems(DataS, NC, SUPPORT);

		long time = (System.currentTimeMillis() - startTime) / 1000;
		Time = "" + time;
	}

	public static void ExtractionItems(DataSet Data, int NC, int support) {
		int cpt = 0, niv = 2;
		HashSet<String> items = new HashSet<String>();
		HashSet<ArrayList<String>> itemsTuple = new HashSet<ArrayList<String>>();
		HashSet<ArrayList<String>> itemsTuplePris = new HashSet<ArrayList<String>>();
		HashSet<ArrayList<String>> itemsTuplePrisSauv = new HashSet<ArrayList<String>>();

		outItems += ("\t ----- Liste des items C1: ----- \n");
		String line = "";
		// ********************************************niveau
		// un********************************
		for (row r : Data.Contenu) {
			for (String el : r.set) {
				// out += (el);
				items.add(el);
			}
		}
		for (String i : items) {
			cpt = 0;
			for (row r : Data.Contenu) {
				if (r.set.contains(i)) {
					cpt++;

				}
			}
			outItems += (i + " : \t" + cpt + "\n");
			if (cpt > support) {
				ArrayList<String> l = new ArrayList<String>();
				l.add(i);
				itemsTuplePris.add(l);
			}
		}

		outItems += ("\n\n\t ### Liste des items pris:  \n");
		cpt = 1;
		for (ArrayList<String> item : itemsTuplePris) {
			outItems += ("Item " + cpt + ":\t" + item.get(0) + "\n");
			cpt++;
		}

		// ******************************************** construction des autres niveaux
		// ********************************

		while ((itemsTuplePris.size() != 1) && (itemsTuplePris.size() != 0)) {
			outItems += ("\n\n Liste des items C" + niv + ":  \n");
			int deb = 1;
			Object[] Tab = (Object[]) itemsTuplePris.toArray();
			itemsTuple = new HashSet<ArrayList<String>>();
			for (ArrayList<String> l : itemsTuplePris) {
				deb++;
				for (int i = deb; i < Tab.length; i++) {
					HashSet<String> tri = new HashSet<String>();
					tri.addAll(l);
					tri.addAll(((ArrayList<String>) Tab[i]));
					if (tri.size() == niv) {
						ArrayList<String> tempor = new ArrayList<String>();
						tempor.addAll(tri);
						Collections.sort(tempor);
						itemsTuple.add(tempor);
					}
				}

			}
			itemsTuplePrisSauv = new HashSet<ArrayList<String>>();
			itemsTuplePrisSauv.addAll(itemsTuplePris);
			itemsTuplePris = new HashSet<ArrayList<String>>();

			for (ArrayList<String> l : itemsTuple) {
				cpt = 0;

				for (row r : Data.Contenu) {
					line = " (";
					boolean find = true;
					for (String el : l) {
						line = line + el + " , ";
						if (!r.set.contains(el)) {
							find = false;
						}
					}
					if (find == true) {
						cpt++;
					}
				}
				outItems += (line.substring(0, line.length() - 2) + ") :\t " + cpt + "\n");
				if (cpt > support) {
					itemsTuplePris.add(l);

				}

			}
			outItems += ("\n\n\t ### Liste des items C" + niv + " pris: \n ");
			cpt = 1;

			for (ArrayList<String> l : itemsTuplePris) {
				line = " (";
				for (String el : l) {
					line = line + el + " , ";
				}
				outItems += ("Item " + cpt + ":\t" + line.substring(0, line.length() - 2) + ")\n");
				cpt++;
			}
			niv++;
		}

		// **************************************Regles
		// d'association***************************************
		itemsTuple = new HashSet<ArrayList<String>>();
		if (itemsTuplePris.size() == 0) {
			itemsTuplePris = itemsTuplePrisSauv;
		}
		ArrayList<RegleAsso> RegleAssoc = new ArrayList<RegleAsso>();

		int trace = 0;
		for (ArrayList<String> l : itemsTuplePris) // pour chaque item candidat
		{
			outRegles += ("\t ## Les Régles d'association ## \n ");
			for (String item : l) {
				ArrayList<String> tempor = new ArrayList<String>();
				tempor.add(item);
				itemsTuple.add(tempor);
			}
			trace = 0;
			for (int j = 2; j <= l.size() - 1; j++) {
				int deb = trace;
				Object[] Tab = (Object[]) itemsTuple.toArray();
				for (int s = trace; s < Tab.length; s++) {
					deb++;
					for (int k = deb; k < Tab.length; k++) {
						HashSet<String> tri = new HashSet<String>();
						tri.addAll(((ArrayList<String>) Tab[s]));
						tri.addAll(((ArrayList<String>) Tab[k]));

						if (tri.size() == j) {
							ArrayList<String> tempor = new ArrayList<String>();
							tempor.addAll(tri);
							Collections.sort(tempor);
							itemsTuple.add(tempor);
						}
					}
				}
				trace = Tab.length;

			}
			for (ArrayList<String> part : itemsTuple) {
				RegleAsso R = new RegleAsso();
				R.PartieGauche.addAll(part);
				for (String i : l) {
					if (!R.PartieGauche.contains(i)) {
						R.PartieDroite.add(i);
					}
				}
				RegleAssoc.add(R);

			}
			// Niveaux de confiance
			int supp, supp2 = 0;
			float nivConf;
			ArrayList<RegleAsso> RegleAssocPrises = new ArrayList<RegleAsso>();
			for (RegleAsso R : RegleAssoc) {
				boolean find = true;
				supp = 0;
				supp2 = 0;
				ArrayList<String> temp = new ArrayList<String>();
				temp.addAll(R.PartieGauche);
				temp.addAll(R.PartieDroite);
				for (row r : Data.Contenu) {
					find = true;
					for (String el : temp) {
						if (!r.set.contains(el)) {
							find = false;
						}
					}
					if (find == true) {
						supp++;
					}
				}
				for (row r : Data.Contenu) {
					find = true;
					for (String el : R.PartieGauche) {
						if (!r.set.contains(el)) {
							find = false;
						}
					}
					if (find == true) {
						supp2++;
					}
				}
				nivConf = ((float) supp / supp2) * 100;
				if (nivConf > NC) {
					RegleAssocPrises.add(R);
				}
				String PG = "";
				String PD = "";
				for (String d : R.PartieGauche) {
					PG = PG + d + " ,";
				}
				for (String d : R.PartieDroite) {
					PD = PD + d + " ,";
				}

				if (PG.length() > 0 && PD.length() > 0)
					outRegles += (PG.substring(0, PG.length() - 1) + "====>" + PD.substring(0, PD.length() - 1) + " :\t"
							+ nivConf + " %\n ");
			}

			outRegles += ("\n\n\t ## Les Régles d'association prises ## \n ");
			for (RegleAsso R : RegleAssocPrises) {
				String PG = "";
				String PD = "";
				for (String d : R.PartieGauche) {
					PG = PG + d + " ,";
				}
				for (String d : R.PartieDroite) {
					PD = PD + d + " ,";
				}

				if (PG.length() > 0 && PD.length() > 0)
					outRegles += (PG.substring(0, PG.length() - 1) + "====>" + PD.substring(0, PD.length() - 1)
							+ " \n ");
			}

			outRegles += "\n\n";
		}

	}

}
