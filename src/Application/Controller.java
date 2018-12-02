package Application;


import Algos.Apriori.AprioriAlgo;
import Algos.KNN.KnnParams;
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
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.Instance;
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
        Algos.Apriori
     *
     * */

    @FXML
    private ComboBox<String> comboBoxAlgos;


    @FXML
    private TextField param1 ;
    @FXML
    private Label lableParam1;
    @FXML
    private TextArea out1;
    @FXML
    private Label labelOut1;


    @FXML
    private TextField param2 ;
    @FXML
    private Label lableParam2;
    @FXML
    private TextArea out2;
    @FXML
    private Label labelOut2;


    @FXML
    private TextField Time;
    @FXML
    private ProgressIndicator progress;


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
            if(fileList.length >= 1) { combobox.setValue(fileList[1].getName()); afficheInstance();}
        }

        pane.setCenter(swingNode);



        /***  ALGOS ***/

        comboBoxAlgos.getItems().add("Apriori");
        comboBoxAlgos.getItems().add("KNN");

        comboBoxAlgos.setValue("Apriori");


        // force int input from confiance and support
        param1.textProperty().addListener(new ChangeListener<String>() {
             @Override
             public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                 if (!newValue.matches("\\d*")) {
                     param1.setText(newValue.replaceAll("[^\\d]", ""));
                 }else {
                     if(!param1.getText().isEmpty()){
                         AprioriAlgo.NC = Integer.valueOf(param1.getText());
                         KnnParams.percent_TrainData = Integer.valueOf(param1.getText());
                     }
                 }
             }
        });

        param2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    param2.setText(newValue.replaceAll("[^\\d]", ""));
                } else {
                    if(!param2.getText().isEmpty()){
                        AprioriAlgo.SUPPORT = Integer.valueOf(param2.getText());
                        KnnParams.K = Integer.valueOf(param2.getText());
                    }
                }
            }
        });

        comboBoxAlgos.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.equals("Apriori")){
                    lableParam1.setText("Confiance");
                    lableParam2.setText("Support");
                    labelOut1.setText("# Items");
                    labelOut2.setText("# Regles d'associations");
                }else{
                    lableParam1.setText("% Training");
                    lableParam2.setText("K Voisins");
                    labelOut1.setText("# Real Class Instances");
                    labelOut2.setText("# Predit Class Instances");
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
            if(PreProcessing.isMissing) {
                missingvalueTab.setDisable(false);
                missingValueRelation.setText(dataSet.relation());
                affichemissingValues();
            }
            else missingvalueTab.setDisable(true);


            /** Instance  Proprities */
            relation.appendText(dataSet.relation());
            nbinstances.appendText(Integer.toString(dataSet.nbInstances()));
            attributes.appendText(String.valueOf(dataSet.nbAttributs()));

            /** Show Attribut's Names and types in the Table Fx */
            showAttributsNames();

            new BoxPlot(fileName,dataSet.insts,swingNode);

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
            missingValuesTable = DynamicTableFx.instqncesToTableView(dataSet.prutInst,missingValuesTable);
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


    boolean mutex = false;
    @FXML
    void process(){
        if(mutex){ System.out.println("BUZZY !! "); return;}
        progress.setProgress(0f);

        mutex = true;
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                out1.setText(""); out2.setText(""); Time.setText("");
                progress.setProgress(0.38f);

                if(comboBoxAlgos.getValue().equals("Apriori")){
                    AprioriAlgo.process(dataSet);
                    out1.setText(AprioriAlgo.outItems);
                    out2.setText(AprioriAlgo.outRegles);
                    Time.setText(AprioriAlgo.Time);
                }else{
                    KNN();
                }

                progress.setProgress(1.00);
                mutex = false;
            }
        });

        th.start();
    }


    void KNN() {
        try{
            long startTime = System.currentTimeMillis();

            String fileName = combobox.getValue();
            DataSource dataSrc = new DataSource(path+fileName);
            Instances instances = dataSrc.getDataSet();

            DataSet dset = new DataSet(instances);
            Instances data = dset.insts;

            String realOut , preditOut ;

            int correct = 0 , incorrect = 0 ;

            Instances trainData = data , testData = data ;

            int index = data.size() * (KnnParams.percent_TrainData / 100);

            // Training Data
            for(int i=index;i<data.size();i++){
                trainData.remove(i);
            }

            // Test Data
            for(int i=0;i<index;i++){
                testData.remove(i);
            }


            // class index
            data.setClassIndex(data.numAttributes() - 1);

            //k - the number of nearest neighbors to use for prediction
            Classifier ibk = new IBk(KnnParams.K);
            ibk.buildClassifier(trainData);

            Evaluation eval = new Evaluation(testData);
            eval.evaluateModel(ibk, testData);

            realOut = "####  Real Class Instances  ####\n";
            preditOut = "####  predit Class Instances  ####\n";

            String sinst ;
            for(Instance inst:testData){
                sinst = inst.toString();
                realOut += sinst+"\n";
                inst.setClassValue(ibk.classifyInstance(inst));
                if(sinst.equals(inst.toString())) correct++; else incorrect++;
                preditOut += inst.toString()+"\n";
            }

            preditOut += "\n\n#### KNN Stats  ####";
            preditOut += "\n\n Total       : " + (int) eval.numInstances() +"\n";
            preditOut += "\n\n Correct     : " + correct +"\n";
            preditOut += "\n\n Incorrect   : " + incorrect +"\n";
            preditOut += "\n\n Erreur Rate : " + eval.errorRate() +"\n";

            out1.setText(realOut);
            out2.setText(preditOut);

            long time = (System.currentTimeMillis()-startTime)/1000 ;
            Time.setText(""+time);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}




