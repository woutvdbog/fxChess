package gui;


import java.util.ArrayList;
import java.util.List;

import domain.Board;
import domain.Game;
import domain.Piece;
import domain.Player;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GamePanel extends Pane {

	private Game dc;
	
	private final int SIZE = 80;

	private Board board;
	
	Piece[][] boardDisplay;
	Rectangle[][] tiles;
	
	private List<Rectangle> highlighted;
	private List<Piece> capturesWhite;
	private List<Piece> capturesBlack;
	
	Label lblTurn;
	
	public GamePanel(PanelController panelController, Game dc,String FEN) {
		this.dc = dc;
	    BorderPane borderPane = new BorderPane();
		lblTurn = new Label();
		lblTurn.setTranslateY(648);
		lblTurn.setTranslateX(640/2 - 70);
	    lblTurn.setStyle("-fx-font: 24 Helvetica;");
		
		dc.startGame(FEN, new Player("Test1", true), new Player("Test2", true));
		
		updateLabels();
		board = dc.getBoard();
		boardDisplay = board.getPieces();
		
		capturesWhite = new ArrayList<>();
		capturesBlack = new ArrayList<>();
		highlighted = new ArrayList<Rectangle>();
		tiles = new Rectangle[8][8];
		
		
		for(int i = 0; i<  8; i++) {
			for(int j =0; j <8; j++) {
				if(boardDisplay[i][j] == null) System.out.print('0');
				else
				System.out.print(boardDisplay[i][j].getType().getChar());
			}
			System.out.println();
		}
		
		GridPane boardGrid = new GridPane();

		int counter = 0;
		for (int i = 0; i < SIZE/10; i++) {
			for (int j = 0; j < SIZE/10; j++) {
				Rectangle r = new Rectangle(j* SIZE, i * SIZE, SIZE, SIZE);
				tiles[i][j] = r;
				if(counter % 2 == 0) {
				r.setFill(Color.WHITE);
				r.setStroke(Color.BLACK);
				} else {
					r.setFill(Color.GRAY);
					r.setStroke(Color.BLACK);
				}
				this.getChildren().add(r);
				counter++;
			}
			counter++;
		}
		
		for(int i = 0; i < boardDisplay.length; i++) {
			for(int j = 0; j < boardDisplay[i].length; j++) {
				
				Piece piece = boardDisplay[i][j];
				
				if(piece != null) {
					ImageView p = piece.getImageView();
					p.setX(piece.getX() * 80);
					p.setY(piece.getY() * 80);
					p.setFitHeight(SIZE);
					p.setFitWidth(SIZE);
					
					p.setOnMousePressed(e -> {
						if(isTurn(piece) && e.getButton() == MouseButton.PRIMARY) clicked(e, piece);
					});
					
					p.setOnMouseDragged(e -> {
						if(isTurn(piece) && e.getButton() == MouseButton.PRIMARY) dragged(e, piece);
					});
					
					p.setOnMouseReleased(e -> {
						if(isTurn(piece) && e.getButton() == MouseButton.PRIMARY) released(e, piece);
					});
					
					this.getChildren().add(p);
				}
			}
		}
		this.getChildren().addAll(boardGrid, lblTurn);

	}
	
	private void updateLabels() {
		lblTurn.setText(String.format("TURN: %s", dc.isWhitesTurn() ? "WHITE" : "BLACK"));
	}
	
	public boolean isTurn(Piece piece) {
		return dc.isWhitesTurn() && piece.isWhite() || !dc.isWhitesTurn() && !piece.isWhite();
	}
	
	public void clicked(MouseEvent e, Piece piece) {
		clearHighlighted();
		checkMoves(piece);
			
		boardDisplay[(int) Math.floor(e.getY()/ SIZE)][(int) Math.floor(e.getX()/ SIZE)] = null;
			
		this.getChildren().addAll(highlighted);
	}

	public void dragged(MouseEvent e, Piece piece) {
		ImageView img = piece.getImageView();
		img.toFront();
		img.setX((e.getX() - SIZE/2));
		img.setY((e.getY() - SIZE/2));
		
	}
	
	public void released(MouseEvent e, Piece p) {

		highlighted.forEach(a -> this.getChildren().remove(a));
		
		int x = (int) ((e.getX() / SIZE));
		int y = (int) ((e.getY() / SIZE));

		if((x < 0 || x > 7) || (y < 0 || y > 7)) {
			resetPiece(p);
			return;
		};
		
		Piece target = boardDisplay[(int) Math.floor(y)][(int) Math.floor(x)];
		
		final int y1 = y;
		final int x1 = x;
		
		boolean validTile = highlighted.stream().anyMatch(t -> (t.getX() / 80) == x1 && (t.getY() / 80) == y1);
		if((target == null || (p.isWhite() && !target.isWhite()) || (!p.isWhite() && target.isWhite())) && validTile) {
				
				if(target != null && target.isWhite()) {
					capturesWhite.add(target);
					this.getChildren().remove(target.getImageView());
				} else if(target != null && !target.isWhite()) {
					capturesBlack.add(target);
					this.getChildren().remove(target.getImageView());
				}
				
			if(x > 7) x = 7;
			if(x < 0) x = 0;
			if(y > 7) y = 7;
			if(y < 0) y = 0;
			
			p.setX(x);
			p.setY(y);
			p.draw();
			
			boardDisplay[y][x] = p;
			
			clearHighlighted();
			
			if(p.isFirstMove()) p.setFirstMove(false);
			dc.setWhitesTurn(!dc.isWhitesTurn());
			updateLabels();
		} else {
			resetPiece(p);
		}
		
		dc.updateBoard(boardDisplay);
		for(int i = 0; i<  8; i++) {
			for(int j =0; j <8; j++) {
				if(boardDisplay[i][j] == null) System.out.print('0');
				else
				System.out.print(boardDisplay[i][j].getType().getChar());
			}
			System.out.println();
		}
	}
	
	private void resetPiece(Piece p) {
		p.draw();
		boardDisplay[(int) p.getY()][(int) Math.floor(p.getX())] = p;
	}
	
	private void clearHighlighted() {
		highlighted.forEach(a -> this.getChildren().remove(a));
		highlighted.clear();
	}
	
	
	private void checkMoves(Piece piece) {
		
		int x = (int) piece.getX();
		int y = (int) piece.getY();
		
		switch(piece.getType()) {
		case ROOK:
			checkStraightSliding(piece, x, y, false);
			break;
		case QUEEN:
			checkStraightSliding(piece, x, y, false);
			checkDiagonalSliding(piece, x, y, false);
		case BISHOP:
			checkDiagonalSliding(piece, x, y, false);
			break;
		case KING:
			checkDiagonalSliding(piece, x, y, true);
			checkStraightSliding(piece, x, y, true);
			break;
		case PAWN:
			pawnMove(piece, x, y);
			break;
		case KNIGHT: 
			knightMove(piece, x, y);
			break;
		}
		
		highlighted.forEach(h -> {
			h.setStroke(Color.BLACK);
			h.setFill(Color.rgb(255, 32, 32, 0.25));
		});
	}
	
	private void knightMove(Piece piece, int x, int y) {
		if((y > 0 && x > 1) && (boardDisplay[y-1][x-2] == null || boardDisplay[y-1][x-2].isWhite() != piece.isWhite())) {
			highlighted.add(new Rectangle(x*SIZE - (2*SIZE), y*SIZE - SIZE, SIZE,SIZE));
		}
		if((y > 1 && x > 0) && (boardDisplay[y-2][x-1] == null || boardDisplay[y-2][x-1].isWhite() != piece.isWhite())) {
			highlighted.add(new Rectangle(x*SIZE - SIZE, y*SIZE - (2*SIZE), SIZE,SIZE));
		}
		if((y > 0 && x < 6) && (boardDisplay[y-1][x+2] == null || boardDisplay[y-1][x+2].isWhite() != piece.isWhite())) {
			highlighted.add(new Rectangle(x*SIZE + (2*SIZE), y*SIZE - SIZE, SIZE,SIZE));
		}
		if((y > 1 && x < 7) && (boardDisplay[y-2][x+1] == null || boardDisplay[y-2][x+1].isWhite() != piece.isWhite())) {
			highlighted.add(new Rectangle(x*SIZE + SIZE, y*SIZE - (2*SIZE), SIZE,SIZE));
		}
		
		
		if((y < 7 && x > 1) && (boardDisplay[y+1][x-2] == null || boardDisplay[y+1][x-2].isWhite() != piece.isWhite())) {
			highlighted.add(new Rectangle(x*SIZE - (2*SIZE), y*SIZE + SIZE, SIZE,SIZE));
		}
		if((y < 6 && x > 0) && (boardDisplay[y+2][x-1] == null || boardDisplay[y+2][x-1].isWhite() != piece.isWhite())) {
			highlighted.add(new Rectangle(x*SIZE - SIZE, y*SIZE + (2*SIZE), SIZE,SIZE));
		}
		if((y < 7 && x < 6) && (boardDisplay[y+1][x+2] == null || boardDisplay[y+1][x+2].isWhite() != piece.isWhite())) {
			highlighted.add(new Rectangle(x*SIZE + (2*SIZE), y*SIZE + SIZE, SIZE,SIZE));
		}
		if((y < 6 && x < 7) && (boardDisplay[y+2][x+1] == null || boardDisplay[y+2][x+1].isWhite() != piece.isWhite())) {
			highlighted.add(new Rectangle(x*SIZE + SIZE, y*SIZE + (2*SIZE), SIZE,SIZE));
		}
	}
	
	private void pawnMove(Piece piece, int x, int y) {
		
		int distance = piece.isFirstMove() ? 2 : 1;
		
		if(piece.isWhite()) {
			boolean hasOpponent = false;
			if((y > 0 && x > 0) && boardDisplay[y - 1][x - 1] != null && !boardDisplay[y - 1][x - 1].isWhite()) {
				hasOpponent = true;
				highlighted.add(new Rectangle(x*SIZE - SIZE, y*SIZE - SIZE, SIZE,SIZE));
			}
			if((y > 0 && x < 7) && boardDisplay[y - 1][x + 1] != null && !boardDisplay[y - 1][x + 1].isWhite()) {
				hasOpponent = true;
				highlighted.add(new Rectangle(x*SIZE + SIZE, y*SIZE - SIZE, SIZE,SIZE));
			}
			if((!hasOpponent && y > (distance == 2 ? 1 : 0)) && boardDisplay[y - distance][x] == null) {
				highlighted.add(new Rectangle(x*SIZE, y*SIZE - SIZE, SIZE,SIZE));
				if(distance == 2) highlighted.add(new Rectangle(x*SIZE, y*SIZE - (2*SIZE), SIZE,SIZE));
			}
		} else {
			boolean hasOpponent = false;
			if((y < 7 && x < 7) && boardDisplay[y + 1][x + 1] != null && boardDisplay[y + 1][x + 1].isWhite()) {
				hasOpponent = true;
				highlighted.add(new Rectangle(x*SIZE + SIZE, y*SIZE + SIZE, SIZE,SIZE));
			}
			if((y < 7 && x > 0) && boardDisplay[y + 1][x - 1] != null && boardDisplay[y + 1][x - 1].isWhite()) {
				hasOpponent = true;
				highlighted.add(new Rectangle(x*SIZE - SIZE, y*SIZE + SIZE, SIZE,SIZE));
			}
			
			if((!hasOpponent && y < (distance == 2 ? 6 : 7)) && boardDisplay[y + distance][x] == null) {
				highlighted.add(new Rectangle(x*SIZE, y*SIZE + SIZE, SIZE,SIZE));
				if(distance == 2) highlighted.add(new Rectangle(x*SIZE, y*SIZE + (2*SIZE), SIZE,SIZE));
			}
		}
	}
	
	private void checkDiagonalSliding(Piece piece, int x, int y, boolean single) {	

		// DOWN RIGHT
		for(int i = y + 1, j = x + 1; i < 8 && j < 8; i++, j++) {
			if(boardDisplay[i][j] == null) {
				highlighted.add(new Rectangle(j*SIZE, i*SIZE, SIZE,SIZE));
				if(single) break;
			} else {
				if(boardDisplay[i][j].isWhite() != piece.isWhite()) highlighted.add(new Rectangle(j*SIZE, i*SIZE, SIZE,SIZE));
				break;
			}
		}
		
		// UP RIGHT
		for(int i = y - 1, j = x + 1; i >= 0 && j < 8; i--, j++) {
			if(boardDisplay[i][j] == null) {
				highlighted.add(new Rectangle(j*SIZE, i*SIZE, SIZE,SIZE));
				if(single) break;
			} else {
				if(boardDisplay[i][j].isWhite() != piece.isWhite()) highlighted.add(new Rectangle(j*SIZE, i*SIZE, SIZE,SIZE));
				break;
			}
		}
		
		// DOWN LEFT
		for(int i = y + 1, j = x - 1; i < 8 && j >= 0; i++, j--) {
			if(boardDisplay[i][j] == null) {
				highlighted.add(new Rectangle(j*SIZE, i*SIZE, SIZE,SIZE));
				if(single) break;
			} else {
				if(boardDisplay[i][j].isWhite() != piece.isWhite()) highlighted.add(new Rectangle(j*SIZE, i*SIZE, SIZE,SIZE));
				break;
			}
		}
		
		// UP LEFT
		for(int i = y - 1, j = x - 1; i >= 0 && j >= 0; i--, j--) {
			if(boardDisplay[i][j] == null) {
				highlighted.add(new Rectangle(j*SIZE, i*SIZE, SIZE,SIZE));
				if(single) break;
			} else {
				if(boardDisplay[i][j].isWhite() != piece.isWhite()) highlighted.add(new Rectangle(j*SIZE, i*SIZE, SIZE,SIZE));
				break;
			}
		}
	}
	
	private void checkStraightSliding(Piece piece, int x, int y, boolean single) {
		
		// SLIDING --------------------------------------------------------
		
		// VERTICAL SLIDING 
		
		for(int i = y + 1; i < 8; i++) {
			if(boardDisplay[i][x] == null) {
				highlighted.add(new Rectangle(x*SIZE, i*SIZE, SIZE,SIZE));
				if(single) break;
			} else {
				if(boardDisplay[i][x].isWhite() != piece.isWhite()) highlighted.add(new Rectangle(x*SIZE, i*SIZE, SIZE,SIZE));
				break;
			}
		}
		
		for(int i = y - 1; i >= 0; i--) {
			if(boardDisplay[i][x] == null) {
				highlighted.add(new Rectangle(x*SIZE, i*SIZE, SIZE,SIZE));
				if(single) break;
			} else {
				if(boardDisplay[i][x].isWhite() != piece.isWhite()) highlighted.add(new Rectangle(x*SIZE, i*SIZE, SIZE,SIZE));
				break;
			}
		}
		
		// HORIZONTAL SLIDING 
		
		for(int i = x + 1; i < 8; i++) {
			if(boardDisplay[y][i] == null) {
				highlighted.add(new Rectangle(i*SIZE, y*SIZE, SIZE,SIZE));
				if(single) break;
			} else {
				if(boardDisplay[y][i].isWhite() != piece.isWhite()) highlighted.add(new Rectangle(i*SIZE, y*SIZE, SIZE,SIZE));
				break;
			}
		}
		for(int i = x - 1; i >= 0; i--) {
			if(boardDisplay[y][i] == null) {
				highlighted.add(new Rectangle(i*SIZE, y*SIZE, SIZE,SIZE));
				if(single) break;
			} else {
				if(boardDisplay[y][i].isWhite() != piece.isWhite()) highlighted.add(new Rectangle(i*SIZE, y*SIZE, SIZE,SIZE));
				break;
			}
		}
	}
}
