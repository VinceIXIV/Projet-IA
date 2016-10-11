package winRight;

/*
 * Classe d'affichage du split droite de la fenetre (visionnage des caméras)
 */

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.Random;
import application.WindowPresenter;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import winLeft.WinLeftPresenter;



public class WinRightPresenter extends GridPane{

	//Déclaration

	private int index_cam=0;
	private int index_row=0;
	private int index_column=0;
	public ArrayList<LayoutCamera> gridLayout;
	private static WinRightPresenter instance=null;
	private Boolean randomView=false;
	private Label postScriptum;

	//constructeur

	private WinRightPresenter(){
		gridLayout= new ArrayList();
		this.setAlignment(Pos.CENTER);
		this.setHgap(5);
		this.setVgap(5);
		this.getStyleClass().add("rightView");
		this.getStylesheets().add("/application/application.css");
		postScriptum=new Label("Sélectionnez une caméra dans la liste..");
		this.add(postScriptum, 0, 0);

	}

	//Classe singleton

	public static WinRightPresenter getInstance(){

		if(instance == null){
	         instance = new WinRightPresenter();
	    }
		return instance;
	}

	/*
	 * Affiche une nouvelle caméra
	 */

	public void addElements(String id){
			System.out.println("add:"+id);
			LayoutCamera cam=new LayoutCamera(id,index_cam,240);
			cam.setIndex_column(index_column);
			cam.setIndex_row(index_row);
			gridLayout.add(cam);
			this.add(gridLayout.get(gridLayout.size()-1),index_column ,index_row );

			System.out.println("finish add:"+id+"-indCol:"+index_column+"-indRow:"+index_row);

			index_cam++;
			index_column++;
			if (index_column ==3 || index_column ==6){
				index_row++;
				index_column=0;
			}

	}

	/*
	 * Supprime une caméra
	 */

	public void removeElements(String id){
			System.out.println("remove:"+id);
			for(int i=0; i<gridLayout.size();i++){


				if(id.equals(gridLayout.get(i).getName())){

					int index_column_tmp=0;
					int index_row_tmp=0;

					this.getChildren().clear();
					gridLayout.get(i).interruptThread();
					gridLayout.remove(i);
					for(int j=0;j<gridLayout.size();j++){

						gridLayout.get(j).reloadView();

						gridLayout.get(j).setIndex_list(j);


						this.add(gridLayout.get(j),index_column_tmp ,index_row_tmp );

						index_column_tmp++;

						if (index_column_tmp ==3 || index_column_tmp ==6){
							index_row_tmp++;
							index_column_tmp=0;
						}

					}

					index_column=index_column_tmp;
					index_row=index_row_tmp;

					break;

				}
			}
		System.out.println("finish remove:"+id);
	}

	/*
	 * Passage en mode aléatoire
	 */

	public void randomView(Boolean randViewBis, ArrayList<String> camList){
		if(randViewBis==true){
			randomView=true;
		}else{
			randomView=false;
		}
		System.out.println("rand:"+randomView);
		index_cam=0;
		clearContent();

		if(randomView==true){
			for(int i=0; i<camList.size(); i++){
				LayoutCamera cam=new LayoutCamera(camList.get(i),i,730);
				cam.setIndex_column(index_column);
				cam.setIndex_row(index_row);

				gridLayout.add(cam);
				this.add(gridLayout.get(gridLayout.size()-1),0 ,0);
				this.getChildren().get(i).setVisible(false);
			}

			Service<Void> viewRandom = new Service<Void>(){
				  @Override
				  protected Task<Void> createTask() {
				    return new Task<Void>(){

				     @Override
				     protected Void call() throws Exception {
				    	 int randTempo=0;
				    	 int firstCycle=0;
							while(randomView=true){
								Random rand=new Random();
						   	 	int randNumber;
						   	 	randNumber=rand.nextInt(gridLayout.size()-1);

								if(firstCycle==0){
									firstCycle=1;
									WinRightPresenter.getInstance().getChildren().get(randNumber).setVisible(true);
									randTempo=randNumber;
								}
								else{
									WinRightPresenter.getInstance().getChildren().get(randTempo).setVisible(false);
									WinRightPresenter.getInstance().getChildren().get(randNumber).setVisible(true);
									randTempo=randNumber;
								}
								try {
									Thread.sleep(5000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						return null;
				      }
				    };
				  }
				};
				viewRandom.start();
		}else{
			//modeRandom.setRandomView();
		}

	}

	public ArrayList getCamChecked(){
		ArrayList<String> ListTempo= new ArrayList<String>();
		for(int i=0; i<gridLayout.size();i++){
			ListTempo.add(gridLayout.get(i).getName());
		}
		return ListTempo;
	}

	/*
	 * Arrete les thread de toutes les caméras
	 */

	public void interruptThreadGridLayout(){

		for(int i=0; i<gridLayout.size();i++){
			gridLayout.get(i).interruptThread();
		}
	}

	public void clearContent(){
		this.getChildren().clear();
		interruptThreadGridLayout();
		gridLayout.clear();
	}
}

