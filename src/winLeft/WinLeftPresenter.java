package winLeft;

/*
 *Classe relative au coté gauche de la fenètre: liste des caméras et prévisualisation
 */

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.sun.prism.paint.Stop;

import application.ImageViewPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import winRight.WinRightPresenter;


public class WinLeftPresenter extends StackPane{

	//Déclaration
	private static final Orientation HORIZONTAL= null;

	private ListView<String> cameraList;
	private ImageView clicked_image_view;
	private Image clicked_image;
	private StackPane split_bot;
	private SplitPane split;
	private ImageViewPane imagePane;
	private ArrayList cameraCheckbox;
	public int i=0;
	public int checkbox_count=0;
	private ArrayList<String> imagefichier;
	private Boolean randomView=false;
	private Label split_bot_preview;


	//Constructeur
	public WinLeftPresenter(){
		System.out.println("Initialisation Split gauche");

		split= new SplitPane();
		split_bot=new StackPane();

		split_bot.getStyleClass().add("stackLeftBot");
		split_bot.getStylesheets().add("/application/application.css");
		cameraList= new ListView<String>();
		cameraList= initList();
		cameraList.getStyleClass().add("leftView");

		this.getStylesheets().add("/application/application.css");
        split.getItems().addAll(cameraList,split_bot);
        split.setOrientation(HORIZONTAL);
    	split.setDividerPositions(0.7);

		this.getChildren().add(split);
	}




	//Méthode d'initialisation de la liste de caméra
	public ListView initList(){
		ListView listTempo= new ListView();
		listTempo.setEditable(true);


		//Importation depuis le fichier image de la liste des images, plus répartition trois par trois pour former une camér+ renommage(obligatoire)
		File file=new File("src/images");
        File[] files= file.listFiles();
        imagefichier=new ArrayList<String>();
        ObservableList<String> names ;
        if(files.length !=0){
            for (int i=0;i<files.length;i++){
            	String imageName= files[i].getName().replaceAll("^(.*?) *-.*", "$1");
            	if(imagefichier.size()!=0 && imageName.equals(imagefichier.get(imagefichier.size()-1))){

            	}else if(imagefichier.size()==0){
            		imagefichier.add(imageName);
            	}else {imagefichier.add(imageName);}


            }
            names= FXCollections.observableArrayList(imagefichier);
        }else{
            names = FXCollections.observableArrayList("");
        }

        listTempo.setItems(names);
        listTempo.setCellFactory(param -> new RadioListCell());
        listTempo.setOnMouseClicked(new EventHandler<MouseEvent>() {


        	//partie gauche-bas de la fenetre: prévisualisation
			@Override
			public void handle(MouseEvent arg0) {

				if (arg0.getClickCount() == 1) {
					split_bot_preview=new Label("Prévisualisation");
					split_bot_preview.setTextFill(Color.web("white"));
					clicked_image = new Image("/images/"+listTempo.getSelectionModel().getSelectedItem()+"-1.jpg");

					clicked_image_view=new ImageView(clicked_image);

					clicked_image_view.getStyleClass().add("my_image");
					clicked_image_view.setFitWidth(200);
					clicked_image_view.setFitHeight(200);
					imagePane= new ImageViewPane(clicked_image_view);
					split_bot.getChildren().add(imagePane);
					split_bot.getChildren().add(split_bot_preview);

	            }
			}
        });
		return listTempo;
	}

	//fonction d'initialisation du mode aléatoire
	public void setRandom(){
		randomView=true;
		WinRightPresenter.getInstance().randomView(randomView,imagefichier);
	}

	//Classe d'initialisation des checkBoxs
	private class RadioListCell extends ListCell<String> {
        @Override
        public void updateItem(String obj, boolean empty) {
            super.updateItem(obj, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                CheckBox radioButton = new CheckBox(obj);
                setGraphic(radioButton);
            	radioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {

					@Override
					public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {

						//si le checkbox est déjà coché, décochage et suppression de la caméra
						if(arg1 && randomView==false){
							WinRightPresenter.getInstance().removeElements("/images/"+obj);
							checkbox_count--;
							System.out.println("check nb"+checkbox_count);
						}

						//si le checkbox n'est pas coché:
						if(arg2){

							//il y a déjà à neuf caméras activées:
							if(checkbox_count>=9 || randomView==true){

								radioButton.setSelected(arg1);
								checkbox_count++;

								if(randomView!=true){
									Alert dialogE = new Alert(AlertType.WARNING);
									dialogE.setTitle("Limite de caméra");
									dialogE.setHeaderText("Vous visionnez actuellement 9 caméras");
									dialogE.setContentText("Aide : pour visionnez une autre caméra, déselectionnez en une");
									dialogE.showAndWait();
								}
								if(randomView==true)
								{
									checkbox_count=0;
								}
							//cochage et ajout d'une caméra
							}else{
								WinRightPresenter.getInstance().addElements(obj);
								checkbox_count++;
								System.out.println("check nb"+checkbox_count);
								System.out.println(radioButton.isSelected());
							}
						}
					}
            	});
            }
        }
	}

	//Cette fonction permet de checker les checkboxs des caméras déjà afficher après ajout d'une nouvelle caméras
	public void reinitBeforeAdd(){
		WinRightPresenter.getInstance().clearContent();
		ArrayList<String> ListCamTempo=WinRightPresenter.getInstance().getCamChecked();
		//cameraList.get
	}


}


