package gui;



import domain.GameController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class MainPanel extends GridPane {
	private GameController dc;
	Button button;
	TextField textField;
	public MainPanel(PanelController panelController, GameController dc) {
		this.dc = dc;
		
		button = new Button("Play");
		textField = new TextField();
		Label label = new Label("FEN string:");
		this.setAlignment(Pos.CENTER);
		this.addRow(0, label);
		this.addRow(1, textField);
		this.addRow(1, button);
		
		textField.setPrefWidth(350);
		textField.setAlignment(Pos.CENTER);
		textField.setText("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
		
		button.setOnAction(e -> {
			panelController.gamePanel(textField.getText(), dc);
		});
	}

}
