package DMweKa;

import DMweKa.Application.TableFx;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * Weka bibs
 **/
/** Weka bibs  **/



public class AttributDataSet {

    static String[] types = {"numeric","nominal","string","date","relational"};


    private String name ;
    private Attribute attribut;
    private ArrayList<Instance> instances ;

    /** one day maybe !! xD */
    //private ProtectedProperties properties;

    private int distinct ;
    private double  max , min , Q1 , mean , Q3 , midRange  , weight , sum ;
    private String mode;
    private boolean isNumeric = false;


    // For count the frequent of each Attribut's value < Nominal , Numeric and Date >
    TreeMap<Double,Integer> valsFreq = new TreeMap<>() ;
    TreeMap<String,Integer> nominalFreq = new TreeMap<>() ;


    AttributDataSet(Attribute attribut, Instances dataSet) {
        this.name = attribut.name();
        this.attribut = attribut;

        this.instances = new ArrayList<>(dataSet);
        weight   = attribut.weight();

        // spetial for the numeric attributs !!
        if(type().equals("numeric")) {
            isNumeric = true;
            numericCalculus(dataSet);
        }
        calculeModeDist();

    }

    public ArrayList<Instance> getinstances(){
        return instances;
    }
    public String getvalue(int i){
        if(i>instances.size()) return "";
        String val ;
        if(isNumeric){
            val = Double.toString(instances.get(i).value(this.attribut));
        }else{
            val = instances.get(i).stringValue(this.attribut);
        }
        return val;
    }

    public TreeMap<String,Integer> getLabelsAndWeightNom(){
        return nominalFreq;
    }

    public TreeMap<Double,Integer> getLabelsAndWeightNum(){
        return valsFreq ;
    }

    public int getindex(){
        return attribut.index();
    }
    public String name() {
        return name;
    }

    public String type() {
        return types[attribut.type()];
    }

    public String distinct() {
        return ""+distinct;
    }

    public String weight() {
        return ""+weight;
    }

    public String max(){ return ""+max; }

    public Double valmax() {return  max;}
    public Double valmin() {return  min;}

    public String min(){
        return ""+min;
    }

    public String mean(){
        return  ""+mean;
    }

    public String sum(){
        return ""+sum;
    }

    public String Q1(){
        return ""+Q1;
    }

    public String Q3(){
        return ""+Q3;
    }


    public String mideRange(){
        return ""+midRange;
    }

    public boolean isNumeric(){
        return isNumeric;
    }

    public String mode(){
        return ""+mode;
    }

    private void numericCalculus(Instances instances) {
        double[] values;
        int  half , quarter;

        // sort attribute data
        values = instances.attributeToDoubleArray(attribut.index());
        Arrays.sort(values);

        // determine indices
        half    = values.length / 2;
        quarter = half / 2;

        this.min = values[0];
        this.max = values[values.length-1];
        this.midRange = (min + max) / 2;
        if (values.length % 2 == 1) this.mean = values[half];
        else this.mean = (values[half] + values[half + 1]) / 2;


        if (half % 2 == 1) {
            Q1 = values[quarter];
            Q3 = values[values.length - quarter - 1];
        }
        else {
            Q1 = (values[quarter] + values[quarter + 1]) / 2;
            Q3 = (values[values.length - quarter - 1] + values[values.length - quarter]) / 2;
        }

        for(int i=0;i<values.length;i++){
            this.sum += values[i];
        }
    }


    private void calculeModeDist(){

        Double numVal ; String nominalVal ; Integer freq ;

        if(isNumeric){
            /** Mode For  Numeric Type !! **/
            for(Instance inst:instances){
                numVal = inst.value(attribut);
                if(valsFreq.containsKey(numVal)){
                    freq = valsFreq.get(numVal)+1;
                    valsFreq.put(numVal,freq);
                }else{
                    valsFreq.put(numVal,1);
                }
            }
            this.distinct = valsFreq.size();
        }else{
            /** Mode For  Nominal , String , Date and Relational Type !! **/
            for(Instance inst:instances){
                nominalVal = inst.stringValue(attribut);
                if(nominalFreq.containsKey(nominalVal)){
                    freq = nominalFreq.get(nominalVal)+1;
                    nominalFreq.put(nominalVal,freq);
                }else{
                    nominalFreq.put(nominalVal,1);
                }
            }
            this.distinct = nominalFreq.size();
        }

        if(nominalFreq.size() == valsFreq.size()) return;
        if(isNumeric){
            Map.Entry<Double,Integer> maxEntry = null;
            for (Map.Entry<Double,Integer> entry : valsFreq.entrySet()) {
                if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0){
                    maxEntry = entry;
                }
            }
            this.mode = Double.toString(maxEntry.getKey());
        }else{
            Map.Entry<String,Integer> maxEntryy = null;
            for (Map.Entry<String,Integer> entry : nominalFreq.entrySet()) {
                if (maxEntryy == null || (entry.getValue() > maxEntryy.getValue()) ){
                    maxEntryy = entry;
                }
            }
            this.mode = maxEntryy.getKey();
        }
    }

    @Override
    public String toString() {
        return "AttributDataSet{" +
                "name='" + name + '\'' +
                ", distinct=" + distinct +
                ", max=" + max +
                ", min=" + min +
                ", Q1=" + Q1 +
                ", mean=" + mean +
                ", Q3=" + Q3 +
                ", midRange=" + midRange +
                ", weight=" + weight +
                ", sum=" + sum +
                ", mode='" + mode + '\'' +
                '}';
    }


    public ObservableList<TableFx> tableFxItems(){
        // Reinsialize the static counter
        TableFx.reInisialize();
        ObservableList<TableFx> items = FXCollections.observableArrayList();
        if(isNumeric){
            for(Double key : valsFreq.descendingKeySet()){
                items.add(new TableFx(Double.toString(key),""+valsFreq.get(key),2));
            }
        }else{
            for(String key : nominalFreq.descendingKeySet()){
                items.add(new TableFx(key,""+nominalFreq.get(key),2));
            }
        }

        return items;
    }

    public ObservableList<TableFx> tableFxItemsNumiric(){
        // Reinsialize the static counter
        TableFx.reInisialize();
        ObservableList<TableFx> items = FXCollections.observableArrayList();
        items.add(new TableFx(max(),min(),mean(),Q1(),Q3(),mideRange(),sum()));
        return items;
    }


    public void toTableFx(){
        // TODO
    }

}
