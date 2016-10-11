package winRight;

/*
 * Classe d'initialisation d'une caméra
 */

import java.io.File;

import application.ImageViewPane;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class LayoutCamera extends Pane{

		//Déclaration

		private Image imageSource1;
		private Image imageSource2;
		private Image imageSource3;
		private ImageView imageSource_view1;
		private ImageView imageSource_view2;
		private ImageView imageSource_view3;
		private ImageViewPane imageSource_pane1;
		private ImageViewPane imageSource_pane2;
		private ImageViewPane imageSource_pane3;
		private Label camNameView;
		private Circle recCam;
		private Group namegrp;

		private int index_list;
		private int index_row;
		private int index_column;
		private String name;
		private Boolean interrupt = false;
		private Boolean visiblee = true;
		private int fileExist=0;
		private int size;

		//Constructeur

	public LayoutCamera(String url, int index, int size){
		name=url;
		index_list=index;
		this.size=size;
		namegrp=new Group();
		imageChanged();
		this.getStyleClass().add("camView");
		this.getStylesheets().add("/application/application.css");

		//Thread permettant le alternance des images qui constitue une caméra: une caméra est représentée par trois images différentes qui permuttent

		Service<Void> imageLoadingService = new Service<Void>(){

			  @Override
			  protected Task<Void> createTask() {
			    return new Task<Void>(){

			     @Override
			     protected Void call() throws Exception {
			    	 int i=1;
						while(!interrupt){
							if(i==1){
								imageSource_view3.setVisible(false);
								imageSource_view2.setVisible(false);
								imageSource_view1.setVisible(true);
								i++;
							}else if(fileExist>=2 && i==2){
								imageSource_view1.setVisible(false);
								imageSource_view3.setVisible(false);
								imageSource_view2.setVisible(true);
								i++;
							}else if (fileExist>=3 && i==3){
								imageSource_view1.setVisible(false);
								imageSource_view2.setVisible(false);
								imageSource_view3.setVisible(true);
								i=1;
							}

							try {
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

						}

					return null;

			      }
			    };
			  }
			};
		imageLoadingService.start();

		//Thread permettant l'effet visuel du cercle rouge sur les caméras

		Service<Void> recCamThread = new Service<Void>(){

			  @Override
			  protected Task<Void> createTask() {
			    return new Task<Void>(){

			     @Override
			     protected Void call() throws Exception {

						while(!interrupt){
							recCam.setVisible(visiblee);
							visiblee=!visiblee;
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

						}

					return null;

			      }
			    };
			  }
			};
		recCamThread.start();

	}

	//Getter et setter

	public int getIndex_list() {
		return index_list;
	}
	public void setIndex_list(int index_list) {
		this.index_list = index_list;
	}
	public String getName(){
		return "/images/"+name;
	}
	public int getIndex_row() {
		return index_row;
	}
	public void setIndex_row(int index_row) {
		this.index_row = index_row;
	}
	public int getIndex_column() {
		return index_column;
	}
	public void setIndex_column(int index_column) {
		this.index_column = index_column;
	}

	//Fonction permettant l'arrêt des threads de la caméra en question

	public void interruptThread(){
		interrupt= true;

	}

	//Fonction d'initialisation de l'affichage d'une caméra: init des trois images et autres visu d'une caméra (nom,..)

	public void imageChanged(){

		//nom + cercle rouge
		camNameView=new Label("REC-"+name);
		camNameView.getStyleClass().add("labelNameCam");
		camNameView.getStylesheets().add("/application/application.css");
		camNameView.setTextFill(Color.web("white"));
		camNameView.setLayoutX(14);
		recCam=new Circle(5,Color.RED);
		recCam.setLayoutX(7);
		recCam.setLayoutY(8);


		//image 1
		File f= new File("src"+"/images/"+name+"-"+1+".jpg");
		if(f.exists()){
			imageSource1=new Image("/images/"+name+"-"+1+".jpg");
			imageSource_view1= new ImageView(imageSource1);
			imageSource_view1.setFitWidth(size);
			imageSource_view1.setFitHeight(size);
			imageSource_pane1= new ImageViewPane(imageSource_view1);
			this.getChildren().add(imageSource_pane1);
			fileExist++;
		}

		//image 2
		f= new File("src"+"/images/"+name+"-"+2+".jpg");
		if(f.exists()){
			imageSource2=new Image("/images/"+name+"-"+2+".jpg");
			imageSource_view2= new ImageView(imageSource2);
			imageSource_view2.setFitWidth(size);
			imageSource_view2.setFitHeight(size);
			imageSource_pane2= new ImageViewPane(imageSource_view2);
			imageSource_view2.setVisible(false);
			this.getChildren().add(imageSource_pane2);

			fileExist++;
		}

		//image 3
		f= new File("src"+"/images/"+name+"-"+3+".jpg");
		if(f.exists()){
			imageSource3=new Image("/images/"+name+"-"+3+".jpg");
			imageSource_view3= new ImageView(imageSource3);
			imageSource_view3.setFitWidth(size);
			imageSource_view3.setFitHeight(size);
			imageSource_pane3= new ImageViewPane(imageSource_view3);
			imageSource_view3.setVisible(false);
			this.getChildren().add(imageSource_pane3);

			fileExist++;
		}
		this.getChildren().add(recCam);
		this.getChildren().add(camNameView);
	}


	//Fonction permettant de mettre à jour la place d'une caméra sur l'écra après désactivation d'une autre

	public void reloadView(){
		if (index_column ==0 && index_row!=0){
			index_row--;
			index_column=2;
		}else if(index_column !=0 ){
			index_column--;
		}else if(index_column ==0 && index_row==0){
			index_column =0;
			index_row =0;
		}
	}
}
