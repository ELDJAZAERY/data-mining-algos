package DMweKa.Application;

import DMweKa.DataSet;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DynamicTableFx {

    //TABLE VIEW AND DATA

    public static TableView instqncesToTableView(Instances instances , TableView tableview){
        int maxNbAttributs = 100 ;
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        tableview.getColumns().clear();


        /**********************************
         * TABLE COLUMN ADDED DYNAMICALLY *
         **********************************/
        for(int i=0 ; i< instances.numAttributes(); i++){
            final int j = i;
            TableColumn col = new TableColumn(instances.attribute(i).name());
            col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });

            tableview.getColumns().addAll(col);
        }

        /********************************
         * Data added to ObservableList *
         ********************************/
        for(int i=0;i< instances.numInstances();i++){
            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int j=0 ; j < instances.numAttributes() ; j++){
                //Iterate Column
                if(instances.instance(i).isMissing(j)){
                    row.add("?");
                    continue;
                }

                if(instances.instance(i).attribute(j).isNumeric()){
                    row.add(Double.toString(instances.instance(i).value(j)));
                }else{
                    row.add(instances.instance(i).stringValue(j));
                }

            }
            data.add(row);
        }

        //FINALLY ADDED TO TableView
        tableview.setItems(data);
        return tableview;
    }



    public static TableView dataSetToTableView(DataSet dataSet , TableView tableview){

        int maxNbAttributs = 100 ;
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        tableview.getColumns().clear();


        /**********************************
         * TABLE COLUMN ADDED DYNAMICALLY *
         **********************************/
        for(int i=0 ; i< Math.min(dataSet.nbAttributs(),maxNbAttributs); i++){
            final int j = i;
            TableColumn col = new TableColumn(dataSet.getAttribut(i).name());
            col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });

            tableview.getColumns().addAll(col);
        }

        /********************************
         * Data added to ObservableList *
         ********************************/
        for(int i=0;i< dataSet.nbInstances();i++){
            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int j=0 ; j < Math.min(dataSet.nbAttributs(),maxNbAttributs); j++){
                //Iterate Column
                row.add(dataSet.getAttribut(j).getvalue(i));
            }
            data.add(row);
        }

        //FINALLY ADDED TO TableView
        tableview.setItems(data);
        return tableview;
    }

    public static TableView missingValueToTableView(HashMap<Instance,Attribute> missing,DataSet dataset ,TableView tableview){

        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        tableview.getColumns().clear();

        String[] columsNames = {"Instance Num","Attribut","New Value"};

        /**********************************
         * TABLE COLUMN ADDED DYNAMICALLY *
         **********************************/
        for(int i=0 ; i < columsNames.length; i++){
            final int j = i;
            TableColumn col = new TableColumn(columsNames[i]);
            col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });

            tableview.getColumns().addAll(col);
        }


        /********************************
         * Data added to ObservableList *
         ********************************/
        ArrayList<Instance> instances = dataset.instances;
        for (Map.Entry<Instance,Attribute> entry : missing.entrySet()) {
            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();

            int index = instances.indexOf(entry.getKey())+1;

            row.add(""+index);                                              // 1 Instance Num
            row.add(entry.getValue().name());                               // 2 Attribut
            row.add(dataset.getAttVal(entry.getKey(),entry.getValue()));    // 3 new value

            data.add(row);
        }

        //FINALLY ADDED TO TableView
        tableview.setItems(data);
        return tableview;
    }



}