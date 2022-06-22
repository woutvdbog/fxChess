package domain;

public class Game {

	private Board board;
	private Player player1;
	private Player player2;
	
	public void startGame(String FEN, Player player1, Player player2) {
		makeBoard(FEN);
		this.player1 = player1;
		this.player2 = player2;
	}
	
	public void makeBoard(String FEN) {
		this.board = new Board(FEN);
	}
	
	public Board getBoard() {
		return board;
	}
	
	public void updateBoard(Piece[][] board) {
		this.board.updateBoard(board);
	}

}
