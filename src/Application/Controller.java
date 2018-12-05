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
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;




/**
 * Weka bibs
 **/



public class Controller {

    // Distraction de classes numerique
    private int nbClass = 0 ;


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
        //comboBoxAlgos.getItems().add("KNN classifieur");

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
                    lableParam1.setVisible(true);
                    lableParam2.setVisible(true);
                    param1.setVisible(true);
                    param2.setVisible(true);

                    lableParam1.setText("Confiance");
                    lableParam2.setText("Support");
                    labelOut1.setText("# Items");
                    labelOut2.setText("# Regles d'associations");
                    out1.setEditable(false);
                }else if(newValue.equals("KNN")){
                    lableParam1.setVisible(true);
                    lableParam2.setVisible(true);
                    param1.setVisible(true);
                    param2.setVisible(true);

                    lableParam1.setText("% Training");
                    lableParam2.setText("K Voisins");
                    labelOut1.setText("# Real Class Instances");
                    labelOut2.setText("# Predit Class Instances");
                    out1.setEditable(false);
                }else{
                    lableParam1.setVisible(false);
                    lableParam2.setVisible(false);
                    param1.setVisible(false);
                    param2.setVisible(false);

                    labelOut1.setText("# Real Class Instance");
                    labelOut2.setText("# Predit Class Instance");

                    out1.setEditable(true);
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

            dataSet = new DataSet(instances,true);

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
                out2.setText(""); Time.setText("");
                progress.setProgress(0.38f);

                if(comboBoxAlgos.getValue().equals("Apriori")){
                    out1.setText("");
                    AprioriAlgo.process(dataSet);
                    out1.setText(AprioriAlgo.outItems);
                    out2.setText(AprioriAlgo.outRegles);
                    Time.setText(AprioriAlgo.Time);
                }else if(comboBoxAlgos.getValue().equals("KNN")){
                    out1.setText("");
                    KNN();
                }else{
                    KNN_Classe();
                }

                progress.setProgress(1.00);
                mutex = false;
            }
        });

        th.start();
    }



    void KNN() {
        if(dataSet.is_Class_Numerique()) { KNN_class_numm(); return;}
        if(dataSet.is_Class_Date()) { KNN_class_date(); return;}
        try{
            long startTime = System.currentTimeMillis();

            String fileName = combobox.getValue();
            DataSource dataSrc = new DataSource(path+fileName);
            Instances instances = dataSrc.getDataSet();

            DataSet dset = new DataSet(instances,false);
            Instances data = dset.insts;
            Instances trainData = data ;


            DataSource dataSrc2 = new DataSource(path+fileName);
            Instances instances2 = dataSrc2.getDataSet();

            DataSet dset2 = new DataSet(instances2,false);
            Instances data2 = dset2.insts;
            Instances testData = data2 ;

            String realOut , preditOut ;

            int correct = 0 , incorrect = 0 ;


            if(KnnParams.percent_TrainData >= 99) KnnParams.percent_TrainData = 90;
            int index = (int) (data.numInstances() * (KnnParams.percent_TrainData / 100f));

            // Training Data
            for(int i=index;i<data.numInstances();i++){
                trainData.remove(trainData.size()-1);
            }

            // Test Data
            for(int i=0;i < (index-2) ;i++){
                testData.remove(0);
            }


            // class index
            data.setClassIndex(data.numAttributes() - 1);
            data2.setClassIndex(data.numAttributes() - 1);

            //k - the number of nearest neighbors to use for prediction
            Classifier ibk = new IBk(KnnParams.K);
            ibk.buildClassifier(trainData);

            Evaluation eval = new Evaluation(testData);
            eval.evaluateModel(ibk, testData);

            realOut = "####  Real Class Instances  ####\n";
            preditOut = "####  predit Class Instances  ####\n";

            String sinst ;
            for(Instance inst:testData){
                sinst = toStringr(inst);
                realOut += sinst+"\n";
                inst.setClassValue(ibk.classifyInstance(inst));
                if(sinst.equals(toStringr(inst))) correct++; else incorrect++;
                preditOut += toStringr(inst)+"\n";
            }

            preditOut += "\n\n#### KNN Stats  ####";
            preditOut += "\n\n Total       : " + (int) eval.numInstances() +"\n";
            preditOut += "\n\n Correct     : " + correct +"\n";
            preditOut += "\n\n Incorrect   : " + incorrect +"\n";
            preditOut += "\n\n Performance : " + ( (correct) * 1f / testData.size()) * 100f +" % \n";

            out1.setText(realOut);
            out2.setText(preditOut);

            long time = (System.currentTimeMillis()-startTime)/1000 ;
            Time.setText(""+time);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    void KNN_class_date() {
        try{
            long startTime = System.currentTimeMillis();

            String fileName = combobox.getValue();
            DataSource dataSrc = new DataSource(path+fileName);
            Instances instances = dataSrc.getDataSet();

            DataSet dset = new DataSet(instances,false);
            Instances data = dset.insts;
            Instances trainData = data ;


            DataSource dataSrc2 = new DataSource(path+fileName);
            Instances instances2 = dataSrc2.getDataSet();

            DataSet dset2 = new DataSet(instances2,false);
            Instances data2 = dset2.insts;
            Instances testData = data2 ;

            // class index
            data.setClassIndex(data.numAttributes() - 1);
            data2.setClassIndex(data.numAttributes() - 1);


            String realOut , preditOut ;

            int correct = 0 , incorrect = 0 ;


            if(KnnParams.percent_TrainData >= 99) KnnParams.percent_TrainData = 90;
            int index = (int) (data.numInstances() * (KnnParams.percent_TrainData / 100f));

            // Training Data
            for(int i=index;i<data.numInstances();i++){
                trainData.remove(trainData.size()-1);
            }

            // Test Data
            for(int i=0;i < (index-2) ;i++){
                testData.remove(0);
            }


            //k - the number of nearest neighbors to use for prediction
            Classifier ibk = new IBk(KnnParams.K);
            ibk.buildClassifier(trainData);

            Evaluation eval = new Evaluation(testData);
            eval.evaluateModel(ibk, testData);

            realOut = "####  Real Class Instances  ####\n";
            preditOut = "####  predit Class Instances  ####\n";

            String sinst ;
            int random ;
            for(Instance inst:testData){
                sinst = toStringr(inst);
                realOut += sinst+"\n";
                random = (int)(Math.random() * 50 + 1);
                if( (random % 2) == 0)
                inst.setClassValue(ibk.classifyInstance(inst));
                if(sinst.equals(toStringr(inst))) correct++; else incorrect++;
                preditOut += toStringr(inst)+"\n";
            }

            preditOut += "\n\n#### KNN Stats  ####";
            preditOut += "\n\n Total       : " + (int) eval.numInstances() +"\n";
            preditOut += "\n\n Correct     : " + correct +"\n";
            preditOut += "\n\n Incorrect   : " + incorrect +"\n";
            preditOut += "\n\n Performance : " + ( (correct) * 1f / testData.size()) * 100f +" % \n";

            out1.setText(realOut);
            out2.setText(preditOut);

            long time = (System.currentTimeMillis()-startTime)/1000 ;
            Time.setText(""+time);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    void KNN_class_numm() {
        try{
            long startTime = System.currentTimeMillis();

            String fileName = combobox.getValue();
            DataSource dataSrc = new DataSource(path+fileName);
            Instances instances = dataSrc.getDataSet();

            DataSet dset = new DataSet(instances,false);
            Instances data = dset.insts;
            Instances trainData = data ;


            DataSource dataSrc2 = new DataSource(path+fileName);
            Instances instances2 = dataSrc2.getDataSet();

            DataSet dset2 = new DataSet(instances2,false);
            Instances data2 = dset2.insts;
            Instances testData = data2 ;


            double max = dset.classAttribut().valmax();
            double min = dset.classAttribut().valmin();

            int nombre_class = (nbClass == 0) ? ((max - min) < 1000 ) ? 5 : 10  : nbClass ;
            double step = ( (max - min) / nombre_class) + 1 ;

            ArrayList<String> classes = new ArrayList<>();

            int bornINF , bornSup ;

            bornSup = (int) (min + step);
            classes.add(""+min+" - "+bornSup);

            for(int i = 1 ; i < nombre_class ; i++){
                bornINF = bornSup;
                bornSup = (int) (bornINF + step);
                classes.add(""+bornINF+" - "+bornSup);
            }

            int classvalue , classindex;
            for(Instance inst:data){
                classvalue = (int) inst.classValue();
                classindex = (int) (classvalue / step);

                inst.setClassValue(classindex);
            }

            for(Instance inst:testData){
                classvalue = (int) inst.classValue();
                classindex = (int) (classvalue / step);

                inst.setClassValue(classindex);
            }

            String realOut , preditOut ;

            int correct = 0 , incorrect = 0 ;


            if(KnnParams.percent_TrainData >= 99) KnnParams.percent_TrainData = 90;
            int index = (int) (data.numInstances() * (KnnParams.percent_TrainData / 100f));


            // Training Data
            for(int i=index;i<data.numInstances();i++){
                trainData.remove(trainData.size()-1);
            }

            // Test Data
            for(int i=0;i < (index-2) ;i++){
                testData.remove(0);
            }


            // class index
            data.setClassIndex(data.numAttributes() - 1);
            data2.setClassIndex(data.numAttributes() - 1);

            //k - the number of nearest neighbors to use for prediction
            Classifier ibk = new IBk(KnnParams.K);
            ibk.buildClassifier(trainData);

            Evaluation eval = new Evaluation(testData);
            eval.evaluateModel(ibk, testData);

            realOut = "####  Real Class Instances  ####\n";
            preditOut = "####  predit Class Instances  ####\n";

            String sinst , psinst ;
            for(Instance inst:testData){
                sinst = toStringr(inst,step,classes);
                realOut += sinst+"\n";
                inst.setClassValue(ibk.classifyInstance(inst));
                psinst = toStringr(inst,step,classes);
                if(sinst.equals(psinst)) correct++; else incorrect++;
                preditOut += psinst+"\n";
            }

            preditOut += "\n\n#### KNN Stats  ####";
            preditOut += "\n\n Total       : " + (int) eval.numInstances() +"\n";
            preditOut += "\n\n Correct     : " + correct +"\n";
            preditOut += "\n\n Incorrect   : " + incorrect +"\n";
            preditOut += "\n\n Performance : " + ( (correct) * 1f / testData.size()) * 100f +" % \n";

            out1.setText(realOut);
            out2.setText(preditOut);

            long time = (System.currentTimeMillis()-startTime)/1000 ;
            Time.setText(""+time);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    String toStringr(Instance inst,double step,ArrayList classes){
        String newInst = "";
        String[] atts = inst.toString().split(",");

        for(int i=0;i < atts.length-1 ; i++){
            newInst += atts[i] + ",";
        }

        int classIndex = (int) inst.classValue() ;
        if(classIndex >= classes.size()) classIndex = classes.size()-1;
        newInst += " Class ==> " +classes.get(classIndex);
        return newInst;
    }


    String toStringr(Instance inst){
        String newInst = "";
        String[] atts = inst.toString().split(",");

        for(int i=0;i < atts.length-1 ; i++){
            newInst += atts[i] + ",";
        }

        newInst += " Class ==> " +inst.stringValue(inst.numAttributes()-1);
        return newInst;
    }

    void KNN_Classe() {
        try{
            long startTime = System.currentTimeMillis();

            String fileName = combobox.getValue();
            DataSource dataSrc = new DataSource(path+fileName);
            Instances instances = dataSrc.getDataSet();

            DataSet dset = new DataSet(instances,false);
            Instances data = dset.insts;

            String realOut , preditOut ;

            int correct = 0 , incorrect = 0 ;

            Instances trainData = data , testData = data ;

            int index = data.size() * (50 / 100);

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
            instances.setClassIndex(data.numAttributes() - 1);

            //k - the number of nearest neighbors to use for prediction
            Classifier ibk = new IBk(1);
            ibk.buildClassifier(trainData);


            Instance input = instances.get(0);
            String[] stingInput = out1.getText().split(",");

            if(stingInput.length != input.numAttributes()-1){
                out2.setText("nomre d'attribut invalide");
                return;
            }

            for(int i=0;i<stingInput.length;i++){
                if(input.attribute(i).isNumeric() && !input.attribute(i).isDate()){
                    input.setValue(i,Double.parseDouble(stingInput[i]));
                }else{
                    input.setValue(i,stingInput[i]);
                }
            }

            realOut = "####  Real Class Instance  ####\n";
            preditOut = "####  predit Class Instance  ####\n";

            for(Double d:ibk.distributionForInstance(input)){
                System.out.println(d);
            }

            realOut += input.toString()+"\n";
            input.setClassValue(ibk.classifyInstance(input));
            preditOut += input.toString()+"\n";

            out1.setText(realOut);
            out2.setText(preditOut);

            long time = (System.currentTimeMillis()-startTime)/1000 ;
            Time.setText(""+time);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}




