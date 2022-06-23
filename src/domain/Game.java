package domain;

public class Game {
	private Board board;
	private Player player1;
	private Player player2;
	
	private int turn;
	
	private boolean whitesTurn;
	
	public void startGame(String FEN, Player player1, Player player2) {
		makeBoard(FEN);
		this.turn = 0;
		this.player1 = player1;
		this.player2 = player2;
		setWhitesTurn(true);
	}
	
	public boolean isWhitesTurn() {
		return whitesTurn;
	}

	public void setWhitesTurn(boolean whitesTurn) {
		this.whitesTurn = whitesTurn;
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

	public int getTurn() {
		return turn;
	}

	public void incrementTurn() {
		this.turn++;
	}

}
