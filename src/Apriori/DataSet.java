package Apriori;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class DataSet {
	
	ArrayList<ArrayList<String>> Attributs= new ArrayList<ArrayList<String>>();
	
	public ArrayList<ArrayList<String>> getAttributs() {
		return Attributs;
	}

	public void setAttributs(ArrayList<ArrayList<String>> attributs) {
		Attributs = attributs;
	}
	


	ArrayList<row> Contenu = new ArrayList<row>();

	public ArrayList<row> getContenu() {
		return Contenu;
	}

	public void setContenu(ArrayList<row> contenu) {
		Contenu = contenu;
	}


	public static DataSet RecupDonnees(String NomFichier){

		DataSet Data= new DataSet();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(NomFichier)));
			String ligne = br.readLine();
			int i;
			while (((ligne = br.readLine()) != null)) {
				// on recherche les lignes ou les attributs sont décris
				if (ligne != null) {
					while (ligne != null) {
						AprioriAlgo.outInstance += ligne +"\n";
						String[] TabL = ligne.split(",");
						row rowTemp = new row();
						for (String s : TabL) {
							if (s.equals("absent")) {
								rowTemp.set.add(s);
							} else {
								if (s.equals("present")) {
									rowTemp.set.add(s);
								} else {
									rowTemp.set.add((s.trim()));
								}
							}
						}
						Data.Contenu.add(rowTemp);
						ligne = br.readLine();
					}
				}
				br.close();
			}}
		catch(Exception e){
			//e.printStackTrace();
		}

		return Data;
	}


}
