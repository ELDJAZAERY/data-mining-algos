package DMweKa.Application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TableFx {

    private static int id = 0;

    /** List Attributs Table **/
    SimpleIntegerProperty num;
    SimpleStringProperty name;
    SimpleStringProperty type;

    /** Selected Attribute Table **/
    SimpleStringProperty label;
    SimpleStringProperty weight;

    /** Max Min Mean Q1 Q3 midRange sum  ---> for each Numeric Attribut **/
    SimpleStringProperty max;
    SimpleStringProperty min;
    SimpleStringProperty mean;
    SimpleStringProperty Q1;
    SimpleStringProperty Q3;
    SimpleStringProperty midRange;
    SimpleStringProperty sum;



    public TableFx(String name,String type){
        id++;
        this.num=new SimpleIntegerProperty(id);

        this.name=new SimpleStringProperty(name);
        this.type=new SimpleStringProperty(type);
    }


    public TableFx(String label,String weight,int tab){
        id++;
        this.num=new SimpleIntegerProperty(id);

        this.label=new SimpleStringProperty(label);
        this.weight=new SimpleStringProperty(weight);
    }


    public TableFx(String max,String min,String mean,String Q1,String Q3,String midRange, String sum){
        id++;
        this.num=new SimpleIntegerProperty(id);

        this.max=new SimpleStringProperty(max);
        this.min=new SimpleStringProperty(min);
        this.mean=new SimpleStringProperty(mean);
        this.Q1=new SimpleStringProperty(Q1);
        this.Q3=new SimpleStringProperty(Q3);
        this.midRange=new SimpleStringProperty(midRange);
        this.sum=new SimpleStringProperty(sum);
    }


    public Integer getNum(){
        return num.get();
    }
    public String getName(){
        return name.get();
    }
    public String getType(){
        return type.get();
    }
    public String getLabel() { return label.get(); }
    public String getWeight() { return weight.get(); }
    public String getMax() { return max.get(); }
    public String getMin(){ return min.get();}
    public String getMean() { return mean.get(); }
    public String getQ1() { return Q1.get();}
    public String getQ3() { return Q3.get(); }
    public String getMidRange() { return midRange.get(); }
    public String getSum() { return sum.get(); }


    public static void reInisialize(){ id = 0; }

}
