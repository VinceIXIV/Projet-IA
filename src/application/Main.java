package application;

/*
 *Initialisation de la scene
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.getIcons().add(new Image("application/icone/icone_appli.gif"));
			primaryStage.setTitle("ZOO-Security");
			primaryStage.setResizable(false);
			Parent root= FXMLLoader.load(getClass().getResource("Window.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
