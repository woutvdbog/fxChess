package application;
	
import domain.GameController;
import gui.PanelController;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		PanelController panelController = new PanelController(primaryStage, new GameController());
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

