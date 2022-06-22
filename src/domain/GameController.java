package domain;

public class GameController {

	private Board board;
	
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
