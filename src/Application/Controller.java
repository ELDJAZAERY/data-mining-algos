package Application;


import Apriori.AprioriAlgo;
import DMweKa.Application.BoxPlot;
import DMweKa.Application.DynamicTableFx;
import DMweKa.Application.TableFx;
import DMweKa.AttributDataSet;
import DMweKa.DataSet;
import DMweKa.PreProcessing;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;


/**
 * Weka bibs
 **/



public class Controller {

    private final String path = "data/";
    private DataSet dataSet;

    private ObservableList<TableFx> attributslist = FXCollections.observableArrayList();


    /** File Chooser  - Data Set Properties */
    @FXML
    private ComboBox<String> combobox;

    @FXML
    private TableView<TableFx> fileContentTable;

    @FXML
    private TableView<TableFx> missingValuesTable;
    @FXML
    private Tab missingvalueTab;
    @FXML
    private Label missingValueRelation;

    @FXML
    private TextField relation;

    @FXML
    private TextField attributes;

    @FXML
    private TextField nbinstances;


    /** Attribut's Names and Types !! Table !! **/
    @FXML
    private TableView<TableFx> attributsTable;

    @FXML
    private TableColumn<TableFx,Integer> num;

    @FXML
    private TableColumn<TableFx,String> name;

    @FXML
    private TableColumn<TableFx,String> type;


    /** Name , Type , Distainct , mode , Num , Lable , weight ---> for each Attribut **/
    /** and Max Min Mean Q1 Q3 midRange sum  ---> for each Numeric Attribut **/
    @FXML
    private TableView<TableFx> selectedAttributsTable;

    @FXML
    private TextField Nom;

    @FXML
    private TextField Type;

    @FXML
    private TextField distincts;

    @FXML
    private TextField mode;

    @FXML
    private TableColumn<TableFx,Integer> nume;

    @FXML
    private TableColumn<TableFx,String> label;

    @FXML
    private TableColumn<TableFx,String> weight;

    /** Max Min Mean Q1 Q3 midRange sum  ---> for each Numeric Attribut **/

    @FXML
    private TableView<TableFx> numericAttributsTable;

    @FXML
    private TableColumn<TableFx,String> max;

    @FXML
    private TableColumn<TableFx,String> min;

    @FXML
    private TableColumn<TableFx,String> mean;

    @FXML
    private TableColumn<TableFx,Integer> Q1;

    @FXML
    private TableColumn<TableFx,String> Q3;

    @FXML
    private TableColumn<TableFx,String> midRange;

    @FXML
    private TableColumn<TableFx,String> sum;


    // Histogramme Barchart
    @FXML
    private BarChart barchart;


    // BoxPlot
    @FXML
    private BorderPane pane;
    private SwingNode swingNode = new SwingNode();


    /**
        ALGOS
        Apriori
     *
     * */

    @FXML
    private ComboBox<String> combobox2;

    @FXML
    private TextField confiance ;

    @FXML
    private TextField support ;

    @FXML
    private TextArea outInstance;

    @FXML
    private TextArea outItems;

    @FXML
    private TextArea outRegles;

    @FXML
    private TextField Time;


    @FXML
    public void initialize() {

        // init the colums

        num.setCellValueFactory(new PropertyValueFactory<>("num"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        type.setCellValueFactory(new PropertyValueFactory<>("Type"));

        nume.setCellValueFactory(new PropertyValueFactory<>("num"));
        label.setCellValueFactory(new PropertyValueFactory<>("label"));
        weight.setCellValueFactory(new PropertyValueFactory<>("weight"));

        /** Max Min Mean Q1 Q3 midRange sum  ---> for each Numeric Attribut **/
        numericAttributsTable.setVisible(false);
        max.setCellValueFactory(new PropertyValueFactory<>("max"));
        min.setCellValueFactory(new PropertyValueFactory<>("min"));
        mean.setCellValueFactory(new PropertyValueFactory<>("mean"));
        Q1.setCellValueFactory(new PropertyValueFactory<>("Q1"));
        Q3.setCellValueFactory(new PropertyValueFactory<>("Q3"));
        midRange.setCellValueFactory(new PropertyValueFactory<>("midRange"));
        sum.setCellValueFactory(new PropertyValueFactory<>("sum"));

        File repo = new File(path);
        if (repo.isDirectory()) {
            File[] fileList = repo.listFiles();
            for (File f : fileList) {
                combobox.getItems().add(f.getName());
            }
            if(fileList.length > 0) { combobox.setValue(fileList[0].getName()); afficheInstance();}
        }

        pane.setCenter(swingNode);



        /***  ALGOS */

        repo = new File(path+"Apriori");
        if (repo.isDirectory()) {
            File[] fileList = repo.listFiles();
            for (File f : fileList) {
                combobox2.getItems().add(f.getName());
            }
            if(fileList.length > 0) { combobox2.setValue(fileList[0].getName());}
        }

        // force int input from confiance and support
        confiance.textProperty().addListener(new ChangeListener<String>() {
             @Override
             public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                 if (!newValue.matches("\\d*")) {
                     confiance.setText(newValue.replaceAll("[^\\d]", ""));
                 }else {
                     if(!confiance.getText().isEmpty())
                        AprioriAlgo.NC = Integer.valueOf(confiance.getText());
                 }
             }
        });

        support.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    support.setText(newValue.replaceAll("[^\\d]", ""));
                } else {
                    if(!support.getText().isEmpty())
                        AprioriAlgo.SUPPORT = Integer.valueOf(support.getText());
                }
            }
        });

    }


    @FXML
    void afficheInstance() {
        clearForInstance();
        String fileName = combobox.getValue();
        DataSource dataSource;
        try {
            dataSource = new DataSource(path+fileName);
            Instances instances = dataSource.getDataSet();

            // affiche les attributs telqu'elles
            DynamicTableFx.instqncesToTableView(instances,missingValuesTable);

            dataSet = new DataSet(instances);

            afficheFileContent();
            if(PreProcessing.missing.size()>0) {
                missingvalueTab.setDisable(false);
                missingValueRelation.setText(dataSet.relation());
                //affichemissingValues();
            }
            else missingvalueTab.setDisable(true);


            /** Instance  Proprities */
            relation.appendText(dataSet.relation());
            nbinstances.appendText(Integer.toString(dataSet.nbInstances()));
            attributes.appendText(String.valueOf(dataSet.nbAttributs()));

            /** Show Attribut's Names and types in the Table Fx */
            showAttributsNames();

            new BoxPlot(fileName,instances,swingNode);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML //en cliquant sur un attribut
    private void showSelectedAttribut(){
        clearForAttribut();
        TableFx item = attributsTable.getSelectionModel().getSelectedItem();
        if(item == null) return;
        AttributDataSet attribut = dataSet.getAttribut(item.getName());

        Nom.appendText(attribut.name());
        Type.appendText(attribut.type());
        distincts.appendText(attribut.distinct());
        mode.appendText(attribut.mode());

        selectedAttributsTable.setItems(attribut.tableFxItems());
        selectedAttributsTable.sort();

        if(attribut.isNumeric()){
            numericAttributsTable.setVisible(true);
            numericAttributsTable.setItems(attribut.tableFxItemsNumiric());
        }else{
            numericAttributsTable.setVisible(false);
        }

        barchar(attribut);
    }


    /** Show Attribut's Names and Types in the Table Fx */
    public void showAttributsNames(){
        attributsTable.setItems(dataSet.listAttributsTableItems());
        attributsTable.sort();
    }


    public void clearForInstance(){
        relation.clear();nbinstances.clear();attributes.clear(); mode.clear();
    }


    public void clearForAttribut(){
        Nom.clear();Type.clear();distincts.clear();
        barchart.getData().clear();barchart.layout();
        mode.clear();
    }

    public void afficheFileContent(){
        try {
            fileContentTable = DynamicTableFx.dataSetToTableView(dataSet,fileContentTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void affichemissingValues(){
        try {
            missingValuesTable = DynamicTableFx.missingValueToTableView(PreProcessing.missing,dataSet,missingValuesTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void barchar(AttributDataSet attribut){
        TreeMap<Double,Integer> labelWeight = attribut.getLabelsAndWeightNum();
        TreeMap<String,Integer> labelWeightNom = attribut.getLabelsAndWeightNom();

        int maxDist = 15 , i = 0;
        if(attribut.isNumeric()){
            for(Map.Entry<Double,Integer> entry : labelWeight.entrySet()){
                XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
                series.getData().add(new XYChart.Data<String, Integer>(Double.toString(entry.getKey()), entry.getValue()));
                barchart.getData().add(series);

                i++;
                if(i >= maxDist ) break;
            }
        }else{
            for(Map.Entry<String,Integer> entry : labelWeightNom.entrySet()){
                XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
                series.getData().add(new XYChart.Data<String, Integer>(entry.getKey(), entry.getValue()));
                barchart.getData().add(series);

                i++;
                if(i >= maxDist ) break;
            }
        }

        barchart.setTitle(attribut.name().toUpperCase());
        barchart.autosize();

    }



    /*** ALGOS **/

    @FXML
    void process(){
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                String file = path+"Apriori/"+combobox2.getValue();
                AprioriAlgo.process(file);

                outItems.setText(""); outInstance.setText(""); outRegles.setText(""); Time.setText("");

                outInstance.setText(AprioriAlgo.outInstance);
                outItems.setText(AprioriAlgo.outItems);
                outRegles.setText(AprioriAlgo.outRegles);
                Time.setText(AprioriAlgo.Time);
            }
        });

        th.start();
    }


}




