package application;

/*
 *Classe principale: initialisation de la fenetre
 */

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import winLeft.WinLeftPresenter;
import winRight.WinRightPresenter;

public class WindowPresenter implements Initializable{

	//D�claration

	@FXML
	StackPane wleft;
	@FXML
	GridPane wright;
	@FXML
	Button addCamera;
	@FXML
	Button randomView;
	@FXML
	Button help;
	@FXML
    public Pane wtop;
	@FXML
	SplitPane splitPane;

	public int last_numCam;
	public int last_numCamBis;
	public ArrayList cameraCheck;
	private Boolean random = false;


	//Constructeur
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		System.out.println("Initialisation fenetre");

		//renommage des images pr�sentes dans le fichier images
		renameAllFiles("src/images");
		splitPane.getStyleClass().add("splitPane");

		//Thread pour temporiser le renommage
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//appel des diff�rents cot�
		try {
			WinLeftPresenter leftView= new WinLeftPresenter();
			wright.getChildren().add(WinRightPresenter.getInstance());
			wleft.getChildren().add(leftView);

			initButton();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	@PostConstruct
	public void init(){
		System.out.println("fenetre initialisee");
	}

	//fonction relative � l'ajout d'une nouvelle cam�ra
	private static void configureFileChooser(final FileChooser fileChooser){
    	fileChooser.setTitle("Save Image");
    	fileChooser.setInitialDirectory(
        	new File("src/images")
    	);
	}

	//fonction de renommage des images dans le r�pertoire images
	public void renameAllFiles(String url){
		int numCam=0;
		int numCamBis=1;

		File file=new File(url);
        File[] files= file.listFiles();
        ObservableList<String> names ;
        if(files.length !=0){
            for (int i=0;i<files.length;i++){
            	if(numCam<10){
            		files[i].renameTo(new File("src/images/cam0"+numCam+"-"+numCamBis+".jpg"));
            	}else{
            		files[i].renameTo(new File("src/images/cam"+numCam+"-"+numCamBis+".jpg"));
            	}


            	numCamBis++;
            	if(numCamBis == 4){
            		numCamBis=1;
            		numCam++;
            	}
        	}
            last_numCam=numCam;
            last_numCamBis=numCamBis;
        }
    }

	public static boolean pregMatch(String pattern, String content) {
	    return content.matches(pattern);
	}

	//fonction d'initialisation des boutons du haut de la fen�tre: ajout, al�atoire/choix, aide
	public void initButton(){

		//bouton ajout de cam�ra
		addCamera.setOnAction(new EventHandler<ActionEvent>(){
         	@Override
        	public void handle(ActionEvent arg0) {

            	FileChooser fileChooser = new FileChooser();
            	configureFileChooser(fileChooser);
            	fileChooser.setTitle("Save Image");
            	fileChooser.getExtensionFilters().addAll(
            		 new FileChooser.ExtensionFilter("JPG", "*.jpg"),
           			 new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
           			 new FileChooser.ExtensionFilter("PNG", "*.png")

           			 );

            	File file = fileChooser.showSaveDialog(addCamera.getScene().getWindow());
            	System.out.println(file);
            	if (file != null){
            		if(last_numCamBis==3){
            			last_numCam++;
            			last_numCamBis=1;
            		}
					 try{
						 InputStream is = new FileInputStream(file);
						 BufferedImage imageSave = ImageIO.read(is);
						 ImageIO.write(imageSave, "jpg", new File ("bin/images/cam"+last_numCam+"-"+last_numCamBis+".jpg"));
						 ImageIO.write(imageSave, "jpg", new File ("src/images/cam"+last_numCam+"-"+last_numCamBis+".jpg"));
						 WinLeftPresenter leftView= new WinLeftPresenter();
						 wleft.getChildren().clear();
						 wleft.getChildren().add(leftView);
						 leftView.reinitBeforeAdd();
					 }catch (IOException ex){
						 System.out.println(ex.getMessage());
					 }
				    Alert dialogE = new Alert(AlertType.WARNING);
					dialogE.setTitle("Warning");
					dialogE.setHeaderText("WARNING!");
					dialogE.setContentText("Faite un refresh du r�pertoire 'images'");
					dialogE.showAndWait();
            	}
         	}

		});
		addCamera.getStyleClass().add("buttonAdd");

		//bouton al�atoire
		randomView.setOnAction(new EventHandler<ActionEvent>(){
         	@Override
        	public void handle(ActionEvent arg0) {
         		random=!random;
         		System.out.println(random);
         		if(random==true){

         			randomView.getStyleClass().add("buttonRand_selected");
	         		wleft.getChildren().clear();
	         		WinLeftPresenter leftView= new WinLeftPresenter();
	         		wleft.getChildren().add(leftView);
	         		leftView.setRandom();

         		}else{
         			randomView.getStyleClass().remove("buttonRand_selected");
         			wleft.getChildren().clear();
	         		WinLeftPresenter leftView= new WinLeftPresenter();
	         		wleft.getChildren().add(leftView);
         			WinRightPresenter.getInstance().randomView(false,null);
         		}
         	}
     	});
		randomView.getStyleClass().add("buttonRand");

		//bouton d'ade
		help.setOnAction(new EventHandler<ActionEvent>(){
         	@Override
        	public void handle(ActionEvent arg0) {
         		Alert dialogW = new Alert(AlertType.INFORMATION);
         		dialogW.setTitle("Aide");
         		dialogW.setHeaderText(null); // No header

         		dialogW.setGraphic(new ImageView(new Image("/application/icone/aide-2.gif")));
         		dialogW.setContentText("-Pour visionner une cam�ra, il vous suffit de cocher dans la liste la cam�ra en question, ou appuier sur le bouton al�atoire en haut � droite de la fen�tre. \n\n"
         				+ "-Vous ne pouvez visionner que 9 cam�ras � la fois.\n\n"
         				+ "-Cliquer sur la cam�ra de votre choix vous permettra de pr�visualiser votre cam�ra.\n\n"
         				+ "-Le bouton 'plus' vous permet d'ajouter de nouvelles cam�ras.\n\n"
         				+ "-Apr�s avoir ajouter une nouvelle cam�ra, faite un refresh du package 'images' dans votr IDE, vous n'avez pas besoin de quitt� le programme."
         				+ "-Une cam�ra est constitu�e de 3 images.\n\n"
         				+ "-Si vous ne voyez pas l'image que vous avez rajout�, cela veut dire qu'elle est all� compl�ter une cam�ra n'ayant pas 3 images.\n\n"
         				+ "-Le bouton al�atoire vous permet de visionner de mani�re al�atoire une cam�ra parmi la liste, en changeant de mani�re r�guli�re.");

         		Stage stage = (Stage) dialogW.getDialogPane().getScene().getWindow();
	         	stage.getIcons().add(new Image("/application/icone/icone_aide_fenetre.gif"));
         		dialogW.showAndWait();
         	}
     	});
		help.getStyleClass().add("buttonHelp");
	}



}
