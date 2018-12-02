package Algos;

import Algos.Apriori.AprioriAlgo;
import Algos.Apriori.row;
import weka.core.Instance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;



public class DataSet {
	
	ArrayList<ArrayList<String>> Attributs= new ArrayList<ArrayList<String>>();
	public ArrayList<row> Contenu = new ArrayList<row>();


	public DataSet(){}
	public DataSet(ArrayList<ArrayList<String>> attributs){
		this.Attributs = attributs;
	}

	public ArrayList<ArrayList<String>> getAttributs() {
		return Attributs;
	}

	public void setAttributs(ArrayList<ArrayList<String>> attributs) {
		this.Attributs = attributs;
	}

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
			}
		}
		catch(Exception e){
			//e.printStackTrace();
		}

		return Data;
	}


	public static DataSet RecupDonnees(DMweKa.DataSet dataSet){

		int nbAttributs = dataSet.insts.numAttributes();
		ArrayList<Instance> instances = new ArrayList<>(dataSet.insts);

		DataSet Data= new DataSet();

		for(Instance inst:instances){
			if(inst.hasMissingValue()) continue;
			row rowTemp = new row();
			for(int i=0;i<nbAttributs;i++){
				if(inst.attribute(i).isNumeric() && !inst.attribute(i).isDate()){
					rowTemp.set.add(""+inst.value(inst.attribute(i)));
				}else{
					rowTemp.set.add(inst.stringValue(inst.attribute(i)));
				}
			}
			Data.Contenu.add(rowTemp);
		}
		return Data;
	}


}
