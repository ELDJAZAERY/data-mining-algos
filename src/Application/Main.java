package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/Application/Interface.fxml"));

        primaryStage.setTitle("DM weka");
        primaryStage.setScene(new Scene(root));
        primaryStage.setFullScreen(true);
        primaryStage.show();
   }


    public static void main(String[] args) {
        launch(args);


        //Apriori_ALGO.process("data/Algos.Apriori/car_data.txt");
        //Apriori_ALGO.process("data/Algos.Apriori/BL.dat.arff");
    }

}
