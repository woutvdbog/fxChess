package gui;

import domain.GameController;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PanelController {
	
	private Stage primaryStage;
	private GameController dc;
	public PanelController(Stage primaryStage, GameController dc) {
		this.primaryStage = primaryStage;
		this.dc = dc;
		
		primaryStage.getIcons().add(new Image(getClass().getResource("/images/q.png").toExternalForm()));
		
		try {
			start(primaryStage, dc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void start(Stage primaryStage, GameController dc) {
		MainPanel root = new MainPanel(this, dc);
		Scene scene = new Scene(root, 500, 200);
		primaryStage.setTitle("fxChess");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void gamePanel(String FEN, GameController dc) {
		GamePanel root = new GamePanel(this, dc,FEN);
		Scene scene = new Scene(root, 8*80, 8*80);
		primaryStage.setResizable(false);
		primaryStage.setTitle("fxChess");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
